package com.feiyangedu.petstore.data;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.feiyangedu.petstore.exception.APIEntityNotFoundException;
import com.feiyangedu.petstore.exception.APINonUniqueEntityException;
import com.feiyangedu.petstore.model.AbstractEntity;

@Component
@Transactional
public class HibernateDao {

	protected static final int DEFAULT_PAGE_SIZE = 10;

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	SessionFactory sessionFactory;

	public void save(AbstractEntity... entities) {
		Session session = sessionFactory.getCurrentSession();
		for (AbstractEntity entity : entities) {
			session.save(entity);
		}
	}

	public void delete(AbstractEntity... entities) {
		Session session = sessionFactory.getCurrentSession();
		for (AbstractEntity entity : entities) {
			session.delete(entity);
		}
	}

	public void update(AbstractEntity... entities) {
		Session session = sessionFactory.getCurrentSession();
		for (AbstractEntity entity : entities) {
			session.update(entity);
		}
	}

	public <T> T load(Class<T> clazz, String id) {
		T t = get(clazz, id);
		if (t == null) {
			throw new APIEntityNotFoundException(clazz);
		}
		return t;
	}

	public <T> T get(Class<T> clazz, String id) {
		return sessionFactory.getCurrentSession().get(clazz, id);
	}

	public <T> T unique(String hql, Object... params) {
		List<T> list = doList(0, 2, hql, params);
		if (list.isEmpty()) {
			throw new APIEntityNotFoundException("AbstractEntity", "Entity not found");
		}
		if (list.size() == 2) {
			throw new APINonUniqueEntityException("AbstractEntity", "Too many entities found.");
		}
		return list.get(0);
	}

	public <T> T first(String hql, Object... params) {
		List<T> list = doList(0, 1, hql, params);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public <T> List<T> list(String hql, Object... params) {
		return doList(0, 1000, hql, params);
	}

	public <T> List<T> listByPage(int pageIndex, String hql, Object... params) {
		return listByPage(pageIndex, DEFAULT_PAGE_SIZE, hql, params);
	}

	public <T> List<T> listByPage(int pageIndex, int pageSize, String hql, Object... params) {
		int firstResult = (pageIndex - 1) * pageSize;
		int maxResults = pageSize;
		return doList(firstResult, maxResults, hql, params);
	}

	<T> List<T> doList(int firstResult, int maxResults, String hql, Object... params) {
		@SuppressWarnings("unchecked")
		Query<T> query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		query.setFetchSize(maxResults);
		return query.getResultList();
	}
}
