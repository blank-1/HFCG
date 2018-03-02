/*显示浮层*/
$("#w_share").on("click",function(){
  $("#markBox").show().on("click",function(){
    $(this).hide()
  })
})
/*显示规则*/
    $("#w_regulation").on("click",function(){
      $(".w_codex").addClass('w_codexD').removeClass('w_codex');

      $(".w_Determine").on("click",function(){
        $(".w_codexD").addClass('w_codex').removeClass('w_codexD');
      })
    })
var lottery={
  index:-1, //当前转动到哪个位置，起点位置
  count:0,  //总共有多少个位置
  timer:0,  //setTimeout的ID，用clearTimeout清除
  speed:1, //初始转动速度
  times:0,  //转动次数
  cycle:160,  //转动基本次数：即至少需要转动多少次再进入抽奖环节
  prize:-1, //中奖位置
  init:function(id){
    if ($("#"+id).find(".lottery-unit").length>0) {
      $lottery = $("#"+id);
      $units = $lottery.find(".lottery-unit");
      this.obj = $lottery;
      this.count = $units.length;
      $lottery.find(".lottery-unit-"+this.index).addClass("active");
      $.each($lottery.find(".lottery-unit-"+this.index),function(){
        //console.log($(this))
      })
    };
  },
  roll:function(){
    var index = this.index;
    var count = this.count;
    var lottery = this.obj;
    $(lottery).find(".lottery-unit-"+index).removeClass("active");
    index += 1;
    if (index>count-1) {
      index = 0;
    };
    $(lottery).find(".lottery-unit-"+index).addClass("active");
    this.index=index;
    return index;
    return false;
  },
  stop:function(index){
    this.prize=index;
    return false;
  }
};
var byValue=8;
function roll(){
  var byvalue_one=byValue;
  lottery.times += 1;
  var curindex=lottery.roll();
  //console.log(byvalue_one);
  if (lottery.times > lottery.cycle+10 && lottery.prize==lottery.index) {
    clearTimeout(lottery.timer);
    var cur=$('.lottery-unit-'+curindex).attr("data-type");
    if(cur=="手气不佳，</br>换个姿势再来吧！"){
       setTimeout(function(){
         $.dialog({
           isDisMask:false,
           title:"手气不佳，</br>换个姿势再来吧！",
           button:["确定","分享"],
           callback:function(){
        	   $("#lottery").find("a").addClass("btn");
           }
        })
       },1000)
    }else{
      setTimeout(function(){
         $.dialog({
           isDisMask:false,
           title:"恭喜您!获得  <span>"+cur+"</span>，运气好到爆</br><div style='font-size:1.2rem;'>财富劵当天到账，如中实物大奖客服人员会在活动结束后与您联系，请保持手机畅通</div>",
           button:["确定","分享"],
           callback:function(){  
        	   $("#lottery").find("a").addClass("btn");
           }
        })
       },1000)       
    }
    /*$.dialog({//未注册
          isDisMask:false,
          title:"您尚未注册，</br> 关注下方二维码注册并</br>理财即可参与抽奖！",
          button:["确定","分享"],
          callback:function(){
             
          }
      });*/
     /* $.dialog({//未投标
        isDisMask:false,
        title:"您尚未投标，</br>投标100元即可参与抽奖",
        button:["确定","分享"],
        callback:function(){
           
        }
     })*/ 
    lottery.prize=-1;
    lottery.times=0;
    click=false;

  }else{

    if (lottery.times<lottery.cycle) {
      lottery.speed -= 10;
    }else if(lottery.times==lottery.cycle) {
      var index = byvalue_one;/*中奖位置*/
      //console.log(index)
      lottery.prize = index; 

    }else{
      if (lottery.times > lottery.cycle+10 && ((lottery.prize==0 && lottery.index==7) || lottery.prize==lottery.index+1)) {
        lottery.speed += 10;
      }else{
        lottery.speed += 20;
      }
    }
    if (lottery.speed<4) {
      lottery.speed=40;
    };
    lottery.timer = setTimeout(roll,lottery.speed);
  }
  return false;
}
var click=false;
  $(".btn").bind("click",function(){
	  $(this).removeClass("btn");
	  lottery.init('lottery');
	  byValue=8;
	  $.ajax({
			url:rootPath+"/game/toIsTurntableDraw",
			type:"post",
			data:{"userToken":$("#userToken").val()},
			success: function (data) {
			var _data = eval('('+data+')');
			$("#shareNum").html(parseInt(_data.num));
			if(_data.isSuccess){
				isTurnDraw();
			}else if(_data.id == 'nolend'){
						//alert(_data.info)
						$.dialog({//未投标
			    	        isDisMask:false,
			    	        title:"您尚未投标，</br>投标100元即可参与抽奖",
			    	        button:["确定","分享"],
			    	        callback:function(){
			    	        	$("#lottery").find("a").addClass("btn");
			    	        }
			    		});
					}else if(_data.id == 'nonum'){
						$.dialog({
			    	          isDisMask:false,
			    	          title:"您已没有抽奖次数",
			    	          button:["确定","分享"],
			    	          callback:function(){
			    	        	  $("#lottery").find("a").addClass("btn");
			    	          }
			    	      });
					}else if(_data.id == 'noregist'){
						$.dialog({//未注册
			    	          isDisMask:false,
			    	          title:"您尚未注册，</br> 关注下方二维码注册并</br>理财即可参与抽奖！",
			    	          button:["确定","分享"],
			    	          callback:function(){
			    	        	  $("#lottery").find("a").addClass("btn");
			    	          }
			    	      });
					}else{
						$.dialog({
		    	          isDisMask:false,
		    	          title:"网络正忙，请您稍后使用！",
		    	          button:["确定","分享"],
		    	          callback:function(){
		    	        	  $("#lottery").find("a").addClass("btn");
		    	          }
		    	      });
					}
				}	
			});
    
//    if (click) {
//      return false;
//    }else{
//    	isTurnDraw();
//    }
  });
  function isTurnDraw(){
	  if($("#flag").val() == 0){
  		$.dialog({//未注册
  	          isDisMask:false,
  	          title:"您尚未注册，</br> 关注下方二维码注册并</br>理财即可参与抽奖！",
  	          button:["确定","分享"],
  	          callback:function(){
  	        	$("#lottery").find("a").addClass("btn");
  	          }
  	      });
  	  return false;
  	}else if($("#flag").val() == 2){
  		$.dialog({//未投标
  	        isDisMask:false,
  	        title:"您尚未投标，</br>投标100元即可参与抽奖",
  	        button:["确定","分享"],
  	        callback:function(){
  	        	$("#lottery").find("a").addClass("btn");
  	        }
  		});
  	  return false;
  	}else if($("#shareNum").html() == '0'){
  		$.dialog({//未投标
  	        isDisMask:false,
  	        title:"您已没有抽奖次数",
  	        button:["确定","分享"],
  	        callback:function(){
  	        	$("#lottery").find("a").addClass("btn");
  	        }
  		});
  	  return false;
  	}else{
  		$("#shareNum").html(parseInt($("#shareNum").html())-1);
  		$.ajax({
  			url:rootPath+"/game/toTurntableDraw",
  			type:"post",
  			data:{"userToken":$("#userToken").val()},
  			success: function (data) {
  			var _data = eval('('+data+')');
  					if(_data.isSuccess){
  						if(data.id != -2){
  							byValue=_data.id;//ajax赋值
  						}
  					}else if(_data.id == 'nolend'){
  						isFlag = true;
  						//alert(_data.info)
  						$.dialog({//未投标
  			    	        isDisMask:false,
  			    	        title:"您尚未投标，</br>投标100元即可参与抽奖",
  			    	        button:["确定","分享"],
  			    	        callback:function(){
  			    	        	$("#lottery").find("a").addClass("btn");
  			    	        }
  			    		});
  					}else if(_data.id == 'nonum'){
  						$.dialog({
  			    	          isDisMask:false,
  			    	          title:"您已没有抽奖次数",
  			    	          button:["确定","分享"],
  			    	          callback:function(){
  			    	        	$("#lottery").find("a").addClass("btn");
  			    	          }
  			    	      });
  					}else if(_data.id == 'noregist'){
  						$.dialog({//未注册
  			    	          isDisMask:false,
  			    	          title:"您尚未注册，</br> 关注下方二维码注册并</br>理财即可参与抽奖！",
  			    	          button:["确定","分享"],
  			    	          callback:function(){
  			    	        	$("#lottery").find("a").addClass("btn");
  			    	          }
  			    	      });
  					}else{
  						$.dialog({
			    	          isDisMask:false,
			    	          title:"网络正忙，请您稍后使用！",
			    	          button:["确定","分享"],
			    	          callback:function(){
			    	        	  $("#lottery").find("a").addClass("btn");
			    	          }
			    	      });
  					}
  				}	
  			});
  		  lottery.speed=200;
  		  roll();
  		  click=true;
  		  return false;
  	}
  }