package com.feiyangedu.petstore.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.feiyangedu.petstore.data.HibernateDao;

public class AbstractService {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	HibernateDao dao;

}
