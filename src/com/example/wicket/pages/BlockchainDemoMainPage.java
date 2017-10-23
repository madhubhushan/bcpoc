package com.example.wicket.pages;

import static com.example.wicket.helper.Constants.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.example.wicket.dataobject.AssetDO;
import com.example.wicket.dataobject.CarDO;
import com.example.wicket.dataobject.ClaimDO;
import com.example.wicket.dataobject.ClaimDO.ClaimStatusEnum;
import com.example.wicket.dataobject.PolicyDO;
import com.example.wicket.dataobject.TransactionDO;
import com.example.wicket.dataobject.TransactionDO.TransactionEnum;
import com.example.wicket.helper.BlockChainRestServiceHelper;
import com.example.wicket.helper.StringHelper;
import com.example.wicket.panel.ModalPanelContent;
import com.example.wicket.panel.RecallVehicleModalWindowContent;

public class BlockchainDemoMainPage extends WebPage {

	private static final long serialVersionUID = 2685458990968710768L;

	// Data models
	private Set<IModel<CarDO>> hondaCars = new HashSet<IModel<CarDO>>();
	private Set<IModel<CarDO>> toyotaCars = new HashSet<IModel<CarDO>>();
	private Set<IModel<CarDO>> hondaSoldCars = new HashSet<IModel<CarDO>>();
	private Set<IModel<CarDO>> toyotaSoldCars = new HashSet<IModel<CarDO>>();
	private Set<IModel<CarDO>> majorWorldDealerCars = new HashSet<IModel<CarDO>>();
	private Set<IModel<CarDO>> hillsideDealerCars = new HashSet<IModel<CarDO>>();
	private Set<IModel<AssetDO>> jackAssets = new HashSet<IModel<AssetDO>>();
	private Set<IModel<AssetDO>> roseAssets = new HashSet<IModel<AssetDO>>();
	private Set<IModel<AssetDO>> romeoAssets = new HashSet<IModel<AssetDO>>();
	private Set<IModel<AssetDO>> julietAssets = new HashSet<IModel<AssetDO>>();
	private Set<IModel<AssetDO>> usaaAssets = new HashSet<IModel<AssetDO>>();
	private Set<IModel<AssetDO>> geicoAssets = new HashSet<IModel<AssetDO>>();
	private Set<IModel<CarDO>> euroWorksBodyShopCars = new HashSet<IModel<CarDO>>();
	private Set<IModel<CarDO>> autoCaresBodyShopCars = new HashSet<IModel<CarDO>>();

	private List<IModel<TransactionDO>> transactions =  new ArrayList<IModel<TransactionDO>>();

	private CarDO draggingCar;

	private String destination;

	// Dynamic components
	private AjaxLink<Void> createVehicleLink;
	private AjaxLink<Void> fileClaimLink;
	private WebMarkupContainer assetTransactionContainer;
	private WebMarkupContainer transactionDetailsRow;
	private ModalWindow getRecallVehicleModalWindow;
	private ModalWindow getPolicyModalWindow;
	private ModalWindow fileClaimModalWindow;
	private ModalWindow errorMessageModalWindow;
	private Panel fileClaimModalpanelContent;
	private Panel getPolicyModalWindowContent;
	private Panel getRecallVehicleModalWindowContent;
	private Fragment errorDetailsModalWindowContent;

	// Wicket IDs
	private static final String MAJOR_WORLD_CONTAINER = "majorWorldContainer";
	private static final String HILL_SIDE_CONTAINER = "hillsideContainer";
	private static final String USAA_CONTAINER = "usaaContainer";
	private static final String GEIKO_CONTAINER = "geicoContainer";
	private static final String EURO_AUTO_WORKS_CONTAINER = "euroAutoWorksContainer";
	private static final String AUTO_CARE_EAST_CONTAINER = "autoCareEastCarsContainer";
	private static final String JACK_CONTAINER = "jackContainer";
	private static final String ROSE_CONTAINER = "roseContainer";
	private static final String ROMEO_CONTAINER = "romeoContainer";
	private static final String JULIET_CONTAINER = "julietContainer";

	private static final String JACK_REFRESHING_VIEW = "jackCarsListRefresingView";
	private static final String ROSE_REFRESHING_VIEW = "roseCarsListRefresingView";
	private static final String ROMEO_REFRESHING_VIEW = "romeoCarsListRefresingView";
	private static final String JULIET_REFRESHING_VIEW = "julietCarsListRefresingView";

	// Constants
	private static final List<String> MANUFACTURERS = Arrays.asList(HONDA, TOYOTA);
	private static final List<String> DEALER_CONTAINERS = Arrays.asList(MAJOR_WORLD_CONTAINER, HILL_SIDE_CONTAINER);
	private static final List<String> CUSTOMER_CONTAINERS = Arrays.asList(JACK_CONTAINER, ROSE_CONTAINER, ROMEO_CONTAINER, JULIET_CONTAINER);
	private static final List<String> INSURER_CONTAINERS = Arrays.asList(USAA_CONTAINER, GEIKO_CONTAINER);
	private static final List<String> BODYSHOP_CONTAINERS = Arrays.asList(EURO_AUTO_WORKS_CONTAINER, AUTO_CARE_EAST_CONTAINER);

	private static final List<String> CUSTOMER_REFRESHING_VIEWS = Arrays.asList("jackCarsListRefresingView", "roseCarsListRefresingView", "julietCarsListRefresingView", "romeoCarsListRefresingView");

	private static final String[] HONDA_MODELS = {HONDA_CITY, HONDA_JAZZ, HONDA_AMAZE, HONDA_BRIO};
	private static final String[] TOYOTA_MODELS = {TOYOTA_FORTUNER, TOYOTA_INNOVA, TOYOTA_COROLLA, TOYOTA_CAMRY};

	public BlockchainDemoMainPage(PageParameters params) {
		super(params);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(createAssetTransactionContainer());
		createErrorDetailsModelWindowContent();
		add(createtransactionDetailsRow());
	}

	private AjaxLink<Void> getCreateVehicleLink(final String id)
	{
		createVehicleLink = new AjaxLink<Void>(id)
		{
			private static final long serialVersionUID = 4562875978259353158L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				try 
				{
					String brand = EMPTY_STRING;
					String model = EMPTY_STRING;
					int modelnumber = new Random().nextInt(4);

					if("createHondaCarLink".equalsIgnoreCase(id))
					{
						brand = HONDA;
						model = HONDA_MODELS[modelnumber];
					}
					else
					{
						brand = TOYOTA;
						model = TOYOTA_MODELS[modelnumber];
					}

					boolean createVehicleSuccess = BlockChainRestServiceHelper.createVehicle(brand, model);

					if(createVehicleSuccess)
					{
						CarDO newlyCreatedCar = BlockChainRestServiceHelper.getAddedVehicleDetails();

						if(newlyCreatedCar != null)
						{
							TransactionDO transaction = new TransactionDO(); 

							if(HONDA.equalsIgnoreCase(newlyCreatedCar.getVehicleOwner()))
							{
								hondaCars.add(Model.of(newlyCreatedCar));
								transaction.setTokenOwner(HONDA);
								transaction.setTransactionSource(HONDA);
							}
							else
							{
								toyotaCars.add(Model.of(newlyCreatedCar));
								transaction.setTokenOwner(TOYOTA);
								transaction.setTransactionSource(TOYOTA);
							}

							transaction.setId(newlyCreatedCar.getLastTransactionTimestamp());
							transaction.setTransactionDestination("N/A");
							transaction.setTransactionType("Asset Creation (Vehicle)");
							transaction.setVehicleOwner(newlyCreatedCar.getVehicleOwner());
							transaction.setInvolvingVehicle(newlyCreatedCar.getVin());
							transaction.setTransactionKey(newlyCreatedCar.getLastTransaction());
							transaction.setId(newlyCreatedCar.getLastTransactionTimestamp());

							transactions.add(Model.of(transaction));

							target.add(createVehicleLink);
							target.add(assetTransactionContainer);	
							target.add(transactionDetailsRow);
						}
						else
						{
							//TODO done
							errorMessageModalWindow.show(target);
						}
					}
					else
					{
						//TODO done
						errorMessageModalWindow.show(target);
					}
				}
				catch(Exception e) 
				{
					e.printStackTrace(System.err);
					//TODO done
					errorMessageModalWindow.show(target);
				}
			}
		};

		createVehicleLink.setOutputMarkupPlaceholderTag(true);

		return createVehicleLink;
	}
	
	private Component createRecallVehicleLink(final String id) 
	{
		AjaxLink<Void> recallVehicleLink = new AjaxLink<Void>(id)
		{
			private static final long serialVersionUID = -737382470284736850L;

			@Override
			protected void onConfigure()
			{
				super.onConfigure();

				boolean isVisible = false;

				if("getHondaRecallVehicleLink".equalsIgnoreCase(id) && hondaSoldCars != null && !hondaSoldCars.isEmpty())
				{
					isVisible = true;
				}
				else if("getToyotaRecallVehicleLink".equalsIgnoreCase(id) && toyotaSoldCars != null && !toyotaSoldCars.isEmpty())
				{
					isVisible = true;
				}
				setVisibilityAllowed(isVisible);
			}

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				getRecallVehicleModalWindowContent = new RecallVehicleModalWindowContent(getRecallVehicleModalWindow.getContentId(), id);

				getRecallVehicleModalWindow.setContent(getRecallVehicleModalWindowContent);
				getRecallVehicleModalWindow.show(target);
			}

		};
		return recallVehicleLink;
	}

	private WebMarkupContainer createAssetTransactionContainer()
	{
		assetTransactionContainer = new WebMarkupContainer("assetTransactionContainer");
		assetTransactionContainer.setOutputMarkupPlaceholderTag(true);

		assetTransactionContainer.add(getCreateVehicleLink("createHondaCarLink"));
		assetTransactionContainer.add(getCreateVehicleLink("createToyotaCarLink"));
		assetTransactionContainer.add(createRecallVehicleLink("getHondaRecallVehicleLink"));
		assetTransactionContainer.add(createRecallVehicleLink("getToyotaRecallVehicleLink"));
		assetTransactionContainer.add(createRecallVehicleModalWindow());
//		assetTransactionContainer.add(createVehicleDetailsModelWindow());
		assetTransactionContainer.add(createGetPolicyModalWindow());
		assetTransactionContainer.add(createFileClaimModalWindow());
		assetTransactionContainer.add(createErrorMessageModalWindow());
		
		assetTransactionContainer.add(getGetPolicyLink("getJackGetPolicyLink"));
		assetTransactionContainer.add(getFileClaimLink("getJackFileClaimLink"));
		assetTransactionContainer.add(getGetPolicyLink("getRoseGetPolicyLink"));
		assetTransactionContainer.add(getFileClaimLink("getRoseFileClaimLink"));
		assetTransactionContainer.add(getGetPolicyLink("getRomeoGetPolicyLink"));
		assetTransactionContainer.add(getFileClaimLink("getRomeoFileClaimLink"));
		assetTransactionContainer.add(getGetPolicyLink("getJulietGetPolicyLink"));
		assetTransactionContainer.add(getFileClaimLink("getJulietFileClaimLink"));
//		assetTransactionContainer.add(getVehicleDetailsLink());

		assetTransactionContainer.add(getManufacturerCarsListRefresingView("hondaCarsListRefresingView", "hondaCarIcon"));
		assetTransactionContainer.add(getManufacturerCarsListRefresingView("toyotaCarsListRefresingView", "toyotaCarIcon"));
		assetTransactionContainer.add(getCarsListContainer(MAJOR_WORLD_CONTAINER, "majorWorldCarsListRefresingView", "majorWorldCarIcon"));
		assetTransactionContainer.add(getCarsListContainer(HILL_SIDE_CONTAINER, "hillsideCarsListRefresingView", "hillSIdeCarIcon"));
		assetTransactionContainer.add(getCustomerAssetsRefreshingView(USAA_CONTAINER, "usaaCarsListRefresingView", "usaaCarIcon"));
		assetTransactionContainer.add(getCustomerAssetsRefreshingView(GEIKO_CONTAINER, "geicoCarsListRefresingView", "geicoCarIcon"));
		assetTransactionContainer.add(getCarsListContainer(EURO_AUTO_WORKS_CONTAINER, "euroAutoWorksCarsListRefresingView", "euroAutoWorksCarIcon"));
		assetTransactionContainer.add(getCarsListContainer(AUTO_CARE_EAST_CONTAINER, "AutoCareEastCarsListRefresingView", "autoCareEastCarIcon"));

		assetTransactionContainer.add(getCustomerAssetsRefreshingView(JACK_CONTAINER, "jackCarsListRefresingView", "johnCarIcon"));
		assetTransactionContainer.add(getCustomerAssetsRefreshingView(ROSE_CONTAINER, "roseCarsListRefresingView", "samCarIcon"));
		assetTransactionContainer.add(getCustomerAssetsRefreshingView(ROMEO_CONTAINER, "romeoCarsListRefresingView", "peterCarIcon"));
		assetTransactionContainer.add(getCustomerAssetsRefreshingView(JULIET_CONTAINER, "julietCarsListRefresingView", "harryCarIcon"));

		return assetTransactionContainer;
	}

	private Component getGetPolicyLink(final String id) {
		AjaxLink<Void> getPolicyLink = new AjaxLink<Void>(id)
		{
			private static final long serialVersionUID = -737382470284736850L;

			@Override
			protected void onConfigure() {
				super.onConfigure();

				boolean isVisible = false;

				if("getJackGetPolicyLink".equalsIgnoreCase(id) && jackAssets != null && !jackAssets.isEmpty() && containsCar(jackAssets))
				{
					isVisible = true;
				}
				else if("getRoseGetPolicyLink".equalsIgnoreCase(id) && roseAssets != null && !roseAssets.isEmpty() && containsCar(roseAssets))
				{
					isVisible = true;
				}
				else if("getRomeoGetPolicyLink".equalsIgnoreCase(id) && romeoAssets != null && !romeoAssets.isEmpty() && containsCar(romeoAssets))
				{
					isVisible = true;
				}
				else if("getJulietGetPolicyLink".equalsIgnoreCase(id) && julietAssets != null && !julietAssets.isEmpty() && containsCar(julietAssets))
				{
					isVisible = true;
				}

				setVisibilityAllowed(isVisible);
			}

			private boolean containsCar(Set<IModel<AssetDO>> assets)
			{
				for(IModel<AssetDO> asset : assets)
				{
					if(asset.getObject() instanceof CarDO)
					{
						return true;
					}
				}
				return false;
			}

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				getPolicyModalWindowContent = new ModalPanelContent(getPolicyModalWindow.getContentId(), getCustomerAssets(id), false)
				{
					private static final long serialVersionUID = -5330394497328707841L;

					@Override
					public void onUpdateModalPanelContent(PolicyDO policy, AjaxRequestTarget target) 
					{
						try
						{
							String customerName = EMPTY_STRING;
							if("getJackGetPolicyLink".equalsIgnoreCase(id))
							{
								customerName = JACK;
							}
							else if("getRoseGetPolicyLink".equalsIgnoreCase(id))
							{
								customerName = ROSE;
							}
							else if("getRomeoGetPolicyLink".equalsIgnoreCase(id))
							{
								customerName = ROMEO;
							}
							else
							{
								customerName = JULIET;
							}

							boolean buyInsuranceSuccess = BlockChainRestServiceHelper.buyInsurance(customerName, policy.getInsurer(), policy.getVehicleId());
							
							if(buyInsuranceSuccess)
							{
								CarDO insuredCar = BlockChainRestServiceHelper.getVehicleDetails(policy.getVehicleId());

								if(insuredCar != null && !StringHelper.isNullOrEmpty(insuredCar.getDigitalId()) && !StringHelper.isNullOrEmpty(insuredCar.getDigitalIdOwner()))
								{
									String digitalID = insuredCar.getDigitalId();
									String digitalIDOwner = insuredCar.getDigitalIdOwner();

									TransactionDO transaction = new TransactionDO();
									transaction.setTokenOwner(digitalIDOwner);
									transaction.setVehicleOwner(insuredCar.getVehicleOwner());
									transaction.setTransactionSource(insuredCar.getVehicleOwner());
									transaction.setId(System.currentTimeMillis());
									transaction.setTransactionDestination(policy.getInsurer());
									transaction.setTransactionType("Asset Creation (Policy)");
									transaction.setInvolvingVehicle(policy.getVehicleId());
									transaction.setTransactionKey(insuredCar.getLastTransaction());

									transactions.add(Model.of(transaction));

									for(IModel<AssetDO> asset : getCustomerAssets(id))
									{
										if(asset.getObject() instanceof CarDO && policy.getVehicleId().equals(((CarDO)asset.getObject()).getVin()))
										{
											((CarDO)asset.getObject()).setInsured(true);
											((CarDO)asset.getObject()).setDigitalId(digitalID);
											((CarDO)asset.getObject()).setDigitalIdOwner(digitalIDOwner);
										}
									}

									if(USAA.equalsIgnoreCase(policy.getInsurer()))
									{
										usaaAssets.add(Model.of((AssetDO)policy));
									}
									else
									{
										geicoAssets.add(Model.of((AssetDO)policy));
									}

									target.add(getPolicyModalWindowContent);
									target.add(fileClaimLink);
									target.add(assetTransactionContainer);
									target.add(transactionDetailsRow);
								}
								else
								{
									// TODO Done
									getPolicyModalWindow.close(target);
									errorMessageModalWindow.show(target);
								}
							}
							else
							{
								showError = true;
								target.add(contentContainer);
								target.add(errorContainer);
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
							// TODO Done
							showError = true;
							target.add(contentContainer);
							target.add(errorContainer);
						}
						if(!showError)
						{
							getPolicyModalWindow.close(target);
						}
					}
				};
				getPolicyModalWindow.setContent(getPolicyModalWindowContent);
				getPolicyModalWindow.show(target);
			}
		};
		return getPolicyLink;
	}

	private Set<IModel<AssetDO>> getCustomerAssets(String id)
	{
		Set<IModel<AssetDO>> customerAssets = new HashSet<IModel<AssetDO>>();
		if("getJackGetPolicyLink".equalsIgnoreCase(id) || "getJackFileClaimLink".equalsIgnoreCase(id))
		{
			customerAssets = jackAssets;
		}
		else if("getRoseGetPolicyLink".equalsIgnoreCase(id) || "getRoseFileClaimLink".equalsIgnoreCase(id))
		{
			customerAssets = roseAssets;
		}
		else if("getRomeoGetPolicyLink".equalsIgnoreCase(id) || "getRomeoFileClaimLink".equalsIgnoreCase(id))
		{
			customerAssets = romeoAssets;
		}
		else
		{
			customerAssets = julietAssets;
		}

		return customerAssets;
	}

	private Component getFileClaimLink(final String id)
	{
		fileClaimLink = new AjaxLink<Void>(id)
		{
			private static final long serialVersionUID = -737382470284736850L;

			@Override
			protected void onConfigure()
			{
				super.onConfigure();

				boolean isVisible = false;
				if("getJackFileClaimLink".equalsIgnoreCase(id) && containsInsuredCar(jackAssets))
				{
					isVisible = true;
				}
				else if("getRoseFileClaimLink".equalsIgnoreCase(id) && containsInsuredCar(roseAssets))
				{
					isVisible = true;
				}
				else if("getRomeoFileClaimLink".equalsIgnoreCase(id) && containsInsuredCar(romeoAssets))
				{
					isVisible = true;
				}
				else if("getJulietFileClaimLink".equalsIgnoreCase(id) && containsInsuredCar(julietAssets))
				{
					isVisible = true;
				}

				setVisibilityAllowed(isVisible);
			}
			
			private boolean containsInsuredCar(Set<IModel<AssetDO>> assets)
			{
				for(IModel<AssetDO> asset : assets)
				{
					if(asset.getObject() instanceof CarDO && ((CarDO)asset.getObject()).isInsured())
					{
						return true;
					}
				}
				return false;
			}

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				fileClaimModalpanelContent = new ModalPanelContent(fileClaimModalWindow.getContentId(), getCustomerAssets(id), true)
				{
					private static final long serialVersionUID = -5330394497328707841L;
					@Override
					public void onUpdateModalPanelContent(PolicyDO policy, AjaxRequestTarget target) 
					{
						String customerName = EMPTY_STRING;
						if("getJackFileClaimLink".equalsIgnoreCase(id))
						{
							customerName = JACK;
						}
						else if("getRoseFileClaimLink".equalsIgnoreCase(id))
						{
							customerName = ROSE;
						}
						else if("getRomeoFileClaimLink".equalsIgnoreCase(id))
						{
							customerName = ROMEO;
						}
						else
						{
							customerName = JULIET;
						}

						try
						{
							TransactionEnum raiseClaimStatus = BlockChainRestServiceHelper.raiseClaim(customerName, policy.getInsurer(), policy.getVehicleId(), "metadata");

							CarDO claimRaisedCar = BlockChainRestServiceHelper.getVehicleDetails(policy.getVehicleId());

							if(TransactionEnum.VALID.equals(raiseClaimStatus))
							{
								if(claimRaisedCar != null && !StringHelper.isNullOrEmpty(claimRaisedCar.getDigitalId()) && !StringHelper.isNullOrEmpty(claimRaisedCar.getDigitalIdOwner()))
								{
									String digitalID = claimRaisedCar.getDigitalId();
									String digitalIDOwner = claimRaisedCar.getDigitalIdOwner();

									TransactionDO transaction = new TransactionDO();
									transaction.setTokenOwner(digitalIDOwner);
									transaction.setVehicleOwner(customerName);
									transaction.setTransactionSource(customerName);
									transaction.setId(claimRaisedCar.getLastTransactionTimestamp());
									transaction.setTransactionDestination(policy.getInsurer());
									transaction.setTransactionType("File Claim");
									transaction.setInvolvingVehicle(policy.getVehicleId());	
									transaction.setTransactionKey(claimRaisedCar.getLastTransaction());

									transactions.add(Model.of(transaction));

									ClaimDO claim = new ClaimDO();
									claim.setClaimNr("Clm_" + System.currentTimeMillis());
									claim.setPolicyNr(policy.getPolicyNr());
									claim.setVehicleId(policy.getVehicleId());
									claim.setInsurer(policy.getInsurer());

									if(USAA.equalsIgnoreCase(policy.getInsurer()))
									{
										usaaAssets.add(Model.of((AssetDO)claim));
									}
									else
									{
										geicoAssets.add(Model.of((AssetDO)claim));
									}

									for(IModel<AssetDO> asset: getCustomerAssets(id))
									{
										if(asset.getObject() instanceof CarDO && ((CarDO)asset.getObject()).isInsured())
										{
											((CarDO)asset.getObject()).setDigitalId(digitalID);
											((CarDO)asset.getObject()).setDigitalIdOwner(digitalIDOwner);
										}
									}
								}
								else
								{
									// TODO Done
									showError = true;
									target.add(contentContainer);
									target.add(errorContainer);
								}

								target.add(fileClaimModalpanelContent);
								target.add(assetTransactionContainer);
								target.add(transactionDetailsRow);
							}
							else if(TransactionEnum.FRAUDULENT.equals(raiseClaimStatus))
							{
								if(claimRaisedCar != null && !StringHelper.isNullOrEmpty(claimRaisedCar.getDigitalId()) && !StringHelper.isNullOrEmpty(claimRaisedCar.getDigitalIdOwner()))
								{
									TransactionDO transaction = new TransactionDO();
									transaction.setTokenOwner(claimRaisedCar.getDigitalIdOwner());
									transaction.setVehicleOwner(claimRaisedCar.getVehicleOwner());
									transaction.setTransactionSource(claimRaisedCar.getVehicleOwner());
									transaction.setId(System.currentTimeMillis());
									transaction.setTransactionDestination(policy.getInsurer());
									transaction.setTransactionType("Fraudulent Claim");
									transaction.setInvolvingVehicle(claimRaisedCar.getVin());	
									transaction.setTransactionStatus(TransactionEnum.FRAUDULENT);
									transaction.setTransactionKey("Fraudulent Claim");

									transactions.add(Model.of(transaction));

									target.add(fileClaimModalpanelContent);
									target.add(assetTransactionContainer);
									target.add(transactionDetailsRow);
								}
								else
								{
									// TODO Done
									showError = true;
									target.add(contentContainer);
									target.add(errorContainer);
								}
							}
							else
							{ 
								// TODO Done
								showError = true;
								target.add(contentContainer);
								target.add(errorContainer);
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
							showError = true;
							target.add(contentContainer);
							target.add(errorContainer);
						}
						if(!showError)
						{
							fileClaimModalWindow.close(target);
						}
					}
				};
				fileClaimModalWindow.setContent(fileClaimModalpanelContent);
				fileClaimModalWindow.show(target);
			}
		};
		fileClaimLink.setOutputMarkupPlaceholderTag(true);
		return fileClaimLink;
	}

	private Component getManufacturerCarsListRefresingView(final String wicketId, final String imageIconId)
	{
		RefreshingView<CarDO> manufacturerCarsListRefresingView = new RefreshingView<CarDO>(wicketId)
		{
			private static final long serialVersionUID = 338149813740014483L;

			@Override
			protected Iterator<IModel<CarDO>> getItemModels()
			{

				if("hondaCarsListRefresingView".equalsIgnoreCase(wicketId))
				{
					return hondaCars.iterator();
				}
				if("toyotaCarsListRefresingView".equalsIgnoreCase(wicketId))
				{
					return toyotaCars.iterator();
				}

				return new HashSet<IModel<CarDO>>().iterator() ;
			}

			@Override
			protected void populateItem(Item<CarDO> item)
			{
				WebMarkupContainer carIcon = new WebMarkupContainer(imageIconId);

				if(item.getModelObject().getManufacturer().equalsIgnoreCase(HONDA))
				{
					carIcon.add(getVehicleOnDragBehavior(item.getModelObject(), HONDA));
				}
				else
				{
					carIcon.add(getVehicleOnDragBehavior(item.getModelObject(), TOYOTA));
				}

				carIcon.add(new AttributeModifier("src", getCarImageURL(item.getModelObject())));

				carIcon.setOutputMarkupId(true);
				item.add(carIcon);
			}
		};
		manufacturerCarsListRefresingView.setOutputMarkupId(true);
		manufacturerCarsListRefresingView.setMarkupId(wicketId);

		return manufacturerCarsListRefresingView;
	}

	private String getCarImageURL(CarDO carDO)
	{
		String url = EMPTY_STRING;
		if(carDO.getManufacturer().equalsIgnoreCase(HONDA))
		{
			if(carDO.getModel().equalsIgnoreCase(HONDA_CITY))
			{
				url = "resources/images/Honda_Jazz_Blue.png";
			}
			else if(carDO.getModel().equalsIgnoreCase(HONDA_JAZZ))
			{
				url = "resources/images/Honda_Jazz_Brown.png";
			}
			else if(carDO.getModel().equalsIgnoreCase(HONDA_AMAZE))
			{
				url = "resources/images/Honda_Jazz_Green.png";	
			}
			else
			{
				url = "resources/images/Honda_Jazz_Pink.png";
			}
		}
		else
		{
			if(carDO.getModel().equalsIgnoreCase(TOYOTA_FORTUNER))
			{
				url = "resources/images/Toyota_Camry_Blue.png";
			}
			else if(carDO.getModel().equalsIgnoreCase(TOYOTA_INNOVA))
			{
				url = "resources/images/Toyota_Camry_Brown.png";
			}
			else if(carDO.getModel().equalsIgnoreCase(TOYOTA_COROLLA))
			{
				url = "resources/images/Toyota_Camry_Green.png";	
			}
			else
			{
				url = "resources/images/Toyota_Camry_Maroon.png";
			}
		}

		return url;
	}

	private Component getCarsListContainer(final String containerId, final String refreshingViewId, final String imageIconId)
	{
		WebMarkupContainer dealerCarsListContainer = new WebMarkupContainer(containerId);
		dealerCarsListContainer.setOutputMarkupId(true);
		dealerCarsListContainer.setMarkupId(containerId);
		dealerCarsListContainer.add(getVehicleOnDropBehavior(containerId));

		RefreshingView<CarDO> dealerCarsListRefresingView = new RefreshingView<CarDO>(refreshingViewId)
		{
			private static final long serialVersionUID = 338149813740014483L;

			@Override
			protected Iterator<IModel<CarDO>> getItemModels()
			{
				if("majorWorldCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return majorWorldDealerCars.iterator();
				}
				if("hillsideCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return hillsideDealerCars.iterator();
				}
				if("euroAutoWorksCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return euroWorksBodyShopCars.iterator();
				}
				if("AutoCareEastCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return autoCaresBodyShopCars.iterator();
				}

				return new HashSet<IModel<CarDO>>().iterator() ;
			}

			@Override
			protected void populateItem(Item<CarDO> item)
			{
				WebMarkupContainer carIcon = new WebMarkupContainer(imageIconId);

				if("AutoCareEastCarsListRefresingView".equalsIgnoreCase(refreshingViewId) || "euroAutoWorksCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					carIcon.add(new AttributeModifier("src", "resources/images/Certified.png"));
				}
				else
				{
					carIcon.add(new AttributeModifier("src", getCarImageURL(item.getModelObject())));
				}

				carIcon.add(getVehicleOnDragBehavior(item.getModelObject(), containerId));
				carIcon.setOutputMarkupId(true);
				item.add(carIcon);
			}
		};
		dealerCarsListContainer.add(dealerCarsListRefresingView);

		return dealerCarsListContainer;
	}

	private Component getCustomerAssetsRefreshingView(final String containerId, final String refreshingViewId, final String imageIconId)
	{
		WebMarkupContainer customerAssetsContainer = new WebMarkupContainer(containerId);
		customerAssetsContainer.setOutputMarkupId(true);
		customerAssetsContainer.setMarkupId(containerId);
		customerAssetsContainer.add(getVehicleOnDropBehavior(containerId));

		RefreshingView<AssetDO> customerAssetsRefreshingView = new RefreshingView<AssetDO>(refreshingViewId)
		{
			private static final long serialVersionUID = -4392702236717969484L;

			@Override
			protected Iterator<IModel<AssetDO>> getItemModels()
			{
				if("jackCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return jackAssets.iterator();
				}
				if("roseCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return roseAssets.iterator();
				}
				if("julietCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return julietAssets.iterator();
				}
				if("romeoCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return romeoAssets.iterator();
				}
				if("usaaCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return usaaAssets.iterator();
				}
				if("geicoCarsListRefresingView".equalsIgnoreCase(refreshingViewId))
				{
					return geicoAssets.iterator();
				}
				return new HashSet<IModel<AssetDO>>().iterator() ;
			}

			@Override
			protected void populateItem(Item<AssetDO> item)
			{
				WebMarkupContainer assetIcon = new WebMarkupContainer(imageIconId);
				assetIcon.setOutputMarkupId(true);
				if(item.getModelObject() instanceof CarDO)
				{
					CarDO car = (CarDO) item.getModelObject();
					if(car.isInsured())
					{
						assetIcon.add(new AttributeModifier("src", getShieldedImageURL(car)));
					}
					else
					{
						assetIcon.add(new AttributeModifier("src", getCarImageURL(car)));
					}
					assetIcon.add(getVehicleOnDragBehavior((CarDO)item.getModelObject(), containerId));
				}
				else if(item.getModelObject() instanceof PolicyDO)
				{
					assetIcon.add(new AttributeModifier("src", "resources/images/Policy.png"));
				}
				else if(CUSTOMER_REFRESHING_VIEWS.contains(refreshingViewId))
				{
					assetIcon.add(new AttributeModifier("src", "resources/images/Policy-claim-ok.png"));
				}
				else
				{
					assetIcon.add(new AttributeModifier("src", "resources/images/Policy-claim.png"));
				}
				item.add(assetIcon);
			}
		};
		customerAssetsContainer.add(customerAssetsRefreshingView);

		return customerAssetsContainer;
	}

	protected String getShieldedImageURL(CarDO carDO)
	{
		String url = EMPTY_STRING;

		if(carDO.getManufacturer().equalsIgnoreCase(HONDA))
		{
			if(carDO.getModel().equalsIgnoreCase(HONDA_CITY))
			{
				url = "resources/images/Honda_Jazz_Blue_Shielded.png";
			}
			else if(carDO.getModel().equalsIgnoreCase(HONDA_JAZZ))
			{
				url = "resources/images/Honda_Jazz_Brown_Shielded.png";
			}
			else if(carDO.getModel().equalsIgnoreCase(HONDA_AMAZE))
			{
				url = "resources/images/Honda_Jazz_Green_Shielded.png";	
			}
			else
			{
				url = "resources/images/Honda_Jazz_Pink_Shielded.png";
			}
		}
		else
		{
			if(carDO.getModel().equalsIgnoreCase(TOYOTA_FORTUNER))
			{
				url = "resources/images/Toyota_Camry_Blue_Shielded.png";
			}
			else if(carDO.getModel().equalsIgnoreCase(TOYOTA_INNOVA))
			{
				url = "resources/images/Toyota_Camry_Brown_Shielded.png";
			}
			else if(carDO.getModel().equalsIgnoreCase(TOYOTA_COROLLA))
			{
				url = "resources/images/Toyota_Camry_Green_Shielded.png";	
			}
			else
			{
				url = "resources/images/Toyota_Camry_Maroon_Shielded.png";
			}
		}

		return url;
	}

	private Behavior getVehicleOnDragBehavior(final CarDO car, final String source)
	{
		return new AjaxEventBehavior("ondragend")
		{
			private static final long serialVersionUID = -8199262244401875803L;

			@Override
			protected void onEvent(AjaxRequestTarget target)
			{
				draggingCar = car;
				boolean isTransactionValid = false;

				TransactionDO transaction = new TransactionDO();
				transaction.setTransactionType("Asset Transfer (Vehicle)");

				if(MANUFACTURERS.contains(source) && DEALER_CONTAINERS.contains(destination))
				{
					try
					{
						String transferTo = EMPTY_STRING;
						if(MAJOR_WORLD_CONTAINER.equalsIgnoreCase(destination))
						{
							transferTo = MAJOR_WORLD;
						}
						else
						{
							transferTo = HILLSIDE_AUTOMALL;
						}

						boolean isTransferSuccess = BlockChainRestServiceHelper.transferVehicle(source, transferTo, draggingCar.getVin(), "transfertodealer");

						if(isTransferSuccess)
						{
							CarDO transferredVehicle = BlockChainRestServiceHelper.getVehicleDetails(draggingCar.getVin());

							if(transferredVehicle != null)
							{
								isTransactionValid = true;

								if(MAJOR_WORLD_CONTAINER.equalsIgnoreCase(destination))
								{
									majorWorldDealerCars.add(Model.of(transferredVehicle));
								}
								else
								{
									hillsideDealerCars.add(Model.of(transferredVehicle));
								}
								transaction.setTokenOwner(transferredVehicle.getVehicleOwner());
								transaction.setVehicleOwner(transferredVehicle.getVehicleOwner());

								if(HONDA.equalsIgnoreCase(source))
								{
									hondaSoldCars.add(Model.of(transferredVehicle));
									hondaCars.remove(Model.of(transferredVehicle));
								}
								else
								{
									toyotaSoldCars.add(Model.of(transferredVehicle));
									toyotaCars.remove(Model.of(transferredVehicle));
								}

								transaction.setTransactionKey(transferredVehicle.getLastTransaction());
							}
							else
							{
								// TODO Done
								errorMessageModalWindow.show(target);
							}
						}
						else
						{
							// TODO Done
							errorMessageModalWindow.show(target);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						// TODO Done
						errorMessageModalWindow.show(target);
						
					}
				}
				else if(DEALER_CONTAINERS.contains(source) && CUSTOMER_CONTAINERS.contains(destination))
				{
					try
					{
						String transferFrom = EMPTY_STRING;
						String transferTo = EMPTY_STRING;
						if(MAJOR_WORLD_CONTAINER.equalsIgnoreCase(source))
						{
							transferFrom = MAJOR_WORLD;
						}
						else
						{
							transferFrom = HILLSIDE_AUTOMALL;
						}

						if(JACK_CONTAINER.equalsIgnoreCase(destination))
						{
							transferTo = JACK;
						}
						else if(ROSE_CONTAINER.equalsIgnoreCase(destination))
						{
							transferTo = ROSE;
						}
						else if(ROMEO_CONTAINER.equalsIgnoreCase(destination))
						{
							transferTo = ROMEO;
						}
						else
						{
							transferTo = JULIET;
						}

						boolean isTransferSuccess = BlockChainRestServiceHelper.transferVehicle(transferFrom, transferTo, draggingCar.getVin(), "transfertouser");

						if(isTransferSuccess)
						{
							CarDO transferredVehicle = BlockChainRestServiceHelper.getVehicleDetails(draggingCar.getVin());

							if(transferredVehicle != null)
							{
								isTransactionValid = true;

								if(JACK_CONTAINER.equalsIgnoreCase(destination))
								{
									jackAssets.add(Model.of((AssetDO) transferredVehicle));
								}
								else if(ROSE_CONTAINER.equalsIgnoreCase(destination))
								{
									roseAssets.add(Model.of((AssetDO) transferredVehicle));
								}
								else if(ROMEO_CONTAINER.equalsIgnoreCase(destination))
								{
									romeoAssets.add(Model.of((AssetDO) transferredVehicle));
								}
								else
								{
									julietAssets.add(Model.of((AssetDO) transferredVehicle));
								}
								transaction.setTokenOwner(transferredVehicle.getVehicleOwner());
								transaction.setVehicleOwner(transferredVehicle.getVehicleOwner());

								if(MAJOR_WORLD_CONTAINER.equalsIgnoreCase(source))
								{
									majorWorldDealerCars.remove(Model.of(draggingCar));
								}
								else
								{
									hillsideDealerCars.remove(Model.of(draggingCar));
								}
								transaction.setTransactionKey(transferredVehicle.getLastTransaction());
							}
							else
							{
								// TODO Done
								errorMessageModalWindow.show(target);
							}
						}
						else
						{
							// TODO Done
							errorMessageModalWindow.show(target);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						// TODO Done
						errorMessageModalWindow.show(target);
					}
				}
				else if(CUSTOMER_CONTAINERS.contains(source) && CUSTOMER_CONTAINERS.contains(destination))
				{
					String seller = EMPTY_STRING;
					String buyer = EMPTY_STRING;
					if(JACK_CONTAINER.equalsIgnoreCase(source))
					{
						seller = JACK;
					}
					else if(ROSE_CONTAINER.equalsIgnoreCase(source))
					{
						seller = ROSE;
					}
					else if(ROMEO_CONTAINER.equalsIgnoreCase(source))
					{
						seller = ROMEO;
					}
					else
					{
						seller = JULIET;
					}

					if(JACK_CONTAINER.equalsIgnoreCase(destination))
					{
						buyer = JACK;
					}
					else if(ROSE_CONTAINER.equalsIgnoreCase(destination))
					{
						buyer = ROSE;
					}
					else if(ROMEO_CONTAINER.equalsIgnoreCase(destination))
					{
						buyer = ROMEO;
					}
					else
					{
						buyer = JULIET;
					}

					try
					{
						TransactionEnum sellVehicleStatus = BlockChainRestServiceHelper.sellVehicle(seller, buyer, draggingCar.getVin());

						CarDO soldCar = BlockChainRestServiceHelper.getVehicleDetails(draggingCar.getVin());

						if(TransactionEnum.VALID.equals(sellVehicleStatus))
						{
							if(soldCar != null)
							{
								soldCar.setInsured(draggingCar.isInsured());
								soldCar.setInsurer(draggingCar.getInsurer());
								
								isTransactionValid = true;

								if(JACK_CONTAINER.equalsIgnoreCase(source))
								{
									jackAssets.remove(Model.of((AssetDO) soldCar));
								}
								else if(ROSE_CONTAINER.equalsIgnoreCase(source))
								{
									roseAssets.remove(Model.of((AssetDO) soldCar));
								}
								else if(ROMEO_CONTAINER.equalsIgnoreCase(source))
								{
									romeoAssets.remove(Model.of((AssetDO) soldCar));
								}
								else
								{
									julietAssets.remove(Model.of((AssetDO) soldCar));
								}

								if(JACK_CONTAINER.equalsIgnoreCase(destination))
								{
									jackAssets.add(Model.of((AssetDO) soldCar));
								}
								else if(ROSE_CONTAINER.equalsIgnoreCase(destination))
								{
									roseAssets.add(Model.of((AssetDO) soldCar));
								}
								else if(ROMEO_CONTAINER.equalsIgnoreCase(destination))
								{
									romeoAssets.add(Model.of((AssetDO) soldCar));
								}
								else
								{
									julietAssets.add(Model.of((AssetDO) soldCar));
								}

								transaction.setTokenOwner(soldCar.getVehicleOwner());
								transaction.setVehicleOwner(soldCar.getVehicleOwner());
								transaction.setTransactionType("Asset Transfer (Vehicle)");
								transaction.setTransactionKey(soldCar.getLastTransaction());
							}
							else
							{
								// TODO Done
								errorMessageModalWindow.show(target);
							}
						}
						else if(TransactionEnum.FRAUDULENT.equals(sellVehicleStatus))
						{
							if(soldCar != null)
							{
								isTransactionValid = true;

								transaction.setTokenOwner(soldCar.getVehicleOwner());
								transaction.setVehicleOwner(soldCar.getVehicleOwner());
								transaction.setTransactionType("Fraudelent Transaction");
								transaction.setTransactionKey("Fraudelent Transaction");
								transaction.setTransactionStatus(TransactionEnum.FRAUDULENT);
							}
							else
							{
								// TODO Done
								errorMessageModalWindow.show(target);
							}
						}
						else
						{
							// TODO Done
							errorMessageModalWindow.show(target);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						// TODO Done
						errorMessageModalWindow.show(target);
					}
				}
				else if(CUSTOMER_CONTAINERS.contains(source) && BODYSHOP_CONTAINERS.contains(destination) && (!euroWorksBodyShopCars.contains(Model.of(draggingCar)) 
						&& !autoCaresBodyShopCars.contains(Model.of(draggingCar))))
				{
					try
					{
						String bodyShop =EMPTY_STRING;

						if(EURO_AUTO_WORKS_CONTAINER.equalsIgnoreCase(destination))
						{
							bodyShop = EURO_AUTOWORKS;
						}
						else
						{
							bodyShop = AUTO_CARE_EAST;
						}

						boolean certifyVehicleSuccess = BlockChainRestServiceHelper.certifyVehicle(bodyShop, draggingCar.getVin());

						if(certifyVehicleSuccess)
						{
							CarDO certifiedCar = BlockChainRestServiceHelper.getVehicleDetails(draggingCar.getVin());

							if(certifiedCar != null)
							{
								isTransactionValid = true;

								transaction.setTokenOwner(certifiedCar.getDigitalIdOwner());
								transaction.setVehicleOwner(certifiedCar.getVehicleOwner());
								transaction.setTransactionType("Vehicle Servicing Certification");
								transaction.setTransactionKey(certifiedCar.getLastTransaction());

								if(EURO_AUTO_WORKS_CONTAINER.equals(destination))
								{
									euroWorksBodyShopCars.add(Model.of(draggingCar));
								}
								else
								{
									autoCaresBodyShopCars.add(Model.of(draggingCar));	
								}
							}
							else
							{
								// TODO Done
								errorMessageModalWindow.show(target);
							}
						}
						else
						{ 
							// TODO Done
							errorMessageModalWindow.show(target);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						//TODO Done
						errorMessageModalWindow.show(target);
					}
				}
				else if(BODYSHOP_CONTAINERS.contains(source) && INSURER_CONTAINERS.contains(destination))
				{
					try
					{
						String insurer = EMPTY_STRING;

						if(USAA_CONTAINER.equalsIgnoreCase(destination))
						{
							insurer = USAA;
						}
						else
						{
							insurer = GEICO;
						}

						boolean releaseTokenSuccess = BlockChainRestServiceHelper.releaseToken(insurer, draggingCar.getVehicleOwner(), draggingCar.getVin(), draggingCar.getDigitalId());

						if(releaseTokenSuccess)
						{
							CarDO tokenReleasedCar = BlockChainRestServiceHelper.getVehicleDetails(draggingCar.getVin());

							if(tokenReleasedCar != null)
							{
								isTransactionValid = true;
								if(EURO_AUTO_WORKS_CONTAINER.equalsIgnoreCase(source))
								{
									euroWorksBodyShopCars.remove(Model.of(tokenReleasedCar));
								}
								else
								{
									autoCaresBodyShopCars.remove(Model.of(tokenReleasedCar));
								}


								transaction.setTokenOwner(tokenReleasedCar.getDigitalIdOwner());
								transaction.setVehicleOwner(tokenReleasedCar.getVehicleOwner());
								transaction.setTransactionType("Digital Token released");
								transaction.setTransactionKey(tokenReleasedCar.getLastTransaction());

								if(USAA_CONTAINER.equalsIgnoreCase(destination))
								{
									AssetDO asset = null;
									for(IModel<AssetDO> assets : usaaAssets)
									{
										asset = assets.getObject();
										if(asset instanceof ClaimDO && ((ClaimDO)asset).getVehicleId().equalsIgnoreCase(draggingCar.getVin()))
										{
											((ClaimDO)asset).setClaimStatus(ClaimStatusEnum.CLOSED);
											if(draggingCar.getVehicleOwner().equalsIgnoreCase(JACK))
											{
												jackAssets.add(Model.of(asset));
											}
											else if(draggingCar.getVehicleOwner().equalsIgnoreCase(ROSE))
											{
												roseAssets.add(Model.of(asset));
											}
											else if(draggingCar.getVehicleOwner().equalsIgnoreCase(ROMEO))
											{
												romeoAssets.add(Model.of(asset));
											}
											else
											{
												julietAssets.add(Model.of(asset));
											}
											usaaAssets.remove(Model.of(asset));
										}
									}
								}
								else
								{
									AssetDO asset = null;
									for(IModel<AssetDO> assets : geicoAssets)
									{
										asset = assets.getObject();
										if(asset instanceof ClaimDO && ((ClaimDO)asset).getVehicleId().equalsIgnoreCase(draggingCar.getVin()))
										{
											((ClaimDO)asset).setClaimStatus(ClaimStatusEnum.CLOSED);
											if(draggingCar.getVehicleOwner().equalsIgnoreCase(JACK))
											{
												jackAssets.add(Model.of(asset));
											}
											else if(draggingCar.getVehicleOwner().equalsIgnoreCase(ROSE))
											{
												roseAssets.add(Model.of(asset));
											}
											else if(draggingCar.getVehicleOwner().equalsIgnoreCase(ROMEO))
											{
												romeoAssets.add(Model.of(asset));
											}
											else
											{
												julietAssets.add(Model.of(asset));
											}
											geicoAssets.remove(Model.of(asset));
										}
									}
								}
							}
							else
							{
								// TODO Done
								errorMessageModalWindow.show(target);
							}
						}
						else
						{
							// TODO Done
							errorMessageModalWindow.show(target);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						//TODO Done
						errorMessageModalWindow.show(target);
					}
				}

				if(isTransactionValid)
				{
					transaction.setId(System.currentTimeMillis());
					transaction.setTransactionSource(getDisplayName(source));
					transaction.setTransactionDestination(getDisplayName(destination));
					transaction.setInvolvingVehicle(draggingCar.getVin());

					transactions.add(Model.of(transaction));

					target.add(assetTransactionContainer);
					target.add(transactionDetailsRow);
				}
				else
				{
					//TODO added here additionally
					errorMessageModalWindow.show(target);
				}
			}
		};
	}

	private String getDisplayName(String input) 
	{
		if(MAJOR_WORLD_CONTAINER.equalsIgnoreCase(input))
		{
			return MAJOR_WORLD;
		}
		else if(HILL_SIDE_CONTAINER.equalsIgnoreCase(input))
		{
			return HILLSIDE_AUTOMALL;
		}
		else if(USAA_CONTAINER.equalsIgnoreCase(input))
		{
			return USAA;
		}
		else if(GEIKO_CONTAINER.equalsIgnoreCase(input))
		{
			return GEICO;
		}
		else if(EURO_AUTO_WORKS_CONTAINER.equalsIgnoreCase(input))
		{
			return EURO_AUTOWORKS;
		}
		else if(AUTO_CARE_EAST_CONTAINER.equalsIgnoreCase(input))
		{
			return AUTO_CARE_EAST;
		}
		else if(JACK_CONTAINER.equalsIgnoreCase(input) || JACK_REFRESHING_VIEW.equals(input))
		{
			return JACK;
		}
		else if(ROSE_CONTAINER.equalsIgnoreCase(input) || ROSE_REFRESHING_VIEW.equals(input))
		{
			return ROSE;
		}
		else if(ROMEO_CONTAINER.equalsIgnoreCase(input) || ROMEO_REFRESHING_VIEW.equals(input))
		{
			return ROMEO;
		}
		else if(JULIET_CONTAINER.equalsIgnoreCase(input) || JULIET_REFRESHING_VIEW.equals(input))
		{
			return JULIET;
		}

		return input;
	}

	private Behavior getVehicleOnDropBehavior(final String destination)
	{
		return new AjaxEventBehavior("ondrop")
		{
			private static final long serialVersionUID = -8199262244401875803L;

			@Override
			protected void onEvent(AjaxRequestTarget target)
			{
				BlockchainDemoMainPage.this.destination = destination;
			}
		};
	}

	private WebMarkupContainer createtransactionDetailsRow()
	{
		transactionDetailsRow = new WebMarkupContainer("transactionDetailsRow");

		RefreshingView<TransactionDO> transactionDetailsRefresingView = new RefreshingView<TransactionDO>("transactionDetailsRefresingView")
		{
			private static final long serialVersionUID = 338149813740014483L;

			@Override
			protected Iterator<IModel<TransactionDO>> getItemModels()
			{
				Collections.reverse(transactions);
				return transactions.listIterator();
			}

			@Override
			protected void populateItem(Item<TransactionDO> item)
			{
				WebMarkupContainer transactionDetailsContainer = new WebMarkupContainer("transactionDetailsContainer");
				if(TransactionEnum.FRAUDULENT.equals(item.getModelObject().getTransactionStatus()))
				{
					transactionDetailsContainer.add(new AttributeModifier("class", "error-transaction"));
				}
				if(TransactionEnum.INVALID.equals(item.getModelObject().getTransactionStatus()))
				{
					transactionDetailsContainer.add(new AttributeModifier("class", "error-transaction"));
				}
				transactionDetailsContainer.add(new Label("block", Model.of(item.getModelObject().getTransactionType())));
				transactionDetailsContainer.add(new Label("transactionTimeStamp", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(item.getModelObject().getId()))));
				transactionDetailsContainer.add(new Label("digitalId", Model.of(item.getModelObject().getInvolvingVehicle())));
				transactionDetailsContainer.add(new Label("transactionFrom", Model.of(item.getModelObject().getTransactionSource())));
				transactionDetailsContainer.add(new Label("owner", Model.of(item.getModelObject().getTokenOwner())));
				transactionDetailsContainer.add(new Label("transactionTo", Model.of(item.getModelObject().getTransactionDestination())));
				transactionDetailsContainer.add(new Label("transactionId", Model.of(item.getModelObject().getTransactionKey())));
				item.add(transactionDetailsContainer);
			}

			@Override
			protected void onAfterRender()
			{
				super.onAfterRender();
				Collections.reverse(transactions);
			}
		};

		transactionDetailsRow.add(transactionDetailsRefresingView);
		transactionDetailsRow.setOutputMarkupPlaceholderTag(true);

		return transactionDetailsRow;
	}

	private ModalWindow createGetPolicyModalWindow()
	{
		getPolicyModalWindow = new ModalWindow("getPolicyModalWindow")
		{
			private static final long serialVersionUID = 9032816101409549762L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.getContent().setOutputMarkupPlaceholderTag(true);
			}
		};

		getPolicyModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			private static final long serialVersionUID = -416489369844156238L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) 
			{
				return true;
			}
		});

		getPolicyModalWindow.setTitle("Get Policy");
		getPolicyModalWindow.setCssClassName(ModalWindow.CSS_CLASS_GRAY);

		getPolicyModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			private static final long serialVersionUID = -416489369844156238L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target)
			{
				target.add(getPolicyModalWindowContent);
				return true;
			}
		});

		getPolicyModalWindow.setInitialHeight(150);
		getPolicyModalWindow.setInitialWidth(300);
		getPolicyModalWindow.setOutputMarkupPlaceholderTag(true);

		return getPolicyModalWindow;
	}

	private ModalWindow createFileClaimModalWindow()
	{
		fileClaimModalWindow = new ModalWindow("fileClaimModalWindow");

		fileClaimModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			private static final long serialVersionUID = -416489369844156238L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});

		fileClaimModalWindow.setTitle("File Claim");
		fileClaimModalWindow.setCssClassName(ModalWindow.CSS_CLASS_GRAY);

		fileClaimModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			private static final long serialVersionUID = -416489369844156238L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target)
			{
				target.add(fileClaimModalpanelContent);
				return true;
			}
		});

		fileClaimModalWindow.setInitialHeight(150);
		fileClaimModalWindow.setInitialWidth(300);
		fileClaimModalWindow.setOutputMarkupPlaceholderTag(true);

		return fileClaimModalWindow;
	}

	private ModalWindow createRecallVehicleModalWindow()
	{
		getRecallVehicleModalWindow = new ModalWindow("getRecallVehicleModalWindow");

		getRecallVehicleModalWindow.setTitle("Recall Vehicles");
		getRecallVehicleModalWindow.setCssClassName(ModalWindow.CSS_CLASS_GRAY);

		getRecallVehicleModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			private static final long serialVersionUID = -416489369844156238L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				target.add(getRecallVehicleModalWindowContent);
				return true;
			}
		});

		getRecallVehicleModalWindow.setInitialHeight(150);
		getRecallVehicleModalWindow.setInitialWidth(300);
		getRecallVehicleModalWindow.setOutputMarkupPlaceholderTag(true);

		return getRecallVehicleModalWindow;
	}
	
	private ModalWindow createErrorMessageModalWindow()
	{
		errorMessageModalWindow = new ModalWindow("errorMessageModalWindow");

		errorMessageModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			private static final long serialVersionUID = -416489369844156238L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) 
			{
				return true;
			}
		});

		errorMessageModalWindow.setTitle("Error");
		errorMessageModalWindow.setCssClassName(ModalWindow.CSS_CLASS_GRAY);

		errorMessageModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			private static final long serialVersionUID = -416489369844156238L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				target.add(errorDetailsModalWindowContent);
				return true;
			}
		});

		errorMessageModalWindow.setInitialHeight(100);
		errorMessageModalWindow.setInitialWidth(300);
		errorMessageModalWindow.setOutputMarkupPlaceholderTag(true);

		return errorMessageModalWindow;
	}
	
	private void createErrorDetailsModelWindowContent()
	{
		errorDetailsModalWindowContent = new Fragment(errorMessageModalWindow.getContentId(), "errorDetailsModalWindowContentFragment", assetTransactionContainer);

		errorDetailsModalWindowContent.add(new Label("errorMessage", new Model<String>()
		{
			private static final long serialVersionUID = 3680341697131694374L;

			@Override
			public String getObject() {
				return "Previous Transaction could not be completed due to some technical difficulties. Please try again";
			}
		}));

		errorMessageModalWindow.setContent(errorDetailsModalWindowContent);
	}
}