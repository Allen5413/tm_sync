package com.zs.service.sale.studentbookorder;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.sale.StudentBookOrder;

/**
 * Created by Allen on 2016/12/7.
 */
public interface EditOrderForSendBySemesterIdService extends EntityService<StudentBookOrder> {
    public void edit(long semesterId)throws Exception;
}
