<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addDisActivity" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form class="form-horizontal" id="addDisActivity_form" method="post" action="">
        <div class="control-group">
           <label class="control-label">分销名称：</label>
           <div class="controls">
                <input type="text" style="width: 200px"
                       class="easyui-validatebox" required="true"
                       name="disName" id="disName" ><span style="color: red">*</span>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">期号：</label>
            <div class="controls">
				<input name="sectionCode" id="sectionCode" type="text" class="easyui-numberbox" precision="0" min="1" max="9999" style="text-align:right;" required="true"/>
				<span style="color: red">*</span>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">目标用户：</label>
            <div class="controls">
				<input type="radio"     name="targetUser"  value="0" checked><font style="font-size: 12px;">全部用户</font>
                <input type="radio"    name="targetUser"   value="1"><font style="font-size: 12px;">平台用户</font>
                <input type="radio"   name="targetUser"   value="2"><font style="font-size: 12px;">加盟销售</font>
				<span style="color: red">*</span>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">分销奖励限制：</label>
            <div class="controls">
				<input name="salesPointStart" id="salesPointStart" type="text" disabled="disabled" class="easyui-numberbox" precision="0" min="0" max="9999999999" style="text-align:right;" value="0"/>至
				<input name="salesPointEnd" id="salesPointEnd" type="text" disabled="disabled" class="easyui-numberbox" precision="0" min="0" max="9999999999" style="text-align:right;" value="9999999999"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">规则期限：</label>
            <div class="controls">
                <input id="ruleStartDate" name="ruleStartDate" editable="false" style="width:100px;" class="easyui-datebox" required="true"/><span style="color: red">*</span>至
                <input id="ruleEndDate" style="width:100px;" editable="false" name="ruleEndDate" class="easyui-datebox" required="true"/>
                <span style="color: red">*</span>
            </div>
        </div>
        <!-- <div class="control-group">
            <label class="control-label">取值时间：</label>
            <div class="controls">
                <input id="ruleStartDate1" name="ruleStartDate1" editable="false" style="width:100px;" class="easyui-datebox"/>至
                <input id="ruleEndDate1" style="width:100px;" editable="false" name="ruleEndDate1" class="easyui-datebox"/>
            </div>
        </div> -->
        <div class="control-group">
            <label class="control-label">分销描述：</label>
            <div class="controls">
                <textarea name="disDiscription" autocomplete="off" class="textbox-text" placeholder="" maxlength="65"
                          style="margin-left: 0px; margin-right: 0px; height: 75px; width: 350px;" required="true"></textarea>
            </div>
        </div>
        
		<hr width="100%">
        <div class="control-group">
            <label class="control-label" style="font-size: 15px">分销详细</label>
            <div class="controls" style="text-align: center">
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="addRules();">添加产品</a>
            </div>
        </div>
        
        <div id="rules">
	        <div id="childRules_v0">
		        <div class="control-group">
		           <label class="control-label">适用产品：</label>
		           <div class="controls">
						<input style='width: 300px' name='lendProductId' id='lendProductId' required="true"/>
						<span style="color: red">*</span>
		            </div>
		        </div>
		        <div class="control-group">
		           <label class="control-label">佣金发放节点：</label>
		           <div class="controls">
		           		<input style='width: 150px' name='commiPaidNode' id='commiPaidNode' required="true" panelHeight="auto"/>
		           		<span style="color: red">*</span>
		            </div>
		        </div>
		        <div class="control-group">
		           <label class="control-label">设置一级佣金值：</label>
		           <div class="controls">
		           		<input name="firstRate" type="text" class="easyui-numberbox" precision="2" min="0" max="100" style="text-align:right;" required="true"/>%
		           		<span style="color: red">*</span>
		            </div>
		        </div>
		        <div class="control-group">
		           <label class="control-label">设置二级佣金值：</label>
		           <div class="controls">
		           		<input name="secondRate" type="text" class="easyui-numberbox" precision="2" min="0" max="100" style="text-align:right;" required="true"/>%
		           		<span style="color: red">*</span>
		            </div>
		        </div>
		        <div class="control-group">
		           <label class="control-label">设置三级佣金值：</label>
		           <div class="controls">
		           		<input name="thirdRate" type="text" class="easyui-numberbox" precision="2" min="0" max="100" style="text-align:right;" required="true"/>%
		           		<span style="color: red">*</span>
		            </div>
		        </div>
	        </div>
        </div>
        <input type="hidden" name="lendProductIds" id="lendProductIds" value="" />
        <input type="hidden" name="commiPaidNodes" id="commiPaidNodes" value="" />
        <input type="hidden" name="firstRates" id="firstRates" value="" />
        <input type="hidden" name="secondRates" id="secondRates" value="" />
        <input type="hidden" name="thirdRates" id="thirdRates" value="" />
    </form>
</div>
<script language="javascript">

	//var commiPaidNodes = [ {"id" : '1', "text" : "放款"}, {"id" : '2', "text" : "周期还款"} ];
	var commiPaidNodes = [ {"id" : '2', "text" : "周期还款"} ];

	//加载关系下拉框。
	$("input[name='lendProductId']").combobox({
	    url: '${ctx}/disActivity/loadLendProduct?selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE'
	});
	
	$("input[name='commiPaidNode']").combobox({
		data : commiPaidNodes,
		valueField : 'id',
		textField : 'text',
		onLoadSuccess : function() {
			$(this).combobox('setValue', '2');
		}
	});
	
	$("#addDisActivity_form").form({
		url : '${ctx}/disActivity/save?me=add',
		dataType : 'json',
		onSubmit : function() {
			if($(this).form('validate')){
				var lendProductIds = $("input[name='lendProductId']");
				var commiPaidNodes = $("input[name='commiPaidNode']");
				//var lendProductIds = $("select[name='lendProductId']");
				//var commiPaidNodes = $("select[name='commiPaidNode']");
				//var lendProductIds = $("select[name=lendProductId] option[selected]");
				var firstRates = $("input[name='firstRate']");
				var secondRates = $("input[name='secondRate']");
				var thirdRates = $("input[name='thirdRate']");

				var arrayLendProductIds = [];
				var arrayCommiPaidNodes = [];
				var arrayFirstRates = [];
				var arraySecondRates = [];
				var arrayThirdRates = [];
				for (var i = 0; i < firstRates.length; i++) {
					if(null == lendProductIds[i].value || '' == lendProductIds[i].value){
						$.messager.alert("系统提示", "出借产品不能存在空值!", "error");
						return false;
					}
					if(null == commiPaidNodes[i].value || '' == commiPaidNodes[i].value){
						$.messager.alert("系统提示", "佣金发放节点不能存在空值!", "error");
						return false;
					}
					if(null == firstRates[i].value || '' == firstRates[i].value){
						$.messager.alert("系统提示", "一级佣金不能存在空值!", "error");
						return false;
					}
					if(null == secondRates[i].value || '' == secondRates[i].value){
						$.messager.alert("系统提示", "二级佣金不能存在空值!", "error");
						return false;
					}
					if(null == thirdRates[i].value || '' == thirdRates[i].value){
						$.messager.alert("系统提示", "三级佣金不能存在空值!", "error");
						return false;
					}
					arrayLendProductIds[i] = lendProductIds[i].value;
					arrayCommiPaidNodes[i] = commiPaidNodes[i].value;
					arrayFirstRates[i] = firstRates[i].value;
					arraySecondRates[i] = secondRates[i].value;
					arrayThirdRates[i] = thirdRates[i].value;
				}
				$("#lendProductIds").val(arrayLendProductIds);
				$("#commiPaidNodes").val(arrayCommiPaidNodes);
				$("#firstRates").val(arrayFirstRates);
				$("#secondRates").val(arraySecondRates);
				$("#thirdRates").val(arrayThirdRates);
				return true;
			}
			return false;
		},
		success : function(data) {
			var json = eval('(' + data + ')');
			if (json.isSuccess) {
				$.messager.alert("系统提示", "操作成功!", "info");
				parent.$("#addDisActivity").dialog("close");
				parent.$("#dis_activity_list_list").datagrid("reload");
			} else {
				$.messager.alert("系统提示", json.info, "error");
			}
		}
	});

	var i = 0;
	function addRules() {
		i++;

		var str = "<div id='childRules_v"+ i +"'>"
				+ "<hr width='80%'>"
				+ "<div class='control-group'><label class='control-label'>适用产品：</label><div class='controls'><input style='width: 300px' name='lendProductId' /><span style='color: red'>*</span></div></div>"
				+ "<div class='control-group'> <label class='control-label'>佣金发放节点：</label> <div class='controls'><input style='width: 150px' name='commiPaidNode' panelHeight='auto'/><span style='color: red'>*</span></div></div>"
				+ "<div class='control-group'><label class='control-label'>设置一级佣金值：</label><div class='controls'><input name='firstRate' type='text' class='easyui-numberbox' precision='2' min='0' max='100' style='text-align:right;'/>%<span style='color: red'>*</span></div></div>"
				+ "<div class='control-group'><label class='control-label'>设置二级佣金值：</label><div class='controls'><input name='secondRate' type='text' class='easyui-numberbox' precision='2' min='0' max='100' style='text-align:right;'/>%<span style='color: red'>*</span></div></div>"
				+ "<div class='control-group'><label class='control-label'>设置三级佣金值：</label><div class='controls'><input name='thirdRate' type='text' class='easyui-numberbox' precision='2' min='0' max='100' style='text-align:right;'/>%<span style='color: red'>*</span>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick='removeRules("+i+");'>删除产品</a></div></div>"
				+ "</div>";
				
		$("#rules").append(str);
		
		$("#childRules_v" + i + '>input').validatebox({ required:true });
		
		$("#childRules_v" + i).find("input[name='lendProductId']").combobox(
						{
							url: '${ctx}/disActivity/loadLendProduct?selectedDisplay=selected',
							textField : 'CONSTANTNAME',
							valueField : 'CONSTANTVALUE'
						});
		$("#childRules_v" + i).find("input[name='commiPaidNode']").combobox(
						{
							data : commiPaidNodes,
							valueField : 'id',
							textField : 'text',
							onLoadSuccess : function() {
								$(this).combobox('setValue', '2');
							}
						});
	}
	// 删除节点
	function removeRules(i) {
		$("#childRules_v" + i).remove();
	}

</script>
</body>
</html>