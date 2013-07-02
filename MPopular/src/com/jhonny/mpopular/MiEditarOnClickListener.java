package com.jhonny.mpopular;

import org.json.JSONArray;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
	private DetalleRedes dr;
	private ProgressDialog pd = null;
	
	
	public MiEditarOnClickListener(int posicion, Context context){
		this.posicionMisRedes = posicion;
		this.context = context;
	}
	
	
	@Override
	public void onClick(View view) {
		try{
			// al pulsar el boton editar de la lista de redes
			RelativeLayout rl = (RelativeLayout)view.getParent();
			ListView lv = (ListView)rl.getParent();
			dr = new DetalleRedes();
			dr = (DetalleRedes)lv.getItemAtPosition(posicionMisRedes);
			
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.dialog_editar_cuenta);
			View vista = dialog.findViewById(R.id.relativedialog);
			
			// carga de las redes sociales
			cargaRedesSocialesEnDialog(vista, dr.getNombreCuenta());
			
			Button dialogButtonAccept = (Button)dialog.findViewById(R.id.button1);
			Button dialogButtonCancel = (Button)dialog.findViewById(R.id.button2);
			EditText et = (EditText)dialog.findViewById(R.id.diag_nombre_cuenta);
			et.setText(dr.getNombreUsuario());
			
			dialogButtonAccept.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						// boton aceptar de la ventana emergente
						try{
							// dialogo de progreso
							pd = new ProgressDialog(context);
							pd.setMessage("Modificando datos...");
							pd.setCancelable(false);
							pd.setIndeterminate(true);
							pd.show();
							
							Red red_nueva = Util.getRedes().get(posicionSpinnerSeleccionada + 1);
							
							RelativeLayout rl = (RelativeLayout)view.getParent();
							EditText dialogCuenta = (EditText)rl.findViewById(R.id.diag_nombre_cuenta);
							String nCuenta = dialogCuenta.getText().toString();
							
							while(nCuenta.contains(" "))
								nCuenta = nCuenta.replace(' ', '.');
							
							String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=7&idCuenta=" +
									dr.getIdCuenta() + "&idRed=" + red_nueva.getIdRed() + "&idUsuario=" + 
									Util.getIdUsuario() + "&nombre=" + nCuenta;
							JSONArray jArray = Util.consultaDatosInternet(url);
						
							if(jArray != null && jArray.getInt(0) == 1){
								// si la actualizacion en bbdd fue correcta
								Util.cargaMisCuentas();
								
								Toast.makeText(context, "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
							}else{
								// se produjo un error al modificar los datos
								Toast.makeText(context, "Error al modificar los datos", Toast.LENGTH_SHORT).show();
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}finally{
							if(pd != null)
								pd.dismiss();
						}
						
						dialog.cancel();
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
	
	
	private void cargaRedesSocialesEnDialog(View view, String nombre_red){
		Integer posicionMostrar = 0;
		
		try{
			if(Util.getRedes() == null)
				Util.cargaRedesSociales();
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, 
						Util.getListaRedes());
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
}
