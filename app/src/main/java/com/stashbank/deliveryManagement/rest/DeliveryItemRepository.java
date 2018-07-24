package com.stashbank.deliveryManagement.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import okhttp3.Interceptor;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.*;

import com.stashbank.deliveryManagement.MainActivity;
import com.stashbank.deliveryManagement.models.*;
import okhttp3.OkHttpClient;
import okhttp3.Cache;

public class DeliveryItemRepository extends BaseRepository {

    public DeliveryItemTask getItemById(
            String id, Predicate<DeliveryItem, Exception> predicate, Context ctx, boolean force
    ) {
		DeliveryItemApi api = createService(DeliveryItemApi.class, ctx, force);
		Call<DeliveryItem> call = api.getItemById(id);
        DeliveryItemTask task = new DeliveryItemTask(predicate, call);
		return task;
	}

	public DeliveryItemsTask getItems(
	        Predicate<List<DeliveryItem>, Exception> predicate, Context ctx, boolean force
    ) {
        DeliveryItemApi api = createService(DeliveryItemApi.class,ctx, force);
        Call<List<DeliveryItem>> call = api.getItems();
        DeliveryItemsTask task = new DeliveryItemsTask(predicate, call);
        return task;
	}

    public DeliveryItemsCountTask getItemsCount(
            Predicate<Integer, Exception> predicate, Context ctx, boolean force
    ) {
        DeliveryItemApi api = createService(DeliveryItemApi.class,ctx, force);
        Call<List<DeliveryItem>> call = api.getItems();
        DeliveryItemsCountTask task = new DeliveryItemsCountTask(predicate, call);
        return task;
    }

	public DeliveryItemTask setItem(
	        String id, DeliveryItem item, Predicate<DeliveryItem, Exception> predicate, Context ctx, boolean force
    ) {
		DeliveryItemApi api = createService(DeliveryItemApi.class,ctx, force);
		Call<DeliveryItem> call = api.setItem(id, item);
        DeliveryItemTask task = new DeliveryItemTask(predicate, call);
		return task;
	}

	public DeliveryItemTask addItem(
	        DeliveryItem item, Predicate<DeliveryItem, Exception> predicate, Context ctx, boolean force
    ) {
		DeliveryItemApi api = createService(DeliveryItemApi.class,ctx, force);
		Call<DeliveryItem> call = api.addItem(item);
        DeliveryItemTask task = new DeliveryItemTask(predicate, call);
        return task;
	}

    public class DeliveryItemTask extends AsyncTask<Void,Void,DeliveryItem> {

        private Predicate<DeliveryItem, Exception> predicate;
        private Call<DeliveryItem> call;
        private Exception error;

        public DeliveryItemTask(
                Predicate<DeliveryItem, Exception> predicate,
                Call<DeliveryItem> call
        ) {
            this.predicate = predicate;
            this.call = call;
        }

        @Override
        protected DeliveryItem doInBackground(Void... params) {
            try {
                DeliveryItem items = call.execute().body();
                return items;
            } catch (Exception e) {
                error = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(DeliveryItem result) {
            super.onPostExecute(result);
            predicate.response(result, error);
        }
    }

    public class DeliveryItemsTask extends AsyncTask<Void,Void,List<DeliveryItem>> {

        private Predicate<List<DeliveryItem>, Exception> predicate;
        private Call<List<DeliveryItem>> call;
	    private Exception error;

	    public DeliveryItemsTask(
	            Predicate<List<DeliveryItem>, Exception> predicate,
                Call<List<DeliveryItem>> call
        ) {
	        this.predicate = predicate;
	        this.call = call;
        }

        @Override
        protected List<DeliveryItem> doInBackground(Void... params) {
            try {
                Response response = call.execute();
                Log.d("RESPONSE", response.headers().toString());
                List<DeliveryItem> items = (List<DeliveryItem>)response.body();
                return items;
            } catch (Exception e) {
                error = e;
                Log.d("ERROR", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<DeliveryItem> result) {
            super.onPostExecute(result);
            predicate.response(result, error);
        }
    }

    public class DeliveryItemsCountTask extends AsyncTask<Void,Void,Integer> {

        private Predicate<Integer, Exception> predicate;
        private Call<List<DeliveryItem>> call;
        private Exception error;

        public DeliveryItemsCountTask(
                Predicate<Integer, Exception> predicate,
                Call<List<DeliveryItem>> call
        ) {
            this.predicate = predicate;
            this.call = call;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                List<DeliveryItem> items = call.execute().body();
                if (items == null)
                    return 0;
                Integer count = 0;
                for (DeliveryItem i: items)
                    if(!i.isDelivered())
                        count++;
                return count;
            } catch (Exception e) {
                error = e;
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            predicate.response(result, error);
        }
    }
	
}
