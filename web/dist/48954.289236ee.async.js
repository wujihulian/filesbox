(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[48954],{66717:function(X){X.exports={root:"root___2GQGR",colorWeak:"colorWeak___2rEQZ","ant-layout":"ant-layout___7Urmk",globalSpin:"globalSpin___8Btav","ant-table":"ant-table___3eTCa","ant-table-thead":"ant-table-thead___2begL","ant-table-tbody":"ant-table-tbody___3TUF7",formBox:"formBox___trJQD",menu:"menu___CxHsC"}},44943:function(){},48954:function(X,N,a){"use strict";a.r(N),a.d(N,{default:function(){return r}});var F=a(71194),P=a(50146),H=a(57663),D=a(71577),Y=a(34792),q=a(48086),c=a(88983),A=a(47933),S=a(67294),ee=a(28216),G=a(66717),J=a.n(G),I=a(41273),w=a(7861),g=a(85893),C,m,r=(C=(0,ee.$j)(p=>{var o=p.globaldesign,n=p.commondesign,v=p.page;return{globaldesign:o,commondesign:n,page:v}}),C(m=class extends S.PureComponent{constructor(){super(...arguments);this.state={visible:!1,width:880,selectValue:"",loadingOk:!1},this.show=o=>{var n=o.attr("data-formid")||"",v=this.props.dispatch;v({type:"globaldesign/changeState",payload:{formId:""}}),n&&this.setState({selectValue:parseInt(n)})},this.renderHtml=o=>{var n=JSON.parse(o),v=n.title,u=v===void 0?{}:v,_=n.desc,d=n.body,x=n.itemList,K=u.headHeight,L=u.headImg,y=u.value,b='<div style="height: '.concat(K,"px; background-image: url(").concat(L,`);">
                      <div style="position: relative;text-align: center;font-size: 36px;height: 50px;top: 15px;">`).concat(y,`</div>
                      <div style="top: 15px;position: relative;text-align: center;line-height: 30px;">`).concat(_.value,`</div>
                    </div>`),e="";x.map(l=>{var t=l.type,s=l.formType,h=l.title,f=l.description,O=l.placeholder,R=l.optionList,k=R===void 0?[]:R,T=l.thumb,M=l.filePath,U="";switch(k.map(j=>{var Q=j.content,z=j.imagePath;s=="checkbox"?U+='<li><span class="checkBtn"></span><span class="checkName">'.concat(Q,"</span></li>"):U+='<li><span class="radioBtn"></span><span class="radioName"><p>'.concat(Q,'</p></span><img style="width: 100%;display: block;" src="').concat(z,'"/></li>')}),t){case"02":e+=`<div class='module formModule' data-type="02" data-form-type='textarea'>
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent"><textarea placeholder="`).concat(O,`" rows="1" cols="20"></textarea></div>
          </div></div>`);break;case"06":e+=`<div class='module formModule' data-type="06" data-form-type='number'>
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent" data-unit="num">
              <input placeholder="`).concat(O,`"/>
              <div class="rightText"></div>
            </div>
          </div></div>`);break;case"03":e+=`<div class='module formModule' data-type="03" data-form-type='radio'>
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent">
              <div class="radioBox">
                `).concat(U,`
              </div>
            </div>
          </div></div>`);break;case"04":e+=`<div class='module formModule' data-type="04" data-form-type='checkbox'>
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent">
              <div class="checkBox">
                `).concat(U,`
              </div>
            </div>
          </div></div>`);break;case"05":e+=`<div class='module formModule' data-type="05" data-form-type='select'>
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent">
              <div class="selectBox">
                <div class="selectValue">
                  <p class="value"></p>
                  <p class="placeholder">`).concat(O,`</p>
                  <div class="selectArrow"></div>
                </div>
                <div class="optionBox">
                  <li>\u9009\u98791</li>
                  <li>\u9009\u98792</li>
                  <li>\u9009\u98793</li>
                </div>
              </div>
            </div>
          </div></div>`);break;case"61":e+=`<div class='module formModule' data-type="61" data-form-type='pictureupload'  style="color:#999;">
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent">
              <div class="pictureBox">
                <img class="tip" src="`).concat("",`/designschool/form/image/picupload.png"/>
                <p>\u70B9\u51FB\u9009\u62E9\u56FE\u7247<br/>
                (\u9700\u5C0F\u4E8E10M\uFF09</p>
              </div>
            </div>
          </div></div>`);break;case"62":e+=`<div class='module formModule' data-type="62" data-form-type='fileupload' style="color:#999;">
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent">
              <div class="fileBox">
                <span style="color:#999;font-size: 22px;float: left;margin-right: 7px;">+</span>\u4E0A\u4F20(\u9700\u5C0F\u4E8E20M\uFF09
              </div>
            </div>
          </div></div>`);break;case"08":e+=`<div class='module formModule' data-type="08" data-form-type='score'>
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent" data-iconNum='1' data-maxVal='5' data-defaultVal='0'>
              <div class="scoreBox">
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
              </div>
            </div>
          </div></div>`);break;case"12":e+=`<div class='module formModule' data-type="12" data-form-type='date'>
            <div class="contentBox">
              <div class="labelbox">
                <div class="label"><span>`.concat(h,`</span></div>
                <div class="desc">`).concat(f,`</div>
              </div>
              <div class="formContent formIcon">
                <div class="dateBox"></div>
                <img class="inputIcon" src="`).concat("",`/designschool/form/Assembly/icon/12.svg"/>
              </div>
            </div>
          </div>`);break;case"34":e+=`<div class='module formModule' data-type="34" data-form-type='name'>
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent formIcon">
              <input/>
              <img class="inputIcon" src="`).concat("",`/designschool/form/Assembly/icon/34.svg"/>
            </div>
          </div></div>`);break;case"35":e+=`<div class='module formModule' data-type="35" data-form-type='mobile'>
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent formIcon" data-check="1" data-checktype="mobile">
              <input/>
              <img class="inputIcon" src="`).concat("",`/designschool/form/Assembly/icon/35.svg"/>
            </div>
          </div></div>`);break;case"81":e+=`<div class='module formModule' data-type="81" data-form-type='desc'>
            <div class="contentBox"><div class="labelbox">
              <div class="desc">`.concat(f,`</div>
            </div>
          </div></div>`);break;case"84":e+=`<div class='module formModule formShowModule' data-show-type="picture" data-type="81" data-form-type='desc'>
            <div class="contentBox">
              <div class="formContent">
                <div style="background: #fff url(`.concat("",`/designschool/form/image/noPicture.png) no-repeat center;height: 50px; background-size: 32px;"></div>
              </div>
            </div>
          </div>`);break;case"85":e+=`<div class='module formModule formShowModule' data-show-type="video"  data-type="85" data-form-type='desc'>
            <div class="contentBox">
              <div class="formContent">
                `.concat(T?'<img src="'.concat(T,'" style="width: 100%;" />'):'<div style="background: #fff url(${cdnPath}/designschool/form/image/noAudio.png) no-repeat center;height: 50px; background-size: 32px;"></div>',`
              </div>
            </div>
          </div>`);break;case"86":e+=`<div class='module formModule formShowModule' data-show-type="audio" data-type="81" data-form-type='desc'>
            <div class="contentBox">
              <div class="formContent">
                `.concat(M?'<audio src="'.concat(M,'">\u60A8\u7684\u6D4F\u89C8\u5668\u4E0D\u652F\u6301 audio \u6807\u7B7E\u3002</audio>'):'<div style="background: #fff url(${cdnPath}/designschool/form/image/noAudio.png) no-repeat center;height: 50px; background-size: 32px;"></div>',`
              </div>
            </div>
          </div>`);break;case"83":e+=`<div class='module formModule formShowModule' data-show-type="file" data-type="81" data-form-type='desc' style="color:#999;">
            <div class="contentBox"><div class="labelbox">
              <div class="label"><span>`.concat(h,`</span></div>
              <div class="desc">`).concat(f,`</div>
            </div>
            <div class="formContent">
              <div class="fileBox">
                \u8BF7\u6DFB\u52A0\u9644\u4EF6
              </div>
            </div>
          </div></div>`);break}});var i=`<div>
                  `.concat(b,`
                  <div id="mainForm">
                    `).concat(e,`
                  </div>
                  <div class="bottomBtnBox" style="clear: both;"><div class="submit">\u63D0\u4EA4</div></div>
                </div>`);return i},this.updataDomCallBack=(o,n)=>{var v=this,u=v.state.selectValue,_=this.props,d=_.commondesign,x=_.globaldesign,K=_.dispatch,L=x.isMobile,y=d.PageForm,b=y.element,e=y.callback,i=y.callClose;b.attr("data-formid",u),b.removeClass("onModule"),v.getFindForm(u,(l,t)=>{l?b.html('<div id="formHeightBox">'+v.renderHtml(t)+"</div>"):b.html('<div id="formHeightBox">'+t+"</div>"),b.css({left:0,height:$("#formHeightBox").height()}),n&&n(),setTimeout(()=>{(0,w.U7)(v)},500)})},this.cancelEvent=()=>{var o=this,n=this.props.dispatch;n({type:"commondesign/setState",payload:{PageForm:{visible:!1,callback:()=>{},callClose:()=>{}}}})},this.getFindForm=(o,n)=>{var v=this,u=this.props.dispatch,_=d=>{var x=d.itemJsonSavePath,K=d.referenceSavePath,L=x?1:0;$.ajax({type:"get",url:x||K,dataType:"html",success:function(b){b&&n&&n(L,b)}})};u({type:"page/getFindForm",payload:{formId:o},callback:d=>{var x=d.data.formInfo;_(x)}})},this.getFormPage=()=>{var o=this,n=this.props,v=n.dispatch,u=n.page,_=n.typeId,d=u.formList;d.length<1&&v({type:"page/findFormPageList",payload:{status:"1",currentPage:1,pageSize:1e3}})}}componentDidMount(){var o=this.props,n=o.visible,v=o.element,u=o.callback,_=o.callClose,d=o.commondesign;this.setState({visible:n,callback:u}),this.getFormPage(),this.show(v)}renderContent(){var o=this,n=this.props.page,v=n.formList,u=this.state.selectValue,_=d=>{o.setState({selectValue:d.target.value})};return(0,g.jsx)("div",{children:(0,g.jsx)("div",{className:"main",children:(0,g.jsx)(A.ZP.Group,{onChange:_,value:u,children:v.map(d=>(0,g.jsx)("div",{className:"pageList",id:d.formId,children:(0,g.jsx)(A.ZP,{value:d.formId,children:d.title})},d.formId))})})})}render(){var o=this,n=this.state,v=n.width,u=n.selectValue,_=n.loadingOk,d=this.props,x=d.commondesign,K=d.element,L=d.dispatch,y=d.visible,b=d.globaldesign,e=x.PageForm.callback,i=()=>{if(!u){q.ZP.warning("\u8BF7\u9009\u62E9\u8868\u5355");return}o.setState({loadingOk:!0});var t={};o.updataDomCallBack(t,()=>{e&&e(t),o.cancelEvent()})},l=()=>{o.cancelEvent(),o.setState({loadingOk:!1})};return(0,g.jsxs)(P.Z,{title:"\u8868\u5355",visible:y,onOk:i,width:v,onCancel:l,maskClosable:!1,className:J().menu,footer:null,children:[o.renderContent(),(0,g.jsxs)("div",{className:"footer",children:[(0,g.jsx)(D.Z,{onClick:i,type:"primary",children:"\u786E\u5B9A"}),(0,g.jsx)(D.Z,{onClick:l,children:"\u53D6\u6D88"})]})]})}})||m)},47933:function(X,N,a){"use strict";a.d(N,{ZP:function(){return b}});var F=a(22122),P=a(96156),H=a(28481),D=a(94184),Y=a.n(D),q=a(21770),c=a(67294),A=a(53124),S=a(97647),ee=a(5467),G=c.createContext(null),J=G.Provider,I=G,w=c.createContext(null),g=w.Provider,C=a(50132),m=a(42550),r=a(98866),p=a(65223),o=function(e,i){var l={};for(var t in e)Object.prototype.hasOwnProperty.call(e,t)&&i.indexOf(t)<0&&(l[t]=e[t]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var s=0,t=Object.getOwnPropertySymbols(e);s<t.length;s++)i.indexOf(t[s])<0&&Object.prototype.propertyIsEnumerable.call(e,t[s])&&(l[t[s]]=e[t[s]]);return l},n=function(i,l){var t,s=c.useContext(I),h=c.useContext(w),f=c.useContext(A.E_),O=f.getPrefixCls,R=f.direction,k=c.useRef(),T=(0,m.sQ)(l,k),M=(0,c.useContext)(p.aM),U=M.isFormItemInput,j=function(se){var V,W;(V=i.onChange)===null||V===void 0||V.call(i,se),(W=s==null?void 0:s.onChange)===null||W===void 0||W.call(s,se)},Q=i.prefixCls,z=i.className,ae=i.children,oe=i.style,te=i.disabled,le=o(i,["prefixCls","className","children","style","disabled"]),ne=O("radio",Q),Z=((s==null?void 0:s.optionType)||h)==="button"?"".concat(ne,"-button"):ne,B=(0,F.Z)({},le),ie=c.useContext(r.Z);B.disabled=te||ie,s&&(B.name=s.name,B.onChange=j,B.checked=i.value===s.value,B.disabled=B.disabled||s.disabled);var de=Y()("".concat(Z,"-wrapper"),(t={},(0,P.Z)(t,"".concat(Z,"-wrapper-checked"),B.checked),(0,P.Z)(t,"".concat(Z,"-wrapper-disabled"),B.disabled),(0,P.Z)(t,"".concat(Z,"-wrapper-rtl"),R==="rtl"),(0,P.Z)(t,"".concat(Z,"-wrapper-in-form-item"),U),t),z);return c.createElement("label",{className:de,style:oe,onMouseEnter:i.onMouseEnter,onMouseLeave:i.onMouseLeave},c.createElement(C.Z,(0,F.Z)({},B,{type:"radio",prefixCls:Z,ref:T})),ae!==void 0?c.createElement("span",null,ae):null)},v=c.forwardRef(n),u=v,_=c.forwardRef(function(e,i){var l,t=c.useContext(A.E_),s=t.getPrefixCls,h=t.direction,f=c.useContext(S.Z),O=(0,q.Z)(e.defaultValue,{value:e.value}),R=(0,H.Z)(O,2),k=R[0],T=R[1],M=function(ue){var he=k,me=ue.target.value;"value"in e||T(me);var fe=e.onChange;fe&&me!==he&&fe(ue)},U=e.prefixCls,j=e.className,Q=j===void 0?"":j,z=e.options,ae=e.buttonStyle,oe=ae===void 0?"outline":ae,te=e.disabled,le=e.children,ne=e.size,Z=e.style,B=e.id,ie=e.onMouseEnter,de=e.onMouseLeave,ce=e.onFocus,se=e.onBlur,V=s("radio",U),W="".concat(V,"-group"),re=le;z&&z.length>0&&(re=z.map(function(E){return typeof E=="string"||typeof E=="number"?c.createElement(u,{key:E.toString(),prefixCls:V,disabled:te,value:E,checked:k===E},E):c.createElement(u,{key:"radio-group-value-options-".concat(E.value),prefixCls:V,disabled:E.disabled||te,value:E.value,checked:k===E.value,style:E.style},E.label)}));var ve=ne||f,pe=Y()(W,"".concat(W,"-").concat(oe),(l={},(0,P.Z)(l,"".concat(W,"-").concat(ve),ve),(0,P.Z)(l,"".concat(W,"-rtl"),h==="rtl"),l),Q);return c.createElement("div",(0,F.Z)({},(0,ee.Z)(e),{className:pe,style:Z,onMouseEnter:ie,onMouseLeave:de,onFocus:ce,onBlur:se,id:B,ref:i}),c.createElement(J,{value:{onChange:M,value:k,disabled:e.disabled,name:e.name,optionType:e.optionType}},re))}),d=c.memo(_),x=function(e,i){var l={};for(var t in e)Object.prototype.hasOwnProperty.call(e,t)&&i.indexOf(t)<0&&(l[t]=e[t]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var s=0,t=Object.getOwnPropertySymbols(e);s<t.length;s++)i.indexOf(t[s])<0&&Object.prototype.propertyIsEnumerable.call(e,t[s])&&(l[t[s]]=e[t[s]]);return l},K=function(i,l){var t=c.useContext(A.E_),s=t.getPrefixCls,h=i.prefixCls,f=x(i,["prefixCls"]),O=s("radio",h);return c.createElement(g,{value:"button"},c.createElement(u,(0,F.Z)({prefixCls:O},f,{type:"radio",ref:l})))},L=c.forwardRef(K),y=u;y.Button=L,y.Group=d,y.__ANT_RADIO=!0;var b=y},88983:function(X,N,a){"use strict";var F=a(38663),P=a.n(F),H=a(44943),D=a.n(H)},50132:function(X,N,a){"use strict";var F=a(22122),P=a(96156),H=a(81253),D=a(28991),Y=a(6610),q=a(5991),c=a(10379),A=a(54070),S=a(67294),ee=a(94184),G=a.n(ee),J=function(I){(0,c.Z)(g,I);var w=(0,A.Z)(g);function g(C){var m;(0,Y.Z)(this,g),m=w.call(this,C),m.handleChange=function(p){var o=m.props,n=o.disabled,v=o.onChange;n||("checked"in m.props||m.setState({checked:p.target.checked}),v&&v({target:(0,D.Z)((0,D.Z)({},m.props),{},{checked:p.target.checked}),stopPropagation:function(){p.stopPropagation()},preventDefault:function(){p.preventDefault()},nativeEvent:p.nativeEvent}))},m.saveInput=function(p){m.input=p};var r="checked"in C?C.checked:C.defaultChecked;return m.state={checked:r},m}return(0,q.Z)(g,[{key:"focus",value:function(){this.input.focus()}},{key:"blur",value:function(){this.input.blur()}},{key:"render",value:function(){var m,r=this.props,p=r.prefixCls,o=r.className,n=r.style,v=r.name,u=r.id,_=r.type,d=r.disabled,x=r.readOnly,K=r.tabIndex,L=r.onClick,y=r.onFocus,b=r.onBlur,e=r.onKeyDown,i=r.onKeyPress,l=r.onKeyUp,t=r.autoFocus,s=r.value,h=r.required,f=(0,H.Z)(r,["prefixCls","className","style","name","id","type","disabled","readOnly","tabIndex","onClick","onFocus","onBlur","onKeyDown","onKeyPress","onKeyUp","autoFocus","value","required"]),O=Object.keys(f).reduce(function(T,M){return(M.substr(0,5)==="aria-"||M.substr(0,5)==="data-"||M==="role")&&(T[M]=f[M]),T},{}),R=this.state.checked,k=G()(p,o,(m={},(0,P.Z)(m,"".concat(p,"-checked"),R),(0,P.Z)(m,"".concat(p,"-disabled"),d),m));return S.createElement("span",{className:k,style:n},S.createElement("input",(0,F.Z)({name:v,id:u,type:_,required:h,readOnly:x,disabled:d,tabIndex:K,className:"".concat(p,"-input"),checked:!!R,onClick:L,onFocus:y,onBlur:b,onKeyUp:l,onKeyDown:e,onKeyPress:i,onChange:this.handleChange,autoFocus:t,ref:this.saveInput,value:s},O)),S.createElement("span",{className:"".concat(p,"-inner")}))}}],[{key:"getDerivedStateFromProps",value:function(m,r){return"checked"in m?(0,D.Z)((0,D.Z)({},r),{},{checked:m.checked}):null}}]),g}(S.Component);J.defaultProps={prefixCls:"rc-checkbox",className:"",style:{},type:"checkbox",defaultChecked:!1,onFocus:function(){},onBlur:function(){},onChange:function(){},onKeyDown:function(){},onKeyPress:function(){},onKeyUp:function(){}},N.Z=J}}]);
