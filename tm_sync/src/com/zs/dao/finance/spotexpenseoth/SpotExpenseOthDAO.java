package com.zs.dao.finance.spotexpenseoth;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.SpotExpenseOth;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * 
 * @author yanghaosen
 *
 */

/**
 * 
 * @author yanghaosen
 *
 */
public interface SpotExpenseOthDAO extends EntityJpaDao<SpotExpenseOth,Long>{
	
	@Query("select a from SpotExpenseOth a where a.spotCode = ?1 order by a.semesterId asc")
	public List<SpotExpenseOth> querySpotExpenseOthOnSemeter(String spotCode);
	
	@Query("select a from SpotExpenseOth a where a.semesterId = ?1 and a.spotCode = ?2")
	public List<SpotExpenseOth> querySpotExpenseOthBySemeter(long semeterId, String spotCode);
}
