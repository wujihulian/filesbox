// watermark.js
Component({
  data: {
    // 这里是一些组件内部数据
    watermarkText: '水印测试'
  },
  attached() {
    // 在组件实例进入页面节点树时执行
    // this.drowsyUserinfo()
    // console.log(wx.getStorageSync('markConfig'))
    this.setData({
      markConfig:wx.getStorageSync('markConfig'),
      watermarkText: (wx.getStorageSync('userInfo') || {}).name || '水印测试'
    })
  },
  methods: {
    // 这里是一个自定义方法
    drowsyUserinfo: function () {
        var markConfig = wx.getStorageSync('markConfig') || {};
        var ctx = wx.createCanvasContext("myCanvas");
        ctx.rotate(20 * Math.PI / 180); //设置文字的旋转角度，角度为45°；
        //对斜对角线以左部分进行文字的填充
        //用for循环达到重复输出文字的效果，这个for循环代表纵向循环
        for (let j = 1; j < 10; j++) { 
          ctx.beginPath();
          ctx.setFontSize(16);
          ctx.setFillStyle("rgba(0,155,169,.5)");
          ctx.fillText(markConfig.wmTitle, 0, 50 * j);
          for (let i = 1; i < 10; i++) { //这个for循环代表横向循环
            ctx.beginPath();
            ctx.setFontSize(15);
            ctx.setFillStyle("rgba(0,155,169,.5)");
            ctx.fillText(markConfig.wmTitle, 180 * i, 150 * j);
          }
        }
        //两个for循环的配合，使得文字充满斜对角线的左下部分
        //对斜对角线以右部分进行文字的填充逻辑同上
        for (let j = 0; j < 10; j++) {
          ctx.beginPath();
          ctx.setFontSize(16);
          ctx.setFillStyle("rgba(0,155,169,.5)");
          ctx.fillText(markConfig.wmTitle, 0, -50 * j);
          for (let i = 1; i < 10; i++) {
            ctx.beginPath();
            ctx.setFontSize(16);
            ctx.setFillStyle("rgba(0,155,169,.5)");
            ctx.fillText(markConfig.wmTitle, 180 * i, -150 * j);
          }
        }
        ctx.draw();
    }
  }
})
