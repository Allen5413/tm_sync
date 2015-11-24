package com.zs.dao.basic.teachmaterialstock.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.FindListByWhereDAO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2015/5/20.
 */
@Service("findTeachMaterialStockBytmIdDAO")
public class FindTeachMaterialStockBytmIdDAOImpl extends BaseQueryDao implements FindListByWhereDAO {
    @Override
    public List<Object[]> findListByWhere(Map<String, String> paramsMap, Map<String, Boolean> sortMap) {

        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select tms.id, ic.id channelId, ic.name, tms.stock from ");
        sbSql.append("teach_material_stock tms, issue_channel ic where ");
        sbSql.append("tms.issue_channel_id = ic.id and tms.teach_material_id = ?");

        String tmId = paramsMap.get("tmId");

        List<Object> params = new ArrayList<Object>();
        params.add(Long.parseLong(tmId));

        if(null != sortMap) {
            sbSql.append("order by ");
            int i = 0;
            for (Iterator it = sortMap.keySet().iterator(); it.hasNext(); ) {
                if(0 < i){
                    sbSql.append(",");
                }
                String key = it.next().toString();
                sbSql.append(key);
                sbSql.append(" ");
                sbSql.append(sortMap.get(key) ? "asc" : "desc");
                i++;
            }
        }
        List<Object[]> list = super.sqlQueryByNativeSql(sbSql.toString(), params.toArray());
        return list;
    }
}
