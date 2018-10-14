package io.lmh.e.a3cs_akings.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;

import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;

public class NotiChecker extends Service {
    private String userId,userName;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        userId=intent.getStringExtra("userId");
        ReportActive();
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void ReportActive(){
        String url = VarStatic.getHostName() + "/notification/checknotifications.php?userId=" +
                URLEncoder.encode(userId);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                           System.out.println("noti checker service"+s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue.add(stringReq);
        requestQueue.start();
    }

}
