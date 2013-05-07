package com.jhonny.mpopular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class BuscadorActivity extends Activity implements OnItemSelectedListener {
	
	private Spinner spRed;
	private List<String> listaRedes = new ArrayList<String>();
	private Map<Integer, String> redes = null;
	private Integer idRed = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buscador);
		
		try{
			// carga las redes sociales de la bbdd
			cargaRedesSociales();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_buscador, menu);
		return true;
	}
	
	
	private void cargaRedesSociales(){
		JSONArray jArray = null;
		
		try{
			String url = "http://free.hostingjava.it/-jhonnyjuncal/index.jsp?consulta=1";
			jArray = Util.consultaDatosInternet(url);
			redes = new HashMap<Integer, String>();
			for(int i=0; i<jArray.length(); i++){
				redes.put(jArray.getInt(i), jArray.getString(++i));
			}
			
			if(redes != null){
				for(int i=1; i<=redes.size(); i++){
					listaRedes.add((String)redes.get(i));
				}
				listaRedes.add("Todas");
			}
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaRedes);
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
		try{
			if(redes != null){
				for(int i=1; i<redes.size(); i++){
					if(pos == i){
						idRed = pos;
						return;
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	
	public void buscarDatosUsuario(View view){
		try{
			EditText editTexto = (EditText)findViewById(R.id.editText1);
			String nombre = editTexto.getText().toString().trim();
			
			if(nombre != null && nombre.length() > 0){
				String url = "http://free.hostingjava.it/-jhonnyjuncal/index.jsp?consulta=2" +
						"&nombre=" + nombre + "&idRed=" + idRed;
				Util.consultaDatosInternet(url);
			}else
				Toast.makeText(this, "No puede estar vacio", Toast.LENGTH_SHORT).show();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
