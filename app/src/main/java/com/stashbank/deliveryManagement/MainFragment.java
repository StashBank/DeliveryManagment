package com.stashbank.deliveryManagement;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.*;
import android.content.*;

import com.stashbank.deliveryManagement.rest.DeliveryItemRepository;
import com.stashbank.deliveryManagement.rest.ReceivingItemRepository;

public class MainFragment extends Fragment
{
    // TODO: refactor this to predicate
	public interface OnButtonClickListener {
		void onDeliveryButtonClick(View view);
		void onReceivingButtonClick(View view);
	}

	OnButtonClickListener buttonClickListener;

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
		getDeliveryCount(view);
		getReceivingCount(view);
		return view;
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

	private void getDeliveryCount(View view) {
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
			String text = String.format("Не завершенных доставок %s", count);
			textView.setText(text);
		});
		task.execute();
	}

	private void getReceivingCount(View view) {
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
			String text = String.format("Не завершенных отгрузок %s", count);
			textView.setText(text);
		});
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
