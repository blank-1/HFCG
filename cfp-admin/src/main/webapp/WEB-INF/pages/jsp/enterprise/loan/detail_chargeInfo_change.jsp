<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="change" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="change_form" method="post">
	<input type="hidden" name="mortgageCarId" id="mortgageCarId" value="${mortgageCarSnapshot.mortgageCarId }"/>
	
	<div class="control-group">
        <label class="control-label">汽车品牌：</label>
        <div class="controls">
        	${mortgageCarSnapshot.automobileBrand }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">汽车型号：</label>
        <div class="controls">
        	${mortgageCarSnapshot.carModel }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">车架号：</label>
        <div class="controls">
        	${mortgageCarSnapshot.frameNumber }
        </div>
    </div>
	变更前
	<hr>
	变更后    

	<div class="control-group">
        <label class="control-label">变更原因：<span style="color: red;">*</span></label>
        <div class="controls">
            <input style="width: 200px" id="changeReason" name="changeReason">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">汽车品牌：<span style="color: red;">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
            class="easyui-validatebox" required="true" missingMessage="不能为空"
                   name="automobileBrand" id="automobileBrand" >
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">汽车型号：<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
            class="easyui-validatebox" required="true" missingMessage="不能为空"
                   name="carModel" id="carModel" >
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">车架号：<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
            class="easyui-validatebox" required="true" missingMessage="不能为空"
                   name="frameNumber" id="frameNumber" >
        </div>
    </div>
    
</form>
</div>
<script language="javascript" >

	//加载变更原因下拉框。
	$("#change_form #changeReason").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=CarChangeReason&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE'
	});

	// 保存变更
	function saveChange(){
		var mortgageCarId = $("#mortgageCarId").val();
		var changeReason = $("#changeReason").combobox("getValue");
		var automobileBrand = $("#automobileBrand").val();
		var carModel = $("#carModel").val();
		var frameNumber = $("#frameNumber").val();
		
		if(mortgageCarId == ''){
			$.messager.alert("验证提示", "抵押信息不能为空", "info");
			return;
		}
		if(changeReason == ''){
			$.messager.alert("验证提示", "变更原因不能为空", "info");
			return;
		}
		if(automobileBrand == ''){
			$.messager.alert("验证提示", "汽车品牌不能为空", "info");
			return;
		}
		if(carModel == ''){
			$.messager.alert("验证提示", "汽车型号不能为空", "info");
			return;
		}
		if(frameNumber == ''){
			$.messager.alert("验证提示", "车架号不能为空", "info");
			return;
		}
		
		$.post('${ctx}/jsp/enterprise/loan/save_chargeInfo_change',
            {
				mortgageCarId:mortgageCarId,
				changeReason:changeReason,
				automobileBrand:automobileBrand,
				carModel:carModel,
				frameNumber:frameNumber
            },
            function(data){
                if(data.result == 'success'){
                	$("#showManageDetail").dialog('close');
                	$.messager.alert("操作提示", "操作成功！", "info");
                    $('#detail_Tabs').tabs('select',1);
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