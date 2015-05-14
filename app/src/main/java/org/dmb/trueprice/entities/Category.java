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

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer ctgId;

    private String ctgLabel;

    private String ctgDescription;

    public Category() {
    }

    public Category(Integer ctgId) {
        this.ctgId = ctgId;
    }

    public Integer getCtgId() {
        return ctgId;
    }

    public void setCtgId(Integer ctgId) {
        this.ctgId = ctgId;
    }

    public String getCtgLabel() {
        return ctgLabel;
    }

    public void setCtgLabel(String ctgLabel) {
        this.ctgLabel = ctgLabel;
    }

    public String getCtgDescription() {
        return ctgDescription;
    }

    public void setCtgDescription(String ctgDescription) {
        this.ctgDescription = ctgDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctgId != null ? ctgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.ctgId == null && other.ctgId != null) || (this.ctgId != null && !this.ctgId.equals(other.ctgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Category[ ctgId=" + ctgId + " ]";
    }
    
}
