package cn.superman.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superman.util.BeanMapperUtil;
import cn.superman.web.service.page.PageResult;
import cn.superman.web.service.page.PageService;

/**
 * 需要特别注意的是，T和V这两个类之间对应的属性，应该用相同的名字，要不同名字的话，要属性上加上@Mapping注解，并配置相应的名字，不然值将无法封装进去
 *
 * @author lianghaohui
 * @param <T> 数据库查出的数据，用于封装数据的类的泛型
 * @param <E> 数据库进行查询时，用于封装查询条件的类的泛型
 * @param <V> 返回给前端时，用于封装返回数据库的类的泛型
 */
public abstract class PageController<T, E, V> {
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<V> list(@RequestParam("pageShowCount") int pageShowCount, @RequestParam("wantPageNumber") int wantPageNumber) {
        return map(getPageService().getPage(pageShowCount, wantPageNumber));
    }

    @RequestMapping(value = "/listWithCondition", method = RequestMethod.GET)
    @ResponseBody
    public PageResult<V> listWithCondition(@RequestParam("pageShowCount") int pageShowCount, @RequestParam("wantPageNumber") int wantPageNumber, E condition) {
        return map(getPageService().getPage(pageShowCount, wantPageNumber, condition));
    }

    private PageResult<V> map(PageResult<T> pageResult) {
        PageResult<V> result = new PageResult<V>();
        result.setCurrentPage(pageResult.getCurrentPage());
        result.setTotalCount(pageResult.getTotalCount());
        result.setTotalPage(pageResult.getTotalPage());
        result.setResult(BeanMapperUtil.mapList(pageResult.getResult(), returnVoClass()));

        return result;
    }

    public abstract PageService<T, E> getPageService();

    public abstract Class<V> returnVoClass();
}
