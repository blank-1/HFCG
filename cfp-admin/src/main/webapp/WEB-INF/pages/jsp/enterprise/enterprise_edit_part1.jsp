<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<body>
<div id="enterprise_add_part1" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<form class="form-horizontal" id="enterprise_add_part1_form" method="post">
<input type="hidden" name="enterpriseId" id="enterpriseId" value="${enterpriseInfo.enterpriseId }">
	<table width="100%">
      <tr>
          <td>
          		
				<div class="control-group">
                    <label class="control-label">公司类型：<span style="color: red;">*</span></label>
                    <div class="controls">
                       	<select name="enterpriseType" id="enterpriseType" class="easyui-combobox" style="width: 200px;" required="true">
		                    <option value="">请选择</option>
		                    <option value="0" ${enterpriseInfo.enterpriseType==0?'selected':''}>保理</option>
		                    <option value="2" ${enterpriseInfo.enterpriseType==2?'selected':''}>资管公司</option>
		                    <option value="1" ${enterpriseInfo.enterpriseType==1?'selected':''}>其它</option>
		                </select>
                    </div>
                </div>

              <div class="control-group">
                  <label class="control-label">账户类型：</label>
                  <div class="controls">
                      <select name="accType" id="accType" class="easyui-combobox" style="width: 200px;" >
                          <option value="0">请选择</option>
                          <option value="4" ${enterpriseInfo.platformId==4?'selected':''}>平台收支账户</option>
                          <option value="6" ${enterpriseInfo.platformId==6?'selected':''}>平台逾期垫付资金账户</option>
                          <option value="7" ${enterpriseInfo.platformId==7?'selected':''}>平台风险准备金账户</option>
                          <option value="8" ${enterpriseInfo.platformId==8?'selected':''}>平台运营账户</option>
                      </select>
                  </div>
              </div>
                <input type="hidden" name="accTypeUpdate" value="${enterpriseInfo.platformId}" />
				<div class="control-group">
                    <label class="control-label">公司名称：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[4,30]" required="true"
                        	name="enterpriseName" id="enterpriseName" value="${enterpriseInfo.enterpriseName }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">组织机构代码：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="organizationCode" id="organizationCode" value="${enterpriseInfo.organizationCode }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">法人姓名：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="legalPersonName" id="legalPersonName" value="${enterpriseInfo.legalPersonName }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">法人身份证号：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="legalPersonCode" id="legalPersonCode" value="${enterpriseInfo.legalPersonCode }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">开户许可证号：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="accountNumber" id="accountNumber" value="${enterpriseInfo.accountNumber }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">税务登记证代码：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="taxRegistrationCode" id="taxRegistrationCode" value="${enterpriseInfo.taxRegistrationCode }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">营业执照注册号：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="businessRegistrationNumber" id="businessRegistrationNumber" value="${enterpriseInfo.businessRegistrationNumber }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">经营年限：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,3]" required="true"
                        	name="operatingPeriod" id="operatingPeriod" value="${enterpriseInfo.operatingPeriod }">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">注册资金：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]" required="true"
                        	name="registeredCapital" id="registeredCapital" value="${enterpriseInfo.registeredCapital }">元
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">经营范围：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <input type="text" style="width: 440px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,200]" required="true"
                        	name="operatingRange" id="operatingRange" value="${enterpriseInfo.operatingRange }">
                    </div>
                </div>

				<div class="control-group">
                    <label class="control-label">企业信息：<span style="color: red;">*</span></label>
                    <div class="controls">
                        <textarea class="easyui-validatebox" style="width: 440px; height: 100px;" 
                        validType="length[2,200]" required="true"
                                name="information" id="information">${enterpriseInfo.information }</textarea>
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
                    <label class="control-label">主营收入：</label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,5]" precision="0"
                        	name="mainRevenue" id="mainRevenue" value="${enterpriseInfo.mainRevenue==0?'':enterpriseInfo.mainRevenue }"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">毛利润：</label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,5]" precision="0"
                        	name="grossProfit" id="grossProfit" value="${enterpriseInfo.grossProfit==0?'':enterpriseInfo.grossProfit }"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">净利润：</label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,5]" precision="0"
                        	name="netProfit" id="netProfit" value="${enterpriseInfo.netProfit==0?'':enterpriseInfo.netProfit }"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">总资产：</label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,5]" precision="0"
                        	name="totalAssets" id="totalAssets" value="${enterpriseInfo.totalAssets==0?'':enterpriseInfo.totalAssets }"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">净资产：</label>
                    <div class="controls">
                        <input type="text" style="width: 100px;"
                        	class="easyui-numberbox" validType="length[0,5]" precision="0"
                        	name="netAssets" id="netAssets" value="${enterpriseInfo.netAssets==0?'':enterpriseInfo.netAssets }"><span style="font-size: 12px;">万元</span>
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
	if('${enterpriseInfo.mainRevenue }' != '' || '${enterpriseInfo.grossProfit }' != '' || 
			'${enterpriseInfo.netProfit }' != '' || '${enterpriseInfo.totalAssets }' != '' || '${enterpriseInfo.netAssets }' != ''){
		if('${enterpriseInfo.mainRevenue }' != '0' || '${enterpriseInfo.grossProfit }' != '0' || 
				'${enterpriseInfo.netProfit }' != '0' || '${enterpriseInfo.totalAssets }' != '0' || '${enterpriseInfo.netAssets }' != '0'){
			$("#marketValue").attr("checked",true);
			$(".market").show();
		}else{
			$("#marketValue").attr("checked",false);
			$(".market").hide();
		}
	}else{
		$("#marketValue").attr("checked",false);
		$(".market").hide();
	}
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
	
	var url = '${ctx}/jsp/enterprise/editEnterprise?r=' + Math.random();
	
	$.post(url,
   		$("#enterprise_add_part1_form").serialize(),
   		function(data){
   	    	if(data.result == 'success'){
   	    		$.messager.alert("操作提示", "保存成功！", "info");
   	    		// 刷新主页面列表
                var params = window.opener.$('#enterpriseList_list').datagrid("options").queryParams;
                window.opener.$('#enterpriseList_list').datagrid("options").queryParams = params;
                window.opener.$('#enterpriseList_list').datagrid('reload');
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
