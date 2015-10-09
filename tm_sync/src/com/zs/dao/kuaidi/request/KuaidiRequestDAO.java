package com.zs.dao.kuaidi.request;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.kuaidi.KuaidiRequest;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/9/30.
 */
public interface KuaidiRequestDAO extends EntityJpaDao<KuaidiRequest, Long> {

    /**
     * 根据快递单号，查询快递单号请求的数据
     * @param number
     * @return
     */
    @Query("FROM KuaidiRequest WHERE number = ?1")
    public List<KuaidiRequest> findByNumber(String number);
}
