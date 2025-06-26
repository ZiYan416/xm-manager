<template>
  <div>
    <div class="search">
      <el-date-picker
          v-model="startDate"
          type="date"
          placeholder="开始日期"
          style="margin-right: 10px"
          value-format="yyyy-MM-dd"
      ></el-date-picker>
      <el-date-picker
          v-model="endDate"
          type="date"
          placeholder="结束日期"
          style="margin-right: 10px"
          value-format="yyyy-MM-dd"
      ></el-date-picker>
      <el-button type="info" plain @click="load(1)">查询</el-button>
      <el-button type="warning" plain @click="reset">重置</el-button>
    </div>

    <div ref="chart" style="width: 100%; height: 400px;"></div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="typeName" label="房间类型" align="center"></el-table-column>
        <el-table-column prop="orderCount" label="订单数量" align="center"></el-table-column>
        <el-table-column prop="totalIncome" label="总收入" align="center"></el-table-column>
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
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"), // 当前用户信息
    };
  },
  created() {
    this.load(1);
  },
  mounted() {
    this.initChart();
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart);
      this.updateChart();
    },
    updateChart() {
      if (!this.chart) return;
      const option = {
        title: {
          text: '财务统计',
        },
        tooltip: {},
        legend: {
          data:['总收入'],
        },
        xAxis: {
          data: this.tableData.map(item => item.typeName),
        },
        yAxis: {},
        series: [{
          name: '总收入',
          type: 'bar',
          data: this.tableData.map(item => item.totalIncome),
        }],
      };
      this.chart.setOption(option);
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request.get("/orders/selectFinanceStatistics", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          startDate: this.startDate,
          endDate: this.endDate,
          hotelId: this.user.id, // 假设用户信息中包含酒店ID
        },
      }).then((res) => {
        if (res.code === "200") {
          this.tableData = res.data; // 直接使用 res.data 作为 tableData
          this.total = this.tableData.length; // 设置 total 为 tableData 的长度
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
