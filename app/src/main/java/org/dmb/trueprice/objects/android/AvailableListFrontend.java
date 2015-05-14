package org.dmb.trueprice.objects.android;

import org.dmb.trueprice.Sync;
import org.dmb.trueprice.objects.AvailableList;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Checkable;

public class AvailableListFrontend extends AvailableList implements Checkable /*, OnClickListener */{
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	
	
	boolean	checked = false;

	public AvailableListFrontend(Long listeId, String listeLabel, String dateModif, int pdtCount) {
		super(listeId, listeLabel, dateModif, pdtCount);
	}

	
	public AvailableListFrontend(boolean checked, Long listeId, String listeLabel, String dateModif, int pdtCount) {
		super(listeId, listeLabel, dateModif, pdtCount);
		setChecked(checked);		
//		Log.d(logHead, "Init with boolean [" + checked +"]");
	}


	
	/*
	 * CHECKABLE interface
	 * @see android.widget.Checkable#setChecked(boolean)
	 */
	
	@Override
	public void setChecked(boolean c) {
//		Log.d(logHead, "setChecked() [" + c +"]");
		this.checked = c;
	}

	@Override
	public boolean isChecked() {
		return this.checked;
	}

	@Override
	public void toggle() {
		setChecked(!isChecked());
	}
	
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() 
			+ " \t ID:[" + getListeId() +"]"
			+ " \t LABEL:[" + getListeLabel() +"]"
			+ " \t SELECTED:[" + isChecked() +"]"			
			+ " \n PDT_COUNT:[" + getPdtCount() +"]"
			+ " \t DATE_MODIFIED:[" + getDate() +"]";
	}
	
	

//	@Override
//	public void onClick(View v) {
//
////		((AvailableListFrontendHolder) v).toggle();
//		
//		toggle();
//		
//		
//		if (isChecked() == true) {
//			
//			Sync.addSelectedId(getListeId());
//			Log.i(logHead, "Add selected_id [" + getListeId() +"] for liste label:[" + getListeLabel() +"]");
//		} else {
//			Sync.removeSelectedId(getListeId());
//			Log.i(logHead, "Del selected_id [" + getListeId() +"] for liste label:[" + getListeLabel() +"]");
//		}	
//	}

}
