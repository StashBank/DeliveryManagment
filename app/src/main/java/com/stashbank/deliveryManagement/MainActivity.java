package com.stashbank.deliveryManagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stashbank.deliveryManagement.models.DeliveryItem;
import com.stashbank.deliveryManagement.models.ReceivingItem;
import com.stashbank.deliveryManagement.rest.DeliveryItemRepository;
import com.stashbank.deliveryManagement.rest.ReceivingItemRepository;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener,
		MainFragment.OnButtonClickListener,
	    DeliveryFragment.DeliveryFragmentEventListener,
        ReceivingFragment.ReceivingFragmentEventListener
{

	private DrawerLayout drawer;
	private Toolbar toolbar;
	private NavigationView navigationView;
	private DeliveryItem selectedDeliveryItem;
	private MainFragment mainFragment;
	private DeliveryFragment deliveryFragment;
	private ReceivingFragment receivingFragment;
	private final int PAYMENT_REQ_CODE = 1;
	private FloatingActionButton fabNew, fabNewReceiving, fabNewDelivery;
	private Animation animOpen, animClose, animRotateClockwise, animRotateAnticlockwise;
	private int selectedMenuId = R.id.nav_home;
	private boolean isFabsOpen = false;
	private boolean doubleBackToExitPressedOnce;

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

		initFabs();
		if (savedInstanceState == null) {
			openMainFragment();
		}
	}

    @Override
	protected void onResume() {
	    super.onResume();
	    if (selectedMenuId == R.id.nav_home && mainFragment != null) {
            mainFragment.updateInfoData();
        }
    }

	private  void initFabs() {
		android.content.Context ctx = getApplicationContext();
		animOpen = AnimationUtils.loadAnimation(ctx, R.anim.fab_open);
		animClose = AnimationUtils.loadAnimation(ctx, R.anim.fab_close);
		animRotateClockwise = AnimationUtils.loadAnimation(ctx, R.anim.rotate_clockwise);
		animRotateAnticlockwise = AnimationUtils.loadAnimation(ctx, R.anim.rotate_anticlockwise);

		fabNew = (FloatingActionButton) findViewById(R.id.fab_new);
		fabNewReceiving = (FloatingActionButton) findViewById(R.id.fab_new_receive);
		fabNewDelivery = (FloatingActionButton) findViewById(R.id.fab_new_delivery);
		fabNew.setOnClickListener(v -> {
            if (selectedMenuId == R.id.nav_home) {
                if (isFabsOpen)
                    hideFabs();
                else
                    showFabs();
            } else if (selectedMenuId == R.id.nav_delivery) {
                openCreateDeliveryCard();
            } else if (selectedMenuId == R.id.nav_shipping) {
                openCreateReceivingCard();
            }
        });
		fabNewReceiving.setOnClickListener(v -> {
            openCreateReceivingCard();
            hideFabs();
        });
		fabNewDelivery.setOnClickListener(v -> {
            openCreateDeliveryCard();
            hideFabs();
        });
	}

	private void showFabs() {
		fabNewReceiving.startAnimation(animOpen);
		fabNewDelivery.startAnimation(animOpen);
		fabNew.startAnimation(animRotateClockwise);
		fabNewReceiving.setClickable(true);
		fabNewDelivery.setClickable(true);
		isFabsOpen = true;
	}

	private void hideFabs() {
		if (isFabsOpen) {
			fabNewReceiving.startAnimation(animClose);
			fabNewDelivery.startAnimation(animClose);
			fabNew.startAnimation(animRotateAnticlockwise);
			fabNewReceiving.setClickable(false);
			fabNewDelivery.setClickable(false);
			isFabsOpen = false;
		}
	}

	private void openCreateDeliveryCard() {
		Intent intent = new Intent(this, EditDeliveryItemActivity.class);
		startActivity(intent);
	}

	private void openCreateReceivingCard() {
		Intent intent = new Intent(this, EditReceivingItemActivity.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else if (selectedMenuId == R.id.nav_home) {
			onExit();
		} else {
			openMainFragment();
		}
	}

	private void onExit() {
		long duration = 2000;
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, duration);
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
		selectedMenuId = menuItem.getItemId();
		switch (selectedMenuId) {
			case R.id.nav_home:
				openMainFragment();
				break;
			case R.id.nav_delivery:
				openDeliveryFragment();
				break;
			case R.id.nav_shipping:
				openReceivingFragment();
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
		hideFabs();
		selectedMenuId = R.id.nav_home;
        mainFragment = new MainFragment();
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, mainFragment)
			.commit();
		navigationView.setCheckedItem(R.id.nav_home);

	}

	private void openDeliveryFragment() {
		hideFabs();
		selectedMenuId = R.id.nav_delivery;
		deliveryFragment = new DeliveryFragment();
		deliveryFragment.setEventListener(this);
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, deliveryFragment)
			.commit();
		navigationView.setCheckedItem(R.id.nav_delivery);
	}

	private void openReceivingFragment() {
		hideFabs();
		selectedMenuId = R.id.nav_shipping;
        receivingFragment = new ReceivingFragment();
        receivingFragment.setEventListener(this);
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, receivingFragment)
			.commit();
		navigationView.setCheckedItem(R.id.nav_shipping);
	}

	@Override
	public void onDeliveryButtonClick(View view) {
		openDeliveryFragment();
	}

	@Override
	public void onReceivingButtonClick(View view) {
		openReceivingFragment();
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
    public void markAsReceived(ReceivingItem receiving) {
        receiving.setReceived(true);
        setReceivingItem(receiving);
    }

    @Override
	public void overridePendingTransition(int enterAnim, int exitAnim) {
		// TODO: Implement this method
		super.overridePendingTransition(enterAnim, exitAnim);
	}

	@Override
	public void makePayment(DeliveryItem delivery) {
		selectedDeliveryItem = delivery;
		Intent intent = new Intent(this, PaymentActivity.class);
		intent.putExtra("number", delivery.getNumber());
		intent.putExtra("amount", delivery.getAmount());
		intent.putExtra("client", delivery.getClient());
		intent.putExtra("deliveryId", delivery.getId());
		startActivityForResult(intent, PAYMENT_REQ_CODE);
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
		boolean payed = data.getBooleanExtra("payed", false);
		if (resultCode == AppCompatActivity.RESULT_OK && payed) {
			markAsPayed(selectedDeliveryItem);
			// Toast.makeText(this, "Payed", Toast.LENGTH_LONG).show();
		}
	}

	void markAsPayed(DeliveryItem item) {
		item.setPayed(true);
		setDeliveryItem(item);
	}

	public void makePhoneCall(String number) {
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel:" + number));
		startActivity(callIntent);
	}

	@Override
	public void markAsDelivered(DeliveryItem item) {
		item.setDelivered(true);
		setDeliveryItem(item);
	}

	private void setDeliveryItem(DeliveryItem item) {
        DeliveryItemRepository.Predicate<DeliveryItem, Exception> p = (i, err) -> {
            showProgress(false);
            if (err != null) {
                log("ERROR " + err);
                // itemAdapter.onFetchDataFailure();
            } else {
                deliveryFragment.fetchData();
            }
        };
		DeliveryItemRepository repository = new DeliveryItemRepository();
        DeliveryItemRepository.DeliveryItemTask task = repository.setItem(item.getId(), item, p, this);
		showProgress(true);
		task.execute();
	}

    private void setReceivingItem(ReceivingItem item) {
        ReceivingItemRepository.Predicate<ReceivingItem, Exception> p = (i, err) -> {
            showProgress(false);
            if (err != null) {
                log("ERROR " + err);
                // itemAdapter.onFetchDataFailure();
            } else {
                receivingFragment.fetchData();
            }
        };
        ReceivingItemRepository repository = new ReceivingItemRepository();
        ReceivingItemRepository.ReceivingItemTask task = repository.setItem(item.getId(), item, p);
        showProgress(true);
        task.execute();
    }

	private void log(String message)
	{
		Log.d("REST", message);
	}

}
