package org.dmb.trueprice.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.dmb.trueprice.objects.SyncGetterRequest;
import org.dmb.trueprice.objects.SyncGetterResponse;
import org.dmb.trueprice.utils.internal.GsonConverter;

import android.content.Context;
import android.util.Log;

public class SyncGetterConnector extends Connector {
	
	private static final String logHead = SyncGetterConnector.class.getSimpleName();

	
//	private String localUrl = "https://192.168.1.2/TruePrice/connexion";
	
	public SyncGetterConnector(Context ctx) {
		super(ctx, "https://vps116172.ovh.net/sync/getter");
	}

	public SyncGetterResponse attemptSyncGetter(SyncGetterRequest ScGetReq) {
		
		String jsonDataDebug = logRequestContent(ScGetReq);
		
		SyncGetterResponse ScGetResponse = null ;
		
		HttpResponse response ;
		
		HttpGet get = new HttpGet(this.url) ;
		
//		InputStream in = null ;
//		OutputStream out = System.out;
		
			
		try {

			if (ScGetReq != null) {
				
////				HttpParams mHttpPrms =  ;
//							
//				get.setParams(new BasicHttpParams()
//					.setParameter("data", jsonDataDebug
//				)
////				mHttpPrms
//				);
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add( new BasicNameValuePair( "data", jsonDataDebug ) );	
				
				get = new HttpGet(this.url + "?" + URLEncodedUtils.format(params, "utf-8"));
				
				Log.d(logHead, "Added param to URL. Now is : \n" + get.getRequestLine());
			}
			
			
			response = Connector.httpClient.execute(get) ;
			
			int rCode = response.getStatusLine().getStatusCode();

			Log.d(this.getClass().getSimpleName() ," =========== Get " + this.url + " returned [" + rCode + "]");
			
				
				Header [] head = response.getAllHeaders();
				
				String str = printHeaders(head) ;
				
				Log.d(this.getClass().getSimpleName() ," =========== Try to print response headers :\n\n" + str);
				
				
				
				if(rCode != HttpStatus.SC_OK || str.contains("TruePriceError") ) { 
						
//					setResult(false);
					String TPError = getHeaderValue(head, "TruePriceError")  ;
					TPError = (TPError == null) ? "" : TPError ;
					
					switch (TPError) {
					case "403":
						setErrorMessage("[" + rCode +"] Authentication failed. Bad value.");
						break;

					default:
						setErrorMessage("[" + rCode +"] Sync Getter Process attempt failed for unknown reason ..." 
							+ " Header[TruePriceError]<=>[" + getHeaderValue(head, "TruePriceError") +"]"
						);
						break;
						
					}
					
					
					logResponseContent(response) ;
					
				
				} 
				
				
				// Si tout semble etre OK
				else {
					
					
					// Esayer de lire le contenu en tant que json
					try {
						
						
						HttpEntity ent = response.getEntity() ;
						String jsonData = getStringFromEntity(ent);
						
						Log.i(logHead, "Got data from InputStream as String : " + jsonData
							+ "\n Now trying to convert it to object -->" );
						
						
						// Convertir la chaine de string en objet
						ScGetResponse = GsonConverter.fromJsonGetterResponse(jsonData);
						
						if (ScGetResponse == null) {
							Log.i(logHead, "The response content was ==> [ ...\n" 
								+ "type = " + ent.getContentType() + "\t"
								+ "encoding = " + ent.getContentEncoding() + "\t"
								+ "length = " + ent.getContentLength()+ "\t"
								+ "data = ... \n" + jsonData
								+ "\n ... ]");
							throw new Exception("The response cannot be parsed. Please retry.");
						}
						
					// Erreur lecture contenu reponse
					} catch (Exception e) {
						Log.w(logHead, "Can't read content from Http Response entity because :" + e.getMessage());
						e.printStackTrace();
					}
				}

		// Erreur globale d' I/O 
		} catch (IOException e) {
			Log.w(logHead, "IO Error append during attempt : " + e.getMessage());
			e.printStackTrace();
//			setResult(false);
		}		
		
		return ScGetResponse ;
	}
		

	@Override
	public void resetValues() {
		super.resetValues();					
	}
	
	private String logRequestContent(SyncGetterRequest ScGetReq)  {
		
		String jsonData = GsonConverter.toJson(ScGetReq);
		
		Log.i(logHead, "Got data for Getter Request as JSON String : \n" + jsonData);
		return jsonData;
		
	}
		
	private void logResponseContent(HttpResponse response) throws IOException {
		
		HttpEntity ent = response.getEntity() ;
		String jsonData = getStringFromEntity(ent);
		
		Log.i(logHead, "Got data from InputStream as String : " + jsonData
			+ "\n Now trying to convert it to object -->" );
		
	}
	
	

}
