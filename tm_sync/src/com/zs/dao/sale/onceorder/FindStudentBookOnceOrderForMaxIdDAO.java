package com.zs.dao.sale.onceorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrder;
import org.springframework.data.jpa.repository.Query;

/**
 * 查询id最大的一个
 * Created by Allen on 2016/6/16.
 */
public interface FindStudentBookOnceOrderForMaxIdDAO extends EntityJpaDao<StudentBookOnceOrder, Long> {
    @Query(nativeQuery = true, value = "select * from student_book_once_order order by id desc limit 1")
    public StudentBookOnceOrder find();
}
