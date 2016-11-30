package com.zs.service.scheduler;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.sale.StudentBookOnceOrder;

import java.util.Map;

/**
 * Created by Allen on 2016/6/16.
 */
public interface SyncStudentOnceOrderService extends EntityService<StudentBookOnceOrder> {
    public void sync()throws Exception;

    public void syncTempAdjust(Map<String, String> map)throws Exception;

    public void syncNewStudentForNotOrder(String... studentCodes)throws Exception;
}
