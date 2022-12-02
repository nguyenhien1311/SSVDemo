<template>
    <div class="container"  style="width: 400px;">
      <form @submit.prevent="login">
        <table class="table table-borderless" >
          <thead>
            <th colspan="2"> <h2 class="mb-3">Login</h2></th>
          </thead>
                                    <tbody>
                                        <tr>
                                            <th scope="row"> <label for="username">User name</label></th>
                                            <td>
                                              <input
                                                class="form-control"
                                                type="text"
                                                name="username"
                                                v-model="state.username"
                                              />
                                              <span v-if="v$.username.$error" style="color: red;">{{v$.username.$errors[0].$message}}</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"><label for="password">Password</label></th>
                                            <td>
                                              <input
                                                class="form-control"
                                                type="password"
                                                name="password"
                                                v-model="state.password"
                                              />
                                              <span v-if="v$.password.$error" style="color: red;">{{v$.password.$errors[0].$message}}</span>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>     
        <div  style="margin-bottom: 30px;">
          You don't have an account? <a @click="moveToRegister" style="cursor: pointer; color: blue;">Register</a>
        </div>
        <button type="submit" class="btn btn-success" id="login_button">
          Login
        </button>
      </form>
    </div>
  </template>

<script>
import axios from 'axios'
const url = 'http://localhost:8080/api/v1/'
import { reactive,computed } from 'vue'
import useVuelidate from '@vuelidate/core'
import {required} from '@vuelidate/validators'

export default {
  setup(){
        const state  = reactive({
          username: "",
          password: ""
        })
        
        const rules = computed(()=>{
            return {
            username:{required},
            password:{required}
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
  methods: {
     login(submitEvent) {
      this.v$.$validate();
            if(!this.v$.$error){
      this.state.username = submitEvent.target.elements.username.value;
      this.state.password = submitEvent.target.elements.password.value;
       axios.post(url+'login', { 'username': this.state.username, 'password': this.state.password }, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(response => (this.handleData(response.data)))
                  }
    },
    moveToRegister() {
      this.$router.push("/register");
    },
    handleData(data) {  
            if (data.msg != undefined) {
                alert(data.msg)
            }else if (data == undefined) {
                alert(data)
            }
      this.$store.commit('setUser',data.body)
      if(this.$store.state.authorities == 'ROLE_USER'){
        this.$router.push("/student/")
      }else{
        this.$router.push("/")
      }
    }
  },
};
</script>