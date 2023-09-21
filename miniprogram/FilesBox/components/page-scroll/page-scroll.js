// components/page-scroll/page-scroll.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    topHeight:{
      type:String,
      value:''
    },
    hasTabbar:{
      type:Boolean,
      value:true
    },
    customStyle:{
      type:String,
      value:''
    },
    refresherBackground:{
      type:String,
      value:'#ffffff'
    },
    isPullDownRefresh:{
      type:Boolean,
      value:false
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    systemInfo:wx.getSystemInfoSync(),
    isPullDownRefresh:false
  },

  /**
   * 组件的方法列表
   */
  methods: {
    // scroll(res) {
    //   // console.log(res.detail.scrollTop)
    //   //如果距顶部小于10，则允许下拉刷新
    //   if (res.detail.scrollTop < 10) {
    //     this.setData({
    //       enablerefresh: true
    //     })
    //   }
    // },
    onReachBottom:function(params) {
      this.triggerEvent("onReachBottom",{})
    },
    onPageScroll:function(e) {
      this.triggerEvent('onPageScroll',e.detail)
    },
    onPullDownRefresh:function(params) {
      this.triggerEvent("onPullDownRefresh",{})
      this.setData({isPullDownRefresh:true})
      setTimeout(() => {
        this.setData({isPullDownRefresh:false})
      }, 2000);
    }
  }
})
