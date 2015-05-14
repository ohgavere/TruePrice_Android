package org.dmb.trueprice.interfaces;

public interface InitResult {
	
	
	public double getPriceUnitSales();
	public double getPriceUnitMeasure();
	public String getQttUnitModified();
	public double getQttValueModified();
	
		
	public void setPriceUnitSales(double priceUnitSales);
	public void setPriceUnitMeasure(double priceUnitMeasure);
	public boolean setQttUnitModified(String qttUnitModified);
	public boolean setQttValueModified(double qttValueModified);
	
	
	

}
