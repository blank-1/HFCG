<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="${ctx}/js/login.js"></script><!-- public javascript -->
<!-- masklayer start  -->
<div class="masklayer" id="login" style="width:332px!important;">
    <div class="logind">
        <h2 class="clearFloat"><span class="font-size:18px; display:inline; float:left;">登录财富派</span> <a href="javascript:" class="logina" data-id="close"></a></h2>
        <!-- equity start -->
        <div class="login">
            <form action="" method="post" name="form" class="">
                <input id="pastUrl" name="pastUrl" type="hidden"  value="${pastUrl}"/>
                <div class="input-group">
                    <label for="unlogin">
                        <input type="text" id="unlogin1" maxlength="20" name="username1" style="color:#AAA;line-height:42px;" value="用户名/手机号" class="ipt-input unlogin"  />

                        <input type="text" id="unlogin" maxlength="20" name="loginName" value=""  class="ipt-input unlogin" style="display:none; line-height:42px;" />
                    </label>
                    <em class="hui"></em>
                </div>
                <div class="input-group clearFloat">
                    <label for="pwlogin">
                        <input type="text" id="pwlogin_txt" maxlength="16" style="color:#AAA;line-height:42px;" name="loginPass1" value="请您输入密码" autocomplete="off" class="ipt-input pwlogin_txt" 	 />
                        <input type="password" id="pwlogin" maxlength="16" name="loginPass" value="" autocomplete="off" class="ipt-input pwlogin" style="display:none;line-height:42px;"/>

                    </label>
                    <em class="passwordem floatLeft"></em><a class="write floatRight passworda"  href="${ctx}/user/rePasswordOne">忘记密码？</a>
                </div>
                <div class="btn-group">
                    <button type="button" id="submit-login" class="btn btn-error mt-0">登录</button>
                    <a class="write floatRight passworda" href="${ctx}/user/regist/home">注册账号</a>
                </div>
            </form>

        </div><!-- equity start -->
    </div>
</div>
<script>
    $(function(){
        //用户名
        var unlogin1 = $("#unlogin1"), unlogin = $("#unlogin");

        unlogin1.bind("focus",function(){

            if($(this).val() != "用户名/手机号") return;

            $(this).css("display","none");
            unlogin.css("display","block");
            unlogin.val("");
            unlogin.focus();
        });

        unlogin.bind("blur",function(){

            if($(this).val() != "") return;
            $(this).css("display","none");
            unlogin1.css("display","block");
            unlogin1.val("用户名/手机号");
        });
        //密码
        var pwlogin_txt = $(".pwlogin_txt"), pwlogin = $(".pwlogin");

        pwlogin_txt.bind("focus",function(){

            if($(this).val() != "请您输入密码") return;

            $(this).css("display","none");
            pwlogin.css("display","block");
            pwlogin.val("");
            pwlogin.focus();
        });

        pwlogin.bind("blur",function(){

            if($(this).val() != "") return;
            $(this).css("display","none");
            pwlogin_txt.css("display","block");
            pwlogin_txt.val("请您输入密码");
        });
    })//function end
</script>
<!-- masklayer end -->

<div class="zhezhao"></div>
<div class="zhezhao1"></div>

