package com.jhonny.mpopular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MisRedesActivity extends Activity {
	
	private ListView listView;
	private List<String> listaRedes = new ArrayList<String>();
	private Map<Integer, String> redes = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mis_redes);
		
		muestraMisRedesSociales();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_mis_redes, menu);
		return true;
	}
	
	
	public void nuevaRedSocial(View view){
		Intent intent = new Intent(this, NuevaRedActivity.class);
		startActivity(intent);
	}
	
	
	private void muestraMisRedesSociales(){
		JSONArray jArray = null;
		
		try{
			String url = "http://free.hostingjava.it/-jhonnyjuncal/index.jsp?consulta=4&idUsuario=" + Util.getIdUsuario();
			jArray = Util.consultaDatosInternet(url);
			redes = new HashMap<Integer, String>();
			
			for(int i=0; i<jArray.length(); i++){
				redes.put(jArray.getInt(i), jArray.getString(++i));
			}
			
			if(redes != null){
				for(int i=1; i<=redes.size(); i++){
					listaRedes.add((String)redes.get(i));
				}
			}
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.listview_personalizado, listaRedes);
			dataAdapter.setDropDownViewResource(R.layout.listview_personalizado);
			
			listView = (ListView)findViewById(R.id.listView1);
			listView.setAdapter(dataAdapter);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
