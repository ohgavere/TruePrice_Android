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
public class AvailableList extends ListHeader {

    public AvailableList(Long listeId, String listeLabel, String date, int pdtCount) {
        super(listeId, listeLabel, date, pdtCount);
    }

    public AvailableList(Long listeId, String listeLabel, String date) {
        super(listeId, listeLabel, date);
    }    
    
    public AvailableList(Long listeId, String listeLabel, int pdtCount) {
        super(listeId, listeLabel, pdtCount);
    }    
    
    
}
