<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
<%--
  Created by IntelliJ IDEA.
  User: yulei
  Date: 2015/7/27
  Time: 19:44
--%>
<html>
<head>
<title></title>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    
    
%>
<link rel="stylesheet" href="<%=ctx %>/css/validationEngine.jquery.css">
<link rel="stylesheet" href="<%=ctx %>/css/bootstrap.css">

<link rel="stylesheet" href="<%=ctx %>/js/themes/bootstrap/easyui.css">
<link rel="stylesheet" href="<%=ctx %>/js/themes/icon.css">


<script type="text/javascript" src="<%=ctx %>/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=ctx %>/js/bootstrap.js"></script>
<script type="text/javascript" src="<%=ctx %>/js/languages/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="<%=ctx %>/js/jquery.validationEngine.js"></script>

<script type="text/javascript" src="<%=ctx %>/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=ctx %>/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=ctx %>/js/jquery.form.js"></script>
<script type="text/javascript" src="<%=ctx %>/js/utils.js"></script>
<script type="text/javascript" src="<%=ctx %>/js/datagrid-detailview.js"></script>
</head>
<body>
  <div id="utils" class="easyui-tabs" data-options="plain:true, fit:true">
    
    
  <div title="用友流水数据导入">
      <form class="container form-horizontal" method="post" action="<%=ctx %>/util/exeYongYouHis" id="yongyouHis">

        <div class="control-group">
          <label class="control-label"></label>
          <div class="controls">

          </div>
        </div>
        <div class="control-group">
        <label class="control-label"></label>
        <div class="controls">

        </div>
      </div>
        <div class="control-group">
          <label class="control-label">导入方式：<span style="color: red;">*</span></label>
          <div class="controls">
            <select  required="true" missingMessage="导入方式不能为空" class="easyui-validatebox"  style="height: auto;" 
                   name="yongyouFlag" id="yongyouFlag" >
                   <option value="1">全部导入</option>
                   <option value="2">部分导入</option>
             </select>
          </div>
        </div>
        <div class="control-group"></div>
        <div class="control-group "  id="yydt" style="display:none;" >
          <label class="control-label">流水日期：</label>
          		<input type="text" class="easyui-datebox" editable="false" style="width: 150px ; height: auto;" id="yongyouStartTime" name="yongyouStartTime">至
				<input type="text" class="easyui-datebox" editable="false" style="width: 150px ; height: auto;" id="yongyouEndTime" name="yongyouEndTime">
        </div>
          <div class="control-group"></div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="yongyouImport" class="btn btn-primary" value="导入"/>
        </div>
      </form>
    </div> 
    

    
  </div>
  <script>

    
     $("#yongyouFlag").change(function() {
        var yongyouFlag = $("#yongyouFlag").val();
        if (yongyouFlag == "1") {
          $("#yydt").css("display", "none");
          $("#yydt").attr("disabled", "disabled");
          return ;
        }
        if (yongyouFlag == "2") {
          $("#yydt").css("display", "block");
          $("#yydt").removeAttr("disabled");
        }
      });
    
    
    $("#yongyouImport").click(function(){
        var form = $("#yongyouHis");
        var validate = form.form('validate');
        var startTime = $("#yongyouStartTime").datebox("getValue");
        var endTime = $("#yongyouEndTime").datebox("getValue");
        var yongyouFlag = $("#yongyouFlag").val();
        var ckTime = true;
        if(yongyouFlag == "2"){
        	 ckTime = checkTime(startTime,endTime);
        }
        if(validate && ckTime){
          $.ajax({
            url:form.attr("action"),
            type:"post",
            data:form.serialize(),
            success:function(data){
            	
              if(data.result=='success')
                $.messager.alert('提示', '导入成功！！', 'info');
              else
             $.messager.alert('提示',data.errMsg, 'info');
            }
          });
        }
      });
    
    
    function checkTime(st,ed){
    	if(st==null || st=='' || ed==null || ed==''){
    		alert("开始时间和结束时间都不能为空！")
    		return false;
    	}
    	
    	var st_ = StringToDate(st);
    	var ed_ = StringToDate(ed);
    	if(st_.getTime()>ed_.getTime()){
    		alert("开始时间不能大于结束时间！")
    		return false;
    	}
    	return true;
    }
    
    function StringToDate(s) {
    	var d = new Date();
    	d.setYear(parseInt(s.substring(0,4),10));
    	d.setMonth(parseInt(s.substring(5,7)-1,10));
    	d.setDate(parseInt(s.substring(8,10),10));
    	return d;
    	}  
  </script>
</body>
</html>
