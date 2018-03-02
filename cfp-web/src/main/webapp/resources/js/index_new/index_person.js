// JavaScript Document
$(function(){
    //点击计算机按钮时
    $("#jsqimg1").each(function(){
        var s=$(this);
        var jsq=$(".jsq");
        var _jsshow=function(){jsq.show();};  
        var _jshide=function(){
            jsq.hide();
            $(".ipt-input").each(function() {
                $(this).val("");
            });
            $(".borjs em").each(function() {
                $(this).html("&nbsp;");
            });
            $(".borjs input").each(function() {
                $(this).removeClass("ipt-error");
            });
            $(".hengtiao").parent().find(".bbordetail").slideUp(500);
            $("#rate_Type1").children("dt").attr("data-id","0").html("付息类型");
            $("#brate2").children("dt").attr("data-id","0").html("选择出借利率");
            $("#bdata2").children("dt").attr("data-id","0").html("选择出借期数");
            $("#mdata2").children("dt").attr("data-id","0").html("选择借款期数");
            $("#rate_Type2").children("dt").attr("data-id","0").html("付息类型");
        };
      
        s.click(function(){
           _jsshow();
        });
        
        $("body").click(function(i){
            if($(i.target).is($("a.closejsq"))){
              _jshide();
            }else if($(i.target).parents().is(s)||$(i.target).is(s)){
              _jsshow();
            }else if($(i.target).parents("#jsqdiv1").is(jsq)){
              _jsshow();
            }else if($(i.target).is(jsq)){
              _jsshow();
            }else{
              _jshide();
          }
        });
    });

    //点击计算按钮时
    $(".jsbtn").click(function(){
      $(this).parent().next().slideDown(500); 
    });
  
    //出借金额输入验证
    $("#borrmon1").blur(function(){
        var emHtml=$(this).parents(".ifselct").siblings("em");
        moneyf($(this),"0",emHtml,"请输入出借金额");
    });
  
    //出借利率验证
    function borratef(rate){
      var massage="";
      massage=" 请选择出借利率";
      if(rate.children("dt").attr("data-id")=="0"){
        rate.siblings("em").html(massage);
      }else{
        assage="&nbsp;";
        rate.siblings("em").html(massage);
      }
      return massage;
    }
    //出借利率验证
    function bordataf(bdata){
      var massage="";
      massage=" 请选择出借期数";
      if(bdata.children("dt").attr("data-id")=="0"){
        bdata.siblings("em").html(massage);
      }else{
        massage="&nbsp;";
        bdata.siblings("em").html(massage);
      }
      return massage;
    }
    //付息类型验证
    function rate_Typef(rate_T){
      var massage="";
      massage=" 请选择付息类型";
      if(rate_T.children("dt").attr("data-id")=="0"){
        rate_T.siblings("em").html(massage);
      }else{
        massage="&nbsp;";
        rate_T.siblings("em").html(massage);
      }
      return massage;
    }
    //出借按钮点击验证
    $("#bormonbtn3").click(function(){
      var emHtml=$("#borrmon1").parent().siblings("em");
      var t1=moneyf($("#borrmon1"),"1",emHtml,"请输入出借金额");
      var t2=borratef($("#brate"));
      var t3=bordataf($("#bdata"));
      var t4=rate_Typef($("#rate_Type1"));
        
      if(t1=="&nbsp;" && t2=="&nbsp;" && t3=="&nbsp;" && t4=="&nbsp;"){
        alert(11);
        $(".borrd2").slideDown(500);
        if($("#aboutid").val()=="0"){
          var timerId2 = null;
          itemShowmy($(".jsq"));  
        }
      }

    });
  
    function itemShowmy(_this){
      timerId2 = setInterval(function(){
        _this.animate({top: "20%" }, 500);
      }, 500);
    }
    //借款期数验证
    function mdataf(mdata){
      var massage="";
      massage=" 请选择借款期数";
      if(mdata.children("dt").attr("data-id")=="0"){
        mdata.siblings("em").html(massage);
      }else{
        massage="&nbsp;";
        mdata.siblings("em").html(massage);
      }
      return massage;
    }

    //借款付息类型验证
    function rateTypef(ratet){
      var massage="";
      massage=" 请选择付息类型";
      if(ratet.children("dt").attr("data-id")=="0"){
        ratet.siblings("em").html(massage);
      }else{
        massage="&nbsp;";
        ratet.siblings("em").html(massage);
      }
      return massage;
    }
    //借款利率验证
    function borrowRatef(rate){
      var massage="";
      massage=" 请选择借款利率";
      if(rate.children("dt").attr("data-id")=="0"){
        rate.siblings("em").html(massage);
      }else{
        massage="&nbsp;";
        rate.siblings("em").html(massage);
      }
      return massage;
    }
    //借款按钮点击验证
    $("#monborr2").click(function(){
        var emHtml=$("#loanmoney").parent().siblings("em");
        var t1=moneyf($("#loanmoney"),"1",emHtml,"请输入借款金额");
        var t2=mdataf($("#mdata2"));
        var t3=borrowRatef($("#borrowRate"));
        var t4=rateTypef($("#rateType"));
        if(t1=="&nbsp;" && t2=="&nbsp;" && t3=="&nbsp;" && t4=="&nbsp;"){
            $(".bor2").slideDown(500);
        }
    });

    //点击向上按钮时
    $(".hengtiao").click(function(){
        $(this).parent().find(".bbordetail").slideUp(500);
    });

  
    //帮助中心页面的总在底部
    function helpbot(){
      var Sys = {};
      var ua = navigator.userAgent.toLowerCase();
      var s;
      (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
      (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
      (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
      (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
      (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
      //以下进行测试
      var banben;//文档高度
      var liulanqi;//浏览器高度
      if (Sys.ie){
        banben = document.body.clientHeight;
        liulanqi =  document.documentElement.clientHeight ;
      }else if (Sys.firefox){
        banben =document.body.clientHeight ;
        liulanqi = document.documentElement.clientHeight ;
      }else if (Sys.chrome){
        banben = document.body.clientHeight;
        liulanqi =  document.documentElement.clientHeight ;
      }else if (Sys.opera){
        banben = document.documentElement.clientHeight ;
        liulanqi = document.body.clientHeight ;
      }else if (Sys.safari){
        banben = document.body.clientHeight;
        liulanqi =  document.documentElement.clientHeight ;
      }else{
        banben = document.body.clientHeight;
        liulanqi =  document.documentElement.clientHeight ;
      }
      if($(".footer").html()===undefined){
        
      }else{
        if(liulanqi<=banben ){
          $(".footer").css({"position":"inherit"});
        }else{
          var  myliu=0;
          if(liulanqi-banben>153){
            myliu=liulanqi-153;
          }else{
            myliu=liulanqi-(liulanqi-banben);
          }
          $(".footer").css({"position":"absolute","top":myliu,"width":"100%"});
        }
      }
    };
    
})

//借款金额输入验证
function moneyf(mond,mytext,emHtml,str1){
      if(undefined===emHtml){
          emHtml=mond.parent().siblings("em").html(massage);
      }
      var mondint;
      if(mond.val()==""){
        
          if(mytext=="0"){
              mondint=0;
              massage="&nbsp;";
              mond.removeClass("ipt-error");
              emHtml.html(massage);
              return massage;
          }else{
              massage=str1;
              mond.addClass("ipt-error");
              emHtml.html(massage);
              return massage;
            
          }
        
      }else{
          if(/^([0-9.]+)$/.test(mond.val())){
              mondint=parseInt(mond.val());
          }else{
              massage="请输入正确的数字";
              mond.addClass("ipt-error");
              emHtml.html(massage);
              return massage;
          }
        
      }
    
    if((mondint<100 || mondint % 100!=0) ){
        massage="请输入大于100 并且是100 倍数的金额！";
        mond.addClass("ipt-error");
        emHtml.html(massage);
    }else{
        if(parseInt($("#yue").html())<=mondint){
          
          massage="余额不足";
          
          mond.addClass("ipt-error");
          emHtml.html(massage);
        }else{
          massage="&nbsp;";
          
          mond.removeClass("ipt-error");
          emHtml.html(massage);
        }
  }
    return massage;
}

//帮助中心首页图标翻滚 
$(document).ready(function(){

    var timerId = null;
    function itemShow(_this){
      if (timerId) return;
      timerId = setInterval(function(){
        _this.find(".grun").children("span").eq(0).animate({top: "-110px" }, 200);
        _this.find(".grun").children("span").eq(1).animate({top: "-110px" }, 200);
      }, 200);
    }
    function itemHide(){
      if (!timerId) return;
      $(".current span").animate({top: "0px" }, 200);
      $(".usq_gr").removeClass("current");
      clearInterval(timerId);
      timerId = null;
    }
    $(".usq_gr").hover(function(){
      $(this).addClass("current");
      itemShow($(this));
    }, itemHide);


    //帮助中心---常见问题---右侧，点击按钮显示名词解释内容
    $(".hright>h2").click(function(){
      if($(this).find("i").hasClass("ibottom")){
        console.log("aa"); 
        $(this).next().slideUp(500,function(){
      helpbot();
        }).siblings("div").slideUp(500);
        $(this).find("i").removeClass("ibottom").addClass("itop").parent().siblings("h2").find("i").removeClass("ibottom").addClass("itop");
      
      }else{
        $(this).next().slideDown(500,function(){
      helpbot();
        }).siblings("div").slideUp(500);
        $(this).find("i").removeClass("itop").addClass("ibottom").parent().siblings("h2").find("i").removeClass("ibottom").addClass("itop");
      }
    });

});