package securechat.com.securechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import securechat.com.securechat.model.Message;
import securechat.com.securechat.util.Constants;

/**
 * Created by Tanzeer on 11/9/2017.
 */

public class DataHelper {

    private static final String DATABASE_NAME = "securechat.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    private static DataHelper instance = null;
    DataBaseOpenHelper dOpenHelper;

    private DataHelper(Context context) {
        this.context = context.getApplicationContext();
        dOpenHelper = new DataBaseOpenHelper(context, DATABASE_NAME,
                DATABASE_VERSION);
    }

    public void closeDbOpenHelper() {
        dOpenHelper.close();
        instance = null;
    }

    public static DataHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataHelper(context);
        }
        return instance;
    }
    public   long insertMessage(Message message)
    {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ContentValues values =  new ContentValues();
        try {
            db = dOpenHelper.getWritableDatabase();
            values.put(DataBaseOpenHelper.KEY_TYPE,message.getType());
            values.put(DataBaseOpenHelper.KEY_MESSAGE_BODY,message.getMessage());
            values.put(DataBaseOpenHelper.KEY_SENDER,message.getSender());
            values.put(DataBaseOpenHelper.KEY_DATE,message.getDate());
           long val= db.insert(DataBaseOpenHelper.MESSAGE_TABLE_NAME,null,values);
            sendIntentAction();
            return val;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return -1L;

    }
    public  Cursor getAllMessages()
    {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ContentValues values =  new ContentValues();
        try {
            db = dOpenHelper.getReadableDatabase();
            cursor =db.query(DataBaseOpenHelper.MESSAGE_TABLE_NAME, new String[] { DataBaseOpenHelper.KEY_MESSAGE_ID,  DataBaseOpenHelper.KEY_TYPE,
                            DataBaseOpenHelper.KEY_MESSAGE_BODY, DataBaseOpenHelper.KEY_DATE, DataBaseOpenHelper.KEY_SENDER }, null, null, null, null,
                    null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if(cursor != null)
                Log.e("cursor"," "+cursor.getCount());
        }
        return  cursor;
    }
    private void sendIntentAction()
    {
        Intent intent=new Intent();
        intent.setAction(Constants.ACTION_NEW_MESSAGE);
        context.sendBroadcast(intent);
    }
}
