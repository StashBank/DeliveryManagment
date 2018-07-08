package com.stashbank.deliverymanagment.adapters;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;

import com.stashbank.deliverymanagment.R;
import com.stashbank.deliverymanagment.models.*;
import android.widget.CompoundButton.*;
import com.stashbank.deliverymanagment.DeliveryFragment;

public class DeliveryItemAdapter extends ArrayAdapter implements OnCheckedChangeListener
{
	
	LayoutInflater layoutInflater;
	DeliveryFragment.DeliveryFragmentEventListener eventListener;
	TextView tvNumber;
	TextView tvAddress;
	TextView tvClient;
	TextView tvAmount;
	CheckBox cbDelivered;
	CheckBox cbPayed;
	Button btnPay;
	Button btnDeliver;
	
	public DeliveryItemAdapter(Context context, ArrayList<DeliveryItem> items, DeliveryFragment.DeliveryFragmentEventListener eventListener) {
		super(context, 0, items);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.eventListener = eventListener;
	}

	@Override
	public long getItemId(int position)
	{
		// Item item = (Item)getItem(position);
		return position; // item.getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.delivery_item, parent, false);
		}
		DeliveryItem item = (DeliveryItem) getItem(position);
		tvNumber = (TextView) view.findViewById(R.id.item_number);
		tvClient = (TextView) view.findViewById(R.id.item_client);
		tvAddress = (TextView) view.findViewById(R.id.item_address);
		tvAmount = (TextView) view.findViewById(R.id.item_amount);
		cbDelivered = (CheckBox) view.findViewById(R.id.item_delivered);
		cbDelivered.setOnCheckedChangeListener(this);
		cbDelivered.setTag(position);
		cbDelivered.setChecked(item.isDelivered());
		cbDelivered.setEnabled(false);
		
		cbPayed = (CheckBox) view.findViewById(R.id.item_payed);
		cbPayed.setChecked(item.isPayed());
		cbPayed.setEnabled(false);
		
		tvNumber.setText(item.getNumber());
		tvClient.setText(item.getClient());
		tvAddress.setText(item.getAddress());
		tvAmount.setText("" + item.getAmount());
		
		btnPay = (Button) view.findViewById(R.id.item_btn_pay);
		btnPay.setTag(position);
		btnPay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View button) {
				int position = (int) button.getTag();
				DeliveryItem item = (DeliveryItem) getItem(position);
				eventListener.makePayment(item);
			}
		});
		btnPay.setVisibility(item.isPayed() ? View.GONE : View.VISIBLE);
		

		btnDeliver = (Button) view.findViewById(R.id.item_btn_delivered);
		btnDeliver.setTag(position);
		btnDeliver.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View button) {
					int position = (int) button.getTag();
					DeliveryItem item = (DeliveryItem) getItem(position);
					eventListener.markAsDelivered(item);
				}
			});
		boolean showDeliveryBtn = !(item.isDelivered() || !item.isPayed());
		btnDeliver.setVisibility(showDeliveryBtn ? View.VISIBLE : View.GONE);
		return view;
	}

	
	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked)
	{
		int position = (int) button.getTag();
		DeliveryItem item = (DeliveryItem) getItem(position);
		item.setDelivered(isChecked);
	}
	
	public void onFetchDataFailure() {
		Toast.makeText(getContext(), "Can't fetch data from server", Toast.LENGTH_LONG).show();
	}

}
