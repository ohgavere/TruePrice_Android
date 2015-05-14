package org.dmb.trueprice.fragments;

import org.dmb.trueprice.R;
import org.dmb.trueprice.RunProduit;
import org.dmb.trueprice.listeners.RunPdtExpectListener;
import org.dmb.trueprice.listeners.RunPdtInitListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RunPdtExpect extends Fragment {
	
	private final String logHead = this.getClass().getSimpleName();

	static RunPdtExpectListener lstn ;
	
	public static TextView txtQttInitUnit ;
	public static TextView txtQttInitValue ;
	public static TextView txtPriceInitSale ;
	public static TextView txtPriceInitMeasure ;
	
	public static TextView txtQttCarried ;
	public static TextView txtHTVA ;
	public static TextView txtTvaTx ;
	public static TextView txtTvaValue ;
	public static TextView txtPriceTTC;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_run_pdt_a_init);
		
		
		 View rootView = inflater.inflate(R.layout.fragment_run_pdt_b_expect, container, false);
//         Bundle args = getArguments();
//         ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                 Integer.toString(args.getInt(ARG_OBJECT)));
//         return rootView;		
		
		 
		 RunPdtExpect.txtQttInitUnit 	= (TextView) rootView.findViewById(R.id.pdt_expc_txt_qtt_init_unit) ;
		 RunPdtExpect.txtQttInitValue	= (TextView) rootView.findViewById(R.id.pdt_expc_txt_qtt_init_value) ;
		 RunPdtExpect.txtPriceInitSale	= (TextView) rootView.findViewById(R.id.pdt_expc_txt_price_init_sale) ;
		 RunPdtExpect.txtPriceInitMeasure = (TextView) rootView.findViewById(R.id.pdt_expc_txt_price_init_measure) ;
		 
		 RunPdtExpect.txtQttCarried		= (TextView) rootView.findViewById(R.id.pdt_expc_txt_qtt_carried) ;
		 RunPdtExpect.txtHTVA 	= (TextView) rootView.findViewById(R.id.pdt_expc_txt_htva) ;
		 RunPdtExpect.txtTvaTx	= (TextView) rootView.findViewById(R.id.pdt_expc_txt_tva_tx) ;
		 RunPdtExpect.txtTvaValue 	= (TextView) rootView.findViewById(R.id.pdt_expc_txt_tva_value) ;
		 RunPdtExpect.txtPriceTTC	= (TextView) rootView.findViewById(R.id.pdt_expc_txt_ttc) ;
		 
		 
		
		/**
		 * 	BUTTONS Listeners
		 */
			
			lstn = new RunPdtExpectListener(getActivity());
			
			Button 
//			btn= (Button) rootView.findViewById(R.id.btn_pdt_expc_backward);
//			
//			btn.setOnClickListener(lstn);
			
//			btn = (Button) rootView.findViewById(R.id.btn_pdt_expc_forward);
//			
//			btn.setOnClickListener(lstn);
			
			btn = (Button) rootView.findViewById(R.id.btn_pdt_expc_lock);
			
			btn.setOnClickListener(lstn);		


			
			RunPdtExpect.txtQttCarried.addTextChangedListener((TextWatcher) lstn);
			
		
		return rootView;
		
	}
	
	
	
	public static void fillInitViewsWithValues (
		String qttInitUnit, String qttInitValue, String priceInitSale, String priceInitMeasure, String tvaTx
	) {
		
		txtQttInitUnit.setText(qttInitUnit);
		txtQttInitValue.setText(qttInitValue);
		txtPriceInitSale.setText(priceInitSale);
		txtPriceInitMeasure.setText(priceInitMeasure);		

		txtTvaTx.setText(tvaTx);		
		
	}
	
	public static void fillExpectedViewsWithValues (
		String qttCarried, String priceHTVA, String TvaValue, String priceTTC 
	) {
					
		txtQttCarried.removeTextChangedListener((TextWatcher) lstn);
		txtQttCarried.setText(qttCarried);
		txtQttCarried.addTextChangedListener((TextWatcher) lstn);
		
		txtHTVA.setText(priceHTVA);
		txtTvaValue.setText(TvaValue);
		txtPriceTTC.setText(priceTTC);
		
	}
	
	public static void updateView (double newQttCarried, double newPriceHTVA, double newTvaValue, double newTTC) {	
		
		// first update UI
		txtQttCarried.removeTextChangedListener((TextWatcher) lstn);
		RunPdtExpect.txtQttCarried	.setText(Double.toString(newQttCarried));
		txtQttCarried.addTextChangedListener((TextWatcher) lstn);
		
		RunPdtExpect.txtHTVA		.setText(Double.toString(newPriceHTVA));
		RunPdtExpect.txtTvaValue	.setText(Double.toString(newTvaValue));
		RunPdtExpect.txtPriceTTC	.setText(Double.toString(newTTC));
		
		
		// Then record result
		
		RunProduit.recordExpectedResult(newQttCarried, newPriceHTVA, newTvaValue, newTTC);
		
	}
	

	public static void clearViews() {
		
		txtQttInitUnit.setText("");
		txtQttInitValue.setText("");
		txtPriceInitSale.setText("");
		txtPriceInitMeasure.setText("");
		
		txtQttCarried.removeTextChangedListener((TextWatcher) lstn);
		RunPdtExpect.txtQttCarried	.setText("");
		txtQttCarried.addTextChangedListener((TextWatcher) lstn);
		
		txtHTVA.setText("");
		txtTvaTx.setText("");
		txtTvaValue.setText("");
		txtPriceTTC.setText("");

	}
	
	
	
}
