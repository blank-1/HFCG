<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.LoanProduct" %>
<%@ include file="../../../common/common.jsp" %>
<body>
<div id="product_loanproductList" class="container-fluid" style="padding: 5px 0px 0px 10px">

    <form method="post">
        <div style="height:auto;padding-left: 30px;">
            名称:<input type="text" id="productName">
            利率:<input type="text" id="annualRate">
            期限:<input type="text" id="searchDueTime">
            状态:<select id="loanSearchState" class="easyui-combobox">
            <option value="-1">全部</option>
            <option value="0">有效</option>
            <option value="1">无效</option>
        </select>
            <a href="javascript:doSearchLoanProduct();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
        </div>
    </form>
    <div id="product_loanproductList_toolbar" style="height:auto;padding-left: 30px;">
	    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true"
	       onclick="toAddLoanProduct()">新增</a>
    </div>
    <table id="product_loanproductList_list">
        <thead>
        <th data-options="field:'loadProductId', checkbox:true"></th>
        <th data-options="field:'productName', width:180,formatter:formatterProductName">产品名称</th>
        <th data-options="field:'annualRate',align:'center',width:100">年利率(%)</th>
        <th data-options="field:'dueTime',align:'center',width:160">期限时长</th>
        <th data-options="field:'versionCode',align:'center',width:80">版本号</th>
        <th data-options="field:'createTime',align:'center',width:160">录入时间</th>
        <th data-options="field:'productState',align:'center',width:100,formatter:formatterProductState">产品状态</th>
        <th data-options="field:'handle',width:200,align:'center',formatter:formatterHandle">操作</th>
        </thead>
    </table>
</div>
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

function formatterProductState(value, row, index) {
    return value == <%=LoanProduct.PUBLISHSTATE_VALID%> ? '有效' : '无效';
}
function formatterProductName(val, row, index) {
    var value = '<a onclick="findLoanProduct(' + index + ')">' + val + '</a>&nbsp;';
    return value;
}
function formatterHandle(value, row, index) {
    var value = '<a class="label label-info" onclick="findLoanProduct(' + index + ')">详情</a>&nbsp;';
    value += '<a class="label label-important" onclick="toEditLoanProduct(' + index + ')">编辑</a>&nbsp;';
    if(row.productState == <%=LoanProduct.PUBLISHSTATE_INVALID%>){
        value += '<a class="label label-success" onclick="enableLoanProduct(' + index + ')">启用</a>&nbsp;';
    }else{
        value += '<a class="label" onclick="disableLoanProduct(' + index + ')">禁用</a>';
    }
    return value;
}

// 列表加载
function doSearch() {
    $("#product_loanproductList_list").datagrid({
        idField: 'loadProductId',
        title: '借款产品列表',
        url: '${ctx}/jsp/product/loan/loanProductPage',
        pagination: true,
        pageSize: 10,
        width: document.body.clientWidth * 0.97,
        height: document.body.clientHeight * 0.8,
        singleSelect: true,
        rownumbers: true,
        toolbar: '#product_loanproductList_toolbar',
        onDblClickRow: function (index, data) {
            findLoanProduct(index);
        },
        onBeforeLoad: function (value, rec) {
            var createTime = $(this).datagrid("getColumnOption", "createTime");
            if (createTime) {
                createTime.formatter = function (value, rowData, rowIndex) {
                    var d = new Date(value);
                    return getDateTimeStr(d);
                    //return value;
                }
            }
            var repaymentMethod = $(this).datagrid("getColumnOption", "repaymentMethod");
            if (repaymentMethod) {
                repaymentMethod.formatter = function (value, rowData, rowIndex) {
                    var str;
                    $.each(cDefines, function (key, theValue) {
                        if (theValue.constantTypeCode == 'repaymentMethod') {
                            if (theValue.constantValue == value) {
                                str = theValue.constantName;
                            }

                        }
                    })
                    return str;
                }
            }
            var repaymentCycle = $(this).datagrid("getColumnOption", "repaymentCycle");
            if (repaymentCycle) {
                repaymentCycle.formatter = function (value, rowData, rowIndex) {
                    var str;
                    $.each(cDefines, function (key, theValue) {
                        if (theValue.constantTypeCode == 'repaymentCycle') {
                            if (theValue.constantValue == value) {
                                str = theValue.constantName;
                            }

                        }
                    })
                    return str;
                }
            }
            var repaymentType = $(this).datagrid("getColumnOption", "repaymentType");
            if (repaymentType) {
                repaymentType.formatter = function (value, rowData, rowIndex) {
                    var str;
                    $.each(cDefines, function (key, theValue) {
                        if (theValue.constantTypeCode == 'repaymentMode') {
                            if (theValue.constantValue == value) {
                                str = theValue.constantName;
                            }

                        }
                    })
                    return str;
                }
            }
            var dueTime = $(this).datagrid("getColumnOption", "dueTime");
            if (dueTime) {
                dueTime.formatter = function (value, rowData, rowIndex) {
                    if (rowData.dueTimeType == <%=LoanProduct.DUETIMETYPE_MONTH%>) {
                        return value + "(月)";
                    } else if (rowData.dueTimeType == <%=LoanProduct.DUETIMETYPE_DAY%>) {
                        return value + "(天)";
                    }
                }
            }

            var annualRate = $(this).datagrid("getColumnOption", "annualRate");
            if (annualRate) {
                annualRate.formatter = function (value, rowData, rowIndex) {
                    return value.toFixed(3) + "%";
                }
            }


        }
    });
}
doSearch();

// 弹出添加页面
function toAddLoanProduct() {
    $("#product_loanproductList").after("<div id='toAddLoanProduct' style=' padding:10px; '></div>");
    $("#toAddLoanProduct").dialog({
        resizable: false,
        title: '新增借款产品',
        href: '${ctx}/jsp/product/loan/addLoanProduct',
        width: 800,
        modal: true,
        height: 600,
        top: 100,
        left: 400,
        buttons: [
            {
                text: '提交',
                iconCls: 'icon-ok',
                handler: function () {
                    try {
                        $("#toAddLoanProduct").contents().find("#product_addLoanProduct_form").submit();
                    } catch (e) {
                        $.messager.alert("系统提示", e, "info");
                    }

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

var tf = true;
function toEditLoanProduct(index) {
    $("#product_loanproductList_list").datagrid("selectRow", index);
    var selRow = $("#product_loanproductList_list").datagrid("getSelected");
    if (selRow) {
        $.post('${ctx}/jsp/product/loan/findLoanApplicationState?loanProductId=' + selRow.loanProductId, function (date) {
            if (date == "success") {//该产品关联有合同，不能修改
                if (tf) {
                    tf = false;
                    $("#product_loanproductList").after("<div id='toEditLoanProduct' style=' padding:10px; '></div>");
                    $("#toEditLoanProduct").dialog({
                        resizable: false,
                        title: '修改借款产品',
                        href: '${ctx}/jsp/product/loan/editLoanProduct?loanProductId=' + selRow.loanProductId,
                        width: 800,
                        modal: true,
                        height: 600,
                        top: 100,
                        left: 400,
                        buttons: [
                            {
                                text: '提交',
                                iconCls: 'icon-ok',
                                handler: function () {
                                    try {
                                        tf = true;
                                        $("#toEditLoanProduct").contents().find("#product_editLoanProduct_form").submit();
                                    } catch (e) {
                                        $.messager.alert("系统提示", e, "info");
                                    }

                                }
                            },
                            {
                                text: '取消',
                                iconCls: 'icon-cancel',
                                handler: function () {
                                    tf = true;
                                    $("#toEditLoanProduct").dialog('close');
                                }
                            }
                        ],
                        onClose: function () {
                            tf = true;
                            $(this).dialog('destroy');
                        }
                    });
                }
            } else {
                $.messager.alert("系统提示", "该产品关联有合同，不可修改", "info");
            }

        });


    } else {
        $.messager.alert("系统提示", "请选择你要修改的信息!", "info");
    }
}

function enableLoanProduct(index) {
    $("#product_loanproductList_list").datagrid("selectRow", index);
    var selRow = $("#product_loanproductList_list").datagrid("getSelected");
    if (selRow) {
        if (selRow.productState == <%=LoanProduct.PUBLISHSTATE_INVALID%>) {
            $.messager.confirm('系统提示', '你确定启用吗？', function (r) {
                if (r) {
                    //判断是否已为禁用状态
                    if (selRow.productState == <%=LoanProduct.PUBLISHSTATE_INVALID%>) {
                        $.get("${ctx}/jsp/product/loan/enableOrdisableLoanProduct?loanProductId=" + selRow.loanProductId + "&Type=enableLoanProduct", function (data) {
                            $("#product_loanproductList_list").datagrid("reload");
                            $("#product_loanproductList_list").datagrid("selectRow", index);
                        });
                    } else {
                        $.messager.alert("系统提示", "该条产品已是启用状态!", "info");
                    }
                }
            });
        } else {
            $.messager.alert("系统提示", "该条产品已是启用状态!", "info");
        }

    } else {
        $.messager.alert("系统提示", "请选择要启用的产品!", "info");
    }

}

function disableLoanProduct(index) {
    $("#product_loanproductList_list").datagrid("selectRow", index);
    var selRow = $("#product_loanproductList_list").datagrid("getSelected");
    if (selRow) {
        if (selRow.productState == <%=LoanProduct.PUBLISHSTATE_VALID%>) {
            $.messager.confirm('系统提示', '你确定禁用吗？', function (r) {
                if (r) {
                    //判断是否已为禁用状态
                    $.get("${ctx}/jsp/product/loan/enableOrdisableLoanProduct?loanProductId=" + selRow.loanProductId + "&Type=disableLoanProduct", function (data) {
                        $("#product_loanproductList_list").datagrid("reload");
                        $("#product_loanproductList_list").datagrid("selectRow", index);
                    });
                }
            });
        } else {
            $.messager.alert("系统提示", "该产品已禁用!", "info");
        }
    } else {
        $.messager.alert("系统提示", "请选择要禁用的产品!", "info");
    }

}

function findLoanProduct(index) {
    $("#product_loanproductList_list").datagrid("selectRow", index);
    var selRow = $("#product_loanproductList_list").datagrid("getSelected");
    if (selRow) {
        $("#product_loanproductList").after("<div id='findLoanProduct' style=' padding:10px; '></div>");
        $("#findLoanProduct").dialog({
            resizable: true,
            title: '查看产品详情',
            modal: true,
            href: '${ctx}/jsp/product/loan/findLoanProduct?loanProductId=' + selRow.loanProductId,
            width: 680,
            modal: true,
            height: 500,
            top: 200,
            left: 500,
            buttons: [
                {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#findLoanProduct").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    } else {
        $.messager.alert("系统提示", "请选择你要查看的信息!", "info");
    }
}


$("#annualRate").combobox({
    url: '${ctx}/jsp/product/loan/findAnnualRate',
    textField: 'annualRateValue',
    valueField: 'annualRateId'
});
$("#searchDueTime").combobox({
    url: '${ctx}/jsp/product/loan/findDueTime',
    textField: 'dueTimeValue',
    valueField: 'dueTimeId'

});
function doSearchLoanProduct() {
    var loanProduct = {};
    loanProduct.productName = $("#productName").val();
    loanProduct.annualRate = $("#annualRate").combobox("getValue");
    loanProduct.searchDueTime = $("#searchDueTime").combobox("getValue");
    loanProduct.loanSearchState = $("#loanSearchState").combobox("getValue");
    $("#product_loanproductList_list").datagrid("options").queryParams = loanProduct;
    $("#product_loanproductList_list").datagrid("load");
}
</script>

</body>
