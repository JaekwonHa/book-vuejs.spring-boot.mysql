import moxios from 'moxios'
import registrationService from '@/services/registration'

describe('services/registration', () => {
  beforeEach(() => {
    moxios.install()
  })

  afterEach(() => {
    moxios.uninstall()
  })

  it('should pass the response to caller when request succeeded', function () {
    expect.assertions(2)
    moxios.wait(() => {
      let request = moxios.requests.mostRecent()
      expect(request).toBeTruthy()
      request.respondWith({
        status: 200,
        response: { result: 'success' }
      })
    })
    return registrationService.register().then(data => {
      expect(data.result).toEqual('success')
    })
  });

  it('should propagate the error to caller when request failed', function () {
    expect.assertions(2)
    moxios.wait(() => {
      let request = moxios.requests.mostRecent()
      expect(request).toBeTruthy()
      request.reject({
        response: {
          status: 400,
          data: {message: 'Bad request'}
        }
      })
    })
    return registrationService.register().catch(error => {
      expect(error.message).toEqual('Bad request')
    })
  });

  it('should call `/registrations` API', function () {
    expect.assertions(1)
    moxios.wait(() => {
      let request = moxios.requests.mostRecent()
      expect(request).toBeTruthy()
      request.respondWith({
        status: 200,
        response: { result: 'success' }
      })
    })
    return registrationService.register()
  });
})
