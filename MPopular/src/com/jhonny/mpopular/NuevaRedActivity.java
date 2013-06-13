package com.jhonny.mpopular;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class NuevaRedActivity extends SherlockActivity implements OnItemSelectedListener {
	
	private Integer posicionSpinnerSeleccionada = 0;
	private Spinner spRed;
	private AdView adView = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_nueva_red);
			
			// carga las redes sociales de la bbdd
			cargaRedesSociales();
			
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
		inflater.inflate(R.menu.menu_nueva_red, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.btn_guardar_red){
			insertarCuentaNueva(null);
		}
		return true;
	}
	
	
	public void insertarCuentaNueva(View view){
		ProgressDialog pd = null;
		
		try{
			// dialogo de progreso
			pd = new ProgressDialog(this);
			pd.setMessage("Guardando datos...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			
			EditText editNombre = (EditText)findViewById(R.id.editText1);
			String nombreCuenta = editNombre.getText().toString();
			Integer idUsuario = Util.getIdUsuario();
			
			if(nombreCuenta != null){
				if(nombreCuenta.length() >= 3){
					Red elemento = new Red();
					
					if(posicionSpinnerSeleccionada != null && posicionSpinnerSeleccionada > 0)
						elemento = Util.getRedes().get(posicionSpinnerSeleccionada);
					
					String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=3&nombre=" + 
							nombreCuenta + "&idUsuario=" + idUsuario + "&idRed=" + elemento.getIdRed();
					Util.consultaDatosInternet(url);
					
					if(pd != null)
						pd.dismiss();
					
					Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
					editNombre.setText("");
				}else{
					Toast.makeText(this, "Mas de 3 caracteres para busqueda", Toast.LENGTH_LONG).show();
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		posicionSpinnerSeleccionada = pos;
	}
	
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	
	private void cargaRedesSociales(){
		
		try{
			if(Util.getRedes() == null)
				Util.cargaRedesSociales();
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, 
						Util.getListaRedes());
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spRed = (Spinner)findViewById(R.id.spinner1);
			spRed.setOnItemSelectedListener(this);
			spRed.setAdapter(dataAdapter);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
