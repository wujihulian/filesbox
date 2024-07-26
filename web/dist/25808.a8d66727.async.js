(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[25808],{25808:function(p,x,v){p=v.nmd(p),ace.define("ace/mode/doc_comment_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(r,d,k){"use strict";var m=r("../lib/oop"),s=r("./text_highlight_rules").TextHighlightRules,o=function(){this.$rules={start:[{token:"comment.doc.tag",regex:"@[\\w\\d_]+"},o.getTagRule(),{defaultToken:"comment.doc",caseInsensitive:!0}]}};m.inherits(o,s),o.getTagRule=function(l){return{token:"comment.doc.tag.storage.type",regex:"\\b(?:TODO|FIXME|XXX|HACK)\\b"}},o.getStartRule=function(l){return{token:"comment.doc",regex:"\\/\\*(?=\\*)",next:l}},o.getEndRule=function(l){return{token:"comment.doc",regex:"\\*\\/",next:l}},d.DocCommentHighlightRules=o}),ace.define("ace/mode/haxe_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/doc_comment_highlight_rules","ace/mode/text_highlight_rules"],function(r,d,k){"use strict";var m=r("../lib/oop"),s=r("./doc_comment_highlight_rules").DocCommentHighlightRules,o=r("./text_highlight_rules").TextHighlightRules,l=function(){var e="break|case|cast|catch|class|continue|default|else|enum|extends|for|function|if|implements|import|in|inline|interface|new|override|package|private|public|return|static|super|switch|this|throw|trace|try|typedef|untyped|var|while|Array|Void|Bool|Int|UInt|Float|Dynamic|String|List|Hash|IntHash|Error|Unknown|Type|Std",n="null|true|false",t=this.createKeywordMapper({"variable.language":"this",keyword:e,"constant.language":n},"identifier");this.$rules={start:[{token:"comment",regex:"\\/\\/.*$"},s.getStartRule("doc-start"),{token:"comment",regex:"\\/\\*",next:"comment"},{token:"string.regexp",regex:"[/](?:(?:\\[(?:\\\\]|[^\\]])+\\])|(?:\\\\/|[^\\]/]))*[/]\\w*\\s*(?=[).,;]|$)"},{token:"string",regex:'["](?:(?:\\\\.)|(?:[^"\\\\]))*?["]'},{token:"string",regex:"['](?:(?:\\\\.)|(?:[^'\\\\]))*?[']"},{token:"constant.numeric",regex:"0[xX][0-9a-fA-F]+\\b"},{token:"constant.numeric",regex:"[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"},{token:"constant.language.boolean",regex:"(?:true|false)\\b"},{token:t,regex:"[a-zA-Z_$][a-zA-Z0-9_$]*\\b"},{token:"keyword.operator",regex:"!|\\$|%|&|\\*|\\-\\-|\\-|\\+\\+|\\+|~|===|==|=|!=|!==|<=|>=|<<=|>>=|>>>=|<>|<|>|!|&&|\\|\\||\\?\\:|\\*=|%=|\\+=|\\-=|&=|\\^=|\\b(?:in|instanceof|new|delete|typeof|void)"},{token:"punctuation.operator",regex:"\\?|\\:|\\,|\\;|\\."},{token:"paren.lparen",regex:"[[({<]"},{token:"paren.rparen",regex:"[\\])}>]"},{token:"text",regex:"\\s+"}],comment:[{token:"comment",regex:"\\*\\/",next:"start"},{defaultToken:"comment"}]},this.embedRules(s,"doc-",[s.getEndRule("start")])};m.inherits(l,o),d.HaxeHighlightRules=l}),ace.define("ace/mode/matching_brace_outdent",["require","exports","module","ace/range"],function(r,d,k){"use strict";var m=r("../range").Range,s=function(){};(function(){this.checkOutdent=function(o,l){return/^\s+$/.test(o)?/^\s*\}/.test(l):!1},this.autoOutdent=function(o,l){var e=o.getLine(l),n=e.match(/^(\s*\})/);if(!n)return 0;var t=n[1].length,c=o.findMatchingBracket({row:l,column:t});if(!c||c.row==l)return 0;var i=this.$getIndent(o.getLine(c.row));o.replace(new m(l,0,l,t-1),i)},this.$getIndent=function(o){return o.match(/^\s*/)[0]}}).call(s.prototype),d.MatchingBraceOutdent=s}),ace.define("ace/mode/folding/cstyle",["require","exports","module","ace/lib/oop","ace/range","ace/mode/folding/fold_mode"],function(r,d,k){"use strict";var m=r("../../lib/oop"),s=r("../../range").Range,o=r("./fold_mode").FoldMode,l=d.FoldMode=function(e){e&&(this.foldingStartMarker=new RegExp(this.foldingStartMarker.source.replace(/\|[^|]*?$/,"|"+e.start)),this.foldingStopMarker=new RegExp(this.foldingStopMarker.source.replace(/\|[^|]*?$/,"|"+e.end)))};m.inherits(l,o),function(){this.foldingStartMarker=/([\{\[\(])[^\}\]\)]*$|^\s*(\/\*)/,this.foldingStopMarker=/^[^\[\{\(]*([\}\]\)])|^[\s\*]*(\*\/)/,this.singleLineBlockCommentRe=/^\s*(\/\*).*\*\/\s*$/,this.tripleStarBlockCommentRe=/^\s*(\/\*\*\*).*\*\/\s*$/,this.startRegionRe=/^\s*(\/\*|\/\/)#?region\b/,this._getFoldWidgetBase=this.getFoldWidget,this.getFoldWidget=function(e,n,t){var c=e.getLine(t);if(this.singleLineBlockCommentRe.test(c)&&!this.startRegionRe.test(c)&&!this.tripleStarBlockCommentRe.test(c))return"";var i=this._getFoldWidgetBase(e,n,t);return!i&&this.startRegionRe.test(c)?"start":i},this.getFoldWidgetRange=function(e,n,t,c){var i=e.getLine(t);if(this.startRegionRe.test(i))return this.getCommentRegionBlock(e,i,t);var a=i.match(this.foldingStartMarker);if(a){var g=a.index;if(a[1])return this.openingBracketBlock(e,a[1],t,g);var h=e.getCommentFoldRange(t,g+a[0].length,1);return h&&!h.isMultiLine()&&(c?h=this.getSectionRange(e,t):n!="all"&&(h=null)),h}if(n!=="markbegin"){var a=i.match(this.foldingStopMarker);if(a){var g=a.index+a[0].length;return a[1]?this.closingBracketBlock(e,a[1],t,g):e.getCommentFoldRange(t,g,-1)}}},this.getSectionRange=function(e,n){var t=e.getLine(n),c=t.search(/\S/),i=n,a=t.length;n=n+1;for(var g=n,h=e.getLength();++n<h;){t=e.getLine(n);var u=t.search(/\S/);if(u!==-1){if(c>u)break;var f=this.getFoldWidgetRange(e,"all",n);if(f){if(f.start.row<=i)break;if(f.isMultiLine())n=f.end.row;else if(c==u)break}g=n}}return new s(i,a,g,e.getLine(g).length)},this.getCommentRegionBlock=function(e,n,t){for(var c=n.search(/\s*$/),i=e.getLength(),a=t,g=/^\s*(?:\/\*|\/\/|--)#?(end)?region\b/,h=1;++t<i;){n=e.getLine(t);var u=g.exec(n);if(!!u&&(u[1]?h--:h++,!h))break}var f=t;if(f>a)return new s(a,c,f,n.length)}}.call(l.prototype)}),ace.define("ace/mode/haxe",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/haxe_highlight_rules","ace/mode/matching_brace_outdent","ace/mode/behaviour/cstyle","ace/mode/folding/cstyle"],function(r,d,k){"use strict";var m=r("../lib/oop"),s=r("./text").Mode,o=r("./haxe_highlight_rules").HaxeHighlightRules,l=r("./matching_brace_outdent").MatchingBraceOutdent,e=r("./behaviour/cstyle").CstyleBehaviour,n=r("./folding/cstyle").FoldMode,t=function(){this.HighlightRules=o,this.$outdent=new l,this.$behaviour=new e,this.foldingRules=new n};m.inherits(t,s),function(){this.lineCommentStart="//",this.blockComment={start:"/*",end:"*/"},this.getNextLineIndent=function(c,i,a){var g=this.$getIndent(i),h=this.getTokenizer().getLineTokens(i,c),u=h.tokens;if(u.length&&u[u.length-1].type=="comment")return g;if(c=="start"){var f=i.match(/^.*[\{\(\[]\s*$/);f&&(g+=a)}return g},this.checkOutdent=function(c,i,a){return this.$outdent.checkOutdent(i,a)},this.autoOutdent=function(c,i,a){this.$outdent.autoOutdent(i,a)},this.$id="ace/mode/haxe"}.call(t.prototype),d.Mode=t}),function(){ace.require(["ace/mode/haxe"],function(r){p&&(p.exports=r)})}()}}]);