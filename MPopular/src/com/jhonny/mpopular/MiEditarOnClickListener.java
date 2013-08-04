package com.jhonny.mpopular;

import org.json.JSONArray;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
	private View view;
	
	
	public MiEditarOnClickListener(int posicion, Context context){
		this.posicionMisRedes = posicion;
		this.context = context;
	}
	
	
	@Override
	public void onClick(View view) {
		try{
			this.view = view;
			
			// al pulsar el boton editar de la lista de redes
			RelativeLayout rl = (RelativeLayout)view.getParent();
			ListView lv = (ListView)rl.getParent();
			drViejo = new DetalleRedes();
			drViejo = (DetalleRedes)lv.getItemAtPosition(posicionMisRedes);
			drViejo.setImagenEditar((ImageView)view.findViewById(R.id.imgeditar));
			
			int imageResource2 = view.getContext().getApplicationContext().getResources().getIdentifier(
					"ic_editar2", "drawable", view.getContext().getApplicationContext().getPackageName());
			Drawable image2 = view.getContext().getResources().getDrawable(imageResource2);
			drViejo.getImagenEditar().setImageDrawable(image2);
			
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
								Toast.makeText(context, context.getResources().getString(R.string.txt_nombre_no_vacio)
										, Toast.LENGTH_SHORT).show();
							}else if(et.length() < 3){
								Toast.makeText(context, context.getResources().getString(R.string.txt_nombre_menos3)
										, Toast.LENGTH_SHORT).show();
							}else{
								RelativeLayout rl = (RelativeLayout)view.getParent();
								EditText dialogCuenta = (EditText)rl.findViewById(R.id.diag_nombre_cuenta);
								nCuenta = dialogCuenta.getText().toString();
								
								if(Util.compruebaExistenciaRed(nCuenta, posicionSpinnerSeleccionada + 1, posicionMisRedes)){
									// boton aceptar de la ventana emergente
									pd = new ProgressDialog(context);
									pd.setMessage(context.getResources().getString(R.string.txt_guardando));
									pd.setCancelable(false);
									pd.setIndeterminate(true);
									pd.show();
									
									
									EdicionAsincrona ea = new EdicionAsincrona();
									ea.execute();
									
								}else{
									// La red ya existe
									Toast.makeText(context, context.getResources().getString(R.string.txt_cuenta_duplicada)
											, Toast.LENGTH_SHORT).show();
								}
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
						devuelveColorFondoBotonEdicion();
					}
				}
			);
			dialog.show();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private void devuelveColorFondoBotonEdicion(){
		try{
			int imageResource2 = view.getContext().getApplicationContext().getResources().getIdentifier(
					"ic_editar", "drawable", view.getContext().getApplicationContext().getPackageName());
			Drawable image2 = view.getContext().getResources().getDrawable(imageResource2);
			drViejo.getImagenEditar().setImageDrawable(image2);
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
				if(pd != null)
					pd.dismiss();
				devuelveColorFondoBotonEdicion();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			try{
				if(dialog != null)
					dialog.dismiss();
				if(jArray != null && jArray.getInt(0) == 1){
					// si la actualizacion en bbdd fue correcta
					Util.cargaMisCuentas(context);
					MisRedesActivity.drAdapter.remove(drViejo);
					MisRedesActivity.drAdapter.add(drNuevo);
					MisRedesActivity.drAdapter.notifyDataSetChanged();
					Toast.makeText(context, context.getResources().getString(R.string.txt_guardado_ok)
							, Toast.LENGTH_SHORT).show();
				}else{
					// se produjo un error al modificar los datos
					Toast.makeText(context, context.getResources().getString(R.string.txt_guardado_error)
							, Toast.LENGTH_SHORT).show();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, context.getResources().getString(R.string.txt_guardado_cancelado)
					, Toast.LENGTH_SHORT).show();
		}
	}
}
