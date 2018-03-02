<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="masklayer" id="hengfengCard">

    <div class="shenf_yanz_main" id="shenf_hide">
        <form id="hf_form" class="form" method="post" target="_blank">

            <input type="hidden" id="ver" name="ver" value=""/>
            <input type="hidden" id="mchnt_cd" name="mchnt_cd" value=""/>
            <input type="hidden" id="mchnt_txn_ssn" name="mchnt_txn_ssn" value=""/>
            <input type="hidden" id="user_id_from1" name="user_id_from" value=""/>
            <input type="hidden" id="mobile_no1" name="mobile_no" value=""/>
            <input type="hidden" id="cust_nm1" name="cust_nm" value=""/>
            <input type="hidden" id="certif_tp" name="certif_tp" value=""/>
            <input type="hidden" id="certif_id1" name="certif_id" value=""/>
            <input type="hidden" id="email" name="email" value=""/>
            <input type="hidden" id="city_id" name="city_id" value=""/>
            <input type="hidden" id="parent_bank_id" name="parent_bank_id" value=""/>
            <input type="hidden" id="bank_nm" name="bank_nm" value=""/>
            <input type="hidden" id="capAcntNo1" name="capAcntNo" value=""/>
            <input type="hidden" id="page_notify_url" name="page_notify_url" value=""/>
            <input type="hidden" id="back_notify_url" name="back_notify_url" value=""/>
            <input type="hidden" id="signature" name="signature" value=""/>


        </form>
    </div>

</div>
<script type="text/javascript">

    function inputToValue(id,val){
        $("#"+id).val(val);
    }
    function toBind(id){

        var mobile=$("#mobile_no").val(),
            userId=$("#user_id_from").val(),
            cardNo=$("#capAcntNo").val(),
            realName=$("#cust_nm").val(),
            idCard=$("#certif_id").val();
        if(!/^[\u4E00-\u9FA5]|[\uFE30-\uFFA0]{2,10}$/.test(realName)) {
            alert("请输入正确的姓名");
            return false;
        }
        console.log("idcard====="+idCard);
        if(null!=idCard || ""!=idCard){
            if(!/(^\d{15}$)|(^\d{17}(\d|X)$)/.test(idCard)) {
                alert("请输入正确的身份证号码");
                return false;
            }
        }

        if(cardNo!=""||cardNo!=null){
            if(!/^\d{16,19}$/.test(cardNo)) {
                alert("请输入正确的银行卡号");
                return false;
            }
            $("#capAcntNo1").val(cardNo);
        }

            $("#mobile_no1").val(mobile);
            $("#user_id_from1").val(id);
            $("#cust_nm1").val(realName),
            $("#certif_id1").val(idCard);
        $.ajax({
            url:"${ctx}/jsp/custom/customer/signAccount",
            type:"post",
            data:{"mobile":mobile,"userId": userId,"cardNo": cardNo,"realName": realName,"idCard": idCard},
            success:function(data){
                $("#ver").val(data.ver);
                $("#mchnt_cd").val(data.mchnt_cd);
                $("#mchnt_txn_ssn").val(data.mchnt_txn_ssn);
                $("#certif_tp").val(data.certif_tp);
                $("#email").val(data.email);
                $("#city_id").val(data.city_id);
                $("#mobile_no1").val(data.mobile_no);
                $("#capAcntNo1").val(data.capAcntNo);
                $("#cust_nm1").val(data.cust_nm);
                $("#certif_id1").val(data.certif_id);

                $("#user_id_from").val(id);
                $("#parent_bank_id").val(data.parent_bank_id);
                $("#bank_nm").val(data.bank_nm);
                $("#page_notify_url").val(data.page_notify_url);
                $("#back_notify_url").val(data.back_notify_url);
                $("#signature").val(data.signature);
                console.log("============监控平台=======")
                $("#hf_form").attr("action",data.actionUrl);
                $("#hf_form").submit();
            }

        });

    }
</script>