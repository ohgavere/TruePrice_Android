/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import java.util.ArrayList;

import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.Produit;

/**
 *
 * @author Guitch
 */

@SuppressWarnings("serial")
public class ListeFrontend extends Liste {
    
    private String esgnLabel ;
    
    private ArrayList<Produit> pdtObjects ;
    private int pdtCount ;
    

    public ListeFrontend( Liste liste, ArrayList<Produit> productsObjects, String esgnLabel) {
        super(liste.getLstId(), liste.getLstUser(), liste.getLstLabel());
        
        setLstDescription(liste.getLstDescription());
        setLstEnseigne(liste.getLstEnseigne());
        // On les reprend quand meme au cas ou
        setLstProduits(liste.getLstProduits());
        // Les produits en Objects pour display + facile
        this.pdtObjects = productsObjects;
        this.pdtCount = productsObjects == null ? 0 : (pdtObjects.size() + 1) ;
        this.esgnLabel = esgnLabel ;
        
    }
    
    
    public ListeFrontend( Liste liste, ArrayList<Produit> productsObjects) {
        
        super(liste.getLstId(), liste.getLstUser(), liste.getLstLabel());
        
        setLstDescription(liste.getLstDescription());
        setLstEnseigne(liste.getLstEnseigne());
        // On les reprend quand meme au cas ou
        setLstProduits(liste.getLstProduits());
        // Les produits en Objects pour display + facile
        this.pdtObjects = productsObjects;
        this.pdtCount = productsObjects == null ? 0 : (pdtObjects.size() + 1) ;
    }
    
    public ListeFrontend( ArrayList<Produit> productsObjects) {
        this.pdtObjects = productsObjects;
        this.pdtCount = productsObjects == null ? 0 :(pdtObjects.size() + 1) ;
    }
    
    public ListeFrontend (Liste liste, String esgnLabel) {
        
        super(liste.getLstId(), liste.getLstUser(), liste.getLstLabel());
        this.esgnLabel = esgnLabel; 
         
        String desc = liste.getLstDescription();
        // Si la description contiens une couleur on l'enlève
        if (desc.contains("]")) {
            desc = desc.substring(desc.indexOf("]")+1);
        }
        
        setLstDescription(desc.length() > 0 ? desc : "<em>Aucune description</em>");
        
        
        setLstEnseigne(liste.getLstEnseigne());
        // On les reprend quand meme au cas ou
        
//        String listeProduitsString = liste.getLstProduits() ;
        setLstProduits(liste.getLstProduits());
        
        
/**
 * Compter le nobre de produits
 */        
        String pdtLine = getLstProduits();
        
        // Si pas de produits
        if (pdtLine == null) {
            this.pdtCount = 0 ;
        } else if (pdtLine.length() == 0 ) {
            this.pdtCount = 0 ;
        } 
        // Si   AU MOINS  1 produit
        else {
            /* Le nbre de produit = nbre de separateurs + 1 */
            // Premier separateur '_'
            int countSeparator = pdtLine.length() - pdtLine.replace("_", "").length() ;
            // Au moins 1 produit ==> +1
            int countProduit = countSeparator + 1 ;
            
            // Si pas plus de  1 ...
            if (countProduit == 1) {
                
                // ... on verifie qu'il n'y pas non plus de 2e separateur '-'
                countSeparator = pdtLine.length() - pdtLine.replace("_", "").length() ;
            
                // Si == 1 aussi on est certain d'avoir 1 seul produit
                                 // Au moins 1 produit ==> +1
//                if ( countProduit < countSeparator + 1 ) {
                
                // Si plus que 1 ==> (countSeparator'-' > 0)
                if ( countSeparator + 1  > countProduit) {
                    // final count == countSeparator + 1
                } 
                
                else {
                    // final count == 1
                    countProduit = countSeparator + 1  ;
                }
            }
            this.pdtCount = countProduit ;
        }
        
    }
    
    public ArrayList<Produit> getPdtObjects() {
        return pdtObjects;
    }

    public void setPdtObjects(ArrayList<Produit> productsObjects) {
        this.pdtObjects = productsObjects;
    }

    public String getEsgnLabel() {
        return esgnLabel;
    }

    public int getPdtCount() {
        return pdtCount;
    }

    public void setEsgnLabel(String esgnLabel) {
        this.esgnLabel = esgnLabel;
    }

    public void setPdtCount(int pdtCount) {
        this.pdtCount = pdtCount;
    }


    
    
    
    
    
    
}
