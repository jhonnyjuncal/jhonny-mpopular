package com.jhonny.mpopular;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MiEliminarOnClickListener implements OnClickListener{
	
	private int posicion;
	private Context context;
	private DetalleRedes dr;
	private ProgressDialog pd = null;
	
	
	public MiEliminarOnClickListener(int posicion, Context context){
		this.posicion = posicion;
		this.context = context;
	}
	
	
	@Override
	public void onClick(View view) {
		try{
			RelativeLayout rl = (RelativeLayout)view.getParent();
			ListView lv = (ListView)rl.getParent();
			dr = new DetalleRedes();
			dr = (DetalleRedes)lv.getItemAtPosition(posicion);
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setMessage("¿Desea borrar la cuenta?");
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton(R.string.txt_aceptar, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						JSONArray jArray = null;
						
						try{
							// dialogo de progreso
							pd = new ProgressDialog(context);
							pd.setMessage("Guardando datos...");
							pd.setCancelable(false);
							pd.setIndeterminate(true);
							pd.show();
							
							String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=4" +
								"&idCuenta=" + dr.getIdCuenta() + "&idRed=" + dr.getIdRed() + 
								"&idUsuario=" + Util.getIdUsuario();
							jArray = Util.consultaDatosInternet(url);
							
							if(jArray.getBoolean(0)){
								// si la actualizacion en bbdd fue correcta
								Util.cargaMisCuentas();
								
								Toast.makeText(context, "Datos eliminados correctamente", Toast.LENGTH_SHORT).show();
							}else{
								// se produjo un error al borrar
								Toast.makeText(context, "Se ha producido un error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
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
			alertDialogBuilder.setNegativeButton(R.string.txt_cancelar, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try{
							dialog.cancel();
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			);
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
