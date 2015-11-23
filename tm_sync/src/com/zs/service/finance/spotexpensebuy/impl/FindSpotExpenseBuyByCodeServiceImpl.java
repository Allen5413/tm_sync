package com.zs.service.finance.spotexpensebuy.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.FindListByWhereDAO;
import com.zs.domain.finance.SpotExpenseBuy;
import com.zs.service.finance.spotexpensebuy.FindSpotExpenseBuyByCodeService;
import com.zs.tools.DateJsonValueProcessorTools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2015/6/8.
 */
@Service("findSpotExpenseBuyByCodeService")
public class FindSpotExpenseBuyByCodeServiceImpl
    extends EntityServiceImpl
    implements FindSpotExpenseBuyByCodeService {

    @Resource
    private FindListByWhereDAO findSpotExpenseBuyByCodeDAO;

    @Override
    @Transactional
    public JSONObject getSpotExpenseBuyByCode(Map<String, String> paramsMap, Map<String, Boolean> sortMap) {
        JSONObject resultJSON = new JSONObject();

        List<Object[]> list = findSpotExpenseBuyByCodeDAO.findListByWhere(paramsMap, sortMap);
        if(null != list && 0 < list.size()){
            for(Object[] objs : list){
                String semester = objs[0]+"年"+("0".equals(objs[1].toString()) ? "上半年" : "下半年");
                Double totalBuy = null;
                JSONObject jsonObject2 = null;
                JSONArray jsonArray = null;
                if(null == resultJSON.get(semester)){
                    jsonObject2 = new JSONObject();
                    jsonObject2.put("0", new JSONArray());
                }else{
                    jsonObject2 = (JSONObject)resultJSON.get(semester);
                }

                //得到之前该学期下的值
                Iterator it = jsonObject2.keys();
                while(it.hasNext()){
                    totalBuy = Double.valueOf(it.next().toString());
                    jsonArray = (JSONArray)jsonObject2.get(totalBuy+"");
                    if(null == jsonArray){
                        jsonArray = new JSONArray();
                    }
                }

                //重新计算新的值
                JSONObject jsonObject = new JSONObject();
                //JSON日期转换必须要传bean，所以这里先把日期format后在设置值
                SpotExpenseBuy spotExpenseBuy = new SpotExpenseBuy();
                spotExpenseBuy.setCreateTime((Date) objs[2]);
                JsonConfig jsonConfig = new JsonConfig();
                jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessorTools());
                jsonObject = JSONObject.fromObject(spotExpenseBuy, jsonConfig);
                jsonObject.put("createTime", "null".equals(jsonObject.get("createTime").toString()) ? "" : jsonObject.get("createTime"));
                jsonObject.put("type", objs[3]);
                jsonObject.put("money", objs[4]);
                jsonObject.put("detail", objs[5]);

                totalBuy = new BigDecimal(totalBuy).add(new BigDecimal(objs[4].toString())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                jsonArray.add(jsonObject);
                jsonObject2 = new JSONObject();
                jsonObject2.put(totalBuy+"", jsonArray);

                //存入新的值
                resultJSON.put(semester, jsonObject2);
            }
        }
        return resultJSON;
    }
}
