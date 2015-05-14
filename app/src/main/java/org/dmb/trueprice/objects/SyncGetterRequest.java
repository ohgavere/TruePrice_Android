/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Guitch
 */
public class SyncGetterRequest {
    
    // Les resultats disponibles sur le device [liste_id, result_releaseDate]
    private Set<Long> askedLists = new HashSet<Long>();
    
    public SyncGetterRequest (Set<Long> askedLists) {
        this.askedLists = askedLists ;
    }

    public Set<Long> getAskedLists() {
        return askedLists;
    }
    
    
    
    @Override
    public String toString() {
        String str = "\n SyncGetRequest n°[" + this.hashCode() +"]\n" ;
        
        str += "Got [" + askedLists.size() + "] lists to download =>" ;
        
        for (Long id : askedLists) {
            str += "\n Member ask for download Liste \t ID:[" + id + "]" ;
        }
        
        return str += "\n" ;
    }
    
    
    
    
}
