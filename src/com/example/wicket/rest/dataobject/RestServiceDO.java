package com.example.wicket.rest.dataobject;

import java.io.Serializable;

public class RestServiceDO implements Serializable
{
	private static final long serialVersionUID = -7885634558750878844L;
	
	private String jsonrpc;
	private String method;
	private ParamsDO params;
	private int id;
	private ResultDO result;
	
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public ParamsDO getParams() {
		return params;
	}
	public void setParams(ParamsDO params) {
		this.params = params;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ResultDO getResult() {
		return result;
	}
	public void setResult(ResultDO result) {
		this.result = result;
	}
}
