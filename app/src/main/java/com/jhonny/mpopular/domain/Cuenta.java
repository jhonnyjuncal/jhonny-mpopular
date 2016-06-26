package com.jhonny.mpopular.domain;

import java.io.Serializable;


public class Cuenta implements Serializable{
	
	private static final long serialVersionUID = -7701322461010796328L;
	
	private Integer idCuenta;
	private Red red;
	private Usuario usuario;
	private String nombre;
	
	
	public Cuenta(){
		
	}
	
	public Cuenta(Integer idCuenta, Red red, Usuario usuario, String nombre){
		this.idCuenta = idCuenta;
		this.red = red;
		this.usuario = usuario;
		this.nombre = nombre;
	}
	
	
	public Integer getIdCuenta() {
		return idCuenta;
	}
	
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	
	public Red getRed() {
		return red;
	}
	
	public void setRed(Red red) {
		this.red = red;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
