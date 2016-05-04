<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@ include file="/common/meta.jsp"%>
  <%@ include file="/common/taglibs.jsp"%>
</head>
<body>
<form action="${pageContext.request.contextPath}/findBankPayReq/req.htm">
  订单号：<input type="text" id="orderNo" name="orderNo">
  <a href="#" onclick="sub()">提交</a>
</form>
<div id="content"></div>
</body>
</html>
<script>
  function sub(){
    var orderNo = $("#orderNo").val();
    $.ajax({
      url:"${pageContext.request.contextPath}/findBankPayReq/req.htm",
      method : 'POST',
      async:false,
      data:{"orderNo":orderNo},
      success:function(data){
        if(data.state == 1){
          alert(data.msg);
        }else{
          $("#content").html(JSON.stringify(data.result));
        }
      }
    });
  }
</script>
