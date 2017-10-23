package com.example.wicket.dataobject;

import java.io.Serializable;

public class TransactionDO implements Serializable
{
	public enum TransactionEnum {
		VALID,
		INVALID,
		FRAUDULENT,
		DUPLICATE
	}
	private static final long serialVersionUID = -5220995813097365775L;
	
	private long id;
	private String transactionType;
	private String transactionSource;
	private String transactionDestination;
	private String vehicleOwner;
	private String tokenOwner;
	private String involvingVehicle;
	private TransactionEnum transactionStatus = TransactionEnum.VALID;
	private String transactionKey;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionSource() {
		return transactionSource;
	}
	public void setTransactionSource(String transactionSource) {
		this.transactionSource = transactionSource;
	}
	public String getTransactionDestination() {
		return transactionDestination;
	}
	public void setTransactionDestination(String transactionDestination) {
		this.transactionDestination = transactionDestination;
	}
	public String getVehicleOwner() {
		return vehicleOwner;
	}
	public void setVehicleOwner(String vehicleOwner) {
		this.vehicleOwner = vehicleOwner;
	}
	public String getTokenOwner() {
		return tokenOwner;
	}
	public void setTokenOwner(String tokenOwner) {
		this.tokenOwner = tokenOwner;
	}
	public String getInvolvingVehicle() {
		return involvingVehicle;
	}
	public void setInvolvingVehicle(String involvingVehicle) {
		this.involvingVehicle = involvingVehicle;
	}
	public TransactionEnum getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(TransactionEnum transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getTransactionKey() {
		return transactionKey;
	}
	public void setTransactionKey(String transactionKey) {
		this.transactionKey = transactionKey;
	}
}
