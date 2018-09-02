package com.stashbank.deliveryManagement.adapters;
import android.net.Uri;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;

import com.stashbank.deliveryManagement.R;
import com.stashbank.deliveryManagement.models.*;
import com.stashbank.deliveryManagement.ui.DeliveryFragment;

public class DeliveryItemAdapter extends ArrayAdapter
{

	LayoutInflater layoutInflater;
	DeliveryFragment.DeliveryFragmentEventListener eventListener;
	ArrayList<DeliveryItem> items;
	
	public DeliveryItemAdapter(Context context, ArrayList<DeliveryItem> items, DeliveryFragment.DeliveryFragmentEventListener eventListener) {
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
			view = layoutInflater.inflate(R.layout.delivery_item, parent, false);
		}
		DeliveryItem item = (DeliveryItem) getItem(position);
        TextView tvNumber = (TextView) view.findViewById(R.id.item_number);
        TextView tvClient = (TextView) view.findViewById(R.id.item_client);
        TextView tvMobile = (TextView) view.findViewById(R.id.item_mobile);
        TextView tvAddress = (TextView) view.findViewById(R.id.item_address);
        TextView tvAmount = (TextView) view.findViewById(R.id.item_amount);
        CheckBox cbDelivered = (CheckBox) view.findViewById(R.id.item_delivered);
		cbDelivered.setTag(position);
		cbDelivered.setChecked(item.isDelivered());
		cbDelivered.setEnabled(false);

        CheckBox cbPayed = (CheckBox) view.findViewById(R.id.item_payed);
		cbPayed.setChecked(item.isPayed());
		cbPayed.setEnabled(false);
		
		tvNumber.setText(item.getNumber());
		tvClient.setText(item.getClient());
		tvMobile.setText(item.getMobile());
		tvAddress.setText(item.getAddress());
		tvAmount.setText("" + item.getAmount());

		// GOOGLE MAPS
        Button btnOPenMap = (Button) view.findViewById(R.id.item_btn_navigation);
        btnOPenMap.setTag(position);
        btnOPenMap.setOnClickListener(v -> openMap(getAddress(v)));

        Button btnPay = (Button) view.findViewById(R.id.item_btn_pay);
		btnPay.setTag(position);
		btnPay.setOnClickListener(button -> {
            int position1 = (int) button.getTag();
            DeliveryItem item1 = (DeliveryItem) getItem(position1);
            eventListener.makePayment(item1);
        });
		btnPay.setVisibility(item.isPayed() ? View.GONE : View.VISIBLE);

        Button btnDeliver = (Button) view.findViewById(R.id.item_btn_delivered);
		btnDeliver.setTag(position);
		btnDeliver.setOnClickListener(button -> {
            int position12 = (int) button.getTag();
            DeliveryItem item12 = (DeliveryItem) getItem(position12);
            button.setEnabled(false);
            eventListener.markAsDelivered(item12);
        });
		boolean showDeliveryBtn = !(item.isDelivered() || !item.isPayed());
		btnDeliver.setVisibility(showDeliveryBtn ? View.VISIBLE : View.GONE);
        Button btnCall = (Button) view.findViewById(R.id.item_btn_call);
		btnCall.setTag(position);
		btnCall.setOnClickListener(v -> onCallButtonClick(v));
		return view;
	}
	
	public void onFetchDataFailure() {
		Toast.makeText(getContext(), R.string.cant_fetch_data_from_server, Toast.LENGTH_LONG).show();
	}

	public void onCallButtonClick(View button) {
		int position = (int) button.getTag();
		DeliveryItem item = (DeliveryItem) getItem(position);
		String phoneNumber = item.getMobile();
		if (this.eventListener != null & phoneNumber != null && phoneNumber != "")
			this.eventListener.makePhoneCall(phoneNumber);
	}

	private void openMap(String address) {
	    Uri uri = Uri.parse(String.format("google.navigation:q=%s", address));
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        getContext().startActivity(intent);
    }

    private String getAddress(View button) {
        int position = (int) button.getTag();
	    DeliveryItem item = (DeliveryItem) getItem(position);
	    if (item != null)
	        return item.getAddress();
	    return "";
    }

}
