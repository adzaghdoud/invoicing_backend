package com.invoicing.dao;

import java.sql.Timestamp;
import java.util.List;

import com.invoicing.model.Transaction;

public interface TransactionsDao {

void addtransaction(Transaction t);
List<Transaction> getlist();
boolean checkexistancetransaction(String transactionID);
void addtracking(String t,int nbtransaction , String state , String comment , String company);
}
