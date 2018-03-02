<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.xt.cfp.core.constants.LoanPublishGuaranteeChannelType" %>
<%@ page import="com.xt.cfp.core.constants.GuaranteeTypeEnum" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<html>
<body>
<div id="loan_publish_add" class="container-fluid" style="padding: 0px 0px 0px 10px;width:100%;">
<form class="form-horizontal" id="loan_publish_add_form" method="post">
	<input type="hidden" name="mainLoanApplicationId" value="${loanApplicationId }">

                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>担保类型：</label>
                    <div class="controls">
                        <select type="text" class="easyui-combobox" required="true" style="width: 150px;" 
                        		name="guaranteeChannel" id="guaranteeChannel" data-options="editable:false">
	                        <option value="">请选择</option>
		                    <option value="1" <c:if test="${loanPublish.guaranteeChannel eq '1'}">selected="selected"</c:if>>无</option>
		                    <option value="2" <c:if test="${loanPublish.guaranteeChannel eq '2'}">selected="selected"</c:if>>担保公司</option>
		                    <option value="3" <c:if test="${loanPublish.guaranteeChannel eq '3'}">selected="selected"</c:if>>风险备用金</option>
                        </select>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>担保公司：</label>
                    <div class="controls">
                        <span id="div_channel">
	                        <input name="companyId" id="companyId" style="width: 150px;" >
                        </span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>保障方式：</label>
                    <div class="controls">
                        <select type="text" class="easyui-combobox" required="true" style="width: 150px;" 
                        		name="guaranteeType" id="guaranteeType" data-options="editable:false">
	                        <option value="" <c:if test="${loanPublish.guaranteeType eq ''}">selected="selected"</c:if>>请选择</option>
		                    <option value="<%=GuaranteeTypeEnum.PRINCIPAL_PROTECTION.getValue()%>"
                                    <c:if test="${loanPublish.guaranteeType eq '1'}">selected="selected"</c:if>>本金保障</option>
		                    <option value="<%=GuaranteeTypeEnum.PRINCIPAL_INTEREST_PROTECTION.getValue()%>"
                                    <c:if test="${loanPublish.guaranteeType eq '0'}">selected="selected"</c:if>>本息保障</option>
                        </select>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>逾期垫付时机：</label>
                    <div class="controls">
                        <input name="overduePayPoint" id="overduePayPoint" style="width: 150px;" >
                    </div>
                </div>
                <c:if test="${loanType ne '5'}">

                    <div class="control-group">
                        <label class="control-label">借款用途：</label>
                        <div class="controls" >
                                ${loanPublish.loanUseageName }
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">用途描述：</label>
                        <div class="controls" >
                                ${loanPublish.usageDesc }
                        </div>
                    </div>
                </c:if>
                <div class="control-group">
                    <label class="control-label">描述：</label>
                    <div class="controls" >
                        	${loanPublish.applicationDesc }
                    </div>
                </div>

                <div class="control-group">
				    <label class="control-label"><span style="color: red">*</span>借款标题：</label>
				    <div class="controls">
				        <input type="text"
				               class="easyui-validatebox" required="true" missingMessage="借款标题不能为空" validType="length[1,25]" invalidMessage="不能超过25个字符！" style="width: 300px"
				               name="loanTitle" id="loanTitle" 
				               <c:if test="${empty loanPublish.loanTitle }">value='${loanPublish.loanUseageName }-${loanPublish.productName }-${loanPublish.dueTime }${loanPublish.dueTimeType =='1'?'天':'月' }'</c:if>
				               <c:if test="${not empty loanPublish.loanTitle }">value='${loanPublish.loanTitle }'</c:if>
				                />
				    </div>
				</div>
                <div class="control-group">
				    <label class="control-label"><span style="color: red">*</span>借款描述：</label>
				    <div class="controls">
				        <textarea rows="3" cols="60" style="width: 500px;height: 150px;" id="desc" name="desc" class="easyui-validatebox" required="true"
				                  missingMessage="请填写借款描述">${loanPublish.desc }</textarea>
				    </div>
				</div>
<c:if test="${loanType ne '5'}">
                <div class="control-group">
                    <label class="control-label"><span style="color: red">*</span>借款用途描述：</label>
                    <div class="controls">
                                    <textarea rows="3" cols="60" style="width: 500px;height: 150px;" id="loanUseageDesc" name="loanUseageDesc" class="easyui-validatebox" required="true"
                                              missingMessage="请填写借款用途描述">${loanPublish.loanUseageDesc }</textarea>
                    </div>
                </div>
</c:if>
    <c:if test="${loanType !='2' && loanType !='3' && loanType !='4' }">
	    <div class="control-group">
	        <label class="control-label"><span style="color: red;">*</span>认证报告：</label>
	
	        <div class="controls">
	            <c:forEach items="${authReports}" var="authReport">
	                <input type="checkbox" name="authInfos" value="${authReport.constantValue}"
	                       <c:forEach items="${exist}" var="e">
	                        <c:if test="${authReport.constantValue eq e}" > checked </c:if>
	                       </c:forEach>
	                        >${authReport.constantName}
	            </c:forEach>
	        </div>
	    </div>
    </c:if>
    
    <c:if test="${loanType =='1' || loanType =='7'}">
        <div class="control-group">
            <label class="control-label"><span style="color: red;">*</span>房屋地址：</label>
            <div class="controls">
                <input type="hidden" name="provinceId" value="${hourse.provinceId}">
                <input type="hidden" name="cityId" value="${hourse.cityId}">
                <input type="hidden" name="districtId" value="${hourse.districtId}">
                    ${hourse.provinceName}
                <span style="font-size: 12px;">省</span>
                    ${hourse.cityName}
                <span style="font-size: 12px;">市</span>
                    ${hourse.districtName}
                <span style="font-size: 12px;">区/县</span><br/>
                <input type="text" style="width: 440px;margin-top: 3px;" class="easyui-validatebox" validType="length[2,50]"
                       name="houseAddr_detail" id="houseAddr_detail" value="${hourse.detail}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><span style="color: red;">*</span>房屋面积：</label>
            <div class="controls">
                <input type="text" style="width: 200px;margin-top: 3px;"
                       class="easyui-numberbox" validType="length[0,4]" precision="0"
                       name="hourseSize" id="hourseSize" value="${hourse.hourseSize}"><span style="font-size: 12px;">平米</span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><span style="color: red;">*</span>总评估值：</label>
            <div class="controls">
                <input type="text" style="width: 200px;"
                       class="easyui-numberbox" validType="length[0,4]" precision="0"
                       name="assessValue" id="assessValue" value="${hourse.assessValue}"><span style="font-size: 12px;">万元</span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><span style="color: red;">*</span>市值：</label>
            <div class="controls">
                <input type="text" style="width: 200px;"
                       class="easyui-numberbox" validType="length[0,4]" precision="0"
                       name="marketValue" id="marketValue" value="${hourse.marketValue}"><span style="font-size: 12px;">万元</span>
            </div>
        </div>
        
    </c:if>


</form>
</div>

<script type="text/javascript">

//加载担保公司
$("#loan_publish_add_form #guaranteeChannel").combobox({
	 onChange: function (n,o) {
		 //如果担保类型不为担保公司，担保公司下拉框为空
		 if(n==2){
			 $("#loan_publish_add_form #companyId").combobox({ disabled: false});	
		 }else{
			 $("#loan_publish_add_form #companyId").combobox("clear");
			 $("#loan_publish_add_form #companyId").combobox("disable");
			 $("#companyId").combobox('setValue', '');
		 }
	} 
});
//加载担保公司
$("#loan_publish_add_form #companyId").combobox({
    url: '${ctx}/jsp/loanPublish/loan/getCompanyList',
    textField: 'companyName',
    valueField: 'companyId',
    editable:false
});
//加载垫付时机
$("#loan_publish_add_form #overduePayPoint").combobox({
    url: '${ctx}/jsp/loanPublish/loan/getOverduePayPointList',
    textField: 'constantName',
    valueField: 'constantValue',
    editable:false
});

$("#loan_publish_add_form").form({
    url: '${ctx}/jsp/loanPublish/loan/saveLoanPublish',
    onSubmit: function () {
    	var val_gua = $("#guaranteeChannel").combobox("getValue");
    	if (val_gua == null || val_gua == '') {
    		$.messager.alert("系统提示", "担保类型不能为空!", "info");
    		return false;
    	} else if(val_gua == 2){
    		var val_companyId = $("#companyId").combobox("getValue");
        	if (val_companyId == null || val_companyId == '') {
        		$.messager.alert("系统提示", "担保公司不能为空!", "info");
        		return false;
        	}
    	}
    	var val_guaranteeType = $("#guaranteeType").combobox("getValue");
    	if (val_guaranteeType == null || val_guaranteeType == '') {
    		$.messager.alert("系统提示", "保障方式不能为空!", "info");
    		return false;
    	}
    	var val_overduePayPoint = $("#overduePayPoint").combobox("getValue");
    	if (val_overduePayPoint == null || val_overduePayPoint == '') {
    		$.messager.alert("系统提示", "逾期垫付时机不能为空!", "info");
    		return false;
    	}
    	return $("#loan_publish_add_form").form("validate");
    },
    success: function (data) {
        if (data == "success") {
            $.messager.alert("系统提示", "发标信息编辑成功!", "info",function(){
                opener.$("#loan_app_list").datagrid("reload");
                parent.$("#loan_publish_edit_tab").dialog("close");
                parent.$("#iframepage").attr("src",$("#iframepage").attr("src"));

                //父页刷新，展示‘发标’按钮
                window.parent.reload();
                //关闭当前页
                window.close();
            });

        }
    }
})

$(function() {
	//页面修改时加载数据
	if('${loanPublish.guaranteeChannel}' != <%=LoanPublishGuaranteeChannelType.GUARANTEECORPORATION.getValue()%>){
		$("#loan_publish_add_form #companyId").combobox("clear");
		 $("#loan_publish_add_form #companyId").combobox("disable");
		 $("#companyId").combobox('setValue', '');
	}else{	
		$("#companyId").combobox('setValue', '${loanPublish.companyId}');
	}
	$("#overduePayPoint").combobox('setValue', '${loanPublish.overduePayPoint}');
});
</script>
</body>
</html>
