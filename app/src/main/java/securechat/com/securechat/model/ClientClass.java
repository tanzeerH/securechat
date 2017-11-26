package securechat.com.securechat.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tanzeer on 11/25/2017.
 */

public class ClientClass {

    private static Socket socket = null;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public static void setUp(Socket s)
    {
        try {
            socket = s;
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        ClientClass.socket = socket;
    }

    public static DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public static void setDataInputStream(DataInputStream dataInputStream) {
        ClientClass.dataInputStream = dataInputStream;
    }

    public static DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public static void setDataOutputStream(DataOutputStream dataOutputStream) {
        ClientClass.dataOutputStream = dataOutputStream;
    }
    public static void makeNull()
    {
        socket = null;
        dataInputStream = null;
        dataOutputStream =  null;
    }
}
