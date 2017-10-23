package com.example.wicket.dataobject;

import java.io.Serializable;

public class PolicyDO extends AssetDO implements Serializable
{
	private static final long serialVersionUID = -8056025183335778860L;
	
	private String policyNr;
	private String vehicleId;
	private String insurer;
	
	public String getPolicyNr() {
		return policyNr;
	}
	public void setPolicyNr(String policyNr) {
		this.policyNr = policyNr;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getInsurer() {
		return insurer;
	}
	public void setInsurer(String insurer) {
		this.insurer = insurer;
	}
	@Override
	public String getAssetId() {
		assetId = this.policyNr;
		return assetId;
	}
	@Override
	public void setAssetId(String assetId) {
		this.assetId = assetId;
		this.policyNr = assetId;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o != null && o instanceof PolicyDO && ((PolicyDO) o).getPolicyNr().equals(this.getPolicyNr());
	}
	@Override
	public int hashCode() {
		return this.policyNr.hashCode();
	}
}
