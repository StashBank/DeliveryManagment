package com.stashbank.deliveryManagement.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.*;

import com.stashbank.deliveryManagement.models.*;
import okhttp3.OkHttpClient;
import okhttp3.Cache;

public class DeliveryItemRepository
{
    public interface Predicate<T, E> {
        void response(T response, E error);
    }

	final static String API_URL = "https://crud-server.firebaseapp.com/";

	private static DeliveryItemApi createService(Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(API_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(httpClientBuilder
                    .cache(cache)
                    .build()
            )
			.build();
		DeliveryItemApi api = retrofit.create(DeliveryItemApi.class);
		return api;
	}

    public DeliveryItemTask getItemById(String id, Predicate<DeliveryItem, Exception> predicate, Context ctx) {
		DeliveryItemApi api = createService(ctx);
		Call<DeliveryItem> call = api.getItemById(id);
        DeliveryItemTask task = new DeliveryItemTask(predicate, call);
		return task;
	}

	public DeliveryItemsTask getItems(Predicate<List<DeliveryItem>, Exception> predicate, Context ctx) {
        DeliveryItemApi api = createService(ctx);
        Call<List<DeliveryItem>> call = api.getItems();
        DeliveryItemsTask task = new DeliveryItemsTask(predicate, call);
        return task;
	}

    public DeliveryItemsCountTask getItemsCount(Predicate<Integer, Exception> predicate, Context ctx) {
        DeliveryItemApi api = createService(ctx);
        Call<List<DeliveryItem>> call = api.getItems();
        DeliveryItemsCountTask task = new DeliveryItemsCountTask(predicate, call);
        return task;
    }

	public DeliveryItemTask setItem(
	        String id, DeliveryItem item, Predicate<DeliveryItem, Exception> predicate, Context ctx
    ) {
		DeliveryItemApi api = createService(ctx);
		Call<DeliveryItem> call = api.setItem(id, item);
        DeliveryItemTask task = new DeliveryItemTask(predicate, call);
		return task;
	}

	public DeliveryItemTask addItem(DeliveryItem item, Predicate<DeliveryItem, Exception> predicate, Context ctx) {
		DeliveryItemApi api = createService(ctx);
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
                List<DeliveryItem> items = call.execute().body();
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
