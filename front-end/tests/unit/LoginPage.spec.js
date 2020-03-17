import Vue from 'vue'
import LoginPage from '@/views/LoginPage.vue'

describe('LoginPage.vue', () => {
  it('should render correct contents', () => {
    const Constuctor = Vue.extend(LoginPage)
    const vm = new Constuctor().$mount()
    const msg = 'TaskAgile'
    expect(vm.$el.querySelector('h1').textContent).toEqual(msg)
  })
})
