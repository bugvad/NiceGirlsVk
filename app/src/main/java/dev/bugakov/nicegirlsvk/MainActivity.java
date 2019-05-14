package dev.bugakov.nicegirlsvk;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.support.annotation.Nullable;
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

import dev.bugakov.nicegirlsvk.adapter.ItemAdapter;
import dev.bugakov.nicegirlsvk.model.Item;
import dev.bugakov.nicegirlsvk.model.Item;

import static dev.bugakov.nicegirlsvk.view.Utils.makeToast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                makeToast("Успех!",
                        MainActivity.this);
            }

            @Override
            public void onError(VKError error) {
                makeToast("Ощибка попробуйте еще раз!", MainActivity.this);

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
            VKSdk.login(this);
        }

        RecyclerView recyclerView = findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);

        ItemViewModel itemViewModel = ViewModelProviders.of(MainActivity.this)
                .get(ItemViewModel.class);
        final ItemAdapter adapter = new ItemAdapter(MainActivity.this);

        itemViewModel.itemPagedList.observe(MainActivity.this,
                new Observer<PagedList<Item>>() {
            @Override
            public void onChanged(@Nullable PagedList<Item> items) {
                Log.i("bs: ", "soup");

                adapter.submitList(items);

            }
        });

        recyclerView.setAdapter(adapter);

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
}
