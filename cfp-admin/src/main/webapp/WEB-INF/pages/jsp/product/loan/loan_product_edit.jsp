<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.FeesItem" %>
<%@ page import="com.xt.cfp.core.pojo.LoanProduct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="product_editLoanProduct_form" method="post">
    <input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
    <input type="hidden" name="loanProductId" id="loanProductId" value="${loanProductId}"/>
    <input type="hidden" name="createTime" id="createTime" value="${loanProduct.createTime}">
    <input type="hidden" name="productState" value="${loanProduct.productState}">


    <div class="control-group">
        <label class="control-label">产品名称<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   style="width: 200px"
                   name="productName" id="productName" value="${loanProduct.productName}"
                   onchange="findProductState(this)" data-options="validType:'validProductName[\'this\']'">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">年利率<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   class="easyui-numberbox" style="width: 100px"
                   name="annualRate" id="annualRate" value="${loanProduct.annualRate}" required="true"
                   missingMessage="请输入年利率" max="100" precision="3">%
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">还款方法<span style="color: red">*</span></label>

        <div class="controls">
            <input id="repaymentMethod" name="repaymentMethod"
                   required="true" missingMessage="请选择还款方法"
                    />
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">还款周期<span style="color: red">*</span></label>

        <div class="controls">
            <input id="repaymentCycle" name="repaymentCycle"
                   required="true" missingMessage="请选择还款周期"
                    />
            <input type="text"
                   class="easyui-numberbox" style="width: 50px;display: none" data-options="min:0,precision:0,max:300"
                   name="cycleValue" id="cycleValue" value="${loanProduct.cycleValue}">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">还款方式<span style="color: red">*</span></label>

        <div class="controls">
            <input id="repaymentType" name="repaymentType"
                   required="true" missingMessage="请选择还款方式"
                    />
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">期限时长<span style="color: red">*</span></label>

        <div class="controls">
            <input type="hidden" id="dueTimeType" name="dueTimeType" value="${loanProduct.dueTimeType}">
            <input type="text"
                   class="easyui-numberbox" style="width: 80px"
                   required="true" missingMessage="请输入期限时长"
                   name="dueTime" id="dueTime" value="${loanProduct.dueTime}">

            <div class="btn-group" data-toggle="buttons-radio">
                <button class="btn" id="dueTimeType_Day" onclick="dueTimeTypeDemo(<%=LoanProduct.DUETIMETYPE_DAY%>)">天
                </button>
                <button class="btn" id="dueTimeType_Month"
                        onclick="dueTimeTypeDemo(<%=LoanProduct.DUETIMETYPE_MONTH%>)">月
                </button>
            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">有效日期<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   class="easyui-datebox" style="width: 100px"
                   name="startDate" id="startDate" value="" editable="false"
                   data-options="validType:'validTimes[\'endDate\']'">-
            <input type="text"
                   class="easyui-datebox"
                   style="width: 100px"
                   name="endDate" id="endDate"
                   value="" editable="false" data-options="validType:'validTimes[\'startDate\']'">
            <input id="startDateId" type="hidden">
            <input id="endDateId" type="hidden">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">版本号<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   style="width: 200px"
                   name="versionCode" id="versionCode" value="${loanProduct.versionCode}"
                   data-options="validType:'validProductVersion[\'this\']'" onchange="findProductVersion(this.value)">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">产品描述<span style="color: red">*</span></label>

        <div class="controls">

            <textarea style="width: 300px;" rows="4" cols="50" name="productDesc"
                      id="productDesc">${loanProduct.productDesc}</textarea>
        </div>
    </div>
    <c:forEach items="${itemTypes}" var="itemType"><%Boolean flag = true;%>
        <div class="control-group">
            <label class="control-label">${itemType.constantName}<span style="color: red"></span></label>

            <div class="controls">
                <div style="display: inline-block;">
                    <c:forEach items="${itemType.childItemTypes}" var="childItemType">
                    <span style="padding-bottom: 10px;display: block">
                        <c:forEach items="${feesItemByLoanProductId}" var="feesItemByLoanProductId">
                            <c:if test="${childItemType.constantValue == feesItemByLoanProductId.itemType}">
                                <c:forEach items="${loanProductFeesItemList}" var="loanProductFeesItem">
                                    <c:if test="${loanProductFeesItem.feesItemId == feesItemByLoanProductId.feesItemId}">
                                        <input feesItemId="${loanProductFeesItem.feesItemId}"
                                               workflowRatio="${loanProductFeesItem.workflowRatio}"
                                               radiceName="${feesItemByLoanProductId.radiceName}"
                                               chargeCycle="${loanProductFeesItem.chargeCycle}" type="checkbox"
                                               id="intro" name="feeItems" value="${childItemType.constantValue}"
                                               onclick="selItemType(this, '${childItemType.constantValue}','100','')"
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
                                   onclick="selItemType(this, '${childItemType.constantValue}','100','')">
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

</form>
<script language="JAVASCRIPT">
//页面初始回显时显示

//新增时判断借款产品名称是否存在并且无效

function findProductState(val) {
    var tf = true;
    var productName = '${loanProduct.productName}';
    if (productName != val.value) {
        $.ajax({
            url: '${ctx}/jsp/product/loan/doProductStateByName?productName=' + encodeURI(val.value.trim()),
            success: function (date) {

                $("#product_editLoanProduct_form #productName").focus();
                $.extend($.fn.validatebox.defaults.rules, {
                    validProductName: {
                        validator: function (val, param) {
                            if (date == "success") {//借款产品名已存在，并且产品状态为无效
                                return true;
                            } else {
                                tf = false;
                                return false;
                            }
                        },
                        message: '用户名已存在，并且当前产品有效'
                    }
                });
            }
        });
    } else {
        if (tf) {
            $("#product_editLoanProduct_form #productName").blur(function () {
                $("#product_editLoanProduct_form #productName").focus();
                $.extend($.fn.validatebox.defaults.rules, {
                    validProductName: {
                        validator: function (val, param) {
                            tf = false;
                            return true;
                        },
                        message: '用户名已存在，并且当前产品有效'
                    }
                });
            });
        }
    }
}

function findProductVersion(val) {
    $.ajax({
        url: '${ctx}/jsp/product/loan/doProductVersionByName?versionCode=' + encodeURIComponent(val.trim()) + "&productName=" + $("#product_editLoanProduct_form #productName").val() + "&loanProductId=" + $("#product_editLoanProduct_form #loanProductId").val(),
        success: function (date) {

            $("#product_editLoanProduct_form #versionCode").focus();
            $.extend($.fn.validatebox.defaults.rules, {
                validProductVersion: {
                    validator: function (val, param) {
                        if (date) {
                            return true;
                        } else {
                            tf = false;
                            return false;
                        }
                    },
                    message: '同一个产品只能有一个版本号'
                }
            });
        }
    });

}
if ('${loanProduct.dueTimeType}' == <%=LoanProduct.DUETIMETYPE_DAY%>) {
    $('#product_editLoanProduct_form #dueTimeType_Day').attr("class", "btn btn-primary");
    $('#product_editLoanProduct_form #dueTimeType_Month').attr("class", "btn");
} else {
    $('#product_editLoanProduct_form #dueTimeType_Day').attr("class", "btn");
    $('#product_editLoanProduct_form #dueTimeType_Month').attr("class", "btn btn-primary");
}
$.ajax({async: false})
var url = "${ctx}/jsp/product/loan/getConstantDefines";
var cDefines = {};
if (cDefines.length == 0) {
    $.get(url, function (data) {
        cDefines = data;
    })
}
//选择修改天和月是触发
function dueTimeTypeDemo(val) {
    $('#product_editLoanProduct_form #dueTimeType').val(val);
    if (val == '<%=LoanProduct.DUETIMETYPE_DAY%>') {
        $('#product_editLoanProduct_form #dueTimeType_Day').attr("class", "btn btn-primary");
        $('#product_editLoanProduct_form #dueTimeType_Month').attr("class", "btn");
    } else {
        $('#product_editLoanProduct_form #dueTimeType_Day').attr("class", "btn");
        $('#product_editLoanProduct_form #dueTimeType_Month').attr("class", "btn btn-primary");
    }
}
$("#product_editLoanProduct_form #productName").focus();
$("#product_editLoanProduct_form #productName").validatebox({
    required: true,
    missingMessage: '请输入产品名称'
});
$("#product_editLoanProduct_form #versionCode").validatebox({
    required: true,
    missingMessage: '请输入版本号'
});
$("#product_editLoanProduct_form #productDesc").validatebox({
    required: true,
    missingMessage: '请输入产品描述'
});

//处理时间显示
$("#startDate").attr("value", getDateStr(new Date('${loanProduct.startDate}')));
$("#startDateId").attr("value", getDateStr(new Date('${loanProduct.startDate}')));
$("#endDate").attr("value", getDateStr(new Date('${loanProduct.endDate}')));
$("#endDateId").attr("value", getDateStr(new Date('${loanProduct.endDate}')));
$("#createTime").attr("value", getDateStr(new Date('${loanProduct.createTime}')));
$('#product_editLoanProduct_form #startDate').datebox({
    required: true,
    missingMessage: '请选择开始日期',
    onSelect: function (date) {
        $("#product_editLoanProduct_form #startDateId").attr("value", date);
    }
});

$('#product_editLoanProduct_form #endDate').datebox({
    required: true,
    missingMessage: '请选择结束日期',
    onSelect: function (date) {

        $("#product_editLoanProduct_form #endDateId").attr("value", date);

    }

});
$.extend($.fn.validatebox.defaults.rules, {
    validTimes: {
        validator: function (val, param) {
            //debugger
            var value = $.fn.datebox.defaults.parser(val);
            if (param == 'startDate') {//开始时间   结束时间大于开始时间
                var startDateVal = $.fn.datebox.defaults.parser(getDateStr(new Date($("#startDateId").val())));
                return value >= startDateVal;
            } else if (param == 'endDate') {
                var endDateVal = $.fn.datebox.defaults.parser(getDateStr(new Date($("#endDateId").val())));
                return value <= endDateVal;
            }
        },
        message: '开始日期必须小于结束日期'
    }
});
var checkedVal = $("input#intro");
for (var j = 0; j < checkedVal.length; j++) {
    var chargecycle = "";
    var feesitemid = "";
    var workflowratio = "";
    var radiceName = "";
    if (checkedVal[j].checked) {
        //$("input#intro").click();
        for (var i = 0; i < checkedVal[j].attributes.length; i++) {
            if (checkedVal[j].attributes[i].name == "chargecycle") {
                chargecycle = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "feesitemid") {
                feesitemid = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "workflowratio") {
                workflowratio = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
            if (checkedVal[j].attributes[i].name == "radicename") {
                radiceName = checkedVal[j].attributes[i].nodeValue;
                continue;
            }
        }
        selItemType(checkedVal[j], checkedVal[j].value, workflowratio, radiceName);
        $("#product_editLoanProduct_form #chargePoint_" + checkedVal[j].value).combobox("select", chargecycle);
        $("#product_editLoanProduct_form #feesItems_" + checkedVal[j].value).combobox("select", feesitemid);
    }
}
function selItemType(theCheckBox, itemType, workflowratio, radiceName) {
    if (theCheckBox.checked) {
        $("#product_editLoanProduct_form #toAdd_" + itemType).append("<input editable='false' style='width:95px;' id='chargePoint_" + itemType + "' name='chargePoint_" + itemType + "'/>&nbsp;&nbsp;");
        $("#product_editLoanProduct_form #chargePoint_" + itemType).combobox({
            url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=chargePoint,chargePoint_b',
            valueField: 'constantValue',
            textField: 'constantName',
            required: true,
            missingMessage: '请选择收费周期'
        })

        $("#product_editLoanProduct_form #toAdd_" + itemType).append("<input editable='false' style='width:95px;' id='feesItems_" + itemType + "' name='feesItems_" + itemType + "'/>");
        $("#product_editLoanProduct_form #feesItems_" + itemType).combobox({
            url: '${ctx}/jsp/product/feesitem/getFeesItemsByItemType?itemType=' + itemType + "&itemKind=<%=FeesItem.ITEMKIND_LOAN%>",
            valueField: 'feesItemId',
            textField: 'itemName',
            required: true,
            missingMessage: '请选择费用项目',
            onSelect: function (val) {
                $.post('${ctx}/jsp/product/loan/findFeesRadicesAndRate?feesItemId=' + val.feesItemId, function (date) {
                    radiceName = date[0].radiceName;
                    $("#product_editLoanProduct_form #radiceName" + itemType).text(radiceName + ";平台收费");
                });
            }
        })
        workflowratio = workflowratio * 100;
        $("#product_editLoanProduct_form #toAdd_" + itemType).append("&nbsp;<span id='radiceName" + itemType + "'>" + radiceName + ";平台收取</span><input style='width: 45px' value = '" + workflowratio + "'" + " id='workflowRatio_" + itemType + "' " +
                "name='workflowRatio_" + itemType + "'/><span id='workflowRatioSpan_" + itemType + "'>%</span>");
        $('#workflowRatio_' + itemType).numberbox({
            min: 0,
            max: 100,
            precision: 3
        });
    } else {
        $("#product_editLoanProduct_form #chargePoint_" + itemType).combobox("destroy");
        $("#product_editLoanProduct_form #feesItems_" + itemType).combobox("destroy");
        $("#product_editLoanProduct_form #workflowRatio_" + itemType).numberbox("destroy");
        $("#product_editLoanProduct_form #radiceName" + itemType).remove();
        $("#product_editLoanProduct_form #workflowRatioSpan_" + itemType).remove();
    }

}


//还款方法
$("#product_editLoanProduct_form #repaymentMethod").combobox({
    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=repaymentMethod',
    valueField: 'constantValue',
    textField: 'constantName'
});
$("#product_editLoanProduct_form #repaymentMethod").combobox("select", '${loanProduct.repaymentMethod}');
//还款方式
$("#product_editLoanProduct_form #repaymentType").combobox({
    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=repaymentMode',
    valueField: 'constantValue',
    textField: 'constantName'
});
$("#product_editLoanProduct_form #repaymentType").combobox("select", '${loanProduct.repaymentType}');
//还款周期
$("#product_editLoanProduct_form #repaymentCycle").combobox({
    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=repaymentCycle',
    valueField: 'constantValue',
    textField: 'constantName',
    onSelect: function (rec) {
        if (rec.constantValue == '<%=LoanProduct.REPAYMENTCYCLE_DAY%>') {
            $("#product_editLoanProduct_form #cycleValue").show();
        } else {
            $("#product_editLoanProduct_form #cycleValue").hide();
        }
    }
});
//判断为天时，显示出多少天
if ('${loanProduct.repaymentCycle}' == '<%=LoanProduct.REPAYMENTCYCLE_DAY%>') {
    $("#product_editLoanProduct_form #cycleValue").show();
} else {
    $("#product_editLoanProduct_form #cycleValue").hide();
}
$("#product_editLoanProduct_form #repaymentCycle").combobox("select", '${loanProduct.repaymentCycle}');

$("#product_editLoanProduct_form").form({
    url: '${ctx}/jsp/product/loan/saveLoanProduct',
    onSubmit: function () {

        var result = $(this).form('validate');
        if (result) {
			
        	/* 注：这里临时注释
            var checkFee = true;
            $.each($("#product_editLoanProduct_form #intro"), function (index, value) {
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
            
            //alert(JSON.stringify($("#product_editLoanProduct_form").serialize()));
            //判断同一产品在有效时间内只能为一个版本号
            /* $.ajax({
             url: '
            ${path}/jsp/product/doProductVersionByName?productName=' + $("#product_editLoanProduct_form #productName").val() + "&startDate=" + getDateStr(new Date($("#startDateId").val())) + "&endDate=" + getDateStr(new Date($("#endDateId").val())) + "&versionCode=" + $("#versionCode").val() + "&flag=editLoanProduct",
             async: false,
             success: function (date) {
             result = date;
             if (result == false) {
             $.messager.alert("系统提示", '同一产品在有效时间内只能为一个版本号', 'info');
             }
             }
             });*/
        }
        var cycleType = $("#product_editLoanProduct_form #repaymentCycle").combobox('getValue');
        if (cycleType == '<%=LoanProduct.REPAYMENTCYCLE_ONCE%>') {
            $("#product_editLoanProduct_form #cycleValue").val($("#product_editLoanProduct_form #dueTime").val());
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
            $.messager.alert("系统提示", "更新产品成功!", "info");
            parent.$("#toEditLoanProduct").dialog("close");
            parent.$("#product_loanproductList_list").datagrid("reload");
            $.each(parent.$("#product_loanproductList_list").datagrid("getRows"),function(index,val){
                if(val.loanProductId == '${loanProductId}'){
                    parent.$("#product_loanproductList_list").datagrid("selectRow", index);
                }
            });
        }
    }
})
</script>
</body>
</html>