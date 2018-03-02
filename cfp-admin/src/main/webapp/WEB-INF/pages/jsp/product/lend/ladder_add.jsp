<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.FeesItem" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="product_toAddLadder_form" method="post">
    <div class="control-group">
        <label class="control-label">阶梯金额<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   class="easyui-numberbox" style="width: 80px" required="true" missingMessage="最小金额必须填写" data-options="min:${startsAt},precision:2"
                   name="minAmount" id="minAmount"/>~<input type="text"
                   class="easyui-numberbox" style="width: 80px" required="true" missingMessage="最大金额必须填写" data-options="min:${startsAt}+${upAt},precision:2"
                   name="maxAmount" id="maxAmount"/>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">费用<span style="color: red">*</span></label>

        <div class="controls">
            <input id="feeItem" name="feeItem"
                   required="required" missingMessage ="必须选择一项费用"
                    />
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">优惠<span style="color: red">*</span></label>

        <div class="controls">
            <div class="btn-group" data-toggle="buttons-radio">
                <button class="btn"
                        onclick="$('#product_toAddLadder_form #discountRate').hide();
                        $('#product_toAddLadder_form #rateStr').hide();
                        $('#product_toAddLadder_form #discountRate').val('100')">
                    免</button>
                <button class="btn active" onclick="$('#product_toAddLadder_form #discountRate').show();
                $('#product_toAddLadder_form #rateStr').show();
                $('#product_toAddLadder_form #discountRate').val('0')">优惠:</button>
            </div>
            <input type="text"
                   class="easyui-numberbox" style="width: 60px" value="0" data-options="max:100,min:0,precision:2"
                   name="discountRate" id="discountRate"/>
            <span id="rateStr">%</span>
        </div>
    </div>

</form>
<script language="JavaScript">
    $("#product_toAddLadder_form #feeItem").combobox({
        data:eval('(${feesItems})') ,
        valueField: 'feesItemId',
        textField: 'itemName'
    })

    $("#product_toAddLadder_form").form({
        onSubmit:function(data) {
            if (!$("#product_toAddLadder_form").form("validate")) {
                return false;
            }
            if ($("#product_toAddLadder_form #minAmount").val() - $("#product_toAddLadder_form #maxAmount").val() >=0) {
                $.messager.alert("系统提示", "阶梯金额最小值不能大于等于最大值!", "info");
                return false;
            }
            var discounType;
            if ($('#product_toAddLadder_form #discountRate').val() == '100') {
                discounType = "免";
            } else {
                discounType = $('#product_toAddLadder_form #discountRate').val() + "%";
            }
            parent.$("#product_addLendProduct_form #discountFeest_list").datagrid("appendRow",{
                balances:$("#product_toAddLadder_form #minAmount").val() + "~" + $("#product_toAddLadder_form #maxAmount").val(),
                discounType:discounType,
                discountRate:($('#product_toAddLadder_form #discountRate').val())/100,
                itemName:$("#product_toAddLadder_form #feeItem").combobox("getText"),
                feesItemId:$("#product_toAddLadder_form #feeItem").combobox("getValue"),
                minAmount:$("#product_toAddLadder_form #minAmount").val(),
                maxAmount:$("#product_toAddLadder_form #maxAmount").val()
            });

            $("#toAddLadder").dialog('close');
            return false;
        }
    })


</script>
</body>
</html>
