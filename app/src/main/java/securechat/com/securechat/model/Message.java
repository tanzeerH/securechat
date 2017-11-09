package securechat.com.securechat.model;

/**
 * Created by Tanzeer on 11/9/2017.
 */

public class Message {
    private int message_id;
    private String message;
    private long date;
    private int type;
    private String sender;
    public static int FLAG_RECEIVED_MESSAGE=  0;
    public static int FLAG_SEND_MESSAGE=  1;

    public int getMessage_id() {
        return message_id;
    }

    public Message(int message_id, String message, long date, int type, String sender) {
        this.message_id = message_id;
        this.message = message;
        this.date = date;
        this.type = type;
        this.sender = sender;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }
}
