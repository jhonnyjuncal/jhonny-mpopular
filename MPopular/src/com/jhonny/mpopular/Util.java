package com.jhonny.mpopular;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
	private static HashMap<Integer, Red> redes;
	private static ArrayList<String> listaRedes = new ArrayList<String>();
	private static HashMap<Integer, Cuenta> cuentas;
	
	
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
	
	public static HashMap<Integer, Red> getRedes(){
		return redes;
	}
	
	public static void setRedes(HashMap<Integer, Red> redes){
		Util.redes = redes;
	}
	
	public static ArrayList<String> getListaRedes(){
		return listaRedes;
	}
	
	public static void setListaRedes(ArrayList<String> listaRedes){
		Util.listaRedes = listaRedes;
	}
	
	public static HashMap<Integer, Cuenta> getCuentas(){
		return cuentas;
	}
	
	public static void setCuentas(HashMap<Integer, Cuenta> cuentas){
		Util.cuentas = cuentas;
	}
	
	
	public static void cargaRedesSociales(){
		JSONArray jArray = null;
		try{
			String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=1";
			jArray = Util.consultaDatosInternet(url);
			
			if(jArray != null){
				int pos = 1;
				redes = new HashMap<Integer, Red>();
				
				for(int i=0; i<jArray.length(); i++){
					JSONObject obj = (JSONObject)jArray.get(i);
					Integer idRed = (Integer)obj.get("idRed");
					String cadena = (String)obj.get("nombreRed");
					
					while(cadena.contains("."))
						cadena = cadena.replace('.', ' ');
					
					Red red = new Red(idRed, cadena);
					
					redes.put(pos, red);
					listaRedes.add(cadena);
					pos++;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void cargaMisCuentas(){
		JSONArray jArray = null;
		try{
			String url = "http://jhonnyapps-mpopular.rhcloud.com/index.jsp?consulta=4&idUsuario=" + Util.getIdUsuario();
			jArray = Util.consultaDatosInternet(url);
			
			if(jArray != null){
				int pos = 1;
				
				for(int i=0; i<jArray.length(); i++){
					JSONObject obj = (JSONObject)jArray.get(i);
					
					Integer id = (Integer)obj.get("idCuenta");
					Integer idRed = (Integer)obj.get("idRed");
					String nombreCuenta = (String)obj.get("nombreCuenta");
					while(nombreCuenta.contains("."))
						nombreCuenta = nombreCuenta.replace('.', ' ');
					String nombreUsuario = (String)obj.get("nombreUsuario");
					while(nombreUsuario.contains("."))
						nombreUsuario = nombreUsuario.replace('.', ' ');
					
					Cuenta cuenta = new Cuenta(id, idRed, nombreUsuario, nombreCuenta);
					
					cuentas.put(pos, cuenta);
					pos++;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
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
