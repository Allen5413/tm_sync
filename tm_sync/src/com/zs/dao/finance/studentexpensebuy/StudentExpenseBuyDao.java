package com.zs.dao.finance.studentexpensebuy;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.StudentExpenseBuy;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/10/20.
 */
public interface StudentExpenseBuyDao extends EntityJpaDao<StudentExpenseBuy,Long> {

    /**
     * 查询一个学期订单明细在财务信息记录漏的学生
     * @param semesterId
     * @return
     * @throws Exception
     */
    @Query(nativeQuery = true, value = "select DISTINCT t.student_code from " +
            "(" +
            "select sbo.student_code, sum(sbotm.count*sbotm.price) price from student_book_order sbo, student_book_order_tm sbotm " +
            "where sbo.order_code = sbotm.order_code and sbo.state > 3 and sbotm.price > 0 and sbotm.count > 0 and sbo.semester_id = ?1 " +
            "group by student_code" +
            ") t," +
            "(" +
            "select seb.student_code, sum(seb.money) price from student_expense_buy seb " +
            "where seb.semester_id = ?1 " +
            "group by student_code " +
            ") t2 where t.student_code = t2.student_code and t.price > t2.price")
    public List<Object> findLeaveOutStudentOrderTM(long semesterId)throws Exception;

    /**
     * 查询一个学生一个学期的订单明细没有记录消费的信息
     * @param semesterId
     * @param studentCode
     * @return
     * @throws Exception
     */
    @Query(nativeQuery = true, value = "select * FROM " +
            "(" +
            "select sbotm.teach_material_id, sbotm.price, sbotm.count from student_book_order sbo, student_book_order_tm sbotm " +
            "where sbo.order_code = sbotm.order_code and sbo.state > 3 and sbotm.count > 0 and sbotm.price > 0 " +
            "and sbo.student_code = ?2 and sbo.semester_id = ?1 " +
            ") t where not EXISTS(" +
            "select * from " +
            "(" +
            "select seb.student_code, seb.teach_material_id from student_expense_buy seb where seb.student_code = ?2 and seb.semester_id = ?1 " +
            ") t2 where t.student_code = t2.student_code and t.teach_material_id = t2.teach_material_id" +
            ")")
    public List<Object[]> findLeaveOutStudentOrderTMByStudentCode(long semesterId, String studentCode)throws Exception;
}
