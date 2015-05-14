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

public class QttDetail implements Serializable {
    private static final long serialVersionUID = 1L;


    private Integer qttId;

    private String qttMesure;

    private String qttQuantite;

    public QttDetail() {
    }

    public QttDetail(Integer qttId) {
        this.qttId = qttId;
    }

    public QttDetail(Integer qttId, String qttMesure, String qttQuantite) {
        this.qttId = qttId;
        this.qttMesure = qttMesure;
        this.qttQuantite = qttQuantite;
    }

    public Integer getQttId() {
        return qttId;
    }

    public void setQttId(Integer qttId) {
        this.qttId = qttId;
    }

    public String getQttMesure() {
        return qttMesure;
    }

    public void setQttMesure(String qttMesure) {
        this.qttMesure = qttMesure;
    }

    public String getQttQuantite() {
        return qttQuantite;
    }

    public void setQttQuantite(String qttQuantite) {
        this.qttQuantite = qttQuantite;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qttId != null ? qttId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QttDetail)) {
            return false;
        }
        QttDetail other = (QttDetail) object;
        if ((this.qttId == null && other.qttId != null) || (this.qttId != null && !this.qttId.equals(other.qttId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.QttDetail[ qttId=" + qttId + " ]";
    }
    
}
