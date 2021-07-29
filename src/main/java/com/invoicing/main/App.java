package com.invoicing.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.google.gson.Gson;
import com.invoicing.hibernate.configuration.AppConfig;
import com.invoicing.model.Transaction;
import com.invoicing.service.TransactionsService;

public class App 
{
    public static void main( String[] args )
    {
       
          
    	  System.out.println("***************************Début Import Transactions Bank*********************");
         AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);	
 		 TransactionsService srvtransaction = (TransactionsService) context.getBean("TransactionsService");
    	  JSONParser jsonParser = new JSONParser();
  		JSONObject jsonObject;
		try {
			jsonObject = (JSONObject) jsonParser.parse(new FileReader(System.getProperty("Filepath")));
			JSONArray jsonArray = (JSONArray) jsonObject.get("transactions");
	  		 Iterator<JSONObject> iterator = jsonArray.iterator();
	              int nb=0;
	  		      while (iterator.hasNext()) {
	        	   JSONObject str = iterator.next();
	        	   Transaction t = new Gson().fromJson(str.toString(), Transaction.class);
	        	   if (srvtransaction.checkexistancetransaction(t.getTransaction_id())) {
	        	   System.out.println("Import nouvelle transaction: "+t.getTransaction_id());
	        	   //srvtransaction.addtransaction(t);    
	               nb ++;
	        	   }
	        	   else System.out.println("La transaction "+t.getTransaction_id()+" existe déja en BDD");
	        	   }
	           context.close();
	           System.out.println("***************************Fin Import Transactions Bank , "+nb+" Nouvelles transactions importees************************");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		 
    }

 
    
    
    
    
    
    }
    



