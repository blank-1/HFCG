<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>

<style type="text/css">
html{ padding:0px; margin:0px; color:#000; -webkit-text-size-adjust:100%; -ms-text-size-adjust:100%;box-sizing: border-box; min-height: 100%;}
/*内外边距通常让各个浏览器样式的表现位置不同*/
body{ font: 14px/1.5 '微软雅黑',YaHei,'MicroSoft YaHei',tahoma,arial; color:#666666;}
body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,textarea,a,p,img,blockquote,th,td,hr,button,article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section{ margin:0px; padding:0px; font-size:14px; font-family:'MicroSoft YaHei';}

/*去掉table cell的边距并让其边重合*/
table{ border-collapse:collapse; border-spacing:0;}

/* 去掉列表前的标识，li 会继承*/
ol,ul,li{ list-style:none;}

/*来自 yahoo,让标题都自定义，适应多个系统应用*/
h1,h2,h3,h4,h5,h6{ font-size:100%; font-weight:500;}

/* 清理浮动*/
.clearFloat:after{ visibility:hidden; display:block; font-size:0; content:""; clear:both; height:0;}
.clearFloat{ zoom:1;}/*兼容ie6 7*/

/* 设置内联，减少浮动带来的bug*/
.floatLeft,.floatRight{ display:inline;}
.floatLeft{ float:left;}
.floatRight{ float:right;}

.agreenment{ width:700px; margin:0 auto; font-family:'SimSun'; color:#333333; line-height:30px; margin-top:40px; margin-bottom:50px;}
.agreenment>h2{ font-size:18px; font-weight:bold; text-align:center;}
.agreenment p,.agreenment h4{ text-indent:2em;}
.agreenment h3,.agreenment h4{ font-weight:bold;}
.agreenment .atable{ color:#333333; line-height:30px; margin:20px 0; width:100%; text-align:center; border-top:1px solid #333333; border-left:1px solid #333333;}
.agreenment .atable tr td{ border-bottom:1px solid #333333; border-right:1px solid #333333;}
.agreenment2{ width:93%; margin:5%; height:400px; overflow-y:scroll;}
.width103{ width:103px; display:inline-block}
.agreen2{ position:relative;}
.agreen2 .zhang{ position:absolute; width:180px; height:180px; background:url(../img/argreen.png) center center no-repeat; left:180px; top:100px;}

</style>

</head>
<style>
p{ text-indent:0em!important;}
</style>
<body>
<div class="wrapper agreenment agreen2">
	<div class="zhang"></div>
	<h2>债权转让及受让协议</h2>
	 
	  <p style="text-align:right">合同编号: ___${agreementCode }___ </p>
    <h3>本债权转让及受让协议（&quot;本协议&quot;）由以下各方于 ${agreementStartDate } 签署并履行：</h3><br/>
    <h3>转让人（原债权人）：${sourceRealName } </h3>
    <h3>联系电话：${fn:substring(sourceMobileNo,0,3)}*****${fn:substring(sourceMobileNo,8,11)}</h3>
    <h3>邮箱地址：${sourceEmail }</h3><br/>
    <h3>受让人（新债权人）：${lendRealName } </h3>
    <h3>联系电话：${fn:substring(lendMobileNo,0,3)}*****${fn:substring(lendMobileNo,8,11)}</h3>
    <h3>邮箱地址：${lendEmail }</h3><br/>
    <h3>服务方：北京汇聚融达网络科技有限公司</h3>
    <h3>地址：北京市朝阳区光华路9号光华路SOHO2期B座11层</h3><br/>
    <h3>鉴于：</h3>
     <p>1.受让人愿意以自有合法资金受让转让人通过  财富派  网（www.caifupad .com）与借款人于  ${agreementStartDate }  签署并生效的编号为  ${agreementCode } &nbsp;《借款及服务协议》（以下简称&ldquo;《借款协议》&rdquo;）项下全部或部分债权（以下简称&ldquo;转让债权&rdquo;）。 </p>
    <p>2. 受让人同意接受《借款协议》之全部条款，并受《借款协议》之约束。 </p>
    <p>3.服务方为债权转让事项提供顾问服务； </p>
    <p>现各方就该债权转让及受让事宜达成如下协议，以资信守： </p>
    <ol>
      <li><strong>第一条 标的债权及受让</strong></li>
    </ol>
    <p><strong>&nbsp;</strong></p>
  <table border="1" cellspacing="0" cellpadding="0" align="center" width="100%" class="atable">
      <tr>
        <td colspan="4">标的债权信息 </td>
      </tr>
      <tr>
        <td>借款ID </td>
        <td colspan="3">${loanApplicationCode } </td>
      </tr>
      <tr>
        <td>借款人姓名 </td>
        <td colspan="3">${loanRealName } </td>
      </tr>
      <tr>
        <td>借款人身份证号 </td>
        <td colspan="3">${loanIdCard } </td>
      </tr>
      <tr>
        <td>借款本金数额 </td>
        <td colspan="3">¥ ${resultBalance } </td>
      </tr>
      <tr>
        <td>原借款期限 </td>
        <td colspan="3">${dueTime }   个月 </td>
      </tr>
      <tr>
        <td colspan="4">受让债权明细 </td>
      </tr>
      <tr>
        <td>受让本金 </td>
        <td colspan="3">人民币${buyBalance }元,大写：人民币 ${buyBalanceBig } </td>
      </tr>
      <tr>
        <td>借款利率 </td>
        <td>${annualRate } %/年 </td>
        <td>每月利息 </td>
        <td>详见每月系统自动生成的还款明细表 </td>
      </tr>
      <tr>
        <td>受让日期 </td>
        <td>${lendTime } </td>
        <td>起息日 </td>
        <td>${paymentDate }</td>
      </tr>
      <tr>
        <td>到期日期 </td>
        <td>${lastRepaymentDate } </td>
        <td>还款方式 </td>
        <td>${repaymentType } </td>
      </tr>
      <tr>
        <td>还款日/结息日 </td>
        <td>详见还款明细表 </td>
        <td>还款期数 </td>
        <td>${cycleCount }期 </td>
      </tr>
      <tr>
        <td>每月应收款总金额 </td>
        <td colspan="3">详见还款明细表 </td>
      </tr>
    </table>
    <p>还款明细表： </p>
  <table border="1" cellspacing="0" cellpadding="0" align="center" width="100%" class="atable">
      <tr>
        <td> 期数 </td>
        <td>金额（元） </td>
        <td>本金（元） </td>
        <td>利息（元） </td>
        <td>还款日期 </td>
      </tr>
      <c:forEach items="${repaymentPlanList}" var="plan">
	   	<tr>
	      <td>第${plan.sectionCode}期 </td>
	      <td>${plan.shouldBalance2}</td>
	      <td>${plan.shouldCapital2}</td>
	      <td>${plan.shouldInterest2}</td>
	      <td>${plan.repaymentDayDisplay}</td>
	    </tr>
    </c:forEach>
    </table>
    <p>&nbsp;</p>
  <table border="1" cellspacing="0" cellpadding="0" align="center" width="100%" class="atable">
      <tr>
        <td colspan="2">服务商 </td>
      </tr>
      <tr>
        <td>公司名称 </td>
        <td>北京汇聚融达网络科技有限公司 </td>
      </tr>
      <tr>
        <td colspan="2">服务费用 </td>
      </tr>
      
    <c:set var="itemtypeService"></c:set>
    <c:if test="${not empty feesItems}">
        <c:forEach var="feesItem" items="${feesItems}" varStatus="s">
            <c:if test="${feesItem.feesItem.itemType == 3}">
                <c:set var="itemtypeService" value="3"/>
            </c:if>
        </c:forEach>
    </c:if>
      
      <tr>
        <td>□${itemtypeService==3?'√':'' }受让人缴纳服务费 </td>
        <td>每月利息× 5 % </td>
      </tr>
    </table>
    <p><strong>第二条 对价支付方式</strong><br/>
      受让人签署本协议并授权服务方将本协议项下的对价直接划付至转让人账户（可通过受让人授权的第三方支付机构或银行），待此转让标在服务方平台满标之日协议自动生效。转让人和受让人均同意第三方支付机构或银行接受委托后进行的行为，所产生的法律后果均由相应委托方承担。 </p>
    <p><strong>第三条 服务方地位</strong><br/>
      转让人与受让人在此确认，北京汇聚融达网络科技有限公司作为理财顾问，不是本协议所涉及的借贷关系的当事人；转让人、受让人和借款人中的任何一方根据借贷关系和/或债权转让及受让关系向对方主张权利时，不得将理财顾问列为共同被告，也不得要求理财顾问承担连带责任。 </p>
    <p><strong>第四条 受让人资格取得</strong><br/>
      受让人按照本协议&quot;第一条&quot;规定支付对价后，即取代转让人成为《借款协议》项下出借人的法律地位，享受《借款协议》约定的出借人的权利，并承担《借款协议》约定的出借人的义务。 <br/>
  <strong>第五条 受让人收益账户</strong><br/>
      本协议生效后，转让债权对应的《借款协议》项下借款本金及利息应当支付至下列受让人于<u> www.caifupad.com </u>网注册的出借人用户名的账户。 <br/>
      账户名： <u>   ${lendRealName }    </u> <br/>
      账号：<u>    ${lendCardCode }   </u><u> </u><br/>
      开户行：<u>   ${lendBankName }    </u>   </p>
    <p align="left"><strong>第六条 转让人权利与义务</strong><br/>
      6.1 依法收取受让人支付的债权转让对价； <br/>
      6.2 向原债务人出具债权已依法转让书面声明和通知； <br/>
      6.3确保所转让的债权真实、合法、有效并完全有权决定处分该债权，自愿承担相关法律责任。 <br/>
  <strong>第七条 受让人权利与义务</strong><br/>
      7.1按协议约定的时间和方式向转让人支付债权转让款项； <br/>
      7.2在依法接受转让人上述债权后，依法行使对债务人的债权及相关从权利； <br/>
      7.3在实现债权过程中可以要求并获得服务方必要的协助。 <br/>
  <strong>第八条 服务方权利义务</strong><br/>
      8.1为本协议项下债权受让、追收及实现提供必要的协助； <br/>
      8.2向转让人或受让人收取必要的服务费用。 </p>
    <p align="left"><strong>第九条 违约责任</strong><br/>
      转让人与受让人同意，如果一方违反其在本协议中所做的陈述、保证、承诺或任何其他义务，致使对方遭受或发生损害、损失等情形，违约方需向守约方赔偿守约方因此遭受的一切经济损失。 <br/>
  <strong>第十条 其他</strong><br/>
      10.1各方确认本协议通过电子文本签署，通过电子文本签署的，电子文本保存在服务方为此设立的专用服务器上备查，各方均认可该形式的协议效力。 <br/>
      10.2转让人及受让人同意服务方根据具体情况对本协议进行更新。 <br/>
      10.3转让人通过本协议所获得的收益应自行申报并缴纳税款。 <br/>
      10.4服务方接受转让人和受让人的委托所产生的法律后果由相应委托方承担。如因转让人或受让人或其他第三方（包括但不限于技术问题）造成的延误或错误，服务方不承担任何责任。 <br/>
      10.5转让人和受让人均确认，本协议的签署、生效和履行以不违反中国的法律法规为前提。如果本协议中的任何一条或多条违反适用的法律法规，则该条将被视为无效，但该无效条款并不影响本协议其他条款的效力。 <br/>
      10.6本协议的签订地为北京市朝阳区；如果转让人和受让人在本协议履行过程中发生任何争议，应本着友好协商的态度解决；如协商不成，提交协议签订地<a name="_GoBack"></a>北京市朝阳区人民法院诉讼解决。 <br/>
      10.7《借款协议》作为本协议的附件，与本协议具有同等的法律效力。 <br/>
      10.8本协议自文本最终生成之日起成立，同时受让人与借款人之间建立借款法律关系，受让人受《借款协议》内容的约束。 <br/>
  <strong><em><u>以下无正文</u><u> </u></em></strong></p>
    <p><strong>转让人：${sourceRealName }                                            受让人：${lendRealName } </strong></p>
    <p><strong>                                </strong></p>
    <p><strong>&nbsp;</strong></p>

    <p><strong>&nbsp;</strong></p>
</div>
</body>
</html>
