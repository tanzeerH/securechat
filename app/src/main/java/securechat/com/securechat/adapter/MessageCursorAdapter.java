package securechat.com.securechat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Tanzeer on 11/9/2017.
 */

public class MessageCursorAdapter extends CursorAdapter {

    public MessageCursorAdapter(Cursor cursor, Context context )
    {
        super(context, cursor, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }
}
