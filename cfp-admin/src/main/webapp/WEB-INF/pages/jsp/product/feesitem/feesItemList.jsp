<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.FeesItem" %>
<%@ include file="../../../common/common.jsp" %>
<body>
<div id="fees_feesItem" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form id="formId" method="post">
    <fieldset style="height:65px;width: 800px;">
            <legend>查询条件</legend>
     <div style="height:auto;padding-left: 30px;" id="fees_feesItem_toolb">
            费用名称:<input type="text" id="itemName" name="itemName">
            费用描述:<input type="text" name="itemDesc" id="itemDesc">
            状态:<select class="easyui-combobox" id="itemState_box" name="itemStateAll" editable="false">
            <option value="-1">全部</option>
            <option value="0">有效</option>
            <option value="1">无效</option>
        </select>
            <a href="javascript:doSearchFees();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
        </div>
        </fieldset>
    </form>
    
    <div id="fees_feesItem_toolbar" style="height:auto;padding-left: 30px;">
        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add"
                                        plain="true"
                                        onclick="addFeesMess();">新增</a>
    </div>
    <table id="fees_feesItem_list">
        <thead>
        <th data-options="field:'feesItemId',checkbox:true"></th>
        <th data-options="field:'itemName', width:180,formatter:formatterFeesName">费用名称</th>
        <th data-options="field:'itemDesc',width:220">费用描述</th>
        <th data-options="field:'itemKind',width:110">费用分类</th>
        <th data-options="field:'feesRate',width:60,formatter:formatterFeesRate">收取比例</th>
        <th data-options="field:'radiceName',width:160">基数</th>
        <th data-options="field:'createTime',width:160">录入时间</th>
        <th data-options="field:'itemState',width:60,formatter:formatterState">状态</th>
        <th data-options="field:'action',title:'操作',width:180,align:'center',formatter:formatterHandle"></th>
        </thead>

    </table>
</div>
<script language="javascript">

var url = "${ctx}/jsp/product/loan/getConstantDefines";
var cDefines = {};
$.ajax({
    async: false,
    url: url,
    success: function (data) {
        cDefines = data;
    }
})
function formatterFeesName(val, row, index) {
    var value = '<a onclick="findFeesMess(' + index + ')">' + val + '</a>&nbsp;';
    return value;
}
function formatterHandle(value, row, index) {
    var value = '<a class="label label-info" onclick="findFeesMess(' + index + ')">详情</a>&nbsp;';
    value += '<a class="label label-important" onclick="editFeesMess(' + index + ')">修改</a>&nbsp;';
    if(row.itemState == <%=FeesItem.ITEMSTATE_DISABLED%>){
        value += '<a class="label label-success" onclick="enableFeesMess(' + index + ')">启用</a>&nbsp;';
    }else{
        value += '<a class="label" onclick="disableFeesMess(' + index + ')">禁用</a>';
    }
    return value;
}


function disableFeesMess(index) {
    $("#fees_feesItem_list").datagrid("selectRow", index);
    var selRow = $("#fees_feesItem_list").datagrid("getSelected");
    if (selRow) {
        if (selRow.itemState == <%=FeesItem.ITEMSTATE_ENABLED%>) {
            $.messager.confirm('系统提示', '你确定禁用吗？', function (r) {
                if (r) {
                    //判断是否已为禁用状态
                    $.get("${ctx}/jsp/product/feesitem/disableFees?feesItemId=" + selRow.feesItemId, function (data) {
                        //$.messager.alert("系统提示", data, "info");
                        $("#fees_feesItem_list").datagrid("reload");
                    });
                }
            });
        } else {
            $.messager.alert("系统提示", "该条费用已禁用!", "info");
        }
    } else {
        $.messager.alert("系统提示", "请选择要禁用的费用!", "info");
    }

}
function enableFeesMess(index) {
    $("#fees_feesItem_list").datagrid("selectRow", index);
    var selRow = $("#fees_feesItem_list").datagrid("getSelected");
    if (selRow) {
        //判断是否已为禁用状态
        if (selRow.itemState == <%=FeesItem.ITEMSTATE_DISABLED%>) {
            $.messager.confirm('系统提示', '你确定启用吗？', function (r) {
                if (r) {
                    //判断是否已为禁用状态
                    $.get("${ctx}/jsp/product/feesitem/enableFees?id=" + selRow.feesItemId, function (data) {
                        //$.messager.alert("系统提示", data, "info");
                        $("#fees_feesItem_list").datagrid("reload");
                    });
                }
            });
        } else {
            $.messager.alert("系统提示", "该条费用已是启用状态!", "info");
        }
    } else {
        $.messager.alert("系统提示", "请选择要禁用的费用!", "info");
    }

}

function doSearchFees() {
    var feesItem = {};
    feesItem.itemName = $("#itemName").val();
    feesItem.itemDesc = $("#itemDesc").val();
    feesItem.itemStateAll = $("#itemState_box").combobox("getValue");
    $("#fees_feesItem_list").datagrid("options").queryParams = feesItem;
    $("#fees_feesItem_list").datagrid("load");
}

function formatterFeesRate(value, row, index) {
    return value + "%";
}

function formatterState(value, row, index) {
    return value == <%=FeesItem.ITEMSTATE_ENABLED%> ? '有效' : '无效';
}

function doSearch() {

    $("#fees_feesItem_list").datagrid({
        idField: 'feesItemId',
        title: '费用列表',
        url: '${ctx}/jsp/product/feesitem/listByPage',
        pagination: true,
        width: document.body.clientWidth * 0.97,
        height: document.body.clientHeight * 0.7,
        singleSelect: true,
        rownumbers: true,
        toolbar: '#fees_feesItem_toolbar',
        onDblClickRow: function (index, data) {
            findFeesMess(index);
        },
        onBeforeLoad: function (value, rec) {
            var createTime = $(this).datagrid("getColumnOption", "createTime");
            if (createTime) {
                createTime.formatter = function (value, rowData, rowIndex) {
                    var d = new Date(value);
                    return getDateTimeStr(d);
                }
            }


            var radiceName = $(this).datagrid("getColumnOption", "radiceName");
            if (radiceName) {
                radiceName.formatter = function (value, rowData, rowIndex) {
                    var str;
                    $.each(cDefines, function (key, theValue) {
                        if (theValue.constantTypeCode == 'radicesType') {
                            if (rowData.radicesType == '0') {
                                str = value;
                            } else {
                                if (theValue.constantValue == rowData.radicesType) {
                                    str = theValue.constantName;
                                }
                            }
                        }
                    })
                    return str;
                }
            }

            var itemKind = $(this).datagrid("getColumnOption", "itemKind");
            if (itemKind) {
                itemKind.formatter = function (value, rowData, rowIndex) {
                    var str;
                    $.each(cDefines, function (key, theValue) {
                        if (theValue.constantTypeCode == 'itemKind') {
                            if (theValue.constantValue == rowData.itemKind) {
                                str = theValue.constantName;
                            }
                        }
                    })
                    return str;
                }
            }


        }
    });

}
function addFeesMess() {
    $("#fees_feesItem").after("<div id='addFees' style=' padding:10px; '></div>");
    $("#addFees").dialog({
        resizable: true,
        title: '新增费用定义',
        href: '${ctx}/jsp/product/feesitem/addFeesItem',
        width: 600,
        modal: true,
        height: 500,
        top: 100,
        left: 400,
        buttons: [
            {
                text: '提交',
                iconCls: 'icon-ok',
                handler: function () {
                    try {
                        $("#addFees").contents().find("#addFees_form").submit();
                    } catch (e) {
                        $.messager.alert("系统提示", e, "info");
                    }

                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#addFees").dialog('close');
                }
            }
        ],
        onClose: function () {
            $(this).dialog('destroy');
        }
    });
}

function editFeesMess(index) {
    $("#fees_feesItem_list").datagrid("selectRow", index);
    var selRow = $("#fees_feesItem_list").datagrid("getSelected");
    if (selRow) {//判断费用是否有产品占用
        $.post('${ctx}/jsp/product/feesitem/findFeesEditState?feesItemId=' + selRow.feesItemId, function (date) {
            if (date == "success") {//该费用没有产品占用，可以修改费用
                $("#fees_feesItem").after("<div id='editFeesMess' style=' padding:10px; '></div>");
                $("#editFeesMess").dialog({
                    resizable: true,
                    title: '修改费用定义',
                    href: '${ctx}/jsp/product/feesitem/editFeesItem?feesItemId=' + selRow.feesItemId,
                    width: 600,
                    modal: true,
                    height: 500,
                    top: 100,
                    left: 400,
                    buttons: [
                        {
                            text: '提交',
                            iconCls: 'icon-ok',
                            handler: function () {
                                if (!($("#editFeesMess").contents().find("#addFlag").val() == "true")) {
                                    $.messager.confirm("系统提示", "是否确认修改该费用？", function (data) {
                                        if (data) {
                                            $("#editFeesMess").contents().find("#addFees_form").submit();
                                        }
                                    })
                                }

                            }
                        },
                        {
                            text: '取消',
                            iconCls: 'icon-cancel',
                            handler: function () {
                                $("#editFeesMess").dialog('close');
                            }
                        }
                    ],
                    onClose: function () {
                        $(this).dialog('destroy');
                    }
                });

            } else {
                $.messager.alert("系统提示", "该费用已被产品占用，不可修改", "info");
            }

        });


    } else {
        $.messager.alert("系统提示", "请选择要修改的费用!", "info");
    }
}

function findFeesMess(index) {
    $("#fees_feesItem_list").datagrid("selectRow", index);
    var selRow = $("#fees_feesItem_list").datagrid("getSelected");
    if (selRow) {
        $("#fees_feesItem").after("<div id='findFeesMess' style=' padding:10px; '></div>");
        $("#findFeesMess").dialog({
            resizable: true,
            title: '查看费用详情',
            modal: true,
            href: '${ctx}/jsp/product/feesitem/findFeesMess?feesItemId=' + selRow.feesItemId,
            width: 500,
            modal: true,
            height: 450,
            top: 200,
            left: 500,
            buttons: [
                {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#findFeesMess").dialog('close');
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
doSearch();
</script>
</body>