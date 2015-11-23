package com.zs.dao.sync.spot;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.Spot;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/6/1.
 */
public interface FindSpotByProvCodeDAO extends EntityJpaDao<Spot, Long> {
    @Query(nativeQuery = true, value = "select s.* from sync_spot s, sync_spot_province sp where s.code = sp.spot_code and sp.province_code = ?1 order by s.code")
    public List<Spot> getSpotByProvCode(String provCode);
}
