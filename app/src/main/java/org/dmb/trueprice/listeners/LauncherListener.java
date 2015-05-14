package org.dmb.trueprice.listeners;

import java.util.HashMap;

import org.dmb.trueprice.Launcher;
import org.dmb.trueprice.Login;
import org.dmb.trueprice.R;
import org.dmb.trueprice.RunListe;
import org.dmb.trueprice.Sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public final class LauncherListener implements View.OnClickListener {
	
	private Context initiator ;
//	Class<Login>
	
	private HashMap<Long, Integer> listItemClickCount = null;
	
	public LauncherListener( Context ctx ) {
		initiator = ctx ;
	}

	@Override
	public void onClick(View v) {
		
		Button button = (Button) v ;
		
		if (button != null) {
			
			Intent i  ;
			
			switch (button.getId()) {
			
				case R.id.launcher_btt_login:
					
					i = new Intent( initiator, Login.class);
					
					i.putExtra(Launcher.FLAG_SIGNED_IN, Launcher.getSigninStatus());
					
					((Activity) initiator).startActivityForResult(i, Launcher.REQID_LOGIN_RESULT);		
					
					break;
					
				case R.id.launcher_btt_sync:
					
					if (Launcher.getSigninStatus()) {
					
						i = new Intent(initiator, Sync.class);
						((Activity) initiator).startActivity(i);				
																
					} else {
						
						Toast.makeText(	initiator, initiator.getResources().getString
							(R.string.error_launcher_not_signed_in) , Toast.LENGTH_SHORT ).show();									
						
					}					
					
					
					break;
					
				case R.id.launcher_btt_run:
					
					/*	Cette section ne nécessite pas de connection internet ! */
					
//					if (Launcher.getSigninStatus()) {
//						
//						i = new Intent(initiator, RunListe.class);
//						((Activity) initiator).startActivity(i);				
//																
//					} else {
//						
//						Toast.makeText(	initiator, initiator.getResources().getString
//							(R.string.error_launcher_not_signed_in) , Toast.LENGTH_SHORT ).show();									
//						
//					}
					
					/*	L'ideal :	*/
					/*---------------
					 * -> Toast " Une bonne habitude : toujours synchroniser une liste avant de l'utiliser. ..."
					 *   + "... Certaines informations peuvent avoir changé sans que vous ne le sachiez :) " 
					 * --
					 * OU |
					 * --
					 * -> Mettre un flag/compteur dans le launcher d'entrée (1 fois suffit ? ) dans
					 *  l'Activity  ' Sync ' ET/OU sur au moins une SyncInitRequest (=> mets à jour les dates de modif)  
					 */
					
					if ( launchListIfClickTwice( button.getId() ) ) {											
						((Activity) initiator).startActivity(new Intent(initiator, RunListe.class));						
					} else {
						Toast.makeText(initiator, "Astuce: Avez-vous vérifié la synchronisation de vos listes ? ", Toast.LENGTH_LONG).show();
					}
					
					
			
					
					break;
					
				case R.id.launcher_btt_calc:
					
					Toast.makeText(	initiator, "This fonction is not yet working. Please come back later." , 
							Toast.LENGTH_SHORT	).show();		

					
					break;

			default:
				break;
			}
			
		} else {
			
			Log.e(this.getClass().getSimpleName(), "Could not identify event source : " + v.getId());
		}
		
	}
	
	private boolean launchListIfClickTwice (long id) {
		if (listItemClickCount == null) {listItemClickCount = new HashMap<Long, Integer>();} 
		if (listItemClickCount.containsKey(id) && listItemClickCount.get(id) == 1)  {
			listItemClickCount.remove(id);
			return true;
		} else {
			listItemClickCount.put(id, 1);
			return false ;
		}
	}			

}
