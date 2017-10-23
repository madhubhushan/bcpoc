package com.example.wicket.rest.dataobject;

import java.io.Serializable;

public class CtorMsgDO implements Serializable
{
	private static final long serialVersionUID = 4936819729236199082L;

	private String function;
	private String[] args;
	
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String[] getArgs() {
		return args;
	}
	public void setArgs(String[] args) {
		this.args = args;
	}
}
