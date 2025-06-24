<template>
  <div id="app">
    <!-- 路由视图 -->
    <router-view></router-view>

    <!-- 全局樱花控制面板 -->
    <global-sakura-control
        ref="sakuraControl"
        @toggle-sakura="toggleSakura"
        @set-sakura-num="setSakuraNum"
        @set-sakura-duration="setSakuraDuration"
    ></global-sakura-control>

    <!-- 全局樱花特效组件 -->
    <sakura-effect ref="sakuraEffect"></sakura-effect>
  </div>
</template>

<script>
import SakuraEffect from '@/components/SakuraEffect.vue';
import GlobalSakuraControl from '@/components/GlobalSakuraControl.vue';

export default {
  name: 'App',
  components: {
    SakuraEffect,
    GlobalSakuraControl
  },
  methods: {
    toggleSakura(enabled) {
      if (enabled) {
        this.$refs.sakuraEffect.startSakuraEffect();
      } else {
        this.$refs.sakuraEffect.stopSakuraEffect();
      }
    },
    // 更新樱花数量设置方法
    setSakuraNum(num) {
      this.$refs.sakuraEffect.changeSakuraNum(num);
    },
    setSakuraDuration(duration) {
      this.$refs.sakuraEffect.setDuration(duration);
    }
  }
}
</script>

<style>
/* 全局样式 */
</style>
