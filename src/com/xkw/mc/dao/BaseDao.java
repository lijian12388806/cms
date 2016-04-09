package com.xkw.mc.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xkw.utils.Direction;
import com.xkw.utils.Page;
import com.xkw.utils.SearchOperator;


public interface BaseDao<T, ID extends Serializable> {
	
	/**
	 * 保存或修改,根据传入的实体对象是否有id来决定保存或是更新
	 * @param T
	 */
	public void saveOrUpdate(T t);
	
	/**
	 * 保存一个对象
	 * @param t
	 */
	public void save(T t);
	
	/**
	 * 修改一个对象
	 * @param t
	 */
	public void update(T t);
	
	/**
	 * 根据id删除对应对象
	 * @param id
	 */
	public void deleteById(ID id);
	
	/**
	 * 删除一个实体对象
	 * @param T
	 */
	public void delete(T t);
	
	/**
	 * 根据id获取实体
	 * @param id
	 * @return
	 */
	public T findOne(ID id);
	
	/**
	 * 获取所有实体
	 * @return
	 */
	public List<T> findAll();
	
	/**
	 * 根据对象的属性查询一个对象,此属性值必须是唯一的,所有只返回一个唯一的对象
	 * @param propertyName 属性值
	 * @param propertyValue 属性名
	 * @return
	 */
	public T getByProperty(String propertyName, Object propertyValue);
	
	
	/**
	 * 根据对象的属性查询满足该属性值的所有对象,
	 * 此属性值不是唯一的,所有返回的是对象的List集合
	 * @param propertyName 属性名
	 * @param propertyValue 属性值
	 * @return
	 */
	public List<T> getsByProperty(String propertyName, Object propertyValue);
	
	
	/**
	 * 根据对象的属性查询满足该属性值的所有对象,
	 * 此属性值不是唯一的,所有返回的是对象的List集合
	 * @param propertyName 属性名
	 * @param propertyValue 属性值
	 * @param orders 用于排序的属性以及排序方式的集合,无需排序则传入Null
	 * @return
	 */
	public List<T> getsByProperty(String propertyName, Object propertyValue, LinkedHashMap<String, Direction> orders);
	
	
	/**
	 * 分页查询
	 * @param page  页码,从0开始取
	 * @param pageSize  每页显示多少条记录, 即从数据库中取多少条记录 
	 * @param orders  排序条件,集合中的key需要与对象的属性名保持一致
	 * @param propertyName  条件查询的属性名
	 * @param propertyValue  条件查询属性的属性值
	 * @return
	 */
	public Page<T> getPage(Integer page, Integer pageSize, Map<String, Object> queryMap, LinkedHashMap<String, Direction> orders);
	
	
	/**
	 * 根据id更新数据库表中对应字段的值
	 * @param id 需要修改的对象/数据库记录的id
	 * @param propertyName  属性名,与对象的属性名称保持一致
	 * @param propertyValue  该属性需要修改的值
	 * @return
	 */
	public int updatePropertyById(ID id, String propertyName, Object propertyValue);
	
	
	/**
	 * 根据id及属性名获取属性值
	 * @param id
	 * @param propertyName 属性名
	 * @return
	 */
	public <E> E getPropertyValueById(ID id, String propertyName);
	
	
	/**
	 * 删除指定类的所有持久化对象从二级缓存中删除, 释放其占用的资源除
	 * @param TClass
	 */
	public void evict(Class<?> TClass);
	
	
	/**
	 * 删除指定类的指定id对应的实体对象从二级缓存中删除, 释放其占用的资源除
	 * @param TClass
	 */
	public void evict(Class<?> TClass, Serializable id);
	
	
	/**
	 * 删除所有在二级缓存中的实体对象, 释放资源
	 */
	public void evictAll();
	
	
	/**
	 * 删除指定的集合缓存
	 * @param str
	 */
	public void evictCollection(String str);

	
	/**
	 * 模糊查询 
	 * @param key
	 * @param properties
	 */
	public List<T> like(String key, String property);
	
	/**
	 * 模糊查询 
	 * @param key
	 * @param properties
	 */
	public List<T> like(String key, String property,Long id);
	
	
	/**
	 * 根据多个id获取对应的对象集合
	 * @param ids
	 * @return
	 */
	public List<T> getByIds(Set<ID> ids, LinkedHashMap<String, Direction> orders);

	
	/**
	 * 通过某个属性获取另一个属性的集合
	 * @param propertyName 属性名
	 * @param propertyValue	属性值
	 * @param destProperty	要获取属性的属性名
	 * @return
	 */
	public <E> List<E> getsByProperty(String propertyName, Object propertyValue, String destProperty);

	
	/**
	 * 根据id对指定的数量型的字段进行自增或自减
	 * @param id
	 * @param propertyName	属性名,必须是数量型的字段, 即字段类型库int,long,float等
	 * @param incr	增量或减量(加减号)
	 * @return
	 */
	public int increment(ID id, String propertyName, Number incr);

	
	/**
	 * 通过某个属性获取另一个属性的集合
	 * @param propertyName
	 * @param propertyValue
	 * @param destProperty
	 * @return
	 */
	public <E> List<E> getsByPropertys(String propertyName, Set<?> propertyValue,
			String destProperty);

	
	/**
	 * 通过某个确定唯一的属性修改另外属性值
	 * @param key
	 * @param value
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public int updatePropertyByProperty(String key, Object value, String propertyName,
			Object propertyValue);

	
	/**
	 * 通用查询方法
	 * @param searchs
	 * @param orders
	 * @return
	 */
	public List<T> generalQuery(List<SearchOperator> searchs, LinkedHashMap<String, Direction> orders);
	
	
	/**
	 * 通用的页查询方法
	 * @param page
	 * @param pageSize
	 * @param searchs
	 * @param orders
	 * @return
	 */
	public Page<T> generalPageQuery(Integer page, Integer pageSize, List<SearchOperator> searchs,
			LinkedHashMap<String, Direction> orders);

}
