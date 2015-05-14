/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import java.util.ArrayList;

/**
 *
 * @author Guitch
 */
public class SyncInitRequest {
    
    // Les resultats disponibles sur le device [liste_id, result_releaseDate]
    private ArrayList<AvailableResult> availableResults = new ArrayList<AvailableResult>();
    // Les listes téléchargées sur le device [liste_id, date_downloaded]
    private ArrayList<OwnedList> ownedLists = new ArrayList<OwnedList>();
    
    public SyncInitRequest (ArrayList<AvailableResult> availableListesResults, ArrayList<OwnedList> alreadyHaveLists) {
        this.availableResults = availableListesResults ;
        this.ownedLists = alreadyHaveLists ;
    }
    
    public ArrayList<AvailableResult> getAvailableResults() {
        return availableResults;
    }
    public ArrayList<OwnedList> getOwnedLists() {
        return ownedLists;
    }    
    public String getResultReleaseDate(int position){
        String returned = availableResults.get(position).getDate();
        return (returned == null ? "" : returned);
    }        
    
//
//    @Override
//    public String toString() {
//        String str = "\n" ;
//        
//        str += "Got [" + availableResults.size() + "] results to update =>" ;
//        
//        for (Long id : getListesResultsIds()) {
//            str += "\n Result for Liste \t ID:[" + id + "]" ;
//            str += "\t ReleaseDate:[" + getResultReleaseDate(id) + "]" ;
//        }
//        
//        return str += "\n" ;
//    }
//    
    
    
    
}
