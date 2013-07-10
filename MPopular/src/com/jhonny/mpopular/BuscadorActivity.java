package com.jhonny.mpopular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
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
import android.widget.ListView;
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


public class BuscadorActivity extends SherlockActivity implements OnItemSelectedListener {
	
	private Spinner spRed;
	private ArrayList<DetalleCuentas> listaResultados = new ArrayList<DetalleCuentas>();
	private HashMap<Integer, HashMap<Integer, String>> redes = null;
	private Integer posicionSpinnerSeleccionada = 0;
	private ListView listView;
	private AdView adView = null;
	private TextView rLabel;
	private TextView rTexto;
	private SlidingMenu menu;
	private ActionBar actionBar;
	private View view;
	private ProgressDialog pd = null;
	private int contador = 0;
	private Context context = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buscador);
		
		try{
			contador = 0;
			context = (Context)this;
			
			// carga las redes sociales de la bbdd
			cargaRedesSociales();
			
			// borra el contenido de las etiquetas de los resultados de la busqueda
			limpiaResultadosBusqueda();
			
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
	        	
//	        	PackageManager pm = getApplicationContext().getPackageManager();
//	        	ApplicationInfo ai = pm.getApplicationInfo( this.getPackageName(), 0);
//	        	String applicationName = (String)pm.getApplicationLabel(ai);
//	        	actionBar.setTitle(applicationName);
	        }
	        
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
		inflater.inflate(R.menu.menu_buscador, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				menu.toggle();
				return true;
				
			case R.id.btn_buscar_usuario:
				try{
					// comprobaciones iniciales para la busqueda
					EditText editTexto = (EditText)findViewById(R.id.editText1);
					String nombre = editTexto.getText().toString().trim();
					
					if(nombre != null){
						if(nombre.length() == 0)
							Toast.makeText(context, "No puede estar vacio", Toast.LENGTH_SHORT).show();
						else if(nombre.length() < 3)
							Toast.makeText(context, "Busqueda minima 3 caracteres", Toast.LENGTH_SHORT).show();
						else{
							// dialogo de progreso
							pd = new ProgressDialog(this);
							pd.setMessage("Buscando...");
							pd.setCancelable(false);
							pd.setIndeterminate(true);
							pd.show();
							
							BuscadorAsincrono ba = new BuscadorAsincrono();
							ba.execute();
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
			contador = 0;
			context = (Context)this;
			
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
	
	
	private void cargaRedesSociales(){
		try{
			if(Util.getRedes() == null)
				Util.cargaRedesSociales();
			
			ArrayList<String> lista = new ArrayList<String>();
			lista.add("Todas");
			lista.addAll(Util.getListaRedes());
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spRed = (Spinner)findViewById(R.id.spinner_busq);
			spRed.setOnItemSelectedListener(this);
			spRed.setAdapter(dataAdapter);
			
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
	
	
	private void buscarDatosUsuario(){
		JSONArray jArrayClave = null;
		JSONArray jArrayValor = null;
		
		try{
			listaResultados = new ArrayList<DetalleCuentas>();
			EditText editTexto = (EditText)findViewById(R.id.editText1);
			String nombre = editTexto.getText().toString().trim();
			
			// oculta el teclado
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editTexto.getWindowToken(), 0);
			
			if(nombre != null && nombre.length() > 0){
				Integer idRed = 0;
				if(posicionSpinnerSeleccionada != null && posicionSpinnerSeleccionada > 0){
					HashMap<Integer, String> elemento = redes.get(posicionSpinnerSeleccionada);
					
					Set<Integer> listaIds = elemento.keySet();
					for(Integer i : listaIds){
						if(i != null)
							idRed = i;
					}
				}
				
				while(nombre.contains(" "))
					nombre = nombre.replace(' ', '.');
				
				String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=2" +
						"&nombre=" + nombre + "&idRed=" + idRed;
				jArrayClave = Util.consultaDatosInternet(url);
				
				rLabel = (TextView)findViewById(R.id.busq_textView2);
				rTexto = (TextView)findViewById(R.id.busq_textView3);
				listView = (ListView)findViewById(R.id.listView1);
				
				if(jArrayClave != null){
					// hay datos que mostrar
					if(jArrayClave.length() > 0){
						for(int i=0; i<jArrayClave.length(); i++){
							jArrayValor = (JSONArray)jArrayClave.get(i);
							
							String n1 = (String)jArrayValor.get(0);
							String n2 = (String)jArrayValor.get(1);
							String n3 = (String)jArrayValor.get(2);
							String n4 = (String)jArrayValor.get(3);
							String n5 = (String)jArrayValor.get(4);
							String n6 = (String)jArrayValor.get(5);
							
							DetalleCuentas detalle = new DetalleCuentas(n1, n2, n3, n4, n5, n6);
							listaResultados.add(detalle);
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private void limpiaResultadosBusqueda(){
		try{
			rLabel = (TextView)findViewById(R.id.busq_textView2);
			rTexto = (TextView)findViewById(R.id.busq_textView3);
			
			rLabel.setVisibility(4); // mostrar invisible
			rTexto.setText("");
			
			if(view != null)
				view.buildDrawingCache(true);
			
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
    		if(contador == 0){
    			contador++;
    			Toast.makeText(this, getResources().getString(R.string.txt_salir_1_aviso), Toast.LENGTH_SHORT).show();
    			return true;
    		}else{
    			finish();
    		}
    	}
    	//para las demas cosas, se reenvia el evento al listener habitual
    	return super.onKeyDown(keyCode, event);
    }
	
	
	
	
	
	/**
	 * Clase privada asincrona para las busquedas
	 * @author jhonny
	 *
	 */
	private class BuscadorAsincrono extends AsyncTask<Void, Integer, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				buscarDatosUsuario();
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
			Toast.makeText(context, "Busqueda cancelada...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			try{
				if(listaResultados != null){
					rLabel.setVisibility(0); //mostrar visible
					String texto = getResources().getString(R.string.label_catidad_resultados);
					rTexto.setText(String.valueOf(listaResultados.size()) + " " + texto);
					listView.setAdapter(new DetalleCuentasAdapter(listaResultados));
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
