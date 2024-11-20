<template>
  <div class="hello">
    <!-- <img alt="logo" src="../assets/logo.png" width="200px"> -->
    <!-- <Hello></Hello>
    <h1>{{ msg }}</h1>
    <h3>{{ list }}</h3>
    <ul>
      <li v-for="(value,index) in list" :key="index">{{ value.bookname }}</li>
    </ul> -->
    <el-container style="height: 80vh;" >
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
      <el-row style="padding: 20px;margin-top: 40px;">
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
        v-model="searchQuery"
        clearable>
        <el-button slot="append" icon="el-icon-search" @click="goToSearch"></el-button>
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
    <!-- 添加浮现书名的模板代码 -->
    <div v-for="book in floatingBooks" :key="book.id" :style="{ position: 'absolute', top: book.position.top, left: book.position.left, fontSize: book.fontSize }" class="floating-book">
      {{ book.name }}
    </div>
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
        searchQuery: '', // 用户输入的搜索内容
        bookNames: [],       // 从数据库获取的书名列表
        floatingBooks: [],    // 当前浮现的书名
      }
    },
  mounted() {
    this.fetchBookNames();
    this.startFloatingBooks();
  },
  methods: {
    fetchBookNames() {
      // 获取书名列表，可以通过 API 调用来获取数据
      this.bookNames = ["四库全书总目提要", "藏园群书经眼录", "中国善本书提要", "郘亭知见传本书目", "崇文总目辑释",
      "直斋书录解题","上海图书馆馆藏家谱提要","清人诗集叙录","文选楼藏书记","郑堂读书记","皕宋楼藏书志","郡斋读书志",
      "中南、西南地区省、市图书馆馆藏古籍稿本提要","铁琴铜剑楼藏书目录","中国通俗小说总目提要","嘉业堂藏书志","石庐金石书志",
      "五十万卷楼藏书目录初编","书画书录解题","荛圃藏书题识","持静斋藏书记要","曲海总目提要","爱日精庐藏书志",
      "天禄琳琅书目后编","文禄堂访书记","中国印谱解题","钦定天禄琳琅书目","四部备要书目提要","士礼居藏书题跋记","仪顾堂题跋",
      "拜经楼藏书题跋","中国农学书录","绛云楼题跋","读书敏求记","四库未收书目提要","中国史学名著题解","东湖丛记","滂喜斋藏书记",
      "楹书隅录","宝礼堂宋本书录","红雨楼题跋","绩语堂题跋","半毡斋题跋","古泉山馆题跋","日本访书志补","经义考补正"];
    },
    goToSearch() {
      // 跳转到 SearchView 页面，并传递 autoSearch 和 searchQuery 参数
      this.$router.push({ path: '/Search', query: { autoSearch: true, query: this.searchQuery } });
    },
    startFloatingBooks() {
      const headerHeight = 60; // header 高度
      const footerHeight = 60; // footer 高度
      const overlapThreshold = 50; // 重叠的最小距离阈值
      setInterval(() => {
        // 随机打乱书名列表，确保每次从不同的书名中选择
        const shuffledBooks = this.shuffleArray(this.bookNames);

        for (let i = 0; i < 7; i++) { // 每次浮现 7 个书名
          let position;

          // 重复生成位置，直到找到一个不与已有书名重叠的位置
          do{
            position = {
              // 确保书名浮现在 header 和 footer 之间，且上下各留出20px的空隙
              top: `${headerHeight + 20 + Math.random() * (window.innerHeight - headerHeight - footerHeight - 40)}px`,
              // 确保书名浮现在左右两侧，并留出20-50px的空隙
              left: Math.random() < 0.5
                ? `${20 + Math.random() * 30}px` // 左侧距离边缘20-50px
                : `${window.innerWidth - 350 + Math.random() * 30}px` // 右侧，适当左移更多空隙避免折叠
            };
          }while (this.isOverlapping(position)); // 位置重叠检查
          // 随机生成字体大小，范围可以根据需求调整
          const fontSize = `${26 + Math.random() * 12}px`; // 随机字号在 26px 到 38px 之间

          // 将书名添加到 floatingBooks 列表
          const randomBook = shuffledBooks[i];
          this.floatingBooks.push({
            name: randomBook,
            id: Date.now() + i, // 用于唯一标识
            position,
            fontSize // 添加 fontSize 属性
          });
        }

        // 5秒后移除浮现的书名
        setTimeout(() => {
          this.floatingBooks.splice(0, 7); // 每次移除3个最早的浮现书名
        }, 7000);
      }, 4500); // 每4秒浮现一组新的书名
    },
    
    // 随机打乱数组顺序
    shuffleArray(array) {
      for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
      }
      return array;
    },

    // 检测新位置是否与现有书名位置重叠
    isOverlapping(newPosition) {
      const overlapThreshold = 40; // 定义重叠的最小距离阈值

      for (const book of this.floatingBooks) {
        const existingPosition = book.position;
        const dx = parseFloat(existingPosition.left) - parseFloat(newPosition.left);
        const dy = parseFloat(existingPosition.top) - parseFloat(newPosition.top);
        const distance = Math.sqrt(dx * dx + dy * dy);
        
        // 打印 distance 和相关信息到日志
        // console.log(`Checking distance between new position and existing position: ${distance}`);
        // console.log(`New position: left=${newPosition.left}, top=${newPosition.top}`);
        // console.log(`Existing position: left=${existingPosition.left}, top=${existingPosition.top}`);

        if (distance < overlapThreshold) {
          return true; // 有重叠
        }
      }

      return false; // 没有重叠
    }
  }
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
  .floating-book {
  /* font-size: 28px; */
  color: #333;
  font-family: "LiSu", serif; /* 设置字体为隶书，若未安装隶书，可使用系统默认字体 */
  opacity: 0;
  animation: floatInOut 7s ease-in-out;
}

@keyframes floatInOut {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  35% {
    opacity: 1;
    transform: translateY(0);
  }
  65% { /* 延长停留在显示状态的时间 */
    opacity: 1;
    transform: translateY(0);
  }
  100% {
    opacity: 0;
    transform: translateY(-20px);
  }
}
</style>