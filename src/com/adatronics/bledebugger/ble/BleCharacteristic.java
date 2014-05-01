package com.adatronics.bledebugger.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

public class BleCharacteristic extends ListActivity {
	private static final String TAG = "BleCharacter";

	private TextView mServiceName;
	private TextView mServiceUUID;

	private BleDevice mConnectedDevice;
	private BluetoothGattService mService;
		
	private class CharacteristicAdapter extends ArrayAdapter<BluetoothGattCharacteristic> {

		public CharacteristicAdapter(ArrayList<BluetoothGattCharacteristic> serviceList) {
			super(getApplicationContext(), 0, serviceList);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.list_item_char, null);
			}
			
			BluetoothGattCharacteristic characteristic = getItem(position);
			
			TextView nameView = (TextView) convertView.findViewById(R.id.characterName);
			nameView.setText(BleGlobal.lookup(characteristic.getUuid().toString(), "Unknown Characteristic"));
			
			TextView uuidView = (TextView) convertView.findViewById(R.id.characterUUID);
			uuidView.setText(characteristic.getUuid().toString());
			
			StringBuilder propertyString = new StringBuilder();
			
			propertyString.append("Properties: ");
			
			if (isCharacterisitcReadable(characteristic)) {
				propertyString.append("Read");
			}
			
			if (isCharacteristicWriteable(characteristic)) {
				if (propertyString.length() != 0) {
					propertyString.append(" . ");
				}
				propertyString.append("Writable");
			}
			
			if (isCharacterisiticNotifiable(characteristic)) {
				if (propertyString.length() != 0) {
					propertyString.append(" . ");
				}
				propertyString.append("Notify");
			}
			
			TextView propertyView = (TextView) convertView.findViewById(R.id.characterProp);
			propertyView.setText(propertyString.toString());
			
			return convertView;
		}		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blecharacteristic);
		
		mServiceName = (TextView) findViewById(R.id.serviceName);
		mServiceUUID = (TextView) findViewById(R.id.serviceUUID);
		
		mConnectedDevice = BtDeviceLab.getInstance(getApplicationContext()).getCurrentBleDevice();
		mConnectedDevice.analyzeService();
		
		mService = mConnectedDevice.getCurrentService();

		mServiceName.setText(BleGlobal.lookup(mService.getUuid().toString(), "Unknown Service"));
		mServiceUUID.setText(mService.getUuid().toString());
		
		while (mConnectedDevice.getCurrentState() != BleDeviceState.FOUNDCHARACTERISTICS) {

		}

		CharacteristicAdapter mAdapter = new CharacteristicAdapter(mConnectedDevice.getCharacteristicList());
		setListAdapter(mAdapter);
	}

	
	@Override
	protected void onListItemClick(ListView listView, View v, int position,
			long id) {
		
		mConnectedDevice.setCurrentCharacteristic(position);
		
		Intent intent = new Intent(this, BleData.class);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}

	public boolean isCharacterisitcReadable(BluetoothGattCharacteristic pChar) {
		return ((pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
	}

	public boolean isCharacteristicWriteable(BluetoothGattCharacteristic pChar) {
		return (pChar.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
	}

	public boolean isCharacterisiticNotifiable(BluetoothGattCharacteristic pChar) {
		return (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
	}
}
