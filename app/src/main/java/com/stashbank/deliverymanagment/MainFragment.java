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
	public static abstract interface OnMainFragmentButtonClickListener
    {
        public abstract void onDeliveryMenuButtonClick(android.view.View view);
		public abstract void onShippingMenuButtonClick(android.view.View view);
		public abstract void onPaymentMenuButtonClick(android.view.View view);

    }

	private Context context;
	private OnMainFragmentButtonClickListener menuButtonClickListner;
	private Button deliveryMenuButton;
	private Button shippingMenuButton;
	private Button paymentMenuButton;
	private View view;
	
	public MainFragment(Context context, OnMainFragmentButtonClickListener menuButtonClickListner) {
		this.context = context;
		this.menuButtonClickListner = menuButtonClickListner;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_main, container, false);
		initButtons();
		return view;
	}
	
	private void initButtons() {
		deliveryMenuButton = view.findViewById(R.id.btn_menu_delivery);
		shippingMenuButton = view.findViewById(R.id.btn_menu_shipping);
		paymentMenuButton = view.findViewById(R.id.btn_menu_payment);

		deliveryMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					menuButtonClickListner.onDeliveryMenuButtonClick(view);
				}
			});

		shippingMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					menuButtonClickListner.onShippingMenuButtonClick(view);
				}
			});

		paymentMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					menuButtonClickListner.onPaymentMenuButtonClick(view);
				}
			});
	}

}
