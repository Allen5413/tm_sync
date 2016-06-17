package com.zs.service.scheduler;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.sale.StudentBookOnceOrder;

/**
 * Created by Allen on 2016/6/16.
 */
public interface SyncStudentOnceOrderService extends EntityService<StudentBookOnceOrder> {
    public void sync()throws Exception;
}
