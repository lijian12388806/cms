<!--
	对于app存储地址的分割, 
	app中一个字段如coverPath会存储多个封面地址,不同地址之间则使用,隔开,
	此tag即将多个封面地址按分隔符分割并存储在jsp域对象中以供使用
	而分割后的结果统一存储以变量key存储,jsp取数据时,也使用key对应的值获取
-->

<%@ tag pageEncoding="UTF-8"%>

<%@ attribute name="path" type="java.lang.String" required="true" %>
<%@ attribute name="separator" type="java.lang.String" required="true" %>
<%@ attribute name="key" type="java.lang.String" required="true" %>

<%
	if(path!=null && path.contains(separator)) {
		String[] paths = path.split(separator);
		request.setAttribute(key, paths);
	} else {
		request.setAttribute(key, path);
	}
%>
