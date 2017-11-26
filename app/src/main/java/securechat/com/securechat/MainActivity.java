package securechat.com.securechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import securechat.com.securechat.adapter.DeviceListAdapter;
import securechat.com.securechat.crypto.RSA;
import securechat.com.securechat.model.ClientClass;
import securechat.com.securechat.model.ServerClass;
import securechat.com.securechat.util.AsyncServer;
import securechat.com.securechat.util.AsyncClient;
import securechat.com.securechat.util.Constants;

public class MainActivity extends AppCompatActivity implements WifiP2pManager.ConnectionInfoListener,WifiP2pManager.PeerListListener {

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    private ListView lvDevices;
    private DeviceListAdapter deviceListAdapter;
    private ArrayList<WifiP2pDevice> wifiP2pDevices=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvDevices=(ListView) findViewById(R.id.lv_devices);
        Button btn=(Button)findViewById(R.id.btn);
        initKeysForEncryption();
        initialize();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mManager != null) {
                    mManager.requestPeers(mChannel,MainActivity.this);
                }
            }
        });


        //just sample code
        //Intent intent=new Intent(MainActivity.this,ChatActivity.class);
      //  startActivity(intent);



    }

    private void initialize()
    {
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("message"," found");
            }

            @Override
            public void onFailure(int reasonCode) {

            }
        });
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

        wifiP2pDevices.clear();
        for (WifiP2pDevice device : peers.getDeviceList()) {

            wifiP2pDevices.add(device);
        }
        deviceListAdapter=new DeviceListAdapter(MainActivity.this,wifiP2pDevices,mManager,mChannel);
        lvDevices.setAdapter(deviceListAdapter);

    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
       registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Constants.wifiP2pInfo=info;

        Toast.makeText(MainActivity.this," in "+ info.groupOwnerAddress +"  "+ info.isGroupOwner +"  ", Toast.LENGTH_SHORT).show();
        if (info.groupFormed && info.isGroupOwner) {

                new AsyncServer().execute();

        }
        else if( info.groupFormed)
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  //  new AsyncClient("").execute();
                   // Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                   // startActivity(intent);
                }
            }, 3000);
        }
    }
    private void initKeysForEncryption()
    {
        if (!RSA.areKeysPresent(MainActivity.this)) {
            // Method generates a pair of keys using the RSA algorithm and stores it
            // in their respective files
            RSA.generateKey(MainActivity.this);
            Log.e("encryption:","encryption key generated");
        }
        else
        {
            Log.e("encryption:","encryption key exists");
        }
        testEncryption();
    }
    private void testEncryption()
    {
        RSA.testEncryption(MainActivity.this);
    }

}
