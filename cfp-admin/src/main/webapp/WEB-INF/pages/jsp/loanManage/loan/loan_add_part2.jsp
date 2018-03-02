<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="loan_add_part2" class="container-fluid" style="padding: 10px 0px 0px 10px;width:50%;">
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="saveButtonAdd2();">
<form class="form-horizontal" id="loan_add_part2_form" method="post">
	<input type="hidden" id="loanApplicationId" name="loanApplicationId" value="${loanApplicationId}">
	<input type="hidden" id="loanApplicationName" name="loanApplicationName">
	<table width="100%">
      <tr>
          <td>
          		<!-- 借款-开始 -->
          		<h4>借款信息</h4>
          		<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款用途：</label>
                    <div class="controls">
                        <input style="width: 200px" id="loanUseage" name="loanUseage">
                    </div>
                </div>

               <div class="control-group">
                      <label class="control-label"><span style="color: red;">*</span>借款用途描述：</label>
                      <div class="controls">
                            <textarea style="width: 450px; height: 100px;"
                                      class="easyui-validatebox" required="true" validType="length[2,200]"
                                      name="loanUseageDesc" id="loanUseageDesc"></textarea>
                      </div>
                 </div>
                
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款产品：</label>
                    <div class="controls">
                        <input name="loanProductId" id="loanProductId" style="width: 200px" >
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">年利率：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="annualRate"></span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">期限：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="dueTime"></span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">还款方式：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="repaymentType"></span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款金额：</label>
                    <div class="controls" >
                        <input type="text" style="width: 200px"
                        	class="easyui-numberbox" required="true" validType="length[2,10]"
                        	name="loanBalance" id="loanBalance"><a style="font-size: 12px;cursor: pointer;" id="showRepaymentPlan" onclick="showRepaymentPlan();">查看还款计划</a>
                        	<br/><br/>
                        	<table id="feesItemList" class="table table-bordered table-condensed" style="font-size: 12px;width: 300px;">
                        	
                        	</table>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>描述：</label>
                    <div class="controls">
                        <textarea style="width: 450px; height: 100px;" 
                        		class="easyui-validatebox" required="true" validType="length[2,200]"
                                name="applicationDesc" id="applicationDesc"></textarea>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">线下编号：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]"
                        	name="offlineApplyCode" id="offlineApplyCode">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">借款人姓名：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="">${trueName}</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">借款人身份证号：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="">${idCard}</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">性别：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="">${sex}</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">出生日期：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="">${birthday}</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款人手机：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-numberbox" required="true" validType="length[0,11]"
                        	name="mobilePhone" id="mobilePhone"  readonly="readonly" value="${userInfoExt.mobileNo}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">借款人邮箱：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-validatebox" validType="length[0,30]"
                        	name="email" id="email">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">最高学历：</label>
                    <div class="controls">
                        <input style="width: 200px" id="education" name="education">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">婚姻状况：</label>
                    <div class="controls">
                        <input style="width: 200px" id="isMarried" name="isMarried">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">有无子女：</label>
                    <div class="controls">
                        <input type="radio" id="childStatus" name="childStatus" value="1" ><font style="font-size: 12px;">无</font>
                        <input type="radio" id="childStatus" name="childStatus" value="2" ><font style="font-size: 12px;">有</font>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">资产情况：</label>
                    <div class="controls">
                        <input type="checkbox" id="assetsInfo" name="assetsInfo" value="1" ><font style="font-size: 12px;">有房</font>
                        <input type="checkbox" id="assetsInfo" name="assetsInfo" value="2" ><font style="font-size: 12px;">有车</font>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">月均收入：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-numberbox" validType="length[0,10]"
                        	name="monthlyIncome" id="monthlyIncome">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">信用卡最高额度：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-numberbox" validType="length[0,10]"
                        	name="maxCreditValue" id="maxCreditValue">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">籍贯：</label>
                    <div class="controls" >
                        <input name="bornAddr_provence" id="bornAddr_provence" style="width: 130px" >
                        <span style="font-size: 12px;">省</span>
                        <input name="bornAddr_city" id="bornAddr_city" style="width: 130px" >
                        <span style="font-size: 12px;">市</span>
                        <input name="bornAddr_district" id="bornAddr_district" style="width: 130px" >
                        <span style="font-size: 12px;">区/县</span><br/>
                        <input type="text" style="width: 440px;margin-top: 3px;" class="easyui-validatebox" validType="length[2,50]" 
                        name="bornAddr_detail" id="_detail">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">户口所在地：</label>
                    <div class="controls" >
                        <input name="registAddr_provence" id="registAddr_provence" style="width: 130px" >
                        <span style="font-size: 12px;">省</span>
                        <input name="registAddr_city" id="registAddr_city" style="width: 130px" >
                        <span style="font-size: 12px;">市</span>
                        <input name="registAddr_district" id="registAddr_district" style="width: 130px" >
                        <span style="font-size: 12px;">区/县</span><br/>
                        <input type="text" style="width: 440px;margin-top: 3px;" class="easyui-validatebox" validType="length[2,50]" 
                        name="registAddr_detail" id="registAddr_detail">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">现住址：</label>
                    <div class="controls" >
                        <input name="residenceAddr_provence" id="residenceAddr_provence" style="width: 130px" >
                        <span style="font-size: 12px;">省</span>
                        <input name="residenceAddr_city" id="residenceAddr_city" style="width: 130px" >
                        <span style="font-size: 12px;">市</span>
                        <input name="residenceAddr_district" id="residenceAddr_district" style="width: 130px" >
                        <span style="font-size: 12px;">区/县</span><br/>
                        <input type="text" style="width: 440px;margin-top: 3px;" class="easyui-validatebox" validType="length[2,50]" 
                        name="residenceAddr_detail" id="residenceAddr_detail">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">现住址邮政编码：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,6]"
                        	name="zipcode" id="zipcode">
                    </div>
                </div>
                <!-- 借款-结束 -->
                
                <hr><!-- ----------------------------------------------------------------------------- -->
                
                <!-- 风控-开始 -->
                <h4>风控步骤</h4>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>风控步骤：</label>
                    <div class="controls">
                        <textarea style="width: 450px; height: 100px;" 
                        		class="easyui-validatebox" required="true" validType="length[2,1500]"
                                name="riskControlInformation" id="riskControlInformation"></textarea>
                    </div>
                </div>
                <!-- 风控-结束 -->
                
                <hr><!-- ----------------------------------------------------------------------------- -->
                
                <!-- 工作-开始 -->
                <h4>工作信息</h4>
                
				<div class="control-group">
                    <label class="control-label">单位性质：</label>
                    <div class="controls">
                        <input style="width: 200px" id="companyNature" name="companyNature">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">单位名称：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-validatebox" validType="length[2,50]"
                        	name="companyName" id="companyName">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">单位地址：</label>
                    <div class="controls" >
                        <input name="workingAddr_provence" id="workingAddr_provence" style="width: 130px" >
                        <span style="font-size: 12px;">省</span>
                        <input name="workingAddr_city" id="workingAddr_city" style="width: 130px" >
                        <span style="font-size: 12px;">市</span>
                        <input name="workingAddr_district" id="workingAddr_district" style="width: 130px" >
                        <span style="font-size: 12px;">区/县</span><br/>
                        <input type="text" style="width: 440px;margin-top: 5px;" class="easyui-validatebox" validType="length[2,50]" 
                        name="workingAddr_detail" id="workingAddr_detail">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">单位电话：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,20]"
                        	name="companyPhone" id="companyPhone">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">职务：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-validatebox" validType="length[0,30]"
                        	name="post" id="post">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">入职时间：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-datebox"
                        	name="joinDate" id="joinDate">
                    </div>
                </div>
                <!-- 工作-结束 -->
                
				<hr><!-- ----------------------------------------------------------------------------- -->
                
                <!-- 联系人-开始 -->
                <h4>联系人信息</h4>
                
                <!-- 联系人数组隐藏域 -->
                <input type="hidden" id="controlsArray" name="controlsArray">
                
                <!-- 联系人单元块-开始 -->
				<div class="control-group" id="contacts">
                    <label class="control-label">关系：</label>
                    <div class="controls" >
                        <input style="width: 100px" id="relationType" name="relationType">
                        <input style="width: 100px" id="relation" name="relation">
                        <span style="font-size: 12px;">姓名：</span>
                        <input type="text" style="width: 100px" class="easyui-validatebox" validType="length[0,10]" 
                        name="concactName" id="concactName">
                        <span style="font-size: 12px;">手机号：</span>
                        <input type="text" style="width: 100px" class="easyui-validatebox" validType="length[0,11]" 
                        name="concatPhone" id="concatPhone">
                        <i class="icon-plus" style="margin-left: 5px;cursor: pointer;" title="添加联系人" onclick="onContacts_plus()"></i>
                    </div>
                </div>
                <!-- 联系人单元块-结束 -->
                
                <!-- 联系人-结束 -->
                
				<hr id="contacts_end"><!-- ----------------------------------------------------------------------------- -->
                
                <!-- 银行卡-开始 -->
                <h4>银行卡信息</h4>
                
                <!-- 借款标情况-开始 -->
                <c:if test="${empty customerCard }"><!-- 后台用户，无卡 -->
                	<div id="div_loanMark">
						<div class="control-group">
		                    <label class="control-label"><span style="color: red;">*</span>银行：</label>
		                    <div class="controls" >
		                        <input  style="width: 200px" id="bankCode" name="bankCode">
		                    </div>
		                </div>
		                
		                <div class="control-group">
		                    <label class="control-label"><span style="color: red;">*</span>开户行：</label>
		                    <div class="controls">
		                       	<input type="text" style="width: 355px;"
		                        	class="easyui-validatebox" required="true" validType="length[0,50]"
		                        	name="registeredBank" id="registeredBank">
		                    </div>
		                </div>
		                
						<div class="control-group">
		                    <label class="control-label"><span style="color: red;">*</span>卡号：</label>
		                    <div class="controls">
		                        <input type="text" style="width: 200px;"
		                        	class="easyui-validatebox" required="true" validType="length[0,30]"
		                        	name="cardCode" id="cardCode">
		                    </div>
		                </div>
		                
						<div class="control-group">
		                    <label class="control-label"><span style="color: red;">*</span>开户名：</label>
		                    <div class="controls">
		                        <input type="text" style="width: 200px"
		                        	class="easyui-validatebox" required="true" validType="length[0,30]"
		                        	name="cardCustomerName" id="cardCustomerName">
		                    </div>
		                </div>
	                </div>
                </c:if>
                
                <c:if test="${not empty customerCard }"><!-- 普通用户，已经有银行卡 -->
	                <div id="div_loanMark">
						<div class="control-group">
		                    <label class="control-label"><span style="color: red;">*</span>银行：</label>
		                    <div class="controls" >
		                        <input type="hidden" style="width: 200px" id="bankCode" name="bankCode" value="${customerCard.bankCode }">


                                <c:if test="${customerCard.bankCode  eq '403'}">  <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国邮政储蓄银行股份有限公司" disabled="disabled">
                                </c:if>

                                <c:if test="${customerCard.bankCode  eq '102'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国工商银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '103'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国农业银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '104'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '105'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国建设银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '301'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="交通银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '302'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中信银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '303'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国光大银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '304'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="华夏银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '305'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国民生银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '306'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="广东发展银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '307'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="平安银行股份有限公司" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '308'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="招商银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '309'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="兴业银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '310'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="上海浦东发展银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '319'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="徽商银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '313'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="其他城市商业银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '314'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="其他农村商业银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '315'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="恒丰银行" disabled="disabled"></c:if>
		                    </div>
		                </div>
		                
		                <div class="control-group">
		                    <label class="control-label"><span style="color: red;">*</span>开户行：</label>
		                    <div class="controls">
		                       	<input type="text" style="width: 355px;"
		                        	class="easyui-validatebox" required="true" validType="length[0,50]"
		                        	name="registeredBank" id="registeredBank" value="${customerCard.registeredBank }" readonly="readonly" >
		                    </div>
		                </div>
		                
						<div class="control-group">
		                    <label class="control-label"><span style="color: red;">*</span>卡号：</label>
		                    <div class="controls">
		                    	<input type="hidden" style="width: 200px;" name="cardCode" id="cardCode" value="${customerCard.cardCode }">
		                        <input type="text" style="width: 200px;" name="cardCodeShow" id="cardCodeShow" value="${customerCard.cardCode }" disabled="disabled">
		                    </div>
		                </div>
		                
						<div class="control-group">
		                    <label class="control-label"><span style="color: red;">*</span>开户名：</label>
		                    <div class="controls">
		                        <input type="hidden" style="width: 200px" name="cardCustomerName" id="cardCustomerName" value="${customerCard.cardCustomerName }" >
		                        <input type="text" style="width: 200px" name="cardCustomerNameShow" id="cardCustomerNameShow" value="${customerCard.cardCustomerName }" disabled="disabled">
		                    </div>
		                </div>
	                </div>
                </c:if>
                <!-- 借款标情况-结束 -->
                
                <!-- 债权标情况-开始 -->
                <div id="div_rightsMark">
					<div class="control-group">
	                    <label class="control-label"><span style="color: red;">*</span>打款卡：</label>
	                    <div class="controls" >
	                        <input name="inCardId" id="inCardId" style="width: 200px" >
	                    </div>
	                </div>
	                
					<div class="control-group">
	                    <label class="control-label"><span style="color: red;">*</span>划扣卡：</label>
	                    <div class="controls" >
	                        <input name="outCardId" id="outCardId" style="width: 200px" >
	                    </div>
	                </div>
                </div>
                <!-- 债权标情况-结束 -->
                <!-- 银行卡-结束 -->
          </td>
      </tr>
	</table>
</form>
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="saveButtonAdd2();">
</div>

<script type="text/javascript">

// 执行:保存。
function saveButtonAdd2(){
	
	// 验证
	if(!$("#loan_add_part2_form").form('validate')){
		return false;
	}
	
	// 单独获取联系人信息。
	var contactsArray = new Array();
	var c = {};
	c.relationType = $("#relationType").combobox("getValue");	
	c.relation = $("#relation").combobox("getValue");	
	c.concactName = $("#concactName").val();	
	c.concatPhone = $("#concatPhone").val();
	contactsArray[0] = c;
	if(i > 0){
		for(var j=1;j <= i;j++){
			var cs = {};
			var isE = false;
			if($("#relationType_" + j).length > 0){
				cs.relationType = $("#relationType_" + j).combobox("getValue");
				isE = true;
			}
			if($("#relation_" + j).length > 0){
				cs.relation = $("#relation_" + j).combobox("getValue");
				isE = true;
			}
			if($("#concactName_" + j).length > 0){
				cs.concactName = $("#concactName_" + j).val();
				isE = true;
			}
			if($("#concatPhone_" + j).length > 0){
				cs.concatPhone = $("#concatPhone_" + j).val();
				isE = true;
			}
			if(isE==true){
				contactsArray[j] = cs;	
			}
		}	
	}
	// 将联系人数组信息存入隐藏区域。
	$("#controlsArray").attr("value", JSON.stringify(contactsArray));
	
	// 获取下列数值的文字信息。
	var loanUseage_Str = $("#loanUseage").combobox("getText");//借款用途
	var loanProductId_Str = $("#loanProductId").combobox("getText");//借款产品
	var dueTime_Str = $("#dueTime").html();//期限
	var loanApplicationName_Str = loanUseage_Str + '-' + loanProductId_Str + '-' + dueTime_Str;//借款合同名称
	$("#loanApplicationName").attr("value",loanApplicationName_Str);
	
	$.post('${ctx}/jsp/loanManage/loan/save_loan_part2?r=' + Math.random(),
   		$("#loan_add_part2_form").serialize(),
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

/* 借款信息-开始  */

	// 加载借款用途下拉框。
	$("#loan_add_part2_form #loanUseage").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=loanUseage&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE'
	});
	
	// 借款产品下拉框。
	$("#loan_add_part2_form #loanProductId").combobox({
	    url: '${ctx}/jsp/product/loan/loadLoanProduct?selectedDisplay=selected',
	    textField: 'PRODUCTNAME',
	    valueField: 'LOANPRODUCTID',
	    onSelect: function (record) {
	    	// 展示详情开始
    		$.post('${ctx}/jsp/product/loan/getLoanProductDetail',
    	   		{
    				loanproductid:record.LOANPRODUCTID
    	   		},
    	   		function(data){
    	   	    	if(data.result == 'success'){
    	   	    		
    	   	 			// 单项填充
    	   	    		$("#annualRate").html(data.data.annualRate + '%');//年利率
    	   	    		$("#dueTime").html(data.data.dueTime);//期限时长
    	   	    		$("#repaymentType").html(data.data.repaymentType);//还款方式
    	   	    		
    	   	    		// 列表填充
    	   	    		$("#feesItemList").empty();// 先清空。
    	   	    		$.each(data.data.feesItemList,function(n,v){
    	   	    			var f = "<tr><td>"+ v.itemType +"</td><td>"+ v.chargeCycle +"</td><td>"+ v.itemName +"</td></tr>";
    	   	    			$("#feesItemList").append(f);
    	   	    		});
    	   	    		
    	   	    	}else if(data.result == 'error'){
    	   	    		$.messager.alert("系统提示", data.errMsg, "info");
    	   	    	}else{
    	   	    		$.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
    	   	    	}
    	   	 },'json');
	    	// 展示详情结束
	    }
	});

	// 加载最高学历下拉框。
	$("#loan_add_part2_form #education").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=education&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE'
	});
	
	// 加载婚姻状况下拉框。
	$("#loan_add_part2_form #isMarried").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=isMarried&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE'
	});
	
	//加载单位性质下拉框。
	$("#loan_add_part2_form #companyNature").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=companyNature&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE'
	});
	
	// 下面加载银行卡列表，如果是普通用户，则不加载。
	var bankCode = '${customerCard.bankCode}';
	if(null == bankCode || '' == bankCode){
		//加载开户行下拉框。
		$("#loan_add_part2_form #bankCode").combobox({
		    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=bank&parentConstant=0&selectedDisplay=selected',
		    textField: 'CONSTANTNAME',
		    valueField: 'CONSTANTID'
		});
	}
	
	//加载关系类型下拉框。
	$("#loan_add_part2_form #relationType").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relationType&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE',
	    idField: 'CONSTANTID',
	    onSelect: function (record) {
	        $("#loan_add_part2_form #relation").combobox("reload",
	        	'${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=' + record.CONSTANTID + '&selectedDisplay=selected');
	    }
	});
	
	//加载关系下拉框。
	$("#loan_add_part2_form #relation").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE'
	});
	
	//加载省份下拉框【籍贯】。
	$("#loan_add_part2_form #bornAddr_provence").combobox({
	    url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
	    textField: 'PROVINCENAME',
	    valueField: 'PROVINCEID',
	    onSelect: function (record) {
	        $("#loan_add_part2_form #bornAddr_city").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
	    }
	});
	//加载城市下拉框【籍贯】。
	$("#loan_add_part2_form #bornAddr_city").combobox({
	    url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID',
	    onSelect: function (record) {
	    	var provinceId = $("#bornAddr_provence").combobox("getValue");
	        $("#loan_add_part2_form #bornAddr_district").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
	    }
	});
	//加载区县下拉框【籍贯】。
	$("#loan_add_part2_form #bornAddr_district").combobox({
	    url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID'
	});
	
	//加载省份下拉框【户口所在地】。
	$("#loan_add_part2_form #registAddr_provence").combobox({
	    url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
	    textField: 'PROVINCENAME',
	    valueField: 'PROVINCEID',
	    onSelect: function (record) {
	        $("#loan_add_part2_form #registAddr_city").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
	    }
	});
	//加载城市下拉框【户口所在地】。
	$("#loan_add_part2_form #registAddr_city").combobox({
	    url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID',
	    onSelect: function (record) {
	    	var provinceId = $("#registAddr_provence").combobox("getValue");
	        $("#loan_add_part2_form #registAddr_district").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
	    }
	});
	//加载区县下拉框【户口所在地】。
	$("#loan_add_part2_form #registAddr_district").combobox({
	    url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID'
	});
	
	//加载省份下拉框【现住址】。
	$("#loan_add_part2_form #residenceAddr_provence").combobox({
	    url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
	    textField: 'PROVINCENAME',
	    valueField: 'PROVINCEID',
	    onSelect: function (record) {
	        $("#loan_add_part2_form #residenceAddr_city").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
	    }
	});
	//加载城市下拉框【现住址】。
	$("#loan_add_part2_form #residenceAddr_city").combobox({
	    url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID',
	    onSelect: function (record) {
	    	var provinceId = $("#residenceAddr_provence").combobox("getValue");
	        $("#loan_add_part2_form #residenceAddr_district").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
	    }
	});
	//加载区县下拉框【现住址】。
	$("#loan_add_part2_form #residenceAddr_district").combobox({
	    url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID'
	});

/* 借款信息-结束  */

/* 工作信息-开始 */

	//加载省份下拉框。
	$("#loan_add_part2_form #workingAddr_provence").combobox({
	    url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
	    textField: 'PROVINCENAME',
	    valueField: 'PROVINCEID',
	    onSelect: function (record) {
	        $("#loan_add_part2_form #workingAddr_city").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
	    }
	});
	//加载城市下拉框。
	$("#loan_add_part2_form #workingAddr_city").combobox({
	    url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID',
	    onSelect: function (record) {
	    	var provinceId = $("#workingAddr_provence").combobox("getValue");
	        $("#loan_add_part2_form #workingAddr_district").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
	    }
	});
	//加载区县下拉框。
	$("#loan_add_part2_form #workingAddr_district").combobox({
	    url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID'
	});

/* 工作信息-结束 */

/* 联系人-开始 */
 	// 执行：添加联系人。
 	var i = 0;
	function onContacts_plus(){
		i += 1;
		var c = "<div class='control-group' id='contacts_" + i + "'>" +
	        "<label class='control-label'>关系：</label>" +
	        "<div class='controls' >" +
	            "<input style='width: 100px' name='relationType_" + i + "' id='relationType_" + i + "'>&nbsp;" +
                "<input style='width: 100px' name='relation_" + i + "' id='relation_" + i + "'>" +
	            "<span style='font-size: 12px;'>&nbsp;姓名：&nbsp;&nbsp;</span>" +
	            "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[2,50]' " +
	            "name='concactName_" + i + "' id='concactName_" + i + "'>" +
	            "<span style='font-size: 12px;'>&nbsp;&nbsp;手机号：&nbsp;</span>" +
	            "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[2,50]' " +
	            "name='concatPhone_" + i + "' id='concatPhone_" + i + "'>" +
	            "<i class='icon-trash' style='margin-left: 5px;cursor: pointer;' title='删除联系人' onclick='onContacts_trash(" + i + ");'></i>" +
	        "</div>" +
	    "</div>";
		$("#contacts_end").before(c);
		
	    //加载关系类型下拉框。
	    $("#loan_add_part2_form #relationType_" + i).combobox({
	        url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relationType&parentConstant=0&selectedDisplay=selected',
	        textField: 'CONSTANTNAME',
	        valueField: 'CONSTANTVALUE',
	        idField: 'CONSTANTID',
	        onSelect: function (record) {
	            $("#loan_add_part2_form #relation_" + i).combobox("reload",
	            	'${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=' + record.CONSTANTID + '&selectedDisplay=selected');
	        }
	    });

	    //加载关系下拉框。
	    $("#loan_add_part2_form #relation_" + i).combobox({
	        url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=0&selectedDisplay=selected',
	        textField: 'CONSTANTNAME',
	        valueField: 'CONSTANTVALUE'
	    });
		
	}
	
	// 执行：删除联系人。
	function onContacts_trash(contacts_num){
		$("#contacts_" + contacts_num).remove();
	}
/* 联系人-结束 */

/* 银行卡信息-开始 */
 	// 执行：银行卡初始化事件。
 	function intoMark(){
		if(1 == ${subjectType}){// 借款标
			$("#div_loanMark").show();
			$("#div_rightsMark").hide();
		}else{// 债权标
			$("#div_loanMark").hide();
			$("#div_rightsMark").show();
		}
	}
	
 	// 执行：改变银行卡表单验证规则。
	function intoValidate(){
		if(1 == ${subjectType}){
			// 去掉
			// inCardId
			// outCardId
		}else{
			// 去掉
			// bankCode
			// registeredBank
			$('#registeredBank').validatebox({
    			required: false
			});
			// cardCode
			$('#cardCode').validatebox({
    			required: false
			});
			// cardCustomerName
			$('#cardCustomerName').validatebox({
    			required: false
			});
		}
	}
 	
 	if(1 != ${subjectType}){
 		//加载打款卡下拉框。
 		$("#loan_add_part2_form #inCardId").combobox({
 		    url: '${ctx}/jsp/custom/customer/loadCustomerCard?selectedDisplay=selected&originalUserId=${originalUserId}',
 		    textField: 'CARDCUSTOMERNAME',
 		    valueField: 'CUSTOMERCARDID'
 		});
 		
 		//加载划扣卡下拉框。
 		$("#loan_add_part2_form #outCardId").combobox({
 		    url: '${ctx}/jsp/custom/customer/loadCustomerCard?selectedDisplay=selected&originalUserId=${originalUserId}',
 		    textField: 'CARDCUSTOMERNAME',
 		    valueField: 'CUSTOMERCARDID'
 		});
 	}
 	
 	// 展示还款计划列表
 	function showRepaymentPlan(){
 	    if ($("#loanProductId").combobox("getValue") && $("#loanBalance").val()) {
 	    	$("#showRepaymentPlan").after("<div id='repaymentPlan' style=' padding:10px; '></div>");
 	        $("#repaymentPlan").dialog({
 	            resizable: false,
 	            title: '还款计划',
 	            href: '${ctx}/jsp/loanManage/loan/toShowRepaymentPlan?loanProductId='+ $("#loanProductId").combobox("getValue") +'&balance=' + $("#loanBalance").val(),
 	            width: 600,
 	            modal: true,
 	            height: 300,
 	            top: 100,
 	            left: 400,
 	            buttons: [
 	                {
	                    text: '取消',
	                    iconCls: 'icon-cancel',
	                    handler: function () {
	                        $("#repaymentPlan").dialog('close');
	                    }
                	}
 	            ],
 	            onClose: function () {
 	                $("#repaymentPlan").dialog('destroy');
 	            }
 	        });
 	    }else{
 	    	$.messager.alert("验证提示", "请选择借款产品并填写借款金额", "info");
 	    }
 	}

/* 银行卡信息-结束 */

$(function(){
	intoMark();
	intoValidate();
});

</script>
</body>
</html>
