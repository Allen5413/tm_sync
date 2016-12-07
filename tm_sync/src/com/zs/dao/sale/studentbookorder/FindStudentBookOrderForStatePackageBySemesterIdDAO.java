package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/12/7.
 */
public interface FindStudentBookOrderForStatePackageBySemesterIdDAO extends EntityJpaDao<StudentBookOrder, Long> {
    @Query("FROM StudentBookOrder where semesterId = ?1 and state = 3")
    public List<StudentBookOrder> find(long semesterId);
}
