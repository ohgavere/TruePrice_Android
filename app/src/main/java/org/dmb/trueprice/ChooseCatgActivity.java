package org.dmb.trueprice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public 	
class ChooseCatgActivity extends ListActivity implements OnItemClickListener {
	
	private final String logHead = this.getClass().getSimpleName();
	
	ListView lv = null ;
		 
	ArrayAdapter<String> adapter = null ;
	
	public ChooseCatgActivity() {super();}
	
	public ChooseCatgActivity(ArrayAdapter<String> adapter ) {
		this.adapter = adapter ;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity_choose_catg);
		
//		ArrayAdapter<String> adpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		ArrayAdapter<String> adpt = new ArrayAdapter<String>(this, R.layout.listitem_simple_item_white);
		
		lv = (ListView) findViewById(android.R.id.list);
		
		lv.setOnItemClickListener(this);
		
		
		Intent i = getIntent();
		
		HashMap<Long, String> tempMap = (HashMap<Long, String>) i.getExtras().get(RunListeStart.ATT_ID_CATG_LABELS);
		
		if ( tempMap != null) {
			Log.d(logHead, "Got incomong categories [ x" 
				+ tempMap.size() + " ]"
				+ Arrays.toString(tempMap.values().toArray())
			);
			
			for (String label : tempMap.values()) {
				adpt.add(label);
			}

			setListAdapter(adpt);	
			
		} else {
			Log.d(logHead, "Got incomong categories NULL !! ");
			
			finish();
		}
		
		
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
		TextView tv = (TextView) view ;
		
		String chosenLabel = null ;
		
		chosenLabel = getListAdapter().getItem(position).toString();
		
		if (chosenLabel != null) {
//			Toast.makeText(this, "Vous avez choisi la categorie [ " + chosenLabel + " ]", Toast.LENGTH_LONG).show();
			
	        Intent result = new Intent();
	        result.putExtra(RunListeStart.FLAG_CHOSEN_CATG_LABEL, chosenLabel);
	        setResult(RESULT_OK, result);
	        finish();			
			
		} else {
			Log.e(logHead, "La categorie choise n'a pas été trouvée ! " 
				+ "label[" + chosenLabel + "] "
				+ "position[ " + position + " ] " 
				+ "id[ " + id + " ]"
			);
			Toast.makeText(this, "Une erreur est survenue", Toast.LENGTH_LONG).show();
		}
	}
	
}