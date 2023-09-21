import {imageUrl} from '../../utils/image';
import {req} from '../../utils/service';
import {URL} from '../../utils/config';
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    dataTree: {
      type: Array,
      value: [],
    },
    checkrule: {
      type: Array,
      value: []
    },
    treeListIndex: { // 当期树形列表的索引
      type: Number,
      value: 1
    },
    isOpenAll: { // 是否展开全部节点
      type: Boolean,
      value: false
    }
  },
  observers: {
    'dataTree': function (params) {
      var arr=[]
      if(this.properties.checkrule.length>0){
        this.setData({
          allChoiceIdList:this.properties.checkrule
        })
        arr = this.showcheck(params)
      }else{
        arr=params
      }
      this.setData({
        tree: this._initSourceData(arr),
      })
    }
  },
  /**
   * 组件的初始数据
   */
  data: {
    tree: [],
    unzipList:['解压到当前','解压到...'],
    imageUrl,
    lang:globalData.lang,
  },
  /**
   * 组件的方法列表
   */
  methods: {
    isOpen(e) {
      const open = 'tree[' + e.currentTarget.dataset.index + '].open'
      this.setData({
        [open]: !this.data.tree[e.currentTarget.dataset.index].open
      })
    },
    _initSourceData(nodes) {
      nodes.forEach(element => {
        element.open = this.properties.isOpenAll // 是否展开
        if (element.childList && element.childList.length > 0) element.childList = this._initSourceData(element.childList)
      })
      return nodes
    },
    showOperate(e){
      let item = e.currentTarget.dataset.item
      // this.setData({
      //   operateShow:true,
      //   checkedItem:item
      // })
      this.triggerEvent('showOperate',{checkedItem:item})
    },
    setCheckedItem(e){
      let item = e.detail.checkedItem
      this.triggerEvent('showOperate',{checkedItem:item})
    },
    closeOperate(){
      this.setData({
        operateShow:false
      })
    },
    showEncrypt(){
      this.setData({
        encryptShow:!this.data.encryptShow
      })
    },
    showPassword(){
      this.setData({
        showPassword:!this.data.showPassword
      })
    },
    textInput(e){
      this.setData({
        password:e.detail
      })
    },
    confirmUnzip(){
      let {password} = this.data
      if(password==null){
        wx.showToast({
          title: '请输入压缩包密码',
          icon:'none'
        })
        return
      }
      this.previewFile()
    },
    // 预览文件
    previewFile() {
      let {sourceID,fileName,index,sort,encrypted} = this.data.checkedItem,previewUrl,{password} = this.data;
      let param = {fullName:fileName,sourceID:sourceID,index:index}
      this.closeOperate()
      if(encrypted&&!password){
        this.showEncrypt()
        return
      }
      if(password){
        param.password = password
      }
      wx.showLoading({
        title:'加载中...'
      })
      req('GET',URL.unzipList,param,{},false).then(data => {
        wx.hideLoading({})
        previewUrl = /http|https/.test(data) ? data : DOMAIN + data
        if(sort=='img'){
          wx.previewImage({
            urls: [`${previewUrl}`],
          })
          return;
        }else{
          wx.navigateTo({
              url: `/pages/link/link?url=${previewUrl}`,
          })
        }
      }).catch(err=>{
        wx.hideLoading({})
        wx.showToast({
          title: err.message,
          icon:'none'
        })
        this.showEncrypt()
        this.setData({
          password:''
        })
      })
    },
    showUnzipList(){
      this.closeOperate()
      this.setData({
        unzipListShow:!this.data.unzipListShow
      })
    },
    unzipFile(e){
      let {index} = e.currentTarget.dataset
      this.showUnzipList()
      this.setData({
        unzipListShow:false,
      })
      this.triggerEvent('unzipFile',{index:index,zipShow:false,choseIndex:this.data.checkedItem.index,fullName:this.data.checkedItem.fileName,directory:this.data.checkedItem.directory})
    },
    childUnzipFile(e){
      this.triggerEvent('unzipFile',{index:e.detail.index,zipShow:false,choseIndex:e.detail.choseIndex,fullName:e.detail.fullName,directory:e.detail.directory})
    }
  }
})
 