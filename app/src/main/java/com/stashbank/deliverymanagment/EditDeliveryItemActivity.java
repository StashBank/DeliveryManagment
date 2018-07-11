package com.stashbank.deliverymanagment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.stashbank.deliverymanagment.models.DeliveryItem;
import com.stashbank.deliverymanagment.rest.DeliveryItemRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDeliveryItemActivity extends AppCompatActivity {

	private ProgressBar progressBar;
	private Button btnSave;
	private EditText etClientName, etClientPhone, etClientAddress, etAmount;
	private CheckBox cbPayed;
	private static final String EMPTY_STRING = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_delivery_item);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSaveButtonClick(v);
			}
		});
		etClientName = (EditText) findViewById(R.id.etClientName);
		etClientPhone = (EditText) findViewById(R.id.etClientPhone);
		etClientAddress = (EditText) findViewById(R.id.etClientAddress);
		etAmount = (EditText) findViewById(R.id.etAmount);
		cbPayed = (CheckBox) findViewById(R.id.cbPayed);
	}

	public void onSaveButtonClick(View view) {
		DeliveryItem item = getDeliveryItem();
		saveItem(item);
	}

	private DeliveryItem getDeliveryItem() {
		DeliveryItem item = new DeliveryItem();
		String client = etClientName.getText().toString();
		item.setClient(client);
		String phone = etClientName.getText().toString();
		item.setMobile(phone);
		String address = etClientAddress.getText().toString();
		item.setAddress(address);
		String amount = etAmount.getText().toString();
		item.setAmount(Double.parseDouble(amount));
		Boolean payed = cbPayed.isChecked();
		item.setPayed(payed);
		return item;
	}

	private void saveItem(DeliveryItem item) {
		DeliveryItemRepository repository = new DeliveryItemRepository();
		Call<DeliveryItem> call = repository.addItem(item);
		showProgress(true);
		btnSave.setEnabled(false);
		call.enqueue(new Callback<DeliveryItem>() {
			@Override
			public void onResponse(Call<DeliveryItem> call, Response<DeliveryItem> response) {
				showProgress(false);
				btnSave.setEnabled(true);
				if (response.isSuccessful()) {
					showToast("Data has been saved");
					clearFields();
				}  else {
					showToast("Data has not been saved");
				}
			}

			@Override
			public void onFailure(Call<DeliveryItem> call, Throwable t) {
				showProgress(false);
				btnSave.setEnabled(true);
				showToast("Error while sending request");
			}
		});
	}

	private void showToast(String messgae) {
		Toast.makeText(this, messgae, Toast.LENGTH_SHORT).show();
	}

	private void showProgress(boolean show) {
		progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	private void clearFields() {
		etClientName.setText(EMPTY_STRING);
		etClientPhone.setText(EMPTY_STRING);
		etClientAddress.setText(EMPTY_STRING);
		etAmount.setText(EMPTY_STRING);
		cbPayed.setChecked(false);
	}
}
