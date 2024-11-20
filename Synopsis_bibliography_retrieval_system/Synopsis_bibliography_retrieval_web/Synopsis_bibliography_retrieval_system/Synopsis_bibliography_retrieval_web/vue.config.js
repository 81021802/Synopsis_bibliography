/* 
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
 */

module.exports = {
  devServer:{
    port:8080,
    open:true,
    https:false,
    host:"localhost",
    proxy:{//代理配置
      //'/dev-apis':{
      [process.env.VUE_APP_BASE_API]:{
        //target:'http://localhost:8080/',
        target:process.env.VUE_APP_SERVICE_URL,
        changeOrigin:true,//开启代理服务进行请求转发
        pathRewrite:{
          //'^/dev-apis':''//重写路径
          ['^'+process.env.VUE_APP_BASE_API]:''
        }
      }
    }
  },
  lintOnSave:false, //关闭格式检查
  productionSourceMap:false, //打包时不生成map文件，加快打包构建
}