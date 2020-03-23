import moxios from 'moxios'
import authenticationService from '@/services/authentication'
import {req} from "vuelidate/lib/validators/common";

describe('services/authentication', function () {
  beforeEach(() => {
    moxios.install()
  })

  afterEach(() => {
    moxios.uninstall()
  })

  it('should call `/authentications` API', function () {
    expect.assertions(1)
    moxios.wait(() => {
      let request = moxios.requests.mostRecent()
      expect(request.url).toEqual('/authenticates')
      request.respondWith({
        status: 200,
        response: { result: 'success' }
      })
    })
    return authenticationService.authenticate()
  });

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
    return authenticationService.authenticate().then(data => {
      expect(data.result).toEqual('success')
    })
  });

  it('should propagate the error to caller when request failed', function () {
    expect.assertions(2)
    moxios.wait(() => {
      let request = moxios.requests.mostRecent()
      expect(request).toBeTruthy()
      request.reject({
        stats: 400,
        response: { message: 'Bad request' }
      })
    })
    return authenticationService.authenticate().catch(error => {
      expect(error.response.message).toEqual('Bad request')
    })
  });
});
