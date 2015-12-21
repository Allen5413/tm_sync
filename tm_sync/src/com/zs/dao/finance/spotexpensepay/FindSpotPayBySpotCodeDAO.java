package com.zs.dao.finance.spotexpensepay;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.SpotExpensePay;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/12/21.
 */
public interface FindSpotPayBySpotCodeDAO extends EntityJpaDao<SpotExpensePay, Long> {
    @Query(nativeQuery = true, value = "SELECT sep.spot_code, sum(money) FROM spot_expense_pay sep " +
            "where sep.spot_code = ?1")
    public Object[] find(String spotCode);
}
