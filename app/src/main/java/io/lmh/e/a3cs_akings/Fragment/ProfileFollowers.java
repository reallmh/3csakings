package io.lmh.e.a3cs_akings.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
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

import io.lmh.e.a3cs_akings.Model.UserAccount;
import io.lmh.e.a3cs_akings.Profile.UserProfileActivity;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.UIAdapters.UserAccountAdapter;

/**
 * Created by E on 7/1/2018.
 */

public class ProfileFollowers extends android.support.v4.app.Fragment {
    private RecyclerView followerRecycler;
    private UserAccountAdapter followerAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String userId, profileId;
    UserProfileActivity userProfileActivity;
    private ArrayList<UserAccount> accounts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        accounts.clear();
        new GetFollowers().execute();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //users
        userId = FunctionsStatic.getUserId(getActivity());
        userProfileActivity=(UserProfileActivity)getActivity();
        profileId=userProfileActivity.getProfileId();
        accounts=new ArrayList<>();

        followerRecycler= (RecyclerView)view.findViewById(R.id.recycler_followers);
        followerAdapter=new UserAccountAdapter(accounts,this.getActivity().getApplicationContext());
        mLayoutManager= new LinearLayoutManager(this.getActivity().getApplicationContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_follower_fragment, container, false);
    }

    //===================================NetWorkClassess==============================
    private class GetFollowers extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;


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
                    String id=object.getString("id");
                    String name=object.getString("name");
                    String followed=object.getString("followed");
                    UserAccount acc=new UserAccount(id,name,followed,"no");
                    accounts.add(acc);
                    System.out.println("acc id "+acc.getUs_id());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            followerRecycler.setLayoutManager(mLayoutManager);
            followerRecycler.setItemAnimator(new DefaultItemAnimator());
            followerRecycler.setAdapter(followerAdapter);


        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/profile/get_profile_followers.php?userId=" +
                        URLEncoder.encode(userId) + "&profileId=" + URLEncoder.encode(profileId));
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
