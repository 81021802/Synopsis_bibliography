import axios from "axios"
//发送get请求，请求data.json文件（会自动到public文件夹下找，不能显示写出public），
//请求成功输出data，请求失败则输出请求失败
/* axios.get('/data.json').then(resp =>{
    console.log(resp.data)

}).catch(err =>{
    console.log("请求失败")
})
 */
const myaxios = axios.create({
    baseURL:'http://8.134.9.133:9090',//基础路径就是Axios对象发送请求时路径的前缀
    //baseURL:process.env.VUE_APP_BASE_API,
    withCredentials: false, // 跨域请求时是否需要访问凭证
    timeout:100000,//毫秒
})
/* myaxios.get('/data.json').then(resp =>{
    console.log(resp.data)
}).catch(err =>{
    console.log("请求失败")
}) */
export default myaxios//导出自定义的Axios对象

// 请求拦截器
axios.interceptors.request.use(function (config){
    return config;//一定要返回，再发送到后端
},function (error){
    return Promise.reject(error);//抛出错误信息
});

// 响应拦截器
axios.interceptors.response.use(function (response){
    return response;//一定要返回响应，否则前端获取不到
},function (error){
    return Promise.reject(error);
})