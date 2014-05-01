package com.adatronics.bledebugger;

import com.adatronics.bledebugger.R;
import com.adatronics.bledebugger.ble.BleScan;
import com.adatronics.bledebugger.classic.BtcScan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainactivity);
		
		View btn_bleButton = (Button) findViewById(R.id.btn_ble);
		View btn_bluetoothButton = (Button) findViewById(R.id.btn_bluetooth);
		View btn_testButton = (Button) findViewById(R.id.btn_test);
		View btn_aboutButton = (Button) findViewById(R.id.btn_about);
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
		
		Intent intent = new Intent(this, BleScan.class);
		startActivity(intent);
	}
	
	public void onBtnClassic(View view) {
		Intent intent = new Intent(this, BtcScan.class);
		startActivity(intent);
	}
	
	public void onBtnTest(View view) {
		
	}
	
	public void onBtnAbout(View view) {
		
	}
}
