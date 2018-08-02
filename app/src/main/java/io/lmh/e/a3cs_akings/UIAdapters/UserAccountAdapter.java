package io.lmh.e.a3cs_akings.UIAdapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.List;

import UI.CircleTransformation;
import io.lmh.e.a3cs_akings.Model.UserAccount;
import io.lmh.e.a3cs_akings.Profile.UserProfileActivity;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;

/**
 * Created by E on 5/19/2018.
 */

public class UserAccountAdapter extends RecyclerView.Adapter<UserAccountAdapter.MyViewHolder> {
    private List<UserAccount> accountsList;
    private Context context;
    String userId, followingId,userName;
    SharedPreferences sharedPreferences;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {

            final UserAccount account = accountsList.get(position);
            sharedPreferences = context.getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
            userId = sharedPreferences.getString("USERID", "");
            userName=sharedPreferences.getString("USERName","");

            //binding image to pic
            Picasso.with(context).load(FunctionsStatic.getProfileImageUrl(account.getUs_id())).error(R.mipmap.avator_placeholder).placeholder(R.mipmap.avator_placeholder).transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).resize(50, 50).centerCrop().into(holder.profile_pic);
            //bind text view
            holder.name.setText(account.getUs_name());
            if(account.getUs_id().equals(userId)){
                holder.followButton.setEnabled(false);
            }else {
            if (account.getUs_followed().equals("yes")) {
                holder.followButton.setText("unfollow");
            } else {
                holder.followButton.setText("follow");
            }}
            followingId=account.getUs_id();

            //follow button onclick
            holder.followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.followButton.getText().equals("follow")) {
                        //
                        holder.followButton.setText("unfollow");
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        String url = VarStatic.getHostName() + "/follow/follow.php?userId=" + URLEncoder.encode(userId) +
                                "&following=" + URLEncoder.encode(account.getUs_id())+"&username="+URLEncoder.encode(userName);
                        StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (!(s.equals("err"))) {
                                    holder.followButton.setText("unfollow");
                                } else {
                                    holder.followButton.setText("follow");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                holder.followButton.setText("follow");
                            }
                        }

                        );
                        requestQueue.add(stringReq);
                        requestQueue.start();
                    }else {
                        holder.followButton.setText("follow");
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        String url = VarStatic.getHostName() + "/follow/unfollow.php?userId=" + URLEncoder.encode(userId) +
                                "&following=" + URLEncoder.encode(followingId);
                        StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (!(s.equals("err"))) {
                                    holder.followButton.setText("follow");
                                } else {
                                    holder.followButton.setText("unfollow");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                holder.followButton.setText("unfollow");
                            }
                        }

                        );
                        requestQueue.add(stringReq);
                        requestQueue.start();
                    }
                }
            });
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, UserProfileActivity.class);
                    intent.putExtra("profileId",account.getUs_id());
                    v.getContext().startActivity(intent);
                }
            });
            holder.profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, UserProfileActivity.class);
                    intent.putExtra("profileId",account.getUs_id());
                    v.getContext().startActivity(intent);

                }
            });


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public int getItemCount() {
        return accountsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView profile_pic;
        private Button followButton;
        private LinearLayout accItem;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.acc_name);
            profile_pic = (ImageView) view.findViewById(R.id.acc_profile_pic);
            followButton = (Button) view.findViewById(R.id.acc_follow_button);
        }


    }

    //useraccount adapter constructer
    public UserAccountAdapter(List<UserAccount> accList, Context Mcontext) {
        this.accountsList = accList;
        this.context = Mcontext;
    }

    //adding account function
    public void addItem(UserAccount account) {
        accountsList.add(account);
        notifyDataSetChanged();
    }

    //removeing account function
    public void removeAt(int position) {
        accountsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, accountsList.size());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
