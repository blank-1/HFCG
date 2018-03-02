(function($){
  var dialog=function(sets){
      var defaults={
      	 isDisMask:true,
         title:"您尚未注册，</br> 关注下方二维码注册并</br>理财即可参与抽奖！",
      	 button:["确定","分享"],
         callback:null
      }
      var opts=$.extend(defaults,sets); 
      opts.isDisMask && ($('<div class="mask" id="mask">').appendTo($('body')));
      var html='<div id="box">'
                  +'<div class="inner">'
                      +'<h2>'+opts.title+'</h2>'
                      +'<div class="w_attention" style="background:rgba(0,0,0,0)">'
                                +'<p style="color:#75102a;font-size:1.4rem;">长按二维码识别图关注我们</p>'              
                                +'<div class="w_Frame">'           
                                    +'<div style="margin-right:20%;"><img src="'+rootPath+'/gameimg/sudokuSweepstake/p1-img8.png" alt=""></div>'
                                    +'<div class="w_Line" id="w_Line">'
                                        +'<img src="'+rootPath+'/gameimg/sudokuSweepstake/p1-img7.png" alt="">'
                                       +'<span class="w_liner"></span>'
                                    +'</div>'
                                +'</div>'            
                            +'</div>'
                      +'<div class="button" id="button"><span id="yes">'+opts.button[0]+'</span><span id="cancel">'+opts.button[1]+'</span></div>'
                  +'</div>'
               +'</div>';
      $(html).appendTo($('body'));

      function showMiddle(){
         var w=$(window),
             box=$('.inner'),
             ml=(w.width()-box.outerWidth(true))/2,
             mt=(w.height()-box.outerHeight(true))/2;
         box.css({
             'left':ml+'px',
             'top':mt+'px'
         })
      }
      showMiddle();

      $(window).resize(function(){
          showMiddle();
      })

      // 关闭
      $('#button span').on('click',function(){
          $('#mask') && $('#mask').remove();
          $('#box').remove();
      })

      // 拖拽
      $('#box').on('mousedown',function(e){
          var posX=e.clientX-$(this).offset().left;
          var posY=e.clientY-$(this).offset().top;
          var box=$(this);
          $(this).css('cursor','move');
          $(document).on('mousemove',function(e){
              var l=e.clientX-posX,
                  t=e.clientY-posY,
                  maxW=$(window).width()-box.outerWidth(true)-20,
                  maxH=$(window).height()-box.outerHeight(true);

              if(l<0){
                l=0
              }else if(l>maxW){
                l=maxW;
              }

              if(t<0){
                t=0
              }else if(t>maxH){
                t=maxH;
              }

              box.css({
                  left:l+'px',
                  top:t+'px'
              })
              
              return false;
          })

          $(document).on('mouseup',function(){
              $(document).off('mousemove');
              $(document).off('mouseup');
          })

          // 点击了确定按钮 
          $('#yes').on('click',function(){
              opts.callback();
          })
          // 点击了取消按钮 
           $('#cancel').on('click',function(){
              $("#markBox").show().on("click",function(){
                $(this).hide();
                $("#lottery").find("a").addClass("btn");
              })
          })
      })
  }

  $.dialog=function(opts){
  	 return new dialog(opts);
  }
})(jQuery)