package com.techostic.techocall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DialerActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		
		dialog.getWindow().setGravity(Gravity.TOP);
		//dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		//dialog.getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL);
		
		this.getWindow().addFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL);   

		    // ...but notify us that it happened.   
		 getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);  
		
		WebView wv = new WebView(this);
		wv.loadUrl("file:///android_asset/www/dialer.html");
		
		wv.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);

		        return true;
		    }
		});

		this.setContentView(wv);
		this.getWindow().setGravity(Gravity.TOP);
		
		//dialog.setContentView(wv);
		
		//dialog.show();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(MotionEvent.ACTION_OUTSIDE == event.getAction()){
			System.out.println("outside");
			return super.onTouchEvent(event);
		}
		System.out.println("a touch");
		return super.onTouchEvent(event);
	}
	   
	  //public boolean onTouchEvent(MotionEvent event) {   
	    // If we've received a touch notification that the user has touched   
	    // outside the app, finish the activity.   
	    /*if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {   
	      //finish();   
	      return true;   
	    }   

	    // Delegate everything else to Activity.   
	    return super.onTouchEvent(event);   
*/	  //}   
	
	public void onCreate1(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		/*builder.setTitle("Test")
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();*/
		
		WebView wv = new WebView(this);
		wv.loadUrl("file:///android_asset/www/dialer.html");
		wv.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);

		        return true;
		    }
		});
		
		
		/*alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int id) {
		        dialog.dismiss();
		    }
		});*/
		
		AlertDialog dialog = alert.create();
		
		dialog.setCanceledOnTouchOutside(false);
		
		dialog.getWindow().setGravity(Gravity.TOP);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL);
		
		alert.setView(wv);
		
		dialog.show();
	}

}
