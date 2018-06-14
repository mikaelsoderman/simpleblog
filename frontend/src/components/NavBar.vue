<template>
<div>
  <b-navbar type="light" variant="light">
    <h5>Welcome! Log in below</h5>
    <b-navbar-nav class="ml-auto">

      <b-nav-form @submit="onSearch" >
        <b-form-input v-model="searchphrase" size="sm" class="mr-sm-2" type="text" placeholder="Search"/>
        <b-button variant="outline-success" class="my-2 my-sm-0" type="submit">Search</b-button>
      </b-nav-form>
    </b-navbar-nav>
  </b-navbar>
  <b-alert :show="dismissCountDown"
           dismissible
           variant="warning"
           @dismissed="dismissCountDown=0"
           @dismiss-count-down="countDownChanged">
    <p>{{ searcherror }}</p>
    <b-progress variant="warning"
                :max="dismissSecs"
                :value="dismissCountDown"
                height="4px">
    </b-progress>
  </b-alert>
</div>
</template>

<script>
export default {
  name: 'NavBar',
  data () {
    return {
      username: this.$parent.username,
      searchphrase: '',
      dismissCountDown: 0,
      dismissSecs: 5,
      searcherror: ''
    }
  },
  methods: {
    onSearch (evt) {
      evt.preventDefault()
      console.log('YAY')
      this.performSearch(this.searchphrase)
      this.searchphrase = ''
    },
    showSearchAlert (errorMessage) {
      this.searcherror = errorMessage
      this.dismissCountDown = this.dismissSecs
    },
    countDownChanged (dismissCountDown) {
      this.dismissCountDown = dismissCountDown
    },
    performSearch (query) {
      this.searchquery = query
      this.$http.get('http://localhost:8080/api/admin/posts/search', {params: {query: query}}).then(response => {
        this.$parent.searchresult = response.body
        if (typeof this.$parent.searchresult === 'undefined' || this.$parent.searchresult.length === 0) {
          this.showSearchAlert('No hits found')
        }
      }, response => {
        this.showSearchAlert('Server error during search')
        console.log('ERROR')
        console.log(response)
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
