package cn.superman.web.service.front;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.superman.constant.ConstantParameter;
import cn.superman.util.BeanMapperUtil;
import cn.superman.util.EncryptUtility;
import cn.superman.util.Log4JUtil;
import cn.superman.web.dao.UserDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.UpdateUserPasswordDTO;
import cn.superman.web.dto.UserLeaderboardDTO;
import cn.superman.web.dto.UserLoginDTO;
import cn.superman.web.dto.UserRegisterDTO;
import cn.superman.web.dto.UserUpdateDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.po.User;
import cn.superman.web.service.EmailService;
import cn.superman.web.service.EmailService.EmailRunnable;
import cn.superman.web.service.page.PageService;

@Service
public class UserService extends PageService<User, User> implements InitializingBean {
    @Autowired
    private UserDao userDao;
    @Autowired
    private EmailService emailService;
    // TODO 以后加上一个过期移除功能吧
    private static Map<Integer, String> updateCodeCache = new HashMap<Integer, String>();
    // TODO 以后加上一个过期移除功能吧
    private static Map<String, String> forgetPasswordCodeCache = new HashMap<String, String>();
    private List<UserLeaderboardDTO> haveDoneProblemTop50Cache = null;
    private List<UserLeaderboardDTO> rightProblemTop50Cache = null;
    private List<UserLeaderboardDTO> sloveProblemTotalValueTop50Cache = null;

    // TODO 往后，应该要添加事务支持
    @Transactional
    public void register(UserRegisterDTO dto) {

        if (!dto.getConfirmPassword().equals(dto.getPassword())) {
            throw new ServiceLogicException("两次密码不一致");
        }

        checkAccountIsUnique(dto.getAccount());

        User user = BeanMapperUtil.map(dto, User.class);
        // 准备用户存储准备，比如进行密码md5加密
        user.setPassword(EncryptUtility.Md5Encoding(user.getPassword()));
        // 如果用户没有填写用户名，自动生成一个
        if (StringUtils.isBlank(user.getNickname())) {
            user.setNickname("用户" + System.currentTimeMillis());
        }

        // 将用户进行数据库存储
        try {
            userDao.add(user);
        } catch (Exception e) {
            Log4JUtil.logError(e);
            throw new ServiceLogicException("注册失败，若有需要请联系管理员");
        }

        // 准备用户必要的初始化信息
        updateUserInitData(user);
    }

    private void updateUserInitData(User user) {
        // 用户创建出来后，用它的ID编号，创建它源码的保存文件，用户保存提交记录的表等内容
        String id = user.getUserId() + "";
        File userJavaFileRoot = new File(ConstantParameter.USER_SUBMIT_CODE_ROOT_PATH + File.separator + id);
        if (userJavaFileRoot.mkdirs()) {
            user.setHaveDoneProblem(0);
            user.setRightProblemCount(0);
            user.setTotalSolveValue(0);
            user.setSourceFileRootPath(userJavaFileRoot.getAbsolutePath());
        }

        int index = user.getUserId() / ConstantParameter.SUBMIT_RECORD_TABLE_CREATE_GAP;
        String tableName = "submit_record" + index;
        user.setSubmitRecordTableName(tableName);
        // 创建出，用于保存该用户的提交记录表
        // 每XX个人一张表,当求余的结果不是1时，表明这张表已经创建出来了，不需要再创建
        if (user.getUserId() % ConstantParameter.SUBMIT_RECORD_TABLE_CREATE_GAP == 1) {
            userDao.createNewSubmitRecordTable(tableName);
        }

        userDao.update(user);
    }

    public User login(UserLoginDTO dto) throws ServiceLogicException {
        User user = new User();
        user.setAccount(dto.getAccount());
        user.setPassword(EncryptUtility.Md5Encoding(dto.getPassword()));
        List<User> users = userDao.findWithCondition(user);
        if (users.size() < 1) {
            throw new ServiceLogicException("账号或者密码错误");
        }

        user = users.get(0);

        if (user.getIsBan() != null && user.getIsBan()) {
            throw new ServiceLogicException("你的账号已经被封，请联系管理员解封");
        }

        return user;
    }

    public void sendForgetPasswordEmail(String account) {
        User condition = new User();
        condition.setAccount(account);
        User user = null;
        try {
            user = userDao.findWithCondition(condition).get(0);
        } catch (Exception e) {
            if (!(e instanceof ArrayIndexOutOfBoundsException)) {
                Log4JUtil.logError(e);
            }
            throw new ServiceLogicException("系统不存在该账号");
        }

        // 生成一个4位随机验证码
        String code = RandomStringUtils.randomNumeric(4);
        forgetPasswordCodeCache.put(account, code);

        String emailSubject = ConstantParameter.SYSTEM_NAME + "重新设置密码验证码";
        String emailContent = "验证码为：" + code;
        String emailReceiver = user.getEmail();
        emailService.sendEmail(new EmailRunnable(emailSubject, emailContent, emailReceiver));
    }

    public void updateUserPassword(UpdateUserPasswordDTO dto) throws ServiceLogicException {

        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new ServiceLogicException("新密码和确认密码不对应");
        }

        if (!dto.getEmailVerificationCode().equals(forgetPasswordCodeCache.get(dto.getAccount()))) {
            throw new ServiceLogicException("邮件验证码不正确");
        }

        // 移除验证码，避免二次使用
        forgetPasswordCodeCache.remove(dto.getAccount());

        User user = null;
        User condition = new User();
        condition.setAccount(dto.getAccount());
        try {
            user = userDao.findWithCondition(condition).get(0);
        } catch (Exception e) {
            if (!(e instanceof ArrayIndexOutOfBoundsException)) {
                Log4JUtil.logError(e);
            }
            throw new ServiceLogicException("系统不存在该账号");
        }
        user.setUserId(user.getUserId());
        user.setPassword(EncryptUtility.Md5Encoding(dto.getNewPassword()));
        userDao.update(user);
    }

    public void checkAccountIsUnique(String account) {
        User user = new User();
        user.setAccount(account);
        if (userDao.queryTotalCountWithCondition(user) > 0) {
            throw new ServiceLogicException("该账号已经被注册了，请更换账号");
        }
    }

    public List<UserLeaderboardDTO> getRightProblemTop50() {
        return rightProblemTop50Cache;
    }

    public List<UserLeaderboardDTO> getHaveDoneProblemTop50() {
        return haveDoneProblemTop50Cache;
    }

    public List<UserLeaderboardDTO> getSloveProblemTotalValueTop50() {
        return sloveProblemTotalValueTop50Cache;
    }

    public void updateUserLeaderboardCache() {
        PageHelper.startPage(1, 50, "right_problem_count desc");
        List<User> users = userDao.find();
        rightProblemTop50Cache = BeanMapperUtil.mapList(users, UserLeaderboardDTO.class);

        PageHelper.startPage(1, 50, "have_done_problem desc");
        users = userDao.find();
        haveDoneProblemTop50Cache = BeanMapperUtil.mapList(users, UserLeaderboardDTO.class);

        PageHelper.startPage(1, 50, "total_solve_value desc");
        users = userDao.find();
        sloveProblemTotalValueTop50Cache = BeanMapperUtil.mapList(users, UserLeaderboardDTO.class);
    }

    public void sendUpdateCodeEmail(User user) {
        // 生成一个4位随机验证码
        String code = RandomStringUtils.randomNumeric(4);
        updateCodeCache.put(user.getUserId(), code);

        String emailSubject = ConstantParameter.SYSTEM_NAME + "用户信息修改验证码";
        String emailContent = "验证码为：" + code;
        String emailReceiver = user.getEmail();
        emailService.sendEmail(new EmailRunnable(emailSubject, emailContent, emailReceiver));
    }

    public void update(UserUpdateDTO dto) {
        if (!dto.getEmailVerificationCode().equals(updateCodeCache.get(dto.getUserId()))) {
            throw new ServiceLogicException("邮件验证码不正确,或者验证码已经失效");
        }

        updateCodeCache.remove(dto.getUserId());

        User user = BeanMapperUtil.map(dto, User.class);
        if (user.getPassword() != null) {
            user.setPassword(EncryptUtility.Md5Encoding(user.getPassword()));
        }
        userDao.update(user);
    }

    public User find(Integer userId) {
        return userDao.findById(userId);
    }

    @Override
    public BaseDao<User, User> getUseDao() {
        return userDao;
    }

    // 当程序加载起来时，就加载一次前50名的内容
    @Override
    public void afterPropertiesSet() throws Exception {
        updateUserLeaderboardCache();
    }

}
