package com.jhonny.mpopular;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


public class BuscadorActivity extends Activity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buscador);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_buscador, menu);
		return true;
	}
	
	
	
}
