package io.lmh.e.a3cs_akings.Message;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

import io.lmh.e.a3cs_akings.Fragment.PostFragment;
import io.lmh.e.a3cs_akings.Model.Message;
import io.lmh.e.a3cs_akings.Model.Post;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.UIStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.UIAdapters.MessageAdapter;

public class MessageActivity extends AppCompatActivity {
    //intent
    Intent intent;
    //context
    Context context;
    //thread flag
    boolean shouldthreadrun=true;
    //ids
    private String userId,messageUserId,latestMessageId;
    //declare view items
    private TextView active_time;
    private RecyclerView msg_recycler;
    private EditText msg_edt;
    //declare contents
    private String message_body;
    private List<Message> messageList;
    //Adapter
     private MessageAdapter messageAdapter;
    //Layout manager
    private LinearLayoutManager layoutManager;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new GetLatestMessage().execute();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //initialize ids
        userId= FunctionsStatic.getUserId(this);
        intent=getIntent();
        messageUserId=intent.getStringExtra("messageUserId");


        //initialize views
        active_time=(TextView)findViewById(R.id.active_time);
        msg_recycler = (RecyclerView) findViewById(R.id.msg_recycler);
        msg_edt = (EditText) findViewById(R.id.edt_message);
        layoutManager = new LinearLayoutManager(getApplicationContext());

       //initialize content
        context=getApplicationContext();
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(getApplicationContext(),messageList);
        msg_recycler.setItemAnimator(null);


        msg_recycler.setLayoutManager(layoutManager);
        msg_recycler.setAdapter(messageAdapter);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while(shouldthreadrun) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    checkLatestMessage();
                }
            }
        }).start();
        */


    }

    public void sendMessage(View view) {
        message_body = msg_edt.getText().toString();
        sendMessage(message_body);
        msg_edt.setText("");
        message_body="";

    }

    ///checknew message
    //check latest posts
    public void checkLatestMessage() {
        String url = VarStatic.getHostName() + "/message/checknewmessage.php?userId=" +
                URLEncoder.encode(userId) + "&receiver=" + URLEncoder.encode(messageUserId)
                + "&latest=" + URLEncoder.encode(getLastMessageId());


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new
                Response.Listener<String>() {
                    @Override

                    public void onResponse(String s) {
                        String newmsg,isactive;
                        System.out.println("last" +s);
                        try{
                            JSONObject jsonObject=new JSONObject(s);
                            newmsg=jsonObject.getString("newmsg");
                            isactive=jsonObject.getString("active");
                            if(newmsg.equals("new")){
                                getNewMessage();
                            }
                            if(isactive.equals("active")){
                                active_time.setText("active now");
                                active_time.setTextColor(Color.GREEN);
                            }else{
                                active_time.setText("");

                            }

                        }catch (Exception e){

                        }

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

    private  String getLastMessageId(){
        if(messageList.size()>0) {
            latestMessageId = messageList.get(messageList.size() - 1).getM_id();
        }else{
            latestMessageId="0";
        }

        return latestMessageId;
    }

    private void moveToLowest(){
        if(messageList.size()>1)
        msg_recycler.scrollToPosition(messageList.size()-1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        shouldthreadrun=false;

    }

    //getnewmessage
    public void getNewMessage() {
        String url = VarStatic.getHostName() + "/message/getnewmessage.php?userId=" +
                URLEncoder.encode(userId) + "&receiver=" + URLEncoder.encode(messageUserId)
                + "&latest=" + URLEncoder.encode(getLastMessageId());


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                String id=object.getString("id");
                                String sender=object.getString("sender");
                                String receiver=object.getString("receiver");
                                String body=object.getString("body");
                                String date=object.getString("date");
                                Message message=new Message(id,sender,receiver,body,date);
                                messageAdapter.addItemToBottom(message);
                                moveToLowest();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

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

    //send message
    //getnewmessage
    public void sendMessage(String ms_body) {
        String url = VarStatic.getHostName() + "/message/sendmessage.php?userId=" +
                URLEncoder.encode(userId) + "&receiver=" + URLEncoder.encode(messageUserId)
                + "&body=" + URLEncoder.encode(ms_body);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                String id=object.getString("id");
                                String sender=object.getString("sender");
                                String receiver=object.getString("receiver");
                                String body=object.getString("body");
                                String date=object.getString("date");
                                Message message=new Message(id,sender,receiver,body,date);
                                messageAdapter.addItemToBottom(message);
                                moveToLowest();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

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

    //Network classes
    //get latest post
    private class GetLatestMessage extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            System.out.println("initial message is"+s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    String id=object.getString("id");
                    String sender=object.getString("sender");
                    String receiver=object.getString("receiver");
                    String body=object.getString("body");
                    String date=object.getString("date");
                    Message message=new Message(id,sender,receiver,body,date);
                    messageList.add(message);

                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/message/getinitialmessage" +
                        ".php?userId=" +
                        URLEncoder.encode(userId)
                        + "&receiver=" + URLEncoder.encode(messageUserId));

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
