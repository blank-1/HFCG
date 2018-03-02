//test
var startX = 0, startY = 0;
margin = 0;
var curPage = 0;
var pageWidth = 0, pageHeight = 0;
var scrollPrevent = false, movePrevent = false, touchDown = false;
var con = null;
//容器
var pages = null;
//每页容器
var distance = 50;
//触发换页的拖动的最小距离,px
var touchUp = false;
//是否手触之后触发,可解决iphone4飞屏的bug
var sizeCon = $(window);
//如用默认值，但小米四和三星手机会有问题，变大
var sizeConDefault = false;
function bindEvent() {
    //开始手指拖动事件
    $(document.body).on("touchstart", function(e) {
         var ele=e.target.tagName;
        if(ele!='INPUT' && ele!='p' && ele!='div'){
            e.preventDefault();
        }
        e = e.changedTouches[0];
        onStart(e);
    });
    $(document.body).on("touchmove", function(e) {
        var ele=e.target.tagName;
        if(ele!='INPUT' && ele!='p' && ele!='div'){
            e.preventDefault();
        }
        onMove(e.changedTouches[0], e);
    });
    $(document.body).on("touchend", function(e) {
        var ele=e.target.tagName;
        if(ele!='INPUT' && ele!='p' && ele!='div'){
            e.preventDefault();
        }
        onEnd(e.changedTouches[0]);
    });
    $(window).bind("orientationchange", function(event) {
        var ro = window.orientation;
        if (ro === 90 || ro === -90) {
            //横屏
            con.trigger("orientationDown", {
                degree: ro,
                curpage: curPage
            });
        } else {
            //竖屏
            con.trigger("orientationUp", {
                degree: ro,
                curpage: curPage
            });
        }
        con.trigger("orientation", {
            degree: ro,
            curpage: curPage
        });
    });
}
//dis 向上拖动的距离
/**
 * 整屏滚动组件
 * 例子：{@linkplain http://h5.lietou-static.com/m/test/js/swipepage/}
 * @module lib/swipepage/swipepage
 * @author 崔久代
 */
/**
 * @constructor swipepage
 * @param container {object} 必需 容器
 * @param items {string} 必需 每个items 就是一页
 * @param dis {number} 滚动触发换页的 阈值
 * @param { boolean} [isTouchUp] [是否手触之后触发,使用后可解决iphone4飞屏的bug]
 * @param {string} [sizeContainer] [获取页面大小的容器，默认是$(window)，但小米四和三星手机会有问题，变大]
 */
function init(container, items, dis, isTouchUp, sizeContainer) {
    if (dis) {
        distance = dis;
    }
    if (isTouchUp) {
        touchUp = true;
    }
    if (sizeContainer) {
        sizeCon = sizeContainer;
        sizeConDefault = false;
    }
    bindEvent();
    initPage(container, items);
    con.on("orientation", function(e) {
        setInit();
    });
}
/**
 * 滚动到 n 页
 * @param n {number} 要滚动到的页面
 */
init.goPage = function(n) {
    animatePage(n);
    con.trigger("page", n);
};
function setInit() {
    if (!sizeConDefault) {
        pages.css({
            width: "100%",
            height: "100%"
        });
        pageWidth = sizeCon.width();
        pageHeight = sizeCon.height();
    } else {
        pageWidth = sizeCon.width();
        pageHeight = sizeCon.height();
        pages.css({
            width: pageWidth + "px",
            height: pageHeight + "px"
        });
    }
    if (touchUp) {
        con.css("-webkit-transition", "all .5s ease-out");
    }
    con.height(pageHeight + "px");
    animatePage(curPage);
}
function initPage(container, items) {
    con = container;
    pages = items;
    setInit();
}
var timer=1,
    time=null;
function onStart(e) {     
    time=setInterval(function(){
       timer+=1;
       if(timer==10){
          var newPage = curPage + 1;
          animatePage(newPage);
          con.trigger("nextPage", newPage);
          con.trigger("page", newPage);
       }
   },1)

    if (movePrevent == true) {
        event.preventDefault();
        return false;
    }
    touchDown = true;
    startX = e.pageX;
    startY = e.pageY;
    margin = con.css("-webkit-transform");
    //margin = "matrix(1, 0, 0, 1, 0, -50)";
    margin = margin.replace("matrix(", "");
    margin = margin.replace(")", "");
    margin = margin.split(",");
    margin = parseInt(margin[5], 10);
}
function onMove(e, oe) {
    clearInterval(time);
    if (movePrevent == true || touchDown != true) {
        event.preventDefault();
        return false;
    }
    event.preventDefault();
    if (scrollPrevent == false && e.pageY != startY && !touchUp) {
        var temp = margin + e.pageY - startY;
        con.css("-webkit-transform", "matrix(1, 0, 0, 1, 0, " + temp + ")");
    }
}
function onEnd(e) {
    clearInterval(time)
    if (movePrevent == true) {
        event.preventDefault();
        return false;
    }
    touchDown = false;
    if (scrollPrevent == false) {
        endX = e.pageX;
        endY = e.pageY;
        if (Math.abs(endY - startY) <= distance && !touchUp) {
            animatePage(curPage);
        } else if (Math.abs(endY - startY) > distance) {
            if (endY > startY) {
                prevPage();
            } else {
                nextPage();
            }
        }
    }
}
function prevPage() {
    var newPage = curPage - 1;
    animatePage(newPage);
    con.trigger("prevPage", newPage);
    con.trigger("page", newPage);
}
/**
 * 下一页触发
 * @emits swipepage#nextPage
 * @param null
 * @property {number} newpage 滚动到的页面数
 */
function nextPage() {
    var newPage = curPage + 1;
    animatePage(newPage);
    con.trigger("nextPage", newPage);
    /**
     * 触发页面切换
     * @fires page
     * @param newPage {number} 翻到的页数
     */
    con.trigger("page", newPage);
}
function animatePage(newPage) {
    if (newPage < 0) {
        newPage = 0;
    }
    if (newPage > pages.length - 1) {
        newPage = pages.length - 1;
    }
    curPage = newPage;
    var newMarginTop = newPage * -pageHeight;
    con.css({
        "-webkit-transform": "matrix(1, 0, 0, 1, 0, " + newMarginTop + ")"
    });
}