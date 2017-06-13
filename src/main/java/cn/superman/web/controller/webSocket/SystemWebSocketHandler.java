package cn.superman.web.controller.webSocket;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import cn.superman.system.service.bean.SandboxStatus;
import cn.superman.system.service.observer.SandboxStatusObserver;
import cn.superman.util.JsonUtil;
import cn.superman.util.OperationSystemInfoUtil.OperationSystemInfo;
import cn.superman.web.controller.webSocket.vo.JavaSandboxStatusRequestVO;
import cn.superman.web.controller.webSocket.vo.OperationSystemInfoRequestVO;
import cn.superman.web.controller.webSocket.vo.PendingHandleProblemCountRequestVO;
import cn.superman.web.controller.webSocket.vo.WebSocketRequestVO;
import cn.superman.web.controller.webSocket.vo.WebSocketResponseVO;
import cn.superman.web.service.admin.AdminOJSystemService;
import cn.superman.web.util.ThreadFactoryUtil;
import cn.superman.web.vo.request.ProblemRequsetVO;

/**
 * 这个类现在责任太重了，应该拆开来的，但是，不想这么麻烦了0.0业务逻辑好像也不会做太多了，就这样先吧
 * 
 * @author 梁浩辉
 * 
 */
@Component
public class SystemWebSocketHandler implements WebSocketHandler {

	private static final Map<String, OJWebSocketSession> MANAGER_SESSIONS = new ConcurrentHashMap<String, OJWebSocketSession>();
	@Autowired
	private AdminOJSystemService ojSystemService;
	private ScheduledExecutorService systemWatcher = Executors
			.newScheduledThreadPool(
					3,
					ThreadFactoryUtil.getLogThreadFactory(this.getClass()
							.getName() + " systemInfoWatcher"));;
	private volatile boolean isFirst = true;

	private SandboxStatusObserver observer = new SandboxStatusObserver() {
		public void statusChanged(Collection<SandboxStatus> status) {
			if (status != null) {
				WebSocketResponseVO responseVO = new WebSocketResponseVO();
				responseVO.setData(status);
				responseVO
						.setResponseType(WebSocketResponseVO.ResponseType.JavaSandboxStatus);
				sendMessageToUsers(
						new TextMessage(JsonUtil.toJson(responseVO)),
						javaSandboxStatusPassport);
			}
		}
	};

	@Override
	protected void finalize() throws Throwable {
		ojSystemService.removeJavaSandboxStatusListen(observer);
		super.finalize();
	}

	// 当连接建立后，会进入这个afterConnectionEstablished方法
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		if (isFirst) {
			synchronized (this) {
				if (isFirst) {
					isFirst = false;
					init();
				}
			}
		}

		OJWebSocketSession ojSession = new OJWebSocketSession();
		ojSession.session = session;
		ojSession.isReceiveJavaSandboxStatus = true;
		ojSession.isReceiveOperationSystemInfo = true;
		ojSession.isReceivePendingHandleProblemCount = true;
		MANAGER_SESSIONS.put(session.getId(), ojSession);
	}

	private void init() {
		ojSystemService.addJavaSandboxStatusListen(observer);
		systemWatcher.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				OperationSystemInfo operationSystemInfo = ojSystemService
						.getOperationSystemInfo();
				WebSocketResponseVO responseVO = new WebSocketResponseVO();
				responseVO.setData(operationSystemInfo);
				responseVO
						.setResponseType(WebSocketResponseVO.ResponseType.OperationSystemInfo);
				sendMessageToUsers(
						new TextMessage(JsonUtil.toJson(responseVO)),
						operationSystemInfoPassport);

			}
		}, 0, 5, TimeUnit.SECONDS);

		systemWatcher.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				int problemRequestCount = ojSystemService
						.getPendingHandleProblemRequest();
				ProblemRequsetVO vo = new ProblemRequsetVO();
				vo.setPendingHandleProblemCount(problemRequestCount);
				WebSocketResponseVO responseVO = new WebSocketResponseVO();
				responseVO.setData(vo);
				responseVO
						.setResponseType(WebSocketResponseVO.ResponseType.PendingHandleProblemCount);
				sendMessageToUsers(
						new TextMessage(JsonUtil.toJson(responseVO)),
						pendingHandleProblemCountPassport);

			}
		}, 0, 1, TimeUnit.SECONDS);

		systemWatcher.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				WebSocketResponseVO responseVO = new WebSocketResponseVO();
				responseVO.setData(ojSystemService.getAppInfo());
				responseVO
						.setResponseType(WebSocketResponseVO.ResponseType.AppSystemInfo);
				sendMessageToUsers(
						new TextMessage(JsonUtil.toJson(responseVO)),
						pendingHandleProblemCountPassport);

			}
		}, 0, 3, TimeUnit.SECONDS);
	}

	// TODO 这个方法必须要重构的，现在这么简单，就不想管了0。0
	public void handleMessage(WebSocketSession session,
			WebSocketMessage<?> message) throws Exception {

		WebSocketRequestVO reqVo = JsonUtil.toBean(
				(String) message.getPayload(), WebSocketRequestVO.class);
		if (WebSocketRequestVO.RequestType.JavaSandboxStatus.equals(reqVo
				.getRequestType())) {
			JavaSandboxStatusRequestVO vo = JsonUtil.toBean(
					(String) message.getPayload(),
					JavaSandboxStatusRequestVO.class);
			MANAGER_SESSIONS.get(session.getId()).isReceiveJavaSandboxStatus = vo
					.isReceiveJavaSandboxStatus();
		} else if (WebSocketRequestVO.RequestType.OperationSystemInfo
				.equals(reqVo.getRequestType())) {
			OperationSystemInfoRequestVO vo = JsonUtil.toBean(
					(String) message.getPayload(),
					OperationSystemInfoRequestVO.class);
			MANAGER_SESSIONS.get(session.getId()).isReceiveJavaSandboxStatus = vo
					.isReceiveOperationSystemInfo();
		} else if (WebSocketRequestVO.RequestType.PendingHandleProblemCount
				.equals(reqVo.getRequestType())) {
			PendingHandleProblemCountRequestVO vo = JsonUtil.toBean(
					(String) message.getPayload(),
					PendingHandleProblemCountRequestVO.class);
			MANAGER_SESSIONS.get(session.getId()).isReceiveJavaSandboxStatus = vo
					.isReceivePendingHandleProblemCount();
		}
	}

	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}

		MANAGER_SESSIONS.remove(session);
	}

	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus closeStatus) throws Exception {
		// 用户刷新页面，也会执行这个方法……
		MANAGER_SESSIONS.remove(session);
	}

	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 */
	public void sendMessageToUsers(TextMessage message, Passport p) {
		for (Map.Entry<String, OJWebSocketSession> entry : MANAGER_SESSIONS
				.entrySet()) {
			try {
				OJWebSocketSession ojSession = entry.getValue();
				if (ojSession.session.isOpen() && p.isCanPass(ojSession)) {
					synchronized (ojSession.session) {
						ojSession.session.sendMessage(message);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessageToUser(String sessionId, TextMessage message) {

		try {
			OJWebSocketSession ojSession = MANAGER_SESSIONS.get(sessionId);
			if (ojSession.session.isOpen()) {
				synchronized (ojSession.session) {
					ojSession.session.sendMessage(message);
				}
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendMessageToUser(WebSocketSession session, TextMessage message) {
		try {
			synchronized (session) {
				session.sendMessage(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class OJWebSocketSession {
		WebSocketSession session;
		boolean isReceiveOperationSystemInfo;
		boolean isReceiveJavaSandboxStatus;
		boolean isReceivePendingHandleProblemCount;
	}

	private interface Passport {
		boolean isCanPass(OJWebSocketSession ojSession);
	}

	private Passport javaSandboxStatusPassport = new Passport() {
		public boolean isCanPass(OJWebSocketSession ojSession) {
			return ojSession.isReceiveJavaSandboxStatus;
		}
	};

	private Passport operationSystemInfoPassport = new Passport() {
		public boolean isCanPass(OJWebSocketSession ojSession) {
			return ojSession.isReceiveOperationSystemInfo;
		}

	};

	private Passport pendingHandleProblemCountPassport = new Passport() {
		public boolean isCanPass(OJWebSocketSession ojSession) {
			return ojSession.isReceivePendingHandleProblemCount;
		}

	};
}
