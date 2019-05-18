package dev.bugakov.nicegirlsvk;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import dev.bugakov.nicegirlsvk.view.LoginActivity;
import dev.bugakov.nicegirlsvk.view.MainActivity;

public class MyApplication extends Application {

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(@Nullable VKAccessToken oldToken, @Nullable VKAccessToken newToken) {
            if (newToken == null) {
                Toast.makeText(MyApplication.this, "Авторизуйтесь ВКонтакте",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MyApplication.this, LoginActivity.class);
                startActivity(intent);
                Log.i("dd: ", "2");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("dd: ", "1");
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }






}
