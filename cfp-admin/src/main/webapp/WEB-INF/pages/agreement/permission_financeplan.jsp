<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- masklayer start -->
<div class="zhezhao5"></div>
<div id="pro_xtb_xieyi" class="masklayer masklback masklaym3">
	<h2 class="clearFloat">
		<span>授权委托书 </span><a href="javascript:;" data-id="httpClose"></a>
	</h2>
	<div class="agreenment agreenment2">
		<p></p>
		<p>编号：${agreementCode}</p>
		<p>甲方（委托方）：${realName }</p>
		<p>身份证号码：${idCard }</p>
		<p>联系电话：${mobileNo }</p>
		<p>住址：${detail }</p>
		<p></p>
		<p>乙方（受托方）：北京汇聚融达网络科技有限公司</p>
		<p>地址：北京市朝阳区光华路9号光华路SOHO2期B座11层</p>
		<p></p>
		<p>委托方参加受托方经营的P2P借贷交易居间服务的互联网平台（即“财富派”）推出的一项“省心服务计划”，为更好的为委托方提供服务，最大限度满足委托方的投资收益需求，现委托方特向受托方做出如下授权，由此产生的责任均由委托方本人承担：
		</p>
		<p></p>
		<p>一、 授权委托事项：</p>
		<p></p>
		<p>1、受托方代委托方在财富派平台上进行账号注册，并将委托方姓名、身份证号码及委托方名下使用的一个银行卡账号提交给委托方出借资金托管第三方机构，为委托方开设账户，供出借资金托管使用；
		</p>
		<p>2、受托方代委托方在财富派平台上点击确认出借资金；</p>
		<p>3、注册成功后，受托方在授权服务期（省心期）内代委托方进行资金的出借、帮助其选择并自动投资平台线上散标、回款管理及回款再出借；
		</p>
		<p></p>
		<p>二、 授权期限：</p>
		<p></p>
		<p>1、本授权期限为${limit}个自然月，自委托方与受托方签署本《授权委托书》生效之日起算，除委托人书面做出相反意思表示外，本《授权委托书》至授权期限届满之日前具有法律效力；
		</p>
		<p>2、省心计划退出方式为授权期限届满即省心期到期后，债权到期退出；
		</p>
		<p>3、期限届满，如甲方仍有委托意向，双方可续签相应协议。
		</p>
		<p></p>
		<p>三、 授权出借信息：</p>
		<p></p>
		<p>1、授权出借账户</p>
		<p>财富派平台账号：${loginName }</p>
		<p>2、授权出借金额：人民币（大写） ${buyBalanceBig }元（￥${buyBalance } ）</p>
		<p>
		3、委托方可选择下述任一收益分配方式实现其个人资金出借需求，最终收益分配方式以委托方在财富派平台购买省心计划点击选择的为准。<br />
		<c:if test="${not empty profitReturnConfig && profitReturnConfig eq '1'  }">
		□× 收益复利投资 <br />
		□√ 收益提取至可用余额 <br />
		</c:if>
		<c:if test="${empty profitReturnConfig || profitReturnConfig eq '0'  }">
		□√ 收益复利投资 <br />
		□× 收益提取至可用余额 <br />
		</c:if>
		</p>
		<p></p>
		<p>四、声明：</p>
		<p></p>
		<p>1、授权期限（省心期）不同于收益期限，具体收益期限以线上实际生成标的对应的《借款及服务协议》中约定为准；</p>
		<p>2、预期收益为范围值不同于实际收益率，具体收益率以线上实际生成标的对应的《借款及服务协议》中约定为准；</p>
		<p></p>
		<p>五、 其他</p>
		<p></p>
		<p>1、 本委托书经委托方在财富派网络平台以线上点击确认的方式签署；</p>
		<p>2、 本委托书生效后，协议各方应严格遵守，任何一方违约，应承担由此造成的守约方的损失。</p>
		<p></p>
		<p></p>
		<p>委托方：${realName }</p>
		<p>财富派ID：${userId }</p>
		<p></p>
		<p>签署日期： ${agreementStartDate }</p>
	</div>
	<!-- <div class="tac">
		<a href="javascript:;" data-id='httpClose'
			class="btn btn-error width103">关闭</a>
	</div> -->
</div>
<!-- masklayer end -->
