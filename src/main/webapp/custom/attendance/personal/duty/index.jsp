<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>值班登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/custom/attendance/personal/duty/js/dutyLogic.js"></script>
<script type="text/javascript">
function addDuty(){
  window.location.href = "<%=contextPath%>/custom/attendance/personal/duty/addDuty.jsp";
}
function deleteOvertime(seqId){
  var msg='确认要删除该加班信息吗？';
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/custom/attendance/act/YHOvertimeRecordAct/delOvertimeById.act?seqId=" + seqId;
    var rtJson = getJsonRs(requestURL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location.reload();
  }
}
function historyOvertime(){
  window.location.href = "<%=contextPath%>/custom/attendance/personal/duty/historyDuty.jsp";
}
function update(seqId){
  location = "<%=contextPath%>/custom/attendance/personal/duty/modify.jsp?seqId="+seqId;
}

function doOnload(){
  var requestURL = "<%=contextPath%>/yh/custom/attendance/act/YHDutyAct/getDutyJsonList.act";
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>申请时间</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>值班原因</td>"
      + "<td nowrap align='center'>开始日期</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
      + "<td nowrap align='center'>状态</td>"
      + "<td nowrap align='center'>操作</td></tr></tbody>");
    $('overtime').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr');
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var status = prcs.status;
      var reason = prcs.reason;
      var statusDesc = "待批";
      var statusOptDesc = ""
      if(status=='2'){
        statusDesc = "未批准";
      }
      if(seqId){  
          tr.update("<td nowrap align='center'>" + prcs.dutyTime.substr(0,10) + "</td>"
              + "<td nowrap align='center'>" + leaderName + "</td>"
              + "<td  align='center'>" + prcs.dutyDesc + "</td>"
              + "<td nowrap align='center'>" + prcs.dutyTime.substr(0,10) + "</td>"
              + "<td nowrap align='center'>" + prcs.beginDate + "</td>"
              + "<td nowrap align='center'>" + prcs.endDate + "</td>"
              + "<td nowrap align='center' title=" + reason + "> " + statusDesc + " </td>"
              + "<td nowrap align='center'>" 
              + "<a href=javascript:update("+seqId+") title='提醒：修改提交后，需要重新审批'>修改</a> &nbsp;&nbsp;"
              + "<a href='#' onclick = 'deleteSingle(" + seqId + ")'>删除</a>"
              + "</td>");
        }
      $('tboday').appendChild(tr);
    }
  }
}
</script>
</head>
 
<body topmargin="5" onload="doOnload();">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;值班登记</span><br>
    </td>
  </tr>
</table>
 
<br>
<div align="center">
<input type="button"  value="值班登记" class="BigButton" onClick="addDuty();" title="值班登记">&nbsp;&nbsp;
<input type="button"  value="值班登记历史记录" class="BigButtonC" onClick="historyOvertime();" title="值班登记历史记录">

</div>
<br></br>
<div id="overtime"></div>
</body>
</html>