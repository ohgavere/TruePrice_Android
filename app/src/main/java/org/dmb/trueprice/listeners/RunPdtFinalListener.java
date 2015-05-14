package org.dmb.trueprice.listeners;

import org.dmb.trueprice.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public final class RunPdtFinalListener implements View.OnClickListener {
	
	private Context initiator ;
//	Class<Login>
	
	public RunPdtFinalListener( Context ctx) {
		initiator = ctx ;
	}

	@Override
	public void onClick(View v) {
		
		Button button = (Button) v ;
		
		if (button != null) {
			
			switch (button.getId()) {
			
//				case R.id.btn_pdt_final_backward	:
//					
//					((Activity) initiator).finish();
//					
//				break;					
					
				case R.id.btn_pdt_final_lock :
					
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
		
		View v = (View) ((Activity) initiator).findViewById(R.id.run_pdt_final_scrollwrapper);
		
		// Si les boutons sont activés on recoit false donc :
//		si false on met l'ombre 0>n>1
//		si true on remet a 0
		v.setAlpha(Float.valueOf( shadowView ? "1" : "0.5"));
				
		
	}
}
