package securechat.com.securechat.util;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Tanzeer on 11/22/2017.
 */

public class CommonUtil {

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    public static String getDeviceStatus(int deviceStatus) {
        Log.d("static", "Peer status :" + deviceStatus);
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }
    private static FileOutputStream getFileStreamtoWrite(Context context, String extenstion)
    {
        //String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(context.getFilesDir(), Constants.FILE_EXTENSION_PATH);
        if(!myDir.exists())
            myDir.mkdirs();
        String fname = extenstion +".key";
        File file = new File (myDir, fname);
        FileOutputStream out=null;
        try {
            out = new FileOutputStream(file);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return out;
    }
    public static OutputStream getOutputStreamtoWrite(Context context, String extenstion)
    {
        //String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(context.getFilesDir(), Constants.FILE_EXTENSION_PATH);
        if(!myDir.exists())
            myDir.mkdirs();
        String fname = extenstion +".key";
        File file = new File (myDir, fname);
        FileOutputStream out=null;
        try {
            out = new FileOutputStream(file);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return out;
    }
    public static InputStream getInputStreamForFile(Context context, String filename)
    {
        //String root = Environment.getExternalStorageDirectory().toString();
        File file = new File(context.getFilesDir(), Constants.FILE_EXTENSION_PATH+filename);

        if(file.exists())
        {
            Log.e("size"," "+ file.length());
        }
        else
        {
            Log.e("size"," not exists");
        }
        FileInputStream inputStream=null;
        try {
            inputStream= new FileInputStream(file);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return inputStream;
    }
    public static String getJsonFromMessage(String message, String eHash)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("message", message);
            jsonObject.put("hash", eHash);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return  jsonObject.toString();
    }
    public static String getMessageFromJSON(JSONObject jsonObject)
    {
        try {
          return  jsonObject.getString("message");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return  "";
    }
    public static String getHashFromJSON(JSONObject jsonObject)
    {
        try {
            return  jsonObject.getString("hash");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return  "";
    }
}
