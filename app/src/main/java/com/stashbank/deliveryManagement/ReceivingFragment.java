package com.stashbank.deliveryManagement;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.*;
import java.util.*;
import com.stashbank.deliveryManagement.models.*;
import com.stashbank.deliveryManagement.adapters.*;
import com.stashbank.deliveryManagement.rest.ReceivingItemRepository;

import android.widget.*;

public class ReceivingFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener
{
    public interface ReceivingFragmentEventListener {
        void showProgress(final boolean show);
        void markAsReceived(ReceivingItem receiving);
        void makePhoneCall(String number);
    }

	ArrayList<ReceivingItem> items = new ArrayList();
	ReceivingItemAdapter itemAdapter;
	ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    ReceivingFragmentEventListener eventListener;

    public void setEventListener(ReceivingFragmentEventListener eventListener) {
        this.eventListener = eventListener;
    }

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
        View view = inflater.inflate(R.layout.fragment_receiving, container, false);
        fetchData();
        itemAdapter = new ReceivingItemAdapter(getActivity(), items, eventListener);
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
        ReceivingItemRepository.Predicate<List<ReceivingItem>, Exception> p = (items, error) -> {
            showLoaderSpinner(false);
            if (isRefresh)
                swipeRefreshLayout.setRefreshing(false);
            if (error != null) {
                log("ERROR " + error);
                itemAdapter.onFetchDataFailure();
            } else if (items != null) {
                itemAdapter.clear();
                itemAdapter.addAll(items);
            }
        };
        if (!isRefresh)
            showLoaderSpinner(true);
        ReceivingItemRepository repository = new ReceivingItemRepository();
        ReceivingItemRepository.ReceivingItemsTask task = repository.getItems(p, getContext(), isRefresh);
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
