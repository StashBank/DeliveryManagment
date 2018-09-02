package com.stashbank.deliveryManagement.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.stashbank.deliveryManagement.DeliveryManagementApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.stashbank.deliveryManagement.DeliveryManagementApp;
import com.stashbank.deliveryManagement.R;


/**
 *
 */
public class DeviceListAdapter extends BaseAdapter implements Filterable {
    public static final String TAG = DeviceListAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<BluetoothDevice> mData;
    //
    private ItemFilter mFilter = new ItemFilter();

    public DeviceListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<BluetoothDevice> data) {
        mData = data;
    }


    public int getCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView	=  mInflater.inflate(R.layout.list_item_device, null);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device	= mData.get(position);

        //set data
        holder.name.setText(
                (device.getName() != null
                        ? device.getName()
                        : DeliveryManagementApp
                        .instance
                        .getApplicationContext().getString(R.string.noname_device))
        );
        holder.address.setText(device.getAddress());

        return convertView;
    }

    /**
     * filter for device list
     * @return
     */
    @Override
    public Filter getFilter() {
        return mFilter;
    }

    static class ViewHolder {

        @BindView(R.id.tv_name) TextView name;
        @BindView(R.id.tv_address) TextView address;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toUpperCase();

            FilterResults results = new FilterResults();

            final List<BluetoothDevice> list = mData ;
            Log.d(TAG,"DeviceListAdapter performFiltering "+list.size() );


            final ArrayList<BluetoothDevice> nlist = new ArrayList<BluetoothDevice>();

            BluetoothDevice filterable ;

            for (int i = 0; i < list.size(); i++) {
                filterable = list.get(i);
                if ( filterable.getAddress().toUpperCase().contains(filterString) ) {
                    nlist.add(filterable);
                    Log.d(TAG,"DeviceListAdapter performFiltering found"+filterString );
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //filteredData = (ArrayList<String>) results.values;
            mData = (ArrayList<BluetoothDevice>) results.values;
            notifyDataSetChanged();
        }

    }


}