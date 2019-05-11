package dev.bugakov.nicegirlsvk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

public class RequestdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestd);

        VKRequest request = VKApi.users().search((VKParameters.from(VKApiConst.SEX, 1,
                "age_from", "17", "age_to", "18")));
        //VKRequest request = new VKRequest("users.search",
          //      VKParameters.from(VKApiConst.SEX, 1, "age_from", "17", "age_to", "18"));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.i("message_real:", response.responseString);
            }
            @Override
            public void onError(VKError error) {
//Do error stuff
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
//I don't really believe in progress
            }
        });
    }
}
