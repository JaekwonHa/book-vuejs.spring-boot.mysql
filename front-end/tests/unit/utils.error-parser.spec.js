import errorParser from '@/utils/error-parser.js'

describe('utils/error-parser.js', () => {
  it('should parse HTTP 400 error', function () {
    const error = {
      response: {
        status: 400,
        data: {
          result: 'failure',
          message: 'This is an error'
        }
      }
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('This is an error')
  })

  it('should parse HTTP 400 unit test error', function () {
    const error = {
      response: {
        status: 400,
        data: {
          message: 'This is a bad request'
        }
      }
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('This is a bad request')
  })

  it('should parse HTTP 400 no message error', function () {
    const error = {
      response: {
        status: 400
      }
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('Bad request')
  })

  it('should parse HTTP 401 unit test error', function () {
    const error = {
      response: {
        status: 401,
        statusText: 'Unauthorized'
      }
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('Request not authorized.')
  })

  it('should parse HTTP 403 error', function () {
    const error = {
      response: {
        status: 403,
        statusText: 'Forbidden'
      }
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('Request forbidden.')
  })

  it('should parse HTTP 404 error', function () {
    const error = {
      response: {
        status: 404,
        statusText: 'Not Found'
      }
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('Request failed. Request endpoint not found on the server.')
  })

  it('should parse HTTP 500 known error', function () {
    const error = {
      response: {
        status: 500,
        statusText: 'internal Server Error',
        data: {
          message: 'This is rephrased error message. Please try again later.'
        }
      }
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('This is rephrased error message. Please try again later.')
  })

  it('should parse HTTP 503 error', () => {
    const error = {
      response: {
        status: 503,
        statusText: 'Service Unavailable'
      }
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('Request failed. Please try again later.')
  })

  it('should parse HTTP 504 error', () => {
    const error = {
      response: {
        status: 504,
        statusText: 'Gateway Timeout'
      }
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('Request failed. Please try again later.')
  })

  it('should parse empty response error', () => {
    const error = {
      request: {}
    }
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('Request failed. No response from the server.')
  })

  it('should parse unknown string error', () => {
    const error = 'Unknown error'
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('Unknown error')
  })

  it('should parse unknown wrapped error', () => {
    const error = new Error('Unknown error')
    const parsed = errorParser.parse(error)
    expect(parsed.message).toEqual('Unknown error')
  })
})
