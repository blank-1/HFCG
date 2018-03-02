<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<%@include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes" />    
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <meta name="format-detection" content="telephone=no"/>
    <meta name="msapplication-tap-highlight" content="no" />
<title>理财嘉年华</title>
<link rel="stylesheet" href="${ctx }/gamecss/reset.css">
<link href="${ctx }/gamecss/sweepstake.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper" class="wrapper">
    <div class="scroller">
    	<div class="w_header">
    		<img src="${ctx }/gameimg/sudokuSweepstake/p1-img1.jpg" alt="">
    		<p class="w_prompt">
    			<c:if test="${registerFlag eq '0' }">
    				<span style="font-size:1.4rem;">您的手机号<span class="w_copper">${phone }</span>未注册</br>请关注下方二维码注册并投标后即可参与抽奖</span>
    			</c:if>
    			<c:if test="${registerFlag eq '2' }">
    				截止目前您的账户<span class="w_copper">${phone }</span>您尚未投标</br>投资100元即可获得抽奖机会
    			</c:if>
    			<c:if test="${registerFlag eq '1' }">
    				截止目前您的账户<span class="w_copper">${phone }</span></br>在平台累计投标金额<span class="w_copper"><fmt:formatNumber value="${allBuyBalance }" pattern="#,##0"/></span>元</br>
    				目前为您赢得的抽奖次数是<span class="w_frequency">${shareNum }</span>次</br>
    			</c:if>
    			<span class="w_ruleBox">每人最高4次 <span class="w_regulation" id="w_regulation">［查看规则］</span></span>
    		</p>
    		<div class="w_codex">
    			<table border="1" class="w_degree">
			         <tr>
			            <th>平台累计投标额/元</th>
			            <th>抽奖次数</th>
			         </tr>
			         <tr>
			            <td>100（含）-10000</td>
			            <td>1</td>
			         </tr>
			         <tr>
			            <td>10000（含）-100000</td>
			            <td>2</td>
			         </tr>
			         <tr>
			            <td>100000（含）以上</td>
			            <td>3</td>
			         </tr>
		         </table>
		         <p style="font-size: 1.6rem;color: #fff;text-align: center;width: 100%;">转盘开启后，分享可获得一次机会</p>
		         <p class="w_Determine">确定</p>
    		</div>
    	</div>
    	<div id="lottery">
    		<%-- <img src="${ctx }/gameimg/sudokuSweepstake/p1-img6.jpg" alt="" style="width: 100%;"> --%>
    		<img src="${ctx}/gameimg/sudokuSweepstake/game_end.jpg" alt="" style="width: 100%;"> 
    		<table border="0" cellpadding="0" cellspacing="0" style="display: none;">
    			<tbody><tr>
    				<td class="lottery-unit lottery-unit-0"><img src="${ctx }/gameimg/sudokuSweepstake/1.png" dataType=""></td>
    				<td class="lottery-unit lottery-unit-5"><img src="${ctx }/gameimg/sudokuSweepstake/2.png" dataType=""></td>
    				<td class="lottery-unit lottery-unit-4"><img src="${ctx }/gameimg/sudokuSweepstake/4.png" dataType=""></td>
    			</tr>
    			<tr>
    				<td class="lottery-unit lottery-unit-7"><img src="${ctx }/gameimg/sudokuSweepstake/7.png" dataType=""></td>
    				<td class="lottery-unit lottery-unit-1"><img src="${ctx }/gameimg/sudokuSweepstake/6.png" dataType=""></td>
    				<td class="lottery-unit lottery-unit-8"><img src="${ctx }/gameimg/sudokuSweepstake/5.png" dataType=""></td>
    			</tr>
    		    	        <tr>
    				<td class="lottery-unit lottery-unit-6"><img src="${ctx }/gameimg/sudokuSweepstake/3.png" dataType=""></td>
    				<td class="lottery-unit lottery-unit-2"><img src="${ctx }/gameimg/sudokuSweepstake/6.png" dataType=""></td>
    				<td class="lottery-unit lottery-unit-3"><img src="${ctx }/gameimg/sudokuSweepstake/8.png" dataType=""></td>
    			</tr>
    		</tbody>
    		</table>
    		<a href="#" class="btn" style="display: none;"></a>
    	</div>
		<div class="w_timerBox"> 
			<div class="w_carefulFont">
                <p>活动时间：1月1日-3月15日</p>
                <p style="color: #ffd800;">好消息记得和小伙伴分享哦！</p>
                <span><img src="${ctx }/gameimg/sudokuSweepstake/p1-img9.png" alt=""></span>
                <c:if test="${empty shareFlag }">
                	<a href="#" class="w_share" id="w_share">分享</a>
                </c:if>
                <c:if test="${not empty shareFlag }">
                	<a href="#" class="w_share" id="w_share">分享</a>
                </c:if>
                <input type="hidden" name="userToken" id="userToken" value="${userToken }">
            </div>
			<div class="w_Highest">
				<img src="${ctx }/gameimg/sudokuSweepstake/p1-img2.jpg" alt="">
				<ul class="w_HigtList">
					<li>100000元≤累计投资总额≤499900元 </br><span class="w_character">获得300元-1499元（即<span class="w_percentage">投资总额*0.3%</span>的现金奖励）</span></li>
					<li>500000元≤累计投资总额≤999900元 </br><span class="w_character">获得1650元-3299元（即<span class="w_percentage">投资总额*0.33%</span>的现金奖励）</span></li>
					<li>累计投资总额≥100万，获得3500元起 </br><span class="w_character">4500元封顶（即<span class="w_percentage">投资总额*0.35%</span>的现金奖励）</span></li>
				</ul>
			</div>
			<div class="w_consumer">
				<img src="${ctx }/gameimg/sudokuSweepstake/p1-img3.jpg" alt="">
				<div class="w_Various">
					<img src="${ctx }/gameimg/sudokuSweepstake/p1-img4.png" alt="">
					<img src="${ctx }/gameimg/sudokuSweepstake/p1-img5.png" alt="">
				</div>
			</div>
			<div class="w_Remarks">
				<p>
					<span>活动备注</span></br>
					1.大转盘活动时间为1月25日10:00-1月31日！ 截止到1月24日平台累计投标额（已放款）决定您的抽奖次数；累计投标额100元(含)-10000元，1次抽奖机会；累计投标额10000元（含）-100000元，2次抽奖机会；累计投标额100000元（含）以上3次抽奖机会</br>
					2.转盘开启后已投标用户app或微信分享只可多得一次抽奖机会。</br>
					3.首投用户指1.1-3.15期间第一次有投标行为的用户。</br>
					4.本月活动所有统计金额均以满标放款时间在1月1日0点至3月15日24点之前为准；2重礼活动以最高奖励金额计算，只奖励一次，3重礼活动同理。</br>
					5.所有奖励金额以用户投标实际支付金额计算，不包括使用财富券抵扣金额。</br>
					6.所有现金奖励在活动结束后三个工作日内返至财富派账户中。</br>
					7.本活动奖励和平台其他活动同享。</br>
					8.本活动最终解释权归财富派所有。
				</p>
				<span class="w_Irrelevant">本活动与 Apple Inc. 无关</span>
			</div>
			<div class="w_attention">                
                <div class="w_Frame">             
                    <div style="margin-right:20%;"><img src="${ctx }/gameimg/sudokuSweepstake/p1-img8.png" alt=""></div>
                    <div class="w_Line" id="w_Line">
                        <img src="${ctx }/gameimg/sudokuSweepstake/p1-img7.png" alt="">
                        <span class="w_liner"></span>
                    </div>
                </div>
                 <p style="color:#f38da5;margin-top:1rem;">长按二维码识别图关注我们</p>               
            </div>
		</div>
    </div>
</div>		
<div class="markBox" id="markBox"></div>
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script src="${ctx }/gamejs/iscroll.js"></script>
<script src="${ctx }/gamejs/sweepstake.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script src="${ctx }/gamejs/wechat_sweep.js"></script>
<script type="text/javascript">
	var rootPath = '<%=ctx%>';
</script>
</body>
</html>