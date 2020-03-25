import axios from 'axios'
import errorParser from '@/utils/error-parser'

export default {
  register (detail) {
    return new Promise((resolve, reject) => {
      axios.post('/registrations', detail).then(({ data }) => {
        resolve(data)
      }).catch((error) => {
        reject(errorParser.parse(error))
      })
    })
  }
}
