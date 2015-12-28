package com.zs.dao.kuaidi.push;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.kuaidi.KuaidiPush;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/9/30.
 */
public interface KuaidiPushDAO extends EntityJpaDao<KuaidiPush, Long> {
    /**
     * 通过快递号查询推送信息
     * @param nu
     * @return
     */
    @Query("FROM KuaidiPush WHERE nu = ?1")
    public KuaidiPush findByNu(String nu);

    /**
     * 查询描述里已经签收的，但是状态还推送是3的
     * @return
     */
    @Query("FROM KuaidiPush where data like '%已签收%' and state != 3")
    public List<KuaidiPush> findByDataAndState();
}
