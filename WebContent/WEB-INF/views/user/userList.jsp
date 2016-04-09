<!DOCTYPE html>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mis" uri="http://mis_functions" %>
<%@ taglib prefix="final" uri="http://final_functions" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>管理员列表</title>
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
    <table id="dg" class="easyui-datagrid" title="管理员列表" class="easyui-datagrid" style="width:100%;height:100%"
			url="${pageContext.request.contextPath}/admin/list"
			toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="true" >
		<thead>
			<tr>
				<th field="id" width="5%" align='center'>ID</th>
	            <th field="username" width="10%" align='center'>账号</th>
	            <th field="realName" width="10%" align='center'>真实姓名</th>
	            <th field="email" width="10%" align='center'>联系邮箱</th>
	            <th field="cellphone" width="8%" align='center'>联系电话</th>
	            <th field="enabled" formatter="formatEnabled" width="8%" align='center'>是否可用</th>
	            <th field="pwd" formatter="formatPwd" width="5%" align='center'>修改密码</th>
	            <th field="Role" formatter="formatCurrentRole" width="8%" align='center'>当前角色</th>
			</tr>
		</thead>
	</table>
	</div>
	<!-- 添加修改表单 -->
    <div id="dlg" class="easyui-dialog" style="width:400px;height:auto;padding:20px 20px"
            closed="true" buttons="#dlg-buttons">
        <div class="ftitle">密码修改</div>
        <form id="fm" method="post" novalidate  enctype="multipart/form-data" >
        	<input type="hidden" id="id" name="id"/>
            <div class="fitem">
                <label>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:</label>
                <span id="usernameSpan"></span>
<!--                 <input id="username" name="username"  class="easyui-textbox"   data-options="readonly:true"> -->
            </div>
            <div class="fitem">
                <label>新密码:</label>
                <input id="password" name="password"  class="easyui-textbox"   data-options="required:true">
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="updatePwd()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">关闭</a>
    </div>
    
	<!-- 修改表单 -->
    <div id="dlg-user" class="easyui-dialog" style="width:400px;height:auto;padding:20px 20px"
            closed="true" buttons="#dlg-buttons-user">
        <div class="ftitle"></div>
        <form id="fm-user" method="post" validate  enctype="multipart/form-data" >
            <input type="hidden" id="id" name="id"/>
            <div class="fitem">
                <label>账号:</label>
                <input id="username" name="username" class="easyui-textbox" data-options="readonly:true" >(<span style="color: #ccc">用户名不可修改</span>)
            </div>
            <div class="fitem">
                <label>真实姓名:</label>
                <input id="realName" name="realName" class="easyui-textbox">
            </div>
            <div class="fitem">
                <label>联系邮箱:</label>
                <input id="email" name="email"  class="easyui-textbox" data-options="">
            </div>
            <div class="fitem">
                <label>联系手机:</label>
                <input id="cellphone" name="cellphone"  class="easyui-textbox" data-options="">
            </div>
            <div class="fitem">
                <label>状态: </label>
                <select id="enabled" name="enabled" class="easyui-combobox" data-options="required:true"> 
               		<option value="1">正常</option>
               		<option value="0">不可用</option>
           	    </select>
            </div>
            <div class="fitem">
                <label>选择角色: </label>
                <select id="role" name="role.id" class="easyui-combobox" style="width: 100px;" data-options="required:true" > 
               		<c:forEach items="${roles}" var="role">
	               		<option value="${role.id}">${role.displayName}</option>
               		</c:forEach>
           	    </select>
            </div>
        </form>
    </div>
    <div id="dlg-buttons-user" align="center">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="updateUser()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-user').dialog('close')" style="width:90px">关闭</a>
    </div>
	<!-- 添加表单 -->
    <div id="dlg-save" class="easyui-dialog" style="width:400px;height:auto;padding:20px 20px"
            closed="true" buttons="#dlg-buttons-user">
        <div class="ftitle"></div>
        <form id="fm-save" method="post" validate  enctype="multipart/form-data" >
            <div class="fitem">
                <label>账号:</label>
                <input id="username" name="username"  class="easyui-textbox" data-options="required:true">
            </div>
            <div class="fitem">
                <label>真实姓名:</label>
                <input id="realName" name="realName" class="easyui-textbox" data-options="required:true">
            </div>
            <div class="fitem">
                <label>密码:</label>
                <input id="password" name="password"  class="easyui-textbox" data-options="required:true"  >
            </div>
            <div class="fitem">
                <label>联系邮箱:</label>
                <input id="email" name="email"  class="easyui-textbox" data-options="">
            </div>
            <div class="fitem">
                <label>联系手机:</label>
                <input id="cellphone" name="cellphone"  class="easyui-textbox" data-options="">
            </div>
            <div class="fitem">
                <label>状态: </label>
                <select id="enabled" name="enabled" class="easyui-combobox" data-options="required:true" > 
               		<option value="1">正常</option>
               		<option value="0">不可用</option>
           	    </select>
            </div>
            <div class="fitem">
                <label>选择角色: </label>
                <select id="role" name="role.id" class="easyui-combobox" style="width: 100px;" data-options="required:true" > 
               		<c:forEach items="${roles}" var="role">
	               		<option value="${role.id}">${role.displayName}</option>
               		</c:forEach>
           	    </select>
            </div>
        </form>
    </div>
    <div id="dlg-buttons-user" align="center">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveUser()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-save').dialog('close')" style="width:90px">关闭</a>
    </div>
	
	<!-- 功能模块 -->
	 <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"  onclick="newUser()" >添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">修改</a>
    </div>
    <script type="text/javascript">
	    function formatEnabled(value) {
	    	if(value) {
	    		return "正常";
	    	}
	    	return "不可用";
	    }
	    
	    function formatPwd(value, row) {
	    	return "<a href='javascript:void(0)' onclick='editPwd("+row.id+",\""+row.username+"\")'>修改密码</a>";
	    }
	    
	    function editPwd(id, username) {
	    	$("#id").val(id);
	    	$("#usernameSpan").text(username);
	    	$('#dlg').dialog('open').dialog('setTitle','密码修改');
	    }
	    
	    function formatCurrentRole(value, row) {
	    	var role = row.role;
	    	if(role) {
	    		return role.displayName;
	    	}
	    	return "无";
	    }
	    
	    
	    function updatePwd() {
	    	$('#fm').form('submit',{
    			url: "${pageContext.request.contextPath}/admin/updatePwd",
    			onSubmit: function(){
    				return $(this).form('validate');
    			},
    			success: function(result){
    				try {
    					result = eval('('+result+')');
    				} catch (e) {
						window.location.href = "${pageContext.request.contextPath}";
    				}
    				if (result.success){
    					$('#dlg').dialog('close'); // close the dialog
    					$('#dg').datagrid('reload'); // reload the category data
    					topCenter("SUCCESS",result.msg);
    				} else {
    					topCenter("Error",result.msg);
    				}
    			}
    		});
	    }
	    
	    function newUser() {
	    	$('#dlg-save').dialog('open').dialog('setTitle','添加管理员账号');
			$('#fm-save').form('clear');
	    }
	    
	    function saveUser() {
	    	$('#fm-save').form('submit',{
    			url: "${pageContext.request.contextPath}/admin/saveOrUpdate",
    			onSubmit: function(){
    				return $(this).form('validate');
    			},
    			success: function(result){
    				try {
    					var result = eval('('+result+')');
    				} catch (e) {
						window.location.href = "${pageContext.request.contextPath}";
    				}
    				if (result.success){
    					$('#dlg-save').dialog('close'); // close the dialog
    					$('#dg').datagrid('reload'); // reload the category data
    					topCenter("SUCCESS",result.msg);
    				} else {
    					topCenter("Error",result.msg);
    				}
    			}
    		});
	    }
	    
	    function editUser() {
	    	var row = $('#dg').datagrid('getSelected');
			$('#fm-user').form('clear');
			if (row){
				$('#dlg-user').dialog('open').dialog('setTitle','管理员账号修改');
				$('#fm-user').form('load',row);
				var role = row.role;
				if(role) {
					$('#fm-user').find("#role").combobox("setValue", role.id);
				}
			}else{
				topCenter('Error',"请选择要更新的选项!!!");
			}
	    }
    	
	    function updateUser() {
	    	$('#fm-user').form('submit',{
    			url: "${pageContext.request.contextPath}/admin/saveOrUpdate",
    			onSubmit: function(){
    				return $(this).form('validate');
    			},
    			success: function(result){
    				try {
    					var result = eval('('+result+')');
    				} catch (e) {
						window.location.href = "${pageContext.request.contextPath}";
    				}
    				if (result.success){
    					$('#dlg-user').dialog('close'); // close the dialog
    					$('#dg').datagrid('reload'); // reload the category data
    					topCenter("SUCCESS",result.msg);
    				} else {
    					topCenter("Error",result.msg);
    				}
    			}
    		});
	    }
    </script>
    </body>
</html>