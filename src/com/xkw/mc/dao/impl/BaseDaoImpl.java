package com.xkw.mc.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Cache;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.xkw.mc.dao.BaseDao;
import com.xkw.utils.Direction;
import com.xkw.utils.Page;
import com.xkw.utils.Reflections;
import com.xkw.utils.SearchOperator;


public abstract class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {
	
	protected Class<T> clazz = null;
	{
		clazz = Reflections.getClassGenricType(this.getClass());
	}
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	/**
	 * 获取当前session
	 * @return
	 */
	protected Session getSession() {
    	return sessionFactory.getCurrentSession();
    }

	/**
	 * QBC查询条件的添加
	 * @param criterion
	 * @return
	 */
	protected Criteria getCriteria(Criterion ... criterions) {
		Criteria criteria = getSession().createCriteria(clazz);
		if(criterions!=null && criterions.length>0) {
			for(Criterion criterion : criterions) {
				criteria.add(criterion);
			}
		}
		return criteria;
	}
	
	@Override
	public void saveOrUpdate(T t) {
		getSession().saveOrUpdate(t);
	}

	@Override
	public void save(T t) {
		getSession().save(t);
	}

	@Override
	public void update(T t) {
		getSession().update(t);
	}
	
	@Override
	public void delete(T t) {
		getSession().delete(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteById(ID id) {
		T t = (T) this.getSession().get(clazz, id);
		if(t!=null) {
			this.getSession().delete(t);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public T findOne(ID id) {
		return (T) getSession().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		String entityName = clazz.getSimpleName();
		return this.getSession().createQuery("from "+entityName).setCacheable(true).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getByProperty(String propertyName, Object propertyValue) {
		Criterion criterion = Restrictions.eq(propertyName, propertyValue);
		return (T) this.getCriteria(criterion).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getsByProperty(String propertyName, Object propertyValue) {
		Criterion criterion = Restrictions.eq(propertyName, propertyValue);
		return this.getCriteria(criterion).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getsByProperty(String propertyName, Object propertyValue, LinkedHashMap<String, Direction> orders) {
		Criterion criterion = Restrictions.eq(propertyName, propertyValue);
		Criteria criteria = this.getCriteria(criterion);
		if(orders!=null && orders.size()>0) {
			for(String key : orders.keySet()) {
				if (orders.get(key).toString().equals("ASC")) {
					criteria.addOrder(Order.asc(key));
				} else {
					criteria.addOrder(Order.desc(key));
				}
			}
		}
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<T> getPage(Integer page, Integer pageSize, Map<String, Object> queryMap,
			LinkedHashMap<String, Direction> orders) {
		String className = clazz.getSimpleName();
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT o FROM ").append(className).append(" o ");
		
		boolean flag = false;
		if(queryMap!=null && queryMap.size()>0) {
			//添加查询条件
			flag = true;
			sql.append(" WHERE ");
			for(String key : queryMap.keySet()) {
				sql.append(" o."+key+"=:"+key+" AND");
			}
			sql.delete(sql.lastIndexOf("AND"), sql.length());
		}
		
		//求取查询的总记录数
		String countSql = sql.toString().replaceFirst("o", "count(o.id)");
		Query countQuery = this.getSession().createQuery(countSql);
		if(flag) {
			//设置查询值
			for(String key : queryMap.keySet()) {
				countQuery.setParameter(key, queryMap.get(key));
			}
		}
		Long total = (Long) countQuery.uniqueResult();
		
		//添加排序
		if(orders!=null && orders.size()>0) {
			sql.append(" ORDER BY ");
			for(String key : orders.keySet()) {
				sql.append(" o.").append(key).append(" ").append(orders.get(key).toString()).append(", ");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
		}
		page = (page<1)?1:page;
		int index = (page-1)*pageSize;
		Query query = getSession().createQuery(sql.toString());
		if(flag) {
			//设置查询值
			for(String key : queryMap.keySet()) {
				query.setParameter(key, queryMap.get(key));
			}
		}
		//设置分页信息
		query.setFirstResult(index).setMaxResults(pageSize);
		List<T> content = query.list();
		
		return new Page<>(content, page, total, pageSize);
	}

	@Override
	public int updatePropertyById(ID id, String propertyName, Object propertyValue) {
		String className = clazz.getSimpleName();
		String sql = "UPDATE "+className+" o SET o."+propertyName+"=:parameter WHERE o.id=:id";
		Query query = getSession().createQuery(sql);
		query.setParameter("parameter", propertyValue);
		query.setParameter("id", id);
		
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E getPropertyValueById(ID id, String propertyName) {
		String className = clazz.getSimpleName();
		String sql = "SELECT o."+propertyName+" FROM "+className+" o WHERE o.id=:id";
		Query query = getSession().createQuery(sql);
		query.setParameter("id", id);
		
		return (E) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> List<E> getsByProperty(String propertyName, Object propertyValue, String destProperty) {
		String className = clazz.getSimpleName();
		String sql = "SELECT o."+destProperty+" FROM "+className +" o WHERE o."+propertyName+"=:key";
		Query query = getSession().createQuery(sql);
		query.setParameter("key", propertyValue);
		
		return query.list();
	}
	
	@Override
	public void evict(Class<?> entityClass) {
		this.sessionFactory.getCache().evictDefaultQueryRegion();
		this.sessionFactory.getCache().evictEntityRegion(entityClass);
	}

	@Override
	public void evict(Class<?> entityClass, Serializable id) {
		this.sessionFactory.getCache().evictDefaultQueryRegion();
		this.sessionFactory.getCache().evictEntity(entityClass, id);
	}
	
	
	@Override
	public void evictCollection(String str) {
		this.sessionFactory.getCache().evictDefaultQueryRegion();
		this.sessionFactory.getCache().evictCollectionRegion(str);
	}
	
	
	@Override
	public void evictAll() {
		Cache cache = this.sessionFactory.getCache();  
        cache.evictEntityRegions();  
        cache.evictCollectionRegions();  
        cache.evictDefaultQueryRegion();  
        cache.evictQueryRegions();
        this.getSession().clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> like(String key, String property) {
		String className = clazz.getSimpleName();
		String sql = "SELECT o FROM "+className+" o WHERE o."+property+" LIKE :key";
		Query query = getSession().createQuery(sql);
		query.setParameter("key", "%"+key+"%");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> like(String key, String property, Long id) {
		String className = clazz.getSimpleName();
		String sql = "SELECT o FROM "+className+" o WHERE o."+property+" LIKE :key AND id!=:id";
		Query query = getSession().createQuery(sql);
		query.setParameter("key", "%"+key+"%");
		query.setParameter("id", id);
		return query.list();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<T> getByIds(Set<ID> ids, LinkedHashMap<String, Direction> orders) {
		if(ids==null || ids.size()<1) {
			return null;
		}
		String className = clazz.getSimpleName();
		StringBuffer sql = new StringBuffer("SELECT o FROM "+className +" o WHERE o.id IN (:ids) ") ;
		
		if(orders!=null && orders.size()>0) {
			sql.append(" ORDER BY ");
			for(String key : orders.keySet()) {
				sql.append(key).append(" "+orders.get(key).toString()+", ");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
		}
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("ids", ids);
		return query.list();
	}

	@Override
	public int increment(ID id, String propertyName, Number incr) {
		String className = clazz.getSimpleName();
		String sql = "UPDATE "+className+" o SET o."+propertyName+"=o."+propertyName+"+:propertyValue WHERE o.id=:id";
		Query query = getSession().createQuery(sql);
		query.setParameter("propertyValue", incr);
		query.setParameter("id", id);
		
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> List<E> getsByPropertys(String propertyName, Set<?> propertyValue, String destProperty) {
		String className = clazz.getSimpleName();
		String sql = "SELECT o."+destProperty+" FROM "+className +" o WHERE o."+propertyName+"in(:keys)";
		Query query = getSession().createQuery(sql);
		query.setParameter("keys", propertyValue);
		return (List<E> ) query.list();
	}

	@Override
	public int updatePropertyByProperty(String key, Object value, String propertyName, Object propertyValue) {
		String className = clazz.getSimpleName();
		String sql = "UPDATE "+className+" o SET o."+propertyName+"=:parameter WHERE o."+key+"=:"+key;
		Query query = getSession().createQuery(sql);
		query.setParameter("parameter", propertyValue);
		query.setParameter(key, value);
		
		return query.executeUpdate();
	}

	
	/**
	 * 通用的查询方法
	 * @param searchs
	 * @param orders
	 * @return
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> generalQuery(List<SearchOperator> searchs, LinkedHashMap<String, Direction> orders) {
		String className = clazz.getSimpleName();
		StringBuffer sql = new StringBuffer("SELECT o FROM " +className+ " o ");
		
		if(searchs!=null && searchs.size()>0) {
			sql.append(" WHERE ");
			for(SearchOperator search : searchs) {
				sql.append(" "+search.fieldName);
				if (search.operator==SearchOperator.Operator.EQ) {
					sql.append("=").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.NOTEQ) {
					sql.append("!=").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.LIKE) {
					sql.append(" LIKE ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.NOTLIKE) {
					sql.append(" NOT LIKE ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.GT) {
					sql.append(" > ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.GTE) {
					sql.append(" >= ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.LT) {
					sql.append(" < ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.LTE) {
					sql.append(" <= ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.NULL) {
					sql.append(" IS NULL ");//操作类型为null或not null时, value值可为null
				} else if (search.operator==SearchOperator.Operator.NOTNULL) {
					sql.append(" IS NOT NULL ");//操作类型为null或not null时, value值可为null
				} else if (search.operator==SearchOperator.Operator.IN) {
					sql.append(" IN ").append("(:"+search.fieldName+")");
				} else if (search.operator==SearchOperator.Operator.NOTIN) {
					sql.append(" NOT IN ").append("(:"+search.fieldName+")");
				}
				sql.append(" AND");
			}
			sql.delete(sql.lastIndexOf("AND"), sql.length());
		}
		
		//添加排序
		if(orders!=null && orders.size()>0) {
			sql.append(" ORDER BY ");
			for(String key : orders.keySet()) {
				sql.append(" o.")
					.append(key)
					.append(" ")
					.append(orders.get(key).toString())
					.append(", ");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
		}
		Query query = getSession().createQuery(sql.toString());
		if(searchs!=null && searchs.size()>0) {
			for(SearchOperator search : searchs) {
				if(search.operator==SearchOperator.Operator.LIKE || search.operator==SearchOperator.Operator.NOTLIKE) {
					query.setParameter(search.fieldName, "%"+search.fieldValue.toString()+"%");
				} else if (search.operator==SearchOperator.Operator.IN || search.operator==SearchOperator.Operator.NOTIN) {
					if(search.fieldValue instanceof Collection) {
						query.setParameterList(search.fieldName, (Collection) search.fieldValue);
					} else if (search.fieldValue.getClass().isArray()) {
						query.setParameterList(search.fieldName, (Object[])search.fieldValue);
					}
				} else if (search.operator==SearchOperator.Operator.NULL || search.operator==SearchOperator.Operator.NOTNULL) {
					//不作为
				} else {
					query.setParameter(search.fieldName, search.fieldValue);
				}
			}
		}
		return query.list();
	}
	
	
	/**
	 * 通用的页查询方法
	 * @param page
	 * @param pageSize
	 * @param searchs
	 * @param orders
	 * @return
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page<T> generalPageQuery(Integer page, Integer pageSize, List<SearchOperator> searchs, LinkedHashMap<String, Direction> orders) {
		String className = clazz.getSimpleName();
		StringBuffer sql = new StringBuffer("SELECT o FROM " +className+ " o ");
		
		if(searchs!=null && searchs.size()>0) {
			sql.append(" WHERE ");
			for(SearchOperator search : searchs) {
				sql.append(" "+search.fieldName);
				if (search.operator==SearchOperator.Operator.EQ) {
					sql.append("=").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.NOTEQ) {
					sql.append("!=").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.LIKE) {
					sql.append(" LIKE ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.NOTLIKE) {
					sql.append(" NOT LIKE ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.GT) {
					sql.append(" > ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.GTE) {
					sql.append(" >= ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.LT) {
					sql.append(" < ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.LTE) {
					sql.append(" <= ").append(":"+search.fieldName);
				} else if (search.operator==SearchOperator.Operator.NULL) {
					sql.append(" IS NULL ");//操作类型为null或not null时, value值可为null
				} else if (search.operator==SearchOperator.Operator.NOTNULL) {
					sql.append(" IS NOT NULL ");//操作类型为null或not null时, value值可为null
				} else if (search.operator==SearchOperator.Operator.IN) {
					sql.append(" IN ").append("(:"+search.fieldName+")");
				} else if (search.operator==SearchOperator.Operator.NOTIN) {
					sql.append(" NOT IN ").append("(:"+search.fieldName+")");
				}
				sql.append(" AND");
			}
			sql.delete(sql.lastIndexOf("AND"), sql.length());
		}
		
		Query countQuery = getSession().createQuery(sql.toString().replaceFirst("o", "count(o.id)"));
		if(searchs!=null && searchs.size()>0) {
			for(SearchOperator search : searchs) {
				if(search.operator==SearchOperator.Operator.LIKE || search.operator==SearchOperator.Operator.NOTLIKE) {
					countQuery.setParameter(search.fieldName, "%"+search.fieldValue.toString()+"%");
				} else if (search.operator==SearchOperator.Operator.IN || search.operator==SearchOperator.Operator.NOTIN) {
					if(search.fieldValue instanceof Collection) {
						countQuery.setParameterList(search.fieldName, (Collection) search.fieldValue);
					} else if (search.fieldValue.getClass().isArray()) {
						countQuery.setParameterList(search.fieldName, (Object[])search.fieldValue);
					}
				} else if (search.operator==SearchOperator.Operator.NULL || search.operator==SearchOperator.Operator.NOTNULL) {
					//不作为
				} else {
					countQuery.setParameter(search.fieldName, search.fieldValue);
				}
			}
		}
		long total = (long) countQuery.uniqueResult();
		
		//添加排序
		if(orders!=null && orders.size()>0) {
			sql.append(" ORDER BY ");
			for(String key : orders.keySet()) {
				sql.append(" o.")
					.append(key)
					.append(" ")
					.append(orders.get(key).toString())
					.append(", ");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
		}
		
		Query query = getSession().createQuery(sql.toString());
		if(searchs!=null && searchs.size()>0) {
			for(SearchOperator search : searchs) {
				if(search.operator==SearchOperator.Operator.LIKE || search.operator==SearchOperator.Operator.NOTLIKE) {
					query.setParameter(search.fieldName, "%"+search.fieldValue.toString()+"%");
				} else if (search.operator==SearchOperator.Operator.IN || search.operator==SearchOperator.Operator.NOTIN) {
					if(search.fieldValue instanceof Collection) {
						query.setParameterList(search.fieldName, (Collection) search.fieldValue);
					} else if (search.fieldValue.getClass().isArray()) {
						query.setParameterList(search.fieldName, (Object[])search.fieldValue);
					}
				} else if (search.operator==SearchOperator.Operator.NULL || search.operator==SearchOperator.Operator.NOTNULL) {
					//不作为
				} else {
					query.setParameter(search.fieldName, search.fieldValue);
				}
			}
		}
		
		page = page<1 ? 1 : page;
		int index = (page-1)*pageSize;
		query.setFirstResult(index).setMaxResults(pageSize);
		List content = query.list();
		return new Page<>(content, page, total, pageSize);
	}

}
