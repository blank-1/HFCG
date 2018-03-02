<%@ page import="com.xt.cfp.core.constants.PublishTarget" %>
<%@ page import="com.xt.cfp.core.constants.PublishOpenTypeEnum" %>
<%@ page import="com.xt.cfp.core.constants.PublishRule" %>
<%--
  User: ren yulin
  Date: 15-7-18
  Time: 下午3:24
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <%
        String ctx = request.getContextPath();
        pageContext.setAttribute("ctx", ctx);
    %>
</head>
<body>
<form class="form-horizontal" id="loan_publish_form" method="post"   enctype="multipart/form-data"  >
    <input type="hidden" name="mainLoanApplicationId" id="mainLoanApplicationId" value="${loanApplicationId}">
    
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>本次发标标题：</label>
        <div class="controls">
            <input type="text" style="width: 400px"
                   required="true" class="easyui-validatebox" validType="length[2,20]"
                   name="thisPublishTitle" id="thisPublishTitle" value="${thisPublishTitle }">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>本次发标金额：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   required="true" class="easyui-numberbox"
                   name="thisPublishBalance" id="thisPublishBalance" >总借款金额：${mainLoanBalance}&nbsp;&nbsp;已发标金额：${mainPublishBalance}
        </div>
    </div>
    
    
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>信用等级：</label>

        <div class="controls">
            <select id="creditLevel" name="creditLevel">
                <option value="">=请选择=</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
            </select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>投放渠道：</label>

        <div class="controls">
            <input type="hidden" id="publishTarget" name="publishTarget" value="<%=PublishTarget.FRONT.getValue()%>">
            <div class="btn-group" data-toggle="buttons-radio">
                <input type="button" class="btn" id="front" value="前台投标" onclick="javascript:$('#publishTarget').val('<%=PublishTarget.FRONT.getValue()%>')">
                <input type="button" class="btn" id="background" value="后台债权匹配" onclick="javascript:$('#publishTarget').val('<%=PublishTarget.BACKGROUND.getValue()%>')">

            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>开标类型：</label>

        <div class="controls">
            <input type="hidden" id="openType" name="openType" value="<%=PublishOpenTypeEnum.NOWOPEN.getValue()%>">
            <div class="btn-group" data-toggle="buttons-radio">
                <input type="button" class="btn" id="nowOpen"
                       onclick="$('#openTimeDiv').hide();$('#openType').val('<%=PublishOpenTypeEnum.NOWOPEN.getValue()%>')" value="即时开标">
                <input type="button" class="btn" id="specOpen"
                       onclick="$('#openTimeDiv').show();$('#openType').val('<%=PublishOpenTypeEnum.SPECOPEN.getValue()%>')" value="定时开标">
            </div>
        </div>
    </div>
    <div id="openTimeDiv" style="display: none">
        <div class="control-group">
            <label class="control-label"><span style="color: red;">*</span>预热时间：</label>
            <div class="controls">
                <input type="text" class="easyui-datetimebox" data-options="showSeconds:false" name="preheatTime" id="preheatTime" editable="false"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><span style="color: red;">*</span>开标时间：</label>
            <div class="controls">
                <input type="text" class="easyui-datetimebox"  data-options="showSeconds:false"  name="openTime" id="openTime" editable="false" />
            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>投标期限：</label>

        <div class="controls">
            <input type="text"
                   required="true"  class="easyui-numberbox"
                   name="bidingDays" id="bidingDays" value="5">天
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>单人最大可投：</label>

        <div class="controls">
            <input type="text"
                   required="true"  class="easyui-numberbox"
                   name="maxBuyBalance" id="maxBuyBalance" >
        </div>
    </div>
    
     <div class="control-group">
        <label class="control-label"><span style="color: red;"></span>优先机制：</label>
        <div class="controls">
        	<input type="radio" id="onlyManual" name="publishRule" value="<%=PublishRule.ONLY_MANUAL.getValue() %>" checked="checked">仅手动</input>&nbsp;&nbsp;
        	<input type="radio" id="firstAutomatic" name="publishRule" value="<%=PublishRule.FIRST_AUTOMATIC.getValue() %>">省心优先</input>&nbsp;&nbsp;
        	<input type="radio" id="onlyAutomatic" name="publishRule" value="<%=PublishRule.ONLY_AUTOMATIC.getValue() %>">仅省心</input>&nbsp;&nbsp;
       </div>
     </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>奖励：</label>

        <div class="controls">
            <div class="btn-group" data-toggle="buttons-radio">
                <input type="button" class="btn" id="noAward" onclick="javascript:$('#rateSpan').hide();$('#awardRate').attr('value','0');" value="无">
                <input type="button" class="btn" id="haveAward" onclick="javascript:$('#rateSpan').show();$('#awardPoint').combobox('reload');$('#awardRate').attr('value','0');" value="有">
            </div>
            <span id="rateSpan" style="display: none">
            <input type="text" style="width: 50px"
                   required="true"  class="easyui-numberbox"
                   name="awardRate" id="awardRate" value="0" precision="1">%
                <input id="awardPoint" name="awardPoint" />
                </span>


        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;">*</span>发标描述：</label>

        <div class="controls">
            <textarea rows="6" cols="60" style="width: 500px;height: 200px" name="desc" id="desc">${desc}</textarea>
        </div>
    </div>

	<!-- 定向设置（开始） -->
	<div class="control-group">
		<label class="control-label"><span style="color: red;">*</span>定向设置：</label>
		<div class="controls">
			<input class="directional_set" id="radioInputAll" checked="checked"  type="radio" name="radioInput" >所有用户</input>
		</div>
		<div class="controls">
			<input class="directional_set" id="radioInputPerson" type="radio" value="radioInputPerson" name="radioInput" >定向密码</input>
			<input id="radioInputP"  type="text"  name="opassword" ></input>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label"><span style="color: red;"></span> </label>
		<div class="controls">
			<input class="directional_set" id="radioInputOnly" type="radio" value="radioInputOnly" name="radioInput" >定向用户</input>
			<input id="radioInputA1"    type="file" name="file" ></input>
		</div>
	</div>
	        
	<div class="control-group">
		<label class="control-label"><span style="color: red;"></span> </label>
		<div class="controls">
			<input class="directional_set" id="UserRadio" type="radio" name="radioInput" >新手用户</input>
			<input id="newUserRadio"   value=""  type="hidden" name="newUserRadio" ></input>
		</div>
	</div>
	<!-- 定向设置（结束） -->

</form>
<script language="JavaScript">

/**判断select **/  
  $("#radioInputA1").hide();
  $("#radioInputP").hide();
  //定向用户
 $("#radioInputPerson").click(function(){
	 $("#radioInputA1").hide();
	 $("#radioInputP").show();
 });
 //定向密码
  $("#radioInputOnly").click(function(){
	  $("#radioInputP").hide();
	  $("#radioInputA1").show();
 });
 //定向 所有
  $("#radioInputAll").click(function(){
	  $("#radioInputP").hide();
	  $("#radioInputA1").hide();
 });
  //新手用户
  $("#UserRadio").click(function(){
	  $("#radioInputP").hide();
	  $("#radioInputA1").hide();
	  $("#newUserRadio").val("newUserRadio");
 });
  
//[优先机制]-仅手动
$("#onlyManual").click(function(){
	$(".directional_set").attr("disabled",false);//定向设置全部可用
	$("#radioInputAll").attr("checked",true);//所有用户选中
	$("#radioInputP").hide();
	$("#radioInputA1").hide();
});
//[优先机制]-省心优先
$("#firstAutomatic").click(function(){
	$(".directional_set").attr("disabled",true);//定向设置全部禁用
	$("#radioInputAll").attr("disabled",false);//所有用户可用
	$("#radioInputAll").attr("checked",true);//所有用户选中
	$("#radioInputP").hide();
	$("#radioInputA1").hide();
	$("#newUserRadio").attr("value","");
});
//[优先机制]-仅省心
$("#onlyAutomatic").click(function(){
	$(".directional_set").attr("disabled",true);//定向设置全部禁用
	$("#radioInputAll").attr("disabled",false);//所有用户可用
	$("#radioInputAll").attr("checked",true);//所有用户选中
	$("#radioInputP").hide();
	$("#radioInputA1").hide();
	$("#newUserRadio").attr("value","");
});
 
	var js_mainLoanBalance = '${mainLoanBalance}';
	var js_mainPublishBalance = '${mainPublishBalance}';

    $("#loan_publish_form").form({
        url:'${ctx}/jsp/loanPublish/loan/publish',
        onSubmit:function(data) {
        	if($('#rateSpan').is(":hidden")){//无奖励
        		$('#awardRate').numberbox('setValue', 0);
        		$('#awardPoint').combobox('setValue', '');
        	}else{//有奖励
        	    if($('#awardRate').val() == 0){
        	    	$.messager.alert("系统提示", "请填写奖励利率！", "info");
    				return false;
        	    }
        		if($('#awardPoint').combobox('getValue') == ''){
        			$.messager.alert("系统提示", "请填写奖励发放时机！", "info");
    				return false;
        		}
        	}
        	var js_thisPublishBalance = $('#thisPublishBalance').val();
        	var js_maxBuyBalance = $('#maxBuyBalance').val();
			if(js_thisPublishBalance > (js_mainLoanBalance - js_mainPublishBalance)){
				$.messager.alert("系统提示", "本次发标金额，不能大于剩余金额！", "info");
				return false;
			}
			if(js_maxBuyBalance - js_thisPublishBalance > 0){
				$.messager.alert("系统提示", "最大单人可投金额，不能大于本次发标金额！", "info");
				return false;
			}
        },
        success:function(data) {
            if (data == 'success') {
                $.messager.alert("系统提示", "借款申请发标成功！", "info",function(){
                    parent.$("#loan_publish").dialog('close');
                    opener.$("#loan_app_list").datagrid("reload");//主列表
                    parent.$("#contract_list").datagrid("reload");//子列表
                    //window.close();
                });
            } else  if(data == 'repart'){
            	 $.messager.alert("系统提示", "定向用户信息与数据库不匹配！", "info");
            }else if(data == 'fail'){
            	 $.messager.alert("系统提示", "定向设置失败！", "info");
            }else if(data == 'notEquals'){
            	 $.messager.alert("系统提示", "定向用户姓名不正确！", "info");
            }
            else {
                $.messager.alert("系统提示", "借款申请发标失败！", "info");
            }
        }
    });
    $("#awardPoint").combobox({
        url:'${ctx}/jsp/loanPublish/loan/loadAwardPoint',
        valueField:'constantValue',
        textField:'constantName'
    })
    $('#awardPoint').hide();
</script>
</body>
</html>
