package com.jhonny.mpopular;

import java.util.ArrayList;
import java.util.List;
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mis_redes);
	}
	
	
	@Override
    protected void onResume(){
    	super.onResume();
    	
    	try{
    		muestraMisRedesSociales();
        }catch(Exception ex){
        	ex.printStackTrace();
        }
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
			
			if(jArray != null){
				for(int i=0; i<jArray.length(); i++){
					listaRedes.add(jArray.getString(i) + " - " + jArray.getString(++i));
				}
			}
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.listview_mis_redes, listaRedes);
			dataAdapter.setDropDownViewResource(R.layout.listview_mis_redes);
			
			listView = (ListView)findViewById(R.id.listView1);
			listView.setAdapter(dataAdapter);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
