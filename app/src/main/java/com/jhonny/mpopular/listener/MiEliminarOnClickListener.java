package com.jhonny.mpopular.listener;

import org.json.JSONArray;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jhonny.mpopular.activity.MisRedesActivity;
import com.jhonny.mpopular.R;
import com.jhonny.mpopular.util.Util;
import com.jhonny.mpopular.adapter.DetalleRedes;


public class MiEliminarOnClickListener implements OnClickListener{
	
	private int posicion;
	private Context context;
	private DetalleRedes dr;
	private ProgressDialog pd = null;
	private JSONArray jArray = null;
	private View view;
	
	
	public MiEliminarOnClickListener(int posicion, Context context){
		this.posicion = posicion;
		this.context = context;
	}
	
	
	@Override
	public void onClick(View view) {
		try{
			this.view = view;
			
			RelativeLayout rl = (RelativeLayout)view.getParent();
			ListView listView = (ListView)rl.getParent();
			dr = new DetalleRedes();
			dr = (DetalleRedes)listView.getItemAtPosition(posicion);
			dr.setImagenEliminar((ImageView)view.findViewById(R.id.imgeliminar));
			
			int imageResource2 = view.getContext().getApplicationContext().getResources().getIdentifier(
					"ic_eliminar2", "drawable", view.getContext().getApplicationContext().getPackageName());
			Drawable image2 = view.getContext().getResources().getDrawable(imageResource2);
			dr.getImagenEliminar().setImageDrawable(image2);
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setMessage(context.getResources().getString(R.string.txt_pregunta_borrar));
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton(R.string.txt_aceptar, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						
						try{
							// dialogo de progreso
							pd = new ProgressDialog(context);
							pd.setMessage(context.getResources().getString(R.string.txt_borrando));
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
							devuelveColorFondoBotonEdicion();
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
	
	
	private void devuelveColorFondoBotonEdicion(){
		try{
			int imageResource2 = view.getContext().getApplicationContext().getResources().getIdentifier(
					"ic_eliminar", "drawable", view.getContext().getApplicationContext().getPackageName());
			Drawable image2 = view.getContext().getResources().getDrawable(imageResource2);
			dr.getImagenEliminar().setImageDrawable(image2);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	private void eliminaRedUsuario(){
		try{
			String url = "http://jhonnyspring-mpopular.rhcloud.com/index.jsp?consulta=6&idCuenta=" + 
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
					Toast.makeText(context, context.getResources().getString(R.string.txt_borrado_ok)
							, Toast.LENGTH_SHORT).show();
				}else{
					// se produjo un error al borrar
					Toast.makeText(context, context.getResources().getString(R.string.txt_borrado_error)
							, Toast.LENGTH_SHORT).show();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(context, context.getResources().getString(R.string.txt_borrado_cancelado)
					, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			try{
				// actualizacion de la lista de cuentas del usuario
				Util.cargaMisCuentas(context);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		@Override
		protected void onPreExecute() {
			devuelveColorFondoBotonEdicion();
		}
	}
}
