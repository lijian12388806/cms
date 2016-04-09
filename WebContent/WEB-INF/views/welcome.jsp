<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎管理员</title>
 <link rel="icon" href="${pageContext.request.contextPath}/icons/favicon.ico">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/easyui.css" >
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/color.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/demo.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
    <style type="text/css">
    	div.box{width:300px;padding:20px;margin:0 auto;border:4px dashed #ccc;}
		div.box>span{color:#999;font-style:italic;}
    </style>
    <script type="text/javascript">
	    function showTime() {
	    	var curTime = new Date();
	    	$("#clock").html(curTime.toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay()));
	    	setTimeout("showTime()", 1000);
	    }
	    
	    $(function(){
	    	showTime();
	    });
    </script>
</head>
<body class="easyui-layout">
	<jsp:include page="common/commonui.jsp" flush="true"/> 
	<div data-options="region:'center'">
		<h1 style="font-size: 20px;text-align: center;">欢迎管理员 <span style="color: red;font-size: 20px;">${user.username}</span> 登陆后台管理系统</h1>
		<div class="box">
			<div class="content">
				<span>现在时间</span><br>
				<span id="clock"></span>
			</div>
		</div>
	</div>
</body>
</html>