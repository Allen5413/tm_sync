 <%@ page language="java" contentType="text/html; charset=utf-8"
     pageEncoding="utf-8"%>
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <html>
 <head>
     <%@ include file="/common/meta.jsp"%>
     <%@ include file="/common/taglibs.jsp"%>
 </head>
 <body>
    ${user.name },你好！<br />
    <a href="${pageContext.request.contextPath}/addUser/openAddUserPage" >新增用户</a>
    <a href="${pageContext.request.contextPath}/editUser/openEditUserPage?id=1" >编辑用户</a>
    <a href="${pageContext.request.contextPath}/findUser/findUserByID?id=1">查看</a>
    <a href="${pageContext.request.contextPath}/findPageUser/findPageByWhere?page=1">查看列表</a>
    <br/><br/>
    <a href="${pageContext.request.contextPath}/addUserGroup/openAddUserGroupPage" >新增用户组</a>
    <a href="${pageContext.request.contextPath}/editUserGroup/openEditUserGroupPage?id=1" >编辑用户组</a>
    <a href="${pageContext.request.contextPath}/findUserGroup/find?id=1">查看</a>
    <a href="${pageContext.request.contextPath}/findPageUserGroup/find?page=1">查看列表</a>
    <br/><br/>
    <a href="${pageContext.request.contextPath}/findSTMPage/findSTMPageByWhere?page=1">查看套教材列表</a>
    <br/><br/>
    <a href="${pageContext.request.contextPath}/addPress/openAddPressPage" >新增出版社</a>
    <a href="${pageContext.request.contextPath}/editPress/openEditPressPage?id=1" >编辑出版社</a>
    <a href="${pageContext.request.contextPath}/findUserGroup/find?id=1">查看</a>
    <a href="${pageContext.request.contextPath}/findPageUserGroup/find?page=1">查看列表</a>
    <br/><br/>
    <a href="${pageContext.request.contextPath}/addTeachMaterialRatio/openAddTeachMaterialRatioPage">自动生成订单</a>
    <a href="${pageContext.request.contextPath}/findPurchaseOrderListByWhere/openFindPurchaseOrderList">查看采购单</a>
 </body>
 </html>