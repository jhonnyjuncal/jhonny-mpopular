package com.jhonny.mpopular;

public class Constantes {
	
	/** Genericas */
	public static String FICHERO_CONFIGURACION = "config.pop";
	
	
	/** CONSULTAS SQL */
	public static String SQL_USER = "jhonny";
	public static String SQL_PASS = "14743430";
	public static String CONSULTA_USUARIO = "SELECT * FROM usuarios WHERE idUsuario = ?";
	public static String INSERTAR_USUARIO_NUEVO = "INSERT INTO usuarios values (null, ?, ?, ?, ?, ?)";
	
	
	/** PROPIEDADES */
	public static String PROP_ID_USUARIO = "idUsuario";
	public static String PROP_NOMBRE = "nombre";
	public static String PROP_TELEFONO = "telefono";
	public static String PROP_EMAIL = "email";
	public static String PROP_PAIS = "pais";
}
