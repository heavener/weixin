<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>


<c:if test="${msg =='0'}">
    <h1>绑定失败</h1>
</c:if>
<c:if test="${msg =='1'}">
    <h1>绑定成功</h1>
</c:if>
<c:if test="${msg =='2'}">
    <h1>用户名密码错误</h1>
</c:if>
<c:if test="${msg =='3'}">
    <h1>该用户已经绑定，请尝试其他用户</h1>
</c:if>

</body>
</html>
