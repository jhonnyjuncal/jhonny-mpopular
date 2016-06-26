package com.jhonny.mpopular.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.jhonny.mpopular.R;
import com.jhonny.mpopular.util.Util;
import com.jhonny.mpopular.util.FileUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class TerminosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	
	private AdView adView = null;
	private View view;
	private int contador = 0;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_terminos);
		
		try{
			// barra de herramientas
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);

			contador = 0;
			this.context = (Context)this;
			
	        Locale locale = getResources().getConfiguration().locale;
	        leeFicheroTerminosCondiciones(locale);

			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
			drawer.setDrawerListener(toggle);
			toggle.syncState();

			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			navigationView.setNavigationItemSelectedListener(this);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getSupportMenuInflater();
//		inflater.inflate(R.menu.menu_terminos, menu);
//		return true;
		return false;
	}

	@Override
    protected void onResume(){
		super.onResume();
		
		try{
			contador = 0;
			this.context = (Context)this;
			
			reiniciarFondoOpciones();
			reiniciodelaaplicacion();
			estableceFuenteRoboto();
			
			// publicidad
			adView = new AdView(this, AdSize.BANNER, "a1518312d054c38");
			LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout2);
			layout.addView(adView);
			adView.loadAd(new AdRequest());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void reiniciodelaaplicacion(){
		if(!FileUtil.cargaDatosPersonales(this)){
			Intent intent = new Intent(this, NuevoUsuarioActivity.class);
			startActivity(intent);
		}
		
		TextView opc_textview1 = (TextView)findViewById(R.id.opc_textView1);
		if(Util.getNombre() == null || Util.getNombre().length() == 0)
			FileUtil.cargaDatosPersonales(context);
		opc_textview1.setText(Util.getNombre());
	}
	
	private void estableceFuenteRoboto(){
		TextView textView = (TextView)findViewById(R.id.ter_textView1);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.ter_textView2);
		textView.setTypeface(Util.getRoboto7(this));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
//				menu.toggle();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void reiniciarFondoOpciones(){
		try{
			LinearLayout layout_busq = (LinearLayout)findViewById(R.id.opc_layout_busq);
			layout_busq.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_redes = (LinearLayout)findViewById(R.id.opc_layout_redes);
			layout_redes.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_conf = (LinearLayout)findViewById(R.id.opc_layout_conf);
			layout_conf.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_acerca = (LinearLayout)findViewById(R.id.opc_layout_acerca);
			layout_acerca.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_terminos = (LinearLayout)findViewById(R.id.opc_layout_terminos);
			layout_terminos.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_ayuda = (LinearLayout)findViewById(R.id.opc_layout_ayuda);
			layout_ayuda.setBackgroundResource(R.color.gris_claro);
			
			if(view != null)
				view.buildDrawingCache(true);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void leeFicheroTerminosCondiciones(Locale locale){
		String linea = null;
		StringBuffer contenido = new StringBuffer();
		InputStream in = null;
		
		try{
			TextView ter_textview = (TextView)findViewById(R.id.ter_textView2);
			String retornoDeCarro = System.getProperty("line.separator");
			
			if(locale != null){
				if(locale.getLanguage().equals("es"))
					in = getAssets().open("terminos_condiciones.txt");
				else
					in = getAssets().open("terms_conditions.txt");
				
				if(in != null){
					InputStreamReader input = new InputStreamReader(in, "UTF-8");
					BufferedReader buffreader = new BufferedReader(input);
					
					while((linea = buffreader.readLine()) != null){
						contenido.append(linea + retornoDeCarro);
					}
					ter_textview.setText(contenido.toString());
				}
				in.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		if(contador == 0){
    			contador++;
    			Toast.makeText(this, getResources().getString(R.string.txt_salir_1_aviso), Toast.LENGTH_SHORT).show();
    			return true;
    		}else{
    			contador = 0;
    			Intent intent = new Intent();
    			intent.setAction(Intent.ACTION_MAIN);
    			intent.addCategory(Intent.CATEGORY_HOME);
    			startActivity(intent);
    		}
    	}
    	//para las demas cosas, se reenvia el evento al listener habitual
    	return super.onKeyDown(keyCode, event);
    }

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Intent intent = null;

		if (id == R.id.nav_principal) {
			intent = new Intent(this, PrincipalActivity.class);
		} else if (id == R.id.nav_buscador) {
			intent = new Intent(this, BuscadorActivity.class);
		} else if (id == R.id.nav_mis_redes) {
			intent = new Intent(this, MisRedesActivity.class);
		} else if (id == R.id.nav_manage) {
			intent = new Intent(this, ConfiguracionActivity.class);
//		} else if (id == R.id.nav_share) {
//			intent = new Intent(this, AyudaActivity.class);
//		} else if (id == R.id.nav_send) {
//			intent = new Intent(this, AyudaActivity.class);
		} else if (id == R.id.nav_acerca) {
			intent = new Intent(this, AcercaActivity.class);
		} else if (id == R.id.nav_terminos) {
			intent = new Intent(this, TerminosActivity.class);
		} else if (id == R.id.nav_ayuda) {
			intent = new Intent(this, AyudaActivity.class);
		}
//		intent = new Intent(this, NuevoUsuarioActivity.class);
//		intent = new Intent(this, NuevaRedActivity.class);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);

		startActivity(intent);

		return true;
	}
}
