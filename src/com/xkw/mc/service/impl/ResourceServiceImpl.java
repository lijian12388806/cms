package com.xkw.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkw.mc.dao.SourceDao;
import com.xkw.mc.entity.Resource;
import com.xkw.mc.service.ResourceService;
import com.xkw.mc.service.SourceService;

@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource, Integer> implements ResourceService {

	@Autowired
	private SourceDao sourceDao;
	
}
