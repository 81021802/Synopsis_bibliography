<template>
    <div>
        <el-container>
        <el-main>
            <el-input
            placeholder="请输入内容"
            v-model="input"
            clearable
            @keyup.enter.native="fetchData">
            <el-button slot="append" icon="el-icon-search" @click="fetchData"></el-button>
            </el-input>
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
    :current-page.sync="currentPage"
    :page-sizes="[20,50,100,200]"
    :page-size="pageSize"
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
                input: '',
                total:0,//总记录数
                currentPage:1,//当前页，默认第1页
                pageSize:20,//每页显示条数，为20
                booklist:[],
                dialogVisible: false,// 控制对话框的显示
                detailsData: {}, // 存储被点击行的详细数据
            }
        },
        created(){
            this.fetchData();
        },
        methods:{
            handleSizeChange:function(size){
                this.pageSize = size;
                console.log(this.pageSize);
                this.fetchData()
            },
            handleCurrentChange:function(currentPage){
                this.currentPage = currentPage;
                console.log(this.currentPage);
                this.fetchData()
            },
            fetchData(){
                searchApi.searchPage(this.input,this.currentPage,this.pageSize).then(response=>{
                    const resp = response.data
                    this.booklist = resp.data;
                    this.total = resp.total;
                    console.log(resp)
                })
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
            }
        }
    }
</script>


<style >
.el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    /* line-height: 160px; */
    min-height: 80vh; 
}
.dialog-content {
  max-height: 400px; /* 或您希望的高度 */
  overflow-y: auto; /* 如果内容太多，需要滚动 */
}

.el-descriptions-item {
  padding: 12px 0; /* 添加一些内部间距 */
}
</style>