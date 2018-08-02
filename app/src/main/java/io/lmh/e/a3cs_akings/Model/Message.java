package io.lmh.e.a3cs_akings.Model;

/**
 * Created by E on 5/20/2018.
 */

public class Message {
    private String m_id,m_sender,m_receiver,m_date,m_message;

    public String getM_message() {
        return m_message;
    }

    public void setM_message(String m_message) {
        this.m_message = m_message;
    }

    public Message(String m_id, String m_sender, String m_receiver, String m_date, String m_message) {
        this.m_id = m_id;
        this.m_sender = m_sender;
        this.m_receiver = m_receiver;
        this.m_date = m_date;
        this.m_message = m_message;
    }

    public Message() {
    }

    public String getM_id() {
        return m_id;
    }

    public String getM_sender() {
        return m_sender;
    }

    public String getM_receiver() {
        return m_receiver;
    }

    public String getM_date() {
        return m_date;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public void setM_date(String m_date) {
        this.m_date = m_date;
    }

    public void setM_receiver(String m_receiver) {
        this.m_receiver = m_receiver;
    }

    public void setM_sender(String m_sender) {
        this.m_sender = m_sender;
    }
}
