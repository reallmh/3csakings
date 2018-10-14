package io.lmh.e.a3cs_akings.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import io.lmh.e.a3cs_akings.Model.Post;
import io.lmh.e.a3cs_akings.NetworkHelper.CheckConnection;
import io.lmh.e.a3cs_akings.Profile.UserProfileActivity;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.UIAdapters.PostAdapter;

/**
 * Created by E on 7/1/2018.
 */

public class ProfilePost extends Fragment {
    RecyclerView.LayoutManager mLayoutManager;
    String userId, profileId;
    private RecyclerView postRecycler;
    private PostAdapter postAdapter;
    private ArrayList<Post> posts;
    UserProfileActivity userProfileActivity;
    CheckConnection checkConnection;
    SwipeRefreshLayout errorRefresh, postRefresh;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        posts.clear();
        if (checkConnection.isNewtowrkOnline(this.getContext().getApplicationContext())) {
            new GetPosts().execute();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //
        userId = FunctionsStatic.getUserId(getActivity());
        userProfileActivity = (UserProfileActivity) getActivity();
        profileId = userProfileActivity.getProfileId();
        postRecycler = (RecyclerView) view.findViewById(R.id.recycler_posts);
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts, this.getActivity().getApplicationContext());
        mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnection = new CheckConnection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.profile_post_fragment, container, false);
    }

    //===================================NetWorkClassess==============================
    private class GetPosts extends AsyncTask<String, String, String> {
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
                    String id = object.getString("id");
                    String accId = object.getString("acc_id");
                    String accName = object.getString("acc_name");
                    String postBody = object.getString("post_body");
                    String date = object.getString("date");
                    String likes = object.getString("likes");
                    String comments = object.getString("comments");
                    String isPhoto = object.getString("isphoto");
                    String liked = object.getString("liked");
                    Post post = new Post(id, accId, accName, postBody, date, comments, likes, isPhoto, liked);
                    posts.add(post);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            postRecycler.setLayoutManager(mLayoutManager);
            postRecycler.setItemAnimator(new DefaultItemAnimator());
            postRecycler.setAdapter(postAdapter);


        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/profile/get_profile_posts.php?userId=" +
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
                System.out.println(ans);
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
