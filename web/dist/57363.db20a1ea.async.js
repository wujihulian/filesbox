(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[57363],{57363:function(v,s,c){"use strict";c.r(s),c.d(s,{velocity:function(){return g}});function u(n){for(var e={},i=n.split(" "),r=0;r<i.length;++r)e[i[r]]=!0;return e}var k=u("#end #else #break #stop #[[ #]] #{end} #{else} #{break} #{stop}"),a=u("#if #elseif #foreach #set #include #parse #macro #define #evaluate #{if} #{elseif} #{foreach} #{set} #{include} #{parse} #{macro} #{define} #{evaluate}"),p=u("$foreach.count $foreach.hasNext $foreach.first $foreach.last $foreach.topmost $foreach.parent.count $foreach.parent.hasNext $foreach.parent.first $foreach.parent.last $foreach.parent $velocityCount $!bodyContent $bodyContent"),h=/[+\-*&%=<>!?:\/|]/;function o(n,e,i){return e.tokenize=i,i(n,e)}function t(n,e){var i=e.beforeParams;e.beforeParams=!1;var r=n.next();if(r=="'"&&!e.inString&&e.inParams)return e.lastTokenWasBuiltin=!1,o(n,e,b(r));if(r=='"'){if(e.lastTokenWasBuiltin=!1,e.inString)return e.inString=!1,"string";if(e.inParams)return o(n,e,b(r))}else{if(/[\[\]{}\(\),;\.]/.test(r))return r=="("&&i?e.inParams=!0:r==")"&&(e.inParams=!1,e.lastTokenWasBuiltin=!0),null;if(/\d/.test(r))return e.lastTokenWasBuiltin=!1,n.eatWhile(/[\w\.]/),"number";if(r=="#"&&n.eat("*"))return e.lastTokenWasBuiltin=!1,o(n,e,d);if(r=="#"&&n.match(/ *\[ *\[/))return e.lastTokenWasBuiltin=!1,o(n,e,W);if(r=="#"&&n.eat("#"))return e.lastTokenWasBuiltin=!1,n.skipToEnd(),"comment";if(r=="$")return n.eat("!"),n.eatWhile(/[\w\d\$_\.{}-]/),p&&p.propertyIsEnumerable(n.current())?"keyword":(e.lastTokenWasBuiltin=!0,e.beforeParams=!0,"builtin");if(h.test(r))return e.lastTokenWasBuiltin=!1,n.eatWhile(h),"operator";n.eatWhile(/[\w\$_{}@]/);var l=n.current();return k&&k.propertyIsEnumerable(l)?"keyword":a&&a.propertyIsEnumerable(l)||n.current().match(/^#@?[a-z0-9_]+ *$/i)&&n.peek()=="("&&!(a&&a.propertyIsEnumerable(l.toLowerCase()))?(e.beforeParams=!0,e.lastTokenWasBuiltin=!1,"keyword"):e.inString?(e.lastTokenWasBuiltin=!1,"string"):n.pos>l.length&&n.string.charAt(n.pos-l.length-1)=="."&&e.lastTokenWasBuiltin?"builtin":(e.lastTokenWasBuiltin=!1,null)}}function b(n){return function(e,i){for(var r=!1,l,f=!1;(l=e.next())!=null;){if(l==n&&!r){f=!0;break}if(n=='"'&&e.peek()=="$"&&!r){i.inString=!0,f=!0;break}r=!r&&l=="\\"}return f&&(i.tokenize=t),"string"}}function d(n,e){for(var i=!1,r;r=n.next();){if(r=="#"&&i){e.tokenize=t;break}i=r=="*"}return"comment"}function W(n,e){for(var i=0,r;r=n.next();){if(r=="#"&&i==2){e.tokenize=t;break}r=="]"?i++:r!=" "&&(i=0)}return"meta"}const g={name:"velocity",startState:function(){return{tokenize:t,beforeParams:!1,inParams:!1,inString:!1,lastTokenWasBuiltin:!1}},token:function(n,e){return n.eatSpace()?null:e.tokenize(n,e)},languageData:{commentTokens:{line:"##",block:{open:"#*",close:"*#"}}}}}}]);