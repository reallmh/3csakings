package io.lmh.e.a3cs_akings.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

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

import UI.CircleTransformation;
import io.lmh.e.a3cs_akings.Model.Post;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.UIStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.UIAdapters.CommentAdapter;

public class Comment extends AppCompatActivity {
    Post post;
    String coverName;
    String userId, post_id, userName;
    ImageView userIcon, love, pic;
    TextView postAccName, postDate, postLike, postComment, postBody;
    EditText commentBody;
    RecyclerView commentRecycler;
    List<io.lmh.e.a3cs_akings.Model.Comment> comments;
    CommentAdapter commentAdapter;
    RecyclerView.LayoutManager manager;
    Activity commentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get serialized data
        commentActivity = this;
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        userId = FunctionsStatic.getUserId(this);
        post_id = post.getPost_id();
        userName = FunctionsStatic.getUserName(this);
        //

        comments = new ArrayList<>();
        super.onCreate(savedInstanceState);
        if (post.getIsphoto().equals("yes")) {
            setContentView(R.layout.activity_image_comment);

        } else {
            setContentView(R.layout.activity_comment);
        }

        Toolbar toolBar = (Toolbar) findViewById(R.id.comment_tool_bar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Comments");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //layout
        //recycler view
        commentRecycler = (RecyclerView) findViewById(R.id.comment_recycler);
        commentAdapter = new CommentAdapter(comments, getApplicationContext());
        manager = new LinearLayoutManager(getApplicationContext());
        new GetComments().execute();
        //

        love = (ImageView) findViewById(R.id.love);
        pic = (ImageView) findViewById(R.id.post_image);
        commentBody = (EditText) findViewById(R.id.edt_comment);
        userIcon = (ImageView) findViewById(R.id.user_icon);
        postAccName = (TextView) findViewById(R.id.post_acc_name);
        postBody = (TextView) findViewById(R.id.post_body);
        postLike = (TextView) findViewById(R.id.post_like);
        postDate = (TextView) findViewById(R.id.post_date);
        postComment = (TextView) findViewById(R.id.post_comment);

        //get data from posts
        coverName = FunctionsStatic.getProfileImageUrl(post.getAcc_id());
        Picasso.with(getApplicationContext()).load(coverName).resize(90, 90).transform(new CircleTransformation()).into(userIcon);

        if (post.getIsphoto().equals("yes")) {
            coverName=FunctionsStatic.getPostUrl(post.getAcc_id()+post.getPost_id());
            Picasso.with(getApplicationContext()).load(coverName).resize(400, 200).centerCrop().error(R.drawable.ic_people_outline_black_24dp).into(pic);
        }

        if (post.getLiked().equals("yes")) {
            love.setImageResource(R.drawable.ic_like_after);
        } else {
            love.setImageResource(R.drawable.ic_like_before);
        }
        bindData();


        //on love button click
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if it has given love
                if (post.getLiked().equals("yes")) {
                    System.out.println("double clicked");
                    String lov = postLike.getText().toString();
                    int ilov = Integer.parseInt(lov);
                    int ansilov = ilov - 1;
                    love.setImageResource(R.drawable.ic_like_before);
                    postLike.setText(Integer.toString(ansilov));
                    post.setLiked("no");
                    love.setEnabled(false);
                    //
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = VarStatic.getHostName() + "/post/postunlike.php?userId=" +
                            URLEncoder.encode(userId)
                            + "&postId=" + URLEncoder.encode(post.getPost_id()) +
                            "&posterId=" + URLEncoder.encode(post.getAcc_id()) +
                            "&posterName=" + URLEncoder.encode(post.getAcc_name()) +
                            "&userName=" + URLEncoder.encode(userName);
                    StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (!(s.equals("err"))) {
                                int likes = Integer.parseInt(s);
                                String responseLike = Integer.toString(likes);
                                love.setImageResource(R.drawable.ic_like_before);
                                postLike.setText(responseLike);
                                post.setLikes(responseLike);
                                post.setLiked("no");
                                love.setEnabled(true);
                            } else if (s.equals("err")) {
                                love.setImageResource(R.drawable.ic_like_after);
                                postLike.setText(post.getLikes());
                                post.setLiked("yes");
                                love.setEnabled(true);
                            } else {
                                love.setImageResource(R.drawable.ic_like_after);
                                postLike.setText(post.getLikes());
                                post.setLiked("yes");
                                love.setEnabled(true);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            love.setImageResource(R.drawable.ic_like_after);
                            postLike.setText(post.getLikes());
                            post.setLiked("yes");
                            love.setEnabled(true);
                        }
                    }

                    );
                    requestQueue.add(stringReq);
                    requestQueue.start();
                } else {
                    String lov = postLike.getText().toString();
                    int ilov = Integer.parseInt(lov);
                    int ansilov = ilov + 1;
                    love.setImageResource(R.drawable.ic_like_after);
                    postLike.setText(Integer.toString(ansilov));
                    post.setLiked("yes");
                    love.setEnabled(false);
                    //
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = VarStatic.getHostName() + "/post/postlike.php?userId=" +
                            URLEncoder.encode(userId)
                            + "&postId=" + URLEncoder.encode(post.getPost_id()) +
                            "&posterId=" + URLEncoder.encode(post.getAcc_id()) +
                            "&posterName=" + URLEncoder.encode(post.getAcc_name()) +
                            "&userName=" + URLEncoder.encode(userName);
                    StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (!(s.equals("err"))) {
                                int likes = Integer.parseInt(s);
                                String responseLike = Integer.toString(likes);
                                love.setImageResource(R.drawable.ic_like_after);
                                postLike.setText(responseLike);
                                post.setLikes(responseLike);
                                post.setLiked("yes");
                                love.setEnabled(true);
                            } else if (s.equals("err")) {
                                love.setImageResource(R.drawable.ic_like_before);
                                postLike.setText(post.getLikes());
                                post.setLiked("no");
                                love.setEnabled(true);
                            } else {
                                love.setImageResource(R.drawable.ic_like_before);
                                postLike.setText(post.getLikes());
                                post.setLiked("no");
                                love.setEnabled(true);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            love.setImageResource(R.drawable.ic_like_before);
                            postLike.setText(post.getLikes());
                            post.setLiked("no");
                            love.setEnabled(true);
                        }
                    }

                    );
                    requestQueue.add(stringReq);
                    requestQueue.start();
                }
            }
        });


    }

    private void bindData() {
        postAccName.setText(post.getAcc_name());
        postBody.setText(post.getPost_body());
        postDate.setText(FunctionsStatic.getNiceTime(post.getDate()));
        postLike.setText(post.getLikes());
        postComment.setText(post.getComments());
    }

    public void createComment(View view) {

        String commentbody = commentBody.getText().toString();
        if (!commentbody.equals("")) {
            String url = VarStatic.getHostName() + "/comment/createcomment.php?" +
                    "userId=" + URLEncoder.encode(userId)
                    + "&postId=" + URLEncoder.encode(post_id) +
                    "&commentbody=" + URLEncoder.encode(commentbody)
                    +"&posterid="+URLEncoder.encode(post.getAcc_id())+"&postername="+URLEncoder.encode(userName);


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (!s.equals("err")) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(s);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                String id = object.getString("id");
                                String accid = object.getString("accid");
                                String postid = object.getString("postid");
                                String commentbody = object.getString("commentbody");
                                String date = object.getString("date");
                                String accname = object.getString("accname");
                                io.lmh.e.a3cs_akings.Model.Comment comment = new io.lmh.e.a3cs_akings.Model.Comment(id, postid, accid, accname, commentbody, date);
                                comments.add(comment);

                                commentBody.setText("");
                                String comments = post.getComments();
                                int comCount = Integer.parseInt(comments) + 1;
                                post.setComments(Integer.toString(comCount));
                                postComment.setText(post.getComments());
                                UIStatic.showSnack(commentActivity.getWindow(), "commented to" + comment.getAcc_name() + "'s account", "success");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    UIStatic.showSnack(commentActivity.getWindow(), "error commenting", "error");
                }
            }
            );
            requestQueue.add(stringReq);
            requestQueue.start();
        }
    }

    //comment async task
    private class GetComments extends AsyncTask<String, String, String> {
        HttpURLConnection conn;

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
                    String accid = object.getString("accid");
                    String postid = object.getString("postid");
                    String commentbody = object.getString("commentbody");
                    String date = object.getString("date");
                    String accname = object.getString("accname");
                    io.lmh.e.a3cs_akings.Model.Comment comment = new io.lmh.e.a3cs_akings.Model.Comment(id, postid, accid, accname, commentbody, date);
                    comments.add(comment);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            commentRecycler.setLayoutManager(manager);
            commentRecycler.setItemAnimator(new DefaultItemAnimator());
            commentRecycler.setAdapter(commentAdapter);


        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            String userId, post_id;
            userId = FunctionsStatic.getUserId(Comment.this);
            post_id = post.getPost_id();
            try {
                url = new URL(VarStatic.getHostName() + "/comment/getcomment.php?userId=" +
                        URLEncoder.encode(userId) + "&postId=" + URLEncoder.encode(post_id));
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
