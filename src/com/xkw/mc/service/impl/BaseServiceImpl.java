package com.xkw.mc.service.impl;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xkw.mc.dao.BaseDao;
import com.xkw.mc.service.BaseService;
import com.xkw.utils.Direction;
import com.xkw.utils.Page;
import com.xkw.utils.SearchOperator;


@Transactional(propagation=Propagation.REQUIRED)
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {
	
	@Autowired
	protected BaseDao<T, ID> baseDao;
	
	@Override
	public void save(T t) {
		baseDao.save(t);
	}

	@Override
	public void update(T t) {
		baseDao.update(t);
	}

	@Override
	public void deleteById(ID id) {
		baseDao.deleteById(id);
	}
	
	@Override
	public void saveOrUpdate(T T){
		baseDao.saveOrUpdate(T);
	}
	
	
	@Override
	public void delete(T T){
		baseDao.delete(T);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public T getById(ID id){
		return baseDao.findOne(id);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<T> getAll(){
		return baseDao.findAll(); 
	}
	
	
	/**
	 * Service层通用分页方法
	 * 获取分页数据,返回page对象
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<T> getPage(Integer page, Integer pageSize, Map<String, Object> queryMap, LinkedHashMap<String, Direction> orders){
		return baseDao.getPage(page, pageSize, queryMap, orders);
	}

	
	@Override
	public int updatePropertyById(ID id, String propertyName,
			Object propertyValue) {
		return baseDao.updatePropertyById(id, propertyName, propertyValue);
	}
	

	@Override
	@Transactional(readOnly=true)
	public T getByProperty(String propertyName, Object propertyValue) {
		return baseDao.getByProperty(propertyName, propertyValue);
	}
	

	@Override
	@Transactional(readOnly=true)
	public List<T> getsByProperty(String propertyName, Object propertyValue) {
		return baseDao.getsByProperty(propertyName, propertyValue);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<T> getsByProperty(String propertyName,
			Object propertyValue, LinkedHashMap<String, Direction> orders) {
		return baseDao.getsByProperty(propertyName, propertyValue, orders);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public <E> E getPropertyValueById(ID id, String propertyName) {
		return baseDao.getPropertyValueById(id, propertyName);
	}
	
	
	@Override
	public void evict(Class<?> TClass) {
		baseDao.evict(TClass);;
	}
	
	
	@Override
	public void evict(Class<?> TClass, Serializable id) {
		baseDao.evict(TClass, id);;
	}
	
	
	@Override
	public void evictCollection(String str) {
		baseDao.evictCollection(str);
	}
	
	
	@Override
	public void evictAll() {
		baseDao.evictAll();
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<T> like(String key, String property) {
		return baseDao.like(key, property);
	}
	

	@Override
	@Transactional(readOnly=true)
	public List<T> like(String key, String property,Long id) {
		return baseDao.like(key, property);
	}


	@Override
	@Transactional(readOnly=true)
	public List<T> getByIds(Set<ID> ids, LinkedHashMap<String, Direction> orders) {
		return baseDao.getByIds(ids, orders);
	}


	@Override
	@Transactional(readOnly=true)
	public <E> List<E> getsByProperty(String propertyName,
			Object propertyValue, String destProperty) {
		return baseDao.getsByProperty(propertyName, propertyValue, destProperty);
	}


	@Override
	public int increment(ID id, String propertyName, Number incr) {
		return baseDao.increment(id, propertyName, incr);
	}


	@Override
	public int updatePropertyByProperty(String key, Object value,
			String propertyName, Object propertyValue) {
		// TODO Auto-generated method stub
		return baseDao.updatePropertyByProperty(key, value, propertyName, propertyValue);
	}


	@Override
	@Transactional(readOnly=true)
	public List<T> generalQuery(List<SearchOperator> searchs, LinkedHashMap<String, Direction> orders) {
		return baseDao.generalQuery(searchs, orders);
	}


	@Override
	@Transactional(readOnly=true)
	public Page<T> generalPageQuery(Integer page, Integer pageSize, List<SearchOperator> searchs,
			LinkedHashMap<String, Direction> orders) {
		return baseDao.generalPageQuery(page, pageSize, searchs, orders);
	}

}
