<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<body>
<div id="enterprise_add_part1" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<form class="form-horizontal" id="enterprise_add_part1_form" method="post">
<input type="hidden" name="enterpriseId" id="enterpriseId" value="">
	<table width="100%">
      <tr>
          <td>
          		
				<div class="control-group">
                    <label class="control-label">公司类型：<span style="color: red;">*</span></label>
                    <div class="controls">
                       	<select name="enterpriseType" id="enterpriseType" class="easyui-combobox" style="width: 200px;" required="true">
		                    <option value="">请选择</option>
		                    <option value="0">保理</option>
		                    <option value="2">资管公司</option>
		                    <option value="1">其它</option>
		                </select>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">公司名称：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[4,30]" required="true"
                        	name="enterpriseName" id="enterpriseName">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">组织机构代码：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="organizationCode" id="organizationCode">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">法人姓名：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="legalPersonName" id="legalPersonName">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">法人身份证号：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="legalPersonCode" id="legalPersonCode">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">开户许可证号：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="accountNumber" id="accountNumber">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">税务登记证代码：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="taxRegistrationCode" id="taxRegistrationCode">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">营业执照注册号：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="businessRegistrationNumber" id="businessRegistrationNumber">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">经营年限：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-numberbox" validType="length[0,3]" required="true"
                        	name="operatingPeriod" id="operatingPeriod">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">注册资金：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-numberbox" validType="length[0,30]" required="true"
                        	name="registeredCapital" id="registeredCapital"><span style="font-size: 12px;">元</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">经营范围：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 440px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,200]" required="true"
                        	name="operatingRange" id="operatingRange">
                    </div>
                </div>

				<div class="control-group">
                    <label class="control-label">企业信息：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <textarea class="easyui-validatebox" style="width: 440px; height: 100px;" 
                        validType="length[2,200]" required="true"
                                name="information" id="information"></textarea>
                    </div>
                </div>

				<div class="control-group">
                    <label class="control-label">
                    	<input type="checkbox" name="marketValue" id="marketValue" onclick="onCheckbox();">
                    	<span style="font-size: 12px;"> 财务状况</span>
                    </label>
                    <div class="controls">
                        
                    </div>
                </div>
                
				<div class="control-group market">
                    <label class="control-label">主营收入：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,10]" precision="0"
                        	name="mainRevenue" id="mainRevenue"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">毛利润：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,10]" precision="0"
                        	name="grossProfit" id="grossProfit"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">净利润：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,10]" precision="0"
                        	name="netProfit" id="netProfit"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">总资产：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,10]" precision="0"
                        	name="totalAssets" id="totalAssets"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">净资产：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,10]" precision="0"
                        	name="netAssets" id="netAssets"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                

          </td>
      </tr>
	</table>
</form>
<input style="margin-left: 200px;margin-bottom: 40px;" type="button" class="btn btn-primary" value="保 存" onclick="saveEnterprise();">
</div>

<script type="text/javascript">

$(function(){
	$(".market").hide();
});

function onCheckbox(){
	if($('#marketValue').is(':checked')){
		$(".market").show();
	}else{
		$(".market").hide();
	}
}

// 执行:保存。
function saveEnterprise(){
	
	// 验证
	if(!$("#enterprise_add_part1_form").form('validate')){
		return false;
	}
	
	var enterpriseName = $("#enterpriseName").val();
	if(enterpriseName.length < 4){
		$.messager.alert("操作提示", "企业名称至少四个字符！", "info");
		return false;
	}
	
	// 如勾选财务状况，则财务状况相关的输入项也都为必填项
	if($('#marketValue').is(':checked')){
		if($('#mainRevenue').val()==''){
			$.messager.alert("操作提示", "主营收入为必填！", "info");
			return false;
		}
		if($('#grossProfit').val()==''){
			$.messager.alert("操作提示", "毛利润为必填！！", "info");
			return false;
		}
		if($('#netProfit').val()==''){
			$.messager.alert("操作提示", "净利润为必填！", "info");
			return false;
		}
		if($('#totalAssets').val()==''){
			$.messager.alert("操作提示", "总资产为必填！", "info");
			return false;
		}
		if($('#netAssets').val()==''){
			$.messager.alert("操作提示", "净资产为必填！", "info");
			return false;
		}
	}
	
	var url = '${ctx}/jsp/enterprise/addEnterprise?r=' + Math.random();
	
	var enterpriseId = $("#enterpriseId").val();
	if(enterpriseId != ""){
		url = '${ctx}/jsp/enterprise/editEnterprise?r=' + Math.random();
	}
	$.post(url,
   		$("#enterprise_add_part1_form").serialize(),
   		function(data){
   	    	if(data.result == 'success'){
   	    		$.messager.alert("操作提示", "保存成功！", "info");
   	    		if(enterpriseId == ""){
   	    			$("#enterpriseId").attr("value",data.data);
   	    		}
   	    	    // 刷新主页面列表
                var params = window.opener.$('#enterpriseList_list').datagrid("options").queryParams;
                window.opener.$('#enterpriseList_list').datagrid("options").queryParams = params;
                window.opener.$('#enterpriseList_list').datagrid('reload');
                window.close();
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
