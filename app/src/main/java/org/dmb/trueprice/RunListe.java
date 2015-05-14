package org.dmb.trueprice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.dmb.trueprice.adapters.RunListStartAdapter;
import org.dmb.trueprice.listeners.RunListeListener;
import org.dmb.trueprice.objects.AvailableList;
import org.dmb.trueprice.objects.SyncHistory;
import org.dmb.trueprice.objects.SyncInitResponse;
import org.dmb.trueprice.objects.android.RunnableListFrontend;
import org.dmb.trueprice.objects.android.SynchronizedList;
import org.dmb.trueprice.tools.AppDataProvider;
import org.dmb.trueprice.utils.internal.GsonConverter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class RunListe extends MenuHandlerActionBarCompatAdapter {

	private String logHead = this.getClass().getSimpleName();

	public final static String 	FLAG_CHOOSEN_LIST		= "com.dmb.trueprice.android.FLAG_SIGNED_IN" ;
//	public final static int 	REQID_LOGIN_RESULT	= 0 ;	
	
	public static AppDataProvider provider ;
	
	
	private ArrayList<AvailableList> 	availableLists ; 
//	private ArrayList<AvailableResult> 	availableResults ;	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_run_liste);
		
		Log.d(logHead, "Creating [" + logHead +"] activity."
			+ "\n\t Wen need a provider ! ..."
		);
		
		try {
			provider = new AppDataProvider(RunListe.this, Launcher.currentUserEmail);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(logHead, "Buildding provider failed. ErrorMessage:[" + e.getMessage() +"] \n GONNA KILL THIS ACTIVITY TO GO BACK TO THE PARENT ONE");
			finish();
		}
		
		Log.d(logHead, "provider done.");
		
		RunListeListener lstn = new RunListeListener(RunListe.this);
					
		Button btn = (Button) findViewById(R.id.btn_run_liste_start);

		btn.setOnClickListener(lstn);
			
		btn = (Button) findViewById(R.id.btn_run_liste_review);

		btn.setOnClickListener(lstn);		

		btn = (Button) findViewById(R.id.btn_run_liste_sync);

		btn.setOnClickListener(lstn);
		
		// Default direction
		lstn.enableStartSetDirection(true);
		
		// Recupérer les données du provier
		// Construire les vues
		if (provider != null) {
			
			showListView();
			
		}
		
	}
	
	private SyncInitResponse 	lastInitResponse ;	
	private SyncInitResponse getLastInitResponse() {
		return lastInitResponse;
	}
	public void setLastInitResponse(SyncInitResponse lastInitResponse) {
		this.lastInitResponse = lastInitResponse;
	}
	
	
	
	
	public void showListView() {
		
//		resetInitTaskFlags();
//		resetGetterTaskFlags();
		
//		clearSelectedIds();
		
		String direction = RunListeListener.getDirection() ;
		
		Log.i(logHead, "Asked to show run direction : \t buildListView(" + direction +")");
		

		
		
		boolean passed = false ;
		
		try {
//			Log.w(logHead, "");
			buildListView(direction);
			
			passed = true ;
		} catch (IOException e) {
			
			Log.w(logHead, "Could not build listView [" + direction + "] because file was not found !"  
				+ "\t" + e.getMessage());
			e.printStackTrace();			
			
		}
		
		ListView liste ;
		if (direction == RunListeListener.ATT_DIRECTION_START) {	
			liste = (ListView) findViewById(R.id.run_listview_start_continue);	
		} else {
			liste = (ListView) findViewById(R.id.run_listview_review);	
		}
		liste.refreshDrawableState();
		
	}
	
		
	
	
	
	/**
	 * 	Views Methods
	 * @throws IOException 
	 */
	private void buildListView(String syncDirection) throws IOException {
		
		ListView liste ;										
		
		switch (syncDirection) {
		
		case RunListeListener.ATT_DIRECTION_START:
			
			Log.i(logHead, "buildListView(DOWN)");
			liste = (ListView) findViewById(R.id.run_listview_start_continue);
			buildStartListe(liste);
			
			break;

		case RunListeListener.ATT_DIRECTION_REVIEW:

			Log.i(logHead, "buildListView(UP)");	
			liste = (ListView) findViewById(R.id.run_listview_review);
			
			buildReviewListe(liste);
			
//			availableResults 	= new ArrayList<AvailableResult>();
//			ownedLists 	= new ArrayList<OwnedList>();
//						
//			/* Si pas encore de requete 
//			 * (Ex.: pas encore de données téléchargées ou de liste finie)
//			*	OU
//			*  Si les données sur le device ont changé depuis la derniere requete
//			*  (Ex.: une liste terminée/téléchargée en plus/moins)
//			*/
//			// Construire la requete à partir des données
//			String json = addExampleRequest();
//			
//			// Sinon envoyer une requete vide, on recoit quand meme une reponse
//			buildInitRequest(json);
//			
//			//Si
//			availableResults = getLastRequest().getAvailableResults();
//			ownedLists = getLastRequest().getOwnedLists() ;
			
			break;
		}
		


				
	}	
	
	
	/**	Build liste fron Sync Init Request & Init/Getter Response(s) if possible	**/
	
	private void buildStartListe(ListView liste) throws IOException {
		
		// Les items finaux affichés
		ArrayList<RunnableListFrontend> listesItems = null ; 
		
		// Les données préalables nécessaires ; reçues après synchronisation (SyncInit)
		// Nécessaire pour le compte de produits et les label (évite de doubler les données)
		availableLists = new ArrayList<AvailableList>();
		
		// Au demarrage de l'activité, aucun fichier n'a été lu, 
		// il faut vérifier si on a des données ...
		// Si pas encore de données pretes a afficher
		if (getLastInitResponse() == null) {
			
			Log.i(logHead, "getLastResponse == null => check if there is a file with data");
			
			// On verifie que le fichier du user existe, sinon il n'y a rien a lire ...
			if (provider.checkUserFileExist(AppDataProvider.SYNC_INIT_RESP_FILENAME)) {
				
				Log.i(logHead, "The file [" + AppDataProvider.SYNC_INIT_RESP_FILENAME +"] exist. Gonna parse data");
				// Enregistrer les nouvelles données dans l'activité
				setLastInitResponse	(
					// Convertir à partir du fichier
					GsonConverter.fromJsonInitResponse	(
						// Dans le dossier du User		// SyncInitResponse.json	
						provider.readFileInUserFolder(AppDataProvider.SYNC_INIT_RESP_FILENAME)
					)
				);
				
				Log.i(logHead, "setLastResponse() has been made from file. Still null ? [" 
				+ (getLastInitResponse() == null ? "TRUE" : "FALSE")  + "]");
				
			}
			
		
		} 
		
		// Historique de synchronisation
		SyncHistory history = null ;
		
		
		// Si on a pu obtenir des données a traiter
		if (getLastInitResponse() != null && getLastInitResponse().getAvailableLists() != null) {
			
			// Recupérer les listes dans SyncInit
			availableLists = getLastInitResponse().getAvailableLists();
			
			listesItems = new ArrayList<RunnableListFrontend>();		

			// Récupérer l'historuque de synchro
			history = GsonConverter.fromJsonSyncHistory(
				provider.getUserSyncHistoryContent()
			);			

			if (history != null && history.getSyncdLists() != null) {				
				
//				listesItems.addAll(transformListeToFrontend(availableLists, "false")) ;					
				
				// Récupérer uniquement les listes synchronisées
				// Les transformer en RunnableListe
				// Les retenir pour affichage
				for (int i = 0 ; i < history.getSyncdLists().size() ; i ++ ) {				
					SynchronizedList 	syListe = history.getSyncdLists().get(i);
					AvailableList 		avListe = getLastInitResponse().getListByID(syListe.getListeId());
					
					listesItems.add(transformListeToFrontend(avListe));
				}				
				
			}
			
		}
		// Si on a pas de données, il faut faire un sync ou mettre des exemples ...
		else {
			// setlastResponse ( addExampleResponse()  ) ; ...
				// OU
			// Add new TextView("Veuillez proceder a une synchronisation")
		}		
		
		
		
		RunListStartAdapter adapter = new RunListStartAdapter(RunListe.this, listesItems);
		
		// Now done in the constructor of the adapter()
//		adapter.setData(listesItems);


		
		if (history != null) {
			adapter.updateSynchronizedList(history) ;
		}
		
		try {
			liste.setAdapter(adapter);			
			Log.i(this.getClass().getSimpleName(), "Adapter setted to liste.");
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), " Error append setting adpater : " + e.getMessage());
			e.printStackTrace();
		}
	
		
		
		
		
		
		liste.setClickable(true);
		
/*
 * 	GIVE LISTENER FOR ROWS BUT NOT INSIDE THE ITEMS 		
 */
		liste.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				RunnableListFrontend liste = (RunnableListFrontend) parent.getItemAtPosition(position);
				
				// Do stuff on liste 
				
//				RunnableListFrontendHolder holder = (RunnableListFrontendHolder) view.getTag();
				
				// Do stuff on holder 
				
//				Log.d(logHead, "Click on view at pos[" + position +"] & id["+ id + "]" + "\t Pressed  " + listItemClickCount.get(id)  + "  times !" );
				
				
				if (launchListIfClickTwice(id)) {
//					Toast.makeText(getApplicationContext(), "You want to run the list with :\n\t ID[" 
//							+ liste.getListeId() + "]\n\t label : " + liste.getListeLabel(),
//					Toast.LENGTH_LONG).show();
					
					Intent i = new Intent(RunListe.this, RunListeStart.class);
					
					i.putExtra(RunListeStart.FLAG_CHOSEN_LIST_ID, id);
					
//					((Activity) initiator).startActivityForResult(i, Launcher.REQID_LOGIN_RESULT);		
					
					startActivity(i);						
					
				} else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.one_more_time) 
							+ " (\"" + liste.getListeLabel() + "\")",
					Toast.LENGTH_LONG).show();
				}
				
				
			}
			
		});
				
	}	
	
	private HashMap<Long, Integer> listItemClickCount = new HashMap<Long, Integer>(); 
	
	private boolean launchListIfClickTwice(long id){
		if (listItemClickCount.containsKey(id) && listItemClickCount.get(id) == 1)  {
			listItemClickCount.remove(id);
			return true;
		} else {
			listItemClickCount.put(id, 1);
			return false ;
		}
	}
	
	
	private void buildReviewListe(ListView liste)  throws IOException {
		
//		ArrayList<AvailableResultFrontend> resultsItems = null ;
//		
//		availableResults = new ArrayList<AvailableResult>();
//		
//		
//		// Au demarrage de l'activité, aucun fichier n'a été lu, 
//		// il faut vérifier si on a des données ...
//		// Si pas encore de données pretes a afficher
//		if (getLastInitRequest() == null) {
//			
//			Log.i(logHead, "getLastRequest == null => check if there is a file with data");
//			
//			// On verifie que le fichier du user existe, sinon il n'y a rien a lire ...
//			if (provider.checkUserFileExist(AppDataProvider.SYNC_INIT_REQ_FILENAME)) {
//				
//				Log.i(logHead, "The file [" + AppDataProvider.SYNC_INIT_REQ_FILENAME +"] exist. Gonna parse data");
//				
//				setLastInitRequest	(
//					GsonConverter.fromJsonInitRequest (
//						provider.readFileInUserFolder(AppDataProvider.SYNC_INIT_REQ_FILENAME)
//					)
//				);
//				
//				Log.i(logHead, "setLastRequest() has been made from file. Still null ? [" 
//				+ (getLastInitRequest() == null ? "TRUE" : "FALSE")  + "]");
//				
//			}
//			
//		
//		} 
//		
//		// Si on a pas de données, il faut faire un sync ou mettre des exemples ...
//		else {
//
//			// setlastRequest ( addExampleRequest()  ) ; ...
//				// OU
//			// Add new TextView("Pour avoir des resultats a envoyer, il faut aller " 
//			//  + "a l'étape 2 d'au moins un des produits d'une de vos listes," 
//			//  + " récupérables dans la section DOWN accessible via le bouton ci-dessus.");
//			
//			
////			String json = addExampleRequest();
////			
////			try {
////				buildInitRequest(json);
////			} catch (Exception e) {
////				Log.e(logHead, "error with json request [" + e.getMessage() +"]");			
////			}
////			
//		}		
//		
//
//		
//		if (getLastInitRequest() != null && getLastInitRequest().getAvailableResults() != null) {
//			
//			availableResults = getLastInitRequest().getAvailableResults();
//			
//			Log.i(logHead, "Build InitReq have set [" + availableResults.size() +"] avResults \n" + availableResults.get(0).toString());
//			
//			resultsItems = new ArrayList<AvailableResultFrontend>();		
//			
//			resultsItems.addAll(transformResultToFrontend(availableResults, "false")) ;			
//		}
//		
//		// GOOD ?????????
//		SyncListUpAdapter adapter = new SyncListUpAdapter(Sync.this, resultsItems);
//		// data = readFromJson();
//				
//		try {
//			liste.setAdapter(adapter);
//			
//			Log.i(this.getClass().getSimpleName(), "Adapter Up setted to liste.");
//		} catch (Exception e) {
//			Log.e(this.getClass().getSimpleName(), " Error append setting adpater : " + e.getMessage());
//			e.printStackTrace();
//		}
//			
		
	}	

		
	
	/**
	 * transformListeToFrontend 
	 * @param avListe
	 * @return
	 */
	private RunnableListFrontend transformListeToFrontend (AvailableList avListe) {
		
		return new RunnableListFrontend (
			avListe.getListeId(), avListe.getListeLabel(),
			avListe.getDate(), avListe.getPdtCount()
		);

	}	
//	/**
//	 * transformListeToFrontend
//	 * @param listes
//	 * @param booleanDefaultValue Send 'true' or 'TRUE' (case insensitive comparison), otherwise it's false. 
//	 * 		If null, uses  AvailableListFrontend constructor without boolean param
//	 * @return
//	 */
//	private ArrayList<RunnableListFrontend> transformListeToFrontend (ArrayList<AvailableList> listes) {
//		
//		ArrayList<RunnableListFrontend> outListes = new ArrayList<RunnableListFrontend>();
//		
//		for (AvailableList listeIn : listes) {
//			
//			RunnableListFrontend listeOut = new RunnableListFrontend (
//				listeIn.getListeId(), listeIn.getListeLabel(),
//				listeIn.getDate(), listeIn.getPdtCount()
//			);
//
//			outListes.add(listeOut);
//			
//		}
//		
//		return outListes;
//		
//	}
	
}
