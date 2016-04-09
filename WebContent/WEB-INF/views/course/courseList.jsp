<!DOCTYPE html>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mis" uri="http://mis_functions" %>
<%@ taglib prefix="final" uri="http://final_functions" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>课件管理</title>
    <link rel="icon" href="${pageContext.request.contextPath}/icons/favicon.ico">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/easyui.css" >
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/color.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/demo.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
    <style>
        #fm{
            margin:0;
            padding:10px 30px;
        }
        .ftitle{
            font-size:14px;
            font-weight:bold;
            padding:5px 0;
            margin-bottom:10px;
            border-bottom:1px solid #ccc;
        }
        .fitem{
            margin-bottom:15px;
        }
        .fitem label{
            display:inline-block;
            width:80px;
        }
        .fitem input{
            width:160px;
        }
    </style>
    <script type="text/javascript">
		$(function() {
		 selectPanel("用户课件列表");
		});
		
		
		//查询按钮调用方法
		function doSearch(){
			$('#dg').datagrid('load',{
				learnStages :$('#learnStages').combobox("getValue"),
				subject: $('#subject').combobox("getValue"),
				classes: $('#classes').combobox("getValue"),
				version: $('#version').combobox("getValue"),
				startDate: $('#startDate').datebox('getValue'),
				endDate: $('#endDate').datebox('getValue'),
			});
		}
		
		//操作按钮
		function formatMgr(value, row) {
// 			var status = row.status;
// 	    	if(status == 0){
// 	    		return "<a href='javascript:void(0)' onclick='toSource("+row.id+")'>查看素材并审核</a>";
// 	    	}else if(status == 1){
// 	    		return "<a href='javascript:void(0)' onclick='toSource("+row.id+")'>重新审核</a>";
// 	    	}else if(status == 2){
// 	    		return "<a href='javascript:void(0)' onclick='toSource("+row.id+")'>重新审核</a>";
// 	    	}
			return "<a href='javascript:void(0)' onclick='toSource("+row.id+")'>查看素材</a>";
		}
		//状态栏
// 		function formatStatus(value){
// 			return "<span>"+convert(value)+" </span>";
// 		}
// 		function convert(status) {
// 			 switch(status){
// 			 case 0:
// 				 return "未审核";
// 			 case 1:
// 				 return "审核不通过";	 
// 			 case 2:
// 				 return "审核通过";	 
// 		     default:
// 		    	 return "";
// 			 }
// 		 }
		//跳转到审核页面
		function toSource(id) {
			window.location.href="${pageContext.request.contextPath}/course/toSource?id="+id;
		}
		
		//学段的级联操作
		function change(){
			alert("1111");
			var learnStages = $("#learnStages").val();
			alert(learnStages);
		}
		
    </script>
</head>
<body class="easyui-layout">
    <jsp:include page="../common/commonui.jsp" flush="true"/> 
    <div data-options="region:'center'">
    <table id="dg" class="easyui-datagrid" title="用户课件列表" class="easyui-datagrid" style="width:100%;height:100%"
			url="${pageContext.request.contextPath}/course/list"
			toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="false" >
		<thead>
			<tr>
	            <th field="name" width="15%" align='center'>课件名称</th>
	            <th field="learnStages" width="10%" align='center'>学段</th>
	            <th field="subject" width="10%" align='center'>科目</th>
	            <th field="version" width="10%" align='center'>版本</th>
	            <th field="classes" width="10%" align='center'>年级</th>
	            <th field="createTime" width="20%" formatter="dateformatter" align='center'>最后修改时间</th>
	            <th field="username" width="10%" align='center'>创建人</th>
	            <th field="aa" formatter="formatMgr"  width="10%" align='center'>操作</th>
			</tr>
		</thead>
	</table>
	</div>
	
	<div id="toolbar" style="padding:2px 5px;">
		<label>学段</label>
		<select id="learnStages" onchange='change(this)' name="learnStages" class="easyui-combobox" onchange="getValTwo()"  editable="false" panelHeight="auto" style="width:100px">  
		     <option value="">全部</option> 
		     <c:forEach items="${learnStages}" var="learnStages">
		           <option value="${learnStages.name}">${learnStages.name}</option> 
		     </c:forEach>
		 </select>
		 <label>学科</label>
		 <select id="subject" name="subject" class="easyui-combobox" onchange="getValTwo()"  editable="false" panelHeight="auto" style="width:100px">  
		     <option value="">全部</option>
		     <c:forEach items="${subject}" var="subject">
		           <option value="${subject.name}">${subject.name}</option> 
		     </c:forEach>
		 </select>
		 <label>年级</label>
		 <select id="classes" name="classes" class="easyui-combobox" panelHeight="auto" style="width:100px">  
		     <option value="">全部</option> 
		     <c:forEach items="${classes}" var="classes">
		           <option value="${classes.name}">${classes.name}</option> 
		     </c:forEach>
		 </select>
		 <label>版本</label>
		 <select id="version" name="version" class="easyui-combobox" onchange="getValTwo()"  editable="false" panelHeight="auto" style="width:100px">  
		     <option value="">全部</option> 
		     <c:forEach items="${version}" var="version">
		           <option value="${version.name}">${version.name}</option> 
		     </c:forEach>
		 </select>
              上传时间:<input id="startDate" name="startDate" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser"></input>-<input id="endDate" name="endDate" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser"></input>
		<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">搜索</a>
	</div>
	
    
    </body>
</html>