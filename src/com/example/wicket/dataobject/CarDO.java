package com.example.wicket.dataobject;

import java.io.Serializable;

public class CarDO extends AssetDO implements Serializable
{
	private static final long serialVersionUID = 3891609834088243707L;
	
	private String vin;
	private String manufacturer;
	private String model;
	private boolean insured;
	private String insurer;
	private String digitalId;
	private String digitalIdOwner;
	private String vehicleOwner;
	private long lastTransactionTimestamp;
	private String lastTransaction;
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
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
	public String getInsurer() {
		return insurer;
	}
	public boolean isInsured() {
		return insured;
	}
	public void setInsured(boolean insured) {
		this.insured = insured;
	}
	public String isInsurer() {
		return insurer;
	}
	public void setInsurer(String insurer) {
		this.insurer = insurer;
	}
	public String getDigitalId() {
		return digitalId;
	}
	public void setDigitalId(String digitalId) {
		this.digitalId = digitalId;
	}
	public String getDigitalIdOwner() {
		return digitalIdOwner;
	}
	public void setDigitalIdOwner(String digitalIdOwner) {
		this.digitalIdOwner = digitalIdOwner;
	}
	@Override
	public String getAssetId() {
		assetId = this.vin;
		return assetId;
	}
	@Override
	public void setAssetId(String assetId) {
		this.assetId = assetId;
		this.vin = assetId;
	}
	
	public String getVehicleOwner() {
		return vehicleOwner;
	}
	public void setVehicleOwner(String vehicleOwner) {
		this.vehicleOwner = vehicleOwner;
	}
	public long getLastTransactionTimestamp() {
		return lastTransactionTimestamp;
	}
	public void setLastTransactionTimestamp(long lastTransactionTimestamp) {
		this.lastTransactionTimestamp = lastTransactionTimestamp;
	}
	public String getLastTransaction() {
		return lastTransaction;
	}
	public void setLastTransaction(String lastTransaction) {
		this.lastTransaction = lastTransaction;
	}
	@Override
	public boolean equals(Object o)
	{
		return o != null && o instanceof CarDO && ((CarDO) o).getVin().equals(this.getVin());
	}
	@Override
	public int hashCode()
	{
		return this.vin.hashCode();
	}
}
