package com.jhonny.mpopular;

import java.io.Serializable;
import android.widget.ImageView;


public class DetalleRedes implements Serializable{
	
	private static final long serialVersionUID = -952135541243638683L;
	
	private Integer idCuenta;
	private Integer idRed;
	private String nombreCuenta;
	private String nombreUsuario;
	private ImageView imagenEditar;
	private ImageView imagenEliminar;
	
	
	public DetalleRedes(){
		
	}
	
	public DetalleRedes(Integer idCuenta, Integer idRed, String nombreCuenta, String nombreUsuario, 
					ImageView imagenEditar, ImageView imagenEliminar){
		this.idCuenta = idCuenta;
		this.idRed = idRed;
		this.nombreCuenta = nombreCuenta;
		this.nombreUsuario = nombreUsuario;
		this.imagenEditar = imagenEditar;
		this.imagenEliminar = imagenEliminar;
	}
	
	
	public Integer getIdCuenta(){
		return idCuenta;
	}
	
	public void setIdCuenta(Integer idCuenta){
		this.idCuenta = idCuenta;
	}
	
	public Integer getIdRed(){
		return idRed;
	}
	
	public void setIdRed(Integer idRed){
		this.idRed = idRed;
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
