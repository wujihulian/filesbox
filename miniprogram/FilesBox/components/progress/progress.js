import {imageUrl} from '../../utils/image';
const {globalData} = getApp();
Component({
  properties:{
    bgColor:{
      type:String,
      value:'#fff',
    },
    showColor:{
      type:String,
      value:'#9966FF',
    },
    height:{
      type:Number,
      value:20,
    },
    percent:{
      type:String,
      value:'',
    }
  },
  data: {
    imageUrl,
    iconArr:[
      {
        url:imageUrl.dustbin,
        count:0
      },{
        url:imageUrl.cloudIcon,
        count:0
      },{
        url:imageUrl.remindFill,
        count:1,
      },{
        url:imageUrl.ellipsis
      }
    ],
    lang:globalData.lang
  },
  methods:{
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
    }
  }
})