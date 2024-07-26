(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2d0c5538"],{"3f2a":function(e,a,n){"use strict";n.r(a);var l=function(){var s=this;return s._self._c,s._m(0)},v=[function(){var s=this,t=s._self._c;return t("div",[t("h1",[s._v("\u4E3B\u9898")]),t("h2",[s._v("\u4F7F\u7528\u548C\u5207\u6362\u4E3B\u9898")]),t("p",[t("code",[s._v("simple-mind-map")]),s._v("\u5185\u7F6E\u4E86\u5F88\u591A\u4E3B\u9898\uFF0C\u53EF\u4EE5\u901A\u8FC7\u5982\u4E0B\u65B9\u5F0F\u83B7\u53D6\u5230\u6240\u6709\u7684\u5185\u7F6E\u4E3B\u9898\u5217\u8868\uFF1A")]),t("pre",{staticClass:"hljs"},[t("code",[t("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" { themeList } "),t("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),t("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map/src/constants/constant'")]),s._v(`
`),t("span",{staticClass:"hljs-comment"},[s._v("// import { themeList } from 'simple-mind-map/src/utils/constant' v0.6.0\u4EE5\u4E0B\u7248\u672C\u4F7F\u7528\u8BE5\u8DEF\u5F84")]),s._v(`
`)])]),t("blockquote",[t("p",[s._v("v0.6.8+\uFF0C\u4E3B\u9898\u5217\u8868\u589E\u52A0\u4E86\u4EE3\u8868\u662F\u5426\u662F\u6697\u9ED1\u4E3B\u9898\u7684\u5B57\u6BB5dark\uFF0C\u4F60\u53EF\u4EE5\u6839\u636E\u8FD9\u4E2A\u5B57\u6BB5\u6765\u5C06\u754C\u9762\u5207\u6362\u4E3A\u6697\u9ED1\u6A21\u5F0F\u3002")])]),t("p",[s._v("\u53EF\u4EE5\u5728\u5B9E\u4F8B\u5316"),t("code",[s._v("simple-mind-map")]),s._v("\u65F6\u6307\u5B9A\u4F7F\u7528\u7684\u4E3B\u9898\uFF1A")]),t("pre",{staticClass:"hljs"},[t("code",[t("span",{staticClass:"hljs-keyword"},[s._v("new")]),s._v(` MindMap({
    `),t("span",{staticClass:"hljs-attr"},[s._v("theme")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'minions'")]),s._v(`
})
`)])]),t("p",[s._v("\u5982\u679C\u60F3\u52A8\u6001\u5207\u6362\u4E3B\u9898\u4E5F\u5F88\u7B80\u5355\uFF1A")]),t("pre",{staticClass:"hljs"},[t("code",[s._v("mindMap.setTheme("),t("span",{staticClass:"hljs-string"},[s._v("'classic'")]),s._v(`)
`)])]),t("p",[s._v("\u5982\u679C\u8981\u83B7\u53D6\u5F53\u524D\u4F7F\u7528\u7684\u4E3B\u9898\u540D\u79F0\u53EF\u4EE5\u4F7F\u7528\uFF1A")]),t("pre",{staticClass:"hljs"},[t("code",[t("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(` theme = mindMap.getTheme()
`)])]),t("h2",[s._v("\u5B9A\u4E49\u65B0\u4E3B\u9898")]),t("p",[s._v("\u9664\u4E86\u53EF\u4EE5\u4F7F\u7528\u5185\u7F6E\u7684\u4E3B\u9898\u5916\uFF0C\u4F60\u4E5F\u53EF\u4EE5\u81EA\u5B9A\u4E49\u65B0\u4E3B\u9898\uFF1A")]),t("pre",{staticClass:"hljs"},[t("code",[t("span",{staticClass:"hljs-keyword"},[s._v("import")]),s._v(" MindMap "),t("span",{staticClass:"hljs-keyword"},[s._v("from")]),s._v(" "),t("span",{staticClass:"hljs-string"},[s._v("'simple-mind-map'")]),s._v(`

`),t("span",{staticClass:"hljs-comment"},[s._v("// \u6CE8\u518C\u65B0\u4E3B\u9898")]),s._v(`
MindMap.defineTheme(`),t("span",{staticClass:"hljs-string"},[s._v("'\u4E3B\u9898\u540D\u79F0'")]),s._v(`, {
    `),t("span",{staticClass:"hljs-comment"},[s._v("// \u4E3B\u9898\u914D\u7F6E")]),s._v(`
})

`),t("span",{staticClass:"hljs-comment"},[s._v("// 1.\u5B9E\u4F8B\u5316\u65F6\u4F7F\u7528\u65B0\u6CE8\u518C\u7684\u4E3B\u9898")]),s._v(`
`),t("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" mindMap = "),t("span",{staticClass:"hljs-keyword"},[s._v("new")]),s._v(` MindMap({
    `),t("span",{staticClass:"hljs-attr"},[s._v("theme")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'\u4E3B\u9898\u540D\u79F0'")]),s._v(`
})

`),t("span",{staticClass:"hljs-comment"},[s._v("// 2.\u52A8\u6001\u5207\u6362\u65B0\u4E3B\u9898")]),s._v(`
mindMap.setTheme(`),t("span",{staticClass:"hljs-string"},[s._v("'\u4E3B\u9898\u540D\u79F0'")]),s._v(`)
`)])]),t("p",[s._v("\u6700\u597D\u5728\u5B9E\u4F8B\u5316\u4E4B\u524D\u8FDB\u884C\u6CE8\u518C\uFF0C\u8FD9\u6837\u5728\u5B9E\u4F8B\u5316\u65F6\u53EF\u4EE5\u76F4\u63A5\u4F7F\u7528\u65B0\u6CE8\u518C\u7684\u4E3B\u9898\u3002")]),t("p",[s._v("\u4E00\u4E2A\u4E3B\u9898\u5176\u5B9E\u5C31\u662F\u4E00\u4E2A\u666E\u901A\u7684\u5BF9\u8C61\uFF0C\u5B8C\u6574\u914D\u7F6E\u53EF\u4EE5\u53C2\u8003"),t("a",{attrs:{href:"https://github.com/wanglin2/mind-map/blob/main/simple-mind-map/src/themes/default.js"}},[s._v("\u9ED8\u8BA4\u4E3B\u9898")]),s._v("\uFF0C"),t("code",[s._v("defineTheme")]),s._v("\u65B9\u6CD5\u4F1A\u628A\u4F60\u4F20\u5165\u7684\u914D\u7F6E\u548C\u9ED8\u8BA4\u914D\u7F6E\u505A\u5408\u5E76\u3002\u5927\u90E8\u5206\u4E3B\u9898\u5176\u5B9E\u9700\u8981\u81EA\u5B9A\u4E49\u7684\u90E8\u5206\u4E0D\u662F\u5F88\u591A\uFF0C\u4E00\u4E2A\u5178\u578B\u7684\u81EA\u5B9A\u4E49\u4E3B\u9898\u914D\u7F6E\u53EF\u4EE5\u53C2\u8003"),t("a",{attrs:{href:"https://github.com/wanglin2/mind-map/blob/main/simple-mind-map/src/themes/blueSky.js"}},[s._v("blueSky")]),s._v("\u3002")]),t("pre",{staticClass:"hljs"},[t("code",[s._v("MindMap.defineTheme("),t("span",{staticClass:"hljs-string"},[s._v("'redSpirit'")]),s._v(`, {
    `),t("span",{staticClass:"hljs-comment"},[s._v("// \u80CC\u666F\u989C\u8272")]),s._v(`
    `),t("span",{staticClass:"hljs-attr"},[s._v("backgroundColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(255, 238, 228)'")]),s._v(`,
    `),t("span",{staticClass:"hljs-comment"},[s._v("// \u8FDE\u7EBF\u7684\u989C\u8272")]),s._v(`
    `),t("span",{staticClass:"hljs-attr"},[s._v("lineColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(230, 138, 131)'")]),s._v(`,
    `),t("span",{staticClass:"hljs-attr"},[s._v("lineWidth")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("3")]),s._v(`,
    `),t("span",{staticClass:"hljs-comment"},[s._v("// \u6982\u8981\u8FDE\u7EBF\u7684\u7C97\u7EC6")]),s._v(`
    `),t("span",{staticClass:"hljs-attr"},[s._v("generalizationLineWidth")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("3")]),s._v(`,
    `),t("span",{staticClass:"hljs-comment"},[s._v("// \u6982\u8981\u8FDE\u7EBF\u7684\u989C\u8272")]),s._v(`
    `),t("span",{staticClass:"hljs-attr"},[s._v("generalizationLineColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(222, 101, 85)'")]),s._v(`,
    `),t("span",{staticClass:"hljs-comment"},[s._v("// \u6839\u8282\u70B9\u6837\u5F0F")]),s._v(`
    `),t("span",{staticClass:"hljs-attr"},[s._v("root")]),s._v(`: {
      `),t("span",{staticClass:"hljs-attr"},[s._v("fillColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(207, 44, 44)'")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("color")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(255, 233, 157)'")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("borderColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("''")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("borderWidth")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("0")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("fontSize")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("24")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("active")]),s._v(`: {
        `),t("span",{staticClass:"hljs-attr"},[s._v("borderColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(255, 233, 157)'")]),s._v(`,
        `),t("span",{staticClass:"hljs-attr"},[s._v("borderWidth")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("3")]),s._v(`,
      }
    },
    `),t("span",{staticClass:"hljs-comment"},[s._v("// \u4E8C\u7EA7\u8282\u70B9\u6837\u5F0F")]),s._v(`
    `),t("span",{staticClass:"hljs-attr"},[s._v("second")]),s._v(`: {
      `),t("span",{staticClass:"hljs-attr"},[s._v("fillColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(255, 255, 255)'")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("color")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(211, 58, 21)'")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("borderColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(222, 101, 85)'")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("borderWidth")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("2")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("fontSize")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("18")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("active")]),s._v(`: {
        `),t("span",{staticClass:"hljs-attr"},[s._v("borderColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(255, 233, 157)'")]),s._v(`,
      }
    },
    `),t("span",{staticClass:"hljs-comment"},[s._v("// \u4E09\u7EA7\u53CA\u4EE5\u4E0B\u8282\u70B9\u6837\u5F0F")]),s._v(`
    `),t("span",{staticClass:"hljs-attr"},[s._v("node")]),s._v(`: {
      `),t("span",{staticClass:"hljs-attr"},[s._v("fontSize")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("14")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("color")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(144, 71, 43)'")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("active")]),s._v(`: {
        `),t("span",{staticClass:"hljs-attr"},[s._v("borderColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(255, 233, 157)'")]),s._v(`
      }
    },
    `),t("span",{staticClass:"hljs-comment"},[s._v("// \u6982\u8981\u8282\u70B9\u6837\u5F0F")]),s._v(`
    `),t("span",{staticClass:"hljs-attr"},[s._v("generalization")]),s._v(`: {
      `),t("span",{staticClass:"hljs-attr"},[s._v("fontSize")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("14")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("fillColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(255, 247, 211)'")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("borderColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(255, 202, 162)'")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("borderWidth")]),s._v(": "),t("span",{staticClass:"hljs-number"},[s._v("2")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("color")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(187, 101, 69)'")]),s._v(`,
      `),t("span",{staticClass:"hljs-attr"},[s._v("active")]),s._v(`: {
        `),t("span",{staticClass:"hljs-attr"},[s._v("borderColor")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'rgb(222, 101, 85)'")]),s._v(`
      }
    }
})
`)])]),t("h2",[s._v("\u5B8C\u6574\u793A\u4F8B")]),t("iframe",{staticStyle:{width:"100%",height:"455px",border:"none"},attrs:{src:"https://wanglin2.github.io/playground/#eNrFV+tvG0UQ/1dWh9A5yDk/0wTjVIXCB6QGoRaJD7kIne/W9rbn3eN2nUcjSxBQadpUBYEoL6ESiZIPSKCCSh5U+Wf8SP8LZm/vZfuSlqpSP/ixszO/mf3N7Nzcpvam5xmrXazVtDq3feIJxLHoeudNSjoe8wXaRD5u5hGjS6xLBXbyiLct12Vrl3ET9VDTZx2kA4IeWywR6ixZntoyNQ5iF892QDrbsTxTMylCJnWxQFImNRcR7bqukhcKaPD468H2neHxJ8O/j0Y/fH5ya2u0dTC4ee/klz2T2oxygSxbkFX8HnMwB+skotzyyoxJFcrNL4Z3dvv7R092v4vM7LZFW/iDNu5gMMvNoMXzaFP6DSMx4PDBbk63XYtzYusA1wsRh3/tDW7sDA4e9R8fj77ZG3775zi6g5uEZqGHjBgphZzuY+eKR3wi9LzSQgh8nHy2M/z+jye7P51sP1TChmVfa/lAvnORucyvId1vNXLlubk8KlcW4Ku8MKPnE4Djn0eHx0BbGsMFt2PWlWIelaR1qVKKraXWh8QR7RqqJIDDB1snDz6NYUcP742ObqjdFqbYt1xy3RKE0UvPYp6Oatp8LMZyGcIrlvJoYS59wOH9A1URw/v/DP69q+Q+Y6IW0YhQk7juGFZxPo+qVfmJoRCyp/msgMu5+ZROg/kO9iOsSXl43GIsbjIqrpDruIbK1VioijUV3iTsWQFMuIqYRQiqMvhJmOkf7owOf5smh2OoT+csegLf4ddpBJUgE3Oy3pKCyTxHVtomDlHO4Ku08GL4yqBlfxtoGdy91T/6tb9/e5ofCm0kzU4SU5LDNBUlWUnzcMJqJeX4eaI+Pejw2kzFOn5nnhZ1dqKrcBkgnWenMdAsylyeKz89j2P0LAB+UAHnXv/f/IxVzyQ90IdFDxpyVr9O2mnUseNHVm660ctHDl6LGnMujAm7NeQwu9vBVBgtLN5xAZqKtzbedXJ6aHkReLagVfn6THg0xxJW6limJgWmNnZSKRZ4XUixqcUtTD0M06mXinabuI6PqVReTjAm4DK9THpKt4TE2bjDDKcryV5a7yVFEP0NZZEdoURchr4vp4D3GSfqQizrLm7KZ6puQ+ogTSuhupB1AmUGeQRFHhRXb+YNNXUEd27041eDL38P71wwffQPb/ePHqULjtGcLvvFR6qcwU9OLvNBE7lEuEhKLSr5YEoxVi23K8eCSE+5h1KFT72gZi+YumAhMAxMlsCwQqjukFUUzCKLphYG8TbuMFMLtkMF4iS7cYGCSr0Au2nFCEkw5jYsqaI2TVFvdIVgFF2wXWJfA5XUrARq6WmqXlC6oS1AT9qmBh2wPW1umsBJgo3+1QspLmDJxYaraLkQzpqmZhTUgBnNWJh3DJtzU4sza6RoizKzphpYqVh8NdBDyIsryMfgEfIWbATFJz+vTNIbQSWGVoMztyuUIYxTUIcwG4QrwbxkMe2+jUmrDerVYtFbjzxn+30t8tyx/BYBvxGqZzkOoa1IEIduhNl+xohLUQRh0PEaAKFSgxxoeU1lQI70xlXOKLxBBPBmuAEZiDuDqcELgmoHRgH+Gj70ZdLBMlmzDZ+tcewDiKmFVzXjpUHatoXweK1QcNc/pnyDG4zzWZvONjC5Csc24Km40aU2N2zWKUCZY8EzakO6CQ/T03r/AbfdVt0="}})])}],i={},_=i,r=n("2877"),c=Object(r.a)(_,l,v,!1,null,null,null);a.default=c.exports}}]);
