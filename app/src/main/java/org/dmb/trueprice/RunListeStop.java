package org.dmb.trueprice;

import org.dmb.trueprice.listeners.RunStartListener;

import android.os.Bundle;
import android.widget.Button;

public class RunListeStop extends MenuHandlerActionBarCompatAdapter {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_liste_start);
		
		RunStartListener lstn = new RunStartListener(this);
		
		Button btn= (Button) findViewById(R.id.btn_pdt_start_catg);
		
		btn.setOnClickListener(lstn);
		
		btn = (Button) findViewById(R.id.btn_pdt_start_state);
		
		btn.setOnClickListener(lstn);
		
		btn = (Button) findViewById(R.id.btn_pdt_start_alpha);
		
		btn.setOnClickListener(lstn);		
		
		
	}
	
	
}
