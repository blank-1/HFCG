<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
  	<meta charset="utf-8" />
  	<title>出借债权 - 财富派</title>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="renderer" content="webkit|ie-comp|ie-stand"></meta>
</head>
<style>
html, body, div, div, object, iframe,h1, h2, h3, h4, h5, h6, p, blockquote, pre,abbr, address, cite, code,del, dfn, em, img, ins, kbd, q, samp,small, strong, sub, sup, var,b, i,dl, dt, dd, ol, ul, li,fieldset, form, label, legend,table, caption, tbody, tfoot, thead, tr, th, td,article, aside, canvas, details, figcaption, figure, footer, header, hgroup, menu, nav, section, summary,time, mark, audio, video{margin:0;padding:0;border:0; outline:0;vertical-align:baseline;font-style: normal;}
html,ul,li,ol,p,h1,h2,h3,h4,h5,h6,dl,dt,dd{margin:0;padding:0;}
html{font-family:Tahoma,Arial,Roboto,"Droid Sans","Helvetica Neue","Droid Sans Fallback","Heiti SC",sans-self,Microsoft YaHei;box-sizing:border-box;-webkit-font-smoothing:antialiased;text-shadow:1px 1px 1px rgba(0,0,0,0.004);}
li{list-style:none;	}
a{text-decoration:none; }
img{border:none;}
html{width: 100%;height: 100%;background: #afafaf;}
body{width: 1200px;height: 100%;margin: 0 auto;background: #fff;color: #666;font-size: 12px;font-family:Microsoft YaHei; }
.userInfo{width: 90%;color: #666;height: 12px;font-size: 20px;padding: 10px 0;margin: 0 5%;}
.formHeadmain{width: 100%;height: 60px;padding: 30px 0;background: #e6e6e6;overflow: hidden;}
.formHeadmain div{width: 100px;text-align: center;display:block;float: left;}
.formHeadmain .first{width: 400px;}
.formHead{height: 60px;background: #fff;padding: 30px 0;}
.formContent{width: 1160px;padding: 20px;height: auto;background: #f1f1f1;}
.titleInfo{width: 900px;height: 12px;padding: 5px 0;}
.titleInfo div{width: 300px;height: 12px;display: block;float: left;padding: 5px 0;}
.formContent ul{width: 1160px;height: auto;margin:30px 0 10px 0;}
.formContent ul li{width: 100%;height: 12px;padding: 20px 0;border-bottom: #afafaf;background: #fff;text-align: center;}
.firstLi{background: #afafaf!important;}
.formContent ul li div{width: 120px;display: block;float: left;}
</style>
<style type="text/css">
*{
font-family:SongTi_GB2312;
}
</style>
<body>
	<h5 class="userInfo">
		用户名：${count.userName}
		回款中债权总数：${count.countNum}个
		待回本金：<fmt:formatNumber value="${count.capital}" type="currency" pattern="#,##0.00"/>元
		待回利息：<fmt:formatNumber value="${count.interest}" type="currency" pattern="#,##0.00"/>元 
	</h5>
	<div class="formHeadmain">
		<div class="first">债权名称</div> 
		<div>借款人</div> 
		<div>出借金额(元</div> 
		<div>待收回款(元)</div>
		<div>已回金额(元)</div> 
		<div>最近回款日</div> 
		<div>债权状态</div> 
		<div>投标时间</div>
		<div>明细</div>
	</div>
	<c:forEach var="vo" items="${list}">
		<div class="formHeadmain formHead">
			<div class="first">${vo.creditorRightsName}</div> 
			<div>${vo.loanLoginName}</div>
			<div><fmt:formatNumber value="${vo.buyPrice}" type="currency" pattern="#,##0.00"/></div> 
			<div><fmt:formatNumber value="${vo.waitTotalpayMent}" type="currency" pattern="#,##0.00"/></div> 
			<div><fmt:formatNumber value="${vo.factBalance}" type="currency" pattern="#,##0.00"/></div> 
			<div><fmt:formatDate value="${vo.currentPayDate}" pattern="yyyy-MM-dd"/></div>
			<div>
				<c:if test="${vo.rightsStateStr eq '0'}">已生效</c:if>
				<c:if test="${vo.rightsStateStr eq '1'}">还款中</c:if>
				<c:if test="${vo.rightsStateStr eq '2'}">已转出</c:if>
				<c:if test="${vo.rightsStateStr eq '3'}">已结清</c:if>
				<c:if test="${vo.rightsStateStr eq '4'}">已删除</c:if>
				<c:if test="${vo.rightsStateStr eq '5'}">申请转出</c:if>
				<c:if test="${vo.rightsStateStr eq '6'}">已转出(平台垫付)</c:if>
				<c:if test="${vo.rightsStateStr eq '7'}">提前结清</c:if>
				<c:if test="${vo.rightsStateStr eq '8'}">转让中</c:if>
			</div> 
			<div><fmt:formatDate value="${vo.buyDate}" pattern="yyyy-MM-dd"/></div> 
			<div></div>
		</div>
		<div class="formContent">
			<div class="titleInfo">
				<div>借款标题：${vo.loanApplicationListVO.loanApplicationTitle}</div> 
				<div>借款时长：${vo.loanApplicationListVO.cycleCount}个月</div> 
				<div>
					年化利率：${vo.loanApplicationListVO.annualRate}%
					<c:if test="${vo.fromWhereStr eq '1' || (vo.fromWhereStr eq '2' && vo.awardPoint ne '放款')}">
						<c:if test="${not empty vo.awardRate}">
							${vo.awardRate}<i class='borlisdeta'>注</i>
						</c:if>
					</c:if>
				</div>
			</div>
			<div class="titleInfo">
				<div>出借金额：<fmt:formatNumber value="${vo.buyPrice}" type="currency" pattern="#,##0.00"/>元</div> 
				<div>预期收益：<fmt:formatNumber value="${vo.expectProfit}" type="currency" pattern="#,##0.00"/>元</div> 
				<div>还款方式：${vo.loanApplicationListVO.repayMentMethod}</div>
			</div>
			<p class="titleInfo">
				<c:if test="${not empty vo.awardPoint}">
					<i class='borlisdeta2'>注</i>投标奖励金额于${vo.awardPoint}时，发放至您的账户，请注意查收。
				</c:if>
				<c:if test="${not empty vo.rateList && fn:length(vo.rateList)>0 && not empty vo.rateValue2}">${vo.rateValue2}</c:if>
				<c:if test="${not empty vo.rateList && fn:length(vo.rateList)>0 && not empty vo.rateValue}">${vo.rateValue}</c:if>
			</p>
			<ul>
				<li class="firstLi">
					<div>回款期</div> 
					<div>回款日期</div> 
					<div>应回本金(元)</div>
					<div>应回利息(元)</div> 
					<div>罚息(元)</div> 
					<div>应回款总额(元)</div> 
					<div>已回款总额(元)</div>
					<div>应缴费用(元)</div> 
					<div>状态</div>
				</li>
				<c:forEach var="ylist" items="${vo.ylist}">
					<li>
						<div>${ylist[0]}</div> 
						<div><fmt:formatDate value="${ylist[1]}" pattern="yyyy-MM-dd"/></div> 
						<div>
							<c:if test="${ylist[2] ne '---'}"><fmt:formatNumber value="${ylist[2]}" type="currency" pattern="#,##0.00"/></c:if>
							<c:if test="${ylist[2] eq '---'}">${ylist[2]}</c:if>
						</div> 
						<div>
							<c:if test="${ylist[3] ne '---'}"><fmt:formatNumber value="${ylist[3]}" type="currency" pattern="#,##0.00"/></c:if>
							<c:if test="${ylist[3] eq '---'}">${ylist[3]}</c:if>
						</div>
						<div>
							<c:if test="${ylist[4] ne '---'}"><fmt:formatNumber value="${ylist[4]}" type="currency" pattern="#,##0.00"/></c:if>
							<c:if test="${ylist[4] eq '---'}">${ylist[4]}</c:if>
						</div> 
						<div>
							<c:if test="${ylist[6] ne '---'}"><fmt:formatNumber value="${ylist[6]}" type="currency" pattern="#,##0.00"/></c:if>
							<c:if test="${ylist[6] eq '---'}">${ylist[6]}</c:if>
						</div> 
						<div>
							<c:if test="${ylist[7] ne '---'}"><fmt:formatNumber value="${ylist[7]}" type="currency" pattern="#,##0.00"/></c:if>
							<c:if test="${ylist[7] eq '---'}">${ylist[7]}</c:if>
						</div> 
						<div>
							<c:if test="${ylist[5] ne '---'}"><fmt:formatNumber value="${ylist[5]}" type="currency" pattern="#,##0.00"/></c:if>
							<c:if test="${ylist[5] eq '---'}">${ylist[5]}</c:if>
						</div>
						<div>
							<c:if test="${ylist[8] eq '0'}">未还款</c:if>
							<c:if test="${ylist[8] eq '1'}">部分还款</c:if>
							<c:if test="${ylist[8] eq '2'}">已还清</c:if>
							<c:if test="${ylist[8] eq '3'}">逾期</c:if>
							<c:if test="${ylist[8] eq '4'}">提前结清</c:if>
							<c:if test="${ylist[8] eq '5'}">已转出</c:if>
							<c:if test="${ylist[8] eq '6'}">平台垫付利息</c:if>
							<c:if test="${ylist[8] eq '8'}">转让中</c:if>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
	</c:forEach>
</body>

</html>
