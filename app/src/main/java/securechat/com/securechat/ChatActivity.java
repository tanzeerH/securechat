package securechat.com.securechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import securechat.com.securechat.adapter.MessageCursorAdapter;
import securechat.com.securechat.crypto.RSA;
import securechat.com.securechat.database.DataHelper;
import securechat.com.securechat.model.ClientClass;
import securechat.com.securechat.model.Message;
import securechat.com.securechat.model.ServerClass;
import securechat.com.securechat.util.AsyncClient;
import securechat.com.securechat.util.AsyncClientDataReceive;
import securechat.com.securechat.util.AsyncServer;
import securechat.com.securechat.util.AsyncServerDataSend;
import securechat.com.securechat.util.CommonUtil;
import securechat.com.securechat.util.Constants;

public class ChatActivity extends AppCompatActivity {


    private ImageButton imgBtnSend;
    private EditText edtMessage;
    private DataHelper dataHelper;
    private MessageCursorAdapter messageCursorAdapter;
    private ListView lvMessage;
    private TextView tvStatus;
    BroadcastReceiver onNewMessageListner=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Log.e("message", " intent received");
            setAdapter();
            //finish();
        }
    };
    BroadcastReceiver onNewStatusListner=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Log.e("message", " status received");
            String msessage=intent.getStringExtra("status");
            Constants.RECENT_STATUS=msessage;
            tvStatus.setText(msessage);
            if(msessage.equals("server: connection closed"))
                finish();
          //  setAdapter();
            //finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        dataHelper = DataHelper.getInstance(ChatActivity.this);
        edtMessage = (EditText)findViewById(R.id.et_text);
        imgBtnSend = (ImageButton)findViewById(R.id.iv_send);
        imgBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
       lvMessage = (ListView)findViewById(R.id.lv_message);
        tvStatus=(TextView)findViewById(R.id.tv_status);

        setAdapter();
        createClinet();
        setIntentFilter();


    }

    @Override
    protected void onResume() {
        super.onResume();
        tvStatus.setText(Constants.RECENT_STATUS);
    }

    private void setIntentFilter()
    {
        IntentFilter intentFilter=new IntentFilter(Constants.ACTION_NEW_MESSAGE);
        registerReceiver(onNewMessageListner,intentFilter);

        intentFilter=new IntentFilter(Constants.ACTION_NEW_STATUS);
        registerReceiver(onNewStatusListner,intentFilter);

    }
    private  void setAdapter()
    {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                messageCursorAdapter = new MessageCursorAdapter(
                        dataHelper.getAllMessages(),   ChatActivity.this);

                lvMessage.setAdapter(messageCursorAdapter);
            }

        });
    }
    private void createClinet()
    {
        if(Constants.wifiP2pInfo != null) {
            if (Constants.wifiP2pInfo.groupFormed && Constants.wifiP2pInfo.isGroupOwner) {

                //new AsyncServer().execute();

            } else {
                new AsyncClient(ChatActivity.this).execute();
            }
        }
    }
    private void sendMessage(){
        String myText= edtMessage.getText().toString();
        if(myText != null && myText.trim().length()>0)
        {
            Message message = new Message(myText, System.currentTimeMillis(),Message.FLAG_SEND_MESSAGE, CommonUtil.getDeviceName());
            //temp. will remove
          //  dataHelper.insertMessage(message);
            if(Constants.wifiP2pInfo != null)
            {
                if (Constants.wifiP2pInfo.groupFormed && Constants.wifiP2pInfo.isGroupOwner) {
                    Log.e("message", "  insert: server");
                    try {
                        byte [] data=RSA.encryptUsingOthersPublicKey(ChatActivity.this,myText);
                        ServerClass.getDataOutputStream().writeInt(data.length);
                        ServerClass.getDataOutputStream().write(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.e("message", "  insert: client");
                    try {
                        byte [] data=RSA.encryptUsingOthersPublicKey(ChatActivity.this,myText);
                        ClientClass.getDataOutputStream().writeInt(data.length);
                        ClientClass.getDataOutputStream().write(data);
                      //  ClientClass.getDataOutputStream().writeUTF(RSA.encryptUsingOthersPublicKey(ChatActivity.this,myText));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

              //  new AsyncClient(myText).execute();
                long res= dataHelper.insertMessage(message);
                Log.e("message","  insert: "+ res);
                edtMessage.setText("");

            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onNewMessageListner);
        unregisterReceiver(onNewStatusListner);
        if(ClientClass.getDataOutputStream() != null)
        {
            try {
                ClientClass.getDataOutputStream().writeInt(-1);
                ClientClass.getSocket().close();
                ClientClass.makeNull();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
