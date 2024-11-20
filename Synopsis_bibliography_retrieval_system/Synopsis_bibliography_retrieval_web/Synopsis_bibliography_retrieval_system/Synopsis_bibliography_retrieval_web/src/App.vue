<template>
  <div id="app">
    <!-- <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>
    </nav>
    <router-view/> -->
    <el-container>
      <el-header class="header">
        <el-menu :default-active="activeIndex" class="el-menu1" mode="horizontal" @select="handleSelect" 
            background-color="#409EFF"
            text-color="#FFFFFF"
            active-text-color="#ffd04b"
            router>
          <el-menu-item index="/">
            <router-link to="/">主页</router-link>
          </el-menu-item>
          <el-menu-item index="/about">
            <router-link to="/about">关于我们</router-link>
          </el-menu-item>
          <el-menu-item index="1" style="float: right">数据管理</el-menu-item>
          <el-submenu index="2" style="float: right">
          <template slot="title">用户管理</template>
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
          <el-menu-item index="3" style="float: right">个人中心</el-menu-item>
        </el-menu>
      </el-header>

      <el-main>
        <router-view/>
      </el-main>

      <el-footer class="footer">
        提要书目检索系统
      </el-footer>
    </el-container> 
    
  </div>
</template>

<script>
  export default {
    data() {
      return {
        // 你可以通过 $route.path 来设置活动导航项
        activeIndex: this.$route.path,
      };
    },
    watch: {
      // 当路由变化时，更新活动导航项
      '$route'(to, from) {
        this.activeIndex = to.path;
      }
    },
  }
</script>

<style>

</style>