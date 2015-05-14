package org.dmb.trueprice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.dmb.trueprice.adapters.ProductsIconsGalleryImageAdapter;
import org.dmb.trueprice.adapters.RunProduitPagerAdapter;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.fragments.RunPdtExpect;
import org.dmb.trueprice.fragments.RunPdtInit;
import org.dmb.trueprice.objects.ProduitFrontend;
import org.dmb.trueprice.objects.ProduitResult;
import org.dmb.trueprice.objects.SyncHistory;
import org.dmb.trueprice.objects.android.SynchronizedList;
import org.dmb.trueprice.tasks.BuildGalleryAdapterAsyncTask;
import org.dmb.trueprice.tools.AppDataProvider;
import org.dmb.trueprice.utils.internal.GsonConverter;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Toast;


public class RunProduit extends MenuHandlerActionBarCompatAdapter implements ActionBar.TabListener {
	
	
	private static final String logHead = RunProduit.class.getSimpleName() ;
	
	
	public final static String 	ATT_ID_SELECTED_PRODUCTS
	= "com.dmb.trueprice.android.RunProduit.ATT_SELECTED_PRODUCTS" ;
	private static ArrayList <ProduitFrontend> 	workedProducts = null;		
	
	public final static String 	ATT_ID_SELECTED_ICONS
	= "com.dmb.trueprice.android.RunProduit.ATT_SELECTED_ICONS" ;
	private HashMap<String, String> neededIcons = null;
	
	HashMap<Long, Bitmap> galleryData = new HashMap<Long, Bitmap>();
	
	
	private String currentUserEmail = null ;
	public AppDataProvider provider = null ;	
	
	private Gallery gallery = null ;
	
	private static ViewPager vwPager ;

    private Button bttGalleryNext = null ;
    private Button bttGalleryPrev = null ;

	
	/**
	 *  Attributes for handling each product data in each fragment
	 *  to create products results ArrayList
	 */
	// L'id du produit courant
	private static Long selectedProductId = Long.valueOf("0") ;
	
	private static ArrayList <ProduitResult> productsResults = new ArrayList<ProduitResult>();	

	protected static HashMap<Long, Integer> resultIndexMap = new HashMap<Long, Integer>(); 
	
	
	
	/**
	 * 
	 * 	Activity  related methods
	 * 
	 */	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_pdt_wrapper);
		

        // Set up action bar.
        final ActionBar actionBar = getSupportActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		// Instancier l'Adapter
        RunProduitPagerAdapter mAdapter = new RunProduitPagerAdapter(getSupportFragmentManager());
		
        vwPager = (ViewPager) findViewById(R.id.pager);
        vwPager.setAdapter(mAdapter);                       
        
        vwPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        
        
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAdapter.getPageTitle(i))
                            .setTabListener(this));
        }        

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       
        
                
/*******
 * 
 *   GPC added
 *   
 */               
		
		try {
			if (currentUserEmail == null) { currentUserEmail = Launcher.currentUserEmail ;}
			provider = new AppDataProvider(RunProduit.this, currentUserEmail);
			Log.i(logHead, "appDataProvider created. userEmail[" + currentUserEmail + "]");
		} catch (Exception e) {
			Log.w(logHead, "Could not create appDataProvider ! userEmail[" + currentUserEmail + "]");
		}
				
//        provider.

        /**
         *  Get Bitmaps for Gallery
         */
        
        
        
        Bitmap [] icons = {};
        String [] names = {};
        
		
		Intent i = getIntent();
		
		neededIcons = (HashMap<String, String>) i.getExtras().get(RunProduit.ATT_ID_SELECTED_ICONS);
		
		workedProducts = (ArrayList<ProduitFrontend>) i.getExtras().get(RunProduit.ATT_ID_SELECTED_PRODUCTS);
		
		if ( neededIcons != null && workedProducts != null) {


			BuildGalleryAdapterAsyncTask buildTask = new BuildGalleryAdapterAsyncTask(RunProduit.this, workedProducts, neededIcons);
			buildTask.execute((Void) null);
			
			
///**	 Will be done in onResult() of the AsyncTask
//			
////			
////			HashMap<Long, Bitmap> tempMap = new HashMap<Long, Bitmap>() ;
////			tempMap.putAll(galleryData);
//			
////			gAdapter = new ProductsIconsGalleryImageAdapter(
////				this, getGalleryData()
////			);
//			
////			gAdapter.setData(galleryData);
//**/
			
		} else {
			Log.d(logHead, "Got incomong categories NULL !! ");
			
//			finish();
		}	        
        
        
        
        
		/**
		 * 	Gallery 
		 */
		
//		 Gallery gallery = (Gallery) findViewById(R.id.gallery_pdt);
		
		findGallery();
		 
//	        selectedImage=(ImageView)findViewById(R.id.imageView1);
		 
//	        gallery.setSpacing(1);	    
		 
//		 	gallery.setAdapter(gAdapter);

	         // clicklistener for Gallery
	        this.gallery.setOnItemClickListener(		
//	        		lstn
	        		
                new OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    //	                Toast.makeText(RunProduit.this, "Your selected position = " + position + " got item ID [" + id + "]", Toast.LENGTH_SHORT).show();
                        // show the selected Image
    //	                selectedImage.setImageResource(mImageIds[position]);
                        setSelectedProductId(id);
                    }
                }
	        );


        bttGalleryNext = (Button) findViewById(R.id.btn_gallery_next);
        bttGalleryPrev = (Button) findViewById(R.id.btn_gallery_previous);

        bttGalleryNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextProduct();
            }
        });
        bttGalleryPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousProduct();
            }
        });

       
	    Log.d(logHead, logHead + " -> onCreate() -> Done.");
	        
	}

    public void showNextProduct() {
        int pos = this.gallery.getSelectedItemPosition() ;
        int posNext = pos + 1 ;
        if (posNext < this.gallery.getCount()) this.gallery.setSelection(posNext);
    }

    public void showPreviousProduct() {
        int pos = this.gallery.getSelectedItemPosition() ;
        int posPrev = pos - 1 ;
        if (posPrev >= 0) this.gallery.setSelection(posPrev);
    }

	
	
	/**
	 * 
	 * 	Fragments & tabs  related methods
	 * 
	 */
	
	
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction arg1) {
		vwPager.setCurrentItem(tab.getPosition());
	}
	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub		
	}

	
	
	
	
	/**
	 * 
	 * 	Gallery 	related methods
	 * 
	 */
	
	
	private ProductsIconsGalleryImageAdapter gAdapter = null ;
	
	public ProductsIconsGalleryImageAdapter initGalleryAdapter(HashMap<Long, Bitmap> namesAndBitmaps) {
		return new ProductsIconsGalleryImageAdapter(RunProduit.this, namesAndBitmaps); 		
	}
	
	public void setGalleryAdapter (ProductsIconsGalleryImageAdapter pdtIconAdapter) {
		this.gAdapter = pdtIconAdapter;
	}
	
	public void attachGalleryAdapter() {
		findGallery();
		this.gallery.setAdapter(this.gAdapter);
		RunProduit.setSelectedProductId(this.gAdapter.getItemId(0));
	}
	
	private void findGallery() {
		if (this.gallery == null) {
			this.gallery = (Gallery) findViewById(R.id.gallery_pdt);
		}
	}
	
	public HashMap<Long, Bitmap> getGalleryData() {
		return galleryData;
	}
	public void setGalleryData(HashMap<Long, Bitmap> galleryData) {
		this.galleryData = galleryData;
	}		
	
	public Object addEntryToGalleryData(Long pdtId, Bitmap bmpIcon) {
		if( this.galleryData == null ) { this.galleryData = new HashMap<Long, Bitmap>();}
//		Log.d(logHead, "Does this map contain existing key[" 
//			+ pdtId + "] ? :> "
//			+ this.galleryData.get(pdtId) 				
//		);
		return this.galleryData.put(pdtId, bmpIcon);
	}
	
	public boolean taskBuildAdapter_success = false ;
	
	
	
	
	
	
	
	
	/**
	 * 
	 * 	Produit Results related
	 * 
	 */
	
	private static ProduitFrontend getReferenceProductById (Long pdtId) {
		
		ProduitFrontend pdtFtdReturned = null ;
		
//		String strLog = " Gonna search the product with id [" + pdtId + "] : " ;
//		String strFalse = "\n\t False => [" ;
//		String strTrue = "\n\t True => [" ;
		
		for (ProduitFrontend pFtd : workedProducts) {
			
			if (Long.toString(pFtd.getPdtId()) == Long.toString(pdtId)) {
//				strTrue += pFtd.getPdtId() + ", " ;
				pdtFtdReturned = pFtd;
//				return pFtd;
			} else {
//				strFalse += pFtd.getPdtId() + ", " ;
			}
		}
		
//		strFalse += "]";
//		strTrue += "]";
//		Log.i(logHead, strLog + strTrue + strFalse ) ;
		
		return pdtFtdReturned;
//		return null;
	}
	private static ProduitResult getResultByPdtId (Long pdtId) {
//		ProduitFrontend pFtd = null ;
		
		for (ProduitResult pRes : productsResults) {
			
			if (pRes.getProductRefId() == pdtId) {
				
				return pRes;
			}
			
		}
		
		return null;
	}	

	public static void setSelectedProductId(Long selectedProductId) {
		RunProduit.selectedProductId = selectedProductId;
				
//		int currentTab = vwPager.getCurrentItem();
		
		Long currentPdtId 
//			= getSelectedProductId();
			// Better perfs like this
			= RunProduit.selectedProductId ;
		
		ProduitResult pRes = getResultByPdtId(currentPdtId);
		
		if (pRes != null ) {
			
			updatePdtInitViews(pRes);
			
			if (pRes.getPriceUnitMeasure() > 0 ||  pRes.getPriceUnitSales() > 0) {
				
				updatePdtInitStatsOnExpectedViews(pRes);
				
			}
		
			if (pRes.getQttCarried() > 0) {
				
				updatePdtExpectedViews(pRes);
			
//				if (pRes.getFinalPrice() > 0) {		
//					updatePdtFinalViews();
//				}
				
			}
		
		} else {
			
			ProduitFrontend pFtd = getReferenceProductById(RunProduit.selectedProductId);
			
			Log.d(logHead, "Gonna try to get refferred product data." 
				+ "\t Id asked is [" + RunProduit.selectedProductId + "] <=> found referer ? \n[" + pFtd + "]"
			);
			
			// The referred units & values
			RunPdtInit.fillViewsWithValues(
				pFtd.getQttUnite(),
				pFtd.getQttValue(),
				"",  //pFtd.getStats().getLastPriceSale(),
				""  //pFtd.getStats().getLastPriceMeasure()
			);	
			
			RunPdtExpect.clearViews();
			
		}
		
	}
	
	
	public static Long getSelectedProductId() {
		return selectedProductId;
	}

//	private ArrayList<ProduitResult> getProductsResults() {
//		return productsResults;
//	}

	
	private static void addProductResult (ProduitResult pdtResult) {
		RunProduit.productsResults.add(pdtResult);
		RunProduit.resultIndexMap.put(
			pdtResult.getProductRefId(),
			RunProduit.productsResults.indexOf(pdtResult)
		);
	}	
	
	
	
	
	
	
	/**
	 * 
	 * Produit Init State
	 * 
	 */
			
	
	/**
	 * This method is responsible for updating the ProduitInit
	 * fragment with the data from the new selected product 
	 * if not null. First check if there's any existing result, 
	 * else get back original data from the ProduitDetailFrontend.
	 * Get it by calling getReferenceProductById() . 
	 */
	private static void updatePdtInitViews(ProduitResult pRes){

		Log.d(logHead, "updatePdtInitViews() ->");
		

		RunPdtInit.fillViewsWithValues(
			pRes.getQttUnitModified(),
			Double.toString(pRes.getQttValueModified()),
			Double.toString(pRes.getPriceUnitSales()),
			Double.toString(pRes.getPriceUnitMeasure())
		);			

	}
		
	
	public static void recordInitResult (String qttUnit, double qttValue, double salePrice, double measurePrice) {
		
		// First get the corresponding result
		Long pdtId = getSelectedProductId();
		int index = -1 ;		
		ProduitResult pRes = null ;
		
		try {
			// Look if we have an existing result
			index = RunProduit.resultIndexMap.get(pdtId);
			
			// Si un resultat existe déjà, MAJ
			if (index > -1) {
				pRes = productsResults.get(index);
				
				updateProductResultFromInitValues(index, qttUnit, qttValue, salePrice, measurePrice);
				
			}
			// Sinon create new
			else {}
			
			
		} catch (Exception e) {
			Log.e(logHead, "Could not record Init Result cause : " + e.getMessage() + ".\t Gonna try to create id ");
//			e.printStackTrace();
			
			if (pdtId != null) {
				
				ProduitFrontend pFtd = getReferenceProductById(pdtId);
				
				// The referred units & values
				pRes = new ProduitResult(
					getSelectedProductId(),
					pFtd.getPdtQtt(),
					pFtd.getQttUnite(),
					Double.valueOf(pFtd.getQttValue())
				);
				
				// The new values
				pRes.setQttUnitModified(qttUnit);
				pRes.setQttValueModified(qttValue);
				pRes.setPriceUnitSales(salePrice);
				pRes.setPriceUnitMeasure(measurePrice);
				
				// Save this new result
				addProductResult(pRes);
				
			} 
			// Should never happen and will be removed later (14.12.14)
			else {
				Log.e(logHead, "Could not get selected product id !!!");
			}			
			
		} finally {
			
			if (pRes != null) {
				
				RunPdtExpect.fillInitViewsWithValues(
					pRes.getQttUnitModified(),
					Double.toString(pRes.getQttValueModified()), 
					Double.toString(pRes.getPriceUnitSales()), 
					Double.toString(pRes.getPriceUnitMeasure()), 
					getReferenceProductById(pRes.getProductRefId()).getPdtTvaTaux()
				);
				
			}
			
		}
		
	}

	
	private static void updateProductResultFromInitValues (int index, String qttUnit, double qttValue, double salePrice, double measurePrice) {

		Log.d(logHead, "updateProductResultFromInitValues() ->");
		
		ProduitResult pRes = productsResults.get(index);
		// Then record new values
		pRes.setQttUnitModified(qttUnit);
		pRes.setQttValueModified(qttValue);
		pRes.setPriceUnitSales(salePrice);
		pRes.setPriceUnitMeasure(measurePrice);
	}
	
	
	
	
	
	/**
	 * 
	 * Produit Expected State
	 * 
	 */	
	
	/**
	 * This method is responsible for updating the ProduitInit
	 * fragment with the data from the new selected product 
	 * if not null. First check if there's any existing result, 
	 * else get back original data from the ProduitDetailFrontend.
	 * Get it by calling getReferenceProductById() . 
	 */
	
	
	private static void updatePdtInitStatsOnExpectedViews(ProduitResult pRes) {
		
		Log.d(logHead, "updatePdtInitStatsOnExpectedViews() ->");
		
		RunPdtExpect.fillInitViewsWithValues(
			pRes.getQttUnitModified(), 
			Double.toString(pRes.getQttValueModified()), 
			Double.toString(pRes.getPriceUnitSales()), 
			Double.toString(pRes.getPriceUnitMeasure()),
			getReferenceProductById(pRes.getProductRefId()).getPdtTvaTaux()
		);
	}
	
	private static void updatePdtExpectedViews(ProduitResult pRes) {

		Log.d(logHead, "updatePdtExpectedViews() ->");
		
		RunPdtExpect.fillExpectedViewsWithValues(
			Double.toString(pRes.getQttCarried()),
			Double.toString(pRes.getPriceHTVA()),
			Double.toString(pRes.getTvaValue()),
			Double.toString(pRes.getPriceTTC())
		);
		
	}
			
	
	public static void recordExpectedResult (double newQttCarried, double newPriceHTVA, double newTvaValue, double newTTC) {
		
		Log.d(logHead, "recordExpectedResult() ->");
		
		// First get the corresponding result
		Long pdtId = getSelectedProductId();
		int index = -1 ;		
		ProduitResult pRes = null ;
		
		try {
			// Look if we have an existing result
			index = RunProduit.resultIndexMap.get(pdtId);
			
			// Si un resultat existe déjà, MAJ
			if (index > -1) {
				pRes = productsResults.get(index);
				
				
				if (pRes.getQttCarried() > 0) {
					updateProductResultFromExpectedValues(pRes, newQttCarried, newPriceHTVA, newTvaValue, newTTC);
				} else {
					initiateExpectedResult(pRes, newQttCarried, newPriceHTVA, newTvaValue, newTTC);
				}
				
			}

			
			
		} catch (Exception e) {
			Log.e(logHead, "Could not record Expected Result cause : " + e.getMessage());
//			e.printStackTrace();		
		}
		
		
		
	}
	
	
	private static void initiateExpectedResult (ProduitResult pRes, double newQttCarried, 
			double newPriceHTVA, double newTvaValue, double newTTC) {
		
		Log.d(logHead, "initiateExpectedResult() ->");
		
		pRes.setTvaTx(RunProduit.getReferenceProductById(pRes.getProductRefId()).getPdtTvaTaux());
		
		pRes.setQttCarried(newQttCarried);
		pRes.setPriceHTVA(newPriceHTVA);
		pRes.setTvaValue(newTvaValue);
		pRes.setPriceTTC(newTTC);
		
	}
	
	private static void updateProductResultFromExpectedValues (ProduitResult pRes, 
			double newQttCarried, double newPriceHTVA, double newTvaValue, double newTTC) {
		
		Log.d(logHead, "updateProductResultFromExpectedValues() ->");
		
		pRes.setQttCarried(newQttCarried);
		pRes.setPriceHTVA(newPriceHTVA);
		pRes.setTvaValue(newTvaValue);
		pRes.setPriceTTC(newTTC);
	}	
	
	


	
	
}
