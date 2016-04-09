<!DOCTYPE html>
<%@page import="org.springframework.security.authentication.CredentialsExpiredException"%>
<%@page import="org.springframework.security.authentication.LockedException"%>
<%@page import="org.springframework.security.authentication.DisabledException"%>
<%@page import="org.springframework.security.authentication.AuthenticationCredentialsNotFoundException"%>
<%@page import="org.springframework.security.authentication.BadCredentialsException"%>
<%@page import="org.springframework.security.core.AuthenticationException"%>
<%@page import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter" %>
<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="${pageContext.request.contextPath}/icons/favicon.ico">

    <title>多屏互动后台管理系统</title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/css/signin.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="${pageContext.request.contextPath}/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
    <body>
<div class="container">
<form class="form-signin" action="${pageContext.request.contextPath}/admin/security-login" method="post">
     <h2 class="form-signin-heading">用户登录</h2>
	 <% 
	 	Object exception = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
	 	if(exception instanceof AuthenticationCredentialsNotFoundException) {
	 %>
	 <h5><font color="red">用户不存在!</font></h5><br/>
	 <%} else if(exception instanceof BadCredentialsException) {%>
	 <h5><font color="red">密码错误!</font></h5><br/>
	 <%} else if(exception instanceof DisabledException) {%>
	 <h5><font color="red">账户不可用,请联系管理员!</font></h5><br/>
	 <%} else if(exception instanceof LockedException) {%>
	 <h5><font color="red">账户被锁,请联系管理员!</font></h5><br/>
	 <%} else if(exception instanceof CredentialsExpiredException) {%>
	 <h5><font color="red">密码已过期!</font></h5><br/>
	 <%} %>
     <input type="text" class="form-control" name="username" id="j_username" placeholder="用户名" required autofocus>
        <input type="password" class="form-control"  name="password" id="j_password" placeholder="密码" required>
        <div class="checkbox">
          <label>
            <input id="_spring_security_remember_me" name="_spring_security_remember_me" type="checkbox" checked="checked" value="true"> 在本机保存我的登录信息.
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">点击登录</button>

</form>
    </div> <!-- /container -->

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="${pageContext.request.contextPath}/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>