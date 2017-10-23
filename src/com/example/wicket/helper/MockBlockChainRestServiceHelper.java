package com.example.wicket.helper;

import java.util.ArrayList;
import java.util.List;

import com.example.wicket.dataobject.CarDO;
import com.example.wicket.dataobject.TransactionDO.TransactionEnum;

import static com.example.wicket.helper.Constants.*;

public class MockBlockChainRestServiceHelper
{
	public static boolean createVehicle(String brand, String model) throws Exception
	{
		return true;
	}

	public static CarDO getAddedVehicleDetails(String brand, String model)
	{
		CarDO car = null;
		
		try
		{
			if(HONDA.equalsIgnoreCase(brand))
			{
				car = getVehicleDetails("12345");
	
				car.setVehicleOwner(HONDA);
				car.setDigitalIdOwner(HONDA);
			}
			else
			{
				car = getVehicleDetails("54321");
				
				car.setVehicleOwner(TOYOTA);
				car.setDigitalIdOwner(TOYOTA);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			car = null;
		}

		return car;
	}
	
	public static CarDO getVehicleDetails(String vinNumber) throws Exception
	{
		CarDO car = new CarDO();
		
		if("54321".equalsIgnoreCase(vinNumber))
		{
			car.setVin("54321");
			car.setManufacturer(TOYOTA);
			car.setModel(TOYOTA_CAMRY);
			car.setVehicleOwner(ROSE);
			car.setLastTransactionTimestamp(System.currentTimeMillis());
			car.setLastTransaction("Test Transaction");
			car.setDigitalId("65432");
			car.setDigitalIdOwner(ROSE);
		}
		else
		{
			car.setVin("12345");
			car.setManufacturer(HONDA);
			car.setModel(HONDA_JAZZ);
			car.setVehicleOwner(JACK);
			car.setLastTransactionTimestamp(System.currentTimeMillis());
			car.setLastTransaction("Initial Transaction");
			car.setDigitalId("23456");
			car.setDigitalIdOwner(JACK);
		}
		
		return car;
	}

	public static boolean buyInsurance(String customerName, String insurer, String vehicleId)
	{
		return true;
	}

	public static TransactionEnum raiseClaim(String customerName, String insurer, String vehicleId, String metadata)
	{
		TransactionEnum raiseClaimStatus = TransactionEnum.VALID;
		
		return raiseClaimStatus;
	}

	public static List<CarDO> getAllVehicleDetails() throws Exception
	{
		List<CarDO> allCars = null;
		
		allCars = new ArrayList<CarDO>();
		
		CarDO car = new CarDO();
		car.setVin("12345");
		car.setManufacturer(HONDA);
		car.setModel(HONDA_JAZZ);
		car.setVehicleOwner(JACK);
		car.setLastTransactionTimestamp(System.currentTimeMillis());
		car.setLastTransaction("Initial Transaction");
		car.setDigitalId("23456");
		car.setDigitalIdOwner(JACK);
		
		allCars.add(car);
		
		CarDO car1 = new CarDO();
		car1.setVin("54321");
		car1.setManufacturer(TOYOTA);
		car1.setModel(TOYOTA_CAMRY);
		car1.setVehicleOwner(ROSE);
		car1.setLastTransactionTimestamp(System.currentTimeMillis());
		car1.setLastTransaction("Test Transaction");
		car1.setDigitalId("65432");
		car1.setDigitalIdOwner(ROSE);
		
		allCars.add(car1);
		
		return allCars;
	}
	
	public static List<CarDO> getRecallVehicleDetails(String manufacturer, String selectedModel) throws Exception
	{
		List<CarDO> recallVehicle = new ArrayList<CarDO>();
		if(HONDA.equalsIgnoreCase(manufacturer))
		{
			CarDO car = new CarDO();
			car.setVin("12345");
			car.setManufacturer(HONDA);
			car.setModel(HONDA_JAZZ);
			car.setVehicleOwner(JACK);
			car.setLastTransactionTimestamp(System.currentTimeMillis());
			car.setLastTransaction("Initial Transaction");
			car.setDigitalId("23456");
			car.setDigitalIdOwner(JACK);
			recallVehicle.add(car);
		}
		else
		{
			CarDO car = new CarDO();
			car.setVin("54321");
			car.setManufacturer(TOYOTA);
			car.setModel(TOYOTA_CAMRY);
			car.setVehicleOwner(ROSE);
			car.setLastTransactionTimestamp(System.currentTimeMillis());
			car.setLastTransaction("Test Transaction");
			car.setDigitalId("65432");
			car.setDigitalIdOwner(ROSE);
			recallVehicle.add(car);
		}
		
		return recallVehicle;
	}

	public static boolean transferVehicle(String transferFrom, String transferTo, String vehicleId, String transferkey)
	{
		return true;
	}

	public static TransactionEnum sellVehicle(String seller, String buyer, String vehicleId)
	{
		TransactionEnum raiseClaimStatus = TransactionEnum.VALID;
		
		return raiseClaimStatus;
	}

	public static boolean certifyVehicle(String bodyShop, String vehicleId)
	{
		boolean certificationSuccess = true;

		return certificationSuccess;
	}

	public static boolean releaseToken(String insurer, String vehicleOwner, String vehicleId, String digitalId)
	{
		boolean releaseTokenSuccess = true;
		
		return releaseTokenSuccess;
	}

	public static boolean recallVehicles(String manufacturer, String selectedModel) throws Exception
	{
		boolean vehicleRecallSuccess = true;

		return vehicleRecallSuccess;
	}
}