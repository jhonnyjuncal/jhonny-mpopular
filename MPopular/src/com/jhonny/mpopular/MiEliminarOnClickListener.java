package com.jhonny.mpopular;

import org.json.JSONArray;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
	private JSONArray jArray = null;
	private ListView listView = null;
	
	
	public MiEliminarOnClickListener(int posicion, Context context){
		this.posicion = posicion;
		this.context = context;
	}
	
	
	@Override
	public void onClick(View view) {
		try{
			RelativeLayout rl = (RelativeLayout)view.getParent();
			listView = (ListView)rl.getParent();
			dr = new DetalleRedes();
			dr = (DetalleRedes)listView.getItemAtPosition(posicion);
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setMessage("¿Desea borrar la cuenta?");
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton(R.string.txt_aceptar, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						
						try{
							// dialogo de progreso
							pd = new ProgressDialog(context);
							pd.setMessage("Eliminando datos...");
							pd.setCancelable(false);
							pd.setIndeterminate(true);
							pd.show();
							
							EliminarAsincrono ea = new EliminarAsincrono();
							ea.execute();
							
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
	
	
	private void eliminaRedUsuario(){
		try{
			String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=6&idCuenta=" + 
			dr.getIdCuenta() + "&idRed=" + dr.getIdRed() + "&idUsuario=" + Util.getIdUsuario();
			
			jArray = Util.consultaDatosInternet(url);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	private class EliminarAsincrono extends AsyncTask<Void, Integer, Boolean>{
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			try{
				eliminaRedUsuario();
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
				
				// se actualiza el adaptador de la lista de las cuentas del usuario
				MisRedesActivity.drAdapter.remove(dr);
				MisRedesActivity.drAdapter.notifyDataSetChanged();
				
				if(jArray != null && jArray.getInt(0) == 1){
					Toast.makeText(context, "Datos eliminados correctamente", Toast.LENGTH_SHORT).show();
				}else{
					// se produjo un error al borrar
					Toast.makeText(context, "Se ha producido un error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, "Eliminacion cancelada...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			try{
				// actualizacion de la lista de cuentas del usuario
				Util.cargaMisCuentas();
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
