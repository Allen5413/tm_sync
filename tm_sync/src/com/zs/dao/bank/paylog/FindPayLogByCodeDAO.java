package com.zs.dao.bank.paylog;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.bank.BankPayLog;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/10/28.
 */
public interface FindPayLogByCodeDAO extends EntityJpaDao<BankPayLog, Long> {
    @Query("from BankPayLog where code = ?1")
    public BankPayLog find(String code);
}
