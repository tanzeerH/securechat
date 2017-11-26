package securechat.com.securechat.util;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import securechat.com.securechat.model.ServerClass;

/**
 * Created by Tanzeer on 11/22/2017.
 */

public class AsyncServer extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.port_number);
            Log.e("message","sever connection: waiting" );

            Socket client = serverSocket.accept();
            ServerClass.setUp(serverSocket,client);


           while(true ) {
               Log.e("message","server: waiting" );


            //   byte[] packetData = new byte[ServerClass.getDataInputStream().readInt()];
              String val= ServerClass.getDataInputStream().readUTF();
               Log.e("message","read" +val);
              //dataInputStream.close();
           }
            /*InputStream inputstream = client.getInputStream();
            String read= readInputStream(inputstream);
            Log.e("message"," read: "+ read);*/
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private String readInputStream(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString("UTF-8");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
       // Toast.makeText(getA)
    }
}
