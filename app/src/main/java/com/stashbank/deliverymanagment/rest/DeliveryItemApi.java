package com.stashbank.deliverymanagment.rest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import com.stashbank.deliverymanagment.models.*;

public interface DeliveryItemApi
{
	@GET("delivery_manegment.delivery")
	Call<List<DeliveryItem>> getItems();
}
