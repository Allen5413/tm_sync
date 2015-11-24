package com.zs.dao.sync.spot;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.Spot;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/6/4.
 */
public interface FindSpotAllDAO extends EntityJpaDao<Spot, Long> {
    @Query("from Spot order by code")
    public List<Spot> getSpotAll();
}
