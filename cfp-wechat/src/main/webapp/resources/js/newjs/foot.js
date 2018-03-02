$(function(){

	// 底部图片切换
	$(".footw_c .pic_link").hover(
		function(){
			if($(this).hasClass("pic_link1")){
				$(this).find("img").attr('src','/images/footer-icons4.jpg');
			}else if($(this).hasClass("pic_link2")){
				$(this).find("img").attr('src','/images/footer-icons1.png');
			}else if($(this).hasClass("pic_link3")){
				$(this).find("img").attr('src','/images/footer-icons2.png');
			}else if($(this).hasClass("pic_link4")){
				$(this).find("img").attr('src','/images/footer-icons3.png');
			}else if($(this).hasClass("pic_link5")){
				$(this).find("img").attr('src','/images/footer-icons5.png');
			}
		},function(){
			if($(this).hasClass("pic_link1")){
				$(this).find("img").attr('src','/images/footer-icons_4.png');
			}else if($(this).hasClass("pic_link2")){
				$(this).find("img").attr('src','/images/footer-icons_1.png');
			}else if($(this).hasClass("pic_link3")){
				$(this).find("img").attr('src','/images/footer-icons_2.png');
			}else if($(this).hasClass("pic_link4")){
				$(this).find("img").attr('src','/images/footer-icons_3.png');
			}else if($(this).hasClass("pic_link5")){
				$(this).find("img").attr('src','/images/footer-icons_5.png');
			}
		}
	);

	$(".footw_t1").hover(
		function(){
			$(this).css({backgroundImage:'url(/images/weibo01_h.png)'});
		},function(){
			$(this).css({backgroundImage:'url(/images/weibo01.png)'});
		}
	);

	$(".tanchu1").hover(
		function(){
			$(this).css({backgroundImage:'url(/images/weibo02_h.png)'});
		},function(){
			$(this).css({backgroundImage:'url(/images/weibo02.png)'});
		}
	);

	$(".tanchu1").click(function(){
		$(".tanchu").show();
	});

	$(".ttt").click(function(){
		$(".tanchu").hide();
	});

});
