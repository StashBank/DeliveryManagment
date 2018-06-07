package com.stashbank.deliverymanagment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.support.v4.widget.*;
import android.support.design.widget.*;
import android.view.*;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v7.app.*;
import android.support.v4.view.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

	private DrawerLayout drawer;
	private Toolbar toolbar;
	private Button deliveryMenuButton;
	private Button shippingMenuButton;
	private Button paymentMenuButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
			this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();
		
		if (savedInstanceState == null) {
			openMainFragment();
			// navigationView.setCheckedItem(R.id.nav_delivery);
		}
		// initButtons();

	}
	
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.top_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {

		switch (menuItem.getItemId()) {
			case R.id.nav_delivery:
				openDeliveryFragment();
				menuItem.setChecked(true);
				break;
			case R.id.nav_shipping:
				openShippingFragment();
				menuItem.setChecked(true);
				break;
			case R.id.nav_share:
				Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_send:
				Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
				break;
		}
		drawer.closeDrawer(GravityCompat.START);

		return true;
	}
	
	private void openMainFragment() {
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, new MainFragment())
			.commit();
	}
	
	private void openDeliveryFragment() {
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, new DeliveryFragment())
			.commit();
	}
	
	private void openShippingFragment() {
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, new ShippingFragment())
			.commit();
	}
	
	private void initButtons() {
		deliveryMenuButton = (Button) findViewById(R.id.btn_menu_delivery);
		shippingMenuButton = (Button) findViewById(R.id.btn_menu_shipping);
		paymentMenuButton = (Button) findViewById(R.id.btn_menu_payment);

		deliveryMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {}
			});

		shippingMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {}
			});

		paymentMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {}
			});
	}
}
