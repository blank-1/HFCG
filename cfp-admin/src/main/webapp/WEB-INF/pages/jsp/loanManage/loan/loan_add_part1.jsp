<%@ page import="com.xt.cfp.core.constants.SubjectTypeEnum" %>
<%@ page import="com.xt.cfp.core.constants.LoanTypeEnum" %>
<%@ page import="com.xt.cfp.core.constants.UserType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="loan_add_part1" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<h3>新增借款</h3>
<form class="form-horizontal" id="loan_add_part1_form" method="post">
	<table width="100%">
      <tr>
          <td>
         
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>标的类型：</label>
                    <div class="controls">
                        <input type="radio" id="subjectType" name="subjectType" onclick="onMark(<%=SubjectTypeEnum.LOAN.getValue()%>);" value="<%=SubjectTypeEnum.LOAN.getValue()%>" checked="checked"><font style="font-size: 12px;">借款标</font>
                        <input type="radio" id="subjectType" name="subjectType" onclick="onMark(<%=SubjectTypeEnum.CREDITOR.getValue()%>);" value="<%=SubjectTypeEnum.CREDITOR.getValue()%>" ><font style="font-size: 12px;">债权标</font>
                    </div>
                </div>
				
          		<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>标的来源：</label>
                    <div class="controls" >
                        <select type="text" class="easyui-combobox" required="true" style="width: 150px;" 
                        		name="channel" id="channel">
	                        <option value="">请选择来源类型</option>
		                    <option value="1">渠道</option>
		                    <option value="2">门店</option>
                        </select>
                        
                        <span id="div_channel">
	                        <input name="channelId" id="channelId" style="width: 150px;" >
	                        <span id="div_channel_originalUserId">
		                        <input name="originalUserId" id="originalUserId" style="width: 150px;">
	                        </span>
                        </span>
                        
                        <span id="div_store" style="display: none;">
	                        <input name="store_channelId" id="store_channelId" style="width: 150px;">
                        </span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款类型：</label>
                    <div class="controls" id="justloan">
                        <input type="radio" id="loanType" name="loanType" value="<%=LoanTypeEnum.LOANTYPE_CREDIT.getValue()%>" checked="checked"><font style="font-size: 12px;">信用贷</font>
                        <input type="radio" id="loanType" name="loanType" value="<%=LoanTypeEnum.LOANTYPE_HOUSE.getValue()%>" ><font style="font-size: 12px;">房贷</font>
                        <input type="radio" id="loanType" name="loanType" value="<%=LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue()%>" ><font style="font-size: 12px;">房产直投</font>
                        <input type="radio" id="loanType" name="loanType" value="<%=LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue()%>" ><font style="font-size: 12px;">个人信用车贷</font>
                    </div>

                    <div class="controls" id="fullcreate" style="display: none">
                        <input type="radio" id="loanType" name="loanType" value="<%=LoanTypeEnum.LOANTYPE_CREDIT.getValue()%>" checked="checked"><font style="font-size: 12px;">信用贷</font>
                        <input type="radio" id="loanType" name="loanType" value="<%=LoanTypeEnum.LOANTYPE_HOUSE.getValue()%>" ><font style="font-size: 12px;">房贷</font>
                        <input type="radio" id="loanType" name="loanType" value="<%=LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue()%>" ><font style="font-size: 12px;">房产直投</font>
                        <input type="radio" id="loanType" name="loanType"  value="<%=LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue()%>" ><font style="font-size: 12px;">现金贷</font>
                    </div>
                </div>
          
          		<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>对应用户类型：</label>
                    <div class="controls">
                        <input type="radio" id="userType" name="userType" value="<%=UserType.LINE.getValue()%>" checked="checked"><font style="font-size: 12px;">线下用户</font>
                        <input type="radio" id="userType" name="userType" value="<%=UserType.COMMON.getValue()%>" ><font style="font-size: 12px;">普通用户</font>
                    </div>
                </div>
          
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款人身份证号：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-validatebox" required="true" validType="length[15,18]"
                        	name="idCard" id="idCard">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款人姓名：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-validatebox" required="true" validType="length[2,10]"
                        	name="trueName" id="trueName">
                    </div>
                </div>

          </td>
      </tr>
	</table>
</form>
<input style="margin-left: 250px;width: 95px" type="button" class="btn btn-primary" value="下一步" onclick="saveButton();" >
</div>

<script type="text/javascript">
   /* $("#div_channel_originalUserId").hide();*/
//   $("#div_store").hide();
  /* $('#channel').combobox('select',1);// 渠道选中*/
//   $("#div_channel_originalUserId").show();
//   $("#fullcreate").css("display","block");
//   $("#justloan").css("display","none");
// 执行：标的类型单击事件。
function onMark(m){
    var values  = $('input[name="loanType"]');
    console.log(values+"   --")
    if(m == '<%=SubjectTypeEnum.CREDITOR.getValue()%>'){// 债权标
		$("#div_channel").show();
		$("#div_store").hide();
//		$('#channel').combobox('select',1);// 渠道选中
		$("#div_channel_originalUserId").show();
        $("#fullcreate").css("display","block");
        $("#justloan").css("display","none");
	}else{
        $("#div_channel_originalUserId").show();
        $("#fullcreate").css("display","none");
        $("#justloan").css("display","block");

	}
}

// 执行：来源类型选择事件。
function onChange(){
	$("#channel").combobox({
		onChange:function(rec){
			if(rec == 2){// 门店
				$("#div_channel").hide();
				$("#div_store").show();
			}else{
				$("#div_channel").show();
				$("#div_store").hide();
			}
		}
	});
}


// 执行:下一步。
function saveButton(){
	console.log("--------------------------------");
	// 验证
	if(!$("#loan_add_part1_form").form('validate')){
		return false;
	}
	
	// 身份证证号验证

	if (!checkCard($("#idCard").val().trim())) {
        $.messager.alert("验证提示", "身份证号格式不正确！", "info");
        return false;
    }

    $.messager.confirm("系统提示", "系统将对该借款人姓名与身份证号进行实名认证，是否确认该借款人的姓名与身份证号？", function (r) {
        if (r) {
            $.post('${ctx}/jsp/loanManage/loan/check_idcard',{trueName:$("#trueName").val(),idCard:$("#idCard").val()},function(data) {
                if (data) {
                    $.post('${ctx}/jsp/loanManage/loan/save_loan_part1',
                            $("#loan_add_part1_form").serialize(),
                            function(data){
                                if(data.result == 'success'){
                                    //$.messager.alert("操作提示", "保存成功！", "info");
                                    window.location.href = '${ctx}/jsp/loanManage/loan/to_loan_add_part234?actionType=add&loanApplicationId=' + data.data.loanApplicationId;
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
                } else {
                    $.messager.alert("系统提示", "借款人姓名与身份证号不符，请核实后填写！", "info");
                }
            })

        	
        }
    })
}

//加载渠道下拉框
$("#loan_add_part1_form #channelId").combobox({
    url: '${ctx}/bondSource/loadBondSource?selectedDisplay=selected',
    textField: 'SOURCENAME',
    valueField: 'BONDSOURCEID',
    onSelect: function (record) {
        $("#loan_add_part1_form #originalUserId").combobox("reload",
        	'${ctx}/bondSource/loadBondSourceUser?bondSourceId=' + record.BONDSOURCEID + '&selectedDisplay=selected');
    }
});

//加载原始债权人下拉框
$("#loan_add_part1_form #originalUserId").combobox({
    url: '${ctx}/bondSource/loadBondSourceUser?bondSourceId=0&selectedDisplay=selected',
    textField: 'BONDNAME',
    valueField: 'USERSOURCEID',
    onChange: function (n,o) {
        $.ajax({
            url:'${ctx}/bondSource/selectUserByBondUserId?userSourceId='+n,
            type:"POST",
            success:function(msg){
               console.log(msg.idcard)

                $("#trueName").val(msg.realname);
                $("#idCard").val(msg.idcard);


                  $("#trueName").attr("readonly",true);
                  $("#idCard").attr("readonly",true);
            }
        });


    }
});

//加载门店下拉框
$("#loan_add_part1_form #store_channelId").combobox({
    url: '${ctx}/jsp/constant/loadStore?&selectedDisplay=selected',
    textField: 'STORENAME',
    valueField: 'STOREID',
});

$(function(){
	onChange();
});

</script>
</body>
</html>
