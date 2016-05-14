package com.hg.leases.dao;

import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.hg.leases.model.LeaseEntity;

/**
 * @author hanleys
 */
public abstract class BaseDao<T extends LeaseEntity> {

	@Autowired
	protected SessionFactory sessionFactory;

	protected void merge(final T object) {
		Session session = sessionFactory.getCurrentSession();
		session.merge(object);
	}

	protected void save(final T object) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(object);
	}

	protected void delete(final T object) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(object);
	}

	@SuppressWarnings("unchecked")
	protected T get(final long id, final Class<T> entity) {
		Session session = sessionFactory.getCurrentSession();
		return (T) session.get(entity, id);
	}

	protected Set<T> get(final Class<T> entity) {
		return get(entity, EMPTY_STRING, EMPTY_STRING);
	}

	protected Set<T> get(final Class<T> entity,
			final String orderByAssociation, final String orderByProperty) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entity);
		createOrderCriteria(orderByAssociation, orderByProperty, criteria);
		return get(criteria);
	}

	@SuppressWarnings("unchecked")
	protected Set<T> get(final Criteria criteria) {
		List<T> results = criteria.list();
		if (!isEmpty(results)) {
			return new LinkedHashSet<T>(results);
		}
		return new LinkedHashSet<T>();
	}

	protected void createLikeCriteria(final String filterAssociation,
			final String filterProperty, final String filterValue,
			final Criteria criteria, final boolean order) {
		Criteria subCriteria = null;
		if (hasLength(filterValue)) {
			if (filterValue.contains("%")) {

				subCriteria = criteria.createCriteria(filterAssociation).add(
						Restrictions.like(filterProperty, filterValue)
								.ignoreCase());
			} else {
				subCriteria = criteria.createCriteria(filterAssociation).add(
						Restrictions.like(filterProperty,
								"%" + filterValue + "%").ignoreCase());
			}
			if (order) {
				subCriteria.addOrder(Order.asc(filterProperty));
			}
		}
	}

	protected void createOrderCriteria(final String orderByAssociation,
			final String orderByProperty, final Criteria criteria) {
		if (hasLength(orderByProperty)) {
			if (hasLength(orderByAssociation)) {
				criteria.createCriteria(orderByAssociation).addOrder(
						Order.asc(orderByProperty));
			} else {
				criteria.addOrder(Order.asc(orderByProperty));
			}
		}
	}
}