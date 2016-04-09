/**
 * 教育报两会专题页
 * @author Created by Brave Chen on 2016/2/25.
 * @version alpha v0.0.0 完成基础功能
 * @version alpha v0.1.0 修改延迟加载功能
 * @version alpha v0.2.0 增加发布评论功能
 * @version aloha v0.2.1 修复延迟加载中的混乱问题
 * @version alpha v0.2.2 修复延迟加载中向上不能加载的问题
 * @version alpha v0.2.3 修复点击发布按钮时弹出导航的问题
 * @version alpha v0.3.0 增加发送成功动画和loading动画
 * @version alpha v0.4.0 增加菜单按钮
 */
/**
 * 全局命名空间和基础配置
 * @type {{optionBase, TouchEvent}}
 */
window.jyb = (function(window,option){
    "use strict";
    var CTX = option.CTX;

    //^([\u4e00-\u9fa5]+|[a-zA-Z0-9]+)$
    /**全局配置**/
    var optionBase = {
        viewW:0,
        viewH:0,
        viewX:0,
        viewY:0,
        articleURL:CTX+"/meeting/ajaxArticle",  //正文文章获取接口
        specURL:CTX+"/meeting/ajaxComments",    //专家评论接口
        massURL:CTX+"/meeting/ajaxUComments",   //网友评论接口
        publishURL:CTX+"/meeting/addUComments", //我要发声发送接口
        defaultImgURL:CTX+"/image/active/meeting2016/placeImg.png",
        videoSrc:null,
        videoImgSrc:null,
        CTX:CTX,
        specialId:option.id,
        isAutoScrolling:false,   //是否正在跳转
        shareOption:null,
        friend:null,
        friends:null,
        debug:false      //全局调试日志开关
    };
    /**触摸事件类型**/
    var TouchEvent = {
        TAP:"tap"
    };
    /**UI事件类型**/
    var UIEvent = {
        OPEN_CONTENT:"openContent",
        JOIN_AT_ONCE:"joinAtOnce",
        GO_TO:"goTo",
        SHOW_NAV:"showNav",
        HOME_COMPLETE:"homeComplete",
        NAV_SHOW:"navShow",
        NAV_HIDE:"navHide"
    };
    /**跳转类型**/
    var GoToType = {
        TO_VIDEO:"toVideo",
        TO_IMG:"toImg",
        TO_ARTICLE:"toArticle",
        TO_SPEC:"toSpec",
        TO_MASS:"toMass",
        TO_PUBLISH:"toPublish",
        TO_OPEN:"toOpen"
    };

    var DataEvent = {
        GET_SPEC:"getSpec",
        GET_MASS:"getMass",
        SEND_COMMENT:"sendComment",
        GET_ARTICLE:"getArticle"
    };

    var ViewState = {
        SHOW:"show",
        HIDE:"hide"
    };

    //===================================
    return {
        optionBase:optionBase,
        TouchEvent:TouchEvent,
        UIEvent:UIEvent,
        GoToType:GoToType,
        DataEvent:DataEvent,
        ViewState:ViewState
    };

})(window,option);
//=================================================================
/**
 * 微信服务类
 */
jyb.WXService = (function(window,$,wx,jyb,undefined){
    "use strict";
    /**微信事件**/

    var WXEvent = {
        READY:"ready",
        WX_ERROR:"error",
        GET_SERVER_DATA:"getServerData",
        INIT_ERROR:"initError",
        SHARE_TIME_LINE_SUCCESS:'shareTimeLineSuccess',
        SHARE_TIME_LINE_CANCEL:'shareTimeLineCancel',
        SHARE_APP_MESSAGE_SUCCESS:'shareAppMessageSuccess',
        SHARE_APP_MESSAGE_CANCEL:'shareAppMessageCancel'
    };

    jyb.WXEvent = WXEvent;

    /**基础设置**/
    var baseMode = {
        WX_UA:'micromessenger',
        msgURL:'http://m.teacherfans.com/jssdk/jssdk/getWxConfig',
        debug:jyb.optionBase.debug,
        appId:null,
        timestamp:null,
        nonceStr:null,
        signature:null,
        jsApiList:['onMenuShareTimeline','onMenuShareAppMessage']
    };

    var parentOption,runInWX = false;
    var onStatusFn;

    function WXService(){
        this.hasError = false;
    }

    /**
     * 初始化
     * @param option
     * @param handler
     */
    WXService.prototype.initialize = function(option,handler){
        parentOption = option;
        if(handler!=null){
            onStatusFn = handler;
        }
        if(isRunInWX()){
            getSitOption();
        }
        this.initialized = true;
    };

    //===================================================
    /**
     * 是否运行在微信中
     */
    function isRunInWX(){
        var str = window.navigator.userAgent.toLowerCase();
        runInWX = str.indexOf(baseMode.WX_UA)!=-1;
        return runInWX;
    }

    /**
     * 获取站点配置
     */
    function getSitOption(){
        $.post(baseMode.msgURL,{},onResultHandler,'json');
        function onResultHandler(data){
            //console.log("server back:",data);
            baseMode.appId = data.appId;
            baseMode.timestamp = data.timestamp;
            baseMode.nonceStr = data.noncestr;
            baseMode.signature = data.signature;
            if(onStatusFn!=null){
                onStatusFn("getServerData",data);
            }
            initWX();
        }
    }

    /**
     * 初始化微信配置
     */
    function initWX(){
        wx.config({
            debug: baseMode.debug, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: baseMode.appId, // 必填，公众号的唯一标识
            timestamp:baseMode.timestamp, // 必填，生成签名的时间戳
            nonceStr: baseMode.nonceStr, // 必填，生成签名的随机串
            signature: baseMode.signature,// 必填，签名，见附录1
            jsApiList: baseMode.jsApiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });

        wx.ready(function(){
            if(!wxService.hasError) {
                if(onStatusFn!=null) {
                    onStatusFn(WXEvent.READY);
                }
            }else{
                if(onStatusFn!=null) {
                    onStatusFn(WXEvent.WX_ERROR);
                }
            }
        });

        wx.error(function(res){
            this.hasError = true;
            if(onStatusFn!=null) {
                onStatusFn(WXEvent.WX_ERROR,res);
            }
        });
    }

    /**
     * 监听分享事件
     * @param toFriends {Object} 朋友圈设置
     * @param toFriend  {Object} 分享朋友设置
     */
    function listenShare(toFriends,toFriend){
        //share to friends home
        wx.onMenuShareTimeline({
            title: toFriends.title, // 分享标题
            link: toFriends.link, // 分享链接
            imgUrl: toFriends.imgUrl, // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                if(onStatusFn!=null) {
                    onStatusFn(WXEvent.SHARE_TIME_LINE_SUCCESS);
                }
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                if(onStatusFn!=null) {
                    onStatusFn(WXEvent.SHARE_TIME_LINE_CANCEL);
                }
            }
        });
        //share to a friend
        wx.onMenuShareAppMessage({
            title: toFriend.title, // 分享标题
            desc: toFriend.desc, // 分享描述
            link: toFriend.link, // 分享链接
            imgUrl: toFriend.imgUrl, // 分享图标
            type: toFriend.type, // 分享类型,music、video或link，不填默认为link
            dataUrl: toFriend.dataUrl, // 如果type是music或video，则要提供数据链接，默认为空
            success: function () {
                // 用户确认分享后执行的回调函数
                if(onStatusFn!=null) {
                    onStatusFn(WXEvent.SHARE_APP_MESSAGE_SUCCESS);
                }
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                if(onStatusFn!=null) {
                    onStatusFn(WXEvent.SHARE_APP_MESSAGE_CANCEL);
                }
            }
        });
    }
    //===========================================
    var wxService;
    return {
        /**
         * 初始化
         * @param option {Object}
         * @param handler {Function}
         */
        initialize:function(option,handler){
            if(!wxService){
                wxService = new WXService();
            }
            wxService.initialize(option,handler);
            /**
             * 是否运行在微信中
             * @returns {boolean}
             */
            this.isRunInWX = function(){
                return runInWX;
            };
            /**
             * 侦听分享朋友和分享朋友圈
             */
            this.listenShare = listenShare;
        }
    };

})(window,jQuery,wx,jyb);
//=================================================================
jyb.AjaxService = (function(window,$,jyb,undefined){
    "use strict";

    function AjaxService(){
    }

    var ap = AjaxService.prototype;
    /**
     * 获取数据
     * @param url {String} [necessary]
     * @param data {String} [necessary]
     * @param handler {String} [necessary]
     */
    ap.getData = function(url,data,handler){
        $.getJSON(url,data,function(backData){
            handler("success",backData);
        });
    };
    /**
     * 发送数据
     * @param url {String} [necessary]
     * @param data {String} [necessary]
     * @param handler {String} [necessary]
     */
    ap.sendData = function(url,data,handler){
        $.post(url,data,function(backData){
            handler("success",backData);
        },"json");
    };

    return AjaxService;

})(window,jQuery,jyb);
//=================================================================
/**
 * 发布评论
 */
jyb.PublishComment = (function(window,$,Hammer,TL,jyb,undefined){
    "use strict";

    var baseMode = jyb.optionBase;

    function PublishComment(){
        this.gnId = null;
        this.element = null;
        this.element$ = null;

        this.nameTxt$ = null;
        this.commentTxt$ = null;
        this.publishBtn$ = null;
        this.publishBtnH = null;
        this.index = null;
    }

    var pp = PublishComment.prototype;

    var ajaxService;

    pp.initialize = function(element){
        this.element = element;
        this.element$ = $(this.element);
        this.gnId = this.element.getAttribute('data-gnId');
        ajaxService = new jyb.AjaxService();

        createChildren.call(this);
        childrenCreated.call(this);

    };

    pp.updateDisplay = function(){
    };

    pp.scrollHandler = function(){
    };

    pp.overInput = function(showOrHide){
        var div$ = mask$.find('.zt-publishDone');
        if(showOrHide){
            mask$.addClass('mask-click');
            div$.hide();
            mask$.show();
        }else{
            mask$.hide();
            mask$.removeClass('mask-click');
            div$.show();
        }
    };

    var mask,mask$;
    /**创建子对象**/
    function createChildren(){
        this.nameTxt$ = this.element$.find('#nameTxt');
        this.commentTxt$ = this.element$.find('#publishTxt');
        this.publishBtn$ = this.element$.find('div.zt-publish-btnBox a');
        this.publishBtnH = new Hammer(this.publishBtn$[0]);
        mask$ = this.element$.find('#maskBox');
        mask = mask$[0];
    }
    /**子对象创建完毕**/
    function childrenCreated(){
        this.publishBtnH.on(jyb.TouchEvent.TAP,onPublishHandler);
    }
    /**发布处理器**/
    function onPublishHandler(e){
        var nameTxt = publishComment.nameTxt$[0].value;
        var commentTxt = publishComment.commentTxt$[0].value;
        var tween1,tween2;
        if(!nameTxt || nameTxt.length>8){
            tween1 = TL.to(publishComment.nameTxt$[0],0.5,{background:'rgba(183,46,34,0.5)',onComplete:function(){
                tween1.reverse();
            }});
            return;
        }
        if(!commentTxt || commentTxt.length>200){
            tween2 = TL.to(publishComment.commentTxt$[0],0.5,{background:'rgba(183,46,34,0.5)',onComplete:function(){
                tween2.reverse();
            }});
            return;
        }
        ajaxService.sendData(baseMode.publishURL,{specialId:baseMode.specialId,name:nameTxt,content:commentTxt},onSendHandler);
    }
    /**发送数据回调**/
    function onSendHandler(type,data){
        switch (type){
            case "success":
                if(data.resCode===200){
                    TL.set(mask,{display:'block',opacity:0});
                    mask$.addClass('mask-publishDone');
                    TL.to(mask,0.5,{opacity:1,onComplete:function(){
                        var timer = setTimeout(function(){
                            clearTimeout(timer);
                            TL.to(mask,0.5,{opacity:0,onComplete:function(){
                                mask$.hide();
                                mask$.removeClass('mask-publishDone');
                            }});
                        },2000);
                    }});
                    publishComment.nameTxt$[0].value = "";
                    publishComment.commentTxt$[0].value = "";
                }
                break;
            case "failed":
                break;
        }
    }

    var publishComment;

    return {
        getInstance:function(){
            if(!publishComment){
                publishComment = new PublishComment();
            }
            return publishComment;
        }
    };
})(window,jQuery,Hammer,TweenLite,jyb);
//=================================================================
/**
 * 专家评论
 */
jyb.SpecComment = (function(window,$,TL,jyb,undefined){
    "use strict";

    var baseMode = jyb.optionBase;

    function SpecComment(){
        this.gnId = null;
        this.element = null;
        this.element$ = null;
        this.index = null;
        this.loadComplete = false;
        this.loading = false;
        this.commentUL = null;
    }

    var sp = SpecComment.prototype;
    var ajaxService;

    sp.initialize = function(element){
        this.element = element;
        this.element$ = $(this.element);
        this.gnId = this.element.getAttribute('data-gnId');
        this.commentUL = this.element$.find('.zt-spec-list')[0];
        ajaxService = new jyb.AjaxService();
    };

    sp.updateDisplay = function(){
    };
    /**滚动刷新和延迟加载**/
    sp.scrollHandler = function(){
        if(!this.loadComplete && !this.loading){
            this.loading = true;
            ajaxService.getData(baseMode.specURL,{specialId:baseMode.specialId},onDataHandler);
            //console.log("spec scroll:",this.loadComplete,this.loading);
        }
    };
    /**
     * 数据处理
     * @param type {String} [necessary]
     * @param data {Object} [necessary]
     */
    function onDataHandler(type,data){
        switch (type){
            case "success":
                if(data.resCode===200){
                    var htmlStr = "";
                    var list = data.data;
                    for(var i= 0,item;(item=list[i])!=null;i++){
                        htmlStr+=getItemTL(item);
                    }
                    TL.set(specComment.commentUL,{opacity:0});
                    specComment.commentUL.innerHTML = htmlStr;
                    var timer = setTimeout(function(){
                        clearTimeout(timer);
                        TL.to(specComment.commentUL,0.5,{opacity:1});
                    },16);
                    specComment.loadComplete = true;
                    specComment.loading = false;
                }else{
                    if(jyb.optionBase.debug){
                        jyb.addLog("In specComment:","ajax error");
                    }
                }
                break;
            case "failed":
                if(jyb.optionBase.debug){
                    jyb.addLog("In specComment:","ajax error");
                }
                break;
            default :
                break;
        }
    }
    /**
     * 获取html模板
     * @param data {Object} [necessary]
     * @returns {string}
     */
    function getItemTL(data){
        return "<li>"+
                    "<div class='zt-s-itemMsg clearFix'>"+
                        "<i><img data-src='"+data.photo+"' src='"+data.photo+"' /></i>"+
                        "<div>"+
                            "<em>"+data.name+"</em>"+
                            "<span>"+data.from+"</span>"+
                        "</div>"+
                    "</div>"+
                    "<i class='zt-s-arrow'></i>"+
                    "<p class='zt-s-itemTxt'>"+data.content+"</p>"+
                "</li>";
    }
    //===================================
    var specComment;
    return {
        getInstance:function(){
            if(!specComment){
                specComment = new SpecComment();
            }
            return specComment;
        }
    };
})(window,$,TweenLite,jyb);
//=================================================================
/**
 * 网友评论
 */
jyb.MassComment = (function(window,$,TL,jyb,undefined){
    "use strict";

    var baseMode = jyb.optionBase;

    function MassComment(){
        this.gnId = null;
        this.element = null;
        this.element$ = null;
        this.index = null;
        this.loadComplete = false;
        this.loading = false;
        this.commentUL = null;
    }

    var mp = MassComment.prototype;
    var ajaxService;

    mp.initialize = function(element){
        this.element = element;
        this.element$ = $(this.element);
        this.commentUL = this.element$.find('.zt-mass-list')[0];
        this.gnId = this.element.getAttribute('data-gnId');
        ajaxService = new jyb.AjaxService();
    };

    mp.updateDisplay = function(){
    };

    mp.scrollHandler = function(){
        if(!this.loadComplete && !this.loading){
            this.loading = true;
            ajaxService.getData(baseMode.massURL,{specialId:baseMode.specialId},onDataHandler);
           // console.log("mass scroll:",this.loadComplete,this.loading);
        }
    };

    function onDataHandler(type,data){
        switch (type){
            case "success":
                //console.log("mass:",data);
                if(data.resCode===200){
                    var htmlStr = "";
                    var list = data.data;
                    for(var i= 0,item;(item=list[i])!=null;i++){
                        htmlStr+=getItemTL(item);
                    }
                    TL.set(massComment.commentUL,{opacity:0});
                    massComment.commentUL.innerHTML = htmlStr;
                    var timer = setTimeout(function(){
                        clearTimeout(timer);
                        TL.to(massComment.commentUL,0.5,{opacity:1});
                    },16);
                    massComment.loadComplete = true;
                    massComment.loading = false;
                }else{
                    if(jyb.optionBase.debug){
                        jyb.addLog("In massComment:","ajax error");
                    }
                }
                break;
            case "failed":
                if(jyb.optionBase.debug){
                    jyb.addLog("In massComment:","ajax error");
                }
                break;
            default :
                break;
        }
    }

    function getItemTL(data){

        return "<li>"+
                    "<em>"+data.name+"</em>"+
                    "<p>"+data.contents+"</p>"+
                "</li>";
    }

    var massComment;
    return {
        getInstance:function(){
            if(!massComment){
                massComment = new MassComment();
            }
            return massComment;
        }
    };
})(window,jQuery,TweenLite,jyb);
//=================================================================
/**
 * 文章模块
 */
jyb.ArticleBox = (function(window,$,TL,jyb,undefined){
    "use strict";

    var baseMode = jyb.optionBase;

    function ArticleBox(){
        this.gnId = null;
        this.element = null;
        this.element$ = null;
        this.loadCompleted = false;
        this.loading = false;
        this.index = null;
    }

    var ap = ArticleBox.prototype;

    var ajaxService;

    ap.initialize = function(element){
        this.element = element;
        this.element$ = $(this.element);
        this.gnId = this.element.getAttribute('data-gnId');
        ajaxService = new jyb.AjaxService();

        createChildren.call(this);
        childrenCreated.call(this);
    };

    ap.updateDisplay = function () {

    };

    ap.scrollHandler = function(){
        if(!this.loadCompleted && !this.loading){
            this.loading = true;
            ajaxService.getData(baseMode.articleURL,{specialId:baseMode.specialId},onArticleComplete);
            //console.log("article scroll:",this.loadCompleted,this.loading);
        }
    };

    var txtBox,txtBox$;

    function createChildren(){
        txtBox$ = $('.zt-a-txtBox');
        txtBox = txtBox$[0];
    }

    function childrenCreated(){

    }

    function onArticleComplete(type,data){
        switch (type){
            case "success":
                if(data){
                    TL.set(txtBox,{opacity:0});
                    var pre = $(txtBox).find('pre')[0];
                    pre.innerHTML = data;
                    var timer = setTimeout(function(){
                        clearTimeout(timer);
                        TL.to(txtBox,0.5,{opacity:1});
                    },16);
                    articleBox.loadCompleted = true;
                    articleBox.loading = false;
                }else{
                    if(jyb.optionBase.debug){
                        jyb.addLog("In onArticleComplete:","ajax error");
                    }
                }
                break;
            case "failed":
                if(jyb.optionBase.debug){
                    jyb.addLog("In onArticleComplete:","ajax error");
                }
                break;
            default:
                break;
        }
    }

    var articleBox;

    return {
        getInstance:function(){
            if(!articleBox){
                articleBox = new ArticleBox();
            }
            return articleBox;
        }
    };
})(window,$,TweenLite,jyb);
//=================================================================
/**
 * 图片展示模块
 */
jyb.ImgBox = (function(window,$,TL,jyb,undefined){
    "use strict";

    var baseMode = jyb.optionBase;
    var imgList,imgBoxList;

    function ImgBox(){
        this.gnId = null;
        this.element = null;
        this.element$ = null;
        this.index = null;
    }

    var ib = ImgBox.prototype;

    ib.initialize = function(element){
        this.element = element;
        this.element$ = $(this.element);
        this.gnId = this.element.getAttribute('data-gnId');

        createChildren.call(this);
        childrenCreated.call(this);
    };

    ib.updateDisplay = function(){

    };

    ib.scrollHandler = function(){
        var start = (!currentImg)?0:currentImg.getAttribute('data-index');
        var item;
        for(var i = start;(item=imgBoxList[i])!=null;i++){
            var item$ = $(item);
            var top = item$.offset().top;

            if(imgList[i].getAttribute('data-load',"1")){
                continue;
            }
            //console.log("imgBox:",imgList[i],"baseMode.viewY:",baseMode.viewY);
            if(top - baseMode.viewY<baseMode.viewH){
                var index = item.getAttribute('data-index');
                currentImg = imgList[index];
                startImgLoad(currentImg);
                //console.log("2222222222222222222222222","imgBox:",index,imgList[index],currentImg);
            }else{
                break;
            }
        }
    };

    function createChildren(){
        imgBoxList = this.element$.find('figure');
        imgList = [];
        var imgItem;
        for(var i= 0,item;(item=imgBoxList[i])!=null;i++){
            item.setAttribute('data-index',i);
            imgItem = $(item).find('img[data-src]')[0];
            imgItem.setAttribute('data-index',i);
            imgList[i] = imgItem;
            //console.log(i,":",item,imgItem);
        }
    }

    function childrenCreated(){

    }

    var currentImg;

    function startImgLoad(imgItem){
        if(!imgItem){
            //console.log('In startImgLoad,imgItem is error');
            return;
        }
        if(imgItem.getAttribute('data-load')==="1"){
            return;
        }
        //console.log("imgBox scroll:",imgItem);
        currentImg = imgItem;
        var imgItem$ = $(imgItem);
        TL.set(currentImg,{opacity:0});
        imgItem$.on('load',onImgComplete).on('error',onImgError);
        imgItem$.attr('src',imgItem.getAttribute('data-src'));
    }

    function onImgComplete(e){
        TL.to(e.target,0.5,{opacity:1});
        e.target.setAttribute('data-load','1');
    }

    function onImgError(e){
        e.target.setAttribute('src',baseMode.defaultImgURL);
    }

    //================================
    var imgBox;
    return {
        getInstance:function(){
            if(!imgBox){
                imgBox = new ImgBox();
            }
            return imgBox;
        }
    };

})(window,jQuery,TweenLite,jyb);
//=================================================================
/**
 * 视频功能模块
 */
jyb.VideoBox = (function(window,$,Hammer,jyb,undefined){
    "use strict";

    function VideoBox(){
        this.gnId = null;
        this.element = null;
        this.element$ = null;
        this.index = null;
    }

    var vp = VideoBox.prototype;

    //定义模块中的播放按钮和video对象,用于赋值
    var playBtn,video,playBtnImg;

    vp.initialize = function(element){
        this.element = element;
        this.element$ = $(this.element);
        this.gnId = element.getAttribute('data-gnId');

        //给播放按钮赋值
        playBtn = this.element$.find("#videoPlayBtn");
        //给video赋值
        video = this.element$.find("#myVideo");
        //视频播放按钮的图片
        playBtnImg = this.element$.find("#videoPlayBtnImg");
        
        
        //将全局的videoSrc基础变量赋值
        jyb.optionBase.videoSrc = video.attr("data-src");
        //将全局的videoImgSrc基础变量赋值
        jyb.optionBase.videoImgSrc = playBtnImg.attr("data-src");
        
        playBtnImg.attr({src:jyb.optionBase.videoImgSrc});
        
        //设置video的属性中的视频源,没有控制栏,不自动播放
        video.attr({src:jyb.optionBase.videoSrc,controls:"controls",autoplay:false});
        
        //当视频播放完成时
        video.on("ended",videoEndedHandler);
        
        video.on("pause",videoPauseHandler);
        
        //点击播放按钮时

        var playBtnH = new Hammer(playBtn[0]);
        playBtnH.on(jyb.TouchEvent.TAP,playBtnClickHandler);
        //playBtn.on("click",playBtnClickHandler);
        
        function videoEndedHandler(e){
        	//显示播放按钮
            playBtn.css({display:"block"});
            //设置video的宽高为0
            video.css({width:"0",height:"0"});
        }
        
        function videoPauseHandler(e){
        	//显示播放按钮
            playBtn.css({display:"block"});
            //设置video的宽高为0
            video.css({width:"0",height:"0"});
        }
        
        function playBtnClickHandler(e){
        	//隐藏播放按钮
            playBtn.css({display:"none"});
            //设置视频的宽高为父容器的宽高
            video.css({width:"100%",height:"100%"});
            //开始播放视频
            video[0].play();
            
            var element = video[0];
        	if(element.requestFullscreen) {
        		element.requestFullscreen();
    	    } else if(element.mozRequestFullScreen) {
    	        element.mozRequestFullScreen();
            } else if(element.msRequestFullscreen){ 
    	      element.msRequestFullscreen();  
    	    } else if(element.oRequestFullscreen){
    	      element.oRequestFullscreen();
    	    }else if(element.webkitRequestFullscreen){
    	      element.webkitRequestFullScreen();
        	}
        }
    };

    vp.updateDisplay = function(){

    };

    vp.scrollHandler = function(){

    };

    var videoBox;
    return {
        getInstance:function(){
            if(!videoBox){
                videoBox = new VideoBox();
            }
            return videoBox;
        }
    };

})(window,jQuery,Hammer,jyb);
//==================================================================
/**
 * 导航模块
 * @type {{getInstance}}
 */
jyb.NavBox = (function(window,$,Hammer,TL,jyb,undefined){
    "use strict";

    var hideTimer;

    function NavBox(){
        this.gnId = null;
        this.element = null;
        this.element$ = null;
        this.elementH = null;
        this.statusFn = null;
        this.visibled = false;
    }

    var np = NavBox.prototype;

    np.initialize = function(element,statusFn){
        this.element = element;
        this.element$ = $(this.element);
        this.elementH = new Hammer(this.element);
        this.gnId = this.element.getAttribute('data-gnId');
        this.statusFn = statusFn;

        createChildren.call(this);
        childrenCreated.call(this);
    };

    np.updateDisplay = function(){

    };

    np.visible = function(showOrHide){
        if(showOrHide){
            this.element$.css('opacity',0).show();
            TL.to(this.element,1,{opacity:1,onComplete:onShowHandler,onCompleteScope:this,onCompleteParams:[true]});
        }else{
            if(hideTimer){
                clearTimeout(hideTimer);
                hideTimer = null;
            }
            TL.to(this.element,1,{opacity:0,onComplete:onShowHandler,onCompleteScope:this,onCompleteParams:[false]});
        }
    };

    np.scrollHandler = function(){

    };

    function onShowHandler(showOrHide){
        if(showOrHide){
            this.visibled = true;
            hideTimer = setTimeout(handleHideTimer,3000);
            this.statusFn(jyb.UIEvent.NAV_SHOW);
        }else{
            this.visibled = false;
            this.element$.hide();
            this.statusFn(jyb.UIEvent.NAV_HIDE);
        }
    }

    function handleHideTimer(){
        clearTimeout(hideTimer);
        hideTimer = null;
        navBox.visible(false);
    }

    function createChildren(){

    }

    var TouchEvent = jyb.TouchEvent;
    var btnList;

    function childrenCreated(){
        btnList = {};
        //this.elementH.on(TouchEvent.TAP,onBtnTapHandler);
        var list = this.element$.find('span');
        for(var i= 0,item;(item=list[i])!=null;i++){
            //console.log(item[0],item);
            var hammer = new Hammer(item);
            hammer.on(TouchEvent.TAP,onBtnTapHandler);
            btnList[item.getAttribute('data-goTo')] = hammer;

        }
    }

    function onBtnTapHandler(e){
        var dom = e.srcEvent.target;
        if(dom===navBox.element){

        }else{
            var dom$ = $(dom);
            var target = dom$.attr('data-goTo')?dom$[0]:dom$.parent().attr('data-goTo')?dom$.parent()[0]:null;
            if(target){
                navBox.statusFn(jyb.UIEvent.GO_TO,{goTo:target.getAttribute('data-goTo')});
            }else{
                //log
                $('#footBox').text("target error:"+target);
            }
        }
    }

    //====================================
    var navBox;
    return {
        getInstance:function(){
            if(!navBox){
                navBox = new NavBox();
            }
            return navBox;
        }
    };

})(window,jQuery,Hammer,TweenLite,jyb);
//==================================================================
/**
 * 内容区域模块
 */
jyb.Content = (function(window,$,Hammer,jyb){
    "use strict";

    var baseMode = jyb.optionBase;
    var currentViewObj;

    function Content(){
        this.gnId = null;
        this.element = null;
        this.element$ = null;
        this.elementH = null;
        this.statusFn = null;
    }

    var cp = Content.prototype;

    cp.initialize = function(element,statusFn){
        this.element = element;
        this.element$ = $(this.element$);
        this.elementH = new Hammer(this.element);
        this.gnId = this.element.getAttribute('data-gnId');
        this.statusFn = statusFn;

        createChildren.call(this);
        childrenCreated.call(this);
    };

    cp.updateDisplay = function(){

    };

    var GoToType = jyb.GoToType;
    /**
     *
     * @param type
     * @param traceSize
     * @returns {*}
     */
    cp.getPosition = function(type,traceSize){
        var gnObj = goToList[type];
        var p,s;
        if(gnObj){
            p = gnObj.element$.offset();
        }else{
            return false;
        }

        var rect = {};
        rect.x = p.left;
        rect.y = p.top;
        if(traceSize){
            rect.width = gnObj.element$.width();
            rect.height = gnObj.element$.height();
        }
        return rect;
    };

    cp.scrollHandler = function(arriveType,upOrDown){
        var gnObj, start;
        if(arriveType){
            start = (arriveType===GoToType.TO_OPEN)?0:goToList[arriveType].index;
            //console.log("arriveType:",arriveType,goToList[arriveType],start);
            for(var i=start,item;(item=refreshList[i])!=null;i++){
                var top = item.element$.offset().top;
               // console.log(top,baseMode.viewY,top - baseMode.viewY,baseMode.viewH);
                if(top - baseMode.viewY<baseMode.viewH){
                    gnObj = item;
                    currentViewObj = gnObj;
                   // console.log("33333333333333:",gnObj);
                    gnObj.scrollHandler();
                }else{
                    break;
                }
            }
            //console.log("111currentViewObj:",currentViewObj);
            return;
        }

        //console.log("11111111currentViewObj:",currentViewObj);
        if(!currentViewObj){
            //console.log("error:","upOrDown:",upOrDown,"currentViewObj:",currentViewObj);
            return;
        }

        i = currentViewObj.index;
        //console.log(i);
       // console.log("**************>>");
        while(true){
           // console.log(i);
            if(i<0 || i>refreshList.length-1){
                break;
            }
            if(i===currentViewObj.index){
                currentViewObj.scrollHandler();
            }else{
                gnObj = refreshList[i];
                top = gnObj.element$.offset().top;
                if(top - baseMode.viewY<baseMode.viewH){
                    gnObj.scrollHandler();
                    currentViewObj = gnObj;
                }else{
                    //break;
                }
            }

            if(upOrDown){
                i--;
            }else{
                i++;
            }
        }
        //console.log("<<**************");


    };

    //==================================

    var videoBox,imgBox,articleBox,specComment,massComment,publishComment;
    var goToList;
    var refreshList;

    function createChildren(){
        goToList = {};
        refreshList = [];

        videoBox = jyb.VideoBox.getInstance();
        videoBox.initialize(document.querySelector('article[data-gnId="videoBox"]'));
        goToList[jyb.GoToType.TO_VIDEO] = videoBox;
        refreshList[0] = videoBox;
        videoBox.index = 0;

        imgBox = jyb.ImgBox.getInstance();
        imgBox.initialize(document.querySelector('article[data-gnId="imgBox"]'));
        goToList[jyb.GoToType.TO_IMG] = imgBox;
        refreshList[1] = imgBox;
        imgBox.index = 1;

        articleBox = jyb.ArticleBox.getInstance();
        articleBox.initialize(document.querySelector('article[data-gnId="articleBox"]'));
        goToList[jyb.GoToType.TO_ARTICLE] = articleBox;
        refreshList[2] = articleBox;
        articleBox.index = 2;

        specComment = jyb.SpecComment.getInstance();
        specComment.initialize(document.querySelector('article[data-gnId="specComment"]'));
        goToList[jyb.GoToType.TO_SPEC] = specComment;
        refreshList[3] = specComment;
        specComment.index = 3;

        massComment = jyb.MassComment.getInstance();
        massComment.initialize(document.querySelector('article[data-gnId="massComment"]'));
        goToList[jyb.GoToType.TO_MASS] = massComment;
        refreshList[4] = massComment;
        massComment.index = 4;

        publishComment = jyb.PublishComment.getInstance();
        publishComment.initialize(document.querySelector('article[data-gnId="publishComment"]'));
        goToList[jyb.GoToType.TO_PUBLISH] = publishComment;
        refreshList[5] = publishComment;
        publishComment.index = 5;
    }

    function childrenCreated(){
        //this.elementH.on(jyb.TouchEvent.TAP,onTapHandler);
    }

    /*function onTapHandler(e){
        var domId = e.target.getAttribute('id');
        if(domId === 'publishTxt' || domId === 'nameTxt' || domId === "publishBtn"){
            return;
        }
        content.statusFn(jyb.UIEvent.SHOW_NAV);
    }*/

    //==================================
    var content;
    return {
        /**
         *
         * @returns {Content}
         */
        getInstance:function(){
            if(!content){
                content = new Content();
            }
            return content;
        }
    }

})(window,$,Hammer,jyb);
//======================================================================
/**
 * 首页模块
 * @type {{getInstance}}
 */
jyb.Home = (function(window,$,Hammer,TL,jyb,undefined){
    "use strict";

    var TouchEvent = jyb.TouchEvent;

    function Home(){
        this.gnId = null;
        this.element = null;
        this.element$ = null;
        this.statusFn = null;
    }

    var hp = Home.prototype;

    hp.initialize = function(element,statusFn){
        //console.log("111111",element);
        this.element = element;
        this.element$ = $(this.element);
        this.gnId = this.element.getAttribute('data-gnId');
        this.statusFn = statusFn;

        createChildren.call(this);
        childrenCreated.call(this);
    };

    hp.updateDisplay = function(){
        if(btnBox){
            var list = btnBox$.find('a');
            var btn1 = $(list[0]),btn2 = $(list[1]),btn3 = $(list[2]);
            TL.fromTo(list[0],0.5,{top:btn1.position()+btn1.height()+20,opacity:0},{opacity:1});
            TL.fromTo(list[1],0.5,{top:btn2.position()+btn2.height()+20,opacity:0,delay:0.5},{opacity:1,delay:0.5});
            TL.fromTo(list[2],0.5,{top:btn3.position()+btn3.height()+20,opacity:0,delay:1},{opacity:1,delay:1});
        }
    };
    var btnBox,btnBox$,btnBoxH,homeImg,homeImg$;

    function createChildren(){
        homeImg$ = this.element$.find('.zt-homeBg');
        homeImg = homeImg$[0];

        btnBox$ = this.element$.find('.zt-home-btnBox');
        btnBox = btnBox$[0];
        btnBoxH = new Hammer(btnBox);
        var list = btnBox$.find('a');
        var btn1 = $(list[0]),btn2 = $(list[1]),btn3 = $(list[2]);
        TL.set([btn1,btn2,btn3],{opacity:0});
    }

    function childrenCreated(){
        TL.set(homeImg,{opacity:0});
        homeImg$.on('load',onBgComplete).on('error',onBgError);
        homeImg.src = homeImg.getAttribute('data-src');

        btnBoxH.on(TouchEvent.TAP,onTapHandler);
    }

    function onBgComplete(e){
        home.statusFn(jyb.UIEvent.HOME_COMPLETE);
        TL.to(homeImg,1,{opacity:1});
    }

    function onBgError(e){
        //console.log('home img error');
    }

    function onTapHandler(e){
        var dom = e.target;
        if(dom.hasAttribute('data-goTo')){
            home.statusFn(jyb.UIEvent.JOIN_AT_ONCE,{type:dom.getAttribute('data-goTo')});
        }
    }

    //========================
    var home;

    return {
        /**
         * 获取单例
         * @returns {Home}
         */
        getInstance:function(){
            if(!home){
                home = new Home();
            }
            return home;
        }
    };

})(window,jQuery,Hammer,TweenLite,jyb);

//===============================================
jyb.LoadingPanel = (function(window,$,undefined){
    "use strict";

    function LoadingPanel(){
        this.element = null;
        this.element$ = null;
        this.loadingBox  = null;
        this.loadingBox$ = null;
        this.gnId = null;
    }

    var lp = LoadingPanel.prototype;

    lp.initialize = function(element){
        this.element = element;
        this.element$ = $(this.element);
        this.gnId = this.element.getAttribute('data-gnId');

        createChildren.call(this);
        childrenCreated.call(this);
    };

    lp.show = function(showOrHide){
        if(showOrHide){
            this.element$.show();
        }else{
            this.element$.hide();
        }
    };

    lp.visible = function(){
        return this.element$.css('display')!=='none';
    };

    function createChildren(){
        this.loadingBox$ = this.element$.find('div[data-gnId="loadingBox"]');
        this.loadingBox = this.loadingBox$[0];
    }

    function childrenCreated(){

    }

    //===============================================
    var loadingPanel;
    return {
        getInstance:function(){
            if(!loadingPanel){
                loadingPanel = new LoadingPanel();
            }
            return loadingPanel;
        }
    }

})(window,jQuery);
//===============================================
/**
 * 主控模块
 */
(function(window,$,Hammer,TL,jyb,undefined){
    "use strict";

    var baseMode = jyb.optionBase;
    var doc$ = $(document),win$ = $(window),footer$ = $('#footer'),logPanel$ = $('#logPanel');
    var loadingPanel;
    var executeList;
    var wxService;

    $(document).ready(initialize);
    win$.on('load',onWinLoadHandler);
    /**
     * 初始化
     */
    function initialize(){

        loadingPanel = jyb.LoadingPanel.getInstance();
        loadingPanel.initialize(document.querySelector('div[data-gnId="loadingPanel"]'));
        loadingPanel.show(true);

        //微信服务对象
        wxService = jyb.WXService;
        wxService.initialize({},onWXStatusHandler);

        baseMode.viewW = win$.width();
        baseMode.viewH = win$.height();

        executeList = {};
        executeList[jyb.UIEvent.JOIN_AT_ONCE] = onJoinAtOnce;
        executeList[jyb.UIEvent.GO_TO] = onGoToHandler;
        //executeList[jyb.UIEvent.SHOW_NAV] = onShowNav;
        executeList[jyb.UIEvent.HOME_COMPLETE] = onHomeComplete;
        executeList[jyb.UIEvent.NAV_SHOW] = onNavShow;
        executeList[jyb.UIEvent.NAV_HIDE] = onNavHide;

        createChildren();
        childrenCreated();
    }

    /**
     * window onload事件
     */
    function onWinLoadHandler(e){
        home.updateDisplay();
        //content.updateDisplay();
    }

    //============================================

    var home,content,navBox,menuBtn,menuBtnH,menuBtn$;

    /**
     * 创建子对象
     */
    function createChildren(){
        home = jyb.Home.getInstance();
        home.initialize(document.querySelector('section[data-gnId="home"]'),onUIStatus);

        navBox = jyb.NavBox.getInstance();
        navBox.initialize(document.querySelector('nav[data-gnId="navBox"]'),onUIStatus);

        content = jyb.Content.getInstance();
        content.initialize(document.querySelector('section[data-gnId="content"]'),onUIStatus);

        menuBtn = document.querySelector('div[data-gnId="menuBtn"]');
        menuBtn$ = $(menuBtn);
        menuBtnH = new Hammer(menuBtn);
    }
    /**
     * 子对象创建完毕
     */
    function childrenCreated(){
        win$.on("resize",onWinResizeHandler);
        win$.on('scroll',onWinScrollHandler);
        menuBtnH.on(jyb.TouchEvent.TAP,onMenuTabHandler);
    }



    /**
     * UI状态回调
     */
    function onUIStatus(type,data){
        executeList[type](type,data);
    }
    /**
     * 首页和功能页切换
     */
    function onJoinAtOnce(type,data){
        TL.set(content.element,{display:'block',left:baseMode.viewW});
        TL.to(home.element,0.3,{left:-baseMode.viewW,onComplete:homeAEnd,onCompleteParams:[data]});
        TL.to(content.element,0.3,{left:0});
    }

    function onHomeComplete(type,data){
        loadingPanel.show(false);
    }

    /**
     * 跳转
     */
    function onGoToHandler(type,data){
        var point = content.getPosition(data.goTo);
        if(point){
            if(navBox.visibled) {
                navBox.visible(false);
            }
            baseMode.isAutoScrolling = true;
            TL.to(document.body,0.3,{scrollTop:point.y,onComplete:scrollAEnd,onCompleteParams:[data.goTo]});
        }
    }

    /**
     * 滚动完毕
     */
    function scrollAEnd(arriveType){
        //navBox.visible(true);
        baseMode.isAutoScrolling = false;
        content.scrollHandler(arriveType);
    }

    /**
     * 显示导航
     * @param type
     * @param data
     */
    function onShowNav(type,data){
        //navBox.visible(true);
    }

    function onMenuTabHandler(e){
        var publish = jyb.PublishComment.getInstance();
        publish.overInput(true);
        menuBtn$.hide();
        navBox.visible(true);
    }

    function onNavShow(type,data){

    }

    function onNavHide(type,data){
        menuBtn$.show();
        var publish = jyb.PublishComment.getInstance();
        publish.overInput(false);
    }

    /**
     * 首页切换完毕
     * @param data
     */
    function homeAEnd(data){
        home.element$.hide();
        menuBtn$.show();
        if(data.type!==jyb.GoToType.TO_OPEN){
            onGoToHandler(null,{goTo:data.type});
        }else{
            content.scrollHandler(jyb.GoToType.TO_OPEN);
        }
    }

    /**
     * 窗口尺寸改变事件
     * @param e
     */
    function onWinResizeHandler(e){
        baseMode.viewW = win$.width();
        baseMode.viewH = win$.height();
    }

    function onWinScrollHandler(e){
        var upOrDown = !(document.body.scrollTop - baseMode.viewY>0);
        baseMode.viewY = document.body.scrollTop;
        if(baseMode.isAutoScrolling){
            return;
        }

        if(content){
            content.scrollHandler(null,upOrDown);
        }
    }

    /**
     * 添加日志
     */
    function addLog(){
        if(!jyb.optionBase.debug || !logPanel$)
            return;
        if(logPanel$.css("display")==="none"){
        	logPanel$.show();
        }
        var ary = Array.prototype.slice.call(arguments,0);
        var text = logPanel$.text();
        for(var i= 0,len=ary.length;i<len;i++){
            text+=ary[i]+"<==|==>";
        }
        text+="\n";
        logPanel$.text(text);
    }

    jyb.addLog = addLog;

    //================微信处理======================
    /**
     * 微信状态处理器
     * @param type
     * @param data
     */
    function onWXStatusHandler(type,data){
        if(jyb.optionBase.debug){
            addLog("wx msg:",type);
        }
        switch(type){
            case wxEvent.WX_ERROR:
                if(jyb.optionBase.debug){
                    for(var key in data){
                        if(data.hasOwnProperty(key)){
                            addLog("wx error msg:",data[key]);
                        }
                    }
                }
                break;
            case wxEvent.READY:
                if(wxService.isRunInWX()){
                    gainConfig();
                    wxService.listenShare(topBase.friends,topBase.friend);
                }
                break;
            case wxEvent.GET_SERVER_DATA:
                if(jyb.optionBase.debug){
                    for(var key2 in data){
                        if(data.hasOwnProperty(key2)){
                            addLog("server data:",key2+":"+data[key2]);
                        }
                    }
                }
                break;
            case wxEvent.INIT_ERROR:
                break;
            case wxEvent.SHARE_TIME_LINE_SUCCESS:
                break;
            case wxEvent.SHARE_TIME_LINE_CANCEL:
                break;
            case wxEvent.SHARE_APP_MESSAGE_SUCCESS:
                break;
            case wxEvent.SHARE_APP_MESSAGE_CANCEL:
                break;
            default:
                break;
        }
    }
    /**
     * 获取配置
     * **/
    function gainConfig(){
        var friend$ = $('#friend');
        var friends$ = $('#friends');
        var friendConfig = {};
        var i$ = friend$.find('i');
        var em$ = friend$.find('em');
        var span$ = friend$.find('span');
        friendConfig.title = i$.text();
        friendConfig.link = window.location.href;
        friendConfig.desc = em$.text();
        friendConfig.imgUrl = span$.attr('data-src');
        friendConfig.type = span$.attr('data-type');
        friendConfig.dataUrl = span$.attr('data-Url');
        var friendsConfig = {};
        i$ = friends$.find('i');
        span$ = friends$.find('span');
        friendsConfig.title = i$.text();
        friendsConfig.link = window.location.href;
        friendsConfig.imgUrl = span$.attr('data-src');
        baseMode.friend = friendConfig;
        baseMode.friends = friendsConfig;
    }

})(window,jQuery,Hammer,TweenLite,jyb);
