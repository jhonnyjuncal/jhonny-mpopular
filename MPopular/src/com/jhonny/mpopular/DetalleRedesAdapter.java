package com.jhonny.mpopular;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		TextView nombreCuenta;
		TextView nombreUsuario;
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
				holder.nombreCuenta = (TextView)vi.findViewById(R.id.nCuenta);
				holder.nombreUsuario = (TextView)vi.findViewById(R.id.nUsuario);
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
				holder.nombreCuenta.setText(dr.getNombreCuenta());
				holder.nombreUsuario.setText(dr.getNombreUsuario());
				int imageResource1 = vi.getContext().getApplicationContext().getResources().getIdentifier(
						"ic_editar", "drawable", vi.getContext().getApplicationContext().getPackageName());
				Drawable image1 = vi.getContext().getResources().getDrawable(imageResource1);
				holder.img_editar.setImageDrawable(image1);
				holder.img_editar.setOnClickListener(null);
				holder.img_editar.setOnClickListener(new MiEditarOnClickListener(position, context));
				
				// imagen eliminar
				int imageResource2 = vi.getContext().getApplicationContext().getResources().getIdentifier(
						"ic_eliminar", "drawable", vi.getContext().getApplicationContext().getPackageName());
				Drawable image2 = vi.getContext().getResources().getDrawable(imageResource2);
				holder.img_eliminar.setImageDrawable(image2);
				holder.img_eliminar.setOnClickListener(null);
				holder.img_eliminar.setOnClickListener(new MiEliminarOnClickListener(position, context));
			}
			convertView = vi;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return convertView;
	}
}
