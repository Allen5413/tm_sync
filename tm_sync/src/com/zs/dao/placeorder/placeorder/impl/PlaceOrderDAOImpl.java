package com.zs.dao.placeorder.placeorder.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.placeorder.placeorder.PlaceOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("placeOrderDAO")
public class PlaceOrderDAOImpl extends BaseQueryDao implements PlaceOrderDAO{

	@Override
	public List<Object[]> querySpotCourseScheduMaterial(String spotCode,int enYear, int enQuarter, String specCode, String levelCode) {
		StringBuilder sql = new StringBuilder("select v1.* from (");
        //统计教材的
        sql.append("SELECT tm.id,tm.name,tm.price,sbs.course_code ");
        sql.append("from teach_material tm, teach_material_course tmc,sync_begin_schedule sbs,semester sem ");
        sql.append("WHERE tm.id = tmc.teach_material_id AND sbs.course_code = tmc.course_code AND sem.year = sbs.academic_year AND sem.quarter = sbs.term AND sem.is_now_semester = 0 AND sbs.enter_year = ? and sbs.quarter = ? and sbs.spec_code = ? and sbs.level_code = ?");
        //sql合并
        sql.append("UNION ALL ");
        //统计套教材的
        sql.append("SELECT tm.id,tm.name,tm.price,sbs.course_code ");
        sql.append("FROM teach_material tm,set_teach_material stm,set_teach_material_tm stmtm,sync_begin_schedule sbs,semester sem ");
        sql.append(" where tm.id = stmtm.teach_material_id AND stm.id = stmtm.set_teach_material_id AND stm.buy_course_code = sbs.course_code AND sem.year = sbs.academic_year AND sem.quarter = sbs.term AND sem.is_now_semester = 0 AND sbs.enter_year = ? AND sbs.quarter = ? AND sbs.spec_code = ? AND sbs.level_code = ?");
        sql.append(") v1");

        Object[] param = {enYear, enQuarter,specCode,levelCode,enYear,enQuarter,specCode,levelCode};

        List<Object[]> list = super.sqlQueryByNativeSql(sql.toString(), param);
        return list;
	}

	@Override
	public String queryMaxOrderNumber(String spotCode, long semesterId) {
		
		StringBuilder sql = new StringBuilder("select max(tmp.order_code) from teach_material_place_order tmp where tmp.spot_code = ? and tmp.semester_id = ?");
		Object[] param = {spotCode, semesterId};
		
		List list = super.sqlQueryByNativeSql(sql.toString(), param);
		if(null != list && list.size() > 0){
			if(null != list.get(0)){
				return (String)(list.get(0));
			}
		}
		
		return null;
	}

}
