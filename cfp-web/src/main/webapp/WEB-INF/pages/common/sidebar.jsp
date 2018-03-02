<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<script type="text/javascript" src="${ctx}/js/person1.js"></script><!-- person1 javascript -->
<script type="text/javascript" charset="utf-8" src="https://gate.soperson.com/10038722/10055763.js"></script>
<!-- dSidebar start -->
<div class="dSidebar"><i class="wazi"></i>
    <ul class="uSide">
        <li class="lSare"><em class="eml12"><i class="il12"></i></em>
            <div class="dlScar dshow"><img src="${ctx}/images/index_new/index_19.jpg" />财富派官方微信</div>
        </li>
        <li class="lPhone"><em class="eml12"><i class="il12"></i></em>
        <div class="dlScar dshow"><img src="${ctx}/images/index_new/ad.jpg" />财富派APP下载</div></li>
        <%--<li class="lCal"><em><i></i></em></li>--%>
        <li class="lCal" id="jsqimg"><em class="eml12"><i class="il12"></i></em>
          <div class="indexjsp dshow">

              <div class="jsq dshow" id="jsqdiv">
                  <h2 class="clearFloat"><span data-id="bor1" class="action"><img src="${ctx}/images/jsq_06.png" />出借计算</span><span data-id="bor2"><img src="${ctx}/images/jsq_03.png" />借款计算</span></h2>
                  <!-- borjs start -->
                  <div class="borjs tab" id="bor1">
                      <div class="clearFloat ifselct">

                    <span class="input-group tal floatLeft ml-60">
                        <i class="ipt_i floatLeft">请输入出借金额：</i><label><input type="text" placeholder="输入出借金额" id="borrmon" class="ipt-input"/></label>
               			 <br /><em class="floatLeft tal mt-5 ml-113">&nbsp;</em>
                    </span>
                          <div class="clear"></div>
                    <span>
                        <!-- select start -->
                        <dl class="select" id="rate_Type">
                            <dt id="rateType" data-id="0" style="width:126px;">付息类型</dt>
                            <dd>
                                <ul style="width:148px;">
                                    <li data-id="1"><a href="javascript:;">等额本金</a></li>
                                    <li data-id="2"><a href="javascript:;">等额本息</a></li>
                                    <li data-id="3"><a href="javascript:;">周期付息到期还本</a></li>
                                </ul>
                            </dd>
                        </dl><!-- select end -->
                         <em class="tal">&nbsp;</em>
                    </span>
                    <span>
                       <!-- select start -->
                        <dl class="select" id="brate">
                            <dt id="annualRate" data-id="0">选择出借利率</dt>
                            <dd>
                                <ul>
                                    <%--<c:forEach items="${profitRate}" var="profit">--%>
                                    <%--<li data-id="${profit.profitRate}"><a href="javascript:">${profit.profitRate}%</a></li>--%>
                                    <%--</c:forEach>--%>
                                    <li data-id="6.5"><a href="javascript:">6.5%</a></li>
                                    <li data-id="7"><a href="javascript:">7%</a></li>
                                    <li data-id="7.5"><a href="javascript:">7.5%</a></li>
                                    <li data-id="8"><a href="javascript:">8%</a></li>
                                    <li data-id="9"><a href="javascript:">9%</a></li>
                                    <li data-id="10"><a href="javascript:">10%</a></li>
                                    <li data-id="11"><a href="javascript:">11%</a></li>
                                    <li data-id="12"><a href="javascript:">12%</a></li>
                                </ul>
                            </dd>
                        </dl><!-- select end -->
                         <em class="tal">&nbsp;</em>
                    </span>
                    <span>
                       <!-- select start -->
                        <dl class="select" id="bdata">
                            <dt id="months" data-id="0">选择出借期数</dt>
                            <dd>
                                <ul>
                                    <%--<c:forEach items="${timeLimits}" var="timeLimit">--%>
                                    <%--<li data-id="${timeLimit.timeLimit}"><a href="javascript:">${timeLimit.timeLimit}期</a></li>--%>
                                    <%--</c:forEach>--%>
                                    <li data-id="1"><a href="javascript:">1期</a></li>
                                    <li data-id="3"><a href="javascript:">3期</a></li>
                                    <li data-id="6"><a href="javascript:">6期</a></li>
                                    <li data-id="9"><a href="javascript:">9期</a></li>
                                    <li data-id="12"><a href="javascript:">12期</a></li>
                                </ul>
                            </dd>
                        </dl><!-- select end -->
                        <em class="tal">&nbsp;</em>
                    </span>
                      </div>
                      <div class="btn-group mb-30">
                          <button type="button" id="bormonbtn" class="btn btn-error mt-0">计算</button>
                          <a href="javascript:;" class="btn btn-gray mt-0 closejsq">关闭</a>
                      </div>
                      <!-- bbordetail start -->
                      <div class="bbordetail borrd2">
                          <div class="bbdil">
                              <p class="tac firstb">计算结果</p>
                              <p class="clearFloat"><span class="floatLeft">出借金额</span><span class="floatRight"><i id="balance">100.00</i>元</span></p>
                              <p class="clearFloat"><span class="floatLeft">获得利息</span><span class="floatRight"><i id="interest">100.00</i>元</span></p>
                              <p class="clearFloat"><span class="floatLeft">回款总额</span><span class="floatRight"><i id="all" class="c_red">100.00</i>元</span></p>
                              <p class="tac firstb"><a href="javascript:void(0);" onclick="detailShow('0');">查看详细&gt;</a></p>
                              <p> <a  href="${ctx}/finance/list" id="subbor" class="btn btn-error">我要出借</a></p>
                          </div>
                      </div><!-- bbordetail end -->
                  </div><!-- borjs end -->
                  <form id="data_jsq" method="post"  target="_blank">
                      <input type="hidden" id="data_balance" name="balance"/>
                      <input type="hidden" id="data_annualRate" name="annualRate"/>
                      <input type="hidden" id="data_months" name="months"/>
                      <input type="hidden" id="data_method" name="method"/>
                  </form>
                  <!-- loanjs start -->
                  <div class="borjs tab display-none" id="bor2">

                      <div class="clearFloat ifselct" >
                    <span class="input-group tal floatLeft ml-60">
                        <i class="ipt_i floatLeft">请输入借款金额：</i><label><input type="text" placeholder="输入借款金额" id="loanmoney" class="ipt-input" /></label>
						<br /><em class="floatLeft tal mt-5 ml-113">&nbsp;</em>
                    </span>

                          <div class="clear"></div>
                    <span>
                        <!-- select start -->
                        <dl class="select" id="lrate_Type">
                            <dt id="lrateType" data-id="0" style="width:126px;">付息类型</dt>
                            <dd>
                                <ul style="width:148px;">
                                    <li data-id="1"><a href="javascript:;">等额本金</a></li>
                                    <li data-id="2"><a href="javascript:;">等额本息</a></li>
                                    <li data-id="3"><a href="javascript:;">周期付息到期还本</a></li>
                                </ul>
                            </dd>
                        </dl><!-- select end -->
                         <em class="tal">&nbsp;</em>
                    </span>
                    <span>
                        <!-- select start -->
                        <dl class="select" id="lbrate">
                            <dt id="lannualRate" data-id="0">选择借款利率</dt>
                            <dd>
                                <ul>
                                    <%--<c:forEach items="${profitRate}" var="profit">--%>
                                    <%--<li data-id="${profit.profitRate}"><a href="javascript:">${profit.profitRate}%</a></li>--%>
                                    <%--</c:forEach>--%>
                                        <li data-id="6.5"><a href="javascript:">6.5%</a></li>
                                        <li data-id="7"><a href="javascript:">7%</a></li>
                                    <li data-id="7.5"><a href="javascript:">7.5%</a></li>
                                    <li data-id="8"><a href="javascript:">8%</a></li>
                                    <li data-id="9"><a href="javascript:">9%</a></li>
                                    <li data-id="10"><a href="javascript:">10%</a></li>
                                    <li data-id="11"><a href="javascript:">11%</a></li>
                                    <li data-id="12"><a href="javascript:">12%</a></li>
                                </ul>
                            </dd>
                        </dl><!-- select end -->
                         <em class="tal">&nbsp;</em>
                    </span>
                    <span>
                      <!-- select start -->
                      <dl class="select" id="mdata">
                          <dt id="dueTime"  data-id="0">选择借款期数</dt>
                          <dd>
                              <ul>
                                  <%--<c:forEach items="${dueTimeMonth}" var="dueTime">--%>
                                  <%--<li data-id="${dueTime.dueTime}"><a href="javascript:">${dueTime.dueTime}期</a></li>--%>
                                  <%--</c:forEach>--%>
                                  <li data-id="1"><a href="javascript:">1期</a></li>
                                  <li data-id="3"><a href="javascript:">3期</a></li>
                                  <li data-id="6"><a href="javascript:">6期</a></li>
                                  <li data-id="9"><a href="javascript:">9期</a></li>
                                  <li data-id="12"><a href="javascript:">12期</a></li>
                              </ul>
                          </dd>
                      </dl><!-- select end -->
              		  <em class="tal">&nbsp;</em>
                    </span>
                      </div>
                      <div class="btn-group mb-30">
                          <button type="button" id="monborr" class="btn btn-error mt-0">计算</button>
                          <a href="javascript:"  class="btn btn-gray mt-0 closejsq">关闭</a>
                      </div>              <!-- bbordetail start -->
                      <div class="bbordetail bor2">
                          <div class="bbdil">

                              <p class="tac firstb">计算结果</p>
                              <p class="clearFloat"><span class="floatLeft">借款金额</span><span class="floatRight"><i id="loan_balance">100.00</i>元</span></p>
                              <p class="clearFloat"><span class="floatLeft">借款总成本（利息）</span><span class="floatRight"><i id="fee">100.00</i>元</span></p>
                              <%--<p class="clearFloat"><span class="floatLeft">月还款额</span><span class="floatRight"><i id="monthBalance" class="c_red">100.00</i>元</span></p>--%>
                              <p class="clearFloat"><span class="floatLeft">还款总额</span><span class="floatRight"><i id="allBalance" class="c_red">100.00</i>元</span></p>
                              <p class="tac firstb"><a href="javascript:void(0);" onclick="detailShow('1');">查看详细&gt;</a></p>
                              <%--<p class="tac"> <a href="${ctx}/finance/list" id="subbor2" class="btn btn-error">我要借款</a></p>--%>
                          </div>
                      </div><!-- bbordetail end -->
                  </div><!-- loanjs end -->
                  <!-- bbordetail start -->
                  <div class="hengtiao">
                      <img src="${ctx }/images/jsq_07.jpg" />
                  </div>
                  <!-- bbordetail end -->
              </div>








            </div>
        </li>
        <li class="lPerson"><em class="eml12"><i class="il12"></i></em>
            <c:if test="${!isLogined}">
                <ul class="ulPersonInfo dshow">
                    <li class="thimg"><img src="${ctx}/images/index_new/banner_Idx/index5_03.png" alt=""></li>
                    <li class="tha"><a href="#"></a>
                        <a href="#" class="thloginBtn" data-mask='mask' data-id="login" ><img src="${ctx}/images/index_new/banner_Idx/index_new6_03.jpg" alt=""></a>
                    </li>
                </ul>
            </c:if>
            <c:if test="${isLogined}">
                <ul class="ulPersonInfo dshow">
                    <li class="ulfirst clearFloat"><img src="${ctx}/images/index_new/banner_Idx/index5_03.png" /><a href="#">${sessionScope.currentUser.loginName}</a></li>
                    <li><a href="${ctx }/person/account/overview">账户余额<br /><font color="#e72d4c"> <fmt:formatNumber value="${cashAccount.value2}" pattern="#,##0.00"/> </font>元</a></li>
                    <li><a href="${ctx}/person/to_lendorder_list">我的订单</a></li>
                    <li class="lrestuxi"><a href="${ctx}/person/toIncome" class="ared">充值</a><a href="${ctx}/person/toWithDraw">提现</a></li>
                    <li class="llast"><a href="${ctx}/user/logout">退出</a></li>
                </ul>
            </c:if>

        </li>
<c:if test="${isLogined}">

            <li class="lMassage"><em class="eml12"><a href="${ctx }/message/toUserMessage"><i class="il12"><label id="mcount"></label></i></a></em>
            </li>
</c:if>
        <li class="lTop display-none" id="lTop"><em class="eml12"><i class="il12"></i></em></li>
    </ul>
</div>
<!-- dSidebar end -->

<script>
    //当滚动条的位置处于距顶部100像素以下时，跳转链接出现，否则消失
    $(function () {
        $(window).scroll(function(){
            if ($(window).scrollTop()>100){
                $("#lTop").fadeIn(500);
            }
            else{
                $("#lTop").fadeOut(500);
            }
        });
        //当点击跳转链接后，回到页面顶部位置
        $("#lTop").click(function(){
            $('body,html').animate({scrollTop:0},1000);
            return false;
        });
        $(".uSide>li").hover(function(){
            $(this).children("em").css("background-image","url(${ctx}/images/index_new/icon1.png)");
            $(this).children(".dshow").show();
        },function(){
            $(this).children("em").css("background-image","none");
            $(this).children(".dshow").hide();

        });
    });
    
    // 百度统计
/*     var _hmt = _hmt || [];
	(function() {
	  var hm = document.createElement("script");
	  hm.src = "https://hm.baidu.com/hm.js?a170dfa53a64a17e8bc1477c5d7f9ac6";
	  var s = document.getElementsByTagName("script")[0]; 
	  s.parentNode.insertBefore(hm, s);
	})(); */

</script>
                