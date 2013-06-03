package com.jhonny.mpopular;

import java.io.Serializable;


public class DetalleCuentas implements Serializable{
	
	private static final long serialVersionUID = 6947503050512248711L;
	private String usuario_nombre;
	private String red_nombre;
	private String usuario_email;
	private String cuenta_nombre;
	private String usuario_pais;
	private String usuario_telefono;
	
	
	public DetalleCuentas(){
		super();
	}
	
	public DetalleCuentas(String usuario_nombre, String red_nombre,
				String usuario_email, String cuenta_nombre, 
				String usuario_pais, String usuario_telefono){
		super();
		this.usuario_nombre = usuario_nombre;
		this.red_nombre = red_nombre;
		this.usuario_email = usuario_email;
		this.cuenta_nombre = cuenta_nombre;
		this.usuario_pais = usuario_pais;
		this.usuario_telefono = usuario_telefono;
	}
	
	
	public String getUsuario_nombre() {
		return usuario_nombre;
	}
	
	
	public void setUsuario_nombre(String usuario_nombre) {
		this.usuario_nombre = usuario_nombre;
	}
	
	
	public String getRed_nombre() {
		return red_nombre;
	}
	
	
	public void setRed_nombre(String red_nombre) {
		this.red_nombre = red_nombre;
	}
	
	
	public String getUsuario_email() {
		return usuario_email;
	}
	
	
	public void setUsuario_email(String usuario_email) {
		this.usuario_email = usuario_email;
	}
	
	
	public String getCuenta_nombre() {
		return cuenta_nombre;
	}
	
	
	public void setCuenta_nombre(String cuenta_nombre) {
		this.cuenta_nombre = cuenta_nombre;
	}
	
	
	public String getUsuario_pais() {
		return usuario_pais;
	}
	
	
	public void setUsuario_pais(String usuario_pais) {
		this.usuario_pais = usuario_pais;
	}
	
	
	public String getUsuario_telefono() {
		return usuario_telefono;
	}
	
	
	public void setUsuario_telefono(String usuario_telefono) {
		this.usuario_telefono = usuario_telefono;
	}
}
