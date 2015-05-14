/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.entities;

import java.io.Serializable;


/**
 *
 * @author Guiitch
 */

public class Liste implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer lstId;

    private long lstUser;

    private String lstLabel;

    private String lstDescription;
    private long lstEnseigne;

    private String lstProduits;

    public Liste() {
    }

    public Liste(Integer listesId) {
        this.lstId = listesId;
    }

    public Liste(Integer listesId, long listesUser, String listesLabel) {
        this.lstId = listesId;
        this.lstUser = listesUser;
        this.lstLabel = listesLabel;
    }

    public Integer getLstId() {
        return lstId;
    }

    public void setLstId(Integer lstId) {
        this.lstId = lstId;
    }

    public long getLstUser() {
        return lstUser;
    }

    public void setLstUser(long lstUser) {
        this.lstUser = lstUser;
    }

    public String getLstLabel() {
        return lstLabel;
    }

    public void setLstLabel(String lstLabel) {
        this.lstLabel = lstLabel;
    }

    public String getLstDescription() {
        return lstDescription;
    }

    public void setLstDescription(String lstDescription) {
        this.lstDescription = lstDescription;
    }

    public long getLstEnseigne() {
        return lstEnseigne;
    }

    public void setLstEnseigne(long lstEnseigne) {
        this.lstEnseigne = lstEnseigne;
    }

    public String getLstProduits() {
        return lstProduits == null ? "" : lstProduits ;
    }

    public void setLstProduits(String lstProduits) {
        this.lstProduits = lstProduits;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lstId != null ? lstId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Liste)) {
            return false;
        }
        Liste other = (Liste) object;
        if ((this.lstId == null && other.lstId != null) || (this.lstId != null && !this.lstId.equals(other.lstId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Listes[ listesId=" + lstId + " ]"
        	+ " \t label[" + lstLabel + "]\t PdtList: [" + getLstProduits() + "]" 
        ;
    }
    
}
