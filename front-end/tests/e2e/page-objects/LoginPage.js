module.exports = {
  url: function () {
    return this.api.launchUrl + 'login'
  },
  elements: {
    app: {
      selector: '#app'
    },
    logoImage: {
      selector: 'img.logo'
    },
    usernameInput: {
      selector: '#username'
    },
    passwordInput: {
      selector: '#password'
    },
    submitButton: {
      selector: 'button[type=submit]'
    },
    formError: {
      selector: '.failed'
    }
  },
  commands: [{
    login: function (username, password) {
      this
        .setValue('@usernameInput', username)
        .setValue('@passwordInput', password)
        .click('@submitButton')
    }
  }]
}
