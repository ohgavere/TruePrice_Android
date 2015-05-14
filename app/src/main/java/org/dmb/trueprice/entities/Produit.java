/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.entities;


import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Guiitch
 */

public class Produit implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long pdtId;

    private String pdtNom;

    private String pdtDescription;

    private String pdtTvaTaux;

    private String pdtLink;

    private Date pdtDtcreation;

    private Date pdtPeriodPrefStart;

    private Date pdtPeriodPrefStop;

    private String pdtProperty;

    private String pdtSubproperty;

    private int pdtCategory;

    private int pdtSubcategory;

    private long pdtEnsigne;

    private long pdtMarque;

    private long pdtQtt;

    public Produit() {
    }

    public Produit(Long pdtId) {
        this.pdtId = pdtId;
    }

    public Produit(Long pdtId, int pdtCategory, int pdtSubcategory) {
        this.pdtId = pdtId;
        this.pdtCategory = pdtCategory;
        this.pdtSubcategory = pdtSubcategory;
    }

    public Long getPdtId() {
        return pdtId;
    }

    public void setPdtId(Long pdtId) {
        this.pdtId = pdtId;
    }

    public String getPdtNom() {
        return pdtNom;
    }

    public void setPdtNom(String pdtNom) {
        this.pdtNom = pdtNom;
    }

    public String getPdtDescription() {
        return pdtDescription;
    }

    public void setPdtDescription(String pdtDescription) {
        this.pdtDescription = pdtDescription;
    }

    public String getPdtTvaTaux() {
        return pdtTvaTaux;
    }

    public void setPdtTvaTaux(String pdtTvaTaux) {
        this.pdtTvaTaux = pdtTvaTaux;
    }

    public String getPdtLink() {
        return pdtLink;
    }

    public void setPdtLink(String pdtLink) {
        this.pdtLink = pdtLink;
    }

    public Date getPdtDtcreation() {
        return pdtDtcreation;
    }

    public void setPdtDtcreation(Date pdtDtcreation) {
        this.pdtDtcreation = pdtDtcreation;
    }

    public Date getPdtPeriodPrefStart() {
        return pdtPeriodPrefStart;
    }

    public void setPdtPeriodPrefStart(Date pdtPeriodPrefStart) {
        this.pdtPeriodPrefStart = pdtPeriodPrefStart;
    }

    public Date getPdtPeriodPrefStop() {
        return pdtPeriodPrefStop;
    }

    public void setPdtPeriodPrefStop(Date pdtPeriodPrefStop) {
        this.pdtPeriodPrefStop = pdtPeriodPrefStop;
    }

    public String getPdtProperty() {
        return pdtProperty;
    }

    public void setPdtProperty(String pdtProperty) {
        this.pdtProperty = pdtProperty;
    }

    public String getPdtSubproperty() {
        return pdtSubproperty;
    }

    public void setPdtSubproperty(String pdtSubproperty) {
        this.pdtSubproperty = pdtSubproperty;
    }

    public int getPdtCategory() {
        return pdtCategory;
    }

    public void setPdtCategory(int pdtCategory) {
        this.pdtCategory = pdtCategory;
    }

    public int getPdtSubcategory() {
        return pdtSubcategory;
    }

    public void setPdtSubcategory(int pdtSubcategory) {
        this.pdtSubcategory = pdtSubcategory;
    }

    public long getPdtEnsigne() {
        return pdtEnsigne;
    }

    public void setPdtEnsigne(long pdtEnsigne) {
        this.pdtEnsigne = pdtEnsigne;
    }

    public long getPdtMarque() {
        return pdtMarque;
    }

    public void setPdtMarque(long pdtMarque) {
        this.pdtMarque = pdtMarque;
    }

    public long getPdtQtt() {
        return pdtQtt;
    }

    public void setPdtQtt(long pdtQtt) {
        this.pdtQtt = pdtQtt;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pdtId != null ? pdtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produit)) {
            return false;
        }
        Produit other = (Produit) object;
        if ((this.pdtId == null && other.pdtId != null) || (this.pdtId != null && !this.pdtId.equals(other.pdtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.ProduitListe[ pdtId=" + pdtId + " ]";
    }
    
}
