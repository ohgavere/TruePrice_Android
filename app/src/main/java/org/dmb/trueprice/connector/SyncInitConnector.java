package org.dmb.trueprice.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.dmb.trueprice.objects.SyncInitRequest;
import org.dmb.trueprice.objects.SyncInitResponse;
import org.dmb.trueprice.utils.internal.GsonConverter;

import android.content.Context;
import android.util.Log;

public class SyncInitConnector extends Connector {
	
	private static final String logHead = SyncInitConnector.class.getSimpleName();

	
//	private String localUrl = "https://192.168.1.2/TruePrice/connexion";
	
	public SyncInitConnector(Context ctx) {
		super(ctx, "https://vps116172.ovh.net/sync");
	}

	public SyncInitResponse attemptSyncInit(SyncInitRequest ScInitReq) {
		
		SyncInitResponse ScInitResponse = null ;
		
		HttpResponse response ;
		
		HttpGet get = new HttpGet(this.url) ;
		
//		InputStream in = null ;
//		OutputStream out = System.out;
		
			
		try {
			
			if (ScInitReq != null) {
				get.setParams(new BasicHttpParams()
					.setParameter("data", GsonConverter.toJson(ScInitReq))
				);				
			}
			
			response = Connector.httpClient.execute(get) ;
			
			int rCode = response.getStatusLine().getStatusCode();

			Log.d(this.getClass().getSimpleName() ," =========== Get " + this.url + " returned [" + rCode + "]");
			
				
				Header [] head = response.getAllHeaders();
				
				String str = printHeaders(head) ;
				
				Log.d(this.getClass().getSimpleName() ," =========== Try to print response headers :\n\n" + str);
				
				
				
				if(rCode != HttpStatus.SC_OK || str.contains("TruePriceError") ) { 
						
//					setResult(false);
					switch (getHeaderValue(head, "TruePriceError")) {
					case "403":
						setErrorMessage("Authentication failed. Bad value.");
						break;

					default:
						setErrorMessage("Sync Init Process attempt failed for unknown reason ..." 
							+ " Header[TruePriceError]<=>[" + getHeaderValue(head, "TruePriceError") +"]"
						);
						break;
					}
				
				} 
				
				
				
				else {

					try {
						
						HttpEntity ent = response.getEntity() ;
						String jsonData = getStringFromEntity(ent);						
						
						Log.i(logHead, "Got data from InputStream as String : " + jsonData
							+ "\n Now trying to convert it to object -->" );
						
						ScInitResponse = GsonConverter.fromJsonInitResponse(jsonData);
						
						if (ScInitResponse == null) {
							Log.i(logHead, "The response content was ==> [ ...\n" 
								+ "type = " + ent.getContentType() + "\t"
								+ "encoding = " + ent.getContentEncoding() + "\t"
								+ "length = " + ent.getContentLength()+ "\t"
								+ "data = ... \n" + jsonData
								+ "\n ... ]");
							throw new Exception("The response cannot be parsed. Please retry.");
						}
						
					} catch (Exception e) {
						Log.w(logHead, "Can't read content from Http Response entity because :" + e.getMessage());
						e.printStackTrace();
					}
				
				
				}

				
		} catch (IOException e) {
			Log.w(logHead, "IO Error append during attempt : " + e.getMessage());
			e.printStackTrace();
//			setResult(false);
		}		
		
		return ScInitResponse ;
	}
		

	@Override
	public void resetValues() {
		super.resetValues();					
	}
	
	
	

}
