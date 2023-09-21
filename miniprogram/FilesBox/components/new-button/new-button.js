// components/new-button/new-button.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    width:{
      type:String,
      value:'300rpx'
    },
    text:{
      type:String,
      value:''
    },
    fn:{
      type:String,
      value:''
    },
    customStyle:{
      type:String,
      value:''
    },
    outStyle:{
      type:String,
      value:''
    },
    forbidden:{
      type:Boolean,
      value:false
    },
    fixedBottom:{
      type:Boolean,
      value:false
    },
    isComponent:{
      type:String,
      value:false
    },
    trigger:{
      type:Boolean,
      value:false
    }
  },

  /**
   * 组件的初始数据
   */
  data: {

  },
  lifetimes:{
    attached(){
      const PAGES = getCurrentPages();
      // 获取当前页面
      this.currentPage = PAGES[PAGES.length - 1]; 
    }
  },
  /**
   * 组件的方法列表
   */
  methods: {
    submit:function(e) {
      let { forbidden, isComponent, fn } = this.properties;
      if(forbidden) return;
      if(this.properties.trigger){
        this.triggerEvent('confirm');
        return;
      }
      if(isComponent) this.currentPage.selectComponent(`#${isComponent}`)[fn]();
      else this.currentPage[this.properties.fn]();
    }
  }
})
