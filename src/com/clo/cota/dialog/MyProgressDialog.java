package com.clo.cota.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.clo.cota.cotaDbReader;

public class MyProgressDialog implements Runnable {

	Handler ctxt = null;
	ProgressDialog dialog = null;
	
	public MyProgressDialog(Handler handler) {
		super();
		this.ctxt = ctxt;
	}
	
	@Override
	public void run() {
//		dialog = ProgressDialog.show(ctxt, "", 
//                "Getting data. Please wait...", true);
	}
	
	public void stop(){
		dialog.cancel();
	}
	
}
