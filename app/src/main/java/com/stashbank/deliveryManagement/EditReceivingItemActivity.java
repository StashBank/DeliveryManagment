package com.stashbank.deliveryManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.stashbank.deliveryManagement.models.ReceivingItem;
import com.stashbank.deliveryManagement.rest.ReceivingItemRepository;

public class EditReceivingItemActivity extends AppCompatActivity {

	private ProgressBar progressBar;
	private Button btnSave;
	private EditText etNumber, etClientName, etClientPhone, etClientAddress;
	private static final String EMPTY_STRING = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_receiving_item);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(v -> onSaveButtonClick(v));
		etNumber = (EditText) findViewById(R.id.etNumber);
		etClientName = (EditText) findViewById(R.id.etClientName);
		etClientPhone = (EditText) findViewById(R.id.etClientPhone);
		etClientAddress = (EditText) findViewById(R.id.etClientAddress);
	}

	public void onSaveButtonClick(View view) {
		if (this.validate()) {
			ReceivingItem item = getReceivingItem();
			saveItem(item);
		}
	}

	private boolean validate() {
		return validateRequired(etNumber, "Номер")
				&& validateRequired(etClientName, "Клиент")
				&& validateRequired(etClientPhone, "Телефон")
				&& validateRequired(etClientAddress, "Адрес");

	}

	private boolean validateRequired(TextView view, String caption) {
		String text = view.getText().toString();
		boolean valid = text != null && text.length() > 0;
		view.setError(valid ? null : String.format("%s объязателен для заполения", caption));
		return valid;
	}


	private ReceivingItem getReceivingItem() {
		ReceivingItem item = new ReceivingItem();
		String number = etNumber.getText().toString();
		item.setNumber(number);
		String client = etClientName.getText().toString();
		item.setClient(client);
		String phone = etClientPhone.getText().toString();
		item.setMobile(phone);
		String address = etClientAddress.getText().toString();
		item.setAddress(address);
		return item;
	}

	private void saveItem(ReceivingItem item) {
        ReceivingItemRepository.Predicate<ReceivingItem, Exception> p = (items, error) -> {
            showProgress(false);
            btnSave.setEnabled(true);
            if (error != null) {
                showToast("Data has not been saved");
            } else {
                clearFields();
                showToast("Data has been saved");
            }
        };
		ReceivingItemRepository repository = new ReceivingItemRepository();
		ReceivingItemRepository.ReceivingItemTask task = repository.addItem(item, p, this);
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
	}
}
