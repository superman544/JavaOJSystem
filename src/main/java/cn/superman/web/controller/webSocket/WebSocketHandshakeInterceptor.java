package cn.superman.web.controller.webSocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import cn.superman.web.po.Manager;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {

//		if (request instanceof ServletServerHttpRequest) {
//			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//
//			HttpSession session = servletRequest.getServletRequest()
//					.getSession(false);
//
//			if (session != null) {
//				Manager manager = (Manager) session.getAttribute("manager");
//				// 可能存在manager未空的情况？但是，这里都必须返回true才比较好处理啊，看来只能在handler那边进行处理了
//				// 而且，因为里面的集合是一个ConcurrentHashMap的关系，key和value都是不能为空的，所以这里要判断一下是否非空
//				if (manager != null) {
//					attributes.put("manager", manager);
//				}
//
//			}
//		}

		return true;
	}

	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {

	}
}
