package com.adatronics.bledebugger.classic;

import java.util.ArrayList;

import com.adatronics.bledebugger.R;
import com.adatronics.bledebugger.RefreshableView;
import com.adatronics.bledebugger.RefreshableView.PullToRefreshListener;
import com.adatronics.bledebugger.model.BleDevice;
import com.adatronics.bledebugger.model.BtDeviceLab;
import com.adatronics.bledebugger.model.BtcDevice;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BtcScan extends ListActivity {
	
	private static final String TAG = "BtcScan";
	
	private BluetoothAdapter mBluetoothAdapter;
	private static final int REQUEST_ENABLE_BT = 0;

	private TextView mScanResults;
	private RefreshableView mRefreshableView;
	
	private BroadcastReceiver scanDevicesReceiver;
	private BroadcastReceiver scanFinishedReceiver; 
	
	private ArrayList<BtcDevice> deviceList;
	private DeviceAdapter deviceAdapter;
		
	private class DeviceAdapter extends ArrayAdapter<BtcDevice> {

		public DeviceAdapter(ArrayList<BtcDevice> deviceList) {
			super(getApplicationContext(), 0, deviceList);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(com.adatronics.bledebugger.R.layout.list_item_btc_scan, null);
			}
			
			BtcDevice device = getItem(position);
			
			TextView nameView = (TextView) convertView.findViewById(R.id.device_name);
			nameView.setText(device.getName());
			
			TextView addressView = (TextView) convertView.findViewById(R.id.device_address);
			addressView.setText(device.getAddress());
						
			return convertView;
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.btcscan);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter != null) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(i, REQUEST_ENABLE_BT);
			}
		} else {
			Toast.makeText(getApplicationContext(), "No Bluetooth Available", Toast.LENGTH_LONG).show();
		}
		
		mScanResults = (TextView) findViewById(R.id.btcScanResults);
		mRefreshableView = (RefreshableView) findViewById(R.id.btcRefreshableView);
		mRefreshableView.setOnRefreshListener(new PullToRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						scanDevices();
					}
				});		
			}
		}, 0);
	}
	
	private void scanDevices() {
		
		if (scanDevicesReceiver == null) {
			scanDevicesReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					String action = intent.getAction();
					if (BluetoothDevice.ACTION_FOUND.equals(action)) {
						BluetoothDevice device = intent
								.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);						
						BtDeviceLab.getInstance(getApplicationContext()).addDevice(new BtcDevice(device));
					}
				}
			};
		}
		
		if (scanFinishedReceiver == null) {
			scanFinishedReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					getListView().setEnabled(true);					
					
					unregisterReceiver(scanFinishedReceiver);
										
					BtDeviceLab mLab = BtDeviceLab.getInstance(getApplicationContext());

					final int i = mLab.getBtcSize();
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							mRefreshableView.finishRefreshing();
							
							deviceList = BtDeviceLab.getInstance(getApplicationContext()).getBtcDeviceList();
							deviceAdapter = new DeviceAdapter(deviceList);
							mScanResults.setText("Found " + i + " Devices");
							setListAdapter(deviceAdapter);
						}
					});
				}
			};
		}
		
		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		IntentFilter filter2 = new IntentFilter(
				BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		registerReceiver(scanDevicesReceiver, filter1);
		registerReceiver(scanFinishedReceiver, filter2);

		getListView().setEnabled(false);
		
		mScanResults.setText("Scaning ...");
		mBluetoothAdapter.startDiscovery();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(i, REQUEST_ENABLE_BT);
			finish();
			return;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mBluetoothAdapter.cancelDiscovery();
		if (scanDevicesReceiver != null) {
			try {
				unregisterReceiver(scanDevicesReceiver);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
