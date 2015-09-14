package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Query;

/**
 * 查询一个学期，订单号最大的一个，下次生成订单号时，累计后面流水号
 * Created by Allen on 2015/5/13.
 */
public interface FindStudentBookOrderForMaxCodeDAO extends EntityJpaDao<StudentBookOrder, Long> {
    @Query(nativeQuery = true, value = "select t.* from (select * from student_book_order where semester_id = ?1 order by RIGHT(order_code,6) desc limit 1) t")
    public StudentBookOrder getStudentBookOrderForMaxCode(Long semesterId);
}
