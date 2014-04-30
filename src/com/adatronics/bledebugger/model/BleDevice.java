package com.adatronics.bledebugger.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.adatronics.bledebugger.R;

import android.R.integer;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

public class BleDevice extends Observable {
	
	private static final String TAG = "BleDevice";

	private String mName;
	private String mAddress;
	private int mRssi;
	
	private BluetoothDevice mDevice;
	private BluetoothGatt mBluetoothGatt;
	private BleDeviceState mCurrentState;
		
	private int mCurrentGattService;
	private int mCurrentCharacteristic;
	private String mCharacteristicRead;
	
	private ArrayList<BluetoothGattService> mServiceList;
	private ArrayList<BluetoothGattCharacteristic> mCharacteristicList;
	
	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			// TODO Auto-generated method stub
			if (status == BluetoothGatt.GATT_SUCCESS
					&& newState == BluetoothProfile.STATE_CONNECTED) {
				
				gatt.discoverServices();
				mCurrentState = BleDeviceState.CONNECTED;

			} else if (status == BluetoothGatt.GATT_SUCCESS
					&& newState == BluetoothProfile.STATE_DISCONNECTED) {
				
				Log.i(TAG, "Lose connection!!!");
				mCurrentState = BleDeviceState.DISCONNECTED;				
			} else if (status != BluetoothGatt.GATT_SUCCESS) {
				gatt.disconnect();
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			Log.d(TAG, "Services Discovered: " + status + "   "
					+ BluetoothGatt.GATT_SUCCESS);
			
			if (status == BluetoothGatt.GATT_SUCCESS) {
				mServiceList = new ArrayList<BluetoothGattService>(gatt.getServices());
				mCurrentState = BleDeviceState.FOUNDSERVICES;
			}
			
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			int offset = 0;
			String ascii = characteristic.getStringValue(offset);
			byte[] hex = characteristic.getValue();
			StringBuilder hexString = new StringBuilder();
			if (hex != null && hex.length > 0) {
				for (byte byteChar : hex)
					hexString.append(String.format("%02X ", byteChar));
			}
			hexString.append(':');
			hexString.append(ascii);
			notifyCharacteristicRead(hexString.toString());			
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onCharacteristicWrite");
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onDescriptorRead");
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onDescriptorWrite");
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			// TODO Auto-generated method stub
			notifyRssiValue(rssi);
			Log.i(TAG, "onReadRemoteRssi");
		}
		
	};
	
	public BleDevice(BluetoothDevice device, Integer rssi) {
		
		mDevice = device;
		
		if (device.getName() != null) {
			mName = device.getName();
		} else {
			mName = "unknown";
		}
		
		if (device.getAddress() != null) {
			mAddress = device.getAddress();
		}
		
		mRssi = rssi;
		mCurrentState = BleDeviceState.DISCONNECTED;
		mBluetoothGatt = null;
		
		mServiceList = null;
		mCharacteristicList = null;
		
		mCurrentCharacteristic = -1;
		mCurrentGattService = -1;
	}
	
	public void connectDevice(Context appContext) {
		mBluetoothGatt = mDevice.connectGatt(appContext, false, mGattCallback);
		mCurrentState = BleDeviceState.CONNECTED;
	}
	
	public void analyzeService() {

		mCharacteristicList = new ArrayList<BluetoothGattCharacteristic>(mServiceList.get(mCurrentGattService).getCharacteristics());
		// don't remove this line
		mCurrentState = BleDeviceState.FOUNDCHARACTERISTICS;
	}
	
	public void disconnectDevice() {
		if (mBluetoothGatt != null) {
			mBluetoothGatt.close();
		}
	}
	
	public BluetoothGatt getBluetoothGatt() {
		return mBluetoothGatt;
	}
	
	public BluetoothDevice getNativeDevice() {
		return mDevice;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}
	
	public String getAddress() {
		return mAddress;
	}
	
	public void setAddress(String newAddress) {
		mAddress= newAddress;
	}
	
	public int getRssi() {
		return mRssi;
	}
	
	public void setRssi(int newRssi) {
		mRssi = newRssi;
	}
	
	public BleDeviceState getCurrentState() {
		return mCurrentState;
	}
	
	public void setCurrentService(int position) {
		mCurrentGattService = position;
	}
	
	public void setCurrentCharacteristic(int position) {
		mCurrentCharacteristic = position;
	}
	
	public BluetoothGattService getCurrentService() {
		if (mCurrentGattService != -1) {
			return mServiceList.get(mCurrentGattService);		
		} else {
			return null;
		}
	}
	
	public BluetoothGattCharacteristic getCurrentCharacteristic() {
		if (mCurrentCharacteristic != -1) {
			return mCharacteristicList.get(mCurrentCharacteristic);
		} else {
			return null;
		}
		
	}
	
	public ArrayList<BluetoothGattService> getGattServiceList() {
		return mServiceList;
	}
	
	public ArrayList<BluetoothGattCharacteristic> getCharacteristicList() {
		return mCharacteristicList;
	}
	
	private void notifyCharacteristicRead(String readResult) {
		this.setChanged();
		notifyObservers(readResult);
	}
	
	private void notifyRssiValue(int value) {
		this.setChanged();
		notifyObservers(new Integer(value));
	}
}
