package com.invoicing.service;

import java.sql.Timestamp;
import java.util.List;

import com.invoicing.model.Transaction;

public interface TransactionsService {
	void addtransaction(Transaction t);
	List<Transaction> getlist();
	boolean checkexistancetransaction(String transactionID);
	void addtracking(String  t,int nbtransaction ,int nb_debit, int nb_credit,String state , String comment , String company);
}
