package org.dmb.trueprice.objects.android;

 public class SynchronizedList {
	
	private Long 	listeId ;
	private String 	listeLabel ;
	private String 	dateSynchronized ;
	
	public SynchronizedList(Long listeId, String listeLabel, String DateSynchronized) {
		this.listeId = listeId;
		this.listeLabel = listeLabel;
		this.dateSynchronized = DateSynchronized;
	}
	
	public String getDateSynchronized() {
		return dateSynchronized;
	}
	public Long getListeId() {
		return listeId;
	}
	public String getListeLabel() {
		return listeLabel;
	}
	public void setDateSynchronized(String dateSynchronized) {
		this.dateSynchronized = dateSynchronized;
	}
	public void setListeId(Long listeId) {
		this.listeId = listeId;
	}
	public void setListeLabel(String listeLabel) {
		this.listeLabel = listeLabel;
	}
	
	
}