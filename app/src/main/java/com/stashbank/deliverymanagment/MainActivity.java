package com.stashbank.deliverymanagment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

	private Button deliveryMenuButton;
	private Button shippingMenuButton;
	private Button paymentMenuButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		deliveryMenuButton = (Button) findViewById(R.id.btn_menu_delivery);
		shippingMenuButton = (Button) findViewById(R.id.btn_menu_shipping);
		paymentMenuButton = (Button) findViewById(R.id.btn_menu_payment);

		deliveryMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		shippingMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ShippingActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		paymentMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
				// MainActivity.this.startActivity(intent);
			}
		});

	}
}
