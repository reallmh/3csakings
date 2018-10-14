package io.lmh.e.a3cs_akings.UIAdapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.List;

import UI.CircleTransformation;
import io.lmh.e.a3cs_akings.Model.Post;
import io.lmh.e.a3cs_akings.Profile.UserProfileActivity;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;
import io.lmh.e.a3cs_akings.comment.Comment;


/**
 * Created by E on 5/20/2018.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //declare vars
    //****************************delclare strings****************************/
    String coverName, userName, userId;

    //****************************declare objects**********************/
    private List<Post> posts;
    private Intent intent;
    private Context context;
    private SharedPreferences sharedPreferences;

    //post adapter constructer
    public PostAdapter(List<Post> postList, Context context) {
        this.context = context;
        this.posts = postList;
        setHasStableIds(true);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        View postImageView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item_image, parent, false);
        View errorPostView=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_err,parent,false);
        switch (viewType) {
            // if viewType 0,view holder for text only
            case 0:
                return new ViewHolderPost(postView);
            // if viewType 2,view holder for image with text
            case 2:
                return new ViewHolderImage(postImageView);
            case 1:
                return new ViewHolderError(errorPostView);
        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
        Post post = posts.get(position);
        if (post.getIsphoto().equals("yes")) {
            return 2;
        } else
        if(post.getIsphoto().equals("no")){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Post post = posts.get(position);
        //get user name and Id
        sharedPreferences = context.getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("USERName", "");
        userId = sharedPreferences.getString("USERID", "");


        //switch to desired view type eg. textonly,textwith image
        switch (holder.getItemViewType()) {
            case 0:
                final ViewHolderPost holderpost = (ViewHolderPost) holder;

                //initialize values to the view,bind values
                coverName = FunctionsStatic.getProfileImageUrl(post.getAcc_id());
                if (post.getLiked().equals("yes")) {
                    holderpost.love.setImageResource(R.drawable.ic_like_after);
                }
                Picasso.with(context).load(coverName).resize(90, 90)
                        .placeholder(R.drawable.ic_profile_loading)
                        .error(R.drawable.ic_profile)
                        .transform(new CircleTransformation())
                        .into(holderpost.profile_pic);
                holderpost.post_acc.setText(post.getAcc_name());
                holderpost.post_comment.setText(post.getComments());
                holderpost.post_like.setText(post.getLikes());
                holderpost.post_body.setText(post.getPost_body());
                holderpost.post_date.setText(" " + FunctionsStatic.getNiceTime(post.getDate()));
                //*****************end binding data to views
                //listeners
                holderpost.love.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if it has given love
                        if (post.getLiked().equals("yes")) {
                            System.out.println("double clicked");
                            String lov = holderpost.post_like.getText().toString();
                            int ilov = Integer.parseInt(lov);
                            int ansilov = ilov - 1;
                            holderpost.love.setImageResource(R.drawable.ic_like_before);
                            holderpost.post_like.setText(Integer.toString(ansilov));
                            post.setLiked("no");
                            holderpost.love.setEnabled(false);
                            //
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                        holderpost.love.setImageResource(R.drawable.ic_like_before);
                                        holderpost.post_like.setText(responseLike);
                                        post.setLikes(responseLike);
                                        post.setLiked("no");
                                        holderpost.love.setEnabled(true);
                                    } else if (s.equals("err")) {
                                        holderpost.love.setImageResource(R.drawable.ic_like_after);
                                        holderpost.post_like.setText(post.getLikes());
                                        post.setLiked("yes");
                                        holderpost.love.setEnabled(true);
                                    } else {
                                        holderpost.love.setImageResource(R.drawable.ic_like_after);
                                        holderpost.post_like.setText(post.getLikes());
                                        post.setLiked("yes");
                                        holderpost.love.setEnabled(true);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    holderpost.love.setImageResource(R.drawable.ic_like_after);
                                    holderpost.post_like.setText(post.getLikes());
                                    post.setLiked("yes");
                                    holderpost.love.setEnabled(true);
                                }
                            }

                            );
                            requestQueue.add(stringReq);
                            requestQueue.start();
                        } else {
                            String lov = holderpost.post_like.getText().toString();
                            int ilov = Integer.parseInt(lov);
                            int ansilov = ilov + 1;
                            holderpost.love.setImageResource(R.drawable.ic_like_after);
                            holderpost.post_like.setText(Integer.toString(ansilov));
                            post.setLiked("yes");
                            holderpost.love.setEnabled(false);
                            //
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                        holderpost.love.setImageResource(R.drawable.ic_like_after);
                                        holderpost.post_like.setText(responseLike);
                                        post.setLikes(responseLike);
                                        post.setLiked("yes");
                                        holderpost.love.setEnabled(true);
                                    } else if (s.equals("err")) {
                                        holderpost.love.setImageResource(R.drawable.ic_like_before);
                                        holderpost.post_like.setText(post.getLikes());
                                        post.setLiked("no");
                                        holderpost.love.setEnabled(true);
                                    } else {
                                        holderpost.love.setImageResource(R.drawable.ic_like_before);
                                        holderpost.post_like.setText(post.getLikes());
                                        post.setLiked("no");
                                        holderpost.love.setEnabled(true);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    holderpost.love.setImageResource(R.drawable.ic_like_before);
                                    holderpost.post_like.setText(post.getLikes());
                                    post.setLiked("no");
                                    holderpost.love.setEnabled(true);
                                }
                            }

                            );
                            requestQueue.add(stringReq);
                            requestQueue.start();
                        }
                    }
                });
                //on comment button click
                holderpost.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //put current post object to comment activity
                        Intent intent = new Intent(context, Comment.class);
                        intent.putExtra("post", post);
                        v.getContext().startActivity(intent);
                    }
                });


                holderpost.profile_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, UserProfileActivity.class);
                        intent.putExtra("profileId",post.getAcc_id());
                        v.getContext().startActivity(intent);
                    }
                });
                break;
            case 2:
                final ViewHolderImage holderpostimage = (ViewHolderImage) holder;
                coverName = FunctionsStatic.getPostUrl(post.getAcc_id()+post.getPost_id());
                Picasso.with(context).load(coverName).placeholder(R.mipmap.background_placeholder)
                        .error(R.mipmap.background_placeholder)
                        .resize(500, 400).centerCrop()
                        .into(holderpostimage.postImage);
                if (post.getLiked().equals("yes")) {
                    holderpostimage.love.setImageResource(R.drawable.ic_like_after);
                }
                coverName=FunctionsStatic.getProfileImageUrl(post.getAcc_id());
                Picasso.with(context).load(coverName).resize(90, 90).transform(new CircleTransformation()).into(holderpostimage.profile_pic);
                holderpostimage.post_acc.setText(post.getAcc_name());
                holderpostimage.post_comment.setText(post.getComments());
                holderpostimage.post_like.setText(post.getLikes());
                holderpostimage.post_body.setText(post.getPost_body());
                holderpostimage.post_date.setText(" " + FunctionsStatic.getNiceTime(post.getDate()));
                //*****************end binding data to views
                //listeners
                holderpostimage.love.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if it has given love
                        if (post.getLiked().equals("yes")) {
                            System.out.println("double clicked");
                            String lov = holderpostimage.post_like.getText().toString();
                            int ilov = Integer.parseInt(lov);
                            int ansilov = ilov - 1;
                            holderpostimage.love.setImageResource(R.drawable.ic_like_middle);
                            holderpostimage.post_like.setText(Integer.toString(ansilov));
                            post.setLiked("no");
                            holderpostimage.love.setEnabled(false);
                            //
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                        holderpostimage.love.setImageResource(R.drawable.ic_like_before);
                                        holderpostimage.post_like.setText(responseLike);
                                        post.setLikes(responseLike);
                                        post.setLiked("no");
                                        holderpostimage.love.setEnabled(true);
                                    } else if (s.equals("err")) {
                                        holderpostimage.love.setImageResource(R.drawable.ic_like_after);
                                        holderpostimage.post_like.setText(post.getLikes());
                                        post.setLiked("yes");
                                        holderpostimage.love.setEnabled(true);
                                    } else {
                                        holderpostimage.love.setImageResource(R.drawable.ic_like_after);
                                        holderpostimage.post_like.setText(post.getLikes());
                                        post.setLiked("yes");
                                        holderpostimage.love.setEnabled(true);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    holderpostimage.love.setImageResource(R.drawable.ic_like_after);
                                    holderpostimage.post_like.setText(post.getLikes());
                                    post.setLiked("yes");
                                    holderpostimage.love.setEnabled(true);
                                }
                            }

                            );
                            requestQueue.add(stringReq);
                            requestQueue.start();
                        } else {
                            String lov = holderpostimage.post_like.getText().toString();
                            int ilov = Integer.parseInt(lov);
                            int ansilov = ilov + 1;
                            holderpostimage.love.setImageResource(R.drawable.ic_like_middle);
                            holderpostimage.post_like.setText(Integer.toString(ansilov));
                            post.setLiked("yes");
                            holderpostimage.love.setEnabled(false);
                            //
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                        holderpostimage.love.setImageResource(R.drawable.ic_like_after);
                                        holderpostimage.post_like.setText(responseLike);
                                        post.setLikes(responseLike);
                                        post.setLiked("yes");
                                        holderpostimage.love.setEnabled(true);
                                    } else if (s.equals("err")) {
                                        holderpostimage.love.setImageResource(R.drawable.ic_like_before);
                                        holderpostimage.post_like.setText(post.getLikes());
                                        post.setLiked("no");
                                        holderpostimage.love.setEnabled(true);
                                    } else {
                                        holderpostimage.love.setImageResource(R.drawable.ic_like_before);
                                        holderpostimage.post_like.setText(post.getLikes());
                                        post.setLiked("no");
                                        holderpostimage.love.setEnabled(true);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    holderpostimage.love.setImageResource(R.drawable.ic_like_before);
                                    holderpostimage.post_like.setText(post.getLikes());
                                    post.setLiked("no");
                                    holderpostimage.love.setEnabled(true);
                                }
                            }

                            );
                            requestQueue.add(stringReq);
                            requestQueue.start();
                        }
                    }
                });
                //on comment button click
                holderpostimage.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //put current post object to comment activity
                        Intent intent = new Intent(context, Comment.class);
                        intent.putExtra("post", post);
                        v.getContext().startActivity(intent);
                    }
                });

                holderpostimage.profile_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, UserProfileActivity.class);
                        intent.putExtra("profileId",post.getAcc_id());
                        v.getContext().startActivity(intent);
                    }
                });

                break;
            case  1:
                final ViewHolderError viewHolderError = (ViewHolderError) holder;

        }


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    //method for adding post items
    public void addItem(Post post) {
        posts.add(0,post);
        notifyDataSetChanged();
    }
    public void addItemToBottom(Post post){
        posts.add(post);
        notifyDataSetChanged();
    }


    //view holder class for holding post without image views
    public class ViewHolderPost extends RecyclerView.ViewHolder {
        public TextView post_acc, post_body, post_date, post_like, post_comment;
        //initialize views
        ImageView profile_pic, love, comment, share;
        LinearLayout layout_post_body;

        //view holder constructer
        public ViewHolderPost(View view) {
            super(view);
            //bind views
            layout_post_body = (LinearLayout) view.findViewById(R.id.layout_post_body);
            profile_pic = (ImageView) view.findViewById(R.id.user_icon);
            love = (ImageView) view.findViewById(R.id.love);
            comment = (ImageView) view.findViewById(R.id.comment);
            post_acc = (TextView) view.findViewById(R.id.post_acc_name);
            post_body = (TextView) view.findViewById(R.id.post_body);
            post_like = (TextView) view.findViewById(R.id.post_like);
            post_comment = (TextView) view.findViewById(R.id.post_comment);
            post_date = (TextView) view.findViewById(R.id.post_date);
        }
    }


    public class ViewHolderError extends RecyclerView.ViewHolder {

        //view holder constructer
        public ViewHolderError(View view) {
            super(view);
            //bind views

        }
    }

    //view holder class for holding post image views
    public class ViewHolderImage extends RecyclerView.ViewHolder {
        //initialize views
        public TextView post_acc, post_body, post_date, post_like, post_comment;
        //initialize views
        ImageView profile_pic, love, comment, share, postImage;
        LinearLayout layout_post_body;

        //view holder constructer
        public ViewHolderImage(View view) {
            super(view);
            //bind views
            layout_post_body = (LinearLayout) view.findViewById(R.id.layout_post_body);
            postImage = (ImageView) view.findViewById(R.id.post_image);
            profile_pic = (ImageView) view.findViewById(R.id.user_icon);
            love = (ImageView) view.findViewById(R.id.love);
            comment = (ImageView) view.findViewById(R.id.comment);
            post_acc = (TextView) view.findViewById(R.id.post_acc_name);
            post_body = (TextView) view.findViewById(R.id.post_body);
            post_like = (TextView) view.findViewById(R.id.post_like);
            post_comment = (TextView) view.findViewById(R.id.post_comment);
            post_date = (TextView) view.findViewById(R.id.post_date);

        }
    }


}
