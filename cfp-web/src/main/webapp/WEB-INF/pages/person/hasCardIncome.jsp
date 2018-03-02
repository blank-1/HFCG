<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>

						<p class="quickPay">
							<img src="${ctx }/images/user-center/pay_061.jpg" style="margin-left:10px;" />一键支付
						</p>
						<div class="cz-list-le">
							<div class="input-group">
								<label>
									<span class="cz-span">开户名：</span>
							        <c:if test="${userExt.isVerified ne '1'}">
							            <small>＊您未进行身份验证，请先进行<a data-mask='mask' data-id="shenfen" href="javascript:;">身份验证</a>再进行充值操作</small>
							        </c:if>
							        <c:if test="${userExt.isVerified eq '1'}">
							            ${userExt.realName}
							        </c:if>
								</label>
							</div>
							<div class="input-group mt-30">
								<label for="bankid">
									<span class="cz-span">银行卡号：</span>
									<div class="bankblock bankuse" style="">
										<div class="titlet clearFloat">
											<span class=" floatLeft" style="background-image: url('${ctx}/images/banklogo/${customerCard.bankCode}.png')">&nbsp;</span>
											<span class="floatRight"><small>尾号：</small>${customerCard.encryptCardNo}</span>
											 <input type="hidden" name="cardId" id="cardId" value="${customerCard.customerCardId}"/>
         								     <input type="hidden" name="cardNo" id="cardNo" value="${customerCard.cardCode}"/>
										</div>
									</div>
									
								</label>
							</div>
							<div class="clear_0"></div>
							<div class="input-group mt-20">
								<label for="password">
									<span class="cz-span">付款限额：</span>
									<a href="javascript:;" class="cz-list-a" data-id="p_bank" data-mask="mask" >点击查看</a>
								</label>
							</div>
							<div class="input-group mt-30">
								<label for="moneyp">
									<span class="cz-span fash-3">充值金额：</span>
									<input type="text" id="moneyp" name="rechargeAmount" class="ipt-input user-cz-input" />
								</label>
								<em class="hui cz-em">*充值最小金额不得小于<font color="#fe2a4d">100</font>元</em>
							</div>
						</div>
						<div class="cz-list-ri">
							<span><img src="${ctx }/images/news_icon/text2_icon.png" />换卡流程</span>
							<p>如遇银行卡丢失或需更换银行卡，请出示以下信息：</p>
							<p>1、手持“身份证以及已绑定银行卡”的清晰照片</p>
							<p>2、身份证清晰照片</p>
							<p>3、已绑定银行卡的清晰照片</p>
							<p>4、如银行卡丢失，需提供银行出示的挂失凭证照</p>
							<p>请将以上信息发送至财富派客服邮箱：</p>
							<p> myservice@mayitz.com，我们将在1-3个工作日进行信息审核及解绑操作，解绑前我们会与您电话沟通，请保持手机畅通，感谢您的配合。
					            详情可拨打客服电话进行咨询：400-061-8080</p>
						</div>
						
