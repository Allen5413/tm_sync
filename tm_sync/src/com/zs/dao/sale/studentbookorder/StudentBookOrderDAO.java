package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/5/12.
 */
public interface StudentBookOrderDAO extends EntityJpaDao<StudentBookOrder, Long> {

    /**
     * 查询学生一个学期的未确认的订单
     * @param studentCode
     * @param semesterId
     * @return
     * @throws Exception
     */
    @Query("FROM StudentBookOrder WHERE studentCode = ?1 AND semesterId = ?2 AND state = 0")
    public List<StudentBookOrder> findByStudentCodeAndSemesterIdForUnconfirmed(String studentCode, Long semesterId)throws Exception;

    /**
     * 查询学生一个学期的未分拣的订单
     * @param studentCode
     * @param semesterId
     * @return
     * @throws Exception
     */
    @Query("FROM StudentBookOrder WHERE studentCode = ?1 AND semesterId = ?2 AND state < 2")
    public List<StudentBookOrder> findByStudentCodeAndSemesterIdForSorting(String studentCode, Long semesterId)throws Exception;

    /**
     * 查询学生一个学期的订单
     * @param studentCode
     * @param semesterId
     * @return
     * @throws Exception
     */
    @Query("FROM StudentBookOrder WHERE studentCode = ?1 AND semesterId = ?2")
    public List<StudentBookOrder> findByStudentCodeAndSemesterId(String studentCode, Long semesterId)throws Exception;
}
