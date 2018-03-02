<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<body>

<div class="cf" style="width:900px;margin:0 10px 5px 10px;">
    <fieldset style="height:80px">
        <legend>查询条件</legend>
        <form name="bondSourceUserQuery" id="" action="" class="fitem"
              autocomplete="off">
            <input type="hidden" id="userId"  name="userId" value="${userId}">
            <table>
                <tbody>
                <tr>
                    <td nowrap="nowrap"><label>创建时间：</label>
                    </td>
                    <td>
                        <input id="i_startDate" name="startDate" style="width:100px;" class="easyui-datebox"/>至<input id="i_endDate" style="width:100px;" name="endDate" class="easyui-datebox"/>
                    </td>
                    <td nowrap="nowrap"><label>充值单号：</label>
                    </td>
                    <td>
                        <input id="externalNo" name="externalNo" value="" />
                    </td>
                    <td nowrap="nowrap"><label>支付方式：</label>
                    </td>
                    <td>
                        <select class="easyui-combobox" id="channelCode" style="width:150px" name="channelCode" >
                            <option value="" >全部</option>
                            <option value="0">划扣</option>
                            <option value="1">转账</option>
                            <option value="2" >现金</option>
                        </select>
                    </td>
                    <td nowrap="nowrap">
                        <div style="margin:10px;">
                            <a href="javascript:void(0);" class="easyui-linkbutton"
                               onclick="javascript:toQueryList();" iconCls="icon-search">查询</a>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </fieldset>
</div>

<div id="income" class="container-fluid" style="padding: 5px 0px 0px 10px">

    <table id="income_list${userId}"></table>
</div>

<script language="javascript">
    function toQueryList(){

        $('#income_list${userId}').datagrid('reload', {
            'startDate' : Trim($('#i_startDate').datebox('getValue'),"g"),
            'endDate' : Trim($('#i_endDate').datebox('getValue'),"g"),
            'externalNo' : Trim($("#externalNo").val(),"g"),
            'userId':${userId},
            'channelCode':Trim($("#channelCode").combobox("getValue"),"g")
        });
    }
    /**
     * 执行：列表加载。
     */
    function loadIncomeList(){
        $("#income_list${userId}").datagrid({
            title: '充值列表',
            url: '${ctx}/rechargeOrder/showIncomeList?userId='+'${userId}',
            pagination: true,
            pageSize: 10,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                {field:'rechargeCode', width:240,title:'充值单号' },
                {field:'createTime', width:140,title:'创建时间' ,formatter:dateTimeFormatter},
                {field:'amount', width:100,title:'充值金额'},
                {field:'channelCode', width:60,title:'支付方式',formatter:payMode},
                {field:'externalNo', width:100,title:'划扣单号/转账流水号'},
                {field:'status', width:60,title:'充值结果',formatter:statusFormatter},
                {field:'loginName', width:60,title:'操作人用户名'},
                {field:'desc', width:80,title:'备注'},
            ]],
        });
    }

    function statusFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '1')
            return "充值成功";
        if(val == '2')
            return "充值失败";
    }

    function payMode(val){
        if (val == undefined || val == "")
            return "";
        if(val=='0')
            return "划扣";
        if(val=='1')
            return "转账";
        if(val=='2')
            return "现金";
    }
    //格式化时间
    function dateTimeFormatter(val) {

        if (val == undefined || val == "")
            return "";
        var date;
        if(val instanceof Date){
            date = val;
        }else{
            date = new Date(val);
        }
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();

        var h = date.getHours();
        var mm = date.getMinutes();
        var s = date.getSeconds();

        var dateStr = y + '-' + (m < 10 ? ('0' + m) : m) + '-'
                + (d < 10 ? ('0' + d) : d);
        var TimeStr = h + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
                + (s < 10 ? ('0' + s) : s);

        return dateStr + ' ' + TimeStr;
    }

    $(function(){
        loadIncomeList();
    });
</script>

</body>