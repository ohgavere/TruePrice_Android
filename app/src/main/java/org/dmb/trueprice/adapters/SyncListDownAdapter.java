package org.dmb.trueprice.adapters;

import java.util.ArrayList;
import java.util.HashSet;

import org.dmb.trueprice.R;
import org.dmb.trueprice.Sync;
import org.dmb.trueprice.objects.AvailableList;
import org.dmb.trueprice.objects.SyncHistory;
import org.dmb.trueprice.objects.android.AvailableListFrontend;
import org.dmb.trueprice.objects.android.AvailableListFrontendHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SyncListDownAdapter extends BaseAdapter /*implements OnItemClickListener OnClickListener */{	

//	private HashSet<Long> selectedIds = new HashSet<Long>();
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	private Context ctx ;
	private LayoutInflater inflater ;
	ArrayList<AvailableListFrontend> listes ;
	
	SyncHistory history = null;
	
	public void updateSynchronizedList(SyncHistory history) {
		this.history = history;
	}
	
	public SyncListDownAdapter(Context initiator) {
		super();
		try {
			this.ctx = initiator ;
			this.inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
			//Log.i(this.getClass().getSimpleName(), "Adapter constructed.");			
		} catch (Exception e) {
			//Log.e(this.getClass().getSimpleName(), "Error construction Adapter : " + e.getMessage());
			e.printStackTrace();
		}			
	}
	
	public SyncListDownAdapter(Context initiator, ArrayList<AvailableListFrontend> listes) {		
		super();
		try {			
			this.ctx = initiator ;
			this.inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);		
			
			// Not good !
			// Gives NullPointer on getCount() from setAdapter() on list without passing data by setData() first
//			this.listes = listes ;
			setData(listes);
			
			//Log.i(this.getClass().getSimpleName(), "Adapter constructed with data.");
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Error construction Adapter : " + e.getMessage());
//			e.printStackTrace();
		}				
	}	
	
	public void setData( ArrayList<AvailableListFrontend> availableLists) {
		this.listes = availableLists;
	}
	
	public  ArrayList<AvailableListFrontend> getAvailableLists() {
		return listes;
	}

	@Override
	public int getCount() {
		return getAvailableLists() == null ? 0 : getAvailableLists().size() ;
	}

	@Override
	public AvailableListFrontend getItem(int position) {
		return getAvailableLists().get(position) ;
	}

	@Override
	public long getItemId(int position) {
		return getAvailableLists().get(position).getListeId() ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		try {
			
//			Log.i(this.getClass().getSimpleName(), "ask getView() : pos[" + position + "] \t Item ID at pos[" + getItemId(position) +"]");
			
			AvailableListFrontendHolder holder = null ;
			
			if (convertView == null) {
//				Log.d(this.getClass().getSimpleName(), "\t convertView == null -> need construireVue("+ position +")--> ...");
				convertView = construireVue(position);
			}
			
			
			// Seems like need to do it each time			
			holder = (AvailableListFrontendHolder) convertView.getTag();			

			// Always do that	
			AvailableListFrontend liste 
//				= listes.get(position);
				=  (AvailableListFrontend) getItem(position);

//			String strlog = "";
//			
//			strlog += "\n Holder from getTag() values :\t" 
//					+ "\t LABEL[" + holder.getLabelValue() 
//					+ "]\t Checked[" + holder.isChecked()
//					+ "]\t PCount[" + holder.getNbProduitValue()
//				+"]" ;
//			
//			strlog += "\n Item at pos (liste) values :\t ID[" + liste.getListeId() 
//					+ "]\t LABEL[" + liste.getListeLabel() 
//					+ "]\t Checked[" + liste.isChecked() 
//					+ "]\t PCount[" + liste.getPdtCount() 
//				+"]" ;
//			
//			Log.d(logHead, strlog);			
			
			
			
//			holder.setChecked(liste.isChecked());
//			liste.setChecked(holder.isChecked());
//			liste.setListeId(holder.getListeId());
			
			// On prepare un label avec l'id 
//			String fullLabel = "  [" + liste.getListeId() + "]  " + liste.getListeLabel()  ;
			
			String dtSynchronized = "" ;
			if (history == null ) {
				dtSynchronized =  "not yet synchronized" ;
			} else {
				dtSynchronized = history.getDateByListId(liste.getListeId()) ;
			}
			
			// On envoye les valeurs par défaut
			holder.fillViewsWithValues(
					// MAUVAIS ID !!
//					liste.getListeId(),
//					holder.getListeId(),
//					getItemId(position),
					
//					liste.getListeLabel(), 
//					"[" + liste.getListeId() + "] " + liste.getListeLabel() ,
					"[" + liste.getListeId() + "] " + liste.getListeLabel() ,
					
				(liste.getDate() == null ? "not yet synchronised" : liste.getDate().toString()),
				liste.getPdtCount(),
				dtSynchronized,
				liste.isChecked()
			);
			
			// On ovverride le label avec un label custom
//			holder.setLabelValue( " [" + liste.getListeId() + "] " + liste.getListeLabel() );
			
			//Log.i(this.getClass().getSimpleName(), "Add item views :[" + holder.getLabelView() +"]<=>[" + holder.getCheckBox() +"]");
			
			convertView.setTag(holder);
			
//			convertView.setOnClickListener((OnClickListener)this);
		
		} catch (Exception e) {
//			Log.e(this.getClass().getSimpleName(), "Error during getView() : " + e.getMessage());
			e.printStackTrace();
		}			
		
		return convertView;
		
	}
	
//	private void getItemClickEvent //(AvailableListFrontend holder){
//									(Boolean selected, Long listeId, String listeLabel) {
//		
////		AvailableListFrontend liste = (AvailableListFrontend) holder ;
//		
////		  liste.toggle();
//		
//		if (selected == true) {
//			Sync.addSelectedId(listeId);
//			Log.i(logHead, "Add selected_id [" + listeId +"] for liste label:[" + listeLabel +"]");
//		} else {
//			Sync.removeSelectedId(listeId);
//			Log.i(logHead, "Del selected_id [" + listeId +"] for liste label:[" + listeLabel +"]");
//		}
//		
//	}

	private View construireVue(int position) {
		
		
		
		// Save resources doing this only once !
		if (inflater == null) {
			inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
		}
		
		// inflate the item layout
		View convertView = inflater.inflate(R.layout.listitem_sync_checkable, null); 					

////		 Retrieve the current liste
//		final 
//		AvailableListFrontend liste 
////			= listes.get(position);
//			=  (AvailableListFrontend) getItem(position);
		
		
		final AvailableListFrontendHolder holder = new AvailableListFrontendHolder();
		
//		Log.i(this.getClass().getSimpleName(), "Asked to build View for liste at pos[" + position +"] with id[" + liste.getListeId()  +"] & Label [" + liste.getListeLabel() +"]");
		
		// inflate widgets from layout to build holder
		holder.setLabelView((TextView) convertView.findViewById(R.id.sync_item_label));
		holder.setCheckBox((CheckBox) convertView.findViewById(R.id.sync_item_check));
		holder.setNbProduitView((TextView) convertView.findViewById(R.id.sync_item_nbprod));
		holder.setDateModifView((TextView) convertView.findViewById(R.id.sync_item_date_modif));
		holder.setDateSyncView((TextView) convertView.findViewById(R.id.sync_item_date_sync));
		
		
		// default is unchecked
		holder.setChecked(false);
		
		
		holder.getCheckBoxView().setClickable(false);
		holder.getCheckBoxView().setFocusable(false);
		
		
		
		// set tag to keep track
		convertView.setTag(holder);			

		
		
		
		
//		// Need to be done after set tag ?
//		holder.setListenerToViews();		
		
		// Set the same event onClick for each widget to get 
		// complete clickable/selectable row.		

//		holder.getCheckBoxView().setOnClickListener( 
//				this);
////			new OnClickListener() { 
////			@Override public void onClick(View v) { 	
////				liste.toggle(); 
////				holder.toggle();
////				holder.getItemClickEvent(holder.isChecked(), holder.getListeId(), holder.getListeLabel()); 
////			}
////		}
////		);		
//
//		holder.getLabelView().setOnClickListener( 
//				this);
////				new OnClickListener() { 
////			@Override public void onClick(View v) { 	holder.toggle();
////				getItemClickEvent(holder.isChecked(), holder.getListeId(), holder.getListeLabel());
////			}
////		});		
//		
//		holder.getTxtVw_nbProduit().setOnClickListener( 
//				this);
////				new OnClickListener() { 
////			@Override public void onClick(View v) { 	holder.toggle();
////				getItemClickEvent(holder.isChecked(), holder.getListeId(), holder.getListeLabel());
////			}
////		});		
//	
//		holder.getTxtVw_dtModif().setOnClickListener( 
//				this);
////				new OnClickListener() { 
////			@Override public void onClick(View v) { 	holder.toggle();
////				getItemClickEvent(holder.isChecked(), holder.getListeId(), holder.getListeLabel()); 
////			}
////		});	
//			
//		holder.getTxtVw_dtSync().setOnClickListener( 
//				(OnClickListener) this);
////				new OnClickListener() { 
////			@Override public void onClick(View v) { 	holder.toggle();
////				getItemClickEvent(holder.isChecked(), holder.getListeId(), holder.getListeLabel()); 
////			}
////		});			
////		
		return convertView ;
		
	}

//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		
//		AvailableListFrontendHolder holder = (AvailableListFrontendHolder) view.getTag();
//		
//		holder.toggle();
//			
//		AvailableListFrontend itemByPos = getItem(position);
//		AvailableListFrontend itemById  = getItem(position);
//		
//		itemByPos.toggle();		
//		itemById.toggle();
//		
//		Log.d(logHead, "Done itemClick() for \t pos[" + position +"] <=> id[" + id +"] <=> holder.checked?[" + holder.isChecked() + "]"
//			+ "\n Item by POS[" + position + "]\t itemID[" + itemByPos.getListeId() +"]\t itemLabel[" + itemByPos.getListeLabel()  +"]"
//			 	+ "\t itemChked[" + itemByPos.isChecked() +"]"
//			+ "\n Item by ID[" + id + "]\t itemID[" + itemById.getListeId() +"]\t itemLabel [" + itemById.getListeLabel()  +"]"
//				+ "\t itemChked[" + itemByPos.isChecked() +"]"
//		);
//		
//	}

//	@Override
//	public void onClick(View v) {
//		
//		int position = this.
//		
//		AvailableListFrontendHolder holder = (AvailableListFrontendHolder) v.getTag();
//		
//		if (holder.isChecked()) {
//			holder.toggle();
//			
//		}
//	}
	
	


}
