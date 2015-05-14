package org.dmb.trueprice.fragments;

import org.dmb.trueprice.R;
import org.dmb.trueprice.listeners.RunPdtFinalListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RunPdtFinal extends Fragment {
	
	RunPdtFinalListener lstn ;
	
//	public RunPdtInit(RunPdtInitListener lstn ) {
//		this.lstn = lstn ;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_run_pdt_a_init);
		
		
		 View rootView = inflater.inflate(R.layout.fragment_run_pdt_c_final, container, false);
//         Bundle args = getArguments();
//         ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                 Integer.toString(args.getInt(ARG_OBJECT)));
//         return rootView;		
		
		
		/**
		 * 	BUTTONS Listeners
		 */
		 
			
			lstn = new RunPdtFinalListener(getActivity());
			
			Button 
//			btn= (Button) rootView.findViewById(R.id.btn_pdt_final_backward);
//			
//			btn.setOnClickListener(lstn);
			
			btn = (Button) rootView.findViewById(R.id.btn_pdt_final_lock);
			
			btn.setOnClickListener(lstn);	


		
		return rootView;
		
	}
	

	
	
	
}
