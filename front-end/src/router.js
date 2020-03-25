import Vue from 'vue'
import VueRouter from 'vue-router'
import HomePage from '@/views/HomePage'
import LoginPage from '@/views/LoginPage'
import RegisterPage from '@/views/RegisterPage'
import BoardPage from "./views/BoardPage";

Vue.use(VueRouter)

export default new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [{
    path: '/',
    name: 'home',
    component: HomePage
  }, {
    path: '/login',
    name: 'login',
    component: LoginPage
  }, {
    path: '/register',
    name: 'register',
    component: RegisterPage
  }, {
    path: '/board/:boardId',
    name: 'board',
    component: BoardPage
  }]
})
