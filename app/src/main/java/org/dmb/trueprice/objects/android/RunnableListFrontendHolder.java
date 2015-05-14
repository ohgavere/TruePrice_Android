package org.dmb.trueprice.objects.android;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

public class RunnableListFrontendHolder  {
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	TextView txtVw_label ;
	
	TextView txtVw_nbProduit ;
	TextView txtVw_dtSync ;
	

	
	/* 
	 * Remplit les 4 TextView avec les valeurs fournies ou des valeurs par défaut
	 */
	public void fillViewsWithValues (String listeLabel, int pdtCount, String dateSync) {
		
		String strVal = Integer.toString(pdtCount);
		String count = ( strVal == null || pdtCount == -1 ) ? null : strVal ;

		Log.d(logHead, "FillViewsWithValues()-> label:[" + listeLabel +"]");

		this.setLabelValue(listeLabel);
		this.setNbProduitValue(count);
		this.setDateSyncValue(dateSync);			
		
		
	}

	
	
	/*		View getters 	*/
	
	
	public TextView getLabelView() 		{ return txtVw_label ;}
	public TextView getTxtVw_nbProduit(){ return txtVw_nbProduit; }
	public TextView getTxtVw_dtSync() 	{ return txtVw_dtSync; }
	
	public CharSequence getLabelValue() 		{ return txtVw_label.getText() ;}
	public CharSequence getDateSyncValue() 		{ return txtVw_dtSync.getText() ;}
	public CharSequence getNbProduitValue() 	{ return txtVw_nbProduit.getText() ;}	
	
	/*		View setters	*/
	
	public void setLabelView(TextView t) 		{ this.txtVw_label = t;}	
	public void setNbProduitView(TextView t) 	{ this.txtVw_nbProduit = t;}	
	public void setDateSyncView(TextView t) 	{ this.txtVw_dtSync = t;}	
	
	public void setLabelValue(String Value) {
		txtVw_label.setText(Value == null ? "" : Value);		
	}
	public void setNbProduitValue(String Value) {
		txtVw_nbProduit.setText(Value == null ? "" : Value);		
	}
	public void setDateSyncValue(String Value) {
		txtVw_dtSync.setText(Value == null ? "" : Value);		
	}	
	
	
}
