package io.lmh.e.a3cs_akings.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

import io.lmh.e.a3cs_akings.Model.Post;
import io.lmh.e.a3cs_akings.NetworkHelper.CheckConnection;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.UIStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.UIAdapters.PostAdapter;

/**
 * Created by E on 6/13/2018.
 */

public class PostFragment extends android.support.v4.app.Fragment {
    private SwipeRefreshLayout refreshLayout;
    private static String USERID;
    private RecyclerView recyclerPost;
    private RecyclerView.LayoutManager mLayoutManager;
    private PostAdapter postAdapter;
    private List<Post> posts;
    LinearLayout errorPage;
    LayoutInflater inflater;
    CheckConnection checkConnection;
    private String latestpostid = "0";
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor sharePrefEditor = null;
    String oldestpostid="0";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //commit latest msg to share preferences
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //crete if not exits ,share pref
        sharedPreferences = getActivity().getSharedPreferences("postconfig", Context.MODE_PRIVATE);
        sharePrefEditor = sharedPreferences.edit();
        System.out.println(sharedPreferences.getString("latestpostid", ""));


        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_post);
        recyclerPost = (RecyclerView) view.findViewById(R.id.recycler_post);
        errorPage = (LinearLayout) view.findViewById(R.id.post_err);
        checkConnection = new CheckConnection();
        USERID = FunctionsStatic.getUserId(getActivity());

        setUpPostFragment();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetPosts().execute();
                refreshLayout.setRefreshing(false);
            }
        });
        recyclerPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = LinearLayoutManager.class.cast
                        (recyclerView.getLayoutManager());
                int totalitemCount = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                boolean endReached = lastVisible + 1 >= totalitemCount;
                if (totalitemCount > 0 && endReached) {
                    System.out.println("end reached");
                    new GetOlderPosts().execute();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        return inflater.inflate(R.layout.post_fragment, container, false);

    }

    public void showErrorPage() {
        System.out.println("error shown");
        posts.add(new Post("", "", "", "", "", "", "", "", ""));

    }

    public void showPostPage() {
        System.out.println("posts shown");
    }


    public void setUpPostFragment() {
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts, getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        new GetPosts().execute();


    }

    public void getLastViewedPost() {
        latestpostid = sharedPreferences.getString("latestpostid", "");
    }
    public String getSamllestPostId(){
        if(posts.size()>4){
            return posts.get(posts.size()-1).getPost_id();
        }
        return "0";
    }



    public void commitLatestPostId() {
        if (posts.size() > 1) {
            sharePrefEditor.putString("latestpostid", posts.get(0).getPost_id());
            sharePrefEditor.commit();
            latestpostid = sharedPreferences.getString("latestpostid", "");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class GetPosts extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        boolean iserr = false;

        @Override
        protected void onPreExecute() {
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(String s) {
            posts.clear();
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
                    Post post = new Post(id, accId, accName, postBody, date, comments, likes, isPhoto,
                            liked);
                    posts.add(post);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                iserr = true;


            }
            refreshLayout.setRefreshing(false);
            recyclerPost.setLayoutManager(mLayoutManager);
            recyclerPost.setItemAnimator(new DefaultItemAnimator());
            recyclerPost.setAdapter(postAdapter);
            refreshLayout.setRefreshing(false);
            commitLatestPostId();
            if (iserr) {
                showErrorPage();
            } else {
                showPostPage();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                checkLatestPosts();
                            }
                        } catch (Exception e) {
                            System.out.println("checking error");
                        }
                    }
                }).start();
            }




        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/post/getposts.php?userId=" +
                        URLEncoder.encode(USERID));

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
                System.out.println(ans);
                return ans;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                iserr = true;
            } catch (IOException e) {
                e.printStackTrace();
                iserr = true;
            }
            return ans;
        }
    }


    //get latest post
    private class GetLatestPost extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        List<Post> newposts = new ArrayList<>();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("latest in getnewpostsasyn" + latestpostid);
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
                    Post post = new Post(id, accId, accName, postBody, date, comments, likes, isPhoto,
                            liked);
                    newposts.add(post);

                }

                for (Post p : newposts) {
                    postAdapter.addItem(p);
                }
                newposts.clear();
                commitLatestPostId();


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            System.out.println("smallest id is"+getSamllestPostId());
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/post/getnewposts" +
                        ".php?userId=" +
                        URLEncoder.encode(USERID)
                        + "&latestpostid=" + URLEncoder.encode(latestpostid));

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

    //check latest posts
    public void checkLatestPosts() {
        System.out.println("latest post id " + latestpostid);
        String url = VarStatic.getHostName() + "/post/checknewposts.php?userId=" +
                URLEncoder.encode(USERID)
                + "&latestpostid=" + URLEncoder.encode(latestpostid);


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.equals("yes")) {
                            new GetLatestPost().execute();
                            UIStatic.showSnack(getActivity().getWindow(), "posts updated", "success");
                        } else {

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

    private class GetOlderPosts extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        List<Post> olderposts = new ArrayList<>();

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
                    String accId = object.getString("acc_id");
                    String accName = object.getString("acc_name");
                    String postBody = object.getString("post_body");
                    String date = object.getString("date");
                    String likes = object.getString("likes");
                    String comments = object.getString("comments");
                    String isPhoto = object.getString("isphoto");
                    String liked = object.getString("liked");
                    Post post = new Post(id, accId, accName, postBody, date, comments, likes, isPhoto,
                            liked);
                    olderposts.add(post);

                }

                for (Post p : olderposts) {
                    postAdapter.addItemToBottom(p);
                }
                olderposts.clear();
                commitLatestPostId();


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/post/getoldernewposts" +
                        ".php?userId=" +
                        URLEncoder.encode(USERID)
                        + "&latestpostid=" + URLEncoder.encode(latestpostid));

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
