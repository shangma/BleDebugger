package com.adatronics.bledebugger.model;

import android.bluetooth.BluetoothDevice;
import android.database.Observable;

public class BtcDevice extends Observable {
	
	private static final String TAG = "BtcDevice";

	private String mName;
	private String mAddress;
	
	private BluetoothDevice mDevice;

	
	public BtcDevice(BluetoothDevice device) {
		mDevice = device;
		mName = device.getName();
		mAddress = device.getAddress();
	}
	

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String mAddress) {
		this.mAddress = mAddress;
	}	
}
