<template>
  <!-- 樱花控制面板 -->
  <div
      class="sakura-control-panel"
      :class="{ expanded: panelExpanded }"
      @mouseenter="panelExpanded = true"
      @mouseleave="panelExpanded = false"
  >
    <div class="panel-header">
      <i class="el-icon-flower"></i>
      <span v-show="panelExpanded">樱花特效控制</span>
    </div>
    <div class="panel-body" v-show="panelExpanded">
      <div class="control-item">
        <span>特效开关：</span>
        <el-switch
            v-model="sakuraEnabled"
            active-color="#e84393"
            @change="toggleSakura"
        ></el-switch>
      </div>
      <div class="control-item">
        <span>樱花数量：{{ sakuraCount }}</span>
        <el-slider
            v-model="sakuraCount"
            :min="10"
            :max="100"
            :step="10"
            :disabled="!sakuraEnabled"
            @change="changeSakuraCount"
        ></el-slider>
      </div>
      <div class="control-item">
        <span>飘落时长：</span>
        <el-input-number
            v-model="sakuraDuration"
            :min="0"
            :max="60"
            :step="5"
            :disabled="!sakuraEnabled"
            style="width: 100%"
        ></el-input-number>
        <div class="duration-hint">
          <span v-if="sakuraDuration === 0">循环播放</span>
          <span v-else>{{ sakuraDuration }}秒</span>
        </div>
      </div>
    </div>
    <div class="trigger-icon" v-show="!panelExpanded">
      <i class="el-icon-flower"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GlobalSakuraControl',
  data() {
    return {
      panelExpanded: false,
      sakuraEnabled: false,
      sakuraCount: 30,
      sakuraDuration: 5, // 0表示循环
    }
  },
  mounted() {
    // 组件挂载时触发开启特效
    this.$nextTick(() => {
      this.toggleSakura(false);
    });
  },
  methods: {
    toggleSakura() {
      this.$emit('toggle-sakura', this.sakuraEnabled);
      if (this.sakuraEnabled) {
        // 开启特效时发送数量和时长设置
        this.$emit('set-sakura-num', this.sakuraCount);
        this.$emit('set-sakura-duration', this.sakuraDuration);
      }
    },
    // 添加change事件处理方法
    changeSakuraCount() {
      this.$emit('set-sakura-num', this.sakuraCount);
    }
  },
  watch: {
    sakuraDuration(newVal) {
      this.$emit('set-sakura-duration', newVal);
    }
  }
}
</script>

<style scoped>
.sakura-control-panel {
  position: fixed;
  top: 50%;
  right: 0;
  transform: translateY(-50%);
  width: 280px;
  background-color: rgba(255, 255, 255, 0.92);
  border-radius: 8px 0 0 8px;
  box-shadow: -5px 0 15px rgba(0, 0, 0, 0.1);
  z-index: 9999;
  overflow: hidden;
  transition: all 0.4s ease;
  border: 1px solid #f0c8d0;
  border-right: none;
  transform: translateX(calc(100% - 40px)) translateY(-50%);
}

.sakura-control-panel.expanded {
  transform: translateX(0) translateY(-50%);
}

.panel-header {
  background: linear-gradient(135deg, #ffafbd, #ffc3a0);
  padding: 12px 15px;
  color: white;
  font-size: 16px;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.panel-header i {
  margin-right: 8px;
  font-size: 18px;
}

.panel-body {
  padding: 15px;
}

.control-item {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
}

.control-item:last-child {
  margin-bottom: 10px;
}

.control-item span {
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
}

.duration-hint {
  text-align: center;
  margin-top: 5px;
  color: #e84393;
  font-size: 12px;
}

.trigger-icon {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #ffafbd, #ffc3a0);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px 0 0 8px;
  cursor: pointer;
  box-shadow: -3px 0 8px rgba(0, 0, 0, 0.1);
}

.trigger-icon i {
  font-size: 24px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}
</style>
