(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[12043],{31223:function(C,B,a){"use strict";a.d(B,{Z:function(){return K}});var I=a(28991),p=a(67294),U={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M678.3 642.4c24.2-13 51.9-20.4 81.4-20.4h.1c3 0 4.4-3.6 2.2-5.6a371.67 371.67 0 00-103.7-65.8c-.4-.2-.8-.3-1.2-.5C719.2 505 759.6 431.7 759.6 349c0-137-110.8-248-247.5-248S264.7 212 264.7 349c0 82.7 40.4 156 102.6 201.1-.4.2-.8.3-1.2.5-44.7 18.9-84.8 46-119.3 80.6a373.42 373.42 0 00-80.4 119.5A373.6 373.6 0 00137 888.8a8 8 0 008 8.2h59.9c4.3 0 7.9-3.5 8-7.8 2-77.2 32.9-149.5 87.6-204.3C357 628.2 432.2 597 512.2 597c56.7 0 111.1 15.7 158 45.1a8.1 8.1 0 008.1.3zM512.2 521c-45.8 0-88.9-17.9-121.4-50.4A171.2 171.2 0 01340.5 349c0-45.9 17.9-89.1 50.3-121.6S466.3 177 512.2 177s88.9 17.9 121.4 50.4A171.2 171.2 0 01683.9 349c0 45.9-17.9 89.1-50.3 121.6C601.1 503.1 558 521 512.2 521zM880 759h-84v-84c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v84h-84c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h84v84c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8v-84h84c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8z"}}]},name:"user-add",theme:"outlined"},A=U,R=a(27029),P=function(W,L){return p.createElement(R.Z,(0,I.Z)((0,I.Z)({},W),{},{ref:L,icon:A}))};P.displayName="UserAddOutlined";var K=p.forwardRef(P)},59584:function(C){C.exports={sharetoModalBox:"sharetoModalBox___3psJO",footBox:"footBox___-EyeN",tableBox:"tableBox___1liq0"}},12043:function(C,B,a){"use strict";a.r(B),a.d(B,{default:function(){return os}});var I=a(14965),p=a(68588),U=a(63185),A=a(9676),R=a(13254),P=a(14277),K=a(34792),f=a(48086),W=a(57663),L=a(71577),cs=a(47673),G=a(4107),H=a(34895),J=a(28216),Q=a(30381),F=a.n(Q),X=a(31223),Y=a(89366),k=a(50206),w=a(79508),b=a(5820),q=a(51964),S=a.n(q),Z=a(67294),ss=a(20640),_s=a.n(ss),Es=a(84059),as=a(10285),hs=a.n(as),N=a(30672),ts=a(59584),T=a.n(ts),vs=a(37406),es=a(96486),Ds=a.n(es),s=a(85893),$,z,ns=G.Z.Search,os=($=(0,J.$j)(V=>{var e=V.common;return{common:e}}),$(z=class extends Z.PureComponent{constructor(){super(...arguments);this.formRef=Z.createRef(),this.state={modelZindex:100,openOption:!1,selectData:{},dataList:[],dataListId:[],showTime:!1,timeValue:Date.now(),keywords:""},this.getshareData=()=>{var e=this,o=this.props,n=o.dispatch,D=o.common,u=D.checkFileParams,l=u.sourceID;n({type:"common/getshareData",payload:{isShareTo:1,sourceID:l},callback:r=>{if(r.success){var E=r.data,h=E.timeTo,c=E.authTo,v=c===void 0?[]:c;e.setState({dataList:v,showTime:!!h,timeValue:h||Date.now()})}}})},this.userShareGroupList=()=>{var e=this,o=this.props.dispatch;o({type:"common/userShareGroupList",callback:n=>{n.success&&e.setState({selectData:n.data})}})}}componentDidMount(){var e=this;e.userShareGroupList(),e.getshareData()}renderHead(){var e=this,o=this.state,n=o.dataList,D=o.openOption,u=o.selectData,l=o.dataListId,r=o.keywords,E=u.groupList,h=E===void 0?[]:E,c=u.userList,v=c===void 0?[]:c,i=[...h,...v],m=t=>{e.setState({openOption:t})},g=t=>{var d=t.groupID||t.userID||t.targetID,M=l.indexOf(d);M<0?(l.push(d),n.push(t)):(l.splice(M,1),n.splice(M,1)),e.setState({dataList:[...n],dataListId:[...l]})},j=t=>{var d=t.target.value;e.setState({keywords:d})},O=t=>{var d=t.target.value;e.setState({keywords:d})},y=t=>{var d=t.userID||t.groupID;return l.indexOf(d)>-1?"liBox on":"liBox"};return i=i.filter(t=>{var d=t.nickname||t.name||"";return d.indexOf(r)>-1}),(0,s.jsxs)("div",{className:"headBox",children:["\u9009\u62E9\u534F\u4F5C\u5206\u4EAB\u7684\u90E8\u95E8\u6216\u7528\u6237",(0,s.jsxs)("div",{className:"rightBox",children:[(0,s.jsxs)(L.Z,{onClick:()=>m(!0),children:[(0,s.jsx)(X.Z,{}),"\u6DFB\u52A0\u7528\u6237\u6216\u90E8\u95E8"]}),D&&(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(b.zd,{opacity:0,style:{zIndex:100},onMaskClick:()=>m(!1)}),(0,s.jsxs)("div",{className:"opationBox",children:[(0,s.jsx)("div",{className:"searchBox",children:(0,s.jsx)(ns,{size:"small",placeholder:"\u641C\u7D22\u7528\u6237\u6216\u90E8\u95E8",onSearch:O,onChange:j,style:{width:280}})}),(0,s.jsx)("div",{className:"userList",children:(0,s.jsx)(S(),{children:i.map((t,d)=>(0,s.jsxs)("div",{className:y(t),onClick:()=>g(t),children:[(0,s.jsx)("span",{className:"iconBox",children:t.userID?(0,s.jsx)(Y.Z,{style:{color:"#9a64ff"}}):(0,s.jsx)(k.Z,{style:{color:"#ffd256"}})}),(0,s.jsx)("span",{children:t.nickname||t.name}),(0,s.jsx)("span",{className:"right",children:(0,s.jsx)(w.Z,{})})]},d))})})]})]})]})]})}renderTableList(){var e=this,o=this.props,n=o.dispatch,D=o.common,u=D.copyDataList,l=this.state.dataList,r=l===void 0?[]:l,E=()=>{e.setState({dataList:[]})},h=()=>{n({type:"common/setState",payload:{copyDataList:r}}),f.ZP.success("\u5DF2\u590D\u5236\u5230\u7C98\u8D34\u677F")},c=()=>{e.state({dataList:u})},v=r.length<1;return(0,s.jsxs)("div",{className:T().tableBox,children:[(0,s.jsxs)("div",{className:"tableHeader",children:[(0,s.jsx)("li",{children:"\u9009\u62E9\u5BF9\u8C61"}),(0,s.jsx)("li",{children:"\u4E0A\u7EA7\u90E8\u95E8"}),(0,s.jsx)("li",{children:"\u6587\u6863\u6743\u9650"})]}),(0,s.jsxs)("div",{className:"tableContnet",children:[(0,s.jsxs)(S(),{children:[r.map((i,m)=>(0,s.jsxs)("div",{className:"td",children:[(0,s.jsx)("li",{children:i.nickname||i.name}),(0,s.jsx)("li",{children:i.parentName||""}),(0,s.jsx)("li",{children:"\u534F\u4F5C\u8005"})]},m)),v&&(0,s.jsx)(P.Z,{style:{marginTop:140},image:P.Z.PRESENTED_IMAGE_SIMPLE})]}),(0,s.jsxs)("div",{className:"footerBottom",children:[!v&&(0,s.jsx)("span",{onClick:E,children:"\u6E05\u7A7A"}),!v&&(0,s.jsx)("span",{onCilck:h,children:"\u590D\u5236\u8BE5\u7EC4\u5408"}),u.length>1&&(0,s.jsx)("span",{onClick:c,children:"\u7C98\u8D34"})]})]})]})}render(){var e=this,o=(0,N.lw)((0,N.Kd)()),n=e.props,D=n.dispatch,u=n.common,l=n.open,r=n.setOpenModel,E=n.issmall,h=n.showIndex,c=u.showmodalList,v=c===void 0?[]:c,i=u.checkFileParams,m=i.sourceID,g=i.name,j=i.isShareTo,O=this.state,y=O.modelZindex,t=O.showTime,d=O.dataList,M=O.timeValue,ds=_=>{e.setState({modelZindex:_})},ls=_=>{e.setState({showTime:_.target.checked})},rs=_=>{e.setState({timeValue:_})},is=_=>{e.setState({timeValue:_})},us=()=>{var _=[];if(d.length<1){f.ZP.warning("\u8BF7\u6DFB\u52A0\u534F\u4F5C\u4EBA\u4E0E\u90E8\u95E8");return}d.map(x=>{_.push({authID:1,targetID:x.userID||x.groupID||x.targetID,targetType:x.userID?1:2})}),D({type:"common/shareSave",payload:{authTo:_,isShareTo:1,sourceID:m,timeTo:t?F()(M).valueOf():0,title:g},callback:x=>{x.success&&r(!1,"visible")}})};return(0,s.jsx)(s.Fragment,{children:(0,s.jsxs)(H.Z,{open:l,issmall:E,showIndex:h,modelZindex:y,setOpenModel:r,setModelZindex:ds,type:"shareto",title:o.formatMessage({id:"log.file.shareTo"}),children:[(0,s.jsxs)("div",{className:T().sharetoModalBox,children:[e.renderHead(),e.renderTableList(),(0,s.jsx)("div",{className:"desc",children:"\u5206\u4EAB\u7ED9\u5BF9\u65B9\u6216\u5BF9\u65B9\u6240\u5728\u90E8\u95E8\u540E\uFF0C\u7528\u6237\u53EF\u4EE5\u5728[\u5206\u4EAB\u7ED9\u6211\u7684]\u4E2D\u770B\u5230\u3002"}),(0,s.jsxs)("div",{className:"timeBox",children:[(0,s.jsx)(A.Z,{onChange:ls,children:"\u5230\u671F\u65F6\u95F4"}),t&&(0,s.jsx)(p.Z,{value:F()(M),showTime:!0,onChange:rs,onOk:is}),(0,s.jsx)("span",{className:"desc",style:{marginLeft:5},children:"\u5F00\u542F\u8BBE\u7F6E\u540E\uFF0C\u8D85\u8FC7\u65F6\u95F4\u5206\u4EAB\u81EA\u52A8\u5931\u6548"})]})]}),(0,s.jsxs)("div",{className:T().footBox,children:[j&&(0,s.jsx)(L.Z,{children:"\u53D6\u6D88\u534F\u4F5C\u5206\u4EAB"}),(0,s.jsx)(L.Z,{type:"primary",style:{marginLeft:5},onClick:us,children:"\u4FDD\u5B58"})]})]})})}})||z)}}]);