package com.jhonny.mpopular;

import org.json.JSONArray;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PrincipalActivity extends Activity {
	
	AdView adView = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		
		// publicidad
		adView = new AdView(this, AdSize.BANNER, "a1518312d054c38");
		LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout2);
		layout.addView(adView);
		adView.loadAd(new AdRequest());
		
		boolean existen = FileUtil.cargaDatosPersonales(this);
		
		if(!existen){
			Intent intent = new Intent(this, NuevoUsuarioActivity.class);
			startActivity(intent);
		}else{
			recuperarDatosUsuario(Util.getIdUsuario());
			muestraDatosPersonales();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return true;
	}
	
	
	public void muestraBuscador(View view){
		try{
			Intent intent = new Intent(this, BuscadorActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	public void muestraMisRedes(View view){
		try{
			Intent intent = new Intent(this, MisRedesActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private void muestraDatosPersonales(){
		try{
			TextView textoNombre = (TextView)findViewById(R.id.textView2);
			TextView textoTelefono = (TextView)findViewById(R.id.textView4);
			TextView textoEmail = (TextView)findViewById(R.id.textView6);
			TextView textoPais = (TextView)findViewById(R.id.textView8);
			
			textoNombre.setText(Util.getNombre());
			textoTelefono.setText(Util.getTelefono());
			textoEmail.setText(Util.getEmail());
			textoPais.setText(Util.getPais());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private void recuperarDatosUsuario(Integer idUsuario){
		JSONArray jArray = null;
		
		try{
			String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=5&idUsuario=" + Util.getIdUsuario();
			jArray = Util.consultaDatosInternet(url);
			
			// Seteo de datos de usuario
			Util.setIdUsuario(jArray.getInt(0));
			String nombre = jArray.getString(1);
			while(nombre.contains("."))
				nombre = nombre.replace('.', ' ');
			Util.setNombre(nombre);
			Util.setTelefono(jArray.getString(2));
			Util.setEmail(jArray.getString(3));
			String pais = jArray.getString(4);
			while(pais.contains("."))
				pais = pais.replace('.', ' ');
			Util.setPais(pais);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
