package com.invoicing.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.invoicing.service.CompanyService;
import com.invoicing.service.TransactionsService;
import com.invoicing.tools.Sendmail;

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
 		 CompanyService srvcompany = (CompanyService) context.getBean("CompanyService");
    	 JSONParser jsonParser = new JSONParser();
  		 JSONObject jsonObject;
  	     File file = new File(args[0]);
		 Date mydate = new Date();
         Timestamp ts=new Timestamp(mydate.getTime());
         SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
  	     boolean flag= false;
  		if(file.length() == 0) {
  		log.error(args[0]+"est vide");	
  		}
  		
  		else {
  		
  		 try {
			jsonObject = (JSONObject) jsonParser.parse(new FileReader(args[0]));
			JSONArray jsonArray = (JSONArray) jsonObject.get("transactions");
	  		 Iterator<JSONObject> iterator = jsonArray.iterator();
	  		 ArrayList<Transaction> listc = new ArrayList<Transaction>();
	  		
	                 int nb=0;
	                 int nb_credit=0;
	                 int nb_debit=0;
	                 double total_credit=0;
	                 double total_debit=0;
	                 double in =0;
	                 double out=0;
	                 // calculate balance before import
	                 List<Transaction> my_transactions=srvtransaction.getlist();
	                 for (int i=0 ; i<my_transactions.size() ; i++) {
	                	if (my_transactions.get(i).getSide().contentEquals("credit")) {
	                		in+=my_transactions.get(i).getAmount();
	                	}
	                	
	                	if (my_transactions.get(i).getSide().contentEquals("debit")) {
	                		out+=my_transactions.get(i).getAmount();
	                	}
	                	
	                 }       
	             	 BigDecimal bd = BigDecimal.valueOf(in);
	        		 BigDecimal bd2 = BigDecimal.valueOf(out);
	        		 BigDecimal result=bd.subtract(bd2);
	        		 result = result.setScale(2, RoundingMode.DOWN);  
	        		 double balance= result.doubleValue();
	        		 double new_balance=balance;	 
	        		 while (iterator.hasNext()) {
	        	     JSONObject str = iterator.next();
	        	     Transaction t = new Gson().fromJson(str.toString(), Transaction.class);
	        	     if (srvtransaction.checkexistancetransaction(t.getTransaction_id())) {
	        		 listc.add(t); 		 
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
	        	     try {
	        	     if (t.getSide().contentEquals("credit")) {
	        	     nb_credit++;
	        	     new_balance+=t.getAmount();
	        	     total_credit+=t.getAmount();
	        	     }
	        	     if (t.getSide().contentEquals("debit")) {
	        	     nb_debit++;
	        	     new_balance=new_balance-t.getAmount();
	        	     total_debit+=t.getAmount();
		        	 }  
	        	     srvtransaction.addtransaction(t);
	        	     nb ++;
	        	     }catch (Exception e) {
	        	     srvtransaction.addtracking(formatter.format(mydate), nb, nb_credit,nb_debit,total_debit,total_credit,balance,new_balance,"KO","Erreur importation transaction "+t.getTransaction_id() + " avec un montant de "+t.getAmount(), args[1]);
	        	     flag=true;
	        	     }
	               
	        	   }
	        	   else log.info("La transaction "+t.getTransaction_id()+" existe déja en BDD");
	        	   }
	  		    log.info("***************************Fin Import Transactions Bank , "+nb+" Nouvelles transactions importees************************");
	          if (!flag) {
	  		  // tracking import case ok 
	  		  srvtransaction.addtracking(formatter.format(mydate),nb,nb_credit,nb_debit,total_debit,total_credit,balance,new_balance, "OK", "", args[1]);
	          //sending mail
	           if (listc.size() >0 ) {
	        	 Sendmail s = new Sendmail();
	        	 s.setMailto(srvcompany.find_email_company(args[1]));
	        	 s.setSubject("Importations transactions bancaires");
	        	 String text=
	        			 "Bonjour "+args[1]+","
	        			 +"<br/>"
	        			 +"<p> Ci-dessous les nouvelles transactions importées de votre compte "+srvcompany.find_bank_company(args[1])+"</p>"
	        			 +"<br/>"
	        	         +"<table width='100%' border='1' align='center'>"
	        	                + "<tr align='center'>"
	        	                + "<td><b>Transaction ID <b></td>"
	        	                + "<td><b>Side <b></td>"
	        	                + "<td><b>Amount<b></td>"
	        	                + "<td><b>Currency<b></td>"
	        	                + "<td><b>Reference<b></td>"
	        	                + "<td><b>Label<b></td>"
	        	                + "</tr>";
	        	 for(int i=0 ;  i<listc.size(); i++) {
	        		 text=text+"<tr>"
	        	             +"<td align='center'>"+listc.get(i).getTransaction_id()+"</td>";
	        		 if (listc.get(i).getSide().contentEquals("debit")) {	 
	        	             text=text+"<td  align='center'><span style='color: red;'>"+listc.get(i).getSide()+"</span></td>";
	        				
	        		 }
	        		 else {
	            		
	        			 text=text+"<td align='center'><span style='color: green;'>"+listc.get(i).getSide()+"</span></td>";
	        		 }
	        		 text=text+"<td  align='center'>"+listc.get(i).getAmount()+"</td>"
	        				 +"<td  align='center'>"+listc.get(i).getCurrency()+"</td>"
	        				 +"<td  align='center'>"+listc.get(i).getReference()+"</td>"
	        				 +"<td  align='center'>"+listc.get(i).getLabel()+"</td></tr>";
	        	 }
	           text=text+"</table> <br/> <p> Cordialement </p> <br/> Email envoyé automatiquement par invoicing backend batch";
	           s.setContain(text);
	           s.sendmail();
	           }
	           
	          }      
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
    



