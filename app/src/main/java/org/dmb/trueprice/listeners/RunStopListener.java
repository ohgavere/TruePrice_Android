package org.dmb.trueprice.listeners;

import org.dmb.trueprice.R;
import org.dmb.trueprice.RunProduit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public final class RunStopListener implements View.OnClickListener {
	
	private Context initiator ;
//	Class<Login>
	
	public RunStopListener( Context ctx ) {
		initiator = ctx ;
	}

	@Override
	public void onClick(View v) {
		
		Button button = (Button) v ;
		
		if (button != null) {
			
			Intent i  ;
			
			switch (button.getId()) {
			
				case R.id.btn_pdt_start_alpha	:
				case R.id.btn_pdt_start_catg	:
				case R.id.btn_pdt_start_state	:
					
//					i = new Intent( initiator, RunPdtInit.class);
					i = new Intent( initiator, RunProduit.class);
					
//					i.putExtra(Launcher.FLAG_SIGNED_IN, Launcher.getSigninStatus());
					
//					((Activity) initiator).startActivityForResult(i, Launcher.REQID_LOGIN_RESULT);		
					
					((Activity) initiator).startActivity(i);					
					
					break;

			default:
				break;
			}
			
		} else {
			
			Log.e(this.getClass().getSimpleName(), "Could not identify event source : " + v.getId());
		}
		
	}

}
