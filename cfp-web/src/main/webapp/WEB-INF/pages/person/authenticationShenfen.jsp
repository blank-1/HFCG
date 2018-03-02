<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="${ctx }/js/infor_5.js"></script><!-- action javascript -->
<!-- masklayer start  -->
<div class="masklayer" id="shenfen">

    <h2 class="clearFloat"><span>身份验证</span><a href="javascript:;" data-id="close"></a></h2>
    <div class="shenf_yanz_main" id="shenf_hide">
      <form action="" class="form" method="post">
        <div class="input_box">
          <div style="height:30px;clear:both;"></div>
          <label>
            <span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</span>
            <input type="text" id="sf_name" value="请输入身份证登记姓名" maxlength="" onfocus="if(this.value==defaultValue) {this.value='';this.type=''}" onblur="if(!value) {value=defaultValue; this.type='text';}" autocomplete="off" />
          </label>
          <em></em>
        </div>
        <div class="input_box">
          <label>
            <span>身份证号</span>
            <input type="text" id="sf_card" value="请输入身份证号" maxlength="19" onfocus="if(this.value==defaultValue) {this.value='';this.type=''}" onblur="if(!value) {value=defaultValue; this.type='text';}" autocomplete="off" />
          </label>
          <em></em>
        </div>
        <div id="idenhelp" style="width:435px;text-align:right;display: none;"><a href="http://help.caifupad.com/guide/common/reg/" target="_black" style="text-decoration:none;">认证出现问题？</a></div>
      	<div style="height:20px;clear:both;"></div>	
        <div class="input_box">
          <button type="button" id="sf_submit" >验证</button>
          <div style="height:70px;clear:both;"></div>
        </div>
      </form>
    </div>
    <div class="shenf_yanz_main" id="shenf_show" style="display:none;">
      <div style="height:70px;clear:both;"></div>
      <p><img src="${ctx }/images/img/true.jpg" style=""/><span>身份验证已通过！</span></p>
      <div style="height:50px;clear:both;"></div>
      <div class="input_box_shenf">
        <a href="javascript:;" data-id="close" id="queren"><button>确认</button></a>
        <div style="height:70px;clear:both;"></div>
      </div>
    </div>

</div>
<!-- masklayer end -->