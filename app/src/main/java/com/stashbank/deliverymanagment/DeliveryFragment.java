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
		// REST
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://crud-server.firebaseapp.com/")
			.addConverterFactory(GsonConverterFactory.create())
			.build();
		DeliveryItemApi api = retrofit.create(DeliveryItemApi.class);
		Call<List<DeliveryItem>> items = api.getItems();
		try {
		items.enqueue(new Callback<List<DeliveryItem>>() {
			@Override
			public void onResponse(Call<List<DeliveryItem>> call, Response<List<DeliveryItem>> response) {
				if (response.isSuccessful()) {
					itemAdapter.addAll(response.body());
				} else {
					log("response code " + response.code());
					itemAdapter.onFetchDataFailure();
				}
			}

			@Override
			public void onFailure(Call<List<DeliveryItem>> call, Throwable t) {
				log("ERROR " + t);
				itemAdapter.onFetchDataFailure();
			}

		});
		} catch (Exception e) {
			log("REST ERROR " + e);
		}
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
				"Some address",
				Math.round(Math.random() * 100000) / 100
			);
			itemAdapter.add(item);
		}
	}
 
}
