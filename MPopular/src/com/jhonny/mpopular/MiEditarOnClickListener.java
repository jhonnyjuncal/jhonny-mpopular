package com.jhonny.mpopular;

import org.json.JSONArray;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class MiEditarOnClickListener implements OnClickListener{
	
	private Integer posicionMisRedes;
	private Integer posicionSpinnerSeleccionada = 0;
	private Context context;
	private Spinner spRed;
	private DetalleRedes drViejo, drNuevo;
	private ProgressDialog pd = null;
	private Dialog dialog = null;
	private EditText et = null;
	private JSONArray jArray = null;
	private String nCuenta = null;
	
	
	public MiEditarOnClickListener(int posicion, Context context){
		this.posicionMisRedes = posicion;
		this.context = context;
	}
	
	
	@Override
	public void onClick(View view) {
		try{
			//this.view = view;
			
			// al pulsar el boton editar de la lista de redes
			RelativeLayout rl = (RelativeLayout)view.getParent();
			ListView lv = (ListView)rl.getParent();
			drViejo = new DetalleRedes();
			drViejo = (DetalleRedes)lv.getItemAtPosition(posicionMisRedes);
			
			dialog = new Dialog(context);
			dialog.setContentView(R.layout.dialog_editar_cuenta);
			View vista = dialog.findViewById(R.id.relativedialog);
			
			// carga de las redes sociales
			cargaRedesSocialesEnDialog(vista, drViejo.getNombreCuenta());
			
			Button dialogButtonAccept = (Button)dialog.findViewById(R.id.button1);
			Button dialogButtonCancel = (Button)dialog.findViewById(R.id.button2);
			et = (EditText)dialog.findViewById(R.id.diag_nombre_cuenta);
			et.setText(drViejo.getNombreUsuario());
			
			dialogButtonAccept.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						try{
							// validacion de datos
							if(et.getText().length() == 0){
								Toast.makeText(context, "No puede estar vacio", Toast.LENGTH_SHORT).show();
							}else if(et.length() < 3){
								Toast.makeText(context, "Minimo 3 caracteres", Toast.LENGTH_SHORT).show();
							}else{
								// boton aceptar de la ventana emergente
								pd = new ProgressDialog(context);
								pd.setMessage("Guardando datos...");
								pd.setCancelable(false);
								pd.setIndeterminate(true);
								pd.show();
								
								RelativeLayout rl = (RelativeLayout)view.getParent();
								EditText dialogCuenta = (EditText)rl.findViewById(R.id.diag_nombre_cuenta);
								nCuenta = dialogCuenta.getText().toString();
								
								EdicionAsincrona ea = new EdicionAsincrona();
								ea.execute();
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			);
			dialogButtonCancel.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// boton cancelar de la ventana emergente
						dialog.cancel();
					}
				}
			);
			dialog.show();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private void guardaDatosEdicionRedUsuario(){
		try{
			Red red_nueva = Util.getRedes().get(posicionSpinnerSeleccionada + 1);
			
			while(nCuenta.contains(" "))
				nCuenta = nCuenta.replace(' ', '.');
			
			String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=7&idCuenta=" +
					drViejo.getIdCuenta() + "&idRed=" + red_nueva.getIdRed() + "&idUsuario=" + 
					Util.getIdUsuario() + "&nombre=" + nCuenta;
			
			drNuevo = new DetalleRedes(drViejo.getIdCuenta(), red_nueva.getIdRed(), 
					red_nueva.getNombreRed(), nCuenta, null, null);
			
			jArray = Util.consultaDatosInternet(url);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private void cargaRedesSocialesEnDialog(View view, String nombre_red){
		Integer posicionMostrar = 0;
		
		try{
			if(Util.getRedes() == null)
				Util.cargaRedesSociales();
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, 
					android.R.layout.simple_spinner_item, Util.getListaRedes());
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spRed = (Spinner)view.findViewById(R.id.spinner1);
			spRed.setAdapter(dataAdapter);
			
			for(int i=0; i<Util.getListaRedes().size(); i++){
				String red = Util.getListaRedes().get(i);
				if(red.equals(nombre_red)){
					posicionMostrar = Integer.valueOf(i);
					break;
				}
			}
			
			spRed.setSelection(posicionMostrar);
			spRed.setOnItemSelectedListener(new OnItemSelectedListener(){
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
						// al seleccionar una nueva red social
						posicionSpinnerSeleccionada = pos;
					}
					
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				}
			);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	private class EdicionAsincrona extends AsyncTask<Void, Integer, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				guardaDatosEdicionRedUsuario();
				publishProgress(0);
			}catch(Exception ex){
				ex.printStackTrace();
				return false;
			}
			return true;
		}
		
		@Override
		protected void onPreExecute() {
			try{
				if(dialog != null)
					dialog.dismiss();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			try{
				if(pd != null)
					pd.dismiss();
				if(jArray != null && jArray.getInt(0) == 1){
					// si la actualizacion en bbdd fue correcta
					Util.cargaMisCuentas();
					MisRedesActivity.drAdapter.remove(drViejo);
					MisRedesActivity.drAdapter.add(drNuevo);
					MisRedesActivity.drAdapter.notifyDataSetChanged();
					Toast.makeText(context, "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
				}else{
					// se produjo un error al modificar los datos
					Toast.makeText(context, "Error al modificar los datos", Toast.LENGTH_SHORT).show();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, "Actualizacion cancelada...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			try{
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
