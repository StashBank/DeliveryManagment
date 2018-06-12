package com.stashbank.deliverymanagment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.*;
import java.util.*;
import com.stashbank.deliverymanagment.models.*;
import com.stashbank.deliverymanagment.adapters.*;
import android.content.*;
import android.widget.*;

public class DeliveryFragment extends Fragment
{
	ArrayList<DeliveryItem> items = new ArrayList<DeliveryItem>();
	DeliveryItemAdapter itemAdapter;
	ListView listView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_delivery, container, false);
		fetchData();
		itemAdapter = new DeliveryItemAdapter(getActivity(), items);
		listView = view.findViewById(R.id.list_view);
		listView.setAdapter(itemAdapter);
		return view;
	}
	
	private void fetchData() {
		for (int i = 1; i <= 30; i++) {
			String number = "000" + i;
			DeliveryItem item = new DeliveryItem(
				i,
				number,
				"Some address",
				Math.random() * 1000
			);
			items.add(item);
		}
	}
 
}
