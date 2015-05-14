package org.dmb.trueprice.tools;

import java.util.ArrayList;
import java.util.HashMap;

import org.dmb.trueprice.objects.ListeDetailFrontend;
import org.dmb.trueprice.objects.SyncGetterRequest;
import org.dmb.trueprice.objects.SyncGetterResponse;

import android.util.Log;

public class SyncGetterVerifier {
	
	public static final String ERR_EMPTY_LIST = "emptyListAsked";
	public static final String ERR_ICON_MISSING = "iconMissing" ;
	
	SyncGetterRequest 	request ;
	SyncGetterResponse 	response ;
//	SyncHistory 		history ;
	HashMap<String, String> errors = new HashMap<String, String>();
	
	ArrayList<String> synchrnizedIconsUNames ;
	ArrayList<String> synchrnizedIconsGNames ;
	
	public SyncGetterVerifier(SyncGetterRequest request, SyncGetterResponse response,/*SyncHistory history*/
			ArrayList<String> actualUserIconFolderFilesNames, ArrayList<String> filesNamesInGenericIconFolder
	) {
		this.request = request ;
		this.response = response;
//		this.history = history;
		this.synchrnizedIconsUNames = actualUserIconFolderFilesNames ;
		this.synchrnizedIconsGNames = filesNamesInGenericIconFolder ;
	}
	
	public HashMap<String, String> checkResponse(/*ArrayList<String> actualFolderFilesNames*/) {
		
		// Verifier le nombre de listes recues
		checkIntegrity();
		
		// Pour chaque liste, verifier le nbre de produits prevus et recus 
			// ( juste au cas où : check supplémentaire de l'intégrité du contenu)
		
		
		// Dans la map produit<->icon recue, verifier chaque image 
		//  puis prevenir qu'il faut les telecharger si certaines manquent 
		checkIcons(/*actualFolderFilesNames*/);
		
		return errors;
		
	}

	
	/**
	 * Check if all asked lists have been received
	 * @return
	 */
	private  SyncGetterVerifier checkIntegrity() {
		if (request.getAskedLists().size() != response.getProvidedLists().size()) {
			for (Long askedId : request.getAskedLists()) {
				boolean provided = false ;
				for (ListeDetailFrontend liste : response.getProvidedLists() ) {
					if (liste.getLstId() == askedId.intValue()) { provided = true ;}
				}
				if (provided == false) { reportMissingListe(askedId) ; }
			}
		}
		return this;
	}
	
	
	/**
	 * For each product we need an icon. This method check for each needed icon if it exist
	 * in generic or user own icon folder depending on the received URL for that product.
	 * @param actualFolderFilesNames The
	 */
	private void checkIcons(/*ArrayList<String> actualFolderFilesNames*/) {
		
		// Récupérer la map des icones necessaire avec les listes telechargees
		ArrayList<String> iconsNeeded = new ArrayList<String>(); 
		iconsNeeded.addAll( response.getMapProduitIcon().values() );
		
//		ArrayList<String> iconsInFolder = actualFolderFilesNames ;
		
		ArrayList<String> iconsSynchronized = new ArrayList<String>() ; 
//		if (history != null ) iconsSynchronized = history.getSynchronizedIcons() ;
		
		if (synchrnizedIconsGNames != null) {iconsSynchronized.addAll(synchrnizedIconsGNames); }
		else {	Log.w("SyncGetterVerifier", "Icon names in GENERIC folder is null !");	}
		
		if (synchrnizedIconsUNames != null) {iconsSynchronized.addAll(synchrnizedIconsUNames); }
		else {	Log.w("SyncGetterVerifier", "Icon names in USER folder is null !");	}

		
		
		// Si folder ou synchronized vide => tout telecharger
		if (iconsSynchronized.size() == 0) {
			
			Log.w("SyncGetterVerifier", "All icons are missing ! (" + iconsSynchronized.size() + "/" + iconsNeeded.size() +")");
			for (String missing : iconsNeeded) { 
				reportMissingIcon(missing, getProductIdFromIcon(missing));
			}

			// sinon check individuel
		} else {
			for (int i = 0 ; i < iconsNeeded.size() ; i++) {

				String iconNeededFullURL 	= iconsNeeded.get(i);
//				String iconInFolder = iconsInFolder.get(i);
//				String iconSynchro 	= iconSynchronized.get(i);
				
				String iconNType = "GENERIC"; // "USER"	//=> (G||U) only // 
				
				if ( ! iconNeededFullURL.contains("generic")) {
					
					iconNType = "USER" ;
					
					if (! iconNeededFullURL.contains("@")) {
						Log.w("SyncGetterVerifier", "checkIcons() WARNING : Icon [" 
							+ iconNeededFullURL + "] \n\t IS NOT GENERIC (?->check with URL) " 
							+ "BUT it seems it does not contain user namespace !"
						);
					}
					
				}
				
				String iconNsimpleName = iconNeededFullURL.substring(iconNeededFullURL.lastIndexOf("/") +1);
				
				String strLog = "Is icon [" + iconNsimpleName +"] in folder ?" 
						;				
				// Si dans le dossier et synchronisé et dans needed : TOUT OK
				// sinon ...
				// Si manque quelque part 
				if ( !  iconsSynchronized.contains(iconNsimpleName) ) {
					
					Log.w("SyncGetterVerifier", strLog + "[" + "FALSE" + "]");
					
//					if			// Dans le dossier mais pas enregistré comme synchro
//					(iconsSynchronized.contains(iconNsimpleName) && !iconsSynchronized.contains(iconNeeded) ) {
//						iconsSynchronized.add(iconNeeded);
//						
//					} else if 	// Enregistré comme synchro mais pas dans le dossier
//					(!iconsInFolder.contains(iconNeeded) && iconsSynchronized.contains(iconNeeded)) {
//						iconsSynchronized.remove(i);
//						reportMissingIcon(iconNeeded);							
//					}
					
					reportMissingIcon(iconNeededFullURL, getProductIdFromIcon(iconNeededFullURL));
					Log.d("SyncGetterVerifier", "Reported missing Icon[" 
					+ iconNsimpleName +"] \t type:[" + iconNType +"] \n\t"
					+ "FullURL:[" + iconNeededFullURL +"]");
					
				} else {
					Log.w("SyncGetterVerifier", strLog + "[" + "TRUE" + "]." 
							+ " Icon already synchronized [" + iconNsimpleName 
					 	+"] \t type:[" + iconNType +"]"
					 );
				}
			}			
		}
		
	}
	
	private Long getProductIdFromIcon(String iconFullUrl) {
		for (Long key : response.getMapProduitIcon().keySet()) {
			if (response.getMapProduitIcon()
				.get(key).equals(iconFullUrl)
			) {
				return key ;
			}
		}
		return null ;
	}
	
	
	private void reportMissingListe(Long askedId) {
		
		String str =  errors.get( ERR_EMPTY_LIST)  ;
		if (str == null) {
			str = new String(Long.toString(askedId)); 
		} else {
			str +=   ("," + Long.toString(askedId)) ;
		} 		 
		errors.put( ERR_EMPTY_LIST , str ) ;
	}
	private void reportMissingIcon(String iconName, Long productId) {
		String str = errors.get( ERR_ICON_MISSING)  ;
		if (str == null) {
			str = new String(productId + "-" + iconName); 
		} else {
			str +=   ("," + productId + "-" + iconName) ;
		} 
		errors.put( ERR_ICON_MISSING , str ) ;
	}

}
