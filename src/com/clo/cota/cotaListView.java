package com.clo.cota;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.clo.cota.entity.User;
import com.clo.cota.http.HttpRequest;
import com.clo.cota.sax.AddressDetailSaxHandler;
import com.clo.cota.sax.MySaxHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class cotaListView extends ListActivity implements OnGestureListener {
	
	static final String LOG_COTA_LISTVIEW = "COTA_LIST_VIEW";
	private List<String> RESULTS = new ArrayList<String>();
	private List<String> RESULTS_IDS = new ArrayList<String>();
	private ListView lv = null;
	HttpRequest httpRequest = null;
	private String answer = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  try{
		  fillArray();
		  setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RESULTS));
		  lv = getListView();
		  lv.setTextFilterEnabled(true);
	  }catch(Exception e){
		  e.printStackTrace();
	  }

	}
	
	@Override
	 protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.v("cotaListView","onListItemClick: " + id + "=" + RESULTS.get((int) id));
		
		httpRequest = new HttpRequest(new Integer(RESULTS_IDS.get((int) id)));
		httpRequest.run();
		
		while(!httpRequest.isDone()){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		answer = httpRequest.getAnswer();
		httpRequest = null;
		User user = parseXML(answer);
		
		Bundle bundle = new Bundle();
		bundle.putString("firstname",user.getFirstname());
		bundle.putString("lastname", user.getLastname());
		bundle.putString("address", user.getAddress());
		bundle.putString("email", user.getEmail());
		bundle.putString("zip", user.getZip());
		bundle.putString("city", user.getCity());

		Intent i = new Intent(this,cotaItemDetail.class);
		i.putExtras(bundle);
		startActivity(i);

	 }
	
	private void fillArray(){
		Bundle b = getIntent().getExtras();
		Set<String> keys = b.keySet();
		int i = 0;
		for(String key: keys){
			System.out.println(key + "=" + b.getString(key));
			RESULTS.add(i,b.getString(key) + " [  #" + key + " ]");
			RESULTS_IDS.add(i,key);
			i++;
		}
	}
	
	private User parseXML(String answer){
		AddressDetailSaxHandler saxHandler = null;
		try{
			SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	        saxHandler = new AddressDetailSaxHandler();
	        System.out.println(answer);
	        InputStream in = new ByteArrayInputStream(answer.getBytes());
	        sp.parse(in,saxHandler);
	        Log.i(LOG_COTA_LISTVIEW,"finished " + saxHandler.getUser().getFirstname());	
		}catch(SAXException e){
			Log.e(LOG_COTA_LISTVIEW,"SAXException" + e.getMessage());
			e.printStackTrace();
		}catch(ParserConfigurationException e){
			Log.e(LOG_COTA_LISTVIEW,"ParserConfigurationException" + e.getMessage());
		}catch(IOException e){
			Log.e(LOG_COTA_LISTVIEW,"IOException" + e.getMessage());
		}catch(Exception e){
			Log.e(LOG_COTA_LISTVIEW,"Exception" + e.getMessage());
			e.printStackTrace();
		}
        return saxHandler.getUser();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("cotaListView","onTouchEvent");
		return false;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("cotaListView","onDown");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("cotaListView","onShowProcess");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("cotaListView","onLongPress");
	}
	
}
