package cn.superman.web.service.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superman.web.dao.AnnouncementDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.po.Announcement;
import cn.superman.web.service.page.PageService;

@Service
public class AnnouncementService extends
		PageService<Announcement, Announcement> {

	@Autowired
	private AnnouncementDao announcementDao;
	private static Announcement defaultCondition = null;
	static {
		defaultCondition = new Announcement();
		defaultCondition.setIsPublish(true);
	}

	@Override
	public BaseDao<Announcement, Announcement> getUseDao() {
		return announcementDao;
	}

	@Override
	public Announcement getDefaultCondition() {
		return defaultCondition;
	}

	public Announcement getAnnouncementById(Integer announcementId) {
		return announcementDao.findById(announcementId);
	}

}
