package com.jhonny.mpopular.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jhonny.mpopular.R;
import com.jhonny.mpopular.util.Util;
import com.jhonny.mpopular.util.FileUtil;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;


public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	
	private View view;
	private int contador = 0;
	private Context context;
	
	//Constants for tablet sized ads (728x90)
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int MED_BANNER_WIDTH = 480;
	//Constants for phone sized ads (320x50)
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_principal);
		
		try{
			// barra de herramientas
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);

//			MMSDK.setLogLevel(MMSDK.LOG_LEVEL_DEBUG);
			contador = 0;
			this.context = (Context)context;

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
//		inflater.inflate(R.menu.menu_principal, menu);
//		return true;
		return false;
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
	
	@Override
    protected void onResume(){
		super.onResume();
		
		try{
			contador = 0;
			
			// muestra el nombre de usuario en las opciones y recarga la publicidad
			cargaDatosIniciales();
			// reinicia la actividad de opciones
			reiniciarFondoOpciones();
			reiniciodelaaplicacion();
			estableceFuenteRoboto();
//			cargaPublicidad();
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
		TextView textView = (TextView)findViewById(R.id.ppal_textView1);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.ppal_textView2);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.ppal_textView3);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.ppal_textView4);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.ppal_textView5);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.ppal_textView6);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.ppal_textView7);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.ppal_textView8);
		textView.setTypeface(Util.getRoboto7(this));
	}
	
	private void cargaDatosIniciales(){
		try{
			if(!FileUtil.cargaDatosPersonales(this)){
				Intent intent = new Intent(this, NuevoUsuarioActivity.class);
				startActivity(intent);
			}else{
				Util.recuperarDatosUsuario(Util.getIdUsuario());
				muestraDatosPersonales();
			}
			
			// se muestra el nombre completo del usuario en la activity opciones
			TextView opc_textview1 = (TextView)findViewById(R.id.opc_textView1);
			if(Util.getNombre() == null || Util.getNombre().length() == 0)
				FileUtil.cargaDatosPersonales(context);
			opc_textview1.setText(Util.getNombre());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void muestraDatosPersonales(){
		try{
			TextView textoNombre = (TextView)findViewById(R.id.ppal_textView2);
			TextView textoTelefono = (TextView)findViewById(R.id.ppal_textView4);
			TextView textoEmail = (TextView)findViewById(R.id.ppal_textView6);
			TextView textoPais = (TextView)findViewById(R.id.ppal_textView8);
			
			textoNombre.setText(Util.getNombre());
			textoTelefono.setText(Util.getTelefono());
			textoEmail.setText(Util.getEmail());
			textoPais.setText(Util.getPais());
			
		}catch(Exception ex){
			ex.printStackTrace();
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
	
//	private void cargaPublicidad(){
//		int placementWidth = BANNER_AD_WIDTH;
//
//		//Finds an ad that best fits a users device.
//		if(canFit(IAB_LEADERBOARD_WIDTH)) {
//		    placementWidth = IAB_LEADERBOARD_WIDTH;
//		}else if(canFit(MED_BANNER_WIDTH)) {
//		    placementWidth = MED_BANNER_WIDTH;
//		}
//
//		MMAdView adView = new MMAdView(this);
//		adView.setApid("154899");
//		MMRequest request = new MMRequest();
//		adView.setMMRequest(request);
//		adView.setId(MMSDK.getDefaultAdId());
//		adView.setWidth(placementWidth);
//		adView.setHeight(BANNER_AD_HEIGHT);
//
//		LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout2);
//		layout.removeAllViews();
//		layout.addView(adView);
//		adView.getAd();
//	}
	
//	protected boolean canFit(int adWidth) {
//		int adWidthPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, adWidth, getResources().getDisplayMetrics());
//		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
//		return metrics.widthPixels >= adWidthPx;
//	}
}
