package com.feiyangedu.petstore.data;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

@Component
public class HibernateInterceptor extends EmptyInterceptor {

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		Long time = System.currentTimeMillis();
		for (int i = 0; i < propertyNames.length; i++) {
			if ("updatedTime".equals(propertyNames[i])) {
				currentState[i] = time;
			}
		}
		return true;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		Long time = System.currentTimeMillis();
		for (int i = 0; i < propertyNames.length; i++) {
			if ("createdTime".equals(propertyNames[i]) || "updatedTime".equals(propertyNames[i])) {
				state[i] = time;
			}
		}
		return true;
	}

}
