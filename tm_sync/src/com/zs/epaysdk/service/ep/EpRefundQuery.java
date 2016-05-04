/**
 * 快捷支付退款交易结果查询接口
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.zs.epaysdk.service.ep;

import com.zs.epaysdk.common.Configure;
import com.zs.epaysdk.common.SignAlgorithm;
import com.zs.epaysdk.service.IPostService;
import com.zs.epaysdk.util.DateTimeUtil;
import com.zs.epaysdk.util.Signature;

import java.util.Map;


public class EpRefundQuery extends IPostService {

    private static final String SERVICE_NAME = "cib.epay.acquire.easypay.refund.query";
    private static final String SERVICE_VER = "01";

    public String exec(Map<String, String> params) {

        if (!params.containsKey("order_date")) {
            params.put("order_date", DateTimeUtil.getDate());
        }
        params.put("appid", Configure.getAppid());
        params.put("service", SERVICE_NAME);
        params.put("ver", SERVICE_VER);
        params.put("timestamp", DateTimeUtil.getDateTime());
        params.put("sign_type", SignAlgorithm.get(SERVICE_NAME));
        params.put("mac", Signature.generateMAC(params));

        return txn(Configure.isDevEnv() ? Configure.EP_DEV_API : Configure.EP_PROD_API, params);
    }
}
