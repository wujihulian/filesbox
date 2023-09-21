const {globalData} = getApp();
Component({
    properties: {
        selected:{
          type:String,
          value:'personal'
        },
        enableClickToPullDownRefresh:{
          type:Boolean,
          value:false
        },
        lang:{
            type:Object,
            value:globalData.lang
        }
    },
    data: {
        lang:globalData.lang,
        "color": "#ccc",
        "selectedColor": "#9a64ff",
        "backgroundColor": "#ffffff",
        hideTabBar:false,
        tabBar:[
            {
                "pagePath": "/pages/personal/personal",
                "iconPath": "/images/tabbarIcon/home.png",
                "selectedIconPath": "/images/tabbarIcon/home1.png",
                "cate": "personal",
                "isShow": true,
                "text": '主页'
            },
            {
                "pagePath": "/pages/organization/organization",
                "iconPath": "/images/tabbarIcon/cloud.png",
                "selectedIconPath": "/images/tabbarIcon/cloud1.png",
                "cate": "organization",
                "isShow": true,
                "text": "共享"
            },
            {
                "pagePath": "/pages/search/search",
                "iconPath": "/images/tabbarIcon/search.png",
                "selectedIconPath": "/images/tabbarIcon/search1.png",
                "cate": "search",
                "isShow": true,
                "text": "发现"
            },
            {
                "pagePath": "/pages/user-info/user-info",
                "iconPath": "/images/tabbarIcon/user.png",
                "selectedIconPath": "/images/tabbarIcon/user1.png",
                "cate": "userInfo",
                "isShow": true,
                "text": "我的"
            }
        ]
    },
    lifetimes:{
        attached(){
            this.setData({
                lang:globalData.lang
            })
        }
    },
    methods:{
        getSystemInfo:function(async = false) {
            // 页面初始化如果从不是自定义顶部导航的页面返回，获取到的数据不正确
            if(!async){
              let { windowHeight, safeArea, windowWidth } = wx.getSystemInfoSync();
              let paddingBottom = (windowHeight-safeArea.bottom)*(750/windowWidth)+100;
              this.setData({paddingBottom})
            }else{
              setTimeout(() => {
                let { windowHeight, safeArea, windowWidth } = wx.getSystemInfoSync();
                let paddingBottom = (windowHeight-safeArea.bottom)*(750/windowWidth)+100;
                this.setData({paddingBottom})
              }, 1000);
            }
        },
        switchTab:function(e){
            let { index, url, item } = e.currentTarget.dataset;
            wx.redirectTo({
                url: url,
            })
        }
    }
})