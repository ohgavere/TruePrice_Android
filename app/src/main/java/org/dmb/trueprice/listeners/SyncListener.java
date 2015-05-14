package org.dmb.trueprice.listeners;

import java.util.HashSet;

import org.dmb.trueprice.R;
import org.dmb.trueprice.Sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public final class SyncListener implements View.OnClickListener {
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	private Context initiator ;
	
	public final static String ATT_DIRECTION_UPLOAD = "UP" ;
	public final static String ATT_DIRECTION_DOWNLOAD = "DOWN" ;
	private static String syncDirection ;
	static {
		syncDirection = ATT_DIRECTION_DOWNLOAD ;					
	}
	
	public SyncListener( Context ctx ) {
		initiator = ctx ;
	}

	@Override
	public void onClick(View v) {
		
		Button button = (Button) v ;
		
		if (button != null) {
			
			Intent i  ;
			
			switch (button.getId()) {
			
			
			case R.id.btn_sync_synchronize	:
				
				// Faire un SyncInit	req + resp
				// == mettre a jour l'affichage des listes (une liste modifiée doit être re-telechargee)							
				
				((Sync) initiator).attempSyncInit();
				
				break;
				
			case R.id.btn_sync_uplist	:
				// Afficher listView des resultats de liste uploadables
					showUploadList();
					
				break;
				
			case R.id.btn_sync_downlist	:
				
				// Afficher listView des listes telechargeables
					showDownloadList();
					
				break ;
				
			case R.id.btn_sync_icons	:
				
				// Afficher listView des listes telechargeables
					((Sync) initiator).attemptSyncIcons();
					
				break ;
				
			case R.id.btn_sync_proceed	:
				
				/*
				 * Sync Getter OU Sync Setter selon direction UP / DOWN
				 */
				
				// Recuperer la direction
				// Recuperer les ID selectionnés
				// Construire Getter/Setter
				// proceed(Getter/Setter);
					
					// Verifier quel bouton est activé pour savoir si on Upload ou Download
					
					Button btnUpload = (Button)((Activity) initiator).findViewById(R.id.btn_sync_uplist);
					
					// Recuperer la liste pour connaitre les listes/resultats choiis

					ListView liste = (ListView) ((Activity) initiator).findViewById(R.id.list) ;
					
					// Si btnUpload est activé, on DOWNLOAD
					
					if (btnUpload.isEnabled()) {
						
						HashSet<Long> idSet = Sync.getSelectedIds();
						
						/*
						 * Launch SyncGetter Request
						 */
						
						if (idSet != null) {
							
							String IDs = "[" ;
							for(Long id : idSet) {
								IDs += Long.toString(id) + ", ";
							}											
							IDs += "]" ;
							
							Toast.makeText(initiator, "You asked to download listes with IDs \t" + IDs, Toast.LENGTH_SHORT).show();
							
							// lancer la requete
							((Sync) initiator).attempSyncGetter();
						}
						
					}
					
					// Sinon UPLOAD
					else {
						
						String IDs = "[" ;
						
						for(Long id : Sync.getSelectedIds()) {
							IDs += Long.toString(id) + ", ";
						}											
						
						IDs += "]" ;
						
						Toast.makeText(initiator, "You asked to upload listes results with IDs \t" +  IDs, Toast.LENGTH_SHORT).show();						
						
					}
					
					
					
//					i = new Intent( initiator, RunPdtInit.class);
//					
//					i.putExtra(Launcher.FLAG_SIGNED_IN, Launcher.getSigninStatus());
					
//					((Activity) initiator).startActivityForResult(i, Launcher.REQID_LOGIN_RESULT);		
					
//					((Activity) initiator).startActivity(i);
					
					
					/*
					 * Launch SyncSetter Request
					 */					
					
					break;

			default:
				break;
			}
			
		} else {
			
			Log.e(logHead, "Could not identify event source : " + v.getId());
		}
		
	}
	
	public void showUploadList() {		
		
		// On prend le bouton d'upload
		Button btn = (Button) ((Sync) initiator).findViewById(R.id.btn_sync_uplist);
		// On le desactive
		btn.setEnabled(false);
		
		// On prend le bouton de download
		btn = (Button) ((Sync) initiator).findViewById(R.id.btn_sync_downlist);
		// On l active
		btn.setEnabled(true);
		
		setDirectionUpload();
		((Sync) initiator).showListView();
		
	}
	
	public void showDownloadList() {		
		
		// On prend le bouton de download
		Button btn = (Button) ((Sync) initiator).findViewById(R.id.btn_sync_downlist);
		// On le desactive
		btn.setEnabled(false);
		
		// On prend le bouton d'upload
		btn = (Button) ((Sync) initiator).findViewById(R.id.btn_sync_uplist);
		// On l active
		btn.setEnabled(true);
		
		setDirectionDownload();
		((Sync) initiator).showListView();
				
	}
	
	private void setDirectionUpload() {
		SyncListener.syncDirection = ATT_DIRECTION_UPLOAD ;
	}
	private void setDirectionDownload() {
		SyncListener.syncDirection = ATT_DIRECTION_DOWNLOAD ;
	}		
	public static String getDirection() {		
		return syncDirection;		
	}

//	public void toggleDirectionAndViews() {		
//		if (getDirection() == ATT_DIRECTION_DOWNLOAD) 
//			setDirectionUpload();
//		else setDirectionDownload();
//		((Sync) initiator).showListView();
//	}

	public void toggleProceedButton(){		
		Button btt = (Button) ((Sync) initiator)
				.findViewById(R.id.btn_sync_proceed);
		
		if (btt.isEnabled())	enableProceed(false, btt);
		else 					enableProceed(true, btt);
	}

	/**
	 * Button can be null
	 * @param enabled
	 * @param btt
	 */
	public void enableProceed(boolean enabled, Button btt) {
		if (btt==null) {
			btt = (Button) ((Sync) initiator).findViewById(R.id.btn_sync_proceed);
		}
		btt.setEnabled(enabled);			
	}
	
	public void enableIconSync(boolean enabled, int iconCount) {
		
		Log.d(logHead, "Enable Sync Icon ? [" + enabled +"]");
		
		LinearLayout 	lyt = (LinearLayout)
			((Sync) initiator).findViewById(R.id.sync_icon_task);
		
		Button 			btt = (Button) 		
			((Sync) initiator).findViewById(R.id.btn_sync_icons);
		
		if (enabled == true) {
			lyt.setVisibility(View.VISIBLE);
			btt.setText("Synchroniser " + iconCount + " icones manquants");
			Log.d(logHead, "Enable done. Lyt null ? [" + (lyt == null ? "TRUE" : "FALSE" ) +"]"
				+ "\t\tBtt null ? [" + (btt == null ? "TRUE" : "FALSE" ) +"]"
			);
		} else {
			lyt.setVisibility(View.GONE);
		}
	}
	
}
