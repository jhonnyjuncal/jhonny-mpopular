package com.jhonny.mpopular;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MisRedesActivity extends SherlockActivity {
	
	private ListView listView;
	private AdView adView = null;
	private SlidingMenu menu;
	private ActionBar actionBar;
	private View view;
	private int contador = 0;
	private ProgressDialog pd = null;
	private Context context;
	public static DetalleRedesAdapter drAdapter = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mis_redes);
			
		try{
			contador = 0;
			this.context = this;
			
			actionBar = getSupportActionBar();
	        if(actionBar != null){
	        	actionBar.setDisplayShowCustomEnabled(true);
	        	
	        	// boton < de la action bar
	        	actionBar.setDisplayHomeAsUpEnabled(false);
	        	actionBar.setHomeButtonEnabled(true);
	        	
//	        	PackageManager pm = getApplicationContext().getPackageManager();
//	        	ApplicationInfo ai = pm.getApplicationInfo( this.getPackageName(), 0);
//	        	String applicationName = (String)pm.getApplicationLabel(ai);
//	        	actionBar.setTitle(applicationName);
	        }
	        
			menu = new SlidingMenu(this);
	        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	        menu.setShadowWidthRes(R.dimen.shadow_width);
	        menu.setShadowDrawable(R.drawable.ext_sombra);
	        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	        menu.setFadeDegree(0.35f);
	        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	        menu.setMenu(R.layout.activity_opciones);
			
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
			
			reiniciarFondoOpciones();
			muestraMisRedesSociales();
			reiniciodelaaplicacion();
			
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
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try{
			SubMenu subMenu1 = menu.addSubMenu("");
			subMenu1.add(getResources().getString(R.string.label_actualizar))
				.setIcon(R.drawable.ic_actualizar)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
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
				.setIcon(R.drawable.ic_agregar)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
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
			subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_holo_light);
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
	
	
	/**
	 * Inicia la actividad principal
	 * @param view
	 */
	public void muestraPrincipal(View view){
		try{
			this.view = view;
			
			LinearLayout layout_inicio = (LinearLayout)findViewById(R.id.opc_layout_inicio);
			layout_inicio.setBackgroundResource(R.color.gris_oscuro);
			view.buildDrawingCache(true);
			
			Intent intent = new Intent(this, PrincipalActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			menu.toggle();
		}
	}
	
	
	/**
	 * Inicia la actividad del buscador
	 * @param view
	 */
	public void muestraBuscador(View view){
		try{
			this.view = view;
			
			LinearLayout layout_busq = (LinearLayout)findViewById(R.id.opc_layout_busq);
			layout_busq.setBackgroundResource(R.color.gris_oscuro);
			view.buildDrawingCache(true);
			
			Intent intent = new Intent(this, BuscadorActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			menu.toggle();
		}
	}
	
	
	/**
	 * Inicia la actividad las redes del usuario
	 * @param view
	 */
	public void muestraMisRedes(View view){
		try{
			this.view = view;
			
			LinearLayout layout_redes = (LinearLayout)findViewById(R.id.opc_layout_redes);
			layout_redes.setBackgroundResource(R.color.gris_oscuro);
			view.buildDrawingCache(true);
			
			Intent intent = new Intent(this, MisRedesActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			menu.toggle();
		}
	}
	
	
	/**
	 * Inicia la actividad Acerca de MPopular
	 * @param view
	 */
	public void muestraAcercaDeMpopular(View view){
		try{
			this.view = view;
			
			LinearLayout layout_acerca = (LinearLayout)findViewById(R.id.opc_layout_acerca);
			layout_acerca.setBackgroundResource(R.color.gris_oscuro);
			view.buildDrawingCache(true);
			
			Intent intent = new Intent(this, AcercaActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			menu.toggle();
		}
	}
	
	
	/**
	 * Muestra los terminos y condiciones de la aplicacion
	 * @param view
	 */
	public void muestraTerminosCondiciones(View view){
		try{
			this.view = view;
			
			LinearLayout layout_terminos = (LinearLayout)findViewById(R.id.opc_layout_terminos);
			layout_terminos.setBackgroundResource(R.color.gris_oscuro);
			view.buildDrawingCache(true);
			
			Intent intent = new Intent(this, TerminosActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			menu.toggle();
		}
	}
	
	
	/**
	 * Muestra la actividad de configuraciones de la aplicacion
	 * @param view
	 */
	public void muestraConfiguracionApp(View view){
		try{
			this.view = view;
			
			LinearLayout layout_conf = (LinearLayout)findViewById(R.id.opc_layout_conf);
			layout_conf.setBackgroundResource(R.color.gris_oscuro);
			view.buildDrawingCache(true);
			
			Intent intent = new Intent(this, ConfiguracionActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			menu.toggle();
		}
	}
	
	
	/**
	 * muestra l aanimacion de ayuda de la aplicacion
	 */
	public void muestraAnimacionAyuda(View view){
		try{
			this.view = view;
			
			LinearLayout layout_conf = (LinearLayout)findViewById(R.id.opc_layout_ayuda);
			layout_conf.setBackgroundResource(R.color.gris_oscuro);
			view.buildDrawingCache(true);
			
			Intent intent = new Intent(this, AyudaActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			menu.toggle();
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
