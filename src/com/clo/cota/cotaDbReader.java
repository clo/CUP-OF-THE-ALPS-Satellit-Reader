package com.clo.cota;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import com.clo.cota.cfg.CotaDbProperties;
import com.clo.cota.entity.User;
import com.clo.cota.enums.EHttpRequest;
import com.clo.cota.http.HttpRequest;
import com.clo.cota.sax.MySaxHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class cotaDbReader extends Activity {
	static final int PROGRESS_DIALOG = 0;
	static final String LOG_COTA_DB = "COTADB";
	static final int KEYCODE_ENTER = 66;
    private EditText searchString;
    private int categoryid=0;
    private String answerXML;
    private Button button;
    private ProgressDialog progressDialog = null;
    private Handler handler = null;
    private TextView textview = null; 
    private EHttpRequest request = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_new);
        //debug = (TextView) findViewById(R.id.Response);
        textview = (TextView) findViewById(R.id.textview);
        registerForContextMenu(textview);
        button = (Button) findViewById(R.id.button);
        searchString = (EditText) findViewById(R.id.search);
        progressDialog = new ProgressDialog(cotaDbReader.this);
        progressDialog.setTitle("HTTP Processing");
        progressDialog.setMessage("Quering COTA database...");
        if(!searchString.hasFocus()){
        	searchString.setFocusable(true);
        	searchString.requestFocus();
        }
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))  
        .showSoftInputFromInputMethod(searchString.getWindowToken(), 0);
        //.hideSoftInputFromWindow(searchString.getWindowToken(), 0);  
//        if (progressDialog != null){
//        	progressDialog.dismiss();
//        }
        button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				runQuery();
			}
		});
        searchString.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KEYCODE_ENTER) {
					Log.v(LOG_COTA_DB,"Enter detected...");
					request = EHttpRequest.NAME;
					runQuery();
				}
				return false;
			}
		});
        
        textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v(LOG_COTA_DB,"onClick on invivisble item");
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))  
		        .hideSoftInputFromWindow(searchString.getWindowToken(), 0);  
				runQuery();
			}
		});
        
        textview.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Log.v(LOG_COTA_DB,"onLongClick on invivisble item");
				// TODO Auto-generated method stub
				
				return false;
			}
		});
        
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Log.v(LOG_COTA_DB,"handleMessage");
				Bundle b = msg.getData();
				if(b.getString("action").equals("STARTPROGDIAG")){
					showProgessDialog();
				}else if(b.getString("action").equals("STOPPROGDIAG")){
					hideProgessDialog();
					answerXML=b.getString("answer");
					showResults();
				}
				
			  }
		};
    }
    
    public void runQuery(){
    	Thread t = new Thread(){
			public void run(){
				try{
				    Log.v(LOG_COTA_DB,"Thread started ...");
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("action","STARTPROGDIAG");
					msg.setData(b);
					handler.sendMessage(msg);
					//start slow request
					HttpRequest hr = new HttpRequest(request); 
					Log.v(LOG_COTA_DB,"request= " + request.toString());
					if (request == EHttpRequest.NAME){
						hr.setSearch(searchString.getText().toString());
					}else if(request == EHttpRequest.CATEGORY){
						hr.setCategoryid(categoryid);
					}else{
						//TODO
					}
					hr.run();
					msg = new Message();
					b = new Bundle();
					b.putString("answer",hr.getAnswer());
					b.putString("action", "STOPPROGDIAG");
					msg.setData(b);
					handler.sendMessage(msg);
					hr = null;
					b = null;
					Log.v(LOG_COTA_DB,"... Thread finished.");
		    	}catch(UnknownHostException e){
		    		showErrorDialog(e.getCause().toString());
		    	}catch(Exception e){
		    		showErrorDialog(e.getCause().toString());
		    	}
			}
		};
		t.start();
    }
    
    private void showProgessDialog(){
    	Log.v(LOG_COTA_DB,"Start progress dialog...");
    	if (progressDialog == null){
    	  progressDialog = ProgressDialog.show(cotaDbReader.this, "Processing", "Quering COTA database...");
    	}
    	progressDialog.show();
    }

    private void hideProgessDialog(){
    	Log.v(LOG_COTA_DB,"Stop progress dialog...");
    	if (progressDialog != null){
    	  progressDialog.dismiss();
    	}
    }
    
    private void showErrorDialog(String error){
    	Log.v(LOG_COTA_DB,"Start progress dialog...");
    	if (progressDialog == null){
    	  progressDialog = ProgressDialog.show(cotaDbReader.this, "Error", "There has been an error occured.\n" + error);
    	}
    	progressDialog.show();
    }
    
    public void showResults(){
		MySaxHandler saxHandler = parseXML();
		if (saxHandler.getUsers().size() > 0){
		  Intent i = new Intent(this,cotaListView.class);
		  prepareDateForActivity(i,saxHandler);
		  startActivity(i);
		}else{
			//no data
			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.data)
	        .setMessage(R.string.no_data)
	        .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                //Stop the activity
	            }
	        })
	        .show();
		}
	}
	
	private void prepareDateForActivity(Intent i,MySaxHandler saxHandler){
		Bundle b = new Bundle();
		for(User user: saxHandler.getUsers()){
        	b.putString(new Integer(user.getId()).toString(), user.getFirstname() + " " + user.getLastname()); 
        }
		i.putExtras(b);
	}
	
	public MySaxHandler parseXML(){
		MySaxHandler saxHandler = null;
		try{
			SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	        saxHandler = new MySaxHandler();
	        Log.v(LOG_COTA_DB, answerXML);
	        InputStream in = new ByteArrayInputStream(answerXML.getBytes());
	        sp.parse(in,saxHandler);
		}catch(SAXException e){
			Log.e(LOG_COTA_DB,"SAXException" + e.getMessage());
			e.printStackTrace();
		}catch(ParserConfigurationException e){
			Log.e(LOG_COTA_DB,this.getClass().getCanonicalName() + " ParserConfigurationException: " + e.getMessage());
		}catch(IOException e){ 
			Log.e(LOG_COTA_DB,this.getClass().getCanonicalName() + " IOException: " + e.getMessage());
		}catch(Exception e){
			Log.e(LOG_COTA_DB,this.getClass().getCanonicalName() + " Exception: " + e.getMessage());
			e.printStackTrace();
		}
        return saxHandler;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.v(LOG_COTA_DB,"menu button presses");
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	public void resetEntryField(){
		searchString.setText("");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    //Handle the back button
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	        exit();
	        return true;
	    }else{
	        return super.onKeyDown(keyCode, event);
	    }

	}
	
	//TODO: not working at the moment
	public boolean onTouchEvent(MotionEvent event){
		Log.v(LOG_COTA_DB,"onTouchEvent detected -> " + event.getAction());
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.quit:
	        exit();
	        return true;
	    case R.id.settings:
	    	startSettingActivity();
	        return true;
	    case R.id.about:
	    	showAboutDialog();
	    	return true;
	    default:
	    	return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showAboutDialog(){
		Log.i(LOG_COTA_DB,"TODO: show about dialog");
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(R.string.about_title)
        .setMessage(R.string.about_text)
        .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        })
        .show();
	}
	
	public void startSettingActivity(){
		Log.i(LOG_COTA_DB,"TODO: show settings");
		Intent i = new Intent(this,CotaDbProperties.class);
		startActivity(i);
	}
	
	private void exit(){
		//Ask the user if they want to quit
        new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(R.string.quit)
        .setMessage(R.string.really_quit)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Stop the activity
                cotaDbReader.this.finish();    
            }

        })
        .setNegativeButton(R.string.no, null)
        .show();

	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.context_menu, menu);
	}

	/*
	+-------------------+-------------+--------------------------------------+
	| KategorieGruppeID | KategorieID | KategorieName                        |
	+-------------------+-------------+--------------------------------------+
	|                 0 |           1 | Archiv 2001                          |
	|                 3 |           2 | Betreuer                             |
	|                 6 |           3 | Botschaften                          |
	|                 2 |           4 | Bronze-Ball                          |
	|                 2 |           5 | Co-Sponsor                           |
	|                 0 |           6 | Diverse                              |
	|                 2 |           7 | Gold-Ball                            |
	|                 2 |           8 | G÷nner                               |
	|                 2 |           9 | Hauptsponsor                         |
	|                 6 |          10 | Helfer                               |
	|                 0 |          11 | Hotel                                |
	|                 2 |          12 | Inserat 1/1                          |
	|                 2 |          13 | Inserat 1/2                          |
	|                 2 |          14 | Inserat 1/4                          |
	|                 2 |          15 | Kommissõr                            |
	|                 0 |          16 | Medien                               |
	|                 2 |          17 | Offizieller Sponsor                  |
	|                 1 |          18 | OK-Stab                              |
	|                 1 |          19 | Ressortchef                          |
	|                 4 |          20 | Schiedsrichter                       |
	|                 2 |          21 | Silber-Ball                          |
	|                 1 |          22 | Mannschaften                         |
	|                 2 |          23 | Tombola                              |
	|                 0 |          24 | Verbõnde / Organisationen            |
	|                 2 |          25 | VIP-Ball                             |
	|                 1 |          28 | Jury                                 |
	|                 6 |          29 | K³nstler                             |
	|                 1 |          30 | Spielleiter                          |
	|                 6 |          31 | Gõste                                |
	|                 0 |          33 | FC Naters                            |
	|                 6 |          36 | Ehrengõste                           |
	|                 6 |          37 | Ehrenmitglied FC Naters              |
	|                 6 |          38 | VIP-Sponsor FC Naters                |
	|                 0 |          62 | Egga-Fest                            |
	|                 0 |          61 | Patronat                             |
	|                 0 |          63 | Badge                                |
	|                 0 |          64 | Notrufnummer                         |
	|                 1 |          65 | Speaker                              |
	|                 0 |          66 | Sitzung_Brainstorming_27072006       |
	|                 1 |          67 | Erweitertes-OK                       |
	|                 2 |          68 | Flyer                                |
	|                 0 |          69 | Podiumsgesprõch mit Apero            |
	|                 0 |          70 | Podiumsgesprõch ohne Apero           |
	|                 2 |          71 | Offizieller Ausr³ster                |
	|                 2 |          72 | Eintrittssponsor                     |
	|                 2 |          73 | Survival Lounge                      |
	|                 0 |          74 | Trainer Team Oberwallis              |
	|                 0 |          75 | Junioren F-Turnier                   |
	|                 0 |          76 | Referent                             |
	|                 0 |          77 | Verstorben                           |
	|                 0 |          78 | Oberwalliser Fussballverein          |
	|                 0 |          79 | Jury-Prõsident                       |
	|                 2 |          81 | Survival Lounge - Olivier Constantin |
	+-------------------+-------------+--------------------------------------+	
	*/
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	  switch (item.getItemId()) {
	  	case R.id.ctx_reset:
	  		resetEntryField();
	  		return true;
	  	case R.id.ctx_settings:
	  		startSettingActivity();
	  		return true;
	  	case R.id.ctx_quit:
	  		exit();
	  		return true;
	  	case R.id.ctx_sub_ok:
	  		this.request = EHttpRequest.CATEGORY;
	  		categoryid=18;
	  		runQuery();
	  		return true;
	  	case R.id.ctx_sub_sponsors_1_site:
	  		this.request = EHttpRequest.CATEGORY;
	  		categoryid=12;
	  		runQuery();
	  		return true;
	  	case R.id.ctx_sub_sponsors_12_site:
	  		this.request = EHttpRequest.CATEGORY;
	  		categoryid=13; 
	  		runQuery();
	  		return true;
	  	case R.id.ctx_sub_sponsors_14_site:
	  		this.request = EHttpRequest.CATEGORY;
	  		categoryid=14; 
	  		runQuery();
	  		return true;
	  	case R.id.ctx_sub_sponsors_main:
	  		this.request = EHttpRequest.CATEGORY;
	  		categoryid=9;
	  		runQuery();
	  		return true;
	  default:
	    return super.onContextItemSelected(item);
	  }
	}
	
	
	
}