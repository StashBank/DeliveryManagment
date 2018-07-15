package com.stashbank.deliveryManagement.rest;

import android.os.AsyncTask;
import com.stashbank.deliveryManagement.models.ReceivingItem;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceivingItemRepository {
    public interface Predicate<T, E> {
        void response(T response, E error);
    }

    final static String API_URL = "https://crud-server.firebaseapp.com/";

    private static ReceivingItemApi createService() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        ReceivingItemApi api = retrofit.create(ReceivingItemApi.class);
        return api;
    }

    public ReceivingItemRepository.ReceivingItemTask getItemById(String id, ReceivingItemRepository.Predicate<ReceivingItem, Exception> predicate) {
        ReceivingItemApi api = createService();
        Call<ReceivingItem> call = api.getItemById(id);
        ReceivingItemRepository.ReceivingItemTask task = new ReceivingItemRepository.ReceivingItemTask(predicate, call);
        return task;
    }

    public ReceivingItemRepository.ReceivingItemsTask getItems(ReceivingItemRepository.Predicate<List<ReceivingItem>, Exception> predicate) {
        ReceivingItemApi api = createService();
        Call<List<ReceivingItem>> call = api.getItems();
        ReceivingItemRepository.ReceivingItemsTask task = new ReceivingItemRepository.ReceivingItemsTask(predicate, call);
        return task;
    }

    public ReceivingItemRepository.ReceivingItemsCountTask getItemsCount(ReceivingItemRepository.Predicate<Integer, Exception> predicate) {
        ReceivingItemApi api = createService();
        Call<List<ReceivingItem>> call = api.getItems();
        ReceivingItemRepository.ReceivingItemsCountTask task = new ReceivingItemRepository.ReceivingItemsCountTask(predicate, call);
        return task;
    }

    public ReceivingItemRepository.ReceivingItemTask setItem(
            String id, ReceivingItem item, ReceivingItemRepository.Predicate<ReceivingItem, Exception> predicate
    ) {
        ReceivingItemApi api = createService();
        Call<ReceivingItem> call = api.setItem(id, item);
        ReceivingItemRepository.ReceivingItemTask task = new ReceivingItemRepository.ReceivingItemTask(predicate, call);
        return task;
    }

    public ReceivingItemRepository.ReceivingItemTask addItem(ReceivingItem item, ReceivingItemRepository.Predicate<ReceivingItem, Exception> predicate) {
        ReceivingItemApi api = createService();
        Call<ReceivingItem> call = api.addItem(item);
        ReceivingItemRepository.ReceivingItemTask task = new ReceivingItemRepository.ReceivingItemTask(predicate, call);
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
