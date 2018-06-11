package com.stashbank.deliverymanagment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.*;
import android.support.design.widget.*;

public class MainFragment extends Fragment
{

	private Button deliveryMenuButton;
	private Button shippingMenuButton;
	private Button paymentMenuButton;
	private View view;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_main, container, false);
		return view;
	}

}
