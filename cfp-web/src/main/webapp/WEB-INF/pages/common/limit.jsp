<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!-- p_bank start -->
<div id="p_bank" class="masklayer masklback p_bank" style="width:863px;">
  <h2 class="clearFloat"><span>提示</span><a href="javascript:;" data-id="close"></a></h2>
  <div class="p_bdetail">
    <h3 class="clearFloat">
      <%--<img src="${ctx}/images/pay_061.jpg" />--%>
      <span>认证支付限额表</span>
    </h3>

    <div class="everybdiv">
      <div class="ebank clearFloat" style="height:400px; overflow-y:scroll;">
          <table cellpadding="0" cellspacing="0" class="e_table mt-20">
              <tr class="e_tr_title">
                  <td width="60">银行名称</td>
                  <td width="60">单笔限额</td>
                  <td width="60">单日限额</td>
                  <td width="60">单月限额</td>
                  <td>备注</td>
              </tr>
              <tr>
                	<td>农业银行</td>
                	<td>20万</td>
                	<td>50万</td>
                	<td>1500万</td>
                	<td>请登录银联用户系统开通银联在线支付业务，请使用在农行“个人客户基本信息”中预留的手机号开通此项业务。 <br>
                    如果银行预留手机号码更换过或者开通过程中提示预留信息有误情况，可到农业银行柜台告知柜员银行预留手机号，修改客户信息的交易码为5286。再登录银联用户系统开通银联在线支付业务。</td>
                </tr>
            	<!--  <tr>
                	<td>浦发银行</td>
                	<td>5万</td>
                	<td>5万</td>
                	<td>150万</td>
                	<td align="left">建议开通及时语业务（短信提示功能），请到浦发银行柜台开通此项业务 。<br>
                    请到浦发银行柜台或登录浦发个人网上银行开通银联在线支付业务，具体可联系浦发银行客服热线95528咨询如何开通此业务，或登录银联用户系统开通。<br>
                    实际支付限额以客户开通设定或银行限额为准。</td>
                </tr>
            	<tr>
                	<td>交通银行</td>
                	<td>5万</td>
                	<td>20万</td>
                	<td>600万</td>
                	<td align="left">需开通银联在线无卡支付业务，交易金额需大于等于1元。<br />注意：批量卡或2000/04/01之前开立的卡，在开通在线支付时，需要先至银行柜台开通电子银行；儿童卡或联名账户借记卡，不允许开通银联在线支付。</td>
                </tr> -->
            	<tr>
                	<td>工商银行</td>
                	<td>5万</td>
                	<td>5万</td>
                	<td>150万</td>
                	<td align="left">建议开通银联在线支付业务。请先前往工行网点办理4204柜面预留手机号码业务，后登录银联用户系统开通银联在线支付业务。</td>
                </tr>
            	<tr>
                	<td>邮储银行</td>
                	<td>5000</td>
                	<td>5000</td>
                	<td>15万</td>
                	<td align="left">请到邮储银行柜台或登录邮储个人网上银行开通银联在线支付业务，具体可联系邮储银行客服热线95580咨询如何开通此业务，或登录银联用户系统开通。</td>
                </tr>
            	<tr>
                	<td>广发银行</td>
                	<td>50万</td>
                	<td>100万</td>
                	<td>3000万</td>
                	<td></td>
                </tr>
            	<tr>
                	<td>民生银行</td>
                	<td>50万</td>
                	<td>50万</td>
                	<td>1500万</td>
                	<td></td>
                </tr>
            	<tr>
                	<td>平安银行</td>
                	<td>50万</td>
                	<td>100万</td>
                	<td>3000万</td>
                	<td></td>
                </tr>
            	<tr>
                	<td>招商银行</td>
                	<td>1万</td>
                	<td>1万</td>
                	<td>30万</td>
                	<td></td>
                </tr>
            	<tr>
                	<td>中国银行</td>
                	<td>5万</td>
                	<td>30万</td>
                	<td>900万</td>
                	<td align="left">1）请先根据需要通过柜台、网银、自助终端、电话银行、手机银行等渠道办理借记卡“银联跨行无卡支付“功能。并根据自身需要设定每日累计交易限额，中行对无卡支付限额默认每卡每日5万元，您可根据需要调高或调低，如设置为0则默认关闭。
                    若需通过网银开通“银联跨行无卡支付“功能，请携带本人身份证件、银行卡以及银行预留手机号的手机前往中国银行各营业网点进行注册开通理财版或贵宾版网上银行。登录个人网上银行，选择“电子支付-银联跨行无卡支付”，按照页面提示操作开通“银联跨行无卡支付”功能，具体开通过程中如有疑问请联系中行客服热线95566咨询如何开通此业务。<br>
                    2）“银联跨行无卡支付”功能开通支付成功后，再登录银联用户系统开通银联在线支付业务。</td>
                </tr>
            	<tr>
                	<td>建设银行</td>
                	<td>2万</td>
                	<td>2万</td>
                	<td>60万</td>
                	<td align="left">需要先到建设银行柜台办理建行高级版（签约版）网银业务并激活后，再登陆银联用户系统，使用办理该高级版（签约版）网银业务的手机号开通银联在线支付业务。</td>
                </tr>
            	<tr>
                	<td>光大银行</td>
                    <td>50万</td>
                    <td>100万</td>
                    <td>3000万</td>
                	<td>请开通网上支付功能，开通方法如下，开通过程中如有疑问具体可联系光大银行客服热线95595咨询如何开通此业务。<br>
                    1、登录网上银行专业版，在"更多功能"-"电子支付"-"支付签约管理"-"网上支付"项下，点击"开通"；<br>
                    2、登录手机银行客户端，点击"我的"右上角按钮，通过"支付签约管理"中开通（需要先开通对外转账版的手机银行）；<br>
                    3、如果您持有我行信用卡，可以使用信用卡预留的手机号码致电4007888888通过电话人工服务申请开通借记卡的网上支付功能，限额为500元/日；<br>
                    4、本人携带有效身份证件原件及卡片到光大银行柜台办理开通。</td>
                </tr>
            	<tr>
                	<td>兴业银行</td>
                	<td>5万</td>
                	<td>5万</td>
                	<td>150万</td>
                	<td>请到兴业银行柜台开通“无卡在线支付功能”。</td>
                </tr>
            	<tr>
                	<td>中信银行</td>
                	<td>1万</td>
                	<td>1万</td>
                	<td>2万</td>
                	<td></td>
                </tr>
            	<tr>
                	<td>华夏银行</td>
                    <td>50万</td>
                    <td>100万</td>
                	<td>3000万</td>
                	<td></td>
                </tr>
                <tr>
                	<td>杭州银行</td>
                    <td>5万</td>
                    <td>30万</td>
                    <td>900万</td>
                	<td></td>
                </tr>
                <tr>
                	<td>北京银行</td>
                    <td>5万</td>
                    <td>30万</td>
                    <td>900万</td>
                	<td></td>
                </tr>
                <tr>
                	<td>浙商银行</td>
                    <td>5千</td>
                    <td>5千</td>
                    <td>15万</td>
                	<td></td>
                </tr>
                <tr>
                	<td>交通银行</td>
                    <td>20万</td>
                    <td>20万</td>
                    <td>600万</td>
                	<td>需开通银联无卡支付业务</td>
                </tr>
                <tr>
                	<td>上海银行</td>
                    <td>5千</td>
                    <td>5千</td>
                    <td>15万</td>
                	<td>需开通银联无卡支付业务</td>
                </tr>
                <tr>
                	<td>浦发银行</td>
                    <td>5万</td>
                    <td>30万</td>
                    <td>900万</td>
                	<td></td>
                </tr>
                <tr>
                	<td>广州银行</td>
                    <td>50万</td>
                    <td>100万</td>
                	<td>3000万</td>
                	<td></td>
                </tr>
                <tr>
                	<td>宁波银行</td>
                    <td>50万</td>
                    <td>50万</td>
                	<td>1500万</td>
                	<td></td>
                </tr>
                <tr>
                	<td>甘肃省农村信用社联合社</td>
                    <td>5万</td>
                    <td>100万</td>
                	<td>3000万</td>
                	<td></td>
                </tr>
                <tr>
                	<td>江苏银行</td>
                    <td>5万</td>
                    <td>30万</td>
                	<td>900万</td>
                	<td></td>
                </tr>
                <tr>
                	<td>恒丰银行</td>
                    <td>50万</td>
                    <td>100万</td>
                	<td>3000万</td>
                	<td></td>
                </tr>

          </table>

      </div>
      <div class="tar">
        <a href="javascript:;" data-id="close" class="btn btn-error">关闭</a>
      </div>
    </div>
  </div>
</div>