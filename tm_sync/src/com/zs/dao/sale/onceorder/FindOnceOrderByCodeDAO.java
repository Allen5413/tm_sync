package com.zs.dao.sale.onceorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrder;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2017/5/16.
 */
public interface FindOnceOrderByCodeDAO extends EntityJpaDao<StudentBookOnceOrder,Long> {

    @Query("from StudentBookOnceOrder where orderCode = ?1")
    public StudentBookOnceOrder find(String code);
}