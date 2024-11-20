import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App.vue'
import router from './router'
import './assets/global.css' 
import myaxios from './utils/myaxios'

Vue.use(ElementUI)
Vue.config.productionTip = false
Vue.prototype.$myaxios = myaxios

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')

const debounce = (fn, delay) => {
  let timer
    return (...args) => {
      if (timer) {
        clearTimeout(timer)
      }
      timer = setTimeout(() => {
        fn(...args)
      }, delay)
    }
}
  
const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver{
    constructor(callback) {
      callback = debounce(callback, 200);
      super(callback);
    }
}
