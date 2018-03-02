<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.FeesItem" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="addFees_form" method="post">
    <input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
    <input type="hidden" name="feesItemId" id="feesItemId" value="${feesItemId}"/>
    <input type="hidden" name="itemState" id="itemState" value="${itemState}">

    <div class="control-group">
        <label class="control-label">费用名称<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   style="width: 200px"
                   name="itemName" id="itemName" value="${feesItem.itemName}" class="easyui-validatebox" required="true"
                   missingMessage="费用名称不能为空" data-options="validType:'validFeesName[\'this\']'"
                   onchange="validFeesByName(this)">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">费用描述<span style="color: red">*</span></label>

        <div class="controls">
            <textarea style="width: 200px;" rows="3" cols="50" name="itemDesc"
                      id="itemDesc">${feesItem.itemDesc}</textarea>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">费用分类<span style="color: red">*</span></label>

        <div class="controls">

            <input id="itemKind" editable="false" name="itemKind" style="width: 200px" required="true"
                   missingMessage="请选择费用分类">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">费用类别<span style="color: red">*</span></label>

        <div class="controls">

            <input id="parentItemType" editable="false" style="width: 200px" required="true" missingMessage="请选择费用类别">

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">费用子类别<span style="color: red">*</span></label>

        <div class="controls">
            <input id="itemType" editable="false" name="itemType" required="true" style="width: 200px"
                   missingMessage="请选择费用子类别">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">收取比例<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   style="width: 180px"
                   name="feesRate" id="feesRate" value="${feesItem.feesRate}" class="easyui-numberbox" min="0" max="100"
                   precision="2" required="true" missingMessage="收取比例最小为0">%
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">基数<span style="color: red">*</span></label>

        <div class="controls">
            <input id="radicesType" name="radicesType" style="width:200px;" value="${feesItem.radicesType}"
                   required="true" missingMessage="请选择基数" editable="false">
        </div>
    </div>

    <div class="control-group" id="customRadiceName" style="display:none">
        <label class="control-label">自定义基数名称<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text" id="radiceName" name="radiceName" style="width:200px;" value="${feesItem.radiceName}">
        </div>
    </div>

    <div class="control-group" id="customRadiceLogic" style="display:none">
        <label class="control-label">自定义基数逻辑<span style="color: red">*</span></label>

        <div class="controls">
            <textarea rows="4" cols="50" style="width:250px;" id="radiceLogic"
                      name="radiceLogic">${feesItem.radiceLogic}</textarea>
        </div>
    </div>

</form>


<script language="JAVASCRIPT">


    $("#itemKind").combobox({
        url: '${ctx}/jsp/product/feesitem/itemKindBox',
        valueField: 'constantValue',
        textField: 'constantName',
        onSelect: function (record) {
            $("#parentItemType").combobox("clear");
            $("#itemType").combobox("clear");
            $("#parentItemType").combobox("reload",
                            '${ctx}/jsp/product/feesitem/itemTypeBox?constantValue=' + record.constantValue);
        }
    });
    $("#addFees_form #itemDesc").validatebox({
        required: true,
        missingMessage: '请输入费用描述'
    });
    $("#parentItemType").combobox({
        valueField: 'constantValue',
        textField: 'constantName',
        onSelect: function (record) {
            $("#itemType").combobox("clear");
            $("#itemType").combobox("reload",
                            '${ctx}/jsp/product/feesitem/itemTypeChildBox?constantValue=' + record.constantValue);
        }
    });

    $("#itemType").combobox({
        valueField: 'constantValue',
        textField: 'constantName',
        onSelect: function (record) {

        }
    });
    $("#radicesType").combobox({
        url: '${ctx}/jsp/product/feesitem/radicesTypeBox',
        valueField: 'constantValue',
        textField: 'constantName',
        onSelect: function (record) {
            if (record.constantValue == 0) {
                $("#customRadiceName").show();//显示
                $("#customRadiceLogic").show();//显示
            } else {
                $("#customRadiceName").hide();//
                $("#customRadiceLogic").hide();//
            }
        }
    });


    function validFeesByName(val) {
        var name = '${feesItem.itemName}';

        if (val.value != name) {
            $.ajax({
                url:'${ctx}/jsp/product/feesitem/validFeesByName?feesName='+encodeURIComponent(val.value),
                success:function(state){
                    if(state){
                        $.messager.alert("系统提示","费用名不能重复！","info");
                    }
                    /*$.extend($.fn.validatebox.defaults.rules, {
                        validFeesName: {
                            validator: function (val, param) {
                                if(state){
                                    return false;
                                }else{
                                    return true;
                                }
                            },
                            message: '费用名称不能重复！'
                        }
                    });*/
                }
            });
        }
    }
    $("#addFees_form").form({
        url: '${ctx}/jsp/product/feesitem/saveFeesItem',
        onSubmit: function () {
            //alert($("#addFees_form").serialize());
            return $(this).form('validate');
        },
        success: function (data) {
            if (data == "success") {
                if ($("#addFlag").attr("value") == 'true') {
                    $.messager.alert("系统提示", "新增费用成功!", "info");
                    parent.$("#addFees").dialog("close");
                } else {
                    $.messager.alert("系统提示", "更新费用成功!", "info");
                    parent.$("#editFeesMess").dialog("close");
                }
                parent.$("#fees_feesItem_list").datagrid("reload");
            }
        }
    })
    //判断是否是为编辑查询，是，则进行判断费用子类别的判断选中
    if ($("#addFlag").attr("value") == 'false') {
        $("#itemKind").combobox("select", '${feesItem.itemKind}');
        //基数
        $("#radicesType").combobox("select", '${feesItem.radicesType}');
        var radicesType = '${feesItem.radicesType}';
        if (radicesType == 0) {
            $("#customRadiceName").show();//显示
            $("#customRadiceLogic").show();//显示
        }

        //处理费用类别
        /*$("#parentItemType").combobox("reload",
                '${ctx}/jsp/product/feesitem/itemTypeBox?constantValue=${feesItem.itemKind}');
        $("#parentItemType").combobox("select", '${constantDefine.constantName}');
        //处理费用子类别
        $("#itemType").combobox("reload",
                '${ctx}/jsp/product/feesitem/itemTypeChildBox?constantValue=${constantDefine.constantValue}');
        $("#itemType").combobox("select", '${constantDefineChild.constantName}');*/
        
      //处理费用类别
        $("#parentItemType").combobox("reload",
                '${ctx}/jsp/product/feesitem/itemTypeBox?constantValue=${feesItem.itemKind}');
        $("#parentItemType").combobox("select", '${constantDefine.constantValue}');
        //处理费用子类别
        $("#itemType").combobox("reload",
                '${ctx}/jsp/product/feesitem/itemTypeChildBox?constantValue=${constantDefine.constantValue}');
        $("#itemType").combobox("select", '${feesItem.itemType}');

    }
</script>
</body>
</html>