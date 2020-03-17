import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginPage from '@/views/LoginPage'

Vue.use(VueRouter)

// const router = new VueRouter({
//   mode: 'history',
//   base: process.env.BASE_URL,
//   routes: []
// })

export default new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [{
    path: '/login',
    name: 'LoginPage',
    component: LoginPage
  }]
})
