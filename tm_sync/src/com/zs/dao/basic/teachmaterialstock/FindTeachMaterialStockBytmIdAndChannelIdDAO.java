package com.zs.dao.basic.teachmaterialstock;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.basic.TeachMaterialStock;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/5/23.
 */
public interface FindTeachMaterialStockBytmIdAndChannelIdDAO extends EntityJpaDao<TeachMaterialStock, Long> {
    @Query("from TeachMaterialStock where teachMaterialId = ?1 and issueChannelId = ?2")
    public TeachMaterialStock getTeachMaterialStockBytmIdAndChannelId(long tmId, long channelId);
}
