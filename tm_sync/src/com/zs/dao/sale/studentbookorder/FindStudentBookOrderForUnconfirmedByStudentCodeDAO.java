package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/6/16.
 */
public interface FindStudentBookOrderForUnconfirmedByStudentCodeDAO extends EntityJpaDao<StudentBookOrder, Long> {
    @Query("FROM StudentBookOrder WHERE studentCode = ?1 and state = 0")
    public List<StudentBookOrder> find(String studentCode);
}
