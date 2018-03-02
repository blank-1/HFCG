<%--
  Created by IntelliJ IDEA.
  User: yulei
  Date: 2015/7/27
  Time: 19:44
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<title></title>
<%@include file="../../common/common.jsp" %>
</head>
<body>
  <div id="utils" class="easyui-tabs" data-options="plain:true, fit:true">
    <div title="手动充值">
      <div id="addAdmin" class="" style="padding: 5px 0px 0px 10px">
        <form class="container form-horizontal" id="rechargeForm" action="${ctx}/sysutils/doSysRechargeForAward" method="post">
          <div class="control-group hidden" id="d_ui">
            <label class="control-label">用户ID<span style="color: red">*</span></label>
            <div class="controls">
              <input type="text"
                     class="easyui-validatebox" required="true" missingMessage="用户ID不能为空"
                     name="userId" id="userId" value="">
            </div>
          </div>
          <div class="control-group hidden" id="d_ra">
            <label class="control-label">充值金额<span style="color: red;">*</span></label>
            <div class="controls">
              <input type="text" required="true" missingMessage="充值金额不能为空" class="easyui-validatebox"
                     name="amount" id="amount" value="">
            </div>
          </div>
          <div class="control-group hidden" id="d_rb">
            <label class="control-label">交易流水号<span style="color: red;">*</span></label>
            <div class="controls">
              <input type="text" required="true" missingMessage="交易流水号不能为空" class="easyui-validatebox"
                     name="externalNo" id="externalNo" value="">
            </div>
          </div>
          <div class="control-group hidden" id="d_rc">
            <label class="control-label">充值渠道<span style="color: red;">*</span></label>
            <div class="controls">
              <select required="true" missingMessage="充值渠道编号不能为空"  style="height: auto;" name="channelCode" id="channelCode" >
          <%--       <c:forEach items="${channels }" var = "channel">
                <option value="${channel.value }">${channel.desc }</option>
                </c:forEach> --%>
                <option value="0">划扣</option>
                <option value="1">转账</option>
                <option value="2">现金</option>
                <option value="FUIOU_POS">富友POS刷卡</option>
              </select>
            </div>
          </div>
          <div class="control-group" id="d_fi_award">
            <label class="control-label">导入文件<span style="color: red">*</span></label>
            <div class="controls">
              <input type="file" name="fi_award" id="fi_award"/>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label">充值原因<span style="color: red">*</span></label>
            <div class="controls">
              <select id="rechargeReason" name="rechargeReason" style="height: auto;">
                <option value="1">活动奖励打款</option>
                <option value="2">线下现金充值</option>
              </select>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label">充值描述</label>
            <div class="controls">
              <textarea name="rechargeDesc" id="rechargeDesc" style="width: 300px;height: 100px;"></textarea>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label"></label>
            <input type="submit" class="btn btn-primary" value="充值"/>
          </div>
        </form>
      </div>
    </div>
    <div title="重新放款提现">
      <form class="container form-horizontal" method="post" action="${ctx}/withdraw/reWithDraw" id="reWithDraw">

        <div class="control-group">
          <label class="control-label"></label>
          <div class="controls">

          </div>
        </div>
        <div class="control-group">
        <label class="control-label"></label>
        <div class="controls">

        </div>
      </div>
        <div class="control-group">
          <label class="control-label">输入提现单号：<span style="color: red;">*</span></label>
          <div class="controls">
            <input type="text" required="true" missingMessage="提现单号不能为空" class="easyui-validatebox"
                   name="withDrawId" id="withDrawId" value="">
          </div>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="withDraw" class="btn btn-primary" value="提现"/>
        </div>
      </form>
    </div>
    
    

    <div title=" 手动生成提现单">
      <form class="container form-horizontal" method="post" action="${ctx}/withdraw/mannalCreatWithDraw" id="createWithdraw">

        <div class="control-group">
          使用条件：用户在平台有通过连连支付绑过卡，默认会提现到此卡
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <div class="controls">

          </div>
        </div>
        <div class="control-group">
          <label class="control-label">输入用户id：<span style="color: red;">*</span></label>
          <div class="controls">
            <input type="text" required="true" id="w_userId" missingMessage="输入用户id" class="easyui-validatebox"
                   name="userId"  value="">
          </div>
        </div>

        <div class="control-group">
          <label class="control-label">输入提现金额：<span style="color: red;">*</span></label>
          <div class="controls">
            <input type="text" required="true" id="w_amount" missingMessage="输入提现金额" class="easyui-validatebox"
                   name="amount" value="">
          </div>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="create_Withdraw" class="btn btn-primary" value="生成提现单"/>
        </div>
      </form>
    </div>
    
    <!-- 修复账户历史记录【开始】 -->
    <div title="修复账户历史记录">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/doCashFlowCalculate" id="doCashFlowCalculateForm">
        <div class="control-group">
          <label class="control-label">输入用户账户ID（ACC_ID）：<span style="color: red;">*</span></label>
          <div class="controls">
            <input type="text" required="true" missingMessage="用户账户ID不能为空" class="easyui-validatebox"
                   name="accId" id="accId">
          </div>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="doCashFlowCalculateButton" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!-- 修复账户历史记录【结束】 -->

    <!-- 重新生成放款提现单【开始】 -->
    <div title="重新生成放款提现单">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/reMakeWithdrawForApply" id="reMakeWithdrawForApplyForm">
        <div class="control-group">
          <label class="control-label">输入借款申请ID（多个 ID 用,号分隔）：<span style="color: red;">*</span></label>
          <div class="controls">
            <input type="text" required="true" missingMessage="借款申请ID不能为空" class="easyui-validatebox"
                   name="applyIds" id="applyIds">
          </div>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="doReMakeWithdrawForApply" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!-- 重新生成放款提现单【结束】 -->

    <!-- 补发未发放的还款奖励【开始】 -->
    <div title="补发未发放的还款奖励">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/reSendAward" id="reSendAwardForm">
        <div class="control-group">
          <label class="control-label">输入时间段,格式(yyyy-mm-dd_yyyy-mm-dd)<span style="color: red;">*</span></label>
          <div class="controls">
            <input type="text" required="true" missingMessage="时间段不能为空" class="easyui-validatebox"
                   name="dateLimit" id="dateLimit">
          </div>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="doReSendAward" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!-- 补发未发放的还款奖励【结束】 -->
    
  <!-- 修复债权还款占比【开始】 -->
    <div title="修复债权还款占比">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/repairRightsDetailData" id="repairRightsDetailData">
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="repairRightsDetailDataBtn" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!-- 修复债权还款占比【结束】 -->
    
    <!-- 根据满标时间，检查并重新生成丢失的合同【开始】 -->
    <div title="根据满标时间，检查并重新生成丢失的合同">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/checkAndCreateAgreementByFullTime" id="checkAndCreateAgreementByFullTime_Form">
        <div class="control-group">
          <label class="control-label">满标时间：<span style="color: red;">*</span></label>
          <div class="controls">
            <input type="text" class="easyui-datebox" editable="false" style="width: 150px" required="true" missingMessage="满标时间不能为空" name="fullTime" id="fullTime">
          </div>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="checkAndCreateAgreementByFullTime_Button" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!-- 根据满标时间，检查并重新生成丢失的合同【结束】 -->
      <!--  补发少发的还款奖励【开始】 -->
    <div title="补发少发的还款奖励">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/reissuedAwardsData" id="reissuedAwardsData">
      <div class="control-group "  id="yydt" style=";" >
          <label class="control-label">执行的手机号（以‘，’分隔）：</label>
          		<input type="text" class="easyui-validatebox" editable="true" style="width: 150px ; height: auto;" id="mobileStr" name="mobileStr">
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="reissuedAwardsDataBtn" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!--  补发少发的还款奖励【结束】 -->
      <!--  导入白名单Excel工具【开始】 -->
    <div title="导入参与佣金白名单Excel工具">
        <form method="post" action="${ctx}/sysutils/importWhiteExcel" id="importWhiteExcel" enctype="multipart/form-data" class="container form-horizontal"   target="_test">
        <div class="control-group">
            <label class="control-label"  >选择导入文件：</label>
            	<div class="controls" ><input type="file" name="importFile"/>
            </div>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="submit" id="importWhiteExcelBtn" class="btn btn-primary" value="执行"/>
        </div>
    </form>
    </div>
    <!--  导入白名单Excel工具【结束】 -->
    
    
         <!--  导入邀请白名单Excel工具【开始】 -->
    <div title="导入邀请（销售）白名单Excel工具">
        <form method="post" action="${ctx}/sysutils/importInviteWhiteExcel" id="importInviteWhiteExcel" enctype="multipart/form-data" class="container form-horizontal"   target="_test">
        <div class="control-group">
            <label class="control-label"  >选择导入文件：</label>
            	<div class="controls" ><input type="file" name="importFile"/>
            </div>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="submit" id="importInviteWhiteExcelBtn" class="btn btn-primary" value="执行"/>
        </div>
    </form>
    </div>
    <!--  导入邀请白名单Excel工具【结束】 -->
    
    
    <div title="资金操作">
        <form method="post" action="${ctx}/sysutils/capitalOperate" onsubmit="return false" id="capitalOperateForm" class="container form-horizontal">
	        <div class="control-group">
	            	<label class="control-label"  >账户id：</label>
	            	<div class="controls" ><input type="text" name="accountId"/></div>
	        </div>
	        <div class="control-group">
	          	<label class="control-label"  >金额：</label>
	            <div class="controls" ><input type="text" name="money"/></div>
	        </div>
	        <div class="control-group">
	          	<label class="control-label"  >操作类型：</label>
            	<div class="controls" >
            		<select name="operate" style="height: auto;">
            			<option value="1">充值</option>
            			<option value="2">支出</option>
            			<option value="4">冻结</option>
            			<option value="5">解冻</option>
            		</select>
            	</div>
	        </div>
	        <div class="control-group">
	          	<label class="control-label"  >原因：</label>
	          	<textarea style="height:150px;width: 220px;" name="desc"></textarea>
	        </div>
	        <div class="control-group">
	          	<label class="control-label"></label>
          		<input type="submit" id="capitalOperateBtn" class="btn btn-primary" value="执行"/>
	        </div>
    	</form>
    </div>
    
      <!--  三级分销手动生成订单对应的佣金【开始】 -->
    <div title="三级分销手动生成订单对应的佣金">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/createCommiProfitTools" id="createCommiProfitTools">
      <div class="control-group "  id="yydt" style=";" >
          <label class="control-label">执行的订单ID（以‘，’分隔）：</label>
          		<input type="text" class="easyui-validatebox" editable="true" style="width: 150px ; height: auto;" id="lendOrderIds" name="lendOrderIds">
        </div>
      <div class="control-group "  id="yydt1" style=";" >
          <label class="control-label">执行的日期：</label>
          		<input type="text" class="easyui-datebox" editable="false" style="width: 150px" required="true" missingMessage="开始时间不能为空" name="ccpStartTime" id="ccpStartTime">
          		<input type="text" class="easyui-datebox" editable="false" style="width: 150px" required="true" missingMessage="结束时间不能为空" name="ccpEndTime" id="ccpEndTime">
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="createCommiProfitToolsBtn" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!--  三级分销手动生成订单对应的佣金【结束】 -->

    <!--  处理支付成功但处理失败的支付订单【开始】 -->
    <div title="处理支付成功但处理失败的支付订单">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/handleUndonePayOrder" id="handleUndonePayOrderForm">
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="handleUndonePayOrderBtn" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!--  处理支付成功但处理失败的支付订单【结束】 -->

    <!--  修复导入错的数据【开始】 -->
    <%--<div title="修复导入错的数据">--%>
      <%--<form class="container form-horizontal" method="post" action="${ctx}/sysutils/repairAcc" id="repairAccForm">--%>
        <%--<div class="control-group">--%>
          <%--<label class="control-label"></label>--%>
          <%--<input type="button" id="repairAccBtn" class="btn btn-primary" value="执行"/>--%>
        <%--</div>--%>
      <%--</form>--%>
    <%--</div>--%>
    <!--  修复导入错的数据【结束】 -->
    
      <!--  参与佣金白名单和邀请白名单通过手机号导入【开始】 -->
      <!--  格式：130xxx001,130xxx002 ...
       	      步骤1：查询出当前用户（手机号对应的），发展下线用户（手机号用户邀请的）
       	      步骤2：确认是否全部导入 - 确定（导入）, 取消（不导入） -->
    <div title="参与佣金白名单和邀请白名单通过手机号导入">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/importWhiteTabsByMobiles" id="importWhiteTabsByMobilesForm">
      <div class="control-group "  id="yydt" style=";" >
          <label class="control-label">手机号（以‘，’分隔）：</label>
          		<input type="text" class="easyui-validatebox" editable="true" style="width: 150px ; height: auto;" id="importWhiteTabsMobiles" name="importWhiteTabsMobiles">
        </div>
      <div class="control-group " style=";" >
          <label class="control-label">相关的人员：</label>
          <textarea   style="width: 300px;height: 100px;"  id="xgryShow" ></textarea>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="findRelationUsersBtn" class="btn btn-primary" value="查询"/>
          <input type="button" id="exeImportWhiteTabsMobilesBtn" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!--  参与佣金白名单和邀请白名单通过手机号导入【结束】 -->
    
    <!--  批量生成省心计划合同【开始】 -->
    <div title="批量生成省心计划合同">
      <form class="container form-horizontal" method="post" action="${ctx}/sysutils/exeSXJHCreateAgreement" id="exeSXJHCreateAgreementForm">
      <div class="control-group "  id="yydt1" style=";" >
          <label class="control-label">执行的日期：</label>
          		<input type="text" class="easyui-datebox" editable="false" style="width: 150px" required="false" missingMessage="开始时间不能为空" name="ccpStartTime" id="ccpStartTime">
          		<input type="text" class="easyui-datebox" editable="false" style="width: 150px" required="false" missingMessage="结束时间不能为空" name="ccpEndTime" id="ccpEndTime">
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="button" id="exeSXJHCreateAgreementBtn" class="btn btn-primary" value="执行"/>
        </div>
      </form>
    </div>
    <!--  批量生成省心计划合同【结束】 -->
    
    
    <!--  导入多级邀请关系【开始】 -->
    <div title="导入多级邀请关系">
        <form method="post" action="${ctx}/sysutils/importMultilevelInvitationExcel" id="importMultilevelInvitationExcel" enctype="multipart/form-data" class="container form-horizontal" target="_blank">
        <div class="control-group">
            <label class="control-label"  >选择导入文件：</label>
            	<div class="controls" ><input type="file" name="importFile"/>
            </div>
        </div>
        <div class="control-group">
          <label class="control-label"></label>
          <input type="submit" id="importMultilevelInvitationExcelBtn" class="btn btn-primary" value="执行"/>
        </div>
    </form>
    </div>
    <!--  导入多级邀请关系【结束】 -->
    
  </div>

  <script>

    //修复导入错的数据
    $("#repairAccBtn").click(function(){
      $.ajax({
        url:"${ctx}/sysutils/repairAcc",
        type:"post",
        success:function(data){
          if(data=='success')
            $.messager.alert('提示', '执行成功！！', 'info');
          else
            $.messager.alert('提示', data, 'info');
        }
      });
    });


    //处理支付成功但处理失败的支付订单
    $("#handleUndonePayOrderBtn").click(function(){
      $.ajax({
        url:"${ctx}/sysutils/handleUndonePayOrder",
        type:"post",
        success:function(data){
          if(data=='success')
            $.messager.alert('提示', '执行成功！！', 'info');
          else
            $.messager.alert('提示', data, 'info');
        }
      });
    });
  
  // 根据满标时间，检查并重新生成丢失的合同
  $("#checkAndCreateAgreementByFullTime_Button").click(function(){
      var form = $("#checkAndCreateAgreementByFullTime_Form");
      var validate = form.form('validate');
      if(validate){
         var win = $.messager.progress({
         	title:'操作提示',
            msg:'正在执行，请等待...'
         });
        $.ajax({
          url:"${ctx}/sysutils/checkAndCreateAgreementByFullTime",
          type:"post",
          data:form.serialize(),
          success:function(data){
            $.messager.progress('close');
			if(data.result == 'success'){
				$.messager.alert("操作提示", "执行成功！<br/>影响借款申请数：" + data.data.loanCount + "<br/>影响借款申请ID：" + data.data.loanIdStr, "info");
			}else if(data.result == 'error'){
				if(data.errCode == 'check'){
					$.messager.alert("验证提示", data.errMsg, "warning");
				}else{
					$.messager.alert("系统提示", data.errMsg, "error");
				}
			}else{
				$.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
			}
          }
        },'json');
      }
    });

    //补发未发放的还款奖励
    $("#doReSendAward").click(function(){
      var form = $("#reSendAwardForm");
      var validate = form.form('validate');
      if (validate) {
        $.ajax({
          url:"${ctx}/sysutils/reSendAward",
          type:"post",
          data:form.serialize(),
          success:function(data){
            if(data=='success')
              $.messager.alert('提示', '执行成功！！', 'info');
            else
              $.messager.alert('提示',data, 'info');
          }
        });
      }
    });
  
  // 修复历史数据
  $("#doCashFlowCalculateButton").click(function(){
      var form = $("#doCashFlowCalculateForm");
      var validate = form.form('validate');
      if(validate){
        $.ajax({
          url:"${ctx}/sysutils/doCashFlowCalculate",
          type:"post",
          data:form.serialize(),
          success:function(data){
            if(data=='success')
              $.messager.alert('提示', '执行成功！！', 'info');
            else
              $.messager.alert('提示',data, 'info');
          }
        });
      }
    });

    $("#create_Withdraw").click(function(){
      var form = $("#createWithdraw");
      var validate = form.form('validate');
      if(validate){
        $.ajax({
          url:"${ctx}/withdraw/mannalCreatWithDraw",
          type:"post",
          data:form.serialize(),
          success:function(data){
            if(data=='success'){
              $("#w_userId").val("");
              $("#w_amount").val("");
              $.messager.alert('提示', '提交成功！！', 'info');
            }
            else
              $.messager.alert('提示',data, 'info');
          }
        });
      }
    });

    $("#withDraw").click(function(){
      var form = $("#reWithDraw");
      var validate = form.form('validate');
      if(validate){
        $.ajax({
          url:"${ctx}/withdraw/reWithDraw",
          type:"post",
          data:form.serialize(),
          success:function(data){
            if(data=='success')
              $.messager.alert('提示', '提交成功！！', 'info');
            else
              $.messager.alert('提示',data, 'info');
          }
        });
      }
    });

  $("#doReMakeWithdrawForApply").click(function(){
    var form = $("#reMakeWithdrawForApplyForm");
    var validate = form.form('validate');
    if(validate){
      $.ajax({
        url:"${ctx}/sysutils/reMakeWithdrawForApply",
        type:"post",
        data:form.serialize(),
        success:function(data){
          if(data=='success')
            $.messager.alert('提示', '成功！！', 'info');
          else
            $.messager.alert('提示',data, 'info');
        }
      });
    }
  });


    $("#rechargeReason").unbind();
    $("#rechargeReason").change(function() {
      var rechargeReasonVal = $("#rechargeReason").val();
      if (rechargeReasonVal == "1") {
        $("#rechargeForm").attr("enctype", "multipart/form-data");
        $("#rechargeForm").attr("action", "${ctx}/sysutils/doSysRechargeForAward");
        $("#rechargeDesc").html("{awardName:\"\"}");

        $("#d_ui").css("display", "none");
        $("#d_ra").css("display", "none");
        $("#d_rb").css("display", "none");
        $("#d_rc").css("display", "none");
        $("#userId").attr("disabled", "disabled");
        $("#amount").attr("disabled", "disabled");
        $("#externalNo").attr("disabled", "disabled");
        $("#channelCode").attr("disabled", "disabled");
        $("#d_fi_award").css("display", "block");
        return ;
      }
      if (rechargeReasonVal == "2") {
        $("#rechargeForm").attr("enctype", "application/x-www-form-urlencoded");
        $("#rechargeForm").attr("action", "${ctx}/sysutils/doSysRecharge");
        $("#rechargeDesc").html("{offlineOrderNo:\"\"}");
        $("#d_ui").css("display", "block");
        $("#d_ra").css("display", "block");
        $("#d_rb").css("display", "block");
        $("#d_rc").css("display", "block");
        $("#userId").removeAttr("disabled");
        $("#externalNo").removeAttr("disabled");
        $("#channelCode").removeAttr("disabled");
        $("#amount").removeAttr("disabled");
        $("#d_fi_award").css("display", "none");

      }
    });
    $("#rechargeReason").change();
    
 // 修复债权还款占比
    $("#repairRightsDetailDataBtn").click(function(){
        var form = $("#repairRightsDetailData");
        $.ajax({
          url:"${ctx}/sysutils/repairRightsDetailData",
          type:"post",
          data:form.serialize(),
          success:function(data){
            if(data=='success')
              $.messager.alert('提示', '执行成功！！', 'info');
            else
              $.messager.alert('提示',data, 'info');
          }
        });
      });
 
    // 修复债权还款占比
    $("#reissuedAwardsDataBtn").click(function(){
        var form = $("#reissuedAwardsData");
        var validate = form.form('validate');
        if(validate){
          $.ajax({
            url:"${ctx}/sysutils/reissuedAwardsData",
            type:"post",
            data:form.serialize(),
            success:function(data){
              if(data=='success')
                $.messager.alert('提示', '执行成功！！', 'info');
              else
                $.messager.alert('提示',data, 'info');
            }
          });
        }
      });
    
    
 	//资金操作
    $("#capitalOperateBtn").click(function(){
        var form = $("#capitalOperateForm");
        var validate = form.form('validate');
        if(validate){
        	var accountId=$("#capitalOperateForm").find("input[name='accountId']").val();
        	var money=$("#capitalOperateForm").find("input[name='money']").val();
        	var desc=$("#capitalOperateForm").find("textarea[name='desc']").val();
        	if(accountId==null||accountId==undefined||accountId.trim()==""){
        		$.messager.alert('提示', '账户id不能为空！！', 'info');
        		return false;
        	}
        	if(isNaN(accountId)){
        		$.messager.alert('提示', '请填写正确的账户id！！', 'info');
        		return false;
        	}
        	if(money==null||money==undefined||money.trim()==""){
        		$.messager.alert('提示', '金额不能为空！！', 'info');
        		return false;
        	}
        	if(isNaN(money)){
        		$.messager.alert('提示', '请填写正确的金额！！', 'info');
        		return false;
        	}
          	$.ajax({
	            url:"${ctx}/sysutils/capitalOperate",
	            type:"post",
	            data:form.serialize(),
	            success:function(data){
	              if(data=='success')
	                $.messager.alert('提示', '执行成功！！', 'info');
	              else
	                $.messager.alert('提示',data, 'info');
	            }
          	});
        }
      });
 	
 	
    // 三级分销手动生成订单对应的佣金
    $("#createCommiProfitToolsBtn").click(function(){
        var form = $("#createCommiProfitTools");
        var validate = form.form('validate');
        if(validate){
          $.ajax({
            url:"${ctx}/sysutils/createCommiProfitTools",
            type:"post",
            data:form.serialize(),
            success:function(data){
              if(data=='success')
                $.messager.alert('提示', '执行成功！！', 'info');
              else
                $.messager.alert('提示',data, 'info');
            }
          });
        }
      });
   /**
     *  参与白名单和邀请白名单通过手机号录入工具 -- start
     */
    //查询相关人员
    $("#findRelationUsersBtn").click(function(){
        var form = $("#importWhiteTabsByMobilesForm");
        var validate = form.form('validate');
        if(validate){
          $.ajax({
            url:"${ctx}/sysutils/checkImportWhiteTabsByMobiles",
            type:"post",
            data:form.serialize(),
            success:function(data){
              if(data.status=='success'){
            	  var userInfo = data.userInfo ;
              	  var text = "" ;
            	  for(var i = 0 ; i < userInfo.length ; i++){
            		  if(text == ""){
            			  text += userInfo[i].loginName + "," + userInfo[i].mobileNo ;
            		  }else{
            			  text += " ; " +userInfo[i].loginName + "," + userInfo[i].mobileNo ;
            		  }
            	  }
             	$("#xgryShow").text(text);
              }else{
                $.messager.alert('提示',data, 'info');
              }
            }
          });
        }
      });
    // 确认之后执行导入
    $("#exeImportWhiteTabsMobilesBtn").click(function(){
    	if(confirm("确定将查询出的用户导入至白名单？")){
	        var form = $("#importWhiteTabsByMobilesForm");
	        var validate = form.form('validate');
	        if(validate){
	          $.ajax({
	            url:"${ctx}/sysutils/importWhiteTabsByMobiles",
	            type:"post",
	            data:form.serialize(),
	            success:function(data){
	              if(data.status=='success')
	                $.messager.alert('提示', '执行成功！！', 'info');
	              else
	                $.messager.alert('提示',data, 'info');
	            }
	          });
	        }
    	}
      });
    /**
     *  参与白名单和邀请白名单通过手机号录入工具 -- end
     */
  /**
    *  批量生成省心计划合同
    */
    $("#exeSXJHCreateAgreementBtn").click(function(){
    	if(confirm("确定生成所有合同？")){
	        var form = $("#exeSXJHCreateAgreementForm");
	        var startTime = $("#exeSXJHCreateAgreementForm input[name=ccpStartTime]").val();
	        var endTime = $("#exeSXJHCreateAgreementForm input[name=ccpEndTime]").val();
	        var param = form.serialize() ;
	        if(startTime == "" ){
	        	param = param.replace("ccpStartTime=","") ;
	        }
	        if(endTime == "" ){
	        	param = param.replace("ccpEndTime=","") ;
	        }
	          $.ajax({
	            url:"${ctx}/sysutils/exeSXJHCreateAgreement",
	            type:"post",
	            data:param,
	            success:function(data){
	              if(data=='success')
	                $.messager.alert('提示', '执行成功！！', 'info');
	              else
	                $.messager.alert('提示',data, 'info');
	            }
	          });
    	}
      });
    
    
  </script>
</body>
</html>
