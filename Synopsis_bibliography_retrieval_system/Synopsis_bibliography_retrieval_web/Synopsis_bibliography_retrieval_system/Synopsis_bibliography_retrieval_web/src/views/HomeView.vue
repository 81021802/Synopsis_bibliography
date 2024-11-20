<template>
  <div class="hello">
    <!-- <img alt="logo" src="../assets/logo.png" width="200px"> -->
    <!-- <Hello></Hello>
    <h1>{{ msg }}</h1>
    <h3>{{ list }}</h3>
    <ul>
      <li v-for="(value,index) in list" :key="index">{{ value.bookname }}</li>
    </ul> -->
    <el-container style="height: 100vh;" >
      <!-- <el-header class="home-header" style="font-size: 12px;height: 60px;padding: 0px;">
      <el-menu :default-active="activeIndex" class="el-menu1" mode="horizontal" @select="handleSelect" 
      background-color="#409EFF"
      text-color="#FFFFFF"
      active-text-color="#ffd04b">
    <el-menu-item index="1" style="float: right">处理中心</el-menu-item>
    <el-submenu index="2" style="float: right">
    <template slot="title">我的工作台</template>
    <el-menu-item index="2-1">选项1</el-menu-item>
    <el-menu-item index="2-2">选项2</el-menu-item>
    <el-menu-item index="2-3">选项3</el-menu-item>
    <el-submenu index="2-4">
      <template slot="title">选项4</template>
      <el-menu-item index="2-4-1">选项1</el-menu-item>
      <el-menu-item index="2-4-2">选项2</el-menu-item>
      <el-menu-item index="2-4-3">选项3</el-menu-item>
    </el-submenu>
  </el-submenu>
  <el-menu-item index="3" style="float: right" disabled>消息中心</el-menu-item>
  <el-menu-item index="4" style="float: right"><a href="https://www.ele.me" target="_blank">订单管理</a></el-menu-item>
</el-menu>
    </el-header> -->
    <el-main class="home-main">
      <el-row>
        <el-col span="24" class="logo-col">
          <img alt="logo" src="../assets/logo.png" height="150px" style="padding-top: 50px">
        </el-col>
      </el-row>
      
      <el-row gutter="20" type="flex" justify="center">
      <el-col span="12" >
        <el-input class="query"
        style="width:100%;"
        size="medium"
        placeholder="请输入搜索内容"
        v-model="input "
        clearable>
        </el-input>
      </el-col>
    </el-row>
    <el-row :gutter="20" type="flex" justify="center" style="margin-top: -30px;">
      <el-col :span="4">
        <router-link to="/Search">
          <el-button type="primary" plain @click="fetchData">全文检索</el-button>
        </router-link>
      </el-col>
      <el-col :span="4">
        <router-link to="/ADretrieval">
          <el-button type="primary" plain @click="ADretrieval">高级检索</el-button>
        </router-link>     
      </el-col>
      <el-col :span="4">
        <router-link to="/Visualization">
          <el-button type="primary" plain @click="Visualization">数据可视化</el-button>
        </router-link>     
      </el-col>
    </el-row>
    </el-main>
    <!-- <el-footer class="home-footer">Footer</el-footer> -->
    </el-container>
    
  </div>
</template>

<script>
// @ is an alias to /src
import Hello from'../components/Hello.vue'
//import myaxios from '../utils/myaxios.js'
//import testApi from '@/api/test.js'
import searchApi from '@/api/search.js';

export default {
  data(){
      return {
        input: ''
      }
    },
  // // 钩子函数，组件创建完毕时执行
  // created(){
  //   this.fetchData();
  // },
  // methods:{
  //   fetchData(){//通过导入的test对象的getList()返回的时promise对象，然后就可以通过then执行成功的回调
  //     testApi.getall().then(resp =>{
  //       console.log(resp.data)
  //       this.list=resp.data
  //     }).catch(err =>{
  //       console.log("请求失败")
  //     });
  //   }
  // },
  // created(){
  //   this.fetchData();
  // },
  // methods: {
  //   fetchData(){searchApi.getList().then((res) => {
  //         console.log(res.data);
  //       }).catch(err =>{
  //       console.log("请求失败")
  //     });
  //   }
  // },
  // name: 'HomeView',
  // components: {
  //  Hello
  // },
  // props:{
  //   msg:String
  // }
}
</script>
<style>

/*   .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px; 
    height: 80vh;
  }
   */
  body > .el-container {
    margin-bottom: 40px;
  }
.home-main{
    background-color: #fff;
    color: #333;
    text-align: center;
    line-height: 100px;
    height: 80vh;
  }
  .logo-col{
    display: flex;
    justify-content: center;
    align-items: center;
  }

</style>