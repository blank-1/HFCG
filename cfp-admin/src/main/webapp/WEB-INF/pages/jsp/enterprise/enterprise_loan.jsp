<%@ page import="com.xt.cfp.core.constants.SubjectTypeEnum" %>
<%@ page import="com.xt.cfp.core.constants.LoanTypeEnum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<body>
<div id="enterprise_loan" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<h3>新增企业借款</h3>
<form class="form-horizontal" id="enterprise_loan_form" method="post">
	<table width="100%">
      <tr>
          <td>
         		
         		<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>标的类型：</label>
                    <div class="controls">
                        <input type="radio" id="subjectType" name="subjectType" onclick="onMark(<%=SubjectTypeEnum.LOAN.getValue()%>);" value="<%=SubjectTypeEnum.LOAN.getValue()%>" checked="checked"><font style="font-size: 12px;">借款标</font>
                        <input type="radio" id="subjectType" name="subjectType" onclick="onMark(<%=SubjectTypeEnum.CREDITOR.getValue()%>);" value="<%=SubjectTypeEnum.CREDITOR.getValue()%>" ><font style="font-size: 12px;">债权标</font>
                    </div>
                </div>
                
                <div class="control-group" id="group_source">
                    <label class="control-label"><span style="color: red;">*</span>标的来源：</label>
                    <div class="controls" >
                        <select type="text" class="easyui-combobox" required="true" style="width: 150px;" 
                        		name="channel" id="channel">
	                        <option value="">请选择来源类型</option>
		                    <option value="1">渠道</option>
		                    <option value="2">门店</option>
                        </select>
                        
                        <span id="div_channel">
	                        <input name="channelId" id="channelId" style="width: 150px;" >
	                        <span id="div_channel_originalUserId">
		                        <input name="originalUserId" id="originalUserId" style="width: 150px;">
	                        </span>
                        </span>
                        
                        <span id="div_store" style="display: none;">
	                        <input name="store_channelId" id="store_channelId" style="width: 150px;">
                        </span>
                    </div>
                </div>
         		
				<div class="control-group" id="group_type1">
                    <label class="control-label"><span style="color: red;">*</span>借款类型：</label>
                    <div class="controls">
                        <input type="radio" class="loanType" id="loanType2" name="loanType" value="2" checked="checked" ><font style="font-size: 12px;">车贷</font>&nbsp;&nbsp;
                        <input type="radio" class="loanType" id="loanType3" name="loanType" value="3"><font style="font-size: 12px;">信用贷</font>&nbsp;&nbsp;
                        <input type="radio" class="loanType" id="loanType4" name="loanType" value="4"><font style="font-size: 12px;">保理</font>&nbsp;&nbsp;
                        <input type="radio" class="loanType" id="loanType5" name="loanType" value="5"><font style="font-size: 12px;">基金</font>&nbsp;&nbsp;
                        <input type="radio" class="loanType" id="loanType6" name="loanType" value="6"><font style="font-size: 12px;">企业标</font>&nbsp;&nbsp;
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>选择企业：</label>
                    <div class="controls">
                        <input id="enterpriseId" name="enterpriseId" style="width: 200px" >
                    </div>
                </div>

          </td>
      </tr>
	</table>
</form>
<input style="margin-left: 250px;width: 95px" type="button" class="btn btn-primary" value="下一步" onclick="saveButton();" >
</div>

<script type="text/javascript">
$("#group_source").hide();

//执行：标的类型单击事件。
function onMark(m){
	if(m == '<%=SubjectTypeEnum.CREDITOR.getValue()%>'){// 债权标
		$("#div_channel").show();
		$("#div_store").hide();
		$('#channel').combobox('select',1);// 渠道选中
		$("#group_source").show();
		//--
		$(".loanType").attr("disabled",true);
		$(".loanType").attr("checked",false);
		$("#loanType6").attr("disabled",false);
	}else{
		$("#group_source").hide();
		$("#group_type1").show();
		// --
		$(".loanType").attr("disabled",false);
	}
}

//执行：来源类型选择事件。
function onChange(){
	$("#channel").combobox({
		onChange:function(rec){
			if(rec == 2){// 门店
				$("#div_channel").hide();
				$("#div_store").show();
			}else{
				$("#div_channel").show();
				$("#div_store").hide();
			}
		}
	});
}

//加载渠道下拉框
$("#enterprise_loan_form #channelId").combobox({
    url: '${ctx}/bondSource/loadBondSource?selectedDisplay=selected',
    textField: 'SOURCENAME',
    valueField: 'BONDSOURCEID',
    onSelect: function (record) {
        $("#enterprise_loan_form #originalUserId").combobox("reload",
        	'${ctx}/bondSource/loadBondSourceUser?bondSourceId=' + record.BONDSOURCEID + '&selectedDisplay=selected');
    }
});

//加载原始债权人下拉框
$("#enterprise_loan_form #originalUserId").combobox({
    url: '${ctx}/bondSource/loadBondSourceUser?bondSourceId=0&selectedDisplay=selected',
    textField: 'BONDNAME',
    valueField: 'USERSOURCEID'
});

//加载门店下拉框
$("#enterprise_loan_form #store_channelId").combobox({
    url: '${ctx}/jsp/constant/loadStore?&selectedDisplay=selected',
    textField: 'STORENAME',
    valueField: 'STOREID'
});

$(function(){
	onChange();
});

//根据借款类型加载企业信息，车贷、信贷=其它企业；保理=保理企业
$(".loanType").click(function(){
	if(this.value == 4){
		$("#enterprise_loan_form #enterpriseId").combobox("reload",
		  '${ctx}/jsp/enterprise/loadEnterprise?selectedDisplay=selected&enterpriseType=0');
	}else if(this.value == 5){
        $("#enterprise_loan_form #enterpriseId").combobox("reload",
          '${ctx}/jsp/enterprise/loadEnterprise?selectedDisplay=selected&enterpriseType=2');
    }else{
		$("#enterprise_loan_form #enterpriseId").combobox("reload",
		  '${ctx}/jsp/enterprise/loadEnterprise?selectedDisplay=selected&enterpriseType=1');
	}
});

//企业下拉框。
$("#enterprise_loan_form #enterpriseId").combobox({
    url: '${ctx}/jsp/enterprise/loadEnterprise?selectedDisplay=selected&enterpriseType=1',
    textField: 'ENTERPRISENAME',
    valueField: 'ENTERPRISEID'
});

// 执行:下一步。
function saveButton(){
	
	// 验证
	if(!$("#enterprise_loan_form").form('validate')){
		return false;
	}
	
	if($("#enterpriseId").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择企业信息！", "info");
		return false;
	}
	
	$.post('${ctx}/jsp/enterprise/loan/save_enterprise_loan',
         $("#enterprise_loan_form").serialize(),
         function(data){
             if(data.result == 'success'){
                 window.location.href = "${ctx}/jsp/enterprise/loan/to_part?actionType=add&loanType=" + data.data.loanType + "&loanApplicationId=" + data.data.loanApplicationId;
             }else if(data.result == 'error'){
                 if(data.errCode == 'check'){
                     $.messager.alert("验证提示", data.errMsg, "info");
                 }else{
                     $.messager.alert("系统提示", data.errMsg, "warning");
                 }
             }else{
                 $.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
             }
         },'json');
}

</script>
</body>
</html>
