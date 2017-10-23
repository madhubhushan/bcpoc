package com.example.wicket.rest.dataobject;

import java.io.Serializable;

public class ResultDO implements Serializable
{
	private static final long serialVersionUID = 1835559173729031910L;
	
	private String status;
	private String message;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	} 
}
