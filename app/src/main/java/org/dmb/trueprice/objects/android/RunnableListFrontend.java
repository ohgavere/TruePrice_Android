package org.dmb.trueprice.objects.android;

import org.dmb.trueprice.objects.AvailableList;

public class RunnableListFrontend extends AvailableList {
	
	private final String logHead = this.getClass().getSimpleName() ;
	
	private String dateSynchronized ;


	public RunnableListFrontend(Long listeId, String listeLabel, int pdtCount) {
		super(listeId, listeLabel, pdtCount);
		
	}
	
	public RunnableListFrontend(Long listeId, String listeLabel, String dateModif, int pdtCount) {
		super(listeId, listeLabel, pdtCount);
		setDate(dateSynchronized);
	}


	@Override
	public String getDate() {
		return getDateSynchronized();
	}
	
	public String getDateSynchronized() {
		return dateSynchronized;
	}
	public void setDateSynchronized(String dateSynchronized) {
		this.dateSynchronized = dateSynchronized;
	}
	
	@Override	
	public void setDate(String date) {
		setDateSynchronized(date);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() 
			+ " \t ID:[" + getListeId() +"]"
			+ " \t LABEL:[" + getListeLabel() +"]"
			+ " \n PDT_COUNT:[" + getPdtCount() +"]"
			+ " \t DATE_SYNCHRONIZED:[" + getDate() +"]";
	}
	
	
}
