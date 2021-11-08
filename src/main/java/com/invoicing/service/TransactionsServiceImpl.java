package com.invoicing.service;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.invoicing.dao.TransactionsDao;
import com.invoicing.model.Transaction;
@Service("TransactionsService")
@Transactional
public class TransactionsServiceImpl implements TransactionsService {
	@Autowired
    private TransactionsDao dao;

	public void addtransaction(Transaction t) {
		// TODO Auto-generated method stub
		dao.addtransaction(t);
	}

	public List<Transaction> getlist() {
		// TODO Auto-generated method stub
		return dao.getlist();
	}

	public boolean checkexistancetransaction(String transactionID) {
		// TODO Auto-generated method stub
		return dao.checkexistancetransaction(transactionID);
	}

	

	public void addtracking(String t, int nbtransaction, int nb_credit, int nb_debit, String state, String comment,
			String company) {
		dao.addtracking(t, nbtransaction, nb_credit, nb_debit,state, comment, company);
	}

}
