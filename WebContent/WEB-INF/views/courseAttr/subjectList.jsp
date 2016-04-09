<!DOCTYPE html>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mis" uri="http://mis_functions" %>
<%@ taglib prefix="final" uri="http://final_functions" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>学科列表</title>
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
	    	return "<a href='javascript:void(0)' onclick='editSubject("+row.id+")' >编辑</a>| "+
	    	"<a href='javascript:void(0)' onclick='deleteSubject("+row.id+")'>删除</a>";
	    }
		//添加学科
		function newSubject() {
			$(".pids").attr("checked", false);
	    	$("#courseId").val("");
	    	$('#dlg-save').dialog('open').dialog('setTitle','添加版本');
	    }
		//保存学科
		function saveSubject() {
	    	$('#fm-save').form('submit',{
    			url: "${pageContext.request.contextPath}/courseAttr/saveOrUpdateSubject",
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
    				window.location.reload();
    			}
    		});
	    }
		//修改学科
		function editSubject(id) {
			$(".pids").attr("checked", false);
	    	$('#fm-save').form('clear');
	    	var rows = $('#dg').datagrid("getRows");
			for(var i=0; i<rows.length; i++){
				if(rows[i].id ==id){
					var row = rows[i];
					var pids = row.pids;
					if(pids) {
						var pidArr = pids.split(",");
						for(var k=0; k<pidArr.length; k++) {
							var tempId = pidArr[k];
							$("input[cid="+tempId+"]").attr("checked",true);
						}
						$("#subjectId").val(row.id);
						$("#name").textbox("setValue",row.name);
					}
					break;
				}
			}
			$('#dlg-save').dialog('open').dialog('setTitle','修改学科 ');
	    }
		//删除学科
		function deleteSubject(id) {
			$.messager.confirm('Confirm','确定删除此学科?',function(r){
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
		//查询学科的id对应的学段名称
		var list = ${courseAttrJson};
		function getName(id) {
			if(list) {
				for(var k=0; k<list.length; k++) {
					var attr = list[k];
					if(attr.id==id) {
						return attr.name;
					}
				}
			}
		}
		//格式化相对应的pids
		function formatPids(value) {
			var pidNames = "";
			if(value) {
				var pidArr = value.split(",");
				for(var k=0; k<pidArr.length; k++) {
					var id = pidArr[k];
					pidNames += (","+getName(id));
				}
				pidNames = pidNames.substring(1);
			}
			return pidNames;
		}
		
		
    </script>
</head>
<body class="easyui-layout">
    <jsp:include page="../common/commonui.jsp" flush="true"/> 
    <div data-options="region:'center'">
    <table id="dg" class="easyui-datagrid" title="学科列表" class="easyui-datagrid" style="width:100%;height:100%"
			url="${pageContext.request.contextPath}/courseAttr/subjectlist"
			toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="false" >
		<thead>
			<tr>
	            <th field="name" width="15%" align='center'>学科名称</th>
	            <th field="pids" formatter="formatPids" width="15%" align='center'>相关学段</th>
	            <th field="aa" formatter="formatMgr"  width="15%" align='center'>操作</th>
			</tr>
		</thead>
	</table>
	</div>
	
	<div id="toolbar" style="padding:2px 5px;">
         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"  onclick="newSubject()" >添加</a>
	</div>
	<!-- 添加表单 -->
    <div id="dlg-save" class="easyui-dialog" style="width:auto;height:auto;padding:20px 20px"
            closed="true" buttons="#dlg-buttons">
        <form id="fm-save" method="post" novalidate  enctype="multipart/form-data" >
        	<input type="hidden" id="subjectId" name="id"/>
            <div class="fitem">
                <label>版本名称:</label>
                <input id="name" name="name"  class="easyui-textbox" data-options="required:true">
            </div>
            <div class="fitem">
                <label>相关学段:</label>
                <c:forEach items="${courseAttrs}" var="attr">
                	<input type="checkbox" cid="${attr.id}" name="pids" class="pids"  value="${attr.id}" id="${attr.id}" style="width:15px;margin-right:0px"/>
                	<label for="${attr.id}" style="width:30px">${attr.name}</label>
                </c:forEach>
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveSubject()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:window.location.reload();$('#dlg-save').dialog('close')" style="width:90px">关闭</a>
    </div>
    
    </body>
</html>