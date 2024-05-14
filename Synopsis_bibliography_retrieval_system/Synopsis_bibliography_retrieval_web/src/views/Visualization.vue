<template>
    <div>
      <el-main class="AD-main">
        <el-collapse v-model="activeNames" @change="handleChange">
        <el-collapse-item title="全库数据分布图" name="1">
          <!-- 直方图组件 -->
          <v-chart class="chart" :option="histogramOption" autoresize />
        </el-collapse-item>
        <el-collapse-item title="提要书目部类分布图" name="2">
          <el-select v-model="selectedBook" placeholder="请选择书名" @change="fetchData">
            <el-option
            v-for="book in books"
            :key="book"
            :label="book"
            :value="book">
            </el-option>
          </el-select>
          <v-chart class="chart" ref="chartRef" :option="pieChartOption" autoresize />
          <v-chart class="chart" v-if="secondPieChartOption" :option="secondPieChartOption" autoresize />
        </el-collapse-item>
        <el-collapse-item title="全库版本朝代分布统计" name="3">
          <v-chart class="chart" :option="timelineOption" autoresize/>
        </el-collapse-item>
        <el-collapse-item title="全库人物籍贯分布统计" name="4">
          <v-chart ref="echartRef" :option="chartOption" style="width: 100%; height: 400px;" />
        </el-collapse-item>
        <el-collapse-item title="全库人物朝代分布统计" name="5">
          <v-chart class="chart" :option="timelineOption1" autoresize/>
        </el-collapse-item>
        <el-collapse-item title="全库人物出现频次Top50" name="6">
          <div style="display: flex; justify-content: center; align-items: center;">
            <el-table :data="top50Figures" border style="width: 100%">
            <el-table-column prop="name" label="姓名" width="180" align="center"></el-table-column>
            <el-table-column prop="dynasty" label="朝代" width="180" align="center"></el-table-column>
            <el-table-column prop="styleName" label="字" width="180" align="center"></el-table-column>
            <el-table-column prop="literaryName" label="号" width="180" align="center"></el-table-column>
            <el-table-column prop="nativePlace" label="籍贯" width="180" align="center"></el-table-column>
            <el-table-column prop="count" label="出现次数" width="100" align="center"></el-table-column>
            </el-table>
          </div>
        </el-collapse-item>
        </el-collapse>

      </el-main>
    </div>
  </template>
  
  <script>
  import { use } from 'echarts/core';
  import { CanvasRenderer } from 'echarts/renderers';
  import {
    PieChart,
    BarChart,
    LineChart,
    MapChart,
    ScatterChart,
    EffectScatterChart,
  } from 'echarts/charts';
  import {
    TitleComponent,
    TooltipComponent,
    LegendComponent,
    GridComponent,
    ToolboxComponent,
    GeoComponent,
    VisualMapComponent,
  } from 'echarts/components';
  import VChart from 'vue-echarts';
  import ECharts from 'vue-echarts';
  import searchApi from '@/api/search.js';
  // 引入高德地图扩展
  import 'echarts-extension-amap';

  use([
    CanvasRenderer,
    PieChart,
    BarChart,
    LineChart,
    MapChart,
    ScatterChart,
    EffectScatterChart,
    TitleComponent,
    TooltipComponent,
    LegendComponent,
    GridComponent,
    ToolboxComponent,
    GeoComponent,
    VisualMapComponent,
  ]);
  
  export default {
    name: 'PieChartComponent',
    components: {
      VChart,
    },
    data() {
      return {
        activeNames: [],
        histogramData: [], // 用于存储从后端获取的直方图数据
        selectedBook: null,
        books: ['四库全书总目提要','藏园群书经眼录','中国善本书提要','郘亭知见传本书目','崇文总目辑释',
        '直斋书录解题','上海图书馆馆藏家谱提要','清人诗集叙录','文选楼藏书记','郑堂读书记','皕宋楼藏书志',
        '郡斋读书志','中南、西南地区省、市图书馆馆藏古籍稿本提要','铁琴铜剑楼藏书目录','中国通俗小说总目提要',
        '嘉业堂藏书志','石庐金石书志','五十万卷楼藏书目录初编','书画书录解题','荛圃藏书题识','持静斋藏书记要',
        '曲海总目提要','爱日精庐藏书志','天禄琳琅书目后编','文禄堂访书记','中国印谱解题','钦定天禄琳琅书目',
        '四部备要书目提要','士礼居藏书题跋记','仪顾堂题跋','拜经楼藏书题跋','中国农学书录','绛云楼题跋',
        '读书敏求记','四库未收书目提要','中国史学名著题解','东湖丛记','滂喜斋藏书记','楹书隅录','宝礼堂宋本书录',
        '宋元旧本书经眼录','红雨楼题跋','绩语堂题跋','半毡斋题跋','古泉山馆题跋','日本访书志补','经义考补正'], // 示例书名列表
        chartTitle: '提要书目部类分布', // 初始标题
        seriesData: [],
        secondPieChartOption: null, // 一开始，第二个饼图的配置为空
        timelineData: [],
        figureTimelineData: [],
        top50Figures:[],
        chartOption: {
          amap: {
            center: [105.403119, 38.028658], // 高德地图中心经纬度
            zoom: 5, // 缩放级别
            mapStyle: 'amap://styles/light', // 地图样式
            features: ['bg', 'road', 'building'], // 显示地图上的要素类型
            viewMode: '3D', // 开启 3D 视图
            resizeEnable: true, // 监听地图容器尺寸变化
            key: 'dd4e2afb1342e43035afc46397a88137' // 请替换成你的 API Key
          },
          series: [
            {
              name: '籍贯',
              type: 'scatter',
              coordinateSystem: 'amap',
              data: [
                // 这里填入你的数据
                // 示例格式: [经度, 纬度, 值]
                { name: '北京', value: [116.405285, 39.904989, 300] },
                { name: '上海', value: [121.472644, 31.231706, 200] },
                // 更多数据点...
              ],
              symbolSize: function (val) {
                return val[2] / 10; // 根据数值确定散点大小
              },
              encode: {
                value: 2
              },
              label: {
                formatter: '{b}',
                position: 'right',
                show: true
              },
              itemStyle: {
                color: '#F06C00'
              },
              emphasis: {
                label: {
                  show: true
                }
              }
            }
          ]
        },
      };
    },
    computed:{
      histogramOption() {
        return {
          title: {
            text: '全库数据分布',
            left: 'center',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          toolbox: {
            show: true,
            feature: {
              dataView: { readOnly: false },
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            data: this.histogramData.map(item => item.name),
            axisLabel: {
              interval: 0,
              rotate: -45, // 根据需要旋转标签，使其不重叠
              fontSize: 10, // 设置字体大小
            }
          },
          yAxis: {
            type: 'value',
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '15%', // 增加底部空间以显示旋转的标签
            containLabel: true
          },
          series: [
            {
              data: this.histogramData.map(item => item.value),
              type: 'bar',
              showBackground: true,
              backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)',
              },
            },
          ],
        };
      },
      pieChartOption(){
        return{
          title: { 
            text: this.selectedBook || this.chartTitle ,// 如果selectedBook为空，则使用chartTitle
            left: 'center', // 标题居中
            top: '20px', // 标题距离顶部20像素
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)' // 显示标签、数值和百分比
          },
          toolbox: {
            show: true,
            feature: {
              dataView: { readOnly: false },
              saveAsImage: {}
            }
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            top: 'middle', // 垂直居中
            align: 'left'
          },
          series: [
            {
              name: this.selectedBook,
              type: 'pie',
              radius: '50%',
              center:['50%', '50%'], // 调整饼图位置，使得标题和图例有足够空间
              data: this.seriesData, // 使用另一个响应式属性来存储系列数据
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
      },
      // 计算属性，生成时间轴图的配置
      timelineOption() {
        return {
          title: {
            text: '版本朝代分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          toolbox: {
            show: true,
            feature: {
              dataView: { readOnly: false },
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: this.timelineData.map(item => item.name), 
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.timelineData, 
              type: 'line',
              areaStyle: {}
            }
          ]
        };
      },
      // 计算属性，生成时间轴图的配置
      timelineOption1() {
        return {
          title: {
            text: '人物朝代分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          toolbox: {
            show: true,
            feature: {
              dataView: { readOnly: false },
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: this.figureTimelineData.map(item => item.name), 
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.figureTimelineData, 
              type: 'line',
              areaStyle: {
                color: '#ffb980' // 设置区域颜色
              },
              itemStyle: {
                color: '#d87a80', // 设置数据点颜色
                borderColor: '#d87a80' // 如果您的数据点有边框，还可以设置边框颜色
              },
            }
          ]
        };
      },
    },
    methods: {
      handleChange(val) {
        console.log(val);
            // 使用 nextTick 确保DOM更新完成后再调用resize
        this.$nextTick(() => {
          if (this.$refs.chart) {
            // 调用ECharts实例的resize方法
            this.$refs.chart.resize();
          }
        });
      },
      fetchHistogramData(){
        requestAnimationFrame(() => {
          searchApi.bookAggregation().then(response=>{
            this.histogramData = response.data.map(item => {
              const key = Object.keys(item)[0]; // 获取当前项的键名
              return {
                name: key,
                value: item[key] // 使用键名从项中获取值
              };
            });
          })
          .catch(error => {
            console.error('Error fetching histogram data:', error);
          });
        });
      },
      fetchData() {
        // 打印当前选中的书名
        console.log("Selected book:", this.selectedBook);
        // 重置第二个饼图的配置为 null，使第二个饼图消失
        this.secondPieChartOption = null;
        searchApi.aggregation(this.selectedBook).then(response=>{
          if (response.data.length === 0) {
          this.seriesData = [];// 清空饼状图的数据
           // 使用 MessageBox 显示中央通知
          MessageBox.alert('数据库中暂无本书部类数据，请选择其他书名。', '提示', {
            type: 'warning',
            center: true,
            showClose: false, // 根据需要隐藏关闭按钮
            confirmButtonText: '确定', // 自定义确定按钮的文本，根据需要调整
          });
        }else{
          this.seriesData = response.data.map(item => {
            const key = Object.keys(item)[0]; // 获取当前项的键名
            return {
              name: key,
              value: item[key] // 使用键名从项中获取值
            };
          });
        }
        }).catch(error => {
          console.error("Error fetching data:", error);
        });
      },
      onChartClick(params) {
      // 假设你已经有一个函数来获取基于params.name的数据
      this.fetchSecondChartData(params.name);
    },
    fetchSecondChartData(name) {
      // 示例: 根据点击的饼图部分的名称请求数据
      searchApi.aggregation1(this.selectedBook,name).then(response => {
        if (response.data.length === 0) {
          this.seriesData = [];// 清空饼状图的数据
          // 使用 MessageBox 显示中央通知
          MessageBox.alert('数据库中暂无本书详细类别信息', '提示', {
            type: 'warning',
            center: true,
            showClose: false, // 根据需要隐藏关闭按钮
            confirmButtonText: '确定', // 自定义确定按钮的文本，根据需要调整
          });   
        }else{
          const newData = response.data.map(item => {
          const key = Object.keys(item)[0]; // 获取当前项的键名
            return {
              name: key,
              value: item[key] // 使用键名从项中获取值
            };
          });
          // 使用响应数据更新第二个饼图的配置
          this.secondPieChartOption = {
            // 用新数据填充饼图配置
            title: {
              text: `${this.selectedBook}+${name} 详细分类`, // 根据点击的部分设置第二个饼图的标题
              left: 'center',
              top: '20px',
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left',
            },
            series: [
              {
                name: '详细分类', // 可以根据实际情况修改
                type: 'pie',
                radius: '50%',
                center: ['50%', '60%'],
                data: newData,
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          };
        }
      }).catch(error => {
        console.error("Error fetching second chart data:", error);
        this.$notify({
          title: '错误',
          message: '获取第二个饼图数据时发生错误。',
          type: 'error'
        });
      });
      },
      fetchTimelineData() {
        // 假设 searchApi.getDynastyDistribution 是获取数据的API调用
        searchApi.editionAggregation().then(response => {
          this.timelineData = response.data.map(item => {
            const key = Object.keys(item)[0];
            return {
              name: key,
              value: item[key]
            };
          });
        })
        .catch(error => {
          console.error("Error fetching dynasty distribution data:", error);
        });
      },
      fetchFigureTimelineData() {
        // 假设 searchApi.getDynastyDistribution 是获取数据的API调用
        searchApi.figureAggregation().then(response => {
          this.figureTimelineData = response.data.map(item => {
            const key = Object.keys(item)[0];
            return {
              name: key,
              value: item[key]
            };
          });
        })
        .catch(error => {
          console.error("Error fetching dynasty distribution data:", error);
        });
      },
      fetchTop50FiguresData() {
        // 调用API获取数据
        searchApi.CountTop50Figures().then((response) => {
          this.top50Figures = response.data;
        }).catch((error) => {
          console.error("获取数据失败", error);
        });
      },
    },
    mounted() {
      this.fetchHistogramData();
      this.$nextTick(() => {
        // 直接访问 ECharts 实例进行事件绑定
        this.$refs.chartRef.chart.on('click', this.onChartClick);
        // 确保地图已经在 ECharts 中初始化
        ECharts.registerMap('china', {});
        if (this.$refs.echartRef) {
          const echartInstance = this.$refs.echartRef.getEchartsInstance();
          echartInstance.setOption(this.chartOption);
        }
      });
      this.fetchTimelineData();
      this.fetchFigureTimelineData();
      this.fetchTop50FiguresData(); 
    },
    beforeDestroy() {
      if (this.$refs.chartRef && this.$refs.chartRef.chart) {
        // 移除之前绑定的点击事件监听器
      this.$refs.chartRef.chart.off('click', this.onChartClick);
      }
    },
  };
  </script>
  
  <style scoped>
  .chart {
    height: 80vh;
  }
  .table-container {
  display: flex;
  justify-content: center;
  align-items: center;
  }
  </style>