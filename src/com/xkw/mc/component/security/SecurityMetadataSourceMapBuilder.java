/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-09
 */
package com.xkw.mc.component.security;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 构造资源与权限之间对应关系的接口
 * @version 1.0
 * @author LiaoGang
 *
 */
public interface SecurityMetadataSourceMapBuilder {

	LinkedHashMap<String, List<String>> buildSrcMap();
}
