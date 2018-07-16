package com.stashbank.deliveryManagement;

import android.content.DialogInterface;
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
import com.stashbank.deliveryManagement.rest.*;
import retrofit2.*;
import com.stashbank.deliveryManagement.models.*;
import android.util.*;
import android.view.View.*;
import java.io.*;


public class PaymentActivity extends AppCompatActivity
{
	private final int MAKE_PAYMENT_CODE = 2;
	private Double amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		Intent intent = getIntent();

		String number = intent.getStringExtra("number");
		TextView tvNumber = (TextView)findViewById(R.id.payment_number);
		tvNumber.setText(number);

		amount = intent.getDoubleExtra("amount", 0);
		TextView tvAmount = (TextView)findViewById(R.id.payment_amount);
		tvAmount.setText(amount.toString());

		String client = intent.getStringExtra("client");
		TextView tvClient = (TextView)findViewById(R.id.payment_client);
		tvClient.setText(client);

		// fetchDeliveryItem(intent.getStringExtra("deliveryId"));
		setEditTextValue(R.id.et_amount, String.format( "%.2f", amount ));
		setEditTextValue(R.id.et_customer_email, "hello@swiffpay.com");
		setEditTextValue(R.id.et_mobile, "012345678");
		setEditTextValue(R.id.et_extra, "invoice=19191");
	}

	public  void onPayment(View view) {
		// TODO: DeliveryItem item = null;
		// view.setEnabled(false);
		makePayment(String.format( "%.2f", amount ));
	}

	public void showProgress(final boolean show) {
		ProgressBar progressView = (ProgressBar) findViewById(R.id.progress);
		if (progressView != null) {
			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}

	void makePayment(String amount) {
		Intent i = new Intent("com.sccp.gpb.emv.MAKE_PAYMENT");
		i.putExtra("amount", getEditTextValue(R.id.et_amount));
		i.putExtra("customer_email", getEditTextValue(R.id.et_customer_email));
		i.putExtra("ref_1", getEditTextValue(R.id.et_ref_1));
		i.putExtra("ref_2", getEditTextValue(R.id.et_ref_2));
		i.putExtra("ref_3", getEditTextValue(R.id.et_ref_3));
		i.putExtra("ref_4", getEditTextValue(R.id.et_ref_4));
		i.putExtra("mobile", getEditTextValue(R.id.et_mobile));
		i.putExtra("extra", getEditTextValue(R.id.et_extra));
		i.putExtra("source", getApplication().getPackageName());
		try {
			showProgress(true);
			startActivityForResult(i, MAKE_PAYMENT_CODE);
		} catch (android.content.ActivityNotFoundException e) {
			showProgress(false);
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	String getEditTextValue(int id) {
		TextView view = (TextView) findViewById(id);
		if (view != null)
			return view.getText().toString();
		return "";
	}

	void setEditTextValue(int id, String text) {
		TextView view = (TextView) findViewById(id);
		if (view != null)
			view.setText(text);
	}

	private void openOkDialog(String message, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
				.setTitle(message)
				.setPositiveButton(R.string.ok, listener)
				.create()
				.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		showProgress(false);
		if (data == null) {return;}
		if (requestCode == MAKE_PAYMENT_CODE) {
			String transaction_response_code = data.getStringExtra("transaction_response_code");
			Intent intent = new Intent();
			boolean payed = transaction_response_code == "0" && resultCode == RESULT_OK;
			intent.putExtra("payed",  payed);
			if (resultCode == RESULT_OK) {
				String message = null;
				switch(transaction_response_code) {
					case "0":
						createTransaction(data);
						openOkDialog("Оплата прошла успешно", (dialog, id) -> finish());
						break;
					case "-941":
						message = "Ошибка валидации входящих параметров";
						break;
					case "-999":
						message ="Отменено пользователем";
						break;
					default:
						createTransaction(data);
						message = "Не известная ошибка";
						break;
				}
				setResult(resultCode, intent);
				/*if (message != null)
					Toast.makeText(this, message, Toast.LENGTH_LONG).show();
				finish();*/
			}
		}
	}

	private void createTransaction(Intent data) {
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
		String msg = String.format("transaction_response_code: %s\n", transaction_response_code);
		msg += String.format("transaction_number: %s\n", transaction_number);
		msg += String.format("transaction_ref: %s\n", transaction_ref);
		msg += String.format("amount: %s\n", amount);
		msg += String.format("order_info: %s\n", order_info);
		msg += String.format("auth_code: %s\n", auth_code);
		msg += String.format("card_type: %s\n", card_type);
		msg += String.format("cc_name: %s\n", cc_name);
		msg += String.format("cc_number: %s\n", cc_number);
		msg += String.format("tsi: %s\n", tsi);
		msg += String.format("tvr: %s\n", tvr);
		msg += String.format("cvm_result: %s\n", cvm_result);
		// openOkDialog(msg, (dialog, id) -> emtyFn());
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void emtyFn() {}

	void fetchDeliveryItem(String id) {
        DeliveryItemRepository.Predicate<DeliveryItem, Exception> p = (item, err) -> {
            // showProgress(false);
            if (err != null) {
                DeliveryItem delivery = item;
                String number = delivery.getNumber();
                TextView tvNumber = (TextView)findViewById(R.id.payment_number);
                tvNumber.setText(number);

                amount = delivery.getAmount();
                TextView tvAmount = (TextView)findViewById(R.id.payment_amount);
                tvAmount.setText(amount.toString());

                String client = delivery.getClient();
                TextView tvClient = (TextView)findViewById(R.id.payment_client);
                tvClient.setText(client);
            } else {
                log("error " + err);
            }
        };
		DeliveryItemRepository repository = new DeliveryItemRepository();
        DeliveryItemRepository.DeliveryItemTask task = repository.getItemById(id, p);
		// showProgress(true);
		task.execute();
	}

	private void log(String message)
	{
		Log.d("REST", message);
	}
}
