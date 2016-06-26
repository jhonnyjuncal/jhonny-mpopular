package com.jhonny.mpopular.domain;

import java.io.Serializable;


public class Red implements Serializable{
	
	private static final long serialVersionUID = 110491857790563312L;
	Integer idRed;
	String nombreRed;
	
	
	public Red(){
	}
	
	public Red(Integer idRed, String nombreRed){
		this.idRed = idRed;
		this.nombreRed = nombreRed;
	}
	
	
	public void setIdRed(Integer idRed){
		this.idRed = idRed;
	}
	
	public Integer getIdRed(){
		return idRed;
	}
	
	public void setNombreRed(String nombreRed){
		this.nombreRed = nombreRed;
	}
	
	public String getNombreRed(){
		return nombreRed;
	}
}
