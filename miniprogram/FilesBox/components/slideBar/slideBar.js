import {imageUrl} from '../../utils/image';
import {moreArr} from './config';
import {req} from '../../utils/service';
import {URL} from '../../utils/config';
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Component({
  properties:{
    slideShow:{
      type:Boolean,
      value:false
    },
    moreArr:{
      type:Array,
      value:[]
    },
    selectIndex:{
      type:Number,
      value:1
    }
  },
  data: {
    imageUrl,
    moreArr,
    lang:globalData.lang,
    statusBarHeight:globalData.systemInfo.statusBarHeight,
    moreIndex:0,
  },
  lifetimes:{
    attached(){
      this.setData({
        lang:globalData.lang
      })
    }
  },
  methods:{
    showNext(e){
      let{index} = e.currentTarget.dataset,{moreArr} = this.data;
      moreArr[index].show = !moreArr[index].show
      this.setData({
        moreArr,
      })
    },
    onShowSlide(){
      this.triggerEvent('onSlideShow',{slideShow:false})
    },
    cateSelect(e){
      let{index,indexs,item} = e.currentTarget.dataset,isAddShow
      this.setData({
        selectIndex:this.data.selectIndex==indexs?'null':indexs,
        moreIndex:index,
      })
      if(index==0&&indexs!=0){
        isAddShow = true
      }else{
        isAddShow = false
      }
      console.log(item)
      if(item.icon=='recycle'){
        item = {
          bgColor: "#F2F2FE",
          block: "recycle",
          name: "回收站",
          type: "recycle",
          url: "/images/icons/search_recycle_bin.png"
        }
        wx.navigateTo({
          url: '/pages/files-list/files-list?item='+JSON.stringify(item),
        })
        return
      }else if(item.icon=='info'){
        wx.navigateTo({
          url: '/pages/news/news',
        })
        return
      }
      this.triggerEvent('onSlideShow',{slideShow:false,item,isAddShow})
    },
    setIndex(){
      this.setData({
        selectIndex:-1
      })
    }
  }
})