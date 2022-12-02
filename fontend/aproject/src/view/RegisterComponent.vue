<template>
  <div class="container " style="width: 400px;">
    <form @submit.prevent="register">
      <table class="table table-borderless">
        <thead>
          <th colspan="2">
            <h2 class="mb-3">Register account</h2>
          </th>
        </thead>
        <tbody>
          <tr>
            <th scope="row"> <label for="username">Full name</label></th>
            <td>
              <input class="form-control" type="text" name="username" v-model="state.fullname" />
              <span v-if="v$.fullname.$error" style="color: red;">{{ v$.fullname.$errors[0].$message }}</span>
            </td>
          </tr>
          <tr>
            <th scope="row"> <label for="username">Email</label></th>
            <td>
              <input class="form-control" type="text" name="username" v-model="state.email" />
              <span v-if="v$.email.$error" style="color: red;">{{ v$.email.$errors[0].$message }}</span>
            </td>
          </tr>
          <tr>
            <th scope="row"> <label for="username">Phone</label></th>
            <td>
              <input class="form-control" type="text" name="username" v-model="state.phone" />
              <span v-if="v$.phone.$error" style="color: red;">{{ v$.phone.$errors[0].$message }}</span>
            </td>
          </tr>
          <tr>
            <th scope="row"> <label for="username">User name</label></th>
            <td>
              <input class="form-control" type="text" name="username" v-model="state.username" />
              <span v-if="v$.username.$error" style="color: red;">{{ v$.username.$errors[0].$message }}</span>
            </td>
          </tr>
          <tr>
            <th scope="row"><label for="password">Password</label></th>
            <td>
              <input class="form-control" type="password" name="password" v-model="state.password" />
              <span v-if="v$.password.$error" style="color: red;">{{ v$.password.$errors[0].$message }}</span>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="alternative-option mt-4" style="margin-bottom: 30px;">
          You already have an account? <a @click="moveToLogin()" style="cursor: pointer; color: blue;">Back to login</a>
        </div>
        <button type="submit" class="btn btn-success" id="login_button">
          Register
        </button>
    </form>
  </div>
</template>

<script>
import axios from 'axios'
const url = 'http://localhost:8080/api/v1/'
import { mapState } from 'vuex';
import { reactive, computed } from 'vue'
import useVuelidate from '@vuelidate/core'
import { required ,email} from '@vuelidate/validators'

export default {
  setup() {
    const state = reactive({
      fullname: "",
      email: "",
      phone : "",
      username: "",
      password: ""
    })



    const rules = computed(() => {
      return {
        fullname: { required },
        email: { required ,email},
        phone: { required },
        username: { required },
        password: { required }
      }
    })

    const v$ = useVuelidate(rules, state)

    return {
      state,
      v$
    }
  },
  data() {
    return {
    };
  },
  computed: mapState({

  }),
  methods: {
    async register() {
      this.v$.$validate();
      if (!this.v$.$error) {


        var data = {
          'name': this.state.fullname,
          'email': this.state.email,
          'phone': this.state.phone,
          'username': this.state.username, 
          'password': this.state.password
        }
        const response = await axios.post(url + 'signin', data , {
          headers: {
            'Content-Type': 'application/json'
          }
        })
        const result = await response.data
        if (result.message != undefined || result.message != null) {
                alert(result.message)
            }
        
        this.$router.push("/login");
      }
    },
    moveToLogin(){
      this.$router.push('/login')
    }
  },
};
</script>