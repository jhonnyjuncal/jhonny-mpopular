package com.jhonny.mpopular.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jhonny.mpopular.R;
import com.jhonny.mpopular.util.Util;
import com.jhonny.mpopular.adapter.DetalleRedes;
import com.jhonny.mpopular.adapter.DetalleRedesAdapter;
import com.jhonny.mpopular.util.FileUtil;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;


public class MisRedesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	
	private ListView listView;
	private View view;
	private int contador = 0;
	private ProgressDialog pd = null;
	private Context context;
	public static DetalleRedesAdapter drAdapter = null;
	
	//Constants for tablet sized ads (728x90)
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int MED_BANNER_WIDTH = 480;
	//Constants for phone sized ads (320x50)
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mis_redes);
			
		try{
			// barra de herramientas
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);

			contador = 0;
			this.context = this;

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
    protected void onResume(){
		super.onResume();
		
		try{
			contador = 0;
			this.context = this;
			
//			reiniciarFondoOpciones();
			muestraMisRedesSociales();
			reiniciodelaaplicacion();
			cargaPublicidad();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void reiniciodelaaplicacion(){
		if(!FileUtil.cargaDatosPersonales(this)){
			Intent intent = new Intent(this, NuevoUsuarioActivity.class);
			startActivity(intent);
		}
		
//		TextView opc_textview1 = (TextView)findViewById(R.id.opc_textView1);
//		if(Util.getNombre() == null || Util.getNombre().length() == 0)
//			FileUtil.cargaDatosPersonales(context);
//		opc_textview1.setText(Util.getNombre());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try{
			SubMenu subMenu1 = menu.addSubMenu("");
			subMenu1.add(getResources().getString(R.string.label_actualizar))
				.setIcon(R.mipmap.ic_actualizar)
				.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						try{
							actualizaMisRedesSocialesEnPantalla();
						}catch(Exception ex){
							ex.printStackTrace();
							return false;
						}
						return true;
					}
				});
			
			subMenu1.add(getResources().getString(R.string.label_add_nueva_red))
				.setIcon(R.mipmap.ic_agregar)
				.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						try{
							navegaNuevaRedActivity();
						}catch(Exception ex){
							ex.printStackTrace();
							return false;
						}
						return true;
					}
				});
			
			MenuItem subMenu1Item = subMenu1.getItem();
			subMenu1Item.setIcon(R.mipmap.ic_configuracion);
			subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return true;
	}
		
	private void navegaNuevaRedActivity(){
		try{
			Intent intent = new Intent(this, NuevaRedActivity.class);
			startActivity(intent);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void actualizaMisRedesSocialesEnPantalla(){
		try{
			// dialogo de progreso
			pd = new ProgressDialog(this);
			pd.setMessage(getResources().getString(R.string.txt_actualizando));
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			
			ActualizaAsincrono aa = new ActualizaAsincrono();
			aa.execute();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void muestraMisRedesSociales(){
		try{
			if(Util.getMisRedes() == null || Util.getMisRedes().size() == 0)
				Util.cargaMisCuentas(context);
			
			for(DetalleRedes dr : Util.getMisRedes()){
				dr.setImagenEditar((ImageView)findViewById(R.id.imgeditar));
				dr.setImagenEliminar((ImageView)findViewById(R.id.imgeliminar));
			}
			
			drAdapter = new DetalleRedesAdapter(this, R.layout.listview_mis_redes, Util.getMisRedes());
			listView = (ListView)findViewById(R.id.listView1);
			listView.setAdapter(drAdapter);
			listView.setItemsCanFocus(false);
			listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Util.setPosEnEdicion(position);
					}
				}
			);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

//	private void reiniciarFondoOpciones(){
//		try{
//			LinearLayout layout_busq = (LinearLayout)findViewById(R.id.opc_layout_busq);
//			layout_busq.setBackgroundResource(R.color.gris_claro);
//
//			LinearLayout layout_redes = (LinearLayout)findViewById(R.id.opc_layout_redes);
//			layout_redes.setBackgroundResource(R.color.gris_claro);
//
//			LinearLayout layout_conf = (LinearLayout)findViewById(R.id.opc_layout_conf);
//			layout_conf.setBackgroundResource(R.color.gris_claro);
//
//			LinearLayout layout_acerca = (LinearLayout)findViewById(R.id.opc_layout_acerca);
//			layout_acerca.setBackgroundResource(R.color.gris_claro);
//
//			LinearLayout layout_terminos = (LinearLayout)findViewById(R.id.opc_layout_terminos);
//			layout_terminos.setBackgroundResource(R.color.gris_claro);
//
//			LinearLayout layout_ayuda = (LinearLayout)findViewById(R.id.opc_layout_ayuda);
//			layout_ayuda.setBackgroundResource(R.color.gris_claro);
//
//			if(view != null)
//				view.buildDrawingCache(true);
//
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//	}

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
	
	private void cargaPublicidad(){
		int placementWidth = BANNER_AD_WIDTH;
		
		//Finds an ad that best fits a users device.
		if(canFit(IAB_LEADERBOARD_WIDTH)) {
		    placementWidth = IAB_LEADERBOARD_WIDTH;
		}else if(canFit(MED_BANNER_WIDTH)) {
		    placementWidth = MED_BANNER_WIDTH;
		}
		
		MMAdView adView = new MMAdView(this);
		adView.setApid("154899");
		MMRequest request = new MMRequest();
		adView.setMMRequest(request);
		adView.setId(MMSDK.getDefaultAdId());
		adView.setWidth(placementWidth);
		adView.setHeight(BANNER_AD_HEIGHT);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout2);
		layout.removeAllViews();
		layout.addView(adView);
		adView.getAd();
	}
	
	protected boolean canFit(int adWidth) {
		int adWidthPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, adWidth, getResources().getDisplayMetrics());
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		return metrics.widthPixels >= adWidthPx;
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
	
	
	
	
	
	/**
	 * Clase privada para la actualizacion de la lista de cuentas
	 * @author jhonny
	 *
	 */
	private class ActualizaAsincrono extends AsyncTask<Void, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try{
				Util.cargaMisCuentas(context);
				for(DetalleRedes dr : Util.getMisRedes()){
					dr.setImagenEditar((ImageView)findViewById(R.id.imgeditar));
					dr.setImagenEliminar((ImageView)findViewById(R.id.imgeliminar));
				}
				publishProgress(0);
			}catch(Exception ex){
				ex.printStackTrace();
				return false;
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			try{
				if(pd != null)
					pd.dismiss();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, getResources().getString(R.string.txt_actualiza_cancelada)
					, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			try{
				drAdapter.notifyDataSetChanged();
				
//				DetalleRedesAdapter dra = new DetalleRedesAdapter(MisRedesActivity.this, 
//								R.layout.listview_mis_redes, Util.getMisRedes());
//				
//				listView = (ListView)findViewById(R.id.listView1);
//				listView.setAdapter(dra);
//				listView.setItemsCanFocus(false);
//				listView.setOnItemClickListener(new OnItemClickListener() {
//						@Override
//						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//							Toast.makeText(MisRedesActivity.this,"Clicked:" + position, Toast.LENGTH_SHORT).show();
//						}
//					}
//				);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
