package com.zs.dao.sale.studentbookordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderTM;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 当课程关联的教材发生改变时，学生订单明细中的教材也要更换
 * Created by Allen on 2015/7/11.
 */
public interface EditStudentBookOrderTMForTMDAO extends EntityJpaDao<StudentBookOrderTM, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE student_book_order_tm sbotm INNER JOIN student_book_order sbo ON sbotm.order_code = sbo.order_code SET teach_material_id = ?1, price = ?2 WHERE sbo.state < 4 AND sbotm.course_code = ?3 AND sbotm.teach_material_id = ?4")
    public void editStudentBookOrderTMForTM(long newTMId, float newPrice, String courseCode, long oldTMId)throws  Exception;
}
