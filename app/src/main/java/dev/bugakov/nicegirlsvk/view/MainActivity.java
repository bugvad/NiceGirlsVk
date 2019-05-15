package dev.bugakov.nicegirlsvk.view;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.DataSource;
import android.app.ActionBar;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.Objects;

import dev.bugakov.nicegirlsvk.MyApplication;
import dev.bugakov.nicegirlsvk.R;
import dev.bugakov.nicegirlsvk.adapter.ItemAdapter;
import dev.bugakov.nicegirlsvk.model.Constant;
import dev.bugakov.nicegirlsvk.model.Item;
import dev.bugakov.nicegirlsvk.model.ItemDataSource;
import dev.bugakov.nicegirlsvk.model.ItemDataSourceFactory;
import dev.bugakov.nicegirlsvk.viewmodel.ItemViewModel;

import static dev.bugakov.nicegirlsvk.view.Utils.makeToast;

public class MainActivity extends AppCompatActivity implements AddPhotoBottomDialogFragment.onsomeEventListener2 {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MainActivity.getSupportActionBar().setIcon(getDrawable(R.mipmap.logo));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("");
        //getSupportActionBar().setIcon(R.drawable.favicon150x150);

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


    @Override
    public void someEvent2() {
        Log.i("ds: bull shit", Constant.getFrom() + " " + Constant.getTo());


        ItemDataSourceFactory.getItemDataSource().invalidate();
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
    }

    public void switch_filters(MenuItem item) {
        AddPhotoBottomDialogFragment addPhotoBottomDialogFragment =
                AddPhotoBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "add_photo_dialog_fragment");
    }
}
