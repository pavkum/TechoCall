package com.techostic.techocall;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.techostic.techocall.modal.Settings;
import com.techostic.techocall.storage.StorageAPI;
import com.techostic.techocall.storage.StorageAPIImpl;
import com.techostic.techocall.webinterface.DialerInterface;

public class DialerActivity extends Activity {

	
	private static DialerActivity dialerActivity = null;
	
	private StorageAPI storageAPIImpl = null;
	
	public static DialerActivity getInstance() {
		return dialerActivity;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		DialerActivity.dialerActivity = this;
		
		storageAPIImpl = StorageAPIImpl.getInstance(this);
		
		super.onCreate(savedInstanceState);
			
		this.getWindow().addFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL);   

		this.getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);  
		
		final WebView wv = new WebView(this);
		
		final Long contactID = this.getIntent().getLongExtra("contactID", -1l);
		
		if(contactID != -1){
			wv.loadUrl("file:///android_asset/www/dialer.html#" + contactID);
		}else{
			finish();
			return;
		}
		
		WebSettings webSettings = wv.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		Settings autoRemove = storageAPIImpl.getSettingsBySettingsName("autoRemove");
		
		
		String autoRemoveStatus = "0"; // dont delete by default
		
		
		if(autoRemove != null){ //no error while obtaining ;
			autoRemoveStatus = autoRemove.getValue();
		}
		
		
		
		final String jsonData = this.getIntent().getStringExtra("json");
		
		wv.addJavascriptInterface(new DialerInterface(this , storageAPIImpl , autoRemoveStatus , this.getIntent().getByteExtra("remaindedUsing", (byte)-1)), "Android");
		
		wv.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);
		        return true;
		    }
		    
		    @Override
		    public void onPageFinished(WebView view, String url) {
		    	super.onPageFinished(view, url);
		    	
		    	Settings openClosed = storageAPIImpl.getSettingsBySettingsName("showCollapsed");
		    	
		    	String openClosedStatus = "0"; // expand by default
		    	
		    	if(openClosed != null){
					openClosedStatus = openClosed.getValue();
				}
		    	
		    	wv.loadUrl("javascript:updateData('" + jsonData + "' , " + openClosedStatus + ");");
		    }
		});
		
		

		this.setContentView(wv);
		this.getWindow().setGravity(Gravity.TOP);
		
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent.getStringExtra("finish") != null){
			finish();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		DialerActivity.dialerActivity = null;
		super.onDestroy();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(MotionEvent.ACTION_OUTSIDE == event.getAction()){
			return super.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

}
