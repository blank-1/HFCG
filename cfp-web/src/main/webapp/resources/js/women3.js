// JavaScript Document
var iNow=0;
var oNum=0;

$(function() {
	$(window).resize(function() {
		imgWH();
	}).resize();
	function imgWH() {
		var i = 0,
			imgpnglength = $('.imgpng').length;
		for(i=0; i < imgpnglength; i++) {
			var imgpng = $('.imgpng').eq(i),
				imgpngW = imgpng.width(),
				maximgpngW = imgpng.attr("width");
			if(imgpngW >= maximgpngW){
				imgpng.attr('width', maximgpngW);
			} else {
				imgpng.attr('width', imgpngW);
			}
		}
		var imgtopW = $('.imgtop').width(),
			imgtopH = $('.imgtop').height();
		//$('.gem-back').css({width:imgtopW*0.375,height:500px,marginTop:imgtopH*0.259});
	}
});


$(function(){
	//右侧导航点击事件
	$("#navListRig ol li").click(function(){
		//var oHeight = document.documentElement.clientHeight;
		var oHeight = $(window).height();
		//alert(oHeight*2);
		$(this).find("em").css({"display":"block"});
		$(this).siblings().find("em").css("display","none");
		if($(this).attr("id")=="women"){
			$('html,body').animate({scrollTop: '0px'},500);
		}else if($(this).attr("id")=="women2"){
			$('html,body').animate({scrollTop: oHeight+125+'px'},500);
		}else if($(this).attr("id")=="women3"){
			$('html,body').animate({scrollTop: oHeight*2+125+'px'},500);
		}else if($(this).attr("id")=="women4"){
			$('html,body').animate({scrollTop: oHeight*3+125+'px'},500);
		}else if($(this).attr("id")=="women5"){
			$('html,body').animate({scrollTop: oHeight*4+125+'px'},500);
		}else{
			return;
		}

	});
	
	var oHei = document.documentElement.clientHeight;	
	$('#navListRig').css({
		"top":(oHei-250)/2+"px"	
	});
	$('#divHei').css({
		"height":(oHei-240)/2+"px"	
	});
	
	window.onresize = function(){
		var oHei = document.documentElement.clientHeight;
		liHei();
	}	
	function liHei(){
		for(var i=0;i<5;i++){
			if(i==0){
				$('#box .li-div .li-linr').eq(i).css('height',(126+oHei)+'px');
			}else{
				$('#box .li-div .li-linr').eq(i).css('height',oHei+'px');
			}
		}
	}
	liHei();
	
	//管理团队
	$('#picList .pic-list-img').mouseenter(function(){
		$('#picList .pic-list-img').find('em').removeClass('cur');
		$(this).find('em').addClass('cur');
		$('#picImg .pic-img-lay').hide();
		$('#picImg .pic-img-lay').eq($(this).index()).show();	
		$('#jianjie .jianjie-lay').hide();
		$('#jianjie .jianjie-lay').eq($(this).index()).show();
	});	
	//合作伙伴
	var iNow=0;
	$('#hzList .hz-item').mouseenter(function(){
		iNow=$(this).index();
		$('#hzList .hz-item').find('.tiao').hide();
		$(this).find('.tiao').show();
		$('#scr').animate({
			"left":-900*$(this).index()+"px"	
		});
	});
	$('#hzList .hz-item').click(function(){
		iNow=$(this).index();
		$('#hzList .hz-item').find('.tiao').hide();
		$(this).find('.tiao').show();
		$('#scr').animate({
			"left":-900*$(this).index()+"px"	
		});
	});
	$('#hz-arrr').click(function(){
		if(iNow==$('#hzList .hz-item').size()-1){
			iNow=$('#hzList .hz-item').size()-1;
		}else{
		iNow++;}
		$('#hzList .hz-item').find('.tiao').hide();
		$('#hzList .hz-item').eq(iNow).find('.tiao').show();
		$('#scr').animate({
			"left":-900*iNow+"px"	
		});
	});
	$('#hz-arrl').click(function(){
		if(iNow==0){
			iNow=0;
		}else{
		iNow--;}
		$('#hzList .hz-item').find('.tiao').hide();
		$('#hzList .hz-item').eq(iNow).find('.tiao').show();
		$('#scr').animate({
			"left":-900*iNow+"px"	
		});
	});	
	//加入我们
	var iDer=0;
	$('#jr').css({
		"width":480*$('#jr-l-b span').size()+"px"	
	});
	$('#jr-l-b span').click(function(){
		iDer=$(this).index();
		$('#jr-l-b span').removeClass('active');
		$(this).addClass('active');
		$('#jr').animate({
			"left":-480*$(this).index()+"px"	
		});
	});
	$('#jr-left').click(function(){
		if(iDer==$('#jr .jr-item').size()-1){
			iDer=$('#jr .jr-item').size()-1;
		}else{
		iDer++;}
		$('#jr-l-b span').removeClass('active');
		$('#jr-l-b span').eq(iDer).addClass('active');
		$('#jr').animate({
			"left":-480*iDer+"px"	
		});
	});
	$('#jr-rig').click(function(){
		if(iDer==0){
			iDer=0;
		}else{
		iDer--;}
		$('#jr-l-b span').removeClass('active');
		$('#jr-l-b span').eq(iDer).addClass('active');
		$('#jr').animate({
			"left":-480*iDer+"px"	
		});
	});
	
});
