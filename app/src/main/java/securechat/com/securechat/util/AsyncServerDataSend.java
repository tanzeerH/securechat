package securechat.com.securechat.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import securechat.com.securechat.model.ClientClass;
import securechat.com.securechat.model.ServerClass;

/**
 * Created by Tanzeer on 11/22/2017.
 */

public class AsyncServerDataSend extends AsyncTask<Void, Void, Void> {

    String messsage ;
    public AsyncServerDataSend(String m)
    {
        messsage=m;
    }
    @Override
    protected Void doInBackground(Void... params) {
        try {

            if(ClientClass.getSocket() != null && ClientClass.getDataOutputStream()!= null)
            {
                ClientClass.getDataOutputStream().writeBytes(messsage);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
