<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="car_loan_edit_part1" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="save_car_loan_edit_part1();">
<form class="form-horizontal" id="car_loan_edit_part1_form" method="post">
<input type="hidden" name="loanApplicationId" id="loanApplicationId" value="${loan.mainLoanApplicationId }">
<input type="hidden" id="loanApplicationName" name="loanApplicationName" value="${loan.loanApplicationName}">
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
		                <select name="province" id="province" style="width: 100px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty provinceInfos}">
	                        	<c:forEach items="${provinceInfos}" var="provinceInfo">
	                        		<c:if test="${provinceInfo.provinceId == car.province}">
	                        			<option value="${provinceInfo.provinceId}" selected="selected">${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        		<c:if test="${provinceInfo.provinceId != car.province}">
	                        			<option value="${provinceInfo.provinceId}" >${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">省</span>
                        <select name="city" id="city" style="width: 100px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty bornCitys}">
	                        	<c:forEach items="${bornCitys}" var="bornCity">
	                        		<c:if test="${bornCity.cityId == car.city}">
	                        			<option value="${bornCity.cityId}" selected="selected">${bornCity.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${bornCity.cityId != car.city}">
	                        			<option value="${bornCity.cityId}" >${bornCity.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
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
                        style="width: 440px; height: 100px;">${car.useDescription }</textarea>
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
                        	name="loanBalance" id="loanBalance" value="${loan.loanBalance }">
                    </div>
                </div>
         
				<div class="control-group">
                    <label class="control-label">线下合同/订单号：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" 
                        	name="offlineApplyCode" id="offlineApplyCode" value="${loan.offlineApplyCode }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">项目描述：</label>
                    <div class="controls">
                        <textarea name="projectDescription" id="projectDescription"
                        class="easyui-validatebox" validType="length[0,300]"
                        style="width: 440px; height: 100px;">${car.projectDescription }</textarea>
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
                        style="width: 440px; height: 100px;">${car.riskControlInformation }</textarea>
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
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="save_car_loan_edit_part1();">
</div>

<script type="text/javascript">

//省份下拉框。
$("#car_loan_edit_part1_form #province").combobox({
    textField: 'PROVINCENAME',
    valueField: 'PROVINCEID',
    onSelect: function (record) {
        $("#car_loan_edit_part1_form #city").combobox("reload",
        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
    }
});

//城市下拉框。
$("#car_loan_edit_part1_form #city").combobox({
    textField: 'CITYNAME',
    valueField: 'CITYID'
});

//借款用途下拉框。
$("#car_loan_edit_part1_form #loanUseage").combobox({
    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=enterpriseLoanUseage&parentConstant=0&selectedDisplay=selected',
    textField: 'CONSTANTNAME',
    valueField: 'CONSTANTVALUE',
    onLoadSuccess: function (record) {
    	$("#car_loan_edit_part1_form #loanUseage").combobox("select", '${loan.loanUseage}');
    }
});

//内部评级下拉框。
$("#car_loan_edit_part1_form #internalRating").combobox({
    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=internalRating&parentConstant=0&selectedDisplay=selected',
    textField: 'CONSTANTNAME',
    valueField: 'CONSTANTVALUE',
    onLoadSuccess: function (record) {
    	$("#car_loan_edit_part1_form #internalRating").combobox("select", '${car.internalRating}');
    }
});

//借款产品下拉框。
$("#car_loan_edit_part1_form #loanProductId").combobox({
    url: '${ctx}/jsp/product/loan/loadLoanProduct?selectedDisplay=selected',
    textField: 'PRODUCTNAME',
    valueField: 'LOANPRODUCTID',
    onLoadSuccess: function (record) {
    	$("#car_loan_edit_part1_form #loanProductId").combobox("select", '${loan.loanProductId}');
    	showLoanProductDetail(${loan.loanProductId});
    },
    onSelect: function (record) {
    	showLoanProductDetail(record.LOANPRODUCTID);
    }
});

//展示详情
function showLoanProductDetail(loanProductId){
	// 展示详情开始
	$.post('${ctx}/jsp/product/loan/getLoanProductDetail',
   		{
			loanproductid:loanProductId
   		},
   		function(data){
   	    	if(data.result == 'success'){
   	    		
   	 			// 单项填充
   	    		$("#annualRate").html(data.data.annualRate + '%');//年利率
   	    		$("#dueTime").html(data.data.dueTime);//期限时长
   	    		$("#repaymentType").html(data.data.repaymentType);//还款方式
   	    		
   	    	}else if(data.result == 'error'){
   	    		//$.messager.alert("系统提示", data.errMsg, "warning");
   	    	}else{
   	    		$.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
   	    	}
   	 },'json');
	// 展示详情结束
}

//打款卡下拉框。
$("#car_loan_edit_part1_form #inCardId").combobox({
    url: '${ctx}/jsp/enterprise/loadCard?enterpriseId=${enterpriseId}&selectedDisplay=selected',
    textField: 'CARDCODE',
    valueField: 'CARDID',
    onLoadSuccess: function (record) {
    	$("#car_loan_edit_part1_form #inCardId").combobox("select", '${loan.inCardId}');
    }
});

// 执行:保存。
function save_car_loan_edit_part1(){
	
	// 验证
	if(!$("#car_loan_edit_part1_form").form('validate')){
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
	
	$.post('${ctx}/jsp/enterprise/loan/save_enterprise_loan_car?r=' + Math.random(),
   		$("#car_loan_edit_part1_form").serialize(),
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
