package cn.superman.web.dao;

import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dao.base.MyBatisRepository;
import cn.superman.web.po.Announcement;

@MyBatisRepository
public interface AnnouncementDao extends BaseDao<Announcement, Announcement> {

}
