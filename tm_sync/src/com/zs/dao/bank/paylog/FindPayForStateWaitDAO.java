package com.zs.dao.bank.paylog;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.bank.BankPayLog;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/5/3.
 */
public interface FindPayForStateWaitDAO extends EntityJpaDao<BankPayLog, Long> {
    @Query("from BankPayLog where state = 0 or state = 3")
    public List<BankPayLog> find();
}
