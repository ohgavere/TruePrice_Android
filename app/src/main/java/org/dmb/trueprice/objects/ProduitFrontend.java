/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import org.dmb.trueprice.entities.Category;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.entities.QttDetail;
import org.dmb.trueprice.entities.Subcategory;

/**
 *
 * @author Guitch
 */

@SuppressWarnings("serial")
public class ProduitFrontend extends Produit {
    
    private String qttUnite ;
    private String qttValue ;
    
    private String catgLabel ;
    private String scatgLabel ;
    
    private String mrqLabel ;
    private String dstrLabel ;

    
    
    public ProduitFrontend(String qttUnite, String qttValue) {
        this.qttUnite = qttUnite;
        this.qttValue = qttValue;
    }

    public ProduitFrontend(String qttUnite, String qttValue, String marqueLabel, String distributeurLabel) {
        this.qttUnite = qttUnite;
        this.qttValue = qttValue;
        this.mrqLabel = marqueLabel;
        this.dstrLabel = distributeurLabel;
    }
    

    public ProduitFrontend(Produit pdt, String qttUnite, String qttValue, String marqueLabel, String distributeurLabel) {
        this.qttUnite = qttUnite;
        this.qttValue = qttValue;
        this.mrqLabel = marqueLabel;
        this.dstrLabel = distributeurLabel;
        setAttrFromProduit(pdt);
    }

    public ProduitFrontend(String qttUnite, String qttValue, String catgLabel, String scatgLabel, String mrqLabel, String dstrLabel, Long pdtId, int pdtCategory, int pdtSubcategory) {
        super(pdtId, pdtCategory, pdtSubcategory);
        this.qttUnite = qttUnite;
        this.qttValue = qttValue;
        this.catgLabel = catgLabel;
        this.scatgLabel = scatgLabel;
        this.mrqLabel = mrqLabel;
        this.dstrLabel = dstrLabel;
    }
    
    
    
    public ProduitFrontend(QttDetail qtt, Produit pdt, Category catg, Subcategory scatg) {        
        this.qttUnite = qtt.getQttMesure();
        this.qttValue = qtt.getQttQuantite();
        this.catgLabel = catg.getCtgLabel();
        this.scatgLabel = scatg.getSctgLabel();
        setAttrFromProduit(pdt);
    }
    
/**
 * Most usefull constructor
 * @param qtt   @Entity
 * @param pdt   @Entity
 * @param catg  @Entity
 * @param scatg @Entity
 * @param marqueLabel   @String
 * @param distributeurLabel @String
 */    
    public ProduitFrontend(QttDetail qtt, Produit pdt, Category catg, Subcategory scatg, String marqueLabel, String distributeurLabel) {        
        this.qttUnite = qtt.getQttMesure();
        this.qttValue = qtt.getQttQuantite();
        this.catgLabel = catg.getCtgLabel();
        this.scatgLabel = scatg.getSctgLabel();
        this.dstrLabel = distributeurLabel;
        this.mrqLabel = marqueLabel;        
        setAttrFromProduit(pdt);
    }
    
    public ProduitFrontend(QttDetail qtt, Produit pdt) {        
        this.qttUnite = qtt.getQttMesure();
        this.qttValue = qtt.getQttQuantite();
        setAttrFromProduit(pdt);
    }

    public ProduitFrontend(QttDetail qtt, Produit pdt, String marqueLabel, String distributeurLabel ) {        
        this.qttUnite = qtt.getQttMesure();
        this.qttValue = qtt.getQttQuantite();
        this.dstrLabel = distributeurLabel;
        this.mrqLabel = marqueLabel;
        setAttrFromProduit(pdt);
    }

    

    public final void setAttrFromProduit(Produit pdt){
        setPdtId(pdt.getPdtId());
        setPdtCategory(pdt.getPdtCategory());
        setPdtDescription(pdt.getPdtDescription());
        setPdtLink(pdt.getPdtLink());
        setPdtNom(pdt.getPdtNom());
        setPdtProperty(pdt.getPdtProperty());
        setPdtSubcategory(pdt.getPdtSubcategory());
        setPdtSubproperty(pdt.getPdtSubproperty());
        setPdtMarque(pdt.getPdtMarque());    
    }    
    
    
    
    public String getQttUnite() {
        return qttUnite;
    }

    public String getQttValue() {
        return qttValue;
    }

    public void setQttUnite(String qttUnite) {
        this.qttUnite = qttUnite;
    }

    public void setQttValue(String qttValue) {
        this.qttValue = qttValue;
    }

    public String getMarqueLabel() {
        return mrqLabel;
    }

    public void setMarqueLabel(String mrqLabel) {
        this.mrqLabel = mrqLabel;
    }
    
    public String getDistributeurLabel() {
        return dstrLabel;
    }

    public void setDistributeurLabel(String dstrLabel) {
        this.dstrLabel = dstrLabel;
    }

    public void setCatgLabel(String catgLabel) {
        this.catgLabel = catgLabel;
    }

    public void setScatgLabel(String scatgLabel) {
        this.scatgLabel = scatgLabel;
    }

    public String getCatgLabel() {
        return catgLabel;
    }

    public String getScatgLabel() {
        return scatgLabel;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
