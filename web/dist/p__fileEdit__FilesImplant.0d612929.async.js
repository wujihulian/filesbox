(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[19365,42557],{2660:function(p){p.exports={audiomodalbox:"audiomodalbox___1e6x2"}},28942:function(p){p.exports={implantBox:"implantBox___3RRdo"}},485:function(p,U,e){"use strict";e.r(U),e.d(U,{default:function(){return $e}});var v=e(57663),u=e(71577),w=e(11849),y=e(28216),h=e(67294),z=e(73935),F=e(14405),j=e(90505),A=e(12541),c=e(93713),L=e(61979),Ke=e(79653),ne=e(20640),te=e.n(ne),Ye=e(9980),Ge=e(40617),ke=e(84651),Qe=e(67754),We=e(97337);const se=(s,n)=>{const{editorId:o}=s;(0,h.useImperativeHandle)(n,()=>({rerender(){A.b.emit(o,A.R)}}),[])},re=(0,h.forwardRef)((s,n)=>{const{modelValue:o=c.e.modelValue,theme:r=c.e.theme,className:l=c.e.className,editorId:t=c.e.editorId,showCodeRowNumber:C=c.e.showCodeRowNumber,previewTheme:i=c.e.previewTheme,noMermaid:d=c.e.noMermaid,noKatex:I=c.e.noKatex,onHtmlChanged:D=c.e.onHtmlChanged,onGetCatalog:g=c.e.onGetCatalog,sanitize:N=c.e.sanitize,mdHeadingId:R=c.e.mdHeadingId,noIconfont:O=c.e.noIconfont,noHighlight:W=c.e.noHighlight,noImgZoomIn:J=c.e.noImgZoomIn,language:b=c.e.language,sanitizeMermaid:X=c.e.sanitizeMermaid}=s,[x]=(0,h.useState)(()=>({editorId:t,noKatex:I,noMermaid:d,noIconfont:O,noHighlight:W}));(0,j.u)(x);const[q,_,E]=(0,j.b)(s);return se(x,n),(0,h.useEffect)(()=>()=>{A.b.clear(t)},[]),(0,F.j)(j.a.Provider,{value:{editorId:x.editorId,tabWidth:2,theme:r,language:b,highlight:q,showCodeRowNumber:C,usedLanguageText:_,previewTheme:i,customIcon:s.customIcon||{}},children:(0,F.j)("div",{id:x.editorId,className:(0,A.c)([c.p,l,r==="dark"&&`${c.p}-dark`,E.fullscreen||E.pageFullscreen?`${c.p}-fullscreen`:"",`${c.p}-previewOnly`]),style:s.style,children:(0,F.j)(j.C,{modelValue:o,setting:E,mdHeadingId:R,onHtmlChanged:D,onGetCatalog:g,sanitize:N,noMermaid:x.noMermaid,noHighlight:x.noHighlight,noKatex:x.noKatex,formatCopiedText:s.formatCopiedText,noImgZoomIn:J,previewOnly:!0,sanitizeMermaid:X},"preview-only")})})});var Je=e(96132),le=e(74981),B=e(30672),ie=e(5953),K=e.n(ie),a=e(85893),de=e(67631),ce=s=>((0,h.useEffect)(()=>{var n=s.fileType=="mp4"&&s.viewUrl&&!s.isM3u8?new(K())({container:document.getElementById("dplayervideo"+s.sourceID+"x"+s.showIndex),lang:"zh-cn",autoplay:!0,theme:"#722ed1",volume:.7,video:{url:s.viewUrl}}):new(K())({container:document.getElementById("dplayervideo"+s.sourceID+"x"+s.showIndex),lang:"zh-cn",autoplay:!0,theme:"#722ed1",volume:.7,video:{url:s.previewUrl||"",type:"customHls",customType:{customHls:function(r){var l=new de;l.loadSource(r.src),l.attachMedia(r)}}}});n.play(),window["video"+s.sourceID+"x"+s.showIndex]=n,setTimeout(function(){$("#dplayervideo"+s.sourceID+"x"+s.showIndex).removeClass("dplayer-hide-controller").addClass("dplayer-hide-controller")},2e3)},[]),(0,a.jsx)("div",{id:"dplayervideo"+s.sourceID+"x"+s.showIndex})),me=ce,Xe=e(56395),qe=e(42557),_e=e(82679),ea=e(34792),P=e(48086),aa=e(66126),Y=e(99177),Z=e(37406),ve=e(45195),ue=e(22004),pe=e(33953),he=e.n(pe),fe=e(52989),ge=e.n(fe),xe=e(45733),ye=e.n(xe),Ae=e(15316),Ie=e.n(Ae),we=e(37461),je=e.n(we),Ce=e(76877),Ne=e.n(Ce),Se=e(17057),De=e.n(Se),Fe=e(2660),Ee=e.n(Fe);class Me extends h.PureComponent{constructor(n){super(n);this.getinfo=()=>{var o=this,r=this.props.audioInfo;o.setState({player:o.player,playIndex:0,playing:!0})},this.onEnd=o=>{},this.prevNext=o=>{},this.goNext=(o,r)=>{},this.onPercentChange=o=>{var r=this,l=this.props.audioInfo,t=r.state,C=t.volume,i=t.playing,d=t.playIndex,I=t.percent,D=t.playedSeconds,g=t.duration,N=t.musicList;this.setState({playing:!0,percent:o,playedSeconds:o/100*g}),player.seekTo(parseFloat(o/100))},this.onVolumeChange=o=>{var r=this,l=this.props.audioInfo,t=r.state,C=t.volume,i=t.playing,d=t.playIndex,I=t.percent,D=t.playedSeconds,g=t.duration,N=t.musicList;this.setState({volume:o/100})},this.removeActiveMusic=o=>{o.preventDefault()},this.onDuration=this.onDuration.bind(this),this.onProgress=this.onProgress.bind(this),this.onEnd=this.onEnd.bind(this),this.state={modelZindex:100,isinit:!1,player:null,musicList:[],playing:!1,volume:1,duration:0,playIndex:-1,percent:0,playedSeconds:0,playmode:"1"}}componentDidMount(){var n=this;n.getinfo()}onDuration(n){this.setState({duration:n})}onProgress(n){var o=this,r=this.props.audioInfo,l=o.state,t=l.volume,C=l.playing,i=l.playIndex,d=l.percent,I=l.playedSeconds,D=l.duration,g=l.musicList;o.setState({percent:n.played*100,playedSeconds:n.playedSeconds})}render(){var n=this,o=this.props.audioInfo,r=n.state,l=r.volume,t=r.playing,C=r.playIndex,i=r.percent,d=r.playedSeconds,I=r.duration,D=r.musicList,g=r.playmode,N=o||{},R=N.sourceInfo||{};return(0,a.jsx)(a.Fragment,{children:(0,a.jsxs)("div",{className:Ee().audiomodalbox,children:[(0,a.jsxs)("div",{className:"progress",children:[(0,a.jsx)("div",{className:"progressLeft",children:(0,Z.mr)(d)}),(0,a.jsx)(Y.Z,{value:i,tooltip:{formatter:null},onChange:this.onPercentChange}),(0,a.jsx)("div",{className:"progressRight",children:(0,Z.mr)(I)})]}),(0,a.jsxs)("div",{className:"playpause",children:[(0,a.jsxs)("div",{className:"playpauseCenter",children:[(0,a.jsx)("a",{onClick:()=>{this.prevNext("prev")},className:"prev",children:(0,a.jsx)("img",{src:Ne()})}),t?(0,a.jsx)("a",{onClick:()=>{this.setState({playing:!t})},className:"play",children:(0,a.jsx)("img",{src:ye()})}):(0,a.jsx)("a",{onClick:()=>{this.setState({playing:!t})},className:"play",children:(0,a.jsx)("img",{src:Ie()})})," ",(0,a.jsx)("a",{onClick:()=>{this.prevNext("next")},className:"next",children:(0,a.jsx)("img",{src:ge()})})]}),(0,a.jsxs)("div",{className:"playpauseRight",children:[g=="1"?(0,a.jsx)("a",{onClick:()=>{this.setState({playmode:"2"}),P.ZP.info("\u968F\u673A\u64AD\u653E",2,null,!1)},className:"playmode",children:(0,a.jsx)("img",{src:De()})}):"",g=="2"?(0,a.jsx)("a",{onClick:()=>{this.setState({playmode:"3"}),P.ZP.info("\u5355\u66F2\u64AD\u653E",2,null,!1)},className:"playmode1",children:(0,a.jsx)("img",{src:he()})}):"",g=="3"?(0,a.jsx)("a",{onClick:()=>{this.setState({playmode:"1"}),P.ZP.info("\u987A\u5E8F\u64AD\u653E",2,null,!1)},className:"playmode",children:(0,a.jsx)("img",{src:je()})}):""]}),(0,a.jsx)(ve.Z,{}),(0,a.jsxs)("div",{className:"volumeBox",children:[" ",(0,a.jsx)(Y.Z,{onChange:this.onVolumeChange,defaultValue:l*100})]})]}),(0,a.jsx)("div",{className:"musicList"}),(0,a.jsx)("div",{style:{display:"none"},children:(0,a.jsx)(ue.Z,{volume:l,url:N.downloadUrl,controls:!0,playing:t,ref:O=>{this.player=O},onProgress:this.onProgress,onDuration:this.onDuration,onEnded:this.onEnd})})]})})}}var Ue=e(28942),Oe=e.n(Ue),H=e(69847),G,k,oa={width:"100%",height:"100%",position:"relative",color:"white"},na={bottom:"5px",left:"5px",position:"absolute",color:"white"},ta={bottom:"5px",right:"5px",position:"absolute",color:"white"},Q=function(n){var o=document.location.href,r=o.indexOf(n+"=");if(r==-1)return!1;var l=o.slice(n.length+r+1),t=l.indexOf("&");return t!=-1&&(l=l.slice(0,t)),l},be=["jpg","jpeg","png","gif","bmp","ico","svg","webp","tif","tiff","cdr","svgz","xbm","pjepg","heic","raw"],Le=["mp3"],Pe="ppt,pptx,pdf,eid,docx,dotx,doc,dot,rtf,uot,htm,wps,wpt,eis,xlsx,xltx,xls,xlt,uos,dbf,csv,xml,et,ett,eip,pptx,potx,ppt,pot,ppsx,pps,dps,dpt,uop",Ze=["3gp","flv","mp4","avi","mov","wmv","pmg","rmvb","mpeg","mpg","rm","f4v","mkv","m4v","webm","3g2","asf","m2ts","mts","ogv","vob","mpe"],He=["json","css","java","cs","xml","sql","js","ts","less","scss","ejs","go","jsx","json5","ls","mysql","nginx","php","py","rb","rst","tsx","html","htm"],$e=(G=(0,y.$j)(s=>{var n=s.common;return{common:n}}),G(k=class extends h.PureComponent{constructor(){super(...arguments);this.state={modelZindex:100,fullScreen:!1,current:1,total:0,target:{},getInfoLoading:!0,viewUrl:"",taskId:(0,Z.fW)(),psddom:"",stack:{},fileInfo:{},theme:"vs-light",language:"typescript",timeStamp:0,sourceHash:Q("sourceHash")?decodeURIComponent(Q("sourceHash")):""},this.getinfo=()=>{var n=this,o=this.props.dispatch,r=this.state,l=r.sourceID,t=r.sourceHash,C=i=>{var d="javascript";return i=="ejs"&&(d="javascript"),i=="txt"&&(d="text"),i=="html"&&(d="html"),i=="ts"&&(d="typescript"),i=="md"&&(d="markdown"),i=="cs"&&(d="csharp"),i=="go"&&(d="golang"),i=="py"&&(d="python"),(i=="sql"||i=="mysql")&&(d="mysql"),i=="less"&&(d="less"),i=="scss"&&(d="scss"),i=="css"&&(d="css"),(i=="json"||i=="json5")&&(d="json"),d};o({type:"common/getFilePreview",payload:{hash:t},callback:i=>{var d=i.data||{};document.title=d.name,n.setState({fileInfo:d,language:C(d.fileType),timeStamp:i.timeStamp,getInfoLoading:!1})}})}}componentDidMount(){var n=this;n.getinfo()}componentWillUnmount(){var n=this}render(){var n=this,o=n.props,r=o.dispatch,l=o.common,t=o.open,C=o.setOpenModel,i=o.videoOption,d=o.iframeUrl,I=o.iframeName,D=I===void 0?"":I,g=o.issmall,N=o.path,R=o.name,O=o.fileType,W=o.isZipModal,J=o.showIndex,b=o.dcmList,X=b===void 0?[]:b,x=o.dcmActive,q=x===void 0?0:x,_=l.sourceID,E=l.iswxminiapp,f=this.state,sa=f.modelZindex,ee=f.fileInfo,M=ee===void 0?{}:ee,ra=f.fullScreen,Re=f.getInfoLoading,la=f.viewUrl,ae=f.taskId,ia=f.psddom,da=f.stack,ca=f.language,ma=f.theme,Te=f.timeStamp,T=(0,B.lw)((0,B.Kd)()),va=m=>{n.setState({modelZindex:m})},ua={selectOnLineNumbers:!0,roundedSelection:!1,readOnly:!0,cursorStyle:"line",automaticLayout:!1},pa=m=>{},ha=m=>{},Ve=m=>{var S=m.fileType||"";if(Ze.indexOf(m.fileType)>-1)return(0,a.jsx)("div",{className:"videobox",children:(0,a.jsx)(me,(0,w.Z)((0,w.Z)({},M),{},{showIndex:"asdasdxxxxxxxxxxxx"}))});if(be.indexOf(m.fileType)>-1)return(0,a.jsx)("div",{className:"imgbox",children:(0,a.jsx)("img",{src:m.downloadUrl})});if(Le.indexOf(m.fileType)>-1)return(0,a.jsx)("div",{className:"audiobox",children:(0,a.jsx)(Me,{audioInfo:m})});if(["md"].indexOf(m.fileType)>-1)return(0,a.jsx)("div",{className:"mdbox",children:(0,a.jsx)(re,{modelValue:m.text,toolbars:[],previewOnly:!0})});if(He.indexOf(m.fileType)>-1)return(0,a.jsx)("div",{className:"mdbox",children:(0,a.jsx)(le.ZP,{width:"100%",height:"100%",style:{"border-radius":"4px"},value:m.text,mode:"javascript",theme:"tomorrow",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}})});if(["txt"].indexOf(m.fileType)>-1)return(0,a.jsx)("div",{className:"txtbox",children:(0,a.jsx)("pre",{"data-lang":"html",children:(0,a.jsx)("code",{children:m.text})})});if(Pe.indexOf(m.fileType)>-1){var V="",oe=m.yzViewData||"",ze=oe?JSON.parse(oe):{};return V=ze.viewUrl||m.pptPreviewUrl||"",(S=="xmind"||S=="smm"||S=="mind"||S=="km"||S=="epub")&&(V=S=="epub"?"/reader/index.html?fileUrl=".concat(encodeURIComponent(M.downloadUrl)):"/newmindMap/index.html?sourceID=".concat(M.sourceID,"&action=&fileType=").concat(S,"&mindUrl=")+encodeURIComponent(M.downloadUrl)),(0,a.jsx)("div",{className:"docbox",children:(0,a.jsx)("iframe",{id:"iframe"+ae,onLoad:()=>{$("#iframe"+ae).focus()},src:V})})}else{var Be=(0,H.pp)(m);return(0,a.jsxs)("div",{className:"bindary-box",children:[(0,a.jsx)("div",{className:"title",children:(0,a.jsx)("div",{className:"ico",children:(0,a.jsx)("img",{src:Be,className:"icon"})})}),(0,a.jsxs)("div",{className:"content-info",children:[(0,a.jsx)("div",{className:"name",children:m.name}),(0,a.jsxs)("div",{className:"size",children:[" ",(0,a.jsx)("span",{children:(0,H.lX)(m.size)})," ",(0,a.jsxs)("span",{className:"sharetime",children:[" ",(0,H.ZM)(Te)]})]}),(0,a.jsx)("div",{className:"share-download",children:(0,a.jsx)(u.Z,{onClick:()=>{E?(te()(m.downloadUrl+"&d=1"),message.success(T.formatMessage({id:"mack.dsr.a"}))):window.location.href=m.downloadUrl+"&d=1"},type:"primary",children:T.formatMessage({id:"common.download"})})}),(0,a.jsx)("div",{className:"error-tips",children:T.formatMessage({id:"explorer.share.errorShowTips"})})]})]})}};return(0,a.jsx)("div",{className:Oe().implantBox,children:!Re&&(0,a.jsx)(a.Fragment,{children:Ve(M)})})}})||k)},42557:function(p,U,e){p=e.nmd(p),ace.define("ace/theme/tomorrow.css",["require","exports","module"],function(v,u,w){w.exports=`.ace-tomorrow .ace_gutter {
  background: #f6f6f6;
  color: #4D4D4C
}

.ace-tomorrow .ace_print-margin {
  width: 1px;
  background: #f6f6f6
}

.ace-tomorrow {
  background-color: #FFFFFF;
  color: #4D4D4C
}

.ace-tomorrow .ace_cursor {
  color: #AEAFAD
}

.ace-tomorrow .ace_marker-layer .ace_selection {
  background: #D6D6D6
}

.ace-tomorrow.ace_multiselect .ace_selection.ace_start {
  box-shadow: 0 0 3px 0px #FFFFFF;
}

.ace-tomorrow .ace_marker-layer .ace_step {
  background: rgb(255, 255, 0)
}

.ace-tomorrow .ace_marker-layer .ace_bracket {
  margin: -1px 0 0 -1px;
  border: 1px solid #D1D1D1
}

.ace-tomorrow .ace_marker-layer .ace_active-line {
  background: #EFEFEF
}

.ace-tomorrow .ace_gutter-active-line {
  background-color : #dcdcdc
}

.ace-tomorrow .ace_marker-layer .ace_selected-word {
  border: 1px solid #D6D6D6
}

.ace-tomorrow .ace_invisible {
  color: #D1D1D1
}

.ace-tomorrow .ace_keyword,
.ace-tomorrow .ace_meta,
.ace-tomorrow .ace_storage,
.ace-tomorrow .ace_storage.ace_type,
.ace-tomorrow .ace_support.ace_type {
  color: #8959A8
}

.ace-tomorrow .ace_keyword.ace_operator {
  color: #3E999F
}

.ace-tomorrow .ace_constant.ace_character,
.ace-tomorrow .ace_constant.ace_language,
.ace-tomorrow .ace_constant.ace_numeric,
.ace-tomorrow .ace_keyword.ace_other.ace_unit,
.ace-tomorrow .ace_support.ace_constant,
.ace-tomorrow .ace_variable.ace_parameter {
  color: #F5871F
}

.ace-tomorrow .ace_constant.ace_other {
  color: #666969
}

.ace-tomorrow .ace_invalid {
  color: #FFFFFF;
  background-color: #C82829
}

.ace-tomorrow .ace_invalid.ace_deprecated {
  color: #FFFFFF;
  background-color: #8959A8
}

.ace-tomorrow .ace_fold {
  background-color: #4271AE;
  border-color: #4D4D4C
}

.ace-tomorrow .ace_entity.ace_name.ace_function,
.ace-tomorrow .ace_support.ace_function,
.ace-tomorrow .ace_variable {
  color: #4271AE
}

.ace-tomorrow .ace_support.ace_class,
.ace-tomorrow .ace_support.ace_type {
  color: #C99E00
}

.ace-tomorrow .ace_heading,
.ace-tomorrow .ace_markup.ace_heading,
.ace-tomorrow .ace_string {
  color: #718C00
}

.ace-tomorrow .ace_entity.ace_name.ace_tag,
.ace-tomorrow .ace_entity.ace_other.ace_attribute-name,
.ace-tomorrow .ace_meta.ace_tag,
.ace-tomorrow .ace_string.ace_regexp,
.ace-tomorrow .ace_variable {
  color: #C82829
}

.ace-tomorrow .ace_comment {
  color: #8E908C
}

.ace-tomorrow .ace_indent-guide {
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bdu3f/BwAlfgctduB85QAAAABJRU5ErkJggg==) right repeat-y
}

.ace-tomorrow .ace_indent-guide-active {
  background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAAZSURBVHjaYvj///9/hivKyv8BAAAA//8DACLqBhbvk+/eAAAAAElFTkSuQmCC") right repeat-y;
} 
`}),ace.define("ace/theme/tomorrow",["require","exports","module","ace/theme/tomorrow.css","ace/lib/dom"],function(v,u,w){u.isDark=!1,u.cssClass="ace-tomorrow",u.cssText=v("./tomorrow.css");var y=v("../lib/dom");y.importCssString(u.cssText,u.cssClass,!1)}),function(){ace.require(["ace/theme/tomorrow"],function(v){p&&(p.exports=v)})}()},96774:function(p){p.exports=function(e,v,u,w){var y=u?u.call(w,e,v):void 0;if(y!==void 0)return!!y;if(e===v)return!0;if(typeof e!="object"||!e||typeof v!="object"||!v)return!1;var h=Object.keys(e),z=Object.keys(v);if(h.length!==z.length)return!1;for(var F=Object.prototype.hasOwnProperty.bind(v),j=0;j<h.length;j++){var A=h[j];if(!F(A))return!1;var c=e[A],L=v[A];if(y=u?u.call(w,c,L,A):void 0,y===!1||y===void 0&&c!==L)return!1}return!0}}}]);
