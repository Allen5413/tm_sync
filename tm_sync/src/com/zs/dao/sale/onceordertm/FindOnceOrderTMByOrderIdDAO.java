package com.zs.dao.sale.onceordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrderTM;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2017/5/16.
 */
public interface FindOnceOrderTMByOrderIdDAO extends EntityJpaDao<StudentBookOnceOrderTM, Long> {
    @Query("select sbotm from StudentBookOnceOrderTM sbotm, TeachMaterial tm where sbotm.teachMaterialId = tm.id and tm.state = 0 and sbotm.orderId = ?1")
    public List<StudentBookOnceOrderTM> find(long orderId);
}
