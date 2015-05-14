package org.dmb.trueprice.objects;


public class ProduitResult {
	
/**
 * Basic attributes to get the refferred product
 */
	private Long productRefId ;
	
	private Long  qttRefId ;
	private String qttRefUnit ;
	private double qttRefValue ;
	
/**
 *  Specific data for result
 */	
	/**
	 * Pdt Init Data
	 */
	private double priceUnitSales ;
	private double priceUnitMeasure ;
	
	// The user should be able to modify the qtt unit & value 
	// according to the current sales unit to be easily calculated
	private String qttUnitModified = null ;
	private double qttValueModified = -1 ;
	
	/**
	 * Pdt Expc Data
	 */
	
	private double qttCarried = -1;
	private double priceHTVA = -1;
	private double priceTTC = -1;	
	
	private String tvaTx = null ;
	private double tvaValue = -1 ;	
	
	/**
	 * Pdt Final Data
	 */

	
	
		
	
/**
 * 
 * Setters & getters
 * 	
 */
	
	/**
	 * 
	 * Main class attributes
	 * 
	 */
	
	public Long getProductRefId() {
		return productRefId;
	}
	public void setProductRefId(Long productRefId) {
		this.productRefId = productRefId;
	}
	
	public Long getQttRefId() {
		return qttRefId;
	}
	public String getQttRefUnit() {
		return qttRefUnit;
	}
	public double getQttRefValue() {
		return qttRefValue;
	}
	

	
	/**
	 * 
	 * Init Result attributes
	 * 
	 */	
	
	public double getPriceUnitSales() {
		return priceUnitSales;
	}
	public double getPriceUnitMeasure() {
		return priceUnitMeasure;
	}
	public String getQttUnitModified() {
		return qttUnitModified == null ? qttRefUnit : qttUnitModified ;
	}
	public double getQttValueModified() {
		return qttValueModified == -1 ? qttRefValue : qttValueModified ;
	}

	
	public void setPriceUnitSales(double priceUnitSales) {
		this.priceUnitSales = priceUnitSales;
	}
	public void setPriceUnitMeasure(double priceUnitMeasure) {
		this.priceUnitMeasure = priceUnitMeasure;
	}
	public boolean setQttUnitModified(String qttUnitModified) {
		if ( qttUnitModified != null && qttUnitModified.length() > 0) {
			this.qttUnitModified = qttUnitModified;
			return true;
		} else {
			return false;
		}
	}
	public boolean setQttValueModified(double qttValueModified) {
		if (qttValueModified > 0) {
			this.qttValueModified = qttValueModified;
			return true;
		} else {
			return false;
		}
	}
	
	
	
	/**
	 * 
	 * Expected Result attributes
	 * 
	 */		
	
	public double getQttCarried() {
		return qttCarried;
	}
	public double getPriceHTVA() {
		return priceHTVA;
	}
	public String getTvaTx() {
		return tvaTx;
	}
	public double getTvaValue() {
		return tvaValue;
	}
	public double getPriceTTC() {
		return priceTTC;
	}
	
	public void setQttCarried(double qttCarried) {
		this.qttCarried = qttCarried;
	}
	public void setPriceHTVA(double priceHTVA) {
		this.priceHTVA = priceHTVA;
	}
	public void setTvaTx(String tvaTx) {
		this.tvaTx = tvaTx;
	}
	public void setTvaValue(double tvaValue) {
		this.tvaValue = tvaValue;
	}
	public void setPriceTTC(double priceTTC) {
		this.priceTTC = priceTTC;
	}
	

	
	
	
	
	
	public ProduitResult( Long referredProductId, Long refferedQttDetailId, String refQttUnit, double refQttValue) {
		this.productRefId = referredProductId;
		this.qttRefId = refferedQttDetailId ;
		this.qttRefUnit = refQttUnit ;
		this.qttRefValue = refQttValue ;
	}
	
	
	
	
}
