package com.stashbank.deliveryManagement.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.*;
import android.widget.*;
import butterknife.BindView;
import ua.pbank.dio.minipos.MiniPosManager;
import ua.pbank.dio.minipos.interfaces.MiniPosConnectionListener;
import ua.pbank.dio.minipos.interfaces.MiniPosServiceErrorListener;
import ua.pbank.dio.minipos.interfaces.MiniPosTransactionListener;
import ua.pbank.dio.minipos.models.Transaction;

import android.util.*;
import com.stashbank.deliveryManagement.R;
import com.stashbank.deliveryManagement.ui.dialog.BaseDialogFragment;
import com.stashbank.deliveryManagement.ui.dialog.ChoicePinpadDialog;


public class PaymentActivity extends AppCompatActivity
{
	public static final String TAG = PaymentActivity.class.getSimpleName();
	private final int MAKE_PAYMENT_CODE = 2;
	private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private BaseDialogFragment mDialog;
	private Double mAmount;
	private String mPurpose;
	private String mReceipt;

	@BindView(R.id.payment_status_message)
	TextView tvMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		Intent intent = getIntent();

		String number = intent.getStringExtra("number");
        setEditTextValue(R.id.payment_number, number);

        double amount = intent.getDoubleExtra("amount", 0);
        int intValue = (int)amount;
        int rest = (int) ((amount - intValue) * 100);
        String amountStr = String.format("%s.%s", intValue, rest);
        setEditTextValue(R.id.payment_amount, amountStr);

		String client = intent.getStringExtra("client");
        setEditTextValue(R.id.payment_client, client);
	}

	public  void onPayment(View view) {
		// view.setEnabled(false);
        String amount = getEditTextValue(R.id.payment_amount);
		makePayment("0.01" /*amount*/);
	}

	public void showProgress(final boolean show) {
		ProgressBar progressView = (ProgressBar) findViewById(R.id.progress);
		if (progressView != null) {
			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}

	void makePayment(String amount) {
		/*Intent i = new Intent("com.sccp.gpb.emv.MAKE_PAYMENT");
		i.putExtra("amount", amount);
		i.putExtra("source", getApplication().getPackageName());
        try {
			showProgress(true);
			startActivityForResult(i, MAKE_PAYMENT_CODE);
		} catch (android.content.ActivityNotFoundException e) {
			showProgress(false);
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
        findViewById(R.id.btn_pay).setEnabled(true);*/
		makePaymentPrivate(Double.parseDouble(amount), "test");
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
            // transaction_response_code = "000";
			boolean payed = transaction_response_code == "000" && resultCode == RESULT_OK;
			intent.putExtra("payed",  payed);
			if (resultCode == RESULT_OK) {
				String message = null;
				switch(transaction_response_code) {
					case "000":
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
						message = "Не известная ошибка";
						break;
				}
				setResult(resultCode, intent);
				if (message != null)
					Toast.makeText(this, message, Toast.LENGTH_LONG).show();
				// finish();
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
		// Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void showOkAnimation() {

    }

	private void log(String message)
	{
		Log.d("REST", message);
	}

	/*
		PRIVATE
	 */

	/**
	 * проверка пользовательских прав доступа
	 */
	private boolean checkUserPermission() {
		// Android M Permission check
		if (!isPermissionGranted()) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
					PERMISSION_REQUEST_COARSE_LOCATION);
			return false;
		} else
			return true;
	}

	private boolean isPermissionGranted() {
		return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
	}

	private void showPinpadDialog() {
		if (checkUserPermission()) {
			mDialog = new ChoicePinpadDialog().newInstance();
			mDialog.show(this);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_COARSE_LOCATION: {

				final Boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
				if (grantResults.length == 0) return;

				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG, "coarse location permission granted");
				} else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle(getString(R.string.functionality_limited));
					builder.setMessage(showRationale ?
							getString(R.string.functionality_message) + "\n" + getString(R.string.grant_permission) :
							getString(R.string.functionality_message) + "\n" + getString(R.string.go_settings));
					builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (showRationale) {
								ActivityCompat.requestPermissions((Activity) PaymentActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
										PERMISSION_REQUEST_COARSE_LOCATION);
							} else {
								Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", getPackageName(), null);
								intent.setData(uri);
								startActivityForResult(intent, 0);
							}
						}
					});
					builder.setNegativeButton(getString(R.string.btnCancelText), null);
					builder.show();
				}
				return;
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		MiniPosManager.getInstance().pinpadSubscribe();
	}

	@Override
	protected void onStop() {
		super.onStop();
		MiniPosManager.getInstance().pinpadUnsubscribe();
	}

	private void makePaymentPrivate(Double amount, String purpose) {
		mAmount = amount;
		mPurpose = purpose;
		mReceipt = "";
		MiniPosManager.getInstance().initPinpad(pinpadConnectionListener);
	}

	/**
	 * PinPad ConnectionListener
	 */
	private MiniPosConnectionListener pinpadConnectionListener = new MiniPosConnectionListener() {
		@Override
		public void onConnectionSuccess(String sn) {
			showProgress(true);
			MiniPosManager.getInstance().startTransaction(mAmount, mPurpose, transactionListener);
		}

		@Override
		public void onConnectionFailed() {
			showProgress(false);
			showPinpadDialog();
		}

		@Override
		public void onDeviceRelease() {
			showProgress(false);
			String msg = getString(R.string.pinpad_released);
			Log.d(TAG, msg);
			tvMessage.setText(msg);
		}

		@Override
		public void onBluetoothRequest() {
			//запрос на включение блютуза
			Toast.makeText(PaymentActivity.this, R.string.turn_on_bluetooth, Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * Transaction listener
	 */
	private MiniPosTransactionListener transactionListener = new MiniPosTransactionListener() {

		@Override
		public void onExeption(String message) {
			Log.d(TAG, "onExeption messsage:" + message);
			showProgress(false);
		}

		@Override
		public void onUpdateUserInterface(String message) {
			tvMessage.setText("onUpdateUserInterface:" + message);
		}


		@Override
		public void onTransactionFinish(Transaction transaction) {
			Log.d(TAG, "onTransactionFinish");
			showProgress(false);
			mReceipt = transaction.getTransactionData().getReceipt(); //получаем чек
		}
	};

	/**
	 * Service Error Listener
	 */
	private MiniPosServiceErrorListener miniPosServiceErrorListener = new MiniPosServiceErrorListener() {
		@Override
		public void onError(String message) {
			Log.d(TAG, "ERROR:" + message);
			tvMessage.setText(message);
			showProgress(false);
		}

		@Override
		public void onUnauthorized() {
			Log.d(TAG, "ERROR: необходима авторизация");
			tvMessage.setText("Необходима авторизация");
			showProgress(false);
		}
	};
}
