package com.invoicing.service;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.invoicing.dao.CompanyDao;

@Service("CompanyService")
@Transactional
public class CompanyServiceImpl implements CompanyService {
	@Autowired
    private CompanyDao dao;

	public String find_email_company( String rs) {
		return dao.find_email_company( rs);
	}

	public String find_bank_company(String rs) {
		return dao.find_bank_company(rs);
	}
    
}
