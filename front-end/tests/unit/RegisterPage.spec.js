import { mount, createLocalVue } from '@vue/test-utils'
import VueRouter from 'vue-router'
import RegisterPage from '@/views/RegisterPage'
import Vuelidate from "vuelidate";
import registrationService from '@/services/registration'

const localVue = createLocalVue()
localVue.use(VueRouter)
const router = new VueRouter()

localVue.use(Vuelidate)

jest.mock('@/services/registration')

describe('RegisterPage.vue', () => {
  let wrapper
  let fieldUsername
  let fieldEmailAddress
  let fieldPassword
  let buttonSubmit
  let registerSpy

  beforeEach(() => {
    wrapper = mount(RegisterPage, {
      localVue,
      router
    })
    fieldUsername = wrapper.find('#username')
    fieldEmailAddress = wrapper.find('#emailAddress')
    fieldPassword = wrapper.find('#password')
    buttonSubmit = wrapper.find('form button[type="submit"]')
    registerSpy = jest.spyOn(registrationService, 'register')
  })

  afterEach(() => {
    registerSpy.mockReset()
    registerSpy.mockRestore()
  })

  afterAll(() => {
    jest.restoreAllMocks()
  })

  it('should render correct contents', () => {
    expect(wrapper.find('.logo').attributes().src).toEqual('/static/images/logo.png')
    expect(wrapper.find('.tagline').text()).toEqual('Open source task management tool')
    expect(fieldUsername.element.value).toEqual('')
    expect(fieldEmailAddress.element.value).toEqual('')
    expect(fieldPassword.element.value).toEqual('')
    expect(buttonSubmit.text()).toEqual('Create account')
  })

  it('should contain data model with initial values', () => {
    expect(wrapper.vm.form.username).toEqual('')
    expect(wrapper.vm.form.emailAddress).toEqual('')
    expect(wrapper.vm.form.password).toEqual('')
  })

  it('should have form inputs bound with data model', async () => {
    const username = 'sunny'
    const emailAddress = 'sunny@local'
    const password = 'VueJsRocks!'

    wrapper.vm.form.username = username
    wrapper.vm.form.emailAddress = emailAddress
    wrapper.vm.form.password = password

    await wrapper.vm.$nextTick()

    expect(fieldUsername.element.value).toEqual(username)
    expect(fieldEmailAddress.element.value).toEqual(emailAddress)
    expect(fieldPassword.element.value).toEqual(password)
  })

  it('should have form submit event handler `submitForm`', () => {
    const stub = jest.fn()
    wrapper.setMethods({submitForm: stub})
    buttonSubmit.trigger('submit')
    expect(stub).toBeCalled()
  });

  it('should register when it is a new user', async function () {
    expect.assertions(2)

    const stub = jest.fn()
    wrapper.vm.$router.push = stub
    wrapper.vm.form.username = 'sunny'
    wrapper.vm.form.emailAddress = 'sunny@taskagile.com'
    wrapper.vm.form.password = 'VueJsRocks!'
    wrapper.vm.submitForm()
    expect(registerSpy).toBeCalled()

    await wrapper.vm.$nextTick()

    expect(stub).toHaveBeenCalledWith({name: 'login'})
  });

  it('should fail it is not a new user', async function () {
    expect.assertions(3)
    wrapper.vm.form.username = 'ted'
    wrapper.vm.form.emailAddress = 'ted@taskagile.com'
    wrapper.vm.form.password = 'JestRocks!'
    expect(wrapper.find('.failed').isVisible()).toBe(false)
    wrapper.vm.submitForm()
    expect(registerSpy).toBeCalled()

    await wrapper.vm.$nextTick()

    expect(wrapper.find('.failed').isVisible()).toBe(true)
  });

  it('should fail when the email address is invalid', function () {
    const spy = jest.spyOn(registrationService, 'register')
    wrapper.vm.form.emailAddress = 'bad-email-address'
    wrapper.vm.submitForm()
    expect(spy).not.toHaveBeenCalled()
    spy.mockReset()
    spy.mockRestore()
  });

  it('should fail when the email address is invalid', function () {
    wrapper.vm.form.username = 'test'
    wrapper.vm.form.emailAddress = 'bad-email-address';
    wrapper.vm.form.password = 'JeskRocks!'
    wrapper.vm.submitForm()
    expect(registerSpy).not.toHaveBeenCalled()
  });

  it('should fail when the username is invalid', function () {
    wrapper.vm.form.username = 'a'
    wrapper.vm.form.emailAddress = 'test@taskagile.com'
    wrapper.vm.form.password = 'JestRocks!'
    wrapper.vm.submitForm()
    expect(registerSpy).not.toHaveBeenCalled()
  });

  it('should fail when the password is invalid', function () {
    wrapper.vm.form.username = 'test'
    wrapper.vm.form.emailAddress = 'test@taskagile.com'
    wrapper.vm.form.password = 'bad!'
    wrapper.vm.submitForm()
    expect(registerSpy).not.toHaveBeenCalled()
  });
})
