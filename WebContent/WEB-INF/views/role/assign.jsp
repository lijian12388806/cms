<!DOCTYPE html>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mis" uri="http://mis_functions" %>
<%@ taglib prefix="final" uri="http://final_functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>用户权限分配</title>
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
        p {line-height: 20px;}
    </style>
    <script type="text/javascript">
    $(function() {
		 $("#selectAll").click(function () {//全选  
             $("#allcheck :checkbox").prop("checked", true);
         });  

         $("#unSelect").click(function () {//全不选  
             $("#allcheck :checkbox").prop("checked", false);  
         });  

         $("#reverse").click(function () {//反选  
             $("#allcheck :checkbox").each(function () {  
                 $(this).prop("checked", !$(this).prop("checked"));  
             });  
         });  
         selectPanel("系统管理");
	 });
    
    </script>
</head>
<body class="easyui-layout">

    <jsp:include page="../common/commonui.jsp" flush="true"/> 
    <div data-options="region:'center'">
	    <div class="easyui-panel" title="用户权限分配" style="width:100%">
			<div style="padding:10px 60px 20px 60px">
		    	<div style="font-size: 16px;font-weight: bold;padding: 10px 30px;">当前角色: <span style="color: red;font-size: 16px;font-weight: bold;">${role.displayName}</span></div>
				<form:form id="fm" action="${pageContext.request.contextPath}/role/updateAuthority" method="post">
				<input type="button" value="全选" id="selectAll"/> <input type="button" value="全不选" id="unSelect"/> <input type="button" value="反选" id="reverse"/>
					<input type="hidden" name="id" value="${role.id}"/>
			        <div style="margin-bottom:20px" id="allcheck">
						<c:forEach items="${auths}" var="auth" >
							<p style="color: #4F94CD;font-size: 16px;font-weight: bold;"><input name="userAuthIds" class="authClass parent" id="${auth.id}" value="${auth.id}" type="checkbox" /><label style="font-size: 16px" for="${auth.id}">${auth.displayName}</label></p>
							<p>
								<c:forEach items="${auth.subAuthorities}" var="subAuth">
									<input name="userAuthIds" class="authClass child child${auth.id}" pid="${auth.id}" id="${subAuth.id}" value="${subAuth.id}" type="checkbox" /><label for="${subAuth.id}">${subAuth.displayName}</label>&emsp;&emsp;
								</c:forEach>
							</p>
						</c:forEach>
					</div>
					<div>
						<a href="javascript:void(0)" onclick="submitForm()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" style="padding:5px 0px;width:30%;">
							<span style="font-size:14px;" id="submit">确定修改</span>
						</a>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	function submitForm() {
		var url = "${pageContext.request.contextPath}/role/updateAuthority";
		$('#fm').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.success) {
					topCenter("SUCCESS", ""+result.msg+", 正在跳转中");
					setTimeout(function() {
						window.location.href = "${pageContext.request.contextPath}/role/tolist";
					}, 2000);
				} else {
					topCenter("Error", result.msg);
				}
			}
		});
	}
	
	//input[checkbox]的回显
	$(function() {
		var arr = "${userAuths}";
		$(".authClass").each(function(i, e) {
			var id = ","+e.id+",";
			if(arr.indexOf(id)!=-1) {
				$(e).attr("checked", true);
			}
		});
		
		$(".parent").click(function() {
			var id = $(this).attr("id");
			if($(this).prop("checked")) {
				$("input[pid="+id+"]").prop("checked", true);
			} else {
				$("input[pid="+id+"]").prop("checked", false);
			}
		});
		
		$(".child").click(function() {
			var pid = $(this).attr("pid");
			if($(this).prop("checked")) {
				$("#"+pid).prop("checked", true);
			} else {
				$("#"+pid).prop("checked", false);
				$(".child").each(function(i, e) {
					if($(this).prop("checked")) {
						$("#"+pid).prop("checked", true);
					}
				});
			}
		});
	});
	
</script>
</html>