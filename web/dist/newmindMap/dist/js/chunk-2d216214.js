(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2d216214"],{c0c4:function(pt,fn,pn){/*! @license DOMPurify 2.4.1 | (c) Cure53 and other contributors | Released under the Apache license 2.0 and Mozilla Public License 2.0 | github.com/cure53/DOMPurify/blob/2.4.1/LICENSE */(function(x,G){pt.exports=G()})(0,function(){"use strict";function x(n){return x=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(o){return typeof o}:function(o){return o&&typeof Symbol=="function"&&o.constructor===Symbol&&o!==Symbol.prototype?"symbol":typeof o},x(n)}function G(n,o){return G=Object.setPrototypeOf||function(a,c){return a.__proto__=c,a},G(n,o)}function dt(){if(typeof Reflect=="undefined"||!Reflect.construct||Reflect.construct.sham)return!1;if(typeof Proxy=="function")return!0;try{return Boolean.prototype.valueOf.call(Reflect.construct(Boolean,[],function(){})),!0}catch(n){return!1}}function ce(n,o,a){return ce=dt()?Reflect.construct:function(c,u,T){var M=[null];M.push.apply(M,u);var Q=Function.bind.apply(c,M),I=new Q;return T&&G(I,T.prototype),I},ce.apply(null,arguments)}function A(n){return ht(n)||gt(n)||yt(n)||bt()}function ht(n){if(Array.isArray(n))return ue(n)}function gt(n){if(typeof Symbol!="undefined"&&n[Symbol.iterator]!=null||n["@@iterator"]!=null)return Array.from(n)}function yt(n,o){if(n){if(typeof n=="string")return ue(n,o);var a=Object.prototype.toString.call(n).slice(8,-1);return a==="Object"&&n.constructor&&(a=n.constructor.name),a==="Map"||a==="Set"?Array.from(n):a==="Arguments"||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(a)?ue(n,o):void 0}}function ue(n,o){(o==null||o>n.length)&&(o=n.length);for(var a=0,c=new Array(o);a<o;a++)c[a]=n[a];return c}function bt(){throw new TypeError(`Invalid attempt to spread non-iterable instance.
In order to be iterable, non-array objects must have a [Symbol.iterator]() method.`)}var vt=Object.hasOwnProperty,Ie=Object.setPrototypeOf,Tt=Object.isFrozen,Nt=Object.getPrototypeOf,At=Object.getOwnPropertyDescriptor,y=Object.freeze,E=Object.seal,Et=Object.create,Fe=typeof Reflect!="undefined"&&Reflect,V=Fe.apply,se=Fe.construct;V||(V=function(n,o,a){return n.apply(o,a)}),y||(y=function(n){return n}),E||(E=function(n){return n}),se||(se=function(n,o){return ce(n,A(o))});var wt=N(Array.prototype.forEach),Ue=N(Array.prototype.pop),W=N(Array.prototype.push),J=N(String.prototype.toLowerCase),me=N(String.prototype.toString),St=N(String.prototype.match),w=N(String.prototype.replace),kt=N(String.prototype.indexOf),xt=N(String.prototype.trim),b=N(RegExp.prototype.test),fe=_t(TypeError);function N(n){return function(o){for(var a=arguments.length,c=new Array(a>1?a-1:0),u=1;u<a;u++)c[u-1]=arguments[u];return V(n,o,c)}}function _t(n){return function(){for(var o=arguments.length,a=new Array(o),c=0;c<o;c++)a[c]=arguments[c];return se(n,a)}}function i(n,o,a){a=a||J,Ie&&Ie(n,null);for(var c=o.length;c--;){var u=o[c];if(typeof u=="string"){var T=a(u);T!==u&&(Tt(o)||(o[c]=T),u=T)}n[u]=!0}return n}function L(n){var o,a=Et(null);for(o in n)V(vt,n,[o])&&(a[o]=n[o]);return a}function X(n,o){for(;n!==null;){var a=At(n,o);if(a){if(a.get)return N(a.get);if(typeof a.value=="function")return N(a.value)}n=Nt(n)}function c(u){return console.warn("fallback value for",u),null}return c}var He=y(["a","abbr","acronym","address","area","article","aside","audio","b","bdi","bdo","big","blink","blockquote","body","br","button","canvas","caption","center","cite","code","col","colgroup","content","data","datalist","dd","decorator","del","details","dfn","dialog","dir","div","dl","dt","element","em","fieldset","figcaption","figure","font","footer","form","h1","h2","h3","h4","h5","h6","head","header","hgroup","hr","html","i","img","input","ins","kbd","label","legend","li","main","map","mark","marquee","menu","menuitem","meter","nav","nobr","ol","optgroup","option","output","p","picture","pre","progress","q","rp","rt","ruby","s","samp","section","select","shadow","small","source","spacer","span","strike","strong","style","sub","summary","sup","table","tbody","td","template","textarea","tfoot","th","thead","time","tr","track","tt","u","ul","var","video","wbr"]),pe=y(["svg","a","altglyph","altglyphdef","altglyphitem","animatecolor","animatemotion","animatetransform","circle","clippath","defs","desc","ellipse","filter","font","g","glyph","glyphref","hkern","image","line","lineargradient","marker","mask","metadata","mpath","path","pattern","polygon","polyline","radialgradient","rect","stop","style","switch","symbol","text","textpath","title","tref","tspan","view","vkern"]),de=y(["feBlend","feColorMatrix","feComponentTransfer","feComposite","feConvolveMatrix","feDiffuseLighting","feDisplacementMap","feDistantLight","feFlood","feFuncA","feFuncB","feFuncG","feFuncR","feGaussianBlur","feImage","feMerge","feMergeNode","feMorphology","feOffset","fePointLight","feSpecularLighting","feSpotLight","feTile","feTurbulence"]),Rt=y(["animate","color-profile","cursor","discard","fedropshadow","font-face","font-face-format","font-face-name","font-face-src","font-face-uri","foreignobject","hatch","hatchpath","mesh","meshgradient","meshpatch","meshrow","missing-glyph","script","set","solidcolor","unknown","use"]),he=y(["math","menclose","merror","mfenced","mfrac","mglyph","mi","mlabeledtr","mmultiscripts","mn","mo","mover","mpadded","mphantom","mroot","mrow","ms","mspace","msqrt","mstyle","msub","msup","msubsup","mtable","mtd","mtext","mtr","munder","munderover"]),Ot=y(["maction","maligngroup","malignmark","mlongdiv","mscarries","mscarry","msgroup","mstack","msline","msrow","semantics","annotation","annotation-xml","mprescripts","none"]),ze=y(["#text"]),Pe=y(["accept","action","align","alt","autocapitalize","autocomplete","autopictureinpicture","autoplay","background","bgcolor","border","capture","cellpadding","cellspacing","checked","cite","class","clear","color","cols","colspan","controls","controlslist","coords","crossorigin","datetime","decoding","default","dir","disabled","disablepictureinpicture","disableremoteplayback","download","draggable","enctype","enterkeyhint","face","for","headers","height","hidden","high","href","hreflang","id","inputmode","integrity","ismap","kind","label","lang","list","loading","loop","low","max","maxlength","media","method","min","minlength","multiple","muted","name","nonce","noshade","novalidate","nowrap","open","optimum","pattern","placeholder","playsinline","poster","preload","pubdate","radiogroup","readonly","rel","required","rev","reversed","role","rows","rowspan","spellcheck","scope","selected","shape","size","sizes","span","srclang","start","src","srcset","step","style","summary","tabindex","title","translate","type","usemap","valign","value","width","xmlns","slot"]),ge=y(["accent-height","accumulate","additive","alignment-baseline","ascent","attributename","attributetype","azimuth","basefrequency","baseline-shift","begin","bias","by","class","clip","clippathunits","clip-path","clip-rule","color","color-interpolation","color-interpolation-filters","color-profile","color-rendering","cx","cy","d","dx","dy","diffuseconstant","direction","display","divisor","dur","edgemode","elevation","end","fill","fill-opacity","fill-rule","filter","filterunits","flood-color","flood-opacity","font-family","font-size","font-size-adjust","font-stretch","font-style","font-variant","font-weight","fx","fy","g1","g2","glyph-name","glyphref","gradientunits","gradienttransform","height","href","id","image-rendering","in","in2","k","k1","k2","k3","k4","kerning","keypoints","keysplines","keytimes","lang","lengthadjust","letter-spacing","kernelmatrix","kernelunitlength","lighting-color","local","marker-end","marker-mid","marker-start","markerheight","markerunits","markerwidth","maskcontentunits","maskunits","max","mask","media","method","mode","min","name","numoctaves","offset","operator","opacity","order","orient","orientation","origin","overflow","paint-order","path","pathlength","patterncontentunits","patterntransform","patternunits","points","preservealpha","preserveaspectratio","primitiveunits","r","rx","ry","radius","refx","refy","repeatcount","repeatdur","restart","result","rotate","scale","seed","shape-rendering","specularconstant","specularexponent","spreadmethod","startoffset","stddeviation","stitchtiles","stop-color","stop-opacity","stroke-dasharray","stroke-dashoffset","stroke-linecap","stroke-linejoin","stroke-miterlimit","stroke-opacity","stroke","stroke-width","style","surfacescale","systemlanguage","tabindex","targetx","targety","transform","transform-origin","text-anchor","text-decoration","text-rendering","textlength","type","u1","u2","unicode","values","viewbox","visibility","version","vert-adv-y","vert-origin-x","vert-origin-y","width","word-spacing","wrap","writing-mode","xchannelselector","ychannelselector","x","x1","x2","xmlns","y","y1","y2","z","zoomandpan"]),Be=y(["accent","accentunder","align","bevelled","close","columnsalign","columnlines","columnspan","denomalign","depth","dir","display","displaystyle","encoding","fence","frame","height","href","id","largeop","length","linethickness","lspace","lquote","mathbackground","mathcolor","mathsize","mathvariant","maxsize","minsize","movablelimits","notation","numalign","open","rowalign","rowlines","rowspacing","rowspan","rspace","rquote","scriptlevel","scriptminsize","scriptsizemultiplier","selection","separator","separators","stretchy","subscriptshift","supscriptshift","symmetric","voffset","width","xmlns"]),Z=y(["xlink:href","xml:id","xlink:title","xml:space","xmlns:xlink"]),Dt=E(/\{\{[\w\W]*|[\w\W]*\}\}/gm),Lt=E(/<%[\w\W]*|[\w\W]*%>/gm),Mt=E(/\${[\w\W]*}/gm),Ct=E(/^data-[\-\w.\u00B7-\uFFFF]/),It=E(/^aria-[\-\w]+$/),Ft=E(/^(?:(?:(?:f|ht)tps?|mailto|tel|callto|cid|xmpp):|[^a-z]|[a-z+.\-]+(?:[^a-z+.\-:]|$))/i),Ut=E(/^(?:\w+script|data):/i),Ht=E(/[\u0000-\u0020\u00A0\u1680\u180E\u2000-\u2029\u205F\u3000]/g),zt=E(/^html$/i),Pt=function(){return typeof window=="undefined"?null:window},Bt=function(n,o){if(x(n)!=="object"||typeof n.createPolicy!="function")return null;var a=null,c="data-tt-policy-suffix";o.currentScript&&o.currentScript.hasAttribute(c)&&(a=o.currentScript.getAttribute(c));var u="dompurify"+(a?"#"+a:"");try{return n.createPolicy(u,{createHTML:function(T){return T},createScriptURL:function(T){return T}})}catch(T){return console.warn("TrustedTypes policy "+u+" could not be created."),null}};function je(){var n=arguments.length>0&&arguments[0]!==void 0?arguments[0]:Pt(),o=function(e){return je(e)};if(o.version="2.4.1",o.removed=[],!n||!n.document||n.document.nodeType!==9)return o.isSupported=!1,o;var a=n.document,c=n.document,u=n.DocumentFragment,T=n.HTMLTemplateElement,M=n.Node,Q=n.Element,I=n.NodeFilter,Ge=n.NamedNodeMap,Gt=Ge===void 0?n.NamedNodeMap||n.MozNamedAttrMap:Ge,Wt=n.HTMLFormElement,qt=n.DOMParser,ee=n.trustedTypes,te=Q.prototype,Yt=X(te,"cloneNode"),$t=X(te,"nextSibling"),Kt=X(te,"childNodes"),ye=X(te,"parentNode");if(typeof T=="function"){var be=c.createElement("template");be.content&&be.content.ownerDocument&&(c=be.content.ownerDocument)}var S=Bt(ee,a),We=S?S.createHTML(""):"",ne=c,ve=ne.implementation,Vt=ne.createNodeIterator,Jt=ne.createDocumentFragment,Xt=ne.getElementsByTagName,Zt=a.importNode,qe={};try{qe=L(c).documentMode?c.documentMode:{}}catch(e){}var k={};o.isSupported=typeof ye=="function"&&ve&&typeof ve.createHTMLDocument!="undefined"&&qe!==9;var F,m,Te=Dt,Ne=Lt,Ae=Mt,Qt=Ct,en=It,tn=Ut,Ye=Ht,Ee=Ft,f=null,$e=i({},[].concat(A(He),A(pe),A(de),A(he),A(ze))),p=null,Ke=i({},[].concat(A(Pe),A(ge),A(Be),A(Z))),s=Object.seal(Object.create(null,{tagNameCheck:{writable:!0,configurable:!1,enumerable:!0,value:null},attributeNameCheck:{writable:!0,configurable:!1,enumerable:!0,value:null},allowCustomizedBuiltInElements:{writable:!0,configurable:!1,enumerable:!0,value:!1}})),q=null,we=null,Ve=!0,Se=!0,Je=!1,U=!1,C=!1,ke=!1,xe=!1,H=!1,re=!1,oe=!1,Xe=!0,Ze=!1,nn="user-content-",_e=!0,Y=!1,z={},P=null,Qe=i({},["annotation-xml","audio","colgroup","desc","foreignobject","head","iframe","math","mi","mn","mo","ms","mtext","noembed","noframes","noscript","plaintext","script","style","svg","template","thead","title","video","xmp"]),et=null,tt=i({},["audio","video","img","source","image","track"]),Re=null,nt=i({},["alt","class","for","id","label","name","pattern","placeholder","role","summary","title","value","style","xmlns"]),ae="http://www.w3.org/1998/Math/MathML",ie="http://www.w3.org/2000/svg",_="http://www.w3.org/1999/xhtml",B=_,Oe=!1,De=null,rn=i({},[ae,ie,_],me),on=["application/xhtml+xml","text/html"],an="text/html",j=null,ln=c.createElement("form"),rt=function(e){return e instanceof RegExp||e instanceof Function},Le=function(e){j&&j===e||(e&&x(e)==="object"||(e={}),e=L(e),F=F=on.indexOf(e.PARSER_MEDIA_TYPE)===-1?an:e.PARSER_MEDIA_TYPE,m=F==="application/xhtml+xml"?me:J,f="ALLOWED_TAGS"in e?i({},e.ALLOWED_TAGS,m):$e,p="ALLOWED_ATTR"in e?i({},e.ALLOWED_ATTR,m):Ke,De="ALLOWED_NAMESPACES"in e?i({},e.ALLOWED_NAMESPACES,me):rn,Re="ADD_URI_SAFE_ATTR"in e?i(L(nt),e.ADD_URI_SAFE_ATTR,m):nt,et="ADD_DATA_URI_TAGS"in e?i(L(tt),e.ADD_DATA_URI_TAGS,m):tt,P="FORBID_CONTENTS"in e?i({},e.FORBID_CONTENTS,m):Qe,q="FORBID_TAGS"in e?i({},e.FORBID_TAGS,m):{},we="FORBID_ATTR"in e?i({},e.FORBID_ATTR,m):{},z="USE_PROFILES"in e&&e.USE_PROFILES,Ve=e.ALLOW_ARIA_ATTR!==!1,Se=e.ALLOW_DATA_ATTR!==!1,Je=e.ALLOW_UNKNOWN_PROTOCOLS||!1,U=e.SAFE_FOR_TEMPLATES||!1,C=e.WHOLE_DOCUMENT||!1,H=e.RETURN_DOM||!1,re=e.RETURN_DOM_FRAGMENT||!1,oe=e.RETURN_TRUSTED_TYPE||!1,xe=e.FORCE_BODY||!1,Xe=e.SANITIZE_DOM!==!1,Ze=e.SANITIZE_NAMED_PROPS||!1,_e=e.KEEP_CONTENT!==!1,Y=e.IN_PLACE||!1,Ee=e.ALLOWED_URI_REGEXP||Ee,B=e.NAMESPACE||_,e.CUSTOM_ELEMENT_HANDLING&&rt(e.CUSTOM_ELEMENT_HANDLING.tagNameCheck)&&(s.tagNameCheck=e.CUSTOM_ELEMENT_HANDLING.tagNameCheck),e.CUSTOM_ELEMENT_HANDLING&&rt(e.CUSTOM_ELEMENT_HANDLING.attributeNameCheck)&&(s.attributeNameCheck=e.CUSTOM_ELEMENT_HANDLING.attributeNameCheck),e.CUSTOM_ELEMENT_HANDLING&&typeof e.CUSTOM_ELEMENT_HANDLING.allowCustomizedBuiltInElements=="boolean"&&(s.allowCustomizedBuiltInElements=e.CUSTOM_ELEMENT_HANDLING.allowCustomizedBuiltInElements),U&&(Se=!1),re&&(H=!0),z&&(f=i({},A(ze)),p=[],z.html===!0&&(i(f,He),i(p,Pe)),z.svg===!0&&(i(f,pe),i(p,ge),i(p,Z)),z.svgFilters===!0&&(i(f,de),i(p,ge),i(p,Z)),z.mathMl===!0&&(i(f,he),i(p,Be),i(p,Z))),e.ADD_TAGS&&(f===$e&&(f=L(f)),i(f,e.ADD_TAGS,m)),e.ADD_ATTR&&(p===Ke&&(p=L(p)),i(p,e.ADD_ATTR,m)),e.ADD_URI_SAFE_ATTR&&i(Re,e.ADD_URI_SAFE_ATTR,m),e.FORBID_CONTENTS&&(P===Qe&&(P=L(P)),i(P,e.FORBID_CONTENTS,m)),_e&&(f["#text"]=!0),C&&i(f,["html","head","body"]),f.table&&(i(f,["tbody"]),delete q.tbody),y&&y(e),j=e)},ot=i({},["mi","mo","mn","ms","mtext"]),at=i({},["foreignobject","desc","title","annotation-xml"]),cn=i({},["title","style","font","a","script"]),le=i({},pe);i(le,de),i(le,Rt);var Me=i({},he);i(Me,Ot);var un=function(e){var t=ye(e);t&&t.tagName||(t={namespaceURI:B,tagName:"template"});var r=J(e.tagName),l=J(t.tagName);return!!De[e.namespaceURI]&&(e.namespaceURI===ie?t.namespaceURI===_?r==="svg":t.namespaceURI===ae?r==="svg"&&(l==="annotation-xml"||ot[l]):Boolean(le[r]):e.namespaceURI===ae?t.namespaceURI===_?r==="math":t.namespaceURI===ie?r==="math"&&at[l]:Boolean(Me[r]):e.namespaceURI===_?!(t.namespaceURI===ie&&!at[l])&&!(t.namespaceURI===ae&&!ot[l])&&!Me[r]&&(cn[r]||!le[r]):!(F!=="application/xhtml+xml"||!De[e.namespaceURI]))},R=function(e){W(o.removed,{element:e});try{e.parentNode.removeChild(e)}catch(t){try{e.outerHTML=We}catch(r){e.remove()}}},Ce=function(e,t){try{W(o.removed,{attribute:t.getAttributeNode(e),from:t})}catch(r){W(o.removed,{attribute:null,from:t})}if(t.removeAttribute(e),e==="is"&&!p[e])if(H||re)try{R(t)}catch(r){}else try{t.setAttribute(e,"")}catch(r){}},it=function(e){var t,r;if(xe)e="<remove></remove>"+e;else{var l=St(e,/^[\r\n\t ]+/);r=l&&l[0]}F==="application/xhtml+xml"&&B===_&&(e='<html xmlns="http://www.w3.org/1999/xhtml"><head></head><body>'+e+"</body></html>");var d=S?S.createHTML(e):e;if(B===_)try{t=new qt().parseFromString(d,F)}catch(g){}if(!t||!t.documentElement){t=ve.createDocument(B,"template",null);try{t.documentElement.innerHTML=Oe?"":d}catch(g){}}var h=t.body||t.documentElement;return e&&r&&h.insertBefore(c.createTextNode(r),h.childNodes[0]||null),B===_?Xt.call(t,C?"html":"body")[0]:C?t.documentElement:h},lt=function(e){return Vt.call(e.ownerDocument||e,e,I.SHOW_ELEMENT|I.SHOW_COMMENT|I.SHOW_TEXT,null,!1)},sn=function(e){return e instanceof Wt&&(typeof e.nodeName!="string"||typeof e.textContent!="string"||typeof e.removeChild!="function"||!(e.attributes instanceof Gt)||typeof e.removeAttribute!="function"||typeof e.setAttribute!="function"||typeof e.namespaceURI!="string"||typeof e.insertBefore!="function"||typeof e.hasChildNodes!="function")},$=function(e){return x(M)==="object"?e instanceof M:e&&x(e)==="object"&&typeof e.nodeType=="number"&&typeof e.nodeName=="string"},O=function(e,t,r){k[e]&&wt(k[e],function(l){l.call(o,t,r,j)})},ct=function(e){var t;if(O("beforeSanitizeElements",e,null),sn(e)||b(/[\u0080-\uFFFF]/,e.nodeName))return R(e),!0;var r=m(e.nodeName);if(O("uponSanitizeElement",e,{tagName:r,allowedTags:f}),e.hasChildNodes()&&!$(e.firstElementChild)&&(!$(e.content)||!$(e.content.firstElementChild))&&b(/<[/\w]/g,e.innerHTML)&&b(/<[/\w]/g,e.textContent)||r==="select"&&b(/<template/i,e.innerHTML))return R(e),!0;if(!f[r]||q[r]){if(!q[r]&&st(r)&&(s.tagNameCheck instanceof RegExp&&b(s.tagNameCheck,r)||s.tagNameCheck instanceof Function&&s.tagNameCheck(r)))return!1;if(_e&&!P[r]){var l=ye(e)||e.parentNode,d=Kt(e)||e.childNodes;if(d&&l)for(var h=d.length,g=h-1;g>=0;--g)l.insertBefore(Yt(d[g],!0),$t(e))}return R(e),!0}return e instanceof Q&&!un(e)?(R(e),!0):r!=="noscript"&&r!=="noembed"||!b(/<\/no(script|embed)/i,e.innerHTML)?(U&&e.nodeType===3&&(t=e.textContent,t=w(t,Te," "),t=w(t,Ne," "),t=w(t,Ae," "),e.textContent!==t&&(W(o.removed,{element:e.cloneNode()}),e.textContent=t)),O("afterSanitizeElements",e,null),!1):(R(e),!0)},ut=function(e,t,r){if(Xe&&(t==="id"||t==="name")&&(r in c||r in ln))return!1;if(!(Se&&!we[t]&&b(Qt,t))){if(!(Ve&&b(en,t))){if(!p[t]||we[t]){if(!(st(e)&&(s.tagNameCheck instanceof RegExp&&b(s.tagNameCheck,e)||s.tagNameCheck instanceof Function&&s.tagNameCheck(e))&&(s.attributeNameCheck instanceof RegExp&&b(s.attributeNameCheck,t)||s.attributeNameCheck instanceof Function&&s.attributeNameCheck(t))||t==="is"&&s.allowCustomizedBuiltInElements&&(s.tagNameCheck instanceof RegExp&&b(s.tagNameCheck,r)||s.tagNameCheck instanceof Function&&s.tagNameCheck(r))))return!1}else if(!Re[t]){if(!b(Ee,w(r,Ye,""))){if((t!=="src"&&t!=="xlink:href"&&t!=="href"||e==="script"||kt(r,"data:")!==0||!et[e])&&!(Je&&!b(tn,w(r,Ye,"")))){if(r)return!1}}}}}return!0},st=function(e){return e.indexOf("-")>0},mt=function(e){var t,r,l,d;O("beforeSanitizeAttributes",e,null);var h=e.attributes;if(h){var g={attrName:"",attrValue:"",keepAttr:!0,allowedAttributes:p};for(d=h.length;d--;){t=h[d];var K=t,D=K.name,v=K.namespaceURI;if(r=D==="value"?t.value:xt(t.value),l=m(D),g.attrName=l,g.attrValue=r,g.keepAttr=!0,g.forceKeepAttr=void 0,O("uponSanitizeAttribute",e,g),r=g.attrValue,!g.forceKeepAttr&&(Ce(D,e),g.keepAttr))if(b(/\/>/i,r))Ce(D,e);else{U&&(r=w(r,Te," "),r=w(r,Ne," "),r=w(r,Ae," "));var ft=m(e.nodeName);if(ut(ft,l,r)){if(!Ze||l!=="id"&&l!=="name"||(Ce(D,e),r=nn+r),S&&x(ee)==="object"&&typeof ee.getAttributeType=="function"&&!v)switch(ee.getAttributeType(ft,l)){case"TrustedHTML":r=S.createHTML(r);break;case"TrustedScriptURL":r=S.createScriptURL(r);break}try{v?e.setAttributeNS(v,D,r):e.setAttribute(D,r),Ue(o.removed)}catch(dn){}}}}O("afterSanitizeAttributes",e,null)}},mn=function e(t){var r,l=lt(t);for(O("beforeSanitizeShadowDOM",t,null);r=l.nextNode();)O("uponSanitizeShadowNode",r,null),ct(r)||(r.content instanceof u&&e(r.content),mt(r));O("afterSanitizeShadowDOM",t,null)};return o.sanitize=function(e){var t,r,l,d,h,g=arguments.length>1&&arguments[1]!==void 0?arguments[1]:{};if(Oe=!e,Oe&&(e="<!-->"),typeof e!="string"&&!$(e)){if(typeof e.toString!="function")throw fe("toString is not a function");if(e=e.toString(),typeof e!="string")throw fe("dirty is not a string, aborting")}if(!o.isSupported){if(x(n.toStaticHTML)==="object"||typeof n.toStaticHTML=="function"){if(typeof e=="string")return n.toStaticHTML(e);if($(e))return n.toStaticHTML(e.outerHTML)}return e}if(ke||Le(g),o.removed=[],typeof e=="string"&&(Y=!1),Y){if(e.nodeName){var K=m(e.nodeName);if(!f[K]||q[K])throw fe("root node is forbidden and cannot be sanitized in-place")}}else if(e instanceof M)t=it("<!---->"),r=t.ownerDocument.importNode(e,!0),r.nodeType===1&&r.nodeName==="BODY"||r.nodeName==="HTML"?t=r:t.appendChild(r);else{if(!H&&!U&&!C&&e.indexOf("<")===-1)return S&&oe?S.createHTML(e):e;if(t=it(e),!t)return H?null:oe?We:""}t&&xe&&R(t.firstChild);for(var D=lt(Y?e:t);l=D.nextNode();)l.nodeType===3&&l===d||ct(l)||(l.content instanceof u&&mn(l.content),mt(l),d=l);if(d=null,Y)return e;if(H){if(re)for(h=Jt.call(t.ownerDocument);t.firstChild;)h.appendChild(t.firstChild);else h=t;return p.shadowroot&&(h=Zt.call(a,h,!0)),h}var v=C?t.outerHTML:t.innerHTML;return C&&f["!doctype"]&&t.ownerDocument&&t.ownerDocument.doctype&&t.ownerDocument.doctype.name&&b(zt,t.ownerDocument.doctype.name)&&(v="<!DOCTYPE "+t.ownerDocument.doctype.name+`>
`+v),U&&(v=w(v,Te," "),v=w(v,Ne," "),v=w(v,Ae," ")),S&&oe?S.createHTML(v):v},o.setConfig=function(e){Le(e),ke=!0},o.clearConfig=function(){j=null,ke=!1},o.isValidAttribute=function(e,t,r){j||Le({});var l=m(e),d=m(t);return ut(l,d,r)},o.addHook=function(e,t){typeof t=="function"&&(k[e]=k[e]||[],W(k[e],t))},o.removeHook=function(e){if(k[e])return Ue(k[e])},o.removeHooks=function(e){k[e]&&(k[e]=[])},o.removeAllHooks=function(){k={}},o}var jt=je();return jt})}}]);