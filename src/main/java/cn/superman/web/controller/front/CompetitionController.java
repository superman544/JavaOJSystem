package cn.superman.web.controller.front;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superman.util.BeanMapperUtil;
import cn.superman.util.EncryptUtility;
import cn.superman.util.Log4JUtil;
import cn.superman.web.bean.ResponseMap;
import cn.superman.web.constant.WebConstant;
import cn.superman.web.controller.PageController;
import cn.superman.web.dto.CompetitionApplyDTO;
import cn.superman.web.dto.CompetitionProblemAnswerDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.po.Competition;
import cn.superman.web.po.CompetitionAccount;
import cn.superman.web.po.Problem;
import cn.superman.web.service.front.CompetitionApplicationService;
import cn.superman.web.service.front.CompetitionService;
import cn.superman.web.service.page.PageService;
import cn.superman.web.util.ThreadFactoryUtil;
import cn.superman.web.vo.request.BeginCompetitionVO;
import cn.superman.web.vo.request.CompetitionApplyVO;
import cn.superman.web.vo.request.CompetitionProblemAnswerVO;
import cn.superman.web.vo.response.CompetitionResponse;
import cn.superman.web.vo.response.ProblemResponse;

@Controller
@RequestMapping("/CompetitionController")
public class CompetitionController extends PageController<Competition, Competition, CompetitionResponse> implements InitializingBean {

    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private CompetitionApplicationService competitionApplicationService;
    private ScheduledExecutorService scheduledExecutor = Executors
            .newScheduledThreadPool(2, ThreadFactoryUtil.getLogThreadFactory(CompetitionController.class + " token cache thread "));
    private Map<Integer, Token> loginTokenCache = new ConcurrentHashMap<Integer, Token>();
    private Map<Integer, Token> applyTokenCache = new ConcurrentHashMap<Integer, Token>();

    @Override
    public PageService<Competition, Competition> getPageService() {
        return competitionService;
    }

    @Override
    public Class<CompetitionResponse> returnVoClass() {
        return CompetitionResponse.class;
    }

    @RequestMapping(value = "/applyToken", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap applyToken(@RequestParam("id") Integer id) {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        Token applyToken = getApplyToken(id);
        if (applyToken == null) {
            throw new ServiceLogicException("暂时无法申请，请检查申请时间");
        }
        responseMap.append("applyToken", applyToken.key);

        return responseMap;
    }

    @RequestMapping(value = "/loginToken", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap loginToken(@RequestParam("id") Integer id) {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        Token loginToken = getLoginToken(id);
        if (loginToken == null) {
            throw new ServiceLogicException("暂时无法申请，请检查比赛时间");
        }

        loginToken.lastNeedTime = System.currentTimeMillis();
        responseMap.append("loginToken", loginToken.key);

        return responseMap;
    }

    private Object loginLockObj = new Object();

    // 并发和吞吐量都不会太大
    private Token getLoginToken(Integer competitionId) {
        if (loginTokenCache.get(competitionId) == null) {
            synchronized (loginLockObj) {
                if (loginTokenCache.get(competitionId) == null) {
                    Competition competition = competitionService.getCompetitionById(competitionId);
                    // 为空也是未关闭
                    if (competition.getIsClose() != null && competition.getIsClose()) {
                        throw new ServiceLogicException("比赛已经关闭");
                    }
                    // 判断比赛是否关闭了，并且是否时间到了，时间到了的话，生成一个令牌带给前端，前端要带着令牌访问，才能进入比赛页面
                    Calendar calendar = Calendar.getInstance();
                    Date now = calendar.getTime();
                    if (now.after(competition.getCompetitionBeginTime()) && now.before(competition.getCompetitionEndTime())) {
                        // 获取比赛时间段
                        long beginTime = competition.getCompetitionBeginTime().getTime();
                        long endTime = competition.getCompetitionEndTime().getTime();
                        // 生成登录令牌
                        try {
                            String key = competition.getCompetitionId() + "#" + beginTime + "#" + endTime + "#asdopad5646#" + System.currentTimeMillis();
                            loginTokenCache.put(competitionId, new Token(EncryptUtility.Md5Encoding(key), competitionId + "", beginTime, endTime));
                        } catch (Exception e) {
                            Log4JUtil.logError(e);
                        }
                    } else {
                        throw new ServiceLogicException("不在比赛时间内，请确定比赛时间");
                    }
                }
            }
        }
        return loginTokenCache.get(competitionId);
    }

    private Object applyLockObj = new Object();

    private Token getApplyToken(Integer competitionId) {
        if (applyTokenCache.get(competitionId) == null) {
            synchronized (applyLockObj) {
                if (applyTokenCache.get(competitionId) == null) {
                    Competition competition = competitionService.getCompetitionById(competitionId);
                    Calendar calendar = Calendar.getInstance();
                    // 生成申请令牌
                    if (competition.getIsCanDeclare() && calendar.getTime().after(competition.getCompetitionApplyBeginTime())
                            && calendar.getTime().before(competition.getCompetitionApplyEndTime())) {
                        // 获取比赛时间段
                        long beginTime = competition.getCompetitionApplyBeginTime().getTime();
                        long endTime = competition.getCompetitionApplyEndTime().getTime();
                        String key = competition.getCompetitionId() + "#" + beginTime + "#" + endTime + "#8sjlaskdja#" + System.currentTimeMillis();
                        applyTokenCache.put(competitionId, new Token(EncryptUtility.Md5Encoding(key), competitionId + "", beginTime, endTime));
                    }
                }
            }
        }
        return applyTokenCache.get(competitionId);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap apply(@Valid CompetitionApplyVO vo) {
        // 判断令牌是否准确
        // 验证token
        Token token = applyTokenCache.get(vo.getCompetitionId());
        if (token == null) {
            throw new ServiceLogicException("没有该比赛编号的申请令牌");
        }

        long beginTime = token.beginTime;
        if (beginTime >= System.currentTimeMillis()) {
            throw new ServiceLogicException("比赛申请还未开始，若发现时间有异常，请联系管理人员，确定服务器时间于当地时间一致");
        }

        // 主要是验证令牌有效期，因为有可能别人拿过期的令牌来继续登录
        long endTime = token.endTime;
        if (endTime <= System.currentTimeMillis()) {
            throw new ServiceLogicException("比赛申请已经结束，若发现时间有异常，请联系管理人员，确定服务器时间于当地时间一致");
        }

        if (!token.key.equals(vo.getToken())) {
            throw new ServiceLogicException("令牌信息不对，无法申请比赛");
        }

        CompetitionApplyDTO dto = BeanMapperUtil.map(vo, CompetitionApplyDTO.class);
        competitionApplicationService.addApplication(dto);

        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap login(@Valid BeginCompetitionVO vo, HttpSession session) {

        // 防止重复登录消耗资源
        if (session.getAttribute(WebConstant.COMPETITION_ACCOUNT_ATTRIBUTE_NAME) != null) {
            return new ResponseMap().buildSucessResponse();
        }

        // 验证token
        Token token = loginTokenCache.get(vo.getCompetitionId());
        if (token == null) {
            throw new ServiceLogicException("没有该比赛编号的令牌");
        }

        if (!token.key.equals(vo.getToken())) {
            throw new ServiceLogicException("令牌信息不对，无法登录比赛");
        }

        long beginTime = token.beginTime;
        if (beginTime >= System.currentTimeMillis()) {
            throw new ServiceLogicException("比赛还未开始，若发现时间有异常，请联系管理人员，确定服务器时间于当地时间一致");
        }

        // 主要是验证令牌有效期，因为有可能别人拿过期的令牌来继续登录
        long endTime = token.endTime;
        if (endTime <= System.currentTimeMillis()) {
            throw new ServiceLogicException("比赛已经结束，若发现时间有异常，请联系管理人员，确定服务器时间于当地时间一致");
        }

        CompetitionAccount competitionAccount = competitionService.loginCompetition(vo.getAccount(), vo.getPassword(), vo.getCompetitionId());
        if (competitionAccount == null) {
            throw new ServiceLogicException("账号或者密码不对");
        }
        session.setAttribute(WebConstant.COMPETITION_ACCOUNT_ATTRIBUTE_NAME, competitionAccount);
        // 设置该比赛的结束时间
        session.setAttribute(WebConstant.COMPETITION_TIME_OUT_ATTRIBUTE_NAME, endTime);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @RequestMapping(value = "/getCompetitionData", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap getCompetitionData(HttpSession session) {
        // 本来这种验证，应该是用拦截器来写的，但是这里省事就不写了
        CompetitionAccount competitionAccount = (CompetitionAccount) session.getAttribute(WebConstant.COMPETITION_ACCOUNT_ATTRIBUTE_NAME);
        if (competitionAccount == null) {
            throw new ServiceLogicException("请登录");
        }

        long endTime = (long) session.getAttribute(WebConstant.COMPETITION_TIME_OUT_ATTRIBUTE_NAME);
        if (endTime <= Calendar.getInstance().getTime().getTime()) {
            throw new ServiceLogicException("比赛已经结束");
        }

        Competition competition = competitionService.getCompetitionById(competitionAccount.getCompetitionId());
        List<Problem> competitionProblems = competitionService.getCompetitionProblems(competition.getCompetitionProblemIds());
        List<ProblemResponse> problemResponses = BeanMapperUtil.mapList(competitionProblems, ProblemResponse.class);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("competitionId", competitionAccount.getCompetitionId())//
                .append("competitionProblems", problemResponses)//
                .append("accountRemark", competitionAccount.getAccountRemark());
        return responseMap;
    }

    @RequestMapping(value = "/submitCompetitionProblemAnswer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap submitCompetitionProblemAnswer(@Valid CompetitionProblemAnswerVO vo, HttpSession session) {

        // 判断是否超时了
        long endTime = (long) session.getAttribute(WebConstant.COMPETITION_TIME_OUT_ATTRIBUTE_NAME);
        if (endTime <= Calendar.getInstance().getTime().getTime()) {
            throw new ServiceLogicException("比赛已经结束");
        }

        // 本来这种验证，应该是用拦截器来写的，但是这里省事就不写了
        CompetitionAccount competitionAccount = (CompetitionAccount) session.getAttribute(WebConstant.COMPETITION_ACCOUNT_ATTRIBUTE_NAME);
        if (competitionAccount == null) {
            throw new ServiceLogicException("请登录");
        }

        CompetitionProblemAnswerDTO dto = BeanMapperUtil.map(vo, CompetitionProblemAnswerDTO.class);
        dto.setCompetitionAccount(competitionAccount);
        competitionService.submitCompetitionProblemAnswer(dto);

        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap logoutCompetition(HttpSession session) {
        session.removeAttribute(WebConstant.COMPETITION_ACCOUNT_ATTRIBUTE_NAME);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 每30分钟清洗一次申请令牌
        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<Integer, Token> entry : applyTokenCache.entrySet()) {
                    Token applyToken = getApplyToken(entry.getKey());
                    applyTokenCache.put(entry.getKey(), applyToken);
                }
            }
        }, 0, 30, TimeUnit.MINUTES);

        // 每5分钟清洗一次登录令牌
        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<Integer, Token> entry : loginTokenCache.entrySet()) {
                    loginTokenCache.put(entry.getKey(), getLoginToken(entry.getKey()));
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    @SuppressWarnings("unused")
    private static class Token {
        String key;
        String dataId;
        long beginTime;
        long endTime;
        // 最后一次使用这个token的时间
        long lastNeedTime;

        public Token() {
        }

        public Token(String key, String dataId, long beginTime, long endTime) {
            this.key = key;
            this.dataId = dataId;
            this.beginTime = beginTime;
            this.endTime = endTime;
        }

    }

}
