// pages/chat/emoji/emoji.js
import { emoji } from '../../../utils/emoji';
Component({
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    STATIC_DOMAIN:'https://test-static.1x.cn'
  },

  /**
   * 组件的方法列表
   */
  methods: {
    choseEmoji:function(e){
      let { index, indexs } = e.currentTarget.dataset;
      let str = emoji[index*20+indexs];
      this.triggerEvent('changeEmoji',{
        value:str
      })
    },
    deleteEmoji:function(e){
      this.triggerEvent('delEmoji');
    }
  }
})
