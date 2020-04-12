const path = require('path')
const del = require('del')
const Chance = require('chance')
const rw = require('rw')

const user = {}

module.exports = {
  before: function () {
    const userDataPath = path.resolve('tests/e2e/data/user.js')
    del.sync([userDataPath])
    // Generating user data
    const chance = new Chance()
    const name = chance.name().split(' ')
    user.firstName = name[0]
    user.lastName = name[1]
    user.username = name[0].toLowerCase() + chance.integer({ min: 0, max: 1000000 })
    user.emailAddress = user.username + '@e2e.taskagile.com'
    user.password = 'MyPassword!'

    let fileContent = '// This file is auto generated by ' + path.basename(__filename) + '\n'
    fileContent += '/* eslint-disable */\n'
    fileContent += 'module.exports = ' + JSON.stringify(user)
    fileContent += '\n/* eslint-enable */\n'
    rw.writeFileSync(userDataPath, fileContent, 'utf8')
  },
  'register page renders elements': function (browser) {
    const registerPage = browser.page.RegisterPage()

    registerPage
      .navigate()
      .waitForElementVisible('@app', 500)
      .assert.visible('@usernameInput')
      .assert.visible('@emailAddressInput')
      .assert.visible('@passwordInput')
      .assert.visible('@firstNameInput')
      .assert.visible('@lastNameInput')
      .assert.visible('@submitButton')
      .assert.hidden('@formError')

    browser.end()
  },
  'register with invalid data': function (browser) {
    const registerPage = browser.page.RegisterPage()

    registerPage
      .navigate()
      .register('', '', '')

    // This assertion is just to make sure the page doesn't
    // redirect to login page. It would be better to assertion
    // the  validation error for each field indiviually.
    browser
      .assert.urlEquals(browser.launchUrl + 'register')
      .end()
  },
  'register with valid data': function (browser) {
    const registerPage = browser.page.RegisterPage()

    registerPage
      .navigate()
      .register(user.username, user.emailAddress, user.firstName, user.lastName, user.password)

    browser.pause(2000)

    browser
      .assert.urlEquals(browser.launchUrl + 'login')
      .end()
  }
}
