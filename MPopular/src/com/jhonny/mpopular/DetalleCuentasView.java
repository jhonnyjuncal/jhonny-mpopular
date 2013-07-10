package com.jhonny.mpopular;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DetalleCuentasView extends LinearLayout {
	
	private TextView usuarioNombre;
	private TextView redNombre;
	private TextView usuarioEmail;
	private TextView cuentaNombre;
	private TextView usuarioPais;
	private TextView usuarioTelefono;
	
	
	public DetalleCuentasView(Context context) {
		super(context);
		inflate(context, R.layout.listview_resultados1, this);
		
		try{
			usuarioNombre = (TextView)findViewById(R.id.usuarionombre);
			redNombre = (TextView)findViewById(R.id.rednombre);
			cuentaNombre = (TextView)findViewById(R.id.cuentanombre);
			usuarioPais = (TextView)findViewById(R.id.usuariopais);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * Este método nos permitirá asignar los valores a los diferentes
	 * componéntes gráficos según el objeto que queramos ver.
	 * @param detalleCuenta
	 */
	public void setDetalleCuentas(DetalleCuentas detalleCuenta){
		try{
			if(usuarioNombre != null)
				usuarioNombre.setText(detalleCuenta.getUsuario_nombre());
			if(redNombre != null)
				redNombre.setText(detalleCuenta.getRed_nombre());
			if(usuarioEmail != null)
				usuarioEmail.setText(detalleCuenta.getUsuario_email());
			if(cuentaNombre != null)
				cuentaNombre.setText(detalleCuenta.getCuenta_nombre());
			if(usuarioPais != null)
				usuarioPais.setText(detalleCuenta.getUsuario_pais());
			if(usuarioTelefono != null)
				usuarioTelefono.setText(detalleCuenta.getUsuario_telefono());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
