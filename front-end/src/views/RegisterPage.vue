<template>
  <div class="container public">
    <div class="row justify-content-center">
      <div class="form">
        <Logo/>
        <form @submit.prevent="submitForm">
          <div v-show="errorMessage" class="alert alert-danger failed">{{errorMessage}}</div>
          <div class="form-group">
            <label for="username">Username</label>
            <input type="text" class="form-control" id="username" v-model="form.username">
            <div class="filed-error" v-if="$v.form.username.$dirty">
              <div class="error" v-if="!$v.form.username.required">Username is required</div>
              <div class="error" v-if="!$v.form.username.alphaNum">Username can only contain letters and numbers</div>
              <div class="error" v-if="!$v.form.username.maxLength">Username must have at least {{$v.form.username.$params.minLength.min}} letters</div>
            </div>
          </div>
          <div class="form-group">
            <label for="emailAddress">Email Address</label>
            <input type="text" class="form-control" id="emailAddress" v-model="form.emailAddress">
            <div class="filed-error" v-if="$v.form.emailAddress.$dirty">
              <div class="error" v-if="!$v.form.emailAddress.required">Email Address is required</div>
              <div class="error" v-if="!$v.form.emailAddress.email">This is not a valid email address</div>
              <div class="error" v-if="!$v.form.emailAddress.maxLength">Email Address is too long. It can contains maximum {{$v.form.emailAddress.$params.minLength.min}} letters</div>
            </div>
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input type="text" class="form-control" id="password" v-model="form.password">
            <div class="field-error" v-if="$v.form.password.$dirty">
              <div class="error" v-if="!$v.form.password.required">Password is required</div>
              <div class="error" v-if="!$v.form.password.minLength">Password is too short. It can contains at least {{$v.form.password.$params.minLength.min}} letters.</div>
              <div class="error" v-if="!$v.form.password.maxLength">Password is too long. It can contains maximium {{$v.form.password.$params.maxLength.max}} letters.</div>
            </div>
          </div>
          <button type="submit" class="btn btn-primary btn-block">Create account</button>
          <p class="accept-terms text-muted">By clicking "Create account", you agree to our <a href="#">terms of service</a> and <a href="#">privacy policy</a></p>
          <p class="text-center text-muted">Already have an account? <a href="/login">Sign in</a></p>
        </form>
      </div>
    </div>
    <PageFooter/>
  </div>
</template>

<script>
import registrationService from '@/services/registration'
import { required, email, minLength, maxLength, alphaNum } from 'vuelidate/lib/validators'
import Logo from '../components/Logo'
import PageFooter from '../components/PageFooter'

export default {
  name: 'RegisterPage',
  components: { PageFooter, Logo },
  data: function () {
    return {
      form: {
        username: '',
        emailAddress: '',
        password: ''

      },
      errorMessage: ''
    }
  },
  validations: {
    form: {
      username: {
        required,
        minLength: minLength(2),
        maxLength: maxLength(50),
        alphaNum
      },
      emailAddress: {
        required,
        email,
        maxLength: maxLength(100)
      },
      password: {
        required,
        minLength: minLength(6),
        maxLength: maxLength(30)
      }
    }
  },
  methods: {
    submitForm (e) {
      this.$v.$touch()
      if (this.$v.$invalid) {
        return
      }
      registrationService.register(this.form).then(() => {
        this.$router.push({ name: 'login' })
      }).catch((error) => {
        this.errorMessage = 'Failed to register user. Reason: ' + (error.message ? error.message : 'Unknown') + '.'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .accept-terms {
    margin: 20px 0 40px 0;
  }
</style>
