package com.jhonny.mpopular;

import org.json.JSONArray;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class NuevoUsuarioActivity extends SherlockActivity {
	
	AdView adView = null;
	ProgressDialog pd = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_nuevo);
			
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
		inflater.inflate(R.menu.menu_nuevo, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.btn_guardar){
			guardaDatosPersonales(null);
		}
		return true;
	}
	
	public void guardaDatosPersonales(View view){
		JSONArray jArray = null;
		
		try{
			// dialogo de progreso
			pd = new ProgressDialog(this);
			pd.setMessage("Guardando datos...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			
			// se recogen los datos introducidos
			EditText editNombre = (EditText)findViewById(R.id.editText1);
			EditText editTelefono = (EditText)findViewById(R.id.editText2);
			EditText editEmail = (EditText)findViewById(R.id.editText3);
			EditText editPais = (EditText)findViewById(R.id.editText4);
			
			String n1 = editNombre.getText().toString().replaceAll(" ", ".");
			String n2 = editTelefono.getText().toString();
			String n3 = editEmail.getText().toString();
			String n4 = editPais.getText().toString().replaceAll(" ", ".");
			
			String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=0&nombre="; 
			url+= n1 + "&telefono=" + n2;
			url+= "&email=" + n3 + "&pais=" + n4;
			jArray = Util.consultaDatosInternet(url);
			
			if(jArray != null && jArray.length() > 0){
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
				
				FileUtil.almacenaDatosConfiguracion(this);
				
				Intent intent = new Intent(this, PrincipalActivity.class);
				startActivity(intent);
				
			}else{
				Toast.makeText(this, "ERROR DE CONEXION CON EL SERVIDOR", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(pd != null)
				pd.dismiss();
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
//    		Intent intent = new Intent(Intent.ACTION_MAIN);
//    		intent.addCategory(Intent.CATEGORY_HOME);
//    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    		startActivity(intent);
    		Toast.makeText(this, "Ha pulsado atras", Toast.LENGTH_SHORT).show();
    	}
    	return false;
    	//para las demas cosas, se reenvia el evento al listener habitual
//    	return super.onKeyDown(keyCode, event);
    }
}
