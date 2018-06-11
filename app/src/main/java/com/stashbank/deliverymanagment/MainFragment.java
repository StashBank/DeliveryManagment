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

	private Context context;
	private MainActivity mainActivity;
	private Button deliveryMenuButton;
	private Button shippingMenuButton;
	private Button paymentMenuButton;
	private View view;
	
	public MainFragment(Context context) {
		this.context = context;
		this.mainActivity = (MainActivity) context;
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
		Button deliveryMenuButton = view.findViewById(R.id.btn_menu_delivery);
		Button shippingMenuButton = view.findViewById(R.id.btn_menu_shipping);
		Button paymentMenuButton = view.findViewById(R.id.btn_menu_payment);

		deliveryMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Toast.makeText(context, "Delivery button click", Toast.LENGTH_SHORT).show();
					mainActivity.openDeliveryFragment();
				}
			});

		shippingMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Toast.makeText(context, "Shipping button click", Toast.LENGTH_SHORT).show();
					mainActivity.openShippingFragment();
				}
			});

		paymentMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "Paymant button click", Toast.LENGTH_SHORT).show();
				}
			});
	}

}
