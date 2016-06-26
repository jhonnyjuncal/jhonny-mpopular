package com.jhonny.mpopular.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jhonny.mpopular.R;
import com.jhonny.mpopular.adapter.DetalleCuentasAdapter;
import com.jhonny.mpopular.domain.DetalleCuentas;
import com.jhonny.mpopular.util.FileUtil;
import com.jhonny.mpopular.util.Util;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;
import org.json.JSONArray;
import java.util.ArrayList;


public class BuscadorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
		OnItemSelectedListener {
	
	private Spinner spRed;
	private ArrayList<DetalleCuentas> listaResultados = new ArrayList<DetalleCuentas>();
	private Integer posicionSpinnerSeleccionada = 0;
	private ListView listView;
	private TextView rLabel;
	private TextView rTexto;
	private View view;
	private ProgressDialog pd = null;
	private int contador = 0;
	private Context context = null;
	private SearchView searchView = null;
	private String textoBuscar = new String();
	
	//Constants for tablet sized ads (728x90)
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int MED_BANNER_WIDTH = 480;
	//Constants for phone sized ads (320x50)
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_buscador);
		
		try{
			contador = 0;
			context = (Context)this;
			listView = (ListView)findViewById(R.id.listView1);

			// barra de herramientas
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			
			// carga las redes sociales de la bbdd
			cargaRedesSociales();
			
			// borra el contenido de las etiquetas de los resultados de la busqueda
			limpiaResultadosBusqueda();
			
	        searchView = new SearchView(getSupportActionBar().getThemedContext());
		    searchView.setQueryHint(getResources().getString(R.string.label_usuario2));
		    searchView.setIconified(false);
		    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
				@Override
				public boolean onQueryTextSubmit(String query) {
					if(query.length() == 0)
						Toast.makeText(context, getResources().getString(R.string.txt_no_puede_vacio)
								, Toast.LENGTH_SHORT).show();
					else if(query.length() < 3)
						Toast.makeText(context, getResources().getString(R.string.txt_busqueda_minima)
								, Toast.LENGTH_SHORT).show();
					else if(posicionSpinnerSeleccionada == 0)
						Toast.makeText(context, getResources().getString(R.string.txt_selecciona_red)
								, Toast.LENGTH_SHORT).show();
					else{
						textoBuscar = query;

						// dialogo de progreso
						pd = new ProgressDialog(BuscadorActivity.this);
						pd.setMessage(getResources().getString(R.string.txt_buscando));
						pd.setCancelable(false);
						pd.setIndeterminate(true);
						pd.show();

						BuscadorAsincrono ba = new BuscadorAsincrono();
						ba.execute();
						return true;
					}
					return false;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					return false;
				}
			});

			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
			drawer.setDrawerListener(toggle);
			toggle.syncState();

			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			navigationView.setNavigationItemSelectedListener(this);
	        
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//	    menu.add(getResources().getString(R.string.label_buscador))
//	    		.setIcon(R.drawable.abs__ic_search)
//	    		.setActionView(searchView)
//	    		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//	    SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
//	    SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
//	    searchView.setSearchableInfo(info);
//	    return true;
//	}
	
	
	@Override
    protected void onResume(){
		super.onResume();

		try{
			contador = 0;
			context = (Context)this;
			
			reiniciarFondoOpciones();
			reiniciodelaaplicacion();
			estableceFuenteRoboto();
			cargaPublicidad();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private void reiniciodelaaplicacion(){
		if(!FileUtil.cargaDatosPersonales(this)){
			Intent intent = new Intent(this, NuevoUsuarioActivity.class);
			startActivity(intent);
		}
		
		TextView opc_textview1 = (TextView)findViewById(R.id.opc_textView1);
		if(Util.getNombre() == null || Util.getNombre().length() == 0)
			FileUtil.cargaDatosPersonales(context);
		opc_textview1.setText(Util.getNombre());
	}
	
	
	private void estableceFuenteRoboto(){
		TextView textView = (TextView)findViewById(R.id.busq_textView1);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.busq_textView2);
		textView.setTypeface(Util.getRoboto7(this));
		textView = (TextView)findViewById(R.id.busq_textView3);
		textView.setTypeface(Util.getRoboto7(this));
	}
	
	private void cargaRedesSociales(){
		try{
			if(Util.getRedes() == null)
				Util.cargaRedesSociales();
			
			ArrayList<String> lista = new ArrayList<String>();
			lista.add(getResources().getString(R.string.label_seleccionar_red));
			lista.addAll(Util.getListaRedes());
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spRed = (Spinner)findViewById(R.id.spinner_busq);
			spRed.setOnItemSelectedListener(this);
			spRed.setAdapter(dataAdapter);
			
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
	
	private void buscarDatosUsuario(){
		JSONArray jArrayClave = null;
		JSONArray jArrayValor = null;
		
		try{
			listaResultados = new ArrayList<DetalleCuentas>();
			
			if(textoBuscar != null && textoBuscar.length() > 0){
				Integer idRed = 0;
				if(posicionSpinnerSeleccionada != null && posicionSpinnerSeleccionada > 0)
					idRed = Util.getRedes().get(posicionSpinnerSeleccionada).getIdRed();
				
				while(textoBuscar.contains(" "))
					textoBuscar = textoBuscar.replace(' ', '.');
				
				String url = "http://jhonnyspring-mpopular.rhcloud.com/index.jsp?consulta=2" +
						"&nombre=" + textoBuscar + "&idRed=" + idRed;
				
				jArrayClave = Util.consultaDatosInternet(url);
				
				rLabel = (TextView)findViewById(R.id.busq_textView2);
				rTexto = (TextView)findViewById(R.id.busq_textView3);
				
				if(jArrayClave != null){
					// hay datos que mostrar
					if(jArrayClave.length() > 0){
						for(int i=0; i<jArrayClave.length(); i++){
							jArrayValor = (JSONArray)jArrayClave.get(i);
							
							String n1 = (String)jArrayValor.get(0);
							if(n1.contains("."))
								n1 = n1.replace('.', ' ');
							String n2 = (String)jArrayValor.get(1);
							String n3 = (String)jArrayValor.get(2);
							String n4 = (String)jArrayValor.get(3);
							String n5 = (String)jArrayValor.get(4);
							if(n5.contains("."))
								n5 = n5.replace('.', ' ');
							String n6 = (String)jArrayValor.get(5);
							
							DetalleCuentas detalle = new DetalleCuentas(n1, n2, n3, n4, n5, n6);
							listaResultados.add(detalle);
						}
					}
					textoBuscar = new String();
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void limpiaResultadosBusqueda(){
		try{
			rLabel = (TextView)findViewById(R.id.busq_textView2);
			rTexto = (TextView)findViewById(R.id.busq_textView3);
			
			rLabel.setVisibility(View.INVISIBLE); // mostrar invisible
			rTexto.setText("");
			
			if(view != null)
				view.buildDrawingCache(true);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void reiniciarFondoOpciones(){
		try{
			LinearLayout layout_busq = (LinearLayout)findViewById(R.id.opc_layout_busq);
			layout_busq.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_redes = (LinearLayout)findViewById(R.id.opc_layout_redes);
			layout_redes.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_conf = (LinearLayout)findViewById(R.id.opc_layout_conf);
			layout_conf.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_acerca = (LinearLayout)findViewById(R.id.opc_layout_acerca);
			layout_acerca.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_terminos = (LinearLayout)findViewById(R.id.opc_layout_terminos);
			layout_terminos.setBackgroundResource(R.color.gris_claro);
			
			LinearLayout layout_ayuda = (LinearLayout)findViewById(R.id.opc_layout_ayuda);
			layout_ayuda.setBackgroundResource(R.color.gris_claro);
			
			if(view != null)
				view.buildDrawingCache(true);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		if(contador == 0){
    			contador++;
    			Toast.makeText(this, getResources().getString(R.string.txt_salir_1_aviso), Toast.LENGTH_SHORT).show();
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
	
	
	
	
	
	/**
	 * Clase privada asincrona para las busquedas
	 * @author jhonny
	 *
	 */
	private class BuscadorAsincrono extends AsyncTask<Void, Integer, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				buscarDatosUsuario();
				publishProgress(0);
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
				
//				Display display = getWindowManager().getDefaultDisplay();
//			    int width = (display.getWidth());
//			    int height = (display.getHeight());
				
				// cambiar el tamaño del listview segun la cantidad de resultados
//				LinearLayout.LayoutParams paramsListView1 = new LinearLayout.LayoutParams(width, height*2);
//				AbsListView.LayoutParams paramsListView2 = new AbsListView.LayoutParams(width, height*2);
//				RelativeLayout.LayoutParams paramsListView3 = new RelativeLayout.LayoutParams(width, height*2);
//				paramsListView3.setMargins(0, 0, 0, 0);
//				listView.setLayoutParams(paramsListView);
//				relative.setLayoutParams(paramsListView3);
//				linear.setLayoutParams(paramsListView1);
//			    relative.getLayoutParams().height = height*2;
//			    relative.getLayoutParams().width = width;
			    
			    // oculta el teclado
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, getResources().getString(R.string.txt_busq_cancelada)
					, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			try{
				if(listaResultados != null){
					rLabel.setVisibility(View.VISIBLE); //mostrar visible
					String texto = getResources().getString(R.string.label_catidad_resultados);
					rTexto.setText(String.valueOf(listaResultados.size()) + " " + texto);
					listView.setAdapter(new DetalleCuentasAdapter(listaResultados));
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
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

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Intent intent = null;

		if (id == R.id.nav_principal) {
			intent = new Intent(this, PrincipalActivity.class);
		} else if (id == R.id.nav_buscador) {
			intent = new Intent(this, BuscadorActivity.class);
		} else if (id == R.id.nav_mis_redes) {
			intent = new Intent(this, MisRedesActivity.class);
		} else if (id == R.id.nav_manage) {
			intent = new Intent(this, ConfiguracionActivity.class);
//		} else if (id == R.id.nav_share) {
//			intent = new Intent(this, AyudaActivity.class);
//		} else if (id == R.id.nav_send) {
//			intent = new Intent(this, AyudaActivity.class);
		} else if (id == R.id.nav_acerca) {
			intent = new Intent(this, AcercaActivity.class);
		} else if (id == R.id.nav_terminos) {
			intent = new Intent(this, TerminosActivity.class);
		} else if (id == R.id.nav_ayuda) {
			intent = new Intent(this, AyudaActivity.class);
		}
//		intent = new Intent(this, NuevoUsuarioActivity.class);
//		intent = new Intent(this, NuevaRedActivity.class);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);

		startActivity(intent);

		return true;
	}
}
