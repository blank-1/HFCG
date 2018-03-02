<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="/WEB-INF/pages/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">  -->
    <title>关于我们 - 财富派</title>
    <script type="text/javascript" src="${ctx }/js/jquery.min.js"></script>
</head>

<body>
<div>
    <form action="/api/query/doQuery" method="post" id="FM">

        交易日期：<input type="text" name="mchnt_txn_dt" value="${now}"/><br/>
        用户登录名：<input type="text" name="cust_no" style="width: 500px" value="15701612615"/>
        <button type="button">Go</button>
    </form>
    <hr />
    <div id="dataList"></div>
</div>
<div style="clear:both;"></div>
<script type="text/javascript">
    $(function (){

        $('[type="button"]').on('click',function (){
            $.ajax({
                url:'/api/query/doQuery'
                ,type:'post'
                ,data:$('#FM').serialize()
                ,dataType:'json'
            }).done(function (o){
                var html = '';
                if(o.resp_code == '0000'){
                    for(var index in o.results){
                        var ele = o.results[index];
                        html += 'user_id: '+ele.user_id+" | 账面总余额: "+ele.ct_balance + " | 可用余额: "+ele.ca_balance+ " | 冻结余额: "+ele.cf_balance+" | 未转结余额: "+ele.cu_balance+"<br />";
                    }
                }else{
                    html = o.resp_desc;
                }


                $("#dataList").html(html);
            });
        });
    });

</script>
</body>
</html>
