<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- 个人房产直投合同之授权委托书 -->
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style>
	.content{width:900px;margin:0 auto;}
	.contitle{text-align:center;}
	.bianhao{text-align:right;}
	.content1 p{line-height:30px;}
</style>
</head>

<body>
<div class="content1">
	<p class="contitle"><strong>授权委托书</strong></p>
		<p class="bianhao"><strong>编号：</strong><strong>__${agreementCode }__</strong></p>
		<p><strong>委托人：__${lendRealName }__ </strong><br/>
		  <strong>身份证号码： __${lendIdCard }__</strong><br/>
		  <strong>联系电话： __${fn:substring(lendMobileNo,0,3)}*****${fn:substring(lendMobileNo,8,11)}__</strong><br/>
		  <strong>地址：  __${lendAddress }__</strong></p>
		<p><strong>受托人：__胡志昂__ </strong><br/>
		  <strong>身份证号码：__140106199011180058__ </strong><br/>
		  <strong>联系电话：__130*****271__ </strong><br/>
		  <strong>地址： ____</strong></p>
		<p><strong>鉴于：</strong>委托人（以下简称&ldquo;本人&rdquo;）与借款人<u> ${loanRealName } </u>通过北京汇聚融达网络科技有限公司（财富派平台， www.caifupad.com 网站）签订了编号为<u> ${agreementCode } </u><br />
		  的《借款及服务协议》。为确保借款人按期履行还款义务，借款人自愿以其所拥有的并有权处分的位于<u> ${addressDetail } </u>产权证字号为<u> ${houseCardNumber }</u> 的合法房产抵押给本委托人作为履行上述借款及服务协议的担保。</p>
		<p><strong>委托范围：</strong>委托人指定受托人<u> 胡志昂 </u>代持本人对借款人享有的的抵押权，负责办理房屋抵押活动有关的事务（包括但不限于签订抵押保证借贷合同、办理房屋抵押/注销登记手续、公证手续、通过民事诉讼或其他方式代为主张<a name="_GoBack" id="_GoBack"></a>抵押权等）。<br />
		  委托人与受托人约定借款人未能于到期还款日（含）前向委托人足额偿还全部到期应还款项的视为逾期；借款人逾期超过5天的，受托人负有向委托人（本人）回购该笔借款的义务，受托人应于本人发出书面通知之日起5个工作日内向本人支付该笔回购金额（回购金额包括但不限于截止到借款人实际付款日止的全部剩余本金、应还末还的利息、罚息、复利、其他应付款项等及其他因此产生的费用）。<br />
		  本人将承担该受托人行为的全部法律后果和法律责任。特此委托。</p>
		<p>此委托书有效期限从<u>${paymentDate }</u>至<u>${lastRepaymentDate }</u>止。</p>
		<p>附：1.委托人身份证复印件；<br/>
		  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.受托人身份证复印件；<br/>
		  <br/></p>
		<p><span style="float:left;">甲方：__${lendRealName }__</span>&nbsp;&nbsp;<span style="float:right;">乙方：__胡志昂__</span></p>
		<div style="clear:both;"></div>
		<p><span style="float:left;">__${paymentDate }__</span><span style="float:right;">&nbsp;&nbsp;__${paymentDate }__</span></p>
	</div>
</body>
</html>
