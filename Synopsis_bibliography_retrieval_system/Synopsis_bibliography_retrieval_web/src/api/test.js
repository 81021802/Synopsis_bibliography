import myaxios from "@/utils/myaxios";//@就表示src目录的

/* myaxios.get('data.json').then(resp =>{
    console.log(resp.data)
}).catch(err =>{
    console.log("请求失败")
}) */

const promise1 = myaxios({
    method:'get',
    url:'data.json'
}).then(resp =>{
    console.log(resp.data)
})

//const BASE_URL='/dev-apis'//定义路径前缀作为常量
const BASE_URL=[process.env.VUE_APP_BASE_API]

export default{
    getList(){
        const promise1 = myaxios({
            method:'get',
            url:'/data.json'
        })
        return promise1
    }
}