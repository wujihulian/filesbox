(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[63460],{63460:function(p,x,k){p=k.nmd(p),ace.define("ace/mode/smithy_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(a,h,f){"use strict";var m=a("../lib/oop"),d=a("./text_highlight_rules").TextHighlightRules,s=function(){this.$rules={start:[{include:"#comment"},{token:["meta.keyword.statement.smithy","variable.other.smithy","text","keyword.operator.smithy"],regex:/^(\$)(\s+.+)(\s*)(=)/},{token:["keyword.statement.smithy","text","entity.name.type.namespace.smithy"],regex:/^(namespace)(\s+)([A-Z-a-z0-9_\.#$-]+)/},{token:["keyword.statement.smithy","text","keyword.statement.smithy","text","entity.name.type.smithy"],regex:/^(use)(\s+)(shape|trait)(\s+)([A-Z-a-z0-9_\.#$-]+)\b/},{token:["keyword.statement.smithy","variable.other.smithy","text","keyword.operator.smithy"],regex:/^(metadata)(\s+.+)(\s*)(=)/},{token:["keyword.statement.smithy","text","entity.name.type.smithy"],regex:/^(apply|byte|short|integer|long|float|double|bigInteger|bigDecimal|boolean|blob|string|timestamp|service|resource|trait|list|map|set|structure|union|document)(\s+)([A-Z-a-z0-9_\.#$-]+)\b/},{token:["keyword.operator.smithy","text","entity.name.type.smithy","text","text","support.function.smithy","text","text","support.function.smithy"],regex:/^(operation)(\s+)([A-Z-a-z0-9_\.#$-]+)(\(.*\))(?:(\s*)(->)(\s*[A-Z-a-z0-9_\.#$-]+))?(?:(\s+)(errors))?/},{include:"#trait"},{token:["support.type.property-name.smithy","punctuation.separator.dictionary.pair.smithy"],regex:/([A-Z-a-z0-9_\.#$-]+)(:)/},{include:"#value"},{token:"keyword.other.smithy",regex:/\->/}],"#comment":[{include:"#doc_comment"},{include:"#line_comment"}],"#doc_comment":[{token:"comment.block.documentation.smithy",regex:/\/\/\/.*/}],"#line_comment":[{token:"comment.line.double-slash.smithy",regex:/\/\/.*/}],"#trait":[{token:["punctuation.definition.annotation.smithy","storage.type.annotation.smithy"],regex:/(@)([0-9a-zA-Z\.#-]+)/},{token:["punctuation.definition.annotation.smithy","punctuation.definition.object.end.smithy","meta.structure.smithy"],regex:/(@)([0-9a-zA-Z\.#-]+)(\()/,push:[{token:"punctuation.definition.object.end.smithy",regex:/\)/,next:"pop"},{include:"#value"},{include:"#object_inner"},{defaultToken:"meta.structure.smithy"}]}],"#value":[{include:"#constant"},{include:"#number"},{include:"#string"},{include:"#array"},{include:"#object"}],"#array":[{token:"punctuation.definition.array.begin.smithy",regex:/\[/,push:[{token:"punctuation.definition.array.end.smithy",regex:/\]/,next:"pop"},{include:"#comment"},{include:"#value"},{token:"punctuation.separator.array.smithy",regex:/,/},{token:"invalid.illegal.expected-array-separator.smithy",regex:/[^\s\]]/},{defaultToken:"meta.structure.array.smithy"}]}],"#constant":[{token:"constant.language.smithy",regex:/\b(?:true|false|null)\b/}],"#number":[{token:"constant.numeric.smithy",regex:/-?(?:0|[1-9]\d*)(?:(?:\.\d+)?(?:[eE][+-]?\d+)?)?/}],"#object":[{token:"punctuation.definition.dictionary.begin.smithy",regex:/\{/,push:[{token:"punctuation.definition.dictionary.end.smithy",regex:/\}/,next:"pop"},{include:"#trait"},{include:"#object_inner"},{defaultToken:"meta.structure.dictionary.smithy"}]}],"#object_inner":[{include:"#comment"},{include:"#string_key"},{token:"punctuation.separator.dictionary.key-value.smithy",regex:/:/,push:[{token:"punctuation.separator.dictionary.pair.smithy",regex:/,|(?=\})/,next:"pop"},{include:"#value"},{token:"invalid.illegal.expected-dictionary-separator.smithy",regex:/[^\s,]/},{defaultToken:"meta.structure.dictionary.value.smithy"}]},{token:"invalid.illegal.expected-dictionary-separator.smithy",regex:/[^\s\}]/}],"#string_key":[{include:"#identifier_key"},{include:"#dquote_key"},{include:"#squote_key"}],"#identifier_key":[{token:"support.type.property-name.smithy",regex:/[A-Z-a-z0-9_\.#$-]+/}],"#dquote_key":[{include:"#dquote"}],"#squote_key":[{include:"#squote"}],"#string":[{include:"#textblock"},{include:"#dquote"},{include:"#squote"},{include:"#identifier"}],"#textblock":[{token:"punctuation.definition.string.begin.smithy",regex:/"""/,push:[{token:"punctuation.definition.string.end.smithy",regex:/"""/,next:"pop"},{token:"constant.character.escape.smithy",regex:/\\./},{defaultToken:"string.quoted.double.smithy"}]}],"#dquote":[{token:"punctuation.definition.string.begin.smithy",regex:/"/,push:[{token:"punctuation.definition.string.end.smithy",regex:/"/,next:"pop"},{token:"constant.character.escape.smithy",regex:/\\./},{defaultToken:"string.quoted.double.smithy"}]}],"#squote":[{token:"punctuation.definition.string.begin.smithy",regex:/'/,push:[{token:"punctuation.definition.string.end.smithy",regex:/'/,next:"pop"},{token:"constant.character.escape.smithy",regex:/\\./},{defaultToken:"string.quoted.single.smithy"}]}],"#identifier":[{token:"storage.type.smithy",regex:/[A-Z-a-z_][A-Z-a-z0-9_\.#$-]*/}]},this.normalizeRules()};s.metaData={name:"Smithy",fileTypes:["smithy"],scopeName:"source.smithy",foldingStartMarker:"(\\{|\\[)\\s*",foldingStopMarker:"\\s*(\\}|\\])"},m.inherits(s,d),h.SmithyHighlightRules=s}),ace.define("ace/mode/matching_brace_outdent",["require","exports","module","ace/range"],function(a,h,f){"use strict";var m=a("../range").Range,d=function(){};(function(){this.checkOutdent=function(s,u){return/^\s+$/.test(s)?/^\s*\}/.test(u):!1},this.autoOutdent=function(s,u){var e=s.getLine(u),i=e.match(/^(\s*\})/);if(!i)return 0;var t=i[1].length,r=s.findMatchingBracket({row:u,column:t});if(!r||r.row==u)return 0;var o=this.$getIndent(s.getLine(r.row));s.replace(new m(u,0,u,t-1),o)},this.$getIndent=function(s){return s.match(/^\s*/)[0]}}).call(d.prototype),h.MatchingBraceOutdent=d}),ace.define("ace/mode/folding/cstyle",["require","exports","module","ace/lib/oop","ace/range","ace/mode/folding/fold_mode"],function(a,h,f){"use strict";var m=a("../../lib/oop"),d=a("../../range").Range,s=a("./fold_mode").FoldMode,u=h.FoldMode=function(e){e&&(this.foldingStartMarker=new RegExp(this.foldingStartMarker.source.replace(/\|[^|]*?$/,"|"+e.start)),this.foldingStopMarker=new RegExp(this.foldingStopMarker.source.replace(/\|[^|]*?$/,"|"+e.end)))};m.inherits(u,s),function(){this.foldingStartMarker=/([\{\[\(])[^\}\]\)]*$|^\s*(\/\*)/,this.foldingStopMarker=/^[^\[\{\(]*([\}\]\)])|^[\s\*]*(\*\/)/,this.singleLineBlockCommentRe=/^\s*(\/\*).*\*\/\s*$/,this.tripleStarBlockCommentRe=/^\s*(\/\*\*\*).*\*\/\s*$/,this.startRegionRe=/^\s*(\/\*|\/\/)#?region\b/,this._getFoldWidgetBase=this.getFoldWidget,this.getFoldWidget=function(e,i,t){var r=e.getLine(t);if(this.singleLineBlockCommentRe.test(r)&&!this.startRegionRe.test(r)&&!this.tripleStarBlockCommentRe.test(r))return"";var o=this._getFoldWidgetBase(e,i,t);return!o&&this.startRegionRe.test(r)?"start":o},this.getFoldWidgetRange=function(e,i,t,r){var o=e.getLine(t);if(this.startRegionRe.test(o))return this.getCommentRegionBlock(e,o,t);var n=o.match(this.foldingStartMarker);if(n){var c=n.index;if(n[1])return this.openingBracketBlock(e,n[1],t,c);var l=e.getCommentFoldRange(t,c+n[0].length,1);return l&&!l.isMultiLine()&&(r?l=this.getSectionRange(e,t):i!="all"&&(l=null)),l}if(i!=="markbegin"){var n=o.match(this.foldingStopMarker);if(n){var c=n.index+n[0].length;return n[1]?this.closingBracketBlock(e,n[1],t,c):e.getCommentFoldRange(t,c,-1)}}},this.getSectionRange=function(e,i){var t=e.getLine(i),r=t.search(/\S/),o=i,n=t.length;i=i+1;for(var c=i,l=e.getLength();++i<l;){t=e.getLine(i);var y=t.search(/\S/);if(y!==-1){if(r>y)break;var g=this.getFoldWidgetRange(e,"all",i);if(g){if(g.start.row<=o)break;if(g.isMultiLine())i=g.end.row;else if(r==y)break}c=i}}return new d(o,n,c,e.getLine(c).length)},this.getCommentRegionBlock=function(e,i,t){for(var r=i.search(/\s*$/),o=e.getLength(),n=t,c=/^\s*(?:\/\*|\/\/|--)#?(end)?region\b/,l=1;++t<o;){i=e.getLine(t);var y=c.exec(i);if(!!y&&(y[1]?l--:l++,!l))break}var g=t;if(g>n)return new d(n,r,g,i.length)}}.call(u.prototype)}),ace.define("ace/mode/smithy",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/smithy_highlight_rules","ace/mode/matching_brace_outdent","ace/mode/behaviour/cstyle","ace/mode/folding/cstyle"],function(a,h,f){"use strict";var m=a("../lib/oop"),d=a("./text").Mode,s=a("./smithy_highlight_rules").SmithyHighlightRules,u=a("./matching_brace_outdent").MatchingBraceOutdent,e=a("./behaviour/cstyle").CstyleBehaviour,i=a("./folding/cstyle").FoldMode,t=function(){this.HighlightRules=s,this.$outdent=new u,this.$behaviour=new e,this.foldingRules=new i};m.inherits(t,d),function(){this.lineCommentStart="//",this.$quotes={'"':'"'},this.checkOutdent=function(r,o,n){return this.$outdent.checkOutdent(o,n)},this.autoOutdent=function(r,o,n){this.$outdent.autoOutdent(o,n)},this.$id="ace/mode/smithy"}.call(t.prototype),h.Mode=t}),function(){ace.require(["ace/mode/smithy"],function(a){p&&(p.exports=a)})}()}}]);