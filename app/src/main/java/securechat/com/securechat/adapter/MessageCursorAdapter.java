package securechat.com.securechat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import securechat.com.securechat.R;
import securechat.com.securechat.database.DataBaseOpenHelper;
import securechat.com.securechat.model.Message;

/**
 * Created by Tanzeer on 11/9/2017.
 */

public class MessageCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    public MessageCursorAdapter(Cursor cursor, Context context )
    {
        super(context, cursor, false);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        RelativeLayout rlSend= (RelativeLayout)view.findViewById(R.id.rl_send_message);
        RelativeLayout rlReceived= (RelativeLayout)view.findViewById(R.id.rl_received_message);

         int type =  cursor.getInt( cursor.getColumnIndex( DataBaseOpenHelper.KEY_TYPE ) );
        if(type == Message.FLAG_SEND_MESSAGE)
        {
            rlSend.setVisibility(View.VISIBLE);
            rlReceived.setVisibility(View.GONE);
            setUpOutgoingMesageUI(view,cursor);
        }
        else{
            rlSend.setVisibility(View.GONE);
            rlReceived.setVisibility(View.VISIBLE);
        }
    }
    private  void setUpOutgoingMesageUI(View view, Cursor cursor)
    {
        TextView tv= (TextView) view.findViewById(R.id.tv_message_send);
        String message =  cursor.getString( cursor.getColumnIndex( DataBaseOpenHelper.KEY_MESSAGE_BODY ) );
        tv.setText(message);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.chat_row, parent, false);
    }
}
