package com.zs.dao.sale.onceorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/7/11.
 */
public interface FindOnceOrderByPackageIdDAO extends EntityJpaDao<StudentBookOnceOrder,Long> {
    @Query("from StudentBookOnceOrder where packageId = ?1")
    public List<StudentBookOnceOrder> find(long packageId);
}
