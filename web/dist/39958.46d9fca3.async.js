(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[39958],{39958:function(R,I,L){R=L.nmd(R),ace.define("ace/mode/ion_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(r,s,T){"use strict";var p=r("../lib/oop"),g=r("./text_highlight_rules").TextHighlightRules,l=function(){var u="TRUE|FALSE",e=u,n="NULL.NULL|NULL.BOOL|NULL.INT|NULL.FLOAT|NULL.DECIMAL|NULL.TIMESTAMP|NULL.STRING|NULL.SYMBOL|NULL.BLOB|NULL.CLOB|NULL.STRUCT|NULL.LIST|NULL.SEXP|NULL",t=n,a=this.createKeywordMapper({"constant.language.bool.ion":e,"constant.language.null.ion":t},"constant.other.symbol.identifier.ion",!0),i={token:a,regex:"\\b\\w+(?:\\.\\w+)?\\b"};this.$rules={start:[{include:"value"}],value:[{include:"whitespace"},{include:"comment"},{include:"annotation"},{include:"string"},{include:"number"},{include:"keywords"},{include:"symbol"},{include:"clob"},{include:"blob"},{include:"struct"},{include:"list"},{include:"sexp"}],sexp:[{token:"punctuation.definition.sexp.begin.ion",regex:"\\(",push:[{token:"punctuation.definition.sexp.end.ion",regex:"\\)",next:"pop"},{include:"comment"},{include:"value"},{token:"storage.type.symbol.operator.ion",regex:"[\\!\\#\\%\\&\\*\\+\\-\\./\\;\\<\\=\\>\\?\\@\\^\\`\\|\\~]+"}]}],comment:[{token:"comment.line.ion",regex:"//[^\\n]*"},{token:"comment.block.ion",regex:"/\\*",push:[{token:"comment.block.ion",regex:"[*]/",next:"pop"},{token:"comment.block.ion",regex:"[^*/]+"},{token:"comment.block.ion",regex:"[*/]+"}]}],list:[{token:"punctuation.definition.list.begin.ion",regex:"\\[",push:[{token:"punctuation.definition.list.end.ion",regex:"\\]",next:"pop"},{include:"comment"},{include:"value"},{token:"punctuation.definition.list.separator.ion",regex:","}]}],struct:[{token:"punctuation.definition.struct.begin.ion",regex:"\\{",push:[{token:"punctuation.definition.struct.end.ion",regex:"\\}",next:"pop"},{include:"comment"},{include:"value"},{token:"punctuation.definition.struct.separator.ion",regex:",|:"}]}],blob:[{token:["punctuation.definition.blob.begin.ion","string.other.blob.ion","punctuation.definition.blob.end.ion"],regex:'(\\{\\{)([^"]*)(\\}\\})'}],clob:[{token:["punctuation.definition.clob.begin.ion","string.other.clob.ion","punctuation.definition.clob.end.ion"],regex:'(\\{\\{)("[^"]*")(\\}\\})'}],symbol:[{token:"storage.type.symbol.quoted.ion",regex:"(['])((?:(?:\\\\')|(?:[^']))*?)(['])"},{token:"storage.type.symbol.identifier.ion",regex:"[\\$_a-zA-Z][\\$_a-zA-Z0-9]*"}],number:[{token:"constant.numeric.timestamp.ion",regex:"\\d{4}(?:-\\d{2})?(?:-\\d{2})?T(?:\\d{2}:\\d{2})(?::\\d{2})?(?:\\.\\d+)?(?:Z|[-+]\\d{2}:\\d{2})?"},{token:"constant.numeric.timestamp.ion",regex:"\\d{4}-\\d{2}-\\d{2}T?"},{token:"constant.numeric.integer.binary.ion",regex:"-?0[bB][01](?:_?[01])*"},{token:"constant.numeric.integer.hex.ion",regex:"-?0[xX][0-9a-fA-F](?:_?[0-9a-fA-F])*"},{token:"constant.numeric.float.ion",regex:"-?(?:0|[1-9](?:_?\\d)*)(?:\\.(?:\\d(?:_?\\d)*)?)?(?:[eE][+-]?\\d+)"},{token:"constant.numeric.float.ion",regex:"(?:[-+]inf)|(?:nan)"},{token:"constant.numeric.decimal.ion",regex:"-?(?:0|[1-9](?:_?\\d)*)(?:(?:(?:\\.(?:\\d(?:_?\\d)*)?)(?:[dD][+-]?\\d+)|\\.(?:\\d(?:_?\\d)*)?)|(?:[dD][+-]?\\d+))"},{token:"constant.numeric.integer.ion",regex:"-?(?:0|[1-9](?:_?\\d)*)"}],string:[{token:["punctuation.definition.string.begin.ion","string.quoted.double.ion","punctuation.definition.string.end.ion"],regex:'(["])((?:(?:\\\\")|(?:[^"]))*?)(["])'},{token:"punctuation.definition.string.begin.ion",regex:"'{3}",push:[{token:"punctuation.definition.string.end.ion",regex:"'{3}",next:"pop"},{token:"string.quoted.triple.ion",regex:"(?:\\\\'|[^'])+"},{token:"string.quoted.triple.ion",regex:"'"}]}],annotation:[{token:["variable.language.annotation.ion","punctuation.definition.annotation.ion"],regex:"('(?:[^']|\\\\\\\\|\\\\')*')\\s*(::)"},{token:["variable.language.annotation.ion","punctuation.definition.annotation.ion"],regex:"([\\$_a-zA-Z][\\$_a-zA-Z0-9]*)\\s*(::)"}],whitespace:[{token:"text.ion",regex:"\\s+"}]},this.$rules.keywords=[i],this.normalizeRules()};p.inherits(l,g),s.IonHighlightRules=l}),ace.define("ace/mode/partiql_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules","ace/mode/ion_highlight_rules"],function(r,s,T){"use strict";var p=r("../lib/oop"),g=r("./text_highlight_rules").TextHighlightRules,l=r("./ion_highlight_rules").IonHighlightRules,u=function(){var e="MISSING",n="FALSE|NULL|TRUE",t=e+"|"+n,a="PIVOT|UNPIVOT|LIMIT|TUPLE|REMOVE|INDEX|CONFLICT|DO|NOTHING|RETURNING|MODIFIED|NEW|OLD|LET",i="ABSOLUTE|ACTION|ADD|ALL|ALLOCATE|ALTER|AND|ANY|ARE|AS|ASC|ASSERTION|AT|AUTHORIZATION|BEGIN|BETWEEN|BIT_LENGTH|BY|CASCADE|CASCADED|CASE|CATALOG|CHAR|CHARACTER_LENGTH|CHAR_LENGTH|CHECK|CLOSE|COLLATE|COLLATION|COLUMN|COMMIT|CONNECT|CONNECTION|CONSTRAINT|CONSTRAINTS|CONTINUE|CONVERT|CORRESPONDING|CREATE|CROSS|CURRENT|CURSOR|DEALLOCATE|DEC|DECLARE|DEFAULT|DEFERRABLE|DEFERRED|DELETE|DESC|DESCRIBE|DESCRIPTOR|DIAGNOSTICS|DISCONNECT|DISTINCT|DOMAIN|DROP|ELSE|END|END-EXEC|ESCAPE|EXCEPT|EXCEPTION|EXEC|EXECUTE|EXTERNAL|EXTRACT|FETCH|FIRST|FOR|FOREIGN|FOUND|FROM|FULL|GET|GLOBAL|GO|GOTO|GRANT|GROUP|HAVING|IDENTITY|IMMEDIATE|IN|INDICATOR|INITIALLY|INNER|INPUT|INSENSITIVE|INSERT|INTERSECT|INTERVAL|INTO|IS|ISOLATION|JOIN|KEY|LANGUAGE|LAST|LEFT|LEVEL|LIKE|LOCAL|LOWER|MATCH|MODULE|NAMES|NATIONAL|NATURAL|NCHAR|NEXT|NO|NOT|OCTET_LENGTH|OF|ON|ONLY|OPEN|OPTION|OR|ORDER|OUTER|OUTPUT|OVERLAPS|PAD|PARTIAL|POSITION|PRECISION|PREPARE|PRESERVE|PRIMARY|PRIOR|PRIVILEGES|PROCEDURE|PUBLIC|READ|REAL|REFERENCES|RELATIVE|RESTRICT|REVOKE|RIGHT|ROLLBACK|ROWS|SCHEMA|SCROLL|SECTION|SELECT|SESSION|SET|SIZE|SOME|SPACE|SQL|SQLCODE|SQLERROR|SQLSTATE|TABLE|TEMPORARY|THEN|TIME|TO|TRANSACTION|TRANSLATE|TRANSLATION|UNION|UNIQUE|UNKNOWN|UPDATE|UPPER|USAGE|USER|USING|VALUE|VALUES|VIEW|WHEN|WHENEVER|WHERE|WITH|WORK|WRITE|ZONE",o=a+"|"+i,c="BOOL|BOOLEAN|STRING|SYMBOL|CLOB|BLOB|STRUCT|LIST|SEXP|BAG",d="CHARACTER|DATE|DECIMAL|DOUBLE|FLOAT|INT|INTEGER|NUMERIC|SMALLINT|TIMESTAMP|VARCHAR|VARYING",h=c+"|"+d,E="AVG|COUNT|MAX|MIN|SUM",m=E,k="CAST|COALESCE|CURRENT_DATE|CURRENT_TIME|CURRENT_TIMESTAMP|CURRENT_USER|EXISTS|DATE_ADD|DATE_DIFF|NULLIF|SESSION_USER|SUBSTRING|SYSTEM_USER|TRIM",N=k,f=this.createKeywordMapper({"constant.language.partiql":t,"keyword.other.partiql":o,"storage.type.partiql":h,"support.function.aggregation.partiql":m,"support.function.partiql":N},"variable.language.identifier.partiql",!0),O={token:f,regex:"\\b\\w+\\b"};this.$rules={start:[{include:"whitespace"},{include:"comment"},{include:"value"}],value:[{include:"whitespace"},{include:"comment"},{include:"tuple_value"},{include:"collection_value"},{include:"scalar_value"}],scalar_value:[{include:"string"},{include:"number"},{include:"keywords"},{include:"identifier"},{include:"embed-ion"},{include:"operator"},{include:"punctuation"}],punctuation:[{token:"punctuation.partiql",regex:"[;:()\\[\\]\\{\\},.]"}],operator:[{token:"keyword.operator.partiql",regex:"[+*/<>=~!@#%&|?^-]+"}],identifier:[{token:"variable.language.identifier.quoted.partiql",regex:'(["])((?:(?:\\\\.)|(?:[^"\\\\]))*?)(["])'},{token:"variable.language.identifier.at.partiql",regex:"@\\w+"},{token:"variable.language.identifier.partiql",regex:"\\b\\w+(?:\\.\\w+)?\\b"}],number:[{token:"constant.numeric.partiql",regex:"[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"}],string:[{token:["punctuation.definition.string.begin.partiql","string.quoted.single.partiql","punctuation.definition.string.end.partiql"],regex:"(['])((?:(?:\\\\.)|(?:[^'\\\\]))*?)(['])"}],collection_value:[{include:"array_value"},{include:"bag_value"}],bag_value:[{token:"punctuation.definition.bag.begin.partiql",regex:"<<",push:[{token:"punctuation.definition.bag.end.partiql",regex:">>",next:"pop"},{include:"comment"},{token:"punctuation.definition.bag.separator.partiql",regex:","},{include:"value"}]}],comment:[{token:"comment.line.partiql",regex:"--.*"},{token:"comment.block.partiql",regex:"/\\*",push:"comment__1"}],comment__1:[{token:"comment.block.partiql",regex:"[*]/",next:"pop"},{token:"comment.block.partiql",regex:"[^*/]+"},{token:"comment.block.partiql",regex:"/\\*",push:"comment__1"},{token:"comment.block.partiql",regex:"[*/]+"}],array_value:[{token:"punctuation.definition.array.begin.partiql",regex:"\\[",push:[{token:"punctuation.definition.array.end.partiql",regex:"\\]",next:"pop"},{include:"comment"},{token:"punctuation.definition.array.separator.partiql",regex:","},{include:"value"}]}],tuple_value:[{token:"punctuation.definition.tuple.begin.partiql",regex:"\\{",push:[{token:"punctuation.definition.tuple.end.partiql",regex:"\\}",next:"pop"},{include:"comment"},{token:"punctuation.definition.tuple.separator.partiql",regex:",|:"},{include:"value"}]}],whitespace:[{token:"text.partiql",regex:"\\s+"}]},this.$rules.keywords=[O],this.$rules["embed-ion"]=[{token:"punctuation.definition.ion.begin.partiql",regex:"`",next:"ion-start"}],this.embedRules(l,"ion-",[{token:"punctuation.definition.ion.end.partiql",regex:"`",next:"start"}]),this.normalizeRules()};p.inherits(u,g),s.PartiqlHighlightRules=u}),ace.define("ace/mode/matching_brace_outdent",["require","exports","module","ace/range"],function(r,s,T){"use strict";var p=r("../range").Range,g=function(){};(function(){this.checkOutdent=function(l,u){return/^\s+$/.test(l)?/^\s*\}/.test(u):!1},this.autoOutdent=function(l,u){var e=l.getLine(u),n=e.match(/^(\s*\})/);if(!n)return 0;var t=n[1].length,a=l.findMatchingBracket({row:u,column:t});if(!a||a.row==u)return 0;var i=this.$getIndent(l.getLine(a.row));l.replace(new p(u,0,u,t-1),i)},this.$getIndent=function(l){return l.match(/^\s*/)[0]}}).call(g.prototype),s.MatchingBraceOutdent=g}),ace.define("ace/mode/folding/cstyle",["require","exports","module","ace/lib/oop","ace/range","ace/mode/folding/fold_mode"],function(r,s,T){"use strict";var p=r("../../lib/oop"),g=r("../../range").Range,l=r("./fold_mode").FoldMode,u=s.FoldMode=function(e){e&&(this.foldingStartMarker=new RegExp(this.foldingStartMarker.source.replace(/\|[^|]*?$/,"|"+e.start)),this.foldingStopMarker=new RegExp(this.foldingStopMarker.source.replace(/\|[^|]*?$/,"|"+e.end)))};p.inherits(u,l),function(){this.foldingStartMarker=/([\{\[\(])[^\}\]\)]*$|^\s*(\/\*)/,this.foldingStopMarker=/^[^\[\{\(]*([\}\]\)])|^[\s\*]*(\*\/)/,this.singleLineBlockCommentRe=/^\s*(\/\*).*\*\/\s*$/,this.tripleStarBlockCommentRe=/^\s*(\/\*\*\*).*\*\/\s*$/,this.startRegionRe=/^\s*(\/\*|\/\/)#?region\b/,this._getFoldWidgetBase=this.getFoldWidget,this.getFoldWidget=function(e,n,t){var a=e.getLine(t);if(this.singleLineBlockCommentRe.test(a)&&!this.startRegionRe.test(a)&&!this.tripleStarBlockCommentRe.test(a))return"";var i=this._getFoldWidgetBase(e,n,t);return!i&&this.startRegionRe.test(a)?"start":i},this.getFoldWidgetRange=function(e,n,t,a){var i=e.getLine(t);if(this.startRegionRe.test(i))return this.getCommentRegionBlock(e,i,t);var o=i.match(this.foldingStartMarker);if(o){var c=o.index;if(o[1])return this.openingBracketBlock(e,o[1],t,c);var d=e.getCommentFoldRange(t,c+o[0].length,1);return d&&!d.isMultiLine()&&(a?d=this.getSectionRange(e,t):n!="all"&&(d=null)),d}if(n!=="markbegin"){var o=i.match(this.foldingStopMarker);if(o){var c=o.index+o[0].length;return o[1]?this.closingBracketBlock(e,o[1],t,c):e.getCommentFoldRange(t,c,-1)}}},this.getSectionRange=function(e,n){var t=e.getLine(n),a=t.search(/\S/),i=n,o=t.length;n=n+1;for(var c=n,d=e.getLength();++n<d;){t=e.getLine(n);var h=t.search(/\S/);if(h!==-1){if(a>h)break;var E=this.getFoldWidgetRange(e,"all",n);if(E){if(E.start.row<=i)break;if(E.isMultiLine())n=E.end.row;else if(a==h)break}c=n}}return new g(i,o,c,e.getLine(c).length)},this.getCommentRegionBlock=function(e,n,t){for(var a=n.search(/\s*$/),i=e.getLength(),o=t,c=/^\s*(?:\/\*|\/\/|--)#?(end)?region\b/,d=1;++t<i;){n=e.getLine(t);var h=c.exec(n);if(!!h&&(h[1]?d--:d++,!d))break}var E=t;if(E>o)return new g(o,a,E,n.length)}}.call(u.prototype)}),ace.define("ace/mode/partiql",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/partiql_highlight_rules","ace/mode/matching_brace_outdent","ace/mode/behaviour/cstyle","ace/mode/folding/cstyle"],function(r,s,T){"use strict";var p=r("../lib/oop"),g=r("./text").Mode,l=r("./partiql_highlight_rules").PartiqlHighlightRules,u=r("./matching_brace_outdent").MatchingBraceOutdent,e=r("./behaviour/cstyle").CstyleBehaviour,n=r("./folding/cstyle").FoldMode,t=function(){this.HighlightRules=l,this.$outdent=new u,this.$behaviour=new e,this.foldingRules=new n};p.inherits(t,g),function(){this.lineCommentStart="--",this.blockComment={start:"/*",end:"*/",nestable:!0},this.getNextLineIndent=function(a,i,o){var c=this.$getIndent(i);if(a=="start"){var d=i.match(/^.*[\{\(\[]\s*$/);d&&(c+=o)}return c},this.checkOutdent=function(a,i,o){return this.$outdent.checkOutdent(i,o)},this.autoOutdent=function(a,i,o){this.$outdent.autoOutdent(i,o)},this.$id="ace/mode/partiql"}.call(t.prototype),s.Mode=t}),function(){ace.require(["ace/mode/partiql"],function(r){R&&(R.exports=r)})}()}}]);