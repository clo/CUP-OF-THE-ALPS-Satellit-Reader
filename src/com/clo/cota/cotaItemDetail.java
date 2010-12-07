package com.clo.cota;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class cotaItemDetail extends Activity implements OnLongClickListener{

	static final String LOG_COTADB_ITEM_DETAIL = "cotadb item detail";
	private TextView fullname = null;
	private TextView firma = null;
	private TextView adresse = null;
	private TextView plzort = null;
	private TextView email = null;
	private TextView mobile = null;
	private TextView funktion = null;
	private View view = null;
	private RelativeLayout rl = null;
	
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
        view = (View) findViewById(R.layout.item_detail);
        init();
        fillItems();
    }
    
    private void init(){
//    	view.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				//startActivity(new Intent(cotaItemDetail.this, cotaItemDetail.class));
//				//Toast.makeText(getApplicationContext(), "TEST", Toast.LENGTH_LONG).show();
//				Dialog dialog = new Dialog(getApplicationContext());
//				dialog.setTitle("TEST");
//				dialog.show();
//				return false;
//			}
//		});
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
}
