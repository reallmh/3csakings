package io.lmh.e.a3cs_akings.UIAdapters;

import android.content.Context;
import android.content.Intent;
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
import io.lmh.e.a3cs_akings.Model.MessageItem;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;

/**
 * Created by E on 8/6/2018.
 */

public class MessageItemAdapter extends RecyclerView.Adapter<MessageItemAdapter.MessageItemViewHolder> {
    private List<MessageItem> messageItems;
    private Context context;

    public MessageItemAdapter(Context context, List<MessageItem> messageItems) {
        this.context = context;
        this.messageItems = messageItems;
        setHasStableIds(true);
    }

    @Override
    public MessageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View messageItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageItemViewHolder(messageItemView);

    }

    @Override
    public void onBindViewHolder(MessageItemViewHolder holder, int position) {
        final MessageItem messageItem = messageItems.get(position);


        Picasso.with(context)
                .load(FunctionsStatic.getProfileImageUrl(messageItem.getReceiverId()))
                .resize(90, 90)
                .placeholder(R.drawable.ic_profile_loading)
                .error(R.drawable.ic_profile)
                .transform(new CircleTransformation()).into(holder.msgItemProfile);

        holder.senderName.setText(messageItem.getReceiver_name());
        holder.lastMessage.setText(messageItem.getLast_message());
        holder.date.setText(messageItem.getLast_message_time());

        //click listener
        holder.acc_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent=new Intent(context, MessageActivity.class);
                intent.putExtra("messageUserId",messageItem.getReceiverId());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }


    //view holder

    static class MessageItemViewHolder extends RecyclerView.ViewHolder {
        private TextView senderName, lastMessage, date;
        private ImageView msgItemProfile;
        private LinearLayout acc_item;

        public MessageItemViewHolder(View itemView) {
            super(itemView);
            acc_item=(LinearLayout)itemView.findViewById(R.id.acc_item);
            msgItemProfile = (ImageView) itemView.findViewById(R.id.msg_item_pc);
            senderName = (TextView) itemView.findViewById(R.id.msg_item_acc_name);
            lastMessage = (TextView) itemView.findViewById(R.id.msg_item_message);
            date = (TextView) itemView.findViewById(R.id.msg_item_date);
            msgItemProfile = (ImageView) itemView.findViewById(R.id.msg_item_pc);
        }
    }


}
