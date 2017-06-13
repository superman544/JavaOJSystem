package cn.superman.web.service.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superman.util.BeanMapperUtil;
import cn.superman.web.dao.AnnouncementDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.AddAnnouncementDTO;
import cn.superman.web.dto.UpdateAnnouncementDTO;
import cn.superman.web.po.Announcement;
import cn.superman.web.service.page.PageService;

@Service
public class AdminAnnouncementService extends
		PageService<Announcement, Announcement> {
	@Autowired
	private AnnouncementDao announcementDao;

	public void add(AddAnnouncementDTO dto) {
		Announcement announcement = BeanMapperUtil.map(dto, Announcement.class);
		if (dto.getIsPublish()) {
			announcement.setAnnouncementPublishTime(new Date());
		}
		announcement.setAnnouncementCreateTime(new Date());
		announcementDao.add(announcement);
	}

	public void deleteById(Integer id) {
		announcementDao.deleteById(id);
	}

	public Announcement findById(Integer id) {
		return announcementDao.findById(id);
	}

	public void update(UpdateAnnouncementDTO dto) {
		Announcement announcement = BeanMapperUtil.map(dto, Announcement.class);
		if (dto.getIsPublish()) {
			announcement.setAnnouncementPublishTime(new Date());
		}
		announcementDao.update(announcement);
	}

	public void publish(Integer id) {
		Announcement announcement = new Announcement();
		announcement.setAnnouncementId(id);
		announcement.setAnnouncementPublishTime(new Date());
		announcement.setIsPublish(true);
		announcementDao.update(announcement);
	}

	@Override
	public BaseDao<Announcement, Announcement> getUseDao() {
		return announcementDao;
	}
}
