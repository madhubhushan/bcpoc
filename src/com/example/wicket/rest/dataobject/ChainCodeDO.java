package com.example.wicket.rest.dataobject;

import java.io.Serializable;

public class ChainCodeDO implements Serializable
{
	private static final long serialVersionUID = -800287880292227710L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
