package com.example.wicket.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.wicket.dataobject.CarDO;
import com.example.wicket.dataobject.TransactionDO.TransactionEnum;
import com.example.wicket.rest.dataobject.RestServiceDO;
import com.example.wicket.rest.dataobject.RestVehicleAssetDO;
import com.google.gson.Gson;

public class BlockChainRestServiceHelper
{
	private static final String CHAINCODE_POST_URL = "http://digitalhome.tcsinsurancelab.com:7050/chaincode";
	private static final String TRANSACTIONS_GET_URL = "http://digitalhome.tcsinsurancelab.com:7050/transactions/";
	
	private static Gson gson = new Gson();

	public static boolean createVehicle(String brand, String model) throws Exception
	{
		boolean createVehicleSuccess = true;
		
		try
		{
			String vehiceCreationResponse = sendPOST(BlockChainRestServiceHelper.buildURLWithParams("createvehicle", "invoke", new String[]{brand, model}));
			
			RestServiceDO vehileCreationRestServiceDO = gson.fromJson(vehiceCreationResponse, RestServiceDO.class);
			
			int responseCode = sendGET(vehileCreationRestServiceDO.getResult().getMessage());
			
			if(responseCode != 200)
			{
				createVehicleSuccess = false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			createVehicleSuccess = false;
		}
		
		return createVehicleSuccess;
	}

	public static CarDO getAddedVehicleDetails()
	{
		CarDO car = null;
		
		try
		{
			String allVehicles = sendPOST(BlockChainRestServiceHelper.buildURLWithParams("read", "query", new String[]{"_assetIndex"}));
			
			RestServiceDO getVehiclesRestServiceDO = gson.fromJson(allVehicles, RestServiceDO.class);
			
			List<String> vinNumbers = new ArrayList<String>();
			
			Pattern pattern = Pattern.compile("\"([^\"]*)\"");
			Matcher matcher = pattern.matcher(getVehiclesRestServiceDO.getResult().getMessage());
			while(matcher.find()) 
			{
				vinNumbers.add(matcher.group(1));
			}
			
			car = getVehicleDetails(vinNumbers.get(vinNumbers.size()-1));
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
		CarDO car = null;
		RestVehicleAssetDO vehicleAsset = new RestVehicleAssetDO();
		
		String vehicleDetailsJson = sendPOST(buildURLWithParams("read", "query", new String[]{vinNumber}));
		
		RestServiceDO returnedDO = gson.fromJson(vehicleDetailsJson, RestServiceDO.class);
		
		if(returnedDO != null && returnedDO.getResult() != null && "OK".equalsIgnoreCase(returnedDO.getResult().getStatus()))
		{
			vehicleAsset = gson.fromJson(returnedDO.getResult().getMessage(), RestVehicleAssetDO.class);
		}
		
		car = new CarDO();
		car.setVin(vehicleAsset.getVehicleid());
		car.setManufacturer(vehicleAsset.getManufacturer());
		car.setModel(vehicleAsset.getModel());
		car.setVehicleOwner(vehicleAsset.getOwnername());
		car.setLastTransactionTimestamp(System.currentTimeMillis());
		car.setLastTransaction(vehicleAsset.getTransactiontype());
		car.setDigitalId(vehicleAsset.getDigitalid());
		car.setDigitalIdOwner(vehicleAsset.getDigitalidowner());
		
		return car;
	}

	public static boolean buyInsurance(String customerName, String insurer, String vehicleId)
	{
		boolean buyInsuranceSuccess = true;

		try
		{
			String buyInsurance = sendPOST(BlockChainRestServiceHelper.buildURLWithParams("buyinsurance", "invoke", new String[]{customerName, insurer, vehicleId}));

			RestServiceDO buyInsuranceRestServiceDO = gson.fromJson(buyInsurance, RestServiceDO.class);
			
			int responseCode = sendGET(buyInsuranceRestServiceDO.getResult().getMessage());
			
			if(responseCode != 200)
			{
				buyInsuranceSuccess = false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			buyInsuranceSuccess = false;
		}
		
		return buyInsuranceSuccess;
	}

	public static TransactionEnum raiseClaim(String customerName, String insurer, String vehicleId, String metadata)
	{
		TransactionEnum raiseClaimStatus = TransactionEnum.VALID;

		try
		{
			String raiseClaim = sendPOST(BlockChainRestServiceHelper.buildURLWithParams("raiseclaim", "invoke", new String[]{customerName, insurer, vehicleId, metadata}));
			
			RestServiceDO raiseClaimRestServiceDO = gson.fromJson(raiseClaim, RestServiceDO.class);
			
			int responseCode = sendGET(raiseClaimRestServiceDO.getResult().getMessage());
			
			if(responseCode != 200)
			{
				raiseClaimStatus = TransactionEnum.FRAUDULENT;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			raiseClaimStatus = TransactionEnum.INVALID;
		}
		return raiseClaimStatus;
	}

	public static List<CarDO> getAllVehicleDetails() throws Exception
	{
		List<CarDO> allCars = null;
		RestVehicleAssetDO[] carJsonList = null;
		String allCarDetailsJson = sendPOST(buildURLWithParams("getallvehicledetails", "query", new String[]{""}));
		
		RestServiceDO allCarDetailsRestServiceDO = gson.fromJson(allCarDetailsJson, RestServiceDO.class);
		
		carJsonList = gson.fromJson(allCarDetailsRestServiceDO.getResult().getMessage(), RestVehicleAssetDO[].class);
		
		if(carJsonList != null && carJsonList.length > 0)
		{
			allCars = new ArrayList<CarDO>();
			
			for(RestVehicleAssetDO carJson : carJsonList)
			{
				CarDO car = new CarDO();
				car.setVin(carJson.getVehicleid());
				car.setManufacturer(carJson.getManufacturer());
				car.setModel(carJson.getModel());
				car.setVehicleOwner(carJson.getOwnername());
				car.setLastTransactionTimestamp(carJson.getTimestamp());
				car.setLastTransaction(carJson.getTransactiontype());
				car.setDigitalId(carJson.getDigitalid());
				car.setDigitalIdOwner(carJson.getDigitalidowner());
				allCars.add(car);
			}
		}
		
		return allCars;
	}
	
	public static List<CarDO> getRecallVehicleDetails(String manufacturer, String selectedModel) throws Exception
	{
		List<CarDO> recallVehicle = null;
		RestVehicleAssetDO[] carJsonList = null;
		String recallVehicleDetailsJson = sendPOST(buildURLWithParams("getrecallvehicledetails", "query", new String[]{manufacturer, selectedModel}));
		
		RestServiceDO recallVehicleDetailsRestServiceDO = gson.fromJson(recallVehicleDetailsJson, RestServiceDO.class);
		
		carJsonList = gson.fromJson(recallVehicleDetailsRestServiceDO.getResult().getMessage(), RestVehicleAssetDO[].class);
		
		if(carJsonList != null && carJsonList.length > 0)
		{
			recallVehicle = new ArrayList<CarDO>();
			
			for(RestVehicleAssetDO carJson : carJsonList)
			{
				CarDO car = new CarDO();
				car.setVin(carJson.getVehicleid());
				car.setManufacturer(carJson.getManufacturer());
				car.setModel(carJson.getModel());
				car.setVehicleOwner(carJson.getOwnername());
				car.setLastTransactionTimestamp(carJson.getTimestamp());
				car.setLastTransaction(carJson.getTransactiontype());
				car.setDigitalId(carJson.getDigitalid());
				car.setDigitalIdOwner(carJson.getDigitalidowner());
				recallVehicle.add(car);
			}
		}
		
		return recallVehicle;
	}

	public static boolean transferVehicle(String transferFrom, String transferTo, String vehicleId, String transferkey)
	{
		boolean isTransferSuccess = true;

		try
		{
			String transferJson = sendPOST(BlockChainRestServiceHelper.buildURLWithParams(transferkey, "invoke", new String[]{transferFrom, transferTo, vehicleId}));

			RestServiceDO transferRestServiceDO = gson.fromJson(transferJson, RestServiceDO.class);
			
			int responseCode = sendGET(transferRestServiceDO.getResult().getMessage());
			
			if(responseCode != 200)
			{
				isTransferSuccess = false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			isTransferSuccess = false;
		}
		
		return isTransferSuccess;
	}

	public static TransactionEnum sellVehicle(String seller, String buyer, String vehicleId)
	{
		TransactionEnum raiseClaimStatus = TransactionEnum.VALID;

		try
		{
			String sellVehicleJson = sendPOST(BlockChainRestServiceHelper.buildURLWithParams("sellvehicle", "invoke", new String[]{seller, buyer, vehicleId}));

			RestServiceDO sellVehicleRestServiceDO = gson.fromJson(sellVehicleJson, RestServiceDO.class);
			
			int responseCode = sendGET(sellVehicleRestServiceDO.getResult().getMessage());
			
			if(responseCode != 200)
			{
				raiseClaimStatus = TransactionEnum.FRAUDULENT;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			raiseClaimStatus = TransactionEnum.INVALID;
		}
		return raiseClaimStatus;
	}

	public static boolean certifyVehicle(String bodyShop, String vehicleId)
	{
		boolean certificationSuccess = true;

		try
		{
			String certifyVehicle = sendPOST(BlockChainRestServiceHelper.buildURLWithParams("certifyvehicle", "invoke", new String[]{bodyShop, vehicleId}));
			
			RestServiceDO certifyVehicleRestServiceDO = gson.fromJson(certifyVehicle, RestServiceDO.class);
			
			int responseCode = sendGET(certifyVehicleRestServiceDO.getResult().getMessage());
			
			if(responseCode != 200)
			{
				certificationSuccess = false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			certificationSuccess = false;
		}
		
		return certificationSuccess;
	}

	public static boolean releaseToken(String insurer, String vehicleOwner, String vehicleId, String digitalId)
	{
		boolean releaseTokenSuccess = true;

		try
		{
			String releaseTokenJson = sendPOST(BlockChainRestServiceHelper.buildURLWithParams("releasetoken", "invoke", new String[]{insurer, vehicleOwner, vehicleId, digitalId}));
			
			RestServiceDO releaseTokenRestServiceDO = gson.fromJson(releaseTokenJson, RestServiceDO.class);
			
			int responseCode = sendGET(releaseTokenRestServiceDO.getResult().getMessage());
			
			if(responseCode != 200)
			{
				releaseTokenSuccess = false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			releaseTokenSuccess = false;
		}
		
		return releaseTokenSuccess;
	}

	public static boolean recallVehicles(String manufacturer, String selectedModel) throws Exception
	{
		boolean vehicleRecallSuccess = true;

		try{
			String recalledVehiclesJson = sendPOST(BlockChainRestServiceHelper.buildURLWithParams("recallvehicles", "invoke", new String[]{manufacturer, selectedModel}));

			RestServiceDO recalledVehiclesRestServiceDO = gson.fromJson(recalledVehiclesJson, RestServiceDO.class);

			int responseCode = sendGET(recalledVehiclesRestServiceDO.getResult().getMessage());

			if(responseCode != 200)
			{
				vehicleRecallSuccess = false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			vehicleRecallSuccess = false;
		}

		return vehicleRecallSuccess;
	}
	
	private static Map<String, Object> buildURLWithParams(String function, String method, String[] args)
	{
		Map<String, Object> ctorMsgMap = new HashMap<String, Object>();
		ctorMsgMap.put("function", function);
		ctorMsgMap.put("args", args); //manufacturer

		Map<String, Object> chainCodeMap = new HashMap<String, Object>();
		chainCodeMap.put("name", "46c56925820bb6d2b6fab8b1dadde2f427954c8fbf4299865fc9a0c11d44fd78fdbdf38c309ff85bffce2479194d77d6bf3fd04fef474d9048df2bb07e47b42e");

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("type", 1);
		paramsMap.put("chaincodeID", chainCodeMap);
		paramsMap.put("ctorMsg", ctorMsgMap);
		paramsMap.put("secureContext", "user_type1_0");

		Map<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("jsonrpc", "2.0");
		reqParams.put("method", method); //invoke,
		reqParams.put("params", paramsMap);
		reqParams.put("id", 0);

		return reqParams;
	}

	private static String sendPOST(Map<String, Object> paramValues) throws Exception 
	{
		System.setProperty("https.protocols", "TLSv1.2");

		URL obj = new URL(CHAINCODE_POST_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");

		Gson gson = new Gson();

		String parms = gson.toJson(paramValues);

		System.out.println("Request params:" + parms);

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(parms.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		StringBuffer response = new StringBuffer();

		if(responseCode == HttpURLConnection.HTTP_OK) //success
		{ 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) 
			{
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} 
		else 
		{
			System.out.println("POST request not worked");
			throw new Exception("POST request not worked");
		}

		return response.toString();
	}

	private static int sendGET(String messageCode) throws Exception
	{
		Thread.sleep(1500);
		URL obj = new URL(TRANSACTIONS_GET_URL + messageCode);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		
		return con.getResponseCode();
	}
}
