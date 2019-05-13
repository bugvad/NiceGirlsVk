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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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


        if (state == -1 || state == 1)
        {
            int offsetValue = page_size * (key - 1);
            return VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                    "age_from", "17", "age_to", "18", "count", 5, "offset", offsetValue)));
        }

        return VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                "age_from", "17", "age_to", "18", "count", 5)));

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
        final ArrayList<ItemQuestion> finalItem = new ArrayList<>();
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

                    Log.i("bs: url: " + i, jsonObject4);
                    finalItem.add(new ItemQuestion(jsonObject4));
                }
                catch (org.json.JSONException e)
                {
                    try {
                        String jsonObject4 = jsonObject3
                                .getJSONObject("crop_photo")
                                .getJSONObject("photo")
                                .getString("photo_75");

                        Log.i("bs: url: " + i, jsonObject4);
                        finalItem.add(new ItemQuestion(jsonObject4));
                    }
                    catch (org.json.JSONException k) {
                        finalItem.add(new ItemQuestion("https://vk.com/images/camera_200.png?ava=1"));
                    }
                }
            }

        } catch (JSONException e) {
            //i.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            Log.i("bs: ", "error: text: " + json.toString());
            Log.i("bs: ", "error: text: " + sStackTrace);

        }

        /*try {
            JSONArray jsonObject2 = json.getJSONArray("response");

            for (int i = 0; i < jsonObject2.length(); i++) {
                JSONObject jsonObject3 = jsonObject2.getJSONObject(i);
                String jsonObject4 = jsonObject3
                        .getJSONObject("crop_photo")
                        .getJSONObject("photo")
                        .getString("photo_1280");

                if (jsonObject4 == null) {
                    jsonObject4 = jsonObject3
                            .getJSONObject("crop_photo")
                            .getJSONObject("photo")
                            .getString("photo_75");
                }

                Log.i("bs: jsonObject4: ", jsonObject4);
                if (jsonObject4 == null) {
                    finalItem.add(new ItemQuestion("https://vk.com/images/camera_200.png?ava=1"));
                }
                else{
                    finalItem.add(new ItemQuestion(jsonObject4));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

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


    public static void finishFlow3(Observable<ArrayList<ItemQuestion>> observableLocal2,
                                   PageKeyedDataSource.LoadCallback<Integer, ItemQuestion> callback, int key)
    {
        io.reactivex.Observer<ArrayList<ItemQuestion>> observer =
                new io.reactivex.Observer<ArrayList<ItemQuestion>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<ItemQuestion> items) {
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
