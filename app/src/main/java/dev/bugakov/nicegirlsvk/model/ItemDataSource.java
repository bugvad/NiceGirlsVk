package dev.bugakov.nicegirlsvk.model;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import dev.bugakov.nicegirlsvk.network.Repository;

import static dev.bugakov.nicegirlsvk.network.Repository.finishFlow1;
import static dev.bugakov.nicegirlsvk.network.Repository.finishFlow2;
import static dev.bugakov.nicegirlsvk.network.Repository.finishFlow3;

public class ItemDataSource extends PageKeyedDataSource<Integer, Item> {

    public static final int PAGE_SIZE = 5;
    private static final int FIRST_PAGE = 1;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Item> callback) {
        int methodCode = 0;

        Log.i("bs: ", "error");
        finishFlow2(Repository.multiobservable(Repository.generateIdsRequest(PAGE_SIZE,5, methodCode)),
                callback);

/*
        VKRequest request = VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                "age_from", "17", "age_to", "18", "count", 5)));

        final ArrayList<Integer> mCatNames= new ArrayList<>();

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                //извлекаем json
                JSONObject json = response.json;

                Log.i("bs: полученный json: ", response.json.toString());

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

                Log.i("bs: проверка", String.valueOf(mCatNames.get(0)));

                VKRequest request1 = VKApi.users().get((VKParameters.from("user_ids",
                        mCatNames.get(0) + "," + mCatNames.get(1) + "," + mCatNames.get(2) +
                                "," + mCatNames.get(3) + "," + mCatNames.get(4),
                        "fields", "crop_photo")));

                final List<Item> item = new ArrayList<>();
                //запрашиваем json с url картинки
                request1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        JSONObject json = response.json;

                        try {
                            JSONArray jsonObject2 = json.getJSONArray("response");
                            for (int i = 0; i < jsonObject2.length(); i++) {
                                JSONObject jsonObject3 = jsonObject2.getJSONObject(i);
                                String jsonObject4 = jsonObject3
                                        .getJSONObject("crop_photo")
                                        .getJSONObject("photo")
                                        .getString("photo_1280");

                                Log.i("bs: url: " + i, jsonObject4);
                                item.add(new Item(jsonObject4));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("bs: ", "error");
                        }

                        callback.onResult(item, null, FIRST_PAGE + 1);

                    }

                    @Override
                    public void onError(VKError error) {
                        Log.i("bs: ", "error1");

                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                        Log.i("bs: ", "error2");

                    }


                });
            }
            @Override
            public void onError(VKError error) {}
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {}
        });*/

    }

    //загрузка предыдущей страницы
    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {

        Log.i("bs: ", "inside loadBefore");

        int methodCode = -1;

        finishFlow1(Repository.multiobservable(Repository.generateIdsRequest(PAGE_SIZE, params.key, methodCode)),
                callback, params.key);

/*
        VKRequest request = VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                "age_from", "17", "age_to", "18", "count", 5, "offset", PAGE_SIZE * (params.key - 1))));

        final ArrayList<Integer> mCatNames= new ArrayList<>();

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                final Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                //извлекаем json
                JSONObject json = response.json;

                Log.i("bs: полученный json: ", response.json.toString());

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

                Log.i("bs: проверка", String.valueOf(mCatNames.get(0)));

                VKRequest request1 = VKApi.users().get((VKParameters.from("user_ids", mCatNames.get(0) + "," + mCatNames.get(1) + "," + mCatNames.get(2) + "," + mCatNames.get(3) + "," + mCatNames.get(4),
                        "fields", "crop_photo")));

                final List<Item> item = new ArrayList<>();
                //запрашиваем json с url картинки
                request1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        JSONObject json = response.json;

                        try {
                            JSONArray jsonObject2 = json.getJSONArray("response");
                            for (int i = 0; i < jsonObject2.length(); i++) {
                                JSONObject jsonObject3 = jsonObject2.getJSONObject(i);
                                String jsonObject4 = jsonObject3
                                        .getJSONObject("crop_photo")
                                        .getJSONObject("photo")
                                        .getString("photo_1280");

                                Log.i("bs: url: " + i, jsonObject4);
                                item.add(new Item(jsonObject4));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callback.onResult(item, adjacentKey);



                    }

                    @Override
                    public void onError(VKError error) {
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                    }


                });
            }
            @Override
            public void onError(VKError error) {}
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {}
        });*/

    }

    //загрузка следующей страницы
    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {
        int methodCode = 1;

        finishFlow3(Repository.multiobservable(Repository.generateIdsRequest(PAGE_SIZE, params.key, methodCode)),
                callback, params.key);
/*
        VKRequest request = VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                "age_from", "17", "age_to", "18", "count", 5, "offset", PAGE_SIZE * (params.key - 1))));

        Log.i("bs: след. страница: ", (PAGE_SIZE * (params.key - 1)) + "");

        final ArrayList<Integer> mCatNames= new ArrayList<>();

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                final Integer key = params.key + 1;
                //извлекаем json
                JSONObject json = response.json;

                Log.i("bs: полученный json: ", response.json.toString());

                try {
                    JSONObject jsonObject1 = json.getJSONObject("response");
                    JSONArray jsonObject2 = jsonObject1.getJSONArray("items");
                    for (int i = 0; i < 5; i++) {
                        try {
                            mCatNames.add((jsonObject2.getJSONObject(i).getInt("id")));
                            Log.i("bs: id: " + i, mCatNames.get(i) + "");
                        }
                        catch (org.json.JSONException e)
                        {
                            mCatNames.add(145227567);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("bs: ", "fail");
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String sStackTrace = sw.toString();
                    Log.i("bs: ", "annoy shit: " + sStackTrace);

                }


                Log.i("bs: проверка", String.valueOf(mCatNames.get(0)));

                VKRequest request1 = VKApi.users().get((VKParameters.from("user_ids", mCatNames.get(0) + "," + mCatNames.get(1) + "," + mCatNames.get(2) + "," + mCatNames.get(3) + "," + mCatNames.get(4),
                        "fields", "crop_photo")));

                final List<Item> item = new ArrayList<>();
                //запрашиваем json с url картинки
                request1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
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
                                    item.add(new Item(jsonObject4));
                                }
                                catch (org.json.JSONException e)
                                {
                                    try {
                                        String jsonObject4 = jsonObject3
                                                .getJSONObject("crop_photo")
                                                .getJSONObject("photo")
                                                .getString("photo_75");

                                        Log.i("bs: url: " + i, jsonObject4);
                                        item.add(new Item(jsonObject4));
                                    }
                                    catch (org.json.JSONException k) {
                                        item.add(new Item("https://vk.com/images/camera_200.png?ava=1"));
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

                        callback.onResult(item, key);


                    }

                    @Override
                    public void onError(VKError error) {
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                    }
                });
            }
            @Override
            public void onError(VKError error) {}
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {}
        });*/
    }
}