package com.xkw.mc.dao;

import com.xkw.mc.entity.Source;
import com.xkw.utils.Page;

public interface SourceDao extends BaseDao<Source, Integer>{

	Page<Source> getSourceList(Integer page, Integer rows, Long sDate, Long eDate);


}
