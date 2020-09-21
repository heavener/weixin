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


		<form action="getUser" method="post">
			<input type="hidden" name="openid" value="${openid }">
			<div data-demo-html="true">
				<div class="ui-field-contain">
					<label for="nickname">昵称:</label>
					<input type="text" name="nickname" id="nickname" placeholder="昵称" value="${nickname }">
				</div>
			</div><!--/demo-html -->
			<div data-demo-html="true">
				<div class="ui-field-contain">
					<label for="username">账号:</label>
					<input type="text" name="username" id="username" placeholder="用户名" value="${nickname }">
				</div>
			</div><!--/demo-html -->
			<div data-demo-html="true">
				<div class="ui-field-contain">
					<label for="password">密码:</label>
					<input type="password" name="password" id="password" placeholder="密码" value="">
				</div>
			</div><!--/demo-html -->

			<div data-demo-html="true">
				<div class="ui-field-contain">
					<label for="phone">电话:</label>
					<input type="tel" data-clear-btn="true" name="phone" id="phone" placeholder="电话" value="">
				</div>
			</div><!--/demo-html -->
			<div data-demo-html="true">
				<div class="ui-field-contain">
					<label for="email">邮箱:</label>
					<input type="email" data-clear-btn="true" name="mail" id="email" placeholder="邮箱" value="">
				</div>
			</div><!--/demo-html -->
			<div data-demo-html="true">
				<div class="ui-field-contain">
					<label for="submit-1">提交:</label>
					<button type="submit" id="submit-1" class="ui-shadow ui-btn ui-corner-all">绑定</button>
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
