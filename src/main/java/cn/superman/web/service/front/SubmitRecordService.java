package cn.superman.web.service.front;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.superman.constant.ConstantParameter;
import cn.superman.system.service.dto.ProblemJudgeResultItem;
import cn.superman.util.BeanMapperUtil;
import cn.superman.util.EncryptUtility;
import cn.superman.util.JsonUtil;
import cn.superman.util.Log4JUtil;
import cn.superman.web.dao.SubmitRecordDao;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.po.SubmitRecord;
import cn.superman.web.po.User;
import cn.superman.web.service.page.PageQuery;
import cn.superman.web.service.page.PageQuery.OrderByType;
import cn.superman.web.service.page.PageResult;
import cn.superman.web.vo.request.SubmitRecordVO;

@Service
public class SubmitRecordService {

    @Autowired
    private SubmitRecordDao submitRecordDao;

    public SubmitRecordVO getSubmitDetails(BigInteger submitId, String tableName) {
        SubmitRecord record = new SubmitRecord();
        record.setSubmitId(submitId);
        record.setSubmitRecordTableName(tableName);
        List<SubmitRecord> list = submitRecordDao.findWithCondition(record);
        if (list == null || list.size() < 1) {
            throw new RuntimeException("无法找到该提交记录");
        }
        SubmitRecord submitRecord = list.get(0);
        SubmitRecordVO vo = BeanMapperUtil.map(submitRecord, SubmitRecordVO.class);

        // 取出具体代码内容放到VO中
        try {
            String code = FileUtils.readFileToString(new File(submitRecord.getCodeFilePath()));
            // 格式清洗一下， 因为运行的时候，将其动态修改了类名，这里要统一返回用户提交时的类名：Main
            Pattern p = Pattern.compile("class (.*)Main");
            Matcher matcher = p.matcher(code);
            if (matcher.find()) {
                // 这是旧的类名
                String oldClassName = matcher.group();
                // 替换类名并替换一下回车,因为网页不认空格，这里再替换一下空格,这样再前端显示的代码，就是原生有格式的代码了
                code = code.replace(oldClassName, "class Main").replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;");
                vo.setCode(code);
            }
        } catch (IOException e) {
            Log4JUtil.logError(e);
            vo.setCode("代码文件丢失");
        }

        // 解释details中的内容，封装到相应的item中,如果题目还没判定的话，这里是无法转换的，将会报错
        try {
            List<ProblemJudgeResultItem> items = JsonUtil.toListBean(submitRecord.getDetails(), ProblemJudgeResultItem[].class);
            vo.setItems(items);
        } catch (Exception e) {
        }

        return vo;
    }

    public File decodeToFileByFilePath(String filePath) {
        try {
            String path = EncryptUtility
                    .AESDencoding(ConstantParameter.PROBLEM_STANDARD_FILE_PATH_SEED, new String(Base64Utils.decodeFromUrlSafeString(filePath)));
            File file = new File(path);
            if (!file.exists()) {
                throw new RuntimeException("无法获取来文件");
            }

            return file;
        } catch (Exception e) {
            Log4JUtil.logError(e);
            throw new ServiceLogicException("无法获取来文件");
        }
    }

    public PageResult<SubmitRecord> getPage(int pageShowCount, int wantPaegNumber, User user) {
        wantPaegNumber = wantPaegNumber < 1 ? 1 : wantPaegNumber;
        PageQuery<SubmitRecord> pageQuery = new PageQuery<SubmitRecord>();
        pageQuery.setWantPageNumber(wantPaegNumber);
        pageQuery.setWantPageShowCount(pageShowCount);
        pageQuery.setOrderByAttributeName("submit_time");
        pageQuery.setOrderByType(OrderByType.DESC);

        SubmitRecord condition = new SubmitRecord();
        condition.setSubmitUserId(user.getUserId());
        condition.setSubmitRecordTableName(user.getSubmitRecordTableName());
        pageQuery.setConditionEntity(condition);
        return getPageResult(pageQuery);
    }

    private PageResult<SubmitRecord> getPageResult(PageQuery<SubmitRecord> pageQuery) {
        PageResult<SubmitRecord> pageResult = new PageResult<SubmitRecord>();

        if (pageQuery.getOrderByAttributeName() == null) {
            PageHelper.startPage(pageQuery.getWantPageNumber(), pageQuery.getWantPageShowCount());
        } else {
            PageHelper.startPage(pageQuery.getWantPageNumber(), pageQuery.getWantPageShowCount(), pageQuery.getOrderByAttributeName() + " "
                    + pageQuery.getOrderByType().name());
        }

        List<SubmitRecord> list = submitRecordDao.findWithCondition(pageQuery.getConditionEntity());

        PageInfo<SubmitRecord> info = new PageInfo<SubmitRecord>(list);
        pageResult.setResult(list);
        pageResult.setTotalCount(info.getTotal());
        pageResult.setCurrentPage(info.getPageNum());
        pageResult.setTotalPage(info.getPages());
        return pageResult;
    }
}
