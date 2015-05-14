package org.dmb.trueprice.adapters;

import java.util.ArrayList;

import org.dmb.trueprice.R;
import org.dmb.trueprice.objects.SyncHistory;
import org.dmb.trueprice.objects.android.RunnableListFrontend;
import org.dmb.trueprice.objects.android.RunnableListFrontendHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RunListStartAdapter extends BaseAdapter /*implements OnItemClickListener OnClickListener */{	

//	private HashSet<Long> selectedIds = new HashSet<Long>();
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	private Context ctx ;
	private LayoutInflater inflater ;
	ArrayList<RunnableListFrontend> listes ;
	
	SyncHistory history = null;
	
	public void updateSynchronizedList(SyncHistory history) {
		this.history = history;
	}
	
	public RunListStartAdapter(Context initiator) {
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
	
	public RunListStartAdapter(Context initiator, ArrayList<RunnableListFrontend> listes) {		
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
	
	public void setData( ArrayList<RunnableListFrontend> runnableLists) {
		this.listes = runnableLists;
	}
	
	public  ArrayList<RunnableListFrontend> getRunnableLists() {
		return listes;
	}

	@Override
	public int getCount() {
		return getRunnableLists() == null ? 0 : getRunnableLists().size() ;
	}

	@Override
	public RunnableListFrontend getItem(int position) {
		return getRunnableLists().get(position) ;
	}

	@Override
	public long getItemId(int position) {
		return getRunnableLists().get(position).getListeId() ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		try {
			
			Log.i(logHead, "ask getView() : pos[" + position + "] \t Item ID at pos[" + getItemId(position) +"]");
			
			RunnableListFrontendHolder holder = null ;
			
			if (convertView == null) {
//				Log.d(this.getClass().getSimpleName(), "\t convertView == null -> need construireVue("+ position +")--> ...");
				convertView = construireVue(position);
			}
			
			
			// Seems like need to do it each time			
			holder = (RunnableListFrontendHolder) convertView.getTag();			

			// Always do that	
			RunnableListFrontend liste 
//				= listes.get(position);
				=  (RunnableListFrontend) getItem(position);

			
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
					
				liste.getPdtCount(),
				dtSynchronized
			);
			
			// On ovverride le label avec un label custom
//			holder.setLabelValue( " [" + liste.getListeId() + "] " + liste.getListeLabel() );
			
			//Log.i(this.getClass().getSimpleName(), "Add item views :[" + holder.getLabelView() +"]<=>[" + holder.getCheckBox() +"]");
			
			convertView.setTag(holder);
			
		
		} catch (Exception e) {
//			Log.e(this.getClass().getSimpleName(), "Error during getView() : " + e.getMessage());
			e.printStackTrace();
		}			
		
		return convertView;
		
	}
	


	private View construireVue(int position) {
		
		
		
		// Save resources doing this only once !
		if (inflater == null) {
			inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
		}
		
		// inflate the item layout
		View convertView = inflater.inflate(R.layout.listitem_run_list, null); 					

////		 Retrieve the current liste
//		final 
//		RunnableListFrontend liste 
////			= listes.get(position);
//			=  (RunnableListFrontend) getItem(position);
		
		
		final RunnableListFrontendHolder holder = new RunnableListFrontendHolder();
		
//		Log.i(this.getClass().getSimpleName(), "Asked to build View for liste at pos[" + position +"] with id[" + liste.getListeId()  +"] & Label [" + liste.getListeLabel() +"]");
		
		// inflate widgets from layout to build holder
		holder.setLabelView((TextView) convertView.findViewById(R.id.run_list_item_label));
		holder.setNbProduitView((TextView) convertView.findViewById(R.id.run_list_item_nbprod));
		holder.setDateSyncView((TextView) convertView.findViewById(R.id.run_list_item_date_sync));
				
		
		
		// set tag to keep track
		convertView.setTag(holder);			

	
		return convertView ;
		
	}



}
