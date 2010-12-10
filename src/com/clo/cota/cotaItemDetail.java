package com.clo.cota;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class cotaItemDetail extends Activity implements OnLongClickListener, OnTouchListener {

	static final String LOG_COTADB_ITEM_DETAIL = "cotadb item detail";
	private TextView fullname = null;
	private TextView firma = null;
	private TextView adresse = null;
	private TextView plzort = null;
	private TextView email = null;
	private TextView mobile = null;
	private TextView funktion = null;
	private RelativeLayout rl = null;
	private TextView textview = null; 
	private float downXValue = 0;
	private float upXValue = 0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        fullname = (TextView) findViewById(R.id.TextView_vollname);
        firma =  (TextView) findViewById(R.id.TextView_firma);
        email =  (TextView) findViewById(R.id.TextView_email);
        plzort =  (TextView) findViewById(R.id.TextView_plz_ort);
        adresse = (TextView) findViewById(R.id.TextView_adresse);
        mobile = (TextView) findViewById(R.id.TextView_mobile);
        funktion = (TextView) findViewById(R.id.TextView_funktion);
        rl = (RelativeLayout)findViewById(R.id.RelativeLayout01);
        textview = (TextView) findViewById(R.id.textview);
        init();
        fillItems();
    }
    
    private void init(){
    	registerForContextMenu(textview);
    	textview.setOnTouchListener(this);
    }
    
    private void fillItems(){
    	Bundle b = getIntent().getExtras();
		fullname.setText(b.getString("firstname") + " " + b.getString("lastname"));
		firma.setText(b.getString("firma"));
		email.setText(b.getString("email"));
		plzort.setText(b.getString("zip") + " " + b.getString("city"));
		adresse.setText(b.getString("address"));
		mobile.setText(b.getString("mobile"));
		funktion.setText(b.getString("funktion"));
    }
    
	//TODO does not work
    public boolean onLongClick(View view){
		Log.v(LOG_COTADB_ITEM_DETAIL,"onLongClick");
		Toast.makeText(getApplicationContext(), "onLongClick", Toast.LENGTH_LONG).show();
		//TODO: got to the 1st Activity CotaDBReader
		return true;
	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		Log.v(LOG_COTADB_ITEM_DETAIL,"onClick");
//	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.v(LOG_COTADB_ITEM_DETAIL,"onToch event received ...");
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				downXValue = event.getX();
				upXValue = 0;
				break;
			case MotionEvent.ACTION_UP:
				upXValue = event.getX();
		}
		float diff = downXValue - upXValue;
		if (diff < 0){
			Log.v(LOG_COTADB_ITEM_DETAIL,"downXValue=" + downXValue + " upXValue=" + upXValue + " diff=" + diff);
			goLeft();
		}else if(diff > 0){
			goRight();
		}
		return false;
	}
	private void goLeft(){
		finish();
	}
	private void goRight(){
		Log.v(LOG_COTADB_ITEM_DETAIL,"goRight -> todo");
	}
}
