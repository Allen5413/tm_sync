<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@ include file="/common/meta.jsp"%>
  <%@ include file="/common/taglibs.jsp"%>
</head>
<body>
<form action="${pageContext.request.contextPath}/kuaidiReq/req.htm">
  <input type="hidden" name="com" value="ems" />
  快递号：<input type="text" id="nu" name="nu">
  <a href="#" onclick="sub()">提交</a>
</form>
<br />
<form action="http://www.kuaidi100.com/poll" method="post">
  参数：<textarea name="param" rows="5" cols="150">{"company":"ems","number":"9920051849015","key":"bwgCJUSX1701","parameters":{"callbackurl":"http://xiwang.attop.com:8080/kuaidiPush/push.htm"}}</textarea>
  <input type="submit" value="直接请求快递100" />
</form>
</body>
</html>
<script>
  function sub(){
    var nu = $("#nu").val();
    $.ajax({
      url:"${pageContext.request.contextPath}/kuaidiReq/req.htm",
      method : 'POST',
      async:false,
      data:{"com":"ems", "nu":nu},
      success:function(data){
        if(data.state == 1){
          alert("请求异常！");
        }else{
          alert(JSON.stringify(data.kuaidiRequest));
        }
      }
    });
  }
</script>
