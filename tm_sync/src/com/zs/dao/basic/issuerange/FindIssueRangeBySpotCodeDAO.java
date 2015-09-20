package com.zs.dao.basic.issuerange;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.basic.IssueRange;
import org.springframework.data.jpa.repository.Query;

/**
 * 通过学习中心编号，查询学习中心的发行渠道
 * Created by Allen on 2015/5/9.
 */
public interface FindIssueRangeBySpotCodeDAO extends EntityJpaDao<IssueRange,Long> {
    @Query("from IssueRange where spotCode = ?1")
    public IssueRange getIssueRangeBySpotCode(String spotCode);
}
