// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueResource from 'vue-resource'
import App from './App'
import router from './router'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import moment from 'moment'

// Import the individual components
import bCard from 'bootstrap-vue/es/components/card/card'
import bCardHeader from 'bootstrap-vue/es/components/card/card-header'
import bCardBody from 'bootstrap-vue/es/components/card/card-body'
import bCardFooter from 'bootstrap-vue/es/components/card/card-footer'
import bCardImg from 'bootstrap-vue/es/components/card/card-img'
import bTable from 'bootstrap-vue/es/components/table/table'
import bContainer from 'bootstrap-vue/es/components/layout/container'
import bRow from 'bootstrap-vue/es/components/layout/row'
import bCol from 'bootstrap-vue/es/components/layout/col'
import bButton from 'bootstrap-vue/es/components/button/button'

Vue.component('blog-post', {
  props: ['title', 'imageUrl', 'date', 'id', 'loggedIn', 'body'],
  template: '<b-card :title="title"\n' +
  '          :img-src="imageUrl"\n' +
  '          img-alt="Image"\n' +
  '          img-top\n' +
  '          tag="article"\n' +
  '          style="max-width: 40rem;"\n' +
  '          class="mb-2">\n' +
  '    <h6 class="card-subtitle mb-2 text-muted">{{ date | blogFormatDate }}</h6>\n' +
  '    <p class="card-text">\n' +
  '      {{ body }}\n' +
  '    </p>\n' +
  '    <b-button v-if="loggedIn" v-on:click="$emit(\'edititem\', id)" variant="primary">Edit</b-button>\n' +
  '    <b-button v-if="loggedIn" v-on:click="$emit(\'deleteitem\', id)" variant="danger">Delete</b-button>\n' +
  '  </b-card>'
})

Vue.use(BootstrapVue)
// Add components globally:
Vue.component('b-card', bCard)
Vue.component('b-card-header', bCardHeader)
Vue.component('b-card-body', bCardBody)
Vue.component('b-card-footer', bCardFooter)
Vue.component('b-card-img', bCardImg)
Vue.component('b-table', bTable)
Vue.component('b-container', bContainer)
Vue.component('b-row', bRow)
Vue.component('b-col', bCol)
Vue.component('b-button', bButton)

Vue.use(VueResource)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})

Vue.http.interceptors.push(function (request) {
  var token = 'Basic ' + localStorage.token
  request.headers.set('Authorization', token)
})

Vue.filter('blogFormatDate', function (value) {
  if (value) {
    return moment(String(value)).format('YYYY-MM-DD HH:mm')
  }
})
