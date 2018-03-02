<%@ taglib prefix="mis" uri="http://www.cfp-admin.com/jsp/taglib" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.LendProduct" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<body>
<div id="product_lendproductList" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form method="post">
        <div style="height:auto;padding-left: 30px;">
            名称:<input type="text" id="productName">
            利率:<input type="text" id="searchProfitRate">
            期限:<input type="text" id="searchTimeLimit">
            状态:<select id="searchProductState" class="easyui-combobox">
            <option value="-1">全部</option>
            <option value="0">有效</option>
            <option value="1">无效</option>
        </select>
            <a href="javascript:doSearchLendProduct();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
        </div>
    </form>
    <div id="product_lendproductList_toolbar" style="height:auto;padding-left: 30px;">
        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add"
                                        plain="true"
                                        onclick="toAddLendProduct()">新增</a>
    </div>

    <table id="product_lendproductList_list">
        <thead>
        <th data-options="field:'lendProductId', checkbox:true"></th>
        <th data-options="field:'productType', width:100">产品分类</th>
        <th data-options="field:'productName', width:180,formatter:formatterProductName">产品名称</th>
        <th data-options="field:'profitRate',width:80">收益利率(%)</th>
        <th data-options="field:'timeLimit',width:100">期限</th>
        <th data-options="field:'closingDate',width:100">封闭期</th>
        <th data-options="field:'startsAt',width:100,formatter:formatterstartsAt,align:'right'">起投金额</th>
        <th data-options="field:'upAt',width:100,formatter:formatterupAt,align:'right'">递增金额</th>
        <%--<th data-options="field:'renewalCycleType',width:160">续费周期类型</th>--%>
        <%--<th data-options="field:'renewal',width:160">续费</th>--%>
        <th data-options="field:'maxPublishCode',width:100">当前期号</th>
        <th data-options="field:'sumPublishBalance',width:100,align:'right',formatter:formatterPublishBalance">累计发售金额
        </th>
        <th data-options="field:'sumSaidBalance',width:100,align:'right',formatter:formatterSaidBalance">累计售卖金额</th>
        <th data-options="field:'productState',width:100">状态</th>
        <th data-options="field:'createTime',width:160">录入时间</th>
        <th data-options="field:'action',title:'操作',width:240,align:'left',formatter:formatterHandle"></th>
        </thead>
    </table>
</div>
<script language="JavaScript">

var url = "${ctx}/jsp/product/loan/getConstantDefines";
var cDefines = {};
$.ajax({
    async: true,
    url: url,
    success: function (data) {
        cDefines = data;
        doSearch();
    }
});
function formatterstartsAt(val, row, index) {
    return formatNum(val, 2);
}
function formatterupAt(val, row, index) {
    return formatNum(val, 2);
}
function formatterPublishBalance(val, row, index) {
    return formatNum(val, 2);
}
function formatterSaidBalance(val, row, index) {
    return formatNum(val, 2);
}

function formatterProductName(val, row, index) {
    var value = '<a onclick="toView(' + index + ')">' + val + '</a>&nbsp;';
    return value;
}
function formatterHandle(value, row, index) {
    var value = '<a class="label label-info" onclick="toView(' + index + ')">详情</a>&nbsp;';
    value += '<a class="label label-important" onclick="toEditLendProduct(' + index + ')">修改</a>&nbsp;';
    if (row.productState == <%=LendProduct.PRODUCTSTATE_INVALID%>) {
        value += '<a class="label label-success" onclick="enableLendProduct(' + index + ')">启用</a>&nbsp;';
    } else {
        value += '<a class="label label-warning" onclick="publishList(' + index + ')">发布</a>&nbsp;';
        value += '<a class="label" onclick="disableLendProduct(' + index + ')">禁用</a>';
    }
    return value;
}
$("#searchProfitRate").combobox({
    url: '${ctx}/jsp/product/lend/searchProfitRate',
    textField: 'profitRateValue',
    valueField: 'profitRateId'
});
$("#searchTimeLimit").combobox({
    url: '${ctx}/jsp/product/lend/searchTimeLimit',
    textField: 'timeLimitValue',
    valueField: 'timeLimitId'

});

function doSearchLendProduct() {
    var lendProductVO = {};
    lendProductVO.productName = $("#productName").val();
    lendProductVO.profitRate = $("#searchProfitRate").combobox("getValue");
    lendProductVO.searchTimeLimit = $("#searchTimeLimit").combobox("getValue");
    lendProductVO.searchProductState = $("#searchProductState").combobox("getValue");
    $("#product_lendproductList_list").datagrid("options").queryParams = lendProductVO;
    $("#product_lendproductList_list").datagrid("load");
}
function disableLendProduct(index) {
    $("#product_lendproductList_list").datagrid("selectRow", index);
    var selRow = $("#product_lendproductList_list").datagrid("getSelected");
    if (selRow) {
        if (selRow.productState == <%=LendProduct.PRODUCTSTATE_VALID%>) {
            $.messager.confirm('系统提示', '你确定禁用吗？', function (r) {
                if (r) {
                    //判断是否已为禁用状态
                    $.get("${ctx}/jsp/product/lend/disableLendProduct?lendProductId=" + selRow.lendProductId, function (data) {
                        $("#product_lendproductList_list").datagrid("reload");
                    });
                }
            });

        } else {
            $.messager.alert("系统提示", "该条产品已禁用!", "info");
        }
    } else {
        $.messager.alert("系统提示", "请选择要禁用的产品!", "info");
    }
}

function enableLendProduct(index) {
    $("#product_lendproductList_list").datagrid("selectRow", index);
    var selRow = $("#product_lendproductList_list").datagrid("getSelected");
    if (selRow) {
        if (selRow.productState == <%=LendProduct.PRODUCTSTATE_INVALID%>) {
            $.messager.confirm('系统提示', '你确定启用吗？', function (r) {
                if (r) {
                    //判断是否已为禁用状态
                    $.get("${ctx}/jsp/product/lend/enableLendProduct?lendProductId=" + selRow.lendProductId, function (data) {
                        $("#product_lendproductList_list").datagrid("reload");
                    });
                }
            });
        } else {
            $.messager.alert("系统提示", "该条费用已启用!", "info");
        }
    } else {
        $.messager.alert("系统提示", "请选择要启用的产品!", "info");
    }

}

function doSearch() {
    $("#product_lendproductList_list").datagrid({
        idField: 'lendProductId',
        title: '出借产品列表',
        url: '${ctx}/jsp/product/lend/lendProductPage',
        pagination: true,
        pageSize: 20,
        width: document.body.clientWidth * 0.97,
        height: document.body.clientHeight * 0.80,
        singleSelect: true,
        rownumbers: true,
        toolbar: '#product_lendproductList_toolbar',
        onDblClickRow: function (index, data) {
            toView(index);
        },
        onBeforeLoad: function (value, rec) {
            var productType = $(this).datagrid("getColumnOption", "productType");
            if (productType) {
                productType.formatter = function (value, rowData, rowIndex) {
                    var str;
                    if(value == '<%=LendProduct.PRODUCTTYPE_RIGHTS%>'){
                    	str="债权类";
                    }else{
                    	str="省心计划";
                    }
                    return str;
                }
            }
            var profitRate = $(this).datagrid("getColumnOption", "profitRate");
            if (profitRate) {
                profitRate.formatter = function (value, rowData, rowIndex) {
                	if(rowData.productType == '<%=LendProduct.PRODUCTTYPE_RIGHTS%>'){
                		return value + "%";
                	}else{
                		return value + "%~"+rowData.profitRateMax+"%";
                	}
                }
            }
            var createTime = $(this).datagrid("getColumnOption", "createTime");
            if (createTime) {
                createTime.formatter = function (value, rowData, rowIndex) {
                    var d = new Date(value);
                    return getDateTimeStr(d);
                }
            }
            var productState = $(this).datagrid("getColumnOption", "productState");
            if (productState) {
                productState.formatter = function (value, rowData, rowIndex) {
                    if (value == '<%=LendProduct.PRODUCTSTATE_VALID%>') {
                        return "有效";
                    } else {
                        return "无效";
                    }
                }
            }
            var timeLimit = $(this).datagrid("getColumnOption", "timeLimit");
            if (timeLimit) {
                timeLimit.formatter = function (value, rowData, rowIndex) {
                    var str;
                    if (rowData.timeLimitType) {
                        $.each(cDefines, function (key, theValue) {
                            if (theValue.constantTypeCode == 'lendProductTimeLimitType') {
                                if (theValue.constantValue == rowData.timeLimitType) {
                                    str = theValue.constantName;
                                }

                            }
                        })
                    }
                    return value + '(' + str + ')';
                }
            }
            var closingDate = $(this).datagrid("getColumnOption", "closingDate");
            if (closingDate) {
                closingDate.formatter = function (value, rowData, rowIndex) {
                    if(rowData.productType=="1"){
                    	var str;
                    	if (rowData.closingType) {
                            $.each(cDefines, function (key, theValue) {
                                if (theValue.constantTypeCode == 'closingType') {
                                    if (theValue.constantValue == rowData.closingType) {
                                        str = theValue.constantName;
                                    }

                                }
                            })
                        }
                        return value + '(' + str + ')';
                    }
                    return "";
                }
            }


        }
    });
}

function toEditLendProduct(index) {
    var tf = true;
    $("#product_lendproductList_list").datagrid("selectRow", index);
    $("#product_lendproductList").after("<div id='toEditLendProduct' style=' padding:10px;'></div>");
    var selRow = $("#product_lendproductList_list").datagrid("getSelected");
    if (selRow) {
        $.post('${ctx}/jsp/product/lend/findLendProductPublishState?lendProductId=' + selRow.lendProductId, function (date) {
            if (date == "success") {
                if (tf) {
                    tf = false;
                    $("#toEditLendProduct").dialog({
                        resizable: false,
                        title: '修改出借产品',
                        href: '${ctx}/jsp/product/lend/editLendProduct?lendProductId=' + selRow.lendProductId,
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
                                        $("#toEditLendProduct").contents().find("#product_addLendProduct_form").submit();
                                    } catch (e) {
                                        alert(e);
                                    }

                                }
                            },
                            {
                                text: '取消',
                                iconCls: 'icon-cancel',
                                handler: function () {
                                    tf = true;
                                    $("#toEditLendProduct").dialog('close');
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
                $.messager.alert("系统提示", "该产品已被占用，不可修改", "info");
                $("#toEditLendProduct").remove();
            }
        });


    } else {
        $.messager.alert("系统提示", "请选择要修改的出借产品!", "info");
    }
}

function toAddLendProduct() {
    $("#product_lendproductList").after("<div id='toAddLendProduct' style=' padding:10px; '></div>");
    $("#toAddLendProduct").dialog({
        resizable: false,
        title: '新增出借产品',
        href: '${ctx}/jsp/product/lend/addLendProduct',
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
                        $("#toAddLendProduct").contents().find("#product_addLendProduct_form").submit();
                    } catch (e) {
                        alert(e);
                    }

                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#toAddLendProduct").dialog('close');
                }
            }
        ],
        onClose: function () {
            $(this).dialog('destroy');
        }
    });
}

function toView(index) {
    $("#product_lendproductList_list").datagrid("selectRow", index);
    var selRow = $("#product_lendproductList_list").datagrid("getSelected");
    if (selRow) {
        $("#product_lendproductList").after("<div id='toViewLendProduct' style=' padding:10px; '></div>");
        $("#toViewLendProduct").dialog({
            resizable: false,
            title: '查看出借产品详细信息',
            href: '${ctx}/jsp/product/lend/toViewLendProduct?lendProductId=' + selRow.lendProductId,
            width: 800,
            modal: true,
            height: 600,
            top: 100,
            left: 400,
            buttons: [
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#toViewLendProduct").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    } else {
        $.messager.alert("系统提示", "请选择要查看的出借产品!", "info");
    }
}

function publishList(index) {
    $("#product_lendproductList_list").datagrid("selectRow", index);
    var selRow = $("#product_lendproductList_list").datagrid("getSelected");
    if (selRow) {
        var url = "${ctx}/jsp/product/lend/publishList?lendProductId=" + selRow.lendProductId;
        window.open(url, "_blank");
    } else {
        $.messager.alert("系统提示", "请选择要发布的出借产品!", "info");
    }
}
</script>
