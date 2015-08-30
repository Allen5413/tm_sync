package com.zs.dao.sale.studentbookordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderTM;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

/**
 * 查询一个渠道下的一本教材在已确认还未发货的订单中的数量
 * Created by Allen on 2015/7/9.
 */
public interface FindTMCountForConfirmOrderDAO extends EntityJpaDao<StudentBookOrderTM, Long> {
    @Query(nativeQuery = true, value = "select sum(sbotm.count) from student_book_order sbo, student_book_order_tm sbotm where sbo.order_code = sbotm.order_code and sbo.is_stock = 0 and sbo.state BETWEEN 1 and 3 and sbotm.teach_material_id = ?1 and sbo.issue_channel_id = ?2")
    public BigDecimal findTMCountForConfirmOrder(long teachMaterialId, long issueChannelId);
}
