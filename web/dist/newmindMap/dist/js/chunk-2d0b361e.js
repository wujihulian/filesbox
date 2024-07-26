(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2d0b361e"],{"27ad":function(p,t,e){"use strict";e.r(t);var n=function(){var s=this;return s._self._c,s._m(0)},r=[function(){var s=this,a=s._self._c;return a("div",[a("h1",[s._v("Watermark plugin")]),a("blockquote",[a("p",[s._v("0.2.24+")])]),a("p",[a("code",[s._v("Watermark")]),s._v(" instance is responsible for displaying the watermark.")]),a("p",[s._v("Please refer to the "),a("a",{attrs:{href:"/mind-map/#/doc/zh/constructor"}},[s._v("Instantiation Options")]),s._v(" of the "),a("code",[s._v("MindMap")]),s._v(" class for configuration.")]),a("h2",[s._v("Register")]),a("pre",{staticClass:"hljs"},[a("code",[a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" MindMap "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map'")]),s._v(`
`),a("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" Watermark "),a("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),a("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/plugins/Watermark.js'")]),s._v(`
`),a("span",{staticClass:"hljs-comment"},[s._v("// import Watermark from 'simple-mind-map/src/Watermark.js' Use this path for versions below v0.6.0")]),s._v(`

MindMap.usePlugin(Watermark)
`)])]),a("p",[s._v("After registration and instantiation of "),a("code",[s._v("MindMap")]),s._v(", the instance can be obtained through "),a("code",[s._v("mindMap.watermark")]),s._v(".")]),a("h2",[s._v("Methods")]),a("h3",[s._v("draw()")]),a("p",[s._v("Redraw the watermark.")]),a("p",[s._v("Note: For imprecise rendering, some watermarks beyond the visible area will be drawn. If you have extreme performance requirements, it is recommended to develop the watermark function yourself.")]),a("h3",[s._v("updateWatermark(config)")]),a("p",[s._v("Update watermark config. Example:")]),a("pre",{staticClass:"hljs"},[a("code",[s._v(`mindMap.watermark.updateWatermark({
    `),a("span",{staticClass:"hljs-attr"},[s._v("text")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'Watermark text'")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("lineSpacing")]),s._v(": "),a("span",{staticClass:"hljs-number"},[s._v("100")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("textSpacing")]),s._v(": "),a("span",{staticClass:"hljs-number"},[s._v("100")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("angle")]),s._v(": "),a("span",{staticClass:"hljs-number"},[s._v("50")]),s._v(`,
    `),a("span",{staticClass:"hljs-attr"},[s._v("textStyle")]),s._v(`: {
      `),a("span",{staticClass:"hljs-attr"},[s._v("color")]),s._v(": "),a("span",{staticClass:"hljs-string"},[s._v("'#000'")]),s._v(`,
      `),a("span",{staticClass:"hljs-attr"},[s._v("opacity")]),s._v(": "),a("span",{staticClass:"hljs-number"},[s._v("1")]),s._v(`,
      `),a("span",{staticClass:"hljs-attr"},[s._v("fontSize")]),s._v(": "),a("span",{staticClass:"hljs-number"},[s._v("20")]),s._v(`
    }
})
`)])]),a("h3",[s._v("hasWatermark()")]),a("blockquote",[a("p",[s._v("v0.3.2+")])]),a("p",[s._v("Gets whether the watermark exists.")])])}],i={},l=i,v=e("2877"),_=Object(v.a)(l,n,r,!1,null,null,null);t.default=_.exports}}]);
