package com.adatronics.bledebugger;

import com.adatronics.bledebugger.R;
import com.adatronics.bledebugger.ble.BleScan;
import com.adatronics.bledebugger.classic.BtcScan;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	
	private static final int REQUEST_DISCOVERABLE = 0;
	private static final int REQUEST_BLE = 1;
	private static final int REQUEST_CLASSIC = 2;
	
	private static final int DISCOVERABLE_DURATION = 3;
	
	
	View btnBle;
	View btnBluetooth;
	Button btnDiscover;
	View btnAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainactivity);
		
		btnBle = (Button) findViewById(R.id.btn_ble);
		btnBluetooth = (Button) findViewById(R.id.btn_bluetooth);
		btnDiscover = (Button) findViewById(R.id.btn_discover);
		btnAbout = (Button) findViewById(R.id.btn_about);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	public void onBtnBLE(View view) {
		
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "The device doesn't support BLE",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		Intent intent = new Intent(this, BleScan.class);
		startActivity(intent);
	}
	
	public void onBtnClassic(View view) {
		Intent intent = new Intent(this, BtcScan.class);
		startActivity(intent);
	}
	
	public void onBtnDiscoverable(View view) {
		Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
		startActivityForResult(i, REQUEST_DISCOVERABLE);
	}
	
	public void onBtnAbout(View view) {
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == REQUEST_DISCOVERABLE) {
			if (resultCode == DISCOVERABLE_DURATION) {
				btnDiscover.setEnabled(false);
				btnDiscover.setText("... Advertising ...");
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						btnDiscover.setEnabled(true);
						btnDiscover.setText("Discoverable");
					}
				}, DISCOVERABLE_DURATION*1000);
			}
		} 
		
//		else if (requestCode == REQUEST_BLE) {
//			if (resultCode == Activity.RESULT_OK) {
//				Log.i(TAG, "the requestCode is: " + requestCode);
//				Log.i(TAG, "the resultCode is: " + resultCode);
//			}
//		} else if (requestCode == REQUEST_CLASSIC) {
//			if (resultCode == Activity.RESULT_OK) {
//				Log.i(TAG, "the requestCode is: " + requestCode);
//				Log.i(TAG, "the resultCode is: " + resultCode);
//			}
//		}

	}
	
	
}
