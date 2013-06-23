package com.jhonny.mpopular;

import org.json.JSONArray;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PrincipalActivity extends SherlockActivity {
	
	private AdView adView = null;
	private SlidingMenu menu;
	private ActionBar actionBar;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		
		try{
			menu = new SlidingMenu(this);
	        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	        menu.setShadowWidthRes(R.dimen.shadow_width);
	        menu.setShadowDrawable(R.drawable.ext_sombra);
	        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	        menu.setFadeDegree(0.35f);
	        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	        menu.setMenu(R.layout.activity_opciones);
	        
	        
	        actionBar = getSupportActionBar();
	        if(actionBar != null){
	        	actionBar.setDisplayShowCustomEnabled(true);
	        	
	        	// boton < de la action bar
	        	actionBar.setDisplayHomeAsUpEnabled(false);
	        	actionBar.setHomeButtonEnabled(true);
	        	
	        	PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
	        	actionBar.setTitle(pInfo.applicationInfo.name);
//	        	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//	        	actionBar.addTab(actionBar.newTab().setText(R.string.label_buscador).setTabListener(this));
//	        	actionBar.addTab(actionBar.newTab().setText(R.string.label_mis_redes).setTabListener(this));
	        }
	        
	        
			boolean existen = FileUtil.cargaDatosPersonales(this);
			
			if(!existen){
				Intent intent = new Intent(this, NuevoUsuarioActivity.class);
				startActivity(intent);
			}else{
				recuperarDatosUsuario(Util.getIdUsuario());
				muestraDatosPersonales();
			}
			
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
		inflater.inflate(R.menu.menu_principal, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				menu.toggle();
				return true;
				
			case R.id.layout_opciones:
				Intent intent = new Intent(this, AcercaActivity.class);
				startActivity(intent);
				return true;
				
			case R.id.submenu1:
				Toast.makeText(this, "submenu1 clicked", Toast.LENGTH_SHORT).show();
				return true;
				
			case R.id.submenu2:
				Toast.makeText(this, "submenu2 clicked", Toast.LENGTH_SHORT).show();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
        }
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
