import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/assets/css/global.css'
import '@/assets/css/theme/index.css'
import request from "@/utils/request";

Vue.config.productionTip = false

Vue.prototype.$request = request
Vue.prototype.$baseUrl = process.env.VUE_APP_BASEURL

Vue.use(ElementUI, {size: "small"})

// 引入全局组件
import SakuraEffect from '/src/components/SakuraEffect.vue'
Vue.component('SakuraEffect', SakuraEffect);

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
