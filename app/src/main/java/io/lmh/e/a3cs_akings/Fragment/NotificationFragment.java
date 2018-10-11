package io.lmh.e.a3cs_akings.Fragment;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.lmh.e.a3cs_akings.Model.Message;
import io.lmh.e.a3cs_akings.Model.Notification;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Services.NotiChecker;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.UIAdapters.NotificationAdapter;

public class NotificationFragment extends Fragment {
    private RecyclerView notiRecycler;
    private NotificationAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<Notification> notifications;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager=new LinearLayoutManager(getContext());
        notifications=new ArrayList<>();

        notiRecycler=(RecyclerView) view.findViewById(R.id.noti_recycler);

        adapter=new NotificationAdapter(notifications,getContext());
        notiRecycler.setLayoutManager(manager);
        //add data
        new getNotis().execute();

        getContext().startService(new Intent(getContext(),NotiChecker.class));









    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.noti_fragment, container, false);

    }
    //get latest post
    private class getNotis extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            System.out.println("initial noti is"+s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    String id=object.getString("id");
                    String reacter_id=object.getString("reacter_acc_id");
                    String reacter_name=object.getString("reacter_name");
                    String type=object.getString("type");
                    String post_id=object.getString("post_id");
                    String noti_body=object.getString("noti_text");
                    String seen=object.getString("seen");
                    String date=object.getString("noti_date");
                    Notification notification=new Notification(id,reacter_name,reacter_id,noti_body,type,post_id,seen,date);
                    System.out.println(notification.getType());
                    notifications.add(notification);


                }

                notiRecycler.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/notification/getnotifications" +
                        ".php?userId=" +
                        URLEncoder.encode(FunctionsStatic.getUserId(getActivity())));

                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream in = null;
                in = conn.getInputStream();
                InputStreamReader inReader = new InputStreamReader(in);

                BufferedReader br = new BufferedReader(inReader);

                String s = null;
                while ((s = br.readLine()) != null) {
                    ans += s + "\n";
                }
                conn.disconnect();
                return ans;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ans;
        }
    }



}
