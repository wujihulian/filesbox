(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[19016],{14660:function(S){S.exports={coffermodalbox:"coffermodalbox___3HxpU"}},19016:function(S,A,t){"use strict";t.r(A),t.d(A,{default:function(){return fs}});var Es=t(71194),ts=t(50146),Os=t(49111),_=t(19650),Ds=t(47673),w=t(4107),js=t(48736),P=t(27049),Zs=t(57663),p=t(71577),Cs=t(34792),n=t(48086),v=t(11849),rs=t(34895),ds=t(28216),is=t(67724),ns=t(57119),ls=t(67294),x=t(30672),y=t(37406),ps=t(14660),cs=t.n(ps),ms=t(34346),ws=t.n(ms),s=t(85893),B,L,fs=(B=(0,ds.$j)(E=>{var r=E.common,o=E.loading;return{common:r,loading:o.effects["common/checkSafePwd"]||o.effects["common/safeSendCode"]}}),B(L=class extends ls.PureComponent{constructor(){super(...arguments);this.state={modelZindex:100,cofferType:1,passwordone:"",key:"asfasfafasf",passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""}}componentDidMount(){var r=this,o=r.props,c=o.dispatch,f=o.common,O=o.open,D=o.setOpenModel,z=o.videoOption,j=o.showIndex,Z=o.issmall,C=o.initialState,h=f.sourceID,l=f.cofferData,T=l===void 0?{}:l,d=T.pathSafe,u=d===void 0?"":d;r.setState({cofferType:u=="isNotSetPwd"?5:(u=="isNotLogin",1),passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""})}componentWillUnmount(){var r=this,o=r.props,c=o.dispatch,f=o.common,O=o.open,D=o.setOpenModel,z=o.videoOption,j=o.showIndex,Z=o.issmall,C=f.sourceID}render(){var r=this,o=r.props,c=o.dispatch,f=o.common,O=o.open,D=o.setOpenModel,z=o.videoOption,j=o.showIndex,Z=o.issmall,C=o.initialState,h=o.loading,l=this.state,T=l.modelZindex,d=l.cofferType,u=l.passwordone,U=u===void 0?"":u,hs=l.key,N=l.passwordtwo,K=N===void 0?"":N,W=l.passwordytwo,R=W===void 0?"":W,$=l.passwordyztwo,b=$===void 0?"":$,k=l.passwordthree,H=k===void 0?"":k,F=l.passwordythree,V=F===void 0?"":F,J=l.passwordyzthree,Q=J===void 0?"":J,X=l.passwordset,Y=X===void 0?"":X,G=l.passwordyset,q=G===void 0?"":G,M=f.addressPath,ss=f.getParams,es=f.viewType,as=f.selectAddressIndex,gs=es==3?M[as]||{}:M[M.length-1]||{},ys=C.currentUser,I=ys.user,e=(0,x.lw)((0,x.Kd)()),us=a=>{r.setState({modelZindex:a})},_s=(a,i)=>{if(es==3){var m=M.length-1;if(as!=m){c({payload:(0,v.Z)((0,v.Z)({},a||ss),{},{sourceID:gs.sourceID},a?{}:{takeNotesType:"none"}),type:"common/refreshListPath",callback:g=>{i&&i()}});return}}c({payload:(0,v.Z)((0,v.Z)({},a||ss),a?{}:{takeNotesType:"none"}),type:"common/getListPath",callback:g=>{i&&i()}})},vs=()=>{var a=(U||"").trim();if(a)c({type:"common/checkSafePwd",payload:{safePwd:(0,y.HI)(a)},callback:i=>{i.success?(n.ZP.success(e.formatMessage({id:"explorer.success"})),c({type:"common/setState",payload:{cofferData:{},cofferVisible:!1}}),_s()):n.ZP.error(i.message)}});else{n.ZP.warning({content:e.formatMessage({id:"user.inputPwd"}),key:hs,duration:2});return}},Ms=()=>{var a=(Y||"").trim(),i=(q||"").trim();if(a&&i&&a==i)c({type:"common/setSafePwd",payload:{safePwd:(0,y.HI)(a)},callback:m=>{m.success?(n.ZP.success(e.formatMessage({id:"explorer.success"})),r.setState({cofferType:1,passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""})):n.ZP.error(m.message)}});else{n.ZP.warning(e.formatMessage({id:"user.rootPwdEqual"}));return}},Ps=()=>{var a=(K||"").trim(),i=(R||"").trim(),m=(b||"").trim();if(!a){n.ZP.warning(e.formatMessage({id:"user.inputOldPwd"}));return}if(i&&m&&i==m)c({type:"common/setSafePwd",payload:{oldSafePwd:(0,y.HI)(a),safePwd:(0,y.HI)(i)},callback:g=>{g.success?(n.ZP.success(e.formatMessage({id:"explorer.success"})),r.setState({cofferType:1,passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""})):n.ZP.error(g.message)}});else{n.ZP.warning(e.formatMessage({id:"user.rootPwdEqual"}));return}},os=()=>{c({type:"common/safeSendCode",payload:{value:I.email},callback:a=>{a.success?(n.ZP.success("".concat(e.formatMessage({id:"user.code"})).concat(e.formatMessage({id:"user.sendSuccess"}),",").concat(e.formatMessage({id:"admin.setting.emailGoToTips"})).concat(I.email).concat(e.formatMessage({id:"common.view"}))),r.setState({cofferType:4,passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""})):n.ZP.error(a.message)}})},xs=()=>{var a=(H||"").trim(),i=(V||"").trim(),m=(Q||"").trim();if(!a){n.ZP.warning(e.formatMessage({id:"user.inputEmailCode"}));return}if(i&&m&&i==m)c({type:"common/setSafePwd",payload:{code:a,safePwd:(0,y.HI)(i)},callback:g=>{g.success?(n.ZP.success(e.formatMessage({id:"explorer.success"})),r.setState({cofferType:1,passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""})):n.ZP.error(g.message)}});else{n.ZP.warning(e.formatMessage({id:"user.rootPwdEqual"}));return}};return(0,s.jsx)(s.Fragment,{children:(0,s.jsx)(rs.Z,{open:O,modelZindex:T,setOpenModel:D,setModelZindex:us,type:"coffer",issmall:Z,resolution:"",showIndex:j,title:"",mask:!0,children:(0,s.jsx)("div",{className:cs().coffermodalbox,children:(0,s.jsxs)("div",{className:"cofferbox",children:[d==2||d==3||d==4?(0,s.jsx)(p.Z,{className:"gobackbtn",onClick:()=>{r.setState({cofferType:1,passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""})},shape:"circle",icon:(0,s.jsx)(is.Z,{})}):"",(0,s.jsx)("div",{className:"cover",children:(0,s.jsx)("img",{src:ws()})}),(0,s.jsx)("div",{className:"cofferType",children:d==1?e.formatMessage({id:"common.Privatesafe",defaultMessage:"\u79C1\u5BC6\u4FDD\u9669\u7BB1"}):d==2?e.formatMessage({id:"admin.pwdEdit",defaultMessage:"\u4FEE\u6539\u5BC6\u7801"}):d==3?e.formatMessage({id:"user.findPwd",defaultMessage:"\u627E\u56DE\u5BC6\u7801"}):d==4?e.formatMessage({id:"user.findPwd",defaultMessage:"\u627E\u56DE\u5BC6\u7801"}):d==5?"".concat(e.formatMessage({id:"admin.member.enable",defaultMessage:"\u542F\u7528"})).concat(e.formatMessage({id:"common.Privatesafe",defaultMessage:"\u79C1\u5BC6\u4FDD\u9669\u7BB1"})):"".concat(e.formatMessage({id:"admin.member.enable",defaultMessage:"\u542F\u7528"})).concat(e.formatMessage({id:"common.Privatesafe",defaultMessage:"\u79C1\u5BC6\u4FDD\u9669\u7BB1"}))}),d==5||d==6?(0,s.jsx)("div",{className:"coffertips",children:e.formatMessage({id:"common.Privatesafetips",defaultMessage:"\u79C1\u5BC6\u4FDD\u9669\u7BB1,\u7ED9\u60A8\u7684\u91CD\u8981\u6570\u636E\u8FDB\u884C\u53CC\u91CD\u4FDD\u62A4"})}):"",(0,s.jsx)(P.Z,{}),d==1?(0,s.jsxs)("div",{className:"cofferPassword",children:[(0,s.jsxs)(_.Z,{style:{width:"100%"},direction:"vertical",children:[(0,s.jsx)("input",{type:"password",style:{opacity:0,position:"absolute",width:0,height:0}}),(0,s.jsx)(w.Z.Password,{value:U,onChange:a=>{r.setState({passwordone:a.target.value})},placeholder:e.formatMessage({id:"user.inputPwd"})})]}),(0,s.jsxs)("div",{className:"editbtns",children:[(0,s.jsx)(p.Z,{onClick:()=>{r.setState({cofferType:2,passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""})},className:"editbtn",type:"link",children:e.formatMessage({id:"admin.pwdEdit",defaultMessage:"\u4FEE\u6539\u5BC6\u7801"})}),(0,s.jsx)(p.Z,{onClick:()=>{r.setState({cofferType:3,passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""})},className:"forgetbtn",type:"link",children:e.formatMessage({id:"user.forgetPwd",defaultMessage:"\u5FD8\u8BB0\u5BC6\u7801"})}),(0,s.jsx)("div",{style:{clear:"both"}})]}),(0,s.jsx)("div",{className:"okBox",children:(0,s.jsx)(p.Z,{loading:h,onClick:()=>{vs()},type:"primary",shape:"round",children:e.formatMessage({id:"user.isLoginEnter",defaultMessage:"\u7ACB\u5373\u8FDB\u5165"})})})]}):d==2?(0,s.jsxs)("div",{className:"cofferEdit",children:[(0,s.jsxs)(_.Z,{style:{width:"100%"},direction:"vertical",children:[(0,s.jsx)("input",{type:"password",style:{opacity:0,position:"absolute",width:0,height:0}}),(0,s.jsx)(w.Z.Password,{value:K,onChange:a=>{r.setState({passwordtwo:a.target.value})},placeholder:e.formatMessage({id:"admin.setting.pwdOld"})}),(0,s.jsx)(P.Z,{}),(0,s.jsx)(w.Z.Password,{value:R,onChange:a=>{r.setState({passwordytwo:a.target.value})},placeholder:e.formatMessage({id:"user.inputPwdAgain"})}),(0,s.jsx)(w.Z.Password,{value:b,onChange:a=>{r.setState({passwordyztwo:a.target.value})},placeholder:e.formatMessage({id:"user.inputNewPwd"})})]}),(0,s.jsx)("div",{className:"okBox",children:(0,s.jsx)(p.Z,{loading:h,onClick:()=>{Ps()},type:"primary",shape:"round",children:e.formatMessage({id:"common.ok",defaultMessage:"\u786E\u5B9A"})})})]}):d==3?(0,s.jsx)("div",{className:"cofferforgot",children:(0,s.jsx)("div",{className:"okBox",children:(0,s.jsx)(p.Z,{loading:h,onClick:()=>{os()},type:"primary",shape:"round",children:e.formatMessage({id:"user.getCode",defaultMessage:"\u83B7\u53D6\u9A8C\u8BC1\u7801"})})})}):d==4?(0,s.jsxs)("div",{className:"cofferemailEdit",children:[(0,s.jsxs)(_.Z,{style:{width:"100%"},direction:"vertical",children:[(0,s.jsx)("input",{type:"password",style:{opacity:0,position:"absolute",width:0,height:0}}),(0,s.jsx)(w.Z,{value:H,addonAfter:(0,s.jsx)(p.Z,{loading:h,onClick:()=>{os()},size:"small",children:e.formatMessage({id:"user.getCode"})}),onChange:a=>{r.setState({passwordthree:a.target.value})},placeholder:e.formatMessage({id:"user.inputEmailCode"})}),(0,s.jsx)(P.Z,{}),(0,s.jsx)(w.Z.Password,{value:V,onChange:a=>{r.setState({passwordythree:a.target.value})},placeholder:e.formatMessage({id:"user.inputNewPwd"})}),(0,s.jsx)(w.Z.Password,{value:Q,onChange:a=>{r.setState({passwordyzthree:a.target.value})},placeholder:e.formatMessage({id:"user.inputPwdAgain"})})]}),(0,s.jsx)("div",{className:"okBox",children:(0,s.jsx)(p.Z,{loading:h,onClick:()=>{xs()},type:"primary",shape:"round",children:e.formatMessage({id:"common.ok",defaultMessage:"\u786E\u5B9A"})})})]}):d==5?(0,s.jsx)("div",{className:"cofferemailOpen",children:(0,s.jsx)("div",{className:"okBox",children:(0,s.jsx)(p.Z,{onClick:()=>{I.email?r.setState({cofferType:6,passwordtwo:"",passwordytwo:"",passwordyztwo:"",passwordthree:"",passwordythree:"",passwordyzthree:"",passwordset:"",passwordyset:""}):ts.Z.confirm({title:e.formatMessage({id:"common.tipsDesc",defaultMessage:"\u63D0\u793A\u8BF4\u660E"}),icon:(0,s.jsx)(ns.Z,{}),content:e.formatMessage({id:"common.Privatesafenoemail",defaultMessage:"\u672A\u7ED1\u5B9A\u90AE\u7BB1,\u8BF7\u5148\u5728\u4E2A\u4EBA\u4E2D\u5FC3\u7ED1\u5B9A\u90AE\u7BB1\u540E\u518D\u8BD5(\u7528\u4E8E\u5BC6\u7801\u627E\u56DE)"}),okText:e.formatMessage({id:"user.bind",defaultMessage:"\u7ED1\u5B9A"}),onOk:()=>{x.m8.push("/personal")},cancelText:e.formatMessage({id:"common.cancel",defaultMessage:"\u53D6\u6D88"})})},type:"primary",shape:"round",children:e.formatMessage({id:"admin.member.enable",defaultMessage:"\u542F\u7528"})})})}):d==6?(0,s.jsxs)("div",{className:"cofferemailOpenset",children:[(0,s.jsxs)(_.Z,{style:{width:"100%"},direction:"vertical",children:[(0,s.jsx)("input",{type:"password",style:{opacity:0,position:"absolute",width:0,height:0}}),(0,s.jsx)(w.Z.Password,{value:Y,onChange:a=>{r.setState({passwordset:a.target.value})},placeholder:e.formatMessage({id:"user.inputPwd"})}),(0,s.jsx)(w.Z.Password,{value:q,onChange:a=>{r.setState({passwordyset:a.target.value})},placeholder:e.formatMessage({id:"user.inputPwdAgain"})})]}),(0,s.jsx)("div",{className:"okBox",children:(0,s.jsx)(p.Z,{loading:h,onClick:()=>{Ms()},type:"primary",shape:"round",children:e.formatMessage({id:"admin.member.enable",defaultMessage:"\u542F\u7528"})})})]}):""]})})})})}})||L)}}]);