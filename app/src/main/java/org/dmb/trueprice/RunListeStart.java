package org.dmb.trueprice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.dmb.trueprice.listeners.RunStartListener;
import org.dmb.trueprice.objects.ListeDetailFrontend;
import org.dmb.trueprice.objects.ProduitFrontend;
import org.dmb.trueprice.tools.AppDataProvider;
import org.dmb.trueprice.utils.internal.GsonConverter;

import android.content.Intent;
import android.os.Bundle;

import com.android.internal.util.*;

import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RunListeStart extends MenuHandlerActionBarCompatAdapter {
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	
	public final static String 	FLAG_CHOSEN_LIST_ID 
	= "com.dmb.trueprice.android.FLAG_CHOSEN_LIST_ID" ;
	
	private Long selectedCatgId ;
	
	
	
	public final static String 	FLAG_CHOSEN_CATG_LABEL 
	= "com.dmb.trueprice.android.FLAG_CHOSEN_CATG_LABEL" ;
	
	public final static int 	REQID_START_BY_CATG = 1 ;
	public final static int 	REQID_CHOOSE_CATG = 1 ;
	
	
	private Long  work_listeId ;
	
	private String currentUserEmail = null ;
	private AppDataProvider provider = null ;
	
	private ListeDetailFrontend runnableList ;
	
	
	
	public final static String 	ATT_ID_CATG_LABELS 
	= "com.dmb.trueprice.android.RunListeStart.ATT_ID_CATG_LABELS" ;
	private HashMap<Long, String> 	listCategories ;
//	private SparseArray<String> listCategories ;
	
	private HashMap<Long, String> 	listSubCategories ;
	private ArrayList<String> listProperties ;
	private ArrayList<String> listSubProperties ;
	
	
	
	// StartViews
	private TextView mTxt_listLabel ;
	private TextView mTxt_listProgress ;
	private TextView mTxt_listPdtInitCount ;
	private TextView mTxt_listPdtCalcCount ;
	private TextView mTxt_listPdtExpcCount ;
	private TextView mTxt_listPdtStopCount ;
	private TextView mTxt_listPdtRemainingCount ;
	
	// ListStatsViews
	private TextView mTxt_listStatLower ;
	private TextView mTxt_listStatNeutral ;
	private TextView mTxt_listStatHigher ;
	private TextView mTxt_listStatRdcCount ;
	private TextView mTxt_listStatRdcValue ;
	
	// StopViews
	private TextView mTxt_listResultItemCount ;
	private TextView mTxt_listResultTTC ;
	private TextView mTxt_listResultLowerPricesCount ;
	private TextView mTxt_listResultProvisional ;
	private TextView mTxt_listResultFinal ;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_liste_start);

		Intent i = getIntent();

    //  ???????????????????????????????
		/*	It's the only place we set SiginInStatus directly,
		 * each other time, we change status is for (dis)connection completed*/
    // ??????????????      WHAT ??????????????  Copied from other file ?

        try {

            work_listeId = i.getExtras().getLong(FLAG_CHOSEN_LIST_ID, -1);
            Log.d(logHead, "onCreate() -> Got Bundle Extra FLAG [chosen_list_id]== " + work_listeId);

        } catch (Exception e) {
            Log.w(logHead, "Could not get Bundle Extra FLAG [chosen_list_id]== " + work_listeId);
            e.printStackTrace();
        }

		
				
		try {
			if (currentUserEmail == null) { currentUserEmail = Launcher.currentUserEmail ;}
			provider = new AppDataProvider(RunListeStart.this, currentUserEmail);
			Log.i(logHead, "appDataProvider created. userEmail[" + currentUserEmail + "]");
		} catch (Exception e) {
			Log.w(logHead, "Could not create appDataProvider ! userEmail[" + currentUserEmail + "]");
		}

		

		
		try {
			
			ArrayList<ListeDetailFrontend> tempArray = GsonConverter
				.fromJsonGetterResponse(
					provider.getUserGetterResponseContent()
				) .getProvidedLists()
			;
			
			for (ListeDetailFrontend loopListe : tempArray) {
				Long loopListId = Long.valueOf(loopListe.getLstId().toString());
				if (loopListId ==  work_listeId) {
					runnableList = loopListe ;
					break;
				} 
			} 
			
			if (runnableList != null) {
				
				Log.i(logHead, "Found the asked list : \n" + runnableList.toString() + ". Now trying to get views & fill them ...");

				
				// Remplir les vues des stats de la progression actuelle de la liste
				// Label, progress %, remain, stop, expc, calc, init
				// Default :
				// Label, 0%, Pdt.All, 0, 0, 0, 
				fillStartViewsWithValues(runnableList.getLstLabel(), 0, runnableList.getPdtCount(), 0, 0, 0, runnableList.getPdtCount());
				
				// Remplir les vues des Stats antérieures de la liste
				fillListStatsViewsWithValues(0,0,0,0,0);
				
				
				getListMetadata( runnableList );
				
				Log.d(logHead, "Fount MetaData : " 
					+ "\t Catg [x " + this.listCategories.size() + "]"
					+ "\t SCatg [x " + this.listSubCategories.size() + "]"
					+ "\t Prop [x" + this.listProperties.size() + "]"
					+ "\t SProp [x" + this.listSubProperties.size() + "]"
				);
				
				
			} else {
				throw new Exception("The asked listId (" + work_listeId + ") was not found in syncGetterResponse.getProvidedLists()");
			}
			
		} catch (Exception e) {
			Log.w(logHead, "Could not get the list from provider ! \n" + e.getMessage());
			e.printStackTrace();
		}
		
		// Remplir les vues des stats de la liste en fin d'utilisation 
		// en fonction de la progression actuelle
		fillStopViewsWithValues(0, 0, 0, 0, 0);
				
		
		RunStartListener lstn = new RunStartListener(this);
		
		Button btn= (Button) findViewById(R.id.btn_pdt_start_catg);
		
		btn.setOnClickListener(lstn);
		
		btn = (Button) findViewById(R.id.btn_pdt_start_state);
		
		btn.setOnClickListener(lstn);
		
		btn = (Button) findViewById(R.id.btn_pdt_start_alpha);
		
		btn.setOnClickListener(lstn);		
		
		
	}
	
	
	
	private void getListMetadata( ListeDetailFrontend runnableList ) {
		
		if (this.listCategories == null) 	{
			this.listCategories	= new HashMap<Long, String>(); //new LongSparseArray<String>();
		}
		if (this.listSubCategories == null) {
			this.listSubCategories = new HashMap<Long, String>() ;
		}		
		if (this.listProperties == null) {
			this.listProperties = new ArrayList<String>()  ;
		}
		if (this.listSubProperties == null) {
			this.listSubProperties = new ArrayList<String>();
		}
		
		
		for (ProduitFrontend pdt : runnableList.getPdtFrontObjects()) {
			
			
			if (pdt.getPdtCategory() > 0) {
				listCategories.put(
					Long.valueOf(Integer.toString(pdt.getPdtCategory())),
					pdt.getCatgLabel()
				);
			}
			
			if (pdt.getPdtSubcategory() > 0) {
				listSubCategories.put(
					Long.valueOf(Integer.toString(pdt.getPdtSubcategory())),
					pdt.getScatgLabel()
				);
			}
			
			String property = pdt.getPdtProperty() ;
			if (property != null) {
				if ( ! listProperties.contains(property)) {
					listProperties.add(property);
				}
			}
			
			property = null ;
			property = pdt.getPdtSubproperty();
			if (property != null ) {
				if ( ! listSubProperties.contains(property)){
					listSubProperties.add(property);
				}
			}		
			
			
			
			
			
		}
		
	}
	
	
	private void fillStartViewsWithValues( String listeLabel, int progress, int pdtRemain,
			int pdtStop, int pdtExpected, int pdtCalculated, int pdtInitialState	)	{
		
		if (mTxt_listLabel == null ) {			
			
			mTxt_listLabel 		= (TextView) findViewById(R.id.run_liste_start_liste_label);
			mTxt_listProgress	= (TextView) findViewById(R.id.run_liste_start_liste_progress);
			// Pdt restant
			mTxt_listPdtRemainingCount 	= (TextView) findViewById(R.id.run_liste_start_count_remaining);
			// Pdt etat 3 (stop)
			mTxt_listPdtStopCount = (TextView) findViewById(R.id.run_liste_start_count_stop);
			// Pdt etat 2 (complété) (expected)
			mTxt_listPdtExpcCount = (TextView) findViewById(R.id.run_liste_start_count_expected);
			// Pdt calculé 1bis (== etat 2 pas complété) (calc)
			mTxt_listPdtCalcCount = (TextView) findViewById(R.id.run_liste_start_count_calc);
			// Pdt etat 1 (init)
			mTxt_listPdtInitCount = (TextView) findViewById(R.id.run_liste_start_count_inital);			
		}
		
		mTxt_listLabel		.setText(listeLabel);
		mTxt_listProgress	.setText(	Integer.toString(	progress) + " %");
		
		mTxt_listPdtStopCount.setText(	Integer.toString(	pdtStop	));
		mTxt_listPdtExpcCount.setText(	Integer.toString(	pdtExpected	));
		mTxt_listPdtCalcCount.setText(	Integer.toString(	pdtCalculated	));
		mTxt_listPdtInitCount.setText(	Integer.toString(	pdtInitialState	));
		
		mTxt_listPdtRemainingCount.setText(Integer.toString(pdtRemain));
		
	}
	
	private void fillListStatsViewsWithValues (int lower, int neutral, int higher, int rdcCount, int rdcValue) {

		if (mTxt_listStatLower == null ) {			
			
			mTxt_listStatLower 		= (TextView) findViewById(R.id.run_liste_stat_lower_count);
			mTxt_listStatNeutral	= (TextView) findViewById(R.id.run_liste_stat_neutral_count);
			mTxt_listStatHigher 	= (TextView) findViewById(R.id.run_liste_stat_higher_count);
			mTxt_listStatRdcCount 	= (TextView) findViewById(R.id.run_liste_stat_rdc_count);
			mTxt_listStatRdcValue 	= (TextView) findViewById(R.id.run_liste_stat_rdc_value);
		
		}
		
		mTxt_listStatLower		.setText(	Integer.toString(	lower ));
		mTxt_listStatNeutral	.setText(	Integer.toString(	neutral ));		
		mTxt_listStatHigher		.setText(	Integer.toString(	higher ));
		mTxt_listStatRdcCount	.setText(	Integer.toString(	rdcCount));
		mTxt_listStatRdcValue	.setText(	Integer.toString(	rdcValue )  + " €"	);

	}
	
	private void fillStopViewsWithValues(int itemCount, int ttc, int lowerPricesCount, int resultProvisionnal, int resultFinal) {
		
		if (mTxt_listResultItemCount == null ) {			
			
			mTxt_listResultItemCount 		= (TextView) findViewById(R.id.run_liste_start_result_count_items);
			mTxt_listResultTTC 			= (TextView) findViewById(R.id.run_liste_start_result_ttc);
			mTxt_listResultFinal 	= (TextView) findViewById(R.id.run_liste_start_result_final);
			mTxt_listResultLowerPricesCount 	= (TextView) findViewById(R.id.run_liste_start_result_lower_prices);
			mTxt_listResultProvisional 	= (TextView) findViewById(R.id.run_liste_start_result_provisionnal);
			
		}
		
		mTxt_listResultTTC			.setText(	Integer.toString(	ttc	)  + " €");
		mTxt_listResultItemCount	.setText(	Integer.toString(	itemCount ));
		mTxt_listResultFinal		.setText(	Integer.toString(	resultFinal )  + " €");
		
		mTxt_listResultLowerPricesCount	.setText(	Integer.toString(	lowerPricesCount));
		mTxt_listResultProvisional		.setText(	Integer.toString(	resultProvisionnal) + " €");
				
	}
	
		
	
	public boolean attemptStartByCatg() {
		
		if( this.listCategories != null && this.listSubCategories != null) {

			Intent i = new Intent(RunListeStart.this, ChooseCatgActivity.class);

			// Le resultat qui sera retourné
			i.putExtra(RunListeStart.FLAG_CHOSEN_CATG_LABEL, "NONE");
			
			// Ajouter un bundle contenant les données pour l'adapter
//			Bundle bl = new Bundle();
			
			ArrayList<String> tempArr = new ArrayList<String>(listCategories.size());
			
			tempArr.addAll(listCategories.values());

			i.putExtra (
				RunListeStart.ATT_ID_CATG_LABELS,
				listCategories
			);

			
			startActivityForResult(i, RunListeStart.REQID_CHOOSE_CATG);					
			
			return true;
			
		} 
		
		else {
			return false ;
		}
		
	}

	private void startByCatg(){
		
		if ( this.selectedCatgId != null && this.selectedCatgId > 0) {
			
			if (this.runnableList != null) {
				
			// 1. recuperer les produits de la bonne categorie 	
				// Les icones des produits necessaires
				HashMap<String, String> neededIcons = new HashMap<String, String>();
				
				// Les produits de la bonne categorie
				ArrayList<ProduitFrontend> neededProducts = new ArrayList<ProduitFrontend>();
						
				String strLog = "" ;
				
				// pour chaque poduit de la liste
				for (ProduitFrontend pdtFtd : runnableList.getPdtFrontObjects() ){

					// Si il fait partie de la categorie demandee
					if (pdtFtd.getPdtCategory() == this.selectedCatgId) {
						
//						strLog += "Add pdt with ID[" + pdtFtd.getPdtId() + "] Label[" + pdtFtd.getPdtNom() + "]";
						neededProducts.add(pdtFtd);
						
//						strLog += "\t ++ icon url: [" + pdtFtd.getPdtLink()  + "]\n" ;
						neededIcons.put( 
							Long.toString(pdtFtd.getPdtId()),
							pdtFtd.getPdtLink()
						);
						
					}
				}
				
				Log.d(logHead, strLog);
				
				
				Intent i = new Intent(RunListeStart.this, RunProduit.class);
				
				// Le resultat qui sera retourné
				i.putExtra(RunListeStart.FLAG_CHOSEN_CATG_LABEL, "NONE");
										
//				ArrayList<String> tempArr = new ArrayList<String>(listCategories.size());				
//				tempArr.addAll(listCategories.values());

				i.putExtra (
					RunProduit.ATT_ID_SELECTED_PRODUCTS,
//					listCategories
					neededProducts
				);

				i.putExtra (
						RunProduit.ATT_ID_SELECTED_ICONS,
//						listCategories
						neededIcons
					);
				
				startActivityForResult(i, RunListeStart.REQID_START_BY_CATG);				
				
				
				
				
				
			}
			
		} else {
			Log.e(logHead, "Can't start by Catg without selected Catg Id > [" + this.selectedCatgId + "]");
		}
		
	}
	
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 * 
	 * arg0 == request Code
	 * arg1 == result Code
	 */
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
//		super.onActivityResult(arg0, arg1, arg2);
		
		switch (arg0) {
			
			case REQID_CHOOSE_CATG :
				
				if (arg1 == RESULT_OK) {					
					
					if (arg2 != null && arg2.getStringExtra(FLAG_CHOSEN_CATG_LABEL) != "NONE" ) {
						
						String chosenLabel = arg2.getStringExtra(FLAG_CHOSEN_CATG_LABEL) ;
						Long chosenId = null ;
						
						if (this.listCategories.containsValue(chosenLabel)) {
							
							String strLog = "Find catg ID : ";
							
							for (Long key : this.listCategories.keySet()) {
								String testStr = this.listCategories.get(key) ;
								if ( testStr.compareTo(chosenLabel) == 0) {
									chosenId = key ;
									strLog += " \n FOUND [" + key + "]" ;
									break;
								} else {
									strLog += "\t not [" + testStr + "]" ;
								}
							}
							
						}
						

						Toast.makeText(RunListeStart.this, 
								"La liste sera parcourue dans la categorie "
								+ "(" + chosenId + ")[" + chosenLabel + "]" ,
						Toast.LENGTH_LONG).show();
						
						setSelectedCatgId(chosenId);
						
						startByCatg();
						
						
					} else {

						// Annuler
						
						Log.d(this.getClass().getSimpleName(), 
						"Could not find choice result ... ");
					}
//					
				} else if (arg0 == RESULT_CANCELED) {
					
					Toast.makeText(	RunListeStart.this, "Operation cancelled" ,
					Toast.LENGTH_SHORT).show();
					
				} else {
					
					Toast.makeText(	RunListeStart.this, "Don't know what happenned ... " ,
					Toast.LENGTH_SHORT).show();
					
				} 
				
				
				Log.d(logHead, "StartByCatg() stopped or cancelled");
				
			break;

		default:
			
			Toast.makeText(RunListeStart.this, "Don't know from wich activity you come from [" 
					 + " reqID = " + arg0 + " ]"	, Toast.LENGTH_SHORT	).show();
			
			break;
		}
		
		
		
		
		
		
	}
	
		
	
	
	public void setSelectedCatgId(Long selectedCatgId) {
		this.selectedCatgId = selectedCatgId;
	}

	
	
	
	
	
	
	
}
