import {imageUrl} from '../../utils/image';
const {globalData} = getApp();
Component({
  properties:{
    titleText:{
      type:String,
      value:'FilesBox'
    },
    showBack:{
      type:Boolean,
      value:false,
    },
  },
  data: {
    imageUrl,
    statusBarHeight:globalData.systemInfo.statusBarHeight
  },
  methods:{
    goBack(){
      wx.navigateBack({
        delta:0
      })
    }
  }
})