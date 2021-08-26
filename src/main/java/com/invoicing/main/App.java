package com.invoicing.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
         log.info("***************************Début Import Transactions Bank pour "+args[1]+"*********************");
         log.info("****************************lancement avec le json  "+args[0]);
         AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);	
 		 TransactionsService srvtransaction = (TransactionsService) context.getBean("TransactionsService");
    	 JSONParser jsonParser = new JSONParser();
  		 JSONObject jsonObject;
  		File file = new File(args[0]);
  		if(file.length() == 0) {
  		log.error(args[0]+"est vide");	
  		}
  		
  		else {
  		
  		 try {
			jsonObject = (JSONObject) jsonParser.parse(new FileReader(args[0]));
			JSONArray jsonArray = (JSONArray) jsonObject.get("transactions");
	  		 Iterator<JSONObject> iterator = jsonArray.iterator();
	              int nb=0;
	  		      while (iterator.hasNext()) {
	        	   JSONObject str = iterator.next();
	        	   Transaction t = new Gson().fromJson(str.toString(), Transaction.class);
	        	   if (srvtransaction.checkexistancetransaction(t.getTransaction_id())) {
	        	   // reformat date and time
	        		 String date;
	        		 String time;
	        		 date = t.getSettled_at().substring(0, t.getSettled_at().indexOf("T"));
	      			 time = t.getSettled_at().substring(t.getSettled_at().indexOf("T")+1,t.getSettled_at().indexOf("Z")-4 );
	      			 t.setSettled_at(date+" "+time);

	      			 date = t.getUpdated_at().substring(0, t.getUpdated_at().indexOf("T"));
	      			 time = t.getUpdated_at().substring(t.getUpdated_at().indexOf("T")+1,t.getUpdated_at().indexOf("Z")-4 );
	        		 t.setUpdated_at(date+" "+time);
	        	     log.info("Import nouvelle transaction: "+t.getTransaction_id() +" de type "+t.getSide()+" avec un montant "+t.getAmount()+" "+t.getCurrency());
	        	     if (t.getSide().contentEquals("credit") && t.getAmount()>1) {
	        	      t.setAmount_HT(t.getAmount()/1.2);
	        	     }
	        	     t.setCompany(args[1].toUpperCase());
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
  		 
  		}
  
         }else {
        	 
        	 log.error("Argument manquant , il faut mettre le path du json et le company"); 
         }
         
         }  
    }
    



