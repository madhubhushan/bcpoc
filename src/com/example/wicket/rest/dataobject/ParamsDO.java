package com.example.wicket.rest.dataobject;

import java.io.Serializable;

public class ParamsDO implements Serializable 
{
	private static final long serialVersionUID = 7351160410763283170L;
	
	private int type;
	private ChainCodeDO chaincodeID;
	private CtorMsgDO ctorMsg;
	private String secureContext;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public ChainCodeDO getChaincodeID() {
		return chaincodeID;
	}
	public void setChaincodeID(ChainCodeDO chaincodeID) {
		this.chaincodeID = chaincodeID;
	}
	public CtorMsgDO getCtorMsg() {
		return ctorMsg;
	}
	public void setCtorMsg(CtorMsgDO ctorMsg) {
		this.ctorMsg = ctorMsg;
	}
	public String getSecureContext() {
		return secureContext;
	}
	public void setSecureContext(String secureContext) {
		this.secureContext = secureContext;
	}
}
