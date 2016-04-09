package com.xkw.mc.service;

import java.io.IOException;

import org.csource.common.MyException;

import com.xkw.mc.entity.Source;
import com.xkw.utils.Page;

public interface SourceService extends BaseService<Source, Integer>{

	Page<Source> getSourceList(Integer page, Integer rows, Long sDate, Long eDate);

	/**
	 * 删除指定id对应的素材
	 * @param id
	 */
	public void deleteSource(Integer id) throws IOException, MyException;


}
