package org.dmb.trueprice.tasks;

import java.util.ArrayList;
import java.util.HashMap;

import org.dmb.trueprice.Sync;
import org.dmb.trueprice.connector.SyncGetterConnector;
import org.dmb.trueprice.objects.ListeDetailFrontend;
import org.dmb.trueprice.objects.SyncGetterRequest;
import org.dmb.trueprice.objects.SyncGetterResponse;
import org.dmb.trueprice.objects.SyncHistory;
import org.dmb.trueprice.objects.android.AvailableListFrontend;
import org.dmb.trueprice.objects.android.SynchronizedList;
import org.dmb.trueprice.tools.SyncGetterVerifier;
import org.dmb.trueprice.utils.internal.GsonConverter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class SyncGetterTask extends AsyncTask<SyncGetterConnector, Void, Boolean> {

	private SyncGetterRequest req;
	private SyncGetterResponse actualResponse;
	private Context ctxt ;
	
	private HashMap<String, String> verifierErrors ;
	
//	private boolean result_task_succeed = false ;

	public SyncGetterTask(Context ctx, SyncGetterRequest request) {
		req = request;
		ctxt = ctx ;
	}
	
//	public boolean getResultTaskSucced () {return result_task_succeed ;}

	@Override
	protected Boolean doInBackground(SyncGetterConnector ... params) {
		
//		SyncGetterConnector GetterForm = new SyncGetterConnector(Sync.this);
		SyncGetterConnector getterForm = params[0] ;
		
		SyncGetterResponse newResponse = getterForm.attemptSyncGetter(this.req);
		
		String error = getterForm.getErrorMessage() ;
		
		Log.w(this.getClass().getSimpleName(), "Sync Getter attempt returned error ? => [" + error + "]");
					
		if (error == null || error.isEmpty() || error.length() == 0 || error == "") {
			
			
//				SyncHistory history = GsonConverter.fromJsonSyncHistory(
//					((Sync)ctxt).provider.getSyncHistoryContent()
//				);
				
				ArrayList<String> filesNamesInUserIconFolder = 
					((Sync) ctxt).provider.getUserIconFolderFilesNames() ;		

				ArrayList<String> filesNamesInGenericIconFolder = 
					((Sync) ctxt).provider.getGenericIconFolderFilesNames();				
				
//				SyncGetterVerifier verifier = new SyncGetterVerifier(req, resp, history);
				SyncGetterVerifier verifier = new SyncGetterVerifier(req, newResponse, filesNamesInUserIconFolder, filesNamesInGenericIconFolder);
//				verifier.checkIntegrity();

				
				verifierErrors = verifier.checkResponse(/*filesNamesInUserIconFolder*/) ;
				
				// Essayer d'obtenir une reponse antérieure
				actualResponse = GsonConverter.fromJsonGetterResponse(
					((Sync)ctxt).provider.getUserGetterResponseContent()
				);
				
				// Si pas encore de reponse enregistrée		
				if (actualResponse == null) {	
					actualResponse = newResponse ;
				}
				// Sinon mettre à jour
				else {
					for (ListeDetailFrontend newListe : newResponse.getProvidedLists()) {
						actualResponse.AddOrUpdateListEntry(newListe);
						((Sync)ctxt).addLastSynchronizedList(newListe);
					}
					for (Long key : newResponse.getMapProduitIcon().keySet()) {
						actualResponse.AddOrUpdateIconEntry(	key, 
							newResponse.getMapProduitIcon().get(key)
						);
					}
				}					
							
				// Ecrire le contenu mis a jour
				((Sync)ctxt).provider.writeUserGetterResponseContent(actualResponse);							
											
				
			
			((Sync) ctxt).taskG_ran_once = true ;
			return true; 				
		} else {
			return false;
		}
		
	}

	@Override
	protected void onPostExecute(final Boolean success) {
					
		// Clear the objet in Sync activity cause it's done.
		((Sync) ctxt).SyncGetterAsyncTask = null;
		
//		showProgress(false);

		// Handle result of task
		if (actualResponse != null && success == true) {
			
			// Enregistrer l'objet pour l'affichage
			((Sync) ctxt).setLastGetterResponse(actualResponse);
			

			
			String finalToastText = "" ;
			
			if (verifierErrors == null || verifierErrors.isEmpty()) {
				
				
				// La tache est terminee avec succes
				((Sync) ctxt).taskG_succeeded = true ;	
				
				finalToastText = "La tâche s'est achevée avec succès. Toutes ces listes snot prêtes à être utilisées.";
				
			} else {
				
				
				
				if (verifierErrors.containsKey(SyncGetterVerifier.ERR_ICON_MISSING)) {
					
					// pour chaque icon
					int count = verifierErrors.get(SyncGetterVerifier.ERR_ICON_MISSING).split(",").length ;
					
					((Sync) ctxt).enableSyncIconsTask(
						verifierErrors.get(SyncGetterVerifier.ERR_ICON_MISSING)
					);
					
//					((Sync) ctxt).i
					
//					Toast.makeText(ctxt, 
					finalToastText +=
						"Un ou plusieurs icône(s) de produit(s)" 
						+ " sont manquants (total: " + count +")." 
//						, Toast.LENGTH_LONG).show()
					;	
										 
					
				} 
				
				

				if (verifierErrors.containsKey(SyncGetterVerifier.ERR_EMPTY_LIST)) {
					
					if (finalToastText.length() > 0) {finalToastText += "\n";}
					
					// pour chaque liste
					String strIds = "" ;
					for (String id : verifierErrors.get(SyncGetterVerifier.ERR_EMPTY_LIST).split(",")) {
						strIds += id + "," ;
					}
					strIds.substring(0,strIds.lastIndexOf(","));
					
//					Toast.makeText(ctxt, 
					finalToastText +=
						"La/Les liste(s) [" + strIds +"] est/sont sans" 
						  + "produit et ne peut/peuvent être utilisée(s)/recupérée(s)." 
//							, Toast.LENGTH_LONG).show()
						;
				}
				
				// La tache est terminee mais avec legeres erreurs
				((Sync) ctxt).taskG_succeeded = false ;				
			}					
			
			
			

			
			Toast.makeText(ctxt, finalToastText, Toast.LENGTH_LONG).show();				
			
		} else {
			Toast.makeText(ctxt,
				"The server response seems to be empty or an error occured " 
				+ "during synchronisation ; cancel operation was done. Please try again later."
				, Toast.LENGTH_LONG).show();
		}
		
		// Tell Sync activity we are done. (update view if needed)
		((Sync) ctxt).finishSyncGetter();;					
		
	}
	
	

	@Override
	protected void onCancelled() {
		
		Log.d("Sync -- static", "SyncGetter AsyncTask onCancelled()");
		
		((Sync) ctxt).SyncGetterAsyncTask = null;
//		showProgress(false);
	}
}	

