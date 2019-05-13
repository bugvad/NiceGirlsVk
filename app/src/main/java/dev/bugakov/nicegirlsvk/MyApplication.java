package dev.bugakov.nicegirlsvk;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

public class MyApplication extends Application {

    static boolean flag = true;

    public static boolean isFlag() {
        return flag;
    }


    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(@Nullable VKAccessToken oldToken, @Nullable VKAccessToken newToken) {
            Log.i("message_real:", "OKK");

            if (newToken == null) {
                Toast.makeText(MyApplication.this, "AccessToken invalidated", Toast.LENGTH_LONG).show();
                Log.i("message_real:", "OK");
                flag = false;
                Intent intent = new Intent(MyApplication.this, MainActivity.class);
                startActivity(intent);
                /*Intent intent = new Intent(MyApplication.this, MainActivity.class);
                intent.putExtra("flag", false);
                startActivity(intent);*/
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("message_real:", "KOK");
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        Log.i("message_real:", "sOK");
        //Intent intent = new Intent(MyApplication.this, MainActivity.class);
        //startActivity(intent);
    }






}
