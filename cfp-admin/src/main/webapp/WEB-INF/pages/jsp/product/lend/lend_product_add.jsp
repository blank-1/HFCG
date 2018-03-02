<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.LendProduct" %>
<%@ page import="com.xt.cfp.core.pojo.FeesItem" %>
<%@ page import="com.xt.cfp.core.constants.LendProductTypeEnum" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="product_addLendProduct_form" method="post">
<input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
<input type="hidden" name="lendProductId" id="lendProductId" value="${lendProductId}"/>
<input type="hidden" name="productType" id="productType" value="1"/>

<input type="hidden" name="discountFees" id="discountFees"/>
<input type="hidden" name="existLoanRatio" id="existLoanRatio"/>

<div class="control-group">
    <label class="control-label">产品名称<span style="color: red">*</span></label>

    <%--<div class="controls">
        <input type="text"
               class="easyui-validatebox" required="true" missingMessage="产品名称不能为空" style="width: 200px"
               name="productName" id="productName" value="${lendProduct.productName}"/>

        <div class="btn-group" data-toggle="buttons-radio">
            <button class="btn active" onclick="$('#product_addLendProduct_form #productType').val('1')">债权类
            </button>
            <button class="btn" onclick="$('#product_addLendProduct_form #productType').val('2')">省心计划</button>
        </div>
    </div>--%>

    <div class="controls">
        <input type="text"
               class="easyui-validatebox" required="true" missingMessage="产品名称不能为空" style="width: 200px"
               name="productName" id="productName" value="${lendProduct.productName}"/>

        <div class="btn-group" data-toggle="buttons-radio">
            <button class="btn btn-primary" id="productType_Rights"
                    onclick="productTypeDemo(<%=LendProductTypeEnum.RIGHTING.getValue()%>)">
                债权类
            </button>
            <button class="btn" id="productType_Financing"
                    onclick="productTypeDemo(<%=LendProductTypeEnum.FINANCING.getValue()%>)">省心计划
            </button>
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
			<span style="width: 130px;display:none;" id="profitRateMax_span">
                ~
                <input type="text"
                       class="easyui-numberbox" required="true" missingMessage="收益利率不能为空"
                       data-options="min:1,precision:2,max:100" style="width: 100px;"
                       name="profitRateMax" id="profitRateMax" value="0"/>%
            </span>
        <span id="close_date_span" style="width: 60px;display: inline-block"> 封闭期<span style="color: red">*</span></span>
        <input type="text" class="easyui-numberbox" style="width: 80px" size="4" data-options="min:0,precision:0"
               required="true" missingMessage="封闭期不能为空"
               name="closingDate" id="closingDate" value=""/>

        <input id="closingType" name="closingType" style="width: 50px" />
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
            <input type="text" class="easyui-numberbox" id="renewal" name="renewal"
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
        <textarea rows="3" cols="60" style="width: 450px;" id="productDesc" name="productDesc" class="easyui-validatebox" required="true"
                  missingMessage="请填写产品描述"/>

    </div>
</div>

<fieldset>
    <legend>费用</legend>
    <c:forEach items="${itemTypes}" var="itemType">
        <div class="control-group">
            <label class="control-label">${itemType.constantName}<span style="color: red">*</span></label>

            <div class="controls">
                <div style="display: inline-block;">
                    <c:forEach items="${itemType.childItemTypes}" var="childItemType">
                    <span style="padding-bottom: 10px;display: block">
                    <input type="checkbox" id="intro" name="feeItems" value="${childItemType.constantValue}"
                           onclick="selItemType(this, '${childItemType.constantValue}')">${childItemType.constantName}&nbsp;&nbsp;
                         <span id="toAdd_${childItemType.constantValue}"/>

                    <br/>
                        </span>
                    </c:forEach>
                </div>
            </div>
        </div>
    </c:forEach>
</fieldset>
<!--取消阶梯优惠
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
                <th data-options="field:'feesItemId',width:1,hidden:true"></th>
                <th data-options="field:'discountRate',width:1,hidden:true"></th>
                <th data-options="field:'minAmount',width:1,hidden:true"></th>
                <th data-options="field:'maxAmount',width:1,hidden:true"></th>
                </thead>
            </table>
        </div>
    </div>
</fieldset -->
<fieldset>
    <legend>适用债权</legend>
    <div class="control-group">
        <label class="control-label">债权列表
            <input class="easyui-validatebox" style="border: 0;background-color: #FFFFFF;color: #FFFFFF;width: 15px"
                   name="loanProducts" required="true" missingMessage="必须选择债权" id="loanProducts"/></label>

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
                <th data-options="field:'itemName',width:160">管理费</th>
                <th data-options="field:'feesItemId',width:160,hidden:true"></th>

                </thead>
            </table>
        </div>
    </div>
</fieldset>
<input type="hidden" name="lendTypeSelect" id="lendTypeSelect" value="">
</form>
<script language="javascript">
var url = "${ctx}/jsp/product/loan/getConstantDefines";
var cDefines = {};
$.ajax({
    async: true,
    url: url,
    success: function (data) {
        cDefines = data;
    }
})

function productTypeDemo(val) {
    $('#product_addLendProduct_form #productType').val(val);
    if (val == '<%=LendProduct.PRODUCTTYPE_FINANCING%>') {
        $('#product_addLendProduct_form #productType_Financing').attr("class", "btn btn-primary");
        $('#product_addLendProduct_form #productType_Rights').attr("class", "btn");
        $("#profitRateMax_span").show();
        $("#close_date_span").hide();
        $("#closingDate").hide();
        $("#closingType").next(".combo").hide();
    } else {
        $('#product_addLendProduct_form #productType_Financing').attr("class", "btn");
        $('#product_addLendProduct_form #productType_Rights').attr("class", "btn btn-primary");
        $("#profitRateMax_span").hide();
        $("#close_date_span").show();
        $("#closingDate").show();
        $("#closingType").next(".combo").show();
    }
}
var feesItems = new Array();
/**
 * 取消阶梯优惠
function toAddLadder() {
    //console.info("====================="+JSON.stringify(feesItems));
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
function selItemType(theCheckBox, itemType) {
    if (theCheckBox.checked) {
        $("#product_addLendProduct_form #toAdd_" + itemType).append("<input style='width:95px;' id='chargePoint_" + itemType + "' name='chargePoint_" + itemType + "'/>&nbsp;&nbsp;");
        $("#product_addLendProduct_form #chargePoint_" + itemType).combobox({
            url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=chargePoint,chargePoint_l',
            valueField: 'constantValue',
            textField: 'constantName',
            required: true,
            missingMessage: '请选择收费周期'
        })
        $("#product_addLendProduct_form #toAdd_" + itemType).append("<input style='width:95px;' id='feesItems_" + itemType + "' name='feesItems_" + itemType + "'/>");
        $("#product_addLendProduct_form #feesItems_" + itemType).combobox({
            url: '${ctx}/jsp/product/feesitem/getFeesItemsByItemType?itemType=' + itemType + "&itemKind=<%=FeesItem.ITEMKIND_LEND%>",
            valueField: 'feesItemId',
            textField: 'itemName',
            required: true,
            missingMessage: '请选择费用项目',
            onSelect: function (rec) {
                //todo 把基数类型转换为基数名称
                var typeName = "";
                if(rec.radicesType == '0'){
                    typeName = rec.radiceName;
                }else{
                    $.each(cDefines, function (cDefineKey, cDefineValue) {
                        if (cDefineValue.constantTypeCode == 'radicesType') {
                            if (cDefineValue.constantValue == rec.radicesType) {
                                typeName = cDefineValue.constantName;
                            }

                        }
                    })
                }

                $("#product_addLendProduct_form #radiceName_" + itemType).html(typeName + "*" + rec.feesRate + "%;" + "平台收费");
                for (var i = 0; i < feesItems.length; i++) {//处理一个费用类别，添加时对应多个费用项的bug
                    var val = feesItems[i];
                    if (rec.itemType == val.itemType) {
                        feesItems.splice(i, 1);
                    }

                }
                feesItems.push(rec);
            }
        })


        $("#product_addLendProduct_form #toAdd_" + itemType).append("&nbsp;<span id='radiceName_" + itemType + "'>平台收取</span><input style='width: 50px' id='workflowRatio_" + itemType + "' " +
                "name='workflowRatio_" + itemType + "'/><span id='workflowRatioSpan_" + itemType + "'>%</span>");
        $('#workflowRatio_' + itemType).numberbox({
            min: 0,
            value: 100,
            max: 100,
            precision: 3
        });

        $("#product_addLendProduct_form #workflowRatio_" + itemType).validatebox({
            required: true,
            missingMessage: '请选择收费比例'
        });
    } else {
        $("#product_addLendProduct_form #chargePoint_" + itemType).combobox("destroy");
        $("#product_addLendProduct_form #feesItems_" + itemType).combobox("destroy");
        $("#product_addLendProduct_form #workflowRatio_" + itemType).numberbox("destroy");
        $("#product_addLendProduct_form #radiceName_" + itemType).remove();
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

})
/* 
 * 取消阶梯优惠
 $("#product_addLendProduct_form #discountFeest_list").datagrid({
    idField: 'id',
    title: '',
    pagination: false,
    width: document.body.clientWidth * 0.28,
    height: document.body.clientHeight * 0.25,
    singleSelect: true,
    rownumbers: false,
    toolbar: '#discountFeest_list_toolbar'

}) */

$("#product_addLendProduct_form").form({
    url: '${ctx}/jsp/product/lend/saveLendProduct',
    onSubmit: function () {

        if(result) {
            $.ajax({
                url: '${ctx}/jsp/product/lend/findProductVersionByName?productName=' + $("#product_addLendProduct_form #productName").val() + "&productType=" + $("#product_addLendProduct_form #productType").val() + "&flag=addLendProduct" + "&lendProductId=" + '${lendProductId}' + "&versionCode=" + $("#versionCode").val() + "&productState=" +<%=LendProduct.PRODUCTSTATE_VALID%>,
                async: false,
                success: function (date) {
                    result = date;
                    if (result == false) {
                        $.messager.alert("系统提示", '同一产品在同一类别下只能为一个版本号!', 'info');
                    }
                }
            });
        }
        //初始化续费起投额
        if($("#renewalType").combobox('getValue') == <%=LendProduct.RENEWALBALANCETYPE_MORETHANSTARTSAT%>){
        	$("#product_addLendProduct_form #renewal").numberbox('setValue', $("#startsAt").val());
        }else if($("#renewalType").combobox('getValue') == <%=LendProduct.RENEWALBALANCETYPE_MORETHANUPAT%>){
        	$("#product_addLendProduct_form #renewal").numberbox('setValue', $("#upAt").val());
        }
        /*取消阶梯优惠
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
        $("#product_addLendProduct_form #discountFees").val(JSON.stringify($("#product_addLendProduct_form #discountFeest_list").datagrid("getData")));
        */
        $("#product_addLendProduct_form #loanProducts").val(JSON.stringify($("#product_addLendProduct_form #loanProduct_list").datagrid("getData")));
        // alert($("#product_addLendProduct_form").serialize());
        /*
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
        }*/
        if($("#product_addLendProduct_form #loanProduct_list").datagrid("getRows").length == 0){
            $.messager.alert("系统提示", "适用债权为必填项，请先选择要绑定的债权！", "info");
            return false;
        }
        if($("#product_addLendProduct_form #productType").val() == '<%=LendProduct.PRODUCTTYPE_FINANCING%>'){
        	$("#product_addLendProduct_form #closingDate").numberbox("setValue","0");
        	$("#product_addLendProduct_form #closingType").combobox("setValue","1");
        	var max=$("#product_addLendProduct_form #profitRateMax").val();
        	var min=$("#product_addLendProduct_form #profitRate").val();
        	min=parseFloat(min);
        	max=parseFloat(max);
        	if(min>max){
        		$.messager.alert("系统提示", "请输入正确的收益范围！", "info");
                return false;
        	}
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
        var result = $("#product_addLendProduct_form").form("validate");
        var maxP=$("#profitRateMax").val();
        if(maxP==undefined||maxP==null||maxP==""){
        	$("#profitRateMax").val(0);
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
            $.messager.alert("系统提示", "新增产品成功!", "info");
            parent.$("#toAddLendProduct").dialog("close");
            parent.$("#product_lendproductList_list").datagrid("reload");
        }else{
        	$.messager.alert("系统提示", "新增产品失败!"+data, "info");
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
