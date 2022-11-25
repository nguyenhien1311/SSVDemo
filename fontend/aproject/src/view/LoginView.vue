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
        <!-- <div class="alternative-option mt-4">
          You don't have an account? <button @click="moveToRegister">Register</button>
        </div> -->
        <button type="submit" class="mt-4 btn-pers" id="login_button">
          Login
        </button>
      </form>
    </div>
  </template>

<script>
import axios from 'axios'
const url = 'http://localhost:8080/api/v1/'
import { mapState } from 'vuex';

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
  computed:mapState({

  }),
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
    // moveToRegister() {
    //   this.$router.push("/register");
    // },
    handleData(data) {  
            if (data.msg != undefined) {
                alert(data.msg)
            }else if (data == undefined) {
                alert(data)
            }
           
      this.$store.commit('setUser',data.body)
      this.$router.push("/")
    }
  },
};
</script>