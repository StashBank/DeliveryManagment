package com.stashbank.deliverymanagment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class DeliveryFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener
{

	public interface DeliveryFragmentEventListener {
		void showProgress(final boolean show);
		void makePayment(DeliveryItem delivery);
		void markAsDelivered(DeliveryItem delivery);
	}

	ArrayList<DeliveryItem> items = new ArrayList<DeliveryItem>();
	DeliveryItemAdapter itemAdapter;
	ListView listView;
	SwipeRefreshLayout swipeRefreshLayout;
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
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
		swipeRefreshLayout.setOnRefreshListener(this);
		return view;
	}

	@Override
	public void onRefresh() {
		this.fetchData(true);
	}

	public void fetchData() {
		this.fetchData(false);
	}
	
	private void fetchData(final boolean isRefresh) {
		DeliveryItemRepository repository = new DeliveryItemRepository();
		Call<List<DeliveryItem>> call = repository.getItems();
		if (!isRefresh)
			showLoaderSpinner(true);
		call.enqueue(new Callback<List<DeliveryItem>>() {
			@Override
			public void onResponse(Call<List<DeliveryItem>> call, Response<List<DeliveryItem>> response) {
				if (isRefresh)
					swipeRefreshLayout.setRefreshing(false);
				else
					showLoaderSpinner(false);
				if (response.isSuccessful()) {
					itemAdapter.clear();
					itemAdapter.addAll(response.body());
				} else {
					log("response code " + response.code());
					itemAdapter.onFetchDataFailure();
				}
			}

			@Override
			public void onFailure(Call<List<DeliveryItem>> call, Throwable t) {
				if (isRefresh)
					swipeRefreshLayout.setRefreshing(false);
				else
					showLoaderSpinner(false);
				log("ERROR " + t);
				itemAdapter.onFetchDataFailure();
			}

		});
	}

	private void showLoaderSpinner(boolean show) {
		if (eventListener != null)
			eventListener.showProgress(show);
	}

	
	private void log(String message)
	{
		Log.d("REST", message);
	}
 
}
