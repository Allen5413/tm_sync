package com.zs.service.finance.spotexpensebuy;

import com.feinno.framework.common.service.EntityService;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * Created by Allen on 2015/6/8.
 */
public interface FindSpotExpenseBuyByCodeService extends EntityService {
    public JSONObject getSpotExpenseBuyByCode(Map<String, String> paramsMap, Map<String, Boolean> sortMap);
}
