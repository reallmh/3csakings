package io.lmh.e.a3cs_akings.UIAdapters;

import android.content.Context;
import android.content.Intent;
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
import io.lmh.e.a3cs_akings.Model.UserAccount;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;

/**
 * Created by E on 5/19/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private List<Comment> commentList;
    private Context context;
    String picUrl;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {

            final Comment comment = commentList.get(position);
            picUrl= FunctionsStatic.getProfileImageUrl(comment.getAcc_id());
            Picasso.with(context).load(picUrl).transform(new CircleTransformation()).placeholder(R.drawable.ic_profile_loading).resize(50,50).into(holder.comment_profile);
            holder.commentbody.setText(comment.getComment_body());
            holder.name.setText(comment.getAcc_name());
            String niceDate=FunctionsStatic.getNiceTime(comment.getDate());
            holder.date.setText(niceDate);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView  name,commentbody,date;
        private ImageView comment_profile;
        public MyViewHolder(View view) {
            super(view);
            comment_profile=(ImageView)view.findViewById(R.id.comment_user_icon);
            name = (TextView) view.findViewById(R.id.comment_acc_name);
            date=(TextView)view.findViewById(R.id.comment_date);
            commentbody=(TextView)view.findViewById(R.id.comment_body);
        }


    }
    //useraccount adapter constructer
    public CommentAdapter(List<Comment> commentList,Context Mcontext) {
        this.commentList = commentList;
        this.context=Mcontext;
    }

    //adding account function
    public void addItem(Comment comment){
        commentList.add(comment);
        notifyDataSetChanged();
    }

    //removeing account function
    public void removeAt(int position){
        commentList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,commentList.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
