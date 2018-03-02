<%@ page import="com.xt.cfp.core.constants.LoanApplicationStateEnum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<head>
</head>
<%
    LoanApplicationStateEnum [] stateList = LoanApplicationStateEnum.values();
    request.setAttribute("stateList",stateList);
%>
<body>

<div class="cf" style="width:900px;margin:0 10px 5px 10px;">
    <fieldset style="height:100px">
        <legend>查询条件</legend>
        <form name="bondSourceUserQuery" id="" action="" class="fitem"
              autocomplete="off">
            <input type="hidden" id="bondSourceId"  name="bondSourceId" value="${bondSourceId}">
            <table>
                <tbody>
                <tr>
                    <td nowrap="nowrap"><label>借款标题：</label>
                    </td>
                    <td align="center"><input id="title"
                                              name="title" value=""
                                              type="text" />
                    </td>
                    <td nowrap="nowrap"><label>借款人用户名：</label>
                    </td>
                    <td><input id="loginName"
                               name="loginName" value=""
                               type="text" />
                    </td>
                    <td nowrap="nowrap"><label>借款人姓名：</label>
                    </td>
                    <td>
                        <input id="realName"
                               name="realName" value=""
                               type="text" />
                    </td>
                    <td nowrap="nowrap"><label>原始债权人用户名：</label>
                    </td>
                    <td>
                        <input id="bondUserLoginName"
                               name="bondUserLoginName" value=""
                               type="text" />
                    </td>
                    </tr><tr>
                    <td nowrap="nowrap"><label>原始债权人姓名：</label>
                    </td>
                    <td>
                        <input id="bondUserRealName"
                               name="bondUserRealName" value=""
                               type="text" />
                    </td>
                    <td nowrap="nowrap"><label>借款状态：</label>
                    </td>
                    <td>
                        <select id="state" class="easyui-combobox">
                            <option value="">请选择</option>
                            <c:forEach items="${stateList}" var="state">
                                <option value="${state.value}">${state.desc}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td nowrap="nowrap">
                        <div style="margin:10px;">
                            <a href="javascript:void(0);" class="easyui-linkbutton"
                               onclick="javascript:toQueryBorrowList();" iconCls="icon-search">查询</a>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </fieldset>
</div>

<div id="borrow" class="container-fluid" style="padding: 5px 0px 0px 10px">

    <table id="borrow_list${bondSourceId}"></table>
</div>

<script language="javascript">
    function toQueryBorrowList(){

        $('#borrow_list${bondSourceId}').datagrid('reload', {
            'title' : Trim($('#title').val(),"g"),
            'loginName' : Trim($('#loginName').val(),"g"),
            'realName' : Trim($("#realName").val(),"g"),
            'bondUserLoginName' : Trim($("#bondUserLoginName").val(),"g"),
            'bondUserRealName' : Trim($("#bondUserRealName").val(),"g"),
            'realName' : Trim($("#realName").val(),"g"),
            'channelId':'${bondSourceId}',
            'applicationState':Trim($("#state").combobox("getValue"),"g")
        });
    }
    /**
     * 执行：列表加载。
     */
    function loadBorrowList(){
        $("#borrow_list${bondSourceId}").datagrid({
            title: '借款列表',
            url: '${ctx}/jsp/loanManage/loan/showBorrowList?bondSourceId='+'${bondSourceId}',
            pagination: true,
            pageSize: 10,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                {field:'loanApplicationId', width:40,title:'借款ID' },
                {field:'loanTypeStr', width:60,title:'借款类型' },
                {field:'title', width:240,title:'借款标题' },
                {field:'loginName', width:100,title:'借款人用户名' },
                {field:'realName', width:60,title:'借款人姓名' },
                {field:'confirmBalance', width:60,title:'借款金额' },
                {field:'loanlimit', width:60,title:'借款期限' },
                {field:'currentRepaymentCount', width:60,title:'当前期数' },
                {field:'shouldCapital', width:60,title:'待还本金' },
                {field:'shouldInterest', width:60,title:'待还利息' },
                {field:'shouldFee', width:60,title:'代缴费用' },
                {field:'shouldFaxi', width:60,title:'代缴罚息' },
                {field:'shouldAll', width:60,title:'代还总额' },
                {field:'bondUserRealName', width:60,title:'原始债权人姓名' },
                {field:'bondUserLoginName', width:80,title:'原始债权人用户名' },
                {field:'statusStr', width:60,title:'借款状态' },
                {field:'action', width:60,title:'操作' ,
                    formatter:function(value,row,index){

                        var result = "";
                        if(row.applicationState == '6')
                            result += "<a class='label label-info' onclick='toRepayment(" + row.loanApplicationId + ")'>还款</a> &nbsp;";
                        else
                            result += "";
                        return result;
                    }
                },
            ]],
        });
    }


    function toRepayment(loanApplicationId){
        var url = '${ctx}/jsp/financial/toRepaymentList?loanApplicationId='+loanApplicationId;
        window.open(url,'_blank',"top=0,left=0,width=" + document.body.clientWidth + ",height=" + (document.body.clientHeight+100));
    }

    function statusFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '1')
            return "充值成功";
        if(val == '2')
            return "充值失败";
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
        loadBorrowList();
    });
</script>

</body>