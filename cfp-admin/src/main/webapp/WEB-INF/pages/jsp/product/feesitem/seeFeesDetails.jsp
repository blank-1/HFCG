<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>

<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="addFees_form" method="post">
    <div class="control-group">
        <label class="control-label">费用名称:</label>

        <div class="controls-text">
            ${feesItem.itemName}&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">费用描述:</label>

        <div class="controls-text">
            ${feesItem.itemDesc}&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">费用分类:</label>

        <div class="controls-text">
            <span id="itemKindSpan"></span>&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">费用类别:</label>

        <div class="controls-text" id="itemTypeId">
            ${feesItem.itemType}&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">收取比例:</label>

        <div class="controls-text">
            ${feesItem.feesRate}%&nbsp;
        </div>
    </div>
    <div class="control-group" id="radicesType">
        <label class="control-label">基数:</label>

        <div class="controls-text" id="radicesTypeId">
            &nbsp;
        </div>
    </div>

    <div class="control-group" id="customRadiceName">
        <label class="control-label">自定义基数名称:</label>

        <div class="controls-text">
            ${feesItem.radiceName}&nbsp;
        </div>
    </div>

    <div class="control-group" id="customRadiceLogic">
        <label class="control-label">自定义基数逻辑:</label>

        <div class="controls-text">
            ${feesItem.radiceLogic}&nbsp;
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">状态:</label>

        <div class="controls-text">
            <span id="itemStateSpan">${feesItem.itemState}</span>
        </div>
    </div>

</form>

<script language="javascript">
    var url = "${ctx}/jsp/product/feesitem/initConstantDefine";
    var constantDefineJson = "";
    $.ajax({
        async: false,
        url: url,
        success: function (data) {
            constantDefineJson = data;
        }
    })
    var radicesTypeValue = '${feesItem.radicesType}';
    if (radicesTypeValue == 0) {
        $("#radicesType").hide();
    } else {
        $("#customRadiceName").hide();
        $("#customRadiceLogic").hide();
    }
    //下拉框处理
    $(document).ready(function () {
        var val =
        ${feesItem.itemKind} ==
        1 ? '借款' : '出借';
        $("#itemKindSpan").text(val);

        var val =
        ${feesItem.itemState} ==
        '0' ? '有效' : '无效';
        $("#itemStateSpan").text(val);
        for (var i = 0; i < constantDefineJson.length; i++) {
            //处理基数
            if (constantDefineJson[i].constantValue == radicesTypeValue && "radicesType" == constantDefineJson[i].constantTypeCode) {
                if (constantDefineJson[i].constantName == '自定义') {
                    $("#radicesType").hide();
                } else {
                    $("#radicesTypeId").text(constantDefineJson[i].constantName);
                    $("#customRadiceName").hide();
                    $("#customRadiceLogic").hide();
                }
            }
            //处理费用类别
            if (constantDefineJson[i].constantValue == ${feesItem.itemType} && "itemChildType" == constantDefineJson[i].constantTypeCode) {
                $("#itemTypeId").text(constantDefineJson[i].constantName);
            }
        }
    });
</script>
</body>
</html>