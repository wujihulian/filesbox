(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2d0b978a"],{"32a6":function(o,t,n){"use strict";n.r(t);var l=function(){var s=this;return s._self._c,s._m(0)},v=[function(){var s=this,a=s._self._c;return a("div",[a("h1",[s._v("\u8BBE\u7F6E\u8282\u70B9\u6837\u5F0F")]),a("p",[s._v("\u672C\u8282\u5C06\u4ECB\u7ECD\u5982\u4F55\u66F4\u65B0\u5F53\u524D\u6FC0\u6D3B\u8282\u70B9\u7684\u6837\u5F0F\u3002")]),a("p",[s._v("\u6837\u5F0F\u603B\u4F53\u4E0A\u5206\u4E3A\u4E24\u7C7B\uFF0C\u4E00\u662F\u5E38\u6001\u7684\u6837\u5F0F\uFF0C\u4E8C\u662F\u6FC0\u6D3B\u7684\u6837\u5F0F\u3002\u8BBE\u7F6E\u7684\u65B9\u6CD5\u90FD\u662F"),a("code",[s._v("setStyle")]),s._v("\u65B9\u6CD5\uFF0C\u901A\u8FC7\u7B2C\u4E09\u4E2A\u53C2\u6570\u8FDB\u884C\u6307\u5B9A\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u5E38\u6001\u6837\u5F0F")]),s._v(`
node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'\u6837\u5F0F\u5C5E\u6027'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'\u6837\u5F0F\u503C'")]),s._v(`)

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u6FC0\u6D3B\u6837\u5F0F")]),s._v(`
node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'\u6837\u5F0F\u5C5E\u6027'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'\u6837\u5F0F\u503C'")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`)
`)])]),a("h2",[s._v("\u8BBE\u7F6E\u5E38\u6001\u6837\u5F0F")]),a("h3",[s._v("\u8BBE\u7F6E\u6587\u5B57\u6837\u5F0F")]),a("p",[s._v("\u6587\u5B57\u6837\u5F0F\u76EE\u524D\u652F\u6301\uFF1A"),a("code",[s._v("\u5B57\u4F53")]),s._v("\u3001"),a("code",[s._v("\u5B57\u53F7")]),s._v("\u3001"),a("code",[s._v("\u884C\u9AD8")]),s._v("\u3001"),a("code",[s._v("\u989C\u8272")]),s._v("\u3001"),a("code",[s._v("\u52A0\u7C97")]),s._v("\u3001"),a("code",[s._v("\u659C\u4F53")]),s._v("\u3001"),a("code",[s._v("\u5212\u7EBF")]),s._v("\u3002")]),a("p",[s._v("\u8FD9\u4E9B\u6837\u5F0F\u9009\u62E9\u7684UI\u754C\u9762\u90FD\u9700\u8981\u4F60\u81EA\u884C\u5F00\u53D1\uFF0C\u7136\u540E\u8C03\u7528\u8282\u70B9\u7684"),a("code",[s._v("setStyle")]),s._v("\u65B9\u6CD5\u66F4\u65B0\u3002")]),a("p",[s._v("\u540C\u6837\u9996\u5148\u9700\u8981\u76D1\u542C\u8282\u70B9\u7684\u6FC0\u6D3B\u4E8B\u4EF6\u6765\u6362\u53D6\u5F53\u524D\u6FC0\u6D3B\u7684\u8282\u70B9\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(` activeNodes = shallowRef([])
mindMap.on(`),a("span",{staticClass:"hljs-string"},[s._v("'node_active'")]),s._v(", "),a("span",{staticClass:"hljs-function"},[s._v("("),a("span",{staticClass:"hljs-params"},[s._v("node, activeNodeList")]),s._v(") =>")]),s._v(` {
    activeNodes.value = activeNodeList
})
`)])]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u5B57\u4F53")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'fontFamily'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'\u5B8B\u4F53, SimSun, Songti SC'")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u5B57\u53F7")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'fontSize'")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("16")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u884C\u9AD8")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'lineHeight'")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("1.5")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u989C\u8272")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'color'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'#fff'")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u52A0\u7C97")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'fontWeight'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'bold'")]),s._v(")"),a("span",{staticClass:"hljs-comment"},[s._v("// node.setStyle('fontWeight', 'normal')")]),s._v(`
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u5212\u7EBF")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u4E0B\u5212\u7EBF")]),s._v(`
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'textDecoration'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'underline'")]),s._v(`)
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5220\u9664\u7EBF")]),s._v(`
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'textDecoration'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'line-through'")]),s._v(`)
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u4E0A\u5212\u7EBF")]),s._v(`
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'textDecoration'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'overline'")]),s._v(`)
})
`)])]),a("h3",[s._v("\u8BBE\u7F6E\u8FB9\u6846\u6837\u5F0F")]),a("p",[s._v("\u8FB9\u6846\u6837\u5F0F\u652F\u6301\u8BBE\u7F6E\uFF1A"),a("code",[s._v("\u989C\u8272")]),s._v("\u3001"),a("code",[s._v("\u865A\u7EBF")]),s._v("\u3001"),a("code",[s._v("\u7EBF\u5BBD")]),s._v("\u3001"),a("code",[s._v("\u5706\u89D2")]),s._v("\u3002")]),a("p",[s._v("\u8BBE\u7F6E\u8FB9\u6846\u6837\u5F0F\u524D\u8BF7\u5148\u68C0\u67E5\u7EBF\u5BBD\u662F\u5426\u88AB\u8BBE\u7F6E\u6210\u4E860\u3002")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u8FB9\u6846\u989C\u8272")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'borderColor'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'#000'")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u8FB9\u6846\u865A\u7EBF")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'borderDasharray'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'5,5'")]),s._v(")"),a("span",{staticClass:"hljs-comment"},[s._v("// node.setStyle('borderDasharray', 'none')")]),s._v(`
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u8FB9\u6846\u5BBD\u5EA6")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'borderWidth'")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("2")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u8FB9\u6846\u5706\u89D2")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'borderRadius'")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("5")]),s._v(`)
})
`)])]),a("h3",[s._v("\u8BBE\u7F6E\u80CC\u666F\u6837\u5F0F")]),a("p",[s._v("\u80CC\u666F\u6837\u5F0F\u4E5F\u5C31\u662F\u80CC\u666F\u989C\u8272\u3002")]),a("pre",{staticClass:"hljs"},[a("code",[s._v("activeNodes.value.forEach("),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'fillColor'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'#fff'")]),s._v(`)
})
`)])]),a("h3",[s._v("\u8BBE\u7F6E\u5F62\u72B6\u6837\u5F0F")]),a("p",[s._v("\u76EE\u524D\u652F\u6301\u4EE5\u4E0B\u5F62\u72B6\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`[
  {
    `),a("span",{staticClass:"hljs-attr"},[s._v("name")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'\u77E9\u5F62'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("value")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'rectangle'")]),s._v(`
  },
  {
    `),a("span",{staticClass:"hljs-attr"},[s._v("name")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'\u83F1\u5F62'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("value")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'diamond'")]),s._v(`
  },
  {
    `),a("span",{staticClass:"hljs-attr"},[s._v("name")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'\u5E73\u884C\u56DB\u8FB9\u5F62'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("value")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'parallelogram'")]),s._v(`
  },
  {
    `),a("span",{staticClass:"hljs-attr"},[s._v("name")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'\u5706\u89D2\u77E9\u5F62'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("value")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'roundedRectangle'")]),s._v(`
  },
  {
    `),a("span",{staticClass:"hljs-attr"},[s._v("name")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'\u516B\u89D2\u77E9\u5F62'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("value")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'octagonalRectangle'")]),s._v(`
  },
  {
    `),a("span",{staticClass:"hljs-attr"},[s._v("name")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'\u5916\u4E09\u89D2\u77E9\u5F62'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("value")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'outerTriangularRectangle'")]),s._v(`
  },
  {
    `),a("span",{staticClass:"hljs-attr"},[s._v("name")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'\u5185\u4E09\u89D2\u77E9\u5F62'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("value")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'innerTriangularRectangle'")]),s._v(`
  },
  {
    `),a("span",{staticClass:"hljs-attr"},[s._v("name")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'\u692D\u5706'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("value")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'ellipse'")]),s._v(`
  },
  {
    `),a("span",{staticClass:"hljs-attr"},[s._v("name")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'\u5706'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("value")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'circle'")]),s._v(`
  }
]
`)])]),a("p",[s._v("\u8BBE\u7F6E\u5F62\u72B6\u524D\u8BF7\u5148\u786E\u8BA4\u8FB9\u6846\u5BBD\u5EA6\u662F\u5426\u88AB\u8BBE\u7F6E\u6210\u4E860\u3002")]),a("pre",{staticClass:"hljs"},[a("code",[s._v("activeNodes.value.forEach("),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'shape'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'circle'")]),s._v(`)
})
`)])]),a("h3",[s._v("\u8BBE\u7F6E\u7EBF\u6761\u6837\u5F0F")]),a("p",[s._v("\u8282\u70B9\u7EBF\u6761\u652F\u6301\u8BBE\u7F6E\uFF1A"),a("code",[s._v("\u989C\u8272")]),s._v("\u3001"),a("code",[s._v("\u865A\u7EBF")]),s._v("\u3001"),a("code",[s._v("\u7EBF\u5BBD")]),s._v("\u3002")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u7EBF\u6761\u989C\u8272")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'lineColor'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'#000'")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u7EBF\u6761\u865A\u7EBF")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'lineDasharray'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'5, 5, 1, 5'")]),s._v(")"),a("span",{staticClass:"hljs-comment"},[s._v("// node.setStyle('lineDasharray', 'none')")]),s._v(`
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u7EBF\u6761\u5BBD\u5EA6")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'lineWidth'")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("3")]),s._v(`)
})
`)])]),a("h3",[s._v("\u8BBE\u7F6E\u8282\u70B9\u5185\u8FB9\u8DDD")]),a("p",[s._v("\u8282\u70B9\u5185\u8FB9\u8DDD\u652F\u6301\u8BBE\u7F6E\u6C34\u5E73\u548C\u5782\u76F4\u65B9\u5411\u7684\u5185\u8FB9\u8DDD\u3002")]),a("pre",{staticClass:"hljs"},[a("code",[s._v("activeNodes.value.forEach("),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'paddingX'")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("50")]),s._v(`)
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'paddingY'")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("50")]),s._v(`)
})
`)])]),a("h2",[s._v("\u8BBE\u7F6E\u6FC0\u6D3B\u6837\u5F0F")]),a("p",[s._v("\u6FC0\u6D3B\u6837\u5F0F\u53EA\u652F\u6301\u8BBE\u7F6E\u8FB9\u6846\u76F8\u5173\u6837\u5F0F\u548C\u80CC\u666F\u3002\u53EF\u4EE5\u901A\u8FC7\u5982\u4E0B\u65B9\u5F0F\u83B7\u53D6\u652F\u6301\u7684\u5C5E\u6027\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" { supportActiveStyle } "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/themes/default'")]),s._v(`

`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" checkIsSupportActive = "),a("span",{staticClass:"hljs-function"},[s._v("("),a("span",{staticClass:"hljs-params"},[s._v("prop")]),s._v(") =>")]),s._v(` {
    `),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(` supportActiveStyle.includes(prop)
}
`)])]),a("p",[s._v("\u5176\u4ED6\u548C\u6570\u503C\u5E38\u6001\u6837\u5F0F\u662F\u4E00\u6837\u7684\uFF0C\u53EA\u9700\u8981\u7ED9"),a("code",[s._v("setStyle")]),s._v("\u65B9\u6CD5\u4F20\u5165\u7B2C\u4E09\u4E2A\u53C2\u6570\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u8FB9\u6846\u989C\u8272")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'borderColor'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'#000'")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u8FB9\u6846\u865A\u7EBF")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'borderDasharray'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'5,5'")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(")"),a("span",{staticClass:"hljs-comment"},[s._v("// node.setStyle('borderDasharray', 'none', true)")]),s._v(`
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u8FB9\u6846\u5BBD\u5EA6")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'borderWidth'")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("2")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u8FB9\u6846\u5706\u89D2")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'borderRadius'")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("5")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`)
})

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8BBE\u7F6E\u80CC\u666F\u989C\u8272")]),s._v(`
activeNodes.value.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(" =>")]),s._v(` {
    node.setStyle(`),a("span",{staticClass:"hljs-string"},[s._v("'fillColor'")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'#fff'")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`)
})
`)])]),a("h2",[s._v("\u5B8C\u6574\u793A\u4F8B")]),a("iframe",{staticStyle:{width:"100%",height:"455px",border:"none"},attrs:{src:"https://wanglin2.github.io/playground/#eNrFV91uG0UUfpXRVmgd5KwdIFwYJypNikBqUBUjFdSt0Hh3bA/MzqxmZ52GyBIqUKAtAlSJClClhhu4gHCDqJI27cvEP4/RM/tfex35wlKj2N6Z+b5zzsz55szsgfGu71v9kBgNoxk4kvoKBUSF/qbNqecLqdABkqRTRYLviJAr4lZR0MOMib1d0kED1JHCQyZYMDPGDuXuDvbjIdsIoJuRVQ96Vz3s24bNEbI5IwrpPo3cQDxkzOY2r9XQ8PT+8PsfRs+/HP33ZPzb15M7t8a3joffPZgc/mVzR/BAIewo2icfCpcEwM3jqVy/sZJYmRw9G58ejX75dvjPg9Gjx8OnP6ZkmN9H5KZqqX1GgF1ZQRub6EAHVTBr9TELidUR8jJ2ehUOfRkMId20wE5ko2J2BFfvYY+yfbOKzOHR3bPT+1XUol4r5PAreFdR1NoyIbY57Bb9ggB37e1yCKOcvE9ot6c0yFovRzmCCakjkMQ9x9e11JDZFmweUMEKbRNHSKyo4BqsY1hVPSnCbk+TbDWA78FLyz15djw6vD2z3JeEdIlc4oK3I4Nb6Xwv1Ov1OfOIkdsYNCIljvKzXl0/F3yNuqoHwDfOA+1il4YBoHQuSpfiq3ujX/8tLkJ3SWKjjGUzbwNvXjKGp3+M7/xfiKDVw/6SEgDL6WvBmg6VDpsbwvjk+ejh4YweroCUlqgGrcxFtKBxU0pA8L8G3+cwUjm8OW+Zb38Dsp88fliY4FXsupQvKeF+bOxjLbZ6eZwJ5JMUUogzq9uVQiiFukv20npdSdwT1kCucEKPcGV1ibrMiH68tP+BWzET5hbUEQyLI82VasxyscKNdAZQ3w3dYRuFrrhbFxbdbRujR8dxbY9PBP03SIxpoNOjzJWEa/D13MaUuVIv057OTu6NT/6cdvaywxKnN/KxIu4VRZA+Jn0pj3KqdoVQWldXRUB1uQamyUgnqvEOpA7SFLEGK+/EZy9CIN/x7z8Pf/o7jik+bc9O7p49gYqRCcQSvGJqrX0aixcMRmKtRgK8QgOVa6pE4FpfCS52D5qET7MW3zTgjgENReB6gBWBFkJNl/aRw3AQbNhGEsQ28YRtRMMJgLr5aKZEgDRrMFoEppaUEKyNAYL6q7QDHcVQGYHzuYc2UT3zYqtmO1RKcHTRYdT5HBjFWwPgZq8XzVpMSUxABCUmCidhZqR4aC5mpJtzo1NmIVZU/TNifDgsRMyqdUYulvWFTCT1MPee1swpdp7A9KlZK+gDmoEOJEJcTG6btmHV4itmUscsEniWEwS2kandKkgpVeuerusNtFavvxbhEPKz7SMJeASBRAPRztOfC9OSS03lRNwOBAtVTERIb8IGqictJfy8Meu+F93LGuitet2/mXou9/t66tnDskvBb2o1OQjSjix0K9kBC0a8lkaQBJ21wSDs3igHRtWIM6Av9dZngeDwDhGZt5MByEBWFm0DXhHiWmjV4NGScChRj+hkrbal2AuIBCO2kZS1kteGmDubas1KYhsYgxcal4r4"}})])}],e={},_=e,i=n("2877"),c=Object(i.a)(_,l,v,!1,null,null,null);t.default=c.exports}}]);
