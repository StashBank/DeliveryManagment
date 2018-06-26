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
import android.support.v7.app.*;
import android.support.v4.view.*;
import android.widget.*;
import com.stashbank.deliverymanagment.rest.*;
import retrofit2.*;
import com.stashbank.deliverymanagment.models.*;
import android.util.*;
import android.view.View.*;


public class PaymentActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		Intent intent = getIntent();
		String number = intent.getStringExtra("number");
		double amount = intent.getDoubleExtra("amount", 0);
		String client = intent.getStringExtra("client");
		String text = String.format(
			"Payment for TTH: %s, UAH: %s, Client: %s",
			number, amount, client
		);
		TextView textView = (TextView)findViewById(R.id.payment_description);
		// textView.setText(text);
		fetchDeliveryItem(intent.getStringExtra("deliveryId"));
		Button btnPay = (Button) findViewById(R.id.btn_pay);
		btnPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1)
			{
				DeliveryItem item = null; //TODO
				makePayment(item);
			}	
			
		});
	}
	
	void makePayment(DeliveryItem item) {
		Intent intent = new Intent();
		intent.putExtra("payed", true);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	void markPayment(DeliveryItem item) {
		DeliveryItemRepository repository = new DeliveryItemRepository();
		Call<DeliveryItem> items = repository.setItem(item.getId(), item);
		// showProgress(true);
		items.enqueue(new Callback<DeliveryItem>() {
				@Override
				public void onResponse(Call<DeliveryItem> call, Response<DeliveryItem> response) {
					// showProgress(false);
					if (response.isSuccessful()) {
						// response.body();
					} else {
						log("response code " + response.code());
						// itemAdapter.onFetchDataFailure();
					}
				}

				@Override
				public void onFailure(Call<DeliveryItem> call, Throwable t) {
					// showProgress(false);
					log("ERROR " + t);
					// itemAdapter.onFetchDataFailure();
				}

			});
	}
	
	void fetchDeliveryItem(String id) {
		DeliveryItemRepository repository = new DeliveryItemRepository();
		Call<DeliveryItem> item = repository.getItemById(id);
		// showProgress(true);
		item.enqueue(new Callback<DeliveryItem>() {
				@Override
				public void onResponse(Call<DeliveryItem> call, Response<DeliveryItem> response) {
					// showProgress(false);
					if (response.isSuccessful()) {
						DeliveryItem delivery = response.body();
						String number = delivery.getNumber();
						double amount = delivery.getAmount();
						String client = delivery.getClient();
						String text = String.format(
							"Payment for TTH: %s, UAH: %s, Client: %s",
							number, amount, client
						);
						TextView textView = (TextView)findViewById(R.id.payment_description);
						textView.setText(text);
					} else {
						log("response code " + response.code());
						// itemAdapter.onFetchDataFailure();
					}
				}

				@Override
				public void onFailure(Call<DeliveryItem> call, Throwable t) {
					// showProgress(false);
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
