package cn.superman.web.controller.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements
		WebSocketConfigurer {

	@Autowired
	private SystemWebSocketHandler handler;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler, "/manager/OJSystemWebSocketServer")
				.addInterceptors(new WebSocketHandshakeInterceptor());

		registry.addHandler(handler, "/manager/sockjs/OJSystemWebSocketServer")
				.addInterceptors(new WebSocketHandshakeInterceptor())
				.withSockJS();
	}

}
