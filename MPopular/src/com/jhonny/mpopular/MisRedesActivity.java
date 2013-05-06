package com.jhonny.mpopular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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
			String url = "http://free.hostingjava.it/-jhonnyjuncal/index.jsp?consulta=1";
			jArray = Util.consultaDatosInternet(url);
			redes = new HashMap<Integer, String>((Map<Integer, String>)jArray.get(1));
			
			if(redes != null){
				for(int i=0; i<redes.size(); i++){
					listaRedes.add((String)redes.get(i));
				}
			}
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaRedes);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			listView = (ListView)findViewById(R.id.listView1);
			
			StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.listview_personalizado, listaRedes);
			listView.setAdapter(adapter);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	private class StableArrayAdapter extends ArrayAdapter<String> {
		
		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
		
		
		public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
			
			for(int i=0; i<objects.size(); i++){
				mIdMap.put(objects.get(i), i);
			}
		}
		
		
		@Override
	    public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}
		
		
		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}
