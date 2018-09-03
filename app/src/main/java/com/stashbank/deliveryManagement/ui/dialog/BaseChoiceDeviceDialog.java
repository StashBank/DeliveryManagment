package com.stashbank.deliveryManagement.ui.dialog;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.stashbank.deliveryManagement.R;
import com.stashbank.deliveryManagement.adapters.DeviceListAdapter;
import com.stashbank.deliveryManagement.ui.widget.ProgressWheel;


/**
 * Created by dn160671kav on 10.08.2016.
 */
public abstract class BaseChoiceDeviceDialog extends BaseDialogFragment {

    public static final String TAG = BaseChoiceDeviceDialog.class.getSimpleName();

    @BindView(R.id.listView)  ListView mListView;
    @BindView(R.id.tvMessage) TextView mMessage;
    @BindView(R.id.imgDevice) ImageView mImageDevice;
    @BindView(R.id.tvCaption) TextView mCaption;
    @BindView(R.id.progressBar) ProgressWheel mProgressBar;
    @BindView(R.id.viewConnectContext) View viewConnectContext;
    @BindView(R.id.viewDeviceContext) View viewDeviceContext;

    private DeviceListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter mBluetoothAdapter;
    private ProgressDialog mProgressDlg;
    private BluetoothDevice mSavedDevice;
    private final BluetoothReceiver mBluetoothReceiver = new BluetoothReceiver();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public BluetoothAdapter getBluetoothAdapter () {
        return  mBluetoothAdapter;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DeviceDialogStyle); //стиль

        Bundle arguments = getArguments();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mProgressDlg = new ProgressDialog(getActivity());

        mProgressDlg.setMessage(getString(R.string.scanning_device));
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel_dlg), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mBluetoothAdapter.cancelDiscovery();
            }
        });
        //

        mDeviceList.addAll(getPairedDevice()); //добавляем все подключенные
        //удаляем сохраненное устройство
        if (mSavedDevice != null)
            mDeviceList.remove(mSavedDevice);

        mAdapter = new DeviceListAdapter(getActivity());
        mAdapter.setData(mDeviceList);

        //mAdapter.getFilter().filter("68:AA:D2:02:F8:CB");//добавил для проверки
    }

    private List<BluetoothDevice> getPairedDevice() {
        ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();

        try {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            //получаем mac сохраненного устройства
            String savedDeviceAddress = getSavedDeviceAddress();

            if (pairedDevices == null || pairedDevices.size() == 0) {
                showToast(getString(R.string.no_paired_devices_found));
            } else {
                for (BluetoothDevice pDevice : pairedDevices) {
                    if (savedDeviceAddress == null || !savedDeviceAddress.equals(pDevice.getAddress())) {
                        //list.add(pDevice);
                    } else if (savedDeviceAddress.equals(pDevice.getAddress())) {
                        mSavedDevice = pDevice;
                    }
                    list.add(pDevice); //for debug
                }

            }
        } catch (Exception err) {
            Toast.makeText(getContext(), err.getMessage(), Toast.LENGTH_LONG).show();
        }
        return list;
    }

    /**
     * возвращает mac адрес сохраненного устройства
     * @return
     */
    public abstract String getSavedDeviceAddress();

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.device_scan_dialog, container,false);

        ButterKnife.bind(this, view);

        mImageDevice.setImageResource(getImageRes());
        mMessage.setText(getMessage());

        return view;
    }

    protected abstract int getImageRes();
    protected abstract String getMessage();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                Log.d(TAG,"BaseChoiceDeviceDialog onItemClick");
                BluetoothDevice btDevice = (BluetoothDevice) mAdapter.getItem(position);
                if (btDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    showConnectView();
                    connectToDevice(btDevice);
                } else {
                    showConnectView();
                    pairDevice(btDevice);
                }
            }
        });

        mListView.setAdapter(mAdapter);
        Log.d(TAG,"BaseChoiceDeviceDialog onViewCreated");
    }


    // Connect to  bluetooth device.
    protected abstract void connectToDevice(BluetoothDevice device);

    public void startScan() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering())
                mBluetoothAdapter.cancelDiscovery();
            showDeviceView();
            mBluetoothAdapter.startDiscovery();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        //register bluetooth receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        getActivity().registerReceiver( mBluetoothReceiver, filter);
        //
        if (mSavedDevice != null) {
            Log.d(TAG,"connectToDevice");
            showConnectView();
            connectToDevice(mSavedDevice);
        } else {
            Log.d(TAG,"startScan");
            startScan();
        }
    }

    private void showConnectView() {
        viewConnectContext.setVisibility(View.VISIBLE);
        viewDeviceContext.setVisibility(View.GONE);
    }

    private void showDeviceView() {
        viewConnectContext.setVisibility(View.GONE);
        viewDeviceContext.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mBluetoothReceiver);
        super.onStop();
    }

    /**
     *  проверка наличия устройсва в списке адаптера
     * @param device
     * @return
     */
    private boolean isDeviceExists(BluetoothDevice device) {
        boolean res = false;
        if (mDeviceList != null) {
            for (BluetoothDevice bluetoothDevice : mDeviceList) {
                if ( bluetoothDevice.getAddress().equals(device.getAddress()) )
                    res = true;
            }
        }
        return  res;
    }


    public void showToast(final String message) {
        final Context context = getActivity();
        if (context != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    Log.d(TAG,message);
                }
            });
        }
    }

    private class BluetoothReceiver extends  BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    //showToast("Enabled");

                    //showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) { //старт сканирования
                mDeviceList = new ArrayList<BluetoothDevice>();
                mDeviceList.addAll(getPairedDevice());
                mAdapter.setData(mDeviceList); //---
                mProgressBar.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { //завершение сканирования

                mAdapter.notifyDataSetChanged();

                //for debug
                for (BluetoothDevice bluetoothDevice : mDeviceList) {
                    Log.d(TAG, bluetoothDevice.getName()+" "+bluetoothDevice.getAddress());
                }

                mProgressBar.setVisibility(View.GONE);
                //mProgressDlg.dismiss();

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) { //обнаружено устройство
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if ( !isDeviceExists(device) ) {
                    mDeviceList.add(device);
                    mAdapter.notifyDataSetChanged();
                }

            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) { // отвязка/привязка устройтва

                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    showToast(getString(R.string.paired_device));
                    showConnectView();
                    connectToDevice(device);
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    showToast(getString(R.string.unpaired_device));
                }  else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDING){
                    showDeviceView();
                    showToast("Отмена");
                }

                mAdapter.notifyDataSetChanged();
            }

        }
    }


}
