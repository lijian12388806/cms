<!DOCTYPE html>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mis" uri="http://mis_functions" %>
<%@ taglib prefix="final" uri="http://final_functions" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>权限列表</title>
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
		 selectPanel("系统管理");
		});
    </script>
</head>
<body class="easyui-layout">
    <jsp:include page="../common/commonui.jsp" flush="true"/> 
    <div data-options="region:'center'">
    <table id="dg" class="easyui-datagrid" title="权限列表" class="easyui-datagrid" style="width:100%;height:100%"
			url="${pageContext.request.contextPath}/authority/list"
			toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="true" >
		<thead>
			<tr>
				<th field="id" width="5%" align='center'>ID</th>
	            <th field="displayName" width="10%" align='center'>权限显示名称</th>
	            <th field="roleName" width="15%" align='center'>权限名称</th>
	            <th field="parentAuthoritys" formatter="formatParent" width="10%" align='center'>父权限</th>
	            <th field="urls" formatter="formatUrls" width="25%" align='center'>拦截的url</th>
			</tr>
		</thead>
	</table>
	</div>
	<!-- 添加/修改表单 -->
    <div id="dlg" class="easyui-dialog" style="width:450px;height:auto;padding:20px 20px"
            closed="true" buttons="#dlg-buttons">
        <div class="ftitle"></div>
        <form id="fm" method="post" validate  enctype="multipart/form-data" >
        	<input type="hidden" id="id" name="id">
            <div class="fitem">
                <label>权限显示名称:</label>
                <input id="displayName" name="displayName"  class="easyui-textbox" data-options="required:true">
            </div>
            <div class="fitem">
                <label>权限名称:</label>
                <input id="roleName" name="roleName"  class="easyui-textbox" data-options="required:true"><br>
                <font color="red">(此权限名称用于加入SpringSecurity中, 必须以ROLE_开头)</font>
            </div>
            <div class="fitem">
                <label>拦截的url:</label>
                <input id="urls" name="urls"  class="easyui-textbox"><br>
                <font color="red">(多个url,以英文的逗号隔开)</font>
            </div>
            <div class="fitem">
                <label>父权限: </label>
                <select id="parentAuthority" name="parentAuthority.id" class="easyui-combobox" style=""> 
           	    	<c:forEach items="${rootAuths}" var="auth">
           	    		<option value="${auth.id}">${auth.displayName}</option>
           	    	</c:forEach>
           	    </select><font color="red">(非必选项)</font><br/>
           	    <font color="red">(不选时,将作为顶级权限,所拦截的url不会加入到权限框架中)</font>
            </div>
        </form>
    </div>
    <div id="dlg-buttons" align="center">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveAuthority()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">关闭</a>
    </div>
	
	<!-- 功能模块 -->
	 <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"  onclick="newAuthority()" >添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editAuthority()">修改</a>
    </div>
    <script type="text/javascript">
    	function getUrls(row) {
    		var resources = row.resources;
    		var urls = "";
    		if(resources) {
				for(index in resources) {
					var resource = resources[index];
					urls += (resource.url + ", ");
				}
				var index = urls.lastIndexOf(",");
				if(index != -1) {
					urls = urls.substring(0, urls.length-2);
				}
    		}
    		return urls;
    	}
    	
    	function formatParent(value, row) {
    		var parent = row.parentAuthority;
    		if(parent) {
    			return parent.displayName;
    		}
    		return "";
    	}
    	
    	function formatUrls(value, row) {
    		return getUrls(row);
    	}

    	function newAuthority() {
	    	$('#dlg').dialog('open').dialog('setTitle','添加权限');
			$('#fm').form('clear');
	    }
	    
	    function saveAuthority() {
	    	var row = $('#dg').datagrid('getSelected');
			if (row){
				var pid = $('#fm').find("#parentAuthority").combobox("getValue");
				var currentId = $('#fm').find("#id").val();
				if(pid==currentId) {
					topCenter("Error", "不能选择自己作为父权限");
					return false;
				}
			}
	    	$('#fm').form('submit',{
    			url: "${pageContext.request.contextPath}/authority/saveOrUpdate",
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
	    
	    function editAuthority() {
	    	var row = $('#dg').datagrid('getSelected');
			if (row){
				$('#fm').form('clear');
				$('#dlg').dialog('open').dialog('setTitle','修改权限');
				$('#fm').form('load',row);
				var parentAuthority = row.parentAuthority;
				if(parentAuthority) {
					$('#fm').find("#parentAuthority").combobox("setValue", parentAuthority.id);
				}
				$('#fm').find("#urls").textbox("setValue", getUrls(row));
			}else{
				topCenter('Error',"请选择要更新的选项!!!");
			}
	    }
    </script>
    </body>
</html>