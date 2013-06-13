package com.jhonny.mpopular;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class MiEliminarOnClickListener implements OnClickListener{
	
	private int posicion;
	private Context context;
	
	
	public MiEliminarOnClickListener(int posicion, Context context){
		this.posicion = posicion;
		this.context = context;
	}
	
	
	@Override
	public void onClick(View view) {
		try{
			RelativeLayout rl = (RelativeLayout)view.getParent();
			ListView lv = (ListView)rl.getParent();
			DetalleRedes dr = (DetalleRedes)lv.getItemAtPosition(posicion);
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setMessage("¿Desea borrar la cuenta?");
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton(R.string.txt_aceptar, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						try{
//							String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=4" +
//								"&idCuenta=" +  +
//								"&idRed=" + dr.getIdRed +
//								"&idUsuario=" + Util.getIdUsuario();
//							jArray = Util.consultaDatosInternet(url);
							
							System.out.println("ELIMINAR POSICION ACEPTAR: " + posicion);
							dialog.cancel();
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			);
			alertDialogBuilder.setNegativeButton(R.string.txt_cancelar, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try{
							System.out.println("ELIMINAR POSICION CANCELAR: " + posicion);
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
