package com.invoicing.service;

import java.sql.Timestamp;

import com.invoicing.model.Company;

public interface CompanyService {
	String  find_email_company(String rs);
	String find_bank_company(String rs);

}
