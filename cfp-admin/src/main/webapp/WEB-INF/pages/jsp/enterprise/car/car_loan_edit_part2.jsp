<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="car_loan_edit_part2" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<input style="float: right;width: 100px;" type="button" class="btn  btn-primary"  value="保 存" onclick="save_car_loan_edit_part2();">
<form class="form-horizontal" id="car_loan_edit_part2_form" method="post" style="margin-top: 50px;">
<input type="hidden" name="loanApplicationId" id="loanApplicationId" value="${loanApplicationId }">
	<table width="100%">
      <tr>
          <td>
          
          		<!-- 车信息数组隐藏域 -->
                <input type="hidden" id="carInfoArray" name="carInfoArray">
                
				<div class="control-group" id="carInfo">
                    <label class="control-label">
                    	<input type="radio" id="arrived" name="arrived" value="1" ${mortgageCar.arrived==1?'checked':'' }><span style="font-size: 12px;">一抵</span>&nbsp;&nbsp;&nbsp;&nbsp;
                    	<input type="radio" id="arrived" name="arrived" value="2" ${mortgageCar.arrived==2?'checked':'' }><span style="font-size: 12px;">二抵</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					汽车品牌：</label>
                    <div class="controls" >
                        <input type="text" style="width: 100px" class="easyui-validatebox" validType="length[0,20]" 
                        name="automobileBrand" id="automobileBrand" value="${mortgageCar.automobileBrand }">&nbsp;&nbsp;
                        <span style="font-size: 12px;">汽车型号：</span>
                        <input type="text" style="width: 100px" class="easyui-validatebox" validType="length[0,10]" 
                        name="carModel" id="carModel" value="${mortgageCar.carModel }">&nbsp;&nbsp;
                        <span style="font-size: 12px;">市场价格：</span>
                        <input type="text" style="width: 100px" class="easyui-numberbox" validType="length[0,10]" 
                        name="marketPrice" id="marketPrice" onkeyup="onRatio();" value="${mortgageCar.marketPrice }"><span style="font-size: 12px;">万元</span>&nbsp;&nbsp;
                        <span style="font-size: 12px;">车架号：</span>
                        <input type="text" style="width: 100px" class="easyui-validatebox" validType="length[0,11]" 
                        name="frameNumber" id="frameNumber" value="${mortgageCar.frameNumber }">
                        <i class="icon-plus" style="margin-left: 5px;cursor: pointer;" title="添加汽车信息" onclick="onCarInfo_plus()"></i>
                    </div>
                </div>
                <hr id="carInfo_end">
          		
          		<div class="control-group">
                    <label class="control-label">抵押总价值：</label>
                    <div class="controls">
                       	<span style="font-size: 12px;" id="totalMortgageValue">${car.totalMortgageValue }</span><span style="font-size: 12px;">万元</span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">授信上限：</label>
                    <div class="controls">
                        <input type="text" style="width: 100px;margin-top: 3px;"
                        	class="easyui-numberbox" validType="length[0,10]" 
                        	name="creditLimit" id="creditLimit" onkeyup="onRatio();" value="${car.creditLimit }"><span style="font-size: 12px;">万元</span>
                        	&nbsp;&nbsp;&nbsp;&nbsp;
                        	<span style="font-size: 12px;">授信比例：</span>
                        	<span style="font-size: 12px;" id="creditRatio"></span>
                    </div>
                </div>
                
                <div class="control-group" style="margin-top: 10px;">
                    <label class="control-label">描述：</label>
                    <div class="controls">
                        <textarea name="mortgageDescription" id="mortgageDescription"
                        class="easyui-validatebox" validType="length[0,200]"
                        style="width: 440px; height: 100px;">${car.mortgageDescription }</textarea>
                    </div>
                </div>

          </td>
      </tr>
	</table>
</form>
</div>

<script type="text/javascript">

//执行：添加汽车信息。
var i = 0;
function onCarInfo_plus(){
	i += 1;
    
    var c = "<div class='control-group' id='carInfo_" + i + "'>" +
	    "<label class='control-label'>" +
	    "<input type='radio' id='arrived_" + i + "' name='arrived_" + i + "' value='1' checked='checked'><span style='font-size: 12px;'>一抵</span>&nbsp;&nbsp;&nbsp;&nbsp;" +
	    "<input type='radio' id='arrived_" + i + "' name='arrived_" + i + "' value='2'><span style='font-size: 12px;'>二抵</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		"汽车品牌：</label>" +
	    "<div class='controls' >" +
	        "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[0,20]'" + 
	        "name='automobileBrand_" + i + "' id='automobileBrand_" + i + "' >&nbsp;&nbsp;&nbsp;" +
	        "<span style='font-size: 12px;'>汽车型号：</span>&nbsp;" +
	        "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[0,10]'" + 
	        "name='carModel_" + i + "' id='carModel_" + i + "'>&nbsp;&nbsp;&nbsp;" +
	        "<span style='font-size: 12px;'>市场价格：</span>&nbsp;" +
	        "<input type='text' style='width: 100px' class='easyui-numberbox' validType='length[0,10]'" + 
	        "name='marketPrice_" + i + "' id='marketPrice_" + i + "' onkeyup='onRatio();'><span style='font-size: 12px;'>万元</span>&nbsp;&nbsp;&nbsp;" +
	        "<span style='font-size: 12px;'>车架号：</span>&nbsp;" +
	        "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[0,11]'" + 
	        "name='frameNumber_" + i + "' id='frameNumber_" + i + "'>&nbsp;" +
	        "<i class='icon-trash' style='margin-left: 5px;cursor: pointer;' title='删除汽车信息' onclick='onCarInfo_trash(" + i + ")'></i>" +
	    "</div>" +
	"</div>";
    
	$("#carInfo_end").before(c);
}

// 执行：删除汽车信息。
function onCarInfo_trash(carInfo_num){
	$("#carInfo_" + carInfo_num).remove();
	onRatio();//重新计算授信比例。
}

//汽车信息加载回显。
function loanCarInfo(){
	var mortgageCarList = ${mortgageCarList};
	if(null != mortgageCarList && mortgageCarList.length > 0){
		for(var im=0;im < mortgageCarList.length;im++){
			var arrived = mortgageCarList[im].arrived;
			var automobileBrand = mortgageCarList[im].automobileBrand;
			var carModel = mortgageCarList[im].carModel;
			var marketPrice = mortgageCarList[im].marketPrice;
			var frameNumber = mortgageCarList[im].frameNumber;
			
			i += 1;
		    
		    var c = "<div class='control-group' id='carInfo_" + i + "'>";
			    c += "<label class='control-label'>";
			    c += "<input type='radio' id='arrived_" + i + "' name='arrived_" + i + "' value='1' ";
			    if(arrived == 1){
			    	c += "checked='checked'";
			    }
			    c += "><span style='font-size: 12px;'>一抵</span>&nbsp;&nbsp;&nbsp;&nbsp;";
			    c += "<input type='radio' id='arrived_" + i + "' name='arrived_" + i + "' value='2' ";
			    if(arrived == 2){
			    	c += "checked='checked'";
			    }
			    c += "><span style='font-size: 12px;'>二抵</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			    c += "汽车品牌：</label>";
			    c += "<div class='controls' >";
			    c += "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[0,20]'";
			    c += "name='automobileBrand_" + i + "' id='automobileBrand_" + i + "' value='" + automobileBrand + "'>&nbsp;&nbsp;&nbsp;";
			    c += "<span style='font-size: 12px;'>汽车型号：</span>&nbsp;";
			    c += "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[0,10]'";
			    c += "name='carModel_" + i + "' id='carModel_" + i + "' value='" + carModel + "'>&nbsp;&nbsp;&nbsp;";
			    c += "<span style='font-size: 12px;'>市场价格：</span>&nbsp;";
			    c += "<input type='text' style='width: 100px' class='easyui-numberbox' validType='length[0,10]'";
			    c += "name='marketPrice_" + i + "' id='marketPrice_" + i + "' onkeyup='onRatio();' value='" + marketPrice + "'><span style='font-size: 12px;'>万元</span>&nbsp;&nbsp;&nbsp;";
			    c += "<span style='font-size: 12px;'>车架号：</span>&nbsp;";
			    c += "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[0,11]'";
			    c += "name='frameNumber_" + i + "' id='frameNumber_" + i + "' value='" + frameNumber + "'>&nbsp;";
			    c += "<i class='icon-trash' style='margin-left: 5px;cursor: pointer;' title='删除汽车信息' onclick='onCarInfo_trash(" + i + ")'></i>";
			    c += "</div>";
			    c += "</div>";
			$("#carInfo_end").before(c);
		}
	}
}

// 执行:保存。
function save_car_loan_edit_part2(){
	
	// 验证
	if(!$("#car_loan_edit_part2_form").form('validate')){
		return false;
	}
	
	// 单独获取汽车信息。
	var carInfoArray = new Array();
	var c = {};
	c.arrived = $("input[name='arrived']:checked").val();
	c.automobileBrand = $("#automobileBrand").val();
	c.carModel = $("#carModel").val();
	c.marketPrice = $("#marketPrice").val();
	c.frameNumber = $("#frameNumber").val();
	carInfoArray[0] = c;
	if(i > 0){
		for(var j=1;j <= i;j++){
			var cs = {};
			var isE = false;
			cs.arrived = $("input[name='arrived_" + j + "']:checked").val();
			if($("#automobileBrand_" + j).length > 0){
				cs.automobileBrand = $("#automobileBrand_" + j).val();
				isE = true;
			}
			if($("#carModel_" + j).length > 0){
				cs.carModel = $("#carModel_" + j).val();
				isE = true;
			}
			if($("#marketPrice_" + j).length > 0){
				cs.marketPrice = $("#marketPrice_" + j).val();
				isE = true;
			}
			if($("#frameNumber_" + j).length > 0){
				cs.frameNumber = $("#frameNumber_" + j).val();
				isE = true;
			}
			if(isE==true){
				carInfoArray[j] = cs;	
			}
		}	
	}
	// 将汽车数组信息存入隐藏区域。
	$("#carInfoArray").attr("value", JSON.stringify(carInfoArray));
	
	$.post('${ctx}/jsp/enterprise/loan/save_enterprise_loan_mortgage_car?r=' + Math.random(),
   		$("#car_loan_edit_part2_form").serialize(),
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

//授信比例=授信金额/抵押总价值
function onRatio(){
	var creditRatio = parseFloat(0);//授信比例
	var marketPrices = parseFloat(0);//抵押总价值
	var creditLimit = parseFloat($('#creditLimit').val());//授信金额
	
	marketPrices = parseFloat($('#marketPrice').val());
	if(i > 0){
		for(var j=1;j <= i;j++){
			if($("#marketPrice_" + j).length > 0){
				marketPrices += parseFloat($("#marketPrice_" + j).val());
			}
		}	
	}
	
	if(marketPrices != '' && marketPrices > 0 && creditLimit != '' && creditLimit > 0){
		creditRatio = creditLimit / marketPrices;
		creditRatio = creditRatio*100;
	}
	
	if(marketPrices != '' && marketPrices > 0){
		$("#totalMortgageValue").html(marketPrices);	
	}
	$("#creditRatio").html(parseInt(creditRatio) + '%');
}

$(function(){
	loanCarInfo();
	onRatio();
});
</script>
</body>
</html>
