package io.lmh.e.a3cs_akings.Message;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.lmh.e.a3cs_akings.Model.Message;
import io.lmh.e.a3cs_akings.Model.MessageItem;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.UIAdapters.MessageAdapter;
import io.lmh.e.a3cs_akings.UIAdapters.MessageItemAdapter;
import io.lmh.e.a3cs_akings.database.KingDBHelper;

/**
 * Created by E on 8/6/2018.
 */

public class MessageListFragment extends Fragment {
    private RecyclerView messageItemList;
    private List<MessageItem> messageItems;
    private MessageItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //test
    List<Message> messageList;
    MessageItemAdapter messageItemAdapter;

    //db
    KingDBHelper kingDBHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        kingDBHelper = new KingDBHelper(getContext());

    }

    public void insertData() {
        new GetMessageItemList().execute();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        //initialize views
        messageItemList = (RecyclerView) view.findViewById(R.id.message_item_list);

        messageItems = new ArrayList<>();
        kingDBHelper=new KingDBHelper(getContext());
        kingDBHelper.deleteMessageItems();
        insertData();

        Cursor dbCursor = kingDBHelper.getMessageItems();
        if (dbCursor.getCount() == 0) {
            System.out.println("no data in db");
        } else {
            while (dbCursor.moveToNext()) {
                MessageItem messageItem = new MessageItem(dbCursor.getString(0), dbCursor.getString(1), dbCursor.getString(2), dbCursor.getString(3), dbCursor.getString(4));
                messageItems.add(messageItem);
            }


        }

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new MessageItemAdapter(getContext(), messageItems);
        messageItemList.setLayoutManager(layoutManager);
        messageItemList.setAdapter(adapter);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("on creaete", "on creaet msg list fra");
        return inflater.inflate(R.layout.message_list_fragment, container, false);

    }


    //===================================NetWorkClassess==============================
    private class GetMessageItemList extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
       @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            try {
                if(!s.equals("[]")) {
                    JSONArray jsonArray=new JSONArray(s);
                    ArrayList<String> idList=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject userinfo =(JSONObject)jsonArray.get(i) ;
                        String id = userinfo.getString("msg_item_id");
                        String sender=userinfo.getString("msg_item_sender_id");
                        String receiver_id = userinfo.getString("msg_item_receiver_id");
                        String name = userinfo.getString("receiver_name");
                        String senderName=userinfo.getString("sender_name");

                        if(name.equals(FunctionsStatic.getUserName(getActivity()))){
                            name=senderName;
                        }
                        if(!idList.isEmpty())
                        idList.clear();;
                        Cursor cursor=kingDBHelper.getMessageItems();
                        while (cursor.moveToNext()){
                            idList.add(cursor.getString(1));
                        }
                        if(idList.contains(sender))
                            break;
                        if(idList.contains(receiver_id))
                            break;
                        String dbreceiver;
                        if(!receiver_id.equals(FunctionsStatic.getUserId(getActivity()))){
                            dbreceiver=receiver_id;
                        }else {
                            dbreceiver=sender;
                        }
                        kingDBHelper.insertMessageData(id,dbreceiver,name,null,null);
                    }
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
                url = new URL(VarStatic.getHostName() + "/message/getmessageitemlist.php?userId=" +
                        URLEncoder.encode(FunctionsStatic.getUserId(getActivity())));
                ;
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

