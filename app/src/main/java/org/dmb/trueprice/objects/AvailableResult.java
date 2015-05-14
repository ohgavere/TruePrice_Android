/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

/**
 *
 * @author Guitch
 */
public class AvailableResult extends ListHeader {


    public AvailableResult(Long listeId, String listeLabel, String dateModified) {
        super(listeId, listeLabel, dateModified);
    }
    
    public AvailableResult(Long listeId, String listeLabel, String date, int pdtCount) {
        super(listeId, listeLabel, date, pdtCount);
    }
    
    public String getDateReleased() {
        return super.getDate(); 
    }
    public void setDateReleased(String date) {
        super.setDate(date);
    }
    
    
    
    
    
}
