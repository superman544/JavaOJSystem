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

<title>判题系统监控页面</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>
<style type="text/css">
#chat {
	width: 410px
}

#console-container {
	width: 400px;
}

#console {
	border: 1px solid #CCCCCC;
	border-right-color: #999999;
	border-bottom-color: #999999;
	height: 170px;
	overflow-y: scroll;
	padding: 5px;
	width: 100%;
}

#console p {
	padding: 0;
	margin: 0;
}
</style>

</head>
<body>
	<div id="pendingHandleProblemCount"></div>
	<div id="javaSandboxStatus"></div>
	<div id="operationSystemInfo"></div>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/sockjs1.1.1.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/jquery.min.js"></script>

	<script>
		var websocket;
		var url = self.document.URL;
		var begin = 0;
		var end = 0;
		var count = 0;
		var pendingHandleProblemCountDIV = $("#pendingHandleProblemCount");
		var javaSandboxStatusDIV = $("#javaSandboxStatus");
		var operationSystemInfoDIV = $("#operationSystemInfo");

		for (var n = 0; n < url.length; n++) {
			if (url.charAt(n) == '/') {
				count++;
				console.log(count);

				if (count == 2) {
					begin = n + 1;
				} else if (count == 3) {
					end = n;
					break;
				}
			}
		}
		url = url.substring(begin, end);

		if ('WebSocket' in window) {
			console.log("启用WebSocket");
			websocket = new WebSocket(
					"ws://"
							+ url
							+ "${pageContext.request.contextPath }/OJSystemWebSocketServer");
		} else if ('MozWebSocket' in window) {
			console.log("启用MozWebSocket");
			websocket = new MozWebSocket(
					"ws://"
							+ url
							+ "${pageContext.request.contextPath }/OJSystemWebSocketServer");
		} else {
			console.log("启用sockjs");
			// 			模拟过，好像是支持到IE7的
			websocket = new SockJS(
					"http://"
							+ url
							+ "${pageContext.request.contextPath }/sockjs/OJSystemWebSocketServer");
		}
		websocket.onopen = function(evnt) {

		};
		websocket.onmessage = function(message) {
			var obj = jQuery.parseJSON(message.data);

			if (obj.responseType == 'PendingHandleProblemCount') {
				pendingHandleProblemCountDIV.html(message.data);
			} else if (obj.responseType == 'JavaSandboxStatus') {
				javaSandboxStatusDIV.html(message.data);
			} else if (obj.responseType == 'OperationSystemInfo') {
				operationSystemInfoDIV.html(message.data);
			}
		};
		websocket.onerror = function(evnt) {
		};
		websocket.onclose = function(evnt) {
		}
	</script>
</body>
</html>