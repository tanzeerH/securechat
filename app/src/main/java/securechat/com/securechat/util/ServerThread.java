package securechat.com.securechat.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import securechat.com.securechat.crypto.RSA;
import securechat.com.securechat.database.DataHelper;
import securechat.com.securechat.model.Message;
import securechat.com.securechat.model.ServerClass;

/**
 * Created by Tanzeer on 11/27/2017.
 */

public class ServerThread extends Thread {

    private Context mContext;
    public ServerThread(Context c)
    { mContext=c;

    }
    private void sendIntentAction(String message)
    {
        Intent intent=new Intent();
        intent.setAction(Constants.ACTION_NEW_STATUS);
        intent.putExtra("status",message);
        mContext.sendBroadcast(intent);
    }
    @Override
    public void run() {
        communicaion();
        super.run();
    }
    private void communicaion()
    {
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.port_number);
            Log.e("TAG","sever connection: waiting" );
            sendIntentAction("server: waiting for connection");

            Socket client = serverSocket.accept();
            ServerClass.setUp(serverSocket,client);

            Log.e("TAG"," sending server public file");
            sendIntentAction("server: sending public file");
            byte[] data = getBytesFromFile(RSA.PUBLIC_KEY_FILE);
            Log.e("TAG"," sending length: "+ data.length);
            ServerClass.getDataOutputStream().writeInt(data.length);
            ServerClass.getDataOutputStream().write(data);
           /* Log.e("TAG"," sending my public file");
            sendMyPublicKeyFile(ServerClass.getSocket().getOutputStream());
            Log.e("TAG"," receiving clients public file");
            receiveClientsPublicFile(ServerClass.getSocket().getInputStream());
            Log.e("TAG"," done with copy");*/
            sendIntentAction("server: receiving public file");
            DataInputStream dataInputStream =  new DataInputStream(ServerClass.getSocket().getInputStream());
            int len= dataInputStream.readInt();
            Log.e("TAG","len: "+ len );
            data=new byte[len];
            dataInputStream.read(data,0,len);
            Log.e("TAG"," read "+ len +" bytes");
            saveKeyToFile(data);
            while(true ) {
                sendIntentAction("server: waiting for message");
                Log.e("message","server: waiting" );


                //   byte[] packetData = new byte[ServerClass.getDataInputStream().readInt()];
                len= dataInputStream.readInt();
                Log.e("TAG", "len: " + len);
                if(len == -1)
                {
                    ServerClass.getSocket().close();
                    ServerClass.makeNull();
                    sendIntentAction("server: connection closed");
                    break;
                }
                else {

                    data = new byte[len];
                    dataInputStream.read(data, 0, len);
                    //Log.e("message","read raw" +val);
                    String val = RSA.encryptUsingMyPrivateKey(mContext, data);
                    Log.e("message", "read" + val);
                    Message message = new Message(val, System.currentTimeMillis(), Message.FLAG_RECEIVED_MESSAGE, CommonUtil.getDeviceName());
                    DataHelper.getInstance(mContext).insertMessage(message);
                }
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
}
