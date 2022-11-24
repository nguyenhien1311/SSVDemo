<template>
    <div class="container">
      <form @submit.prevent="register">
        <h2 class="mb-3">Register account</h2>
        <div class="input">
          <label for="username">User name</label>
          <input
            class="form-control"
            type="text"
            name="username"
          />
        </div>
        <div class="input">
          <label for="password">Password</label>
          <input
            class="form-control"
            type="password"
            name="password"
          />
        </div>
        <div class="input">
          <label for="password">Password</label>
          <input
            class="form-control"
            type="password"
            name="password"
          />
        </div>
        <button type="submit" class="mt-4 btn-pers" id="login_button">
          Confirm
        </button>
      </form>
    </div>
  </template>

<script>
import axios from 'axios'
const url = 'http://localhost:8080/api/v1/'
import { mapState } from 'vuex';

export default {
  data() {
    return {
      username: "",
      password: ""
    };
  },
  computed:mapState({

  }), 
  methods: {
   async  register(submitEvent) {
      this.username = submitEvent.target.elements.username.value;
      this.password = submitEvent.target.elements.password.value;
      const response = await axios.post(url+'signin', { 'username': this.username, 'password': this.password }, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
      const data = await response.data.body
      this.$store.commit('setUser',data)
      console.log(this.$store.state.user)
      this.$router.push("/login");
    },
  },
};
</script>