import {imageUrl} from '../../utils/image';
const {globalData} = getApp();
Component({
  properties:{
    bgColor:{
      type:String,
      value:'#E4E4E4',
    },
    showColor:{
      type:String,
      value:'#4F91FF',
    },
  },
  data: {
    imageUrl,
    isList:false,
    lang:globalData.lang
  },
  methods:{
    switchList(){
      this.setData({isList:!this.data.isList})
    },
    goToPage(e){
      let{index,item} = e.currentTarget.dataset,url=''
      if(index==1){
        url = '/pages/transmissionCenter/transmissionCenter'
      }
      if(index==2){
        url = '/pages/messages/messages'
      }
      wx.navigateTo({
        url:url,
      })
    },
    showOperate(){
      this.setData({operateShow:!this.data.operateShow})
    }
  }
})