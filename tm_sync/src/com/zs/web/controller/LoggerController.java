package com.zs.web.controller;

import com.feinno.framework.common.dao.support.PageInfo;
import com.feinno.framework.common.domain.AbstractEntity;
import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityService;
import com.feinno.framework.common.web.AbstractJQueryEntityController;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志输出
 * Created by Allen on 2015/5/7.
 */
public class LoggerController<T extends AbstractEntity, M extends EntityService<T>>
        extends AbstractJQueryEntityController<T, M> {
    private int defaultPageSize = 10;

    protected PageInfo<T> getPageInfo(HttpServletRequest request) {
        PageInfo pageinfo = new PageInfo();
        pageinfo.setCountOfCurrentPage(defaultPageSize);
        String page = request.getParameter("page");
        if(StringUtils.isNotBlank(page)) {
            pageinfo.setCurrentPage(Integer.parseInt(page));
        }
        String rows = request.getParameter("rows");
        if(StringUtils.isNotBlank(rows)) {
            pageinfo.setCountOfCurrentPage(Integer.parseInt(rows));
        }
        return pageinfo;
    }


    public String outputException(HttpServletRequest request, Exception e, Logger log, String msg){
        if (e instanceof BusinessException) {
            String eMsg = e.getMessage();
            if(-1 < eMsg.indexOf("StaleObjectStateException")){
                log.info("您操作的数据已经被修改，请重新获取最新的数据再做操作！");
                request.setAttribute("errorMsg", "您操作的数据已经被修改，请重新获取最新的数据再做操作！");
                eMsg = "您操作的数据已经被修改，请重新获取最新的数据再做操作！";
            }else{
                log.info(e.getMessage());
                request.setAttribute("errorMsg", eMsg);
            }
            return eMsg;
        } else {
            request.setAttribute("errorMsg", msg+", 操作失败");
            super.handleException(msg+", 操作失败", e, request);
            return msg+", 操作失败";
        }
    }
}
