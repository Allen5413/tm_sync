package com.zs.dao.sync.spot;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.Spot;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/5/10.
 */
public interface FindSpotByCodeDAO extends EntityJpaDao<Spot, Long> {
    @Query("from Spot where code = ?1")
    public Spot getSpotByCode(String code);
}
