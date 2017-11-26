package securechat.com.securechat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tanzeer on 11/9/2017.
 */

public class DataBaseOpenHelper extends SQLiteOpenHelper {

    public static String MESSAGE_TABLE_NAME="messages";
    public static String KEY_MESSAGE_ID="_id";
    public static String KEY_TYPE ="type";
    public static String KEY_MESSAGE_BODY="message_body";
    public static String KEY_SENDER ="sender";
    public static String KEY_DATE="date";
    private static  String SQL_CREATE_MESSAGE_TABLE=" create table "+ MESSAGE_TABLE_NAME+ " ( " + KEY_MESSAGE_ID +" integer primary key autoincrement, "
            + KEY_MESSAGE_BODY +" text not null, " + KEY_DATE +" text not null, "+ KEY_TYPE +" integer not null, " + KEY_SENDER+  " text not null )";
    public DataBaseOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
