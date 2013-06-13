package com.jhonny.mpopular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.json.JSONArray;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;


public class BuscadorActivity extends SherlockActivity implements OnItemSelectedListener {
	
	private Spinner spRed;
	private ArrayList<DetalleCuentas> listaResultados = new ArrayList<DetalleCuentas>();
	private HashMap<Integer, HashMap<Integer, String>> redes = null;
	private Integer posicionSpinnerSeleccionada = 0;
	private ListView listView;
	private AdView adView = null;
	private TextView rLabel;
	private TextView rTexto;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buscador);
		
		try{
			ActionBar ab = getSupportActionBar();
	        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
	        ab.setTitle(getString(R.string.label_buscador));
	        
			// carga las redes sociales de la bbdd
			cargaRedesSociales();
			
			// borra el contenido de las etiquetas de los resultados de la busqueda
			limpiaResultadosBusqueda();
			
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
	
	
	private void cargaRedesSociales(){
		try{
			if(Util.getRedes() == null)
				Util.cargaRedesSociales();
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, 
						Util.getListaRedes());
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
}
