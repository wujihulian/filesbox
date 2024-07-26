(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[63917],{54049:function(Q,j,a){"use strict";a.r(j),a.d(j,{default:function(){return G}});var X=a(71194),K=a(50146),q=a(13062),W=a(71230),ss=a(89032),O=a(15746),es=a(77883),S=a(85986),ts=a(63185),N=a(9676),ns=a(36877),w=a(18480),ls=a(34792),Y=a(48086),k=a(67294),F=a(28216),Z=a(30381),_=a.n(Z),H=a(96278),$=a.n(H),J=a(5283),as=a.n(J),V=a(87269),is=a.n(V),z=a(54870),cs=a.n(z),rs=a(41273),r=a(85893),R,L,G=(R=(0,F.$j)(I=>{var s=I.commondesign,l=I.page;return{commondesign:s,page:l}}),R(L=class extends k.PureComponent{constructor(){super(...arguments);this.state={visible:!1,styleType:"xtwlb",classify:[],startNum:1,endNum:10,list:[],options:[],loading:!1,sTeacher:0,sNum:0,sDate:0,sTitle:0,sborder:0,isAll:!1},this.listAllSetting=()=>{var s=this.props.dispatch;s({type:"page/listAllSetting",payload:{showCount:1},callback:l=>{this.setState({visible:!0,options:l.data})}})},this.updataDomCallBack=s=>{var l=this,o=this.props.differenceType;o==1?l.pcDom(s):l.mobileDom(s)},this.pcDom=s=>{var l=this.props.commondesign,o=l.Article,h=o.visible,t=o.element,u=o.callback,g=o.callClose,n="",v,f=0,c=s.list.length,d=s.list[0],m=""+(d.computerPicPath?JSON.parse(d.computerPicPath).sourcePath||JSON.parse(d.computerPicPath)[0].sourcePath:"/design/designstatic/common/image/information/defult.jpg");t.css("height","auto");var M=s.sTeacher,C=s.sDate,y=s.sNum,x=s.sTitle,E=e=>"/pubinfo/".concat(e.infoID,".shtml"),P=e=>M?'<p class="teacher">'.concat(e.realName||e.nickname,"</p>"):"",b=e=>C?'<span class="sDate">'.concat(_()(e.gmtCreate).format("YYYY-MM-DD"),"</span>"):"",T=e=>y?'<span class="sNum"><i class="iconfont icon-eye"></i>'.concat(e.viewCount,"</span>"):"",B=e=>x?'<span class="title moreTitle">'.concat(e.title,"</span>"):'<p><span class="title">'.concat(e.title,'</span></p><p class="introduce">').concat(e.introduce,"</p>");switch(s.styleType){case"xtwlb":t.css({width:"100%",left:0}),n='<ul class="xtwlb newxtwlb">',v=e=>`<li>
                   <a href="`.concat(E(e),`">
                     <div class="coverBox">
                       <div class="maskBox">
                         <img src="`).concat(e.thumb||e.avatar,`"/>
                         <div class="coverMask">`).concat(e.introduce,`</div>
                       </div>
                     </div>
                     <p class="title">`).concat(e.title,`</p>
                     `).concat(P(e),`
                     <p class="info">
                       `).concat(T(e),`
                       `).concat(b(e),`
                     </p>
                   </a>
                 </li>`);break;case"twlb":t.css({width:"100%",left:0}),n='<ul class="twlb newtwlb">',v=e=>`<li>
                    <a href="`.concat(E(e),`">
                    <div class="coverBox">
                      <img src="`).concat(e.computerPicPath,`"/>
                    </div>
                    <div class="rightBox">
                      `).concat(B(e),`
                      `).concat(P(e),`
                      <p class="info">
                        `).concat(T(e),`
                        `).concat(b(e),`
                      </p>
                    </div>
                    </a>
                  </li>`);break;case"btrqlb":t.css({width:"100%",left:0}),n='<ul class="btrqlb newbtrqlb">',v=e=>parseInt(s.sTitle)==1?`<li>
                      <a href="`.concat(E(e),`">
                      <div class="rightBox">
                        <p><span class="title">`).concat(e.title,`</span></p>
                      </div>
                      </a>
                    </li>`):`<li>
                    <a href="`.concat(E(e),`">
                    <div class="rightBox">
                      `).concat(B(e),`
                      `).concat(P(e),`
                      <p class="info">
                        `).concat(T(e),`
                        `).concat(b(e),`
                      </p>
                    </div>
                    </a>
                  </li>`);break;case"new3":t.css({width:"100%",left:0}),f=1,c=c>6?6:c,n=' <ul class="btrqlb new3">',n+=`<div class="topBox">
                        <div class="leftbox">
                          <img src="`.concat(m,`">
                        </div>
                        <div class="rightbox">
                          <div class="headBox">
                            <div class="title">`).concat(d.title,`</div>
                            <div class="time">`).concat(parseInt(s.sDate)==1?'<span class="sDate">'.concat(_()(d.gmtCreate).format("YYYY-MM-DD"),"</span>"):"",`</div>
                          </div>
                          <div class="conBox">
                            <p class="info">`).concat(d.introduce,`</p>
                          </div>
                        </div>
                     </div>`),v=e=>`<li >
                    <a href="/pubinfo/`.concat(e.infoID,`.shtml">
                      <div class="conBox">
                        <img src="`).concat(e.computerPicPath,`">
                        <p class="title">`).concat(e.title,`</p>
                        <p class="time">
                          `).concat(parseInt(s.sDate)==1?'<span class="sDate">'.concat(_()(e.gmtCreate).format("YYYY-MM-DD"),"</span>"):"",`
                        </p>
                       </div>
                     </a>
                   </li>`);break;case"new4":t.css({width:"100%",left:0}),f=1,c=c>5?5:c,n='<ul class="btrqlb new4">',n+=`<div class="leftbox">
                      <div class="top">
                        <img src="`.concat(m,`">
                      </div>
                      <div class="conbox">
                        <div class="left">
                          `).concat(parseInt(s.sDate)==1?`<span class="sDate">
                            <span class="dateTop">`.concat(_()(d.gmtCreate).format("DD"),`</span>
                            <span class="dateBottom">`).concat(_()(d.gmtCreate).format("YYYY/MM"),`</span>
                          </span>`):"",`
                        </div>
                        <div class="right">
                          <div class="title">`).concat(d.title,`</div>
                          <p class="info">`).concat(d.introduce,`</p>
                        </div>
                      </div>
                    </div>`),v=e=>`<li>
                    <a href="/pubinfo/`.concat(e.infoID,`.shtml">
                      <div class="conbox">
                        <div class="left">
                          `).concat(parseInt(s.sDate)==1?`<span class="sDate">
                            <span class="dateTop">`.concat(_()(e.gmtCreate).format("DD"),`</span>
                            <span class="dateBottom">`).concat(_()(e.gmtCreate).format("YYYY/MM"),`</span>
                          </span>`):"",`
                        </div>
                        <div class="right">
                          <div class="title">`).concat(e.title,`</div>
                          <p class="info">`).concat(e.introduce,`</p>
                        </div>
                      </div>
                    </a>
                  </li>`);break}for(var p=document.createElement("div"),D=f,i=c;D<i;D++){var A=s.list[D];A.computerPicPath=data.thumb||data.avatar?data.thumb||data.avatar:"/design/designstatic/common/image/information/defult.jpg",p.innerHTML=A.detail,A.detail=p.innerText,n+=v(A,D)}n+="</ul>";var U=t.find(".boxHtml");U.length>0?U.html(n):t.html('<div class="boxHtml">'+n+"</div>"),t.css({backgroundColor:"#fff"}),t.attr("data-article",JSON.stringify({styleType:s.styleType,classify:s.classify,startNum:s.startNum,endNum:s.endNum,sTeacher:s.sTeacher,sNum:s.sNum,sTitle:s.sTitle,sborder:s.sborder,sDate:s.sDate,isAll:s.isAll}))},this.mobileDom=s=>{var l=this,o=this.props,h=o.commondesign,t=o.differenceType,u=h.Article,g=u.visible,n=u.element,v=u.callback,f=u.callClose,c="",d;n.css({height:"auto"});var m=s.sDate,M=s.sNum,C=s.sTitle,y=i=>"/pubinfo/".concat(i.infoID,".shtml"),x=i=>m?'<span class="teacher">'+_()(i.gmtCreate).format("YYYY-MM-DD")+"</span>":"",E=i=>M?'<span class="curren">'+i.viewCount+"</span>":"",P=i=>C?'<span class="title moreTitle">'+i.title+"</span>":'<span class="title">'+i.title+'</span><span class="introduce" >'+i.introduce+"</span>";switch(s.styleType){case"xtwlb":n.css({width:"100%",left:0}),c='<ul class="xtwlb">',d=i=>'<li><a href="'+y(i)+'"><img src="'+(i.thumb||i.avatar)+'"/><span class="title">'+i.title+"</span>"+x(i)+E(i)+"</a></li>";break;case"twlb":n.css({width:"100%",left:0}),c='<ul class="twlb">',d=i=>'<li><a href="'+y(i)+'"><span class="rser">'+P(i)+x(i)+E(i)+'</span><img src="'+(i.thumb||i.avatar)+'"/></a></li>';break;case"btrqlb":n.css({width:"100%",left:0}),c='<ul class="btrqlb">',d=i=>'<li><a href="'+y(i)+'">'+P(i)+x(i)+E(i)+"</a></li>";break}for(var b=document.createElement("div"),T=0,B=s.list.length;T<B;T++){var p=s.list[T];p.computerPicPath=p.computerPicPath?JSON.parse(p.computerPicPath):"/design/designstatic/common/image/information/defult.jpg",p.computerSinglePicPath=""+(p.computerPicPath?p.computerPicPath.sourcePath||p.computerPicPath[0].sourcePath:"/design/designstatic/common/image/information/defult.jpg"),b.innerHTML=p.detail,p.detail=b.innerText,c+=d(p,T)}c+="</ul>";var D=n.find(".boxHtml");D.length>0?D.html(c):n.html('<div class="boxHtml">'+c+"</div>"),n.css({backgroundColor:"#fff"}),n.attr("data-article",JSON.stringify({styleType:s.styleType,classify:s.classify,startNum:s.startNum,endNum:s.endNum,isAll:s.isAll,sNum:s.sNum,sTitle:s.sTitle||0,sDate:s.sDate}))},this.show=s=>{s?(s=JSON.parse(s),this.setState({styleType:s.styleType,classify:s.classify,startNum:s.startNum,endNum:s.endNum,sTeacher:s.sTeacher,sNum:s.sNum,sTitle:s.sTitle||0,sDate:s.sDate,isAll:s.isAll=="1"})):this.setState({styleType:"xtwlb",classify:[],startNum:1,endNum:10,sTeacher:0,sNum:0,sTitle:0,sDate:0,isAll:!1})},this.renderStyle=()=>{var s=this.state.styleType,l=(h,t)=>{this.setState({styleType:t})},o=[{type:"xtwlb",image:""},{type:"twlb",image:""},{type:"btrqlb",image:""}];return(0,r.jsxs)("div",{children:[(0,r.jsx)("div",{className:"label",children:"\u98CE\u683C\uFF1A"}),(0,r.jsx)("div",{className:"content",children:o.map(h=>{var t=h.type==s?"on":"";return(0,r.jsx)("div",{kay:h.type,className:"styleli ".concat(h.type," ").concat(t),onClick:u=>l(u,h.type),style:h.image?{backgroundImage:"url(".concat(h.image,")")}:{},children:" "})})})]})},this.carryCallBack=s=>{var l=this,o=this.props.commondesign,h=o.Article.callback,t=this.state,u=t.visible,g=t.styleType,n=t.classify,v=t.startNum,f=t.endNum,c=t.sTeacher,d=t.sNum,m=t.sTitle,M=t.sDate,C=t.isAll;if(s.length>0){var y={list:s,styleType:g,classify:n,startNum:v,endNum:f,sTeacher:c,sNum:d,sTitle:m,sDate:M,isAll:C?"1":"0"};l.updataDomCallBack(y),h&&h(y),l.cancelEvent()}else Y.ZP.error("\u5F53\u524D\u5206\u7C7B\u4E0B\u6682\u65E0\u8D44\u8BAF\uFF0C\u8BF7\u91CD\u65B0\u9009\u62E9");this.setState({loading:!1})},this.cancelEvent=()=>{var s=this,l=this.props.dispatch;l({type:"commondesign/setState",payload:{Article:{visible:!1,callback:()=>{},callClose:()=>{}}}})}}componentDidMount(){var s=this.props.commondesign,l=s.Article,o=l.visible,h=l.element,t=l.callback,u=l.callClose;this.setState({visible:o}),this.listAllSetting();var g=h.attr("data-article");console.log("modata",g),this.show(g)}renderClassify(){var s=this.state,l=s.classify,o=s.startNum,h=s.endNum,t=s.options,u=s.isAll,g=c=>{this.setState({classify:c})},n=c=>{this.setState({startNum:c||0})},v=c=>{this.setState({endNum:c||o})},f=c=>{this.setState({isAll:c.target.checked})};return(0,r.jsxs)("div",{children:[(0,r.jsx)("div",{className:"label",children:"\u5206\u7C7B\uFF1A"}),(0,r.jsxs)("div",{className:"content",children:[(0,r.jsx)(w.Z,{value:l,fieldNames:{label:"typeName",value:"infoTypeID",children:"children"},changeOnSelect:!0,options:t,onChange:g,placeholder:"\u8BF7\u9009\u62E9\u5206\u7C7B",style:{width:440}}),(0,r.jsx)(N.Z,{onChange:f,checked:u,children:"\u663E\u793A\u5F53\u524D\u5206\u7C7B+\u6240\u5C5E\u5206\u7C7B\u5185\u5BB9"})]}),(0,r.jsx)("div",{className:"label",children:"\u8BFB\u53D6\uFF1A"}),(0,r.jsxs)("div",{className:"content",style:{marginBottom:50},children:[(0,r.jsx)(S.Z,{value:o,onChange:n,min:1,style:{width:210}}),(0,r.jsx)("span",{style:{width:20,display:"inline-block",textAlign:"center"},children:"~"}),(0,r.jsx)(S.Z,{value:h,onChange:v,min:o,max:o+99,style:{width:210}})]}),(0,r.jsx)("div",{className:"label",children:"\u8BBE\u7F6E\uFF1A"}),this.renderSetting()]})}renderSetting(){var s=this.props.differenceType,l=this.state,o=l.sDate,h=l.sNum,t=l.sTitle,u=l.sTeacher,g=l.styleType,n=m=>parseInt(m),v=m=>{this.setState({sDate:m.target.checked?1:0})},f=m=>{this.setState({sTeacher:m.target.checked?1:0})},c=m=>{this.setState({sNum:m.target.checked?1:0})},d=m=>{this.setState({sTitle:m.target.checked?1:0})};return(0,r.jsx)("div",{className:"content",style:{marginBottom:30,lineHeight:"34px"},children:(0,r.jsxs)(W.Z,{children:[(0,r.jsxs)(O.Z,{children:[" ",(0,r.jsx)(N.Z,{onChange:v,checked:n(o)==1,style:{marginRight:52},children:"\u5C55\u793A\u53D1\u5E03\u65E5\u671F "})," "]}),s==1&&(0,r.jsxs)(O.Z,{children:[" ",(0,r.jsx)(N.Z,{onChange:f,checked:n(u)==1,style:{marginRight:52},children:"\u5C55\u793A\u53D1\u5E03\u8005 "})," "]}),(0,r.jsxs)(O.Z,{children:[" ",(0,r.jsx)(N.Z,{onChange:c,checked:n(h)==1,style:{marginRight:52},children:"\u5C55\u793A\u9605\u8BFB\u4EBA\u6570 "})," "]}),g!="xtwlb"&&(0,r.jsxs)(O.Z,{children:[" ",(0,r.jsx)(N.Z,{onChange:d,checked:n(t)==1,style:{marginRight:52},children:"\u53EA\u5C55\u793A\u6807\u9898 "})," "]})]})})}render(){var s=this.state,l=s.visible,o=s.styleType,h=s.classify,t=s.startNum,u=s.endNum,g=s.isAll,n=s.loading,v=this.props.dispatch,f=d=>{this.cancelEvent()},c=d=>{this.setState({loading:!0}),v({type:"page/firstHomepageDetail",payload:{isAll:g?"1":"0",InfoTypeId:h[h.length-1],beginNum:t,endNum:u},callback:m=>{m.success&&this.carryCallBack(m.data)}})};return(0,r.jsx)(K.Z,{title:"\u8D44\u8BAF",visible:l,onOk:n?()=>{}:c,width:600,confirmLoading:n,onCancel:f,maskClosable:!1,className:$().hrefModal,children:(0,r.jsxs)("div",{className:"divRow",children:[this.renderStyle(),this.renderClassify()]})})}})||L)}}]);
