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
public class SyncInitResponse {
    
//    protected HashMap<Long , String> availableLists =  new HashMap<Long, String>();
    protected ArrayList<AvailableList> availableLists =  new ArrayList<AvailableList>();
    
//    public SyncInitResponse (HashMap<Long, String> availableListes) {
    public SyncInitResponse (ArrayList<AvailableList> availableListes) {
        this.availableLists = availableListes ;
    }
//    
//    public String getDateModified(Long listeId) { 
//        return availableLists.get(listeId);
//    }   
//    public Set<Long> getListesIds() {
//        return availableLists.keySet();
//    }
//    public Collection<String> getTimeElapsedAll() {
//        return availableLists.values();
//    }

//    @Override
//    public String toString() {
//        String str = "\n" ;
//        
//        str += "Got [" + availableLists.size() + "] lists ready to download =>" ;
//        
//        for (Long id : getListesIds()) {
//            str += "\n Liste \t ID:[" + id + "]" ;
//            str += "\t Date Modified:[" + getDateModified(id) + "]" ;
//        }
//        
//        return str += "\n" ;
//    }

    public ArrayList<AvailableList> getAvailableLists() {
        return availableLists;
    }
    
    
    public AvailableList getListByID(long listeId) {
    	for (AvailableList l : getAvailableLists()) {
    		if (l.getListeId() == listeId) return l ;
    	}
    	return null;
    }
    
    
    
}
