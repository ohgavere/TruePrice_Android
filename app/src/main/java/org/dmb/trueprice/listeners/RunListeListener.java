package org.dmb.trueprice.listeners;

import org.dmb.trueprice.R;
import org.dmb.trueprice.RunListe;
import org.dmb.trueprice.RunListeStart;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public final class RunListeListener implements View.OnClickListener {
	
	private Context initiator ;
	
	public final static String ATT_DIRECTION_START = "START" ;
	public final static String ATT_DIRECTION_REVIEW = "REVIEW" ;
	private static String runDirection ;
	
	private final String logHead = this.getClass().getSimpleName();
	
	static {
		runDirection = ATT_DIRECTION_REVIEW ;					
	}
		
	
	public RunListeListener( Context ctx ) {
		initiator = ctx ;
	}

	@Override
	public void onClick(View v) {
		
		Button button = (Button) v ;
		
		if (button != null) {
			
//			Intent i  ;
			
			switch (button.getId()) {
			
				case R.id.btn_run_liste_start:
					
					
					enableStartSetDirection(true);
					
				break;
					
				case R.id.btn_run_liste_review:
					
					enableReviewSetDirection(true);
					
				break;


			default:
				break;
			}
			
		} else {
			
			Log.e(this.getClass().getSimpleName(), "Could not identify event source : " + v.getId());
		}
		
	}

	public void enableStartSetDirection(boolean enabled) {
		Log.d(logHead, "enableStartSetDirection [" + enabled + "]");
		if (enabled == true) { 
			disableReview();
//			enableReview();
			enableStart();
//			disableStart();
			setDirectionStartList();
		} else {
			disableStart();
			enableReview();
		}
	}
	public void enableReviewSetDirection(boolean enabled) {
		Log.d(logHead, "enableReviewSetDirection [" + enabled + "]");
		if (enabled == true) {
			disableStart();
			enableReview();
			setDirectionReviewList();
		} else {
			disableReview();
			enableStart();
		}
	}
	
	public void setDirectionStartList() {
		RunListeListener.runDirection = ATT_DIRECTION_START ;
	}
	public void setDirectionReviewList() {
		RunListeListener.runDirection = ATT_DIRECTION_REVIEW ;
	}		
	public static String getDirection() {		
		return runDirection;		
	}
	
	@SuppressLint("NewApi")
	private void enableStart() {
		Button btt = (Button) ((RunListe)this.initiator).findViewById(R.id.btn_run_liste_start);
		btt.setEnabled(false);
		
		ListView lv = (ListView) ((RunListe)this.initiator).findViewById(R.id.run_listview_start_continue);
		lv.setEnabled(true);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			lv.setAlpha(100);	
		}		
	}
	@SuppressLint("NewApi")
	private void disableStart() {
		Button btt = (Button) ((RunListe)this.initiator).findViewById(R.id.btn_run_liste_start);
		btt.setEnabled(true);
		
		ListView lv = (ListView) ((RunListe)this.initiator).findViewById(R.id.run_listview_start_continue);
		lv.setEnabled(false);	
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			lv.setAlpha(50);	
		}			
			
	}
	
	@SuppressLint("NewApi")
	private void enableReview() {
		Button btt = (Button) ((RunListe)this.initiator).findViewById(R.id.btn_run_liste_review);
		btt.setEnabled(false);
		
		ListView lv = (ListView) ((RunListe)this.initiator).findViewById(R.id.run_listview_review);
		lv.setEnabled(true);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			lv.setAlpha(100);	
		}			
	}
	@SuppressLint("NewApi")
	private void disableReview() {
		Button btt = (Button) ((RunListe)this.initiator).findViewById(R.id.btn_run_liste_review);
		btt.setEnabled(true);
		
		ListView lv = (ListView) ((RunListe)this.initiator).findViewById(R.id.run_listview_review);
		lv.setEnabled(false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			lv.setAlpha(50);	
		}			
	}
}
