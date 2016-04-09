<!DOCTYPE html>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mis" uri="http://mis_functions" %>
<%@ taglib prefix="final" uri="http://final_functions" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>分类管理</title>
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
            margin-bottom:5px;
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
		 selectPanel("系统管理");
	 });
 
    </script>
</head>
<body class="easyui-layout">

    <jsp:include page="../common/commonui.jsp" flush="true"/> 
    
    <div data-options="region:'center'">
    <div class="easyui-panel" title="密码修改" style="width:100%">
		<div style="padding:10px 60px 20px 60px">
		<form id="fm" action="${pageContext.request.contextPath}/cms/admin/updateMyAccount" method="post">
			<input type="hidden" name="id" value="${user.id}"/>
			<table cellpadding="5">
       			 <tr>
            		<td>用户名:</td>
            		<td>
            			<input name="username" value="${user.username}" class="easyui-textbox"  readonly="true" data-options="required:true"/>
            		</td>
        		</tr>
        		 <tr>
            		<td>新密码:</td>
            		<td><input id="password" name="password" validType="length[4,32]" class="easyui-textbox" required="true" type="password" value=""/></td>
        		</tr>
        		<tr>
            		<td>确认密码:</td>
            		<td><input id="repassword" name="repassword" validType="same['password']"class="easyui-textbox" required="true" type="password" value=""/></td>
        		</tr>
        	</table>
			
			<div>
			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" style="padding:5px 0px;width:30%;">
				<span style="font-size:14px;" onclick="submitForm()" >确定修改</span>
			</a>
		</div>
		</form>
		</div>
		</div>
		</div>
		<script type="text/javascript">
			function submitForm() {
				islogin();
				var url = "${pageContext.request.contextPath}/cms/admin/updateMyAccount?id=${user.id}";
				$('#fm').form('submit', {
					url : url,
					onSubmit : function() {
						return $(this).form('validate');
					},
					success : function(result) {
						var result = eval('(' + result + ')');
						if (result.success) {
							topCenter("SUCCESS", result.msg);
						} else {
							topCenter("Error", result.msg);
						}
					}
				});
			}
		</script>
	</body>
</html>