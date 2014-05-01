package com.adatronics.bledebugger.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.R.integer;
import android.content.Context;

public class BtDeviceLab {

	private ArrayList<BleDevice> mBleDevices;
	private ArrayList<BtcDevice> mBtcDevices;
	
	private Context mContext;
	public static BtDeviceLab mBtDeviceLab = null;
	private int mCurrentBleDevice;
	
	private BtDeviceLab(Context appContext) {
		mContext = appContext;
		mBleDevices = new ArrayList<BleDevice>();
		mBtcDevices = new ArrayList<BtcDevice>();
		mCurrentBleDevice = -1;
	}
	
	public static BtDeviceLab getInstance(Context appContext) {
		if (mBtDeviceLab == null) {
			mBtDeviceLab = new BtDeviceLab(appContext);
		}
		
		return mBtDeviceLab;
	}
	
	public void addDevice(BleDevice newDevice) {
		for (BleDevice device : mBleDevices) {
			if (device.getAddress().equals(newDevice.getAddress())) {
				return;
			}
		}
		mBleDevices.add(newDevice);
	}
	
	public void addDevice(BtcDevice newDevice) {
		for (BtcDevice device : mBtcDevices) {
			if (device.getAddress().equals(newDevice.getAddress())) {
				return;
			}
		}
		mBtcDevices.add(newDevice);
	}
	
	public int getBleSize() {
		return mBleDevices.size();
	}
	
	public ArrayList<BleDevice> getBleDeviceList() {
		return mBleDevices;
	}
	
	public void setCurrentBleDevice(int position){
		mCurrentBleDevice = position;
	}
	
	public BleDevice getCurrentBleDevice() {
		return mBleDevices.get(mCurrentBleDevice);
	}
	
	public int getBtcSize() {
		return mBtcDevices.size();
	}
	
	public ArrayList<BtcDevice> getBtcDeviceList() {
		return mBtcDevices;
	}
}
