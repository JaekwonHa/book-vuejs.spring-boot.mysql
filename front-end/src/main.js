import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import Vuelidate from 'vuelidate'
import { library as faLibrary } from '@fortawesome/fontawesome-svg-core'
import { faHome, faSearch, faPlus, faEllipsisH, faUserPlus, faListUl } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { i18n } from './i18n'
import eventBus from './event-bus'
import realTimeClient from '@/real-time-client'

axios.defaults.baseURL = '/api'
axios.defaults.headers.common.Accept = 'application/json'
axios.interceptors.response.use(
  response => response,
  (error) => {
    return Promise.reject(error)
  }
)

// Enable Vuelidate
Vue.use(Vuelidate)

// Set up FontAwesome
faLibrary.add(faHome, faSearch, faPlus, faEllipsisH, faUserPlus, faListUl)
Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.config.productionTip = false

// 모든 컴포넌트에서 접근 가능하도록 전역객체로 설정하고 App.vue 에서 초기화
Vue.prototype.$bus = eventBus
Vue.prototype.$rt = realTimeClient

// router, store, i18n module 을 Vue Component 생성자에 넣어준다. prototype 에 등록하는 방법과의 차이점은 모르겠음
new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')
