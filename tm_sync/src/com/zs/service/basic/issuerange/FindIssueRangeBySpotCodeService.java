package com.zs.service.basic.issuerange;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.basic.IssueRange;

/**
 * Created by Allen on 2015/5/26.
 */
public interface FindIssueRangeBySpotCodeService extends EntityService<IssueRange> {
    public IssueRange getIssueRangeBySpotCode(String spotCode);
}
