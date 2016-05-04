/**
 * 智能代付单笔查询接口
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.zs.epaysdk.service.py;


import com.zs.epaysdk.common.Configure;
import com.zs.epaysdk.common.SignAlgorithm;
import com.zs.epaysdk.service.IPostService;
import com.zs.epaysdk.util.DateTimeUtil;
import com.zs.epaysdk.util.Signature;

import java.util.Map;

public class PyQuery extends IPostService {

    private static final String SERVICE_NAME = "cib.epay.payment.get";
    private static final String SERVICE_VER = "02";

    @Override
    public String exec(Map<String, String> params) {

        params.put("appid", Configure.getAppid());
        params.put("service", SERVICE_NAME);
        params.put("ver", SERVICE_VER);
        params.put("timestamp", DateTimeUtil.getDateTime());
        params.put("sign_type", SignAlgorithm.get(SERVICE_NAME));
        params.put("mac", Signature.generateMAC(params));

        return txn(Configure.isDevEnv() ? Configure.PY_DEV_API : Configure.PY_PROD_API, params);
    }
}
