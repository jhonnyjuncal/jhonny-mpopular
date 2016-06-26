package com.jhonny.mpopular.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Typeface;
import com.jhonny.mpopular.adapter.DetalleRedes;
import com.jhonny.mpopular.domain.Constantes;
import com.jhonny.mpopular.domain.Cuenta;
import com.jhonny.mpopular.domain.Red;
import com.jhonny.mpopular.domain.Usuario;


public class Util implements Serializable{
	
	private static final long serialVersionUID = -1330676313731275220L;
	private static int idUsuario;
	private static String nombre;
	private static String telefono;
	private static String email;
	private static String pais;
	private static HashMap<Integer, Red> redes;
	private static ArrayList<String> listaRedes = new ArrayList<String>();
	private static ArrayList<DetalleRedes> misRedes = new ArrayList<DetalleRedes>();
	private static Integer posEnEdicion;
	
	
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
	
	public static ArrayList<DetalleRedes> getMisRedes(){
		return misRedes;
	}
	
	public static void setMisRedes(ArrayList<DetalleRedes> misRedes){
		Util.misRedes = misRedes;
	}
	
	public static Integer getPosEnEdicion() {
		return Util.posEnEdicion;
	}
	
	public static void setPosEnEdicion(Integer posEnEdicion) {
		Util.posEnEdicion = posEnEdicion;
	}
	
	public static Typeface getRoboto1(Context context){
		return Typeface.createFromAsset(context.getAssets(), Constantes.ROBOTO1);
	}
	
	public static Typeface getRoboto2(Context context){
		return Typeface.createFromAsset(context.getAssets(), Constantes.ROBOTO2);
	}
	
	public static Typeface getRoboto3(Context context){
		return Typeface.createFromAsset(context.getAssets(), Constantes.ROBOTO3);
	}
	
	public static Typeface getRoboto4(Context context){
		return Typeface.createFromAsset(context.getAssets(), Constantes.ROBOTO4);
	}
	
	public static Typeface getRoboto5(Context context){
		return Typeface.createFromAsset(context.getAssets(), Constantes.ROBOTO5);
	}
	
	public static Typeface getRoboto6(Context context){
		return Typeface.createFromAsset(context.getAssets(), Constantes.ROBOTO6);
	}
	
	public static Typeface getRoboto7(Context context){
		return Typeface.createFromAsset(context.getAssets(), Constantes.ROBOTO7);
	}
	
	public static Typeface getRoboto8(Context context){
		return Typeface.createFromAsset(context.getAssets(), Constantes.ROBOTO8);
	}
	
	
	
	
	public static void cargaRedesSociales(){
		JSONArray jArray = null;
		try{
			String url = "http://jhonnyspring-mpopular.rhcloud.com/index.jsp?consulta=1";
			jArray = Util.consultaDatosInternet(url);
			
			if(jArray != null){
				int pos = 1;
				redes = new HashMap<Integer, Red>();
				
				for(int i=0; i<jArray.length(); i++){
					JSONObject obj = (JSONObject)jArray.get(i);
					Integer idRed = (Integer)obj.get("idRed");
					String cadena = (String)obj.get("nombre");
					
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
	
	
	public static void cargaMisCuentas(Context context){
		JSONArray jArray = null;
		try{
			if(Util.idUsuario <= 0)
				FileUtil.cargaDatosPersonales(context);
			
			String url = "http://jhonnyspring-mpopular.rhcloud.com/index.jsp?consulta=4&idUsuario=" + Util.getIdUsuario();
			jArray = Util.consultaDatosInternet(url);
			misRedes = new ArrayList<DetalleRedes>();
			
			if(jArray != null){
				int pos = 1;
				
				for(int i=0; i<jArray.length(); i++){
					JSONObject obj = (JSONObject)jArray.get(i);
					
					String cuentaNombre = (String)obj.getString("nombre");
					JSONObject obj2 = (JSONObject)obj.get("usuario");
					String usuarioNombre = (String)obj2.getString("nombre");
					while(usuarioNombre.contains("."))
						usuarioNombre = usuarioNombre.replace(".", " ");
					String usuarioTelefono = (String)obj2.getString("telefono");
					Integer usuarioIdUsuario = (Integer)obj2.getInt("idUsuario");
					String usuarioPais = (String)obj2.getString("pais");
					String usuarioEmail = (String)obj2.getString("email");
					Integer cuentaIdCuenta = (Integer)obj.getInt("idCuenta");
					JSONObject obj3 = (JSONObject)obj.get("red");
					Integer redIdRed = (Integer)obj3.getInt("idRed");
					String redNombre = (String)obj3.getString("nombre");
					
					Usuario usuario = new Usuario(usuarioIdUsuario, usuarioNombre, usuarioTelefono, usuarioEmail, usuarioPais, null);
					Red red = new Red(redIdRed, redNombre);
					Cuenta cuenta = new Cuenta(cuentaIdCuenta, red, usuario, cuentaNombre);
					DetalleRedes dr = new DetalleRedes(cuenta.getIdCuenta(), red.getIdRed(), cuenta.getNombre(), usuario.getNombre(), null, null);
					misRedes.add(dr);
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
	
	
	/**
	 * Devuelve la fecha formateada dependiendo de la configuracion del telefono
	 * @param fecha
	 * @param locale
	 * @return fecha formateada
	 */
	public static String getFechaFormateada(Date fecha, Locale locale){
		String resultado = "";
		
		try{
			DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
			resultado = dateFormatter.format(fecha);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultado;
	}
	
	
	/**
	 * Metodo que borra todos los datos de la aplicacion en el dispositivo
	 */
	public static void eliminacionCompletaDatos(Context context){
		try{
			FileUtil.borraFicheroActualDeLaAplicacion(context);
			
			Util.setIdUsuario(0);
			Util.setNombre(null);
			Util.setTelefono(null);
			Util.setEmail(null);
			Util.setPais(null);
			Util.setMisRedes(null);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * Descarga los datos del usuario de la bbdd en la nube
	 * @param idUsuario
	 */
	public static void recuperarDatosUsuario(Integer idUsuario){
		JSONArray jArray = null;
		try{
			String url = "http://jhonnyspring-mpopular.rhcloud.com/index.jsp?consulta=5&idUsuario=" + Util.getIdUsuario();
			jArray = Util.consultaDatosInternet(url);
			
			// Seteo de datos de usuario
			if(jArray != null){
				JSONObject obj = (JSONObject)jArray.get(0);
				
				if(obj != null){
					Util.setIdUsuario(obj.getInt("idUsuario"));
					String nombre = obj.getString("nombre");
					while(nombre.contains("."))
						nombre = nombre.replace('.', ' ');
					Util.setNombre(nombre);
					Util.setTelefono(obj.getString("telefono"));
					Util.setEmail(obj.getString("email"));
					String pais = obj.getString("pais");
					while(pais.contains("."))
						pais = pais.replace('.', ' ');
					Util.setPais(pais);
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * Comprueba que no se repitan las redes
	 * @param valorIntroducido
	 * @param posicionSpinnerSeleccionada
	 * @return boolean
	 */
	public static boolean compruebaExistenciaRed(String valorIntroducido, Integer posicionSpinnerSeleccionada, 
				Integer posicionEnEdicion){
		try{
			if(valorIntroducido != null && posicionSpinnerSeleccionada != null){
				Red red = Util.getRedes().get(posicionSpinnerSeleccionada);
				if(misRedes != null && misRedes.size() > 0){
					for(int i=0; i<misRedes.size(); i++){
						if(posicionEnEdicion != null && i == posicionEnEdicion)
							continue;
						if(misRedes.get(i).getNombreUsuario().equals(valorIntroducido) && 
									misRedes.get(i).getIdRed().equals(red.getIdRed())){
							return false;
						}
					}
				}
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
}
