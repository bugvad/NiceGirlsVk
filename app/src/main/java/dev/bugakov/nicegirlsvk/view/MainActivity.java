package dev.bugakov.nicegirlsvk.view;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.DataSource;
import android.app.ActionBar;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("");

        Button button = findViewById(R.id.button);
        TextView alert = findViewById(R.id.alert);
        RecyclerView recyclerView = findViewById(R.id.list);

        if (!VKSdk.isLoggedIn())
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            button.setVisibility(View.VISIBLE);
            alert.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }


        if (!Constant.checkInternetConnection(this)){
            DialogFragment dialogFragment = new FireMissilesDialogFragment();
            dialogFragment.show(getFragmentManager(), "dialogFragment");
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);

        ItemViewModel itemViewModel = ViewModelProviders.of(MainActivity.this)
                .get(ItemViewModel.class);
        final ItemAdapter adapter = new ItemAdapter(MainActivity.this);

        itemViewModel.getItemPagedList().observe(MainActivity.this,
                new Observer<PagedList<Item>>() {
                    @Override
                    public void onChanged(@Nullable PagedList<Item> items) {
                        adapter.submitList(items);

                    }
                });

        recyclerView.setAdapter(adapter);
    }


    @Override
    public void event() {
        ItemDataSourceFactory.getItemDataSource().invalidate();
        RecyclerView recyclerView = findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);

        ItemViewModel itemViewModel = ViewModelProviders.of(MainActivity.this)
                .get(ItemViewModel.class);
        final ItemAdapter adapter = new ItemAdapter(MainActivity.this);

        itemViewModel.getItemPagedList().observe(MainActivity.this,
                new Observer<PagedList<Item>>() {
                    @Override
                    public void onChanged(@Nullable PagedList<Item> items) {
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

    public void goton(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void tologin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
