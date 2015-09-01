<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<tr>
  <td class="tbg" colspan="20">
    <p class="right tablePages">
      <a href="#" onclick="pageSub('start')">首页</a>
      <a class="prev" href="#" onclick="pageSub('up')">上一页</a>
      ${pageInfo.currentPage}
      <a class="next" href="#" onclick="pageSub('down')">下一页</a>
      <a href="#" onclick="pageSub('end')">尾页</a> &nbsp; &nbsp;
      <span>转到第 <input class="input_30" type="text" id="changePage" /> 页</span> <a class="ptrun" href="#" onclick="pageSub('change')">转</a>
    </p>
    <p class="tNum">
      共 ${pageInfo.totalCount} 条记录，每页<input type="text" id="rows" name="rows" style="width: 30px;" value="${pageInfo.countOfCurrentPage}" />条，共${pageInfo.totalPage}页
    </p>
  </td>
</tr>
<script>
  $(function(){
    $("#rows").keyup(function(){
      var rowsVal = $("#rows").val();
      if(isNaN(rowsVal) || 1 > rowsVal) {
        $("#rows").val(rowsVal.substring(0, rowsVal.length-1))
      }
    });
  });
  function pageSub(flag){
    var isSub = true;
    var currentPage = "${pageInfo.currentPage}";
    if(flag == "start"){
      $("#page").val(1);
    }
    if(flag == "up"){
      if(currentPage > 1){
        $("#page").val(currentPage-1);
      }else{
        isSub = false;
        alert("已经是第一页");
      }
    }
    if(flag == "down"){
      if(currentPage < ${pageInfo.totalPage}){
        currentPage = parseInt(currentPage);
        $("#page").val(currentPage+1);
      }else{
        isSub = false;
        alert("已经是最后一页");
      }
    }
    if(flag == "end"){
      $("#page").val("${pageInfo.totalPage}");
    }
    if(flag == "change"){
      if($("#changePage").val() > 0 && $("#changePage").val() <= "${pageInfo.totalPage}") {
        $("#page").val($("#changePage").val());
      }else{
        isSub = false;
        alert("跳转的页数不正确");
      }
    }
    if(isSub){
      pageForm.submit();
    }
  }
</script>