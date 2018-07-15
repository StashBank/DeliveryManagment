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
	public interface OnButtonClickListener {
		void onDeliveryButtonClick(View view);
		void onShippingButtonClick(View view);
	}
	OnButtonClickListener buttonClickListener;

	@Override
	public void onAttach(Context context)
	{
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
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_main, container, false);
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
                buttonClickListener.onShippingButtonClick(view12);
        });
	}

	private void getDeliveryCount(View view) {
		DeliveryItemRepository repository = new DeliveryItemRepository();
		DeliveryItemRepository.DeliveryItemsCountTask task = repository.getItemsCount((count, err) -> {
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
		ReceivingItemRepository repository = new ReceivingItemRepository();
		ReceivingItemRepository.ReceivingItemsCountTask task = repository.getItemsCount((count, err) -> {
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

}
