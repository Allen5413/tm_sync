<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@ include file="/common/meta.jsp"%>
  <%@ include file="/common/taglibs.jsp"%>
</head>
<body>
  <table width="100%" border="1">
    <tr>
      <td>status</td>
      <td>message</td>
      <td>state</td>
      <td>com</td>
      <td>nu</td>
      <td>data</td>
      <td>operateTime</td>
    </tr>
    <c:forEach var="data" items="${list}">
      <tr>
        <td>${data.status}</td>
        <td>${data.message}</td>
        <td>${data.state}</td>
        <td>${data.com}</td>
        <td>${data.nu}</td>
        <td>${data.data}</td>
        <td>${data.operateTime}</td>
      </tr>
    </c:forEach>
  </table>
</body>
</html>
