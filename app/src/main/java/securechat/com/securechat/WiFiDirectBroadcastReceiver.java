package securechat.com.securechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by Tanzeer on 10/21/2017.
 */

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;
   /* WifiP2pManager.PeerListListener myPeerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            Log.e("device size"," "+ wifiP2pDeviceList.getDeviceList().size());
            for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList())
            {
                Log.e("device name", device.deviceName+"  "+device.deviceAddress);
                String device_name= device.deviceName;
                if(device_name.equals("Android_86ff"))
                {

                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = device.deviceAddress;
                    mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                        @Override
                        public void onSuccess() {
                            Log.e("device name", " successfully added");
                        }

                        @Override
                        public void onFailure(int reason) {
                            //failure logic
                        }
                    });

                }
                if(device_name.equals("Galaxy A7"))
                {

                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = device.deviceAddress;
                    mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                        @Override
                        public void onSuccess() {
                            Log.e("device name", " successfully added");
                        }

                        @Override
                        public void onFailure(int reason) {
                            //failure logic
                        }
                    });

                }

                // device.deviceName
            }
        }
    };*/

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.e("message"," action: "+ action);
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.e("message"," p2p enabled ");
            } else {
                Log.e("message"," p2p not enabled ");
            }
            // Check to see if Wi-Fi is enabled and notify appropriate activity
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (mManager != null) {
                mManager.requestPeers(mChannel, mActivity);
            }
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // we are connected with the other device, request connection
                // info to find group owner IP
                mManager.requestConnectionInfo(mChannel, mActivity);
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }


}
