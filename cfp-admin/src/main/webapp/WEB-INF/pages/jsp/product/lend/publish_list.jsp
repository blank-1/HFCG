<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.LendProductPublish" %>
<%@ page import="com.xt.cfp.core.pojo.LPPublishChannelDetail" %>
<%@ page import="com.xt.cfp.core.constants.LendProductPublishStateEnum" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="product_publishList" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <input type="button" class="btn btn-primary" value="发布产品" onclick="toPublish()">
    <table id="product_publishList_list">
        <thead>
        <th data-options="field:'lendProductPublishId', checkbox:true"></th>
        <th data-options="field:'publishName', width:180">产品名称</th>
        <th data-options="field:'publishCode',width:100">期号</th>
        <th data-options="field:'publishBalance',width:100">发售量</th>
        <th data-options="field:'soldBalance',width:160,align:'right'">已售金额</th>
        <th data-options="field:'startEnd',width:160">有效期</th>
        <th data-options="field:'publishState',width:160">状态</th>
        <th data-options="field:'channelInfo',width:360">渠道分配</th>
        </thead>
    </table>
</div>
</body>
<script language="JavaScript">
    var url = "${ctx}/jsp/product/loan/getConstantDefines";
    var cDefines = {};
    $.ajax({
        async: true,
        url: url,
        success: function (data) {
            cDefines = data;
        }
    })
    $("#product_publishList_list").datagrid({
        idField: 'lendProductPublishId',
        title: '产品发布列表',
        url: '${ctx}/jsp/product/lend/publishForPage?lendProductId=${lendProductId}',
        pagination: true,
        pageSize: 20,
        width: document.body.clientWidth * 0.97,
        singleSelect: true,
        rownumbers: true,
        onBeforeLoad: function (value, rec) {
            var startEnd = $(this).datagrid("getColumnOption", "startEnd");
            if (startEnd) {
                startEnd.formatter = function (value, rowData, rowIndex) {
                    if (rowData.startDate && rowData.endDate) {
                        return getDateStr(new Date(rowData.startDate)) + "~" + getDateStr(new Date(rowData.endDate));
                    } else {
                        return "长期有效";
                    }
                }
            }

            var publishBalance = $(this).datagrid("getColumnOption", "publishBalance");
            if (publishBalance) {
                publishBalance.formatter = function (value, rowData, rowIndex) {
                    if (rowData.publishBalanceType == '<%=LendProductPublish.PUBLISHBALANCETYPE_SPEC%>') {
                        return value + "元";
                    } else {
                        return "匹配当前债权";
                    }
                }
            }
             var soldBalance = $(this).datagrid("getColumnOption", "soldBalance");
            if (soldBalance) {
                soldBalance.formatter = function (value, rowData, rowIndex) {
                    return value + "元";
                }
            }
            var channelInfo = $(this).datagrid("getColumnOption", "channelInfo");
            if (channelInfo) {
                channelInfo.formatter = function (value, rowData, rowIndex) {
                    var channels = rowData.publishChannelDetails;
                    var str ="";
                    $.each(channels,function(key,channelValue) {
                        $.each(cDefines, function (cDefineKey, cDefineValue) {
                            if (cDefineValue.constantTypeCode == 'publishChannel') {
                                if (cDefineValue.constantValue == channelValue.channel) {
                                    str += cDefineValue.constantName + ":";
                                }

                            }
                        })
                        if (channelValue.publishBalanceType == '<%=LPPublishChannelDetail.CHANNELBALANCETYPE_RATIO%>') {
                            str+= (channelValue.publishBalance*100).toFixed(2) + "%";
                        } else {
                            str+= channelValue.publishBalance;
                        }
                        str +="&nbsp;&nbsp;";
                    });
                    return str;
                }
            }
            var publishState = $(this).datagrid("getColumnOption", "publishState");
            if (publishState) {
                publishState.formatter = function (value, rowData, rowIndex) {
                    switch (value) {
                        case '<%=LendProductPublishStateEnum.FORCESTOP.value2Char()%>' :
                            return "手动下线";
                            break;
                        case '<%=LendProductPublishStateEnum.TIMEOUT.value2Char()%>' :
                            return "已过期";
                            break;
                        case '<%=LendProductPublishStateEnum.PAUSE.value2Char()%>' :
                            return "暂停";
                            break;
                        case '<%=LendProductPublishStateEnum.SELLING.value2Char()%>' :
                            return "发售中";
                            break;
                        case '<%=LendProductPublishStateEnum.SOLDOUT.value2Char()%>' :
                            return "已售罄";
                            break;
                        case '<%=LendProductPublishStateEnum.WAITING.value2Char()%>' :
                            return "未开始";
                            break;

                    }
                }
            }
        }
    })

    function toPublish() {
        var lendProductId = ${lendProductId};
        $("#product_publishList").after("<div id='toPublish' style=' padding:10px; '></div>");
        $("#toPublish").dialog({
            resizable: false,
            title: '发布省心计划',
            href: '${ctx}/jsp/product/lend/toPublish?lendProductId=' + lendProductId,
            width: 600,
            modal: true,
            height: 400,
            top: 100,
            left: 400,
            buttons: [
                {
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                    	 var vald = $("#product_toPublish_form").form('validate');
                         if(!vald){
                        	 return false;
                         }
                        $("#toPublish").contents().find("#product_toPublish_form").submit();

                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#toPublish").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }
</script>
</html>
