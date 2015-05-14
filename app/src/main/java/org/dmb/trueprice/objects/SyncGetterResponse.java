/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.dmb.trueprice.objects.android.SynchronizedList;

import android.util.Log;

/**
 *
 * @author Guitch
 */
public class SyncGetterResponse  {
    
    // Les resultats disponibles sur le device [liste_id, result_releaseDate]
    private ArrayList<ListeDetailFrontend> providedLists = new ArrayList<ListeDetailFrontend>();
    private HashMap<Long, String>   mapProduitIcon  ;
    
    public SyncGetterResponse (ArrayList<ListeDetailFrontend> askedLists, HashMap<Long, String>   mapProduitIcon) {
        this.providedLists = askedLists ;
        this.mapProduitIcon = mapProduitIcon ;
    }

    public ArrayList<ListeDetailFrontend> getProvidedLists() {
        return providedLists;
    }

    public HashMap<Long, String> getMapProduitIcon() {
        return mapProduitIcon;
    }
    
    
    
    
    @Override
    public String toString() {
        String str = "\n" ;
        
        str += "Got [" + providedLists.size() + "] lists to send =>" ;
        
        for (ListeDetailFrontend liste : providedLists) {
            str += "\n Member ask for download Liste \t ID:[" + liste.getLstId() + "]" ;
        }
        str += "\n Got map Produit ID <-> Icon ==> " ;
        
        for (long id : mapProduitIcon.keySet()) {
            str += "\n ID:[" + id + "] ICON[" + mapProduitIcon.get(id) +"]";
        }
        
        return str += "\n" ;
    }
    
    
    
	public void AddOrUpdateListEntry(ListeDetailFrontend  liste) {
		int i = 0 ;
		for (ListeDetailFrontend l : getProvidedLists()) {
			i++;
			if (l.getLstId() == liste.getLstId()) {
				getProvidedLists().remove(l);
				break;
			} else {
				Log.d("SyncGetterResponse", "SyncGetterResponse for break try: nb[" + i +"]");
			}
		}
		getProvidedLists().add(liste);
	}
    
	public void AddOrUpdateIconEntry(Long pdtId, String iconUrl) {
		int i = 0 ;
		for (Long key : getMapProduitIcon().keySet()) {
			i++;
			if (key == pdtId && getMapProduitIcon().get(key).equals(iconUrl)) {
				getMapProduitIcon().remove(key);
				break;
			} else {
				Log.d("SyncGetterResponse", "SyncGetterResponse for break try: nb[" + i +"]");
			}
		}
		getMapProduitIcon().put(pdtId, iconUrl);
	}
        
}
