package com.stashbank.deliverymanagment.adapters;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;
import com.stashbank.deliverymanagment.models.*;
import android.support.v7.appcompat.*;
import android.widget.CompoundButton.*;

public class DeliveryItemAdapter extends ArrayAdapter implements OnCheckedChangeListener
{
	
	LayoutInflater layoutInflater;
	
	TextView tvNumber;
	TextView tvAddress;
	TextView tvAmount;
	CheckBox cbDelivered;
	
	public DeliveryItemAdapter(Context context, ArrayList<DeliveryItem> items) {
		super(context, 0, items);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		tvNumber = view.findViewById(R.id.item_number);
		tvAddress = view.findViewById(R.id.item_address);
		tvAmount = view.findViewById(R.id.item_amount);
		cbDelivered = view.findViewById(R.id.item_delivered);
		cbDelivered.setOnCheckedChangeListener(this);
		cbDelivered.setTag(position);
		cbDelivered.setChecked(item.getDelivered());
		
		tvNumber.setText(item.getNumber());
		tvAddress.setText(item.getAddress());
		tvAmount.setText("" + item.getAmount());
		return view;
	}

	
	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked)
	{
		int position = button.getTag();
		DeliveryItem item = (DeliveryItem) getItem(position);
		item.setDelivered(isChecked);
	}
	
	public void onFetchDataFailure() {
		Toast.makeText(getContext(), "Can't fetch data from server", Toast.LENGTH_LONG);
	}

}
