<template>
  <div>
    <div class="search">
      <el-date-picker
          v-model="startDate"
          type="date"
          placeholder="开始日期"
          style="margin-right: 10px"
          value-format="yyyy-MM-dd"
          @change="handleStartDateChange"
      ></el-date-picker>
      <el-date-picker
          v-model="endDate"
          type="date"
          placeholder="结束日期"
          style="margin-right: 10px"
          value-format="yyyy-MM-dd"
          :picker-options="endDatePickerOptions"
      ></el-date-picker>
      <el-select v-model="chartType" placeholder="选择图表类型" style="margin-right: 10px">
        <el-option label="柱状图" value="bar"></el-option>
        <el-option label="折线图" value="line"></el-option>
        <el-option label="饼图" value="pie"></el-option>
      </el-select>
      <el-button type="info" plain @click="load(1)">查询</el-button>
      <el-button type="warning" plain @click="reset">重置</el-button>
    </div>

    <div ref="chart" style="width: 100%; height: 400px;"></div>

    <div class="table">
      <el-table :data="tableData" stripe :span-method="spanMethod">
        <el-table-column v-if="user.role === 'ADMIN'" prop="hotelName" label="酒店名称" align="center"></el-table-column>
        <el-table-column v-else prop="typeName" label="房间类型" align="center"></el-table-column>
        <el-table-column prop="orderCount" label="订单数量" align="center"></el-table-column>
        <el-table-column v-if="user.role === 'ADMIN'" prop="totalIncome" label="酒店总收入" align="center"></el-table-column>
        <el-table-column v-else prop="totalIncome" label="收入" align="center"></el-table-column>
        <el-table-column v-if="user.role === 'ADMIN'" label="平台分成" align="center">
          <template v-slot="scope">
            {{ (scope.row.totalIncome * 0.05).toFixed(2) }} <!-- 计算平台分成 -->
          </template>
        </el-table-column>
        <!-- 新增总收入列 -->
        <el-table-column label="总收入" align="center">
          <template v-slot="scope">
                <span style="font-size: 18px; font-weight: bold;">
                  {{ totalIncomeSum }}
                </span>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "Finance",
  data() {
    return {
      tableData: [], // 统计数据
      chart: null, // ECharts 实例
      pageNum: 1, // 当前页码
      pageSize: 10, // 每页显示条数
      total: 0, // 总记录数
      startDate: null, // 开始日期
      endDate: null, // 结束日期
      chartType: 'bar', // 默认图表类型为柱状图
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"), // 当前用户信息
      colors: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'], // 房型颜色数组
      totalIncomeSum: 0, // 新增总收入总和变量
      endDatePickerOptions: {
        disabledDate(time) {
          return time.getTime() < new Date(this.startDate).getTime();
        }
      }
    };
  },
  created() {
    this.load(1); // 页面加载时自动获取数据
  },
  mounted() {
    this.initChart();
  },
  watch: {
    chartType(newVal) {
      // 当图表类型改变时，直接更新图表
      this.updateChart();
    },
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart);
      this.updateChart();
    },
    updateChart() {
      if (!this.chart) return;

      // 动态生成图例数据
      const legendData = this.tableData.map(item => {
        return this.user.role === 'ADMIN' ? item.hotelName || '未知酒店' : item.typeName || '未知类型';
      });

      let option = {
        title: {
          text: '财务统计',
          left: 'center'
        },
        tooltip: {
          trigger: this.chartType === 'pie' ? 'item' : 'axis'
        },
        legend: {
          bottom: 0,
          type: 'scroll'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          top: '15%',
          containLabel: true
        }
      };

      if (this.chartType === 'pie') {
        // 饼图配置
        option.legend.data = legendData; // 饼图图例使用数据项名称

        option.series = [
          {
            name: '总收入',
            type: 'pie',
            radius: ['40%', '75%'],
            data: this.tableData.map((item, index) => ({
              value: item.totalIncome || 0,
              name: legendData[index],
              itemStyle: { color: this.colors[index % this.colors.length] }
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
            label: {
              show: true,
              formatter: '{b}: {c} ({d}%)'
            }
          }
        ];
      } else if (this.chartType === 'bar') {
        // 柱状图配置 - 特殊处理图例
        option.legend.data = legendData; // 柱状图图例也使用数据项名称

        // 将每个数据项作为单独的系列
        option.series = legendData.map((name, index) => {
          const item = this.tableData[index];
          return {
            name: name,
            type: 'bar',
            data: [item.totalIncome || 0],
            itemStyle: { color: this.colors[index % this.colors.length] },
            // barWidth: '60%',
            label: {
              show: true,
              position: 'top',
              formatter: '{c}'
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              }
            }
          };
        });

        option.xAxis = {
          type: 'category',
          data: ['总收入'], // 只显示一个分类
          axisLine: { show: true },
          axisTick: { show: true }
        };

        option.yAxis = {
          type: 'value',
          name: '金额',
          axisLine: { show: true },
          axisTick: { show: true },
          splitLine: { show: true }
        };
      } else {
        // 折线图和其他图表类型配置
        // 关键修改：设置图例数据为系列名称 ['总收入']
        option.legend.data = ['总收入'];

        option.xAxis = {
          type: 'category',
          data: legendData,
          axisLabel: { rotate: 30, interval: 0 },
          axisLine: { show: true },
          axisTick: { show: true }
        };

        option.yAxis = {
          type: 'value',
          name: '金额',
          axisLine: { show: true },
          axisTick: { show: true },
          splitLine: { show: true }
        };

        option.series = [
          {
            name: '总收入', // 系列名称
            type: this.chartType,
            data: this.tableData.map((item, index) => ({
              value: item.totalIncome || 0,
              itemStyle: { color: this.colors[index % this.colors.length] }
            })),
            label: {
              show: true,
              position: 'top'
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              }
            }
          }
        ];
      }

      this.chart.setOption(option, true);
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;

      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        startDate: this.startDate,
        endDate: this.endDate,
      };

      // 如果用户是管理员，不传 hotelId；如果是普通用户，传入 hotelId
      if (this.user.role !== 'ADMIN') {
        params.hotelId = this.user.id;
      }

      // 添加角色参数
      params.role = this.user.role;

      this.$request.get("/orders/selectFinanceStatistics", { params }).then((res) => {
        if (res.code === "200") {
          this.tableData = res.data || []; // 确保数据为空数组
          this.total = this.tableData.length; // 设置 total 为 tableData 的长度

          // 计算总收入总和
          this.totalIncomeSum = this.tableData.reduce((sum, item) => {
            return sum + (this.user.role === 'ADMIN' ? item.totalIncome * 0.05 : item.totalIncome);
          }, 0);

          this.updateChart(); // 更新图表
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    reset() {
      this.startDate = null;
      this.endDate = null;
      this.load(1);
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
    spanMethod({ row, column, rowIndex, columnIndex }) {
      // 如果是“总收入”列
      if (column.label === "总收入") {
        // 第一行显示，其余行隐藏
        if (rowIndex === 0) {
          return [this.tableData.length, 1]; // 合并所有行
        } else {
          return [0, 0]; // 隐藏其他行
        }
      }
    },
    handleStartDateChange(val) {
      if (!val) {
        this.endDatePickerOptions.disabledDate = () => false; // 如果开始日期为空，不限制结束日期
      } else {
        this.endDatePickerOptions.disabledDate = (time) => {
          return time.getTime() < new Date(val).getTime();
        };
      }
    },
  },
};
</script>

<style scoped>
.search {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
