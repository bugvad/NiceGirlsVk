package dev.bugakov.nicegirlsvk.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Utils {

    public static void makeToast(String messageText, Context context)
    {
        Toast toast = Toast.makeText(context,
                messageText,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
