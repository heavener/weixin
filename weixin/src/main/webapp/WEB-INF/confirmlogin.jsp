<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/js/jquerymobile/jquery.mobile-1.4.5.min.css" />
	<script src="${pageContext.request.contextPath}/js/jquerymobile/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquerymobile/jquery.mobile-1.4.5.min.js"></script>
</head>
<body>
<div data-role="page" class="jqm-demos">

	<div data-role="header" class="jqm-header" align="center">
		<%--<a href="#" data-role="button" data-icon="home">首页</a>--%>
		<%--<a href="#" data-role="button" class="ui-btn-right" data-icon="search">搜索</a>--%>
	</div><!-- /header -->

	<div role="main" class="ui-content jqm-content">


		<form action="scanlogin" method="post">
			<input type="hidden" name="openid" value="${openid }">
			<input type="hidden" name="uuid" value="${uuid }">
			<div data-demo-html="true">
				<div class="ui-field-contain">
					<button type="submit" id="submit-1" class="ui-shadow ui-btn ui-corner-all">登录</button>
					<button type="button" id="submit-2" class="ui-shadow ui-btn ui-corner-all">取消</button>
				</div>
				<div class="ui-field-contain" align="center">
					<img alt="" width="80" height="80" src="${headimgurl }">
				</div>
			</div><!--/demo-html -->

		</form>
	</div><!-- /panel -->

</div>

</body>
</html>
