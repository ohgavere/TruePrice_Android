package org.dmb.trueprice.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;

import android.content.Context;
import android.util.Log;

public class ConnTester  {

	SelfSignCertHttpClient client ;
	

	public ConnTester(Context context) {
		client = new SelfSignCertHttpClient(context);		
	}

	
	public void attemptConnectionSecure() {
		
//		SelfSignCertHttpClient httpClient = new SelfSignCertHttpClient(
////				createClientConnectionManager() , 
//				null , mContext);
		
		
		
//		client.createClientConnectionManager();
		
		HttpResponse response ;
		
		HttpGet get = new HttpGet("https://192.168.1.2/TruePrice/") ;
		
		InputStream in = null ;
		OutputStream out = System.out;
		
//			try {
				
				get.setParams(
//						HttpParams.class.newInstance()
						new BasicHttpParams()
					.setParameter("AndroidFirstParam", "value 1 from android")
					.setParameter("SecondParam", " So cool reading this =) ! ^^")
				);
//			} catch (InstantiationException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IllegalAccessException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		
			
		try {
			
			response = client.execute(get) ;
			
			String str = response.getAllHeaders().toString() ;
			
			Log.d(" =========== Try to print headers :\n\n", str);
			
			out.write(str.getBytes());
			
			HttpEntity entity = response.getEntity() ;
			
			Log.d(" =========== Try to print response entity :\n\n", entity.toString());
			
			entity.writeTo(out);
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}	
	
}
