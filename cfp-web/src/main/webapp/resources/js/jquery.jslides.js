/**
 * jQuery jslides 1.1.0
 *
 * http://www.cactussoft.cn
 *
 * Copyright (c) 2009 - 2013 Jerry
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 */
$(function(){
	var numpic = $('#slides li').size()-1;
	var nownow = 0;
	var inout = 0;
	var TT = 0;
	var SPEED = 5000;


	$('#slides li').eq(0).siblings('li').css({'display':'none'});


	var ulstart = '<ul id="pagination">',
		ulcontent = '',
		ulend = '</ul>';
	ADDLI();
	var pagination = $('#pagination li');
	var paginationwidth = $('#pagination').width();
	
	
	pagination.eq(0).addClass('current')
		
	function ADDLI(){
		//var lilicount = numpic + 1;
		for(var i = 0; i <= numpic; i++){
			ulcontent += '<li>' + '<a href="#">' + (i+1) + '</a>' + '</li>';
		}
		
		$('#slides').after(ulstart + ulcontent + ulend);	
	}

	pagination.on('click',DOTCHANGE)
	
	function DOTCHANGE(){
		
		var changenow = $(this).index();
		
		$('#slides li').eq(nownow).css('z-index','900');
		$('#slides li').eq(changenow).css({'z-index':'800'}).show();
		pagination.eq(changenow).addClass('current').siblings('li').removeClass('current');
		$('#slides li').eq(nownow).fadeOut(400,function(){$('#slides li').eq(changenow).fadeIn(500);});
		nownow = changenow;
	}
	$(".rightwrap").on("click",gonext)
	function gonext(){
		
		var changenow = $('#pagination li.current').index()+1;
		var oLi=document.getElementById("slides").getElementsByTagName("li");
        var oLilength=oLi.length;
        if(changenow==oLilength){
			changenow=0;
		}
			$('#slides li').eq(nownow).css('z-index','900');
			$('#slides li').eq(changenow).css({'z-index':'800'}).show();
			pagination.eq(changenow).addClass('current').siblings('li').removeClass('current');
			$('#slides li').eq(nownow).fadeOut(400,function(){$('#slides li').eq(changenow).fadeIn(500);});
			nownow = changenow;
	}
	$(".leftwrap").on("click",gopre)
	function gopre(){
		
		var changenow = $('#pagination li.current').index()-1;
		if(changenow<0){
//			changenow=2;
		}
			$('#slides li').eq(nownow).css('z-index','900');
			$('#slides li').eq(changenow).css({'z-index':'800'}).show();
			pagination.eq(changenow).addClass('current').siblings('li').removeClass('current');
			$('#slides li').eq(nownow).fadeOut(400,function(){$('#slides li').eq(changenow).fadeIn(500);});
			nownow = changenow;
	}
	pagination.mouseenter(function(){
		inout = 1;
	})
	
	pagination.mouseleave(function(){
		inout = 0;
	})
	
	function GOGO(){
		
		var NN = nownow+1;
		
		if( inout == 1 ){
			} else {
			if(nownow < numpic){
			$('#slides li').eq(nownow).css('z-index','900');
			$('#slides li').eq(NN).css({'z-index':'800'}).show();
			pagination.eq(NN).addClass('current').siblings('li').removeClass('current');
			$('#slides li').eq(nownow).fadeOut(400,function(){$('#slides li').eq(NN).fadeIn(500);});
			nownow += 1;

		}else{
			NN = 0;
			$('#slides li').eq(nownow).css('z-index','900');
			$('#slides li').eq(NN).stop(true,true).css({'z-index':'800'}).show();
			$('#slides li').eq(nownow).fadeOut(400,function(){$('#slides li').eq(0).fadeIn(500);});
			pagination.eq(NN).addClass('current').siblings('li').removeClass('current');

			nownow=0;

			}
		}
		TT = setTimeout(GOGO, SPEED);
	}
	
	TT = setTimeout(GOGO, SPEED); 
	
	
	
	$(".nav_child").hide();
	$(".nav_listl").mouseover(function(){
		$(this).find(">a").addClass("current").siblings(".nav_child").show();
	}).mouseout(function(){
		$(this).find(">a").removeClass("current").siblings(".nav_child").hide();
	}); 

})



$(document).ready(function() {
	
	function indexPange(){
		
		var Sys = {};

		var ua = navigator.userAgent.toLowerCase();

		var s;
		
		(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
		
		(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
		
		(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
		
		(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
		
		(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
		
		//以下进行测试
		var liulanqi;//浏览器高度
		var l_width;
		if (Sys.ie){
			
			liulanqi = document.documentElement.clientHeight ;
			l_width=document.documentElement.clientWidth;
		}else if (Sys.firefox){
			
			liulanqi = document.documentElement.clientHeight ;
			l_width=document.documentElement.clientWidth ;
		}else if (Sys.chrome){
			
			liulanqi = document.documentElement.clientHeight ;
			l_width=document.documentElement.clientWidth;
		}else if (Sys.opera){
			
			liulanqi = document.body.clientHeight ;
			l_width=document.body.clientWidth;
		}else if (Sys.safari){
			
			liulanqi = document.documentElement.clientHeight ;
			l_width=document.documentElement.clientWidth;
			
		}else{
			
			liulanqi = document.documentElement.clientHeight ;
			l_width=document.documentElement.clientWidth;
		}
		var _height=liulanqi-100;

		if(l_width<=1200){
			l_width=1200;
		}
		$("#full-screen-slider").css({"width":(l_width).toString()});
		$("#full-screen-slider").css({"height":(l_width/2.3 ).toString()});
		$("#slides li").css("background-size",l_width+'px '+'auto');
		
		$("#slides li a").css({"height":($("#full-screen-slider").height()).toString()});
	}
		indexPange();
	$(window).resize(function(){
		indexPange();
	});
});
