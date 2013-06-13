package com.jhonny.mpopular;

import java.io.Serializable;


public class Cuenta implements Serializable{
	
	private static final long serialVersionUID = -7701322461010796328L;
	private Integer idCuenta;
	private Integer idRed;
	private String nombreCuenta;
	private String nombreUsuario;
	
	
	public Cuenta(){
	}
	
	public Cuenta(Integer idCuenta, Integer idRed, String nombreCuenta, String nombreUsuario){
		this.idCuenta = idCuenta;
		this.idRed = idRed;
		this.nombreCuenta = nombreCuenta;
		this.nombreUsuario = nombreUsuario;
	}
	
	
	public Integer getIdCuenta() {
		return idCuenta;
	}
	
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	
	public Integer getIdRed() {
		return idRed;
	}
	
	public void setIdRed(Integer idRed) {
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
}
