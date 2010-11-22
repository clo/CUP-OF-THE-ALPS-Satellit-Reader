package com.clo.cota;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//import javax.xml.transform.stream.StreamSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.clo.cota.dialog.MyProgressDialog;
import com.clo.cota.entity.User;
import com.clo.cota.http.HttpRequest;
import com.clo.cota.sax.MySaxHandler;

//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.SavedState;
import android.widget.Toast;

public class cotaDbReader extends Activity {
	static final int PROGRESS_DIALOG = 0;
	static final String LOG_COTA_DB = "COTADB";
	private TextView debug;
    private EditText searchString;
    private String answerXML;
    private Button button;
    private TextView xmlResponse;
    private ProgressDialog progressDialog;
    HttpRequest httpRequest = null;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case(0):
                    progressDialog.dismiss();
                    break;
                
            }
        }
    };
    //private ListView lv;
    //private SharedPreferences sharedResults;
    

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        debug = (TextView) findViewById(R.id.Response);
        button = (Button) findViewById(R.id.getit);
        searchString = (EditText) findViewById(R.id.searchParam);
        xmlResponse = (TextView) findViewById(R.id.xml); 
        //lv = (ListView) findViewById(R.id.);
        button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				httpRequest = new HttpRequest(searchString.getText().toString());
				httpRequest.run();
				//putResultToSharedPref();
				while(!httpRequest.isDone()){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				answerXML = httpRequest.getAnswer();
				showResults();
			}
		});
        searchString.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (searchString.getText().toString().equals("entering_name")){
					searchString.setText("");
				}
			}
		});
        
        //sharedResults = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
    }
    
    
    
//    public void doPost(){
//    	try {
//            HttpClient client = new DefaultHttpClient();  
//            String postURL = "http://somepostaddress.com";
//            HttpPost post = new HttpPost(postURL); 
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("user", "kris"));
//                params.add(new BasicNameValuePair("pass", "xyz"));
//                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
//                post.setEntity(ent);
//                HttpResponse responsePOST = client.execute(post);  
//                HttpEntity resEntity = responsePOST.getEntity();  
//                if (resEntity != null) {    
//                    Log.i("RESPONSE",EntityUtils.toString(resEntity));
//                }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


	public void setDebug(TextView debug) {
		this.debug = debug;
	}


	public TextView getDebug() {
		return debug;
	}
	
	public void showResults(){
		xmlResponse.setText(answerXML);
		MySaxHandler saxHandler = parseXML();
		Intent i = new Intent(this,cotaListView.class);
		prepareDateForActivity(i,saxHandler);
		startActivity(i);
	}
	
	private void prepareDateForActivity(Intent i,MySaxHandler saxHandler){
		Bundle b = new Bundle();
		for(User user: saxHandler.getUsers()){
        	b.putString(new Integer(user.getId()).toString(), user.getFirstname() + " " + user.getLastname()); 
        }
		i.putExtras(b);
	}
	
	public MySaxHandler parseXML(){
		//SharedPreferences.Editor prefsEditor = sharedResults.edit();
		MySaxHandler saxHandler = null;
		try{
			SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	        saxHandler = new MySaxHandler();
	        System.out.println(answerXML);
	        InputStream in = new ByteArrayInputStream(answerXML.getBytes());
	        sp.parse(in,saxHandler);
	        int i = 1;
	        for(User user: saxHandler.getUsers()){
	        	System.out.print(user.getId());
	        	xmlResponse.append("USER: " + user.getId() + "|" + user.getFirstname() + "|" + user.getLastname() + "\n");
	        	i++;
	        }
	        Log.e(LOG_COTA_DB,"finished " + i);	
		}catch(SAXException e){
			Log.e(LOG_COTA_DB,"SAXException" + e.getMessage());
			e.printStackTrace();
		}catch(ParserConfigurationException e){
			Log.e(LOG_COTA_DB,"ParserConfigurationException" + e.getMessage());
		}catch(IOException e){
			Log.e(LOG_COTA_DB,"IOException" + e.getMessage());
		}catch(Exception e){
			Log.e(LOG_COTA_DB,"Exception" + e.getMessage());
			e.printStackTrace();
		}
        return saxHandler;
        //prefsEditor.putString(MY_NAME, "Sai");
        //prefsEditor.putString(MY_WALLPAPER, "f664.PNG");
        //prefsEditor.commit();
	}
	
	private void processHttp() {
		//progressDialog = ProgressDialog.show(cotaDbReader.this, "Processing", "Quering COTA database...");
//		new Thread() {
//			public void run() {
//				executeHttpGet();
				//handler.sendEmptyMessage(0);
//			}
//		}.start();
	}
	
//	@Override
//	protected Dialog onCreateDialog(int id) {
//		if(id == 1){
//			ProgressDialog loadingDialog = new ProgressDialog(this);
//			loadingDialog.setMessage("searching...");
//			loadingDialog.setIndeterminate(true);
//			loadingDialog.setCancelable(true);
//			return loadingDialog;
//		}
//		return super.onCreateDialog(id);
//	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    //Handle the back button
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
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

	        return true;
	    }
	    else {
	        return super.onKeyDown(keyCode, event);
	    }

	}
    
}