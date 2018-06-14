/* globals localStorage */

export default {
  login (http, email, pass, cb) {
    cb = arguments[arguments.length - 1]
    if (localStorage.token) {
      // eslint-disable-next-line
      if (cb) cb(true)
      this.onChange(true)
      return
    }

    loginRequest(http, email, pass, (res) => {
      if (res.authenticated) {
        localStorage.token = res.token
        localStorage.username = res.username
        localStorage.user = JSON.stringify(res.user)
        // eslint-disable-next-line
        if (cb) cb(true)
        this.onChange(true)
      } else {
        localStorage.user = null
        // eslint-disable-next-line
        if (cb) cb(false)
        this.onChange(false)
      }
    })
  },

  getToken () {
    return localStorage.token
  },

  logout (cb) {
    delete localStorage.token
    delete localStorage.user
    delete localStorage.username
    // eslint-disable-next-line
    if (cb) cb()
    this.onChange(false)
  },

  loggedInUser () {
    console.dir(localStorage.user)
    return localStorage.user
  },

  loggedIn () {
    return !!localStorage.token
  },

  onChange () {}
}

function loginRequest (http, email, pass, cb) {
  http.post('http://localhost:8080/api/admin/users/login', {email: email, password: pass}).then(response => {
    // eslint-disable-next-line
    cb({
      authenticated: true,
      user: response.body,
      username: response.body.name
    })
  }, response => {
    // eslint-disable-next-line
    cb({
      authenticated: false,
      user: null,
      username: null
    })
  })
}
