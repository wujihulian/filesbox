(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[46163],{60618:function(g,o,l){"use strict";l.r(o),l.d(o,{sieve:function(){return a}});function f(n){for(var e={},i=n.split(" "),r=0;r<i.length;++r)e[i[r]]=!0;return e}var d=f("if elsif else stop require"),k=f("true false not");function t(n,e){var i=n.next();if(i=="/"&&n.eat("*"))return e.tokenize=p,p(n,e);if(i==="#")return n.skipToEnd(),"comment";if(i=='"')return e.tokenize=h(i),e.tokenize(n,e);if(i=="(")return e._indent.push("("),e._indent.push("{"),null;if(i==="{")return e._indent.push("{"),null;if(i==")"&&(e._indent.pop(),e._indent.pop()),i==="}")return e._indent.pop(),null;if(i==","||i==";"||/[{}\(\),;]/.test(i))return null;if(/\d/.test(i))return n.eatWhile(/[\d]/),n.eat(/[KkMmGg]/),"number";if(i==":")return n.eatWhile(/[a-zA-Z_]/),n.eatWhile(/[a-zA-Z0-9_]/),"operator";n.eatWhile(/\w/);var r=n.current();return r=="text"&&n.eat(":")?(e.tokenize=c,"string"):d.propertyIsEnumerable(r)?"keyword":k.propertyIsEnumerable(r)?"atom":null}function c(n,e){return e._multiLineString=!0,n.sol()?(n.next()=="."&&n.eol()&&(e._multiLineString=!1,e.tokenize=t),"string"):(n.eatSpace(),n.peek()=="#"?(n.skipToEnd(),"comment"):(n.skipToEnd(),"string"))}function p(n,e){for(var i=!1,r;(r=n.next())!=null;){if(i&&r=="/"){e.tokenize=t;break}i=r=="*"}return"comment"}function h(n){return function(e,i){for(var r=!1,u;(u=e.next())!=null&&!(u==n&&!r);)r=!r&&u=="\\";return r||(i.tokenize=t),"string"}}const a={name:"sieve",startState:function(n){return{tokenize:t,baseIndent:n||0,_indent:[]}},token:function(n,e){return n.eatSpace()?null:(e.tokenize||t)(n,e)},indent:function(n,e,i){var r=n._indent.length;return e&&e[0]=="}"&&r--,r<0&&(r=0),r*i.unit},languageData:{indentOnInput:/^\s*\}$/}}}}]);