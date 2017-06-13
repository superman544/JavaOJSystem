package cn.superman.system.communicator;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import cn.superman.system.communicator.listener.SandboxIdleListener;
import cn.superman.system.dto.CommonRequest;
import cn.superman.system.dto.CommunicatorStatus;
import cn.superman.system.dto.JavaSandboxStartInfo;
import cn.superman.system.dto.JudgeProblemRequest;
import cn.superman.system.sandbox.dto.SandboxInitData;
import cn.superman.util.Log4JUtil;
import cn.superman.web.util.ThreadFactoryUtil;

import com.google.gson.Gson;

public class CommunicatorManager {
	private BlockingQueue<Communicator> judgeingCommunicators = new LinkedBlockingQueue<Communicator>();
	private BlockingQueue<Communicator> noJudgeingCommunicators = new LinkedBlockingQueue<Communicator>();
	private BlockingQueue<Communicator> haveStopCommunicators = new LinkedBlockingQueue<Communicator>();
	private BlockingQueue<JudgeProblemRequest> problemRequests = new LinkedBlockingQueue<JudgeProblemRequest>();
	private BlockingQueue<JudgeProblemRequest> highPriorityProblemRequests = new LinkedBlockingQueue<JudgeProblemRequest>();
	// 注意，这个map仅仅是为了执行非请求判题的操作（比如，检查沙箱状态等）方便而设立，绝对不要从里面获取交流者，然后用于提交判题请求
	private Map<String, Communicator> allCommunicators = new ConcurrentHashMap<String, Communicator>();
	private ExecutorService watchingExecutor = Executors
			.newSingleThreadExecutor(ThreadFactoryUtil
					.getLogThreadFactory(CommunicatorManager.class.getName()
							+ " watchingExecutor "));

	private static volatile CommunicatorManager communicatorManager = null;
	// 主要用于当题目已经取出了，但是暂时没有沙箱可以处理时，精确显示等待判题显示的显示问题
	private int wantHandlePrblemCount = 0;

	public static CommunicatorManager getInstance() {
		if (communicatorManager == null) {
			synchronized (CommunicatorManager.class) {
				if (communicatorManager == null) {
					communicatorManager = new CommunicatorManager();
				}
			}
		}

		return communicatorManager;
	}

	private CommunicatorManager() {
		init();
	}

	private void init() {

		// TODO 现在的代码还是挺难看的，到时候把一些东西抽取出来变成一个个方法以及变成一个Runnable这样吧
		watchingExecutor.execute(new Runnable() {
			@Override
			public void run() {
				while (true && !Thread.interrupted()) {

					JudgeProblemRequest request = null;
					try {
						// 如果有空闲的沙箱的话，就取出一个判题请求，先取出优先级高的，这里并不会阻塞
						request = highPriorityProblemRequests.poll();

						if (request == null) {
							// 当前没有判题请求的话，则会一直阻塞在这里
							request = problemRequests.take();
						}
						wantHandlePrblemCount++;
					} catch (InterruptedException e) {
						continue;
					}

					Communicator communicator = null;
					try {
						communicator = noJudgeingCommunicators.take();
						wantHandlePrblemCount--;
					} catch (InterruptedException e) {
						Log4JUtil.logError(e);
					}

					// 移进判题列表中
					judgeingCommunicators.add(communicator);
					communicator.sendRequset(request.getRequest(),
							request.getExecutor());
					communicator.setJudgeing(true);
					communicator = null;
				}
			}
		});
	}

	/**
	 * 返回一个交流者的身份区别凭证
	 * 
	 * @param sandboxStartInfo
	 * @return
	 */
	public String makeNewSandBox(JavaSandboxStartInfo sandboxStartInfo) {
		try {

			Process process = openNewSandBox(sandboxStartInfo);
			return connectToNewSandBox(sandboxStartInfo.getIp(),
					sandboxStartInfo.getPort(), process);
		} catch (Exception e) {
			Log4JUtil.logError(e);
			return null;
		}
	}

	private Process openNewSandBox(JavaSandboxStartInfo sandboxStartInfo)
			throws IOException {
		SandboxInitData sandboxInitData = new SandboxInitData();
		sandboxInitData.setPort(sandboxStartInfo.getPort());
		sandboxInitData.setClassFileRootPath(sandboxStartInfo
				.getProblemClassFileRootPath());

		Gson gson = new Gson();
		String command = "java -jar " + sandboxStartInfo.getJarFilePath() + " "
				+ gson.toJson(sandboxInitData);
		return Runtime.getRuntime().exec(command);
	}

	private String connectToNewSandBox(String ip, int port, Process process) {
		Communicator communicator = new Communicator(ip, port, process);
		boolean flag = communicator.connectToSandbox();
		if (!flag) {
			return null;
		}

		String url = ip + ":" + port;
		allCommunicators.put(url, communicator);
		noJudgeingCommunicators.add(communicator);

		communicator
				.addSandboxIdleListener(new CommunicatorSandboxIdleListener(
						communicator, url));
		return url;
	}

	public void stopSingleCommunicatorById(String idCard) {
		Communicator javaCommunicator = allCommunicators.get(idCard);
		if (javaCommunicator == null) {
			throw new RuntimeException("没有该连接");
		}

		if (haveStopCommunicators.contains(javaCommunicator)) {
			return;
		}

		// 判断当前该连接是否为空闲连接
		if (noJudgeingCommunicators.contains(javaCommunicator)) {
			// 因为集合本身是线程安全的，所以如果这里移除成功了，其它地方就不可能（不从map上拿的话）在拿到这个链接了。
			boolean remove = noJudgeingCommunicators.remove(javaCommunicator);
			if (remove) {
				// 直接加入到停止集合里
				haveStopCommunicators.add(javaCommunicator);
				javaCommunicator.setStop(true);
				return;
			}
		}

		// 如果上面都不成立，只能设置为请求停止状态，等该沙箱处理完题目后，再将其停止
		javaCommunicator.setWantStop(true);
	}

	public void closeSandboxConnectById(String idCard) {
		Communicator javaCommunicator = allCommunicators.get(idCard);
		if (javaCommunicator == null) {
			throw new RuntimeException("没有该连接");
		}

		if (haveStopCommunicators.contains(javaCommunicator)) {
			// 直接关闭沙箱
			haveStopCommunicators.remove(javaCommunicator);
			allCommunicators.remove(idCard);
			javaCommunicator.closeWithSandboxConnect();
			return;
		}

		// 判断当前该连接是否为空闲连接
		if (noJudgeingCommunicators.contains(javaCommunicator)) {
			// 因为集合本身是线程安全的，所以如果这里移除成功了，那定时器那里，就不可能在拿到这个链接了。
			boolean remove = noJudgeingCommunicators.remove(javaCommunicator);
			if (remove) {
				allCommunicators.remove(idCard);
				javaCommunicator.closeWithSandboxConnect();
			}
		}

		// 如果上面都不成立，只能设置为请求关闭状态，等该沙箱处理完题目后，再将其停止
		javaCommunicator.setWantClose(true);
	}

	public void publicCommonRequest(String communicatorIdCard,
			CommonRequest commonRequest) {
		Communicator communicator = allCommunicators.get(communicatorIdCard);

		if (communicator != null) {
			communicator.sendRequset(commonRequest.getRequest(),
					commonRequest.getExecutor());
		}
	}

	public void publicJudgeProblemRequest(JudgeProblemRequest problemRequest) {
		problemRequests.add(problemRequest);
	}

	public CommunicatorStatus getCommunicatorStatus(String communicatorIdCard) {
		Communicator javaCommunicator = allCommunicators
				.get(communicatorIdCard);
		CommunicatorStatus c = new CommunicatorStatus();
		c.setJudgeing(javaCommunicator.isJudgeing());
		c.setStop(javaCommunicator.isStop());
		c.setWantClose(javaCommunicator.isWantClose());
		c.setWantStop(javaCommunicator.isWantStop());

		return c;
	}

	public int getPendingHandleProblemRequest() {
		return highPriorityProblemRequests.size() + problemRequests.size()
				+ wantHandlePrblemCount;
	}

	private class CommunicatorSandboxIdleListener implements
			SandboxIdleListener {
		private Communicator communicator;
		private String communicatorIdCard;

		public CommunicatorSandboxIdleListener(Communicator communicator,
				String communicatorIdCard) {
			this.communicator = communicator;
			this.communicatorIdCard = communicatorIdCard;
		}

		@Override
		public void sandBoxIdelNow() {
			if (communicator == null) {
				return;
			}
			// 先移除出来
			judgeingCommunicators.remove(communicator);
			communicator.setJudgeing(false);
			// 先判断，是否被设置了，想要停止工作的标志
			if (communicator.isWantStop()) {
				haveStopCommunicators.add(communicator);
				communicator.setStop(true);
				communicator.setWantStop(false);
			} else if (communicator.isWantClose()) {
				allCommunicators.remove(communicatorIdCard);
				communicator.closeWithSandboxConnect();
			} else {
				noJudgeingCommunicators.add(communicator);
				communicator.setStop(false);
				communicator.setWantStop(false);
			}
		}
	}

}
