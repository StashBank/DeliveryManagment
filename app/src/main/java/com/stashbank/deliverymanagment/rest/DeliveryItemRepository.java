package com.stashbank.deliverymanagment.rest;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.*;
import com.stashbank.deliverymanagment.models.*;
import retrofit2.http.*;
import okhttp3.OkHttpClient;

public class DeliveryItemRepository implements DeliveryItemApi
{
	final String API_URL = "https://crud-server.firebaseapp.com/";
	DeliveryItemApi createService() {
		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(API_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(httpClientBuilder.build())
			.build();
		DeliveryItemApi api = retrofit.create(DeliveryItemApi.class);
		return api;
	}

	@Override
	public Call<DeliveryItem> getItemById(String id)
	{
		DeliveryItemApi api = createService();
		Call<DeliveryItem> call = api.getItemById(id);
		return call;
	}
	
	@Override
	public Call<List<DeliveryItem>> getItems()
	{
		DeliveryItemApi api = createService();
		Call<List<DeliveryItem>> call = api.getItems();
		return call;
	}

	@Override
	public Call<DeliveryItem> setItem(String id, DeliveryItem item)
	{
		DeliveryItemApi api = createService();
		Call<DeliveryItem> call = api.setItem(id, item);
		return call;
	}

	@Override
	public Call<DeliveryItem> addItem(DeliveryItem item)
	{
		DeliveryItemApi api = createService();
		Call<DeliveryItem> call = api.addItem(item);
		return call;
	}
	
}
