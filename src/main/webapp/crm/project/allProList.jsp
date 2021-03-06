<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Page pageobj=(Page)request.getAttribute("page"); 
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>所有项目</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/pro.js"></script>
    <script type="text/javascript">
		//初始化下拉框
		function loadStates(){
			 $("pType").value="${pType}";
			 $("pState").value="${pState}";
			 $("pStage").value="${pStage}";
		}
		
   		
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
			loadStates();
			createCancelButton('searchForm',-50,5);
			closeProgressBar();
		}
	</script>
  </head>
  
  <body>
      <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;项目管理 > 所有项目
            </div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeBlue1"  onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" onClick="selectProject(1)">我的项目</div> 
                            <div id="tabType2" class="tabTypeWhite"  onClick="selectProject(2)">所有项目</div> 
                                                           
                        </div>
                    </th>
                    <td><a href="javascript:void(0)" onClick="projPopDiv(1);return false;" class="newBlueButton">新建项目</a></td>
                </tr>
            </table>
		 	<div id="listContent">     
            	<div class="listSearch">
			 		<form id="searchForm" action="projectAction.do" method="get" >
                        <input type="hidden" name="op" value="searAllProject" />
                        项目名称：<input class="inputSize2 inputBoxAlign" type="text" name="pName" value="${pName}" style="width:100px"/>&nbsp;
                        项目类别：<logic:notEmpty name="projectType"><select id="pType" name="pType" class="inputBoxAlign" style="width:90px">
                                    <option value="">全部</option>
                                    <logic:iterate id="proType" name="projectType">
                                        <option value="${proType.typId}">${proType.typName}</option>
                                    </logic:iterate>
                                </select>
                            </logic:notEmpty>
                            <logic:empty name="projectType">
                                <select id="pType" class="inputBoxAlign" disabled>
                                    <option value="">未添加项目类别</option>
                                </select>
                            </logic:empty>
                         &nbsp;
						 项目状态：<select id="pState" name="pState" class="inputBoxAlign" style="width:90px">
							<option value="">全部</option>
							<option value="0">失败</option>
							<option value="1">有效</option>
							<option value="2">搁置</option>
							<option value="3">放弃</option>
                         </select>
						 &nbsp;
						 项目阶段：<select id="pStage" name="pStage" class="inputBoxAlign" style="width:90px">
							<option value="">全部</option>
							<option value="0">项目跟踪</option>
							<option value="1">签约实施</option>
							<option value="2">结项验收</option>
                            <option value="3">完成归档</option>
                         </select>&nbsp;&nbsp;
                         <button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                   </form>
				</div>	           
                <div class="dataList">
            		<table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                    	<tr>
                          	<th style="width:8%">项目状态</th>
                          	<th style="width:30%">项目名称</th>
                            <th style="width:12%">项目阶段</th>
                         	<th style="width:12%">项目类别</th>
                          	<th style="width:14%">立项日期</th>
                          	<th style="width:14%">预计完成日期</th>
                          	<th style="width:10%;border-right:0px">操作</th>
                        </tr>
                        <logic:notEmpty name="allProList">
                        	<logic:iterate id="project" name="allProList" scope="request">
                            <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('projectAction.do?op=showProDesc&proId=${project.proId}')">
                              	<td>
                                	<logic:equal value="0" name="project" property="proState">失败</logic:equal>
                                  	<logic:equal value="1" name="project" property="proState">有效</logic:equal>
                                  	<logic:equal value="2" name="project" property="proState">搁置</logic:equal>
                                  	<logic:equal value="3" name="project" property="proState">放弃</logic:equal>
                                  	
                              	</td>
                              	<td><a href="projectAction.do?op=showProDesc&proId=${project.proId}" style="cursor:pointer" target="_blank">${project.proTitle}</a>
                                </td>
                                <td>
                                <logic:equal value="0" name="project" property="proPeriod">项目跟踪</logic:equal>
                                <logic:equal value="1" name="project" property="proPeriod">签约实施</logic:equal>
                                <logic:equal value="2" name="project" property="proPeriod">结项验收</logic:equal>
                                <logic:equal value="3" name="project" property="proPeriod">完成归档</logic:equal>
                                </td>
                                <td>${project.proType.typName}&nbsp;</td>
                              	<td><span id="proCreDate<%=count%>"></span>&nbsp;</td>
                              	<td>
                                	<logic:notEmpty  name="project" property="proFinDate">
                                        <span id="proFinDate<%=count%>"></span>&nbsp;
                                    </logic:notEmpty>
                                    <logic:empty  name="project" property="proFinDate">
                                    	<span class="gray">未指定</span>
                                    </logic:empty>
                                </td>
							  	<td>
								 <a href="projectAction.do?op=showProDesc&proId=${project.proId}" target="_blank">
							<img style="cursor:pointer" src="images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
                            
							<img onClick="projPopDiv(11,'${project.proId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
							<img onClick="projDelDiv(1,'${project.proId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>
								</td>
                            </tr>
                            <script type="text/javascript">
						  		dateFormat('proCreDate','${project.proCreDate}',<%=count%>);
								dateFormat('proFinDate','${project.proFinDate}',<%=count%>);
								rowsBg('tr',<%=count%>);
	 	             	 	</script>
                            <%count++; %>
                          	</logic:iterate>
                       	</logic:notEmpty>
                        <logic:empty name="allProList">
                            <tr>
                                <td colspan="7" class="noDataTd">未找到相关数据!</td>
                            </tr>
                        </logic:empty>
                    </table>
                    <logic:notEmpty name="allProList">
					 	<logic:equal name="pageType" value="default">
							<script type="text/javascript">
                                createPage('projectAction.do?op=getAllProject','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                            </script>
						</logic:equal>
				 		<logic:equal name="pageType" value="searAllProject">
                        	<script type="text/javascript">
                                createPage('projectAction.do?op=searAllProject&pName='+ encodeURIComponent('${pName}') +'&pType=${pType}&pState=${pState}&pStage=${pStage}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                            </script>
						</logic:equal>
                    </logic:notEmpty>
		   		</div>			
       	  	</div>
  		</div> 
	</div>
  </body>
	<script type="text/javascript">loadTabTypeWidth();</script>
</html>
