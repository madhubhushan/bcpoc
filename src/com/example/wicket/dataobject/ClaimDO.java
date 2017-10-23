package com.example.wicket.dataobject;

public class ClaimDO extends AssetDO {

	private static final long serialVersionUID = -4490516754156859155L;
	
	private String claimNr;
	private ClaimStatusEnum claimStatus = ClaimStatusEnum.OPEN;
	private double claimAmount;
	private String policyNr;
	private String vehicleId;
	private String insurer;
	
	public String getClaimNr() {
		return claimNr;
	}

	public void setClaimNr(String claimNr) {
		this.claimNr = claimNr;
	}

	public ClaimStatusEnum getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(ClaimStatusEnum claimStatus) {
		this.claimStatus = claimStatus;
	}

	public double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(double claimAmount) {
		this.claimAmount = claimAmount;
	}

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
		assetId = this.claimNr;
		return assetId;
	}

	@Override
	public void setAssetId(String assetId) {
		this.assetId = assetId;
		this.claimNr = assetId;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o != null && o instanceof ClaimDO && ((ClaimDO) o).getClaimNr().equals(this.getClaimNr());
	}
	@Override
	public int hashCode() {
		return this.claimNr.hashCode();
	}

	public enum ClaimStatusEnum {
		OPEN,
		CLOSED;
	}
}
