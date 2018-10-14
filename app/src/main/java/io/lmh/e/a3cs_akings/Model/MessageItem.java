package io.lmh.e.a3cs_akings.Model;

/**
 * Created by E on 8/6/2018.
 */

public class MessageItem {
    private String id, receiverId, receiver_name, last_message, last_message_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getLast_message_time() {
        return last_message_time;
    }

    public MessageItem(String id, String receiverId, String receiver_name, String last_message, String last_message_time) {
        this.id = id;
        this.receiverId = receiverId;
        this.receiver_name = receiver_name;
        this.last_message = last_message;
        this.last_message_time = last_message_time;
    }

    public void setLast_message_time(String last_message_time) {
        this.last_message_time = last_message_time;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }
}
