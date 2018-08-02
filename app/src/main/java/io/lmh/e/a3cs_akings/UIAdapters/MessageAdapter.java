package io.lmh.e.a3cs_akings.UIAdapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.lmh.e.a3cs_akings.Model.Message;
import io.lmh.e.a3cs_akings.R;


/**
 * Created by E on 5/20/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    //initialize vars
    private List<Message> messages;
    private Intent intent;
    private Context context;
    private SharedPreferences sharedPreferences;

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Message message=messages.get(position);

        holder.id.setText(message.getM_id());
        holder.sender.setText(message.getM_sender());
        holder.receiver.setText(message.getM_receiver());
        holder.body.setText(message.getM_message());
        holder.date.setText(message.getM_date());
        //shared pref
        sharedPreferences= context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String userId=sharedPreferences.getString("USERID","");
        //check if the user id is current ID
        if(message.getM_sender().equals(userId)){
            holder.message_body.setBackgroundColor(Color.GRAY);

        }else {
            holder.message_body.setBackgroundColor(Color.GREEN);
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    //view holder class for holding message views
    public class MyViewHolder extends RecyclerView.ViewHolder {
        //initialize views
        public TextView id,sender,receiver,date,body;
        LinearLayout message_body;

        //view holder constructer
        public MyViewHolder(View view) {
            super(view);
            //bind views
            message_body=(LinearLayout)view.findViewById(R.id.message_body);
            body=(TextView)view.findViewById(R.id.txt_msg_body);
            id = (TextView) view.findViewById(R.id.txt_m_id);
            sender = (TextView) view.findViewById(R.id.txt_m_sender);
            receiver = (TextView) view.findViewById(R.id.txt_m_receiver);
            date=(TextView)view.findViewById(R.id.txt_m_date);

        }


    }
    //message adapter constructer
    public MessageAdapter(List<Message> accList, Context context) {
        this.context = context;
        this.messages = accList;
        setHasStableIds(true);
    }

    //method for adding message items
    public  void  addItem(Message message){
        messages.add(message);
        notifyDataSetChanged();

    }


}
