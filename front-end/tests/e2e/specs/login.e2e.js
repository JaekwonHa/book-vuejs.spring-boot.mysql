// For authoring Nightwatch tests, see
// https://nightwatchjs.org/guide

module.exports = {
  'login test': browser => {
    browser
      .url(process.env.VUE_DEV_SERVER_URL + 'login')
      .waitForElementVisible('#app', 5000)
      .assert.attributeContains('.logo', 'src', '/static/images/logo.png')
      .end()
  }
}

// module.exports = {
//   'login test': browser => {
//     browser
//       .init()
//       .waitForElementVisible('#app')
//       .assert.elementPresent('.hello')
//       .assert.containsText('h1', 'Welcome to Your Vue.js App')
//       .assert.elementCount('img', 1)
//       .end()
//   },
//
//   'example e2e test using a custom command': browser => {
//     browser
//       .openHomepage()
//       .assert.elementPresent('.hello')
//       .end()
//   }
// }
