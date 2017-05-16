package com.zs.service.sale.onceorder;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.sale.StudentBookOnceOrder;

/**
 * Created by Allen on 2017/5/16.
 */
public interface EditOnceOrderForSendService extends EntityService<StudentBookOnceOrder> {
    public void edit(String orderCode);
}
