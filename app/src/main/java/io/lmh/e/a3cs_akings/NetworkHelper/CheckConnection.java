package io.lmh.e.a3cs_akings.NetworkHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;

import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.VarStatic;

/**
 * Created by E on 7/29/2018.
 */

public class CheckConnection {
     public static boolean status=true;
     private  Context context;

    public  boolean isNewtowrkOnline(Context context){
        ConnectivityManager connectivityManager=null;
        NetworkInfo networkInfo=null;
        RequestQueue requestQueue=null;
        String url = VarStatic.getHostName() + "/post/postlike.php?";
        StringRequest stringReq=null;

        requestQueue= Volley.newRequestQueue(context);
        try {
           stringReq= new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    status = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    status = false;
                }
            }

            );
            requestQueue.add(stringReq);
            requestQueue.start();
        }catch (Exception e){
            return false;
        }

        return  status;
    }
}
