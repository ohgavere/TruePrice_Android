package org.dmb.trueprice.objects.android;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

public class AvailableListFrontendHolder implements Checkable/*, OnClickListener*/ {
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	TextView txtVw_label ;
	CheckBox chkVw_select ;
	
	TextView txtVw_nbProduit ;
	TextView txtVw_dtSync ;
	TextView txtVw_dtModif ;
	

	
	/* 
	 * Remplit les 4 TextView avec les valeurs fournies ou des valeurs par défaut
	 */
	public void fillViewsWithValues (String listeLabel, String dateModif, int pdtCount, String dateSync, Boolean checked) {
		
		String strVal = Integer.toString(pdtCount);
		String count = ( strVal == null || pdtCount == -1 ) ? null : strVal ;

		Log.d(logHead, "FillViewsWithValues()-> label:[" + listeLabel +"]");

		this.setLabelValue(listeLabel);
		this.setNbProduitValue(count);
		this.setDateModifValue(dateModif);
		this.setDateSyncValue(dateSync);			
		
		setChecked(checked);
		
	}

	
	
	/*		View getters 	*/
	
	public CheckBox getCheckBoxView() 	{ return chkVw_select ;}
	
	public TextView getLabelView() 		{ return txtVw_label ;}
	public TextView getTxtVw_nbProduit(){ return txtVw_nbProduit; }
	public TextView getTxtVw_dtModif() 	{ return txtVw_dtModif; }
	public TextView getTxtVw_dtSync() 	{ return txtVw_dtSync; }
	
	public CharSequence getLabelValue() 		{ return txtVw_label.getText() ;}
	public CharSequence getDateModifValue() 	{ return txtVw_dtModif.getText() ;}
	public CharSequence getDateSyncValue() 		{ return txtVw_dtSync.getText() ;}
	public CharSequence getNbProduitValue() 	{ return txtVw_nbProduit.getText() ;}	
	
	/*		View setters	*/
	
	public void setCheckBox(CheckBox c) { 	
//		Log.d(logHead, "setChkBox checked?[" + c.isChecked() +"]");
		// recupere le checkBox
		this.chkVw_select = c; 
		// Si la valeur actuelle est differente de celle recue par le chkBox, update()
//		if (c.isChecked() != this.isChecked()) {
////			Log.d(logHead, "\t Need update : chkBox=[" + c.isChecked() 
////					+"]\t this=[" + this.isChecked() +"]");
//			this.setChecked(c.isChecked());
//		}
//		Log.d(logHead, "this.checked updated : isChecked()?[" + this.isChecked() +"]");
		
	}
	
	public void setLabelView(TextView t) 		{ this.txtVw_label = t;}	
	public void setNbProduitView(TextView t) 	{ this.txtVw_nbProduit = t;}	
	public void setDateSyncView(TextView t) 	{ this.txtVw_dtSync = t;}	
	public void setDateModifView(TextView t) 	{ this.txtVw_dtModif = t;}	
	
	public void setLabelValue(String Value) {
		txtVw_label.setText(Value == null ? "" : Value);		
	}
	public void setNbProduitValue(String Value) {
		txtVw_nbProduit.setText(Value == null ? "" : Value);		
	}
	public void setDateSyncValue(String Value) {
		txtVw_dtSync.setText(Value == null ? "" : Value);		
	}	
	public void setDateModifValue(String Value) {
		txtVw_dtModif.setText(Value == null ? "" : Value);		
	}
	
	private void setChkBoxChecked(boolean b) {
		this.chkVw_select.setChecked(b);
	}
	

	public void toggle() {
		setChecked( ! isChecked() );
//		setChkBoxChecked(isChecked());
		
	}
	

//	@Override
//	public void onClick(View v) {		
//		//liste.toggle();
//		
//		toggle();
//				
//	}

//	public void setListenerToViews(){
//		getCheckBoxView().setOnClickListener(this);
//		getLabelView().setOnClickListener(this);
//		getTxtVw_dtModif().setOnClickListener(this);
//		getTxtVw_dtSync().setOnClickListener(this);
//		getTxtVw_nbProduit().setOnClickListener(this);
//	}
	

	@Override
	public void setChecked(boolean checked) {
//		setChecked(checked);
		setChkBoxChecked(checked);
	}


	@Override
	public boolean isChecked() {
		return getCheckBoxView().isChecked();
	}

}
