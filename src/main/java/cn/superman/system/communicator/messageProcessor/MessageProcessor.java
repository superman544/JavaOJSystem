package cn.superman.system.communicator.messageProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import cn.superman.system.commandExecutor.ResponseExecutor;
import cn.superman.system.communicator.listener.SandboxIdleListener;
import cn.superman.system.sandbox.constant.CommunicationSignal;
import cn.superman.system.sandbox.dto.Request;
import cn.superman.system.sandbox.dto.Response;
import cn.superman.util.JsonUtil;
import cn.superman.util.Log4JUtil;
import cn.superman.util.UUIDUtil;

public class MessageProcessor {
	private static Map<String, ResponseExecutor> responseExecutors = new HashMap<String, ResponseExecutor>();
	private Thread messageBoxThread;
	private Scanner scanner;
	private OutputStream outputStream;
	private SandboxIdleListener idleListener;

	public MessageProcessor(InputStream inputStream, OutputStream outputStream) {
		try {

			scanner = new Scanner(new InputStreamReader(inputStream, "UTF-8"));
			this.outputStream = outputStream;
			openMessageListen();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private void openMessageListen() {
		messageBoxThread = new Thread() {
			@Override
			public void run() {
				String message = null;
				Response response = null;

				while (!this.isInterrupted()) {
					if (scanner.hasNextLine()) {
						message = scanner.nextLine();
						response = JsonUtil.toBean(message, Response.class);
						if (CommunicationSignal.ResponseSignal.IDLE
								.equals(response.getResponseCommand())) {
							if (idleListener != null) {
								idleListener.sandBoxIdelNow();
							}
							// 这里理应再修改一下，利用观察者模式，做到对修改关闭，做扩展开放，可以动态添加多个监听器
						} else if (CommunicationSignal.ResponseSignal.ERROR
								.equals(response.getResponseCommand())) {
							Log4JUtil.logError(new RuntimeException(response
									.getData()));
						} else {
							ResponseExecutor commandExecutor = responseExecutors
									.remove(response.getSignalId());
							if (commandExecutor != null) {
								commandExecutor.executor(response);
							}
						}
					}
				}
			}
		};

		messageBoxThread.start();
		messageBoxThread
				.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						Log4JUtil.logError(e);
					}
				});
	}

	public void close() {
		try {
			outputStream.close();
		} catch (IOException e) {
			Log4JUtil.logError(e);
		}
		scanner.close();
		messageBoxThread.interrupt();
	}

	public void sendRequset(Request request, ResponseExecutor executor) {
		try {
			request.setSignalId(UUIDUtil.getUUID());
			String data = JsonUtil.toJson(request);
			addExecutor(request.getSignalId(), executor);
			outputStream.write((data + "\n").getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void addExecutor(String signalId, ResponseExecutor commandExecutor) {
		responseExecutors.put(signalId, commandExecutor);
	}

	public SandboxIdleListener getIdleListener() {
		return idleListener;
	}

	public void setIdleListener(SandboxIdleListener idleListener) {
		this.idleListener = idleListener;
	}

}
