(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2d0e4e1f"],{"91b3":function(p,t,e){"use strict";e.r(t);var n=function(){var s=this;return s._self._c,s._m(0)},v=[function(){var s=this,a=s._self._c;return a("div",[a("h1",[s._v("\u5982\u4F55\u6301\u4E45\u5316\u6570\u636E")]),a("blockquote",[a("p",[s._v("\u76EE\u524D\u63D0\u4F9B\u4E86\u4E00\u79CD\u65B0\u65B9\u5F0F\uFF0C\u53EF\u4EE5\u53C2\u8003"),a("a",{attrs:{href:"https://wanglin2.github.io/mind-map/#/doc/zh/deploy/%E5%AF%B9%E6%8E%A5%E8%87%AA%E5%B7%B1%E7%9A%84%E5%AD%98%E5%82%A8%E6%9C%8D%E5%8A%A1"}},[s._v("\u5BF9\u63A5\u81EA\u5DF1\u7684\u5B58\u50A8\u670D\u52A1")]),s._v("\u3002")])]),a("p",[s._v("\u5728\u7EBF"),a("code",[s._v("demo")]),s._v("\u7684\u6570\u636E\u662F\u5B58\u50A8\u5728\u7535\u8111\u672C\u5730\u7684\uFF0C\u4E5F\u5C31\u662F"),a("code",[s._v("localStorage")]),s._v("\u91CC\uFF0C\u5F53\u7136\uFF0C\u4F60\u4E5F\u53EF\u4EE5\u5B58\u50A8\u5230\u6570\u636E\u5E93\u4E2D\u3002")]),a("h2",[s._v("\u4FDD\u5B58\u6570\u636E")]),a("p",[s._v("\u4FDD\u5B58\u6570\u636E\uFF0C\u4E00\u822C\u6709\u4E24\u79CD\u505A\u6CD5\uFF0C\u4E00\u662F\u8BA9\u7528\u6237\u624B\u52A8\u4FDD\u5B58\uFF0C\u4E8C\u662F\u5F53\u753B\u5E03\u4E0A\u7684\u6570\u636E\u6539\u53D8\u540E\u81EA\u52A8\u4FDD\u5B58\uFF0C\u663E\u7136\uFF0C\u7B2C\u4E8C\u4E2D\u4F53\u9A8C\u66F4\u597D\u4E00\u70B9\u3002")]),a("p",[s._v("\u8981\u83B7\u53D6\u753B\u5E03\u7684\u6570\u636E\uFF0C\u53EF\u4EE5\u4F7F\u7528"),a("code",[s._v("getData")]),s._v("\u65B9\u6CD5\uFF0C\u53EF\u4EE5\u4F20\u9012\u4E00\u4E2A\u53C2\u6570\uFF0C"),a("code",[s._v("true")]),s._v("\u6307\u5B9A\u8FD4\u56DE\u7684\u6570\u636E\u4E2D\u5305\u542B\u914D\u7F6E\u6570\u636E\uFF0C"),a("code",[s._v("false")]),s._v("\u6307\u5B9A\u53EA\u8FD4\u56DE\u8282\u70B9\u6811\u6570\u636E\u3002")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" data = mindMap.getData("),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`)
`)])]),a("p",[s._v("\u5305\u542B\u914D\u7F6E\u7684\u5B8C\u6574\u6570\u636E\u7ED3\u6784\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`{
    layout,
    root,
    `),a("span",{staticClass:"hljs-attr"},[s._v("theme")]),s._v(`: {
        template,
        config
    },
    view
}
`)])]),a("p",[s._v("\u4F60\u53EF\u4EE5\u76F4\u63A5\u628A\u83B7\u53D6\u5230\u7684\u6570\u636E\u4FDD\u5B58\u8D77\u6765\u5373\u53EF\u3002")]),a("p",[s._v("\u5982\u679C\u8981\u81EA\u52A8\u4FDD\u5B58\uFF0C\u90A3\u4E48\u80AF\u5B9A\u9700\u8981\u76D1\u542C\u76F8\u5173\u4E8B\u4EF6\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".$bus.$on("),a("span",{staticClass:"hljs-string"},[s._v("'data_change'")]),s._v(", "),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("data")]),s._v(" =>")]),s._v(` {
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u8282\u70B9\u6811\u6570\u636E\u6539\u53D8")]),s._v(`
    `),a("span",{staticClass:"hljs-comment"},[s._v("// data\u5373\u5B8C\u6574\u6570\u636E\u4E2D\u7684root\u90E8\u5206")]),s._v(`
})
`),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".$bus.$on("),a("span",{staticClass:"hljs-string"},[s._v("'view_data_change'")]),s._v(", "),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("data")]),s._v(" =>")]),s._v(` {
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u89C6\u56FE\u6570\u636E\u6539\u53D8")]),s._v(`
    `),a("span",{staticClass:"hljs-comment"},[s._v("// data\u5373\u5B8C\u6574\u6570\u636E\u4E2D\u7684view\u90E8\u5206")]),s._v(`
})
`)])]),a("p",[s._v("\u4E3B\u9898\u548C\u7ED3\u6784\u7684\u6539\u53D8\u4E00\u822C\u662F\u5F00\u53D1\u8005\u63D0\u4F9B\u4E00\u4E2Aui\u754C\u9762\u8BA9\u7528\u6237\u9009\u62E9\uFF0C\u6240\u4EE5\u53EF\u4EE5\u81EA\u884C\u89E6\u53D1\u4FDD\u5B58\u3002")]),a("h2",[s._v("\u56DE\u663E\u6570\u636E")]),a("p",[s._v("\u5F53\u4ECE\u6570\u636E\u5E93\u83B7\u53D6\u5230\u4E86\u4FDD\u5B58\u7684\u6570\u636E\uFF0C\u90A3\u4E48\u600E\u4E48\u6E32\u67D3\u5230\u753B\u5E03\u4E0A\u5462\uFF0C\u9996\u5148\u53EF\u4EE5\u76F4\u63A5\u5728"),a("code",[s._v("new")]),s._v("\u4E00\u4E2A"),a("code",[s._v("MindMap")]),s._v("\u5B9E\u4F8B\u65F6\u76F4\u63A5\u4F20\u5165\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u4ECE\u6570\u636E\u4E2D\u53D6\u51FA\u5404\u4E2A\u90E8\u5206")]),s._v(`
`),a("span",{staticClass:"hljs-keyword"},[s._v("let")]),s._v(` { root, layout, theme, view } = storeData
`),a("span",{staticClass:"hljs-keyword"},[s._v("let")]),s._v(" mindMap = "),a("span",{staticClass:"hljs-keyword"},[s._v("new")]),s._v(` MindMap({
    `),a("span",{staticClass:"hljs-attr"},[s._v("el")]),s._v(`: container,
    `),a("span",{staticClass:"hljs-attr"},[s._v("data")]),s._v(`: root,
    `),a("span",{staticClass:"hljs-attr"},[s._v("layout")]),s._v(`: layout,
    `),a("span",{staticClass:"hljs-attr"},[s._v("theme")]),s._v(`: theme.template,
    `),a("span",{staticClass:"hljs-attr"},[s._v("themeConfig")]),s._v(`: theme.config,
    `),a("span",{staticClass:"hljs-attr"},[s._v("viewData")]),s._v(`: view,
    `),a("span",{staticClass:"hljs-comment"},[s._v("// ...")]),s._v(`
})
`)])]),a("p",[s._v("\u5176\u6B21\u5982\u679C\u662F\u5305\u542B\u914D\u7F6E\u7684\u5B8C\u6574\u6570\u636E\u4E5F\u53EF\u4EE5\u8C03\u7528"),a("code",[s._v("setFullData")]),s._v("\u65B9\u6CD5\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`mindMap.setFullData(data)
`)])]),a("p",[s._v("\u5982\u679C\u662F\u7EAF\u8282\u70B9\u6570\u636E\u53EF\u4EE5\u8C03\u7528"),a("code",[s._v("setData")]),s._v("\u65B9\u6CD5\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`mindMap.setData(data)
`)])]),a("p",[s._v("\u4FEE\u6539\u7ED3\u6784\u53EF\u4EE5\u8C03\u7528"),a("code",[s._v("setLayout")]),s._v("\u65B9\u6CD5\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`mindMap.setLayout(layout)
`)])]),a("p",[s._v("\u8BBE\u7F6E\u4E3B\u9898\u53EF\u4EE5\u8C03\u7528"),a("code",[s._v("setTheme")]),s._v("\u65B9\u6CD5\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`mindMap.setTheme(theme)
`)])]),a("p",[s._v("\u8BBE\u7F6E\u4E3B\u9898\u914D\u7F6E\u53EF\u4EE5\u8C03\u7528"),a("code",[s._v("setThemeConfig")]),s._v("\u65B9\u6CD5\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`mindMap.setThemeConfig(themeConfig)
`)])]),a("p",[s._v("\u8BBE\u7F6E\u89C6\u56FE\u6570\u636E\u53EF\u4EE5\u8C03\u7528"),a("code",[s._v("view.setTransformData")]),s._v("\u65B9\u6CD5\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`mindMap.view.setTransformData(view)
`)])]),a("h3",[s._v("\u5B8C\u6574\u793A\u4F8B")]),a("iframe",{staticStyle:{width:"100%",height:"455px",border:"none"},attrs:{src:"https://wanglin2.github.io/playground/#eNrFVc1u00AQfpXRIpQEpXYqcQpuVaAggdSCypHtYWNvkoX1ruVdN42qXHpEBU7lzI1bxQEJtc9D0z4Gs/6Lm0QIiQOWLO3OzPd945md9Ql5nCTeUcZJnwQmTEViwXCbJdtUiTjRqYUTSPmwC1rt6UxZHnXBjJmUenLAhzCDYapjaCFDq0bsCRXtsaRwUWLQLPlGjNaNmCWUUAVAleQWnM1FboHKpKSKKt+H208/rz9/ub44m5//mJ9/n3+8oCrUylgYcfsc43aZZQhhZqpCaHdgaxtOHCeTPLXtl29e7XvGpkKNxHDaLiU8xDpc26YZ73Q6VM3uyN1+OL05vVyR+yepIZOmoVVXsN0galSAT6rKtXMXAJd9iHSYxVxZx/pMcrd8Mn0RtVsl8qlWlgnF01anW6Ai1O4X7O6hxBkoaZgKs+XH1pkpmX+9LL6/6I17ZiWZCwzHQkYpVy747YJjiW6tyrLSr6uzm6tvy2J3BdeIHi58zbj/lEG1LG0VTihhD7S2+zrir7URVmiFyJbkQ9vqQivE1mGbDvPwWecRHgs8GoFfjB0OHG4sx1lhluMOIIjEEYSSGbNFSdnuXR5rSnJ3GSCihbc+DBgS+OhtBlZMVms5YC6k+pBgkFmrFeyEUoTvMaQxaBi2OpCBXyD+yHAH3ZyvJfQi0WoV+I064NbYqSxKslNeMZR4fnGvlCPjcRN7oTGUYF3d9QLgNUpWnYmJiOy4D5u93v08DiCpO5VyVBRHPHfkTXbvveXSVlQLIBsYLTNbAAFcv/vQK3dWJ4vNqvyYi9EYwx/2eslxpbxe90GlHLN0JFC3Yk1YFOEVVBnq1L2y03+Z8WaVQZl0vUdCPKV5D0iXFB1wN7n3zmiFP46cnpYO7EA9gZTgf6EYO8/HpZfi/Sdi7pq1MUj1xPAUSSgpJ2jNv6LArrbaocrcZmT2G71jRY0="}})])}],l={},i=l,_=e("2877"),c=Object(_.a)(i,n,v,!1,null,null,null);t.default=c.exports}}]);