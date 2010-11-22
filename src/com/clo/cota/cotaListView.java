package com.clo.cota;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class cotaListView extends ListActivity implements OnGestureListener {
	
	private List<String> RESULTS = new ArrayList<String>();
	
	private ListView lv = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  fillArray();
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RESULTS));

	  lv = getListView();
	  lv.setTextFilterEnabled(true);

	}
	
	@Override
	 protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Log.i("cotaListView","onListItemClick: " + id + "=" + RESULTS.get((int) id));
		
//		super.onListItemClick(l, v, position, id);
//	  
//		Intent intent = new Intent();
//		Bundle bundle = new Bundle();
//	  
//		bundle.putString("country", l.getItemAtPosition(position).toString());
//		intent.putExtras(bundle);
//		setResult(RESULT_OK, intent);
//		finish();
	 }
	
	private void fillArray(){
		Bundle b = getIntent().getExtras();
		Set<String> keys = b.keySet();
		for(String key: keys){
			System.out.println(key + "=" + b.getString(key));
			RESULTS.add(b.getString(key) + " [  #" + key + " ]");
		}
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
