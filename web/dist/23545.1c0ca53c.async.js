(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[23545],{21059:function(M,x,t){"use strict";t.d(x,{Z:function(){return T}});var A=t(28991),E=t(67294),N={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M862 465.3h-81c-4.6 0-9 2-12.1 5.5L550 723.1V160c0-4.4-3.6-8-8-8h-60c-4.4 0-8 3.6-8 8v563.1L255.1 470.8c-3-3.5-7.4-5.5-12.1-5.5h-81c-6.8 0-10.5 8.1-6 13.2L487.9 861a31.96 31.96 0 0048.3 0L868 478.5c4.5-5.2.8-13.2-6-13.2z"}}]},name:"arrow-down",theme:"outlined"},I=N,U=t(27029),d=function(v,h){return E.createElement(U.Z,(0,A.Z)((0,A.Z)({},v),{},{ref:h,icon:I}))};d.displayName="ArrowDownOutlined";var T=E.forwardRef(d)},26139:function(M,x,t){"use strict";t.d(x,{Z:function(){return T}});var A=t(28991),E=t(67294),N={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M868 545.5L536.1 163a31.96 31.96 0 00-48.3 0L156 545.5a7.97 7.97 0 006 13.2h81c4.6 0 9-2 12.1-5.5L474 300.9V864c0 4.4 3.6 8 8 8h60c4.4 0 8-3.6 8-8V300.9l218.9 252.3c3 3.5 7.4 5.5 12.1 5.5h81c6.8 0 10.5-8 6-13.2z"}}]},name:"arrow-up",theme:"outlined"},I=N,U=t(27029),d=function(v,h){return E.createElement(U.Z,(0,A.Z)((0,A.Z)({},v),{},{ref:h,icon:I}))};d.displayName="ArrowUpOutlined";var T=E.forwardRef(d)},76734:function(M){M.exports={newColorPicker:"newColorPicker___2va38"}},99026:function(M){M.exports={root:"root___5nrfZ",colorWeak:"colorWeak___kupbm","ant-layout":"ant-layout___1DD9B",globalSpin:"globalSpin___3uQ9r","ant-table":"ant-table___1sDrd","ant-table-thead":"ant-table-thead___2H8Ay","ant-table-tbody":"ant-table-tbody___2ZNQk",formBox:"formBox___Vglm1",hrefModal:"hrefModal___1AVcP",hoverClick:"hoverClick___2HAJP",shop:"shop___3b4U5"}},96328:function(M,x,t){"use strict";var A=t(67294),E=t(79941),N=t(53390),I=t(76734),U=t.n(I),d=t(85893);function T(h){var c=h.offsetTop;return h.offsetParent!=null&&(c+=T(h.offsetParent)),c}function F(h){var c=h.offsetLeft;return h.offsetParent!=null&&(c+=F(h.offsetParent)),c}class v extends A.Component{constructor(){super(...arguments);this.state={displayColorPicker:!1,pageX:0,pageY:0},this.handleClick=c=>{var y=this.props.cssData,D=y===void 0?{devX:0,devY:0}:y,W=D.devX,j=D.devY;this.first=!0;var P=root.clientWidth,B=root.clientHeight,L=$(c.target),g=L.offset().left+W,b=L.offset().top+L.outerHeight()+5+j;g+220>P&&(g=P-220),b+333>B&&(b=B-333),this.setState({displayColorPicker:!this.state.displayColorPicker,pageX:g,pageY:b})},this.handleClear=()=>{var c=this.props.onChange,y=c===void 0?D=>{}:c;this.setState({displayColorPicker:!1,scrollTop:0}),y("transparent")},this.handleClose=()=>{this.setState({displayColorPicker:!1,scrollTop:0})}}render(){var c=this.props,y=c.className,D=y===void 0?"colorPicker":y,W=c.onChange,j=W===void 0?e=>{}:W,P=c.value,B=P===void 0?"#000000":P,L=c.style,g=this.state,b=g.pageX,G=g.pageY,X=e=>{e.rgb.a<1&&!(this.first&&e.rgb.a==0)?j("rgba(".concat(e.rgb.r,", ").concat(e.rgb.g,", ").concat(e.rgb.b,", ").concat(e.rgb.a,")")):j(e.hex),this.first=!1},p=(0,E.ZP)({default:{color:{height:"100%",borderRadius:"2px",background:B},colorBox:{height:"100%",borderRadius:"2px",boxShadow:"0 0 0 1px rgba(0,0,0,.1)",background:'url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAMUlEQVQ4T2NkYGAQYcAP3uCTZhw1gGGYhAGBZIA/nYDCgBDAm9BGDWAAJyRCgLaBCAAgXwixzAS0pgAAAABJRU5ErkJggg==")'},swatch:{width:"100%",height:"100%",padding:"2px",background:"#fff",borderRadius:"1px",boxShadow:"0 0 0 1px rgba(0,0,0,.1)",display:"inline-block",cursor:"pointer"},popover:{position:"fixed",zIndex:"101",background:"#ccc",top:G,left:b},cover:{position:"fixed",top:"0px",right:"0px",bottom:"0px",left:"0px"},clearBtn:{position:"absolute",bottom:"10px",height:"18px",lineHeight:"18px",right:"8px",zIndex:1,fontSize:"12px",cursor:"pointer"}}});return(0,d.jsxs)("div",{style:L,className:D+" "+U().newColorPicker,children:[(0,d.jsx)("div",{style:p.swatch,onClick:this.handleClick,children:(0,d.jsx)("div",{style:p.colorBox,children:(0,d.jsx)("div",{style:p.color})})}),this.state.displayColorPicker?(0,d.jsxs)("div",{style:p.popover,children:[(0,d.jsx)("div",{style:p.clearBtn,onClick:this.handleClear,children:"\u6E05\u7A7A"}),(0,d.jsx)("div",{style:p.cover,onClick:this.handleClose}),(0,d.jsx)(N.xS,{color:B,onChange:X})]}):null]})}}x.Z=v},23545:function(M,x,t){"use strict";t.r(x),t.d(x,{default:function(){return se}});var A=t(71194),E=t(50146),N=t(66456),I=t(5322),U=t(57663),d=t(71577),T=t(77883),F=t(85986),v=t(11849),h=t(88983),c=t(47933),y=t(47673),D=t(4107),W=t(67294),j=t(26139),P=t(21059),B=t(28216),L=t(89256),g=t(56173),b=t(96328),G=t(30672),X=t(99026),p=t.n(X),e=t(85893),q,ee,ge=D.Z.TextArea,ae=c.ZP.Group,se=(q=(0,B.$j)(V=>{var n=V.commondesign,_=V.globaldesign;return{commondesign:n,globaldesign:_}}),q(ee=class extends W.PureComponent{constructor(){super(...arguments);this.state={visible:!1,menuIco:"icon-outdent",navList:[{name:"\u9996\u9875",href:{name:"\u9996\u9875",url:"/",data:'{"data":{"designId":"home","url":"/"},"hrefType":"page","openMode":"_blank"}'},before:"",nid:0}],openStyle:"left",moduleColor:"#fff",textColor:"#314659",linesHeight:40,showEdit:99},this.updataDomCallBack=n=>{var _=this,R=this.props,S=R.commondesign,Y=R.globaldesign,u=S.HeaderNav,O=u.element,z=u.callback,te=u.callClose,r=S.designId,w,k=$("#canvasId"),f=O.attr("data-mid");k.find("#drawer-".concat(f)).length>0?(w=k.find("#drawer-".concat(f)),w.attr("class","drawer drawer-".concat(n.openStyle," drawer-open"))):(k.append('<div id="drawer-'.concat(f,'" class="drawer drawer-').concat(n.openStyle,' drawer-open" ></div>')),w=k.find("#drawer-".concat(f)),w.on("click",function(){$(this).removeClass("drawer-open")}));for(var Z=`<style>
    #drawer-`.concat(f,` .drawer-content *{
      color:`).concat(n.textColor,`;
    }
    #drawer-`).concat(f,` .drawer-content a{
      color:`).concat(n.textColor,`;
    }
    #drawer-`).concat(f,` .drawer-content {
      background-color:`).concat(n.moduleColor,`;
    }
    #drawer-`).concat(f," .drawer-title, #drawer-").concat(f,` li{
      height: `).concat((0,g.bf)(n.linesHeight),`;
      line-height: `).concat((0,g.bf)(n.linesHeight),`;
    }
  </style>
  <div class="drawer-mask"></div>
  <div class="drawer-content" >
    <div class="drawer-title"><img src="`).concat(Y.logo,`" /><span>\u9996\u9875</span></div>
    <ul class="drawer-menu">
  `),K=0,J=n.list.length;K<J;K++){var H=n.list[K];Z+='<a hrefd="'.concat(H.href.url,'"><li>').concat(H.before?"<img src='".concat(H.before,"'/>"):"","<span>").concat(H.name,"</span></li></a>")}Z+="</ul></div>",w.html(Z),O.find(".box").html('<i class="iconfont '+n.menuIco+'"></i>'),O.attr("data-headernav",JSON.stringify(n)),O.attr("data-audition",n.audition.join(","))},this.show=n=>{n?(n=JSON.parse(n),this.setState({menuIco:n.menuIco||"icon-outdent",navList:n.list,openStyle:n.openStyle,moduleColor:n.moduleColor,textColor:n.textColor,linesHeight:n.linesHeight})):this.setState({menuIco:"icon-outdent",navList:[{name:"\u9996\u9875",href:{name:"\u9996\u9875",url:"/",data:'{"data":{"designId":"home","url":"/"},"hrefType":"page","openMode":"_blank"}'},before:"",nid:0}],openStyle:"left",moduleColor:"#fff",textColor:"#314659",linesHeight:40}),this.setState({visible:!0,showEdit:99})},this.cancelEvent=()=>{var n=this,_=this.props.dispatch;_({type:"commondesign/setState",payload:{HeaderNav:{visible:!1,callback:()=>{},callClose:()=>{}}}})}}componentDidMount(){var n=this.props.commondesign,_=n.HeaderNav,R=_.visible,S=_.element,Y=_.callback,u=_.callClose;this.setState({visible:R});var O=S.attr("data-headernav");this.show(O)}render(){var n=this,_=this.props,R=_.commondesign,S=_.dispatch,Y=R.HeaderNav.callback,u=this.state,O=u.visible,z=u.menuIco,te=u.showEdit,r=u.navList,w=u.openStyle,k=u.moduleColor,f=u.textColor,Z=u.linesHeight,K=(0,G.lw)((0,G.Kd)()),J=o=>{var s={menuIco:z,list:r,openStyle:w,moduleColor:k,textColor:f,linesHeight:Z},a=[];Array.from(r).forEach(l=>{var i=l.href.url||"";i.indexOf("/common/audition.html")>-1&&a.push((0,L.jS)("courseWareId",i))}),s.audition=a,n.updataDomCallBack(s),Y&&Y(s),this.cancelEvent()},H=()=>{this.cancelEvent()},ne=()=>{for(var o={},s=0,a=r.length;s<a;s++){var l=r[s];o["n"+l.nid]=!0}for(var i=r.length,m=0,C=r.length;m<C;m++)o["n"+m]||(i=m);this.setState({navList:[...r,{name:"\u83DC\u5355\u540D\u79F0",href:{name:"\u83DC\u5355\u94FE\u63A5",url:""},before:"",after:"",nid:i}]})},oe=(o,s,a)=>{this.setState({showEdit:a})},le=(o,s,a)=>{r[a].name=o.target.value,this.setState({showEdit:99,navList:r})},Ce=o=>{var s=o.courseWareType+"",a=o.courseWareId,l="",i=!1;switch(s){case"1":i=!0;break;case"2":i=!0;break;case"3":i=!0;break;case"4":i=!0;break;case"5":i=!0;break;default:i=!1}return l=i?"/liveclass/wap/livecourse.html?cwid=".concat(a):"/views/wap/course_wap.html?cwid=".concat(a),l},re=(o,s,a)=>{S({type:"commondesign/setState",payload:{Href:{visible:!0,type:{course:!0,information:!0,link:!0,dialog:!0,page:!0,member:!0,shop:!0,forum:!0,question:!0},typex:!1,callback:l=>{var i=l.data,m=(0,g.wS)(l,n),C=m.url,Q=r[a].href;Q.name=i.title||i.courseName||i.typeName||i.courseWareName||i.classifyName||i.vipName||i.shopName||i.name||C,Q.url=C,Q.data=JSON.stringify(l),this.setState({navList:[...r]})}}}})},ie=(o,s,a)=>{window.fileBoxApi.fileSelect({title:K.formatMessage({id:"explorer.selectImage"}),type:"file",single:!0,allowExt:"jpg,jpeg,png",callback:function(i){var m=i.data||{},C=m.selectIds.length?m.selectItems||[]:[];C.length&&(r[a].before=C[0].path,n.setState({navList:[...r]}))}})},Ee=(o,s,a)=>{window.fileBoxApi.fileSelect({title:K.formatMessage({id:"explorer.selectImage"}),type:"file",single:!0,allowExt:"jpg,jpeg,png",callback:function(i){var m=i.data||{},C=m.selectIds.length?m.selectItems||[]:[];C.length&&(r[a].after=C[0].path,n.setState({navList:[...r]}))}})},ce=(o,s,a)=>{var l=r.splice(a,1)[0];r.splice(a-1,0,l),this.setState({navList:[...r]})},de=(o,s,a)=>{var l=r.splice(a,1)[0];r.splice(a+1,0,l),this.setState({navList:[...r]})},he=(o,s,a)=>{r.splice(a,1),this.setState({navList:[...r]})},ue=[{title:"\u83DC\u5355\u540D\u79F0",key:"name",width:"25%",render:(o,s,a)=>(0,e.jsx)("span",{className:p().hoverClick,children:te==a?(0,e.jsx)(D.Z,{onBlur:l=>le(l,s,a),autoFocus:!0,defaultValue:s.name,maxLength:8}):(0,e.jsx)("span",{onClick:l=>oe(l,s,a),children:(0,e.jsx)("span",{title:s.name,className:"name",children:s.name?s.name:"\u672A\u547D\u540D"})})})},{title:"\u94FE\u63A5",key:"href",width:"45%",render:(o,s,a)=>(0,e.jsx)("span",{className:p().hoverClick,children:(0,e.jsx)("span",{children:(0,e.jsx)("span",{onClick:l=>re(l,s,a),title:s.href.name,className:"name",style:{maxWidth:140},children:s.href.name?s.href.name:"\u83DC\u5355\u94FE\u63A5"})})})},{title:"\u56FE\u6807",key:"before",render:(o,s,a)=>(0,e.jsx)("span",{className:p().hoverClick,onClick:l=>ie(l,s,a),children:s.before?(0,e.jsx)("img",{src:s.before}):(0,e.jsx)("div",{className:"place",children:"\u65E0\u56FE"})})},{title:"\u6392\u5E8F",key:"index",render:(o,s,a)=>(0,e.jsxs)("span",{className:p().hoverClick,children:[a!=0?(0,e.jsx)(j.Z,{onClick:l=>ce(l,s,a)}):(0,e.jsx)(j.Z,{style:{color:"#ccc",cursor:"not-allowed"}}),a!=r.length-1?(0,e.jsx)(P.Z,{onClick:l=>de(l,s,a)}):(0,e.jsx)(P.Z,{style:{color:"#ccc",cursor:"not-allowed"}})]})},{title:"\u64CD\u4F5C",key:"action",render:(o,s,a)=>(0,e.jsx)("span",{children:s.nid==0?(0,e.jsx)("span",{style:{color:"#ccc",cursor:"not-allowed"},children:"\u5220\u9664"}):(0,e.jsx)("a",{href:"javascript:;",onClick:l=>he(l,s,a),children:"\u5220\u9664"})})}],ve={className:"iconfont ".concat(z),style:{position:"absolute",top:0,right:-40,width:30,height:28,fontSize:32,color:"#333"},onClick:()=>{S({type:"commondesign/setState",payload:{Iconfont:{visible:!0,callback:o=>{console.log("data",o),n.setState({menuIco:o.icon})},callClose:()=>{}}}})}},pe={value:w,style:{position:"absolute",top:0,right:-180,width:170,height:28},onChange:o=>{this.setState({openStyle:o.target.value})}},_e={value:k,style:{position:"absolute",top:0,right:-75,width:60,height:28},onChange:o=>{this.setState({moduleColor:o})}},fe={value:f,style:{position:"absolute",top:0,right:-75,width:60,height:28},onChange:o=>{this.setState({textColor:o})}},me={value:Z,style:{position:"absolute",top:0,right:-75,width:60,height:32},onChange:o=>{this.setState({linesHeight:o})}};return(0,e.jsx)(E.Z,{title:"\u83DC\u5355\u8BBE\u7F6E",visible:O,onOk:J,width:680,onCancel:H,maskClosable:!1,className:p().hrefModal,children:(0,e.jsxs)("div",{className:"divRow",children:[(0,e.jsxs)("div",{className:"label",style:{width:110},children:["\u83DC\u5355\u56FE\u6807\uFF1A",(0,e.jsx)("i",(0,v.Z)({},ve))]}),(0,e.jsxs)("div",{className:"label",style:{width:110},children:["\u5F39\u51FA\u4F4D\u7F6E\uFF1A",(0,e.jsxs)(ae,(0,v.Z)((0,v.Z)({},pe),{},{children:[(0,e.jsx)(c.ZP,{value:"left",children:"\u5DE6\u4FA7\u5C4F"}),(0,e.jsx)(c.ZP,{value:"right",children:"\u53F3\u4FA7\u5C4F"})]}))]}),(0,e.jsxs)("div",{className:"label",style:{width:110},children:["\u80CC\u666F\u989C\u8272\uFF1A",(0,e.jsx)(b.Z,(0,v.Z)({},_e))," "]}),(0,e.jsxs)("div",{className:"label",style:{width:110},children:["\u6587\u5B57\u989C\u8272\uFF1A",(0,e.jsx)(b.Z,(0,v.Z)({},fe))," "]}),(0,e.jsxs)("div",{className:"label",style:{width:110},children:["\u83DC\u5355\u9879\u9AD8\u5EA6\uFF1A",(0,e.jsx)(F.Z,(0,v.Z)({},me)),(0,e.jsx)(d.Z,{type:"primary",onClick:ne,style:{position:"absolute",top:0,right:-560},children:"\u65B0\u5EFA\u83DC\u5355\u9879"})]}),(0,e.jsx)("div",{className:"content",style:{padding:"0 20px"},children:(0,e.jsx)(I.Z,{rowKey:o=>o.nid,columns:ue,dataSource:r,pagination:!1})})]})})}})||ee)}}]);