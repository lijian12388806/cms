<!DOCTYPE html>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mis" uri="http://mis_functions" %>
<%@ taglib prefix="final" uri="http://final_functions" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>学段列表</title>
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
		 selectPanel("属性配置");
		});
		
		//操作按钮
		function formatMgr(value, row) {
	    	return "<a href='javascript:void(0)' onclick='editLearning("+row.id+")' >编辑</a>| "+
	    	"<a href='javascript:void(0)' onclick='deleteLearning("+row.id+")'>删除</a>";
	    }
		//添加学段
		function newLearning() {
	    	$('#dlg-save').dialog('open').dialog('setTitle','添加学段');
	    	$("#courseId").val("");
	    }
		//保存学段
		function saveLearnStages() {
	    	$('#fm-save').form('submit',{
    			url: "${pageContext.request.contextPath}/courseAttr/saveOrUpdateLearnStages",
    			onSubmit: function(){
    				return $(this).form('validate');
    			},
    			success: function(result){
    				var result = eval('('+result+')');
    				if (result.success){
    					$('#dlg-save').dialog('close');
    					$('#dg').datagrid('reload');
    					topCenter("SUCCESS",result.msg);
    				} else {
    					topCenter("Error",result.msg);
    				}
    			}
    		});
	    }
		//修改学段
		function editLearning(id) {
	    	$('#fm-save').form('clear');
	    	var rows = $('#dg').datagrid("getRows");
			for(var i=0; i<rows.length; i++){
				if(rows[i].id ==id){
					var row = rows[i];
					$("#courseId").val(row.id);
					$("#name").textbox("setValue",row.name);
					break;
				}
			}
			$('#dlg-save').dialog('open').dialog('setTitle','修改学段 ');
	    }
		//删除学段
		function deleteLearning(id) {
			$.messager.confirm('Confirm','确定删除此学段?',function(r){
				if (r){
					$.post('${pageContext.request.contextPath}/courseAttr/delete',{id:id},function(result){
						if (result.success){
							$('#dg').datagrid('reload');	// reload the user data
						} else if(result.status==1) {
							$.messager.show({	// show error message
								title: 'Error',
								msg: result.msg,
								showType:'slide',
								style:{
									right:'',
									bottom:''
								}
							});
						}
					},'json');
				}
			});
	    }
    </script>
</head>
<body class="easyui-layout">
    <jsp:include page="../common/commonui.jsp" flush="true"/> 
    <div data-options="region:'center'">
    <table id="dg" class="easyui-datagrid" title="学段列表" class="easyui-datagrid" style="width:100%;height:100%"
			url="${pageContext.request.contextPath}/courseAttr/learninglist"
			toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="false" >
		<thead>
			<tr>
	            <th field="name" width="15%" align='center'>学段名称</th>
	            <th field="aa" formatter="formatMgr"  width="15%" align='center'>操作</th>
			</tr>
		</thead>
	</table>
	</div>
	
	<div id="toolbar" style="padding:2px 5px;">
         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"  onclick="newLearning()" >添加</a>
	</div>
	<!-- 添加表单 -->
    <div id="dlg-save" class="easyui-dialog" style="width:auto;height:auto;padding:20px 20px"
            closed="true" buttons="#dlg-buttons">
        <form id="fm-save" method="post" novalidate  enctype="multipart/form-data" >
        	<input type="hidden" id="courseId" name="id"/>
            <div class="fitem">
                <label>学段名称:</label>
                <input id="name" name="name"  class="easyui-textbox" data-options="required:true">
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveLearnStages()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-save').dialog('close')" style="width:90px">关闭</a>
    </div>
    
    </body>
</html>