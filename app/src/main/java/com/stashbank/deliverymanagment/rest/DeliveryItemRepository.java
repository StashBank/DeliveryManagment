package com.stashbank.deliverymanagment.rest;

import retrofit2.*;
import retrofit2.converter.gson.*;
import java.util.*;
import com.stashbank.deliverymanagment.models.*;

public class DeliveryItemRepository implements DeliveryItemApi
{
	Retrofit retrofit;
	DeliveryItemApi api;
	
	public DeliveryItemRepository() {
		retrofit = new Retrofit.Builder()
			.baseUrl("https://crud-server.firebaseapp.com/")
			.addConverterFactory(GsonConverterFactory.create())
			.build();
		api = retrofit.create(DeliveryItemApi.class);
		
	}

	@Override
	public Call<DeliveryItem> getItemById(String id)
	{
		Call<DeliveryItem> items = api.getItemById(id);
		return items;
	}
	
	@Override
	public Call<List<DeliveryItem>> getItems()
	{
		Call<List<DeliveryItem>> items = api.getItems();
		return items;
	}

	@Override
	public Call<DeliveryItem> setItem(String id, DeliveryItem item)
	{
		return api.setItem(id, item);
	}
	
	@Override
	public Call<DeliveryItem> markPayment(String id, boolean payed) {
		return api.markPayment(id, payed);
	}
	
}
