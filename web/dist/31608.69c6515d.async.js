(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[31608],{31608:function(m,i,s){"use strict";s.r(i),s.d(i,{mscgen:function(){return u},msgenny:function(){return a},xu:function(){return l}});function c(t){return{name:"mscgen",startState:b,copyState:d,token:x(t),languageData:{commentTokens:{line:"#",block:{open:"/*",close:"*/"}}}}}const u=c({keywords:["msc"],options:["hscale","width","arcgradient","wordwraparcs"],constants:["true","false","on","off"],attributes:["label","idurl","id","url","linecolor","linecolour","textcolor","textcolour","textbgcolor","textbgcolour","arclinecolor","arclinecolour","arctextcolor","arctextcolour","arctextbgcolor","arctextbgcolour","arcskip"],brackets:["\\{","\\}"],arcsWords:["note","abox","rbox","box"],arcsOthers:["\\|\\|\\|","\\.\\.\\.","---","--","<->","==","<<=>>","<=>","\\.\\.","<<>>","::","<:>","->","=>>","=>",">>",":>","<-","<<=","<=","<<","<:","x-","-x"],singlecomment:["//","#"],operators:["="]}),a=c({keywords:null,options:["hscale","width","arcgradient","wordwraparcs","wordwrapentities","watermark"],constants:["true","false","on","off","auto"],attributes:null,brackets:["\\{","\\}"],arcsWords:["note","abox","rbox","box","alt","else","opt","break","par","seq","strict","neg","critical","ignore","consider","assert","loop","ref","exc"],arcsOthers:["\\|\\|\\|","\\.\\.\\.","---","--","<->","==","<<=>>","<=>","\\.\\.","<<>>","::","<:>","->","=>>","=>",">>",":>","<-","<<=","<=","<<","<:","x-","-x"],singlecomment:["//","#"],operators:["="]}),l=c({keywords:["msc","xu"],options:["hscale","width","arcgradient","wordwraparcs","wordwrapentities","watermark"],constants:["true","false","on","off","auto"],attributes:["label","idurl","id","url","linecolor","linecolour","textcolor","textcolour","textbgcolor","textbgcolour","arclinecolor","arclinecolour","arctextcolor","arctextcolour","arctextbgcolor","arctextbgcolour","arcskip","title","deactivate","activate","activation"],brackets:["\\{","\\}"],arcsWords:["note","abox","rbox","box","alt","else","opt","break","par","seq","strict","neg","critical","ignore","consider","assert","loop","ref","exc"],arcsOthers:["\\|\\|\\|","\\.\\.\\.","---","--","<->","==","<<=>>","<=>","\\.\\.","<<>>","::","<:>","->","=>>","=>",">>",":>","<-","<<=","<=","<<","<:","x-","-x"],singlecomment:["//","#"],operators:["="]});function o(t){return new RegExp("^\\b("+t.join("|")+")\\b","i")}function n(t){return new RegExp("^(?:"+t.join("|")+")","i")}function b(){return{inComment:!1,inString:!1,inAttributeList:!1,inScript:!1}}function d(t){return{inComment:t.inComment,inString:t.inString,inAttributeList:t.inAttributeList,inScript:t.inScript}}function x(t){return function(r,e){if(r.match(n(t.brackets),!0,!0))return"bracket";if(!e.inComment){if(r.match(/\/\*[^\*\/]*/,!0,!0))return e.inComment=!0,"comment";if(r.match(n(t.singlecomment),!0,!0))return r.skipToEnd(),"comment"}if(e.inComment)return r.match(/[^\*\/]*\*\//,!0,!0)?e.inComment=!1:r.skipToEnd(),"comment";if(!e.inString&&r.match(/\"(\\\"|[^\"])*/,!0,!0))return e.inString=!0,"string";if(e.inString)return r.match(/[^\"]*\"/,!0,!0)?e.inString=!1:r.skipToEnd(),"string";if(!!t.keywords&&r.match(o(t.keywords),!0,!0)||r.match(o(t.options),!0,!0)||r.match(o(t.arcsWords),!0,!0)||r.match(n(t.arcsOthers),!0,!0))return"keyword";if(!!t.operators&&r.match(n(t.operators),!0,!0))return"operator";if(!!t.constants&&r.match(n(t.constants),!0,!0))return"variable";if(!t.inAttributeList&&!!t.attributes&&r.match("[",!0,!0))return t.inAttributeList=!0,"bracket";if(t.inAttributeList){if(t.attributes!==null&&r.match(o(t.attributes),!0,!0))return"attribute";if(r.match("]",!0,!0))return t.inAttributeList=!1,"bracket"}return r.next(),null}}}}]);