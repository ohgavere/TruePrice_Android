package org.dmb.trueprice.tasks;

import java.util.ArrayList;

import org.dmb.trueprice.Sync;
import org.dmb.trueprice.connector.SyncIconsConnector;
import org.dmb.trueprice.objects.SyncHistory;
import org.dmb.trueprice.objects.android.SynchronizedList;
import org.dmb.trueprice.utils.internal.GsonConverter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class SyncHistoryTask extends AsyncTask<Void, Void, Boolean> {

	private Context ctxt ;
	
	private ArrayList<SynchronizedList> newHistory = new ArrayList<SynchronizedList>();
	
	SyncHistory actualHistory ;
		
	
	public SyncHistoryTask(Context ctx, ArrayList<SynchronizedList> newHistory ) {
		this.ctxt		= ctx ;
		this.newHistory = newHistory ;
	}
	
	@Override
	protected Boolean doInBackground(Void ... params)  {
		
		try {		
			
			actualHistory = GsonConverter.fromJsonSyncHistory(
				((Sync)ctxt).provider.getUserSyncHistoryContent()
			);
		
			if (actualHistory == null) {	actualHistory = new SyncHistory(new ArrayList<SynchronizedList>());	}			
				
			for (SynchronizedList newListeSynchronized : newHistory) {
								
				actualHistory.AddOrUpdateEntry(newListeSynchronized);
				
			}					
			
			((Sync)ctxt).provider.writeUserSynchronizedLists(actualHistory);
			
			
			
			
			((Sync) ctxt).taskH_ran_once = true ;
			
	        return true;		
			
			
		} catch (Exception e) {
			((Sync) ctxt).taskH_ran_once = false ;
			
			e.printStackTrace();
			
			return false ;
	    }		
		
	}

	@Override
	protected void onPostExecute(final Boolean success) {
					
		// Clear the objet in Sync activity cause it's done.
		((Sync) ctxt).SyncHistoryAsyncTask = null;
							
		// La tache est terminee
		((Sync) ctxt).taskH_succeeded = true ;		
		
		// Tell Sync activity we are done. (update view if needed)
		((Sync) ctxt).finishSyncHistory();
		
	}
	
	

	@Override
	protected void onCancelled() {
		
		Log.d("Sync -- static", "Sync History AsyncTask onCancelled()");
		
		((Sync) ctxt).SyncHistoryAsyncTask = null;
//		showProgress(false);
	}
}	

