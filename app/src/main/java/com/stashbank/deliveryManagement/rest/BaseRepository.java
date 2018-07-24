package com.stashbank.deliveryManagement.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseRepository<T> {
    public interface Predicate<T, E> {
        void response(T response, E error);
    }

    final static String API_URL = "https://crud-server.firebaseapp.com/";


    protected static final Interceptor getRewriteCacheControlInterceptor(Context context, boolean force) {
        return chain -> {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            okhttp3.Response.Builder builder = originalResponse.newBuilder();
            if (!isNetworkAvailable(context)) {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                builder = originalResponse.newBuilder()
                    .header("Cache-Control", "only-if-cached, max-stale=" + maxStale);
            } else if (force) {
                builder = originalResponse.newBuilder()
                        .header("Cache-Control", "no-cache, no-store, must-revalidate");
            } else {
                int maxAge = 60; // read from cache for 1 minute
                builder = originalResponse.newBuilder()
                        .header("Cache-Control", "max-age=" + maxAge);
            }
            return builder.build();
        };
    }

    protected static  boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected static <T> T createService(Class<T> serviceClass, Context context, boolean force) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        Interceptor interceptor = getRewriteCacheControlInterceptor(context, force);
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        httpClientBuilder = httpClientBuilder.cache(cache);
        OkHttpClient client = httpClientBuilder
                .addNetworkInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        T api = retrofit.create(serviceClass);
        return api;
    }
}
