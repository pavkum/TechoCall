package com.techostic.techocall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.techostic.techocall.webinterface.DialerInterface;

public class DialerActivity extends Activity {

	
	private static DialerActivity dialerActivity = null;
	
	public static DialerActivity getInstance() {
		return dialerActivity;
	}
	
	/*public void onCreate1(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setBooleanProperty("SetFullscreen", false);
		super.init();
		this.setBooleanProperty("SetFullscreen", false);
		this.getWindow().addFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL);   
		this.getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);  
		this.getWindow().setGravity(Gravity.TOP);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		Long contactID = this.getIntent().getLongExtra("contactID", -1l);
		
		if(contactID != -1){
			
			super.loadUrl("file:///android_asset/www/dialer.html");
		}else{
			this.endActivity();
		}
		
	}*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		DialerActivity.dialerActivity = this;
		
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
		
		final String jsonData = this.getIntent().getStringExtra("json");
		
		wv.addJavascriptInterface(new DialerInterface(this), "Android");
		
		wv.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);
		        return true;
		    }
		    
		    @Override
		    public void onPageFinished(WebView view, String url) {
		    	super.onPageFinished(view, url);
		    	
		    	wv.loadUrl("javascript:updateData('" + jsonData + "');");
		    }
		});
		
		

		this.setContentView(wv);
		this.getWindow().setGravity(Gravity.TOP);
		
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		System.out.println("extra intent added");
		if(intent.getStringExtra("finish") != null){
			System.out.println("finish called");
			finish();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		System.out.println("being destroyed");
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
