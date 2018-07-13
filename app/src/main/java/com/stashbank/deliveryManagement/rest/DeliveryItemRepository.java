package com.stashbank.deliveryManagement.rest;

import android.os.AsyncTask;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.*;
import com.stashbank.deliveryManagement.models.*;
import okhttp3.OkHttpClient;

public class DeliveryItemRepository
{
    public interface Predicate<T, E> {
        void response(T response, E error);
    }

	final static String API_URL = "https://crud-server.firebaseapp.com/";

	private static DeliveryItemApi createService() {
		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(API_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(httpClientBuilder.build())
			.build();
		DeliveryItemApi api = retrofit.create(DeliveryItemApi.class);
		return api;
	}

	public DeliveryItemTask getItemById(String id, Predicate<DeliveryItem, Exception> predicate) {
		DeliveryItemApi api = createService();
		Call<DeliveryItem> call = api.getItemById(id);
        DeliveryItemTask task = new DeliveryItemTask(predicate, call);
		return task;
	}

	public DeliveryItemsTask getItems(Predicate<List<DeliveryItem>, Exception> predicate) {
        DeliveryItemApi api = createService();
        Call<List<DeliveryItem>> call = api.getItems();
        DeliveryItemsTask task = new DeliveryItemsTask(predicate, call);
        return task;
	}

	public DeliveryItemTask setItem(
	        String id, DeliveryItem item, Predicate<DeliveryItem, Exception> predicate
    ) {
		DeliveryItemApi api = createService();
		Call<DeliveryItem> call = api.setItem(id, item);
        DeliveryItemTask task = new DeliveryItemTask(predicate, call);
		return task;
	}

	public DeliveryItemTask addItem(DeliveryItem item, Predicate<DeliveryItem, Exception> predicate) {
		DeliveryItemApi api = createService();
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
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<DeliveryItem> result) {
            super.onPostExecute(result);
            predicate.response(result, error);
        }
    }
	
}
