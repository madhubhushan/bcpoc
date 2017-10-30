package com.example.wicket.helper;

import static com.example.wicket.helper.Constants.HILLSIDE_AUTOMALL;
import static com.example.wicket.helper.Constants.HONDA;
import static com.example.wicket.helper.Constants.JACK;
import static com.example.wicket.helper.Constants.ROSE;
import static com.example.wicket.helper.Constants.TOYOTA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.example.wicket.application.BlockchainDemoSession;
import com.example.wicket.dataobject.CarDO;
import com.example.wicket.dataobject.ClaimDO;
import com.example.wicket.dataobject.ClaimDO.ClaimStatusEnum;
import com.example.wicket.dataobject.TransactionDO.TransactionEnum;

public class MockBlockChainRestServiceHelper
{
	public static CarDO getAddedVehicleDetails(String brand, String model)
	{
		CarDO car = new CarDO();
		
		car.setVin("vin"+getRandomNumber());
		car.setLastTransactionTimestamp(System.currentTimeMillis());
		car.setDigitalId("did"+getRandomNumber());
		
		if(HONDA.equalsIgnoreCase(brand))
		{
			car.setDigitalIdOwner(HONDA);
			car.setManufacturer(HONDA);
			car.setModel(model);
			car.setVehicleOwner(HONDA);
			car.setLastTransaction("Vehicle created : "+HONDA+" "+model);
		}
		else
		{
			car.setDigitalIdOwner(TOYOTA);
			car.setManufacturer(TOYOTA);
			car.setModel(model);
			car.setVehicleOwner(TOYOTA);
			car.setLastTransaction("Vehicle created : "+TOYOTA+" "+model);
		}

		return car;
	}
	
	public static CarDO transferVehicle(String transferFrom, String transferTo, String vehicleId, String transferkey, BlockchainDemoSession blockchainDemoSession)
	{
		boolean isHondaCar = false;
		
		CarDO car = null;
		
		Map<String, CarDO> hondaCars = blockchainDemoSession.getHondaCars();
		Map<String, CarDO> toyotaCars = blockchainDemoSession.getToyotaCars();
		
		if(hondaCars.containsKey(vehicleId))
		{
			isHondaCar = true;
			car = hondaCars.get(vehicleId);
		}
		else
		{
			car = toyotaCars.get(vehicleId);
		}
		
		car.setLastTransactionTimestamp(System.currentTimeMillis());
		car.setLastTransaction("Asset transfer from "+transferFrom+" to "+transferTo);
		
		
		if("transfertodealer".equalsIgnoreCase(transferkey))
		{
			car.setDigitalIdOwner(HILLSIDE_AUTOMALL);
			car.setVehicleOwner(HILLSIDE_AUTOMALL);
		}
		else if("transfertouser".equalsIgnoreCase(transferkey))
		{
			if(JACK.equalsIgnoreCase(transferTo))
			{
				car.setDigitalIdOwner(JACK);
				car.setVehicleOwner(JACK);
			}
			else
			{
				car.setDigitalIdOwner(ROSE);
				car.setVehicleOwner(ROSE);
			}
		}
		
		if(isHondaCar)
		{
			hondaCars.put(vehicleId, car);
		}
		else
		{
			toyotaCars.put(vehicleId, car);
		}
		
		blockchainDemoSession.setHondaCars(hondaCars);
		blockchainDemoSession.setToyotaCars(toyotaCars);
		
		return car;
	}
	
	public static CarDO buyInsurance(String customerName, String insurer, String vehicleId, BlockchainDemoSession blockchainDemoSession)
	{
		boolean isHondaCar = false;
		
		CarDO car = null;
		
		Map<String, CarDO> hondaCars = blockchainDemoSession.getHondaCars();
		Map<String, CarDO> toyotaCars = blockchainDemoSession.getToyotaCars();
		
		if(hondaCars.containsKey(vehicleId))
		{
			isHondaCar = true;
			car = hondaCars.get(vehicleId);
		}
		else
		{
			car = toyotaCars.get(vehicleId);
		}
		
		car.setLastTransactionTimestamp(System.currentTimeMillis());
		car.setLastTransaction("Insurance policy issued by "+insurer+" for vehicle "+vehicleId);
		
		if(isHondaCar)
		{
			hondaCars.put(vehicleId, car);
		}
		else
		{
			toyotaCars.put(vehicleId, car);
		}
		
		blockchainDemoSession.setHondaCars(hondaCars);
		blockchainDemoSession.setToyotaCars(toyotaCars);
		
		return car;
	}
	
	public static TransactionEnum raiseClaim(String customerName, String insurer, String vehicleId, String metadata, BlockchainDemoSession blockchainDemoSession)
	{
		if(blockchainDemoSession.getClaims().containsKey(vehicleId))
		{
			ClaimDO claim = blockchainDemoSession.getClaims().get(vehicleId);
			
			if(ClaimStatusEnum.OPEN == claim.getClaimStatus()
					&& ! claim.getInsurer().equalsIgnoreCase(insurer))
			{
				return TransactionEnum.FRAUDULENT;
			}
			else if(ClaimStatusEnum.OPEN == claim.getClaimStatus()
					&& claim.getInsurer().equalsIgnoreCase(insurer))
			{
				return TransactionEnum.DUPLICATE;
			}
			else
			{
				boolean isHondaCar = false;
				CarDO car = new CarDO();
				
				Map<String, CarDO> hondaCars = blockchainDemoSession.getHondaCars();
				Map<String, CarDO> toyotaCars = blockchainDemoSession.getToyotaCars();
				
				if(hondaCars.containsKey(vehicleId))
				{
					isHondaCar = true;
					car = hondaCars.get(vehicleId);
				}
				else
				{
					car = toyotaCars.get(vehicleId);
				}
				
				car.setLastTransactionTimestamp(System.currentTimeMillis());
				car.setLastTransaction("Claim Raised by "+customerName+" to "+insurer+" for vehicle "+vehicleId);
				car.setDigitalIdOwner(insurer);
				
				if(isHondaCar)
				{
					hondaCars.put(vehicleId, car);
				}
				else
				{
					toyotaCars.put(vehicleId, car);
				}
				
				blockchainDemoSession.setHondaCars(hondaCars);
				blockchainDemoSession.setToyotaCars(toyotaCars);
				
				return TransactionEnum.VALID;
			}
		}
		else
		{
			boolean isHondaCar = false;
			CarDO car = new CarDO();
			
			Map<String, CarDO> hondaCars = blockchainDemoSession.getHondaCars();
			Map<String, CarDO> toyotaCars = blockchainDemoSession.getToyotaCars();
			
			if(hondaCars.containsKey(vehicleId))
			{
				isHondaCar = true;
				car = hondaCars.get(vehicleId);
			}
			else
			{
				car = toyotaCars.get(vehicleId);
			}
			
			car.setLastTransactionTimestamp(System.currentTimeMillis());
			car.setLastTransaction("Claim Raised by "+customerName+" to "+insurer+" for vehicle "+vehicleId);
			car.setDigitalIdOwner(insurer);
			
			if(isHondaCar)
			{
				hondaCars.put(vehicleId, car);
			}
			else
			{
				toyotaCars.put(vehicleId, car);
			}
			
			blockchainDemoSession.setHondaCars(hondaCars);
			blockchainDemoSession.setToyotaCars(toyotaCars);
			
			return TransactionEnum.VALID;
		}
	}

	public static TransactionEnum sellVehicle(String seller, String buyer, String vehicleId, BlockchainDemoSession blockchainDemoSession)
	{
		TransactionEnum raiseClaimStatus = TransactionEnum.VALID;
		
		if(blockchainDemoSession.getClaims().containsKey(vehicleId))
		{
			ClaimDO claim = blockchainDemoSession.getClaims().get(vehicleId);
			
			if(ClaimStatusEnum.OPEN == claim.getClaimStatus())
			{
				raiseClaimStatus = TransactionEnum.FRAUDULENT;
			}
		}
		
		if(TransactionEnum.VALID == raiseClaimStatus)
		{
			boolean isHondaCar = false;
			CarDO car = new CarDO();
			
			Map<String, CarDO> hondaCars = blockchainDemoSession.getHondaCars();
			Map<String, CarDO> toyotaCars = blockchainDemoSession.getToyotaCars();
			
			if(hondaCars.containsKey(vehicleId))
			{
				isHondaCar = true;
				car = hondaCars.get(vehicleId);
			}
			else
			{
				car = toyotaCars.get(vehicleId);
			}

			car.setDigitalIdOwner(buyer);
			car.setVehicleOwner(buyer);
			car.setLastTransactionTimestamp(System.currentTimeMillis());
			car.setLastTransaction("Vehicle "+vehicleId+" sold by "+seller+" to "+buyer);
			
			if(isHondaCar)
			{
				hondaCars.put(vehicleId, car);
			}
			else
			{
				toyotaCars.put(vehicleId, car);
			}
			
			blockchainDemoSession.setHondaCars(hondaCars);
			blockchainDemoSession.setToyotaCars(toyotaCars);
		}
		
		return raiseClaimStatus;
	}
	
	public static CarDO getVehicleDetails(String vinNumber, BlockchainDemoSession blockchainDemoSession) throws Exception
	{
		CarDO car = new CarDO();
		
		Map<String, CarDO> hondaCars = blockchainDemoSession.getHondaCars();
		Map<String, CarDO> toyotaCars = blockchainDemoSession.getToyotaCars();
		
		if(hondaCars.containsKey(vinNumber))
		{
			car = hondaCars.get(vinNumber);
		}
		else
		{
			car = toyotaCars.get(vinNumber);
		}
		
		return car;
	}
	
	public static boolean certifyVehicle(String bodyShop, String vehicleId, BlockchainDemoSession blockchainDemoSession)
	{
		boolean certificationSuccess = false;
		
		if(blockchainDemoSession.getClaims().containsKey(vehicleId))
		{
			ClaimDO claim = blockchainDemoSession.getClaims().get(vehicleId);
			
			if(ClaimStatusEnum.OPEN == claim.getClaimStatus())
			{
				certificationSuccess = true;
			}
		}
		else
		{
			certificationSuccess = false;
		}

		if(certificationSuccess)
		{
			boolean isHondaCar = false;
			CarDO car = new CarDO();
			
			Map<String, CarDO> hondaCars = blockchainDemoSession.getHondaCars();
			Map<String, CarDO> toyotaCars = blockchainDemoSession.getToyotaCars();
			
			if(hondaCars.containsKey(vehicleId))
			{
				isHondaCar = true;
				car = hondaCars.get(vehicleId);
			}
			else
			{
				car = toyotaCars.get(vehicleId);
			}
			
			car.setLastTransactionTimestamp(System.currentTimeMillis());
			car.setLastTransaction("Service for vehicle "+vehicleId+" certified by "+bodyShop);
			
			if(isHondaCar)
			{
				hondaCars.put(vehicleId, car);
			}
			else
			{
				toyotaCars.put(vehicleId, car);
			}
			
			blockchainDemoSession.setHondaCars(hondaCars);
			blockchainDemoSession.setToyotaCars(toyotaCars);
		}
		
		return certificationSuccess;
	}
	
	public static boolean releaseToken(String insurer, String vehicleOwner, String vehicleId, String digitalId, BlockchainDemoSession blockchainDemoSession)
	{
		boolean releaseTokenSuccess = true;
		
		if(blockchainDemoSession.getClaims().containsKey(vehicleId))
		{
			ClaimDO claim = blockchainDemoSession.getClaims().get(vehicleId);
			
			if(ClaimStatusEnum.OPEN == claim.getClaimStatus()
					&& claim.getInsurer().equalsIgnoreCase(insurer))
			{
				releaseTokenSuccess = true;
			}
			else
			{
				releaseTokenSuccess = false;
			}
		}
		else
		{
			releaseTokenSuccess = false;
		}
		
		if(releaseTokenSuccess)
		{
			boolean isHondaCar = false;
			CarDO car = new CarDO();
			
			Map<String, CarDO> hondaCars = blockchainDemoSession.getHondaCars();
			Map<String, CarDO> toyotaCars = blockchainDemoSession.getToyotaCars();
			
			if(hondaCars.containsKey(vehicleId))
			{
				isHondaCar = true;
				car = hondaCars.get(vehicleId);
			}
			else
			{
				car = toyotaCars.get(vehicleId);
			}
			
			car.setDigitalIdOwner(car.getVehicleOwner());
			car.setLastTransactionTimestamp(System.currentTimeMillis());
			car.setLastTransaction("Digital Token released by "+insurer);
			
			if(isHondaCar)
			{
				hondaCars.put(vehicleId, car);
			}
			else
			{
				toyotaCars.put(vehicleId, car);
			}
			
			blockchainDemoSession.setHondaCars(hondaCars);
			blockchainDemoSession.setToyotaCars(toyotaCars);
		}
		
		return releaseTokenSuccess;
	}
	
	private static String getRandomNumber()
	{
	    int numbers = 100000 + (int)(new Random().nextFloat() * 899900);
	    return String.valueOf(numbers);
	}

	public static List<CarDO> getRecallVehicleDetails(String manufacturer, String selectedModel, BlockchainDemoSession blockchainDemoSession) 
	{
		List<CarDO> cars = new ArrayList<>();
		
		Map<String, CarDO> carsMap = null;
		
		if(HONDA.equalsIgnoreCase(manufacturer))
		{
			carsMap = blockchainDemoSession.getHondaCars();
		}
		else
		{
			carsMap = blockchainDemoSession.getToyotaCars();
		}
		
		Set<Entry<String, CarDO>> entrySet = carsMap.entrySet();
	    Iterator<Entry<String, CarDO>> iterator = entrySet.iterator();
		 
	    while(iterator.hasNext())
	    {
	    	Map.Entry<String, CarDO> mapEntry = (Map.Entry<String, CarDO>)iterator.next();
	    	CarDO car = mapEntry.getValue();
	    	if(car.getModel().equalsIgnoreCase(selectedModel))
	    	{
	    		cars.add(car);
	    	}
	    }
		
		return cars;
	}
}