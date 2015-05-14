package org.dmb.trueprice.listeners;

import org.dmb.trueprice.R;
//import org.dmb.trueprice.RunPdtFinal;



import org.dmb.trueprice.fragments.RunPdtExpect;
import org.dmb.trueprice.fragments.RunPdtInit;
import org.dmb.trueprice.tools.Calculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public final class RunPdtExpectListener implements View.OnClickListener, TextWatcher {
	
	private static final String logHead = RunPdtExpectListener.class.getSimpleName();
	
	private Context initiator ;
//	Class<Login>
	
	public RunPdtExpectListener( Context ctx) {
		initiator = ctx ;
	}

	@Override
	public void onClick(View v) {
		
		Button button = (Button) v ;
		
		if (button != null) {
			
			Intent i  ;
			
			switch (button.getId()) {
			
//				case R.id.btn_pdt_expc_backward	:
//					
//					((Activity) initiator).finish();
//					
//				break;
//				
//				case R.id.btn_pdt_expc_forward :
//					
//					i = new Intent( initiator, RunPdtFinal.class);
//
//					((Activity) initiator).startActivity(i);					
//					
//				break;					
//					
				case R.id.btn_pdt_expc_lock :
										
//					i.putExtra(Launcher.FLAG_SIGNED_IN, Launcher.getSigninStatus());
					
//					((Activity) initiator).startActivityForResult(i, Launcher.REQID_LOGIN_RESULT);		
										
					
					RevertAccess();
					
					
					break;

			default:
				break;
			}
			
		} else {
			
			Log.e(this.getClass().getSimpleName(), "Could not identify event source : " + v.getId());
		}
		
	}

	private boolean locked = false ;
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	private boolean isLocked() {
		return locked;
	}
	
	private void RevertAccess() {		
		
//		Button btn = (Button) ((Activity) initiator).findViewById(R.id.btn_pdt_expc_backward);
		
//		boolean actualState = btn.isEnabled() ;
		boolean actualState = isLocked();
		actualState = !actualState ;
		
		Toast.makeText(initiator.getApplicationContext(), 
			"Produit" + ( actualState ? " verrouillé " : " déverouillé" ),
			Toast.LENGTH_SHORT).show();
		
//		btn.setEnabled( ! actualState );
//		
//		btn = (Button) ((Activity) initiator).findViewById(R.id.btn_pdt_expc_forward);
//		
//		btn.setEnabled( ! actualState );		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) { 
			shadowView( actualState );
		}
		
		setLocked(actualState);
		
	}
	
	@SuppressLint("NewApi")
	private void shadowView(boolean shadowView) {
		
		View v = (View) ((Activity) initiator).findViewById(R.id.run_pdt_expc_scrollwrapper);
		
		// Si les boutons sont activés on recoit false donc :
//		si false on met l'ombre 0>n>1
//		si true on remet a 0
		v.setAlpha(Float.valueOf( shadowView ? "1" : "0.35"));
				
		
	}

	
	
	
	
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
	

	@Override
	public void afterTextChanged(Editable s) {

		boolean parseable = false ;
		
		double newQuantityCarried = -1 ;
		double newPriceHTVA = -1 ;
		double newTTC = -1 ;
		double newTvaValue = -1 ;
		
		
		try {
			
			
			try {
				
				newQuantityCarried = Calculator.getSafeDoubleFromString(s.toString());
				
				double basePriceSale = Calculator.getSafeDoubleFromString(RunPdtExpect.txtPriceInitSale.getText().toString());
				
				newPriceHTVA = newQuantityCarried * basePriceSale ;
				newPriceHTVA = Calculator.roundDoubleDecimal(newPriceHTVA);
				
				double baseTvaTx = Calculator.getSafeDoubleFromString(RunPdtExpect.txtTvaTx.getText().toString());
				
				newTvaValue = (newPriceHTVA * baseTvaTx) / 100 ;
				newTvaValue = Calculator.roundDoubleDecimal(newTvaValue);
				
				newTTC = newPriceHTVA + newTvaValue ;
				newTTC = Calculator.roundDoubleDecimal(newTTC);
								
				parseable = true ;
				
			} catch (Exception e) {				
				Log.w(logHead, "Could not parse at least one value : " + e.getMessage());
			}	
			
			
			
			if (parseable = true) { 
				
				RunPdtExpect.txtQttCarried.removeTextChangedListener(this);
				
				RunPdtExpect.updateView(newQuantityCarried, newPriceHTVA, newTvaValue, newTTC);
					
				RunPdtExpect.txtQttCarried.addTextChangedListener(this);				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
