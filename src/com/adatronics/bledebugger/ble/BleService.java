package com.adatronics.bledebugger.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adatronics.bledebugger.R;
import com.adatronics.bledebugger.model.BleDevice;
import com.adatronics.bledebugger.model.BtDeviceLab;
import com.adatronics.bledebugger.model.BleDeviceState;
import com.adatronics.bledebugger.model.BleGlobal;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author BojunPan@adatronics
 * 
 *         2014-4-4
 */
public class BleService extends ListActivity {
	
	private static final String TAG = "BleService";
	public static final String CONNECTED_DEVICE = "com.adatronics.bledebugger.connectedDevice";
	
	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	
	private BleDevice mConnectedDevice;

	private TextView mDeviceAddress;
	
	private class ServiceAdapter extends ArrayAdapter<BluetoothGattService> {

		public ServiceAdapter(ArrayList<BluetoothGattService> serviceList) {
			super(getApplicationContext(), 0, serviceList);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(com.adatronics.bledebugger.R.layout.list_item_service, null);
			}
			
			BluetoothGattService service = getItem(position);
			
			TextView nameView = (TextView) convertView.findViewById(R.id.service_name);
			nameView.setText(BleGlobal.lookup(service.getUuid().toString(), "Unknown Service"));
			
			TextView uuidView = (TextView) convertView.findViewById(R.id.service_uuid);
			uuidView.setText(service.getUuid().toString());
			
			return convertView;
		}		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bleservice);

		mConnectedDevice = BtDeviceLab.getInstance(getApplicationContext()).getCurrentBleDevice();
		if (mConnectedDevice ==  null) {
			Log.i(TAG, "the device is null");
		}
		mDeviceAddress = (TextView) findViewById(R.id.deviceAddress);
		mDeviceAddress.setText("Device Address: " + mConnectedDevice.getAddress());
		
		mConnectedDevice.connectDevice(getApplicationContext());
		
		while(!(mConnectedDevice.getCurrentState() == BleDeviceState.FOUNDSERVICES)) {
			
		}
		
		ServiceAdapter serviceAdapter = new ServiceAdapter(mConnectedDevice.getGattServiceList());
		setListAdapter(serviceAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView listView, View v, int position,
			long id) {

		mConnectedDevice.setCurrentService(position);
		
		Intent intent = new Intent(getApplicationContext(), BleCharacteristic.class);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}
}
