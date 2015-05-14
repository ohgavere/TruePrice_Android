package org.dmb.trueprice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.dmb.trueprice.adapters.SyncListDownAdapter;
import org.dmb.trueprice.adapters.SyncListUpAdapter;
import org.dmb.trueprice.connector.SyncGetterConnector;
import org.dmb.trueprice.connector.SyncInitConnector;
//import org.dmb.trueprice.adapters.SyncListDownAdapter.ViewHolder;
import org.dmb.trueprice.listeners.SyncListener;
import org.dmb.trueprice.objects.AvailableList;
import org.dmb.trueprice.objects.AvailableResult;
import org.dmb.trueprice.objects.ListeDetailFrontend;
import org.dmb.trueprice.objects.OwnedList;
import org.dmb.trueprice.objects.SyncGetterRequest;
import org.dmb.trueprice.objects.SyncGetterResponse;
import org.dmb.trueprice.objects.SyncHistory;
import org.dmb.trueprice.objects.SyncInitRequest;
import org.dmb.trueprice.objects.SyncInitResponse;
import org.dmb.trueprice.objects.android.AvailableListFrontend;
import org.dmb.trueprice.objects.android.AvailableListFrontendHolder;
import org.dmb.trueprice.objects.android.AvailableResultFrontend;
import org.dmb.trueprice.objects.android.SynchronizedList;
import org.dmb.trueprice.tasks.SyncGetterTask;
import org.dmb.trueprice.tasks.SyncHistoryTask;
import org.dmb.trueprice.tasks.SyncIconsTask;
import org.dmb.trueprice.tasks.SyncInitTask;
import org.dmb.trueprice.tools.AppDataProvider;
import org.dmb.trueprice.utils.internal.GsonConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Sync extends MenuHandlerActionBarCompatAdapter {	
	
	private static HashSet<Long> selectedIds = new HashSet<Long>();	
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	private static SyncListener listener ;
	
	private SyncInitRequest 	lastInitRequest ;
	private SyncInitResponse 	lastInitResponse ;
	
	private SyncGetterRequest 	lastGetterRequest ;
	private SyncGetterResponse 	lastGetterResponse ;
	
	private ArrayList<AvailableList> 	availableLists ; 
	private ArrayList<AvailableResult> 	availableResults ;
	private ArrayList<OwnedList> 		ownedLists ;
	
//	private ArrayList<AvailableListFrontend> availableListsFtd ; 
	
	public AppDataProvider provider ;
	
	public boolean taskI_ran_once = false;
	public boolean taskI_succeeded = false ;
		
	public boolean taskG_ran_once = false;
	public boolean taskG_succeeded = false ;	
	
	public boolean taskIcons_ran_once = false;
	public boolean taskIcons_succeeded = false ;
		
	public boolean taskH_ran_once = false;
	public boolean taskH_succeeded = false ;	

	/**
	 * 	ACTIVITY Methods
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync);		

		boolean canGetUserFolder = false ;
		String error = null ;
		
		try {
			
			Log.d(logHead, "Gonna build data Provider with user mail from launcher == [" + Launcher.currentUserEmail + "]");
			
			provider = new AppDataProvider(Sync.this, Launcher.currentUserEmail);		
//			provider.doExample();
			
			canGetUserFolder = true ;
								
		} catch (Exception e) {
			error = e.getMessage() != null ? e.getMessage() : e.getLocalizedMessage() ;
			Log.e(logHead, "Impossible to get AppDataProvider, probably for user folder." 
					+ " The error message is [" + e.getMessage() +"]"
					+ "\n\t The message ==> \t" + "PLEASE CONTACT SUPPORT" +"\n"
					+ "\t will be displayed to user. The app can't work without user specific folder."
					);
			e.printStackTrace();
		}
		
		
		if (canGetUserFolder == true) {
			
			listener = new SyncListener(Sync.this);
			
			Button btt = (Button) findViewById(R.id.btn_sync_synchronize);
			btt.setOnClickListener(listener);
			
			btt = (Button) findViewById(R.id.btn_sync_proceed);
			btt.setOnClickListener(listener);
			
			btt = (Button) findViewById(R.id.btn_sync_uplist);
			btt.setOnClickListener(listener);
			
			btt = (Button) findViewById(R.id.btn_sync_downlist);
			btt.setOnClickListener(listener);

			btt = (Button) findViewById(R.id.btn_sync_icons);
			btt.setOnClickListener(listener);			
			
			
			listener.enableIconSync(false, 0);
			
			
			// Down par defaut
//			buildListView(listener.getDirection());	
//			buildListView(listener.ATT_DIRECTION_UPLOAD);
//			listener.showUploadList();
			
			// This will make the buttons enabled in the right way, 
			// and then it calls Sync.ShowListView()
			listener.showDownloadList();
						
		} else {
			
 
			
			
		}
		
		
		
		// instantiate it within the onCreate method
		mProgressDialog = new ProgressDialog(Sync.this);
		mProgressDialog.setMessage("Telechargement des icones manquants");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		
		
	}
	

	
	
	/**
	 * 	SELECTED IDs
	 */
	
	public static HashSet<Long> getSelectedIds() {
		return selectedIds;
	}
	
	public static int getSelectedIdsCount() {
		return selectedIds.size();
	}
	
	public static void addSelectedId(Long id) {
		selectedIds.add(id);
		checkSelectedCount();
	}
	
	public static void removeSelectedId(Long id) {
		selectedIds.remove(id);
		checkSelectedCount();
	}	
	private static void clearSelectedIds(){
		selectedIds.clear();
		listener.enableProceed(false, null);
	}	
	private static void checkSelectedCount(){
		if (getSelectedIdsCount() > 0) {
			listener.enableProceed(true, null);
		} else {
			listener.enableProceed(false, null);
		}
	}
	
	/**
	 * 	Views Methods
	 * @throws IOException 
	 */
	private void buildListView(String syncDirection) throws IOException {
		
		ListView liste = (ListView) findViewById(R.id.list);										
		
		switch (syncDirection) {
		
		case "DOWN":
			
			Log.i(logHead, "buildListView(DOWN)");
			
			buildDownListe(liste);
			
			break;

		case "UP":

			Log.i(logHead, "buildListView(UP)");	
			
			buildUpListe(liste);
			
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
	
	public void showListView() {
		
//		resetInitTaskFlags();
//		resetGetterTaskFlags();
		
		clearSelectedIds();
		
		String direction = SyncListener.getDirection() ;
		
		Log.i(logHead, "Asked to show sync direction : \t buildListView(" + direction +")");
		
//		if (direction == SyncListener.ATT_DIRECTION_DOWNLOAD) {
//			
//			buildListView(SyncListener.ATT_DIRECTION_UPLOAD);
//		} else {
//			buildListView(SyncListener.ATT_DIRECTION_DOWNLOAD);
//		}
		
		
		boolean passed = false ;
		
		try {
//			Log.w(logHead, "");
			buildListView(direction);
			
			passed = true ;
		} catch (IOException e) {
			
			Log.w(logHead, "Could not toggleViews because file was not found !"  
				+ "\t" + e.getMessage() +"\n Re-Building actual view ... ");
			e.printStackTrace();			
			
			try {
				int checkDir = direction.compareToIgnoreCase(SyncListener.ATT_DIRECTION_DOWNLOAD);
				if (checkDir==0) {
					buildListView(SyncListener.ATT_DIRECTION_UPLOAD); 	
				} else {
					buildListView(SyncListener.ATT_DIRECTION_DOWNLOAD);
				}		
				passed = true ;
			} catch (IOException  e2) {
				Log.w(logHead, "Could not go back ... \t" + e2.getMessage() 
					+ "\n ... fall back to actual state ...");
				e2.printStackTrace();
			
			} finally {
				
				if (passed == true) {
					resetInitTaskFlags();
					resetGetterTaskFlags();
				}
			}
		}
		
		ListView liste = (ListView) findViewById(R.id.list);	
		liste.refreshDrawableState();
	}
	
		
		
	
	/**
	 *	WebApp Objects for request/response
	 */
	
	/** Backend basic Objects	**/
	
	private SyncInitResponse getLastInitResponse() {
		return lastInitResponse;
	}
	private SyncInitRequest getLastInitRequest() {
		return lastInitRequest;
	}
	private SyncGetterRequest getLastGetterRequest() {
		return lastGetterRequest;
	}
	private SyncGetterResponse getLastGetterResponse() {
		return lastGetterResponse;
	}
	
	public void setLastInitResponse(SyncInitResponse response) {
		lastInitResponse = response ;
//		if (this.lastResponse != null) {
//			Log.i(logHead, "Gonna try to write changes INTO SyncInitResponse file");
//			provider.writeUserInitResponseContent(GsonConverter.toJson(lastResponse));
//			Log.i(logHead, "OK.\t...\tGonna try to update listeView adapter data");
//			updateAvailableListsFromResponse();
//		}
	}	
	public void setLastInitRequest(SyncInitRequest request) {
		lastInitRequest = request ;
//		if (this.lastRequest != null) {
//			Log.i(logHead, "Gonna try to write changes for SyncInitRequest");
//			provider.writeUserInitRequestContent(GsonConverter.toJson(lastRequest));
//		}		
	}
	public void setLastGetterRequest(SyncGetterRequest lastGetterRequest) {
		this.lastGetterRequest = lastGetterRequest;
	}
	public void setLastGetterResponse(SyncGetterResponse lastGetterResponse) {
		this.lastGetterResponse = lastGetterResponse;
	}
	
	/**	Frontend advanced objects	**/
	// Download liste
	private void updateAvailableListsFromInitResponse() {		
		this.availableLists = getLastInitResponse().getAvailableLists() ;
	}	
	// Upload liste
	private void updateOwnedResultsFromInitRequest () {
		this.availableResults = getLastInitRequest().getAvailableResults();
		this.ownedLists = getLastInitRequest().getOwnedLists();
	}	
	
	// Requete Sync Init
	private void buildInitRequestFromJson (String json){		
		SyncInitRequest request = GsonConverter.fromJsonInitRequest(json);
		setLastInitRequest(request);
		if (request != null) {	
			updateOwnedResultsFromInitRequest();
		} else {
			Log.e(logHead, "Could not read Data. New user or read error ? -> see previous log hints." 
//				+ " Really add examples ?"
			) ;
		}		
	}
	
	private void buildGetterRequest () {	
		
		if (getSelectedIdsCount() > 0) {
			SyncGetterRequest request = new SyncGetterRequest(getSelectedIds());
			setLastGetterRequest(request);	
			
//			updateAvailableListsFromInitResponse();
			Log.i(logHead, "Sync GET object OK. : " + getLastGetterRequest().toString());
		} else {
			Log.e(logHead, "Could not create Object SyncGetterRequest !!" ) ;
		}
	}
	
	
	/**	Frontend advanced objects	**/
	
	/**
	 * transformListeToFrontend
	 * @param listes
	 * @param booleanDefaultValue Send 'true' or 'TRUE' (case insensitive comparison), otherwise it's false. 
	 * 		If null, uses  AvailableListFrontend constructor without boolean param
	 * @return
	 */
	private ArrayList<AvailableListFrontend> transformListeToFrontend (ArrayList<AvailableList> listes, String booleanDefaultValue) {
		
		ArrayList<AvailableListFrontend> outListes = new ArrayList<AvailableListFrontend>();
		
		for (AvailableList listeIn : listes) {
			
			AvailableListFrontend listeOut ;
			 
			if ( booleanDefaultValue == null ) {					
					listeOut = new AvailableListFrontend (listeIn.getListeId(), listeIn.getListeLabel(),
							listeIn.getDate(), listeIn.getPdtCount()
					);
			} else {
					listeOut = new AvailableListFrontend (Boolean.getBoolean(booleanDefaultValue), 
						listeIn.getListeId(), listeIn.getListeLabel(),
						listeIn.getDate(), listeIn.getPdtCount()
					);
			}
			
			outListes.add(listeOut);
			
		}
		return outListes;
		
	}
	/**
	 * transformResultToFrontend
	 * @param listes
	 * @param booleanDefaultValue Send 'true' or 'TRUE' (case insensitive comparison), otherwise it's false. 
	 * 		If null, uses  AvailableListFrontend constructor without boolean param
	 * @return
	 */
	private ArrayList<AvailableResultFrontend> transformResultToFrontend (ArrayList<AvailableResult> listes, String booleanDefaultValue) {
		
		ArrayList<AvailableResultFrontend> outListes = new ArrayList<AvailableResultFrontend>();
		
		for (AvailableResult listeIn : listes) {
			
			AvailableResultFrontend listeOut ;
			 
			if ( booleanDefaultValue == null ) {					
					listeOut = new AvailableResultFrontend(
						listeIn.getListeId(), listeIn.getListeLabel(),
						listeIn.getDate(), listeIn.getPdtCount()
					);
			} else {
					listeOut = new AvailableResultFrontend( Boolean.getBoolean(booleanDefaultValue), 
						listeIn.getListeId(), listeIn.getListeLabel(),
						listeIn.getDate(), listeIn.getPdtCount()
					);
			}
			
			outListes.add(listeOut);
			
		}
		return outListes;
		
	}
	
	
	/**	Build liste fron Sync Init Request & Init/Getter Response(s) if possible	**/
	
	private void buildDownListe(ListView liste) throws IOException {
		
		ArrayList<AvailableListFrontend> listesItems = null ; 
		
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
		

		
		// Si on a pu obtenir des données a traiter
		if (getLastInitResponse() != null && getLastInitResponse().getAvailableLists() != null) {
			
			availableLists = getLastInitResponse().getAvailableLists();
			listesItems = new ArrayList<AvailableListFrontend>();		
			listesItems.addAll(transformListeToFrontend(availableLists, "false")) ;					
		}
		
		// Si on a pas de données, il faut faire un sync ou mettre des exemples ...
		else {
			// setlastResponse ( addExampleResponse()  ) ; ...
				// OU
			// Add new TextView("Veuillez proceder a une synchronisation")
		}		
		
		
		
		SyncListDownAdapter adapter = new SyncListDownAdapter(Sync.this, listesItems);
		
		// Now done in the constructor of the adapter()
//		adapter.setData(listesItems);

		SyncHistory history = 
			GsonConverter.fromJsonSyncHistory(
				provider.getUserSyncHistoryContent()
		);
		
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
 * 	GIVE LISTENER FOR ROWS BUT NOT FOR THE ITEMS 		
 */
		liste.setOnItemClickListener(new OnItemClickListener() {
			
			

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				ListeDetailFrontend liste = (ListeDetailFrontend) parent.getItemAtPosition(position);
//				CheckedLinearLayout liste = (CheckedLinearLayout) parent.getItemAtPosition(position);
//				ViewHolder liste = (ViewHolder) parent.getItemAtPosition(position);
				
				AvailableListFrontend liste = (AvailableListFrontend) parent.getItemAtPosition(position);
				
//				liste.isChecked();
//				liste.setChecked(false);
				liste.toggle();
				
				AvailableListFrontendHolder holder = (AvailableListFrontendHolder) view.getTag();
				
				holder.toggle();
				
				if (holder.isChecked() && liste.isChecked()) {
					addSelectedId(liste.getListeId());
					Log.w(logHead, "Added ID cause one/two was checked : \n"
							+ "\t holder:chked[" + holder.isChecked() +"]\t liste:chked:[" + liste.isChecked() +"]"
						);					
				} else {
					removeSelectedId(liste.getListeId());
					Log.w(logHead, "Removed ID cause one/two was not checked : \n"
						+ "\t holder:chked[" + holder.isChecked() +"]\t liste:chked:[" + liste.isChecked() +"]"
					);
				}
				
				
//				Toast.makeText(getApplicationContext(),
//					      "Clicked on Row: ID[" + liste.getListeId() + "] label : " + liste.getListeLabel(),
//					      Toast.LENGTH_LONG).show();				
				
			}
			
		});
		
//		Log.i(logHead, "(DOWN) On item click lstn added.");				
		
	}
	
	private void buildUpListe(ListView liste)  throws IOException {
		
		ArrayList<AvailableResultFrontend> resultsItems = null ;
		
		availableResults = new ArrayList<AvailableResult>();
		
		
		// Au demarrage de l'activité, aucun fichier n'a été lu, 
		// il faut vérifier si on a des données ...
		// Si pas encore de données pretes a afficher
		if (getLastInitRequest() == null) {
			
			Log.i(logHead, "getLastRequest == null => check if there is a file with data");
			
			// On verifie que le fichier du user existe, sinon il n'y a rien a lire ...
			if (provider.checkUserFileExist(AppDataProvider.SYNC_INIT_REQ_FILENAME)) {
				
				Log.i(logHead, "The file [" + AppDataProvider.SYNC_INIT_REQ_FILENAME +"] exist. Gonna parse data");
				
				setLastInitRequest	(
					GsonConverter.fromJsonInitRequest (
						provider.readFileInUserFolder(AppDataProvider.SYNC_INIT_REQ_FILENAME)
					)
				);
				
				Log.i(logHead, "setLastRequest() has been made from file. Still null ? [" 
				+ (getLastInitRequest() == null ? "TRUE" : "FALSE")  + "]");
				
			}
			
		
		} 
		
		// Si on a pas de données, il faut faire un sync ou mettre des exemples ...
		else {

			// setlastRequest ( addExampleRequest()  ) ; ...
				// OU
			// Add new TextView("Pour avoir des resultats a envoyer, il faut aller " 
			//  + "a l'étape 2 d'au moins un des produits d'une de vos listes," 
			//  + " récupérables dans la section DOWN accessible via le bouton ci-dessus.");
			
			
//			String json = addExampleRequest();
//			
//			try {
//				buildInitRequest(json);
//			} catch (Exception e) {
//				Log.e(logHead, "error with json request [" + e.getMessage() +"]");			
//			}
//			
		}		
		

		
		if (getLastInitRequest() != null && getLastInitRequest().getAvailableResults() != null) {
			
			availableResults = getLastInitRequest().getAvailableResults();
			
			Log.i(logHead, "Build InitReq have set [" + availableResults.size() +"] avResults \n" + availableResults.get(0).toString());
			
			resultsItems = new ArrayList<AvailableResultFrontend>();		
			
			resultsItems.addAll(transformResultToFrontend(availableResults, "false")) ;			
		}
		
		// GOOD ?????????
		SyncListUpAdapter adapter = new SyncListUpAdapter(Sync.this, resultsItems);
		// data = readFromJson();
				
		try {
			liste.setAdapter(adapter);
			
			Log.i(this.getClass().getSimpleName(), "Adapter Up setted to liste.");
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), " Error append setting adpater : " + e.getMessage());
			e.printStackTrace();
		}
			
		
	}	

	

/*
 * 	Sync INIT Task	
 */
	
	public SyncInitTask SyncInitAsyncTask ;
	
	public void attempSyncInit() {
		
		if (SyncInitAsyncTask != null) {
			Log.e(logHead, "The Async Task is not null, return null ? ;");
//			return ;
		}
		
		if (getLastInitRequest() == null) { 
			Log.i(logHead, "getLastRequest() == null, we need it to create SyncInitTask()  \n read it :\t strData = provider.getInitResponseContent()");
			String strData = provider.getUserInitRequestContent() ;
			Log.i(logHead, "build it :\t buildInitRequest ( strData )");
			buildInitRequestFromJson(strData);
			Log.i(logHead, "OK.\t buildInitRequest ( strData )");
		}
		
		Log.i(logHead, "Launch SyncInitTask with request ? --> " + getLastInitRequest());
		
		SyncInitAsyncTask = new SyncInitTask(Sync.this, getLastInitRequest());
		
//		SyncInitAsyncTask.execute((Void) null) ;	
		SyncInitAsyncTask.execute(new SyncInitConnector(Sync.this));
		
	}
	
	public void finishSyncInit() 	{
		
//		task_succeeded = SyncInitAsyncTask.getResultTaskSucced() ;
		
		if (taskI_ran_once && taskI_succeeded) {
			Log.d("Sync -- static", "The sync process exited gracefully, UPDATING VIEW."
					+ "\n\tTask runned without technical error ? [" + taskI_ran_once + "]"
					+ "\n\tTask exited with application level success ? [" + taskI_succeeded + "]"
				);			
			showListView();
		} else {
			Log.e("Sync -- static", "The sync process occured at least one error, not updating view."
				+ "\n\tTask runned without technical error ? [" + taskI_ran_once + "]"
				+ "\n\tTask exited with application level success ? [" + taskI_succeeded + "]"
			);
		}		
		
	}

	private void resetInitTaskFlags () {
		taskI_ran_once = false ;
		taskI_succeeded = false ;
	}
	

/*
 * 	Sync GETTER Task	
 */	
	
		public SyncGetterTask SyncGetterAsyncTask ;
		
		public void attempSyncGetter() {
			
			if (SyncGetterAsyncTask != null) {
				Log.e(logHead, "The Sync Getter Async Task is not null, set it to null ?");
				SyncGetterAsyncTask = null ;
//				return ;
			}
			
			// Construire la requete
			// TODO: le faire a chaque fois ?
//			if (getLastGetterRequest() == null) { 
				buildGetterRequest(); 
//			}
			
			Log.i(logHead, "Launch SyncGetterTask with request ? --> " + getLastGetterRequest());
			
			SyncGetterAsyncTask = new SyncGetterTask(Sync.this, getLastGetterRequest());
			
//			SyncInitAsyncTask.execute((Void) null) ;	
			SyncGetterAsyncTask.execute(new SyncGetterConnector(Sync.this));
			
		}
		
		public void finishSyncGetter() {
			
//			task_succeeded = SyncInitAsyncTask.getResultTaskSucced() ;
			
			if (taskG_ran_once && taskG_succeeded) {
				Log.d("SyncG", "The syncG process exited gracefully, UPDATING VIEW."
						+ "\n\tTask runned without technical error ? [" + taskG_ran_once + "]"
						+ "\n\tTask exited with application level success ? [" + taskG_succeeded + "]"
					);		
				
				// Not here but after history
//				showListView();
			} else {
				Log.e("SyncG", "The syncG process occured at least one error, not updating view."
					+ "\n\tTask runned without technical error ? [" + taskG_ran_once + "]"
					+ "\n\tTask exited with application level success ? [" + taskG_succeeded + "]"
				);
			}	
			
			resetGetterTaskFlags();
			
			SyncGetterAsyncTask = null ;
			
			
			
			// Lancer la sauvegarde de l'historique			
			attempSyncHistory();
			
		}

		private void resetGetterTaskFlags () {
			taskG_ran_once = false ;
			taskG_succeeded = false ;
		}
			
	
/*
 * 	Sync HISTORY Task	
 */	
	
		public SyncHistoryTask SyncHistoryAsyncTask ;
		
		public void attempSyncHistory() {
			
			if (SyncHistoryAsyncTask != null) {
				Log.e(logHead, "The Sync History Async Task is not null, set it to null ? (== ensure reset ?)");
				SyncGetterAsyncTask = null ;
//						return ;
			}			
			
			SyncHistoryAsyncTask = new SyncHistoryTask(Sync.this, getLastSyncdLists());
			
			SyncHistoryAsyncTask.execute((Void) null);
			
		}
		
		public void finishSyncHistory() {
			
			
			if (taskH_ran_once && taskH_succeeded) {
				Log.d(logHead, "The syncH process exited gracefully, UPDATING VIEW."
						+ "\n\tTask runned without technical error ? [" + taskH_ran_once + "]"
						+ "\n\tTask exited with application level success ? [" + taskH_succeeded + "]"
					);
				// Update the listview content
				showListView();
			} else {
				Log.w(logHead, "The syncH process occured at least one error, not updating view."
					+ "\n\tTask runned without technical error ? [" + taskH_ran_once + "]"
					+ "\n\tTask exited with application level success ? [" + taskH_succeeded + "]"
				);
			}	
			
			resetHistoryTaskFlags();
			
			SyncHistoryAsyncTask = null ;
			
		}

		private void resetHistoryTaskFlags () {
			taskH_ran_once = false ;
			taskH_succeeded = false ;
		}
			
		
	public void addLastSynchronizedList (ListeDetailFrontend newListe) {
		if (lastSynchronizedLists == null) {
			this.lastSynchronizedLists = new ArrayList<SynchronizedList>();
		}
		
		lastSynchronizedLists.add (
			new SynchronizedList (
				Long.valueOf(newListe.getLstId().toString()),
				newListe.getLstLabel(),
				getFormattedDateNow()
			)
		);
	}
		
	private  ArrayList<SynchronizedList> lastSynchronizedLists ;
	
	private ArrayList<SynchronizedList> getLastSyncdLists() {
		return lastSynchronizedLists;		
	}
		
	
    private static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
    /**
     * Renvoie la date actuelle formattee
     * @return La date actuelle formattee
     */
    public static String getFormattedDateNow(){
        DateTime dt = new DateTime();
        /* Formatage de la date et conversion en texte */
        DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
        String dateDerniereConnexion = dt.toString(formatter);    
        return dateDerniereConnexion;
    }	
	
/*
 * 	Sync Icons Task	
 */			
	private SyncIconsTask downloadTask = null	;	
		
	private HashMap<String, String> lastIconMap ;
	
	public static final String 	genKeyPrefix = "gen_" ;	
	public static final String 	userKeyPrefix = "custom_" ;
	
	private HashMap<String, String> getLastIconMap() {
		return lastIconMap;
	}
	private void setlastIconMap(HashMap<String, String> missingIconMap) {
		this.lastIconMap = missingIconMap;
	}
		
	public void enableSyncIconsTask (String missingIcons) {
		
		listener.enableIconSync(true, missingIcons.split(",").length);
		
		HashMap<String, String> missingIconMap = new HashMap<String, String>();
		
//		String 	genKeyPrefix = "gen_" ;
		int 	genPdtCount = 0;

//		String 	userKeyPrefix = "custom_" ;
		int 	userPdtCount = 0;		
		
		for (String item : missingIcons.split(",")) {
//			String pdtId 	= item.substring(0, item.indexOf("-") - 1) ;
			String pdtUrl   = item.substring(item.indexOf("-")) ;
			
			if (pdtUrl.contains("/generic")) {
				genPdtCount ++ ;
				missingIconMap.put(genKeyPrefix + genPdtCount, item);
			} else {
				userPdtCount ++ ;
				missingIconMap.put(userKeyPrefix + userPdtCount, item);
			}
		}
		
		setlastIconMap(null);
		setlastIconMap(missingIconMap);
		
//		this.getCurrentFocus().invalidate();
		
		View v = findViewById(R.id.sync_main_layout);
//		v.invalidate();
		v.refreshDrawableState();
		
	}

	// declare the dialog as a member field of your activity
	ProgressDialog mProgressDialog;

	// Initialize it in onCreate()

	// execute this when the downloader must be fired
	//	mTask = new task().execute() ;
	//	++ mDialog.setOnCancelListener (onCancel(){ mTask.cancel(true) ; })

	
	public void attemptSyncIcons(){
		
		
		downloadTask = new SyncIconsTask(Sync.this, getLastIconMap());
		downloadTask.execute((Void) null);

		mProgressDialog.setOnCancelListener(
			new DialogInterface.OnCancelListener() { @Override 
				public void onCancel(DialogInterface dialog) {
		        	downloadTask.cancel(true);
		    }}
		);			
		
	}
	
	
	public void finishSyncIcons(){
		
//		task_succeeded = SyncInitAsyncTask.getResultTaskSucced() ;
		
		if (taskIcons_ran_once && taskIcons_succeeded) {
			Log.d("Sync", "The SyncIcons process exited gracefully, UPDATING VIEW."
					+ "\n\tTask runned without technical error ? [" + taskIcons_ran_once + "]"
					+ "\n\tTask exited with application level success ? [" + taskIcons_succeeded + "]"
				);			
			// Mettre a jour l'affichage
			// TODO inutile ?
			showListView();
		} else {
			Log.e("Sync", "The SyncIcons process occured at least one error, not updating view."
				+ "\n\tTask runned without technical error ? [" + taskIcons_ran_once + "]"
				+ "\n\tTask exited with application level success ? [" + taskIcons_succeeded + "]"
			);
		}		
		
		listener.enableIconSync(false, 0);
		resetIconsTaskFlags();

		downloadTask = null ;
	}	
	
	private void resetIconsTaskFlags () {
		taskIcons_ran_once = false ;
		taskIcons_succeeded = false ;
	}
	
}
