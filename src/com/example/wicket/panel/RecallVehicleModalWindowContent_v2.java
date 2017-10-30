package com.example.wicket.panel;

import static com.example.wicket.helper.Constants.EMPTY_STRING;
import static com.example.wicket.helper.Constants.HONDA;
import static com.example.wicket.helper.Constants.HONDA_AMAZE;
import static com.example.wicket.helper.Constants.HONDA_BRIO;
import static com.example.wicket.helper.Constants.HONDA_CITY;
import static com.example.wicket.helper.Constants.HONDA_JAZZ;
import static com.example.wicket.helper.Constants.TOYOTA;
import static com.example.wicket.helper.Constants.TOYOTA_CAMRY;
import static com.example.wicket.helper.Constants.TOYOTA_COROLLA;
import static com.example.wicket.helper.Constants.TOYOTA_FORTUNER;
import static com.example.wicket.helper.Constants.TOYOTA_INNOVA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.example.wicket.dataobject.CarDO;
import com.example.wicket.helper.BlockChainRestServiceHelper;

public class RecallVehicleModalWindowContent_v2 extends Panel  
{
	private static final long serialVersionUID = 1L;
	
	private WebMarkupContainer recalledVehicleDetailsContainer;
	
	Set<IModel<CarDO>> recallVehicles = new HashSet<IModel<CarDO>>();
	private String selectedModel;
	private String reCallLinkId;
	private static final String[] HONDA_MODELS = {HONDA_CITY, HONDA_JAZZ, HONDA_AMAZE, HONDA_BRIO};
	private static final String[] TOYOTA_MODELS = {TOYOTA_FORTUNER, TOYOTA_INNOVA, TOYOTA_COROLLA, TOYOTA_CAMRY};

	public RecallVehicleModalWindowContent_v2(String id, String reCallLinkId) 
	{
		super(id);
		this.reCallLinkId = reCallLinkId;
	}
	
	@Override
	protected void onInitialize() 
	{
		super.onInitialize();
		
		WebMarkupContainer contentContainer = new WebMarkupContainer("contentContainer");
		contentContainer.add(getModelDD());
		contentContainer.add(getSubmitButton());
		
		add(contentContainer);
		
		add(getContainer());
	}
	
	private Component getModelDD() 
	{
		final IModel<String> vehicleModel = new Model<String>();
		final DropDownChoice<String> vehicleModelDD = new DropDownChoice<String>("vehicleModelDD", vehicleModel, getVehicleModelList(reCallLinkId));
		vehicleModelDD.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = -7679311805396718717L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				selectedModel = vehicleModelDD.getModelObject().toString();
			}

		});
		vehicleModelDD.setOutputMarkupPlaceholderTag(true);	
		return vehicleModelDD;
	}

	private Component getSubmitButton() 
	{

		Button submitButton = new Button("submit");
		submitButton.add(new AjaxFormComponentUpdatingBehavior("onclick")
		{
			private static final long serialVersionUID = 2606654482015570232L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				try 
				{
					String manufacturer = EMPTY_STRING;
	
					if("getHondaRecallVehicleLink".equalsIgnoreCase(reCallLinkId))
					{
						manufacturer = HONDA;
					}
					else
					{
						manufacturer = TOYOTA;
					}
	
					if(selectedModel != null && selectedModel != EMPTY_STRING)
					{
						//TODO
						List<CarDO> allCars = BlockChainRestServiceHelper.getRecallVehicleDetails(manufacturer, selectedModel);
						//List<CarDO> allCars = MockBlockChainRestServiceHelper.getRecallVehicleDetails(manufacturer, selectedModel);
						
						if(allCars != null && !allCars.isEmpty())
						{
							for(CarDO car : allCars)
							{
								recallVehicles.add(Model.of(car));
							}
						}

						target.add(recalledVehicleDetailsContainer);
						target.appendJavaScript("Wicket.Window.get().autoSizeWindow();");
					}
				} 
				catch (Exception e) {
					e.printStackTrace(System.err);
				}
				
			}
		});
		submitButton.setOutputMarkupPlaceholderTag(true);
		
		return submitButton;
	}

	private WebMarkupContainer getContainer() 
	{
		recalledVehicleDetailsContainer = new WebMarkupContainer("recalledVehicleDetailsContainer")
		{
			private static final long serialVersionUID = -2319659075729669192L;

			protected void onConfigure() 
			{
				if(!recallVehicles.isEmpty())
				{
					setVisibilityAllowed(true);
				}
			};
		};
		recalledVehicleDetailsContainer.setOutputMarkupPlaceholderTag(true);
		RefreshingView<CarDO> recalledVehicleDetailsRefresingView = new RefreshingView<CarDO>("recalledVehicleDetailsRefresingView")
		{
			private static final long serialVersionUID = 338149813740014483L;

			@Override
			protected Iterator<IModel<CarDO>> getItemModels() 
			{
				return recallVehicles.iterator();
			}

			@Override
			protected void populateItem(Item<CarDO> item) 
			{
				Label vinNumber = new Label("vinNumber", Model.of(item.getModelObject().getVin()));
				Label vehicleName = new Label("vehicleOwner", Model.of(item.getModelObject().getVehicleOwner()));
				item.add(vinNumber);
				item.add(vehicleName);
			}
			
			@Override
			protected void onConfigure() 
			{
				super.onConfigure();
				if(!recallVehicles.isEmpty())
				{
					setVisibilityAllowed(true);
				}
			}
		};
		recalledVehicleDetailsRefresingView.setOutputMarkupPlaceholderTag(true);
		recalledVehicleDetailsContainer.add(recalledVehicleDetailsRefresingView);
		return recalledVehicleDetailsContainer;
	}
	
	private List<? extends String> getVehicleModelList(String id)
	{
		if("getToyotaRecallVehicleLink".equalsIgnoreCase(id))
		{
			return Arrays.asList(TOYOTA_MODELS);
		}
		else
		{
			return Arrays.asList(HONDA_MODELS);
		}
	}
}