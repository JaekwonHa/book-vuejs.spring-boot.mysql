import axios from 'axios'

export default {
  register (detail) {
    return new Promise((resolve, reject) => {
      axios.post('/registrations', detail).then(({ data }) => {
        resolve(data)
      }).catch((error) => {
        reject(error)
      })
    })
  }
}
