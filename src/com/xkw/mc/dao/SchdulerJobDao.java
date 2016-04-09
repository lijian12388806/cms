package com.xkw.mc.dao;

import com.xkw.mc.entity.SchdulerJob;

public interface SchdulerJobDao extends BaseDao<SchdulerJob, Integer> {

	/**
	 * 根据任务名以及任务组, 删除一个任务记录
	 * @param jobGroup
	 * @param jobName
	 * @return
	 */
	public int deleteByGroupAndName(String jobGroup, String jobName);

	
	/**
	 * 删除指定属性的所有记录
	 * @param key
	 * @param value
	 */
	public void deleteByProperty(String key, String value);

}
