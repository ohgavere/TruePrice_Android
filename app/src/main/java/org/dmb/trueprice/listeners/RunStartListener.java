package org.dmb.trueprice.listeners;

import org.dmb.trueprice.R;
import org.dmb.trueprice.RunListeStart;
import org.dmb.trueprice.RunProduit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public final class RunStartListener implements View.OnClickListener {
	
	private Context initiator ;
//	Class<Login>
	
	public RunStartListener( Context ctx ) {
		initiator = ctx ;
	}

	@Override
	public void onClick(View v) {
		
		Button button = (Button) v ;
		
		if (button != null) {
			
			Intent i  ;
			
			switch (button.getId()) {
			
				case R.id.btn_pdt_start_catg	:
					
					((RunListeStart) initiator).attemptStartByCatg();
					
					break;
			
				case R.id.btn_pdt_start_alpha	:
				case R.id.btn_pdt_start_state	:
															
					((RunListeStart) initiator).startActivity(
						new Intent( initiator, RunProduit.class)
					);
					
					break;

			default:
				break;
			}
			
		} else {
			
			Log.e(this.getClass().getSimpleName(), "Could not identify event source : " + v.getId());
		}
		
	}

}
