<template>
    <div>
    <el-container>
  <el-main class="AD-main">
    <el-form :model="searchForm" label-position="top" @submit.native.prevent="onSearch">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="书名">
          <el-input v-model="searchForm.bookName" placeholder="请输入书名"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="来源作者">
          <el-input v-model="searchForm.bookAuthor" placeholder="请输入来源作者"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="题名">
          <el-input v-model="searchForm.title" placeholder="请输入题名"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="所属部">
          <el-input v-model="searchForm.part" placeholder="请输入所属部"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="类别">
          <el-input v-model="searchForm.category" placeholder="请输入类别"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="版本">
          <el-input v-model="searchForm.edition" placeholder="请输入版本"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="责任者">
          <el-input v-model="searchForm.author" placeholder="请输入责任者"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="提要">
          <el-input v-model="searchForm.summary" placeholder="请输入提要"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="连接方式">
          <el-select v-model="joinMethod" placeholder="请选择连接方式" style="width: 100%;">
          <el-option label="AND" value="and"></el-option>
          <el-option label="OR" value="or"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item>
          <el-button type="primary" @click="onSearch">搜索</el-button>
          <el-button type="button" @click="resetForm">重置</el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
      <el-table
            :data="booklist"
            :border=True
            :header-row-style="{height:'50px'}"
            :row-style="{height: '60px'}"
            max-height="500">
            <el-table-column
            prop="id"
            label="ID"
            width="120">
            <template slot-scope="scope">
                <div v-html="scope.row.id"></div>
            </template>
            </el-table-column>
            <el-table-column
            prop="bookName"
            label="书名"
            width="120">
            <template slot-scope="scope">
                <div v-html="scope.row.bookName"></div>
            </template>
            </el-table-column>
            <el-table-column
            prop="bookAuthor"
            label="来源作者"
            width="120">
            <template slot-scope="scope">
                <div v-html="scope.row.bookAuthor"></div>
            </template>
            </el-table-column>
            <el-table-column
            prop="title"
            label="题名"
            width="120">
            <template slot-scope="scope">
                <div v-html="scope.row.title"></div>
            </template>
            </el-table-column>
            <el-table-column
            prop="location"
            label="所在卷数"
            width="120">
            <template slot-scope="scope">
                <div v-html="scope.row.location"></div>
            </template>
            </el-table-column>
            <el-table-column
            prop="part"
            label="所属部"
            width="120">
            <template slot-scope="scope">
                <div v-html="scope.row.part"></div>
            </template>
            </el-table-column>
            <el-table-column
            prop="category"
            label="类别"
            width="120">
            <template slot-scope="scope">
                <div v-html="scope.row.category"></div>
            </template>
            </el-table-column>
            <el-table-column
            prop="edition"
            label="版本项"
            width="120">
            <template slot-scope="scope">
                <div v-html="scope.row.edition"></div>
            </template>
            </el-table-column>
            <el-table-column
            prop="author"
            label="责任者项"
            width="120">
            <template slot-scope="scope">
                <div v-html="scope.row.author"></div>
            </template>
            </el-table-column>
            <el-table-column
            prop="summary"
            label="提要"
            show-overflow-tooltip='true' 
            width="300">
            <template slot-scope="scope">
                <div v-html="scope.row.summary"></div>
            </template>
            </el-table-column>
            <el-table-column label="操作">
            <template slot-scope="scope">
                <el-button
                size="mini"
                @click="showDetails(scope.row)">查看详情</el-button>
            </template>
            </el-table-column>
        </el-table>
    <el-dialog
    :visible.sync="dialogVisible"
    title="详细信息"
    @close="handleDialogClose"
    width="60%">
    <!-- 在这里插入你的详细信息内容，可以使用 'detailsData' -->
    <div class="dialog-content">
        <!-- 这里假设 'detailsData' 包含一个 'description' 字段 -->
        <el-descriptions bordered>
        <el-descriptions-item label="ID">
            <div v-html="renderHtml(detailsData.id)"></div>
        </el-descriptions-item>
        <el-descriptions-item label="书名">
            <div v-html="renderHtml(detailsData.bookName)"></div>
        </el-descriptions-item>
        <el-descriptions-item label="来源作者">
            <div v-html="renderHtml(detailsData.bookAuthor)"></div>
        </el-descriptions-item>
        <el-descriptions-item label="题名">
            <div v-html="renderHtml(detailsData.title)"></div>
        </el-descriptions-item>
        <el-descriptions-item label="所在卷数">
            <div v-html="renderHtml(detailsData.location)"></div>
        </el-descriptions-item>
        <el-descriptions-item label="所属部">
            <div v-html="renderHtml(detailsData.part)"></div>
        </el-descriptions-item>
        <el-descriptions-item label="类别">
            <div v-html="renderHtml(detailsData.category)"></div>
        </el-descriptions-item>
        <el-descriptions-item label="版本项">
            <div v-html="renderHtml(detailsData.edition)"></div>
        </el-descriptions-item>
        <el-descriptions-item label="责任者项">
            <div v-html="renderHtml(detailsData.author)"></div>
        </el-descriptions-item>
        <el-descriptions-item label="提要" :span="3">
            <div v-html="renderHtml(detailsData.summary)"></div>
        </el-descriptions-item>
        <!-- ... 更多字段 ... -->
        </el-descriptions>
    </div>

    </el-dialog>
    <el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :hide-on-single-page="True"
    :current-page.sync="searchForm.pageNum"
    :page-sizes="[20,50,100,200]"
    :page-size="searchForm.pageSize"
    layout="total, sizes, prev, pager, next, jumper"
    :total="total">
    </el-pagination>
  </el-main>
</el-container>
    </div>
</template>

<script>
import searchApi from '@/api/search.js';
export default {
  data() {
      return {
        searchForm: {
        'bookName':'',
        'bookAuthor':'',
        'title':'',
        'part':'',
        'category':'',
        'edition':'',
        'author': '',
        'summary':'',
        pageNum:1,//当前页，默认第1页
        pageSize:20,//每页显示条数，为20
        'preTags':'<span style=\"color: red\">',
        'postTags':'</span>',
      },
      joinMethod:'and',
      booklist:[],
      total:0,//总记录数
      dialogVisible: false,// 控制对话框的显示
      detailsData: {}, // 存储被点击行的详细数据
    };
  },
  methods: {
    onSearch() {
      console.log('执行搜索', this.searchForm);
      if (this.joinMethod === "and"){
          searchApi.onSearch(this.searchForm).then(response=>{
          const resp = response.data;
          this.booklist = resp.data;
          this.total = resp.total;
          console.log(resp);
        }).catch(error => console.error('搜索错误:', error));
      }else if (this.joinMethod === "or"){
          searchApi.onSearchor(this.searchForm).then(response=>{
          const resp = response.data;
          this.booklist = resp.data;
          this.total = resp.total;
          console.log(resp);
        }).catch(error => console.error('搜索错误:', error));
      }
      
    },
    resetForm() {
      this.searchForm = {
        bookName:'',
        bookAuthor:'',
        title:'',
        part:'',
        category:'',
        edition:'',
        author: '',
        summary:'',
        pageNum:1,
        pageSize:20,
        preTags:'<span style=\"color: red\">',
        postTags:'</span>',
      };
      this.joinMethod = 'and'
    },
    handleSizeChange:function(size){
        this.searchForm.pageSize = size;
        console.log(this.searchForm.pageSize);
        this.onSearch()
    },
    handleCurrentChange:function(pageNum){
        this.searchForm.pageNum = pageNum;
        console.log(this.searchForm.pageNum);
        this.onSearch()
    },
    showDetails(row) {
      this.detailsData = row; // 将被点击行的数据设置为详细信息数据
      this.dialogVisible = true; // 显示对话框
    },
      renderHtml(htmlContent) {
          // 此处可以添加XSS防范措施，确保内容的安全
          return htmlContent; // 假设htmlContent已经是安全的
    },
      handleDialogClose() {
      this.dialogVisible = false; // 当对话框关闭时，更新显示状态
    },
  },
}
</script>


<style >
.AD-main{
    background-color: #fff;
    color: #333;
    text-align: center;
    height: 80vh;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
</style>