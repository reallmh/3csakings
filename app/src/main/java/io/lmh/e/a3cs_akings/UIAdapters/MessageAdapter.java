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
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import UI.CircleTransformation;
import io.lmh.e.a3cs_akings.Message.MessageActivity;
import io.lmh.e.a3cs_akings.Model.Message;
import io.lmh.e.a3cs_akings.Model.MessageItem;
import io.lmh.e.a3cs_akings.Model.Post;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;

/**
 * Created by E on 8/6/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    private Context context;
    SharedPreferences sharedPreferences;

    public MessageAdapter(Context context, List<Message> mess) {
        this.context = context;
        this.messages = mess;
        setHasStableIds(true);
    }

    @Override
    public int getItemViewType(int position) {
        Message message=messages.get(position);
        sharedPreferences=context.getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
        String userId=sharedPreferences.getString("USERID","");

        if(message.getM_sender().equals(userId)){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sender= LayoutInflater.from(parent.getContext()).inflate(R.layout.send_msg_item, parent, false);
        View receiver=LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_msg_item,parent,false);
      switch (viewType){
          case 1:
              return new SendMessageViewHolder(sender);
          case 2:
             return new  ReceiveMessageViewHolder(receiver);
      }
      System.out.println("null is returned");
      return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           Message message=messages.get(position);
           System.out.println("messageg size"+messages.size());
           switch (holder.getItemViewType()){
               case 1:
                   final SendMessageViewHolder sendMessageViewHolder=(SendMessageViewHolder)holder;
                    sendMessageViewHolder.body.setText(message.getM_message());
                    break;
               case 2:
                   final ReceiveMessageViewHolder receiveMessageViewHolder=(ReceiveMessageViewHolder)holder;
                  receiveMessageViewHolder.body.setText(message.getM_message());
                  Picasso.with(context).load(FunctionsStatic.getProfileImageUrl(message.getM_sender()))
                          .transform(new CircleTransformation())
                          .error(R.drawable.ic_profile).resize(50,50).centerCrop()
                          .placeholder(R.drawable.ic_profile_loading)
                          .into(receiveMessageViewHolder.sender_pic);
                  break;
           }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    //view holder class for sending message
    public class SendMessageViewHolder extends RecyclerView.ViewHolder {
        //initialize views
        public TextView date, body;
        LinearLayout message_body;

        //view holder constructer
        public SendMessageViewHolder(View view) {
            super(view);
            //bind views
            body = (TextView) view.findViewById(R.id.txt_msg_body);
            date = (TextView) view.findViewById(R.id.msg_date);


        }
        //view holder for receiving messages
    }

    public class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {
        //initialize views
        public TextView date, body;
        LinearLayout message_body;
        ImageView sender_pic;

        //view holder constructer
        public ReceiveMessageViewHolder(View view) {
            super(view);
            //bind views

            body = (TextView) view.findViewById(R.id.txt_msg_body);
            date = (TextView) view.findViewById(R.id.msg_date);
            sender_pic=(ImageView)view.findViewById(R.id.msg_sender_pc);



        }



    }
    //method for adding post items
    public void addItem(Message message) {
        messages.add(0,message);
        notifyDataSetChanged();
    }
    public void addItemToBottom(Message message){
        messages.add(message);
        notifyDataSetChanged();
    }



}
