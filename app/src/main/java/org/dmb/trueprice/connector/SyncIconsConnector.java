package org.dmb.trueprice.connector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class SyncIconsConnector extends Connector {
	
	private static final String logHead = SyncIconsConnector.class.getSimpleName();

	
//	private String localUrl = "https://192.168.1.2/TruePrice/connexion";
	
	public SyncIconsConnector(Context ctx) {
		super(ctx, "https://vps116172.ovh.net/sync/getter");
	}

	public Bitmap getIcon () {
		
//		String jsonDataDebug = logRequestContent(askedIcons);
		
//		SyncGetterResponse ScGetResponse = null ;
		
		HttpResponse response ;
		
		HttpGet get = null ; 
//		= new HttpGet(this.url) ;
		
//		InputStream in = null ;
//		OutputStream out = System.out;
		
			
		try {

			if (this.url != null) {
				get = new HttpGet(this.url);	
				Log.d(logHead, "Added param to URL. Now is : \n" + get.getRequestLine());
				
			}
			
			
			response = Connector.httpClient.execute(get) ;
			
			int rCode = response.getStatusLine().getStatusCode();

			Log.d(this.getClass().getSimpleName() ," =========== Get " + this.url + " returned [" + rCode + "]");
			

				if(rCode != HttpStatus.SC_OK ) { 
						
//					setResult(false);
//					String TPError = getHeaderValue(head, "TruePriceError")  ;
//					TPError = (TPError == null) ? "" : TPError ;
					
//					switch (TPError) {
//					default:
						setErrorMessage ( "[" + rCode + "] " 
							+ "Sync Icon attempt failed. Message : "
							+ response.getStatusLine().getReasonPhrase()
						);
//						break;
//						
//					}
				} 
				
				
				// Si tout semble etre OK
				else {
					
					Bitmap bmp = null ;
					
					try {
						
						HttpEntity ent = response.getEntity() ;
						
						bmp = getBitmapFromEntity(ent);
						
						Log.i(logHead, "Got Bitmap from response ? => " + (bmp == null ? "FALSE" : "TRUE"));
						
						// Convertir la chaine de string en objet
//						ScGetResponse = GsonConverter.fromJsonGetterResponse(jsonData);
						
						if (bmp == null) {
							Log.i(logHead, "The response content was ==> [ ...\n" 
								+ "type = " + ent.getContentType() + "\t"
								+ "encoding = " + ent.getContentEncoding() + "\t"
								+ "length = " + ent.getContentLength()+ "\t"
//								+ "data = ... \n" + bmp
								+ "\n ... ]");
							throw new Exception("The response cannot be parsed. Please retry.");
						}
						
						
						else {
							return bmp ;
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
		
		return null ;
	}
		

	@Override
	public void resetValues() {
		super.resetValues();					
	}
	
	
	protected Bitmap getBitmapFromEntity(HttpEntity ent) throws IOException {
		
		InputStream is = null;
		BufferedInputStream bis = null ;
		Bitmap bmp = null ;
		
		try {  	
			
			is = ent.getContent();
            bis = new BufferedInputStream(is);
            bmp = BitmapFactory.decodeStream(bis);
						
			      
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (is != null) {is.close();}
			if (bis != null) {bis.close();}
		}
		return bmp ;
				
	}	
	
	

}
