package com.jhonny.mpopular;

import java.io.Serializable;


public class Util implements Serializable{
	
	private static final long serialVersionUID = -1330676313731275220L;
	private static int idUsuario;
	private static String nombre;
	private static String telefono;
	private static String email;
	private static String pais;
	
	
	public static int getIdUsuario() {
		return Util.idUsuario;
	}
	
	public static void setIdUsuario(int idUsuario) {
		Util.idUsuario = idUsuario;
	}
	
	public static String getNombre() {
		return Util.nombre;
	}
	
	public static void setNombre(String nombre) {
		Util.nombre = nombre;
	}
	
	public static String getTelefono() {
		return Util.telefono;
	}
	
	public static void setTelefono(String telefono) {
		Util.telefono = telefono;
	}
	
	public static String getEmail() {
		return Util.email;
	}
	
	public static void setEmail(String email) {
		Util.email = email;
	}
	
	public static String getPais() {
		return Util.pais;
	}
	
	public static void setPais(String pais) {
		Util.pais = pais;
	}
}
