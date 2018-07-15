package com.stashbank.deliveryManagement.rest;

import com.stashbank.deliveryManagement.models.ReceivingItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReceivingItemApi {
    @GET("delivery_manegment.receiving")
    Call<List<ReceivingItem>> getItems();

    @GET("delivery_manegment.receiving/{id}")
    Call<ReceivingItem> getItemById(@Path("id") String id);

    @PUT("delivery_manegment.receiving/{id}")
    Call<ReceivingItem> setItem(@Path("id") String id, @Body ReceivingItem item);

    @POST("delivery_manegment.receiving")
    Call<ReceivingItem> addItem(@Body ReceivingItem item);
}
