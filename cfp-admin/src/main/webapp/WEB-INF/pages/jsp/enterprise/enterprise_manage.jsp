<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addEnterprise" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="addEnterprise_form" method="post">
	<input type="hidden" name="enterpriseId" id="enterpriseId" value="${enterpriseInfo.enterpriseId }"/>
	
	<div class="control-group">
        <label class="control-label">企业名称：</label>
        <div class="controls">
        	${enterpriseInfo.enterpriseName }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">组织机构代码：</label>
        <div class="controls">
        	${enterpriseInfo.organizationCode }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">注册号：</label>
        <div class="controls">
        	${enterpriseInfo.businessRegistrationNumber }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">法人姓名：</label>
        <div class="controls">
        	${enterpriseInfo.legalPersonName }
        </div>
    </div>
	
    <div class="control-group">
        <label class="control-label">合同期限：<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="contractBegin" >至
			<input type="text" class="easyui-datebox" editable="false" style="width: 150px" id="contractEnd" >
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">单笔保理限额：<span style="color: red;">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
            class="easyui-numberbox" required="true" missingMessage="不能为空"
                   name="singleMaximumAmount" id="singleMaximumAmount" >
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">年度保理限额：<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
            class="easyui-numberbox" required="true" missingMessage="不能为空"
                   name="annualMaximumLimit" id="annualMaximumLimit" >
            <input type="checkbox" name="unLimit" id="unLimit" onclick="onCheckbox(0);" >不限制&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="checkbox" name="toSingle" id="toSingle" onclick="onCheckbox(1);" >同单笔额度
        </div>
    </div>
    
</form>
</div>
<script language="javascript" >

	function onCheckbox(sign){
		if(sign == 0){
			if($("#unLimit").is(':checked')){
				$("#annualMaximumLimit").attr("value","");
			}
		}else if(sign == 1){
			if($("#toSingle").is(':checked')){
				var singleAmount = $("#singleMaximumAmount").val();
				if(singleAmount == ""){
					$.messager.alert("操作提示", "先填写单笔保理限额！", "info");
				}else{
					$("#annualMaximumLimit").attr("value",singleAmount);	
				}
			}
		}
	}
	
	// 保存管理
	function saveManage(){
		var enterpriseId = $("#enterpriseId").val();
		var contractBegin = $("#contractBegin").datebox("getValue");
		var contractEnd = $("#contractEnd").datebox("getValue");
		var singleMaximumAmount = $("#singleMaximumAmount").val();
		var annualMaximumLimit = $("#annualMaximumLimit").val();
		if($("#unLimit").is(':checked')){
			annualMaximumLimit = "0";
		}
		
		if(enterpriseId == ''){
			$.messager.alert("验证提示", "企业ID不能为空", "info");
			return;
		}
		if(contractBegin == ''){
			$.messager.alert("验证提示", "开始期限不能为空", "info");
			return;
		}
		if(contractEnd == ''){
			$.messager.alert("验证提示", "结束期限不能为空", "info");
			return;
		}
		if(singleMaximumAmount == ''){
			$.messager.alert("验证提示", "单笔限额不能为空", "info");
			return;
		}
		if(annualMaximumLimit == ''){
			$.messager.alert("验证提示", "年度限额不能为空" + annualMaximumLimit, "info");
			return;
		}
		
		$.post('${ctx}/jsp/enterprise/doManage',
            {
				enterpriseId:enterpriseId,
				contractBegin:contractBegin,
				contractEnd:contractEnd,
				singleMaximumAmount:singleMaximumAmount,
				annualMaximumLimit:annualMaximumLimit
            },
            function(data){
                if(data.result == 'success'){
                	$("#showManageDetail").dialog('close');
                	$.messager.alert("操作提示", "操作成功！", "info");
                    $('#enterpriseList_list').datagrid('reload');
                }else if(data.result == 'error'){
                    if(data.errCode == 'check'){
                        $.messager.alert("验证提示", data.errMsg, "info");
                    }else{
                        $.messager.alert("系统提示", data.errMsg, "info");
                    }
                }else{
                    $.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
                }
         },'json');
	}

</script>
</body>
</html>