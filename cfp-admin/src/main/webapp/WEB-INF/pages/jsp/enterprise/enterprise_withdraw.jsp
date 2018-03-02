<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>

</head>
<body>
<div id="addWithdraw" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <fieldset style="height:65px">
        <legend>企业信息</legend>
        <table align="center" border="0" width="100%" >

            <tr>
                <td align="left" style="font-size: 12px;">企业名称：${enterpriseInfoExt.enterpriseName}</td>
                <td align="left" style="font-size: 12px;" >企业类型：
                	<c:if test="${enterpriseInfoExt.enterpriseType eq '0'}" >保理</c:if>
                    <c:if test="${enterpriseInfoExt.enterpriseType ne '0'}" >其它</c:if>
                </td>
                <td align="left" style="font-size: 12px;">法人姓名：${enterpriseInfoExt.legalPersonName}</td>
                <td align="left" style="font-size: 12px;">开户许可证：${enterpriseInfoExt.accountNumber}</td>
            </tr>
            <tr>
                <td align="left"  style="font-size: 12px;">账户余额：${enterpriseInfoExt.value}</td>
                <td align="left" style="font-size: 12px;">可用金额：${enterpriseInfoExt.avilableValue}</td>
                <td align="left" style="font-size: 12px;">冻结金额：${enterpriseInfoExt.freezeValue}</td>
                <td align="left" style="font-size: 12px;">状态：
                    <c:if test="${enterpriseInfoExt.state eq '0'}" >正常</c:if>
                    <c:if test="${enterpriseInfoExt.state ne '0'}" >禁用</c:if>
                </td>
            </tr>
        </table>
   </fieldset>
    <fieldset style="height:330px">
        <legend>提现</legend>
        <form class="form-horizontal" id="withDraw_form" method="post" action="${ctx}/jsp/enterprise/withDraw">
            <input type="hidden" name="enterpriseId" id="enterpriseId" value="${enterpriseInfoExt.enterpriseId}"/>
            <input type="hidden" name="userId" id="userId" value="${enterpriseInfoExt.userId}"/>

            <div class="control-group">
                <label class="control-label">可提现金额：</label>
                <div class="controls">
                    ${enterpriseInfoExt.avilableValue}元
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">提现金额：</label>
                <div class="controls">
                    <input type="text" style="width: 200px"
                     class="easyui-validatebox" required="true" missingMessage="提现金额不能为空"  validtype="amount"
                           name="withdrawAmount" id="withdrawAmount" value=""><span style="color: red">*</span>
                </div>
            </div>
            <c:if test="${not empty customerCard.cardCodeShort }">

                    <div class="control-group">
                        <label class="control-label">选择提现卡：</label>
                        <div class="controls">
                           <input type="radio" checked name="isCardUsed" value="0" onclick="showCardInput(0)"/>
                            <c:forEach items="${constantDefines}" var="constantDefine" >
                                <c:if test="${constantDefine.constantDefineId eq customerCard.bankCode}">${constantDefine.constantName}</c:if>
                            </c:forEach>
                               	 尾号${customerCard.cardCodeShort}
                        </div>
                        <div>
                            <input type="radio" name="isCardUsed" value="1" onclick="showCardInput(1)"/>其他银行卡
                        </div>
                    </div>
            </c:if>

            <div id="temp1"></div>


        </form>

        <div id="cardInput" style="display: none;">

            <div class="control-group">
                <label class="control-label" >公司名称：</label>
                <div class="controls">
                    <input type="text" style="width: 200px"
                           class="easyui-validatebox" required="true" missingMessage="公司名称"
                           name="companyName" id="companyName" value="${enterpriseInfoExt.enterpriseName}"><span style="color: red;">*</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">开户行：</label>
                <div class="controls">

                    <select name="bankCode" class="easyui-combobox" style="width: 120px;">
                        <c:forEach items="${constantDefines}" var="constantDefine" >
                            <option value="${constantDefine.constantDefineId}">${constantDefine.constantName}</option>
                        </c:forEach>
                    </select>
                    <input type="text" style="width: 80px"
                           class="easyui-validatebox"
                           name="registeredBank" id="registeredBank1" value="">分行
                </div>
                <div class="controls">
                    <input type="text" style="width: 200px"
                           class="easyui-validatebox"
                           name="registeredBank" id="registeredBank2" value="">支行
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">银行卡号：</label>
                <div class="controls">
                    <input type="text" style="width: 200px"
                           class="easyui-validatebox" required="true" missingMessage="银行卡号"
                           name="cardNo" id="cardNo" value=""><span style="color: red">*</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">备注信息：</label>
                <div class="controls">
                    <textarea name="remark" autocomplete="off" class="textbox-text" placeholder="" style="margin-left: 0px; margin-right: 0px; height: 75px; width: 250px;"></textarea>
                </div>
            </div>
        </div>

        <div id="temp" style="display: none;"></div>
    </fieldset>
</div>

<script type="text/javascript">

    $(function(){
        <c:if test="${ empty customerCard.cardCodeShort }">
            $("#temp1").append($("#cardInput"));
            $("#cardInput").show();
        </c:if>
    });

    function showCardInput(v){
        if(v=='0') {
            $("#temp").append($("#cardInput"));
            $("#temp1").html("");
        }else{
            $("#temp1").append($("#cardInput"));
            $("#temp").html("");
            $("#cardInput").show();
        }
    }
</script>
</body>
</html>