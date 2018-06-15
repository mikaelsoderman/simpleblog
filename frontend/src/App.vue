<template>
  <div id="app">

    <b-container>
      <b-row>
        <b-col>
          <h1>Picture Blog</h1>
        </b-col>
        <b-navbar v-if="loggedIn" type="light" variant="faded">
          <h5 id="welcome-msg">Welcome {{ username }}! </h5>
            <router-link v-if="loggedIn" to="/logout">
                <b-button variant="danger">Log out</b-button>
            </router-link>
        </b-navbar>
      </b-row>
      <b-row id="main-content">
        <b-col v-if="this.searchresult.length > 0">
          <b-navbar type="light" variant="light">
            <h5>Search result for query {{ this.searchquery }}</h5>
            <b-navbar-nav class="ml-auto">
              <b-button variant="danger" @click="loadPosts()" size="sm" class="my-2 my-sm-0" type="submit">Close</b-button>
            </b-navbar-nav>
          </b-navbar>
          <b-list-group>
            <b-list-group-item v-for="(searchpost, index) in this.searchresult" :key="searchpost.id">
              <h3>#{{ index + 1 }}</h3>
              <blog-post v-bind:title="searchpost.title"
                         v-bind:imageUrl="searchpost.imageUrl"
                         v-bind:date="searchpost.date"
                         v-bind:body="searchpost.body"
                         v-bind:id="searchpost.id"
                         v-bind:loggedIn="loggedIn"
                         v-on:deleteitem="deleteItem"
                         v-on:edititem="editItem"></blog-post>
            </b-list-group-item>
          </b-list-group>

        </b-col>
        <b-col v-else-if="posts.length > 0">
          <blog-post v-bind:title="posts[currentItem].title"
                     v-bind:imageUrl="posts[currentItem].imageUrl"
                     v-bind:date="posts[currentItem].date"
                     v-bind:body="posts[currentItem].body"
                     v-bind:id="posts[currentItem].id"
                     v-bind:loggedIn="loggedIn"
                     v-on:deleteitem="deleteItem"
                     v-on:edititem="editItem"></blog-post>
        </b-col>
        <b-col>
          <NavBar></NavBar>

          <b-form v-if="loggedIn" @submit.prevent="savePost" @reset="resetPostEdit">
            <b-form-group id="dateinputgroup" label="Date:" label-for="dateinput">
                <b-form-input ref="date" id="dateinput" type="text" v-model="editedpost.date" required placeholder="Enter date and time">
                  </b-form-input>
              </b-form-group>
            <b-form-group id="titleInputGroup" label="Title:" label-for="titleInput">
                <b-form-input ref="title" id="titleInput" type="text" v-model="editedpost.title" required placeholder="Enter post title">
                  </b-form-input>
              </b-form-group>
            <b-form-group id="bodyInputGroup" label="Body text:" label-for="bodyInput">
                <b-form-textarea ref="body" id="textarea1" v-model="editedpost.body" placeholder="Enter something" :rows="3" :max-rows="6">
                  </b-form-textarea>
              </b-form-group>
            <b-form-group id="imageInputGroup" label="URL to image:" label-for="imageInput">
                <b-form-input ref="image" id="imageInput" type="url" v-model="editedpost.imageUrl" required placeholder="Enter URL to an image">
                  </b-form-input>
              </b-form-group>
            <b-button type="submit" variant="primary">Submit</b-button>
            <b-button type="reset" variant="danger">Reset</b-button>
          </b-form>
          <b-alert variant="danger" dismissible :show="showAlert" @dismissed="showAlert=false"> {{ alertmsg }}</b-alert>
          <login v-if="!loggedIn"></login>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import auth from './auth'
import moment from 'moment'
import Login from './components/Login'
import NavBar from './components/NavBar'

export default {
  components: {Login, NavBar},
  data () {
    return {
      currentItem: 0,
      loggedIn: auth.loggedIn(),
      username: this.$root.username,
      loggedInUser: auth.loggedInUser(),
      token: this.$root.token,
      showAlert: false,
      alertmsg: '',
      posts: [
      ],
      searchresult: [
      ],
      searchquery: '',
      editedpost: {
        id: '',
        date: '',
        title: '',
        body: '',
        imageUrl: ''
      }
    }
  },
  created () {
    this.token = localStorage.token
    this.username = localStorage.username
    auth.onChange = loggedIn => {
      this.loggedIn = loggedIn
    }
  },
  methods: {
    nextItem () {
      if ((event.keyCode === 37 || event.keyCode === 38) && this.currentItem > 0) {
        this.currentItem--
      } else if ((event.keyCode === 39 || event.keyCode === 40) && this.currentItem < this.posts.length - 1) {
        this.currentItem++
      }
    },
    updatePosts (newList) {
      this.posts = newList
    },
    updateSearchResult (newList) {
      this.searchresult = newList
    },
    loadPosts (postid) {
      this.$http.get('api/admin/posts/').then(response => {
        // get body data
        this.updatePosts(response.body)
        this.updateSearchResult('')
        this.searchquery = ''
        if (postid) {
          this.currentItem = getPostIndex(postid, response.body)
        }
      }, response => {
        this.showAlert = true
        this.alertmsg = 'Lost connection to server'
      })
    },
    editItem (idToEdit) {
      var itemindex = getPostIndex(idToEdit, this.posts)
      this.editedpost.date = this.posts[itemindex].date
      this.editedpost.title = this.posts[itemindex].title
      this.editedpost.body = this.posts[itemindex].body
      this.editedpost.imageUrl = this.posts[itemindex].imageUrl
      this.editedpost.id = this.posts[itemindex].id
    },
    deleteItem (idToDelete) {
      this.$http.delete('api/admin/posts/' + idToDelete, {headers: {'Authorization': 'Basic ' + this.$parent.token}}).then(response => {
        this.posts = this.removePostFromList(idToDelete, this.posts)
        this.searchresult = this.removePostFromList(idToDelete, this.searchresult)

        if (this.currentItem >= this.posts.length - 1) {
          this.currentItem--
        }
      }, response => {
        this.showAlert = true
        this.alertmsg = 'Failed to delete. Lost connection to server'
      })
    },
    removePostFromList (idToRemove, theList) {
      var posts = []

      var post
      for (post of theList) {
        if (post.id !== idToRemove) {
          posts.push(post)
        }
      }
      return posts
    },
    savePost () {
      var blogDate = moment(this.editedpost.date)
      var newpost = {
        id: this.editedpost.id,
        date: blogDate.format('YYYY-MM-DD HH:mm'),
        title: this.editedpost.title,
        body: this.editedpost.body,
        imageUrl: this.editedpost.imageUrl,
        author: JSON.parse(this.loggedInUser)
      }

      this.$http.post('api/admin/posts/', newpost).then(response => {
        this.showAlert = false
        this.editedpost.date = ''
        this.editedpost.title = ''
        this.editedpost.body = ''
        this.editedpost.imageUrl = ''
        this.editedpost.id = ''
        this.loadPosts(response.body.id)
      }, response => {
        this.showAlert = true
        this.alertmsg = 'Failed to save blog'
      })
    },
    resetPostEdit () {
      this.editedpost.date = ''
      this.editedpost.title = ''
      this.editedpost.body = ''
      this.editedpost.imageUrl = ''
      this.editedpost.id = ''
    }
  },
  mounted () {
    document.addEventListener('keyup', this.nextItem)
    this.loadPosts()
  }
}

var getPostIndex = function (postId, list) {
  var idx = 0
  list.forEach(function (post, index) {
    if (post.id === postId) {
      idx = index
    }
  })
  return idx
}

</script>
<style>
  body{
    background:url('https://picsum.photos/1600/1200/?rand') no-repeat center center;
    min-height:100%;
    background-size:cover;
  }

  div.row {
    background-color: rgba(255, 255, 255, 0.92);
    padding: 25px 30px 5px 30px;
  }

  div#main-content {
    background-color: rgba(255, 255, 255, 0.92);
    padding-bottom: 40px;
    padding-top: 5px;
  }

  #welcome-msg {
    margin-right: 30px;
  }
</style>
