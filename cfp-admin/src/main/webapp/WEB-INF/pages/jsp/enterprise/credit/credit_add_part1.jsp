<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="credit_add_part1" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="save_credit_add_part1();">
<form class="form-horizontal" id="credit_add_part1_form" method="post">
<input type="hidden" name="loanApplicationId" id="loanApplicationId" value="${loanApplicationId }">
<input type="hidden" id="loanApplicationName" name="loanApplicationName">
	<table width="100%">
      <tr>
          <td>
          		
          		<div class="control-group">
                    <label class="control-label">企业名称：</label>
                    <div class="controls">
                       	<span style="font-size: 12px;">${enterpriseName }</span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">组织机构编码：</label>
                    <div class="controls">
                       	<span style="font-size: 12px;">${organizationCode }</span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">企业法人：</label>
                    <div class="controls">
                       	<span style="font-size: 12px;">${legalPersonName }</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">项目地区：</label>
                    <div class="controls">
                       	<input name="province" id="province" style="width: 100px" >
                        <span style="font-size: 12px;">省</span>
                        <input name="city" id="city" style="width: 100px" >
                        <span style="font-size: 12px;">市</span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款用途：</label>
                    <div class="controls">
                       	<input id="loanUseage" name="loanUseage" style="width: 200px" >
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">用途描述：</label>
                    <div class="controls">
                        <textarea name="useDescription" id="useDescription"
                        class="easyui-validatebox" validType="length[0,200]"
                        style="width: 440px; height: 100px;"></textarea>
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
                    <label class="control-label"><span style="color: red;">*</span>借款金额：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-numberbox" validType="length[0,10]" required="true"
                        	name="loanBalance" id="loanBalance">
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
                    <label class="control-label">项目描述：</label>
                    <div class="controls">
                        <textarea name="projectDescription" id="projectDescription"
                        class="easyui-validatebox" validType="length[0,300]"
                        style="width: 440px; height: 100px;"></textarea>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">内部评级：</label>
                    <div class="controls">
                       	<input id="internalRating" name="internalRating" style="width: 200px" >
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">风险控制信息：</label>
                    <div class="controls">
                        <textarea name="riskControlInformation" id="riskControlInformation"
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
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="save_credit_add_part1();">
</div>

<script type="text/javascript">

//省份下拉框。
$("#credit_add_part1_form #province").combobox({
    url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
    textField: 'PROVINCENAME',
    valueField: 'PROVINCEID',
    onSelect: function (record) {
        $("#credit_add_part1_form #city").combobox("reload",
        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
    }
});

//城市下拉框。
$("#credit_add_part1_form #city").combobox({
    url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
    textField: 'CITYNAME',
    valueField: 'CITYID'
});

//借款用途下拉框。
$("#credit_add_part1_form #loanUseage").combobox({
    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=enterpriseLoanUseage&parentConstant=0&selectedDisplay=selected',
    textField: 'CONSTANTNAME',
    valueField: 'CONSTANTVALUE'
});

//内部评级下拉框。
$("#credit_add_part1_form #internalRating").combobox({
    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=internalRating&parentConstant=0&selectedDisplay=selected',
    textField: 'CONSTANTNAME',
    valueField: 'CONSTANTVALUE'
});

//借款产品下拉框。
$("#credit_add_part1_form #loanProductId").combobox({
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
$("#credit_add_part1_form #inCardId").combobox({
    url: '${ctx}/jsp/enterprise/loadCard?enterpriseId=${enterpriseId}&selectedDisplay=selected',
    textField: 'CARDCODE',
    valueField: 'CARDID'
});

// 执行:保存。
function save_credit_add_part1(){
	
	// 验证
	if(!$("#credit_add_part1_form").form('validate')){
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
	
	$.post('${ctx}/jsp/enterprise/loan/save_enterprise_loan_credit?r=' + Math.random(),
   		$("#credit_add_part1_form").serialize(),
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

</script>
</body>
</html>
