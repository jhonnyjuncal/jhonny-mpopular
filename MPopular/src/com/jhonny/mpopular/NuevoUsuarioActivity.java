package com.jhonny.mpopular;

import org.json.JSONArray;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;


public class NuevoUsuarioActivity extends SherlockActivity {
	
	private ProgressDialog pd = null;
	private ActionBar actionBar;
	private int contador = 0;
	private EditText editNombre;
	private EditText editTelefono;
	private EditText editEmail;
	private EditText editPais;
	private Context context;
	
	//Constants for tablet sized ads (728x90)
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int MED_BANNER_WIDTH = 480;
	//Constants for phone sized ads (320x50)
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo);
		
		try{
			contador = 0;
			this.context = (Context)this;
			
			actionBar = getSupportActionBar();
	        if(actionBar != null){
	        	actionBar.setDisplayShowCustomEnabled(true);
	        	// boton < de la action bar
	        	actionBar.setDisplayHomeAsUpEnabled(false);
	        	actionBar.setHomeButtonEnabled(true);
	        }
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
    protected void onResume(){
		super.onResume();
		
		try{
			contador = 0;
			this.context = (Context)this;
			
			estableceFuenteRoboto();
			cargaPublicidad();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void estableceFuenteRoboto(){
		TextView textView = (TextView)findViewById(R.id.nue_textView3);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.nue_textView4);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.nue_textView5);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.nue_textView6);
		textView.setTypeface(Util.getRoboto7(this));
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
			try{
				// comprobacion de datos obligatorios
				editNombre = (EditText)findViewById(R.id.nue_editText1);
				editTelefono = (EditText)findViewById(R.id.nue_editText2);
				editEmail = (EditText)findViewById(R.id.nue_editText3);
				editPais = (EditText)findViewById(R.id.nue_editText4);
				
				String nombre = editNombre.getText().toString().trim();
				String telefono = editTelefono.getText().toString().trim();
				String email = editEmail.getText().toString().trim();
				String pais = editPais.getText().toString().trim();
				
				if(nombre == null || nombre.length() == 0)
					Toast.makeText(context, getResources().getString(R.string.txt_nombre_no_vacio)
							, Toast.LENGTH_SHORT).show();
				else if(nombre.length() < 3)
					Toast.makeText(context, getResources().getString(R.string.txt_nombre_menos3)
							, Toast.LENGTH_SHORT).show();
				else if(telefono == null || telefono.length() == 0)
					Toast.makeText(context, getResources().getString(R.string.txt_telefono_no_vacio)
							, Toast.LENGTH_SHORT).show();
				else if(telefono.length() < 3)
					Toast.makeText(context, getResources().getString(R.string.txt_telefono_menos3)
							, Toast.LENGTH_SHORT).show();
				else if(email == null || email.length() == 0)
					Toast.makeText(context, getResources().getString(R.string.txt_email_no_vacio)
							, Toast.LENGTH_SHORT).show();
				else if(email.length() < 3)
					Toast.makeText(context, getResources().getString(R.string.txt_email_menos3)
							, Toast.LENGTH_SHORT).show();
				else if(!email.contains("@") || !email.contains("."))
					Toast.makeText(context, getResources().getString(R.string.txt_email_incorrecto)
							, Toast.LENGTH_SHORT).show();
				else if(pais == null || pais.length() == 0)
					Toast.makeText(context, getResources().getString(R.string.txt_pais_no_vacio)
							, Toast.LENGTH_SHORT).show();
				else if(pais.length() < 3)
					Toast.makeText(context, getResources().getString(R.string.txt_pais_menos3)
							, Toast.LENGTH_SHORT).show();
				else{
					// oculta el teclado
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(editPais.getWindowToken(), 0);
					
					// dialogo de progreso
					pd = new ProgressDialog(this);
					pd.setMessage(getResources().getString(R.string.txt_guardando));
					pd.setCancelable(false);
					pd.setIndeterminate(true);
					pd.show();
					
					NuevoUsuarioAsincrono nua = new NuevoUsuarioAsincrono();
					nua.execute();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return true;
	}
	
	private void guardaDatosPersonales(){
		JSONArray jArray = null;
		
		try{
			// se recogen los datos introducidos
			editNombre = (EditText)findViewById(R.id.nue_editText1);
			editTelefono = (EditText)findViewById(R.id.nue_editText2);
			editEmail = (EditText)findViewById(R.id.nue_editText3);
			editPais = (EditText)findViewById(R.id.nue_editText4);
			
			String n1 = editNombre.getText().toString().replaceAll(" ", ".");
			String n2 = editTelefono.getText().toString();
			String n3 = editEmail.getText().toString();
			String n4 = editPais.getText().toString().replaceAll(" ", ".");
			
			String url = "http://jhonnyspring-mpopular.rhcloud.com/index.jsp?consulta=0&nombre="; 
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
				
			}else{
				Toast.makeText(this, getResources().getString(R.string.txt_error_servidor)
						, Toast.LENGTH_SHORT).show();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		if(contador == 0){
    			contador++;
    			Toast.makeText(this, getResources().getString(R.string.txt_antes_de_salir), Toast.LENGTH_SHORT).show();
    			return true;
    		}else{
    			contador = 0;
    			Intent intent = new Intent();
    			intent.setAction(Intent.ACTION_MAIN);
    			intent.addCategory(Intent.CATEGORY_HOME);
    			startActivity(intent);
    		}
    	}
    	//para las demas cosas, se reenvia el evento al listener habitual
    	return super.onKeyDown(keyCode, event);
    }
	
	private void iniciaActividadIncial(){
		Intent intent = new Intent(this, PrincipalActivity.class);
		startActivity(intent);
	}
	
	private void cargaPublicidad(){
		int placementWidth = BANNER_AD_WIDTH;
		
		//Finds an ad that best fits a users device.
		if(canFit(IAB_LEADERBOARD_WIDTH)) {
		    placementWidth = IAB_LEADERBOARD_WIDTH;
		}else if(canFit(MED_BANNER_WIDTH)) {
		    placementWidth = MED_BANNER_WIDTH;
		}
		
		MMAdView adView = new MMAdView(this);
		adView.setApid("154899");
		MMRequest request = new MMRequest();
		adView.setMMRequest(request);
		adView.setId(MMSDK.getDefaultAdId());
		adView.setWidth(placementWidth);
		adView.setHeight(BANNER_AD_HEIGHT);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout2);
		layout.removeAllViews();
		layout.addView(adView);
		adView.getAd();
	}
	
	protected boolean canFit(int adWidth) {
		int adWidthPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, adWidth, getResources().getDisplayMetrics());
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		return metrics.widthPixels >= adWidthPx;
	}
	
	
	
	
	
	
	
	
	private class NuevoUsuarioAsincrono extends AsyncTask<Void, Integer, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				guardaDatosPersonales();
			}catch(Exception ex){
				ex.printStackTrace();
				return false;
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			try{
				if(pd != null)
					pd.dismiss();
				Toast.makeText(context, getResources().getString(R.string.txt_primer_guardado_ok)
						, Toast.LENGTH_SHORT).show();
				
				iniciaActividadIncial();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, getResources().getString(R.string.txt_guardado_cancelado)
					, Toast.LENGTH_SHORT).show();
		}
	}
}
