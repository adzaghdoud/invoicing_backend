package com.invoicing.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQuery;

@Entity
@Table(name="transactions")
@NamedNativeQuery(
        name = "add_tracking_import",
        query = "insert into suivi_import (date,nb_transaction_imported,nb_credit,nb_debit,total_debit,total_credit,old_balance,new_balance,state,comment,company)values(?,?,?,?,?,?,?,?,?,?,?)",
                    resultClass=Transaction.class
    )
public class Transaction {
	@Id
	private String transaction_id;
	private double amount;
	private String side;
	private String status;
	private String label;
	private String operation_type;
	private String currency;
	private String settled_at;
	private String updated_at;
	private String reference;
	private double amount_HT;
	private String company;
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getOperation_type() {
		return operation_type;
	}
	public void setOperation_type(String operation_type) {
		this.operation_type = operation_type;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSettled_at() {
		return settled_at;
	}
	public void setSettled_at(String settled_at) {
		this.settled_at = settled_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public double getAmount_HT() {
		return amount_HT;
	}
	public void setAmount_HT(double d) {
		this.amount_HT = d;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}

    
    		

}
