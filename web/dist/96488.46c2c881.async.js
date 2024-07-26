(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[96488],{96488:function(_,x,k){_=k.nmd(_),ace.define("ace/mode/doc_comment_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(n,h,p){"use strict";var d=n("../lib/oop"),g=n("./text_highlight_rules").TextHighlightRules,a=function(){this.$rules={start:[{token:"comment.doc.tag",regex:"@[\\w\\d_]+"},a.getTagRule(),{defaultToken:"comment.doc",caseInsensitive:!0}]}};d.inherits(a,g),a.getTagRule=function(c){return{token:"comment.doc.tag.storage.type",regex:"\\b(?:TODO|FIXME|XXX|HACK)\\b"}},a.getStartRule=function(c){return{token:"comment.doc",regex:"\\/\\*(?=\\*)",next:c}},a.getEndRule=function(c){return{token:"comment.doc",regex:"\\*\\/",next:c}},h.DocCommentHighlightRules=a}),ace.define("ace/mode/rust_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules","ace/mode/doc_comment_highlight_rules"],function(n,h,p){"use strict";var d=n("../lib/oop"),g=n("./text_highlight_rules").TextHighlightRules,a=n("./doc_comment_highlight_rules").DocCommentHighlightRules,c=/\\(?:[nrt0'"\\]|x[\da-fA-F]{2}|u\{[\da-fA-F]{6}\})/.source,e=/[a-zA-Z_\xa1-\uffff][a-zA-Z0-9_\xa1-\uffff]*/.source,t=function(){var o=this.createKeywordMapper({"keyword.source.rust":"abstract|alignof|as|async|await|become|box|break|catch|continue|const|crate|default|do|dyn|else|enum|extern|for|final|if|impl|in|let|loop|macro|match|mod|move|mut|offsetof|override|priv|proc|pub|pure|ref|return|self|sizeof|static|struct|super|trait|type|typeof|union|unsafe|unsized|use|virtual|where|while|yield|try","storage.type.source.rust":"Self|isize|usize|char|bool|u8|u16|u32|u64|u128|f16|f32|f64|i8|i16|i32|i64|i128|str|option|either|c_float|c_double|c_void|FILE|fpos_t|DIR|dirent|c_char|c_schar|c_uchar|c_short|c_ushort|c_int|c_uint|c_long|c_ulong|size_t|ptrdiff_t|clock_t|time_t|c_longlong|c_ulonglong|intptr_t|uintptr_t|off_t|dev_t|ino_t|pid_t|mode_t|ssize_t","constant.language.source.rust":"true|false|Some|None|Ok|Err|FALSE|TRUE","support.constant.source.rust":"EXIT_FAILURE|EXIT_SUCCESS|RAND_MAX|EOF|SEEK_SET|SEEK_CUR|SEEK_END|_IOFBF|_IONBF|_IOLBF|BUFSIZ|FOPEN_MAX|FILENAME_MAX|L_tmpnam|TMP_MAX|O_RDONLY|O_WRONLY|O_RDWR|O_APPEND|O_CREAT|O_EXCL|O_TRUNC|S_IFIFO|S_IFCHR|S_IFBLK|S_IFDIR|S_IFREG|S_IFMT|S_IEXEC|S_IWRITE|S_IREAD|S_IRWXU|S_IXUSR|S_IWUSR|S_IRUSR|F_OK|R_OK|W_OK|X_OK|STDIN_FILENO|STDOUT_FILENO|STDERR_FILENO","constant.language":"macro_rules|mac_variant"},"identifier");this.$rules={start:[{token:"variable.other.source.rust",regex:"'"+e+"(?![\\'])"},{token:"string.quoted.single.source.rust",regex:"'(?:[^'\\\\]|"+c+")'"},{token:"identifier",regex:"r#"+e+"\\b"},{stateName:"bracketedComment",onMatch:function(i,s,r){return r.unshift(this.next,i.length-1,s),"string.quoted.raw.source.rust"},regex:/r#*"/,next:[{onMatch:function(i,s,r){var u="string.quoted.raw.source.rust";return i.length>=r[1]?(i.length>r[1]&&(u="invalid"),r.shift(),r.shift(),this.next=r.shift()):this.next="",u},regex:/"#*/,next:"start"},{defaultToken:"string.quoted.raw.source.rust"}]},{token:"string.quoted.double.source.rust",regex:'"',push:[{token:"string.quoted.double.source.rust",regex:'"',next:"pop"},{token:"constant.character.escape.source.rust",regex:c},{defaultToken:"string.quoted.double.source.rust"}]},{token:["keyword.source.rust","text","entity.name.function.source.rust","punctuation"],regex:"\\b(fn)(\\s+)((?:r#)?"+e+")(<)",push:"generics"},{token:["keyword.source.rust","text","entity.name.function.source.rust"],regex:"\\b(fn)(\\s+)((?:r#)?"+e+")"},{token:["support.constant","punctuation"],regex:"("+e+"::)(<)",push:"generics"},{token:"support.constant",regex:e+"::"},{token:"variable.language.source.rust",regex:"\\bself\\b"},a.getStartRule("doc-start"),{token:"comment.line.doc.source.rust",regex:"///.*$"},{token:"comment.line.doc.source.rust",regex:"//!.*$"},{token:"comment.line.double-dash.source.rust",regex:"//.*$"},{token:"comment.start.block.source.rust",regex:"/\\*",stateName:"comment",push:[{token:"comment.start.block.source.rust",regex:"/\\*",push:"comment"},{token:"comment.end.block.source.rust",regex:"\\*/",next:"pop"},{defaultToken:"comment.block.source.rust"}]},{token:["keyword.source.rust","identifier","punctuaction"],regex:"(?:(impl)|("+e+"))(<)",stateName:"generics",push:[{token:"punctuaction",regex:"<",push:"generics"},{token:"variable.other.source.rust",regex:"'"+e+"(?![\\'])"},{token:"storage.type.source.rust",regex:"\\b(u8|u16|u32|u64|u128|usize|i8|i16|i32|i64|i128|isize|char|bool)\\b"},{token:"punctuation.operator",regex:"[,:]"},{token:"keyword",regex:"\\b(?:const|dyn)\\b"},{token:"punctuation",regex:">",next:"pop"},{token:"paren.lparen",regex:"[(]"},{token:"paren.rparen",regex:"[)]"},{token:"identifier",regex:"\\b"+e+"\\b"},{token:"keyword.operator",regex:"="}]},{token:o,regex:e},{token:"keyword.operator",regex:/\$|[-=]>|[-+%^=!&|<>]=?|[*/](?![*/])=?/},{token:"punctuation.operator",regex:/[?:,;.]/},{token:"paren.lparen",regex:/[\[({]/},{token:"paren.rparen",regex:/[\])}]/},{token:"meta.preprocessor.source.rust",regex:"\\b\\w\\(\\w\\)*!|#\\[[\\w=\\(\\)_]+\\]\\b"},{token:"constant.numeric.source.rust",regex:/\b(?:0x[a-fA-F0-9_]+|0o[0-7_]+|0b[01_]+|[0-9][0-9_]*(?!\.))(?:[iu](?:size|8|16|32|64|128))?\b/},{token:"constant.numeric.source.rust",regex:/\b(?:[0-9][0-9_]*)(?:\.[0-9][0-9_]*)?(?:[Ee][+-][0-9][0-9_]*)?(?:f32|f64)?\b/}]},this.embedRules(a,"doc-",[a.getEndRule("start")]),this.normalizeRules()};t.metaData={fileTypes:["rs","rc"],foldingStartMarker:"^.*\\bfn\\s*(\\w+\\s*)?\\([^\\)]*\\)(\\s*\\{[^\\}]*)?\\s*$",foldingStopMarker:"^\\s*\\}",name:"Rust",scopeName:"source.rust"},d.inherits(t,g),h.RustHighlightRules=t}),ace.define("ace/mode/folding/cstyle",["require","exports","module","ace/lib/oop","ace/range","ace/mode/folding/fold_mode"],function(n,h,p){"use strict";var d=n("../../lib/oop"),g=n("../../range").Range,a=n("./fold_mode").FoldMode,c=h.FoldMode=function(e){e&&(this.foldingStartMarker=new RegExp(this.foldingStartMarker.source.replace(/\|[^|]*?$/,"|"+e.start)),this.foldingStopMarker=new RegExp(this.foldingStopMarker.source.replace(/\|[^|]*?$/,"|"+e.end)))};d.inherits(c,a),function(){this.foldingStartMarker=/([\{\[\(])[^\}\]\)]*$|^\s*(\/\*)/,this.foldingStopMarker=/^[^\[\{\(]*([\}\]\)])|^[\s\*]*(\*\/)/,this.singleLineBlockCommentRe=/^\s*(\/\*).*\*\/\s*$/,this.tripleStarBlockCommentRe=/^\s*(\/\*\*\*).*\*\/\s*$/,this.startRegionRe=/^\s*(\/\*|\/\/)#?region\b/,this._getFoldWidgetBase=this.getFoldWidget,this.getFoldWidget=function(e,t,o){var i=e.getLine(o);if(this.singleLineBlockCommentRe.test(i)&&!this.startRegionRe.test(i)&&!this.tripleStarBlockCommentRe.test(i))return"";var s=this._getFoldWidgetBase(e,t,o);return!s&&this.startRegionRe.test(i)?"start":s},this.getFoldWidgetRange=function(e,t,o,i){var s=e.getLine(o);if(this.startRegionRe.test(s))return this.getCommentRegionBlock(e,s,o);var r=s.match(this.foldingStartMarker);if(r){var u=r.index;if(r[1])return this.openingBracketBlock(e,r[1],o,u);var l=e.getCommentFoldRange(o,u+r[0].length,1);return l&&!l.isMultiLine()&&(i?l=this.getSectionRange(e,o):t!="all"&&(l=null)),l}if(t!=="markbegin"){var r=s.match(this.foldingStopMarker);if(r){var u=r.index+r[0].length;return r[1]?this.closingBracketBlock(e,r[1],o,u):e.getCommentFoldRange(o,u,-1)}}},this.getSectionRange=function(e,t){var o=e.getLine(t),i=o.search(/\S/),s=t,r=o.length;t=t+1;for(var u=t,l=e.getLength();++t<l;){o=e.getLine(t);var m=o.search(/\S/);if(m!==-1){if(i>m)break;var f=this.getFoldWidgetRange(e,"all",t);if(f){if(f.start.row<=s)break;if(f.isMultiLine())t=f.end.row;else if(i==m)break}u=t}}return new g(s,r,u,e.getLine(u).length)},this.getCommentRegionBlock=function(e,t,o){for(var i=t.search(/\s*$/),s=e.getLength(),r=o,u=/^\s*(?:\/\*|\/\/|--)#?(end)?region\b/,l=1;++o<s;){t=e.getLine(o);var m=u.exec(t);if(!!m&&(m[1]?l--:l++,!l))break}var f=o;if(f>r)return new g(r,i,f,t.length)}}.call(c.prototype)}),ace.define("ace/mode/rust",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/rust_highlight_rules","ace/mode/folding/cstyle"],function(n,h,p){"use strict";var d=n("../lib/oop"),g=n("./text").Mode,a=n("./rust_highlight_rules").RustHighlightRules,c=n("./folding/cstyle").FoldMode,e=function(){this.HighlightRules=a,this.foldingRules=new c,this.$behaviour=this.$defaultBehaviour};d.inherits(e,g),function(){this.lineCommentStart="//",this.blockComment={start:"/*",end:"*/",nestable:!0},this.$quotes={'"':'"'},this.$id="ace/mode/rust"}.call(e.prototype),h.Mode=e}),function(){ace.require(["ace/mode/rust"],function(n){_&&(_.exports=n)})}()}}]);