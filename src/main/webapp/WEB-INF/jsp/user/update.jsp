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
<title>登陆页面</title>
</head>
<body>
	<form
		action="${pageContext.request.contextPath }/UserController/loginUser"
		method="post">
		账号：<input type="text" name="account"> 密码：<input
			type="password" name="password"> 验证码：<input type="text"
			name="verificationCode"><img id="verificationCodeImg"
			src="${imageSrcBase64Data }"
			onclick="return changeVerificationCode()"> <input type="submit"
			value="登陆">
	</form>
	${message }
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/jquery.min.js"></script>
	<script type="text/javascript">
		function changeVerificationCode() {
			var verificationCodeImg = $("#verificationCodeImg");
			$
					.ajax({
						type : "post",
						url : "${pageContext.request.contextPath }/UserController/changeVerificationCode",
						async : false,
						dataType : "json",
						success : function(respones) {
							verificationCodeImg.attr("src", respones.message);
						}

					});
		}
	</script>
</body>
</html>