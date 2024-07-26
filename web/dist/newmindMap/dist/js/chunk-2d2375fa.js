(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2d2375fa"],{fb9a:function(o,n,t){"use strict";t.r(n);var l=function(){var s=this;return s._self._c,s._m(0)},v=[function(){var s=this,a=s._self._c;return a("div",[a("h1",[s._v("\u5982\u4F55\u6E32\u67D3\u4E00\u4E2A\u53F3\u952E\u83DC\u5355")]),a("p",[s._v("\u53F3\u952E\u83DC\u5355\u53EF\u4EE5\u65B9\u4FBF\u7684\u5B8C\u6210\u4E00\u4E9B\u529F\u80FD\uFF0C\u5927\u4F53\u4E0A\u5206\u4E24\u79CD\uFF0C\u4E00\u662F\u5728\u753B\u5E03\u4E0A\u70B9\u51FB\u53F3\u952E\uFF0C\u4E8C\u662F\u5728\u8282\u70B9\u4E0A\u70B9\u51FB\u53F3\u952E\uFF0C\u4E24\u8005\u7684\u529F\u80FD\u80AF\u5B9A\u662F\u4E0D\u4E00\u6837\u7684\uFF0C\u751A\u81F3\u6839\u8282\u70B9\u548C\u5176\u4ED6\u8282\u70B9\u529F\u80FD\u4E0A\u4E5F\u6709\u4E9B\u4E0D\u540C\uFF0C\u6BD4\u5982\u6839\u8282\u70B9\u4E0D\u80FD\u6DFB\u52A0\u540C\u7EA7\u8282\u70B9\uFF0C\u4E5F\u4E0D\u80FD\u88AB\u5220\u9664\u7B49\u7B49\u3002")]),a("p",[s._v("\u53F3\u952E\u83DC\u5355\u7684UI\u754C\u9762\u9700\u8981\u4F60\u81EA\u884C\u5F00\u53D1\uFF0C\u53EF\u4EE5\u8BBE\u7F6E\u6210\u7EDD\u5BF9\u5B9A\u4F4D\u6216\u56FA\u5B9A\u5B9A\u4F4D\uFF0C\u7136\u540E\u8BA9\u5B83\u663E\u793A\u5728\u9F20\u6807\u53F3\u952E\u70B9\u51FB\u7684\u4F4D\u7F6E\u5373\u53EF\u3002")]),a("h2",[s._v("\u53F3\u952E\u83DC\u5355\u7684\u663E\u793A\u548C\u9690\u85CF")]),a("p",[s._v("\u9996\u5148\u76D1\u542C"),a("code",[s._v("node_contextmenu")]),s._v("\u4E8B\u4EF6\u5728\u53F3\u952E\u70B9\u51FB\u8282\u70B9\u65F6\u663E\u793A\u83DC\u5355\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u5F53\u524D\u53F3\u952E\u70B9\u51FB\u7684\u7C7B\u578B")]),s._v(`
`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" type = ref("),a("span",{staticClass:"hljs-string"},[s._v("''")]),s._v(`)
`),a("span",{staticClass:"hljs-comment"},[s._v("// \u5982\u679C\u70B9\u51FB\u7684\u8282\u70B9\uFF0C\u90A3\u4E48\u4EE3\u8868\u88AB\u70B9\u51FB\u7684\u8282\u70B9")]),s._v(`
`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" currentNode = shallowRef("),a("span",{staticClass:"hljs-literal"},[s._v("null")]),s._v(`)
`),a("span",{staticClass:"hljs-comment"},[s._v("// \u83DC\u5355\u663E\u793A\u7684\u4F4D\u7F6E")]),s._v(`
`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" left = ref("),a("span",{staticClass:"hljs-number"},[s._v("0")]),s._v(`)
`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" top = ref("),a("span",{staticClass:"hljs-number"},[s._v("0")]),s._v(`)
`),a("span",{staticClass:"hljs-comment"},[s._v("// \u662F\u5426\u663E\u793A\u83DC\u5355")]),s._v(`
`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" show = ref("),a("span",{staticClass:"hljs-literal"},[s._v("false")]),s._v(`)

mindMap.on(`),a("span",{staticClass:"hljs-string"},[s._v("'node_contextmenu'")]),s._v(", "),a("span",{staticClass:"hljs-function"},[s._v("("),a("span",{staticClass:"hljs-params"},[s._v("e, node")]),s._v(") =>")]),s._v(` {
    type.value = `),a("span",{staticClass:"hljs-string"},[s._v("'node'")]),s._v(`
    left.value = e.clientX + `),a("span",{staticClass:"hljs-number"},[s._v("10")]),s._v(`
    top.value = e.clientY + `),a("span",{staticClass:"hljs-number"},[s._v("10")]),s._v(`
    show.value = `),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`
    currentNode.value = node
})
`)])]),a("p",[s._v("\u4F60\u53EF\u4EE5\u6839\u636E\u5F53\u524D\u70B9\u51FB\u7684\u8282\u70B9\u6765\u5224\u65AD\u4E00\u4E9B\u64CD\u4F5C\u662F\u5426\u53EF\u7528\u3002\u6BD4\u5982\u6839\u8282\u70B9\u4E0D\u80FD\u5220\u9664\uFF0C\u4E0D\u80FD\u63D2\u5165\u540C\u7EA7\u8282\u70B9\uFF0C\u53C8\u6BD4\u5982\u540C\u7EA7\u7B2C\u4E00\u4E2A\u8282\u70B9\u4E0D\u80FD\u518D\u88AB\u5F80\u4E0A\u79FB\uFF0C\u540C\u7EA7\u6700\u540E\u4E00\u4E2A\u8282\u70B9\u4E0D\u80FD\u88AB\u5F80\u4E0B\u79FB\u3002")]),a("p",[s._v("\u5BF9\u4E8E\u753B\u5E03\u7684\u5904\u7406\u4F1A\u6BD4\u8F83\u9EBB\u70E6\uFF0C\u4E0D\u80FD\u76F4\u63A5\u76D1\u542C"),a("code",[s._v("contextmenu")]),s._v("\u4E8B\u4EF6\uFF0C\u56E0\u4E3A\u4F1A\u548C\u53F3\u952E\u591A\u9009\u8282\u70B9\u51B2\u7A81\uFF0C\u6240\u4EE5\u9700\u8981\u7ED3\u5408"),a("code",[s._v("mousedown")]),s._v("\u4E8B\u4EF6\u548C"),a("code",[s._v("mouseup")]),s._v("\u4E8B\u4EF6\u6765\u5904\u7406\u3002")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u8BB0\u5F55\u9F20\u6807\u53F3\u952E\u6309\u4E0B\u7684\u4F4D\u7F6E")]),s._v(`
`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" mousedownX = ref("),a("span",{staticClass:"hljs-number"},[s._v("0")]),s._v(`)
`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" mousedownY = ref("),a("span",{staticClass:"hljs-number"},[s._v("0")]),s._v(`)
`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" isMousedown = ref("),a("span",{staticClass:"hljs-literal"},[s._v("false")]),s._v(`)

mindMap.on(`),a("span",{staticClass:"hljs-string"},[s._v("'svg_mousedown'")]),s._v(", "),a("span",{staticClass:"hljs-function"},[s._v("("),a("span",{staticClass:"hljs-params"},[s._v("e")]),s._v(") =>")]),s._v(` {
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5982\u679C\u4E0D\u662F\u53F3\u952E\u70B9\u51FB\u76F4\u63A5\u8FD4\u56DE")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(" (e.which !== "),a("span",{staticClass:"hljs-number"},[s._v("3")]),s._v(`) {
        `),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(`
    }
    mousedownX.value = e.clientX
    mousedownY.value = e.clientY
    isMousedown.value = `),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`
})

mindMap.on(`),a("span",{staticClass:"hljs-string"},[s._v("'mouseup'")]),s._v(", "),a("span",{staticClass:"hljs-function"},[s._v("("),a("span",{staticClass:"hljs-params"},[s._v("e")]),s._v(") =>")]),s._v(` {
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(` (!isMousedown.value) {
        `),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(`
    }
    isMousedown.value = `),a("span",{staticClass:"hljs-literal"},[s._v("false")]),s._v(`
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5982\u679C\u9F20\u6807\u677E\u5F00\u548C\u6309\u4E0B\u7684\u8DDD\u79BB\u5927\u4E8E3\uFF0C\u5219\u4E0D\u8BA4\u4E3A\u662F\u70B9\u51FB\u4E8B\u4EF6")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(` (
        `),a("span",{staticClass:"hljs-built_in"},[s._v("Math")]),s._v(".abs(mousedownX.value - e.clientX) > "),a("span",{staticClass:"hljs-number"},[s._v("3")]),s._v(` ||
        `),a("span",{staticClass:"hljs-built_in"},[s._v("Math")]),s._v(".abs(mousedownX.value - e.clientY) > "),a("span",{staticClass:"hljs-number"},[s._v("3")]),s._v(`
    ) {
        hide()
        `),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(`
    }
    type.value = `),a("span",{staticClass:"hljs-string"},[s._v("'svg'")]),s._v(`
    left.value = e.clientX + `),a("span",{staticClass:"hljs-number"},[s._v("10")]),s._v(`
    top.value = e.clientY + `),a("span",{staticClass:"hljs-number"},[s._v("10")]),s._v(`
    show.value = `),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`
})
`)])]),a("p",[s._v("\u5F88\u7B80\u5355\uFF0C\u5176\u5B9E\u5C31\u662F\u5224\u65AD\u9F20\u6807\u6309\u4E0B\u548C\u677E\u5F00\u7684\u8DDD\u79BB\u662F\u5426\u5F88\u5C0F\uFF0C\u662F\u7684\u8BDD\u5C31\u8BA4\u4E3A\u662F\u70B9\u51FB\u4E8B\u4EF6\uFF0C\u5426\u5219\u5E94\u8BE5\u662F\u9F20\u6807\u62D6\u52A8\u3002")]),a("p",[s._v("\u53F3\u952E\u83DC\u5355\u663E\u793A\u4E86\uFF0C\u80AF\u5B9A\u5C31\u9700\u8981\u9690\u85CF\uFF0C\u5F53\u5DE6\u952E\u70B9\u51FB\u4E86\u753B\u5E03\u3001\u5DE6\u952E\u70B9\u51FB\u4E86\u8282\u70B9\u3001\u5DE6\u952E\u70B9\u51FB\u4E86\u5C55\u5F00\u6536\u8D77\u6309\u94AE\u65F6\u90FD\u9700\u8981\u9690\u85CF\u53F3\u952E\u83DC\u5355\u3002")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" hide = "),a("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
    show.value = `),a("span",{staticClass:"hljs-literal"},[s._v("false")]),s._v(`
    left.value = `),a("span",{staticClass:"hljs-number"},[s._v("0")]),s._v(`
    top.value = `),a("span",{staticClass:"hljs-number"},[s._v("0")]),s._v(`
    type.value = `),a("span",{staticClass:"hljs-string"},[s._v("''")]),s._v(`
}
mindMap.on(`),a("span",{staticClass:"hljs-string"},[s._v("'node_click'")]),s._v(`, hide)
mindMap.on(`),a("span",{staticClass:"hljs-string"},[s._v("'draw_click'")]),s._v(`, hide)
mindMap.on(`),a("span",{staticClass:"hljs-string"},[s._v("'expand_btn_click'")]),s._v(`, hide)
`)])]),a("h2",[s._v("\u590D\u5236\u3001\u526A\u5207\u3001\u7C98\u8D34\u7684\u5B9E\u73B0")]),a("p",[s._v("\u63A5\u4E0B\u6765\u4ECB\u7ECD\u4E00\u4E0B\u590D\u5236\u3001\u526A\u5207\u3001\u7C98\u8D34\u7684\u5B9E\u73B0\u3002")]),a("p",[s._v("\u4E00\u822C\u6765\u8BF4\u4F60\u7684\u53F3\u952E\u83DC\u5355\u4E2D\u80AF\u5B9A\u4E5F\u4F1A\u6DFB\u52A0\u8FD9\u4E09\u4E2A\u6309\u94AE\uFF0C\u53E6\u5916\u5FEB\u6377\u952E\u64CD\u4F5C\u4E5F\u662F\u5FC5\u4E0D\u53EF\u5C11\u7684\uFF0C\u4F46\u662F\u8FD9\u4E09\u4E2A\u5FEB\u6377\u952E\u662F\u6CA1\u6709\u5185\u7F6E\u7684\uFF0C\u6240\u4EE5\u4F60\u9700\u8981\u81EA\u5DF1\u6CE8\u518C\u4E00\u4E0B\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v("mindMap.keyCommand.addShortcut("),a("span",{staticClass:"hljs-string"},[s._v("'Control+c'")]),s._v(`, copy)
mindMap.keyCommand.addShortcut(`),a("span",{staticClass:"hljs-string"},[s._v("'Control+v'")]),s._v(`, paste)
mindMap.keyCommand.addShortcut(`),a("span",{staticClass:"hljs-string"},[s._v("'Control+x'")]),s._v(`, cut)
`)])]),a("p",[s._v("\u5982\u9700\u5220\u9664\u8C03\u7528"),a("code",[s._v("removeShortcut")]),s._v("\u65B9\u6CD5\u5373\u53EF\u3002")]),a("p",[s._v("\u63A5\u4E0B\u6765\u5B9E\u73B0\u4E00\u4E0B\u8FD9\u4E09\u4E2A\u65B9\u6CD5\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u4FDD\u5B58\u590D\u5236/\u526A\u5207\u7684\u8282\u70B9\u7684\u6570\u636E\uFF0C\u540E\u7EED\u53EF\u4EE5\u539F\u6765\u7C98\u8D34")]),s._v(`
`),a("span",{staticClass:"hljs-keyword"},[s._v("let")]),s._v(" copyData = "),a("span",{staticClass:"hljs-literal"},[s._v("null")]),s._v(`

`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" copy = "),a("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
    copyData = mindMap.renderer.copyNode()
}

`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" cut = "),a("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
    mindMap.execCommand(`),a("span",{staticClass:"hljs-string"},[s._v("'CUT_NODE'")]),s._v(", "),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("_copyData")]),s._v(" =>")]),s._v(` {
        copyData = _copyData
    })
}

`),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" paste = "),a("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
    mindMap.execCommand(`),a("span",{staticClass:"hljs-string"},[s._v("'PASTE_NODE'")]),s._v(`, copyData)
}
`)])]),a("h2",[s._v("\u5B8C\u6574\u793A\u4F8B")]),a("iframe",{staticStyle:{width:"100%",height:"455px",border:"none"},attrs:{src:"https://wanglin2.github.io/playground/#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"}})])}],e={},i=e,c=t("2877"),_=Object(c.a)(i,l,v,!1,null,null,null);n.default=_.exports}}]);
