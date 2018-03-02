<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="loan_add_part3" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<input style="margin-left: 800px;" type="button" class="btn  btn-primary" value="保 存" onclick="saveButtonEdit3();">
<form class="form-horizontal" id="loan_add_part3_form" method="post">
	<input type="hidden" id="loanApplicationId" name="loanApplicationId" value="${loanApplicationId}">
	<table width="100%">
      <tr>
          <td>
          		<hr>
          
          		<h3>抵押信息[编辑]</h3>
          		
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>抵押类型：</label>
                    <div class="controls">
                        <input type="radio" id="mortgageType" name="mortgageType" value="1" checked="${house.mortgageType==1?'checked':''}"><font style="font-size: 12px;">一抵</font>
                        <input type="radio" id="mortgageType" name="mortgageType" value="2" checked="${house.mortgageType==2?'checked':''}"><font style="font-size: 12px;">二抵</font>
                    </div>
                </div>
          
				<div class="control-group">
                    <label class="control-label">房屋类型：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px" id="houseType" name="houseType">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">房屋地址：</label>
                    <div class="controls" >
                        <select name="houseAddr_provence" id="houseAddr_provence" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty provinceInfos}">
	                        	<c:forEach items="${provinceInfos}" var="provinceInfo">
	                        		<c:if test="${provinceInfo.provinceId == houseAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" selected="selected">${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        		<c:if test="${provinceInfo.provinceId != houseAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" >${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        
                        <span style="font-size: 12px;">省</span>
                        <select name="houseAddr_city" id="houseAddr_city" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty houseCitys && not empty houseAddr.province}">
	                        	<c:forEach items="${houseCitys}" var="houseCity">
	                        		<c:if test="${houseCity.cityId == houseAddr.city}">
	                        			<option value="${houseCity.cityId}" selected="selected">${houseCity.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${houseCity.cityId != houseAddr.city}">
	                        			<option value="${houseCity.cityId}" >${houseCity.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        
                        <span style="font-size: 12px;">市</span>
                        <select name="houseAddr_district" id="houseAddr_district" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty houseDistricts && not empty houseAddr.city}">
	                        	<c:forEach items="${houseDistricts}" var="houseDistrict">
	                        		<c:if test="${houseDistrict.cityId == houseAddr.district}">
	                        			<option value="${houseDistrict.cityId}" selected="selected">${houseDistrict.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${houseDistrict.cityId != houseAddr.district}">
	                        			<option value="${houseDistrict.cityId}" >${houseDistrict.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        
                        <span style="font-size: 12px;">区/县</span><br/>
                        <input type="text" style="width: 440px;margin-top: 3px;" class="easyui-validatebox" validType="length[2,50]" 
                        name="houseAddr_detail" id="houseAddr_detail" value="${houseAddr.detail}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">房屋面积：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-numberbox" validType="length[0,4]" precision="0"
                        	name="houseSize" id="houseSize" value="${house.houseSize}"><span style="font-size: 12px;">平米</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">购买时间：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;"
                        	class="easyui-datebox"
                        	name="buyDate" id="buyDate" value="<fmt:formatDate value="${house.buyDate}" pattern="yyyy-MM-dd"/>">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">购买价格：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;"
                        	class="easyui-numberbox" validType="length[0,4]" precision="0"
                        	name="buyValue" id="buyValue" value="${house.buyValue}"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">市场价格：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;"
                        	class="easyui-numberbox" validType="length[0,4]" precision="0"
                        	name="marketValue" id="marketValue" value="${house.marketValue}"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">评估价格：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;"
                        	class="easyui-numberbox" validType="length[0,4]" precision="0"
                        	name="assessValue" id="assessValue" value="${house.assessValue}"><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">房产证字号：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;"
                        	class="easyui-validatebox" validType="length[0,30]" 
                        	name="houseCardNumber" id="houseCardNumber" value="${house.houseCardNumber}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">备注：</label>
                    <div class="controls">
                        <textarea class="easyui-validatebox" style="width: 440px; height: 100px;" validType="length[2,200]"
                                name="desc" id="desc">${house.desc}</textarea>
                    </div>
                </div>

          </td>
      </tr>
	</table>
</form>
<input style="margin-left: 800px;margin-bottom: 40px;" type="button" class="btn btn-primary" value="保 存" onclick="saveButtonEdit3();">
</div>

<script type="text/javascript">

// 执行:保存。
function saveButtonEdit3(){
	
	// 验证
	if(!$("#loan_add_part3_form").form('validate')){
		return false;
	}
	
	$.post('${ctx}/jsp/loanManage/loan/save_loan_part3?r=' + Math.random(),
   		$("#loan_add_part3_form").serialize(),
   		function(data){
   	    	if(data.result == 'success'){
   	    		$.messager.alert("操作提示", "保存成功！", "info");
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

// 加载房屋类型下拉框。
$("#loan_add_part3_form #houseType").combobox({
    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=houseType&parentConstant=0&selectedDisplay=selected',
    textField: 'CONSTANTNAME',
    valueField: 'CONSTANTVALUE',
    onLoadSuccess: function (record) {
    	$("#loan_add_part3_form #houseType").combobox("select", '${house.houseType}');
    }
});

//加载省份下拉框。
$("#loan_add_part3_form #houseAddr_provence").combobox({
    //url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
    textField: 'PROVINCENAME',
    valueField: 'PROVINCEID',
    onSelect: function (record) {
        $("#loan_add_part3_form #houseAddr_city").combobox("reload",
        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
    }
});

//加载城市下拉框。
$("#loan_add_part3_form #houseAddr_city").combobox({
    //url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
    textField: 'CITYNAME',
    valueField: 'CITYID',
    onSelect: function (record) {
    	var provinceId = $("#houseAddr_provence").combobox("getValue");
        $("#loan_add_part3_form #houseAddr_district").combobox("reload",
        	'${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
    }
});

//加载区县下拉框。
$("#loan_add_part3_form #houseAddr_district").combobox({
    //url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
    textField: 'CITYNAME',
    valueField: 'CITYID'
});

</script>
</body>
</html>
