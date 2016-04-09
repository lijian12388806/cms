package com.xkw.mc.dao;

import com.xkw.mc.entity.Resource;

public interface ResourceDao extends BaseDao<Resource, Integer> {

	public void deleteByAuthId(Integer id);

}
