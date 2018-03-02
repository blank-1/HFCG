<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="factoring_add_part1" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="save_factoring_add_part1();">
<form class="form-horizontal" id="factoring_add_part1_form" method="post">
<input type="hidden" name="loanApplicationId" id="loanApplicationId" value="${loanApplicationId }">
<input type="hidden" id="loanApplicationName" name="loanApplicationName">
	<table width="100%">
      <tr>
          <td>
          		
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款用途：</label>
                    <div class="controls">
                       	<input id="loanUseage" name="loanUseage" style="width: 200px" >
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>选择产品：</label>
                    <div class="controls">
                       	<input name="loanProductId" id="loanProductId" style="width: 200px" >
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">年利率：</label>
                    <div class="controls">
                       	<span style="font-size: 12px;" id="annualRate"></span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">期限：</label>
                    <div class="controls">
                       	<span style="font-size: 12px;" id="dueTime"></span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">还款方式：</label>
                    <div class="controls">
                       	<span style="font-size: 12px;" id="repaymentType"></span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>融资金额：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-numberbox" validType="length[0,10]" required="true" 
                        	name="financingAmount" id="financingAmount">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>授信金额：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-numberbox" validType="length[0,10]" required="true" 
                        	name="loanBalance" id="loanBalance">&nbsp;
                        	<a id="showRepaymentPlan" onclick="showRepaymentPlan();" style="font-size: 12px;cursor: pointer;">查看还款计划</a>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>融资方：</label>
                    <div class="controls">
		                <input name="financingParty" id="financingParty" style="width: 200px" >
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>付款方：</label>
                    <div class="controls">
		                <input name="paymentParty" id="paymentParty" style="width: 200px" >
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">还款来源：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" disabled="disabled"
                        	name="sourceOfRepayment_display" id="sourceOfRepayment_display" value="${enterpriseName }">
                        	<input type="hidden" id="sourceOfRepayment" name="sourceOfRepayment" value="${enterpriseName }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">线下合同/订单号：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" 
                        	name="offlineApplyCode" id="offlineApplyCode">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>应收账款说明：</label>
                    <div class="controls">
                        <textarea name="accountReceivableDescription" id="accountReceivableDescription"
                        class="easyui-validatebox" validType="length[0,200]" required="true" 
                        style="width: 440px; height: 100px;"></textarea>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">项目综合评价：</label>
                    <div class="controls">
                        <textarea name="projectComprehensiveEvaluati" id="projectComprehensiveEvaluati"
                        class="easyui-validatebox" validType="length[0,200]"
                        style="width: 440px; height: 100px;"></textarea>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">款项风险评价：</label>
                    <div class="controls">
                        <textarea name="moneyRiskAssessment" id="moneyRiskAssessment"
                        class="easyui-validatebox" validType="length[0,200]"
                        style="width: 440px; height: 100px;"></textarea>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">项目风险保障方案：</label>
                    <div class="controls">
                    	<input type="checkbox" id="fieldAdjustmentMark" name="fieldAdjustmentMark" value="0">&nbsp;<span style="font-size: 12px;">360度实地尽调 - 大数据思维保障项目质量</span><br/>
                        <textarea name="fieldAdjustmentValue" id="fieldAdjustmentValue"
                        class="easyui-validatebox" validType="length[0,200]"
                        style="width: 440px; height: 100px;"></textarea>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">:</label>
                    <div class="controls">
                        <input type="checkbox" id="repaymentGuaranteeMark" name="repaymentGuaranteeMark" value="0">&nbsp;<span style="font-size: 12px;">还款保证金 - 构建风险缓释空间</span><br/>
                        <textarea name="repaymentGuaranteeValue" id="repaymentGuaranteeValue"
                        class="easyui-validatebox" validType="length[0,200]"
                        style="width: 440px; height: 100px;"></textarea>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">:</label>
                    <div class="controls">
                        <input type="checkbox" id="aidFundMark" name="aidFundMark" value="0">&nbsp;<span style="font-size: 12px;">法律援助基金 - 平台资金支持护航维权启动</span><br/>
                        <textarea name="aidFundValue" id="aidFundValue"
                        class="easyui-validatebox" validType="length[0,200]"
                        style="width: 440px; height: 100px;"></textarea>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>打款卡：</label>
                    <div class="controls">
                       	<input id="inCardId" name="inCardId" style="width: 200px" >
                    </div>
                </div>
				
          </td>
      </tr>
	</table>
</form>
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="save_factoring_add_part1();">
</div>

<script type="text/javascript">

//借款用途下拉框。
$("#factoring_add_part1_form #loanUseage").combobox({
    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=enterpriseLoanUseage&parentConstant=0&selectedDisplay=selected',
    textField: 'CONSTANTNAME',
    valueField: 'CONSTANTVALUE'
});

//融资方下拉框。
$("#factoring_add_part1_form #financingParty").combobox({
    url: '${ctx}/jsp/enterprise/loadCoLtd?enterpriseId=${enterpriseId}&selectedDisplay=selected',
    textField: 'COMPANYNAME',
    valueField: 'COID'
});

//付款方下拉框。
$("#factoring_add_part1_form #paymentParty").combobox({
    url: '${ctx}/jsp/enterprise/loadCoLtd?enterpriseId=${enterpriseId}&selectedDisplay=selected',
    textField: 'COMPANYNAME',
    valueField: 'COID'
});

//借款产品下拉框。
$("#factoring_add_part1_form #loanProductId").combobox({
    url: '${ctx}/jsp/product/loan/loadLoanProduct?selectedDisplay=selected',
    textField: 'PRODUCTNAME',
    valueField: 'LOANPRODUCTID',
    onSelect: function (record) {
    	// 展示详情开始
		$.post('${ctx}/jsp/product/loan/getLoanProductDetail',
	   		{
				loanproductid:record.LOANPRODUCTID
	   		},
	   		function(data){
	   	    	if(data.result == 'success'){
	   	    		
	   	 			// 单项填充
	   	    		$("#annualRate").html(data.data.annualRate + '%');//年利率
	   	    		$("#dueTime").html(data.data.dueTime);//期限时长
	   	    		$("#repaymentType").html(data.data.repaymentType);//还款方式
	   	    		
	   	    	}else if(data.result == 'error'){
	   	    		$.messager.alert("系统提示", data.errMsg, "warning");
	   	    	}else{
	   	    		$.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
	   	    	}
	   	 },'json');
    	// 展示详情结束
    }
});

//打款卡下拉框。
$("#factoring_add_part1_form #inCardId").combobox({
    url: '${ctx}/jsp/enterprise/loadCard?enterpriseId=${enterpriseId}&selectedDisplay=selected',
    textField: 'CARDCODE',
    valueField: 'CARDID'
});

// 执行:保存。
function save_factoring_add_part1(){
	
	// 验证
	if(!$("#factoring_add_part1_form").form('validate')){
		return false;
	}
	
	if($("#loanUseage").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择借款用途！", "info");
		return false;
	}
	if($("#loanProductId").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择产品！", "info");
		return false;
	}
	if($("#financingParty").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择融资方！", "info");
		return false;
	}
	if($("#paymentParty").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择付款方！", "info");
		return false;
	}
	if($("#inCardId").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择打款卡！", "info");
		return false;
	}
	
	// 获取下列数值的文字信息。
	var loanUseage_Str = $("#loanUseage").combobox("getText");//借款用途
	var loanProductId_Str = $("#loanProductId").combobox("getText");//借款产品
	var dueTime_Str = $("#dueTime").html();//期限
	var loanApplicationName_Str = loanUseage_Str + '-' + loanProductId_Str + '-' + dueTime_Str;//借款合同名称
	$("#loanApplicationName").attr("value",loanApplicationName_Str);
	
	$.post('${ctx}/jsp/enterprise/loan/save_enterprise_loan_factoring?r=' + Math.random(),
   		$("#factoring_add_part1_form").serialize(),
   		function(data){
   	    	if(data.result == 'success'){
   	    		$.messager.alert("操作提示", "保存成功！", "info");
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

//展示还款计划列表
function showRepaymentPlan(){
    if ($("#loanProductId").combobox("getValue") && $("#loanBalance").val()) {
    	$("#showRepaymentPlan").after("<div id='repaymentPlan' style=' padding:10px; '></div>");
        $("#repaymentPlan").dialog({
            resizable: false,
            title: '还款计划',
            href: '${ctx}/jsp/loanManage/loan/toShowRepaymentPlan?loanProductId='+ $("#loanProductId").combobox("getValue") +'&balance=' + $("#loanBalance").val(),
            width: 600,
            modal: true,
            height: 300,
            top: 100,
            left: 400,
            buttons: [
                {
                   text: '取消',
                   iconCls: 'icon-cancel',
                   handler: function () {
                       $("#repaymentPlan").dialog('close');
                   }
           	}
            ],
            onClose: function () {
                $("#repaymentPlan").dialog('destroy');
            }
        });
    }else{
    	$.messager.alert("验证提示", "请选择借款产品并填写授信金额！", "info");
    }
}

</script>
</body>
</html>
