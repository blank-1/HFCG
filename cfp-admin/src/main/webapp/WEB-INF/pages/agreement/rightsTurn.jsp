<%--
  User: Ren yulin
  Date: 15-8-1 上午5:39
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <meta name="keywords" content="" />
    <meta name="description" content="" />

    <title>债权转让及受让协议</title>

    <link rel="stylesheet" type="text/css" href="../../css/index.css" /><!-- index css -->
    <script type="text/javascript" src="../../js/jquery.min.js"></script><!-- jquery 类库 javascript -->
</head>

<body>
<div class="wrapper agreenment">
    <p align="right"><strong>编号:  <span style="text-decoration:underline">${rightsCode}</span></strong></p>
    <h2>债权转让及受让协议 </h2>
    <h3>本债权转让及受让协议（&quot;本协议&quot;）由以下各方于
        __<span style="text-decoration:underline">${year}</span>__年
        __<span style="text-decoration:underline">${month}</span>__月
        __<span style="text-decoration:underline">${day}</span>__日签署并履行： </h3>
    <h3>转让人（原债权人）： ${oldLenderRealName}</h3>
    <h3>受让人（新债权人）： ${newLenderRealName}</h3>
    <h3>服务商：北京汇聚融达网络科技有限公司 </h3>
    <p>鉴于：转让人与本协议&quot;第一条&quot;约定的借款人签订《借款协议》，故转让人与受让人经平等协商，由转让人将《借款协议》项下对借款人享有的全部债权转让给受让人，受让人同意受让该债权，服务商为债权转让事项提供顾问服务；现各方就该债权转让及受让事宜达成如下协议，以资信守： <br>
        <strong>第一条 标的债权及受让 </strong></p>
    <table border="0" cellspacing="0" cellpadding="0" class="atable" >
        <tr>
            <td>借款人姓名 </td>
            <td >身份证号码 </td>
            <td>《借款协议》编号</td>
            <td >借款用途 </td>
            <td>最初还款日</td>
            <td>还款期限（月）</td>
            <td>剩余还款月数 </td>
        </tr>
        <tr>
            <td >&nbsp;${loanUserRealName}</td>
            <td >&nbsp;${loanIDCard}</td>
            <td >&nbsp;${loanApplicationCode}</td>
            <td >&nbsp;${loanUseage}</td>
            <td >&nbsp;${firstRepaymentDay}</td>
            <td >&nbsp;${repaymentMonths}</td>
            <td >&nbsp;${unRepaymentMonths}</td>
        </tr>
    </table>
    <table border="0" cellspacing="0" cellpadding="0" class="atable" >
        <tr>
            <td>序号 </td>
            <td>受让人 </td>
            <td>身份证号后四位 </td>
            <td>转让对价 </td>
            <td>还款起始日期 </td>
            <td>还款期限（月）</td>
            <td>剩余还款月数 </td>
            <td>预期收益 </td>
        </tr>
        <tr>
            <td >&nbsp;</td>
            <td >&nbsp;</td>
            <td >&nbsp;</td>
            <td >&nbsp;</td>
            <td >&nbsp;</td>
            <td >&nbsp;</td>
            <td >&nbsp;</td>
            <td >&nbsp;</td>
        </tr>
    </table>
    <p><strong>第二条 对价支付方式 </strong><br>
        受让人在本协议签署或生成后3个工作日内，授权服务商将本协议项下的对价直接划付至转让人账户（可通过受让人授权的第三方支付机构或银行）。转让人和受让人均同意第三方支付机构或银行接受委托后进行的行为所产生的法律后果均由相应委托方承担。 <br>
        <strong>第三条 服务商地位 </strong><br>
        转让人与受让人在此确认，北京汇聚融达网络科技有限公司作为理财顾问，不是本协议所涉及的借贷关系的当事人；转让人、受让人和借款人中的任何一方根据借贷关系和/或债权转让及受让关系向对方主张权利时，不得将理财顾问列为共同被告，也不得要求理财顾问承担连带责任。 <br>
        <strong>第四条 受让人资格取得 </strong><br>
        受让人按照本协议&quot;第一条&quot;规定支付对价后，即取代转让人成为《借款协议》项下出借人的法律地位，享受《借款协议》约定的出借人的权利，并承担《借款协议》约定的出借人的义务。 <br>
        <strong>第五条 甲方权利与义务 </strong><br>
        1）依法收取乙方支付的债权转让对价。 <br>
        2） 向原债务人出具债权已依法转让书面声明和通知。 <br>
        3）为本协议项下债权受让、追收及实现提供必要的协助。 <br>
        4）确保所转让的债权真实、合法、有效并完全有权决定处分该债权，自愿承担相关法律责任。 <br>
        <strong>第六条 乙方权利与义务 </strong><br>
        1）按协议约定的时间和方式向甲方支付债权转让款项。 <br>
        2）在依法接受甲方上述债权后，依法行使对债务人的债权及相关从权利。 <br>
        3）在实现债权过程中可以要求并获得甲方必要的协助。 <br>
        <strong>第七条 违约责任 </strong><br>
        甲乙双方同意，如果一方违反其在本协议中所做的陈述、保证、承诺或任何其他义务，致使对方遭受或发生损害、损失等情形，违约方需向守约方赔偿守约方因此遭受的一切经济损失。 <br>
        <strong>第八条 其他 </strong><br>
        1)本协议采用电子文本形式制成，并保存在服务商为此设立的专用服务器上备查，各方均以其在计算机上点击确认的方式对该协议进行签署，各方均认可该形式的协议效力。 <br>
        2)转让人及受让人同意顾问根据具体情况对本协议进行更新。 <br>
        3)转让人与受让人以书面形式（包括电子文本等）签订的补充协议与本协议具有同等的法律效力。 <br>
        4)转让人通过本协议所获得的收益应自行申报并缴纳税款。 <br>
        5)服务商接受转让人和受让人的委托所产生的法律后果由相应委托方承担。如因转让人或受让人或其他第三方（包括但不限于技术问题）造成的延误或错误，服务商不承担任何责任。 <br>
        6)转让人和受让人均确认，本协议的签署、生效和履行以不违反中国的法律法规为前提。如果本协议中的任何一条或多条违反适用的法律法规，则该条将被视为无效，但该无效条款并不影响本协议其他条款的效力。 <br>
        7)本协议的签订地及履行地为北京市朝阳区；如果转让人和受让人在本协议履行过程中发生任何争议，应友好协商解决；如协商不成，则须提交协议签订地及履行地北京市朝阳区人民法院进行诉讼。 <br>
        8)《借款协议》作为本协议的附件，与本协议具有同等的法律效力。 <br>
        9)本协议自文本最终生成之日起成立；自受让人将本协议&quot;第一条&quot;约定的款项划付至转让人账户时生效，同时，受让人与借款人之间建立借款法律关系，受让人受《借款协议》内容的约束。 <br>
        <strong><em><u>以下无正文 </u></em></strong></p>
</div>
</body>
</html>
