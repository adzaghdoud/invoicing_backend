package com.invoicing.dao;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import com.invoicing.model.Company;


@Repository("CompanyDao")
public class CompanyDaoImpl extends  AbstractDao implements CompanyDao {

	public String find_email_company(String rs) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		CriteriaQuery<Company> criteria = builder.createQuery(Company.class);
		Root<Company> root = criteria.from(Company.class);
		criteria.select(root).where(builder.equal(root.get("rs"), rs));
		return getSession().createQuery(criteria).getSingleResult().getEmail();
	}

	public String find_bank_company(String rs) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		CriteriaQuery<Company> criteria = builder.createQuery(Company.class);
		Root<Company> root = criteria.from(Company.class);
		criteria.select(root).where(builder.equal(root.get("rs"), rs));
		return getSession().createQuery(criteria).getSingleResult().getBankname();
	}
}
