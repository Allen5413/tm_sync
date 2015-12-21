package com.zs.dao.finance.spotexpensebuy;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.SpotExpenseBuy;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LihongZhang on 2015/5/14.
 */
public interface SpotExpenseBuyDao extends EntityJpaDao<SpotExpenseBuy,Long>{
    /**
     * 查询一个学期订单明细在财务信息记录漏的学习中心
     * @param semesterId
     * @return
     * @throws Exception
     */
    @Query(nativeQuery = true, value = "select DISTINCT t.spot_code from (" +
            "select tmpo.spot_code, sum(potm.count*potm.tm_price) price from teach_material_place_order tmpo, place_order_teach_material potm " +
            "where tmpo.id = potm.order_id and tmpo.order_status > '3' and potm.tm_price > 0 and potm.count > 0 and tmpo.semester_id = ?1 " +
            "group by spot_code) t,(" +
            "select seb.spot_code, sum(seb.money) price from spot_expense_buy seb " +
            "where seb.semester_id = ?1 " +
            "group by spot_code " +
            ") t2 where t.spot_code = t2.spot_code and t.price > t2.price")
    public List<Object> findLeaveOutSpotOrderTM(long semesterId)throws Exception;

    /**
     * 查询一个中心一个学期的订单明细没有记录消费的信息
     * @param semesterId
     * @param studentCode
     * @return
     * @throws Exception
     */
    @Query(nativeQuery = true, value = "select * FROM " +
            "(select tmpo.spot_code, potm.teach_material_id, potm.tm_price, potm.count " +
            "from teach_material_place_order tmpo, place_order_teach_material potm " +
            "where tmpo.id = potm.order_id and tmpo.order_status > '3' and potm.count > 0 and potm.tm_price > 0 " +
            "and tmpo.spot_code = ?2 and tmpo.semester_id = ?1 ) t where not EXISTS " +
            "(select * from " +
            "(select seb.spot_code, seb.teach_material_id " +
            "from spot_expense_buy seb " +
            "where seb.spot_code = ?2 and seb.semester_id = ?1 ) t2 " +
            "where t.spot_code = t2.spot_code and t.teach_material_id = t2.teach_material_id)")
    public List<Object[]> findLeaveOutSpotOrderTMBySpotCode(long semesterId, String spotCode)throws Exception;
}
