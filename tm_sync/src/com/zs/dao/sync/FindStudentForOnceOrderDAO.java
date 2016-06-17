package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.Student;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 查询能够满足生成一次性订单的学生
 * Created by Allen on 2016/6/16.
 */
public interface FindStudentForOnceOrderDAO extends EntityJpaDao<Student, Long> {
    @Query("FROM Student where isOnceOrder = 1")
    public List<Student> find();
}
