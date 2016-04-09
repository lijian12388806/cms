<%-- <%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"  isELIgnored="false" %> --%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> --%>

<%@page import="com.xkw.utils.Const"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.GrantedAuthority" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="java.util.Collection" %>
<%@ taglib prefix='security' uri='http://www.springframework.org/security/tags' %>
<%
	request.setAttribute("ctx", request.getContextPath()); 
	request.setAttribute("BASE_URL", Const.BASE_URL); 
%>
<script type="text/javascript">
	window.onload = function(){
		var objLoading = document.getElementById("pageLoad");
		objLoading.style.display = "none";
	}
</script>
<div id="pageLoad" style="position: fixed;margin: 0px;margin-top: 0px;margin-left: 0px;width: 100%;height: 100%;z-index: 9999;background-color: white;text-align: center;font-size: 30px;padding-top: 100px;">
	加载中,请稍候...
</div>

<div data-options="region:'north',border:false"
	style="height: 62px; background: #B3DFDA; padding: 8px">
	<table width="100%" style="border: 0;">
		<tr>
			<td width="70%">
				<h1 style="color: red;font-size: 16px;">多屏互动后台管理系统</h1>
			</td>
			<td>
				<div align="right">
			            <span>当前登录用户: ${user.username}</span>
			            <a href="${pageContext.request.contextPath}/admin/security-logout">退出</a>
			    </div>
			</td>
		</tr>
	</table>
</div>
<div data-options="region:'west',split:true,title:'导航菜单'" style="width: 180px;">
	<div id="menu" class="easyui-accordion" data-options="border:false">
		<div title="内容管理" style="height:auto;">
			<div class="easyui-panel" style="width:172px;">
				<div class="easyui-menu" data-options="inline:true" style="width:100%">
					<div onclick="javascript:window.location.href='${pageContext.request.contextPath}/source/tolist'" >
						<span>素材管理</span>
					</div>
				</div>
				<div class="easyui-menu" data-options="inline:true" style="width:100%">
					<div onclick="javascript:window.location.href='${pageContext.request.contextPath}/courseAttr/tolist'" >
						<span>课件管理</span>
					</div>
				</div>
			</div>
		</div>
		<div title="属性配置" style="height:auto;">
			<div class="easyui-panel" style="width:172px;">
				<div class="easyui-menu" data-options="inline:true" style="width:100%">
					<div onclick="javascript:window.location.href='${pageContext.request.contextPath}/courseAttr/tolearninglist'" >
						<span>学段配置</span>
					</div>
				</div>
				<div class="easyui-menu" data-options="inline:true" style="width:100%">
					<div onclick="javascript:window.location.href='${pageContext.request.contextPath}/courseAttr/tosubjectlist'" >
						<span>学科配置</span>
					</div>
				</div>
				<div class="easyui-menu" data-options="inline:true" style="width:100%">
					<div onclick="javascript:window.location.href='${pageContext.request.contextPath}/courseAttr/toclasslist'" >
						<span>年级配置</span>
					</div>
				</div>
				<div class="easyui-menu" data-options="inline:true" style="width:100%">
					<div onclick="javascript:window.location.href='${pageContext.request.contextPath}/courseAttr/toversionlist'" >
						<span>版本配置</span>
					</div>
				</div>
			</div>
		</div>
		
		<security:authorize ifAnyGranted="ROLE_ADMIN_MGR,ROLE_ROLE_MGR,ROLE_AUTH_MGR,ROLE_ADMIN_MY_ACCOUNT">
			<div title="系统管理" style="padding: 0px;height:auto;">
				<div class="easyui-panel" style="width:172px;">
					<security:authorize ifAnyGranted="ROLE_ADMIN_MGR">
						<div class="easyui-menu" data-options="inline:true" style="width:100%">
							<div  onclick="javascript:window.location.href='${pageContext.request.contextPath}/admin/tolist'">
								<span>管理员管理</span>
							</div>
						</div>
					</security:authorize>
					<security:authorize ifAnyGranted="ROLE_ROLE_MGR">
						<div class="easyui-menu" data-options="inline:true" style="width:100%">
							<div  onclick="javascript:window.location.href='${pageContext.request.contextPath}/role/tolist'">
								<span>角色管理</span>
							</div>
						</div>
					</security:authorize>
					<security:authorize ifAnyGranted="ROLE_AUTH_MGR">
						<div class="easyui-menu" data-options="inline:true" style="width:100%">
							<div  onclick="javascript:window.location.href='${pageContext.request.contextPath}/authority/tolist'">
								<span>权限管理</span>
							</div>
						</div>
					</security:authorize>
					<security:authorize ifAnyGranted="ROLE_ADMIN_MY_ACCOUNT">
						<div class="easyui-menu" data-options="inline:true" style="width:100%">
							<div onclick="javascript:window.location.href='${pageContext.request.contextPath}/admin/account'">
								<span>我的账户</span>
							</div>
						</div>
					</security:authorize>
				</div>
			</div>
		</security:authorize>
	</div>
</div>
<div data-options="region:'south',border:false" style="height:25px;background:#A9FACD;padding:5px;"><center>多屏互动后台管理系统</center></div>
<script type="text/javascript">
var NowDate = new Date();
jQuery(document).ready(function () {
    window.setInterval("NowTimeView()", 1000);
    jQuery("#Span_Date").text(NowDate.toLocaleDateString());
    switch (NowDate.getDay()) {
        case 0:
            jQuery("#Span_Week").text("星期天");
            break;
        case 1:
            jQuery("#Span_Week").text("星期一");
            break;
        case 2:
            jQuery("#Span_Week").text("星期二");
            break;
        case 3:
            jQuery("#Span_Week").text("星期三");
            break;
        case 4:
            jQuery("#Span_Week").text("星期四");
            break;
        case 5:
            jQuery("#Span_Week").text("星期五");
            break;
        case 6:
            jQuery("#Span_Week").text("星期六");
            break;
    }
});

function getParameter(param) {
	var query = window.location.search;
	var iLen = param.length;
	var iStart = query.indexOf(param);
	if (iStart == -1) {
		return "";
	}
	iStart += iLen + 1;
	var iEnd = query.indexOf("&", iStart);
	if (iEnd == -1) {
		return query.substring(iStart);
	}
	return query.substring(iStart, iEnd);
}

function NowTimeView() {
    jQuery("#Span_Time").text(NowDate.toLocaleTimeString());
    NowDate.setSeconds(NowDate.getSeconds() + 1);
}

//判断是否登录
function islogin(){
	var username="${user.username}";
	if(username==""||username==null){
		window.location.href="${pageContext.request.contextPath}/admin/redirect-login";
		return false;
	}
	return true;
}
//提示信息框
function topCenter(title,msg){
	$.messager.show({
		title:title,
		msg:msg,
		timeout:1000,
		showType:'slide',
		style:{
			right:'',
			bottom:''
		}
	});
}
function selectPanel(title){
	$('#menu').accordion('select',title);
}
//解析URL
var staticURL="http://www.xueyi360.com";
function splitURL(value){
	var strAry = value.toString().split(",");
	   var stb="";
	   for(var i=0;i<strAry.length;i++){
		   if(strAry[i].substring(0,1)==("/")){
			   stb+=staticURL+strAry[i]+",";  
		   }else{
			   stb+=staticURL+"/"+",";  
		   }
		  
	   }
	  var str =stb.substring(0, stb.length-1);
	  return str;
// return "";
}

//格式化时间
function formatterDate(){
	var date=new Date();
	if (!value){return '';}
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}
function formattertime(value){
	var date=new Date(value*1000);
	if (!value){return '';}
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}
function myformatter(value){
	var date=new Date(value);
	if (!value){return '';}
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}
function dateformatter(value) {
	var date=new Date(value);
	if (!value){return '';}
	var y = date.getFullYear();
	var month = date.getMonth()+1;
	var d = date.getDate();
	var h = date.getHours();
	var min = date.getMinutes();
	var s = date.getSeconds();
	return y+'-'+(month<10?('0'+month):month)+'-'+(d<10?('0'+d):d)+" "+(h<10?('0'+h):h)+":"+(min<10?('0'+min):min)+":"+(s<10?('0'+s):s);
}
function myparser(s){
	if (!s) return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	} else {
		return new Date();
	}
}
function mydatatimeparser(s){
	var t = Date.parse(s);
	if (!isNaN(t)){
		return new Date(t);
	} else {
		return new Date();
	}
}
//去除文本中的Html并且截取指定长度的字符串
function removeHtml(content, num) {
	content = content.replace(/<[^>]+>/g, "").replace(/\s/g,"");
	 if(num>0 && content.length > num) {
		 content = content.substring(0,num);
	 }
	 return content;
}

$.extend($.fn.validatebox.defaults.rules, {
    idcard: {// 验证身份证
        validator: function (value) {
            return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        message: '身份证号码格式不正确'
    },
    minLength: {
        validator: function (value, param) {
            return value.length >= param[0];
        },
        message: '请输入至少（2）个字符.'
    },
    length: { validator: function (value, param) {
        var len = $.trim(value).length;
        return len >= param[0] && len <= param[1];
    },
        message: "输入内容长度必须介于{0}和{1}之间."
    },
    phone: {// 验证电话号码
        validator: function (value) {
            return /^((\d2,3)|(\d{3}\-))?(0\d2,3|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '格式不正确,请使用下面格式:020-88888888'
    },
    mobile: {// 验证手机号码
        validator: function (value) {
            return /^(13|15|18)\d{9}$/i.test(value);
        },
        message: '手机号码格式不正确'
    },
    intOrFloat: {// 验证整数或小数
        validator: function (value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '请输入数字，并确保格式正确'
    },
    currency: {// 验证货币
        validator: function (value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '货币格式不正确'
    },
    qq: {// 验证QQ,从10000开始
        validator: function (value) {
            return /^[1-9]\d{4,9}$/i.test(value);
        },
        message: 'QQ号码格式不正确'
    },
    integer: {// 验证整数 可正负数
        validator: function (value) {
            //return /^[+]?[1-9]+\d*$/i.test(value);
				var reg = new RegExp("^[0-9]*$"); 
//             return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
				return reg.test(value);
        },
        message: '请输入整数'
    },
    age: {// 验证年龄
        validator: function (value) {
            return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
        },
        message: '年龄必须是0到120之间的整数'
    },

    chinese: {// 验证中文
        validator: function (value) {
            return /^[\Α-\￥]+$/i.test(value);
        },
        message: '请输入中文'
    },
    english: {// 验证英语
        validator: function (value) {
            return /^[A-Za-z]+$/i.test(value);
        },
        message: '请输入英文'
    },
    unnormal: {// 验证是否包含空格和非法字符
        validator: function (value) {
            return /.+/i.test(value);
        },
        message: '输入值不能为空和包含其他非法字符'
    },
    username: {// 验证用户名
        validator: function (value) {
            return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
        },
        message: '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
    },
    faxno: {// 验证传真
        validator: function (value) {
            //            return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
            return /^((\d2,3)|(\d{3}\-))?(0\d2,3|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '传真号码不正确'
    },
    zip: {// 验证邮政编码
        validator: function (value) {
            return /^[1-9]\d{5}$/i.test(value);
        },
        message: '邮政编码格式不正确'
    },
    ip: {// 验证IP地址
        validator: function (value) {
            return /d+.d+.d+.d+/i.test(value);
        },
        message: 'IP地址格式不正确'
    },
    name: {// 验证姓名，可以是中文或英文
        validator: function (value) {
            return /^[\Α-\￥]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
        },
        message: '请输入姓名'
    },
    date: {// 验证姓名，可以是中文或英文
        validator: function (value) {
            //格式yyyy-MM-dd或yyyy-M-d
            return /^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\2(?:29))$/i.test(value);
        },
        message: '清输入合适的日期格式'
    },
    msn: {
        validator: function (value) {
            return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
        },
        message: '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
    },
    same: {
        validator: function (value, param) {
            if ($("#" + param[0]).val() != "" && value != "") {
                return $("#" + param[0]).val() == value;
            } else {
                return true;
            }
        },
        message: '两次输入的密码不一致！'
    }
});
</script>
