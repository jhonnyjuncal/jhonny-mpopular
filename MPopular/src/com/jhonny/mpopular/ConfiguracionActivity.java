package com.jhonny.mpopular;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


public class ConfiguracionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_configuracion, menu);
		return true;
	}
}
