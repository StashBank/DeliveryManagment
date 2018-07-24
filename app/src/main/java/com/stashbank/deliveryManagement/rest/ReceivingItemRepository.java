package com.stashbank.deliveryManagement.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.stashbank.deliveryManagement.MainActivity;
import com.stashbank.deliveryManagement.models.ReceivingItem;

import java.io.File;
import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceivingItemRepository extends BaseRepository {

    public ReceivingItemTask getItemById(
            String id, Predicate<ReceivingItem, Exception> predicate, Context ctx, boolean force
    ) {
        ReceivingItemApi api = createService(ReceivingItemApi.class, ctx, force);
        Call<ReceivingItem> call = api.getItemById(id);
        ReceivingItemTask task = new ReceivingItemTask(predicate, call);
        return task;
    }

    public ReceivingItemsTask getItems(
            Predicate<List<ReceivingItem>, Exception> predicate, Context ctx, boolean force
    ) {
        ReceivingItemApi api = createService(ReceivingItemApi.class,ctx, force);
        Call<List<ReceivingItem>> call = api.getItems();
        ReceivingItemsTask task = new ReceivingItemsTask(predicate, call);
        return task;
    }

    public ReceivingItemRepository.ReceivingItemsCountTask getItemsCount(
            Predicate<Integer, Exception> predicate, Context ctx, boolean force
    ) {
        ReceivingItemApi api = createService(ReceivingItemApi.class,ctx, force);
        Call<List<ReceivingItem>> call = api.getItems();
        ReceivingItemsCountTask task = new ReceivingItemsCountTask(predicate, call);
        return task;
    }

    public ReceivingItemRepository.ReceivingItemTask setItem(
            String id, ReceivingItem item, Predicate<ReceivingItem, Exception> predicate, Context ctx, boolean force
    ) {
        ReceivingItemApi api = createService(ReceivingItemApi.class,ctx, force);
        Call<ReceivingItem> call = api.setItem(id, item);
        ReceivingItemTask task = new ReceivingItemTask(predicate, call);
        return task;
    }

    public ReceivingItemTask addItem(
            ReceivingItem item, Predicate<ReceivingItem, Exception> predicate, Context ctx, boolean force
    ) {
        ReceivingItemApi api = createService(ReceivingItemApi.class,ctx, force);
        Call<ReceivingItem> call = api.addItem(item);
        ReceivingItemTask task = new ReceivingItemTask(predicate, call);
        return task;
    }

    public class ReceivingItemTask extends AsyncTask<Void,Void,ReceivingItem> {

        private ReceivingItemRepository.Predicate<ReceivingItem, Exception> predicate;
        private Call<ReceivingItem> call;
        private Exception error;

        public ReceivingItemTask(
                ReceivingItemRepository.Predicate<ReceivingItem, Exception> predicate,
                Call<ReceivingItem> call
        ) {
            this.predicate = predicate;
            this.call = call;
        }

        @Override
        protected ReceivingItem doInBackground(Void... params) {
            try {
                ReceivingItem items = call.execute().body();
                return items;
            } catch (Exception e) {
                error = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(ReceivingItem result) {
            super.onPostExecute(result);
            predicate.response(result, error);
        }
    }

    public class ReceivingItemsTask extends AsyncTask<Void,Void,List<ReceivingItem>> {

        private ReceivingItemRepository.Predicate<List<ReceivingItem>, Exception> predicate;
        private Call<List<ReceivingItem>> call;
        private Exception error;

        public ReceivingItemsTask(
                ReceivingItemRepository.Predicate<List<ReceivingItem>, Exception> predicate,
                Call<List<ReceivingItem>> call
        ) {
            this.predicate = predicate;
            this.call = call;
        }

        @Override
        protected List<ReceivingItem> doInBackground(Void... params) {
            try {
                List<ReceivingItem> items = call.execute().body();
                return items;
            } catch (Exception e) {
                error = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ReceivingItem> result) {
            super.onPostExecute(result);
            predicate.response(result, error);
        }
    }

    public class ReceivingItemsCountTask extends AsyncTask<Void,Void,Integer> {

        private ReceivingItemRepository.Predicate<Integer, Exception> predicate;
        private Call<List<ReceivingItem>> call;
        private Exception error;

        public ReceivingItemsCountTask(
                ReceivingItemRepository.Predicate<Integer, Exception> predicate,
                Call<List<ReceivingItem>> call
        ) {
            this.predicate = predicate;
            this.call = call;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                List<ReceivingItem> items = call.execute().body();
                if (items == null)
                    return 0;
                Integer count = 0;
                for (ReceivingItem i: items) {
                    if (i == null)
                        throw new NullPointerException("Receiving item is null");
                    if (!i.isReceived())
                        count++;
                }
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
