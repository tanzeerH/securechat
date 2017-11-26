package securechat.com.securechat.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import securechat.com.securechat.model.ClientClass;
import securechat.com.securechat.model.ServerClass;

/**
 * Created by Tanzeer on 11/22/2017.
 */

public class AsyncClientDataReceive extends AsyncTask<Void, Void, Void> {



    @Override
    protected Void doInBackground(Void... params) {
        if( ClientClass.getSocket() != null && ClientClass.getDataOutputStream() != null  ) {

            try {
                while (true) {
                    DataInputStream dataInputStream = new DataInputStream(ClientClass.getSocket().getInputStream());
                    //   byte[] packetData = new byte[ServerClass.getDataInputStream().readInt()];
                    String val = dataInputStream.readUTF();
                    Log.e("message", "read" + val);
                    //dataInputStream.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}
