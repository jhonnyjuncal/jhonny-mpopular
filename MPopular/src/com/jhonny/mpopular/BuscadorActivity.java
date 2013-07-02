package com.jhonny.mpopular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.json.JSONArray;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buscador);
		
		try{
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
		if(item.getItemId() == R.id.btn_guardar){
			buscarDatosUsuario(null);
		}
		return true;
	}
	
	
	@Override
    protected void onResume(){
		super.onResume();

		try{
			reiniciarFondoOpciones();
		
			TextView opc_textview1 = (TextView)findViewById(R.id.opc_textView1);
			opc_textview1.setText(Util.getNombre());
			
		}catch(Exception ex){
			ex.printStackTrace();
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
			
			spRed = (Spinner)findViewById(R.id.spinner1);
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
	
	
	public void buscarDatosUsuario(View view){
		this.view = view;
		JSONArray jArrayClave = null;
		JSONArray jArrayValor = null;
		ProgressDialog pd = null;
		
		try{
			pd = new ProgressDialog(this);
			pd.setMessage("Buscando...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			
			listaResultados = new ArrayList<DetalleCuentas>();
			EditText editTexto = (EditText)findViewById(R.id.editText1);
			String nombre = editTexto.getText().toString().trim();
			
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
				
				rLabel = (TextView)findViewById(R.id.textView2);
				rTexto = (TextView)findViewById(R.id.textView3);
				
				if(jArrayClave != null){
					// hay datos que mostrar
					rLabel.setVisibility(0); // mostrar visible
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
						String texto = getResources().getString(R.string.label_catidad_resultados);
						rTexto.setText(String.valueOf(listaResultados.size()) + " " + texto);
					}else{
						String texto = getResources().getString(R.string.label_sin_resultados);
						rTexto.setText(texto);
					}
				}
				
				listView = (ListView)findViewById(R.id.listView1);
				listView.setAdapter(new DetalleCuentasAdapter(listaResultados));
				
			}else{
				Toast.makeText(this, "No puede estar vacio", Toast.LENGTH_SHORT).show();
			}
			
			
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editTexto.getWindowToken(), 0);
			
			if(view != null)
				view.buildDrawingCache(true);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(pd != null)
				pd.dismiss();
		}
	}
	
	
	private void limpiaResultadosBusqueda(){
		try{
			rLabel = (TextView)findViewById(R.id.textView2);
			rTexto = (TextView)findViewById(R.id.textView3);
			
			rLabel.setVisibility(4); // mostrar invisible
			rTexto.setText("");
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
}
