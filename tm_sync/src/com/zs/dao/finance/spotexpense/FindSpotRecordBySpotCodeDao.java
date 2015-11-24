package com.zs.dao.finance.spotexpense;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.SpotExpense;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 根据学习中心编号查询学习中心费用信息
 * Created by LihongZhang on 2015/5/16.
 */
public interface FindSpotRecordBySpotCodeDao extends EntityJpaDao<SpotExpense,Long> {

    @Query("from SpotExpense where spotCode = ?1 order by semesterId")
    public List<SpotExpense> getSpotEBySpotCode(String spotCode);

    @Query("from SpotExpense where spotCode = ?1 and semesterId = ?2")
    public SpotExpense getSpotEBySpotCode(String spotCode, long semesterId);
}
