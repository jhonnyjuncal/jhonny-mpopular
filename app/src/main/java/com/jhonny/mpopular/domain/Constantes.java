package com.jhonny.mpopular.domain;

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
	
	
	/** FUNTES */
	public static String ROBOTO1 = "fonts/Roboto-Thin.ttf";
	public static String ROBOTO2 = "fonts/Roboto-ThinItalic.ttf";
	public static String ROBOTO3 = "fonts/Roboto-Bold.ttf";
	public static String ROBOTO4 = "fonts/Roboto-Black.ttf";
	public static String ROBOTO5 = "fonts/Roboto-BlackItalic.ttf";
	public static String ROBOTO6 = "fonts/Roboto-Medium.ttf";
	public static String ROBOTO7 = "fonts/Roboto-Regular.ttf";
	public static String ROBOTO8 = "fonts/Roboto-Light.ttf";
	
}
