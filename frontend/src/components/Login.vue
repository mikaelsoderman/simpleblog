<template>
  <div class="container">

    <h2 class="form-signin-heading">Sign in to add new posts</h2>
    <b-form @submit="login">
      <b-form-group id="emailinputgroup"
                    label="Email:"
                    label-for="emailinput" label-sr-only>
        <b-form-input v-model="email" type="email" id="inputEmail" placeholder="Email address" required
               autofocus>
        </b-form-input>
      </b-form-group>
      <b-form-group id="passwordinputgroup"
                    label="Password:"
                    label-for="passwordinput" label-sr-only>
        <b-form-input v-model="pass" type="password" id="inputPassword" placeholder="Password" required>
        </b-form-input>
      </b-form-group>
      <b-button type="submit" variant="primary">Sign in</b-button>
      <p v-if="error" class="error">Bad login information</p>
      <b-alert variant="danger"
               dismissible
               :show="error"
               @dismissed="error=false">
        Login failed
      </b-alert>
    </b-form>

  </div> <!-- /container -->
</template>

<script>
import auth from '../auth'

export default {
  name: 'login',
  data () {
    return {
      email: 'user@test.com',
      pass: '',
      error: false,
      errors: [],
      response: []
    }
  },
  methods: {
    login () {
      auth.login(this.$http, this.email, this.pass, loggedIn => {
        if (!loggedIn) {
          this.error = true
        } else {
          localStorage.token = btoa(this.email + ':' + this.pass)
          this.$parent.token = localStorage.token
          this.$parent.username = localStorage.username
          //  this.$router.replace(this.$route.query.redirect || '/')
        }
      })
    }
  }
}
</script>

<style scoped>
  .error {
    color: red;
  }
</style>
