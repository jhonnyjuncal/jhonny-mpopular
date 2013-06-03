package com.jhonny.mpopular;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class DetalleRedesAdapter extends ArrayAdapter<DetalleRedes> {
	
	private ArrayList<DetalleRedes> misRedes = new ArrayList<DetalleRedes>();
	private Context context;
	private int layoutResourceId;
	
	
	public DetalleRedesAdapter(Context context, int layoutResourceId, ArrayList<DetalleRedes> misRedes) {
		super(context, layoutResourceId, misRedes);
		this.misRedes = misRedes;
		this.context = context;
		this.layoutResourceId = layoutResourceId;
	}
	
	
	private class ViewHolder{
		TextView descripcion;
		ImageView img_editar;
		ImageView img_eliminar;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		View vi = convertView;
		
		try{
		
			if(convertView == null){
				// NO existe, creamos uno
				LayoutInflater inflater = ((MisRedesActivity)context).getLayoutInflater();
				vi = inflater.inflate(layoutResourceId, parent, false);
				holder = new ViewHolder();
				holder.descripcion = (TextView)vi.findViewById(R.id.texto);
				holder.img_editar = (ImageView)vi.findViewById(R.id.imgeditar);
				holder.img_eliminar = (ImageView)vi.findViewById(R.id.imgeliminar);
				vi.setTag(holder);
			}else{
				// Existe, reutilizamos
				holder = (ViewHolder)vi.getTag();
			}
			
			DetalleRedes dr = misRedes.get(position);
			
			if(vi != null){
				// imagen editar
				holder.descripcion.setText(dr.getNombreCuenta() + " - " + dr.getNombreUsuario());
				int imageResource1 = vi.getContext().getApplicationContext().getResources().getIdentifier(
						"edit", "drawable", vi.getContext().getApplicationContext().getPackageName());
				Drawable image1 = vi.getContext().getResources().getDrawable(imageResource1);
				holder.img_editar.setImageDrawable(image1);
				holder.img_editar.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view) {
						try{
							final Dialog dialog = new Dialog(context);
							dialog.setContentView(R.layout.dialog_editar_cuenta);
							Button dialogButtonAccept = (Button)dialog.findViewById(R.id.button1);
							Button dialogButtonCancel = (Button)dialog.findViewById(R.id.button2);
							dialogButtonAccept.setOnClickListener(
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										dialog.cancel();
									}
								}
							);
							dialogButtonCancel.setOnClickListener(
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										dialog.cancel();
									}
								}
							);
							
							dialog.show();
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				});
			
				// imagen eliminar
				int imageResource2 = vi.getContext().getApplicationContext().getResources().getIdentifier(
						"bt_remove", "drawable", vi.getContext().getApplicationContext().getPackageName());
				Drawable image2 = vi.getContext().getResources().getDrawable(imageResource2);
				holder.img_eliminar.setImageDrawable(image2);
				holder.img_eliminar.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view) {
						try{
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setMessage("¿Desea borrar la cuenta?");
							alertDialogBuilder.setCancelable(false);
							alertDialogBuilder.setPositiveButton(R.string.txt_aceptar, 
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int id) {
										try{
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
				});
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return convertView;
	}
}
