package org.dmb.trueprice.adapters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.dmb.trueprice.R;
import org.dmb.trueprice.Sync;
import org.dmb.trueprice.objects.AvailableList;
import org.dmb.trueprice.objects.AvailableResult;
import org.dmb.trueprice.objects.ListeDetailFrontend;
import org.dmb.trueprice.objects.android.AvailableListFrontend;
import org.dmb.trueprice.objects.android.AvailableResultFrontend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SyncListUpAdapter extends BaseAdapter {	
	
//	private HashSet<Long> selectedIds = new HashSet<Long>();
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	private Context ctx ;
	private LayoutInflater inflater ;
	ArrayList<AvailableResultFrontend> results ;
	
	public SyncListUpAdapter(Context initiator) {
		super();
		this.ctx = initiator ;
		this.inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
	}
	
	public SyncListUpAdapter(Context initiator, ArrayList<AvailableResultFrontend> data) {		
		super();
		try {			
			this.ctx = initiator ;
			this.inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);		
			this.results = data ;
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Error construction Adapter : " + e.getMessage());
			e.printStackTrace();
		}				
	}	
	
	
	public void setResults(ArrayList<AvailableResultFrontend> data) {
		this.results = data;
	}
	
	public ArrayList<AvailableResultFrontend> getData() {
		return results;
	}

	@Override
	public int getCount() {
		return results == null ? 0 : results.size() ;
	}

	@Override
	public Object getItem(int position) {
		return results == null ? null : results.get(position) ;
	}

	@Override
	public long getItemId(int position) {
		return results == null ? Long.parseLong("-1") : results.get(position).getListeId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		try {
			
			//Log.i(this.getClass().getSimpleName(), "ask getView() : pos[" + position + "]");
			
			AvailableResultFrontend holder = null ;
			
			if (convertView == null) {
				convertView = construireVue(position);
			}
			
			// Seems like need to do it each time
	//		else {
				holder = (AvailableResultFrontend) convertView.getTag();
	//		}
			
			// Always do that	
			AvailableResultFrontend liste = results.get(position);	
			holder.setLabelValue( " [" + liste.getListeId() + "] " + liste.getListeLabel()
				+ " Produits [" + liste.getPdtCount() + "]. Modifiée le " + liste.getDate()
			);
			
			holder.setChecked(false);
			
			//Log.i(this.getClass().getSimpleName(), "Add item views :[" + holder.getLabelView() +"]<=>[" + holder.getCheckBox() +"]");
		
		} catch (Exception e) {
			//Log.e(this.getClass().getSimpleName(), "Error during getView() : " + e.getMessage());
			e.printStackTrace();
		}			
		
		return convertView;
		
	}

	private void getItemClickEvent(AvailableResultFrontend holder){		
		
		AvailableResultFrontend liste = (AvailableResultFrontend) holder ;
		
		 
//		 Toast.makeText(ctx,
		//Log.i(LogHead,
//		   "Clicked on tag : " + v.getTag() + " with data ==>" +
//		   "\t id: " + liste.getListeId() + "\t label: " + liste.getListeLabel() +
//		   "\t pdt_count: " + liste.getPdtCount() + "\t date_modified: " + liste.getDate()
//		   + "\t checked : [" + liste.isChecked() + "]"
//		);
//		   , Toast.LENGTH_LONG).show();					 

		  liste.toggle();
		  
//		Log.i(logHead, "State updated ? isCkecked() -->[" + liste.isChecked() 
//				+ "] view --> [" + liste.getCheckBoxView().isSelected() +"]");
		 
		
		if (liste.isChecked()) {
			Sync.addSelectedId(liste.getListeId());
			Log.i(logHead, "Add selected_id [" + liste.getListeId() +"]");
		} else {
			Sync.removeSelectedId(liste.getListeId());
			Log.i(logHead, "Del selected_id [" + liste.getListeId() +"]");
		}
		

//		 Log.i(getClass().getSimpleName(), "OnClick - END - for View v:   tag:[" + v.getTag() +"]"
//				 + "\t id?:[" + v.getId() +"]" + "\t class?:[" + v.getClass() +"]"
//				 
//				 );		
		
	}
	
	private View construireVue(int position) {
		
		//Log.i(this.getClass().getSimpleName(), "Asked to build View ");
		
		if (inflater == null) {
			inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
		}
		
		View convertView = inflater.inflate(R.layout.listitem_sync_checkable, null); 			

		final AvailableResultFrontend holder = results.get(position);
		holder.setLabelView((TextView) convertView.findViewById(R.id.sync_item_label));
		holder.setCheckBox((CheckBox) convertView.findViewById(R.id.sync_item_check));
		
		convertView.setTag(holder);
		
		holder.setChecked(false);		
		
		holder.getCheckBoxView().setOnClickListener(new OnClickListener() { 
			@Override public void onClick(View v) { getItemClickEvent(holder); }
		});
				
		holder.getLabelView().setOnClickListener(new OnClickListener() { 
			@Override public void onClick(View v) { getItemClickEvent(holder); }
		});		
		
		return convertView ;
		
	}
	
	
	
	
	
//	/**
//	 * 	SELECTED IDs
//	 */
//	
//	public HashSet<Long> getSelectedIds() {
//		return selectedIds;
//	}
//	
//	public int getSelectedIdsCount() {
//		return selectedIds.size();
//	}
//	
//	public void addSelectedId(Long id) {
//		selectedIds.add(id);
//	}
//	
//	public void removeSelectedId(Long id) {
//		selectedIds.remove(id);
//	}	

}
