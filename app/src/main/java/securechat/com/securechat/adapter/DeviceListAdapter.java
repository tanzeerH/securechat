package securechat.com.securechat.adapter;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import securechat.com.securechat.ChatActivity;
import securechat.com.securechat.R;
import securechat.com.securechat.util.CommonUtil;


public class DeviceListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<WifiP2pDevice> p2pDevices;
	private WifiP2pManager mManager;
	WifiP2pManager.Channel mChannel;



	public DeviceListAdapter(Context context, ArrayList<WifiP2pDevice> list, WifiP2pManager mMngr, WifiP2pManager.Channel channel) {
		this.mContext = context;
		this.p2pDevices =list;
		mManager= mMngr;
		mChannel = channel;
	}

	@Override
	public int getCount() {
		return p2pDevices.size();
	}

	@Override
	public Object getItem(int position) {
		return p2pDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView name;
		ImageView ivFlag;
		TextView tv_chat;
		TextView tvStatus;
		TextView tvConnect;
		TextView tvClose;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_device, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.tv_device);
			holder.tv_chat = (TextView) convertView.findViewById(R.id.tv_chat);
			holder.ivFlag=(ImageView)convertView.findViewById(R.id.iv_flag);
			holder.tvStatus = (TextView)convertView.findViewById(R.id.tv_status);
			holder.tvConnect = (TextView)convertView.findViewById(R.id.tv_connect);
			holder.tvClose = (TextView) convertView.findViewById(R.id.tv_close);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final WifiP2pDevice wifiP2pDevice=p2pDevices.get(position);

		String devName=p2pDevices.get(position).deviceName;
		if(devName.trim().length() == 0)
			devName = p2pDevices.get(position).deviceAddress;
		holder.name.setText(devName);
		holder.tvStatus.setText(CommonUtil.getDeviceStatus(p2pDevices.get(position).status));
		holder.tvConnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = wifiP2pDevice.deviceAddress;
				config.wps.setup = WpsInfo.PBC;
				connect(config);
				//Intent intent=new Intent(mContext,ChatActivity.class);
				//mContext.startActivity(intent);
			}
		});
		holder.tv_chat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e(" chat"," chat clicked");
				Intent intent=new Intent(mContext,ChatActivity.class);
				mContext.startActivity(intent);
			}
		});
		// status 0 means already connected. So, start chatting
		if(p2pDevices.get(position).status == 0)
		{
			holder.tvConnect.setVisibility(View.GONE);
			holder.tv_chat.setVisibility(View.VISIBLE);
			holder.tvClose.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.tvConnect.setVisibility(View.VISIBLE);
			holder.tv_chat.setVisibility(View.GONE);
			holder.tvClose.setVisibility(View.GONE);
		}
		holder.tvClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				disconnect();
			}
		});

		//holder.ivFlag.setImageResource(p2pDevices.get(position).getIconId());

		return convertView;
	}
	public void connect(final WifiP2pConfig config) {
		mManager.connect(mChannel,  config, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {
				// WiFiDirectBroadcastReceiver will notify us. Ignore for now.
				String address= config.deviceAddress;
				Toast.makeText(mContext, "Connect successfull.",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int reason) {
				Toast.makeText(mContext, "Connect failed. Retry.",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void disconnect() {

		mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

			@Override
			public void onFailure(int reasonCode) {
				Log.d("TAG", "Disconnect failed. Reason :" + reasonCode);
			}

			@Override
			public void onSuccess() {
				//fragment.getView().setVisibility(View.GONE);
			}

		});
	}
}
