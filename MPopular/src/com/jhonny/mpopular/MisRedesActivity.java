package com.jhonny.mpopular;

import java.util.ArrayList;
import org.json.JSONArray;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class MisRedesActivity extends Activity {
	
	private ListView listView;
	private ArrayList<DetalleRedes> misRedes = new ArrayList<DetalleRedes>();
	private AdView adView = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_mis_redes);
			
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
			misRedes = new ArrayList<DetalleRedes>();
			String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=4&idUsuario=" + Util.getIdUsuario();
			jArray = Util.consultaDatosInternet(url);
			
			if(jArray != null){
				for(int i=0; i<jArray.length(); i++){
					String n1 = jArray.getString(i);
					String n2 = jArray.getString(++i);
					ImageView img1 = (ImageView)findViewById(R.id.imgeditar);
					ImageView img2 = (ImageView)findViewById(R.id.imgeliminar);
					DetalleRedes detalle = new DetalleRedes(n1, n2, img1, img2);
					
					misRedes.add(detalle);
				}
			}
			
			DetalleRedesAdapter dra = new DetalleRedesAdapter(this, R.layout.listview_mis_redes, misRedes);
			listView = (ListView)findViewById(R.id.listView1);
			listView.setAdapter(dra);
			listView.setItemsCanFocus(false);
			listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Toast.makeText(MisRedesActivity.this,"Clicked:" + position, Toast.LENGTH_SHORT).show();
					}
				}
			);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
