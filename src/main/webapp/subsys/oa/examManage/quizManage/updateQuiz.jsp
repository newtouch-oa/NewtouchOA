<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
%>
<html>
<head>
<title>新增培训记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
function doInit() {
  doType();
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamQuizAct/showQuiz.act?seqId=" + seqId;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  //alert(rsText);
  if (prc.seqId) {
    $("seqId").value = prc.seqId;
    $("roomId").value = prc.roomId;
    $("questionsType").value = prc.questionsType;
    $("questionsRank").value = prc.questionsRank;
    $("questions").value = prc.questions;
    $("answerA").value = prc.answerA;
    $("answerB").value = prc.answerB;
    $("answerC").value = prc.answerC;
    $("answerD").value = prc.answerD;
    $("answerE").value = prc.answerE;
    $("answers").value = prc.answers;
  }
}
function doType(){
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "roomId"
              , tableName: "EXAM_QUIZ_SET"
              , codeField: "SEQ_ID"
              , nameField: "ROOM_NAME"
              , value: "0", isMustFill: "1"
              , filterField: " "
              , filterValue: ''
              , order: ""
              , reloadBy: ""
              , actionUrl: ""
              });
  mgrSec.loadData();
  mgrSec.bindData2Cntrl();
}

function doSubmit(){
  if($("questions").value.trim() == ""){ 
    alert("题目不能为空！");
    $("questions").focus();
    $("questions").select();
    return false;
  }
  var pars = $('form1').serialize() ;
  var requestURL = contextPath + "/yh/subsys/oa/examManage/act/YHExamQuizAct/updateQuiz.act";
  var json = getJsonRs(requestURL,pars);
  if (json.rtState == '1') {
    alert(json.rtMsrg);
    return ; 
  } else {
      window.location.href = contextPath + "/subsys/oa/examManage/quizManage/manage.jsp";
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">新建试题</span>
    </td>
  </tr>
</table>
<form method="post" name="form1" id="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.examManage.data.YHExamQuiz">
<input type="hidden" name="seqId" id="seqId" value="">
<table class="TableBlock" width="70%" align="center">
    <tr>
      <td nowrap class="TableData"> 所属题库：</td>
      <td class="TableData">
        <select name="roomId" id="roomId">
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 题型：</td>
      <td class="TableData">
        <select name="questionsType" id="questionsType">
            <option value="0" >单选</option>
            <option value="1" >多选</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 难度：</td>
      <td class="TableData">
        <select name="questionsRank" id="questionsRank">
  <option value="0" >低</option>
  <option value="1" >中</option>
  <option value="2" >高</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 题目：<font style='color:red'>*</font></td>
      <td class="TableData">
         <textarea name="questions" id="questions" class="BigInput" cols="50" rows="3"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选答案A：</td>
      <td class="TableData">
        <input type="text" name="answerA" id="answerA" size="40" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选答案B：</td>
      <td class="TableData">
        <input type="text" name="answerB" id="answerB" size="40" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选答案C：</td>
      <td class="TableData">
        <input type="text" name="answerC" id="answerC" size="40" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选答案D：</td>
      <td class="TableData">
        <input type="text" name="answerD" id="answerD" size="40" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选答案E：</td>
      <td class="TableData">
        <input type="text" name="answerE" id="answerE" size="40" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 正确答案：</td>
      <td class="TableData">
        <input type="text" name="answers" id="answers" size="10" maxlength="100" class="BigInput" value=""> 
        <FONT color=red>注：单个答案直接输入字母，如A；多个答案连续输入字母，如ABC。 </FONT>
      </td>
    </tr>
   <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="保存" class="BigButton" onClick="doSubmit();">&nbsp;&nbsp;
<input type="button" value="返回" class="BigButton" onClick="javascript:history.back();">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>