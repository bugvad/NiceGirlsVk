package dev.bugakov.nicegirlsvk.network;

import android.arch.paging.PageKeyedDataSource;
import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import dev.bugakov.nicegirlsvk.model.Item;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class Repository {

    private static String concatenateIds(ArrayList<Integer> idArray) {
        StringBuilder sb = new StringBuilder();
        for (Integer s : idArray) {
            sb.append(s);
            sb.append(",");
        }
        String ans = sb.toString();
        return ans.substring(0, ans.length() - 1);
    }

    public static VKRequest generateIdsRequest(int page_size, int key, int state, int from, int to) {


        if (state == -1 || state == 1)
        {
            int offsetValue = page_size * (key - 1);
            return VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                    "age_from", from, "age_to", to, "count", 5, "offset", offsetValue)));
        }

        return VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                "age_from", from, "age_to", to, "count", 5)));

    }

    static ArrayList<Integer> parseIdsJson(VKResponse response) {
        final ArrayList<Integer> mCatNames = new ArrayList<>();
        JSONObject json = response.json;
        try {
            JSONObject jsonObject1 = json.getJSONObject("response");
            JSONArray jsonObject2 = jsonObject1.getJSONArray("items");
            for (int i = 0; i < jsonObject2.length(); i++) {
                mCatNames.add((jsonObject2.getJSONObject(i).getInt("id")));
            }
        } catch (JSONException e) {
            //do nothing
        }
        return mCatNames;
    }

    static VKRequest generateUrlRequest(ArrayList<Integer> idArray) {

        String idsString = concatenateIds(idArray);

        VKRequest request = VKApi
                .users()
                .get((VKParameters.from(
                        VKApiConst.USER_IDS, idsString,
                        "fields", "crop_photo")));

        return request;
    }

    static ArrayList<Item> parseUrlJson(VKResponse response) {
        final ArrayList<Item> finalItem = new ArrayList<>();
        JSONObject json = response.json;

        try {
            JSONArray jsonObject2 = json.getJSONArray("response");
            for (int i = 0; i < jsonObject2.length(); i++) {
                JSONObject jsonObject3 = jsonObject2.getJSONObject(i);
                try
                {
                    String jsonObject4 = jsonObject3
                            .getJSONObject("crop_photo")
                            .getJSONObject("photo")
                            .getString("photo_1280");

                    finalItem.add(new Item(jsonObject4));
                }
                catch (org.json.JSONException e)
                {
                    //do nothing
                }
            }

        } catch (JSONException e) {
            //do nothing
        }

        return finalItem;
    }


    static Observable<VKRequest> makeIdObservable(VKRequest request) {
        Observable<VKRequest> observableLocal =
                Observable.create(subscriber -> request
                        .executeWithListener(new VKRequest.VKRequestListener() {

                            @Override
                            public void onComplete(VKResponse response) {
                                ArrayList<Integer> IdsJson = parseIdsJson(response);
                                VKRequest UrlRequest = generateUrlRequest(IdsJson);

                                subscriber.onNext(UrlRequest);
                                subscriber.onComplete();
                            }

                            @Override
                            public void onError(VKError error) {
                                //subscriber.onError(error.getException());
                            }

                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                // subscriber.onError(new Exception());
                            }
                        }));
        return observableLocal;
    }




    static Observable<ArrayList<Item>> makeUrlObservable(VKRequest request)
    {
        Observable<ArrayList<Item>> croco =
                Observable.create(subscriber -> request
                        .executeWithListener(new VKRequest.VKRequestListener() {

                            @Override
                            public void onComplete(VKResponse response) {

                                ArrayList<Item> Urls = parseUrlJson(response);

                                subscriber.onNext(Urls);
                                subscriber.onComplete();
                            }

                            @Override
                            public void onError(VKError error) {
                                //subscriber.onError(error.getException());
                            }

                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                // subscriber.onError(new Exception());
                            }
                        }));

        return croco;
    }

    public static Observable<ArrayList<Item>> multiobservable(VKRequest request)
    {
        Observable<ArrayList<Item>> observableLocal2 = makeIdObservable(request).flatMap(address -> {
            return makeUrlObservable(address);
        });
        return observableLocal2;
    }

    public static void finishFlow2(Observable<ArrayList<Item>> observableLocal2,
                            PageKeyedDataSource.LoadInitialCallback<Integer, Item> callback){

        io.reactivex.Observer<ArrayList<Item>> observer =
                new io.reactivex.Observer<ArrayList<Item>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ArrayList<Item> items) {
                callback.onResult(items, null, 2);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observableLocal2.subscribe(observer);
    }

    public static void finishFlow1(Observable<ArrayList<Item>> observableLocal2,
                            PageKeyedDataSource.LoadCallback<Integer, Item> callback, int key)
    {



                io.reactivex.Observer<ArrayList<Item>> observer =
                        new io.reactivex.Observer<ArrayList<Item>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(ArrayList<Item> items) {
                    final Integer adjacentKey = (key > 1) ? key - 1 : null;
                    callback.onResult(items, adjacentKey);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            };
            observableLocal2.subscribe(observer);
    }


    public static void finishFlow3(Observable<ArrayList<Item>> observableLocal2,
                                   PageKeyedDataSource.LoadCallback<Integer, Item> callback, int key)
    {
        io.reactivex.Observer<ArrayList<Item>> observer =
                new io.reactivex.Observer<ArrayList<Item>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Item> items) {
                        final Integer adjacentKey = key + 1;
                        callback.onResult(items, adjacentKey);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
        observableLocal2.subscribe(observer);
    }


}
