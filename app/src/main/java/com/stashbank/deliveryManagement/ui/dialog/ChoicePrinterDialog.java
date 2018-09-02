package com.stashbank.deliveryManagement.ui.dialog;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import ua.pbank.dio.minipos.MiniPosManager;
import ua.pbank.dio.minipos.interfaces.MiniPosConnectionListener;

import com.stashbank.deliveryManagement.R;
import com.stashbank.deliveryManagement.models.PairedDevice;

/**
 * Created by dn160671kav on 17.08.2016.
 */
public class ChoicePrinterDialog extends BaseChoiceDeviceDialog {

    private static final String TAG = ChoicePrinterDialog.class.getName();

    public static ChoicePrinterDialog newInstance() {
        ChoicePrinterDialog dialog = new ChoicePrinterDialog();
        return dialog;
    }

    @Override
    protected String getDialogTag() {
        return TAG;
    }


    @Override
    public String getSavedDeviceAddress() {
        PairedDevice device = new Select().from(PairedDevice.class).where("type = ?","p").executeSingle();
        if (device != null) Log.d(TAG,"srored address:"+device.getAddress());
        return (device != null ? device.getAddress() : null);
    }

    @Override
    protected int getImageRes() {
        if (getSavedDeviceAddress() != null)
            return R.drawable.red_print;
        else
            return R.drawable.print;
    }

    @Override
    protected String getMessage() {
        if (getSavedDeviceAddress() != null)
            return getString(R.string.message_printer);
        else
            return getString(R.string.message_printer_new);
    }

    @Override
    protected void connectToDevice(final BluetoothDevice device) {

        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();

        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        /**
         *
         */
        MiniPosManager.getInstance().connectToPrinter(bluetoothAdapter, device, new MiniPosConnectionListener() {
            @Override
            public void onConnectionSuccess(String sn) {
                saveDevice(device);
                if (getActivity() != null)
                    getActivity().runOnUiThread(() -> {
                        showToast(getString(R.string.connect_device_success));
                        MiniPosConnectionListener miniPosConnectionListener = MiniPosManager.getInstance().getPrinterConnectionListener();
                        if (miniPosConnectionListener != null) {
                            miniPosConnectionListener.onConnectionSuccess(sn);
                        }
                        dismiss();
                    });
            }

            @Override
            public void onConnectionFailed() {
                if (getActivity() != null) {
                    showToast(getString(R.string.connect_device_error));
                    final Thread thread = new Thread(() -> scanDevice());
                    thread.start();
                }
            }

            @Override
            public void onDeviceRelease() {
                onConnectionFailed();
            }

            @Override
            public void onBluetoothRequest() {

            }
        });

    }

    public void scanDevice() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startScan();
            }
        });
    }

    public void saveDevice(BluetoothDevice device) {
        new Delete().from(PairedDevice.class).where("type = ?","p").execute();   //удаляем сохраненные
        PairedDevice pairedDevice = new PairedDevice("",device.getAddress(),"p"); //принтер
        pairedDevice.save();
    }



}
