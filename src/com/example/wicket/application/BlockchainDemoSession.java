package com.example.wicket.application;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import com.example.wicket.dataobject.CarDO;
import com.example.wicket.dataobject.ClaimDO;

public class BlockchainDemoSession extends WebSession
{
	private static final long serialVersionUID = 1L;
	
	Map<String, CarDO> hondaCars = null;
	Map<String, CarDO> toyotaCars = null;
	Map<String, ClaimDO> claims = null;

	public BlockchainDemoSession(Request request) 
	{
		super(request);
	}

	
	public static BlockchainDemoSession get()
	{
		return (BlockchainDemoSession)Session.get();
	}


	public Map<String, CarDO> getHondaCars() 
	{
		if(hondaCars == null)
		{
			hondaCars = new HashMap<>();
		}
		
		return hondaCars;
	}


	public void setHondaCars(Map<String, CarDO> hondaCars) 
	{
		this.hondaCars = hondaCars;
	}


	public Map<String, CarDO> getToyotaCars() 
	{
		if(toyotaCars == null)
		{
			toyotaCars = new HashMap<>();
		}
		
		return toyotaCars;
	}


	public void setToyotaCars(Map<String, CarDO> toyotaCars) 
	{
		this.toyotaCars = toyotaCars;
	}
	

	
	public Map<String, ClaimDO> getClaims() 
	{
		if(claims == null)
		{
			claims = new HashMap<>();
		}
		
		return claims;
	}


	public void setClaims(Map<String, ClaimDO> claims) {
		this.claims = claims;
	}
}
