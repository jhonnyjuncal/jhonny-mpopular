package com.jhonny.mpopular;

import org.json.JSONArray;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class NuevoUsuarioActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_nuevo, menu);
		return true;
	}
	
	
	public void guardaDatosPersonales(View view){
		ProgressDialog pd = null;
		JSONArray jArray = null;
		
		try{
			EditText editNombre = (EditText)findViewById(R.id.editText1);
			EditText editTelefono = (EditText)findViewById(R.id.editText2);
			EditText editEmail = (EditText)findViewById(R.id.editText3);
			EditText editPais = (EditText)findViewById(R.id.editText4);
			
			
			pd = ProgressDialog.show(this,"Usuario nuevo","guardando datos...",true,false,null);
			
			String url = "http://free.hostingjava.it/-jhonnyjuncal/index.jsp?consulta=0&nombre=" + 
				editNombre.getText().toString() + "&telefono=" + editTelefono.getText().toString() +
				"&email=" + editEmail.getText().toString() + "&pais=" + editPais.getText().toString();
			
			jArray = Util.consultaDatosInternet(url);
			
			// Seteo de datos de usuario
			Util.setIdUsuario(jArray.getInt(0));
			Util.setNombre(jArray.getString(1));
			Util.setTelefono(jArray.getString(2));
			Util.setEmail(jArray.getString(3));
			Util.setPais(jArray.getString(4));
			
			FileUtil.almacenaDatosConfiguracion(this);
			
			Intent intent = new Intent(this, PrincipalActivity.class);
			startActivity(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(pd != null)
				pd.dismiss();
		}
	}
}
