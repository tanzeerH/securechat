package securechat.com.securechat.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import securechat.com.securechat.crypto.RSA;
import securechat.com.securechat.database.DataHelper;
import securechat.com.securechat.model.ClientClass;
import securechat.com.securechat.model.Message;
import securechat.com.securechat.model.ServerClass;

/**
 * Created by Tanzeer on 11/22/2017.
 */

public class AsyncClient extends AsyncTask<Void, Void, Void> {

   Context mContext;
    public AsyncClient(Context c)
    {
        mContext=c;
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
                Log.e("TAG","reading int " );
                DataInputStream dataInputStream =  new DataInputStream(ClientClass.getSocket().getInputStream());
                int len= dataInputStream.readInt();
                Log.e("TAG","len: "+ len );
                byte[] data=new byte[len];
                dataInputStream.read(data,0,len);
                Log.e("TAG"," read "+ len +" bytes");
                saveKeyToFile(data);
                 data = getBytesFromFile(RSA.PUBLIC_KEY_FILE);
                Log.e("TAG"," sending length: "+ data.length);
                DataOutputStream dataOutputStream =  new DataOutputStream(ClientClass.getSocket().getOutputStream());
                dataOutputStream.writeInt(data.length);
                dataOutputStream.write(data);
               /* Log.e("TAG"," receiving servers public file");
                receiveSeversPublicFile(ClientClass.getSocket().getInputStream());
                Log.e("TAG"," sending my public file");
                sendMyPublicKeyFile(ClientClass.getSocket().getOutputStream());
                Log.e("TAG"," done with copy");*/
                while(true ) {
                    Log.e("message","client: waiting" );

                    //   byte[] packetData = new byte[ServerClass.getDataInputStream().readInt()];
                   // String val= dataInputStream.readUTF();
                     len= dataInputStream.readInt();
                    Log.e("TAG","len: "+ len );
                     data=new byte[len];
                    dataInputStream.read(data,0,len);
                    //Log.e("message","read raw" +val);
                    String val= RSA.encryptUsingMyPrivateKey(mContext,data);
                    Message message = new Message(val, System.currentTimeMillis(),Message.FLAG_RECEIVED_MESSAGE, CommonUtil.getDeviceName());
                    DataHelper.getInstance(mContext).insertMessage(message);
                    Log.e("message","read" +val);
                    //dataInputStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage());
            } finally {


            }


        return null;
    }
    private byte[] getBytesFromFile(String filename)
    {      File file=  new File(mContext.getFilesDir(),RSA.PUBLIC_KEY_FILE);
        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            return b;

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        }
        catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
        return  null;
    }
    private void receiveSeversPublicFile(InputStream inputStream)
    {
        try {
            final File f = new File(mContext.getFilesDir(),Constants.FILE_EXTENSION_PATH+"other.key");

            f.createNewFile();
            FileCopyUtility.copyFile(inputStream,new FileOutputStream(f));
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
    }
    private void sendMyPublicKeyFile(OutputStream outputStream)
    {
        InputStream inputStream=CommonUtil.getInputStreamForFile(mContext,"public.key");
        FileCopyUtility.copyFile(inputStream,outputStream);
        Log.e("TAG"," file copy done");
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }
    private void saveKeyToFile(byte[] bytes)
    {
        try {
            FileOutputStream fos = new FileOutputStream(new File(mContext.getFilesDir(), RSA.OTHER_KEY_FILE));
            fos.write(bytes);
            fos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
