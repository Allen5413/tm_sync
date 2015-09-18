package com.zs.dao.basic.finance.spotexpenseoth;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.SpotExpenseOth;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/9/18.
 */
public interface FindBySpotCodeAndSemesterDAO extends EntityJpaDao<SpotExpenseOth, Long> {
    @Query("from SpotExpenseOth where spotCode = ?1 and semesterId = ?2")
    public SpotExpenseOth findBySpotCodeAndSemester(String studentCode, long semesterId);
}
