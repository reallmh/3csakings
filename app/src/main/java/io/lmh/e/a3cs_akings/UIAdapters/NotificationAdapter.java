package io.lmh.e.a3cs_akings.UIAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import UI.CircleTransformation;
import io.lmh.e.a3cs_akings.Model.Comment;
import io.lmh.e.a3cs_akings.Model.Notification;
import io.lmh.e.a3cs_akings.Model.UserAccount;
import io.lmh.e.a3cs_akings.Profile.UserProfileActivity;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;

/**
 * Created by E on 5/19/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<Notification> notifications;
    private Context context;
    String picUrl;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noti_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {

            final Notification notification = notifications.get(position);

            Picasso.with(context).load(FunctionsStatic.getProfileImageUrl(notification.getReacterid()))
                    .resize(80, 80)
                    .transform(new CircleTransformation())
                    .centerCrop()
                    .error(R.drawable.ic_profile)
                    .into(holder.profile_pic);

            if (notification.getType().equals("liked")){
                Picasso.with(context).load(R.drawable.ic_like_after)
                        .error(R.drawable.ic_like_after)
                        .into(holder.type_pic);
            }else if(notification.getType().equals("follow")){
                Picasso.with(context).load(R.drawable.ic_follow)
                        .error(R.drawable.ic_follow)
                        .into(holder.type_pic);
            }else if(notification.getType().equals("comment")){
                Picasso.with(context).load(R.drawable.ic_comment)
                        .error(R.drawable.ic_comment)
                        .into(holder.type_pic);
            }else{
                Picasso.with(context).load(R.drawable.ic_notice)
                        .error(R.drawable.ic_notice)
                        .into(holder.type_pic);
            }

            if(notification.getSeen().equals("1")){
                holder.noti_body.setBackgroundColor(Color.WHITE);
            }

            holder.name.setText(notification.getReactername());
            holder.body.setText(notification.getNotibody());
            holder.date.setText(notification.getDate());

            holder.noti_body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=null;
                    if (notification.getType().equals("liked")){

                    }else if(notification.getType().equals("follow")){
                        intent=new Intent(context, UserProfileActivity.class);
                        intent.putExtra("profileId",notification.getReacterid());
                        v.getContext().startActivity(intent);

                    }else if(notification.getType().equals("comment")){

                    }else{

                    }

                }
            });


        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView  name,body,date;
        private ImageView profile_pic,type_pic;
        private LinearLayout noti_body;
        public MyViewHolder(View view) {
            super(view);
            noti_body=(LinearLayout)view.findViewById(R.id.linear_noti_body);
             name=(TextView)view.findViewById(R.id.noti_profile_name);
             body=(TextView)view.findViewById(R.id.noti_body);
             date=(TextView)view.findViewById(R.id.noti_date);

             profile_pic=(ImageView)view.findViewById(R.id.notif_profile_pic);
             type_pic=(ImageView)view.findViewById(R.id.noti_img_type);
        }


    }
    //useraccount adapter constructer
    public NotificationAdapter(List<Notification> notiList, Context Mcontext) {
        this.notifications = notiList;
        this.context=Mcontext;
    }

    //adding account function
    public void addItem(Notification noti){
        notifications.add(noti);
        notifyDataSetChanged();
    }

    //removeing account function
    public void removeAt(int position){
        notifications.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,notifications.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
