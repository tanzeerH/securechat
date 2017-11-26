package securechat.com.securechat.util;

import android.net.wifi.p2p.WifiP2pInfo;

import java.io.File;
import java.net.Socket;

/**
 * Created by Tanzeer on 11/9/2017.
 */

public class Constants {

    public static WifiP2pInfo wifiP2pInfo = null;
    public static int port_number = 8888;
    public static String ACTION_NEW_MESSAGE = "intent.action.newmessge";
    public static final String FILE_EXTENSION_PATH= "SecureChat"+ File.separator+"extension"+File.separator;
}
