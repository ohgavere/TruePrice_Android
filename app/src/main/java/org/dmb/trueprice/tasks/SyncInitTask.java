package org.dmb.trueprice.tasks;

import org.dmb.trueprice.Sync;
import org.dmb.trueprice.connector.SyncInitConnector;
import org.dmb.trueprice.objects.SyncInitRequest;
import org.dmb.trueprice.objects.SyncInitResponse;
import org.dmb.trueprice.utils.internal.GsonConverter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class SyncInitTask extends AsyncTask<SyncInitConnector, Void, Boolean> {

	private SyncInitRequest req;
	private SyncInitResponse resp;
	private Context ctxt ;
	
//	private boolean result_task_succeed = false ;

	public SyncInitTask(Context ctx, SyncInitRequest request) {
		req = request;
		ctxt = ctx ;
	}
	
//	public boolean getResultTaskSucced () {return result_task_succeed ;}

	@Override
	protected Boolean doInBackground(SyncInitConnector ... params) {
		
//		SyncInitConnector initForm = new SyncInitConnector(Sync.this);
		SyncInitConnector initForm = params[0] ;
		
		resp = initForm.attemptSyncInit(this.req);
		
		String error = initForm.getErrorMessage() ;
		
		Log.w(this.getClass().getSimpleName(), "Sync Init attempt returned error ? => [" + error + "]");
					
		if (error == null || error.isEmpty() || error.length() == 0 || error == "") {
			((Sync) ctxt).taskI_ran_once = true ;
			return true; 				
		} else {
			return false;
		}
		
	}

	@Override
	protected void onPostExecute(final Boolean success) {
					
		// Clear the objet in Sync activity cause it's done.
		((Sync) ctxt).SyncInitAsyncTask = null;
		
//		showProgress(false);

		// Handle result of task
		if (resp != null && success == true) {				
			// Enregistrer l'objet pour l'affichage
			((Sync) ctxt).setLastInitResponse(resp);
			// Ecrire la reponse du serveur dans le fichier
			((Sync) ctxt).provider.writeUserInitResponseContent(
				GsonConverter.toJson(resp)
			);
			
			// La tache est terminee
//			result_task_succeed = true;
			((Sync) ctxt).taskI_succeeded = true ;
			
			Toast.makeText(ctxt, "Task succeeded. Data updated successfully.", Toast.LENGTH_LONG).show();				
			
		} else {
			Toast.makeText(ctxt,
				"The server response seems to be empty or an error occured " 
				+ "during synchronisation ; cancel operation was done. Please try again later."
				, Toast.LENGTH_LONG).show();
		}
		
		// Tell Sync activity we are done. (update view if needed)
		((Sync) ctxt).finishSyncInit();					
		
	}
	
	

	@Override
	protected void onCancelled() {
		
		Log.d("Sync -- static", "SyncInit AsyncTask onCancelled()");
		
		((Sync) ctxt).SyncInitAsyncTask = null;
//		showProgress(false);
	}
}	

