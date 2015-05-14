package org.dmb.trueprice.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

import org.dmb.trueprice.R;
import org.dmb.trueprice.RunPdtCalc;
import org.dmb.trueprice.fragments.RunPdtInit;
import org.dmb.trueprice.fragments.RunPdtInit.TargetView;
import org.dmb.trueprice.tools.Calculator;

import java.text.ParseException;

//import org.dmb.trueprice.RunPdtExpect;

public final class RunPdtInitListener_old implements View.OnClickListener, OnFocusChangeListener, TextWatcher
//, OnItemClickListener
{

	private static final String logHead = RunPdtInitListener_old.class.getSimpleName();

	private Context initiator ;
//	Class<Login>

	public RunPdtInitListener_old(Context ctx) {
		initiator = ctx ;
	}

	@Override
	public void onClick(View v) {
		
		Button button = (Button) v ;
		
		if (button != null) {
			
			Intent i  ;
			
			switch (button.getId()) {
			
				case R.id.btn_pdt_init_prix	:
				case R.id.btn_pdt_init_rdc :
					
					i = new Intent( initiator, RunPdtCalc.class);

					((Activity) initiator).startActivity(i);					
					
				break;					
//					
//				case R.id.btn_pdt_init_forward :
//					
//					i = new Intent( initiator, RunPdtExpect.class);
//					
////					i.putExtra(Launcher.FLAG_SIGNED_IN, Launcher.getSigninStatus());
//					
////					((Activity) initiator).startActivityForResult(i, Launcher.REQID_LOGIN_RESULT);		
//					
//					((Activity) initiator).startActivity(i);					
//					
//					break;

			default:
				break;
			}
			
		} else {
			
			Log.e(this.getClass().getSimpleName(), "Could not identify event source : " + v.getId());
		}
		
	}

	
	
	
	private static int lastFocusedViewId ;
	public static int getLastFocusedViewId() {
		return lastFocusedViewId;
	}
	private void setLastFocusedViewId(int lastFocusedViewId) {
		RunPdtInitListener_old.lastFocusedViewId = lastFocusedViewId;
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus == true) {
            Log.d(logHead, "This view earned focus : [" + v.getId() + "]");
            setLastFocusedViewId(v.getId());
        } else {
            Log.d(logHead, "This view lost focus : [" + v.getId() + "]");
        }
//		else if (hasFocus == false){
//
//			try {
//				
//				
//				int vid = v.getId();
//				if (vid == RunPdtInit.txtQttValue.getId()
//					|| vid == RunPdtInit.txtPriceUnitSales.getId()
//					|| vid == RunPdtInit.txtPriceUnitMeasure.getId()
//				) {
//					
//					String value = ((TextView)v).getText().toString() ;		
//					
//					if (value != null) {
//						value = Double.toString(Calculator.getSafeDoubleFromString(value.toString()));
//					}
//					
//					if (value != null) {
//						
//						if (vid == RunPdtInit.txtQttValue.getId()) {
//							
////							RunPdtInit.txtQttValue.removeTextChangedListener(this);
//							RunPdtInit.txtQttValue.setText(value);
////							RunPdtInit.txtQttValue.addTextChangedListener(this);						
//							
//						} else if (vid == RunPdtInit.txtPriceUnitSales.getId()) {
//							
////							RunPdtInit.txtPriceUnitSales.removeTextChangedListener(this);
//							RunPdtInit.txtPriceUnitSales.setText(value);
////							RunPdtInit.txtPriceUnitSales.addTextChangedListener(this);
//							
//						} else if (vid == RunPdtInit.txtPriceUnitMeasure.getId()) {
//							
////							RunPdtInit.txtPriceUnitMeasure.removeTextChangedListener(this);
//							RunPdtInit.txtPriceUnitMeasure.setText(value);
////							RunPdtInit.txtPriceUnitMeasure.addTextChangedListener(this);
//							
//						}				
//						
//					}
//					
//				}				
//				
//			} catch (ParseException e) {
//				Log.w(logHead, "Could not round value for view with id [" + v.getId() + "] : " + e.getMessage());
//			} catch (Exception e1) {
//				Log.e(logHead, "FATAL ERROR : " + e1.getMessage());
//				e1.printStackTrace();
//			}
//			
//		}
	}


//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
//
//		Toast.makeText(initiator, "Your selected position = " + position + 
//				" with id = " + id + " correspondnig to : " +
//				((Activity) initiator).getResources().getResourceName(Integer.valueOf(Float.toString(id)))				
//				
//			, Toast.LENGTH_SHORT).show();		
//		
//	}
	
	
	
	
	
/**
 * 
 * 		TEXT WATCHER
 * 	
 */
	
	
	int lastViewTried = -1 ;
	int lastLength = -1 ;	
	
//	final String [] targets =  new String [] {"qtt","sale","measure"} ;
	TargetView lastTargetView = null ;
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	
	@Override
	public void afterTextChanged(Editable s) {			
		
		int currentFocusedView = RunPdtInitListener_old.getLastFocusedViewId() ;
		
		boolean parseable = false ;
		
		
		
		boolean toMeasurePrice = false ;
		boolean toSalePrice = false ;
		boolean toQttValue = false ;
		
		double actualQttValue = -1 ;
		double actualPriceMeasure = -1 ;
		double actualPriceSale = -1 ;
		
		double newQttValue = -1 ;
		double newPriceSale = -1 ;
		double newPriceMeasure = -1 ;
		
//		Log.d(logHead, "afterTextChanged() ->\n"
//			+ " last ID [ " + lastViewTried + " ] <=> last Length [" + lastLength + "]\n"
//			+ " curr ID [ " + currentFocusedView + " ] <=> curr Length [" + s.length() + "]"
//		);
		
		if (// premier tour
			(lastViewTried == -1 && lastLength == -1)
			// OU 
			||
			// la derniere vue essayée n'est pas la même 
			(lastViewTried != currentFocusedView )
			//OU
			||
			// que la nouvelle valeur est plus ou moins longue
			(lastLength != s.length() && s.length() > 0)
		   ) 
		{
			
			// On essaye de convertir 
			
			lastViewTried = currentFocusedView;
			lastLength = s.length();
			
			// Convertir les valeurs necessaires selon le cas
			try {
	
//				Log.d(logHead, "Try convert for " + currentFocusedView);
				
			// From QttValue to Sale/Measure Price
				if (currentFocusedView == RunPdtInit.txtQttValue.getId()) {
					
					// reference values
					try {
						actualPriceMeasure = Calculator.getSafeDoubleFromString(RunPdtInit.txtPriceUnitMeasure.getText().toString());
					} catch (Exception e) {}
					try {
						actualPriceSale = Calculator.getSafeDoubleFromString(RunPdtInit.txtPriceUnitSales.getText().toString());
					} catch (Exception e) {}
					
					if (actualPriceMeasure 	> 0 || actualPriceSale 		== -1) { toSalePrice 	= true ; }
					if (actualPriceSale 	> 0 || actualPriceMeasure 	== -1) { toMeasurePrice	= true ; }
					
					if (toSalePrice == true && toMeasurePrice == true) {
						if (lastTargetView == TargetView.MEASURE) {
							toSalePrice = false ;
						} else if (lastTargetView == TargetView.SALE || lastTargetView == null) {
							toMeasurePrice = false ;
						}
					}
						
					// currently updated value
					newQttValue = Calculator.getSafeDoubleFromString(s.toString());
	
					// should modify this value
					if (toSalePrice == true) {
						newPriceSale 	= Calculator.fromMeasureToSale(newQttValue, actualPriceMeasure);
						
//						Log.d(logHead, "New Qtt [" + newQttValue + "] ... from actual Price_Measure [" + actualPriceMeasure 
//								+ "] ... Gives new Price_Sale [ " + newPriceSale + " ]"
//							); 
						
					} else if (toMeasurePrice == true) {
						newPriceMeasure = Calculator.fromSaleToMeasure(newQttValue, actualPriceSale);
						
//						Log.d(logHead, "New Qtt [" + newQttValue + "] ... from actual Price_Sale [" + actualPriceSale
//								+ "] ... Gives new Price_Measure [ " + newPriceMeasure + " ]"
//							); 
					}
					
					// If at least one could be calculated
					if (
						(
							(toMeasurePrice == true && newPriceMeasure > 0)
						||
							(toSalePrice 	== true && newPriceSale > 0) 
						)
						&& newQttValue > 0
					) {
						parseable = true ;
					}
	
					
					
			// From SalePrice to MeasurePrice/Qtt
				} else if (currentFocusedView == RunPdtInit.txtPriceUnitSales.getId()) {
							
												
					// reference values
					try {
						actualPriceMeasure = Calculator.getSafeDoubleFromString(RunPdtInit.txtPriceUnitMeasure.getText().toString());
					} catch (Exception e) {}
					try {
						actualQttValue = Calculator.getSafeDoubleFromString(RunPdtInit.txtQttValue.getText().toString());
					} catch (Exception e) {}
					
					if (actualPriceMeasure 	> 0 || actualQttValue		== -1) { toQttValue 	= true ; }
					if (actualQttValue 		> 0 || actualPriceMeasure 	== -1) { toMeasurePrice	= true ; }
					
					if (toQttValue == true && toMeasurePrice == true) {
						if (lastTargetView == TargetView.MEASURE) {
							toQttValue = false ;
						} else if (lastTargetView == TargetView.QTT || lastTargetView == null) {
							toMeasurePrice = false ;
						}
					}							
					
					// Currently updated value 
					newPriceSale = Calculator.getSafeDoubleFromString(s.toString());				
									
					// should modify this value
					if (toQttValue == true) {
						newQttValue = Calculator.fromPricesToQtt(actualPriceMeasure, newPriceSale);
						
//						Log.d(logHead, "New Price_Sale [" + newPriceSale + "] ... from actual Price_Measure [" + actualPriceMeasure 
//								+ "] ... Gives new Qtt [ " + newQttValue + " ]"
//							); 
						
					} else if (toMeasurePrice == true) {
						newPriceMeasure = Calculator.fromSaleToMeasure(actualQttValue, newPriceSale);
						
//						Log.d(logHead, "New Price_Sale [" + newPriceSale + "] ... from actual Qtt [" + actualQttValue
//								+ "] ... Gives new Price_Measure [ " + newPriceMeasure + " ]"
//							); 
					}							
	
					
					// If at least one could be calculated
					if (
						(
							(toMeasurePrice == true && newPriceMeasure > 0)
						||
							(toQttValue 	== true && newQttValue > 0) 
						)
						&& newPriceSale > 0
					) {
						parseable = true ;
					}							
					
					
			// From  From MeasurePrice to SalePrice
				} else if (currentFocusedView == RunPdtInit.txtPriceUnitMeasure.getId()) {		
												
					// reference values
					try {
						actualPriceSale = Calculator.getSafeDoubleFromString(RunPdtInit.txtPriceUnitSales.getText().toString());
					} catch (Exception e) {}
					try {
						actualQttValue = Calculator.getSafeDoubleFromString(RunPdtInit.txtQttValue.getText().toString());
					} catch (Exception e) {}
					
					if (actualPriceSale		> 0 || actualQttValue	== -1) { toQttValue  = true ; }
					if (actualQttValue 		> 0 || actualPriceSale 	== -1) { toSalePrice = true ; }
					
					if (toQttValue == true && toSalePrice == true) {
						if (lastTargetView == TargetView.SALE) {
							toQttValue = false ;
						} else if (lastTargetView == TargetView.QTT || lastTargetView == null) {
							toSalePrice = false ;
						}
					}							
					
					// Currently updated value 			
					newPriceMeasure = Calculator.getSafeDoubleFromString(s.toString());		
					
					
					// should modify this value
					if (toQttValue == true) {
						newQttValue = Calculator.fromPricesToQtt(newPriceMeasure, actualPriceSale);
						
//						Log.d(logHead, "New Price_Measure [" + newPriceMeasure + "] ... from actual Price_Sale [" + actualPriceSale
//								+ "] ... Gives new Qtt [ " + newQttValue + " ]"
//							); 
						
					} else if (toSalePrice == true) {
						newPriceSale = Calculator.fromMeasureToSale(actualQttValue, newPriceMeasure);
						
//						Log.d(logHead, "New Price_Measure [" + newPriceMeasure + "] ... from actual Qtt [" + actualQttValue
//								+ "] ... Gives new Price_Sale [ " + newPriceSale + " ]"
//							); 
					}							
					
					
					// If at least one could be calculated
					if (
						(
							(toSalePrice == true && newPriceSale > 0)
						||
							(toQttValue  == true && newQttValue  > 0) 
						)
						&& newPriceMeasure > 0
					) {
						parseable = true ;
					}									
												
					
			// In case of ...
				} else {
					parseable = false;
				}
						
				
		// Then update if conversions succeeded		
				
//				Log.d(logHead, "Try to update impacted values.");
				
				// If got effective parsed data, update views
				if (parseable == true) {
					
					try {
						
						//  From QttValue to SalePrice
						if (currentFocusedView == RunPdtInit.txtQttValue.getId()) {

														
							
							if (toSalePrice == true) {
								
//								Log.d(logHead, "New Qtt [" + newQttValue + "] ... " 
//									+ "from actual Price_Measure [" + actualPriceMeasure 
//									+ "] ... Gives new Price_Sale [ " + newPriceSale + " ]"
//								); 							
									
								RunPdtInit.txtPriceUnitSales.removeTextChangedListener(this);
									
								RunPdtInit.setTextToPriceSale(Double.toString(newPriceSale));
									
								RunPdtInit.txtPriceUnitSales.addTextChangedListener(this);
								
								lastTargetView = TargetView.SALE ;
								
							} else if (toMeasurePrice == true) {
								
//								Log.d(logHead, "New Qtt [" + newQttValue + "] ... " 
//									+ "from actual Price_Sale [" + actualPriceSale
//									+ "] ... Gives new Price_Measure [ " + newPriceMeasure + " ]"
//								); 							
									
								RunPdtInit.txtPriceUnitMeasure.removeTextChangedListener(this);
									
								RunPdtInit.setTextToPriceMeasure(Double.toString(newPriceMeasure));
									
								RunPdtInit.txtPriceUnitMeasure.addTextChangedListener(this);
								
								lastTargetView = TargetView.MEASURE ;
							}
							
							
						}
						
						// From SalePrice to MeasurePrice
						else if (currentFocusedView == RunPdtInit.txtPriceUnitSales.getId()) { 		
							
							if (toQttValue == true) {
								
//								Log.d(logHead, "New Price_Sale [" + newPriceSale + "] ... from actual Price_Measure [" + actualPriceMeasure 
//										+ "] ... Gives new Qtt [ " + newQttValue + " ]"
//									); 							
									
								RunPdtInit.txtQttValue.removeTextChangedListener(this);
									
								RunPdtInit.setTextToQttValue(Double.toString(newQttValue));
									
								RunPdtInit.txtQttValue.addTextChangedListener(this);
								
								lastTargetView = TargetView.QTT ;
								
							} else if (toMeasurePrice == true) {
								
//								Log.d(logHead, "New Price_Sale [" + newPriceSale + "] ... from actual Qtt [" + actualQttValue
//										+ "] ... Gives new Price_Measure  [ " + newPriceMeasure + " ]"
//									); 											
								
								RunPdtInit.txtPriceUnitMeasure.removeTextChangedListener(this);
								
								RunPdtInit.setTextToPriceMeasure(Double.toString(newPriceMeasure));
								
								RunPdtInit.txtPriceUnitMeasure.addTextChangedListener(this);	
								
								lastTargetView = TargetView.MEASURE ;
							}	
							
//							s = s.delete(0, s.length()) ;
//							s= s.append(Double.toString(Calculator.getSafeDoubleFromString(s.toString())));
//							
//							RunPdtInit.txtPriceUnitSales.removeTextChangedListener(this);
//							RunPdtInit.txtPriceUnitSales.setText(s);
//							RunPdtInit.txtPriceUnitSales.addTextChangedListener(this);
//																				
													
						}
						
						// From MeasurePrice to SalePrice
						else if (currentFocusedView == RunPdtInit.txtPriceUnitMeasure.getId()) { 									
							
							if (toQttValue == true) {
								
//								Log.d(logHead, "New Price_Measure [" + newPriceMeasure + "] ... from actual Price_Sale [" + actualPriceSale
//										+ "] ... Gives new Qtt [ " + newQttValue + " ]"
//									); 							
									
								RunPdtInit.txtQttValue.removeTextChangedListener(this);
									
								RunPdtInit.setTextToQttValue(Double.toString(newQttValue));
									
								RunPdtInit.txtQttValue.addTextChangedListener(this);
								
								lastTargetView = TargetView.QTT ;
								
								
							} else if (toSalePrice == true) {
																		
//								Log.d(logHead, "New Price_Measure [" + newPriceMeasure + "] ... from actual Qtt [" + actualQttValue
//									+ "] ... Gives new Price_Sale [ " + newPriceSale + " ]"
//								); 
								
								RunPdtInit.txtPriceUnitSales.removeTextChangedListener(this);
								
								RunPdtInit.setTextToPriceSale(Double.toString(newPriceSale));
								
								RunPdtInit.txtPriceUnitSales.addTextChangedListener(this);	
								
								lastTargetView = TargetView.SALE ;
							}										
							
//							s = s.delete(0, s.length()) ;
//							s= s.append(Double.toString(Calculator.getSafeDoubleFromString(s.toString())));
//							
//							RunPdtInit.txtPriceUnitMeasure.removeTextChangedListener(this);
//							RunPdtInit.txtPriceUnitMeasure.setText(s);
//							RunPdtInit.txtPriceUnitMeasure.addTextChangedListener(this);							
							
						}						
						
					} catch (ParseException e) {
						Log.d(logHead, "Not updated from " + currentFocusedView + " cause not parsed : " + e.getMessage());
					}
					
					// finally reset this flag
					parseable = false;
					
				} else {
					Log.d(logHead, "Not updating from " + currentFocusedView + " cause not parsed.");
				}					
				
				
			} catch (Exception e) {
				parseable = false ;
				Log.e(logHead, "Could not convert one or more double " 
					+ "values for last focused view : " + currentFocusedView 
					+ "\t ==> " +e.getMessage()
				);
//				e.printStackTrace();
			}
			
			
		}
		
}

	
		

}
