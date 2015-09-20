package com.zs.service.basic.issuerange.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.issuerange.FindIssueRangeBySpotCodeDAO;
import com.zs.domain.basic.IssueRange;
import com.zs.service.basic.issuerange.FindIssueRangeBySpotCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Allen on 2015/5/26.
 */
@Service("findIssueRangeBySpotCodeService")
public class FindIssueRangeBySpotCodeServiceImpl extends EntityServiceImpl<IssueRange, FindIssueRangeBySpotCodeDAO>
    implements FindIssueRangeBySpotCodeService{

    @Resource
    private FindIssueRangeBySpotCodeDAO findIssueRangeBySpotCodeDAO;

    @Override
    public IssueRange getIssueRangeBySpotCode(String spotCode) {
        return findIssueRangeBySpotCodeDAO.getIssueRangeBySpotCode(spotCode);
    }
}
