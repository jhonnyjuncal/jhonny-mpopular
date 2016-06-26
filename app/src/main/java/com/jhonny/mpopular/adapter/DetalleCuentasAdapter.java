package com.jhonny.mpopular.adapter;

import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jhonny.mpopular.domain.DetalleCuentas;


public class DetalleCuentasAdapter extends BaseAdapter {
	
	private ArrayList<DetalleCuentas> detalles;
	
	
	public DetalleCuentasAdapter(ArrayList<DetalleCuentas> detalles){
		this.detalles = detalles;
		//Cada vez que cambiamos los elementos debemos noficarlo
        notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		return detalles.size();
	}
	
	
	@Override
	public Object getItem(int position) {
		return detalles.get(position);
	}
	
	
	@Override
	public long getItemId(int id) {
		return 0;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DetalleCuentasView view;
		
		if(convertView == null){
			// NO existe, creamos uno
			view = new DetalleCuentasView(parent.getContext());
		}else{
			// Existe, reutilizamos
			view = (DetalleCuentasView)convertView;
		}
        view.setDetalleCuentas(detalles.get(position));
        
        return view;
	}
}
