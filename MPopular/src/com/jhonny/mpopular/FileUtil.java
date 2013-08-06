package com.jhonny.mpopular;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import android.content.Context;


public class FileUtil implements Serializable{
	
	private static final long serialVersionUID = 6340298289567938341L;
	
	
	public static boolean cargaDatosPersonales(Context ctx){
		boolean resp = false;
		try{
			InputStream instream = ctx.openFileInput(Constantes.FICHERO_CONFIGURACION);
			if(instream != null){
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				int contador = 1;
				String linea = buffreader.readLine();
				
				while(linea != null){
					if(linea.equals(""))
						linea = buffreader.readLine();
					if(linea != null){
						resp = true;
						switch(contador){
							case 1:
								// linea 1 del fichero
								Util.setIdUsuario(Integer.parseInt(linea));
								break;
							case 2:
								Util.setNombre(linea); 
								break;
							case 3:
								Util.setTelefono(linea);
								break;
							case 4:
								Util.setEmail(linea);
								break;
							case 5:
								Util.setPais(linea);
								break;
						}
					}
					contador++;
					linea = buffreader.readLine();
				}
			}
		}catch(FileNotFoundException fnf){
			try{
				FileOutputStream out = ctx.openFileOutput(Constantes.FICHERO_CONFIGURACION, Context.MODE_PRIVATE);
				out.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resp;
	}
	
	
	
	public static void almacenaDatosConfiguracion(Context ctx){
		OutputStreamWriter out = null;
		
		try{
			OutputStream output = ctx.openFileOutput(Constantes.FICHERO_CONFIGURACION, Context.MODE_PRIVATE);
    		out = new OutputStreamWriter(output);
    			
    		// valor de idUsuario
			out.write(Util.getIdUsuario() + "\r\n");
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(out != null)
					out.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	
	/**
	 * borra el fichero de la aplicacion
	 * @param ctx
	 */
	public static void borraFicheroActualDeLaAplicacion(Context ctx){
		OutputStreamWriter out = null;
		try{
			OutputStream output = ctx.openFileOutput(Constantes.FICHERO_CONFIGURACION, Context.MODE_PRIVATE);
			out = new OutputStreamWriter(output);
			out.write("");
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				out.close();
			}catch(IOException e){
				e.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
