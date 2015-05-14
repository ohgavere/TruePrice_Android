/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import android.util.Log;

/**
 *
 * @author Guitch
 */
public abstract class ListHeader {
    
    private Long listeId ;
    private String listeLabel ;
    private String date ;
    private int pdtCount ;

    public ListHeader(Long listeId, String listeLabel, String date, int pdtCount) {
    	Log.d(this.getClass().getSimpleName(), "listeHeader()-> ID[" + listeId + "] \tLabel:[" + listeLabel + "]");
        this.listeId = listeId;
        this.listeLabel = listeLabel;
        this.date = date;
        this.pdtCount = pdtCount;
    }

    public ListHeader(Long listeId, String listeLabel, String date) {
        this.listeId = listeId;
        this.listeLabel = listeLabel;
        this.date = date;        
    }
    
    public ListHeader(Long listeId, String listeLabel, int pdtCount) {
        this.listeId = listeId;
        this.listeLabel = listeLabel;
        this.pdtCount = pdtCount;      
    }        

    public String getDate() {
        return date;
    }

    public Long getListeId() {
        return listeId;
    }

    public String getListeLabel() {
        return listeLabel;
    }

    public int getPdtCount() {
        return pdtCount;
    }
    
    public void setDate(String date) {
        this.date = date;
    }

    public void setListeId(Long listeId) {
        this.listeId = listeId;
    }

    public void setListeLabel(String listeLabel) {
        this.listeLabel = listeLabel;
    }

    public void setPdtCount(int pdtCount) {
        this.pdtCount = pdtCount;
    }
    
    
	@Override
	public String toString() {
		return this.getClass().getSimpleName() 
			+ " \t ID:[" + getListeId() +"]"
			+ " \t LABEL:[" + getListeLabel() +"]"
			+ " \n PDT_COUNT:[" + getPdtCount() +"]"
			+ " \t DATE_?_:[" + getDate() +"]";
	}
    
    
}
