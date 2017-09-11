package com.zs.service.sale.studentbookorder;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.sale.StudentBookOrder;

/**
 * Created by Allen on 2017/5/16.
 */
public interface EditOrderForSendService extends EntityService<StudentBookOrder> {
    public void edit(String orderCodes);
}
