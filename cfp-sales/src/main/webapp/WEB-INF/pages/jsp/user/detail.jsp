<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>

</head>
<body>
<div id="addBondSource" class="container-fluid" style="padding: 5px 0px 0px 10px">

	<form action="${ctx}/phoneSell/prepaid/prepaidList" method="post" id="more_form" target="_blank">
		<input type="hidden" id="adminCode" name="adminCode" value="${adminCode}">
	</form>

    <div>
        <div><b>基本信息：</b></div>
        <div >
            <table align="center" border="0" width="100%" >
                <tr>
                    <td style="font-size: 12px;">用户名：</td>
                    <td style="font-size: 12px;">${userInfo.loginName}</td>
                    <td style="font-size: 12px;">用户手机：</td>
                    <td style="font-size: 12px; position: relative;">
                        ${userInfo.mobileNo}
                            <%-- <mis:PermisTag code="010004">
                                <a id = "s1" href="#" >查看手机号</a>
                            </mis:PermisTag> --%>
                        <span id="s_mobile" style="position:absolute;top: -15px; left: 89px;border: 1px solid black;display: none;"> ${userInfo.mobileNo}</span>
                    </td> 
                    <td style="font-size: 12px;">用户身份证号：</td>
                    <td style="font-size: 12px;">
                        ${userInfoExt.encryptIdCardNo}
                        <c:if test="${not empty userInfoExt.encryptIdCardNo}">

                            <mis:PermisTag code="010005">
                                <a href="#"  id = "s2" >查看身份证号</a>
                            </mis:PermisTag>
                            <span id="idCard" style="position: absolute; top: 45px; right: 89px; border: 1px solid black; display: none;">${userInfoExt.idCard}</span>

                            <script type="text/javascript">
                                $(function(){
                                    $("#s2").click(function(){
                                        $("#idCard").show();
                                    });
                                    $("#s2").hover(function(){
                                        $("#idCard").hide();
                                    })
                                });
                            </script>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td style="font-size: 12px;">注册时间：</td>
                    <td style="font-size: 12px;"><fmt:formatDate value="${userInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td style="font-size: 12px;">最后登陆时间：</td>
                    <td style="font-size: 12px;"><fmt:formatDate value="${userInfo.lastLoginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td style="font-size: 12px;">&nbsp;</td>
                    <td style="font-size: 12px;">&nbsp;</td>
                </tr>
            </table>
        </div>
    </div>

    <div>
        <div><b>账户信息：</b></div>
        <div>
            <table align="center" border="0" width="100%" >
                <tr>
                    <td style="font-size: 12px;">充值总额：</td>
                    <td style="font-size: 12px;"><fmt:formatNumber value="${allRechargeValue}" pattern="#,##0.00"/>元</td>
                    <td style="font-size: 12px;">提现总额：</td>
                    <td style="font-size: 12px;"><fmt:formatNumber value="${allWithDrawValue}" pattern="#,##0.00"/>元</td>
                    <td style="font-size: 12px;">投资总额：</td>
                    <td style="font-size: 12px;"><fmt:formatNumber value="${totalLendAmount}" pattern="#,##0.00"/>元</td>
                </tr>
                <tr>
                    <td style="font-size: 12px;">可用金额：</td>
                    <td style="font-size: 12px;"><fmt:formatNumber value="${cashAccount.availValue2}" pattern="#,##0.00"/>元</td>
                    <td style="font-size: 12px;">冻结金额：</td>
                    <td style="font-size: 12px;"><fmt:formatNumber value="${cashAccount.frozeValue2}" pattern="#,##0.00"/>元</td>
                    <td style="font-size: 12px;">投资收益：</td>
                    <td style="font-size: 12px;"><fmt:formatNumber value="${allProfit}" pattern="#,##0.00"/>元</td>
                </tr>
            </table>


            <div id="container" style="min-width:700px;height:400px"></div>

        </div>
    </div>
	
	<div style="margin-top: 20px;">
        <div><b>充值记录：</b></div>
        <div>
            <table align="center" border="1" width="100%" >
                <tr>
                    <th style="font-size: 12px;">充值时间</th>
                    <th style="font-size: 12px;">充值金额(元)</th>
                    <th style="font-size: 12px;">充值卡</th>
                    <th style="font-size: 12px;">充值状态</th>
                </tr>
                <c:forEach items="${prepaids}" var="vo">
                    <tr>
                        <td style="font-size: 12px;">${vo.time}</td>
                        <td style="font-size: 12px;">${vo.amount}</td>
                        <td style="font-size: 12px;">${vo.card}</td>
                        <td style="font-size: 12px;">${vo.status}</td>

                    </tr>
                </c:forEach>

            </table>
        </div>
        <div style="float: right;"><a style="cursor: pointer;" onclick="toMore(1)">>>更多</a></div>
    </div>
    
    <form action="${ctx}/phoneSell/order/to_lendorder_detail" method="post" id="orderDetail_form" target="_blank">
		<input type="hidden" id="lendOrderId" name="lendOrderId">
	</form>
    
    <script type="text/javascript">
	    function todetail(val){
			$("#lendOrderId").val(val);
			$("#orderDetail_form").submit();
		}
		
		function toMore(type){
			if(type==2){
				$("#more_form").attr("action","${ctx}/phoneSell/order/orderList");
			}else if(type==3){
				$("#more_form").attr("action","${ctx}/phoneSell/withdraw/withdrawList");
			}else{
				$("#more_form").attr("action","${ctx}/phoneSell/prepaid/prepaidList");
			}
			$("#more_form").submit();
		}
    </script>
    
    <div>
        <div><b>投资记录：</b></div>
        <div>
            <table align="center" border="1" width="100%" >
                <tr>
                    <th style="font-size: 12px;">订单号</th>
                    <th style="font-size: 12px;">购买债权/产品</th>
                    <th style="font-size: 12px;">购买时间</th>
                    <th style="font-size: 12px;">购买金额（元）</th>
                    <th style="font-size: 12px;">订单状态</th>
                    <th style="font-size: 12px;">操作</th>
                </tr>
                <c:forEach items="${orders}" var="vo">
                    <tr>
                        <td style="font-size: 12px;">${vo.orderNO}</td>
                        <td style="font-size: 12px;">${vo.productName}</td>
                        <td style="font-size: 12px;">${vo.time}</td>
                        <td style="font-size: 12px;">${vo.amount}</td>
						<td style="font-size: 12px;">${vo.state}</td>
						<td style="font-size: 12px;"><a style="cursor: pointer;" onclick="todetail('${vo.orderId}')">明细</a></td>
                    </tr>
                </c:forEach>

            </table>
        </div>
        <div style="float: right;"><a style="cursor: pointer;" onclick="toMore(2)">>>更多</a></div>
    </div>
    
    <div style="margin-top: 20px;">
        <div><b>提现记录：</b></div>
        <div>
            <table align="center" border="1" width="100%" >
                <tr>
                    <th style="font-size: 12px;">提现时间</th>
                    <th style="font-size: 12px;">提现金额（元）</th>
                    <th style="font-size: 12px;">提现卡</th>
                    <th style="font-size: 12px;">提现状态</th>
                </tr>
                <c:forEach items="${withdraws}" var="vo">
                    <tr>
                        <td style="font-size: 12px;">${vo.time}</td>
                        <td style="font-size: 12px;">${vo.amount}</td>
                        <td style="font-size: 12px;">${vo.card}</td>
                        <td style="font-size: 12px;">${vo.status}</td>
                    </tr>
                </c:forEach>

            </table>
        </div>
        <div style="float: right;"><a style="cursor: pointer;" onclick="toMore(3)">>>更多</a></div>
    </div>
	
    <div style="margin-top: 20px;">
        <div><b>变更记录：</b></div>
        <div>
            <table align="center" border="1" width="100%" >
                <tr>
                    <th style="font-size: 12px;">客服工号</th>
                    <th style="font-size: 12px;">客服姓名</th>
                    <th style="font-size: 12px;">开始时间</th>
                    <th style="font-size: 12px;">结束时间</th>
                </tr>
                <c:forEach items="${recordList}" var="vo">
                    <tr>
                        <td style="font-size: 12px;">${vo.adminCode}</td>
                        <td style="font-size: 12px;">${vo.displayName}</td>
                        <td style="font-size: 12px;"><fmt:formatDate value="${vo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td style="font-size: 12px;"><fmt:formatDate value="${vo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

                    </tr>
                </c:forEach>

            </table>
        </div>
    </div>

    <script type="text/javascript">

        $(function () {
            $('#container').highcharts({
                chart: {
                    type: 'column'
                },
                xAxis: {
                    categories: ${chartData[0]}
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: '金额 (元)'
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table width="200">',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f} 元</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },exporting: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: ${chartData[1]}

        });
    });

    </script>
</div>
</body>
</html>

