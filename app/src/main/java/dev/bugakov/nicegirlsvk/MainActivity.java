package dev.bugakov.nicegirlsvk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                makeToast("Успех!");
                Intent intent = new Intent(MainActivity.this, RequestdActivity.class);
                startActivity(intent);
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


        final List<Phone> phones = new ArrayList<>();
        final ArrayList<Integer> mCatNames = new ArrayList<>();
        final ArrayList<String> avatarsUrls = new ArrayList<>();

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        // создаем адаптер


        //формируем запрос на 20 айтемов
        VKRequest request = VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                "age_from", "17", "age_to", "18")));

        //делаем запрос

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

                for (int k = 0; k < mCatNames.size(); k++)
                {
                    VKRequest request1 = VKApi.users().get((VKParameters.from("user_ids", mCatNames.get(k),
                            "fields", "crop_photo")));

                    //запрашиваем json с url картинки
                    request1.executeWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {
                            JSONObject json = response.json;
                            Log.i("bs: json с id", response.json.toString());

                            try {
                                JSONArray jsonObject2 = json.getJSONArray("response");
                                for (int i = 0; i < jsonObject2.length(); i++) {
                                    JSONObject jsonObject3 = jsonObject2.getJSONObject(i);
                                    String jsonObject4 = jsonObject3
                                            .getJSONObject("crop_photo")
                                            .getJSONObject("photo")
                                            .getString("photo_1280");

                                    Log.i("bs: url: " + i, jsonObject4);
                                    avatarsUrls.add(jsonObject4);
                                    phones.add(new Phone(jsonObject4));

                                    DataAdapter adapter = new DataAdapter(getApplicationContext(), phones);
                                    // устанавливаем для списка адаптер
                                    recyclerView.setAdapter(adapter);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VKError error) {
                        }

                        @Override
                        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                        }


                    });
                }


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

    /*public void onMyButtonClick(View view) {
        VKSdk.login(this);
    }*/
}