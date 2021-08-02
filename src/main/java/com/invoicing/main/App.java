package com.invoicing.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.jandex.Main;
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
    	final org.apache.logging.log4j.Logger log =  LogManager.getLogger(App.class);
         if (args.length >0) { 
         log.info("***************************Début Import Transactions Bank*********************");
         log.info("****************************lancement avec le json  "+args[0]);
         AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);	
 		 TransactionsService srvtransaction = (TransactionsService) context.getBean("TransactionsService");
    	 JSONParser jsonParser = new JSONParser();
  		JSONObject jsonObject;
		try {
			jsonObject = (JSONObject) jsonParser.parse(new FileReader(args[0]));
			JSONArray jsonArray = (JSONArray) jsonObject.get("transactions");
	  		 Iterator<JSONObject> iterator = jsonArray.iterator();
	              int nb=0;
	  		      while (iterator.hasNext()) {
	        	   JSONObject str = iterator.next();
	        	   Transaction t = new Gson().fromJson(str.toString(), Transaction.class);
	        	   if (srvtransaction.checkexistancetransaction(t.getTransaction_id())) {
	        	   log.info("Import nouvelle transaction: "+t.getTransaction_id());
	        	   srvtransaction.addtransaction(t);    
	               nb ++;
	        	   }
	        	   else log.info("La transaction "+t.getTransaction_id()+" existe déja en BDD");
	        	   }
	          
	           log.info("***************************Fin Import Transactions Bank , "+nb+" Nouvelles transactions importees************************");
	           context.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(ExceptionUtils.getStackTrace(e));
		}
  		 
    
  
         }else {
        	 
        	 log.error("Argument manquant , il faut mettre le path du json"); 
         }
         
         }  
    }
    



