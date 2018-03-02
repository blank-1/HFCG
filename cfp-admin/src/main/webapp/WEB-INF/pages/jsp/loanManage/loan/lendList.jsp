<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<head>
</head>
<body>

<div class="cf" style="width:900px;margin:0 10px 5px 10px;">
</div>

<div id="lender" class="container-fluid" style="padding: 5px 0px 0px 10px">

    <table id="lender_list"></table>
</div>

<script language="javascript">
    /**
     * 执行：列表加载。
     */
    function loadLenderList(){
        $("#lender_list").datagrid({
            title: '出借人列表',
            url: '${ctx}/jsp/loanManage/loan/getLender?loanApplicationId=${loanApplicationId}',
            pagination: true,
            pageSize: 10,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                {field:'lenderLoginName', width:100,title:'出借人用户名' },
                {field:'lenderRealName', width:100,title:'出借人姓名'},
                {field:'lendTime', width:100,title:'出借时间',formatter:dateTimeFormatter},
                {field:'completeTime', width:100,title:'结束时间',formatter:dateTimeFormatter},
                {field:'buyPrice', width:100,title:'出借金额'},
                {field:'factBalance', width:60,title:'已还本金'},
                {field:'waitTotalpayMent', width:60,title:'未还本金'},
                {field:'rightState', width:80,title:'状态',formatter:statusFormatter},
            ]],
        });
    }
    //格式化状态
    function statusFormatter(val){
        if(val==null){
            return "";
        }else{
            <c:forEach items="${creditorRightStats}" var="status">
                if(val=='${status.value}') return '${status.desc}';
            </c:forEach>
        }
    }

    //格式化时间
    function dateTimeFormatter(val) {

        if (val == undefined || val == "")
            return "待放款";
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
        loadLenderList();
    });
</script>

</body>