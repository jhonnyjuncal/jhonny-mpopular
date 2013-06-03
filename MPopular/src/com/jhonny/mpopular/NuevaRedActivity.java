package com.jhonny.mpopular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class NuevaRedActivity extends Activity implements OnItemSelectedListener {
	
	private Integer posicionSpinnerSeleccionada = 0;
	private Spinner spRed;
	private List<String> listaRedes = new ArrayList<String>();
	private HashMap<Integer, HashMap<Integer, String>> redes = new HashMap<Integer, HashMap<Integer, String>>();
	private AdView adView = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_nueva_red);
			
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
		getMenuInflater().inflate(R.menu.menu_nueva_red, menu);
		return true;
	}
	
	
	public void insertarCuentaNueva(View view){
		ProgressDialog pd = null;
		
		try{
			// dialogo de progreso
			pd = new ProgressDialog(this);
			pd.setMessage("Guardando datos...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			
			EditText editNombre = (EditText)findViewById(R.id.editText1);
			String nombreCuenta = editNombre.getText().toString();
			Integer idUsuario = Util.getIdUsuario();
			
			if(nombreCuenta != null){
				if(nombreCuenta.length() >= 3){
					Integer idRed = 0;
					if(posicionSpinnerSeleccionada != null && posicionSpinnerSeleccionada > 0){
						HashMap<Integer, String> elemento = redes.get(posicionSpinnerSeleccionada);
					
						Set<Integer> listaIds = elemento.keySet();
						for(Integer i : listaIds){
							if(i != null)
								idRed = i;
						}
					}
					
					String url = "http://free.hostingjava.it/-jhonnyjuncal/index.jsp?consulta=3&nombre=" + 
							nombreCuenta + "&idUsuario=" + idUsuario + "&idRed=" + idRed;
					Util.consultaDatosInternet(url);
					
					if(pd != null)
						pd.dismiss();
					
					Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
					editNombre.setText("");
				}else{
					Toast.makeText(this, "Mas de 3 caracteres para busqueda", Toast.LENGTH_LONG).show();
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
		JSONArray jArray = null;
		
		try{
			String url = "http://free.hostingjava.it/-jhonnyjuncal/index.jsp?consulta=1";
			jArray = Util.consultaDatosInternet(url);
			HashMap<Integer, String> redes2 = null;
			redes = new HashMap<Integer, HashMap<Integer, String>>();
			
			if(jArray != null){
				for(int i=0; i<jArray.length(); i++){
					redes2 = new HashMap<Integer, String>();
					redes2.put(jArray.getInt(i), jArray.getString(++i));
					redes.put(i, redes2);
				}
				
				for(int i=1; i<jArray.length(); i++){
					String cadena = jArray.getString(i);
					while(cadena.contains("."))
						cadena = cadena.replace(".", " ");
					listaRedes.add(cadena);
					i++;
				}
				
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaRedes);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				spRed = (Spinner)findViewById(R.id.spinner1);
				spRed.setOnItemSelectedListener(this);
				spRed.setAdapter(dataAdapter);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
