package com.stashbank.deliveryManagement;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.*;
import java.util.*;
import com.stashbank.deliveryManagement.models.*;
import com.stashbank.deliveryManagement.adapters.*;

import android.widget.*;

import com.stashbank.deliveryManagement.rest.*;
import android.util.*;

public class DeliveryFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener
{

	public interface DeliveryFragmentEventListener {
		void showProgress(final boolean show);
		void makePayment(DeliveryItem delivery);
		void markAsDelivered(DeliveryItem delivery);
		void makePhoneCall(String number);
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
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
	
	public void fetchData(final boolean isRefresh) {
        DeliveryItemRepository.Predicate<List<DeliveryItem>, Exception> p = (items, error) -> {
            showLoaderSpinner(false);
            if (isRefresh)
                swipeRefreshLayout.setRefreshing(false);
            if (error != null) {
                log("ERROR " + error);
                itemAdapter.onFetchDataFailure();
            } else {
                itemAdapter.clear();
                itemAdapter.addAll(items);
            }
        };
        if (!isRefresh)
            showLoaderSpinner(true);
		DeliveryItemRepository repository = new DeliveryItemRepository();
		DeliveryItemRepository.DeliveryItemsTask task = repository.getItems(p, getContext(), isRefresh);
		task.execute();
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
