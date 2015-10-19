package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.SpotProvince;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/10/19.
 */
public interface SpotProvinceDAO extends EntityJpaDao<SpotProvince, Long> {

    /**
     * 查询学习中心关联的省中心编号
     * @param spotCode
     * @return
     */
    @Query("FROM SpotProvince WHERE spotCode = ?1")
    public SpotProvince findBySpotCode(String spotCode);
}
