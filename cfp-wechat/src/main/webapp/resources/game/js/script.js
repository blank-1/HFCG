function init(){//初始化函数
	countDwn()
	gameAnimate()
}
var gameTime,
	sppeds;
function countDwn(){//点击开始游戏实现倒计时
	var flag=0,
		timer=null;
		$("#mask").hide();
		flag=1;
		if(flag==1){
			var time=3;
			var timer=setInterval(function(){
				time--;
				$("#font").html(time)
				if(time<=0){
					clearInterval(timer)
					$("#countDown").hide();
					$(".mask").hide();
					$("#process").show();
					statrGame();
					var num=null;
					$('.list').each(function(){
						var $this=$(this);
						var speed=1;
							gameTime=setInterval(function(){
							sppeds=setInterval(function(){
									return speed++;
							},400)	
							num++;
							$this.css("marginTop",num+speed+'px')
						},50)
					})
				}
			},800)
		}

}
function statrGame(){
	var val=$("#countDwn").html();
	var timerOut=setInterval(function(){
		val--;
		$("#countDwn").html(val)
		if(val<=10){
			$("#countDwn").css({
				'color':"#FFCE00"
			})
		}
		if(val<=5){
			$("#countDwn").css({
				'color':"#F70802"
			})
		}
		if(val<=0){
			clearInterval(timerOut)
			location.href=rootPath+"/game/result?num="+resultNum;
		}
	},1000)
}
var life=parseInt($("#life").html()),
	resultNum=0;
function gameAnimate(){//动态添加人物
	for (var i=0;i<3;i++){
		var objul = '<ul class="list"></ul>'
		$("#process").append(objul);
	}
	for(var j=0;j<200;j++){
		var random=parseInt(10*Math.random());
		var objli='<li dataType="'+(random)+'"></li>';
		$(".list").append(objli);
	}
	var len=$('.list li').length;
	$('.list li').each(function(){
		var ind=$(this).attr("dataType"),
		    rows=Math.floor(Math.random()*4),
		    cols=Math.floor(Math.random()*200);
		$("#process").children('ul').eq(rows).find('li').eq(cols).addClass("odd");
		$(this).on("click",function(){
			$(this).off("click")
			if($(this).hasClass("odd")){		
				$(this).addClass("die").removeClass("odd");
				resultNum+=1;				
				$('.number>span').html(resultNum)
			}else{
				$(this).addClass("boom");
				life-=1;
				$("#life").html(life)
				if(life<1){
					location.href=rootPath+"/game/result?num="+resultNum;
				}
			}

		})
	})
}
init()