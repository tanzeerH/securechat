package securechat.com.securechat.database;

import android.content.Context;

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
}
