<!DOCTYPE html>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mis" uri="http://mis_functions" %>
<%@ taglib prefix="final" uri="http://final_functions" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>角色列表</title>
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
    <table id="dg" class="easyui-datagrid" title="角色列表" class="easyui-datagrid" style="width:100%;height:100%"
			url="${pageContext.request.contextPath}/role/list"
			toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="true" >
		<thead>
			<tr>
				<th field="id" width="5%" align='center'>ID</th>
	            <th field="displayName" width="10%" align='center'>角色名</th>
	            <th field="enabled" formatter="formatEnabled" width="10%" align='center'>是否可用</th>
	            <th field="Roles" formatter="formatRole" width="5%" align='center'>分配权限</th>
			</tr>
		</thead>
	</table>
	</div>
	<!-- 添加/修改表单 -->
    <div id="dlg" class="easyui-dialog" style="width:420px;height:auto;padding:20px 20px"
            closed="true" buttons="#dlg-buttons">
        <div class="ftitle"></div>
        <form id="fm" method="post" validate  enctype="multipart/form-data" >
        	<input type="hidden" id="id" name="id">
            <div class="fitem">
                <label>角色名称:</label>
                <input id="displayName" name="displayName"  class="easyui-textbox" data-options="required:true">
            </div>
            <div class="fitem">
                <label>是否可用: </label>
                <select id="enabled" name="enabled" class="easyui-combobox" data-options="required:true" > 
               		<option value="1">正常</option>
               		<option value="0">不可用</option>
           	    </select><br/><br/>
            </div>
        	    <font color="red" size="12px">(设为不可用后,会导致拥有该角色的用户无法正常使用)</font>
        </form>
    </div>
    <div id="dlg-buttons" align="center">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveRole()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">关闭</a>
    </div>
	
	<!-- 功能模块 -->
	 <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"  onclick="newRole()" >添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editRole()">修改</a>
    </div>
    <script type="text/javascript">
	    function formatEnabled(value) {
	    	if(value) {
	    		return "正常";
	    	}
	    	return "不可用";
	    }
	    
    	function formatRole(value, row) {
	    	return "<a href='${pageContext.request.contextPath}/role/toAssign?id="+row.id+"'>分配权限</a>";
	    }
	    
	    function newRole() {
	    	$('#dlg').dialog('open').dialog('setTitle','添加角色');
			$('#fm').form('clear');
	    }
	    
	    function saveRole() {
	    	$('#fm').form('submit',{
    			url: "${pageContext.request.contextPath}/role/saveOrUpdate",
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
	    
	    function editRole() {
	    	var row = $('#dg').datagrid('getSelected');
			if (row){
				$('#dlg').dialog('open').dialog('setTitle','修改角色');
				$('#fm').form('load',row);
			}else{
				topCenter('Error',"请选择要更新的选项!!!");
			}
	    }
    </script>
    </body>
</html>