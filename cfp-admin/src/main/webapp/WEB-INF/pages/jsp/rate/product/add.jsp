<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addRateProduct" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form class="form-horizontal" id="addRateProduct_form" method="post" action="${ctx}/jsp/rate/product/add">

        <div class="control-group">
            <label class="control-label">加息券名称：</label>

           <div class="controls">
                <input type="text" style="width: 350px"
                       class="easyui-validatebox" required="true" missingMessage="加息券名称不能为空" validType="length[4,20]"
                       id="rateProductName" name="rateProductName" /><span style="color: red">*</span>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">加息利率：</label>
            
            <div class="controls">
                <input type="text" style="width:100px;" 
                	   class="easyui-validatebox" required="true" missingMessage="加息利率不能为空" validType="length[1,3]"
                	   onkeyup="check_rateValue()"
                	   id="rateValue" name="rateValue" /> %<span style="color: red">*</span>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">使用场景：</label>

            <div class="controls">
                <input type="radio" name="usageScenario" value="1" checked /> 所有&nbsp;
                <input type="radio" name="usageScenario" value="2" /> 投标&nbsp;
                <input type="radio" name="usageScenario" value="3" /> 购买理财&nbsp;
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">使用条件：</label>

            <div class="controls">
                <input type="radio" id="condition0_nothing_radio1" name="condition0_nothing" onclick="onclick_condition0_nothing_radio()" value="false" checked /> 无条件&nbsp;
                <input type="radio" id="condition0_nothing_radio2" name="condition0_nothing" onclick="onclick_condition0_nothing_radio()" value="true" /> 有条件&nbsp;
            </div>
            
        </div>
        <div class="control-group">
            <label class="control-label"></label>

            <div class="controls">
                <input type="checkbox" class="condition" id="condition1_term" name="condition1_term" onclick="onclick_condition1_term_checkbox()" value="true"/> 标的期限&nbsp;
                
                <input type="text" style="width:50px;" 
                	   class="easyui-validatebox con1" required="true" missingMessage="不能为空" validType="length[1,2]"
                	   onkeyup="check_con1_min()"
                	   id="con1_min" name="con1_min" />-
                <input type="text" style="width:50px;" 
                	   class="easyui-validatebox con1" required="true" missingMessage="不能为空" validType="length[1,2]"
                	   onkeyup="check_con1_max()"
                	   id="con1_max" name="con1_max" /> 限投 n1-n2月标
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"></label>

            <div class="controls">
                <input type="checkbox" class="condition" id="condition2_type" name="condition2_type" onclick="onclick_condition2_type_checkbox()" value="true"/> 标的类型&nbsp;
                <input type="checkbox" class="con2" id="con2_0" name="con2_0" value="true" /> 信贷&nbsp;
                <input type="checkbox" class="con2" id="con2_1" name="con2_1" value="true" /> 房贷&nbsp;
                <input type="checkbox" class="con2" id="con2_2" name="con2_2" value="true" /> 企业车贷&nbsp;
                <input type="checkbox" class="con2" id="con2_3" name="con2_3" value="true" /> 企业信贷&nbsp;
                <input type="checkbox" class="con2" id="con2_4" name="con2_4" value="true" /> 企业保理&nbsp;
                <input type="checkbox" class="con2" id="con2_5" name="con2_5" value="true" /> 基金&nbsp;
                <input type="checkbox" class="con2" id="con2_6" name="con2_6" value="true" /> 企业标&nbsp;
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"></label>

            <div class="controls">
                <input type="checkbox" class="condition" id="condition3_amount" name="condition3_amount" onclick="onclick_condition3_amount_checkbox()" value="true"/> 起投金额&nbsp;
                
                <input type="text" style="width:80px;" 
                	   class="easyui-validatebox" required="true" missingMessage="不能为空"
                	   onkeyup="check_con3_start_amount()" maxlength="8"
                	   id="con3_start_amount" name="con3_start_amount" /> 单笔投资达 n 时可用
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">使用次数：</label>

            <div class="controls">
                <input type="radio" id="usageTimes_radio1" name="usageTimes_radio" onclick="onclick_usageTimes_radio()" value="1" checked /> 单次&nbsp;
                
                <input type="radio" id="usageTimes_radio2" name="usageTimes_radio" onclick="onclick_usageTimes_radio()" value="" /> 多次
                <input type="text" style="width:100px;"
                	   class="easyui-validatebox" required="true" missingMessage="使用次数不能为空" 
                	   onkeyup="check_usageTimes()" maxlength="4"
                	   id="usageTimes" name="usageTimes" />
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">有效时长：</label>

            <div class="controls">
                <input id="usageDuration_radio1" name="usageDuration_radio" onclick="onclick_usageDuration_radio()" type="radio" value="" checked />
                    <input type="text" style="width:100px;"
                    	   class="easyui-validatebox" required="true" missingMessage="有效时长不能为空" 
                    	   onkeyup="check_usageDuration()" maxlength="4"
                    	    id="usageDuration" name="usageDuration" />天

                <input id="usageDuration_radio2" name="usageDuration_radio" onclick="onclick_usageDuration_radio()" type="radio" value="0" /> 长期有效
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">生效时间：</label>

            <div class="controls">
                <input id="v_startDate" name="startDate" editable="false" style="width:100px;" class="easyui-datebox" />至
                <input id="w_endDate" name="endDate" editable="false" style="width:100px;" class="easyui-datebox" /><span style="color: red">*</span>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">叠加使用：</label>

            <div class="controls">
                <input type="radio" name="isOverlay" value="0" checked/> 不可以&nbsp;
                <input type="radio" name="isOverlay" value="1" /> 可以（谨慎选择）&nbsp;
            </div>
        </div>
        
        <div class="control-group" id="voucherRemark">
            <label class="control-label">使用说明：</label>

            <div class="controls">
                <textarea name="usageRemark" autocomplete="off" class="textbox-text" placeholder=""
                          style="margin-left: 0px; margin-right: 0px; height: 75px; width: 350px;"></textarea>
            </div>
        </div>
        
    </form>
</div>
<script language="javascript">
	//加息利率
	function check_rateValue(){
	    var val = $("#rateValue").val();
	    if(isNaN(val)||val<0){
	        $("#rateValue").val("");
	    }
	}
	
	//标的期限（小）
	function check_con1_min(){
		var minval = $("#con1_min").val();
	    if(isNaN(minval)||minval<=0){
	        $("#con1_min").val("");
	    }
	}
	
	//标的期限（大）
	function check_con1_max(){
	    var maxval = $("#con1_max").val();
	    if(isNaN(maxval)||maxval<=0){
	        $("#con1_max").val("");
	    }
	}
	
	//起投金额
	function check_con3_start_amount(){
	    var val = $("#con3_start_amount").val();
	    if(isNaN(val)||val<=0){
	        $("#con3_start_amount").val("");
	    }
	}
	
	//使用次数
	function check_usageTimes(){
	    var val = $("#usageTimes").val();
	    if(isNaN(val)||val<=0){
	        $("#usageTimes").val("");
	    }
	}
	
	//有效时长
	function check_usageDuration(){
	    var val = $("#usageDuration").val();
	    if(isNaN(val)||val<=0){
	        $("#usageDuration").val("");
	    }
	}
	
	//使用次数，单选事件
	function onclick_usageTimes_radio(){
        var checked1 = $("#usageTimes_radio1").attr("checked");//单次
        var checked2 = $("#usageTimes_radio2").attr("checked");//多次
        if(checked1=='checked'){
            $("#usageTimes").attr("disabled","disabled");
            $('#usageTimes').validatebox('remove'); //删除
            //$("#usageTimes").attr("value","1");//赋值单次
        }
        if(checked2=='checked'){
            $("#usageTimes").removeAttr("disabled");
            $('#usageTimes').validatebox('reduce'); //恢复
            $("#usageTimes").attr("value","");//赋值空
        }
    }
	
	//有效时长，单选事件
	function onclick_usageDuration_radio(){
        var checked1 = $("#usageDuration_radio1").attr("checked");//N天
        var checked2 = $("#usageDuration_radio2").attr("checked");//长期
        if(checked1=='checked'){
        	$("#usageDuration").removeAttr("disabled");
            $('#usageDuration').validatebox('reduce'); //恢复
            $("#usageDuration").attr("value","");//赋值空
        }
        if(checked2=='checked'){
            $("#usageDuration").attr("disabled","disabled");
            $('#usageDuration').validatebox('remove'); //删除
            //$("#usageDuration").attr("value","0");//赋值单次
        }
    }
	
	//标的期限，复选事件
	function onclick_condition1_term_checkbox(){
		$(".con1").attr("value","");//赋值空
        if($("#condition1_term").is(":checked")){//选中
        	$(".con1").removeAttr("disabled");
        	$('.con1').validatebox('reduce'); //恢复
        }else{
        	$(".con1").attr("disabled","disabled");
        	$('.con1').validatebox('remove'); //删除
        }
    }
	
	//标的类型，复选事件
	function onclick_condition2_type_checkbox(){
		$(".con2").removeAttr("checked");//清理选中
        if($("#condition2_type").is(":checked")){//选中
        	$(".con2").removeAttr("disabled");
        }else{
        	$(".con2").attr("disabled","disabled");
        }
    }
	
	//起投金额，复选事件
	function onclick_condition3_amount_checkbox(){
		$("#con3_start_amount").attr("value","");//赋值空
        if($("#condition3_amount").is(":checked")){//选中
        	$("#con3_start_amount").removeAttr("disabled");
        	$('#con3_start_amount').validatebox('reduce'); //恢复
        }else{
        	$("#con3_start_amount").attr("disabled","disabled");
        	$('#con3_start_amount').validatebox('remove'); //删除
        }
    }
	
	//使用条件，单选事件
	function onclick_condition0_nothing_radio(){
        var checked1 = $("#condition0_nothing_radio1").attr("checked");//无条件
        var checked2 = $("#condition0_nothing_radio2").attr("checked");//有条件
        $(".condition").removeAttr("checked");//清理选中
        if(checked1=='checked'){
            $(".condition").attr("disabled","disabled");
        }
        if(checked2=='checked'){
        	$(".condition").removeAttr("disabled");
        }
        //标的期限，复选事件
		onclick_condition1_term_checkbox();
		//标的类型，复选事件
		onclick_condition2_type_checkbox();
		//起投金额，复选事件
		onclick_condition3_amount_checkbox();
    }
	
	//页面初始化
	$(function () {
		//使用次数，单选事件
		onclick_usageTimes_radio();
		//标的期限，复选事件
		onclick_condition1_term_checkbox();
		//标的类型，复选事件
		onclick_condition2_type_checkbox();
		//起投金额，复选事件
		onclick_condition3_amount_checkbox();
		//使用条件，单选事件
		onclick_condition0_nothing_radio();
    });
	
</script>
</body>
</html>