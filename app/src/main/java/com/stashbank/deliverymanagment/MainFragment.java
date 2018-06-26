package com.stashbank.deliverymanagment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.*;
import android.support.design.widget.*;
import android.content.*;

public class MainFragment extends Fragment
{
	public interface OnButtonClickListner {
		public void onDeliveryButtonClick(View view);
		public void onShippingButtonClick(View view);
		public void onPaymentButtonClick(View view);
	}
	OnButtonClickListner buttonClickListner;

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		try {
			buttonClickListner = (OnButtonClickListner) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(
				context.toString() + "must implement OnButtonClickListner interface"
			);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		initButtons(view);
		return view;
	}
	
	private void initButtons(View view) {
		Button deliveryMenuButton = (Button) view.findViewById(R.id.btn_menu_delivery);
		Button shippingMenuButton = (Button) view.findViewById(R.id.btn_menu_shipping);
		Button paymentMenuButton = (Button) view.findViewById(R.id.btn_menu_payment);

		deliveryMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (buttonClickListner != null)
						buttonClickListner.onDeliveryButtonClick(view);
				}
			});

		shippingMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (buttonClickListner != null)
						buttonClickListner.onShippingButtonClick(view);
				}
			});

		paymentMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (buttonClickListner != null)
						buttonClickListner.onPaymentButtonClick(view);
				}
			});
	}

}
