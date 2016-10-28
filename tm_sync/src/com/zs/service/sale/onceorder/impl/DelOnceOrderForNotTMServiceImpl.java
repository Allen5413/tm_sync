package com.zs.service.sale.onceorder.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.sale.onceorder.DelOnceOrderForNotTMDAO;
import com.zs.dao.sale.onceorderlog.DelOnceOrderLogForNotTMDAO;
import com.zs.domain.sale.StudentBookOnceOrder;
import com.zs.service.sale.onceorder.DelOnceOrderForNotTMService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Allen on 2016/10/28.
 */
@Service("delOnceOrderForNotTMService")
public class DelOnceOrderForNotTMServiceImpl extends EntityServiceImpl<StudentBookOnceOrder, DelOnceOrderForNotTMDAO>
    implements DelOnceOrderForNotTMService{

    @Resource
    private DelOnceOrderForNotTMDAO delOnceOrderForNotTMDAO;
    @Resource
    private DelOnceOrderLogForNotTMDAO delOnceOrderLogForNotTMDAO;


    @Override
    @Transactional
    public void del() {
        delOnceOrderForNotTMDAO.del();
        delOnceOrderLogForNotTMDAO.del();
    }
}
