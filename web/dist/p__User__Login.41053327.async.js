(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[91796],{63434:function(I,U,e){"use strict";var O=e(63185),K=e(9676),Z=e(28991),z=e(81253),a=e(85893),C=e(22270),j=e(67294),h=e(64893),F=e(47869),o=["options","fieldProps","proFieldProps","valueEnum"],_=j.forwardRef(function(w,H){var f=w.options,J=w.fieldProps,re=w.proFieldProps,oe=w.valueEnum,q=(0,z.Z)(w,o);return(0,a.jsx)(F.Z,(0,Z.Z)({ref:H,valueType:"checkbox",valueEnum:(0,C.h)(oe,void 0),fieldProps:(0,Z.Z)({options:f},J),lightProps:(0,Z.Z)({labelFormatter:function(){return(0,a.jsx)(F.Z,(0,Z.Z)({ref:H,valueType:"checkbox",mode:"read",valueEnum:(0,C.h)(oe,void 0),filedConfig:{customLightMode:!0},fieldProps:(0,Z.Z)({options:f},J),proFieldProps:re},q))}},q.lightProps),proFieldProps:re},q))}),S=j.forwardRef(function(w,H){var f=w.fieldProps,J=w.children;return(0,a.jsx)(K.Z,(0,Z.Z)((0,Z.Z)({ref:H},f),{},{children:J}))}),B=(0,h.G)(S,{valuePropName:"checked"}),Q=B;Q.Group=_,U.Z=Q},8632:function(I){I.exports={loginActions:"loginActions___1ssQy",appBox:"appBox___2Z4Pe",thirdLoginBox:"thirdLoginBox___26Cx8",loginIcon:"loginIcon___1ZkkY",dingding:"dingding___2u0zM",wechat:"wechat___3iCey",enWechat:"enWechat___2PmVf",qq:"qq___3WvNS",dingLogin:"dingLogin___3Yuzo",wwLogin:"wwLogin___3u2W9"}},73430:function(I){I.exports={container:"container___2mKrh",loginForm:"loginForm___3ypRW",qrLoginBtn:"qrLoginBtn___3g74p",copyRight:"copyRight___XB4m_",qrcodeBox:"qrcodeBox___5t3G7",qrMask:"qrMask___17eyU",languageSelect:"languageSelect___3ubhc"}},62608:function(I,U,e){"use strict";e.r(U);var O=e(49111),K=e(19650),Z=e(34792),z=e(48086),a=e(39428),C=e(11849),j=e(3182),h=e(2824),F=e(85304),o=e(37406),_=e(89366),S=e(2603),B=e(67294),Q=e(37476),w=e(5966),H=e(63434),f=e(30672),J=e(32601),re=e(59646),oe=e.n(re),q=e(89688),ge=e.n(q),Me=e(2975),Ce=e.n(Me),W=e(85050),i=e.n(W),y=e(8632),T=e.n(y),s=e(85893),pe="",ye=()=>{var fe,Ae,M,ee,Y,N=(0,f.YB)(),le=(0,f.I0)(),r=(0,f.tT)("@@initialState"),R=r.initialState,de=r.setInitialState,me=(0,B.useState)({}),se=(0,h.Z)(me,2),c=se[0],_e=se[1],ne=(0,B.useState)(!1),ce=(0,h.Z)(ne,2),te=ce[0],ve=ce[1],ae=(0,B.useState)(!1),ue=(0,h.Z)(ae,2),G=ue[0],ie=ue[1],V=(0,B.useState)(!1),X=(0,h.Z)(V,2),L=X[0],k=X[1],Se=(0,B.useState)(!1),Ee=(0,h.Z)(Se,2),he=Ee[0],xe=Ee[1],Ie=function(){var t=(0,j.Z)((0,a.Z)().mark(function l(){var d,u,n,m,D,v,E,x;return(0,a.Z)().wrap(function(g){for(;;)switch(g.prev=g.next){case 0:if(!(R!=null&&R.fetchUserInfo)){g.next=6;break}return g.next=3,R==null||(m=R.fetchUserInfo)===null||m===void 0?void 0:m.call(R);case 3:n=g.sent,g.next=11;break;case 6:return g.next=8,(0,F.PR)({});case 8:D=g.sent,v=D.data,n=v;case 11:if(window.ignoreFileSize=((d=n)===null||d===void 0||(u=d.user)===null||u===void 0?void 0:u.ignoreFileSize)||0,!n){g.next=17;break}return g.next=15,de(De=>(0,C.Z)((0,C.Z)({},De),{},{currentUser:n,adminauth:(0,o.Dh)(n),systemInfo:c}));case 15:return localStorage.setItem("dingAccess",((E=n)===null||E===void 0||(x=E.user)===null||x===void 0?void 0:x.dingOpenId)||"0"),g.abrupt("return",!0);case 17:return g.abrupt("return",!1);case 18:case"end":return g.stop()}},l)}));return function(){return t.apply(this,arguments)}}(),Pe=function(){var t=(0,j.Z)((0,a.Z)().mark(function l(d){var u,n,m,D,v,E;return(0,a.Z)().wrap(function(p){for(;;)switch(p.prev=p.next){case 0:if((c==null?void 0:c.needCheckCode)!="1"){p.next=7;break}if(u=(0,f.Kd)(),n=$("#drag").text(),m=u==="en-US"?"Successful verification":"\u9A8C\u8BC1\u901A\u8FC7",n===m){p.next=7;break}return z.ZP.warning(N.formatMessage({id:"pages.login.sliderVerify"})),p.abrupt("return");case 7:return p.prev=7,D={name:d.name,password:(0,o.HI)(d.password),isGraphicCode:0},p.next=11,(0,F.xS)((0,C.Z)({},D));case 11:if(v=p.sent,E=v.data,!(E!=null&&E.token)){p.next=20;break}return(0,o.d8)("token",E.token),localStorage.setItem("rember",d.rember),localStorage.setItem("name",d.rember?d.name:""),localStorage.setItem("password",d.rember?(0,o.HI)(d.password):""),b(),p.abrupt("return");case 20:p.next=24;break;case 22:p.prev=22,p.t0=p.catch(7);case 24:case"end":return p.stop()}},l,null,[[7,22]])}));return function(d){return t.apply(this,arguments)}}(),P=function(){var t=(0,j.Z)((0,a.Z)().mark(function l(){var d,u,n,m;return(0,a.Z)().wrap(function(v){for(;;)switch(v.prev=v.next){case 0:return v.next=2,(0,F.So)({});case 2:d=v.sent,u=d.data,n=u===void 0?{}:u,m=n.pluginList||[],window.isyz=m.findIndex(E=>E.type=="yzow"&&E.status==1)>-1,_e((0,C.Z)((0,C.Z)({},n),{},{registerConfig:JSON.parse(n.registerConfig||"{}"),thirdLoginConfig:JSON.parse(n.thirdLoginConfig||"[]")}));case 8:case"end":return v.stop()}},l)}));return function(){return t.apply(this,arguments)}}(),A=t=>{var l=document.createElement("iframe");l.src=t,l.style.display="none",document.body.appendChild(l)},b=function(){var t=(0,j.Z)((0,a.Z)().mark(function l(){var d,u,n,m,D,v,E,x;return(0,a.Z)().wrap(function(g){for(;;)switch(g.prev=g.next){case 0:return g.next=2,Ie();case 2:if(d=g.sent,u=document.body.clientWidth,n=/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)||u<768,m=(c==null?void 0:c.defaultHome)=="1"&&!n?"/desktop":"/",!d){g.next=15;break}if(f.m8){g.next=9;break}return g.abrupt("return");case 9:if(D=f.m8.location.query,v=D,E=v.redirect,x=v.isInfo,x!="1"){g.next=14;break}return location.href=E||m,g.abrupt("return");case 14:f.m8.push(E||m);case 15:case"end":return g.stop()}},l)}));return function(){return t.apply(this,arguments)}}();return(0,B.useLayoutEffect)(()=>{P(),localStorage.setItem("dingAccess","0")},[]),(0,B.useEffect)(()=>{if(G){var t="".concat(location.origin,"/api/ding/login/callback");window.DTFrameLogin({id:"dingLogin",width:280,height:280},{redirect_uri:encodeURIComponent(t),client_id:c.dingClientId,scope:"openid",response_type:"code",state:"dddd",prompt:"consent"},function(){var l=(0,j.Z)((0,a.Z)().mark(function d(u){var n,m,D,v;return(0,a.Z)().wrap(function(x){for(;;)switch(x.prev=x.next){case 0:if(n=u.authCode,!(n===pe||localStorage.getItem("dingAccess")=="1")){x.next=3;break}return x.abrupt("return");case 3:return pe=n,x.next=6,(0,f.WY)("/api/ding/login/callback",{method:"GET",params:{authCode:n,state:"dddd"}});case 6:m=x.sent,D=m.data,v=D===void 0?{}:D,v.token&&((0,o.d8)("token",v.token),b());case 9:case"end":return x.stop()}},d)}));return function(d){return l.apply(this,arguments)}}(),l=>{z.ZP.error(l)})}},[G]),(0,B.useEffect)(()=>{L&&le({type:"personal/getWechatData",payload:{type:"7"},callback:t=>{var l=t.sig,d=t.agentId,u="".concat(location.origin,"/enwechat_login.html?sig=").concat(l),n=(0,f.Kd)()=="en-US"?"en":"zh";new window.WwLogin({id:"wwLogin",login_type:"CorpApp",appid:"ww2d1e9bafb529c21f",agentid:d,self_redirect:!0,redirect_uri:encodeURIComponent(u),state:"STATE",lang:n})}})},[L]),(0,B.useEffect)(()=>{he&&le({type:"personal/getWechatData",payload:{type:"2"},callback:t=>{var l=t.appId,d=t.sig,u="https://dev.filesbox.cn/wechat_login.html?&cdnPath=".concat(window.location.origin,"&sig=").concat(d),n=(0,f.Kd)()=="en-US"?"en":"cn";new window.WxLogin({id:"wechatLogin",appid:l,scope:"snsapi_login,snsapi_userinfo",self_redirect:!0,redirect_uri:encodeURIComponent(u),state:"xxxx",lang:n})}})},[he]),(0,B.useEffect)(()=>{var t=l=>{if(l.data){var d=Object.prototype.toString.call(l.data)==="[object Object]"?l.data:JSON.parse(l.data),u=d.token,n=u===void 0?"":u;n&&n!="undefined"&&n!=null&&(0,o.d8)("token",n)}};return window.addEventListener("message",t),()=>{window.removeEventListener("message",t)}},[]),(0,s.jsx)(J.Z,{isLogin:!0,submitText:N.formatMessage({id:"common.login"}),initialValues:{rember:localStorage.getItem("rember")=="true",name:localStorage.getItem("name"),password:(0,o.pe)(unescape(localStorage.getItem("password")||""))},actions:(0,s.jsxs)("div",{className:T().loginActions,children:[(0,s.jsx)("div",{className:T().appBox,children:(0,s.jsxs)(K.Z,{children:[(0,s.jsx)("img",{width:24,src:ge(),onClick:()=>A("//update.wxbig.cn/filesbox/download/")}),(0,s.jsx)("a",{href:"https://apps.apple.com/app/filesbox/id1670829879",target:"_blank",children:(0,s.jsx)("img",{width:24,src:oe()})}),(0,s.jsx)(Q.Y,{width:248,submitter:!1,trigger:(0,s.jsx)("img",{width:24,src:Ce()}),children:(0,s.jsx)("img",{width:200,src:i()})})]})}),c!=null&&(fe=c.thirdLoginConfig)!==null&&fe!==void 0&&fe.length?(0,s.jsx)("div",{className:T().thirdLoginBox,children:te?(0,s.jsxs)(K.Z,{align:"center",className:T().loginIcon,children:[c!=null&&(Ae=c.thirdLoginConfig)!==null&&Ae!==void 0&&Ae.find(t=>t.thirdName=="dingding")?(0,s.jsx)(Q.Y,{width:340,submitter:!1,visible:G,onVisibleChange:ie,trigger:(0,s.jsx)("span",{className:T().dingding}),children:(0,s.jsx)("div",{id:"dingLogin",className:T().dingLogin})}):(0,s.jsx)(s.Fragment,{}),c!=null&&(M=c.thirdLoginConfig)!==null&&M!==void 0&&M.find(t=>t.thirdName=="enWechat")?(0,s.jsx)(Q.Y,{width:360,submitter:!1,visible:L,onVisibleChange:k,trigger:(0,s.jsx)("span",{className:T().enWechat}),children:(0,s.jsx)("div",{id:"wwLogin",className:T().wwLogin})}):(0,s.jsx)(s.Fragment,{}),c!=null&&(ee=c.thirdLoginConfig)!==null&&ee!==void 0&&ee.find(t=>t.thirdName=="wechat")?(0,s.jsx)(Q.Y,{width:340,submitter:!1,visible:he,onVisibleChange:xe,trigger:(0,s.jsx)("span",{className:T().wechat}),children:(0,s.jsx)("div",{id:"wechatLogin",className:T().wwLogin})}):(0,s.jsx)(s.Fragment,{})]}):(0,s.jsx)("span",{onClick:()=>ve(!0),children:(0,s.jsx)(f._H,{id:"admin.login.thirdLogin",defaultMessage:"\u5176\u4ED6\u65B9\u5F0F\u767B\u5F55"})})}):(0,s.jsx)(s.Fragment,{})]}),systemInfo:c,onFinish:Pe,children:(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(w.Z,{name:"name",fieldProps:{size:"large",prefix:(0,s.jsx)(_.Z,{})},placeholder:N.formatMessage({id:"pages.login.username.placeholder",defaultMessage:"\u8F93\u5165\u7528\u6237\u540D"}),rules:[{required:!0,whitespace:!0,message:(0,s.jsx)(f._H,{id:"pages.login.username.required",defaultMessage:"\u7528\u6237\u540D\u662F\u5FC5\u586B\u9879"})}]}),(0,s.jsx)(w.Z.Password,{name:"password",fieldProps:{size:"large",prefix:(0,s.jsx)(S.Z,{})},placeholder:N.formatMessage({id:"pages.login.password.placeholder",defaultMessage:"\u8F93\u5165\u5BC6\u7801"}),rules:[{required:!0,message:(0,s.jsx)(f._H,{id:"pages.login.password.required",defaultMessage:"\u5BC6\u7801\u662F\u5FC5\u586B\u9879"})}]}),(c==null?void 0:c.needCheckCode)=="1"&&(0,s.jsx)("div",{id:"drag"}),(0,s.jsxs)("div",{style:{marginBottom:24},children:[(0,s.jsx)(H.Z,{noStyle:!0,name:"rember",children:(0,s.jsx)(f._H,{id:"user.keepPwd",defaultMessage:"\u8BB0\u4F4F\u5BC6\u7801"})}),c!=null&&(Y=c.registerConfig)!==null&&Y!==void 0&&Y.enableRegister?(0,s.jsx)("a",{style:{float:"right"},onClick:()=>{f.m8.push("/register")},children:(0,s.jsx)(f._H,{id:"admin.login.regist",defaultMessage:"\u6CE8\u518C\u8D26\u53F7"})}):(0,s.jsx)(s.Fragment,{})]})]})})};U.default=ye},6417:function(){$.fn.drag=function(I){var U,e=this,O=!1,K={},I=$.extend(K,I),Z=localStorage.getItem("umi_locale")==="en-US"?"Drag the slider to the far right":"\u8BF7\u6309\u4F4F\u6ED1\u5757\uFF0C\u62D6\u52A8\u5230\u6700\u53F3\u8FB9",z='<div class="drag_bg"></div><div class="drag_text" onselectstart="return false;" unselectable="on">'+Z+'</div><div class="handler handler_bg"></div>';this.append(z);var a=e.find(".handler"),C=e.find(".drag_bg"),j=e.find(".drag_text"),h=e.width()-a.width();a.mousedown(function(o){O=!0,U=o.pageX-parseInt(a.css("left"),10)}),$(document).mousemove(function(o){var _=o.pageX-U;O&&(_>0&&_<=h?(a.css({left:_}),C.css({width:_})):_>h&&(a.css({left:h}),C.css({width:h}),F()))}).mouseup(function(o){O=!1;var _=o.pageX-U;_<h&&(a.css({left:0}),C.css({width:0}))}),a.on("touchstart",function(o){o.preventDefault();var _=o.originalEvent.targetTouches[0];O=!0,U=_.pageX-parseInt(a.css("left"),10)}),$(document).on("touchmove",function(o){o.preventDefault();var _=o.originalEvent.targetTouches[0],S=_.pageX-U;O&&(S>0&&S<=h?(a.css({left:S}),C.css({width:S})):S>h&&(a.css({left:h}),C.css({width:h}),F()))}),$(document).on("touchend",function(o){var _=o.originalEvent.changedTouches[0];O=!1;var S=_.pageX-U;S<h&&(a.css({left:0}),C.css({width:0}))}),$(document).on("touchcancel",function(o){a.css({left:0}),C.css({width:0})});function F(){a.removeClass("handler_bg").addClass("handler_ok_bg"),j.text(localStorage.getItem("umi_locale")==="en-US"?"Successful verification":"\u9A8C\u8BC1\u901A\u8FC7"),e.css({color:"#fff"}),a.unbind("mousedown"),a.unbind("touchstart"),$(document).unbind("mousemove"),$(document).unbind("touchmove"),$(document).unbind("mouseup"),$(document).unbind("touchend"),$(document).unbind("touchcancel")}}},32601:function(I,U,e){"use strict";e.d(U,{Z:function(){return Ae}});var O=e(39428),K=e(3182),Z=e(43358),z=e(34041),a=e(2824),C=e(81316),j=e.n(C),h=e(91152),F=e.n(h),o=e(67294),_=e(43504),S=e(30672),B=e(57663),Q=e(71577),w=e(34792),H=e(48086),f=e(37476),J=e(81473),re=e(59879),oe=e(84059),q=e(22003),ge=e(37406),Me=e(6417),Ce=e(73430),W=e.n(Ce),i=e(85893),y=null,T=()=>{var M=(0,S.YB)(),ee=(0,o.useState)(!1),Y=(0,a.Z)(ee,2),N=Y[0],le=Y[1],r=(0,o.useState)(""),R=(0,a.Z)(r,2),de=R[0],me=R[1],se=(0,o.useState)(""),c=(0,a.Z)(se,2),_e=c[0],ne=c[1],ce=(0,o.useState)(!1),te=(0,a.Z)(ce,2),ve=te[0],ae=te[1],ue=(0,o.useState)(!1),G=(0,a.Z)(ue,2),ie=G[0],V=G[1],X=(P,A)=>{$.ajax({type:"get",url:"/api/disk/getSystemTime",contentType:"application/json;charset=utf-8",dataType:"text",success:function(t){P&&P(t)},error:function(t){A&&A(t)}})},L=function(){var P=(0,K.Z)((0,O.Z)().mark(function A(){return(0,O.Z)().wrap(function(t){for(;;)switch(t.prev=t.next){case 0:X(function(l,d){$.ajax({type:"post",url:"/api/disk/findLoginAuthUrl",contentType:"application/json;charset=utf-8",dataType:"json",data:JSON.stringify({clientType:"webScanLogin",signature:(0,ge.x4)(JSON.stringify({nonceStr:Date.now(),timestamp:l})),timestamp:l}),success:function(n){var m=n!=null?n:{},D=m.success,v=m.code,E=m.message,x=m.data,p=x===void 0?{}:x;D?(ne(""),ae(!1),V(!1),me(p.qrUrl),Se(p.qrUrl.split("key=")[1])):H.ZP.error(M.formatMessage({id:v,defaultMessage:E}))},error:function(n){d&&d(n)}})});case 1:case"end":return t.stop()}},A)}));return function(){return P.apply(this,arguments)}}(),k=(P,A)=>{$.ajax({type:"post",url:"/api/disk/scanLogin",contentType:"application/json;charset=utf-8",dataType:"json",data:JSON.stringify(P),success:function(t){A&&A(t)},error:function(t){console.log("scanLogin",t)}})},Se=P=>{y&&y.close();var A="wss://".concat(window.location.host,"/websocket/webchat/scanLogin/").concat(P);y=new q.Z(A),y.onopen=Ee,y.onmessage=he,y.onerror=xe,y.onclose=Ie},Ee=()=>{y.send(JSON.stringify({action:"confirm",msgType:"webScanLogin"}))},he=P=>{var A=Pe(P.data)?JSON.parse(P.data):{type:P.data};switch(A.action){case"scan":V(!0);break;case"auth":k({tempAuth:A.tempAuth,sourceDomain:A.schoolDomain},function(b){var t=b.code,l=b.success,d=b.data,u=d===void 0?{}:d;if(l)(0,ge.d8)("token",u.token),y.send(JSON.stringify({action:"feedBack",actionVal:"success",msgType:"webScanLogin",tempAuth:A.tempAuth})),y.close(),location.href="/";else{var n=b.message;switch(parseInt(t)){case 701:n="\u4E34\u65F6\u6388\u6743\u7801\u65E0\u6548";break;case 601:n="\u4E8C\u7EF4\u7801\u5DF2\u5931\u6548";break;case 609:n="\u60A8\u672A\u6CE8\u518C\uFF0C\u8BF7\u5148\u6CE8\u518C";break;case 610:n="\u8D26\u53F7\u5DF2\u9501\u5B9A\uFF0C\u8BF7\u8054\u7CFB\u7BA1\u7406\u5458";break}ae(!0),ne(n),y.send(JSON.stringify({action:"feedBack",actionVal:"fail",msgType:"webScanLogin",tempAuth:A.tempAuth}))}});break;case"feedBack":break;default:A.code=="601"&&(ae(!0),ne("\u4E8C\u7EF4\u7801\u5DF2\u5931\u6548"))}},xe=()=>{},Ie=()=>{},Pe=P=>{if(typeof P=="string")try{var A=JSON.parse(P);return!!(typeof A=="object"&&A)}catch(b){return!1}};return(0,o.useEffect)(()=>{N?L():y&&y.close()},[N]),(0,i.jsxs)(f.Y,{width:300,visible:N,onVisibleChange:le,title:(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(J.Z,{style:{marginRight:8}}),M.formatMessage({id:"common.login"})]}),trigger:(0,i.jsx)("div",{className:W().qrLoginBtn}),submitter:!1,modalProps:{bodyStyle:{textAlign:"center"},maskClosable:!1},children:[(0,i.jsxs)("div",{className:W().qrcodeBox,children:[ve?(0,i.jsxs)("div",{className:W().qrMask,children:[(0,i.jsx)("p",{children:_e}),(0,i.jsx)(Q.Z,{type:"link",size:"small",icon:(0,i.jsx)(re.Z,{}),onClick:L,children:M.formatMessage({id:"user.codeRefresh",defaultMessage:"\u70B9\u51FB\u5237\u65B0"})})]}):null,ie?(0,i.jsx)("div",{className:W().qrMask,children:(0,i.jsx)("p",{children:"\u626B\u7801\u6210\u529F"})}):null,de?(0,i.jsx)(oe.ZP,{size:200,value:de}):(0,i.jsx)(i.Fragment,{})]}),M.formatMessage({id:"pages.login.scanLogin"})]})},s=T,pe={"en-US":"US English","ja-JP":"JP \u65E5\u672C\u8A9E","fr-FR":"FR Fran\xE7ais","es-ES":"ES Espa\xF1ol","ru-RU":"RU Ruso","zh-CN":"CN \u7B80\u4F53\u4E2D\u6587","zh-TW":"HK \u7E41\u9AD4\u4E2D\u6587","it-IT":"IT Italia"},ye={"en-US":"en-US","zh-TW":"zh-TW","zh-CN":"zh-CN","ru-RU":"ru-RU","ja-JP":"ja-JP","it-IT":"it-IT"},fe=M=>{var ee=M.isLogin,Y=M.submitText,N=M.initialValues,le=M.actions,r=M.systemInfo,R=M.onFinish,de=M.children,me=(0,o.useState)(window.location.origin),se=(0,a.Z)(me,2),c=se[0],_e=se[1],ne=(0,S.Kd)(),ce=(0,o.useState)(ne),te=(0,a.Z)(ce,2),ve=te[0],ae=te[1],ue=(0,S.I0)();(0,o.useEffect)(()=>{(r==null?void 0:r.needCheckCode)=="1"&&$("#drag").drag()},[r==null?void 0:r.needCheckCode]);var G=()=>{var ie=(0,S.XZ)(),V=ie.map((L,k)=>({value:L,label:pe[L]})),X=L=>{(0,S.i_)(L,!0),ae(L),ue({type:"common/setState",payload:{showmodalList:[]}})};return(0,i.jsx)("div",{className:W().languageSelect,children:(0,i.jsx)(z.Z,{bordered:!1,value:ve,defaultValue:"en-US",style:{width:120},size:"small",onChange:X,options:V})})};return(0,i.jsxs)("div",{className:W().container,children:[Object.keys(r).length?(0,i.jsxs)(S.ql,{children:[(0,i.jsx)("title",{children:"".concat((r==null?void 0:r.systemName)||"\u89C6\u9891\u4E2D\u5FC3"," - ").concat((r==null?void 0:r.systemDesc)||"\u8D44\u6E90\u7BA1\u7406\u5668")}),(0,i.jsx)("link",{rel:"shortcut icon",href:(r==null?void 0:r.browserLogo)||F()})]}):(0,i.jsx)(i.Fragment,{}),(0,i.jsx)("div",{style:{flex:"1",padding:"60px 0 32px",opacity:Object.keys(r).length?1:0},children:(0,i.jsxs)("div",{className:W().loginForm,children:[ee?(0,i.jsx)(s,{}):(0,i.jsx)(i.Fragment,{}),(0,i.jsx)(_.U,{contentStyle:{minWidth:280,maxWidth:"75vw"},logo:(0,i.jsx)("img",{alt:"logo",src:(r==null?void 0:r.systemLogo)||j()}),title:(r==null?void 0:r.systemName)||"\u89C6\u9891\u4E2D\u5FC3",subTitle:(0,i.jsxs)("div",{children:[(0,i.jsx)("div",{children:(r==null?void 0:r.systemDesc)||"\u8D44\u6E90\u7BA1\u7406\u5668"}),c.indexOf("demo.filesbox.cn")>-1?(0,i.jsx)("div",{style:{fontSize:13,color:"#722ed1"},className:"tyanuser",children:"\u4F53\u9A8C\u8D26\u53F7 Demo / Demo123"}):""]}),initialValues:N,actions:le,submitter:{searchConfig:{submitText:Y}},onFinish:function(){var ie=(0,K.Z)((0,O.Z)().mark(function V(X){return(0,O.Z)().wrap(function(k){for(;;)switch(k.prev=k.next){case 0:return k.next=2,R(X);case 2:case"end":return k.stop()}},V)}));return function(V){return ie.apply(this,arguments)}}(),children:de}),G()]})}),r!=null&&r.globalIcp?(0,i.jsx)("span",{className:W().copyRight,children:r.globalIcp}):(0,i.jsx)(i.Fragment,{})]})},Ae=fe},89688:function(I){I.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAAXNSR0IArs4c6QAAAERlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAA6ABAAMAAAABAAEAAKACAAQAAAABAAAAMKADAAQAAAABAAAAMAAAAADbN2wMAAAEKUlEQVRoBe2ZT0hUQRzH3V3NFMIu/SekQ1BY4cFQRHcViS7hKZIOBSEiJHWrU53q1LEiEBLB6tilJOwfq7IaxJISih4jS0IKqq2w1LXPT95b3xtnn87bfYdiH4wzv9/8ft/f9zfzZpw3W1RUeP7hEZicnNwUj8cHlpeXQ6ZpiI/4Coapr9M+7BRM21VVVX9CodCb4eHh06a+4iO+gmHq67Q3Hjmns7QTicSWxcXFl4xotLm5eZ46PDIycnBpaWlPOp3eLTbhcHgWsh8aGxunqdOM/Gbq4eLi4paGhoaU2Ph9ck5AAg8NDXVC9qi0IdZKEtukrT70zaF7JHrayVgs1q3amMo5JwD5vRDuoRwzCU4CzyntJDFj4qfa5rQGBgcHz0B8wpS8kBAf8RUMlZSJ7HsGLPJ9JsGy2TITZ5uamu5l6/fS+5oBdpAWQHtsYAiMUi5Svtu6bDU2KbGlf8Rh02NhOlQbaxonIDsIO8wDpr/EDkG7lxG8RT1m6zzqMbFlZ+q1bQSLTeC+YNu6jdbGrxCLtotgt50BGNGvyOOUGGQ8MbFdxm6IUo3tVurMQ18Xyd3JKDbQMJoBAkYgf0nFFSKUJoonefETG8vWRd7qu0xfRMX3ko0S4D09AlilF2COfZVsDodNMIwSYHSqTcB92hrFKDYJQgILvKdJEx9TW/AXTHy0CUA0xFR2AnQCwDnkG5xzpiORiJx5Zk0CmNoSb0p82JEO0JY1sR2xn8XdjSwbgOvRLjp2mmss1isOy2+0D7H1HUd/16HPe5MY7cR4BvAEpcIOgP46x46rtmzX2jUAQIdtYNUVZH9K0QUmWrEy5CWQhtNKfG0C9OxY6XX/2ekWA5V0sXScirIlECi7fIIXEsjnaPrBKsyAn1HLp09hBvI5mn6wCjPgZ9Ty6fN/zgBnkR+aUcrpBk2D56VaEz8Lp6xHiSdOdJzTnAafOnVBtok1IDGVGC5Odp/2FSorKzuPwUNA5ikzlHPRaPS17RR0LbEkphV7XrhYnNaE1n7Q1NbWfsHypHzY8Kz5iFiDEoCCs38fsH3rcdDOgM1HJc+Z/KfdF2DtiqFyUON6JqAal5SUyA1cTvf5KqYi/0YeVXSeolEC3OW/J4EOyi9PVH+dPwXb9LZa+028XvxkMlmRSqXk/kZ+qJDfAy5ofOTW+ZPosdmFjbzTrgf9TfSPUc6zSN/W1dWte7fqAkDQLmLVSJVramrkIz8hem4v9qv9lpzgJuOdtLkk2AdRS71aoZvC5sWqxrxl9AqZwwfvUUgg+DH2jlCYARai9pBXXl6e0fMPMNN2jidnHq3eabNeO+cZKC0tjbMduk6PyKPWcWQlPjvNZ3SvnGTEh7vWuFPnp51zAvX19R8J3AqhcYr8/tXPrLSpZNC3SZ9lMy4+/GMM9KJY5VCQgxiBv5O5gTdVViRMAAAAAElFTkSuQmCC"},59646:function(I){I.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAAXNSR0IArs4c6QAAAERlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAA6ABAAMAAAABAAEAAKACAAQAAAABAAAAMKADAAQAAAABAAAAMAAAAADbN2wMAAAESUlEQVRoBe2ZS0hVQRzGvVdNKx89lIogw4IgAwNFqHxCC5FEAiGwJGpTlIvaVFa06bFoE0nQxhaBLYoIowIX2c1Xat0eUlZGoYVhC80Cr7fy1W/gLC7H85i553hWd2CYMzP//zffN68zZ05cXCzEesBRD/gceTtwDgQCy/x+f8Xs7Ox2n8/nLy0tPRINXEI0Tk58IJ4C4dNgHIP8Yg3rebSYngqA/HrIP5ibm9uiI/xGl5fO+qUtHRr29vauhHyrAXmB3BwtvGcjMDk5eQOS2XqiiHpdUlLSoi+XzXsyAkydQghV6UlBfopYR5zT18nmPREAwQN6QpTNUnaI3n+mr1PJezWFynWkxthC9xcXFz/SlStnF3wE+vv7F8FqjcbsCz1/PjU1dYMb5AVm1C8ybUvcxq6yCZzVEPtK7MvIyHick5PzTyMcJwSMjo7mx8fHDxcWFn7r6elZFQ6HK7EtwCaTGCYOEQMpKSlP8/Pzp3iWDsoC2tvbC2ZmZk5AYDfk540g5eOU34FBY1lZWVAw6ezsTJ2enq7m8SBxB/WG7Qpf6i9Q34DvNM+2wRDIzKutre0k4JeI84gb+UBolPIR7DeTxhvZGJXh94E1Us00e29UH1kmJQACPsjfJK2NdF7IZ0T8RESRnQipnoT8cS/Ji46hvRWclertOsl2BDo6OvKY890AJtqBuVj/nd6v4R3Rbodp+x6A/FmPyYcgXQ75d3bkRb3lFOrq6sqCfKUMkFs2zP2r7EBS5G0FsPXtw0h693BDBFPnmgqO5QjQ++Jl42UYYNcZUWnQTkCeCphTW6bPS1UMUwHaGWatKqBD+zFVf1MB4+PjSapgLtiLo4RSMBXASyRZCckFY9bcUlUYUwHp6emTqmAu2G9UxTAVkJubG2JR/VIFdGJPe2XBYHCJCoapAA1kWAXMqS1TKG1iYmKvCo6dgLcqYC7ZXuT8tVwWy1IAQ9olC+SWHaOQyfmriVTqBGApgNd6p1vEVHAgX8ERvlFGhOVxGgDxITNEuk6FgFu2zICHtF3L4c50M7EcAQDEhdNttwip4kB+Fxz6tFOBobulAOHBbcItQ0/vCj9H3nLom7UVUFRU1EcvBPSOXuUZhetWbdkKEM6AXLYCWcC6AX583LPClxLAImphFDzfkdgFz9GuuEM1DVIChDdroY5kxhTJ5QqIP+G7WFyQWQZpAWIt0CNXLNFcqoT8H6btURk4aQECjHvPMyR9MsBObBBwimn7UQZDSYDYzhITE/fQgPKHhwwZzeY+38UNsvZKAgQoN8wDJFXEvyJvFxBruQgj/bF9wdV7Dan0HxtlAaJBtrYOkdDQYCQB8UyZ+G3UzKKvZs1kM5eTGLW0hISErVTXU/dJ76P5NSUnJ+/kel3pQ8ryLGTUUGQZHx/poVDoMCTF9ctv0lekd5m/PyLt9M+cr4qxLaU8i3QQUa10SrfeLpaP9YAHPfAfB9FimdsBZi4AAAAASUVORK5CYII="},2975:function(I){I.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAABsklEQVRIS81V7VXCQBCc6wArECoQK5C9BqQDoQKxArUCpQKhAq0gCxUYO4gVaAfrm7yDF44Ll/zgPfdnbjMz++1wZnNnxsf/IVDVMYArAEMAlZlV3vttLgPZCFT1FcD9CSC+P4vIb8qnlSAofgNA5TmrAMxFZBM7JgkC+GcONfEuMUmSoCiK0jnHfPc1RnLdTNcRQS7nZvblnJuKCMGgqgsALw0lSxHht9pSBJaRPQIwCIUvRWQZixKRPe4BQS73ZvbtvR+q6geA2yBEAEwAPDaE7WsRE8wAsHMOjMBBdem9nxRFsXHO3QCo06GqKwB3jZ8eRITte5giVX2KlNCnduabmU0aBJWIzFSV6jXSxLkg1hFBHMFOYT1sZraNIihb5qQ1glhNrURV68InCNr6IV2D0HbNLmIaRqo6BcDoGDbb8yfTaRe7WegyBysRmQdyLrr3zPpYszan5mBgZpzky0gllZOg1dhtzrnxyUkOSlOd0WVtdNtFOxIzWyUiOSIKymedt+kOQVW5EljYU/dgDWDR+x7EMsNA8TaQlMeFM8BdlDw0rUXukug+PtmT2Qcs5fsH0SrVGT4JBgAAAAAASUVORK5CYII="},85050:function(I,U,e){I.exports=e.p+"static/wechat.811e07a6.jpg"}}]);