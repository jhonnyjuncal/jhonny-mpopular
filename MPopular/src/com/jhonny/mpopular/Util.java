package com.jhonny.mpopular;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


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
	
	
	public static JSONArray consultaDatosInternet(String url){
		InputStream is = null;
		JSONArray jArray = null;
		JSONObject jObject = null;
		String result = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		StringBuilder sb = null;
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			
			// Conversion de la repsueta en Sring
			BufferedReader reader = new BufferedReader(new InputStreamReader(is),8);
			sb = new StringBuilder();
			sb.append(reader.readLine());
			
			is.close();
			result = sb.toString();
			
			if(result != null && !result.isEmpty()){
				if(result.contains("<!DOCTYPE")){
					result = result.replace(result.substring(result.indexOf("<!DOCTYPE"), result.length()),"");
					
					// Lectura de los datos de respuesta
					jObject = new JSONObject(result);
					if(jObject.has("valores"))
						jArray = new JSONArray(jObject.get("valores").toString());
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return jArray;
	}
}
