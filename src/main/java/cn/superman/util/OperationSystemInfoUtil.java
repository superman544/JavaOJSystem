package cn.superman.util;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class OperationSystemInfoUtil {
	private OperationSystemInfoUtil() {
		throw new AssertionError("不要实例化工具类哦");
	}
	
	private static OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
			.getOperatingSystemMXBean();

	public static OperationSystemInfo getOperationSystemInfo() {
		OperationSystemInfo operationSystemInfo = new OperationSystemInfo();
		operationSystemInfo.totalPhysicalMemory = operatingSystemMXBean
				.getTotalPhysicalMemorySize();
		operationSystemInfo.freePhysicalMemory = operatingSystemMXBean
				.getFreePhysicalMemorySize();
		operationSystemInfo.operatingSystemName = operatingSystemMXBean
				.getName();
		operationSystemInfo.systemCpuCount = operatingSystemMXBean
				.getAvailableProcessors();
		operationSystemInfo.systemCpuLoad = operatingSystemMXBean
				.getSystemCpuLoad();

		return operationSystemInfo;
	}

	public static class OperationSystemInfo {
		private long totalPhysicalMemory;
		private long freePhysicalMemory;
		private String operatingSystemName;
		private int systemCpuCount;
		private double systemCpuLoad;

		public long getTotalPhysicalMemory() {
			return totalPhysicalMemory;
		}

		public long getFreePhysicalMemory() {
			return freePhysicalMemory;
		}

		public String getOperatingSystemName() {
			return operatingSystemName;
		}

		public int getSystemCpuCount() {
			return systemCpuCount;
		}

		/**
		 * 注意，不是即时cpu使用率，只是最近的使用率，有延迟不是很准确的，0.235345353,表示使用率为23.5%
		 * 
		 * @return
		 */
		public double getSystemCpuLoad() {
			return systemCpuLoad;
		}

	}
}
