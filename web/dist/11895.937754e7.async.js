(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[11895],{88528:function(y,f,e){"use strict";e.d(f,{Z:function(){return K}});var B=e(28991),u=e(67294),U={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M484 443.1V528h-84.5c-4.1 0-7.5 3.1-7.5 7v42c0 3.8 3.4 7 7.5 7H484v84.9c0 3.9 3.2 7.1 7 7.1h42c3.9 0 7-3.2 7-7.1V584h84.5c4.1 0 7.5-3.2 7.5-7v-42c0-3.9-3.4-7-7.5-7H540v-84.9c0-3.9-3.1-7.1-7-7.1h-42c-3.8 0-7 3.2-7 7.1zm396-144.7H521L403.7 186.2a8.15 8.15 0 00-5.5-2.2H144c-17.7 0-32 14.3-32 32v592c0 17.7 14.3 32 32 32h736c17.7 0 32-14.3 32-32V330.4c0-17.7-14.3-32-32-32zM840 768H184V256h188.5l119.6 114.4H840V768z"}}]},name:"folder-add",theme:"outlined"},F=U,L=e(27029),_=function(w,i){return u.createElement(L.Z,(0,B.Z)((0,B.Z)({},w),{},{ref:i,icon:F}))};_.displayName="FolderAddOutlined";var K=u.forwardRef(_)},87596:function(y){y.exports={shareModalBox:"shareModalBox___P-guE",qrcodeModal:"qrcodeModal___2JgeB"}},11895:function(y,f,e){"use strict";e.r(f),e.d(f,{default:function(){return oe}});var B=e(88983),u=e(47933),U=e(48736),F=e(27049),L=e(57663),_=e(71577),K=e(9715),n=e(55843),w=e(47673),i=e(4107),De=e(17462),Y=e(76772),C=e(11849),k=e(34895),q=e(28216),ee=e(19675),te=e(88528),ae=e(51964),se=e.n(ae),V=e(67294),b=e(30672),ne=e(87596),le=e.n(ne),S=e(20342),fe=e(37406),t=e(85893),$,z,oe=($=(0,q.$j)(H=>{var s=H.common;return{common:s}}),$(z=class extends V.PureComponent{constructor(){super(...arguments);this.formRef=V.createRef(),this.state={modelZindex:100,showOptions:!1,changeValue:{},initialValues:{},shareID:0,windowType:"_blank"}}componentDidMount(){var s=this,p=s.props,r=p.common,O=p.dispatch,o=r.updateWebsiteData,M=o===void 0?{}:o,E=M.name,T=M.oexeContent;if(T){var c=JSON.parse(T),I=c.value,j=c.target,Z=c.width,v=c.height,R=c.imgIco,m={name:E,linkUrl:I,openType:j,width:Z,height:v,imgIco:R||""};s.formRef.current.setFieldsValue(m),s.setState({windowType:j,showOptions:!0})}}render(){var s=this,p=(0,b.lw)((0,b.Kd)()),r=s.props,O=r.dispatch,o=r.common,M=r.open,E=r.setOpenModel,T=r.videoOption,c=o.sourceID,I=o.shareData,j=I===void 0?{}:I,Z=o.viewType,v=o.addressPath,R=o.selectAddressIndex,m=o.updateWebsiteData,ie=Z==3?v[R]:v[v.length-1],d=this.state,re=d.modelZindex,P=d.showOptions,Oe=d.time,N=d.changeValue,de=N===void 0?{}:N,J=d.initialValues,ue=J===void 0?{}:J,Me=d.shareID,G=d.windowType,_e=a=>{s.setState({modelZindex:a})},ce=a=>{var l=a.name,h=a.linkUrl,g=a.openType,x=a.width,W=a.height,ve=a.imgIco,Q=JSON.stringify({type:"url",value:h,target:g,width:x,height:W,imgIco:ve});s.ajaxTimeout||(s.ajaxTimeout=1,m.name?O({type:"common/fileSave",payload:{sourceID:m.sourceID,content:Q},callback:D=>{if(D.success){var A=D.data;window.zipSourceId=A.sourceID?[A.sourceID]:[],(0,S.gp)(s),E(!1),s.ajaxTimeout=0}}}):O({type:"common/operation",payload:{name:l+".oexe",operation:"mkfile",sourceID:ie.sourceID,content:Q},callback:D=>{if(D.success){var A=D.data,X=A.source||{};window.zipSourceId=X.sourceID?[X.sourceID]:[],(0,S.gp)(s),E(!1),s.ajaxTimeout=0}}}))},me=a=>{},he=a=>{var l={};a.options&&a.options.indexOf("3")>-1&&(l={options:a.options.filter(h=>h!="2"),downNum:""},this.formRef.current.setFieldsValue(l)),s.setState({changeValue:(0,C.Z)((0,C.Z)((0,C.Z)({},de),a),l)})},pe=a=>{s.setState({windowType:a.target.value})},Ee=()=>{var a=this.formRef.current.getFieldsValue(),l=a.linkUrl;!l||(G=="_blank"?window.open(l):O({type:"common/setState",payload:{websitePreviewVisible:!0,previewParams:a}}))};return(0,t.jsx)(t.Fragment,{children:(0,t.jsx)(k.Z,{open:M,modelZindex:re,setOpenModel:E,setModelZindex:_e,type:"website",title:p.formatMessage({id:m.name?"log.file.updateWebsite":"log.file.newWebsite"}),children:(0,t.jsx)("div",{className:le().shareModalBox,children:(0,t.jsxs)(se(),{children:[(0,t.jsx)(Y.Z,{style:{textAlign:"center",marginBottom:20},message:"\u53EF\u4EE5\u5C06url\u94FE\u63A5\u62D6\u5165\u5230\u6587\u4EF6\u533A\u57DF,\u81EA\u52A8\u521B\u5EFA\u7F51\u5740\u94FE\u63A5!",type:"success"}),(0,t.jsxs)(n.Z,{ref:this.formRef,name:"basic",labelCol:{span:6},wrapperCol:{span:16},initialValues:ue,onFinish:ce,onFinishFailed:me,onValuesChange:he,autoComplete:"off",height:520,children:[(0,t.jsx)(n.Z.Item,{label:"\u540D\u79F0",name:"name",style:{display:m.name?"none":""},rules:[{required:!0,message:"\u8BF7\u8F93\u5165\u5E94\u7528\u540D"}],children:(0,t.jsx)(i.Z,{placeholder:"\u8BF7\u8F93\u5165\u5E94\u7528\u540D"})}),(0,t.jsx)(n.Z.Item,{label:"\u7F51\u5740\u94FE\u63A5",name:"linkUrl",rules:[{required:!0,message:"\u8BF7\u8F93\u5165http/https\u94FE\u63A5"},{type:"url",message:"\u8BF7\u8F93\u5165\u6B63\u786E\u7684\u94FE\u63A5\u5730\u5740"}],children:(0,t.jsx)(i.Z,{placeholder:"\u8BF7\u8F93\u5165http/https\u94FE\u63A5"})}),(0,t.jsx)(F.Z,{children:(0,t.jsxs)(_.Z,{onClick:()=>{s.setState({showOptions:!P})},children:["\u9AD8\u7EA7\u8BBE\u7F6E",(0,t.jsx)(ee.Z,{})]})}),(0,t.jsx)(n.Z.Item,{style:{display:P?"":"none"},label:"\u6253\u5F00\u65B9\u5F0F",name:"openType",children:(0,t.jsxs)(u.ZP.Group,{defaultValue:"_blank",buttonStyle:"solid",onChange:pe,children:[(0,t.jsx)(u.ZP.Button,{value:"_blank",children:"\u65B0\u7A97\u53E3"}),(0,t.jsx)(u.ZP.Button,{value:"_self",children:"\u5BF9\u8BDD\u6846"})]})}),(0,t.jsx)(n.Z.Item,{style:{display:P?"":"none"},label:"\u56FE\u7247\u56FE\u6807",name:"imgIco",rules:[{required:!1,message:"\u8BF7\u8F93\u5165http/https\u94FE\u63A5"}],children:(0,t.jsx)(i.Z,{placeholder:"\u9009\u62E9\u56FE\u7247\u6216\u7C98\u8D34\u7F51\u7EDC\u56FE\u7247url",addonAfter:(0,t.jsx)(te.Z,{style:{cursor:"pointer"},onClick:()=>{window.fileBoxApi&&window.fileBoxApi.fileSelect({title:p.formatMessage({id:"explorer.selectFile"}),type:"file",single:!0,allowExt:"jpg,jpeg,png,bmp",callback:function(l){var h=l.data||{},g=h.selectIds.length?h.selectItems||[]:[];if(g.length){var x=g[0]||{},W={imgIco:x.path||x.downloadUrl};s.formRef.current.setFieldsValue(W)}}})}})})}),(0,t.jsxs)(n.Z.Item,{style:{display:P&&G=="_self"?"":"none"},label:"\u5BF9\u8BDD\u6846\u5C3A\u5BF8",name:"size",children:[(0,t.jsx)(n.Z.Item,{name:"width",initialValue:"80%",style:{display:"inline-block",width:"calc(50% - 8px)"},children:(0,t.jsx)(i.Z,{placeholder:"\u5BBD\u5EA6"})}),(0,t.jsx)(n.Z.Item,{name:"height",initialValue:"70%",rules:[{required:!0}],style:{display:"inline-block",width:"calc(50% - 8px)",margin:"0 8px"},children:(0,t.jsx)(i.Z,{placeholder:"\u9AD8\u5EA6"})})]}),(0,t.jsxs)(n.Z.Item,{wrapperCol:{span:24},className:"formFooter",children:[(0,t.jsx)(_.Z,{onClick:Ee,style:{marginRight:12},children:"\u9884\u89C8"}),(0,t.jsx)(_.Z,{type:"primary",htmlType:"submit",children:"\u4FDD\u5B58"})]})]})]})})})})}})||z)}}]);