package com.stashbank.deliveryManagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.stashbank.deliveryManagement.DeliveryFragment;
import com.stashbank.deliveryManagement.R;
import com.stashbank.deliveryManagement.ReceivingFragment;
import com.stashbank.deliveryManagement.models.DeliveryItem;
import com.stashbank.deliveryManagement.models.ReceivingItem;

import java.util.ArrayList;

public class ReceivingItemAdapter extends ArrayAdapter
{

	LayoutInflater layoutInflater;
	ReceivingFragment.ReceivingFragmentEventListener eventListener;
	TextView tvNumber, tvAddress, tvClient, tvMobile;
	CheckBox cbReceived;
	Button btnReceived, btnCall;
	ArrayList<ReceivingItem> items;

	public ReceivingItemAdapter(Context context, ArrayList<ReceivingItem> items,
                                ReceivingFragment.ReceivingFragmentEventListener eventListener) {
		super(context, 0, items);
		this.items = items;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.eventListener = eventListener;
	}

	@Override
	public long getItemId(int position) {
		// Item item = (Item)getItem(position);
		return position; // item.getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.receiving_item, parent, false);
		}
        ReceivingItem item = (ReceivingItem) getItem(position);
		tvNumber = (TextView) view.findViewById(R.id.item_number);
		tvClient = (TextView) view.findViewById(R.id.item_client);
		tvMobile = (TextView) view.findViewById(R.id.item_mobile);
		tvAddress = (TextView) view.findViewById(R.id.item_address);
		
		tvNumber.setText(item.getNumber());
		tvClient.setText(item.getClient());
		tvMobile.setText(item.getMobile());
		tvAddress.setText(item.getAddress());

        cbReceived = (CheckBox) view.findViewById(R.id.item_received);
        cbReceived.setChecked(item.isReceived());
        cbReceived.setEnabled(false);

        btnReceived = (Button) view.findViewById(R.id.item_btn_received);
        btnReceived.setTag(position);
        btnReceived.setOnClickListener(button -> {
            int position12 = (int) button.getTag();
            ReceivingItem item12 = (ReceivingItem) getItem(position12);
            button.setEnabled(false);
            if (eventListener != null)
                eventListener.markAsReceived(item12);
        });
		boolean showReceivedBtn = !item.isReceived();
        btnReceived.setVisibility(showReceivedBtn ? View.VISIBLE : View.GONE);

		btnCall = (Button) view.findViewById(R.id.item_btn_call);
		btnCall.setTag(position);
		btnCall.setOnClickListener(v -> onCallButtonClick(v));
		return view;
	}
	
	public void onFetchDataFailure() {
		Toast.makeText(getContext(), R.string.cant_fetch_data_from_server, Toast.LENGTH_LONG).show();
	}

	public void onCallButtonClick(View button) {
		int position = (int) button.getTag();
        ReceivingItem item = (ReceivingItem) getItem(position);
		String phoneNumber = item.getMobile();
		if (this.eventListener != null & phoneNumber != null && phoneNumber != "")
			this.eventListener.makePhoneCall(phoneNumber);
	}

}
