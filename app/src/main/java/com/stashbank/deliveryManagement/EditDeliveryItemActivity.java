package com.stashbank.deliveryManagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.stashbank.deliveryManagement.models.DeliveryItem;
import com.stashbank.deliveryManagement.rest.DeliveryItemRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDeliveryItemActivity extends AppCompatActivity {

	private ProgressBar progressBar;
	private Button btnSave;
	private EditText etNumber, etClientName, etClientPhone, etClientAddress, etAmount;
	private CheckBox cbPayed;
	private static final String EMPTY_STRING = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_delivery_item);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(v -> onSaveButtonClick(v));
		etNumber = (EditText) findViewById(R.id.etNumber);
		etClientName = (EditText) findViewById(R.id.etClientName);
		etClientPhone = (EditText) findViewById(R.id.etClientPhone);
		etClientAddress = (EditText) findViewById(R.id.etClientAddress);
		etAmount = (EditText) findViewById(R.id.etAmount);
		cbPayed = (CheckBox) findViewById(R.id.cbPayed);
	}

	public void onSaveButtonClick(View view) {
		if (this.validate()) {
			DeliveryItem item = getDeliveryItem();
			saveItem(item);
		}
	}

	private boolean validate() {
		return validateRequired(etNumber, "Номер")
				&& validateRequired(etClientName, "Клиент")
				&& validateRequired(etClientPhone, "Телефон")
				&& validateRequired(etClientAddress, "Адрес")
				&& validateAmount();

	}

	private boolean validateRequired(TextView view, String caption) {
		String text = view.getText().toString();
		boolean valid = text != null && text.length() > 0;
		view.setError(valid ? null : String.format("%s объязателен для заполения", caption));
		return valid;
	}

	private boolean validateAmount() {
		double amount = 0;
		String amountStr = etAmount.getText().toString();
		if (amountStr != null && amountStr.length() > 0)
			try {
				amount = Double.parseDouble(amountStr);
			} catch (Exception e) {}
		boolean valid = amount > 0;
		etAmount.setError(valid ? null : "Введите корректную сумму");
		return valid;
	}


	private DeliveryItem getDeliveryItem() {
		DeliveryItem item = new DeliveryItem();
		String number = etNumber.getText().toString();
		item.setNumber(number);
		String client = etClientName.getText().toString();
		item.setClient(client);
		String phone = etClientPhone.getText().toString();
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
        DeliveryItemRepository.Predicate<DeliveryItem, Exception> p = (items, error) -> {
            showProgress(false);
            btnSave.setEnabled(true);
            if (error != null) {
                showToast("Data has not been saved");
            } else {
                clearFields();
                showToast("Data has been saved");
            }
        };
		DeliveryItemRepository repository = new DeliveryItemRepository();
        DeliveryItemRepository.DeliveryItemTask task = repository.addItem(item, p, this);
		showProgress(true);
		btnSave.setEnabled(false);
        task.execute();
	}

	private void showToast(String messgae) {
		Toast.makeText(this, messgae, Toast.LENGTH_SHORT).show();
	}

	private void showProgress(boolean show) {
		progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	private void clearFields() {
		etNumber.setText(EMPTY_STRING);
		etClientName.setText(EMPTY_STRING);
		etClientPhone.setText(EMPTY_STRING);
		etClientAddress.setText(EMPTY_STRING);
		etAmount.setText(EMPTY_STRING);
		cbPayed.setChecked(false);
	}
}
