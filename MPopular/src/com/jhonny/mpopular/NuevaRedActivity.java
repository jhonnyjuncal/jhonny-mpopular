package com.jhonny.mpopular;

import java.util.ArrayList;
import org.json.JSONArray;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class NuevaRedActivity extends SherlockActivity implements OnItemSelectedListener {
	
	private Integer posicionSpinnerSeleccionada = 0;
	private Spinner spRed;
	private AdView adView = null;
	private SlidingMenu menu;
	private ActionBar actionBar;
	private View view;
	private ProgressDialog pd = null;
	private Context context;
	private EditText nombreRed;
	private JSONArray jArray = null;
	private DetalleRedes dr = new DetalleRedes();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nueva_red);
		
		try{
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
			
			// carga las redes sociales de la bbdd
			cargaRedesSociales();
			
			// publicidad
			adView = new AdView(this, AdSize.BANNER, "a1518312d054c38");
			LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout2);
			layout.addView(adView);
			adView.loadAd(new AdRequest());
			
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
							Toast.makeText(context, "No puede estar vacio", Toast.LENGTH_SHORT).show();
						}else if(valorIntroducido.length() <= 3){
							Toast.makeText(context, "Minimo 4 caracteres", Toast.LENGTH_SHORT).show();
						}else{
							// oculta el teclado
							InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(nombreRed.getWindowToken(), 0);
							
							// dialogo de progreso
							pd = new ProgressDialog(this);
							pd.setMessage("Guardando datos...");
							pd.setCancelable(false);
							pd.setIndeterminate(true);
							pd.show();
							
							// llamada al hilo asincrono
							InsertaRedAsincrono ira = new InsertaRedAsincrono();
							ira.execute();
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
	
	
	@Override
    protected void onResume(){
		super.onResume();
		
		try{
			this.context = this;
			reiniciarFondoOpciones();
			
			TextView opc_textview1 = (TextView)findViewById(R.id.opc_textView1);
			opc_textview1.setText(Util.getNombre());
			
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
	
	
	private void insertarCuentaNueva(){
		try{
			String nombreCuenta = nombreRed.getText().toString();
			Red elemento = new Red();
			
			if(posicionSpinnerSeleccionada != null && posicionSpinnerSeleccionada > 0){
				elemento = Util.getRedes().get(posicionSpinnerSeleccionada);
				
				String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=3&nombre=" + 
						nombreCuenta + "&idUsuario=" + Util.getIdUsuario() + "&idRed=" + elemento.getIdRed();
				jArray = Util.consultaDatosInternet(url);
				
				if(jArray != null){
					dr.setIdCuenta(jArray.getInt(0));
					dr.setIdRed(jArray.getInt(1));
					Red red = Util.getRedes().get(dr.getIdRed());
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
			lista.add("Seleccionar");
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
			
			if(view != null)
				view.buildDrawingCache(true);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		Util.setMisRedes(null);
    	}
    	//para las demas cosas, se reenvia el evento al listener habitual
    	return super.onKeyDown(keyCode, event);
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
					Toast.makeText(context, "Cuenta a�adida correctamente", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, "Error al a�adir la red", Toast.LENGTH_SHORT).show();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, "Busqueda cancelada...", Toast.LENGTH_SHORT).show();
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
