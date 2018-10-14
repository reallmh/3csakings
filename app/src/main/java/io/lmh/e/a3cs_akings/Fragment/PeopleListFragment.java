package io.lmh.e.a3cs_akings.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

import io.lmh.e.a3cs_akings.MainTask;
import io.lmh.e.a3cs_akings.Model.UserAccount;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.UIAdapters.UserAccountAdapter;

/**
 * Created by E on 6/16/2018.
 */

public class PeopleListFragment extends Fragment {
    private RecyclerView followerRecycler;
    private UserAccountAdapter followerAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String userId;
    MainTask userProfileActivity;
    private ArrayList<UserAccount> accounts;
    private MainTask mainTask;
    Toolbar mainToolBar;
    private String earliestaccount;
    SwipeRefreshLayout refreshPeople;
    boolean iserror=false;
    private EditText search;
    private  static String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.people_fragment, container, false);


    }


    @Override
    public void onResume() {
        super.onResume();
        accounts.clear();
        new GetFollowers().execute();
    }

    //set earliest account id
    public void commitEarliestAccountId() {
        if (accounts.size() > 2) {
            earliestaccount = accounts.get(accounts.size() - 1).getUs_id();
        }
    }


    public void showErrorPage() {
       accounts.clear();
        accounts.add(new UserAccount("", "", "", ""));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //users
        userId = FunctionsStatic.getUserId(getActivity());
        userProfileActivity = (MainTask) getActivity();
        accounts = new ArrayList<>();
        mainTask = (MainTask) getActivity();
        mainToolBar = mainTask.getToolbar();
        refreshPeople = (SwipeRefreshLayout) view.findViewById(R.id.refresh_people);
        search=(EditText)view.findViewById(R.id.edt_search_people);


        followerRecycler = (RecyclerView) view.findViewById(R.id.recycler_people);
        followerAdapter = new UserAccountAdapter(accounts, this.getActivity().getApplicationContext());
        mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());


        followerRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = LinearLayoutManager.class.cast
                        (recyclerView.getLayoutManager());
                int totalitemCount = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                boolean endReached = lastVisible + 1 >= totalitemCount;
                if (totalitemCount > 15 && endReached) {

                    new GetEarlierFollowers().execute();
                }
            }
        });
        refreshPeople.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //on refresh follow people
                iserror=false;
                new GetFollowers().execute();
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name=s.toString();
                new GetFollowerByNameAsync().execute();

            }
        });


    }

    //===================================NetWorkClassess==============================
    private class GetEarlierFollowers extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        List<UserAccount> erlierAccounts = new ArrayList<>();

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String followed = object.getString("followed");
                    UserAccount acc = new UserAccount(id, name, followed, "no");
                    erlierAccounts.add(acc);
                    System.out.println("acc id " + acc.getUs_id());


                }

                for (UserAccount acco : erlierAccounts) {
                    accounts.add(acco);
                }
                erlierAccounts.clear();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            commitEarliestAccountId();
            followerRecycler.setLayoutManager(mLayoutManager);
            followerRecycler.setItemAnimator(new DefaultItemAnimator());
            followerRecycler.setAdapter(followerAdapter);


        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                commitEarliestAccountId();
                url = new URL(VarStatic.getHostName() + "/people/getearlierpeople.php?userId=" +
                        URLEncoder.encode(userId) + "&earliestid=" + URLEncoder.encode(earliestaccount));
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

    //get earlier followers
    private class GetFollowers extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            refreshPeople.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(String s) {
            refreshPeople.setRefreshing(false);
            super.onPostExecute(s);
            accounts.clear();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String followed = object.getString("followed");
                    UserAccount acc = new UserAccount(id, name, followed, "no");
                    accounts.add(acc);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            commitEarliestAccountId();
            followerRecycler.setLayoutManager(mLayoutManager);
            followerRecycler.setItemAnimator(new DefaultItemAnimator());
            followerRecycler.setAdapter(followerAdapter);
            if(iserror){
                showErrorPage();

            }


        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/people/getpeople.php?userId=" +
                        URLEncoder.encode(userId));
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
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
                iserror=true;
            } catch (IOException e) {
                e.printStackTrace();
                iserror=true;
            }
            return ans;
        }
    }


    //get follower by name
    private class GetFollowerByNameAsync extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        List<UserAccount> accountsByName = new ArrayList<>();

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {

            System.out.println(s);
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String followed = object.getString("followed");
                    UserAccount acc = new UserAccount(id, name, followed, "no");
                    accountsByName.add(acc);

                }
                accounts.clear();
                for (UserAccount acco : accountsByName) {
                    followerAdapter.addItem(acco);
                }
                accountsByName.clear();

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/people/getpeoplebyname.php?userId=" +
                        URLEncoder.encode(userId) + "&name=" + URLEncoder.encode(name));
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);
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
