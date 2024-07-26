(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2d230098"],{eb27:function(p,t,n){"use strict";n.r(t);var l=function(){var s=this;return s._self._c,s._m(0)},e=[function(){var s=this,a=s._self._c;return a("div",[a("h1",[s._v("\u5982\u4F55\u6E32\u67D3\u4E00\u4E2A\u5927\u7EB2")]),a("p",[s._v("\u601D\u7EF4\u5BFC\u56FE\u672C\u8D28\u5C31\u662F\u4E00\u9897\u6811\uFF0C\u6240\u4EE5\u4F60\u53EF\u4EE5\u4F7F\u7528\u6811\u7EC4\u4EF6\u6765\u5B8C\u6210\u5927\u7EB2\u7684\u663E\u793A\u3002")]),a("p",[s._v("\u53EF\u4EE5\u76D1\u542C"),a("code",[s._v("data_change")]),s._v("\u4E8B\u4EF6\u6765\u83B7\u53D6\u5F53\u524D\u6700\u65B0\u7684\u601D\u7EF4\u5BFC\u56FE\u6570\u636E\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v("mindMap.on("),a("span",{staticClass:"hljs-string"},[s._v("'data_change'")]),s._v(", "),a("span",{staticClass:"hljs-function"},[s._v("("),a("span",{staticClass:"hljs-params"},[s._v("data")]),s._v(") =>")]),s._v(` {
    `),a("span",{staticClass:"hljs-comment"},[s._v("// data\u6570\u636E\u662F\u4E0D\u5E26\u8282\u70B9\u5BF9\u8C61\u7684\u7EAF\u6570\u636E")]),s._v(`
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5982\u679C\u4F60\u9700\u8981\u64CD\u4F5C\u8282\u70B9\u5BF9\u8C61\uFF0C\u53EF\u4EE5\u4F7F\u7528mindMap.renderer.renderTree")]),s._v(`
    `),a("span",{staticClass:"hljs-built_in"},[s._v("console")]),s._v(`.log(data, mindMap.renderer.renderTree)
})
`)])]),a("p",[s._v("\u901A\u5E38\u70B9\u51FB\u4E86\u5927\u7EB2\u7684\u67D0\u4E2A\u8282\u70B9\uFF0C\u4F1A\u5C06\u753B\u5E03\u5B9A\u4F4D\u5230\u8BE5\u8282\u70B9\u5E76\u6FC0\u6D3B\u8BE5\u8282\u70B9\uFF0C\u8FD9\u53EF\u4EE5\u8FD9\u4E48\u505A\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(` node = data._node
mindMap.renderer.moveNodeToCenter(node)
node.active()

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u5728v0.6.7+\u7248\u672C\u53EF\u4EE5\u8FD9\u4E48\u505A\uFF1A")]),s._v(`
mindMap.execCommand(`),a("span",{staticClass:"hljs-string"},[s._v("'GO_TARGET_NODE'")]),s._v(", node)"),a("span",{staticClass:"hljs-comment"},[s._v("// \u6216\u8005\u4F20\u8282\u70B9\u7684uid")]),s._v(`
`)])]),a("p",[s._v("\u5F53\u5728\u5927\u7EB2\u6811\u4E0A\u7F16\u8F91\u4E86\u67D0\u4E2A\u8282\u70B9\u7684\u5185\u5BB9\uFF0C\u9700\u8981\u540C\u6B65\u5230\u601D\u7EF4\u5BFC\u56FE\u6811\u4E0A\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v("data._node.setText("),a("span",{staticClass:"hljs-string"},[s._v("'xxx'")]),s._v(`)
`)])]),a("p",[s._v("\u8981\u63D2\u5165\u5144\u5F1F\u8282\u70B9\u6216\u5B50\u8282\u70B9\u53EF\u4EE5\u8FD9\u4E48\u64CD\u4F5C\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[s._v("mindMap.execCommand("),a("span",{staticClass:"hljs-string"},[s._v("'INSERT_NODE'")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("false")]),s._v(`)
mindMap.execCommand(`),a("span",{staticClass:"hljs-string"},[s._v("'INSERT_CHILD_NODE'")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("false")]),s._v(`)
`)])]),a("h2",[s._v("\u8FDB\u9636")]),a("p",[s._v("\u8981\u5B9E\u73B0\u4E00\u4E2A\u529F\u80FD\u5B8C\u5584\u7684\u5927\u7EB2\u5E76\u4E0D\u5BB9\u6613\uFF0C\u4E0B\u9762\u4ECB\u7ECD\u4E00\u4E0B\u5305\u542B\u5B9A\u4F4D\u3001\u7F16\u8F91\u3001\u62D6\u62FD\u3001\u5220\u9664\u3001\u5355\u72EC\u7F16\u8F91\u529F\u80FD\u7684\u5927\u7EB2\u5B9E\u73B0\u3002")]),a("p",[s._v("\u4EE5"),a("a",{attrs:{href:"https://element.eleme.cn/#/zh-CN/component/tree"}},[s._v("ElementUI Tree\u7EC4\u4EF6")]),s._v("\u4E3A\u4F8B\u3002")]),a("p",[s._v("\u5B9E\u73B0\u76D1\u542C"),a("code",[s._v("data_change")]),s._v("\u4E8B\u4EF6\u6765\u5237\u65B0\u6811\u6570\u636E\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" { nodeRichTextToTextWithWrap } "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/utils'")]),s._v(`

`),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".mindMap.on("),a("span",{staticClass:"hljs-string"},[s._v("'data_change'")]),s._v(", "),a("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
    `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.refresh()
})

{
    `),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("refresh")]),s._v("("),a("span",{staticClass:"hljs-params"}),s._v(")")]),s._v(` {
        `),a("span",{staticClass:"hljs-keyword"},[s._v("let")]),s._v(" data = mindMap.getData()"),a("span",{staticClass:"hljs-comment"},[s._v("// \u83B7\u53D6\u601D\u7EF4\u5BFC\u56FE\u6811\u6570\u636E")]),s._v(`
        data.root = `),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(" "),a("span",{staticClass:"hljs-comment"},[s._v("// \u6807\u8BB0\u6839\u8282\u70B9")]),s._v(`
        `),a("span",{staticClass:"hljs-comment"},[s._v("// \u904D\u5386\u6811\uFF0C\u6DFB\u52A0\u4E00\u4E9B\u5C5E\u6027")]),s._v(`
        `),a("span",{staticClass:"hljs-keyword"},[s._v("let")]),s._v(" walk = "),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("root")]),s._v(" =>")]),s._v(` {
            `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5982\u679C\u662F\u5BCC\u6587\u672C\u8282\u70B9\uFF0C\u90A3\u4E48\u8C03\u7528nodeRichTextToTextWithWrap\u65B9\u6CD5\u5C06<p><span></span><p>\u5F62\u5F0F\u7684\u8282\u70B9\u5BCC\u6587\u672C\u5185\u5BB9\u8F6C\u6362\u6210\\n\u6362\u884C\u7684\u6587\u672C")]),s._v(`
            `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(` text = (root.data.richText
                ? nodeRichTextToTextWithWrap(root.data.text)
                : root.data.text
            ).replaceAll(`),a("span",{staticClass:"hljs-regexp"},[s._v("/\\n/g")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("'<br>'")]),s._v(`)
            root.textCache = text `),a("span",{staticClass:"hljs-comment"},[s._v("// \u4FDD\u5B58\u4E00\u4EFD\u4FEE\u6539\u524D\u7684\u6570\u636E\uFF0C\u7528\u4E8E\u5BF9\u6BD4\u662F\u5426\u4FEE\u6539\u4E86")]),s._v(`
            root.label = text`),a("span",{staticClass:"hljs-comment"},[s._v("// \u7528\u4E8E\u6811\u7EC4\u4EF6\u6E32\u67D3")]),s._v(`
            root.uid = root.data.uid`),a("span",{staticClass:"hljs-comment"},[s._v("// \u7528\u4E8E\u6811\u7EC4\u4EF6\u6E32\u67D3")]),s._v(`
            `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(" (root.children && root.children.length > "),a("span",{staticClass:"hljs-number"},[s._v("0")]),s._v(`) {
                root.children.forEach(`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-params"},[s._v("item")]),s._v(" =>")]),s._v(` {
                    walk(item)
                })
            }
        }
        walk(data)
        `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".data = [data]"),a("span",{staticClass:"hljs-comment"},[s._v("// \u8D4B\u503C\u7ED9\u6811\u7EC4\u4EF6")]),s._v(`
    }
}
`)])]),a("p",[s._v("\u6A21\u677F\u5982\u4E0B\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-tag"},[s._v("<"),a("span",{staticClass:"hljs-name"},[s._v("el-tree")]),s._v(`
    `),a("span",{staticClass:"hljs-attr"},[s._v("ref")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"tree"')]),s._v(`
    `),a("span",{staticClass:"hljs-attr"},[s._v("node-key")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"uid"')]),s._v(`
    `),a("span",{staticClass:"hljs-attr"},[s._v("draggable")]),s._v(`
    `),a("span",{staticClass:"hljs-attr"},[s._v("default-expand-all")]),s._v(`
    `),a("span",{staticClass:"hljs-attr"},[s._v(":data")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"data"')]),s._v(`
    `),a("span",{staticClass:"hljs-attr"},[s._v(":highlight-current")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"true"')]),s._v(`
    `),a("span",{staticClass:"hljs-attr"},[s._v(":expand-on-click-node")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"false"')]),s._v(`
    `),a("span",{staticClass:"hljs-attr"},[s._v(":allow-drag")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"checkAllowDrag"')]),s._v(`
    @`),a("span",{staticClass:"hljs-attr"},[s._v("node-drop")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"onNodeDrop"')]),s._v(`
    @`),a("span",{staticClass:"hljs-attr"},[s._v("current-change")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"onCurrentChange"')]),s._v(`
    @`),a("span",{staticClass:"hljs-attr"},[s._v("mouseenter.native")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"isInTreArea = true"')]),s._v(`
    @`),a("span",{staticClass:"hljs-attr"},[s._v("mouseleave.native")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"isInTreArea = false"')]),s._v(`
>`)]),s._v(`
    `),a("span",{staticClass:"hljs-tag"},[s._v("<"),a("span",{staticClass:"hljs-name"},[s._v("span")]),s._v(`
        `),a("span",{staticClass:"hljs-attr"},[s._v("class")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"customNode"')]),s._v(`
        `),a("span",{staticClass:"hljs-attr"},[s._v("slot-scope")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"{ node, data }"')]),s._v(`
        `),a("span",{staticClass:"hljs-attr"},[s._v(":data-id")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"data.uid"')]),s._v(`
        @`),a("span",{staticClass:"hljs-attr"},[s._v("click")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"onClick(data)"')]),s._v(`
    >`)]),s._v(`
        `),a("span",{staticClass:"hljs-tag"},[s._v("<"),a("span",{staticClass:"hljs-name"},[s._v("span")]),s._v(`
            `),a("span",{staticClass:"hljs-attr"},[s._v("class")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"nodeEdit"')]),s._v(`
            `),a("span",{staticClass:"hljs-attr"},[s._v("contenteditable")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"true"')]),s._v(`
            `),a("span",{staticClass:"hljs-attr"},[s._v(":key")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"getKey()"')]),s._v(`
            @`),a("span",{staticClass:"hljs-attr"},[s._v("keydown.stop")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"onNodeInputKeydown($event, node)"')]),s._v(`
            @`),a("span",{staticClass:"hljs-attr"},[s._v("keyup.stop")]),s._v(`
            @`),a("span",{staticClass:"hljs-attr"},[s._v("blur")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"onBlur($event, node)"')]),s._v(`
            @`),a("span",{staticClass:"hljs-attr"},[s._v("paste")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"onPaste($event, node)"')]),s._v(`
            `),a("span",{staticClass:"hljs-attr"},[s._v("v-html")]),s._v("="),a("span",{staticClass:"hljs-string"},[s._v('"node.label"')]),s._v(`
        >`)]),a("span",{staticClass:"hljs-tag"},[s._v("</"),a("span",{staticClass:"hljs-name"},[s._v("span")]),s._v(">")]),s._v(`
    `),a("span",{staticClass:"hljs-tag"},[s._v("</"),a("span",{staticClass:"hljs-name"},[s._v("span")]),s._v(">")]),s._v(`
`),a("span",{staticClass:"hljs-tag"},[s._v("</"),a("span",{staticClass:"hljs-name"},[s._v("el-tree")]),s._v(">")]),s._v(`
`)])]),a("h3",[s._v("\u5B9A\u4F4D\u8282\u70B9")]),a("p",[s._v("\u7ED9\u8282\u70B9\u7ED1\u5B9A\u4E86\u4E00\u4E2A"),a("code",[s._v("click")]),s._v("\u4E8B\u4EF6\u7528\u4E8E\u5728\u753B\u5E03\u5185\u5B9A\u4F4D\u70B9\u51FB\u7684\u8282\u70B9\uFF0C\u53EF\u4EE5\u8C03\u7528\u601D\u7EF4\u5BFC\u56FE\u7684\u76F8\u5173\u65B9\u6CD5\u5B9E\u73B0\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u6FC0\u6D3B\u5F53\u524D\u8282\u70B9\u4E14\u79FB\u52A8\u5F53\u524D\u8282\u70B9\u5230\u753B\u5E03\u4E2D\u95F4")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("onClick")]),s._v("("),a("span",{staticClass:"hljs-params"},[s._v("data")]),s._v(")")]),s._v(` {
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u6839\u636Euid\u77E5\u9053\u601D\u7EF4\u5BFC\u56FE\u8282\u70B9\u5BF9\u8C61")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" targetNode = "),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.mindMap.renderer.findNodeByUid(data.uid)
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5982\u679C\u5F53\u524D\u5DF2\u7ECF\u662F\u6FC0\u6D3B\u72B6\u6001\uFF0C\u90A3\u4E48\u4E0A\u9762\u90FD\u4E0D\u505A")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(" (targetNode && targetNode.nodeData.data.isActive) "),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(`
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u601D\u7EF4\u5BFC\u56FE\u8282\u70B9\u6FC0\u6D3B\u65F6\u9ED8\u8BA4\u4F1A\u805A\u7126\u5230\u5185\u90E8\u521B\u5EFA\u7684\u4E00\u4E2A\u9690\u85CF\u8F93\u5165\u6846\u4E2D\uFF0C`stopFocusOnNodeActive`\u65B9\u6CD5\u662F\u7528\u4E8E\u5173\u95ED\u8FD9\u4E2A\u7279\u6027\uFF0C\u56E0\u4E3A\u6211\u4EEC\u60F3\u628A\u7126\u70B9\u7559\u5728\u5927\u7EB2\u7684\u8F93\u5165\u6846\u4E2D")]),s._v(`
    `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.mindMap.renderer.textEdit.stopFocusOnNodeActive()
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5B9A\u4F4D\u5230\u76EE\u6807\u8282\u70B9")]),s._v(`
    `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".mindMap.execCommand("),a("span",{staticClass:"hljs-string"},[s._v("'GO_TARGET_NODE'")]),s._v(", data.uid, "),a("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
        `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5B9A\u4F4D\u5B8C\u6210\u540E\u518D\u5F00\u542F\u524D\u9762\u5173\u95ED\u7684\u7279\u6027")]),s._v(`
        `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.mindMap.renderer.textEdit.openFocusOnNodeActive()
    })
}
`)])]),a("h3",[s._v("\u7F16\u8F91")]),a("p",[s._v("\u6211\u4EEC\u901A\u8FC7\u81EA\u5B9A\u4E49\u6811\u8282\u70B9\u5185\u5BB9\u6E32\u67D3\u4E86\u4E00\u4E2A"),a("code",[s._v("contenteditable=true")]),s._v("\u7684\u6807\u7B7E\u7528\u4E8E\u8F93\u5165\u6587\u672C\uFF0C\u7136\u540E\u5728"),a("code",[s._v("blur")]),s._v("\u4E8B\u4EF6\u4E2D\u4FEE\u6539\u8282\u70B9\u6587\u672C\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" { textToNodeRichTextWithWrap } "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/utils'")]),s._v(`

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u5931\u53BB\u7126\u70B9\u66F4\u65B0\u8282\u70B9\u6587\u672C")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("onBlur")]),s._v("("),a("span",{staticClass:"hljs-params"},[s._v("e, node")]),s._v(")")]),s._v(` {
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u8282\u70B9\u6570\u636E\u6CA1\u6709\u4FEE\u6539\u90A3\u4E48\u4EC0\u4E48\u4E5F\u4E0D\u7528\u505A")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(` (node.data.textCache === e.target.innerHTML) {
        `),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(`
    }
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u6839\u636E\u662F\u5426\u662F\u5BCC\u6587\u672C\u6A21\u5F0F\u83B7\u53D6\u4E0D\u540C\u7684\u6587\u672C\u6570\u636E")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(` richText = node.data.data.richText
    `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(` text = richText ? e.target.innerHTML : e.target.innerText
    `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" targetNode = "),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.mindMap.renderer.findNodeByUid(node.data.uid)
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(" (!targetNode) "),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(` (richText) {
        `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5982\u679C\u662F\u5BCC\u6587\u672C\u8282\u70B9\uFF0C\u90A3\u4E48\u9700\u8981\u5148\u8C03\u7528textToNodeRichTextWithWrap\u65B9\u6CD5\u5C06<br>\u6362\u884C\u7684\u6587\u672C\u8F6C\u6362\u6210<p><span></span><p>\u5F62\u5F0F\u7684\u8282\u70B9\u5BCC\u6587\u672C\u5185\u5BB9")]),s._v(`
        `),a("span",{staticClass:"hljs-comment"},[s._v("// \u7B2C\u4E8C\u4E2A\u53C2\u6570\u4EE3\u8868\u8BBE\u7F6E\u7684\u662F\u5BCC\u6587\u672C\u5185\u5BB9")]),s._v(`
        `),a("span",{staticClass:"hljs-comment"},[s._v("// \u7B2C\u4E09\u4E2A\u53C2\u6570\u6307\u5B9A\u8981\u91CD\u7F6E\u5BCC\u6587\u672C\u8282\u70B9\u7684\u6837\u5F0F")]),s._v(`
        targetNode.setText(textToNodeRichTextWithWrap(text), `),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`)
    } `),a("span",{staticClass:"hljs-keyword"},[s._v("else")]),s._v(` {
        targetNode.setText(text)
    }
}
`)])]),a("h3",[s._v("\u62D6\u62FD")]),a("p",[s._v("\u8BBE\u7F6E\u4E86"),a("code",[s._v("draggable")]),s._v("\u5C5E\u6027\u5373\u53EF\u5F00\u542F\u62D6\u62FD\uFF0C\u9996\u5148\u6839\u8282\u70B9\u662F\u4E0D\u5141\u8BB8\u62D6\u62FD\u7684\uFF0C\u6240\u4EE5\u901A\u8FC7"),a("code",[s._v("allow-drag")]),s._v("\u5C5E\u6027\u4F20\u5165\u4E00\u4E2A\u5224\u65AD\u65B9\u6CD5\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u6839\u8282\u70B9\u4E0D\u5141\u8BB8\u62D6\u62FD")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("checkAllowDrag")]),s._v("("),a("span",{staticClass:"hljs-params"},[s._v("node")]),s._v(")")]),s._v(` {
    `),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(` !node.data.root
}
`)])]),a("p",[s._v("\u7136\u540E\u76D1\u542C\u62D6\u62FD\u5B8C\u6210\u4E8B\u4EF6"),a("code",[s._v("node-drop")]),s._v("\u6765\u5B9E\u73B0\u753B\u5E03\u5185\u8282\u70B9\u7684\u8C03\u6574\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u62D6\u62FD\u7ED3\u675F\u4E8B\u4EF6")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("onNodeDrop")]),s._v("("),a("span",{staticClass:"hljs-params"},[s._v("data, target, position")]),s._v(")")]),s._v(` {
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u88AB\u62D6\u62FD\u7684\u8282\u70B9")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" node = "),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.mindMap.renderer.findNodeByUid(data.data.uid)
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u62D6\u62FD\u5230\u7684\u76EE\u6807\u8282\u70B9")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" targetNode = "),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.mindMap.renderer.findNodeByUid(target.data.uid)
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(` (!node || !targetNode) {
        `),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(`
    }
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u6839\u636E\u4E0D\u540C\u62D6\u62FD\u7684\u60C5\u51B5\u8C03\u7528\u4E0D\u540C\u7684\u65B9\u6CD5")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("switch")]),s._v(` (position) {
        `),a("span",{staticClass:"hljs-keyword"},[s._v("case")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'before'")]),s._v(`:
            `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".mindMap.execCommand("),a("span",{staticClass:"hljs-string"},[s._v("'INSERT_BEFORE'")]),s._v(`, node, targetNode)
            `),a("span",{staticClass:"hljs-keyword"},[s._v("break")]),s._v(`
        `),a("span",{staticClass:"hljs-keyword"},[s._v("case")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'after'")]),s._v(`:
            `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".mindMap.execCommand("),a("span",{staticClass:"hljs-string"},[s._v("'INSERT_AFTER'")]),s._v(`, node, targetNode)
            `),a("span",{staticClass:"hljs-keyword"},[s._v("break")]),s._v(`
        `),a("span",{staticClass:"hljs-keyword"},[s._v("case")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'inner'")]),s._v(`:
            `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".mindMap.execCommand("),a("span",{staticClass:"hljs-string"},[s._v("'MOVE_NODE_TO'")]),s._v(`, node, targetNode)
            `),a("span",{staticClass:"hljs-keyword"},[s._v("break")]),s._v(`
        `),a("span",{staticClass:"hljs-attr"},[s._v("default")]),s._v(`:
            `),a("span",{staticClass:"hljs-keyword"},[s._v("break")]),s._v(`
    }
}
`)])]),a("h3",[s._v("\u5220\u9664\u8282\u70B9")]),a("p",[s._v("\u9996\u5148\u901A\u8FC7\u6811\u7EC4\u4EF6\u7684"),a("code",[s._v("current-change")]),s._v("\u4E8B\u4EF6\u6765\u4FDD\u5B58\u5F53\u524D\u9AD8\u4EAE\u7684\u6811\u8282\u70B9\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-comment"},[s._v("// \u5F53\u524D\u9009\u4E2D\u7684\u6811\u8282\u70B9\u53D8\u5316\u4E8B\u4EF6")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("onCurrentChange")]),s._v("("),a("span",{staticClass:"hljs-params"},[s._v("data")]),s._v(")")]),s._v(` {
    `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.currentData = data
}
`)])]),a("p",[s._v("\u7136\u540E\u901A\u8FC7\u76D1\u542C"),a("code",[s._v("keydown")]),s._v("\u4E8B\u4EF6\u6765\u5B8C\u6210\u5220\u9664\u8282\u70B9\u7684\u64CD\u4F5C\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".addEventListener("),a("span",{staticClass:"hljs-string"},[s._v("'keydown'")]),s._v(", "),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.onKeyDown)

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u5220\u9664\u8282\u70B9")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("onKeyDown")]),s._v("("),a("span",{staticClass:"hljs-params"},[s._v("e")]),s._v(")")]),s._v(` {
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(" (["),a("span",{staticClass:"hljs-number"},[s._v("46")]),s._v(", "),a("span",{staticClass:"hljs-number"},[s._v("8")]),s._v("].includes(e.keyCode) && "),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.currentData) {
        e.stopPropagation()
        `),a("span",{staticClass:"hljs-comment"},[s._v("// \u5904\u7406\u5F53\u524D\u6B63\u5728\u7F16\u8F91\u8282\u70B9\u5185\u5BB9\u65F6\u5220\u9664\u7684\u60C5\u51B5")]),s._v(`
        `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.mindMap.renderer.textEdit.hideEditTextBox()
        `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" node = "),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".mindMap.renderer.findNodeByUid("),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.currentData.uid)
        `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(` (node && !node.isRoot) {
            `),a("span",{staticClass:"hljs-comment"},[s._v("// \u9996\u5148\u4ECE\u6811\u91CC\u5220\u9664")]),s._v(`
            `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".$refs.tree.remove("),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.currentData)
            `),a("span",{staticClass:"hljs-comment"},[s._v("// \u7136\u540E\u4ECE\u753B\u5E03\u91CC\u5220\u9664")]),s._v(`
            `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".mindMap.execCommand("),a("span",{staticClass:"hljs-string"},[s._v("'REMOVE_NODE'")]),s._v(`, [node])
        }
    }
}
`)])]),a("h3",[s._v("\u521B\u5EFA\u65B0\u8282\u70B9")]),a("p",[s._v("\u901A\u8FC7\u76D1\u542C\u8282\u70B9\u5185\u5BB9\u7F16\u8F91\u6846\u7684"),a("code",[s._v("keydown")]),s._v("\u4E8B\u4EF6\u6765\u5B8C\u6210\u6DFB\u52A0\u65B0\u8282\u70B9\u7684\u64CD\u4F5C\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" { createUid } "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/utils'")]),s._v(`

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u8282\u70B9\u8F93\u5165\u533A\u57DF\u6309\u952E\u4E8B\u4EF6")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("onNodeInputKeydown")]),s._v("("),a("span",{staticClass:"hljs-params"},[s._v("e")]),s._v(")")]),s._v(` {
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u56DE\u8F66\u952E\u6DFB\u52A0\u540C\u7EA7\u8282\u70B9")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(" (e.keyCode === "),a("span",{staticClass:"hljs-number"},[s._v("13")]),s._v(` && !e.shiftKey) {
        e.preventDefault()
        `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.insertNode()
    }
    `),a("span",{staticClass:"hljs-comment"},[s._v("// tab\u952E\u6DFB\u52A0\u5B50\u8282\u70B9")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(" (e.keyCode === "),a("span",{staticClass:"hljs-number"},[s._v("9")]),s._v(`) {
        e.preventDefault()
        `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(`.insertChildNode()
    }
}

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u63D2\u5165\u5144\u5F1F\u8282\u70B9")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("insertNode")]),s._v("("),a("span",{staticClass:"hljs-params"}),s._v(")")]),s._v(` {
    `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".mindMap.execCommand("),a("span",{staticClass:"hljs-string"},[s._v("'INSERT_NODE'")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("false")]),s._v(`, [], {
        `),a("span",{staticClass:"hljs-attr"},[s._v("uid")]),s._v(`: createUid()
    })
}

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u63D2\u5165\u4E0B\u7EA7\u8282\u70B9")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("insertChildNode")]),s._v("("),a("span",{staticClass:"hljs-params"}),s._v(")")]),s._v(` {
    `),a("span",{staticClass:"hljs-built_in"},[s._v("this")]),s._v(".mindMap.execCommand("),a("span",{staticClass:"hljs-string"},[s._v("'INSERT_CHILD_NODE'")]),s._v(", "),a("span",{staticClass:"hljs-literal"},[s._v("false")]),s._v(`, [], {
        `),a("span",{staticClass:"hljs-attr"},[s._v("uid")]),s._v(`: createUid()
    })
}
`)])]),a("h3",[s._v("\u62E6\u622A\u8F93\u5165\u6846\u7684\u7C98\u8D34\u64CD\u4F5C")]),a("p",[s._v("\u4E3A\u4EC0\u4E48\u8981\u62E6\u622A\u8F93\u5165\u6846\u7684\u7C98\u8D34\u64CD\u4F5C\uFF0C\u56E0\u4E3A\u7528\u6237\u53EF\u80FD\u7C98\u8D34\u7684\u662F\u5BCC\u6587\u672C\u5185\u5BB9\uFF0C\u4E5F\u5C31\u662F\u5E26html\u6807\u7B7E\u7684\uFF0C\u4F46\u662F\u4E00\u822C\u6211\u4EEC\u90FD\u4E0D\u5E0C\u671B\u7528\u6237\u7C98\u8D34\u8FD9\u79CD\u5185\u5BB9\uFF0C\u53EA\u5141\u8BB8\u7C98\u8D34\u7EAF\u6587\u672C\uFF0C\u6240\u4EE5\u6211\u4EEC\u8981\u62E6\u622A\u7C98\u8D34\u4E8B\u4EF6\uFF0C\u5904\u7406\u4E00\u4E0B\u7528\u6237\u7C98\u8D34\u7684\u5185\u5BB9\uFF1A")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" { getTextFromHtml } "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/utils'")]),s._v(`

`),a("span",{staticClass:"hljs-comment"},[s._v("// \u62E6\u622A\u7C98\u8D34\u4E8B\u4EF6")]),s._v(`
`),a("span",{staticClass:"hljs-function"},[a("span",{staticClass:"hljs-title"},[s._v("onPaste")]),s._v("("),a("span",{staticClass:"hljs-params"},[s._v("e")]),s._v(")")]),s._v(` {
    e.preventDefault()
    `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" selection = "),a("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(`.getSelection()
    `),a("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(" (!selection.rangeCount) "),a("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(`
    selection.deleteFromDocument()`),a("span",{staticClass:"hljs-comment"},[s._v("// \u5220\u9664\u5F53\u524D\u9009\u533A\uFF0C\u4E5F\u5C31\u662F\u5982\u679C\u5F53\u524D\u7528\u6237\u5728\u8F93\u5165\u6846\u4E2D\u9009\u62E9\u4E86\u4E00\u4E9B\u6587\u672C\uFF0C\u4F1A\u88AB\u5220\u9664")]),s._v(`
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u4ECE\u526A\u8D34\u677F\u91CC\u53D6\u51FA\u6587\u672C\u6570\u636E")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("let")]),s._v(" text = (e.clipboardData || "),a("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".clipboardData).getData("),a("span",{staticClass:"hljs-string"},[s._v("'text'")]),s._v(`)
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u8C03\u7528\u5E93\u63D0\u4F9B\u7684getTextFromHtml\u65B9\u6CD5\u53BB\u9664\u683C\u5F0F")]),s._v(`
    text = getTextFromHtml(text)
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u53BB\u9664\u6362\u884C")]),s._v(`
    text = text.replaceAll(`),a("span",{staticClass:"hljs-regexp"},[s._v("/\\n/g")]),s._v(", "),a("span",{staticClass:"hljs-string"},[s._v("''")]),s._v(`)
    `),a("span",{staticClass:"hljs-comment"},[s._v("// \u521B\u5EFA\u6587\u672C\u8282\u70B9\u6DFB\u52A0\u5230\u5F53\u524D\u9009\u533A")]),s._v(`
    `),a("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" node = "),a("span",{staticClass:"hljs-built_in"},[s._v("document")]),s._v(`.createTextNode(text)
    selection.getRangeAt(`),a("span",{staticClass:"hljs-number"},[s._v("0")]),s._v(`).insertNode(node)
    selection.collapseToEnd()
}
`)])]),a("p",[s._v("\u5230\u8FD9\u91CC\u57FA\u672C\u529F\u80FD\u5C31\u90FD\u5B8C\u6210\u4E86\uFF0C\u662F\u4E0D\u662F\u89C9\u5F97\u633A\u7B80\u5355\u7684\uFF1F\u6838\u5FC3\u539F\u7406\u548C\u64CD\u4F5C\u786E\u5B9E\u5F88\u7B80\u5355\uFF0C\u9EBB\u70E6\u7684\u662F\u5404\u79CD\u60C5\u51B5\u548C\u51B2\u7A81\u7684\u5904\u7406\uFF0C\u6BD4\u5982\u7126\u70B9\u7684\u51B2\u7A81\u3001\u5FEB\u6377\u952E\u7684\u51B2\u7A81\u3001\u64CD\u4F5C\u7684\u65F6\u95F4\u987A\u5E8F\u7B49\u7B49\uFF0C\u6240\u4EE5\u52A1\u5FC5\u5148\u9605\u8BFB\u4E00\u4E0B\u5B8C\u6574\u7684\u6E90\u7801"),a("a",{attrs:{href:"https://github.com/wanglin2/mind-map/blob/main/web/src/pages/Edit/components/Outline.vue"}},[s._v("Outline.vue")]),s._v("\u3002")])])}],_={},i=_,v=n("2877"),c=Object(v.a)(i,l,e,!1,null,null,null);t.default=c.exports}}]);
