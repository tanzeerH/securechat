package securechat.com.securechat.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tanzeer on 11/25/2017.
 */

public class ServerClass {

    public static ServerSocket serverSocket;
    public static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public static void setUp(ServerSocket sc, Socket s)
    {
        try {
            serverSocket=sc;
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
        ServerClass.socket = socket;
    }

    public static DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public static void setDataInputStream(DataInputStream dataInputStream) {
        ServerClass.dataInputStream = dataInputStream;
    }

    public static DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public static void setDataOutputStream(DataOutputStream dataOutputStream) {
        ServerClass.dataOutputStream = dataOutputStream;
    }
    public static void makeNull()
    {
        serverSocket=null;
        socket = null;
        dataInputStream = null;
        dataOutputStream =  null;
    }
}
