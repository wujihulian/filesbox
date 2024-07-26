(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[65280],{65280:function(f,v,x){f=x.nmd(f),ace.define("ace/mode/nim_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(s,m,k){"use strict";var p=s("../lib/oop"),g=s("./text_highlight_rules").TextHighlightRules,u=function(){var h=this.createKeywordMapper({variable:"var|let|const",keyword:"assert|parallel|spawn|export|include|from|template|mixin|bind|import|concept|raise|defer|try|finally|except|converter|proc|func|macro|method|and|or|not|xor|shl|shr|div|mod|in|notin|is|isnot|of|static|if|elif|else|case|of|discard|when|return|yield|block|break|while|echo|continue|asm|using|cast|addr|unsafeAddr|type|ref|ptr|do|declared|defined|definedInScope|compiles|sizeOf|is|shallowCopy|getAst|astToStr|spawn|procCall|for|iterator|as","storage.type":"newSeq|int|int8|int16|int32|int64|uint|uint8|uint16|uint32|uint64|float|char|bool|string|set|pointer|float32|float64|enum|object|cstring|array|seq|openArray|varargs|UncheckedArray|tuple|set|distinct|void|auto|openarray|range","support.function":"lock|ze|toU8|toU16|toU32|ord|low|len|high|add|pop|contains|card|incl|excl|dealloc|inc","constant.language":"nil|true|false"},"identifier"),e="(?:0[xX][\\dA-Fa-f][\\dA-Fa-f_]*)",t="(?:[0-9][\\d_]*)",n="(?:0o[0-7][0-7_]*)",r="(?:0[bB][01][01_]*)",i="(?:"+e+"|"+t+"|"+n+"|"+r+")(?:'?[iIuU](?:8|16|32|64)|u)?\\b",o="(?:[eE][+-]?[\\d][\\d_]*)",a="(?:[\\d][\\d_]*(?:[.][\\d](?:[\\d_]*)"+o+"?)|"+o+")",c="(?:"+e+"(?:'(?:(?:[fF](?:32|64)?)|[dD])))|(?:"+a+"|"+t+"|"+n+"|"+r+")(?:'(?:(?:[fF](?:32|64)?)|[dD]))",d=`\\\\([abeprcnlftv\\"']|x[0-9A-Fa-f]{2}|[0-2][0-9]{2}|u[0-9A-Fa-f]{8}|u[0-9A-Fa-f]{4})`,l="[a-zA-Z][a-zA-Z0-9_]*";this.$rules={start:[{token:["identifier","keyword.operator","support.function"],regex:"("+l+")([.]{1})("+l+")(?=\\()"},{token:"paren.lparen",regex:"(\\{\\.)",next:[{token:"paren.rparen",regex:"(\\.\\}|\\})",next:"start"},{include:"methods"},{token:"identifier",regex:l},{token:"punctuation",regex:/[,]/},{token:"keyword.operator",regex:/[=:.]/},{token:"paren.lparen",regex:/[[(]/},{token:"paren.rparen",regex:/[\])]/},{include:"math"},{include:"strings"},{defaultToken:"text"}]},{token:"comment.doc.start",regex:/##\[(?!])/,push:"docBlockComment"},{token:"comment.start",regex:/#\[(?!])/,push:"blockComment"},{token:"comment.doc",regex:"##.*$"},{token:"comment",regex:"#.*$"},{include:"strings"},{token:"string",regex:"'(?:\\\\(?:[abercnlftv]|x[0-9A-Fa-f]{2}|[0-2][0-9]{2}|u[0-9A-Fa-f]{8}|u[0-9A-Fa-f]{4})|.{1})?'"},{include:"methods"},{token:h,regex:"[a-zA-Z][a-zA-Z0-9_]*\\b"},{token:["keyword.operator","text","storage.type"],regex:"([:])(\\s+)("+l+")(?=$|\\)|\\[|,|\\s+=|;|\\s+\\{)"},{token:"paren.lparen",regex:/\[\.|{\||\(\.|\[:|[[({`]/},{token:"paren.rparen",regex:/\.\)|\|}|\.]|[\])}]/},{token:"keyword.operator",regex:/[=+\-*\/<>@$~&%|!?^.:\\]/},{token:"punctuation",regex:/[,;]/},{include:"math"}],blockComment:[{regex:/#\[]/,token:"comment"},{regex:/#\[(?!])/,token:"comment.start",push:"blockComment"},{regex:/]#/,token:"comment.end",next:"pop"},{defaultToken:"comment"}],docBlockComment:[{regex:/##\[]/,token:"comment.doc"},{regex:/##\[(?!])/,token:"comment.doc.start",push:"docBlockComment"},{regex:/]##/,token:"comment.doc.end",next:"pop"},{defaultToken:"comment.doc"}],math:[{token:"constant.float",regex:c},{token:"constant.float",regex:a},{token:"constant.integer",regex:i}],methods:[{token:"support.function",regex:"(\\w+)(?=\\()"}],strings:[{token:"string",regex:"(\\b"+l+')?"""',push:[{token:"string",regex:'"""',next:"pop"},{defaultToken:"string"}]},{token:"string",regex:"\\b"+l+'"(?=.)',push:[{token:"string",regex:'"|$',next:"pop"},{defaultToken:"string"}]},{token:"string",regex:'"',push:[{token:"string",regex:'"|$',next:"pop"},{token:"constant.language.escape",regex:d},{defaultToken:"string"}]}]},this.normalizeRules()};p.inherits(u,g),m.NimHighlightRules=u}),ace.define("ace/mode/folding/cstyle",["require","exports","module","ace/lib/oop","ace/range","ace/mode/folding/fold_mode"],function(s,m,k){"use strict";var p=s("../../lib/oop"),g=s("../../range").Range,u=s("./fold_mode").FoldMode,h=m.FoldMode=function(e){e&&(this.foldingStartMarker=new RegExp(this.foldingStartMarker.source.replace(/\|[^|]*?$/,"|"+e.start)),this.foldingStopMarker=new RegExp(this.foldingStopMarker.source.replace(/\|[^|]*?$/,"|"+e.end)))};p.inherits(h,u),function(){this.foldingStartMarker=/([\{\[\(])[^\}\]\)]*$|^\s*(\/\*)/,this.foldingStopMarker=/^[^\[\{\(]*([\}\]\)])|^[\s\*]*(\*\/)/,this.singleLineBlockCommentRe=/^\s*(\/\*).*\*\/\s*$/,this.tripleStarBlockCommentRe=/^\s*(\/\*\*\*).*\*\/\s*$/,this.startRegionRe=/^\s*(\/\*|\/\/)#?region\b/,this._getFoldWidgetBase=this.getFoldWidget,this.getFoldWidget=function(e,t,n){var r=e.getLine(n);if(this.singleLineBlockCommentRe.test(r)&&!this.startRegionRe.test(r)&&!this.tripleStarBlockCommentRe.test(r))return"";var i=this._getFoldWidgetBase(e,t,n);return!i&&this.startRegionRe.test(r)?"start":i},this.getFoldWidgetRange=function(e,t,n,r){var i=e.getLine(n);if(this.startRegionRe.test(i))return this.getCommentRegionBlock(e,i,n);var o=i.match(this.foldingStartMarker);if(o){var a=o.index;if(o[1])return this.openingBracketBlock(e,o[1],n,a);var c=e.getCommentFoldRange(n,a+o[0].length,1);return c&&!c.isMultiLine()&&(r?c=this.getSectionRange(e,n):t!="all"&&(c=null)),c}if(t!=="markbegin"){var o=i.match(this.foldingStopMarker);if(o){var a=o.index+o[0].length;return o[1]?this.closingBracketBlock(e,o[1],n,a):e.getCommentFoldRange(n,a,-1)}}},this.getSectionRange=function(e,t){var n=e.getLine(t),r=n.search(/\S/),i=t,o=n.length;t=t+1;for(var a=t,c=e.getLength();++t<c;){n=e.getLine(t);var d=n.search(/\S/);if(d!==-1){if(r>d)break;var l=this.getFoldWidgetRange(e,"all",t);if(l){if(l.start.row<=i)break;if(l.isMultiLine())t=l.end.row;else if(r==d)break}a=t}}return new g(i,o,a,e.getLine(a).length)},this.getCommentRegionBlock=function(e,t,n){for(var r=t.search(/\s*$/),i=e.getLength(),o=n,a=/^\s*(?:\/\*|\/\/|--)#?(end)?region\b/,c=1;++n<i;){t=e.getLine(n);var d=a.exec(t);if(!!d&&(d[1]?c--:c++,!c))break}var l=n;if(l>o)return new g(o,r,l,t.length)}}.call(h.prototype)}),ace.define("ace/mode/nim",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/nim_highlight_rules","ace/mode/folding/cstyle"],function(s,m,k){"use strict";var p=s("../lib/oop"),g=s("./text").Mode,u=s("./nim_highlight_rules").NimHighlightRules,h=s("./folding/cstyle").FoldMode,e=function(){g.call(this),this.HighlightRules=u,this.foldingRules=new h,this.$behaviour=this.$defaultBehaviour};p.inherits(e,g),function(){this.lineCommentStart="#",this.blockComment={start:"#[",end:"]#",nestable:!0},this.$id="ace/mode/nim"}.call(e.prototype),m.Mode=e}),function(){ace.require(["ace/mode/nim"],function(s){f&&(f.exports=s)})}()}}]);