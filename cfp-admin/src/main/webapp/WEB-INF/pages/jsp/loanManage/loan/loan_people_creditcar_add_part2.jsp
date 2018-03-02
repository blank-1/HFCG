<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="loan_add_part2" class="container-fluid" style="padding: 10px 0px 0px 10px;width:50%;">
    <input style="float: right;width: 100px" type="button" class="btn  btn-primary" value="保 存"
           onclick="saveButtonAdd2();">
    <form class="form-horizontal" id="loan_add_part2_form" method="post">
        <input type="hidden" id="loanApplicationId" name="loanApplicationId" value="${loanApplicationId}">
        <input type="hidden" id="loanApplicationName" name="loanApplicationName">
        <table width="100%">
            <tr>
                <td>
                    <!-- 借款-开始 -->
                    <h4>借款信息</h4>
                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>借款用途：</label>
                        <div class="controls">
                            <input style="width: 200px" id="loanUseage" name="loanUseage">
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>借款用途描述：</label>
                        <div class="controls">
                            <textarea style="width: 450px; height: 100px;"
                                      class="easyui-validatebox" required="true" validType="length[2,200]"
                                      name="loanUseageDesc" id="loanUseageDesc"></textarea>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>借款产品：</label>
                        <div class="controls">
                            <input name="loanProductId" id="loanProductId" style="width: 200px">
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">年利率：</label>
                        <div class="controls">
                            <span style="font-size: 12px;" id="annualRate"></span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">期限：</label>
                        <div class="controls">
                            <span style="font-size: 12px;" id="dueTime"></span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">还款方式：</label>
                        <div class="controls">
                            <span style="font-size: 12px;" id="repaymentType"></span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>借款金额：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px"
                                   class="easyui-numberbox" required="true" validType="length[2,10]"
                                   name="loanBalance" id="loanBalance"><a style="font-size: 12px;cursor: pointer;"
                                                                          id="showRepaymentPlan"
                                                                          onclick="showRepaymentPlan();">查看还款计划</a>
                            <br/><br/>
                            <table id="feesItemList" class="table table-bordered table-condensed"
                                   style="font-size: 12px;width: 300px;">

                            </table>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>描述：</label>
                        <div class="controls">
                        <textarea style="width: 450px; height: 100px;"
                                  class="easyui-validatebox" required="true" validType="length[2,200]"
                                  name="applicationDesc" id="applicationDesc"></textarea>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">线下编号：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px;margin-top: 3px;"
                                   class="easyui-validatebox" validType="length[0,30]"
                                   name="offlineApplyCode" id="offlineApplyCode">
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">现住址：</label>
                        <div class="controls" >
                            <input name="residenceAddr_provence" id="residenceAddr_provence" style="width: 130px" >
                            <span style="font-size: 12px;">省</span>
                            <input name="residenceAddr_city" id="residenceAddr_city" style="width: 130px" >
                            <span style="font-size: 12px;">市</span>
                            <input name="residenceAddr_district" id="residenceAddr_district" style="width: 130px" >
                            <span style="font-size: 12px;">区/县</span><br/>
                        </div>
                    </div>

                    <!-- 借款人信息-->


                    <div class="control-group">
                        <label class="control-label">借款人姓名：</label>
                        <div class="controls">
                            <span style="font-size: 12px;" id="">${trueName}</span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">借款人身份证号：</label>
                        <div class="controls">
                            <span style="font-size: 12px;" id="">${idCard}</span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">性别：</label>
                        <div class="controls">
                            <span style="font-size: 12px;" id="">${sex}</span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">出生日期：</label>
                        <div class="controls">
                            <span style="font-size: 12px;" id="">${birthday}</span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>借款人手机：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px"
                                   class="easyui-numberbox" required="true" validType="length[0,11]"
                                   name="mobilePhone" id="mobilePhone" value="${userInfoExt.mobileNo}" readonly="readonly">
                        </div>
                    </div>



                    <div class="control-group">
                        <label class="control-label">月均收入：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px"
                                   class="easyui-numberbox" validType="length[0,10]"
                                   name="monthlyIncome" id="monthlyIncome">
                        </div>
                    </div>



                    <div class="control-group">
                        <label class="control-label">现住址：</label>
                        <div class="controls">
                            <input type="text" style="width: 440px;margin-top: 3px;" class="easyui-validatebox"
                                   validType="length[2,50]"
                            name="residenceAddr_detail" id="residenceAddr_detail">
                        </div>
                    </div>

                    <!-- 借款-结束 -->

                    <hr><!-- ----------------------------------抵押信息开始------------------------------------------- -->

                    <h3>车辆信息</h3>



                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>车辆型号：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px;margin-top: 3px;"
                                   class="easyui"   precision="0"
                                   name="carModel" id="carModel"><span style="font-size: 12px;"></span>
                        </div>
                    </div>


                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>行驶里程：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px;margin-top: 3px;"
                                   class="easyui-numberbox"   precision="0"
                                   name="mileage" id="mileage"><span style="font-size: 12px;">公里</span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>购买时间：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px;"
                                   class="easyui-datebox"
                                   name="buyTime" id="buyTime">
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>产品金额：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px;"
                                   class="easyui-numberbox" validType="length[0,6]" precision="1"
                                   name="carMoney" id="carMoney"><span style="font-size: 12px;">万元</span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>市场评估价格：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px;"
                                   class="easyui-numberbox" validType="length[0,6]" precision="1"
                                   name="appraisal" id="appraisal"><span style="font-size: 12px;">万元</span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>市场价格：</label>
                        <div class="controls">
                            <input type="text" style="width: 200px;"
                                   class="easyui-numberbox" validType="length[0,6]" precision="1"
                                   name="originalPrice" id="originalPrice"><span style="font-size: 12px;">万元</span>
                        </div>
                    </div>




                    <!-- ---------------------------------------抵押信息结束-------------------------------------- -->


                    <!-- 风控-开始 -->
                    <h4>风控步骤</h4>

                    <div class="control-group">
                        <label class="control-label"><span style="color: red;">*</span>风控步骤：</label>
                        <div class="controls">
                        <textarea style="width: 450px; height: 100px;"
                                  class="easyui-validatebox" required="true" validType="length[2,1500]"
                                  name="riskControlInformation" id="riskControlInformation"></textarea>
                        </div>
                    </div>
                    <!-- 风控-结束 -->

                    <hr><!-- ----------------------------------------------------------------------------- -->



                    <hr><!-- ----------------------------------------------------------------------------- -->


                    <hr id="contacts_end">
                    <!-- ----------------------------------------------------------------------------- -->

                    <!-- 银行卡-开始 -->
                    <h4>银行卡信息</h4>

                    <!-- 借款标情况-开始 -->
                    <c:if test="${empty customerCard }"><!-- 后台用户，无卡 -->
                    <div id="div_loanMark">
                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>银行：</label>
                            <div class="controls">
                                <input style="width: 200px" id="bankCode" name="bankCode">
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>开户行：</label>
                            <div class="controls">
                                <input type="text" style="width: 355px;"
                                       class="easyui-validatebox" required="true" validType="length[0,50]"
                                       name="registeredBank" id="registeredBank">
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>卡号：</label>
                            <div class="controls">
                                <input type="text" style="width: 200px;"
                                       class="easyui-validatebox" required="true" validType="length[0,30]"
                                       name="cardCode" id="cardCode">
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>开户名：</label>
                            <div class="controls">
                                <input type="text" style="width: 200px"
                                       class="easyui-validatebox" required="true" validType="length[0,30]"
                                       name="cardCustomerName" id="cardCustomerName">
                            </div>
                        </div>
                    </div>
                    </c:if>

                    <c:if test="${not empty customerCard }"><!-- 普通用户，已经有银行卡 -->
                    <div id="div_loanMark">
                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>银行：</label>
                            <div class="controls">
                            <%--    <input type="hidden" style="width: 200px" id="bankCode" name="bankCode"
                                       value="${customerCard.bankCode }">
                             &lt;%&ndash;   <input type="text" style="width: 200px" id="bankName" name="bankName"
                                       value="${bankName }" disabled="disabled">&ndash;%&gt;--%>
                                <c:if test="${customerCard.bankCode  eq '403'}">  <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国邮政储蓄银行股份有限公司" disabled="disabled">
                                </c:if>

                                <c:if test="${customerCard.bankCode  eq '102'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国工商银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '103'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国农业银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '104'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '105'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国建设银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '301'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="交通银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '302'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中信银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '303'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国光大银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '304'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="华夏银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '305'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国民生银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '306'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="广东发展银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '307'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="平安银行股份有限公司" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '308'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="招商银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '309'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="兴业银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '310'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="上海浦东发展银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '319'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="徽商银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '313'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="其他城市商业银行 " disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '314'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="其他农村商业银行" disabled="disabled"></c:if>
                                <c:if test="${customerCard.bankCode  eq '315'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="恒丰银行" disabled="disabled"></c:if>

                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>开户行：</label>
                            <div class="controls">
                                <input type="text" style="width: 355px;"
                                       class="easyui-validatebox" required="true" validType="length[0,50]"
                                       name="registeredBank" id="registeredBank"
                                       value="${customerCard.registeredBank }">
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>卡号：</label>
                            <div class="controls">
                                <input type="hidden" style="width: 200px;" name="cardCode" id="cardCode"
                                       value="${customerCard.cardCode }">
                                <input type="text" style="width: 200px;" name="cardCodeShow" id="cardCodeShow"
                                       value="${customerCard.cardCode }" disabled="disabled">
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>开户名：</label>
                            <div class="controls">
                                <input type="hidden" style="width: 200px" name="cardCustomerName" id="cardCustomerName"
                                       value="${customerCard.cardCustomerName }">
                                <input type="text" style="width: 200px" name="cardCustomerNameShow"
                                       id="cardCustomerNameShow" value="${customerCard.cardCustomerName }"
                                       disabled="disabled">
                            </div>
                        </div>
                    </div>
                    </c:if>
                    <!-- 借款标情况-结束 -->

                    <!-- 债权标情况-开始 -->
                    <div id="div_rightsMark">
                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>打款卡：</label>
                            <div class="controls">
                                <input name="inCardId" id="inCardId" style="width: 200px">
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label"><span style="color: red;">*</span>划扣卡：</label>
                            <div class="controls">
                                <input name="outCardId" id="outCardId" style="width: 200px">
                            </div>
                        </div>
                    </div>
                    <!-- 债权标情况-结束 -->
                    <!-- 银行卡-结束 -->
                </td>
            </tr>
        </table>
    </form>
    <input style="float: right;width: 100px" type="button" class="btn  btn-primary" value="保 存"
           onclick="saveButtonAdd2();">
</div>

<script type="text/javascript">

    // 执行:保存。
    function saveButtonAdd2() {

        // 验证
        if (!$("#loan_add_part2_form").form('validate')) {
            return false;
        }


        // 获取下列数值的文字信息。
        var loanUseage_Str = $("#loanUseage").combobox("getText");//借款用途
        var loanProductId_Str = $("#loanProductId").combobox("getText");//借款产品
        var dueTime_Str = $("#dueTime").html();//期限
        var loanApplicationName_Str = loanUseage_Str + '-' + loanProductId_Str + '-' + dueTime_Str;//借款合同名称
        $("#loanApplicationName").attr("value", loanApplicationName_Str);

        $.post('${ctx}/jsp/loanManage/loan/saveLoanPeopleCreditCarPart2?r=' + Math.random(),
            $("#loan_add_part2_form").serialize(),
            function (data) {
                if (data.result == 'success') {
                    $.messager.alert("操作提示", "保存成功！", "info");

                } else if (data.result == 'error') {
                    if (data.errCode == 'check') {
                        $.messager.alert("验证提示", data.errMsg, "info");
                    } else {
                        $.messager.alert("系统提示", data.errMsg, "info");
                    }
                } else {
                    $.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
                }
            }, 'json');

    }

    /* 借款信息-开始  */

    // 加载借款用途下拉框。
    $("#loan_add_part2_form #loanUseage").combobox({
        url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=loanUseage&parentConstant=0&selectedDisplay=selected',
        textField: 'CONSTANTNAME',
        valueField: 'CONSTANTVALUE'
    });

    // 借款产品下拉框。
    $("#loan_add_part2_form #loanProductId").combobox({
        url: '${ctx}/jsp/product/loan/loadLoanProduct?selectedDisplay=selected',
        textField: 'PRODUCTNAME',
        valueField: 'LOANPRODUCTID',
        onSelect: function (record) {
            // 展示详情开始
            $.post('${ctx}/jsp/product/loan/getLoanProductDetail',
                {
                    loanproductid: record.LOANPRODUCTID
                },
                function (data) {
                    if (data.result == 'success') {

                        // 单项填充
                        $("#annualRate").html(data.data.annualRate + '%');//年利率
                        $("#dueTime").html(data.data.dueTime);//期限时长
                        $("#repaymentType").html(data.data.repaymentType);//还款方式

                        // 列表填充
                        $("#feesItemList").empty();// 先清空。
                        $.each(data.data.feesItemList, function (n, v) {
                            var f = "<tr><td>" + v.itemType + "</td><td>" + v.chargeCycle + "</td><td>" + v.itemName + "</td></tr>";
                            $("#feesItemList").append(f);
                        });

                    } else if (data.result == 'error') {
                        $.messager.alert("系统提示", data.errMsg, "info");
                    } else {
                        $.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
                    }
                }, 'json');
            // 展示详情结束
        }
    });

    // 下面加载银行卡列表，如果是普通用户，则不加载。
    var bankCode = '${customerCard.bankCode}';
    if (null == bankCode || '' == bankCode) {
        //加载开户行下拉框。
        $("#loan_add_part2_form #bankCode").combobox({
            url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=bank&parentConstant=0&selectedDisplay=selected',
            textField: 'CONSTANTNAME',
            valueField: 'CONSTANTID'
        });
    }

    //加载省份下拉框【现住址】。
    $("#loan_add_part2_form #residenceAddr_provence").combobox({
        url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
        textField: 'PROVINCENAME',
        valueField: 'PROVINCEID',
        onSelect: function (record) {
            $("#loan_add_part2_form #residenceAddr_city").combobox("reload",
                '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
        }
    });
    //加载城市下拉框【现住址】。
    $("#loan_add_part2_form #residenceAddr_city").combobox({
        url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
        textField: 'CITYNAME',
        valueField: 'CITYID',
        onSelect: function (record) {
            var provinceId = $("#residenceAddr_provence").combobox("getValue");
            $("#loan_add_part2_form #residenceAddr_district").combobox("reload",
                '${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
        }
    });
    //加载区县下拉框【现住址】。
    $("#loan_add_part2_form #residenceAddr_district").combobox({
        url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
        textField: 'CITYNAME',
        valueField: 'CITYID'
    });

    /* 借款信息-结束  */

    /* 工作信息-开始 */

    /* 工作信息-结束 */

    /* 联系人-开始 */
    // 执行：添加联系人。
    var i = 0;

    function onContacts_plus() {
        i += 1;
        var c = "<div class='control-group' id='contacts_" + i + "'>" +
            "<label class='control-label'>关系：</label>" +
            "<div class='controls' >" +
            "<input style='width: 100px' name='relationType_" + i + "' id='relationType_" + i + "'>&nbsp;" +
            "<input style='width: 100px' name='relation_" + i + "' id='relation_" + i + "'>" +
            "<span style='font-size: 12px;'>&nbsp;姓名：&nbsp;&nbsp;</span>" +
            "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[2,50]' " +
            "name='concactName_" + i + "' id='concactName_" + i + "'>" +
            "<span style='font-size: 12px;'>&nbsp;&nbsp;手机号：&nbsp;</span>" +
            "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[2,50]' " +
            "name='concatPhone_" + i + "' id='concatPhone_" + i + "'>" +
            "<i class='icon-trash' style='margin-left: 5px;cursor: pointer;' title='删除联系人' onclick='onContacts_trash(" + i + ");'></i>" +
            "</div>" +
            "</div>";
        $("#contacts_end").before(c);




    }

    // 执行：删除联系人。
    function onContacts_trash(contacts_num) {
        $("#contacts_" + contacts_num).remove();
    }

    /* 联系人-结束 */

    /* 银行卡信息-开始 */
    // 执行：银行卡初始化事件。
    function intoMark() {
        if (1 == ${subjectType}) {// 借款标
            $("#div_loanMark").show();
            $("#div_rightsMark").hide();
        } else {// 债权标
            $("#div_loanMark").hide();
            $("#div_rightsMark").show();
        }
    }

    // 执行：改变银行卡表单验证规则。
    function intoValidate() {
        if (1 == ${subjectType}) {
            // 去掉
            // inCardId
            // outCardId
        } else {
            // 去掉
            // bankCode
            // registeredBank
            $('#registeredBank').validatebox({
                required: false
            });
            // cardCode
            $('#cardCode').validatebox({
                required: false
            });
            // cardCustomerName
            $('#cardCustomerName').validatebox({
                required: false
            });
        }
    }

    if (1 != ${subjectType}) {
        //加载打款卡下拉框。
        $("#loan_add_part2_form #inCardId").combobox({
            url: '${ctx}/jsp/custom/customer/loadCustomerCard?selectedDisplay=selected&originalUserId=${originalUserId}',
            textField: 'CARDCUSTOMERNAME',
            valueField: 'CUSTOMERCARDID'
        });

        //加载划扣卡下拉框。
        $("#loan_add_part2_form #outCardId").combobox({
            url: '${ctx}/jsp/custom/customer/loadCustomerCard?selectedDisplay=selected&originalUserId=${originalUserId}',
            textField: 'CARDCUSTOMERNAME',
            valueField: 'CUSTOMERCARDID'
        });
    }

    // 展示还款计划列表
    function showRepaymentPlan() {
        if ($("#loanProductId").combobox("getValue") && $("#loanBalance").val()) {
            $("#showRepaymentPlan").after("<div id='repaymentPlan' style=' padding:10px; '></div>");
            $("#repaymentPlan").dialog({
                resizable: false,
                title: '还款计划',
                href: '${ctx}/jsp/loanManage/loan/toShowRepaymentPlan?loanProductId=' + $("#loanProductId").combobox("getValue") + '&balance=' + $("#loanBalance").val(),
                width: 600,
                modal: true,
                height: 300,
                top: 100,
                left: 400,
                buttons: [
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#repaymentPlan").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $("#repaymentPlan").dialog('destroy');
                }
            });
        } else {
            $.messager.alert("验证提示", "请选择借款产品并填写借款金额", "info");
        }
    }

    /* 银行卡信息-结束 */

    $(function () {
        intoMark();
        intoValidate();
    });

</script>
</body>
</html>
