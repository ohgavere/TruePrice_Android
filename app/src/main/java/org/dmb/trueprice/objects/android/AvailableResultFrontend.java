package org.dmb.trueprice.objects.android;

import org.dmb.trueprice.objects.AvailableResult;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

public class AvailableResultFrontend extends AvailableResult implements Checkable {
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	TextView txtView ;
	CheckBox chkView ;
	
	boolean	checked = false;

	public AvailableResultFrontend(Long listeId, String listeLabel, String date, int pdtCount) {
		super(listeId, listeLabel, date, pdtCount);
	}

	public AvailableResultFrontend(Long listeId, String listeLabel, String date) {
		super(listeId, listeLabel, date);
	}
	
	public AvailableResultFrontend(boolean checked, Long listeId, String listeLabel, String date, int pdtCount) {
		super(listeId, listeLabel, date, pdtCount);
		setChecked(checked);		
//		Log.d(logHead, "Init with boolean [" + checked +"]");
	}

	/*		View getters 	*/
	
	public CheckBox getCheckBoxView() { return chkView ;}
	
	public TextView getLabelView() { return txtView ;}
	
	public String getLabelValue() { return txtView.getText().toString() ;}
	
	/*		View setters	*/
	
	public void setCheckBox(CheckBox c) { 	
//		Log.d(logHead, "setChkBox checked?[" + c.isChecked() +"]");
		// recupere le checkBox
		this.chkView = c; 
		// Si la valeur actuelle est differente de celle recue par le chkBox, update()
		if (c.isChecked() != this.isChecked()) {
//			Log.d(logHead, "\t Need update : chkBox=[" + c.isChecked() 
//					+"]\t this=[" + this.isChecked() +"]");
			this.setChecked(c.isChecked());
		}
//		Log.d(logHead, "this.checked updated : isChecked()?[" + this.isChecked() +"]");
	}
	
	public void setLabelView(TextView t) { this.txtView = t;}	
	
	public void setLabelValue(String Value) {
		txtView.setText(Value);		
	}
	
	
	private void setChkBoxChecked(boolean b) {
//		Log.d(logHead, "Ask update chkBox state to [" + b +"] ... ");
		if (this.chkView != null) { 
			this.chkView.setChecked(b);
//			Log.d(logHead, " ... OK.");
		} else {
//			Log.d(logHead, " ... but chkBox View is null");
		}
		
//		if (this.chkView == null) { this.chkView = new CheckBox(null); }
//		this.chkView.setChecked(b);
	}
	
	/*
	 * CHECKABLE interface
	 * @see android.widget.Checkable#setChecked(boolean)
	 */
	
	@Override
	public void setChecked(boolean c) {
//		Log.d(logHead, "setChecked() [" + c +"]");
		this.checked = c;
//		this.chkView.setChecked(c);
		setChkBoxChecked(c);
	}

	@Override
	public boolean isChecked() {
		return this.checked;
	}

	@Override
	public void toggle() {
		// On inverse
		this.checked = !isChecked();
		// On update le ChkBoxView
		setChkBoxChecked(isChecked());		
	}
	
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() 
			+ " \t ID:[" + getListeId() +"]"
			+ " \t LABEL:[" + getListeLabel() +"]"
			+ " \t SELECTED:[" + isChecked() +"]"			
			+ " \n PDT_COUNT:[" + getPdtCount() +"]"
			+ " \t DATE_RELEASE:[" + getDate() +"]";
	}

}
