(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2d20f137"],{b1a3:function(h,a,e){"use strict";e.r(a);var n=function(){var s=this;return s._self._c,s._m(0)},i=[function(){var s=this,t=s._self._c;return t("div",[t("h1",[s._v("Deploy")]),t("p",[s._v("The 'web' directory of this project provides a complete project developed based on the 'simple mind map' library, 'Vue2. x', and 'ElementUI'. The data is stored locally on the computer by default, and can also be manipulated locally on the computer. Originally intended as an online 'demo', it can also be directly used as an online version of the mind map application, online address: "),t("a",{attrs:{href:"https://wanglin2.github.io/mind-map/"}},[s._v("https://wanglin2.github.io/mind-map/")]),s._v(".")]),t("p",[s._v("If your network environment is slow to access the 'GitHub' service, you can also deploy it to your server.")]),t("h2",[s._v("Deploying to a static file server")]),t("p",[s._v("The project itself does not rely on the backend, so it can be deployed to a static file server. The following commands can be executed in sequence:")]),t("pre",{staticClass:"hljs"},[t("code",[s._v("git "),t("span",{staticClass:"hljs-built_in"},[s._v("clone")]),s._v(` https://github.com/wanglin2/mind-map.git
`),t("span",{staticClass:"hljs-built_in"},[s._v("cd")]),s._v(` mind-map
`),t("span",{staticClass:"hljs-built_in"},[s._v("cd")]),s._v(` simple-mind-map
npm i
npm link
`),t("span",{staticClass:"hljs-built_in"},[s._v("cd")]),s._v(` ..
`),t("span",{staticClass:"hljs-built_in"},[s._v("cd")]),s._v(` web
npm i
npm link simple-mind-map
`)])]),t("p",[s._v("Then you can choose to start the local service:")]),t("pre",{staticClass:"hljs"},[t("code",[s._v(`npm run serve
`)])]),t("p",[s._v("You can also directly package and generate construction products:")]),t("pre",{staticClass:"hljs"},[t("code",[s._v(`npm run build
`)])]),t("p",[s._v("The packaged entry page 'index.html' can be found in the project root directory, and the corresponding static resources are located in the 'dist' directory under the root directory. The 'html' file will access the resources in the 'dist' directory through relative paths, such as 'dist/xxx'. You can directly upload these two files or directories to your static file server. In fact, this project is deployed to 'GitHub Pages' in this way.")]),t("p",[s._v("If you do not have any code modification requirements, it is also possible to directly copy these files from this repository.")]),t("p",[s._v("If you want to package 'index.html' into the 'dist' directory as well, you can modify the 'scripts.build' command in the 'web/package.json' file to delete '&& node ../copy.js' from 'vue-cli-service build && node ../copy.js'.")]),t("p",[s._v("If you want to modify the directory for packaging output, you can modify the 'outputDir' configuration of the 'web/vue.config.js' file to the path you want to output.")]),t("p",[s._v("If you want to modify the path of the 'index. html' file referencing static resources, you can modify the 'publicPath' configuration of the 'web/vue.config.js' file. And the "),t("code",[s._v("window.externalPublicPath")]),s._v(" config in "),t("code",[s._v("web/public/index.html")]),s._v(" file.")]),t("p",[s._v("In addition, the default route used is 'hash ', which means that there will be '#'in the path. If you want to use the 'history' route, you can modify the 'web/src/router.js' file to:")]),t("pre",{staticClass:"hljs"},[t("code",[t("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" router = "),t("span",{staticClass:"hljs-keyword"},[s._v("new")]),s._v(` VueRouter({
  routes
})
`)])]),t("p",[s._v("Change to:")]),t("pre",{staticClass:"hljs"},[t("code",[t("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" router = "),t("span",{staticClass:"hljs-keyword"},[s._v("new")]),s._v(` VueRouter({
  `),t("span",{staticClass:"hljs-attr"},[s._v("mode")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'history'")]),s._v(`,
  routes
})
`)])]),t("p",[s._v("However, this requires backend support, as our application is a single page client application. If the backend is not properly configured, users will return 404 when accessing sub routes directly in the browser. Therefore, you need to add a candidate resource on the server that covers all situations: if the 'URL' cannot match any static resources, the same 'index. html' page should be returned.")]),t("h2",[s._v("Docker")]),t("h2",[s._v("Docker")]),t("blockquote",[t("p",[s._v("Thank you very much "),t("a",{attrs:{href:"https://github.com/shuiche-it"}},[s._v("\u6C34\u8F66")]),s._v(", This section is written by him, and the corresponding Docker package is also maintained by him.")])]),t("p",[s._v("Install directly from Docker Hub:")]),t("pre",{staticClass:"hljs"},[t("code",[s._v(`docker run -d -p 8081:8080 shuiche/mind-map:latest
`)])]),t("p",[s._v("Mindmap has activated port 8080 as the web service entry point in the container. When running the container through Docker, it is necessary to specify a local mapping port. In the above case, we mapped the local port 8081 to the container port 8080.")]),t("p",[s._v("After the installation is completed, check the container's running status through 'Docker PS'.")]),t("p",[s._v("Open 127.0.0.1:8081 in the browser to use the Web mind map function.")]),t("h2",[s._v("Docking with one's own storage services")]),t("p",[s._v("The application data is stored locally in the browser by default, and the local storage capacity of the browser is relatively small, so it is easy to trigger restrictions when inserting more images in the mind map. Therefore, a better choice is to dock with your own storage service, which usually has two ways:")]),t("h3",[s._v("The first")]),t("p",[s._v("Simply clone the warehouse code and modify the relevant methods in 'web/src/API/index.js' to obtain data from your database and store it in your data.")]),t("h3",[s._v("The second")]),t("p",[s._v("Many times, you may want to always use the latest code from this repository, so the first method is not very convenient because you need to manually merge the code, so the second method is provided.")]),t("p",[s._v("Specific operating steps:")]),t("ol",[t("li",[s._v("Copy the packaged resources of the web application")])]),t("p",[s._v("This includes the 'dist' directory and the 'index.html' file.")]),t("ol",{attrs:{start:"2"}},[t("li",[s._v("Modify the copied 'index.html' file")])]),t("p",[s._v("Firstly, insert the following code into the 'head' tag:")]),t("pre",{staticClass:"hljs"},[t("code",[s._v(`<script>
  `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".takeOverApp = "),t("span",{staticClass:"hljs-literal"},[s._v("true")]),s._v(`
</script>
`)])]),t("p",[s._v("This line of code will prompt the application not to initialize the application 'i.e.: new Vue()', but to give control to you. Next, insert your own 'js' code at the end of the 'body', either inline or out of chain. The inline example is as follows:")]),t("pre",{staticClass:"hljs"},[t("code",[s._v(`<script>
  `),t("span",{staticClass:"hljs-comment"},[s._v("// Your own method of requesting data")]),s._v(`
  `),t("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" getDataFromBackend = "),t("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
    `),t("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(" "),t("span",{staticClass:"hljs-keyword"},[s._v("new")]),s._v(" "),t("span",{staticClass:"hljs-built_in"},[s._v("Promise")]),s._v("("),t("span",{staticClass:"hljs-function"},[s._v("("),t("span",{staticClass:"hljs-params"},[s._v("resolve, reject")]),s._v(") =>")]),s._v(` {
      `),t("span",{staticClass:"hljs-built_in"},[s._v("setTimeout")]),s._v("("),t("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
        resolve({
          `),t("span",{staticClass:"hljs-comment"},[s._v("// MindMap data")]),s._v(`
          `),t("span",{staticClass:"hljs-attr"},[s._v("mindMapData")]),s._v(`: {
            `),t("span",{staticClass:"hljs-attr"},[s._v("root")]),s._v(`: {
              `),t("span",{staticClass:"hljs-string"},[s._v('"data"')]),s._v(`: {
                  `),t("span",{staticClass:"hljs-string"},[s._v('"text"')]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v('"\u6839\u8282\u70B9"')]),s._v(`
              },
              `),t("span",{staticClass:"hljs-string"},[s._v('"children"')]),s._v(`: []
            },
            `),t("span",{staticClass:"hljs-attr"},[s._v("theme")]),s._v(": { "),t("span",{staticClass:"hljs-string"},[s._v('"template"')]),s._v(":"),t("span",{staticClass:"hljs-string"},[s._v('"avocado"')]),s._v(","),t("span",{staticClass:"hljs-string"},[s._v('"config"')]),s._v(`:{} },
            `),t("span",{staticClass:"hljs-attr"},[s._v("layout")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v('"logicalStructure"')]),s._v(`,
            `),t("span",{staticClass:"hljs-attr"},[s._v("config")]),s._v(`: {},
            `),t("span",{staticClass:"hljs-attr"},[s._v("view")]),s._v(`: {}
          },
          `),t("span",{staticClass:"hljs-comment"},[s._v("// Page language, supporting Chinese (zh) and English (en)")]),s._v(`
          `),t("span",{staticClass:"hljs-attr"},[s._v("lang")]),s._v(": "),t("span",{staticClass:"hljs-string"},[s._v("'zh'")]),s._v(`,
          `),t("span",{staticClass:"hljs-comment"},[s._v("// Page Section Configuration")]),s._v(`
          `),t("span",{staticClass:"hljs-attr"},[s._v("localConfig")]),s._v(": "),t("span",{staticClass:"hljs-literal"},[s._v("null")]),s._v(`
        })
      }, `),t("span",{staticClass:"hljs-number"},[s._v("200")]),s._v(`)
    })
  }
  `),t("span",{staticClass:"hljs-comment"},[s._v("// Register Global Method")]),s._v(`
  `),t("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" setTakeOverAppMethods = "),t("span",{staticClass:"hljs-function"},[s._v("("),t("span",{staticClass:"hljs-params"},[s._v("data")]),s._v(") =>")]),s._v(` {
    `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(`.takeOverAppMethods = {}
    `),t("span",{staticClass:"hljs-comment"},[s._v("// Function for obtaining mind map data")]),s._v(`
    `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".takeOverAppMethods.getMindMapData = "),t("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
      `),t("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(` data.mindMapData
    } 
    `),t("span",{staticClass:"hljs-comment"},[s._v("// Functions for Saving Mind Map Data")]),s._v(`
    `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".takeOverAppMethods.saveMindMapData = "),t("span",{staticClass:"hljs-function"},[s._v("("),t("span",{staticClass:"hljs-params"},[s._v("data")]),s._v(") =>")]),s._v(` {
      `),t("span",{staticClass:"hljs-built_in"},[s._v("console")]),s._v(`.log(data)
      `),t("span",{staticClass:"hljs-comment"},[s._v("// The trigger frequency of this function may be high, so you should do throttling or anti shaking measures")]),s._v(`
    }
    `),t("span",{staticClass:"hljs-comment"},[s._v("// Function to obtain language")]),s._v(`
    `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".takeOverAppMethods.getLanguage = "),t("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
      `),t("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(` data.lang
    }
    `),t("span",{staticClass:"hljs-comment"},[s._v("// Functions for Saving Languages")]),s._v(`
    `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".takeOverAppMethods.saveLanguage = "),t("span",{staticClass:"hljs-function"},[s._v("("),t("span",{staticClass:"hljs-params"},[s._v("lang")]),s._v(") =>")]),s._v(` {
      `),t("span",{staticClass:"hljs-built_in"},[s._v("console")]),s._v(`.log(lang)
    }
    `),t("span",{staticClass:"hljs-comment"},[s._v("// Get locally configured functions")]),s._v(`
    `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".takeOverAppMethods.getLocalConfig = "),t("span",{staticClass:"hljs-function"},[s._v("() =>")]),s._v(` {
      `),t("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(` data.localConfig
    }
    `),t("span",{staticClass:"hljs-comment"},[s._v("// Save locally configured functions")]),s._v(`
    `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".takeOverAppMethods.saveLocalConfig = "),t("span",{staticClass:"hljs-function"},[s._v("("),t("span",{staticClass:"hljs-params"},[s._v("config")]),s._v(") =>")]),s._v(` {
      `),t("span",{staticClass:"hljs-built_in"},[s._v("console")]),s._v(`.log(config)
    }
  }
  `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".onload = "),t("span",{staticClass:"hljs-keyword"},[s._v("async")]),s._v(` () => {
    `),t("span",{staticClass:"hljs-keyword"},[s._v("if")]),s._v(" (!"),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".takeOverApp) "),t("span",{staticClass:"hljs-keyword"},[s._v("return")]),s._v(`
    `),t("span",{staticClass:"hljs-comment"},[s._v("// request data")]),s._v(`
    `),t("span",{staticClass:"hljs-keyword"},[s._v("const")]),s._v(" data = "),t("span",{staticClass:"hljs-keyword"},[s._v("await")]),s._v(` getDataFromBackend()
    `),t("span",{staticClass:"hljs-comment"},[s._v("// Method for setting global")]),s._v(`
    setTakeOverAppMethods(data)
    `),t("span",{staticClass:"hljs-comment"},[s._v("// Mind Map Instance Creation Completion Event")]),s._v(`
    `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".$bus.$on("),t("span",{staticClass:"hljs-string"},[s._v("'app_inited'")]),s._v(", "),t("span",{staticClass:"hljs-function"},[s._v("("),t("span",{staticClass:"hljs-params"},[s._v("mindMap")]),s._v(") =>")]),s._v(` {
      `),t("span",{staticClass:"hljs-built_in"},[s._v("console")]),s._v(`.log(mindMap)
    })
    `),t("span",{staticClass:"hljs-comment"},[s._v("// You can use window$ Bus$ On() to listen for some events in the application")]),s._v(`
    `),t("span",{staticClass:"hljs-comment"},[s._v("// Instantiate Page")]),s._v(`
    `),t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(`.initApp()
  }
</script>
`)])]),t("p",[s._v("As shown above, when you set the 'window.takeOverApp=true' flag, the application will no longer actively instantiate, but will expose the instantiated methods for you to call. You can first request the data of the mind map from the backend, and then register the relevant methods. The application will call internally at the appropriate time to achieve the purpose of echo and save.")]),t("p",[s._v("The advantage of doing this is that whenever the code in this repository is updated, you can simply copy the packaged files to your own server. With a slight modification of the 'index. html' page, you can achieve synchronous updates and use your own storage service.")]),t("h2",[s._v("Modifying Static Resource Paths")]),t("p",[s._v("If you want to maintain synchronous updates with the code in this repository as in the previous section, but also want to modify the storage location of static resources, for example, the default hierarchical relationship is:")]),t("pre",{staticClass:"hljs"},[t("code",[s._v(`-dist
--css
--fonts
--img
--js
-logo.ico

-index.html
`)])]),t("p",[s._v("And you want to adjust it to this:")]),t("pre",{staticClass:"hljs"},[t("code",[s._v(`-assets
--dist
---css
---fonts
---img
---js
-logo.ico

-index.html
`)])]),t("p",[s._v("So you can configure the 'window.externalPublicPath' in 'index.html' as the default "),t("code",[s._v("./dist/")]),s._v(" is modified to:")]),t("pre",{staticClass:"hljs"},[t("code",[t("span",{staticClass:"hljs-built_in"},[s._v("window")]),s._v(".externalPublicPath = "),t("span",{staticClass:"hljs-string"},[s._v("'./assets/dist/'")]),s._v(`
`)])]),t("p",[s._v("At the same time, the paths of the inline '.ico', '.js', and '.css' resources in 'index.html' need to be manually modified by you.")]),t("p",[s._v("It should be noted that it is best not to adjust the directory hierarchy within the 'dist' directory, otherwise exceptions may occur.")]),t("p",[s._v("If you want to replace some of the static resources, such as the theme image and structure image, with your own designed image, you can directly overwrite it with the same name.")])])}],o={},l=o,c=e("2877"),r=Object(c.a)(l,n,i,!1,null,null,null);a.default=r.exports}}]);
