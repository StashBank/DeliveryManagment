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
import retrofit2.*;
import retrofit2.converter.gson.*;
import com.stashbank.deliverymanagment.rest.*;
import android.util.*;
import android.view.View.*;

public class DeliveryFragment extends Fragment
{
	public interface DeliveryFragmentEventListener {
		public void showProgress(final boolean show);
		public void makePayment(DeliveryItem delivery);
		public void markAsDelivered(DeliveryItem delivery);
	}

	ArrayList<DeliveryItem> items = new ArrayList<DeliveryItem>();
	DeliveryItemAdapter itemAdapter;
	ListView listView;
	DeliveryFragmentEventListener eventListener;
	
	public void setEventListener(DeliveryFragmentEventListener eventListener) {
		this.eventListener = eventListener;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_delivery, container, false);
		fetchData();
		itemAdapter = new DeliveryItemAdapter(getActivity(), items, eventListener);
		listView = (ListView) view.findViewById(R.id.list_view);
		listView.setAdapter(itemAdapter);
		return view;
	}
	
	private void fetchData() {
		// REST
		DeliveryItemRepository repository = new DeliveryItemRepository();
		Call<List<DeliveryItem>> items = repository.getItems();
		if (eventListener != null)
			eventListener.showProgress(true);
		items.enqueue(new Callback<List<DeliveryItem>>() {
			@Override
			public void onResponse(Call<List<DeliveryItem>> call, Response<List<DeliveryItem>> response) {
				if (eventListener != null)
					eventListener.showProgress(false);
				if (response.isSuccessful()) {
					itemAdapter.addAll(response.body());
				} else {
					log("response code " + response.code());
					itemAdapter.onFetchDataFailure();
				}
			}

			@Override
			public void onFailure(Call<List<DeliveryItem>> call, Throwable t) {
				if (eventListener != null)
					eventListener.showProgress(false);
				log("ERROR " + t);
				itemAdapter.onFetchDataFailure();
			}

		});
	}
	
	private void log(String message)
	{
		Log.d("REST", message);
	}
	
	private void generateData() {
		for (int i = 1; i <= 30; i++) {
			String number = "000" + i;
			DeliveryItem item = new DeliveryItem(
				"" + i,
				number,
				"Client " + i,
				"Some address",
				Math.round(Math.random() * 100000) / 100
			);
			itemAdapter.add(item);
		}
	}
 
}
