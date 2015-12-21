package com.zs.dao.finance.spotexpenseoth;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.SpotExpenseOth;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/12/21.
 */
public interface SetSpotExpenseOthDAO extends EntityJpaDao<SpotExpenseOth, Long> {
    /**
     * 重新计算该中心的数据情况
     * @param code
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT " +
            "t2. CODE, sum(ifnull(t1.pay, 0)), sum(ifnull(t1.buy, 0)), " +
            "sum(case when ifnull(t1.pay, 0) - ifnull(t1.buy, 0) > 0 then 0 else ifnull(t1.buy, 0) - ifnull(t1.pay, 0) end), " +
            "sum(case when ifnull(t1.pay, 0) - ifnull(t1.buy, 0) > 0 then ifnull(t1.pay, 0) - ifnull(t1.buy, 0) else 0 end), " +
            "t1.semester_id " +
            "FROM student_expense t1, sync_spot t2, sync_student t6 " +
            "WHERE t1.student_code = t6.code AND t6.spot_code = t2. CODE " +
            "and t2.code = ?1 " +
            "GROUP BY t1.semester_id")
    public List<Object[]> findCountSetData(String code);
}
