package com.xkw.mc.service.impl;

import java.io.IOException;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkw.mc.dao.SourceDao;
import com.xkw.mc.entity.Source;
import com.xkw.mc.service.SourceService;
import com.xkw.utils.FastDfsUtils;
import com.xkw.utils.Page;
@Service
public class SourceServiceImpl extends BaseServiceImpl<Source, Integer> implements SourceService{

	@Autowired
	private SourceDao sourceDao;
	/**
	 * 素材列表数据
	 */
	@Override
	public Page<Source> getSourceList(Integer page, Integer rows, Long sDate, Long eDate) {
		// TODO Auto-generated method stub
		return sourceDao.getSourceList(page,rows,sDate,eDate);
	}
	
	
	@Override
	public void deleteSource(Integer id) throws IOException, MyException {
		Source source = sourceDao.findOne(id);
		if(source!=null) {
			FastDfsUtils.delete(source.getPath());
		}
		sourceDao.delete(source);
	}
	

}
