package com.jhonny.mpopular;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class NuevoActivity extends Activity {
	
	private InputStream is = null;
	private StringBuilder sb = null;
	private JSONArray jArray = null;
	private JSONObject jObject = null;
	private String result = null;
	
	
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
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		try{
			EditText editNombre = (EditText)findViewById(R.id.editText1);
			EditText editTelefono = (EditText)findViewById(R.id.editText2);
			EditText editEmail = (EditText)findViewById(R.id.editText3);
			EditText editPais = (EditText)findViewById(R.id.editText4);
			
			
			pd = ProgressDialog.show(this,"Usuario nuevo","guardando datos...",true,false,null);
			
			HttpClient httpclient = new DefaultHttpClient();
			String url = "http://free.hostingjava.it/-jhonnyjuncal/index.jsp?consulta=0&nombre=" + 
				editNombre.getText().toString() + "&telefono=" + editTelefono.getText().toString() +
				"&email=" + editEmail.getText().toString() + "&pais=" + editPais.getText().toString();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			
			// Conversion de la repsueta en Sring
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			sb = new StringBuilder();
			sb.append(reader.readLine());
			
			is.close();
			result = sb.toString();
			result = result.replace(result.substring(result.indexOf("<!DOCTYPE"), result.length()),"");
			
			
			// Lectura de los datos de respuesta
			jObject = new JSONObject(result);
			jArray = new JSONArray(jObject.get("valores").toString());
			
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
