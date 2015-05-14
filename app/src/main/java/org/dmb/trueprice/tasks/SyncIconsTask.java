package org.dmb.trueprice.tasks;

import java.util.HashMap;

import org.dmb.trueprice.Sync;
import org.dmb.trueprice.connector.SyncIconsConnector;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class SyncIconsTask extends AsyncTask<Void, Void, Boolean> {

//	private SyncGetterRequest req;
//	private SyncGetterResponse resp;
	private Context ctxt ;
	
	private HashMap<String,String> askedIcons = new HashMap<String, String>();
		
//	private boolean result_task_succeed = false ;

	public SyncIconsTask(Context ctx, HashMap<String,String> iconMap) {
//		req = request;
		ctxt = ctx ;
		askedIcons = iconMap;
	}
	
//	public boolean getResultTaskSucced () {return result_task_succeed ;}

	@Override
	protected Boolean doInBackground(Void ... params)  {
		
//		SyncGetterConnector GetterForm = new SyncGetterConnector(Sync.this);
		
//		SyncGetterConnector getterForm = params[0] ;
//		
//		resp = getterForm.attemptSyncGetter(this.req);
//		
//		String error = getterForm.getErrorMessage() ;
//		
//		Log.w(this.getClass().getSimpleName(), "Sync Getter attempt returned error ? => [" + error + "]");
//					
//		if (error == null || error.isEmpty() || error.length() == 0 || error == "") {
//			
//			
//				SyncHistory history = GsonConverter.fromJsonSyncHistory(
//					((Sync)ctxt).provider.getSyncHistoryContent()
//				);
//				
//				SyncGetterVerifier verifier = new SyncGetterVerifier(req, resp, history);
////				verifier.checkIntegrity();
//				
//				ArrayList<String> filesNamesInUserIconFolder = 
//					((Sync) ctxt).provider.getUserIconFolderFilesNames() ;
//				
//				verifierErrors = verifier.checkResponse(filesNamesInUserIconFolder) ;
//					
//			
//			
//			
//			((Sync) ctxt).taskG_ran_once = true ;
//			return true; 				
//		} else {
//			return false;
//		}
		
		SyncIconsConnector conector = new SyncIconsConnector(ctxt);

		
		for (String pdtType : askedIcons.keySet()) {
					
			Bitmap bmp = null ;
			
			try {
			
				String url = askedIcons.get(pdtType) ;
				String id = url.substring(0, url.indexOf("-")) ;
				url = url.substring(url.indexOf("-") +1 );
				
				Log.d("SyncIconsTask", "Ask to download icon with ID [" + id + "] at url [" + url + "]");
				
				conector.setUrl(url);
				
				bmp = conector.getIcon();
			
				if (pdtType.contains(Sync.genKeyPrefix)) 
				{
					((Sync) ctxt).provider.writeFileInGenericIconFolder(
							
						// le nom de l'image
						url.substring(url.lastIndexOf("/")),
						// le bitmap
						bmp
					);
				} else {
					((Sync) ctxt).provider.writeFileInUserIconFolder(
							
							// le nom de l'image
							url.substring(url.lastIndexOf("/")),
							// le bitmap
							bmp
						);
					}								
			} catch (Exception e) {
				 e.printStackTrace();
				return false ;
				
	        }
		}
				
				
				
				
				
//				
//	            // this will be useful to display download percentage
//	            // might be -1: server did not report the length
//	            int fileLength = connection.getContentLength();
//	
//	            // download the file
//	            input = connection.getInputStream();
//	            output = new FileOutputStream("/sdcard/file_name.extension");
//	
//	            byte data[] = new byte[4096];
//	            long total = 0;
//	            int count;
//	            
//	            while ((count = input.read(data)) != -1) {
//	                // allow canceling with back button
//	                if (isCancelled()) {
//	                    input.close();
//	                    return null;
//	                }
//	                total += count;
//	                // publishing the progress....
//	                if (fileLength > 0) // only if total length is known
//	                    publishProgress((int) (total * 100 / fileLength));
//	                output.write(data, 0, count);
//	            }
//	            
//			} catch (Exception e) {
//				 e.printStackTrace();
//				return false ;
//				
//	        } finally {
//	            try {
//	                if (output != null)
//	                    output.close();
//	                if (input != null)
//	                    input.close();
//	            } catch (IOException ignored) {
//	            }
//	
//	            if (connection != null)
//	                connection.disconnect();
//	        }
//        
//	}
		
		((Sync) ctxt).taskIcons_ran_once = true ;
        return false;			
		
	}

	@Override
	protected void onPostExecute(final Boolean success) {
					
		// Clear the objet in Sync activity cause it's done.
		((Sync) ctxt).SyncGetterAsyncTask = null;
		
//		showProgress(false);

		// Handle result of task
//		if (resp != null && success == true) {
			
			// Enregistrer l'objet pour l'affichage
//			((Sync) ctxt).setLastGetterResponse(resp);
//			// Ecrire la reponse du serveur dans le fichier
//			((Sync) ctxt).provider.writeUserGetterResponseContent(
//				GsonConverter.toJson(resp)
//			);
//			
//			
//			
//			if (verifierErrors == null || verifierErrors.isEmpty()) {
//				
//			} else {
//				
//				if (verifierErrors.containsKey(SyncGetterVerifier.ERR_ICON_MISSING)) {
//					// pour chaque icon
//					int count = 0 ;
//					for (String id : verifierErrors.get(SyncGetterVerifier.ERR_ICON_MISSING).split(",")) {
//						count ++ ;
//					}
//					
//					Toast.makeText(ctxt, "Un ou plusieurs icône(s) de produit(s)" 
//					+ " sont manquants (total: " + count +")." , Toast.LENGTH_LONG).show();					
//					
//				} else  if (verifierErrors.containsKey(SyncGetterVerifier.ERR_EMPTY_LIST)) {
//					// pour chaque liste
//					String strIds = "" ;
//					for (String id : verifierErrors.get(SyncGetterVerifier.ERR_EMPTY_LIST).split(",")) {
//						strIds += id + "," ;
//					}
//					strIds.substring(0,strIds.lastIndexOf(","));
//					
//					Toast.makeText(ctxt, "La/Les liste(s) [" + strIds +"] est/sont sans" 
//						  + "produit et ne peut/peuvent être utilisée(s)/recupérée(s).", Toast.LENGTH_LONG).show();
//				}
//				
//			}					
			
			
//			
//			
//			// La tache est terminee
////			result_task_succeed = true;
//			((Sync) ctxt).taskG_succeeded = true ;
//			
//			Toast.makeText(ctxt, "Task succeeded. File wrote successfully.", Toast.LENGTH_LONG).show();				
			
//		} else {
//			Toast.makeText(ctxt,
//				"The server response seems to be empty or an error occured " 
//				+ "during synchronisation ; cancel operation was done. Please try again later."
//				, Toast.LENGTH_LONG).show();
//		}
//		
//		// Tell Sync activity we are done. (update view if needed)
//		((Sync) ctxt).finishSyncGetter();					
		
		
		// La tache est terminee
		((Sync) ctxt).taskIcons_succeeded = true ;		
		
		// Tell Sync activity we are done. (update view if needed)
		((Sync) ctxt).finishSyncIcons();
		
	}
	
	

	@Override
	protected void onCancelled() {
		
		Log.d("Sync -- static", "Sync Icons AsyncTask onCancelled()");
		
		((Sync) ctxt).SyncGetterAsyncTask = null;
//		showProgress(false);
	}
}	

