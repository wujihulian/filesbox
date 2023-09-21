import{renderSize,timeFromNow,timeFormat,genUuid} from '../../utils/util';
import {req} from '../../utils/service';
import {URL} from '../../utils/config';
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Component({
  properties:{
    // attributeShow:{
    //   type:Boolean,
    //   value:false
    // }
  },
  data: {
    statusBarHeight:globalData.systemInfo.statusBarHeight,
    lang:globalData.lang,
    formatShow:false,
    actionShow: false,
    actions: [],
  },
  methods:{
    getFormat(param){
      param.size = renderSize(param.size)
      // console.log(param)
      let {actions} = this.data
      if(param.fileType=='pdf'){
        actions = [
          {
            name: 'doc',
          },
          {
            name: 'docx',
          },
          {
            name: 'jpg',
          },
          {
            name: 'png',
          },
        ]
      }else if(param.fileType=='doc'||param.fileType=='docx'){
        actions = [
          {
            name: 'pdf',
          },
          {
            name: 'jpg',
          },
          {
            name: 'png',
          },
        ]
      }else if(param.fileType=='ppt'||param.fileType=='pptx'){
        actions = [
          {
            name: 'pdf',
          },
          {
            name: 'jpg',
          },
          {
            name: 'png',
          },
        ]
      }else{
        actions = [
          {
            name: 'pdf',
          }
        ]
      }
      this.setData({
        formatShow:true,
        dataList:param,
        actions,
        formatFileType:actions[0].name
      })
    },
    onClose(){
      this.setData({
        formatShow:false
      })
    },
    showAction(){
      this.setData({
        actionShow:!this.data.actionShow
      })
    },
    onSelect(e){
      let {name} = e.detail
      this.setData({
        formatFileType:name
      })
    },
    confirm(){
      let {sourceID} = this.data.dataList,{formatFileType} = this.data
      this.setData({
        actionShow:false,
        formatShow:false
      })
      this.triggerEvent('onConvertFile',{suffix:formatFileType,sourceID})
    }
  },
})