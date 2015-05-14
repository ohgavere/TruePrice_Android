package org.dmb.trueprice.connector;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.dmb.trueprice.R;
import org.dmb.trueprice.tools.SelfSignCertHttpClient;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LoginConnector extends Connector {
	
	private String username ;
	private String password ;
	
	private boolean result = false ;	
	
//	private String localUrl = "https://192.168.1.2/TruePrice/connexion";
	
	public LoginConnector(Context ctx) {
//		super(ctx, "https://192.168.1.2/TruePrice/connexion");
		super(ctx, "https://vps116172.ovh.net/connexion");
	}

	public boolean attemptLogin(String username, String passwd) { 
		
		// Creer la requete GET de la page de login pour verifier qu'on y a bien acces
		
		setUsername(username);
		setPassword(passwd);
		
		HttpResponse response ;
		
		HttpGet get = new HttpGet(this.url) ;
		
//		InputStream in = null ;
//		OutputStream out = System.out;
		
					
		try {
			
			// Executer la requete
			response = Connector.httpClient.execute(get) ;
			// Recupere le code de retour
			int rCode = response.getStatusLine().getStatusCode();

			Log.d(this.getClass().getSimpleName() ," =========== Get " + url + " returned [" + rCode + "]");
			
			// Les headers recus
			String str = printHeaders(response.getAllHeaders()) ;
			
			Log.d(this.getClass().getSimpleName() ," =========== Try to print headers :\n" + str);
			
//			out.write(str.getBytes());			

			
			// Si status 200 OK.
			if (rCode == HttpStatus.SC_OK) {
				// Terminer le travail sur cette page
				// !! Obligatoire avant de lancer une nouvelle requete
				// Permert aussi de liberer la memoire
				response.getEntity().consumeContent();
				
//				resetValues();
				
			
				// Preparer la requete de login en POST
				HttpPost post = new HttpPost(url) ;
								
//				ContentValues cv = new ContentValues();
//				cv.put("email", this.username);
//				cv.put("motdepasse", this.password);
				
//				FileBody fileBody = new FileBody(fileToUpload, "application/octet-stream");
//				entity.addPart("upload_file", fileBody);
	
				// Ajouter les attributs 
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			    
				nameValuePairs.add(new BasicNameValuePair("email", this.username)); 				
				nameValuePairs.add(new BasicNameValuePair("motdepasse", this.password));
				
				// Former une entite
				UrlEncodedFormEntity entity  = new UrlEncodedFormEntity(nameValuePairs); 
				
				// Integrer l'entite a la requete
				post.setEntity(entity);
				
				
//					post.setParams(new BasicHttpParams()
//						.setParameter("email", this.username)
//						.setParameter("motdepasse", this.password)
//					);
					
				// Verifier les header
				str = printHeaders(post.getAllHeaders());
					
				Log.d(this.getClass().getSimpleName() ," =========== Try to send post with headers : " + str);
				
				
				// Executer la requere
				response = Connector.httpClient.execute(post);
			
				// Recupere le code status
				rCode = response.getStatusLine().getStatusCode();

				Log.d(this.getClass().getSimpleName() ," =========== Get " 
					+ this.url + " this.returned [" + rCode + "] <=> [" 
					+ response.getStatusLine().getReasonPhrase() +"]"
				);
				
				// Recuperer les headers
				Header [] head = response.getAllHeaders();
				str = printHeaders(head) ;
				Log.d(this.getClass().getSimpleName() ," =========== Try to print headers :\n\n" + str);
				
				// Si status NOT 200 OK    __OU__    Contiens une erreur logicielle (mauvais login, mauvais passwd, ...)
				if(rCode != HttpStatus.SC_OK || str.contains("TruePriceError") ) { 
						
					setResult(false);
					switch (getHeaderValue(head, "TruePriceError")) {
					case "403":
						setErrorMessage("Authentication failed. Bad value.");
						break;

					default:
						setErrorMessage("Authentication failed.");
						break;
					}
				
				}
				// Si 200 OK      __ET__     pas d'erreur logicielle
				else {
					// User authentifié
//					Log.w(this.getClass().getSimpleName(), "Connection attempt returned : is user logged in ? => [" + resultat + "]");
					setResult(true);
				}
			}
			
			
		}
		
		
		// Probable erreure de connexion wifi non active ou mal config
		 catch (ConnectException | UnknownHostException coEx ) {
//			coEx.printStackTrace();
			String errMess = coEx.getMessage();
			Log.e(this.getClass().getSimpleName(), coEx.getClass().getSimpleName() +" : " + errMess);
			
			if (errMess != null) {
				if (errMess.contains(CON_ERROR_UNREACHABLE) || coEx instanceof UnknownHostException) {
//					Toast.makeText(getContext(), getContext().getResources().getString(R.string.error_connector_network_unreachable), 10000).show();
					setErrorMessage(CON_ERROR_UNREACHABLE);
				}
			}			
			
			setResult(false);
			
		}
		
		
		
		// Le serveur n'a pas répondu dans les temps
		catch (ConnectTimeoutException coToEx ) {
			coToEx.printStackTrace();
			String errMess = coToEx.getMessage();
			Log.e(this.getClass().getSimpleName(), "Connect Timeout : " + errMess);
			
			if (errMess != null) {
//				Toast.makeText(getContext(), getContext().getResources().getString(R.string.error_connector_timeout), 10000).show();
				setErrorMessage(CON_ERROR_TIMEOUT);
			}
			setResult(false);
						
		} catch (IOException e) {
			e.printStackTrace();
			setResult(false);
		}
		
		
		
		return getResult() ;
	}
	
	
	
//	public static boolean attemptLogin() { return false;}

	protected void setResult(boolean result) {
		this.result = result;
	}	
	public boolean getResult() { return result ;}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void resetValues() {
		super.resetValues();
		setUsername("");
		setPassword("");					
	}
	

}
