<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="zhezhao1"></div>
<div class="masklayer" id="hengfengCard">
    <h2 class="clearFloat"><span>恒丰开户</span><a href="javascript:;" data-id="close"></a></h2>
    <div class="shenf_yanz_main" id="shenf_hide">
        <form id="hf_form" class="form" method="post" target="_blank">

            <input type="hidden" id="ver" name="ver" value=""/>
            <input type="hidden" id="mchnt_cd" name="mchnt_cd" value=""/>
            <input type="hidden" id="mchnt_txn_ssn" name="mchnt_txn_ssn" value=""/>
            <input type="hidden" id="user_id_from" name="user_id_from" value="${userExt.userId}"/>
            <input type="hidden" id="mobile_no" name="mobile_no" value="${userExt.mobileNo}"/>
            <input type="hidden" id="cust_nm" name="cust_nm" value="${userExt.realName}"/>
            <input type="hidden" id="certif_tp" name="certif_tp" value=""/>
            <input type="hidden" id="certif_id" name="certif_id" value="${userExt.idCard}"/>
            <input type="hidden" id="email" name="email" value=""/>
            <input type="hidden" id="city_id" name="city_id" value=""/>
            <input type="hidden" id="parent_bank_id" name="parent_bank_id" value=""/>
            <input type="hidden" id="bank_nm" name="bank_nm" value=""/>
            <input type="hidden" id="capAcntNo" name="capAcntNo" value="${cardCode}"/>
            <input type="hidden" id="page_notify_url" name="page_notify_url" value=""/>
            <input type="hidden" id="back_notify_url" name="back_notify_url" value=""/>
            <input type="hidden" id="signature" name="signature" value=""/>

            <div class="input_box">
                <div style="height:30px;clear:both;"></div>
                <label>
                    <span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</span>
                    <c:if test="${userExt.isVerified ne '0'}"><input readonly type="text" id="sf_name" name="" value="${userExt.realName}" autocomplete="off"/></c:if>
                    <c:if test="${userExt.isVerified eq '0'}"><input type="text" oninput="inputToValue('cust_nm',this.value)" id="sf_name" name="" value="" autocomplete="off" placeholder="请输入身份证登记姓名"/></c:if>

                </label>
                <em></em>
            </div>
            <div class="input_box">
                <label>
                    <span>身份证号</span>
                    <c:if test="${userExt.isVerified ne '0'}"><input readonly type="text" id="sf_card" value="${userExt.idCard}" maxlength="19"  autocomplete="off"/></c:if>
                    <c:if test="${userExt.isVerified eq '0' }"><input type="text" oninput="inputToValue('certif_id',this.value)" id="sf_card" value="" maxlength="19"  autocomplete="off" placeholder="请输入身份证号"/></c:if>
                </label>
                <em></em>
            </div>
            <div class="input_box">
                <label>
                    <span>银行卡号</span>
                    <c:if test="${not empty cardCode}"><input readonly type="text" value="${cardCode}" maxlength="19"  autocomplete="off"/></c:if>
                    <c:if test="${empty cardCode}"><input type="text" oninput="inputToValue('capAcntNo',this.value)" value="" maxlength="19"  autocomplete="off" placeholder="请输入银行卡号"/></c:if>
                </label>
                <em></em>
            </div>
            <div style="height:20px;clear:both;"></div>
            <div class="input_box">
                <button type="button" id="sf_submit" onclick="toBind()">提交</button>
                <button type="button" onclick="reloadFlush()">恒丰开户完成</button>
                <div style="height:70px;clear:both;"></div>
            </div>
        </form>
    </div>

</div>
<script type="text/javascript">
    function inputToValue(id,val){
        $("#"+id).val(val);
    }
    function reloadFlush(){
        location.href=location.href;
    }
    function toBind(){
        var mobile=$("#mobile_no").val(),
            userId=$("#user_id_from").val(),
            cardNo=$("#capAcntNo").val(),
            realName=$("#cust_nm").val(),
            idCard=$("#certif_id").val();
        if(!/^[\u4E00-\u9FA5]|[\uFE30-\uFFA0]{2,10}$/.test(realName)) {
            alert("请输入正确的姓名");
            return false;
        }
        if(!/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/.test(idCard)) {
            alert("请输入正确的身份证号码");
            return false;
        }
        if(!/^\d{16,19}$/.test(cardNo)) {
            alert("请输入正确的银行卡号");
            return false;
        }
        $.ajax({
            url:rootPath+"/api/openAccount/signAccount",
            type:"post",
            data:{"mobile":mobile,"userId": userId,"cardNo": cardNo,"realName": realName,"idCard": idCard},
            success:function(data){
                $("#ver").val(data.ver);
                $("#mchnt_cd").val(data.mchnt_cd);
                $("#mchnt_txn_ssn").val(data.mchnt_txn_ssn);
                $("#certif_tp").val(data.certif_tp);
                $("#email").val(data.email);
                $("#city_id").val(data.city_id);
                $("#parent_bank_id").val(data.parent_bank_id);
                $("#bank_nm").val(data.bank_nm);
                $("#page_notify_url").val(data.page_notify_url);
                $("#back_notify_url").val(data.back_notify_url);
                $("#signature").val(data.signature);
                $("#hf_form").attr("action",data.actionUrl);
                $("#hf_form").submit();
            }

        });

    }
</script>