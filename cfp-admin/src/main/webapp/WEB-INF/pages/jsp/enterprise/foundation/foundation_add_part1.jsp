<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="foundation_add_part1" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">

<iframe style="display:none;" name = "ajax_iframe"></iframe>
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="save_foundation_add_part1();" >
<form class="form-horizontal" id="foundation_add_part1_form" method="post" enctype="multipart/form-data" target="ajax_iframe">
<input type="hidden" name="loanApplicationId" id="loanApplicationId" value="${loanApplicationId }">
<input type="hidden" id="loanApplicationName" name="loanApplicationName">
	<table width="100%">
      <tr>
          <td>

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
                  <div class="controls" >
                      <input type="text" style="width: 200px"
                             class="easyui-numberbox" required="true" validType="length[2,10]"
                             name="loanBalance" id="loanBalance"><a style="font-size: 12px;cursor: pointer;" id="showRepaymentPlan" onclick="showRepaymentPlan();">查看还款计划</a>
                      <br/><br/>
                      <table id="feesItemList" class="table table-bordered table-condensed" style="font-size: 12px;width: 300px;">

                      </table>
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
                    <label class="control-label"><span style="color: red;">*</span>项目描述：</label>
                    <div class="controls">
                        <textarea name="applicationDesc" id="applicationDesc"  required="true"
                        class="easyui-validatebox" validType="length[0,300]"
                        style="width: 440px; height: 100px;"></textarea>
                    </div>
                </div>

              <div class="control-group">
                  <label class="control-label">受托投资管理机构：</label>
                  <div class="controls">
                      <span style="font-size: 12px;" id="">${enterpriseInfo.enterpriseName}</span>
                  </div>
              </div>
              <div class="control-group">
                  <label class="control-label">组织机构编码：</label>
                  <div class="controls">
                      <span style="font-size: 12px;" id="">${enterpriseInfo.organizationCode}</span>
                  </div>
              </div>
             <div class="control-group">
                  <label class="control-label">企业法人：</label>
                  <div class="controls">
                      <span style="font-size: 12px;" id="">${enterpriseInfo.legalPersonName}</span>
                  </div>
              </div>
              <div class="control-group">
                  <label class="control-label"><span style="color: red;">*</span>托管机构：</label>
                  <div class="controls">
                      <input name="coId" id="coId" style="width: 200px" >
                  </div>
              </div>
              <div class="control-group">
                  <label class="control-label"><span style="color: red;">*</span>定向委托投资类型：</label>
                  <div class="controls">
                      <input style="width: 200px" id="investmentType" name="investmentType">
                  </div>
              </div>

              <div class="control-group">
                  <label class="control-label"><span style="color: red;">*</span>标的详情说明：</label>
                  <div class="controls">
                      <input type="file" name="importFile" id="importFile"  required="true" />
                  </div>
              </div>

              <div class="control-group">
                  <label class="control-label"><span style="color: red;">*</span>交易说明书：</label>
                  <div class="controls">
                      <input type="file" name="importFileTrade" id="importFileTrade"  required="true" />
                  </div>
              </div>

              <div class="control-group">
                  <label class="control-label"><span style="color: red;">*</span>风险提示函：</label>
                  <div class="controls">
                      <input type="file" name="importFileRiskTip" id="importFileRiskTip"  required="true" />
                  </div>
              </div>

                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>打款卡：</label>
                    <div class="controls">
                       	<input id="inCardId" name="inCardId" style="width: 200px" > <font  style="font-size: 12px;cursor: pointer;">(显示该公司及该公司人员的银行卡)</font>
                    </div>
                </div>
				
          </td>
      </tr>
	</table>
</form>
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="save_foundation_add_part1();">
</div>

<script type="text/javascript">

//托管机构下拉框。
$("#foundation_add_part1_form #coId").combobox({
    url: '${ctx}/jsp/enterprise/loadCoLtd?enterpriseId=${enterpriseId}&selectedDisplay=selected',
    textField: 'COMPANYNAME',
    valueField: 'COID'
});

//借款产品下拉框。
$("#foundation_add_part1_form #loanProductId").combobox({
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
$("#foundation_add_part1_form #inCardId").combobox({
    url: '${ctx}/jsp/enterprise/loadCard?enterpriseId=${enterpriseId}&selectedDisplay=selected',
    textField: 'CARDCODE',
    valueField: 'CARDID'
});

// 执行:保存。
function save_foundation_add_part1(){
	
	// 验证
	if(!$("#foundation_add_part1_form").form('validate')){
		return false;
	}

	if($("#loanProductId").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择产品！", "info");
		return false;
	}

    if($("#coId").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择托管机构！", "info");
		return false;
	}

    if($("#investmentType").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择定向委托投资类型！", "info");
		return false;
	}

    if($("#importFile").val() != ''){
        var filepath=$("#importFile").val();
        var file_path = filepath.replace(/\\/g,"/");
        var fileNameStr = file_path.substring(file_path.lastIndexOf("/"));
        if(fileNameStr.length>50){
            $.messager.alert("系统提示","文件名过长！", "info");
            return false;
        }
        var extStart=filepath.lastIndexOf(".");
        var ext=filepath.substring(extStart,filepath.length).toUpperCase();
        if(ext!=".PDF"){
            $.messager.alert("系统提示","标的详情说明限于pdf格式", "info");
            return false;
        }
    }else
    {
        $.messager.alert("系统提示", "标的详情说明文件!", "info");
        return false;
    }

    if($("#importFileTrade").val() != ''){
        var filepath=$("#importFileTrade").val();
        var file_path = filepath.replace(/\\/g,"/");
        var fileNameStr = file_path.substring(file_path.lastIndexOf("/"));
        if(fileNameStr.length>50){
            $.messager.alert("系统提示","文件名过长！", "info");
            return false;
        }
        var extStart=filepath.lastIndexOf(".");
        var ext=filepath.substring(extStart,filepath.length).toUpperCase();
        if(ext!=".PDF"){
            $.messager.alert("系统提示","交易说明书限于pdf格式", "info");
            return false;
        }
    }else
    {
        $.messager.alert("系统提示", "交易说明书!", "info");
        return false;
    }

    if($("#importFileRiskTip").val() != ''){
        var filepath=$("#importFileRiskTip").val();
        var file_path = filepath.replace(/\\/g,"/");
        var fileNameStr = file_path.substring(file_path.lastIndexOf("/"));
        if(fileNameStr.length>50){
            $.messager.alert("系统提示","文件名过长！", "info");
            return false;
        }
        var extStart=filepath.lastIndexOf(".");
        var ext=filepath.substring(extStart,filepath.length).toUpperCase();
        if(ext!=".PDF"){
            $.messager.alert("系统提示","风险提示函限于pdf格式", "info");
            return false;
        }
    }else
    {
        $.messager.alert("系统提示", "风险提示函!", "info");
        return false;
    }

	if($("#inCardId").combobox('getValue') == ''){
		$.messager.alert("验证提示", "请选择打款卡！", "info");
		return false;
	}
	
	// 获取下列数值的文字信息。
	var loanUseage_Str ="基金";
	var loanProductId_Str = $("#loanProductId").combobox("getText");//借款产品
	var dueTime_Str = $("#dueTime").html();//期限
	var loanApplicationName_Str = loanUseage_Str + '-' + loanProductId_Str + '-' + dueTime_Str;//借款合同名称
	$("#loanApplicationName").attr("value",loanApplicationName_Str);
	$("#foundation_add_part1_form").attr("action","${ctx}/jsp/enterprise/loan/save_enterprise_loan_foundation");
    $("#foundation_add_part1_form").submit();
	/*$.post('${ctx}/jsp/enterprise/loan/save_enterprise_loan_foundation?r=' + Math.random(),
   		$("#foundation_add_part1_form").serialize(),
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
   	 },'json');*/

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
    	$.messager.alert("验证提示", "请选择借款产品并填写借款金额！", "info");
    }
}



$("#investmentType").combobox({
    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=INVESTMENT_TYPE&parentConstant=0&selectedDisplay=selected',
    textField: 'CONSTANTNAME',
    valueField: 'CONSTANTVALUE'
});

</script>
</body>
</html>
