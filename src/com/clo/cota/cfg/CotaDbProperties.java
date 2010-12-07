package com.clo.cota.cfg;

import com.clo.cota.R;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class CotaDbProperties extends PreferenceActivity {
	
	private SharedPreferences props = null;
	private SharedPreferences.Editor editor = null;
	
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		this.addPreferencesFromResource(R.xml.properties);
		init();
		editor.putString("username","lochrobe");
		editor.commit();
	}
	
	public void init(){
		props = getPreferences(MODE_PRIVATE);
		editor = props.edit();
	}
	
	public static final SharedPreferences
		getAppProperties(ContextWrapper ctx) {
		return ctx.getSharedPreferences(
		ctx.getPackageName()
		+ "_preferences", 0);
	}
	
}
