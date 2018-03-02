<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<body>

<div class="cf" style=" width:1200px;margin:0 10px 5px 10px;">
    <fieldset style="height:80px">
        <legend>查询条件</legend>
        <form name="bondSourceUserQuery" action="${ctx}/bondSource/bondSourceUserlist" class="fitem"
              autocomplete="off">
            <table>
                <tbody>
                <tr>
                    <td nowrap="nowrap"><label>提交时间：</label>
                    </td>
                    <td>
                        <input id="w_startDate" name="startDate" style="width:100px;" class="easyui-datebox"/>至<input id="w_endDate" style="width:100px;" name="endDate" class="easyui-datebox"/>
                    </td>
                    <td nowrap="nowrap"><label>提现结果：</label>
                    </td>
                    <td>
                        <select class="easyui-combobox" id="withDrawStatus" style="width:150px" name="withDrawStatus" >
                            <option value="" >全部</option>
                            <option value="0">待审核</option>
                            <option value="1">审核通过</option>
                            <option value="2" >驳回</option>
                        </select>
                    </td>
                    <td nowrap="nowrap"><label>打款结果：</label>
                    </td>
                    <td>
                        <select class="easyui-combobox" id="tranStatus" style="width:150px" name="tranStatus" >
                            <option value="" >全部</option>
                            <option value="0" >未打款</option>
                            <option value="1" >未提交</option>
                            <option value="2" >处理中</option>
                            <option value="3" >打款成功</option>
                            <option value="4" >打款失败</option>
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

<div id="withDraw" class="container-fluid" style="padding: 5px 0px 0px 10px">

    <table id="withDraw_list${userId}"></table>
</div>

<script language="javascript">
    function toQueryList(){
        $('#withDraw_list${userId}').datagrid('reload', {
            'startDate' : Trim($('#w_startDate').datebox('getValue'),"g"),
            'endDate' : Trim($('#w_endDate').datebox('getValue'),"g"),
            'userId':${userId},
            'verifyStatus':Trim($("#withDrawStatus").combobox("getValue"),"g"),
            'transStatus':Trim($("#tranStatus").combobox("getValue"),"g")
        });
    }
    /**
     * 执行：列表加载。
     */
    function loadWithDrawList(){
        $("#withDraw_list${userId}").datagrid({
            title: '提现列表',
            url: '${ctx}/withdraw/showWithDrawList?userId='+'${userId}',
            pagination: true,
            pageSize: 10,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                {field:'withdrawId', width:60,title:'提现单号' },
                {field:'createTime', width:140,title:'创建时间' ,formatter:dateTimeFormatter},
                {field:'withdrawAmount', width:100,title:'提现金额'},
                {field:'bankName', width:60,title:'开户行'},
                {field:'cardNo', width:100,title:'银行卡号'},
                {field:'verifyStatus', width:60,title:'审核状态' ,formatter:statusFormatter},
                {field:'transStatus', width:60,title:'打款状态' ,formatter:transStatusFormatter},
                {field:'operateName', width:60,title:'操作人用户名'},
                {field:'remark', width:80,title:'备注'},
            ]],
        });
    }

    function transStatusFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '0')
            return "未打款";
        if(val == '1')
            return "未提交";
        if(val == '2')
            return "处理中";
        if(val == '3')
            return "打款成功";
        if(val == '4')
            return "打款失败";
    }

    function statusFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '0')
            return "待审核";
        if(val == '1')
            return "审核通过";
        if(val == '2')
            return "驳回";
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
        loadWithDrawList();
    });
</script>

</body>