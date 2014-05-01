package com.adatronics.bledebugger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
	}
	
	public void onBtnBack(View view) {
		finish();
	}

}
