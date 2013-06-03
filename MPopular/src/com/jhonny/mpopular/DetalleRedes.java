package com.jhonny.mpopular;

import java.io.Serializable;
import android.widget.ImageView;


public class DetalleRedes implements Serializable{
	
	private static final long serialVersionUID = -952135541243638683L;
	private String nombreCuenta;
	private String nombreUsuario;
	private ImageView imagenEditar;
	private ImageView imagenEliminar;
	
	
	public DetalleRedes(){
		
	}
	
	public DetalleRedes(String nombreCuenta, String nombreUsuario, ImageView imagenEditar, ImageView imagenEliminar){
		this.nombreCuenta = nombreCuenta;
		this.nombreUsuario = nombreUsuario;
		this.imagenEditar = imagenEditar;
		this.imagenEliminar = imagenEliminar;
	}
	
	
	public String getNombreCuenta() {
		return nombreCuenta;
	}
	
	public void setNombreCuenta(String nombreCuenta) {
		this.nombreCuenta = nombreCuenta;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
	public ImageView getImagenEditar() {
		return imagenEditar;
	}
	
	public void setImagenEditar(ImageView imagenEditar) {
		this.imagenEditar = imagenEditar;
	}
	
	public ImageView getImagenEliminar() {
		return imagenEliminar;
	}
	
	public void setImagenEliminar(ImageView imagenEliminar) {
		this.imagenEliminar = imagenEliminar;
	}
}
