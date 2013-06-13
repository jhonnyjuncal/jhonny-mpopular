package com.jhonny.mpopular;

import android.app.Dialog;
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


public class MiEditarOnClickListener implements OnClickListener{
	
	private int posicion;
	private Context context;
	private Spinner spRed;
	private Integer posicionSpinnerSeleccionada = 0;
	
	
	public MiEditarOnClickListener(int posicion, Context context){
		this.posicion = posicion;
		this.context = context;
	}
	
	
	@Override
	public void onClick(View view) {
		try{
			RelativeLayout rl = (RelativeLayout)view.getParent();
			ListView lv = (ListView)rl.getParent();
			DetalleRedes dr = (DetalleRedes)lv.getItemAtPosition(posicion);
			String nombre_red = dr.getNombreCuenta();
			
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.dialog_editar_cuenta);
			View vista = dialog.findViewById(R.id.relativedialog);
			
			// carga de las redes sociales
			cargaRedesSocialesEnDialog(vista, nombre_red);
			
			Button dialogButtonAccept = (Button)dialog.findViewById(R.id.button1);
			Button dialogButtonCancel = (Button)dialog.findViewById(R.id.button2);
			EditText et = (EditText)dialog.findViewById(R.id.editText1);
			et.setText(dr.getNombreCuenta());
			//nombreCuenta = dr.getNombreCuenta();
			
			dialogButtonAccept.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// boton aceptar de la ventana emergente
						Red elemento = Util.getRedes().get(posicionSpinnerSeleccionada);
						
//						Integer idRed = 0;
//						Set<Integer> listaIds = elemento.keySet();
//						for(Integer i : listaIds){
//							if(i != null)
//								idRed = i;
//						}
						
//						String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=7&idCuenta=" +
//									idCuenta + "&idRed=" + idRed + "&idUsuario=" + Util.getIdUsuario() +
//									"&nombre=" + nombreCuenta;
//						JSONArray jArray = Util.consultaDatosInternet(url);
						
//						if(jArray.getBoolean(0)){
//							// si la actualizacion en bbdd fue correcta
//							Red anterior = Util.getRedes().get(spRed.getSelectedItemPosition());
//							
//						}
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
		int posEditarSpinner = 0;
		
		try{
			if(Util.getRedes() == null)
				Util.cargaRedesSociales();
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, 
						Util.getListaRedes());
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spRed = (Spinner)view.findViewById(R.id.spinner1);
			spRed.setAdapter(dataAdapter);
			spRed.setSelection(posEditarSpinner);
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
