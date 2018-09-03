package com.stashbank.deliveryManagement.rest;

import java.util.List;
import retrofit2.*;
import retrofit2.http.*;
import com.stashbank.deliveryManagement.models.*;

public interface DeliveryItemApi {

    @GET("delivery_manegment.delivery")
    @Headers("Cache-Control: max-age=640000")
	Call<List<DeliveryItem>> getItems();

	@GET("delivery_manegment.delivery/{id}")
	Call<DeliveryItem> getItemById(@Path("id") String id);

	@PUT("delivery_manegment.delivery/{id}")
	Call<DeliveryItem> setItem(@Path("id") String id, @Body DeliveryItem item);

	@POST("delivery_manegment.delivery")
	Call<DeliveryItem> addItem(@Body DeliveryItem item);
}