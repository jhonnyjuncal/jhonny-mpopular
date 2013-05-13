package com.jhonny.mpopular;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


public class AcercaActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acerca);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_acerca, menu);
		return true;
	}
}
