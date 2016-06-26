package com.jhonny.mpopular.domain;

import java.io.Serializable;
import java.util.Date;


public class Usuario implements Serializable {
	
	private static final long serialVersionUID = -9037679944535668903L;
	
	private Integer idUsuario;
	private String nombre;
	private String telefono;
	private String email;
	private String pais;
	private Date fechaAlta;
	
	
	public Usuario(){
		
	}
	
	public Usuario(Integer idUsuario, String nombre, String telefono, String email, String pais, Date fechaAlta){
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.pais = pais;
		this.fechaAlta = fechaAlta;
	}
	
	
	public Integer getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPais() {
		return pais;
	}
	
	public void setPais(String pais) {
		this.pais = pais;
	}
	
	public Date getFechaAlta() {
		return fechaAlta;
	}
	
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
}
