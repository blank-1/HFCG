<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.LendProduct" %>
<%@ page import="com.xt.cfp.core.pojo.FeesItem" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="product_addLendProduct_form" method="post">
<input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
<input type="hidden" name="lendProductId" id="lendProductId" value="${lendProduct.lendProductId}"/>
<input type="hidden" name="productType" id="productType" value="${lendProduct.productType}"/>
<input type="hidden" name="loanProducts" id="loanProducts"/>
<input type="hidden" name="discountFees" id="discountFees"/>
<input type="hidden" name="existLoanRatio" id="existLoanRatio"/>
<input type="hidden" name="lendTypeSelect" id="lendTypeSelect" value="">

<div class="control-group">
    <label class="control-label">产品名称<span style="color: red">*</span></label>

    <div class="controls">
        <input type="text"
               class="easyui-validatebox" required="true" missingMessage="产品名称不能为空" style="width: 200px"
               name="productName" id="productName" value="${lendProduct.productName}"/>

        <div class="btn-group" data-toggle="buttons-radio">
            <c:if test="${lendProduct.productType eq '1'}">
            	<button class="btn" id="productType_Rights" onclick="productTypeDemo(<%=LendProduct.PRODUCTTYPE_RIGHTS%>)">债权类</button>
            </c:if>
            <c:if test="${lendProduct.productType eq '2'}">
            	<button class="btn" id="productType_Financing" onclick="productTypeDemo(<%=LendProduct.PRODUCTTYPE_FINANCING%>)">省心计划</button>
        	</c:if>
        </div>
    </div>
</div>
<div class="control-group">
    <label class="control-label">收益利率<span style="color: red">*</span></label>

    <div class="controls">
            <span style="width: 130px;display: inline-block">
                <input type="text"
                       class="easyui-numberbox" required="true" missingMessage="收益利率不能为空"
                       data-options="min:1,precision:2,max:100" style="width: 100px"
                       name="profitRate" id="profitRate" value="${lendProduct.profitRate}"/>%
            </span>
            <c:if test="${lendProduct.productType eq '2'}">
            <span style="width: 130px;display: inline-block">
                ~
                <input type="text"
                       class="easyui-numberbox" required="true" missingMessage="收益利率不能为空"
                       data-options="min:1,precision:2,max:100" style="width: 100px"
                       name="profitRateMax" id="profitRateMax" value="${lendProduct.profitRateMax}"/>%
            </span>
			</c:if>
	        <c:if test="${lendProduct.productType eq '1'}">
	        	<span style="width: 60px;display: inline-block"> 封闭期<span style="color: red">*</span></span>
		        <input type="text"
		               class="easyui-numberbox" style="width: 80px" size="4" data-options="min:0,precision:0"
		               required="true" missingMessage="封闭期不能为空"
		               name="closingDate" id="closingDate" value="${lendProduct.closingDate}"/>
		
		        <input id="closingType" name="closingType" style="width: 50px"
		               required="required"
		                />
	        </c:if>
        
    </div>
</div>
<div class="control-group">
    <label class="control-label">起售金额<span style="color: red">*</span></label>

    <div class="controls">
            <span style="width: 200px;display: inline-block">
                <input type="text"
                       class="easyui-numberbox" data-options="min:0,precision:2" style="width: 100px"
                       required="true" missingMessage="起售金额不能为空"
                       name="startsAt" id="startsAt" value="${lendProduct.startsAt}"/>
            </span>
        <span style="width: 60px;display: inline-block">递增金额<span style="color: red">*</span></span>
        <input type="text"
               class="easyui-numberbox" data-options="min:0,precision:2" style="width: 100px"
               required="true" missingMessage="递增不能为空"
               name="upAt" id="upAt" value="${lendProduct.upAt}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label">期限<span style="color: red">*</span></label>

    <div class="controls">
            <span style="width: 200px;display: inline-block">
            <input type="text"
                   class="easyui-numberbox" style="width: 80px" size="4" data-options="min:0,precision:0"
                   name="timeLimit" id="timeLimit" value="${lendProduct.timeLimit}"/>
            <input id="timeLimitType" name="timeLimitType" style="width: 50px"
                   required="true" missingMessage="期限不能为空"
                    />
            </span>
        <span style="width: 60px;display: inline-block"> 返息周期<span style="color: red">*</span></span>
        <input id="toInterestPoint" name="toInterestPoint" style="width: 100px"
               required="true" missingMessage="返息不能为空"
                />

    </div>
</div>

<div class="control-group">
    <label class="control-label">续费<span style="color: red">*</span></label>

    <div class="controls">
        <span style="width: 200px;display: inline-block">
            <input id="renewalCycleType" name="renewalCycleType"
                   required="required" missingMessage="续费项不能为空"
                    />
            </span>
            <span id="spanRenewalType">
            <span style="width: 60px;display: inline-block"> 类型<span style="color: red">*</span></span>

            <input type="text"
                   style="width: 150px"
                   name="renewalType" id="renewalType">
            <input type="text" class="easyui-numberbox" id="renewal" name="renewal" value="0"
                   style="width: 80px;display: none">
            </span>
    </div>
</div>
<div class="control-group">
    <label class="control-label">版本号<span style="color: red">*</span></label>

    <div class="controls">
        <input type="text"
               class="easyui-validatebox" required="true" missingMessage="版本号不能为空" style="width: 200px"
               name="versionCode" id="versionCode" value="${lendProduct.versionCode}"/>
    </div>
</div>
<div class="control-group">
    <label class="control-label">产品描述<span style="color: red">*</span></label>

    <div class="controls">
        <textarea rows="3" cols="60" style="width: 450px;" id="productDesc"
                  name="productDesc" class="easyui-validatebox" required="true"
                  missingMessage="请填写产品描述">${lendProduct.productDesc}</textarea>
    </div>
</div>
<fieldset>
    <legend>费用</legend>
    <c:forEach items="${itemTypes}" var="itemType"><%Boolean flag = true;%>
        <div class="control-group">
            <label class="control-label">${itemType.constantName}<span style="color: red">*</span></label>

            <div class="controls">
                <div style="display: inline-block;">
                    <c:forEach items="${itemType.childItemTypes}" var="childItemType">
                    <span style="padding-bottom: 10px;display: block">
                        <c:forEach items="${FeesItemList}" var="FeesItemList">
                            <c:if test="${childItemType.constantValue == FeesItemList.itemType}">
                                <c:forEach items="${lendProductFeesItem}" var="lendProductFeesItem">
                                    <c:if test="${lendProductFeesItem.feesItemId == FeesItemList.feesItemId}">
                                        <input feesItemId="${lendProductFeesItem.feesItemId}"
                                               radiceName="${FeesItemList.radiceName}"
                                               itemName="${FeesItemList.itemName}"
                                               itemType="${FeesItemList.itemType}"
                                               workflowRatio="${lendProductFeesItem.workflowRatio}"
                                               chargePoint="${lendProductFeesItem.chargePoint}" type="checkbox"
                                               id="intro" name="feeItems" value="${childItemType.constantValue}"
                                               onclick="selItemType(this, '${childItemType.constantValue}','','100')"
                                               checked="checked">
                                        ${childItemType.constantName}&nbsp;&nbsp;<% flag = false;%>
                                        <span id="toAdd_${childItemType.constantValue}"></span>

                                        <br/>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                        </c:forEach>
                        <c:if test="<%=flag%>">
                            <input type="checkbox" id="intro" name="feeItems"
                                   value="${childItemType.constantValue}"
                                   onclick="selItemType(this, '${childItemType.constantValue}','','100')">
                            ${childItemType.constantName}&nbsp;&nbsp;
                            <span id="toAdd_${childItemType.constantValue}"></span>
                            <br/>
                        </c:if>
                        <c:if test="<%=!flag%>">
                            <% flag = true;%>
                        </c:if>
                        </span>
                    </c:forEach>
                </div>
            </div>
        </div>
    </c:forEach>
</fieldset>
<!-- 取消阶梯优惠 
<fieldset>
    <legend>阶梯优惠</legend>
    <div class="control-group">
        <label class="control-label">优惠</label>

        <div class="controls">
            <div id="discountFeest_list_toolbar" style="height:auto">

                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                   onclick="toAddLadder()">新增</a>
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                   onclick="delLadder()">删除</a>
            </div>
            <table id="discountFeest_list" style="padding: 10px">
                <thead>
                <th data-options="field:'id', checkbox:true"></th>
                <th data-options="field:'balances',width:200">金额范围</th>
                <th data-options="field:'itemName',width:160">管理费</th>
                <th data-options="field:'discounType',width:60">优惠</th>
                <th data-options="field:'feesItemId',width:80,hidden:true">feesItemId</th>
                <th data-options="field:'discountRate',width:80,hidden:true">discountRate</th>
                <th data-options="field:'minAmount',width:80,hidden:true">minAmount</th>
                <th data-options="field:'maxAmount',width:80,hidden:true">maxAmount</th>
                </thead>
            </table>
        </div>
    </div>
</fieldset> -->
<fieldset>
    <legend>适用债权</legend>
    <div class="control-group">
        <label class="control-label">债权列表</label>

        <div class="controls">
            <div id="loanProduct_list_toolbar" style="height:auto">

                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                   onclick="toAddLoanProduct()">新增</a>
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                   onclick="delLoanProduct()">删除</a>
            </div>
            <table id="loanProduct_list" style="padding: 10px">
                <thead>
                <th data-options="field:'loanProductId', checkbox:true"></th>
                <th data-options="field:'productName',width:200">债权</th>
                <th data-options="field:'ratioStr',width:60">占比(%)</th>
                <th data-options="field:'ratio',width:1,hidden:true"></th>
                <th data-options="field:'lendLoanBindingId',width:50,hidden:true"></th>
                <th data-options="field:'lendProductId',width:50,hidden:true"></th>
                <th data-options="field:'chargePoint',width:50,hidden:true"></th>
                <th data-options="field:'itemName',width:160">管理费</th>
                <th data-options="field:'feesItemId',width:160,hidden:true"></th>

                </thead>
            </table>
        </div>
    </div>
</fieldset>
</form>
<script language="javascript">
var feesItems = [];
/**
 * 取消阶梯优惠
function toAddLadder() {

    $("#discountFeest_list").after("<div id='toAddLadder' style=' padding:10px; '></div>");
    $("#toAddLadder").dialog({
        resizable: false,
        title: '增加阶梯优惠',
        href: '${ctx}/jsp/product/lend/toAddLadder?feesItems=' + encodeURIComponent(JSON.stringify(feesItems)) + "&startsAt=" + $("#product_addLendProduct_form #startsAt").val() +
                "&upAt=" + $("#product_addLendProduct_form #upAt").val(),
        width: 500,
        modal: true,
        height: 300,
        top: 200,
        left: 500,
        buttons: [
            {
                text: '提交',
                iconCls: 'icon-ok',
                handler: function () {
                    $("#toAddLadder").contents().find("#product_toAddLadder_form").submit();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#toAddLadder").dialog('close');
                }
            }
        ],
        onClose: function () {
            $(this).dialog('destroy');
        }
    });
}

function delLadder() {
    var selRow = $("#product_addLendProduct_form #discountFeest_list").datagrid("getSelected");
    if (selRow) {
        $.messager.confirm("系统提示", "是否确定删除该优惠？", function (data) {
            if (data) {
                $("#product_addLendProduct_form #discountFeest_list").datagrid("deleteRow",
                        $("#product_addLendProduct_form #discountFeest_list").datagrid("getRowIndex", selRow));
            }
        })
    } else {
        $.messager.alert("系统提示", "请选择要删除的优惠信息！", "info");
    }

}
*/
var checkedVal = $("input#intro");
for (var j = 0; j < checkedVal.length; j++) {
    var chargepoint = "";
    var feesitemid = "";
    var radiceName = "";
    var itemName = "";
    var itemType = "";
    var workflowratio = "";
    if (checkedVal[j].checked) {
        //$("input#intro").click();
        for (var i = 0; i < checkedVal[j].attributes.length; i++) {
            if (checkedVal[j].attributes[i].name == "chargepoint") {
                chargepoint = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "feesitemid") {
                feesitemid = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "radicename") {
                radiceName = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "itemname") {
                itemName = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "workflowratio") {
                workflowratio = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "itemtype") {
                itemType = checkedVal[j].attributes[i].nodeValue;

            }
        }

        selItemType(checkedVal[j], checkedVal[j].value, radiceName, workflowratio);
        //页面初始化时把费用类别赋值给一个数组，让其阶梯优惠选择费用类别时使用
        var fees = {};
        fees.feesItemId = feesitemid;
        fees.itemName = itemName;
        fees.itemType = itemType;
        feesItems.push(fees);
        $("#product_addLendProduct_form #chargePoint_" + checkedVal[j].value).combobox("select", chargepoint);
        $("#product_addLendProduct_form #feesItems_" + checkedVal[j].value).combobox("select", feesitemid);
    }
}
function selItemType(theCheckBox, itemType, radiceName, workflowratio) {

    if (theCheckBox.checked) {
        $("#product_addLendProduct_form #toAdd_" + itemType).append("<input editable='false' style='width:95px;' id='chargePoint_" + itemType + "' name='chargePoint_" + itemType + "'/>&nbsp;&nbsp;");
        $("#product_addLendProduct_form #chargePoint_" + itemType).combobox({
            url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=chargePoint,chargePoint_l',
            valueField: 'constantValue',
            textField: 'constantName',
            required: true,
            missingMessage: '请选择收费周期'
        });

        $("#product_addLendProduct_form #toAdd_" + itemType).append("<input style='width:110px;'  id='feesItems_" + itemType + "' name='feesItems_" + itemType + "'/>");
        $("#product_addLendProduct_form #feesItems_" + itemType).combobox({
            url: '${ctx}/jsp/product/feesitem/getFeesItemsByItemType?itemType=' + itemType + "&itemKind=<%=FeesItem.ITEMKIND_LEND%>",
            valueField: 'feesItemId',
            textField: 'itemName',
            required: true,
            missingMessage: '请选择费用项目',
            onSelect: function (rec) {
                for (var i = 0; i < feesItems.length; i++) {//处理一个费用类别，添加时对应多个费用项的bug
                    var val = feesItems[i];
                    if (rec.itemType == val.itemType) {
                        feesItems.splice(i, 1);
                    }

                }
                feesItems.push(rec);
                $.post('${ctx}/jsp/product/loan/findFeesRadicesAndRate?feesItemId=' + rec.feesItemId, function (date) {
                    radiceName = date[0].radiceName;
                    $("#product_addLendProduct_form #radiceName" + itemType).text(radiceName + ";平台收取");
                });
            }
        });

        workflowratio = workflowratio * 100;
        $("#product_addLendProduct_form #toAdd_" + itemType).append("&nbsp;<span id='radiceName" + itemType + "'>" + radiceName + ";平台收取</span><input style='width: 50px' value = '" + workflowratio + "'" + " id='workflowRatio_" + itemType + "' " +
                "name='workflowRatio_" + itemType + "'/><span id='workflowRatioSpan_" + itemType + "'>%</span>");
        $('#workflowRatio_' + itemType).numberbox({
            min: 0,
            max: 100,
            precision: 3
        });
    } else {
        $("#product_addLendProduct_form #chargePoint_" + itemType).combobox("destroy");
        $("#product_addLendProduct_form #feesItems_" + itemType).combobox("destroy");
        $("#product_addLendProduct_form #radiceName" + itemType).remove();
        $("#product_addLendProduct_form #workflowRatio_" + itemType).numberbox("destroy");
        $("#product_addLendProduct_form #workflowRatioSpan_" + itemType).remove();
    }

}

function toAddLoanProduct() {
	setLendTypeSelect();
    if ($("#product_addLendProduct_form #productType").val() == '<%=LendProduct.PRODUCTTYPE_RIGHTS%>') {
        if ($("#product_addLendProduct_form #loanProduct_list").datagrid("getRows").length > 0) {
            $.messager.alert("系统提示", "债权类出借产品，只能绑定一个借款产品，请删除已绑定借款产品！", "info");
            return false;
        }
    }
    $("#loanProduct_list").after("<div id='toAddLoanProduct' style=' padding:10px; '></div>");
    $("#toAddLoanProduct").dialog({
        resizable: false,
        title: '选择借款产品',
        href: '${ctx}/jsp/product/lend/toSelLoanProduct?productType=' + $('#product_addLendProduct_form #productType').val(),
        width: 500,
        modal: true,
        height: 300,
        top: 200,
        left: 500,
        buttons: [
            {
                text: '提交',
                iconCls: 'icon-ok',
                handler: function () {
                    $("#toAddLoanProduct").contents().find("#product_toSelLoanProduct_form").submit();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#toAddLoanProduct").dialog('close');
                }
            }
        ],
        onClose: function () {
            $(this).dialog('destroy');
        }
    });
}

function setLendTypeSelect(){
	var flag1 = $('#productType_Rights').hasClass("btn-primary");
	var flag2 = $('#productType_Financing').hasClass("btn-primary");
	if(flag1){
		$("#lendTypeSelect").val("1");
	}else if(flag2){
		$("#lendTypeSelect").val("2");
	}else{}
}

function delLoanProduct() {
    var selRow = $("#product_addLendProduct_form #loanProduct_list").datagrid("getSelected");
    if (selRow) {
        $.messager.confirm("系统提示", "是否确定删除该债权？", function (data) {
            if (data) {
                $("#product_addLendProduct_form #loanProduct_list").datagrid("deleteRow",
                        $("#product_addLendProduct_form #loanProduct_list").datagrid("getRowIndex", selRow));
                $("#product_addLendProduct_form #existLoanRatio").val($("#product_addLendProduct_form #existLoanRatio").val() * 1 - selRow.ratio * 1);
            }
        })
    } else {
        $.messager.alert("系统提示", "请选择要删除的债权信息！", "info");
    }

}


$("#product_addLendProduct_form #toInterestPoint").combobox({
    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=toInterestPoint',
    valueField: 'constantValue',
    textField: 'constantName'
});
$("#product_addLendProduct_form #closingType").combobox({
    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=closingType',
    valueField: 'constantValue',
    textField: 'constantName'
});
$("#product_addLendProduct_form #timeLimitType").combobox({
    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=lendProductTimeLimitType',
    valueField: 'constantValue',
    textField: 'constantName'
});
$("#product_addLendProduct_form #renewalCycleType").combobox({
    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=renewalCycleType',
    valueField: 'constantValue',
    textField: 'constantName',
    onSelect: function (rec) {
        if (rec.constantValue == '1') {
            $("#product_addLendProduct_form #spanRenewalType").hide();
            $("#product_addLendProduct_form #renewalType").combobox('setValue', '0');
            $("#product_addLendProduct_form #renewal").numberbox('setValue', '0');
        } else {
            $("#product_addLendProduct_form #spanRenewalType").show();
        }
    }
});
$("#product_addLendProduct_form #renewalType").combobox({
    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=renewalBalanceType',
    valueField: 'constantValue',
    textField: 'constantName',
    onSelect: function (rec) {
        if (rec.constantValue == '3') {
            $("#renewal").show();
        } else {
            $("#renewal").hide();
        }
    }
});
$("#product_addLendProduct_form #loanProduct_list").datagrid({
    idField: 'loanProductId',
    title: '',
    pagination: false,
    width: document.body.clientWidth * 0.28,
    height: document.body.clientHeight * 0.25,
    singleSelect: true,
    rownumbers: false,
    toolbar: '#loanProduct_list_toolbar'

});
$("#product_addLendProduct_form #discountFeest_list").datagrid({
    idField: 'id',
    title: '',
    pagination: false,
    width: document.body.clientWidth * 0.28,
    height: document.body.clientHeight * 0.25,
    singleSelect: true,
    rownumbers: false,
    toolbar: '#discountFeest_list_toolbar'

});
//设置下拉框默认选中
$("#product_addLendProduct_form #closingType").combobox("select", '${lendProduct.closingType}');
$("#product_addLendProduct_form #timeLimitType").combobox("select", '${lendProduct.timeLimitType}');
$("#product_addLendProduct_form #toInterestPoint").combobox("select", '${lendProduct.toInterestPoint}');
$("#product_addLendProduct_form #renewalCycleType").combobox("select", '${lendProduct.renewalCycleType}');
$("#product_addLendProduct_form #renewalType").combobox("select", '${lendProduct.renewalType}');

//页面初始回显时显示

if ('${lendProduct.productType}' == <%=LendProduct.PRODUCTTYPE_FINANCING%>) {
    $('#product_addLendProduct_form #productType_Financing').attr("class", "btn btn-primary");
    $('#product_addLendProduct_form #productType_Rights').attr("class", "btn");
} else {
    $('#product_addLendProduct_form #productType_Financing').attr("class", "btn");
    $('#product_addLendProduct_form #productType_Rights').attr("class", "btn btn-primary");
}
function productTypeDemo(val) {
    $('#product_addLendProduct_form #productType').val(val);
    if (val == '<%=LendProduct.PRODUCTTYPE_FINANCING%>') {
        $('#product_addLendProduct_form #productType_Financing').attr("class", "btn btn-primary");
        $('#product_addLendProduct_form #productType_Rights').attr("class", "btn");
    } else {
        $('#product_addLendProduct_form #productType_Financing').attr("class", "btn");
        $('#product_addLendProduct_form #productType_Rights').attr("class", "btn btn-primary");
    }
}

//处理阶梯优惠
/**
 * 取消阶梯优惠
$.ajax({
    url: '${ctx}/jsp/product/lend/findLendProductLadderDiscountFees?lendProductId=' + '${lendProduct.lendProductId}',
    success: function (json) {
        $.each(json, function (index, ObjectVal) {
            var lendProductLadderDisCount = ObjectVal.lendProductLadderDiscount;
            var minAmountVal = lendProductLadderDisCount.minAmount;
            var maxAmountVal = lendProductLadderDisCount.maxAmount;
            var discountRateVal = ObjectVal.discountRate * 100;
            var discounType = (100 - discountRateVal);
            //alert(discountRateVal)
            if (discounType == '100') {
                discounType = "免";
            } else {
                discounType = discounType + "%";
            }

            //页面初始化时，为阶梯优惠列表赋值
            $("#product_addLendProduct_form #discountFeest_list").datagrid("appendRow", {
                balances: minAmountVal + "~" + maxAmountVal,
                discounType: discounType,
                discountRate: (100 - discountRateVal) / 100,
                itemName: ObjectVal.itemName,
                feesItemId: ObjectVal.feesItemId,
                lpldfid: ObjectVal.lpldfId,
                lpldId: lendProductLadderDisCount.lpldId,
                minAmount: minAmountVal,
                maxAmount: maxAmountVal
            });
        });


    }
});
 */
//处理适用债权
$.ajax({
    url: '${ctx}/jsp/product/lend/lendLoanBindings?lendProductId=' + '${lendProduct.lendProductId}',
    success: function (json) {
        $.each(json, function (index, ObjectVal) {
            $("#product_addLendProduct_form #loanProduct_list").datagrid("appendRow", {
                loanProductId: ObjectVal.loanProductId,
                productName: ObjectVal.loanProduct.productName,
                ratioStr: (ObjectVal.loanRatio * 100) + "%",
                ratio: ObjectVal.loanRatio,
                itemName: ObjectVal.feesItem==null?"":ObjectVal.feesItem.itemName,
                feesItemId: ObjectVal.feesItemId,
                lendLoanBindingId: ObjectVal.lendLoanBindingId,
                lendProductId: ObjectVal.lendProductId,
                chargePoint: ObjectVal.chargePoint
            });
            $("#product_addLendProduct_form #existLoanRatio").val($("#product_addLendProduct_form #existLoanRatio").val() * 1 + ObjectVal.loanRatio);
            $("#product_addLendProduct_form #loanProduct_list").datagrid("acceptChanges");
        })
    }
});

$("#product_addLendProduct_form").form({
    url: '${ctx}/jsp/product/lend/saveLendProduct',
    onSubmit: function () {
        var result = $("#product_addLendProduct_form").form("validate");
        if (result) {
            $.ajax({
                url: '${ctx}/jsp/product/lend/findProductVersionByName',
                async: false,
                data: {
                    productName : $("#product_addLendProduct_form #productName").val(),
                    productType : $("#product_addLendProduct_form #productType").val(),
                    flag : "editLendProduct",
                    lendProductId : '${lendProduct.lendProductId}',
                    versionCode : $("#versionCode").val(),
                    productState : '${lendProduct.productState}'
                },
                type: "post",
                success: function (date) {
                    result = date;
                    if (result == false) {
                        $.messager.alert("系统提示", '同一产品在同一类别下只能为一个版本号!', 'info');
                        return false;
                    }
                }
            });
        }
        //alert($("#product_addLendProduct_form").serialize());
        $("#product_addLendProduct_form #loanProducts").val(JSON.stringify($("#product_addLendProduct_form #loanProduct_list").datagrid("getData")));
      	
        //初始化续费起投额
        if($("#renewalType").combobox('getValue') == <%=LendProduct.RENEWALBALANCETYPE_MORETHANSTARTSAT%>){
        	$("#product_addLendProduct_form #renewal").numberbox('setValue', $("#startsAt").val());
        }else if($("#renewalType").combobox('getValue') == <%=LendProduct.RENEWALBALANCETYPE_MORETHANUPAT%>){
        	$("#product_addLendProduct_form #renewal").numberbox('setValue', $("#upAt").val());
        }
        /** 取消阶梯优惠
        $("#product_addLendProduct_form #discountFees").val(JSON.stringify($("#product_addLendProduct_form #discountFeest_list").datagrid("getData")));
        var falg = false;
        $.each($("#product_addLendProduct_form #discountFeest_list").datagrid("getRows"), function (index, value) {
            if (!falg) {
                $.each($("#product_addLendProduct_form #intro"), function (index, itemTypeObj) {
                    if (itemTypeObj.checked) {
                        if (!falg) {
                            $.ajax({
                                url: '${ctx}/jsp/product/lend/findFeesMessById?feesItemId=' + value.feesItemId,
                                async: false,
                                success: function (date) {
                                    if (itemTypeObj.value == date) {//只要存在一个阶梯优惠不在费用中的就不让用户提交表单
                                        falg = true;
                                    }
                                }

                            });

                        }
                    }

                });
            }
        });
        if($("#product_addLendProduct_form #discountFeest_list").datagrid("getRows").length>0){//有数据
            if (!falg) {
                $.messager.alert("系统提示", "阶梯优惠的管理费名称必须是所选择的费用", "info");
                return false;
            }
        }
        
        var checkFee = true;
        $.each($("#product_addLendProduct_form #intro"), function (index, value) {
//            alert(index+"====="+value.checked);
            if (value.checked) {
                checkFee = false;
                return !checkFee;
            }

        });
        if (checkFee) {
            $.messager.alert("系统提示", "费用信息为必选项", "info");
            return false;
        }
        */
        if($("#product_addLendProduct_form #productType").val() == '<%=LendProduct.PRODUCTTYPE_FINANCING%>'){
        	var max=$("#product_addLendProduct_form #profitRateMax").val();
        	var min=$("#product_addLendProduct_form #profitRate").val();
        	min=parseFloat(min);
        	max=parseFloat(max);
        	if(min>max){
        		$.messager.alert("系统提示", "请输入正确的收益范围！", "info");
                return false;
        	}
        }
        if ($("#product_addLendProduct_form #loanProduct_list").datagrid("getRows").length == 0) {
            $.messager.alert("系统提示", "适用债权为必填项，请先选择要绑定的债权！", "info");
            return false;
        }
        if ($("#product_addLendProduct_form #productType").val() == '<%=LendProduct.PRODUCTTYPE_RIGHTS%>') {
            if ($("#product_addLendProduct_form #loanProduct_list").datagrid("getRows").length > 1) {
                $.messager.alert("系统提示", "债权类出借产品，只能绑定一个借款产品，请删除已绑定借款产品！", "info");
                return false;
            } else {
                if (($("#product_addLendProduct_form #existLoanRatio").val()) != 1) {
                    $.messager.alert("系统提示", "适用债权列表的占比之和必须为100%!", "info");
                    return false;
                }
            }

        }else if($("#product_addLendProduct_form #productType").val() == '<%=LendProduct.PRODUCTTYPE_FINANCING%>'){
        	var loanList = $("#product_addLendProduct_form #loanProduct_list").datagrid("getRows");
        	if(loanList.length==0){
        		$.messager.alert("系统提示", "适用债权为必填项，请先选择要绑定的债权!", "info");
	        	return false;
        	}
        }
        Utils.loading();
        if (!result) {
            Utils.loaded();
        }
        return result;
    },
    success: function (data) {
        Utils.loaded();
        if (data == "success") {
            $.messager.alert("系统提示", "产品修改成功!", "info");
            parent.$("#toEditLendProduct").dialog("close");
            parent.$("#product_lendproductList_list").datagrid("reload");
        }else{
        	$.messager.alert("系统提示", "产品修改失败!"+data, "info");
        }
    }
});


$(function(){
	//省心计划
	$("#productType_Financing").click(function(){
	
		$("#product_addLendProduct_form #toInterestPoint").html("");
		$("#product_addLendProduct_form #renewalCycleType").html("");
		//返息周期
		$("#product_addLendProduct_form #toInterestPoint").combobox({
		    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=toInterestPoint&productType=finance&r_='+Math.random(),
		    valueField: 'constantValue',
		    textField: 'constantName'
		});
		//续费
		$("#product_addLendProduct_form #renewalCycleType").combobox({
		    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=renewalCycleType&productType=finance&r_='+Math.random(),
		    valueField: 'constantValue',
		    textField: 'constantName',
		    onSelect: function (rec) {
		        if (rec.constantValue == '1') {
		            $("#product_addLendProduct_form #spanRenewalType").hide();
		            $("#product_addLendProduct_form #renewalType").combobox('setValue', '0');
		            $("#product_addLendProduct_form #renewal").numberbox('setValue', '0');
		        } else {
		            $("#product_addLendProduct_form #spanRenewalType").show();
		        }
		    }
		});
	});
	//债权标
	$("#productType_Rights").click(function(){
	$("#product_addLendProduct_form #toInterestPoint").html("");
		$("#product_addLendProduct_form #renewalCycleType").html("");
		//返息周期
		$("#product_addLendProduct_form #toInterestPoint").combobox({
		    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=toInterestPoint&r_='+Math.random(),
		    valueField: 'constantValue',
		    textField: 'constantName'
		});
		//续费
		$("#product_addLendProduct_form #renewalCycleType").combobox({
		    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=renewalCycleType&r_='+Math.random(),
		    valueField: 'constantValue',
		    textField: 'constantName',
		    onSelect: function (rec) {
		        if (rec.constantValue == '1') {
		            $("#product_addLendProduct_form #spanRenewalType").hide();
		            $("#product_addLendProduct_form #renewalType").combobox('setValue', '0');
		            $("#product_addLendProduct_form #renewal").numberbox('setValue', '0');
		        } else {
		            $("#product_addLendProduct_form #spanRenewalType").show();
		        }
		    }
		});
	});
});

</script>
</body>
</html>
