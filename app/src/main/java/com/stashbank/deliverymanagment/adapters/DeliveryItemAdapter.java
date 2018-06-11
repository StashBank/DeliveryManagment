package com.stashbank.deliverymanagment.adapters;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;
import com.stashbank.deliverymanagment.models.*;
import android.support.v7.appcompat.*;

public class DeliveryItemAdapter extends BaseAdapter
{
	Context context;
	LayoutInflater layoutInflater;
	ArrayList<DeliveryItem> items;
	
	TextView tvNumber;
	TextView tvAddress;
	TextView tvAmount;
	CheckBox cbDelivered;
	
	public DeliveryItemAdapter(Context context, ArrayList<DeliveryItem> items) {
		this.context = context;
		this.items = items;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getItem(int position)
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		Item item = (Item)getItem(position);
		return item.getId();
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
		
		tvNumber.setText(item.getNumber());
		tvAddress.setText(item.getAddress());
		tvAmount.setText("" + item.getAmount());
		return view;
	}


	@Override
	public int getCount()
	{
		return items.size();
	}

}
