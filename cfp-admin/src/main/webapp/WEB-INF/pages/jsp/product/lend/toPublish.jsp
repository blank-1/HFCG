<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>

<%@ page import="com.xt.cfp.core.pojo.LendProductPublish" %>
<%@ page import="com.xt.cfp.core.pojo.LendProduct" %>
<%@ page import="com.xt.cfp.core.pojo.LPPublishChannelDetail" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.apache.commons.lang.time.DateFormatUtils" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="product_toPublish_form" method="post">
    <input type="hidden" name="lendProductId" id="lendProductId" value="${lendProduct.lendProductId}"/>
    <input type="hidden" name="productType" id="productType" value="${lendProduct.productType}"/>
    <input type="hidden" name="channelInfo" id="channelInfo"/>
    <input type="hidden" id="channelBalanceType" name="channelBalanceType" value="2"/>

    <div class="control-group">
        <label class="control-label">产品名称<span style="color: red">*</span></label>

        <div class="controls">
            ${lendProduct.productName}

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">发售金额<span style="color: red">*</span></label>

        <div class="controls">
            <select id="publishBalanceType" name="publishBalanceType" style="width: 100px">
                <option value="<%=LendProductPublish.PUBLISHBALANCETYPE_RIGHTS%>">匹配当前债权</option>
                <option value="<%=LendProductPublish.PUBLISHBALANCETYPE_SPEC%>"> 指定金额</option>
            </select>
            <input type="text"
                   class="easyui-numberbox" style="width: 120px;display: none" value="${lendProduct.startsAt}"
                   size="8" data-options="min:${lendProduct.startsAt},precision:0"
                   name="publishBalance" id="publishBalance"/>
            <span id="spanBalanceDesc" style="display: none">${lendProduct.startsAt}起售</span>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">发售期<span style="color: red">*</span></label>

        <div class="controls">
            <select id="publishDateType" style="width: 100px">
                <option value="1">长期有效</option>
                <option value="2">指定</option>
            </select>
            <span id="spanPublishIntervalType" style="display: none">
                <input type="text"
                       class="easyui-datebox" style="width: 100px"
                       name="startDate" id="startDate" value="<%=DateFormatUtils.format(new Date(), "yyyy-MM-dd")%>">-
            <input type="text"
                   class="easyui-datebox"
                   style="width: 100px"
                   name="endDate" id="endDate">
                   <input type="hidden" id="_hidden_date" value="<%=DateFormatUtils.format(new Date(), "yyyy-MM-dd")%>">
            </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">渠道分配<span style="color: red">*</span></label>

        <div class="controls">

            <div class="btn-group" id="theChannel" data-toggle="buttons-radio">
                <button class="btn disabled" id="channelBalanceType_balance"
                        onclick="changeBalanceType('1');return false;">金额
                </button>
                <button class="btn active" id="channelBalanceType_ratio"
                        onclick="changeBalanceType('2');return false;">比例
                </button>
            </div>
            <table id="channel_list">
                <thead>
                <tr>
                    <%--<th data-options="field:'id', checkbox:true"></th>--%>
                    <th data-options="field: 'channel', width: 80,editor: {
                type: 'combobox',
                options: {
                    valueField: 'constantValue',
                    textField: 'constantName',
                    editable:false,
                    required: true,
                    panelHeight:'auto',
                    url: '${ctx}/jsp/product/loan/getConstantByType?typeCode=publishChannel'
                }
            }">渠道
                    </th>
                    <th data-options="field: 'channelId', width: 80,hidden:true">
                    </th>

                    <th data-options="field: 'theValue', width: 80,editor:{
                type: 'numberbox',
                required: true,
                min:0,
                precision:2
            }">金额/比例
                    </th>
                    <th data-options="field: 'unit', width: 50">
                    </th>
                </tr>

                </thead>
            </table>
        </div>
    </div>
    <!-- 省心计划新增功能START  -->
    <c:if test="${lendProduct.productType eq '2'  }">
	<div class="control-group">
        <input type="hidden" value="1" id="recommend" name="recommend">
        <label class="control-label">是否推荐<span style="color: red">*</span></label>

        <div class="controls">
            <input onchange="changeRecommend(this)" type="checkbox" checked="checked"/>
        </div>
        
    </div>
	<div class="control-group">
        <label class="control-label">发布标题<span style="color: red">*</span></label>

        <div class="controls">
	        <input type="text" class="easyui-validatebox validatebox-text" name="publishTitle_sx" id="publishTitle_sx" validtype="length[2,20]" required="true"  >
        </div>
        
    </div>
	</c:if>
	 <!-- 省心计划新增功能END  -->
</form>

<script language="JavaScript">
	function changeRecommend(obj){
		var flag=$(obj).attr("checked");
		if(flag){
			$("#recommend").val(1);
		}else{
			$("#recommend").val(0);
		}
	}
    var editIndex = undefined;
    var sumTheValue;
    $("#product_toPublish_form #channel_list").datagrid({
        singleSelect: true,
        rownumbers: true,
        pagination: false,
        width: 300,
        toolbar: [
            {
                iconCls: 'icon-add',
                text: '添加',
                handler: function () {
                    append();
                }
            },
            {
                iconCls: 'icon-add',
                text: '删除',
                handler: function () {
                    remove();
                }
            }
        ]

    })
    $('#startDate').datebox({
    	editable:false,
        required:false
    });
    $('#endDate').datebox({
    	editable:false,
        required:false
    });    
    $("#product_toPublish_form").form({
    	
        url: '${ctx}/jsp/product/lend/saveProductPublish',
        onSubmit: function () {
            //endEditing();
            $('#channel_list').datagrid("acceptChanges");
            var rows = $("#product_toPublish_form #channel_list").datagrid("getRows");
            var sumTheValue = 0;
            $.each(rows, function (key, value) {
                sumTheValue += value.theValue * 1;
            });
            
            if($("#publishDateType").combobox('getValue')=='2'){
	            var _hiddenDate = parseDate($('#_hidden_date').val());
	            var startDate = parseDate($('#startDate').datebox('getValue'));
	            var endDate = parseDate($('#endDate').datebox('getValue'));
	            if(!(_hiddenDate <= startDate)){
	            	$.messager.alert("系统提示", "起始日期必须大于系统日期["+$('#_hidden_date').val()+"]!", "info");
	            	return false;
	            }  
	            
	            if(!(startDate <= endDate)){
	            	$.messager.alert("系统提示", "截止日期必须大于起始日期!", "info");
	            	return false;
	            }   
	        }    
	                     
	            
	            if ($('#channelBalanceType').val() == '2') {
	                if (sumTheValue / 100 < 1) {
	                    $.messager.alert("系统提示", "所有渠道比例之和必须为100%!", "info");
	                    beginEdit();
	                    return false;
	                }
	            } else {
	                if ($("#publishBalanceType").combobox('getValue') == '<%=LendProductPublish.PUBLISHBALANCETYPE_SPEC%>') {
	                    if (sumTheValue * 1 - $("#publishBalance").numberbox("getValue") * 1 != 0) {
	                        $.messager.alert("系统提示", "所有渠道金额之和必须为指定发布金额!", "info");
	                        beginEdit();
	                        return false;
	                    }
	                }
	            }

            $('#channel_list').datagrid("acceptChanges");
            $("#channelInfo").val(JSON.stringify($("#product_toPublish_form #channel_list").datagrid("getData")));
            //return false;
            //alert($("#product_toPublish_form").serialize());
        },
        success: function (data) {
            if (data == "success") {
                $.messager.alert("系统提示", "出借产品发布成功!", "info");
                $("#toPublish").dialog('close');
                $("#product_publishList_list").datagrid('reload');
            }
        }

    })

    $("#publishBalanceType").combobox({
        onSelect: function (rec) {
            var rows = $("#channel_list").datagrid("getRows");
            if (rec.value == '<%=LendProductPublish.PUBLISHBALANCETYPE_RIGHTS%>') {
                $("#publishBalance").hide();
                $("#spanBalanceDesc").hide();
                $("#channelBalanceType_balance").addClass("disabled");
                $("#channelBalanceType_ratio").addClass("active");
                $('#channelBalanceType').val('2');

                $.each(rows, function (key, value) {
                    var rowIndex = $('#channel_list').datagrid('getRowIndex', value);
                    $('#channel_list').datagrid('updateRow',{
                        index: rowIndex,
                        row: {
                            unit: '%'
                        }
                    });
                });
                changeBalanceType('2');

            } else if (rec.value == '<%=LendProductPublish.PUBLISHBALANCETYPE_SPEC%>') {
                if ('${lendProduct.productType}' == '<%=LendProduct.PRODUCTTYPE_RIGHTS%>') {
                    $.messager.alert("系统提示", "债权类的出借产品，不能指定金额为发售金额！", "info");
                    $("#publishBalanceType").combobox("setValue", '<%=LendProductPublish.PUBLISHBALANCETYPE_RIGHTS%>');
                    return false;
                }
                $("#publishBalance").show();
                $("#spanBalanceDesc").show();
                $("#channelBalanceType_balance").removeClass("disabled");
                $("#channelBalanceType_balance").addClass("active");
                $("#channelBalanceType_ratio").removeClass("active");
                $('#channelBalanceType').val('1');

                $.each(rows, function (key, value) {
                    var rowIndex = $('#channel_list').datagrid('getRowIndex', value);
                    $('#channel_list').datagrid('updateRow',{
                        index: rowIndex,
                        row: {
                            unit: '元'
                        }
                    });
                });
                changeBalanceType('1');
            }
        }

    })
    $("#publishDateType").combobox({
        onSelect: function (rec) {
            if (rec.value == '1') {
                $("#spanPublishIntervalType").hide();
            } else if (rec.value == '2') {
                $("#spanPublishIntervalType").show();
            }
        }
    })

    function changeBalanceType(balanceType) {
        $('#channelBalanceType').val(balanceType);
        $('#channel_list').datagrid("acceptChanges");
        var rows = $("#channel_list").datagrid("getRows");
        if (balanceType == '<%=LPPublishChannelDetail.CHANNELBALANCETYPE_BALANCE%>') {
            $.each(rows, function (key, value) {
                var rowIndex = $('#channel_list').datagrid('getRowIndex', value);
                $('#channel_list').datagrid('updateRow',{
                    index: rowIndex,
                    row: {
                        unit: '元'
                    }
                });
            });
        } else {
            $.each(rows, function (key, value) {
                var rowIndex = $('#channel_list').datagrid('getRowIndex', value);
                $('#channel_list').datagrid('updateRow',{
                    index: rowIndex,
                    row: {
                        unit: '%'
                    }
                });
            });

        }
        beginEdit();

    }

    function beginEdit(){
        var rows = $('#channel_list').datagrid("getRows");
        $.each(rows,function(key,value){
            var therowIndex = $('#channel_list').datagrid("getRowIndex", value);
            $('#channel_list').datagrid('selectRow', therowIndex).datagrid('beginEdit', therowIndex);
        })
    }

    function append() {
//        if (endEditing()) {
        var channelType = $('#channelBalanceType').val();
        var unitstr = "";
        if (channelType == '1') {
            unitstr = "元";
        } else {
            unitstr = "%";
        }
        $('#channel_list').datagrid('appendRow', {
            channel: 1,
            theValue: "0",
            unit: unitstr
        });
        editIndex = $('#channel_list').datagrid('getRows').length - 1;
        $('#channel_list').datagrid('selectRow', editIndex)
                .datagrid('beginEdit', editIndex);
//        }
    }

    function remove() {
        var selRow = $("#channel_list").datagrid("getSelected");
        if (selRow) {
            $.messager.confirm("系统提示", "是否确定删除该渠道？", function (data) {
                if (data) {
                    $("#channel_list").datagrid("deleteRow",
                            $("#channel_list").datagrid("getRowIndex", selRow));
                }
            })
        } else {
            $.messager.alert("系统提示", "请选择要删除的渠道!", "info");
        }
    }

    function endEditing() {
        if (editIndex == undefined) {
            return true;
        }
        if ($('#channel_list').datagrid('validateRow', editIndex)) {
            var ed = $('#channel_list').datagrid('getEditor', {index: editIndex, field: 'channel'});
            var channel = $(ed.target).combobox('getValue');
            $('#channel_list').datagrid('getRows')[editIndex]['channel'] = $(ed.target).combobox('getText');
            //alert($('#channel_list').datagrid('getRows')[editIndex]['channel']);
            $('#channel_list').datagrid('getRows')[editIndex]['channelId'] = channel;

            return true;
        } else {
            return false;
        }
    }

    function parseDate(dateTime) {
        return _parseDate(dateTime);
    }

    function _parseDate(dateTime) {
        var arr = dateTime.split("-");
        var date = new Date(arr[0], arr[1], arr[2]);
        var times = date.getTime();
        return times;
    }
</script>
</body>
</html>
