package org.dmb.trueprice.tools;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.Locale;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;
import org.dmb.trueprice.R;

import android.content.Context;
import android.os.Build;
import android.util.Log;


public class SelfSignCertHttpClient extends DefaultHttpClient {

	final static String AGENT_UTILISATEUR = "%s/%s (Android/%s/%s/%s/%s)"; 
	
	private static String agentUtilisateur ; 
	
	private static final String modele = Build.MODEL;
	private static final String device = Build.DEVICE;
	private static final String deviceId= Build.ID;
	private static final String manufacturer = Build.MANUFACTURER;
	private static final String product = Build.PRODUCT;
	private static final String versionSysteme = Build.VERSION.RELEASE;
	private static final String codeSysteme = Build.DISPLAY;
	
	private static final String paramsReg = Locale.getDefault().toString() 
			+ "] also availabable [" + Locale.getAvailableLocales().toString();
	
	private static String nomPaquetage ;
	
	private static Context mContext ;
	
	/*	Permet d'obtenir des informations 
	 * sur les appli installees sur le device
	 * donc inutile ici */
//	private static PackageManager manager ; 
//	
//	private static PackageInfo info = null;
	
	private static void setContext(Context context) {
		mContext = context;
		formatUserAgent();
	}

	public SelfSignCertHttpClient(Context context) {
		super();
		setContext(context);
		createClientConnectionManager();
	}	
	
	public SelfSignCertHttpClient(HttpParams params, Context context) {
		super(params);
		setContext(context);
		createClientConnectionManager();
	}

	private SelfSignCertHttpClient(ClientConnectionManager httpConnectionManager, HttpParams params, Context context) {
		super(httpConnectionManager, params);
		setContext(context);
	}	

//	public SelfSignCertHttpClient(Context ctx) {
		
//		mContext = ctx ;
//		
//		nomPaquetage = mContext.getPackageName();
		
//		manager = mContext.getPackageManager();
//		
//		try { 
//			  info = manager.getPackageInfo(mContext.getPackageName(), 0); 
//		
//		String nomPaquetage = info.packageName; 
//		String nomVersion = info.versionName;
//		
//		} catch (NameNotFoundException e1) { 
//		}		
		
//		formatUserAgent();
		
//	}
	
	private static void formatUserAgent() {
		
		String appName = null ;
		if (mContext != null) { 		
			nomPaquetage = mContext.getPackageName();
			appName = mContext.getApplicationInfo().name ;
		}
		
		agentUtilisateur = String.format( AGENT_UTILISATEUR, 
			// App infos
			nomPaquetage, (appName == null ? "app name not found" : appName),
			// Device hardware infos
			device, deviceId, modele, product, manufacturer, 
			// Device software infos
			versionSysteme, codeSysteme, 
			// Device "Locale" infos
			paramsReg
		);	
	}
	

	
//	public void attemptConnection() {
//		
//		AndroidHttpClient httpClient = AndroidHttpClient.newInstance(agentUtilisateur, mContext) ;
//		
//		HttpResponse response ;
//		
//		HttpGet get = new HttpGet("https://192.168.1.2/TruePrice/") ;
//		
//		InputStream in = null ;
//		OutputStream out = System.out;
//		
//			try {
//				
//				get.setParams(HttpParams.class.newInstance()
//					.setParameter("AndroidFirstParam", "value 1 from android")
//					.setParameter("SecondParam", " So cool reading this =) ! ^^")
//				);
//			} catch (InstantiationException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IllegalAccessException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		
//			
//		try {
//			
//			response = httpClient.execute(get) ;
//			
////			if (response.)
//			
//			HttpEntity entity = response.getEntity() ;
//			
//			entity.writeTo(out);
//			
//			
//			
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//		
//	}
	
	
	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		
		SchemeRegistry registry = new SchemeRegistry();
		
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		
		registry.register(new Scheme("https", newSslSocketFactory(), 443));
		
		return new SingleClientConnManager(getParams(), registry);
	}
	
	
	private SSLSocketFactory newSslSocketFactory() {
        try {
            
        	// Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("BKS");
            
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = SelfSignCertHttpClient.mContext.getResources()
            		
            	//name of your keystore file here
//            		.openRawResource(R.raw.localhost);			// 132321
//            		.openRawResource(R.raw.trueprice_android); 	// 132321
            		.openRawResource(R.raw.manual_store_bks); 	// 132321
            
            try {
            	
            	Log.d(this.getClass().getSimpleName(), "Start loading own certificate(s)");
            	
                // Initialize the keystore with the provided trusted certificates
                // Provide the password of the keystore
//                trusted.load(in, "NTherchies7050012".toCharArray());
//            	trusted.load(in, "NTherchies132321".toCharArray());
            	trusted.load(in, "132321".toCharArray());
                
                Log.d(this.getClass().getSimpleName(), "Loading certificate(s) done.");
                
            } finally {
                in.close();
            }
            
            // Pass the keystore to the SSLSocketFactory. The factory is responsible
            // for the verification of the server certificate.
            SSLSocketFactory sf = new SSLSocketFactory(trusted);
            
            
            
            // Hostname verification from certificate
            // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
            
            // This can be changed to less stricter verifiers, according to need
            
//            The only difference between BROWSER_COMPATIBLE and STRICT is that a wildcard (such as "*.foo.com")
//            with BROWSER_COMPATIBLE matches all subdomains, including "a.b.foo.com".
            
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
            
            
            
            return sf;
        } catch (Exception e) {
        	Log.d(this.getClass().getSimpleName(), "Excp. thrown during sslSocketFactory : " + e.getMessage() + e.getStackTrace().toString());
        	e.printStackTrace();
            throw new AssertionError(e);
        }
    }	
	
}
