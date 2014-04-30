package com.adatronics.bledebugger.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;

public class BleDeviceLab {

	private ArrayList<BleDevice> mBleDevices;
	
	private Context mContext;
	public static BleDeviceLab mBleDeviceLab = null;
	private int mCurrentDevice;
	
	private BleDeviceLab(Context appContext) {
		mContext = appContext;
		mBleDevices = new ArrayList<BleDevice>();
		mCurrentDevice = -1;
	}
	
	public static BleDeviceLab getInstance(Context appContext) {
		if (mBleDeviceLab == null) {
			mBleDeviceLab = new BleDeviceLab(appContext);
		}
		
		return mBleDeviceLab;
	}
	
	public void addDevice(BleDevice newDevice) {
		for (BleDevice device : mBleDevices) {
			if (device.getAddress().equals(newDevice.getAddress())) {
				return;
			}
		}
		mBleDevices.add(newDevice);
	}
	
	public int getSize() {
		return mBleDevices.size();
	}
	
	public ArrayList<BleDevice> getDeviceList() {
		return mBleDevices;
	}
	
	public void setCurrentDevice(int position){
		mCurrentDevice = position;
	}
	
	public BleDevice getCurrentDevice() {
		return mBleDevices.get(mCurrentDevice);
	}
}
