<%@ page import="com.xt.cfp.core.constants.VoucherConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<%
    //财富券类型
    VoucherConstants.VoucherTypeEnum[] voucherType = VoucherConstants.VoucherTypeEnum.values();
    request.setAttribute("voucherType",voucherType);
    //基数类型
    VoucherConstants.CardinalTypeEnum[] cardinalType = VoucherConstants.CardinalTypeEnum.values();
    request.setAttribute("cardinalType",cardinalType);
    //使用场景
    VoucherConstants.UsageScenario[] usageScenario = VoucherConstants.UsageScenario.values();
    request.setAttribute("usageScenario",usageScenario);
%>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addVoucherProduct" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form class="form-horizontal" id="addVoucherProduct_form" method="post" action="${ctx}/voucher/save">
        <input type="hidden" name="voucherProductId" id="voucherProductId" value="${voucherProduct.voucherProductId}"/>

        <div class="control-group">

            <label class="control-label">财富券名称：</label>

           <div class="controls">
                <input type="text" style="width: 200px"
                       class="easyui-validatebox" required="true" missingMessage="财富券名称不能为空"
                       name="voucherName" id="voucherName" value="${voucherProduct.voucherName}"><span style="color: red">*</span>
            </div>

        </div>
        <div class="control-group">
            <label class="control-label">财富券类型：</label>

            <div class="controls">
                <select id="voucherType" class="easyui-combobox"
                        <%--editable="false"--%>
                        name="voucherType" style="width:160px;">
                    <option value="">请选择</option>
                    <c:forEach items="${voucherType}" var="type">
                        <option value="${type.value}"
                                <c:if test="${voucherProduct.voucherType eq type.value}">selected="selected"</c:if>>${type.desc}
                        </option>
                    </c:forEach>
                </select>
            <span id="amount">
                <input type="text" id="amount1" name="amount" style="width: 100px" disabled="disabled" class="easyui-validatebox" required="true" missingMessage="金额不能为空"/> 元<span
                    style="color: red">*</span>
            </span>

            <span style="display: none;" id="rate">
                <input type="text" id="rate1" name="rate" style="width: 100px;"  class="easyui-validatebox" required="true" missingMessage="不能为空"/> %<span style="color: red">*</span>
            </span>
                <select id="cardinalType" class="easyui-combobox" disabled="disabled" name="cardinalType"
                        <%--editable="false"--%>
                        style="width:100px;">
                    <option value="">请选择</option>
                    <c:forEach items="${cardinalType}" var="type">
                        <option value="${type.value}"
                                <c:if test="${voucherProduct.cardinalType eq type.value}">selected="selected"</c:if>>${type.desc}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">有效期：</label>

            <div class="controls">
                <input id="v_startDate" name="startDate" editable="false" style="width:100px;" class="easyui-datebox"/>至<input
                    id="w_endDate" style="width:100px;" editable="false" name="endDate" class="easyui-datebox"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">有效时长：</label>

            <div class="controls">
                <input name="effectiveCount" id="effectiveCount2" checked onclick="effective_Count()" type="radio" value="">
                    <input id="effectiveCount" class="easyui-validatebox" required="true" missingMessage="有效时长不能为空" style="width:100px;" onkeyup="effectiveCountValue()"/>
                    <input id="effectiveCount3"  style="width:100px;display: none;" disabled />天

                <input name="effectiveCount" id="effectiveCount1" onclick="effective_Count()" type="radio" value="-1"> 长期有效
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">使用场景：</label>

            <div class="controls">
                <select id="usageScenario" class="easyui-combobox"
                        <%--editable="false"  --%>
                        name="usageScenario"
                        style="width:200px;">
                    <option value="-1">请选择</option>
                    <c:forEach items="${usageScenario}" var="type">
                        <option value="${type.value}"
                                <c:if test="${voucherProduct.usageScenario eq type.value}">selected="selected"</c:if>>${type.desc}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">使用条件：</label>
            <div class="controls">
                <input name="conditionAmount" id="conditionAmount2" checked onclick="condition_Amount()" type="radio" value="">
                <input id="conditionAmount" style="width:100px;" class="easyui-validatebox" required="true" missingMessage="使用条件不能为空" onkeyup="conditionAmountValue()"/><input id="conditionAmount3" style="width:100px;display: none;" disabled />元可用
                <input name="conditionAmount" id="conditionAmount1" onclick="condition_Amount()" type="radio" value="-1"> 无限制

            </div>
        </div>


        <div class="control-group">
            <label class="control-label">叠加使用：</label>

            <div class="controls">
                <input type="radio" checked name="isOverlay" value="0"> 不可以
                <input type="radio" name="isOverlay" value="1"> 可以
            </div>
        </div>
        <div class="control-group" id="status">
            <label class="control-label">体验型财富券：</label>

            <div class="controls">
                <input name="isExperience" type="checkbox" value="1">体验型财富券，投标回款将由平台收回，用户仅获得利息收益
            </div>
        </div>
        <div class="control-group" id="voucherRemark">
            <label class="control-label">备注：</label>

            <div class="controls">
                <textarea name="voucherRemark" autocomplete="off" class="textbox-text" placeholder=""
                          style="margin-left: 0px; margin-right: 0px; height: 75px; width: 350px;"></textarea>
            </div>
        </div>

    </form>
</div>
<script language="javascript">

    function effectiveCountValue(){
        var val = $("#effectiveCount").val();
        if(isNaN(val)||val<=0){
            $("#effectiveCount").val("");
        }
        $("#effectiveCount2").val(val);
    }
    function conditionAmountValue(){
        var val = $("#conditionAmount").val();
        if(isNaN(val)||val<=0){
            $("#conditionAmount").val("");
        }
        $("#conditionAmount2").val(val);
    }

    function effective_Count(){
        var checked1 = $("#effectiveCount1").attr("checked");
        var checked2 = $("#effectiveCount2").attr("checked");
        if(checked1=='checked'){
            $("#effectiveCount").attr("disabled","disabled");
            $('#effectiveCount').validatebox('remove'); //删除
            $("#effectiveCount3").show();
            $("#effectiveCount").hide();
        }
        if(checked2=='checked'){
            $("#effectiveCount").removeAttr("disabled");
            $('#effectiveCount').validatebox('reduce'); //恢复
            $("#effectiveCount3").hide();
            $("#effectiveCount").show();
        }
    }

    function condition_Amount(){
        var checked1 = $("#conditionAmount1").attr("checked");
        var checked2 = $("#conditionAmount2").attr("checked");
        if(checked1=='checked'){
            $("#conditionAmount").attr("disabled","disabled");
            $('#conditionAmount').validatebox('remove'); //删除
            $("#conditionAmount3").show();
            $("#conditionAmount").hide();
        }
        if(checked2=='checked'){
            $("#conditionAmount").removeAttr("disabled");
            $('#conditionAmount').validatebox('reduce'); //恢复
            $("#conditionAmount3").hide();
            $("#conditionAmount").show();
        }
    }

    function selectType(v) {
        if (v == '0') {
            $("#amount").show();
            $('#amount1').validatebox('reduce');
            $("#rate").hide();
            $('#rate1').validatebox('remove'); //删除
            $("#amount").children().removeAttr("disabled");

            $("#cardinalType").combobox({
                disabled: true
                ,required: false});
            $('#cardinalType').validatebox('selectedRemove'); //删除
        }
        if (v == '1') {
            $("#amount").hide();
            $('#amount1').validatebox('remove');
            $("#rate").show();
            $('#rate1').validatebox('reduce'); //恢复
            $("#cardinalType").combobox({
                disabled: false,
                required: true,
                validType: 'selectV'});

        }
        if (v == '') {
            $("#amount").show();
            $("#rate").hide();
            $("#amount").children().attr("disabled", "disabled");
            $("#cardinalType").combobox({
                disabled: true
                ,required: false,});

        }
    }
    function doChange() {
        $("#voucherType").combobox({
            onChange: function (rec) {
                selectType(rec);
            },
            required: true,
            validType: 'selectV'
        });

        $("#usageScenario").combobox({
            required: true,
            validType: 'selectV'
        });


    }

    $(function () {
        doChange();
    });
</script>
</body>
</html>