<template>
    <div>
        <el-container style="height: calc(100vh - 60px - 60px);">
        <el-main>
            <el-input
            placeholder="请输入内容"
            v-model="input"
            clearable
            style="padding-top: 20px;"
            @keyup.enter.native="fetchData">
            <el-button slot="append" icon="el-icon-search" @click="fetchData"></el-button>
            </el-input>
            <div v-if="booklist.length === 0" class="no-data-message">
                暂无数据
            </div>
            <el-table
            v-else
            :data="booklist"
            :border=True
            :header-row-style="{height:'50px'}"
            :row-style="{height: '60px'}"
            max-height="530"
            fit
            >
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
            <el-table-column label="操作" width="120">
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
            <!-- 实体识别按钮 -->
            <el-button 
                type="primary" 
                size="mini" 
                @click="handleEntityRecognition()">
                实体识别
            </el-button>
            <!-- 取消实体识别按钮 -->
            <el-button 
                type="danger" 
                size="mini" 
                @click="cancelEntityRecognition" 
                v-if="showLegend">
                取消
            </el-button>
        </el-descriptions-item>
        <!-- ... 更多字段 ... -->
        </el-descriptions>
        <!-- 图例 -->
        <div v-if="showLegend" class="legend">
            <div class="legend-item" v-for="item in legend" :key="item.label">
                <span :class="'legend-color ' + item.className"></span>
                <span>{{ item.labelName }}</span>
            </div>
        </div>
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
// 在 Vue 文件中的 script 部分顶部引入
import init, { simplecc } from "simplecc-wasm";

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
                showLegend: false, // 控制图例显示
                legend: [
                    { label: 'PER', labelName: '人名', className: 'highlight-per' },
                    { label: 'LOC', labelName: '地名', className: 'highlight-loc' },
                    { label: 'TIM', labelName: '时间', className: 'highlight-tim' },
                    { label: 'BOO', labelName: '著作', className: 'highlight-boo' },
                    { label: 'STA', labelName: '社会身份', className: 'highlight-sta' },
                    { label: 'ORG', labelName: '组织机构', className: 'highlight-org' },
                    { label: 'SEA', labelName: '藏书印', className: 'highlight-sea' }
                ]
            }
        },
        watch: {
            dialogVisible(newVal) {
                if (newVal) {
                    // 弹窗打开时保存原始提要
                    if (!this.detailsData.originalSummary) {
                        this.detailsData.originalSummary = this.detailsData.summary;
                    }
                } else {
                    // 弹窗关闭时重置内容
                    this.resetSummary();
                }
            }
        },
        created(){
            this.fetchData();
        },
        mounted() {
            init()
                .then(() => {
                    console.log("simplecc-wasm initialized");
                })
                .catch((error) => {
                    console.error("Failed to initialize simplecc-wasm:", error);
                });

            // 检查是否需要自动搜索
            if (this.$route.query.autoSearch && this.$route.query.query) {
            // 将路由参数中的 query 赋值给 input
            this.input = this.$route.query.query;

            // 调用 fetchData 方法执行搜索
            this.fetchData();
            }
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
            async handleEntityRecognition(){
                // 获取 detailsData.id
                const id = this.detailsData.id;

                // 确保 ID 为数字
                const numericId = Number(id);
                if (isNaN(numericId)) {
                    this.$message.error('无效的 ID 格式');
                    return;
                }
                
                try{
                    // 调用封装的 searchApi.getEntity 方法
                    const response = await searchApi.getEntity(id);
                    // console.log(response.data); // 直接访问后端返回的数据
                    if (response && response.data) {
                        const entities = response.data; // 假设返回的数据是实体数组
                        // 显示图例
                        this.showLegend = true;
                        // 调用高亮方法，处理 summary 内容
                        this.highlightEntities(this.detailsData.summary, entities);
                    } else {
                        this.$message.warning('未识别到相关实体。');
                    }
                }catch(error){
                    console.error('实体识别失败:', error);
                    this.$message.error('实体识别失败，请稍后重试。');
                }
            },
            highlightEntities(summary, entities) {
                // 第一步：清理 summary 中已有的 <span> 标签，确保没有旧的高亮
                // console.log(summary)
                summary = summary.replace(/<\/?span[^>]*>/g, '');
                // console.log(summary)
                // 逆序遍历实体，避免位置偏移
                entities.sort((a, b) => b.startIdx - a.startIdx);
                // console.log(entities)
                // 插入高亮标记
                entities.forEach(entity => {
                    const { startIdx, endIdx, entity: entityText, label } = entity;
                    // 如果是人物实体（PER），替换为超链接
                    let replacement;
                    if (label === "PER") {
                        // 如果是人物实体（PER），将简体字转为繁体字并生成超链接
                        const traditionalText = simplecc(entityText, "s2t"); // 使用 simplecc 转换为繁体字
                        replacement = `<a href="https://cbdb.fas.harvard.edu/cbdbapi/person.php?name=${traditionalText}" class="highlight-per" target="_blank">${entityText}</a>`;
                    }else{
                        const className = `highlight-${label.toLowerCase()}`; // 根据 label 动态生成类名
                        replacement = `<span class="${className}">${entityText}</span>`;
                    }
                    
                    summary = summary.slice(0, startIdx) + replacement + summary.slice(endIdx);
                })
                // console.log(summary)
                // 更新 summary 内容
                this.detailsData.summary = summary;
            },
            cancelEntityRecognition() {
                // 隐藏图例
                this.showLegend = false;

                // 重置提要内容，移除所有高亮标记
                this.resetSummary();

                this.$message.info('已取消实体识别');
            },
            resetSummary() {
                // 使用原始提要恢复内容
                if (this.detailsData.originalSummary) {
                    this.detailsData.summary = this.detailsData.originalSummary;
                }
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
            this.showLegend = false;   // 隐藏图例
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
    height: calc(100vh - 60px - 60px); /* 假设 header 和 footer 各为 60px */
    width: 100%;
    overflow: hidden; /* 移除滚动 */
}
.dialog-content {
    max-height: 400px; /* 或您希望的高度 */
    overflow-y: auto; /* 如果内容太多，需要滚动 */
}

.el-descriptions-item {
    padding: 12px 0; /* 添加一些内部间距 */
}

.no-data-message {
    padding: 20px;
    font-size: 16px;
    color: #999;
    text-align: center;
}
.legend {
    display: flex;
    flex-wrap: wrap;
    margin-top: 16px; /* 图例和按钮之间的间距 */
    gap: 12px;
}

.legend-item {
    display: flex;
    align-items: center;
    gap: 6px;
}

.legend-color {
    width: 16px;
    height: 16px;
    border-radius: 50%;
}
.legend-color.highlight-per {
    background-color: #FFD700; /* 金黄色 */
    text-decoration: underline;
    cursor: pointer;
}

.legend-color.highlight-loc {
    background-color: #98FB98; /* 苍绿色 */
}

.legend-color.highlight-tim {
    background-color: #FFA07A; /* 浅橙色 */
}

.legend-color.highlight-boo {
    background-color: #AFEEEE; /* 苍青色 */
}

.legend-color.highlight-sta {
    background-color: #DDA0DD; /* 浅紫色 */
}

.legend-color.highlight-org {
    background-color: #87CEFA; /* 天蓝色 */
}

.legend-color.highlight-sea {
    background-color: #FFB6C1; /* 浅粉色 */
}
/* 人名 (PER) */
.highlight-per {
    background-color: #FFD700; /* 金黄色 */
    color: #000; /* 黑色字体 */
    /* font-weight: bold; */
    padding: 2px 4px;
    border-radius: 4px;
}

/* 地名 (LOC) */
.highlight-loc {
    background-color: #98FB98; /* 苍绿色 */
    color: #000; /* 黑色字体 */
    /* font-weight: bold; */
    padding: 2px 4px;
    border-radius: 4px;
}

/* 时间 (TIM) */
.highlight-tim {
    background-color: #FFA07A; /* 浅橙色 */
    color: #000; /* 黑色字体 */
    /* font-weight: bold; */
    padding: 2px 4px;
    border-radius: 4px;
}

/* 著作 (BOO) */
.highlight-boo {
    background-color: #AFEEEE; /* 苍青色 */
    color: #000; /* 黑色字体 */
    /* font-weight: bold; */
    padding: 2px 4px;
    border-radius: 4px;
}

/* 社会身份 (STA) */
.highlight-sta {
    background-color: #DDA0DD; /* 浅紫色 */
    color: #000; /* 黑色字体 */
    /* font-weight: bold; */
    padding: 2px 4px;
    border-radius: 4px;
}

/* 组织机构 (ORG) */
.highlight-org {
    background-color: #87CEFA; /* 天蓝色 */
    color: #000; /* 黑色字体 */
    /* font-weight: bold; */
    padding: 2px 4px;
    border-radius: 4px;
}

/* 藏书印 (SEA) */
.highlight-sea {
    background-color: #FFB6C1; /* 浅粉色 */
    color: #000; /* 黑色字体 */
    /* font-weight: bold; */
    padding: 2px 4px;
    border-radius: 4px;
}
</style>