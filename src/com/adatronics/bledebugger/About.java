package com.adatronics.bledebugger;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class About extends Activity {

	private static final String TAG = "About";
	private WebView adaWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		
		adaWebView = (WebView) findViewById(R.id.adaWebView);
		adaWebView.setWebViewClient(new MyWebViewClient());
		adaWebView.getSettings().setJavaScriptEnabled(true);
		
		if (NetworkCheck.isInternetAvailable(getApplicationContext())) {
			adaWebView.loadUrl("http://www.adatronics.com/");		
		} else {
			Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
			finish();
		}
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				adaWebView.reload();
			}
		}, 3000);
	}

	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}
}
