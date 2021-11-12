package com.invoicing.dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import com.invoicing.model.Transaction;

@Repository("TransactionDaoImpl")
	public class TransacationsDaoImpl extends  AbstractDao implements TransactionsDao{

		public void addtransaction(Transaction t) {
			// TODO Auto-generated method stub
			persist(t);
		}

		public List<Transaction> getlist() {
			// TODO Auto-generated method stub
			CriteriaBuilder builder = getSession().getCriteriaBuilder();
			CriteriaQuery<Transaction> criteria = builder.createQuery(Transaction.class);
			Root<Transaction> root = criteria.from(Transaction.class);
			criteria.select(root);
			Query<Transaction> q=getSession().createQuery(criteria);		
			if (q.list().size() == 0) {
			}
			return q.list();
		}

		public boolean checkexistancetransaction(String transactionID) {
			// TODO Auto-generated method stub
			CriteriaBuilder builder = getSession().getCriteriaBuilder();
			CriteriaQuery<Transaction> criteria = builder.createQuery(Transaction.class);
			Root<Transaction> root = criteria.from(Transaction.class);
			criteria.select(root).where(builder.equal(root.get("transaction_id"), transactionID));
			Query<Transaction> q=getSession().createQuery(criteria);
	         try {
	        	 q.getSingleResult(); 
	         }catch(NoResultException e) {
	         return true; 
	         }
			return false;
			
	}

		public void addtracking(String t, int nbtransaction,int nb_credit,int nb_debit,double total_debit,double total_credit,double old_balance,double new_balance,String state, String comment, String company) {
			javax.persistence.Query query = getSession().createNamedQuery("add_tracking_import", Transaction.class);
			query.setParameter(1, t);
			query.setParameter(2, nbtransaction);
			query.setParameter(3, nb_credit);
			query.setParameter(4, nb_debit);
			query.setParameter(5, total_debit);
			query.setParameter(6, total_credit);
			query.setParameter(7, old_balance);
			query.setParameter(8, new_balance);
			query.setParameter(9, state);
			query.setParameter(10, comment);
			query.setParameter(11, company);	
			query.executeUpdate();	
		}
}