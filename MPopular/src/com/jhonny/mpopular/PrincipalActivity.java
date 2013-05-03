package com.jhonny.mpopular;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class PrincipalActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		
		boolean existen = FileUtil.cargaDatosPersonales(this);
		
		if(!existen){
			Intent intent = new Intent(this, NuevoActivity.class);
			startActivity(intent);
		}else{
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
}
