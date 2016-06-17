package com.zs.dao.sale.studentbookonceorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrder;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2016/6/17.
 */
public interface FindStudentBookOnceOrderByStudentCodeForUnConfirmDAO extends EntityJpaDao<StudentBookOnceOrder, Long> {

    @Query("FROM StudentBookOnceOrder WHERE studentCode = ?1 and state = 0 ")
    public StudentBookOnceOrder find(String code);
}
