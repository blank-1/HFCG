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
<form class="form-horizontal" id="product_addLoanProduct_form" method="post">
    <input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
    <input type="hidden" name="loanProductId" id="loanProductId" value="${loadProductId}"/>


    <div class="control-group">
        <label class="control-label">产品名称<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   style="width: 200px"
                   name="productName" id="productName" value="${loanProduct.productName}" onchange="findProductState(this)" data-options="validType:'validProductName[\'startDate\']'">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">年利率<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   class="easyui-numberbox" style="width: 100px" data-options="min:0,precision:2,max:100"
                   name="annualRate" id="annualRate" value="${loanProduct.annualRate}" required="true"
                   missingMessage="请输入年利率">%
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
                   name="cycleValue" id="cycleValue" value="1">
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
            <input type="hidden" id="dueTimeType" name="dueTimeType">
            <input type="text"
                   required="true" missingMessage="请输入期限时长"
                   class="easyui-numberbox" style="width: 80px"
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
                   class="easyui-datebox" style="width: 100px" data-options="validType:'validTime[\'endDate\']'"
                   name="startDate" id="startDate" value="${loanProduct.startDate}" editable="false">-
            <input type="text"
                   class="easyui-datebox"
                   style="width: 100px"
                   name="endDate" id="endDate" data-options="validType:'validTime[\'startDate\']'"
                   value="${loanProduct.endDate}" editable="false">
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
    
    <c:forEach items="${itemTypes}" var="itemType">
        <div class="control-group">
            <label class="control-label">${itemType.constantName}<span style="color: red"></span></label>

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

</form>
<script language="JAVASCRIPT">

    //新增时判断借款产品名称是否存在并且无效
    function findProductState(val){
        $.ajax({
            url:'${ctx}/jsp/product/loan/doProductStateByName?productName='+encodeURI(val.value.trim()),
            success:function(date){
                $("#product_addLoanProduct_form #productName").focus();
                $.extend($.fn.validatebox.defaults.rules, {
                    validProductName: {
                        validator: function (val, param) {
                            if(date == "success"){//借款产品名已存在，并且产品状态为无效
                                return true;
                            }else{
                                return false;
                            }
                        },
                        message: '借款产品名已存在，并且当前产品有效'
                    }
                });
            }
        });
    }
    function findProductVersion(val) {
        $.ajax({
            url: '${ctx}/jsp/product/loan/doProductVersionByName?versionCode=' + encodeURIComponent(val.trim()) + "&productName=" + $("#product_addLoanProduct_form #productName").val() + "&loanProductId=" + $("#product_addLoanProduct_form #loanProductId").val(),
            success: function (date) {

                $("#product_addLoanProduct_form #versionCode").focus();
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
    function dueTimeTypeDemo(val) {
        $('#product_addLoanProduct_form #dueTimeType').val(val);
        if (val == '<%=LoanProduct.DUETIMETYPE_DAY%>') {
            $('#product_addLoanProduct_form #dueTimeType_Day').attr("class", "btn btn-primary");
            $('#product_addLoanProduct_form #dueTimeType_Month').attr("class", "btn");
        } else {
            $('#product_addLoanProduct_form #dueTimeType_Day').attr("class", "btn");
            $('#product_addLoanProduct_form #dueTimeType_Month').attr("class", "btn btn-primary");
        }
    }
    $("#product_addLoanProduct_form #productName").focus();
    $("#product_addLoanProduct_form #productName").validatebox({
        required: true,
        missingMessage: '请输入产品名称'
    });

    $("#product_addLoanProduct_form #productDesc").validatebox({
        required: true,
        missingMessage: '请输入产品描述'
    });


    $('#product_addLoanProduct_form #startDate').datebox({
        required: true,
        missingMessage: '请选择开始日期',
        onSelect: function (date) {
            $("#product_addLoanProduct_form #startDateId").attr("value", date);
        }
    });
    $('#product_addLoanProduct_form #endDate').datebox({
        required: true,
        missingMessage: '请选择结束日期',
        onSelect: function (date) {
            $("#product_addLoanProduct_form #endDateId").attr("value", date);
        }

    });


    $("#product_addLoanProduct_form #versionCode").validatebox({
        required: true,
        missingMessage: '请输入版本号'
    });
    $.extend($.fn.validatebox.defaults.rules, {
        validTime: {
            validator: function (val, param) {
                var value = $.fn.datebox.defaults.parser(val);
                if (param == 'startDate') {//开始时间
                    var startDateVal = $.fn.datebox.defaults.parser(getDateTimeStr(new Date($("#startDateId").val())));
                    if ($("#startDate").val() == "") {//结束时间大于开始时间
                        return value >= startDateVal;
                    }
                } else if (param == 'endDate') {
                    var endDateVal = $.fn.datebox.defaults.parser(getDateTimeStr(new Date($("#endDateId").val())));
                    if ($("#endDate").val() == "") {//结束时间
                        return value <= endDateVal;
                    }
                }

            },
            message: '开始日期必须小于结束日期'
        }
    });


    function selItemType(theCheckBox, itemType) {
        if (theCheckBox.checked) {
            $("#product_addLoanProduct_form #toAdd_" + itemType).append("<input editable='false' style='width:95px;' id='chargePoint_" + itemType + "' name='chargePoint_" + itemType + "'/>&nbsp;&nbsp;");
            $("#product_addLoanProduct_form #chargePoint_" + itemType).combobox({
                url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=chargePoint,chargePoint_b',
                valueField: 'constantValue',
                textField: 'constantName',
                required: true,
                missingMessage: '请选择收费周期'
            })
            $("#product_addLoanProduct_form #toAdd_" + itemType).append("<input editable='false' style='width:95px;' id='feesItems_" + itemType + "' name='feesItems_" + itemType + "'/>");
            $("#product_addLoanProduct_form #feesItems_" + itemType).combobox({
                url: '${ctx}/jsp/product/feesitem/getFeesItemsByItemType?itemType=' + itemType + "&itemKind=<%=FeesItem.ITEMKIND_LOAN%>",
                valueField: 'feesItemId',
                textField: 'itemName',
                required: true,
                missingMessage: '请选择费用项目',
                onSelect: function (val) {
                    $.post('${ctx}/jsp/product/loan/findFeesRadicesAndRate?feesItemId=' + val.feesItemId, function (date) {
                        var radiceName = date[0].radiceName;
                        $("#product_addLoanProduct_form #radiceName" + itemType).text(radiceName + ";平台收费");
                    });
                }
            })

            $("#product_addLoanProduct_form #toAdd_" + itemType).append("&nbsp;<span id='radiceName" + itemType + "'>平台收取</span><input style='width: 45px' id='workflowRatio_" + itemType + "' " +
                    "name='workflowRatio_" + itemType + "'/><span id='workflowRatioSpan_" + itemType + "'>%</span>");
            $('#workflowRatio_' + itemType).numberbox({
                min: 0,
                value: 100,
                max: 100,
                precision: 3
            });

            $("#product_addLoanProduct_form #workflowRatio_" + itemType).validatebox({
                required: true,
                missingMessage: '请选择收费比例'
            });
        } else {
            $("#product_addLoanProduct_form #chargePoint_" + itemType).combobox("destroy");
            $("#product_addLoanProduct_form #feesItems_" + itemType).combobox("destroy");
            $("#product_addLoanProduct_form #workflowRatio_" + itemType).numberbox("destroy");
            $("#product_addLoanProduct_form #radiceName" + itemType).remove();
            $("#product_addLoanProduct_form #workflowRatioSpan_" + itemType).remove();
        }

    }

    //还款方法
    $("#product_addLoanProduct_form #repaymentMethod").combobox({
        url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=repaymentMethod',
        valueField: 'constantValue',
        textField: 'constantName'
    });
    //还款方式
    $("#product_addLoanProduct_form #repaymentType").combobox({
        url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=repaymentMode',
        valueField: 'constantValue',
        textField: 'constantName'
    });
    //还款周期
    $("#product_addLoanProduct_form #repaymentCycle").combobox({
        url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=repaymentCycle',
        valueField: 'constantValue',
        textField: 'constantName',
        onSelect: function (rec) {
            if (rec.constantValue == '<%=LoanProduct.REPAYMENTCYCLE_DAY%>') {
                $("#product_addLoanProduct_form #cycleValue").show();
            }  else {
                $("#product_addLoanProduct_form #cycleValue").hide();
            }
        }
    });


    $("#product_addLoanProduct_form").form({
        url: '${ctx}/jsp/product/loan/saveLoanProduct',
        onSubmit: function () {
        	/* 注意：这里临时注释
            var checkFee = true;
            $.each($("#product_addLoanProduct_form #intro"), function (index, value) {
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
            
            //alert($("#product_addLoanProduct_form").serialize());
            //判断同一产品在有效时间内只能为一个版本号
           /* var result = $(this).form('validate');
            if(result){
                $.ajax({
                    url: '${path}/jsp/product/doProductVersionByName?productName=' + $("#product_editLoanProduct_form #productName").val() + "&startDate=" + getDateStr(new Date($("#startDateId").val()))+"&endDate="+getDateStr(new Date($("#endDateId").val()))+"&versionCode="+$("#versionCode").val()+"&flag=addLoanProduct",
                    async:false,
                    success: function (date) {
                        result=date;
                        if(result == false){
                            $.messager.alert("系统提示",'同一产品在有效时间内只能为一个版本号','info');
                        }
                        *//*$("#product_editLoanProduct_form #versionCode").focus();
                         $.extend($.fn.validatebox.defaults.rules, {
                         validProductVersion: {
                         validator: function (val, param) {
                         if (date == "false") {
                         return true;
                         } else {
                         console.info("666666666666666666");
                         return false;
                         }
                         },
                         message: '同一个产品在同一时期只能有一个版本号'
                         }
                         });
                         $("#product_editLoanProduct_form #versionCode").blur();
                         return date;*//*
                    }
                });
            }*/
            var cycleType = $("#product_addLoanProduct_form #repaymentCycle").combobox('getValue');
            if (cycleType == '<%=LoanProduct.REPAYMENTCYCLE_ONCE%>') {
                $("#product_addLoanProduct_form #cycleValue").val($("#product_addLoanProduct_form #dueTime").val());
            }
            var result = $(this).form('validate');
          
            return result;
        },
        success: function (data) {
            Utils.loaded();
            if (data == "success") {
                $.messager.alert("系统提示", "新增产品成功!", "info");
                parent.$("#toAddLoanProduct").dialog("close");
                parent.$("#product_loanproductList_list").datagrid("reload");
            }else{
                var _data =  eval("("+data+")");
                $.messager.alert("系统提示", _data.info, "info");
            }
        }
    })
</script>
</body>
</html>