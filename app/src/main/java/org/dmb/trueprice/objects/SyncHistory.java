package org.dmb.trueprice.objects;

import java.util.ArrayList;

import org.dmb.trueprice.objects.android.SynchronizedList;

public class SyncHistory {
	
	private ArrayList<SynchronizedList> syncdLists ;
	
	public SyncHistory( ArrayList<SynchronizedList> syncdLists) {
		setSyncdLists(syncdLists);
	}

	public void setSyncdLists(ArrayList<SynchronizedList> syncdLists) {
		this.syncdLists = syncdLists;
	}
	
	public ArrayList<SynchronizedList> getSyncdLists() {
		return syncdLists;
	}
	
	public void AddOrUpdateEntry(SynchronizedList newListe) {
				
		for (SynchronizedList l : getSyncdLists()) {
			
			if (l.getListeId() == newListe.getListeId()) {
				getSyncdLists().remove(l);
				break;
			}
			
		}
		
		getSyncdLists().add(newListe);
		
	}
	
	public String getDateByListId(Long listeId) {
		
		for (SynchronizedList l : getSyncdLists()) {
			
			if (l.getListeId() == listeId) {
				return l.getDateSynchronized();
			}
			
		}		
		return null ;
		
	}
	

}
