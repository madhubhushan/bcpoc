package com.example.wicket.application;

import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import com.example.wicket.pages.BlockchainDemoMainPage;
import com.example.wicket.pages.BlockchainDemoMainPage_v1;
import com.example.wicket.pages.BlockchainDemoMainPage_v2;

public class BlockchainDemoApplication extends WebApplication {

	@Override
	public RuntimeConfigurationType getConfigurationType() {
		return RuntimeConfigurationType.DEPLOYMENT;
	}

	@Override
	public Class<? extends Page> getHomePage()
	{
		return BlockchainDemoMainPage_v1.class;
	}

	@Override
	protected void init()
	{
		super.init();
		mountPage("/main", BlockchainDemoMainPage.class);
		mountPage("/v1", BlockchainDemoMainPage_v1.class);
		mountPage("/v2", BlockchainDemoMainPage_v2.class);
	}
	
	@Override
	public Session newSession(Request request, Response response) 
	{
		return new BlockchainDemoSession(request);
	}
}
