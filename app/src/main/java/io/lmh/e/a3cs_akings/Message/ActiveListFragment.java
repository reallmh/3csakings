package io.lmh.e.a3cs_akings.Message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.lmh.e.a3cs_akings.Model.Message;
import io.lmh.e.a3cs_akings.Model.UserAccount;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.UIAdapters.ActiveItemAdapter;
import io.lmh.e.a3cs_akings.database.KingDBHelper;

/**
 * Created by E on 8/6/2018.
 */

public class ActiveListFragment extends Fragment {
    private List<UserAccount> userAccounts;
    private ActiveItemAdapter activeItemAdapter;
    private RecyclerView activeRecycler;
    private LinearLayoutManager manager;

    RequestQueue requestQueue;

    private  boolean shouldthreadRun=true;

    //db
    KingDBHelper kingDBHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activeRecycler=(RecyclerView)view.findViewById(R.id.recycler_active_list);
        userAccounts=new ArrayList<>();

        activeItemAdapter=new ActiveItemAdapter(userAccounts,getContext());
        manager=new LinearLayoutManager(getContext());
        activeRecycler.setLayoutManager(manager);
        activeRecycler.setAdapter(activeItemAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (shouldthreadRun){
                    getActiveList();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.active_list_fragment, container, false);

    }

    //getnewmessage
    public void getActiveList() {
        String url = VarStatic.getHostName() + "/active/getactivelist.php";
        requestQueue= Volley.newRequestQueue(getContext());
        StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        if(!s.equals("err")) {
                            userAccounts.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(s);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = (JSONObject) jsonArray.get(i);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    UserAccount userAccount = new UserAccount(id, name, null, null);
                                    userAccounts.add(userAccount);
                                }
                                activeItemAdapter.notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
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



}
