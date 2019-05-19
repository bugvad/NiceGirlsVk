package dev.bugakov.nicegirlsvk.view;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import dev.bugakov.nicegirlsvk.MyApplication;
import dev.bugakov.nicegirlsvk.R;

import static dev.bugakov.nicegirlsvk.view.Utils.makeToast;


public class LoginActivity extends AppCompatActivity {

    Button button;
    Button logout_button;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                makeToast("Вход выполнен", LoginActivity.this);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(VKError error) {
                makeToast("Ошибка. Попробуйте еще раз", LoginActivity.this);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.button);
        logout_button = findViewById(R.id.button1);
        if (!VKSdk.isLoggedIn()) {
            button.setVisibility(View.VISIBLE);
        }
        else {
            logout_button.setVisibility(View.VISIBLE);
        }
    }

    public void Login(View view) {
        VKSdk.login(this);
    }

    public void Logout(View view) {
        VKSdk.logout();
        makeToast("Выход выполнен", LoginActivity.this);
        button.setVisibility(View.VISIBLE);
    }

    public void About(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }



}
