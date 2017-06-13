<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<title>代码提交页面</title>
</head>
<body>
	<form
		action="${pageContext.request.contextPath }/AnswerSubmitController/submitAnswer"
		method="post">
		<input type="hidden" name="token" value="${token }"> 题目编号：<input
			type="text" name="submitProblemId"> 代码：
		<textarea rows="30" cols="30" name="code"></textarea>
		代码语言：<input type="text" name="codeLanguage"> <input
			type="submit" value="提交">
	</form>
	${message }
</body>
</html>