package com.stashbank.deliverymanagment;

import android.os.Bundle;
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

public class ReceivingFragment extends Fragment
{
	ArrayList<ReceivingItem> items = new ArrayList();
	DeliveryItemAdapter itemAdapter;
	ListView listView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_shipping, container, false);
	}

}
