package org.dmb.trueprice.tasks;

import java.util.ArrayList;
import java.util.HashMap;

import org.dmb.trueprice.RunProduit;
import org.dmb.trueprice.objects.ProduitFrontend;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class BuildGalleryAdapterAsyncTask extends AsyncTask<Void, Void, Void> {

	private String logHead = this.getClass().getSimpleName() ;
	
	private Context ctxt ;
	
	private ArrayList<ProduitFrontend> workedProducts ;
	private HashMap<String, String> neededIcons = null;

	public BuildGalleryAdapterAsyncTask(Context ctx, ArrayList<ProduitFrontend> workedProducts, HashMap<String, String> neededIcons) {
		Log.d(logHead, "asked new " + logHead + "()");
		this.ctxt = ctx ;
		this.workedProducts = workedProducts;
		this.neededIcons = neededIcons;
	}
	
	@Override
	protected Void doInBackground(Void ... params)  {
		
		try {		
			
//			String strLog = "\n\t workedProducts map content ::";
//			
//			for (ProduitFrontend pdtInit : workedProducts) {
//				strLog += "\n\t\t [ " + pdtInit.getPdtId() + " <-> " + pdtInit.getPdtNom() + " <-> " + pdtInit.getPdtLink() + " ]" ;
//			}
//			strLog +="\n ==========" ;
//			
//			
//			
//			strLog += "\n\t IconMap content ::";
//			
//			for (String pdtId : neededIcons.keySet()) {
//				strLog += "\n\t\t [ " + pdtId + " <-> " + neededIcons.get(pdtId) + " ]" ;
//			}
//			
//			strLog += "\n ===============================" ;
			
			
			Log.d(logHead, "Got incomong products[ x" 
				+ workedProducts.size() + " ] \t icons[ x" + neededIcons.size() +" ]"
//				+ strLog
			);
			
			
			
			HashMap<Long, Bitmap> galleryDataToBuild = new HashMap<Long, Bitmap>(workedProducts.size());

			String strLog = " :: Gonna try to build map < pdtId , pdtLink > :: initial map size [" + galleryDataToBuild.size() + "] ";
			
			String pdtId ;
			String iconUrl ; 

			
			for ( ProduitFrontend pdt : workedProducts) { 
				
				pdtId = pdt.getPdtId().toString();
				iconUrl = neededIcons.get(pdtId) ;
				
//				strLog += "\n\t Try add [" + pdtId + "][" + iconUrl + "] ... " ;
				
				
				try {
					
					Bitmap bmp = ((RunProduit) ctxt).provider.getBitmapFromGenericIconFolder (iconUrl) ;
					
//					strLog += " got bitmap ? [" 
//						+ (bmp == null ? " FAILED ! " : " TRUE ") 
//						+ "]"
//					;
					
//					Object o =
//					galleryDataToBuild.put(
					((RunProduit) ctxt).addEntryToGalleryData (
						Long.valueOf(pdtId),
						// Récupérer le bitmap dans le dossier
						bmp
					) ;
					
//					strLog += " Done. new map size [" + getGalleryData().size() + "]. Returned ? " + o ;
		
//					galleryDataToBuild.notifyAll();
					
				} catch (Exception e) {
					
//					strLog += " FAILED !! " ;
					
					e.printStackTrace();
				} finally {
					
					
					
				}
				
			}
			
			strLog += "\n ==> Finaly builded map is of size [" + ((RunProduit) ctxt).getGalleryData().size() + "]" ;
			
			Log.d(logHead, strLog);
			
			
			
			
		// DONT USE IT IF USE addEntry..()
			// Enregistrer les données dans l'activity
//			setGalleryData(galleryDataToBuild);			
			
			
			((RunProduit) ctxt).runOnUiThread(new Runnable() {	
				
				public void run() {					
					
					// Attacher l'adapter a la liste
					((RunProduit) ctxt).setGalleryAdapter(
						// Initier un nouvel adapter ...
							((RunProduit) ctxt).initGalleryAdapter(
							// ... avec les nouvelles données 
								((RunProduit) ctxt).getGalleryData()
						)
					);								
					
					
					((RunProduit) ctxt).attachGalleryAdapter();
				}
			});
			
			
			
			((RunProduit) ctxt).taskBuildAdapter_success = true ;
			
//	        return true;		
			
			
		} catch (Exception e) {
			((RunProduit) ctxt).taskBuildAdapter_success = false ;
			
			e.printStackTrace();
			
//			return false ;
	    }		
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		
		if (((RunProduit) ctxt).taskBuildAdapter_success == true) {
			
			
			
		}
					
//		// Clear the objet in Sync activity cause it's done.
//		((RunProduit) ctxt).SyncHistoryAsyncTask = null;
//							
//		// La tache est terminee
//		((RunProduit) ctxt).taskH_succeeded = true ;		
//		
//		// Tell Sync activity we are done. (update view if needed)
//		((RunProduit) ctxt).finishSyncHistory();
		
	}


	@Override
	protected void onCancelled() {
		
		Log.d(logHead, "BuildGalleryAdapter AsyncTask onCancelled()");
		
//		showProgress(false);
	}
}	