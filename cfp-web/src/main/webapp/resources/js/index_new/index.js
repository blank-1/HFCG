$(document).ready(function(){

    /**
     * @Author                           CXM
     * @DateTime2015-12-11T10:51:18+0800
     * @param                            {mouse}
     * @return                           {mouse}
     * 平台新闻 常见问题 切换
     */
    $(".stab").click(function(){
        $(this).addClass("saction").siblings("span").removeClass("saction");
        var id=$(this).attr("data-id");
        $("#"+id).siblings("div").hide();
        $("#"+id).fadeIn(500);
    });

    /**
     * @Author                           CXM
     * @DateTime2015-12-11T10:58:33+0800
     * @param                            {[type]}
     * @return                           {[type]}
     * 鼠标第一次点击视频播放按钮时悬浮图片隐藏
     */
    $("#iVedio").click(function(){
        $(".zhezhao1").show();
        $("#vediogroup").show();
    });



    bottomB();
    $(window).resize(function(){
        bottomB();
    });
    /**
     * @Author                           CXM
     * @DateTime2015-12-11T10:56:09+0800
     * @return                           {[type]}
     * 总在最底部  兼容火狐
     */
    function bottomB(){
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
        var b_width;//文档宽度
        var liu_width;//浏览器宽度
        var _ie9=true,_ie8=true;
        if (Sys.ie){

            if(parseInt(Sys.ie)<9){

                _ie9=false;
                if(parseInt(Sys.ie)<=8){
                    _ie8=false;
                }else{
                    _ie8=true;
                }
            }else{
                _ie9=true;
            }

            banben = document.body.clientHeight;
            liulanqi =  document.documentElement.clientHeight ;
            b_width=document.body.clientWidth;
            liu_width=document.documentElement.clientWidth;
        }else if (Sys.firefox){
            banben =document.body.clientHeight ;
            liulanqi = document.documentElement.clientHeight ;
            b_width=document.body.clientWidth;
            liu_width=document.documentElement.clientWidth;
        }else if (Sys.chrome){
            banben = document.body.clientHeight;
            liulanqi =  document.documentElement.clientHeight ;
            b_width=document.body.clientWidth;
            liu_width=document.documentElement.clientWidth;
        }else if (Sys.opera){
            banben = document.documentElement.clientHeight ;
            liulanqi = document.body.clientHeight ;
            b_width=document.body.clientWidth;
            liu_width=document.documentElement.clientWidth;
        }else if (Sys.safari){
            banben = document.body.clientHeight;
            liulanqi =  document.documentElement.clientHeight;
            b_width=document.body.clientWidth;
            liu_width=document.documentElement.clientWidth;
        }else{
            banben = document.body.clientHeight;
            liulanqi =  document.documentElement.clientHeight ;
            b_width=document.body.clientWidth;
            liu_width=document.documentElement.clientWidth;
        }

        if(_ie8){
                //banner全屏
                var indexheight=$(".myimgid").height();
                var liuheight=b_width/5.4;

                if(liuheight>=350||indexheight>=350||liuheight<10){
                    liuheight=350;
                    indexheight=350;

                }

                if($(".myimgid").height()==0){
                    $(".index_focus").height(liuheight);
                }else{

                 $(".index_focus").height(indexheight);

                }

                if(b_width>=755 ){
                    if($(".myimgid").height()==0){

                      $(".sRollNotice .dEye").height(liuheight);
                    }else{

                      $(".sRollNotice .dEye").height(indexheight);
                    }
                }else{
                    $(".sRollNotice .dEye").height(350);
                }

        }else{
            $(".index_focus img").height(350);
        }if(b_width<=992 ){
            $(".header .dNav").slideUp(500);
        }else{

            $(".header .dNav").slideDown(500);
        }

        $(".index_focus").css("width","100%");
        /**
         * @Author                           CXM
         * @DateTime2015-12-11T11:00:45+0800
         * @param                            {992}
         * @return                           {[type]}
         * 屏幕大于992 和 屏幕小于992时的右侧边栏 细节
         */
        if(b_width<=767 ){
            $(".dSidebar").addClass("dSidebars");
            $(".uSide>li").children("em").addClass("background");
            $(".uSide>li").find(".dshow").show();
            $(".uSide>li").hover(function(){
                $(this).children("em").addClass("background");
                $(this).find(".dshow").show();
            },function(){
                $(this).children("em").addClass("background");
                $(this).find(".dshow").show();

            });

        }else{
            //放小后导航栏
            $(".dSidebar").removeClass("dSidebars");
                $(".uSide>li").children("em").removeClass("background");
                $(".uSide>li").find(".dshow").hide();
            $(".uSide>li").hover(function(){
                $(this).children("em").addClass("background");
                $(this).find(".dshow").show();
            },function(){
                $(this).children("em").removeClass("background");
                $(this).find(".dshow").hide();

            });
        }


        /**
         * @Author                           CXM
         * @DateTime2015-12-11T11:02:00+0800
         * @param                            {[type]}
         * @return                           {[type]}
         * 投标列表 提示文字显示或隐藏
         */
        $(".uFinList .lthree").each(function(){
            var t_html=$(this).html();


              if(_ie9){
                    if(b_width<=767 ){
                        $(".eyearm").html("年化利率： ");
                        if(t_html.indexOf('期限：')!=-1){

                            $(this).html(t_html);

                        }else{

                            $(this).html("期限："+t_html);

                        }
                    }else{

                        $(".eyearm").html("");

                        if(t_html.indexOf('期限：')!=-1){

                         $(this).html(t_html.substring(3));

                        }else{
                            $(this).html(t_html);

                        }
                    }
                    if(b_width<=357 ){
                        $(".eyearm").html("");
                    }
                } else{

                if(t_html.indexOf('期限：')!=-1){

                    $(this).html(t_html.substring(3));

                }else{
                    $(this).html(t_html);

                }
            }
        });

    }
    
    
  //首页进度条
	$('div.lendimm ul.uFinList').mouseenter(function(){
		var _thisAnmation = $(this).find("li.anmation"); 
		var ratePersent = _thisAnmation.attr("data-value") ;
		_thisAnmation.show();		
		_thisAnmation.find('.tiao').animate({
			"width":ratePersent+"%"	
		},800,function(){
			_thisAnmation.find('.rigval_cl').css("left",parseFloat(ratePersent)+5+"%").fadeIn();	
			if(parseFloat(ratePersent) < 70){
				_thisAnmation.find('.rigval_cl').removeClass("rigval2").addClass("rigval");
			}else{
				_thisAnmation.find('.rigval').removeClass("rigval").addClass("rigval2");
			}
			_thisAnmation.find('.numval').fadeIn(300);
		});
		
	});	
	$('div.lendimm ul.uFinList').mouseleave(function(){		
		var _thisAnmation = $(this).find("li.anmation"); 
		_thisAnmation.find('.tiao').animate({
			"width":"0%"	
		});
		_thisAnmation.find('.rigval_cl').css("left","0%").fadeOut();
		_thisAnmation.find('.numval').fadeOut();
		_thisAnmation.hide();		
	});	

})
