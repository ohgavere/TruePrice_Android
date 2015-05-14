package org.dmb.trueprice.fragments;

import java.text.ParseException;

import org.dmb.trueprice.R;
import org.dmb.trueprice.RunProduit;
import org.dmb.trueprice.listeners.RunPdtInitListener;
import org.dmb.trueprice.tools.Calculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RunPdtInit extends Fragment  {
	
	private final String logHead = this.getClass().getSimpleName();
	
	static RunPdtInitListener lstn ;
	
	
//	private TextView txtStatPriceMinus1 ;
//	private TextView txtStatPriceMinus2 ;
//	private TextView txtStatPriceMin ;
//	private TextView txtStatPriceMax ;
//	private TextView txtStatPriceAverage ;
//	private TextView txtStatPriceMost ;
	
	public static TextView txtQttUnit ;
	public static TextView txtQttValue ;
	public static EditText txtPriceUnitSales ;
	public static EditText txtPriceUnitMeasure ;

    private static ImageView targetSwitcher ;
	
	private static TextWatcher watcher = null ;
	
	public enum TargetView { QTT,SALE,MEASURE };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		
		 View rootView = inflater.inflate(R.layout.fragment_run_pdt_a_init, container, false);
//         Bundle args = getArguments();
//         ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                 Integer.toString(args.getInt(ARG_OBJECT)));
//         return rootView;						
		 
		 RunPdtInit.txtQttUnit 	= (TextView) rootView.findViewById(R.id.pdt_init_txt_qtt_unit) ;
		 RunPdtInit.txtQttValue 	= (TextView) rootView.findViewById(R.id.pdt_init_txt_qtt_value) ;
		 RunPdtInit.txtPriceUnitSales 	= (EditText) rootView.findViewById(R.id.pdt_init_txt_price_unit_sales) ;
		 RunPdtInit.txtPriceUnitMeasure 	= (EditText) rootView.findViewById(R.id.pdt_init_txt_price_unit_measure) ;
		 
		 RunPdtInit.targetSwitcher = (ImageView) rootView.findViewById(R.id.img_switch_price_type) ;

		/**
		 * 	BUTTONS Listeners
		 */
		 
		 lstn = new RunPdtInitListener(getActivity());
				
		 Button btn= (Button) rootView.findViewById(R.id.btn_pdt_init_prix);
		
		 btn.setOnClickListener(lstn);
		
		 btn = (Button) rootView.findViewById(R.id.btn_pdt_init_rdc);
		
		 btn.setOnClickListener(lstn);
		
//		btn = (Button) rootView.findViewById(R.id.btn_pdt_init_forward);
		
//		btn.setOnClickListener(lstn);	
				
		
		
		// Pour savoir quel editText a été modifié en dernier
//		RunPdtInit.txtQttUnit.setOnFocusChangeListener(lstn);
		RunPdtInit.txtQttValue.setOnFocusChangeListener(lstn);
		RunPdtInit.txtPriceUnitSales.setOnFocusChangeListener(lstn);
		RunPdtInit.txtPriceUnitMeasure.setOnFocusChangeListener(lstn);
		
		
		
//		watcher  = new TextWatcher() {};
//		watcher = new InitResultTextWatcher();
		watcher = lstn ;
		
		// Récupérer la nouvelle valeur si possible, puis mettre à jour les autres champs
//		RunPdtInit.txtQttUnit.addTextChangedListener(watcher);
		RunPdtInit.txtQttValue.addTextChangedListener(watcher);
		RunPdtInit.txtPriceUnitSales.addTextChangedListener(watcher);
		RunPdtInit.txtPriceUnitMeasure.addTextChangedListener(watcher);


        targetSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchEditableTarget();
                lstn.switchCalcTarget();
            }
        });

		return rootView;
		
	}

	public static void setTextToPriceSale (String s) throws ParseException {
		// Mettre a jour l'UI
		RunPdtInit.txtPriceUnitSales.setText(s);
		// Enregistrer la nouvelle valeur
		RunProduit.recordInitResult(
			getTextQttUnit(),
			Calculator.getSafeDoubleFromString(getTextQttValue()),
			Calculator.getSafeDoubleFromString(getTextPriceSale()),
			Calculator.getSafeDoubleFromString(getTextPriceMeasure())
		);
	}
	
	public static void setTextToPriceMeasure (String s) throws ParseException {
		// Mettre a jour l'UI
		RunPdtInit.txtPriceUnitMeasure.setText(s);
		// Enregistrer la nouvelle valeur
		RunProduit.recordInitResult(
			getTextQttUnit(),
			Calculator.getSafeDoubleFromString(getTextQttValue()),
			Calculator.getSafeDoubleFromString(getTextPriceSale()),
			Calculator.getSafeDoubleFromString(getTextPriceMeasure())
		);
	}
	
	public static void setTextToQttValue (String s) throws ParseException {
		// Mettre a jour l'UI
		RunPdtInit.txtQttValue.setText(s);
		// Enregistrer la nouvelle valeur
		RunProduit.recordInitResult(
			getTextQttUnit(),
			Calculator.getSafeDoubleFromString(getTextQttValue()),
			Calculator.getSafeDoubleFromString(getTextPriceSale()),
			Calculator.getSafeDoubleFromString(getTextPriceMeasure())
		);
	}


    private void switchEditableTarget () {

        if (RunPdtInit.txtPriceUnitSales.isEnabled()) {
            RunPdtInit.txtPriceUnitSales.setEnabled(false);
            RunPdtInit.txtPriceUnitMeasure.setEnabled(true);

        } else if (RunPdtInit.txtPriceUnitMeasure.isEnabled()) {
            RunPdtInit.txtPriceUnitMeasure.setEnabled(false);
            RunPdtInit.txtPriceUnitSales.setEnabled(true);
        }

    }
	

	
	public static void fillViewsWithValues( String qttUnit, String qttValue, String priceUnitSale, String priceUnitMeasure) {
		
		txtQttUnit.removeTextChangedListener(watcher);
		txtQttValue.removeTextChangedListener(watcher);
		txtPriceUnitSales.removeTextChangedListener(watcher);
		txtPriceUnitMeasure.removeTextChangedListener(watcher);
		
		txtQttUnit.setText(qttUnit);
		txtQttValue.setText(qttValue);
		txtPriceUnitSales.setText(priceUnitSale);
		txtPriceUnitMeasure.setText(priceUnitMeasure);		
		
		txtQttUnit.addTextChangedListener(watcher);
		txtQttValue.addTextChangedListener(watcher);
		txtPriceUnitSales.addTextChangedListener(watcher);
		txtPriceUnitMeasure.addTextChangedListener(watcher);
	}
	

	private static String getTextQttUnit () {
		return txtQttUnit.getText().toString();
	}
	private static String getTextQttValue () {
		return txtQttValue.getText().toString();
	}
	private static String getTextPriceSale () {
		return txtPriceUnitSales.getText().toString();
	}
	private static String getTextPriceMeasure () {
		return txtPriceUnitMeasure.getText().toString();
	}	
	
}
