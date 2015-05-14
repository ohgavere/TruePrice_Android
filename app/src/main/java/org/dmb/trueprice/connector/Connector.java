package org.dmb.trueprice.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.dmb.trueprice.tools.SelfSignCertHttpClient;

import android.content.Context;
import android.util.Log;

public class Connector {
	
//	public static final String CON_ERROR_WRONG_VALUE = "Wrong value." ;
//	public static final String CON_ERROR_BAD_VALUE = "Bad parameter value." ;
	public static final String CON_ERROR_UNREACHABLE = "ENETUNREACH" ; // Given in Error message from java.net.ConnectException // Use String.contains()
	public static final String CON_ERROR_TIMEOUT = "Connection timed out." ;
	
	private String username ;
	private String password ;
	
	private String errorMessage = "" ;
	
	protected static SelfSignCertHttpClient httpClient ;
	
	private static Context context ;
	
	protected String url ;
	
	public Connector(Context ctx, String url) {
		
		setContext(ctx);
		
		setUrl(url);
		
		getHttpClient();
	}
	
	public static SelfSignCertHttpClient getHttpClient() {
		if (httpClient == null) {
			
		    HttpParams httpParameters = new BasicHttpParams();
		    // Set the timeout in milliseconds until a connection is established.
		    // The default value is zero, that means the timeout is not used. 
		    int timeoutConnection = 4000;
		    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		    // Set the default socket timeout (SO_TIMEOUT) 
		    // in milliseconds which is the timeout for waiting for data.
		    int timeoutSocket = 6000;
		    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);		
			
			httpClient = new SelfSignCertHttpClient(httpParameters, getContext());			
			
		}		
		return httpClient;
	}
	
	protected String getHeaderValue (Header[] head, String key) {
		
		String value = null ;

		if (head != null && key != null) {
			
			int i = 0 ;
			String hName = null ;
			
			do {
				
				if (head[i] != null) { 
					// Anti - ArrayIndxOutOfBound Ex
					hName = ( head[i].getName() == null) ? "" :  head[i].getName() ;
				}
				
				if (hName != null && hName.compareTo(key) == 0 ) { 
					value = head[i].getValue() ; 
				} i++ ;
				
			} while (i < head.length 
//					|| value == null 
					) ; 			
			
		}		
		
		
		if (value == null) {
			Log.i(this.getClass().getSimpleName(), "getHeaderValue() -> [" + key + "] not found");
		}
		
		
		return value ;
	}
	protected String printHeaders (Header[] head ) {
		String str = "" ;
		
		for (int i = 0 ; i < head.length ; i++ ) {
			str += "["+ head[i].getName() + "] <=> [" + head[i].getValue() + "]\n";
		}		
		
		return str ;
	}

	
	

	protected void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {return errorMessage;}
	
	public void setContext(Context context) {
		this.context = context;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void resetValues() {

		setErrorMessage("");
		httpClient = new SelfSignCertHttpClient(getContext());
		
	}

	public static Context getContext() {
		return context;
	}
	
	
	protected String getStringFromEntity(HttpEntity ent) throws IOException {
//		JsonReader jRead = new JsonReader(new InputStreamReader(ent.getContent()));																	
		String jsonData = "" ;						
		BufferedReader r = null ;
		
		// Essayer de lire le flux pour obtenir un String de json
		try {  
			
			r = new BufferedReader(new InputStreamReader(ent.getContent(),"ISO-8859-1")) ;						
			String strOut ="";
//			StringBuilder sb = new StringBuilder(8192);
			
			while ((strOut = r.readLine()) != null) {
//				sb.append(strOut);
				jsonData += strOut;
			}							     							
			      
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (r != null) {r.close();}
		}
		return jsonData ;
				
	}

}
