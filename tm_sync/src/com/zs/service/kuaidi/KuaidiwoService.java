package com.zs.service.kuaidi;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Allen on 2015/9/1.
 */
public interface KuaidiwoService {
    /**
     * 查询EMS快递信息(JSON)
     * @param cno 快递单号
     * @return json格式的数据
     */
    public JSONObject queryForEMSByJson(String cno);

}
