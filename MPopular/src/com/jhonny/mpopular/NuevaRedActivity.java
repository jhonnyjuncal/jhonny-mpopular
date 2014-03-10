package com.jhonny.mpopular;

import java.util.ArrayList;
import org.json.JSONArray;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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


public class NuevaRedActivity extends SherlockActivity implements OnItemSelectedListener {
	
	private Integer posicionSpinnerSeleccionada = 0;
	private Spinner spRed;
	private SlidingMenu menu;
	private ActionBar actionBar;
	private View view;
	private ProgressDialog pd = null;
	private Context context;
	private EditText nombreRed;
	private JSONArray jArray = null;
	private DetalleRedes dr = new DetalleRedes();
	
	//Constants for tablet sized ads (728x90)
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int MED_BANNER_WIDTH = 480;
	//Constants for phone sized ads (320x50)
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nueva_red);
		
		try{
			this.context = (Context)this;
			
			actionBar = getSupportActionBar();
	        if(actionBar != null){
	        	actionBar.setDisplayShowCustomEnabled(true);
	        	// boton < de la action bar
	        	actionBar.setDisplayHomeAsUpEnabled(false);
	        	actionBar.setHomeButtonEnabled(true);
	        }
	        
			menu = new SlidingMenu(this);
	        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	        menu.setShadowWidthRes(R.dimen.shadow_width);
	        menu.setShadowDrawable(R.drawable.ext_sombra);
	        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	        menu.setFadeDegree(0.35f);
	        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	        menu.setMenu(R.layout.activity_opciones);
			
			// carga las redes sociales de la bbdd
			cargaRedesSociales();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_nueva_red, menu);
		return true;
	}

	@Override
    protected void onResume(){
		super.onResume();
		
		try{
			this.context = (Context)this;
			
			reiniciarFondoOpciones();
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
		TextView textView = (TextView)findViewById(R.id.new_textView1);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.new_textView2);
		textView.setTypeface(Util.getRoboto7(this));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				menu.toggle();
				return true;
				
			case R.id.btn_guardar_red:
				try{
					// validacion de datos
					nombreRed = (EditText)findViewById(R.id.nueva_red_editText1);
					String valorIntroducido = nombreRed.getText().toString();
					
					if(valorIntroducido != null){
						if(valorIntroducido.length() == 0){
							Toast.makeText(context, getResources().getString(R.string.txt_nombre_no_vacio)
									, Toast.LENGTH_SHORT).show();
						}else if(valorIntroducido.length() < 3){
							Toast.makeText(context, getResources().getString(R.string.txt_nombre_menos3)
									, Toast.LENGTH_SHORT).show();
						}else if(valorIntroducido.contains(" ")){
							Toast.makeText(context, getResources().getString(R.string.txt_nombre_no_espacio)
									, Toast.LENGTH_SHORT).show();
						}else{
							if(Util.compruebaExistenciaRed(valorIntroducido, posicionSpinnerSeleccionada, null)){
								// oculta el teclado
								InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(nombreRed.getWindowToken(), 0);
								
								// dialogo de progreso
								pd = new ProgressDialog(this);
								pd.setMessage(getResources().getString(R.string.txt_guardando));
								pd.setCancelable(false);
								pd.setIndeterminate(true);
								pd.show();
								
								// llamada al hilo asincrono
								InsertaRedAsincrono ira = new InsertaRedAsincrono();
								ira.execute();
								
							}else{
								// La red ya existe
								Toast.makeText(context, context.getResources().getString(R.string.txt_cuenta_duplicada)
										, Toast.LENGTH_SHORT).show();
							}
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
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
	
	private void insertarCuentaNueva(){
		try{
			String nombreCuenta = nombreRed.getText().toString();
			Red elemento = new Red();
			
			if(posicionSpinnerSeleccionada != null && posicionSpinnerSeleccionada > 0){
				elemento = Util.getRedes().get(posicionSpinnerSeleccionada);
				
				String url = "http://jhonnyspring-mpopular.rhcloud.com/index.jsp?consulta=3&nombre=" + 
						nombreCuenta + "&idUsuario=" + Util.getIdUsuario() + "&idRed=" + elemento.getIdRed();
				jArray = Util.consultaDatosInternet(url);
				
				if(jArray != null){
					dr.setIdCuenta(jArray.getInt(0));
					dr.setIdRed(jArray.getInt(1));
					Red red = Util.getRedes().get(elemento.getIdRed());
					dr.setNombreCuenta(red.getNombreRed());
					dr.setNombreUsuario(jArray.getString(3));
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		posicionSpinnerSeleccionada = pos;
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	private void cargaRedesSociales(){
		try{
			if(Util.getRedes() == null || Util.getRedes().size() == 0)
				Util.cargaRedesSociales();
			
			ArrayList<String> lista = new ArrayList<String>();
			lista.add(getResources().getString(R.string.label_seleccionar_red));
			lista.addAll(Util.getListaRedes());
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spRed = (Spinner)findViewById(R.id.spinner_nueva_red);
			spRed.setOnItemSelectedListener(this);
			spRed.setAdapter(dataAdapter);
			
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
    		Util.cargaMisCuentas(context);
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
	
	
	
	
	
	
	private class InsertaRedAsincrono extends AsyncTask<Void, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try{
				insertarCuentaNueva();
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
				
				MisRedesActivity.drAdapter.add(dr);
				
				if(jArray != null){
					nombreRed.setText("");
					posicionSpinnerSeleccionada = 0;
					spRed.setSelection(0);
					Toast.makeText(context, getResources().getString(R.string.txt_cuanta_guardada_ok)
							, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, getResources().getString(R.string.txt_cuanta_guardada_error)
							, Toast.LENGTH_SHORT).show();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, getResources().getString(R.string.txt_cuanta_guardada_cancel)
					, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			try{
				MisRedesActivity.drAdapter.notifyDataSetChanged();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
