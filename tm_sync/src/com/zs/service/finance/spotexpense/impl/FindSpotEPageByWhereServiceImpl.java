package com.zs.service.finance.spotexpense.impl;

import com.feinno.framework.common.dao.support.PageInfo;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.FindPageByWhereDAO;
import com.zs.service.finance.spotexpense.FindSpotEPageByWhereService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by LihongZhang on 2015/5/17.
 */
@Service
public class FindSpotEPageByWhereServiceImpl extends EntityServiceImpl implements FindSpotEPageByWhereService {

    @Resource
    private FindPageByWhereDAO findSpotEPageByWhereDao;

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public PageInfo findPageByWhere(PageInfo pageInfo, Map<String, String> map, Map<String, Boolean> sortMap) throws Exception {
        pageInfo = findSpotEPageByWhereDao.findPageByWhere(pageInfo, map, sortMap);
        if(null != pageInfo && null != pageInfo.getPageResults() && 0 < pageInfo.getPageResults().size()){
            List<Object[]> list = pageInfo.getPageResults();
            JSONArray jsonArray = new JSONArray();
            for(Object[] objs : list){
                //JSON日期转换必须要传bean，所以这里先把日期format后在设置值
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("spotCode", objs[0]);
                jsonObject.put("spotName", objs[1]);
                jsonObject.put("pay", objs[2]);
                jsonObject.put("buy", objs[3]);
                jsonObject.put("confirmedMoney", objs[4]);
                jsonObject.put("acc", new BigDecimal((Float)objs[2]).subtract(new BigDecimal((Float)objs[3])).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
                jsonObject.put("semester", objs[7]+"年"+("0".equals(objs[8].toString()) ? "上半年":"下半年"));
                jsonArray.add(jsonObject);
            }
            pageInfo.setPageResults(jsonArray);
        }
        return pageInfo;
    }
}
