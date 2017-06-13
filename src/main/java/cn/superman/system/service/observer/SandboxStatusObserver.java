package cn.superman.system.service.observer;

import java.util.Collection;

import cn.superman.system.service.bean.SandboxStatus;

public interface SandboxStatusObserver {
	void statusChanged(Collection<SandboxStatus> status);
}
