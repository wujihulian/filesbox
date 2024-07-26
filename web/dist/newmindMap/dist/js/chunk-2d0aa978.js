(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2d0aa978"],{1256:function(i,v,t){"use strict";t.r(v);var n=function(){var s=this;return s._self._c,s._m(0)},_=[function(){var s=this,a=s._self._c;return a("div",[a("h1",[s._v("Export \u63D2\u4EF6")]),a("p",[a("code",[s._v("Export")]),s._v("\u63D2\u4EF6\u63D0\u4F9B\u5BFC\u51FA\u7684\u529F\u80FD\u3002")]),a("h2",[s._v("\u6CE8\u518C")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" MindMap "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map'")]),s._v(`
`),a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" Export "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/plugins/Export.js'")]),s._v(`
`),a("span",{staticClass:"hljs-comment"},[s._v("// import Export from 'simple-mind-map/src/Export.js' v0.6.0\u4EE5\u4E0B\u7248\u672C\u4F7F\u7528\u8BE5\u8DEF\u5F84")]),s._v(`

MindMap.usePlugin(Export)
`)])]),a("p",[s._v("\u6CE8\u518C\u5B8C\u4E14\u5B9E\u4F8B\u5316"),a("code",[s._v("MindMap")]),s._v("\u540E\u53EF\u901A\u8FC7"),a("code",[s._v("mindMap.doExport")]),s._v("\u83B7\u53D6\u5230\u8BE5\u5B9E\u4F8B\u3002")]),a("h2",[s._v("\u65B9\u6CD5")]),a("p",[s._v("\u6240\u6709\u5BFC\u51FA\u7684\u65B9\u6CD5\u90FD\u662F\u5F02\u6B65\u65B9\u6CD5\uFF0C\u8FD4\u56DE\u4E00\u4E2A"),a("code",[s._v("Promise")]),s._v("\u5B9E\u4F8B\uFF0C\u4F60\u53EF\u4EE5\u4F7F\u7528"),a("code",[s._v("then")]),s._v("\u65B9\u6CD5\u83B7\u53D6\u6570\u636E\uFF0C\u6216\u8005\u4F7F\u7528"),a("code",[s._v("async await")]),s._v("\u51FD\u6570\u83B7\u53D6\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v("mindMap.doExport.png().then("),a("span",{staticClass:"hljs-function"},[s._v("("),a("span",{staticClass:"hljs-params"},[s._v("data")]),s._v(") =>")]),s._v(` {
  `),a("span",{staticClass:"hljs-comment"},[s._v("// ...")]),s._v(`
})

`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" "),a("span",{staticClass:"hljs-keyword"},[s._v("export")]),s._v(" = "),a("span",{staticClass:"hljs-keyword"},[s._v("async")]),s._v(` () => {
  `),a("span",{staticClass:"hljs-keyword"},[s._v("let")]),s._v(" data = "),a("span",{staticClass:"hljs-keyword"},[s._v("await")]),s._v(` mindMap.doExport.png()
  `),a("span",{staticClass:"hljs-comment"},[s._v("// ...")]),s._v(`
}
`)])]),a("p",[s._v("\u8FD4\u56DE\u7684\u6570\u636E\u4E3A"),a("code",[s._v("data:url")]),s._v("\u683C\u5F0F\u7684\uFF0C\u4F60\u53EF\u4EE5\u521B\u5EFA\u4E00\u4E2A"),a("code",[s._v("a")]),s._v("\u6807\u7B7E\u6765\u89E6\u53D1\u4E0B\u8F7D\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("let")]),s._v(" a = "),a("span",{staticClass:"hljs-built_in"},[s._v("document")]),s._v(".createElement("),a("span",{staticClass:"hljs-string"},[s._v("'a'")]),s._v(`)
a.href = `),a("span",{staticClass:"hljs-string"},[s._v("'xxx.png'")]),a("span",{staticClass:"hljs-comment"},[s._v("// .png\u3001.svg\u3001.pdf\u3001.md\u3001.json\u3001.smm")]),s._v(`
a.download = `),a("span",{staticClass:"hljs-string"},[s._v("'xxx'")]),s._v(`
a.click()
`)])]),a("h3",[s._v("png(name, transparent = false, rotateWhenWidthLongerThenHeight)")]),a("ul",[a("li",[a("p",[a("code",[s._v("name")]),s._v("\uFF1A\u540D\u79F0\uFF0C\u53EF\u4E0D\u4F20")])]),a("li",[a("p",[a("code",[s._v("transparent")]),s._v("\uFF1Av0.5.7+\uFF0C\u6307\u5B9A\u5BFC\u51FA\u56FE\u7247\u7684\u80CC\u666F\u662F\u5426\u662F\u900F\u660E\u7684")])]),a("li",[a("p",[a("code",[s._v("rotateWhenWidthLongerThenHeight")]),s._v(": v0.6.15+\uFF0CBoolean, false, \u662F\u5426\u5728\u56FE\u7247\u5BBD\u6BD4\u9AD8\u957F\u65F6\u81EA\u52A8\u65CB\u8F6C90\u5EA6")])])]),a("p",[s._v("\u5BFC\u51FA\u4E3A"),a("code",[s._v("png")]),s._v("\u3002")]),a("h3",[s._v("svg(name, plusCssText)")]),a("ul",[a("li",[a("p",[a("code",[s._v("name")]),s._v("\uFF1A"),a("code",[s._v("svg")]),s._v("\u6807\u9898")])]),a("li",[a("p",[a("code",[s._v("plusCssText")]),s._v("\uFF1Av0.4.0+\uFF0C\uFF08v0.6.16+\u5DF2\u53BB\u9664\u8BE5\u53C2\u6570\uFF0C\u6539\u4E3A\u5728\u5B9E\u4F8B\u5316\u65F6\u901A\u8FC7"),a("code",[s._v("resetCss")]),s._v("\u914D\u7F6E\u4F20\u5165\uFF09\uFF0C\u5F53\u5F00\u542F\u4E86\u8282\u70B9\u5BCC\u6587\u672C\u7F16\u8F91\uFF0C\u4E14"),a("code",[s._v("domToImage")]),s._v("\u4F20\u4E86"),a("code",[s._v("false")]),s._v("\u65F6\uFF0C\u53EF\u4EE5\u6DFB\u52A0\u9644\u52A0\u7684"),a("code",[s._v("css")]),s._v("\u6837\u5F0F\uFF0C\u5982\u679C"),a("code",[s._v("svg")]),s._v("\u4E2D\u5B58\u5728"),a("code",[s._v("dom")]),s._v("\u8282\u70B9\uFF0C\u60F3\u8981\u8BBE\u7F6E\u4E00\u4E9B\u9488\u5BF9\u8282\u70B9\u7684\u6837\u5F0F\u53EF\u4EE5\u901A\u8FC7\u8FD9\u4E2A\u53C2\u6570\u4F20\u5165\uFF0C\u6BD4\u5982\uFF1A")])])]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`svg(
  `),a("span",{staticClass:"hljs-string"},[s._v("''")]),s._v(`, 
  `),a("span",{staticClass:"hljs-literal"},[s._v("false")]),s._v(`, 
  `),a("span",{staticClass:"hljs-string"},[s._v(`\`* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }\``)]),s._v(`
)
`)])]),a("p",[s._v("\u5BFC\u51FA\u4E3A"),a("code",[s._v("svg")]),s._v("\u3002")]),a("h3",[s._v("pdf(name, useMultiPageExport)")]),a("blockquote",[a("p",[s._v("v0.2.1+")])]),a("ul",[a("li",[a("p",[a("code",[s._v("name")]),s._v("\uFF1A\u6587\u4EF6\u540D\u79F0")])]),a("li",[a("p",[a("code",[s._v("useMultiPageExport")]),s._v(": v0.6.15+\uFF0CBoolean, false, \u662F\u5426\u591A\u9875\u5BFC\u51FA\uFF0C\u9ED8\u8BA4\u4E3A\u5355\u9875")])])]),a("p",[s._v("\u5BFC\u51FA\u4E3A"),a("code",[s._v("pdf")]),s._v("\uFF0C\u548C\u5176\u4ED6\u5BFC\u51FA\u65B9\u6CD5\u4E0D\u4E00\u6837\uFF0C\u8FD9\u4E2A\u65B9\u6CD5\u4E0D\u4F1A\u8FD4\u56DE\u6570\u636E\uFF0C\u4F1A\u76F4\u63A5\u89E6\u53D1\u4E0B\u8F7D\u3002")]),a("blockquote",[a("p",[s._v("v0.6.0\u7248\u672C\u4EE5\u540E\uFF0C\u9700\u8981\u989D\u5916\u6CE8\u518C\u4E00\u4E2AExportPDF\u63D2\u4EF6")])]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" ExportPDF "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/plugins/ExportPDF.js'")]),s._v(`
MindMap.usePlugin(ExportPDF)
`)])]),a("h3",[s._v("json(name, withConfig)")]),a("p",[a("code",[s._v("name")]),s._v("\uFF1A\u6682\u65F6\u6CA1\u6709\u7528\u5904\uFF0C\u4F20\u7A7A\u5B57\u7B26\u4E32\u5373\u53EF")]),a("p",[a("code",[s._v("withConfig``\uFF1ABoolean")]),s._v(", \u9ED8\u8BA4\u4E3A"),a("code",[s._v("true")]),s._v("\uFF0C\u6570\u636E\u4E2D\u662F\u5426\u5305\u542B\u914D\u7F6E\uFF0C\u5426\u5219\u4E3A\u7EAF\u601D\u7EF4\u5BFC\u56FE\u8282\u70B9\u6570\u636E")]),a("p",[s._v("\u8FD4\u56DE"),a("code",[s._v("json")]),s._v("\u6570\u636E\u3002")]),a("h3",[s._v("smm(name, withConfig)")]),a("p",[a("code",[s._v("simple-mind-map")]),s._v("\u81EA\u5B9A\u4E49\u7684\u6587\u4EF6\u683C\u5F0F\uFF0C\u5176\u5B9E\u5C31\u662F"),a("code",[s._v("json")]),s._v("\uFF0C\u548C"),a("code",[s._v("json")]),s._v("\u65B9\u6CD5\u8FD4\u56DE\u7684\u6570\u636E\u4E00\u6A21\u4E00\u6837\u3002")]),a("h3",[s._v("md()")]),a("blockquote",[a("p",[s._v("v0.4.7+")])]),a("p",[s._v("\u5BFC\u51FA"),a("code",[s._v("markdown")]),s._v("\u6587\u4EF6\u3002")]),a("h3",[s._v("getSvgData()")]),a("p",[s._v("\u83B7\u53D6"),a("code",[s._v("svg")]),s._v("\u6570\u636E\uFF0C\u5F02\u6B65\u65B9\u6CD5\uFF0C\u8FD4\u56DE\u4E00\u4E2A\u5BF9\u8C61\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`{
  node`),a("span",{staticClass:"hljs-comment"},[s._v("// svg\u8282\u70B9")]),s._v(`
  str`),a("span",{staticClass:"hljs-comment"},[s._v("// svg\u5B57\u7B26\u4E32")]),s._v(`
}
`)])]),a("h3",[s._v("xmind(name)")]),a("blockquote",[a("p",[s._v("v0.6.6+\uFF0C\u9700\u8981\u989D\u5916\u6CE8\u518C\u4E00\u4E2AExportXMind\u63D2\u4EF6")])]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" ExportXMind "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/plugins/ExportXMind.js'")]),s._v(`
MindMap.usePlugin(ExportXMind)
`)])]),a("p",[s._v("\u5BFC\u51FA\u4E3A"),a("code",[s._v("xmind")]),s._v("\u6587\u4EF6\u7C7B\u578B\uFF0C\u5F02\u6B65\u65B9\u6CD5\uFF0C\u8FD4\u56DE\u4E00\u4E2A"),a("code",[s._v("Promise")]),s._v("\u5B9E\u4F8B\uFF0C\u8FD4\u56DE\u7684\u6570\u636E\u4E3A\u4E00\u4E2A"),a("code",[s._v("zip")]),s._v("\u538B\u7F29\u5305\u7684"),a("code",[s._v("data:url")]),s._v("\u6570\u636E\uFF0C\u53EF\u4EE5\u76F4\u63A5\u4E0B\u8F7D\u3002")])])}],e={},o=e,l=t("2877"),p=Object(l.a)(o,n,_,!1,null,null,null);v.default=p.exports}}]);