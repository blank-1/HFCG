<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="/WEB-INF/pages/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <title>关于我们 - 财富派</title>
    <script type="text/javascript" src="${ctx }/js/jquery.min.js"></script>
</head>

<body>
<fieldset><legend>充值</legend>
    <form action="/api/recharge/doRecharge" method="post" id="FM">

        银行号：<input type="text" name="iss_ins_cd" value="0803090000"/><br/>
        充值方式：<input type="text" name="order_pay_type" value="B2C"/>（B2C =个人| B2B 企业）<br/>
        钱：<input type="text" name="amt" value=""/><br/>
        用户登录名：<input type="text" name="login_id" value=""/>
        <button type="submit" class="recharge">充值</button>
    </form>
</fieldset>
<fieldset><legend>提现</legend>
    <form action="${ctx}/api/recharge/doWithdraw" method="post" id="FM_w">

        钱：<input type="text" name="amt" value=""/><br/>
        用户登录名：<input type="text" name="login_id" value=""/>
        <button type="submit">提现</button>
    </form>
</fieldset>

<fieldset><legend>密码重置</legend>
    <form action="${ctx}/api/recharge/doResetPassword" method="post" id="FM_reset">

        重置项：<input type="text" name="busi_tp" value=""/>（1:重置登录密码, 2:修改登录密码, 3:支付密码重置）<br/>
        用户登录名：<input type="text" name="login_id" value=""/>
        <button type="submit">重置密码</button>
    </form>
</fieldset>


<fieldset><legend>查询</legend>
    <form action="${ctx}/api/query/doQuery" method="post" id="FM_qy">

        交易日期：<input type="text" name="mchnt_txn_dt" value="${now}"/><br/>
        用户登录名：<input type="text" name="cust_no" style="width: 500px" value="15701612615"/>
        <button type="button" class="query-info">查询</button>
    </form>
    <hr />
    <div id="dataList"></div>
</fieldset>
<div style="clear:both;"></div>
<script type="text/javascript">
    $(function (){

        $('.query-info').on('click',function (){
            $.ajax({
                url:'${ctx}/api/query/doQuery'
                ,type:'post'
                ,data:$('#FM_qy').serialize()
                ,dataType:'json'
            }).done(function (o){
                var html = '';
                if(o.success){
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
