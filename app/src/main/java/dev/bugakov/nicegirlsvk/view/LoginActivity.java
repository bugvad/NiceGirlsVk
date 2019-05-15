package dev.bugakov.nicegirlsvk.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import dev.bugakov.nicegirlsvk.MyApplication;
import dev.bugakov.nicegirlsvk.R;

import static dev.bugakov.nicegirlsvk.view.Utils.makeToast;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                MyApplication.setFlag(false);
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
        if (MyApplication.isFlag()) {
        VKSdk.login(this);
        }

    }

    public void Login(View view) {
        VKSdk.login(this);

    }
}
