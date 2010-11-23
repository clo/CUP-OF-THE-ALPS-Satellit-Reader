package com.clo.cota;

import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class cotaItemDetail extends Activity {

	private TextView fullname = null;
	private TextView firma = null;
	private TextView adresse = null;
	private TextView plzort = null;
	private TextView email = null;
	
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
        fillItems();
    }
    
    private void fillItems(){
    	Bundle b = getIntent().getExtras();
		fullname.setText(b.getString("firstname") + " " + b.getString("lastname"));
		firma.setText(b.getString("firma"));
		email.setText(b.getString("email"));
		plzort.setText(b.getString("zip") + " " + b.getString("city"));
		adresse.setText(b.getString("address"));
    }
	
}
