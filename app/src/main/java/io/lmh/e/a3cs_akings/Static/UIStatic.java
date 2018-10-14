package io.lmh.e.a3cs_akings.Static;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;

/**
 * Created by E on 6/17/2018.
 */

public class UIStatic {
    public static void showSnack(Window window, String message,String type){
        Snackbar snackbar=Snackbar.make(window.getDecorView().findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG);
        View view=snackbar.getView();
        if(type.equals("success")){
            view.setBackgroundColor(Color.DKGRAY);
        }
        if (type.equals("error")){
            view.setBackgroundColor(Color.RED);
        }
        snackbar.show();
    }
}
