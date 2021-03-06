/**
 * 无跳转，直接POST通讯方式的接口
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.zs.epaysdk.service;

import com.zs.epaysdk.comm.IRequestService;
import com.zs.epaysdk.common.Configure;
import com.zs.epaysdk.util.Signature;

import java.io.IOException;
import java.util.Map;


public abstract class IPostService {

    /**
     * 通讯接口，返回报文为json格式
     *
     * @param params post待发送的包含所有参数的Map
     * @return JSON报文
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws java.io.IOException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.net.MalformedURLException
     * @throws java.security.KeyManagementException
     */
    protected static String txn(String url, Map<String, String> params) {

        String json = null;
        try {
            IRequestService requestor = IRequestService.getInstance();
            json = (String) requestor.sendPost(url, params);
            if (Configure.isNeedChkSign() && !Signature.verifyMAC(Signature.jsonToMap(json))) {
                return Configure.SIGN_ERROR_RESULT;
            }
        } catch (IOException e) {
            json = Configure.TXN_ERROR_RESULT;
        } catch (Exception e) {
            e.printStackTrace();
            json = Configure.SYS_ERROR_RESULT;
        }
        return json;
    }

    /**
     * 外层调用接口，只传入需要用户输入的参数，并补全所有参数
     *
     * @param params
     * @return JSON报文
     */
    public abstract String exec(Map<String, String> params);
}
