package com.stashbank.deliverymanagment;

import android.app.Activity;
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
import com.stashbank.deliverymanagment.models.*;
import com.stashbank.deliverymanagment.rest.*;
import retrofit2.*;
import android.util.*;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener,
	MainFragment.OnButtonClickListner,
	DeliveryFragment.DeliveryFragmentEventListener
{

	private DrawerLayout drawer;
	private Toolbar toolbar;
	private NavigationView navigationView;
	private DeliveryItem selectedDeliveryItem;

	private DeliveryFragment deliveryFragment;
	
	private int PAYMENT_REQ_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
			this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();
		
		if (savedInstanceState == null) {
			openMainFragment();
		}

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
			case R.id.nav_home:
				openMainFragment();
				break;
			case R.id.nav_delivery:
				openDeliveryFragment();
				break;
			case R.id.nav_shipping:
				openShippingFragment();
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
		navigationView.setCheckedItem(R.id.nav_home);

	}
	
	
	private void openDeliveryFragment() {
		deliveryFragment = new DeliveryFragment();
		deliveryFragment.setEventListener(this);
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, deliveryFragment)
			.commit();
		navigationView.setCheckedItem(R.id.nav_delivery);
	}
	
	private void openShippingFragment() {
		ShippingFragment fragment = new ShippingFragment();
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, fragment)
			.commit();
		navigationView.setCheckedItem(R.id.nav_shipping);
	}
	
	@Override
	public void onDeliveryButtonClick(View view)
	{
		openDeliveryFragment();
	}

	@Override
	public void onShippingButtonClick(View view)
	{
		openShippingFragment();
	}

	@Override
	public void onPaymentButtonClick(View view)
	{
		// TODO: Implement this method
		Toast.makeText(this, "Paymant button click", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void showProgress(final boolean show) {
		ProgressBar progressView = (ProgressBar) findViewById(R.id.progress);
		if (progressView != null) {
			FrameLayout frame = (FrameLayout) findViewById(R.id.fragment_container);
			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
			// frame.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public void overridePendingTransition(int enterAnim, int exitAnim)
	{
		// TODO: Implement this method
		super.overridePendingTransition(enterAnim, exitAnim);
	}

	@Override
	public void makePayment(DeliveryItem delivery)
	{
		selectedDeliveryItem = delivery;
		/*Intent intent = new Intent(this, PaymentActivity.class);
		intent.putExtra("number", delivery.getNumber());
		intent.putExtra("amount", delivery.getAmount());
		intent.putExtra("client", delivery.getClient());
		intent.putExtra("deliveryId", delivery.getId());
		startActivityForResult(intent, PAYMENT_REQ_CODE);*/
		Intent i = new Intent("com.sccp.gpb.emv.MAKE_PAYMENT");
		i.putExtra("amount", delivery.getNumber().toString());
		i.putExtra("customer_email", "hello@swiffpay.com");
		i.putExtra("ref_1", "");
		i.putExtra("ref_2", "");
		i.putExtra("ref_3", "");
		i.putExtra("ref_4", "");
		i.putExtra("mobile", "12345678");
		i.putExtra("extra", "invoice=19191");
		i.putExtra("source", getApplication().getPackageName());
		try {
			showProgress(true);
			startActivityForResult(i, PAYMENT_REQ_CODE);
		} catch (android.content.ActivityNotFoundException e) {
			showProgress(false);
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		showProgress(false);
		if (data == null) {return;}
		if (requestCode == PAYMENT_REQ_CODE) {
			onPaymentActivity(resultCode, data);
		}
	}

	void onPaymentActivity(int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			String transaction_response_code = data.getStringExtra("transaction_response_code");
			String transaction_number = data.getStringExtra("transaction_number");
			String transaction_ref = data.getStringExtra("transaction_ref");
			String amount = data.getStringExtra("amount");
			String order_info = data.getStringExtra("order_info");
			String auth_code = data.getStringExtra("auth_code");
			String extra = data.getStringExtra("extra");
			String email = data.getStringExtra("customer_email");
			String mobile = data.getStringExtra("mobile");
			String ref1 = data.getStringExtra("ref_1");
			String ref2 = data.getStringExtra("ref_2");
			String ref3 = data.getStringExtra("ref_3");
			String ref4 = data.getStringExtra("ref_4");

			String card_type = data.getStringExtra("card_type");
			String cc_name = data.getStringExtra("cc_name");
			String cc_number = data.getStringExtra("cc_number");
			String currency = data.getStringExtra("currency");

			String tsi = data.getStringExtra("TSI");
			String tvr = data.getStringExtra("TVR");
			String cvm_result = data.getStringExtra("CVMResult");

			// markAsPayed(selectedDeliveryItem);
			Toast.makeText(this, "Payed", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Error while make payment", Toast.LENGTH_LONG).show();
		}
	}
	
	void markAsPayed(DeliveryItem item) {
		item.setPayed(true);
		setDeliveryItem(item);
	}
	
	@Override
	public void markAsDelivered(DeliveryItem item)
	{
		item.setDelivered(true);
		setDeliveryItem(item);
	}

	private void setDeliveryItem(DeliveryItem item) {
		DeliveryItemRepository repository = new DeliveryItemRepository();
		Call<DeliveryItem> call = repository.setItem(item.getId(), item);
		showProgress(true);
		call.enqueue(new Callback<DeliveryItem>() {
			@Override
			public void onResponse(Call<DeliveryItem> call, Response<DeliveryItem> response) {
				showProgress(false);
				if (response.isSuccessful()) {
					deliveryFragment.fetchData();
				} else {
					log("response code " + response.code());
				}
			}

			@Override
			public void onFailure(Call<DeliveryItem> call, Throwable t) {
				showProgress(false);
				log("ERROR " + t);
				// itemAdapter.onFetchDataFailure();
			}

		});
	}
	
	private void log(String message)
	{
		Log.d("REST", message);
	}

}
