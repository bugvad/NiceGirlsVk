package dev.bugakov.nicegirlsvk;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                makeToast("Успех!");
            }

            @Override
            public void onError(VKError error) {
                makeToast("Ощибка попробуйте еще раз!");

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!MyApplication.isFlag())
        {
            Log.i("bs: ", "yep");
            VKSdk.login(this);
        }
            //VKSdk.login(this);
        Log.i("bs: ", "noup");
        //VKSdk.login(this);

        RecyclerView recyclerView = findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);

        ItemViewModel itemViewModel = ViewModelProviders.of(MainActivity.this)
                .get(ItemViewModel.class);
        final ItemAdapter adapter = new ItemAdapter(MainActivity.this);

        itemViewModel.itemPagedList.observe(MainActivity.this,
                new Observer<PagedList<ItemQuestion>>() {
            @Override
            public void onChanged(@Nullable PagedList<ItemQuestion> items) {
                Log.i("bs: ", "soup");

                //in case of any changes
                //submitting the items to adapter
                adapter.submitList(items);

            }
        });

        recyclerView.setAdapter(adapter);

        //setUI(recyclerView, Repository.multiobservable(Repository.generateIdsRequest()));

        //заполняем recyclerView
        //getList(recyclerView);

        //swipe-to-refresh
      /*  mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getList(recyclerView);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.setAdapter(adapter);*/
    }

    //проверки и получение
    public void getList(final RecyclerView recyclerView) {

        VKRequest request = VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                "age_from", "17", "age_to", "18", "count", 5)));

        final ArrayList<Integer> mCatNames = new ArrayList<>();

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                //извлекаем json
                JSONObject json = response.json;

                Log.i("bs: полученный чик: ", response.json.toString());

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
                        mCatNames.get(0) + "," + mCatNames.get(1) + "," +
                                mCatNames.get(2) + "," + mCatNames.get(3) + "," + mCatNames.get(4),
                        "fields", "crop_photo")));

                final ArrayList<Item> item = new ArrayList<>();
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

                        //DataAdapter adapterMain = new DataAdapter(MainActivity.this, item);

                        //adapterMain.notifyDataSetChanged();
                        //recyclerView.setAdapter(adapterMain);
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
        });



    }




    public void makeToast(String messageText)
    {
        Toast toast = Toast.makeText(getApplicationContext(),
                messageText,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

   /* public void onMyButtonClick(View view) {
        VKSdk.login(this);
    }*/
}
