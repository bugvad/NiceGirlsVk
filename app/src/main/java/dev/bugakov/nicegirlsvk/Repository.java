package dev.bugakov.nicegirlsvk;

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

import java.util.ArrayList;

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

    public static VKRequest generateIdsRequest(int page_size, int key, int state) {

        int offsetValue = -2;

        if (state == -1 || state == 1)
        {
            offsetValue = page_size * (key - 1);
        }
        else if (state == 0)
        {
            offsetValue = 5;
        }

        VKRequest request = VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                "age_from", "17", "age_to", "18", "count", offsetValue)));
        return request;
    }

    static ArrayList<Integer> parseIdsJson(VKResponse response) {
        final ArrayList<Integer> mCatNames = new ArrayList<>();
        JSONObject json = response.json;
        try {
            JSONObject jsonObject1 = json.getJSONObject("response");
            JSONArray jsonObject2 = jsonObject1.getJSONArray("items");
            for (int i = 0; i < jsonObject2.length(); i++) {
                mCatNames.add((jsonObject2.getJSONObject(i).getInt("id")));
                Log.i("bs: id: " + i, mCatNames.get(i) + "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("bs: ", "fail");
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

    static ArrayList<ItemQuestion> parseUrlJson(VKResponse response) {
        ArrayList<ItemQuestion> finalItem = null;
        JSONObject json = response.json;

        try {
            JSONArray jsonObject2 = json.getJSONArray("response");

            for (int i = 0; i < jsonObject2.length(); i++) {
                JSONObject jsonObject3 = jsonObject2.getJSONObject(i);
                String jsonObject4 = jsonObject3
                        .getJSONObject("crop_photo")
                        .getJSONObject("photo")
                        .getString("photo_1280");
                finalItem.add(new ItemQuestion(jsonObject4));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return finalItem;
    }


    static Observable<VKRequest> makeIdObservable(VKRequest request) {
        Observable<VKRequest> observableLocal =
                Observable.create(subscriber -> request
                        .executeWithListener(new VKRequest.VKRequestListener() {

                            @Override
                            public void onComplete(VKResponse response) {
                                //Do complete stuff
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
                                //I don't really believe in progress
                                // subscriber.onError(new Exception());
                            }
                        }));
        return observableLocal;
    }




    static Observable<ArrayList<ItemQuestion>> makeUrlObservable(VKRequest request)
    {
        Observable<ArrayList<ItemQuestion>> croco =
                Observable.create(subscriber -> request
                        .executeWithListener(new VKRequest.VKRequestListener() {

                            @Override
                            public void onComplete(VKResponse response) {

                                ArrayList<ItemQuestion> Urls = parseUrlJson(response);

                                subscriber.onNext(Urls);
                                subscriber.onComplete();
                            }

                            @Override
                            public void onError(VKError error) {
                                //subscriber.onError(error.getException());
                            }

                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                //I don't really believe in progress
                                // subscriber.onError(new Exception());
                            }
                        }));

        return croco;
    }

    public static Observable<ArrayList<ItemQuestion>> multiobservable(VKRequest request)
    {
        Observable<ArrayList<ItemQuestion>> observableLocal2 = makeIdObservable(request).flatMap(address -> {
            return makeUrlObservable(address);
        });
        return observableLocal2;
    }

    public static void finishFlow2(Observable<ArrayList<ItemQuestion>> observableLocal2,
                            PageKeyedDataSource.LoadInitialCallback<Integer, ItemQuestion> callback){

        io.reactivex.Observer<ArrayList<ItemQuestion>> observer =
                new io.reactivex.Observer<ArrayList<ItemQuestion>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ArrayList<ItemQuestion> items) {
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

    public static void finishFlow1(Observable<ArrayList<ItemQuestion>> observableLocal2,
                            PageKeyedDataSource.LoadCallback<Integer, ItemQuestion> callback, int key)
    {



                io.reactivex.Observer<ArrayList<ItemQuestion>> observer =
                        new io.reactivex.Observer<ArrayList<ItemQuestion>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(ArrayList<ItemQuestion> items) {
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


}
