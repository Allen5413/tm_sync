package com.zs.dao;

import com.feinno.framework.common.dao.jpa.JapDynamicQueryDao;
import com.feinno.framework.common.dao.support.PageInfo;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.util.Assert;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 执行动态sql
 * Created by Allen on 2015/5/11.
 */
public class BaseQueryDao extends JapDynamicQueryDao {

    /**
     * 执行sql原生方法，可以返回任何字段，不受entity的影响
     * @param sql
     * @param args
     * @return
     */
    protected List sqlQueryByNativeSql(String sql, Object... args) {
        Session session = super.entityManager.unwrap(org.hibernate.Session.class);
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        if(args != null && args.length != 0) {
            for(int i = 0; i < args.length; ++i) {
                sqlQuery.setParameter(i, args[i]);
            }
        }
        return sqlQuery.list();
    }

    protected PageInfo pageSqlQueryByNativeSql(PageInfo pageInfo, String sql, String field, Object... args) {
        long totalCount = this.queryCount(true, sql, args);
        pageInfo.setTotalCount(totalCount);
        Session session = super.entityManager.unwrap(org.hibernate.Session.class);
        SQLQuery sqlQuery = session.createSQLQuery("select "+field+" "+sql);
        if(args != null && args.length != 0) {
            for(int i = 0; i < args.length; ++i) {
                sqlQuery.setParameter(i, args[i]);
            }
        }
        sqlQuery.setMaxResults(pageInfo.getCountOfCurrentPage());
        sqlQuery.setFirstResult(pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1));
        pageInfo.setPageResults(sqlQuery.list());
        return pageInfo;
    }

    protected PageInfo pagedQueryByJpql(PageInfo pageInfo, String ql, Object... args) {
        long totalCount = this.queryCount(false, ql, args);
        pageInfo.setTotalCount(totalCount);
        Query query = this.entityManager.createQuery(ql);
        if(args != null && args.length != 0) {
            for(int i = 0; i < args.length; ++i) {
                query.setParameter(i+1, args[i]);
            }
        }

        query.setMaxResults(pageInfo.getCountOfCurrentPage());
        query.setFirstResult(pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1));
        pageInfo.setPageResults(query.getResultList());
        return pageInfo;
    }

    protected PageInfo queryByNativeSql(Class resultClass, PageInfo pageInfo, String sql, Object... args) {
        long totalCount = this.queryCount(true, sql, args);
        pageInfo.setTotalCount(totalCount);
        Query query = this.entityManager.createNativeQuery(sql, resultClass);
        if(args != null && args.length != 0) {
            for(int i = 0; i < args.length; ++i) {
                query.setParameter(i+1, args[i]);
            }
        }

        query.setMaxResults(pageInfo.getCountOfCurrentPage());
        query.setFirstResult(pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1));
        pageInfo.setPageResults(query.getResultList());
        return pageInfo;
    }
    
    protected void batchInsert(List list, int num)throws Exception{
        try {
            if(null != list && 0 < list.size()) {
                int tmp = 1;
                for (int i = 0; i < list.size(); i++) {
                    Session session = super.entityManager.unwrap(org.hibernate.Session.class);
                    session.persist(list.get(i));
                    if (tmp == num) {
                        session.flush();
                        session.clear();
                        tmp = 1;
                    }
                    tmp++;
                }
            }
        }catch(Exception e){
            throw e;
        }
    }

    protected void batchUpdate(List list, int num)throws Exception{
        try {
            if(null != list && 0 < list.size()) {
                int tmp = 1;
                for (int i = 0; i < list.size(); i++) {
                    Session session = super.entityManager.unwrap(org.hibernate.Session.class);
                    session.merge(list.get(i));
                    if (tmp == num) {
                        session.flush();
                        session.clear();
                        tmp = 1;
                    }
                    tmp++;
                }
            }
        }catch(Exception e){
            throw e;
        }
    }

    private long queryCount(boolean nativeSql, String ql, Object... args) {
        Query query = null;
        if(nativeSql) {
            String countQueryString = "select count(1) from (select 1 " + ql + ") tempAlias";
            query = super.entityManager.createNativeQuery(countQueryString);
        } else {
            String countQueryString = "select count(*) " + removeSelect(removeOrders(ql));
            query = super.entityManager.createQuery(countQueryString);
        }

        if(args != null && args.length != 0) {
            for(int i = 0; i < args.length; ++i) {
                query.setParameter(i+1, args[i]);
            }
        }
        if(nativeSql) {
            BigInteger result = (BigInteger)(query.getSingleResult());
            return Long.valueOf(result.toString());
        } else {
            return ((Long)query.getSingleResult()).longValue();
        }
    }

    private long queryHqlCount(boolean nativeSql, String ql, Object... args) {
        String countQueryString = "select count(*) " + removeSelect(removeOrders(ql));
        Query query = null;
        if(nativeSql) {
            query = super.entityManager.createNativeQuery(countQueryString);
        } else {
            query = super.entityManager.createQuery(countQueryString);
        }

        if(args != null && args.length != 0) {
            for(int i = 0; i < args.length; ++i) {
                query.setParameter(i+1, args[i]);
            }
        }
        if(nativeSql) {
            BigInteger result = (BigInteger)(query.getSingleResult());
            return Long.valueOf(result.toString());
        } else {
            return ((Long)query.getSingleResult()).longValue();
        }
    }

    private static String removeSelect(String hql) {
        Assert.hasText(hql);
        int beginPos = hql.toLowerCase().indexOf("from");
        Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword \'from\'");
        return hql.substring(beginPos);
    }

    private static String removeOrders(String hql) {
        Assert.hasText(hql);
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();

        while(m.find()) {
            m.appendReplacement(sb, "");
        }

        m.appendTail(sb);
        return sb.toString();
    }
} 
