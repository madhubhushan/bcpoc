package com.example.wicket.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.example.wicket.dataobject.AssetDO;
import com.example.wicket.dataobject.CarDO;
import com.example.wicket.dataobject.PolicyDO;

public abstract class ModalPanelContent extends Panel 
{
	private static final long serialVersionUID = -8736752287102219250L;
	
	private static final List<String> INSURANCE_COMPANIES = Arrays.asList("Insurer1", "Insurer2");
	private Set<IModel<AssetDO>> customerAssets;
	boolean isShowOnlyInsuredVIN;
	DropDownChoice<String> getPolicyInsurerDD;
	DropDownChoice<String> vehicleVINDD;
	protected WebMarkupContainer contentContainer;
	protected WebMarkupContainer errorContainer;
	PolicyDO policy;

	protected boolean showError;

	

	public ModalPanelContent(String id, Set<IModel<AssetDO>> customerAssets,  boolean isShowOnlyInsuredVIN) 
	{
		super(id);
		this.customerAssets = customerAssets;
		this.isShowOnlyInsuredVIN = isShowOnlyInsuredVIN;
		policy = new PolicyDO();
	}
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		contentContainer = new WebMarkupContainer("contentContainer")
		{
			private static final long serialVersionUID = 1L;

			protected void onConfigure() 
			{
				setVisibilityAllowed(!showError);
			};
		};
				
		contentContainer.add(getPolicyInsurerDD());
		
		contentContainer.add(getVehicleVINDD());
		
		contentContainer.add(getSubmitButton());
		contentContainer.setOutputMarkupPlaceholderTag(true);
		add(contentContainer);
		
		errorContainer = new WebMarkupContainer("errorContainer")
		{
			private static final long serialVersionUID = 1L;

			protected void onConfigure() 
			{
				setVisibilityAllowed(showError);
			};
		};
		
		errorContainer.add(getErrorMessage());
		errorContainer.setOutputMarkupPlaceholderTag(true);
		add(errorContainer);		
	}

	private Component getErrorMessage() 
	{
		return new Label("errorMessage", new Model<String>()
		{
			private static final long serialVersionUID = 3680341697131694374L;

			@Override
			public String getObject() {
				return "Previous Transaction could not be completed due to some technical difficulties. Please try again";
			}
		});
	}
	private Component getVehicleVINDD() 
	{
		vehicleVINDD = new DropDownChoice<String>("vehicleVINDD", new PropertyModel<String>(policy, "vehicleId"),getVehicleVINList());
		vehicleVINDD.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = -7679311805396718717L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				policy.setVehicleId(vehicleVINDD.getModelObject().toString());
//				target.appendJavaScript("setTimeout(function(){var thisWindow = Wicket.Window.get();\n"
//	                    + "if (thisWindow) {\n"
//						+ "var modalElement = document.querySelector('.wicket-modal'); \n"
//	                    + "modalElement.style.removeProperty('top');\n"
//	                    + "modalElement.style.removeProperty('left');\n"
//	                    + "modalElement.style.removeProperty('position');\n"
//	                    + "modalElement.style.setProperty('top', '25%');\n"
//	                    + "modalElement.style.setProperty('left', '25%');\n"
//	                    + "}}, 100)");
			}
	
		});
		vehicleVINDD.setOutputMarkupPlaceholderTag(true);	
		return vehicleVINDD;
	}
	private Component getPolicyInsurerDD() 
	{
		getPolicyInsurerDD = new DropDownChoice<String>("policyInsurerDD", new PropertyModel<String>(policy, "insurer"), INSURANCE_COMPANIES);
				
		getPolicyInsurerDD.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 6083039313988087140L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				policy.setInsurer(getPolicyInsurerDD.getModelObject().toString());
//				target.appendJavaScript("setTimeout(function(){var thisWindow = Wicket.Window.get();\n"
//	                    + "if (thisWindow) {\n"
//						+ "var modalElement = document.querySelector('.wicket-modal'); \n"
//	                    + "modalElement.style.removeProperty('top');\n"
//	                    + "modalElement.style.removeProperty('left');\n"
//	                    + "modalElement.style.removeProperty('position');\n"
//	                    + "modalElement.style.setProperty('top', '25%');\n"
//	                    + "modalElement.style.setProperty('left', '25%');\n"
//	                    + "}}, 100)");
			}
	
		});
		getPolicyInsurerDD.setOutputMarkupPlaceholderTag(true);
		return getPolicyInsurerDD;
	}
	private List<? extends String> getVehicleVINList() 
	{
		List<String> insuredVehicleVINList = new ArrayList<String>();
		
		
		for(IModel<AssetDO> asset : customerAssets)
		{
			if(asset.getObject() instanceof CarDO &&(!isShowOnlyInsuredVIN || (isShowOnlyInsuredVIN && ((CarDO) asset.getObject()).isInsured())))
			{
				insuredVehicleVINList.add(((CarDO) asset.getObject()).getVin());
			}
		}
		
		return insuredVehicleVINList;
	}
	
	private Button getSubmitButton() 
	{
		Button submitButton = new Button("submit");
		submitButton.add(new AjaxFormComponentUpdatingBehavior("onclick")
		{
			private static final long serialVersionUID = 2606654482015570232L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				policy.setPolicyNr("Pol_" + System.currentTimeMillis());
				onUpdateModalPanelContent(policy, target);
			}
		});
		submitButton.setOutputMarkupPlaceholderTag(true);
		
		return submitButton;
	}
	
	public abstract void onUpdateModalPanelContent(PolicyDO policy, AjaxRequestTarget target);
	
}
