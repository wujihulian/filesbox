// components/load-area/load-area.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    loadingState: {
      type:Number,
      value:1
    },
    type: {
      type:String,
      value:'default'
    },
    loadingTop:{
      type:String,
      value:'300rpx'
    },
    loadingColor:{
      type:String,
      value:'#4F91FF'
    },
    list:{
      type:Object,
      value:['1','1']
    },
    speed:{
      type:String,
      value:'0'
    }
  },

  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
    goLogin:function(params) {
      wx.navigateTo({
        url: '/pages/personal-login/personal-login',
      })
    }
  }
})
