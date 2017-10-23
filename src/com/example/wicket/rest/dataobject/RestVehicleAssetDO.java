package com.example.wicket.rest.dataobject;

public class RestVehicleAssetDO
{
	private String vehicleid;
	private String manufacturer;
	private String model;
	private String ownername;
	private String digitalid;
	private String digitalidowner;
	private long timestamp;
	private String transactiontype;
	private String transactionmetadata;
	
	public String getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(String vehicleid) {
		this.vehicleid = vehicleid;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getOwnername() {
		return ownername;
	}
	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}
	public String getDigitalid() {
		return digitalid;
	}
	public void setDigitalid(String digitalid) {
		this.digitalid = digitalid;
	}
	public String getDigitalidowner() {
		return digitalidowner;
	}
	public void setDigitalidowner(String digitalidowner) {
		this.digitalidowner = digitalidowner;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getTransactiontype() {
		return transactiontype;
	}
	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}
	public String getTransactionmetadata() {
		return transactionmetadata;
	}
	public void setTransactionmetadata(String transactionmetadata) {
		this.transactionmetadata = transactionmetadata;
	}
}
