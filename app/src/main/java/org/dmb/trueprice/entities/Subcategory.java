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

public class Subcategory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer sctgId;
    private String sctgLabel;
    private String sctgDescription;

    private long sctgParent;
    private long sctgSubparent;

    public Subcategory() {
    }

    public Subcategory(Integer sctgId) {
        this.sctgId = sctgId;
    }

    public Subcategory(Integer sctgId, long sctgParent) {
        this.sctgId = sctgId;
        this.sctgParent = sctgParent;
    }

    public Integer getSctgId() {
        return sctgId;
    }

    public void setSctgId(Integer sctgId) {
        this.sctgId = sctgId;
    }

    public String getSctgLabel() {
        return sctgLabel;
    }

    public void setSctgLabel(String sctgLabel) {
        this.sctgLabel = sctgLabel;
    }

    public String getSctgDescription() {
        return sctgDescription;
    }

    public void setSctgDescription(String sctgDescription) {
        this.sctgDescription = sctgDescription;
    }

    public long getSctgParent() {
        return sctgParent;
    }

    public void setSctgParent(long sctgParent) {
        this.sctgParent = sctgParent;
    }

    public long getSctgSubparent() {
        return sctgSubparent;
    }

    public void setSctgSubparent(long sctgSubparent) {
        this.sctgSubparent = sctgSubparent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sctgId != null ? sctgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subcategory)) {
            return false;
        }
        Subcategory other = (Subcategory) object;
        if ((this.sctgId == null && other.sctgId != null) || (this.sctgId != null && !this.sctgId.equals(other.sctgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Subcategory[ sctgId=" + sctgId + " ]";
    }
    
}
