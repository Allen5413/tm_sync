package com.zs.dao.kuaidi.push;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.kuaidi.KuaidiPush;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/9/30.
 */
public interface KuaidiPushDAO extends EntityJpaDao<KuaidiPush, Long> {
    @Query("FROM KuaidiPush WHERE nu = ?1")
    public KuaidiPush findByNu(String nu);
}
