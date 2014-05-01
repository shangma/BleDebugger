package com.adatronics.bledebugger.ble;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import com.adatronics.bledebugger.R;
import com.adatronics.bledebugger.model.BleDevice;
import com.adatronics.bledebugger.model.BtDeviceLab;
import com.adatronics.bledebugger.model.BleDeviceState;
import com.adatronics.bledebugger.model.BleGlobal;

import android.R.string;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BleData extends Activity implements Observer {
	private static final String TAG = "BleData";

	private TextView mRssi;
	private TextView mCharacterName;
	private TextView mConnectionState;
	private TextView mUuid;
	private TextView mProperty;
	
	private Button mWriteButton;
	private EditText Hex;
	private EditText ASCII;
	private BluetoothDevice device;
	
	private boolean write_flag = false;
	private boolean read_flag = false;
	private boolean notify_flag = false;
	
	private BleDevice mConnectedDevice;
	private BluetoothGatt mBluetoothGatt;
	private BluetoothGattService mService;
	private BluetoothGattCharacteristic mCharacteristic;
	
	private Boolean isReading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bledata);
		
		mCharacterName = (TextView) findViewById(R.id.ble_character);
		mUuid = (TextView) findViewById(R.id.UUID);
		mConnectionState = (TextView) findViewById(R.id.connection_state);
		mProperty = (TextView) findViewById(R.id.properties);
		mRssi = (TextView) findViewById(R.id.rssi);
		
		Hex = (EditText) findViewById(R.id.Hex);
		ASCII = (EditText) findViewById(R.id.ASCII);
		mWriteButton = (Button) findViewById(R.id.write);

		mConnectedDevice = BtDeviceLab.getInstance(getApplicationContext()).getCurrentBleDevice();
		mConnectedDevice.addObserver(this);
		mBluetoothGatt = mConnectedDevice.getBluetoothGatt();
		mService = mConnectedDevice.getCurrentService();
		mCharacteristic = mConnectedDevice.getCurrentCharacteristic();
		
		
		mCharacterName.setText(BleGlobal.lookup(mCharacteristic.getUuid().toString(), "Unknow Characteristic"));
		mUuid.setText("UUID: " + mCharacteristic.getUuid().toString().toUpperCase());
		setPropertyString();
		updateConnectionState("Connected");

		Hex.setEnabled(false);
		ASCII.setEnabled(false);
		mWriteButton.setVisibility(View.INVISIBLE);
		
		mBluetoothGatt.readCharacteristic(mCharacteristic);
		mBluetoothGatt.readRemoteRssi();
		isReading = true;
		
	}
	
	private void setPropertyString() {
		
		StringBuilder propertyString = new StringBuilder();
		
		propertyString.append("Properties: ");
		
		if (isCharacterisitcReadable(mCharacteristic)) {
			propertyString.append("Read");
		}
		
		if (isCharacteristicWriteable(mCharacteristic)) {
			if (propertyString.length() != 0) {
				propertyString.append(" . ");
			}
			propertyString.append("Writable");
			Log.i(TAG, "enable button 1");
			setWritableVisible();
		}
		
		if (isCharacterisiticNotifiable(mCharacteristic)) {
			if (propertyString.length() != 0) {
				propertyString.append(" . ");
			}
			propertyString.append("Notify");
		}
		
		updateProperties(propertyString.toString());
	}

	public static boolean isCharacterisitcReadable(BluetoothGattCharacteristic pChar) {
		return ((pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
	}

	public static boolean isCharacteristicWriteable(BluetoothGattCharacteristic pChar) {
		return (pChar.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
	}

	public boolean isCharacterisiticNotifiable(BluetoothGattCharacteristic pChar) {
		return (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
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
		isReading = false;
		Log.i(TAG, "onStop");
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	private void setWritableVisible() {
		Log.i(TAG, "enable button 2");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mWriteButton.setVisibility(View.VISIBLE);
				ASCII.setEnabled(true);
			}
		});
	}

	public void onBtnWrite(View v) {
		String data = "";
		data = ASCII.getText().toString();
		mCharacteristic.setValue(data.getBytes());
		mBluetoothGatt.writeCharacteristic(mCharacteristic);
		Toast.makeText(this, "String Value Updated", Toast.LENGTH_SHORT).show();
	}


	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		if (data instanceof String) {
			String mixedString = (String) data;
			String[] values =  mixedString.split(":");
		
			String hexString = values[0];
			String asciiString = values[1];
			
			updateHex(hexString);
			updateAscii(asciiString);
			
		} else if (data instanceof Integer) {
			
			int value  = (Integer)data;
			updateRssi(mBluetoothGatt, value);			
		}
	}
	
	private void updateHex(final String hex) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Hex.setText(hex);
			}
		});
	}

	private void updateAscii(final String ascii) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ASCII.setText(ascii);
			}
		});
	}
	
	private void updateRssi(final BluetoothGatt gatt, final int rssi) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mRssi.setText("Rssi: " + String.valueOf(rssi));
				if (isReading) {
					gatt.readRemoteRssi();
				}
			}
		});
	}

	private void updateProperties(final String properties) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mProperty.setText(properties);
			}
		});
	}
	
	
	private void updateConnectionState(final String state) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mConnectionState.setText("State: " + state);
			}
		});
	}


}
