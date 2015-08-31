package com.zs.dao.basic.kuaidi;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.basic.Kuaidi;
import org.springframework.data.jpa.repository.Query;

/**
 * 通过快递单号查询信息
 * Created by Allen on 2015/8/31.
 */
public interface FindKuaidiByNuDAO extends EntityJpaDao<Kuaidi,Long> {
    @Query("from Kuaidi where nu = ?1")
    public Kuaidi findKuaidiByNu(String nu);
}
