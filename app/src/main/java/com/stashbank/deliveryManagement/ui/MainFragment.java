package com.stashbank.deliveryManagement.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.*;
import android.content.*;

import com.stashbank.deliveryManagement.R;
import com.stashbank.deliveryManagement.rest.DeliveryItemRepository;
import com.stashbank.deliveryManagement.rest.ReceivingItemRepository;

public class MainFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener
{
    // TODO: refactor this to predicate
	public interface OnButtonClickListener {
		void onDeliveryButtonClick(View view);
		void onReceivingButtonClick(View view);
	}

	OnButtonClickListener buttonClickListener;
    SwipeRefreshLayout swipeRefreshLayout;
	View deliveryInfoWrap, receivingInfoWrap, deliveryInfoProgress, receivingInfoProgress;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			buttonClickListener = (OnButtonClickListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(
				context.toString() + "must implement OnButtonClickListener interface"
			);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
        deliveryInfoWrap = view.findViewById(R.id.delivery_info_wrap);
        deliveryInfoProgress = view.findViewById(R.id.delivery_info_progress);
        receivingInfoWrap = view.findViewById(R.id.receiving_info_wrap);
        receivingInfoProgress = view.findViewById(R.id.receiving_info_progress);
		intiClickEvents(view);
		getDeliveryCount(view, false);
		getReceivingCount(view, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
		return view;
	}

    @Override
    public void onRefresh() {
        this.updateInfoData(true);
    }

	public void updateInfoData(boolean isRefresh) {
        if (isRefresh)
            swipeRefreshLayout.setRefreshing(false);
	    View view = getView();
        getDeliveryCount(view, isRefresh);
        getReceivingCount(view, isRefresh);
    }
	
	private void intiClickEvents(View view) {
		CardView deliveryCard = (CardView) view.findViewById(R.id.menu_card_delivery);
		CardView receivingCard = (CardView) view.findViewById(R.id.menu_card_receiving);

		deliveryCard.setOnClickListener(view1 -> {
            if (buttonClickListener != null)
                buttonClickListener.onDeliveryButtonClick(view1);
        });

		receivingCard.setOnClickListener(view12 -> {
            if (buttonClickListener != null)
                buttonClickListener.onReceivingButtonClick(view12);
        });
	}

	private void getDeliveryCount(View view, boolean isRefresh) {
        showDeliveryInfoProgress(true);
		DeliveryItemRepository repository = new DeliveryItemRepository();
		DeliveryItemRepository.DeliveryItemsCountTask task = repository.getItemsCount((count, err) -> {
            showDeliveryInfoProgress(false);
			if (err != null) {
				String message = "Error while getting delivery count";
				Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
				return;
			}
			TextView textView = (TextView) view.findViewById(R.id.tv_delivery_count);
			String text = String.format("Осталось %s", count);
			textView.setText(text);
		}, getContext(), isRefresh);
		task.execute();
	}

	private void getReceivingCount(View view, boolean isRefresh) {
        showReceivingInfoProgress(true);
		ReceivingItemRepository repository = new ReceivingItemRepository();
		ReceivingItemRepository.ReceivingItemsCountTask task = repository.getItemsCount((count, err) -> {
            showReceivingInfoProgress(false);
			if (err != null) {
				String message = "Error while getting receiving count";
				Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
				Log.d("ERROR: ", err.toString());
				return;
			}
			TextView textView = (TextView) view.findViewById(R.id.tv_receiving_count);
			String text = String.format("Осталось %s", count);
			textView.setText(text);
		}, getContext(), isRefresh);
		task.execute();
	}

	private void showDeliveryInfoProgress(boolean show) {
	    if (deliveryInfoWrap != null && deliveryInfoProgress != null) {
            deliveryInfoProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            deliveryInfoWrap.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showReceivingInfoProgress(boolean show) {
	    if (receivingInfoWrap != null && receivingInfoWrap != null) {
            receivingInfoProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            receivingInfoWrap.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
