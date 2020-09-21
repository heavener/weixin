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
					<label >订单编号:</label>
					${order.id }
				</div>
			</div><!--/demo-html -->
			<div data-demo-html="true">
				<div class="ui-field-contain">
					<label >交易金额:</label>
					${order.totalprice }
				</div>
			</div><!--/demo-html -->


		</form>
	</div><!-- /panel -->

</div>

</body>
</html>
