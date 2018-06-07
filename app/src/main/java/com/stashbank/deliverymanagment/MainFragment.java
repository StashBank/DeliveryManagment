package com.stashbank.deliverymanagment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.*;

public class MainFragment extends Fragment
{

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

}
