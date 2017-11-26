package securechat.com.securechat.util;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import securechat.com.securechat.model.ClientClass;
import securechat.com.securechat.model.ServerClass;

/**
 * Created by Tanzeer on 11/22/2017.
 */

public class AsyncClient extends AsyncTask<Void, Void, Void> {

    String message;
    public AsyncClient(String m)
    {
        message =  m;
    }

    @Override
    protected Void doInBackground(Void... params) {


            Log.e("TAG", "Client: creating.");
            Socket socket = new Socket();
            //  int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);

            try {
                //Log.d(WiFiDirectActivity.TAG, "Opening client socket - ");
                socket.bind(null);
                socket.connect((new InetSocketAddress(Constants.wifiP2pInfo.groupOwnerAddress, Constants.port_number)), 2000);
                Log.e("message","is Connected: "+socket.isConnected() );
                ClientClass.setUp(socket);
                while(true ) {
                    Log.e("message","client: waiting" );
                    DataInputStream dataInputStream =  new DataInputStream(ClientClass.getSocket().getInputStream());
                    //   byte[] packetData = new byte[ServerClass.getDataInputStream().readInt()];
                    String val= dataInputStream.readUTF();
                    Log.e("message","read" +val);
                    //dataInputStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage());
            } finally {


            }


        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }
  /*  private class DataReadingClass extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                InputStream inputStream = ServerClass.socket.getInputStream();
                while(inputStream.readIn)
                {

                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }*/
}
