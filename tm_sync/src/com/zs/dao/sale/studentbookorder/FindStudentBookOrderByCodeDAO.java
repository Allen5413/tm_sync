package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/7/22.
 */
public interface FindStudentBookOrderByCodeDAO extends EntityJpaDao<StudentBookOrder, Long> {
    @Query("FROM StudentBookOrder where orderCode = ?1")
    public StudentBookOrder findStudentBookOrderByCode(String orderCode);
}
