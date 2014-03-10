package com.jhonny.mpopular;

import org.json.JSONArray;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;


public class ConfiguracionActivity extends SherlockActivity {
	
	private SlidingMenu menu;
	private ActionBar actionBar;
	private View view;
	private ProgressDialog pd = null;
	private int contador = 0;
	private Context context;
	private EditText editNombre;
	private EditText editTelefono;
	private EditText editEmail;
	private EditText editPais;
	private JSONArray jArray2 = null;
	
	//Constants for tablet sized ads (728x90)
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int MED_BANNER_WIDTH = 480;
	//Constants for phone sized ads (320x50)
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion);
		
		try{
			contador = 0;
			this.context = this;
			
			menu = new SlidingMenu(this);
	        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	        menu.setShadowWidthRes(R.dimen.shadow_width);
	        menu.setShadowDrawable(R.drawable.ext_sombra);
	        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	        menu.setFadeDegree(0.35f);
	        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	        menu.setMenu(R.layout.activity_opciones);
	        
	        actionBar = getSupportActionBar();
	        if(actionBar != null){
	        	actionBar.setDisplayShowCustomEnabled(true);
	        	// boton < de la action bar
	        	actionBar.setDisplayHomeAsUpEnabled(false);
	        	actionBar.setHomeButtonEnabled(true);
	        }
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_configuracion, menu);
		return true;
	}
	
	@Override
    protected void onResume(){
		super.onResume();
		
		try{
			contador = 0;
			
			reiniciarFondoOpciones();
			cargaDatosUsuario();
			reiniciodelaaplicacion();
			estableceFuenteRoboto();
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
		
		TextView opc_textview1 = (TextView)findViewById(R.id.opc_textView1);
		if(Util.getNombre() == null || Util.getNombre().length() == 0)
			FileUtil.cargaDatosPersonales(context);
		opc_textview1.setText(Util.getNombre());
	}
	
	private void estableceFuenteRoboto(){
		TextView textView = (TextView)findViewById(R.id.conf_textView1);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.conf_textView2);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.conf_textView3);
		textView.setTypeface(Util.getRoboto7(this));
		Button boton = (Button)findViewById(R.id.btn_baja_definitiva);
		boton.setTypeface(Util.getRoboto7(this));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				menu.toggle();
				return true;
			
			case R.id.conf_btn_guardar:
				try{
					// comprobacion de datos obligatorios
					editNombre = (EditText)findViewById(R.id.conf_editText1);
					editTelefono = (EditText)findViewById(R.id.conf_editText2);
					editEmail = (EditText)findViewById(R.id.conf_editText3);
					editPais = (EditText)findViewById(R.id.conf_editText4);
					
					String nombre = editNombre.getText().toString().trim();
					String telefono = editTelefono.getText().toString().trim();
					String email = editEmail.getText().toString().trim();
					String pais = editPais.getText().toString().trim();
					
					if(nombre == null || nombre.length() == 0)
						Toast.makeText(context, getResources().getString(R.string.txt_nombre_no_vacio)
								, Toast.LENGTH_SHORT).show();
					else if(nombre.length() < 3)
						Toast.makeText(context, getResources().getString(R.string.txt_nombre_menos3)
								, Toast.LENGTH_SHORT).show();
					else if(nombre.contains(" "))
						Toast.makeText(context, getResources().getString(R.string.txt_nombre_no_espacio)
								, Toast.LENGTH_SHORT).show();
					else if(telefono == null || telefono.length() == 0)
						Toast.makeText(context, getResources().getString(R.string.txt_telefono_no_vacio)
								, Toast.LENGTH_SHORT).show();
					else if(telefono.length() < 3)
						Toast.makeText(context, getResources().getString(R.string.txt_telefono_menos3)
								, Toast.LENGTH_SHORT).show();
					else if(email == null || email.length() == 0)
						Toast.makeText(context, getResources().getString(R.string.txt_email_no_vacio)
								, Toast.LENGTH_SHORT).show();
					else if(email.length() < 3)
						Toast.makeText(context, getResources().getString(R.string.txt_email_menos3)
								, Toast.LENGTH_SHORT).show();
					else if(!email.contains("@") || !email.contains("."))
						Toast.makeText(context, getResources().getString(R.string.txt_email_incorrecto)
								, Toast.LENGTH_SHORT).show();
					else if(pais == null || pais.length() == 0)
						Toast.makeText(context, getResources().getString(R.string.txt_pais_no_vacio)
								, Toast.LENGTH_SHORT).show();
					else if(pais.length() < 3)
						Toast.makeText(context, getResources().getString(R.string.txt_pais_menos3)
								, Toast.LENGTH_SHORT).show();
					else{
						// oculta el teclado
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(editPais.getWindowToken(), 0);
						
						// dialogo de progreso
						pd = new ProgressDialog(this);
						pd.setMessage(getResources().getString(R.string.txt_guardando));
						pd.setCancelable(false);
						pd.setIndeterminate(true);
						pd.show();
						
						ConfiguracionAsincrono ca = new ConfiguracionAsincrono();
						ca.execute();
					}
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void cargaDatosUsuario(){
		try{
			editNombre = (EditText)findViewById(R.id.conf_editText1);
			editNombre.setText(Util.getNombre());
			
			editTelefono = (EditText)findViewById(R.id.conf_editText2);
			editTelefono.setText(Util.getTelefono());
			
			editEmail = (EditText)findViewById(R.id.conf_editText3);
			editEmail.setText(Util.getEmail());
			
			editPais = (EditText)findViewById(R.id.conf_editText4);
			editPais.setText(Util.getPais());
			
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
	
	private void guardaDatosConfiguracion(){
		JSONArray jArray1 = null;
		
		try{
			// se recogen los datos introducidos
			editNombre = (EditText)findViewById(R.id.conf_editText1);
			editTelefono = (EditText)findViewById(R.id.conf_editText2);
			editEmail = (EditText)findViewById(R.id.conf_editText3);
			editPais = (EditText)findViewById(R.id.conf_editText4);
			
			String n1 = editNombre.getText().toString().replaceAll(" ", ".");
			String n2 = editTelefono.getText().toString();
			String n3 = editEmail.getText().toString();
			String n4 = editPais.getText().toString().replaceAll(" ", ".");
			
			String url = "http://jhonnyspring-mpopular.rhcloud.com/index.jsp?consulta=8";
			url+="&idUsuario=" + Util.getIdUsuario() + "&nombre=";
			url+= n1 + "&telefono=" + n2 + "&email=" + n3 + "&pais=" + n4;
			
			jArray1 = Util.consultaDatosInternet(url);
			
			if(jArray1 != null && jArray1.length() > 0){
				// Seteo de datos de usuario
				Util.setIdUsuario(jArray1.getInt(0));
				String nombre = jArray1.getString(1);
				while(nombre.contains("."))
					nombre = nombre.replace('.', ' ');
				Util.setNombre(nombre);
				Util.setTelefono(jArray1.getString(2));
				Util.setEmail(jArray1.getString(3));
				String pais = jArray1.getString(4);
				while(pais.contains("."))
					pais = pais.replace('.', ' ');
				Util.setPais(pais);
				
				FileUtil.almacenaDatosConfiguracion(context);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void eliminaUsuarioYDatos(){
		try{
			String url = "http://jhonnyspring-mpopular.rhcloud.com/index.jsp?consulta=10" +
					"&idUsuario=" + Util.getIdUsuario();
			jArray2 = Util.consultaDatosInternet(url);
			
			if(jArray2 != null && jArray2.getInt(0) == 1){
				// borrado realizado correctamente
				Util.eliminacionCompletaDatos(context);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void eliminaUsuarioDefinitivamente(View view){
		try{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle(getResources().getString(R.string.txt_borrado_total));
			alertDialogBuilder.setMessage(getResources().getString(R.string.txt_borrado_total_confirma));
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton(R.string.txt_aceptar, 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							try{
								// dialogo de progreso
								pd = new ProgressDialog(context);
								pd.setMessage(getResources().getString(R.string.txt_borrando));
								pd.setCancelable(false);
								pd.setIndeterminate(true);
								pd.show();
								
								EliminaUsuarioAsincrono eua = new EliminaUsuarioAsincrono();
								eua.execute();
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				);
				alertDialogBuilder.setNegativeButton(R.string.txt_cancelar, 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try{
								dialog.cancel();
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				);
				
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				
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
	
	
	
	
	
	
	
	private class ConfiguracionAsincrono extends AsyncTask<Void, Integer, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				guardaDatosConfiguracion();
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
				Toast.makeText(context, getResources().getString(R.string.txt_guardado_ok)
						, Toast.LENGTH_SHORT).show();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, getResources().getString(R.string.txt_guardado_cancelado)
					, Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	
	
	
	private class EliminaUsuarioAsincrono extends AsyncTask<Void, Integer, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				eliminaUsuarioYDatos();
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
				Toast.makeText(context, getResources().getString(R.string.txt_borrado_ok)
						, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, NuevoUsuarioActivity.class);
				startActivity(intent);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, getResources().getString(R.string.txt_borrado_cancelado)
					, Toast.LENGTH_SHORT).show();
		}
	}
}
