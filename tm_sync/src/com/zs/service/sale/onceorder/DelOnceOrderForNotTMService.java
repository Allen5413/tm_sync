package com.zs.service.sale.onceorder;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.sale.StudentBookOnceOrder;

/**
 * Created by Allen on 2016/10/28.
 */
public interface DelOnceOrderForNotTMService extends EntityService<StudentBookOnceOrder> {
    public void del();
}
