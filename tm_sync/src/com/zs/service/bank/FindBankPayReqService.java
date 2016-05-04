package com.zs.service.bank;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.bank.BankSearchLog;
import net.sf.json.JSONObject;

/**
 * Created by Allen on 2016/5/4.
 */
public interface FindBankPayReqService extends EntityService<BankSearchLog> {
    public String find(String orderNo)throws Exception;
}
